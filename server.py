"""server.py: Creates an instance of a simple FTP server. Handles multiple clients at a time."""

import _thread
import socket
import re
from utils import *
from config import *
from user import *

class Server:

    # Used in sending and receiving data
    buffer_size = 1024

    # Some regular expressions used to parse commands. There used to be more here but they got complicated, and I got
    # frustrated...
    command_port_arg_ip_pattern = re.compile('\d+,\d+,\d+,\d+')

    # All our templates for reply code responses
    resp = CONFIG['replyCodeResponses']

    def __init__(self, ip_num, port_num):

        # IP and port the server will be available at
        self.port = port_num
        self.ip = ip_num

        # A socket used to establish control lines with clients.
        # over the place. Data lines are opened and closed within their command handlers. We do it this way because
        # we're only ever in active mode and not many commands we're implementing here transfer data anyway.
        self.sock_control = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.sock_control.bind((self.ip, self.port))
        self.sock_control.listen(1)

        # State of all currently connected client. Indexed by an incrementing session ID number.
        self.users = {}

        # This is a dictionary lookup for commands and their function pointers to handle them. Note it includes every
        # known FTP command, even the ones we haven't implemented, because we want to able to serve the correct response
        # when a valid unimplemented command has been made.
        self.commands_to_service = {
            "abor": self.handle_unimplemented_command,
            "acct": self.handle_unimplemented_command,
            "adat": self.handle_unimplemented_command,
            "allo": self.handle_unimplemented_command,
            "appe": self.handle_unimplemented_command,
            "auth": self.handle_unimplemented_command,
            "bye": self.run_quit_command,
            "ccc": self.handle_unimplemented_command,
            "cd": self.run_cwd_command,
            "cdup": self.handle_unimplemented_command,
            "conf": self.handle_unimplemented_command,
            "cwd": self.run_cwd_command,
            "dele": self.run_delete_command,
            "delete": self.run_delete_command,
            "enc": self.handle_unimplemented_command,
            "eprt": self.handle_unimplemented_command,
            "epsv": self.handle_unimplemented_command,
            "feat": self.handle_unimplemented_command,
            "get": self.run_get_command,
            "help": self.handle_unimplemented_command,
            "lang": self.handle_unimplemented_command,
            "list": self.handle_unimplemented_command,
            "lprt": self.handle_unimplemented_command,
            "lpsv": self.handle_unimplemented_command,
            "mdtm": self.handle_unimplemented_command,
            "mic": self.handle_unimplemented_command,
            "mkd": self.run_mkdir_cmd,
            "mkdir": self.run_mkdir_cmd,
            "mlsd": self.handle_unimplemented_command,
            "mlst": self.handle_unimplemented_command,
            "mode": self.handle_unimplemented_command,
            "nlst": self.handle_unimplemented_command,
            "noop": self.handle_unimplemented_command,
            "opts": self.handle_unimplemented_command,
            "pass": self.run_password_cmd,
            "password": self.run_password_cmd,
            "pasv": self.handle_unimplemented_command,
            "pbsz": self.handle_unimplemented_command,
            "port": self.run_port_cmd,
            "prot": self.handle_unimplemented_command,
            "put": self.run_put_command,
            "pwd": self.run_pwd_command,
            "quit": self.run_quit_command,
            "rein": self.handle_unimplemented_command,
            "rest": self.handle_unimplemented_command,
            "retr": self.run_get_command,
            "rmd": self.handle_unimplemented_command,
            "rnfr": self.handle_unimplemented_command,
            "rnto": self.handle_unimplemented_command,
            "site": self.handle_unimplemented_command,
            "size": self.handle_unimplemented_command,
            "smnt": self.handle_unimplemented_command,
            "stat": self.handle_unimplemented_command,
            "stor": self.run_put_command,
            "stou": self.handle_unimplemented_command,
            "stru": self.handle_unimplemented_command,
            "syst": self.handle_unimplemented_command,
            "type": self.handle_unimplemented_command,
            "user": self.run_user_cmd,
            "username": self.run_user_cmd,
            "xcup": self.handle_unimplemented_command,
            "xmkd": self.run_mkdir_cmd,
            "xpwd": self.run_pwd_command,
            "xrcp": self.handle_unimplemented_command,
            "xrmd": self.handle_unimplemented_command,
            "xrsq": self.handle_unimplemented_command,
            "xsem": self.handle_unimplemented_command
        }

        log("Serving at %s:%s" % (ip_num, port_num))

    def listen(self):
        session_id = -1

        # Get a control line opened and wait for someone to bite
        log("Listening...")
        while True:
            # Wait for someone to bite
            incoming_conn_control, incoming_address = self.sock_control.accept()
            session_id += 1

            # Start off a session for this new client and continue listening
            thread_id = _thread.start_new_thread(self.start_session, (session_id, incoming_conn_control, incoming_address))
            log("Client connected. Starting new session. Thread ID: %d, Session ID: %s" % (thread_id, session_id))
            log("Listening for more connections...")

    def start_session(self, sid, conn_control, address):
        self.users[sid] = User(conn_control, address)
        log("(SID: %d, User: %s) Client connected at address %s:%s"
            % (sid, self.users[sid].username, self.users[sid].ip, self.users[sid].port))

        self.reply(sid, self.resp['220'])

        # The meat of it all. We'll loop, theoretically forever, accepting commands on the control line.
        while True:
            try:
                if not self.users[sid]:
                    break

                data = self.users[sid].conn_control.recv(self.buffer_size)

                # Client likely disconnected. Let's stop listening for commands then.
                if not data:
                    break

                data_readable = data.decode('ascii')
                log("(SID: %d, User: %s) Received: %s" % (sid, self.users[sid].username, data_readable))

                (command, argument) = self.parse(strip_lines(data.decode('ascii')))
                command_key = command.lower()

                self.commands_to_service[command_key](sid, argument)

            except ConnectionResetError:
                log("(SID: %d, User: %s) Client closed the connection." % (sid, self.users[sid].username))
                self.users[sid] = None
                break

            except CommandUnrecognizedError:
                self.reply(sid, self.resp['500'])
            except ParamsSyntaxError:
                self.reply(sid, self.resp['501'])
            except NotLoggedInError as reason:
                self.users[sid].reset_creds()
                self.reply(sid, self.resp['530'] % reason)
            except FileStoragePermissionError:
                self.reply(sid, self.resp['532'])
            except IOError as reason:
                self.reply(sid, self.resp['550'] % reason)
            except socket.error:
                self.reply(sid, self.resp['425'])
            except OverflowError as reason:
                self.reply(sid, self.resp['425'] +
                           "You gave me a bad port number. Needs to be 0-65535. Also, check your firewalls.")
            except OSError:
                self.reply(sid, self.resp['500'])

    def parse(self, command):
        """Used to make sense of commands and return a tuple containing the command name and argument"""

        # The 1st space divides the command name and argument. I love simple protocols.
        result = command.split(" ", 1)
        name_result = result[0].lower()

        if name_result is None or name_result not in self.commands_to_service:
            # We were just given a command we've literally never heard of... Or garbage. Most likely pure garbage.
            raise CommandUnrecognizedError

        if len(result) > 1:
            # Anything after that first space?
            arg_result = result[1]
            if arg_result is None:
                # This shouldn't happen at this point, but hey, you never know
                raise ParamsSyntaxError
        else:
            arg_result = ''

        return name_result, arg_result.strip()

    def assert_user_logged_in(self, sid):
        """Used to guard most priviledged commands."""
        if not self.users[sid].is_valid:
            raise NotLoggedInError("Unauthorized.")

    def assert_user_can_store(self, sid):
        """Used to guard storage based commands (e.g. put)."""
        if not self.users[sid].is_valid:
            raise FileStoragePermissionError("Unauthorized.")

    def reply(self, sid, response):
        """Uses the currently open control line to send a message to the client"""
        log("(SID: %d, User: %s) Replying to client: %s" % (sid, self.users[sid].username, response))
        self.users[sid].conn_control.send(bytes(response, 'ascii'))

    def run_user_cmd(self, sid, username):
        self.users[sid].username = username
        self.reply(sid, self.resp['331'])

    def run_password_cmd(self, sid, password):
        self.users[sid].password = password
        if self.users[sid].is_valid():
            self.reply(sid, self.resp['230'] % self.users[sid].username)
        else:
            raise NotLoggedInError("Login incorrect.")

    def run_port_cmd(self, sid, location):
        ip_result = self.command_port_arg_ip_pattern.match(location)
        ip = ip_result.group(0).replace(',', '.')

        # Port is returned in two decimals representing a two parts of a hexadecimal. We have to parse that and get a
        # sensible port number

        port_result = location.replace(ip_result.group(0) + ',', '')
        port_hex = tuple(map(lambda dec: int(dec, 0), port_result.split(',')))
        port = hex_concat(port_hex[0], port_hex[1])

        # port_result = location.replace(ip_result.group(0) + ',', '').split(',')
        # port = (port_result[0] * 256) + port_result[1]

        self.users[sid].ip = ip
        self.users[sid].data_port = port

        self.reply(sid, self.resp['200'] % 'Got your port number.')

    def run_mkdir_cmd(self, sid, dir_path):
        self.assert_user_logged_in(sid)
        dir_resolved = self.users[sid].get_abs_path_from_root(dir_path)
        if not folder_exists(dir_resolved):
            create_dir(dir_resolved)
            self.reply(sid, self.resp['257'] % 'Created "%s"' % self.users[sid].get_rel_path(dir_path))
        else:
            raise IOError('Directory already exists')

    def run_cwd_command(self, sid, dir_path):
        self.assert_user_logged_in(sid)

        if folder_exists(self.users[sid].get_abs_path_from_root(dir_path)):
            self.users[sid].change_dir(dir_path)
            log("(SID: %d, User: %s) Client changed working directory to: %s" % (sid, self.users[sid].username, self.users[sid].get_rel_cwd()))
            self.reply(sid, self.resp['257'] % "CWD command successful")
        else:
            raise IOError('Directory does not exist')

    def run_pwd_command(self, sid, arg_empty):
        self.assert_user_logged_in(sid)
        self.reply(sid, self.resp['200'] % '"%s" is the current working directory' % self.users[sid].get_rel_cwd())

    def run_get_command(self, sid, file_name_path):
        self.assert_user_logged_in(sid)
        resolved_file_path = self.users[sid].get_abs_path_from_root(file_name_path)

        if not file_exists(resolved_file_path):
            raise IOError('File does not exist')

        file_handle = open(resolved_file_path, 'rb')

        data = file_handle.read(self.buffer_size)

        data_sending_sock = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
        data_sending_sock.connect((self.users[sid].ip, self.users[sid].data_port))

        self.reply(sid, self.resp['150'] % 'retrieval of file: %s' % file_name_path)

        while data:
            data_sending_sock.send(data)
            data = file_handle.read(self.buffer_size)
        file_handle.close()
        data_sending_sock.close()

        self.reply(sid, self.resp['226'] % 'Transfer completed for file: %s' % get_filename(file_name_path))

    def run_put_command(self, sid, file_name_path):
        self.assert_user_can_store(sid)

        file_name = get_filename(file_name_path)
        display_file_path = self.users[sid].get_rel_path(file_name_path)
        resolved_file_path = self.users[sid].get_abs_path(display_file_path)


        data_receiving_sock = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
        data_receiving_sock.connect((self.users[sid].ip, self.users[sid].data_port))

        self.reply(sid, self.resp['150'] % 'local file: %s' % file_name)

        file_handle = open(resolved_file_path, 'wb')
        while True:
            packet = data_receiving_sock.recv(self.buffer_size)
            if not packet:
                file_handle.close()
                break
            else:
                file_handle.write(packet)
        data_receiving_sock.close()

        self.reply(sid, self.resp['226'] % 'Transfer completed for %s. Remote path: %s'
                   % (get_filename(file_name), display_file_path))

    def run_delete_command(self, sid, file_path):
        self.assert_user_logged_in(sid)
        delete_file(self.users[sid].get_abs_path_from_root(file_path))
        self.reply(sid, self.resp['200'] % 'File successfully deleted.')

    def run_quit_command(self, sid, arg_empty):
        self.reply(sid, self.resp['221'])
        self.users[sid].conn_control.close()
        self.users[sid] = None

    def handle_unimplemented_command(self, sid, command_name):
        self.reply(sid, self.resp['202'])


# A couple of exceptions down here mainly for handling situations requiring a 5xx response. This helps make handling
# these weird situations easier from inside each block of command logic.

class CommandUnrecognizedError(SyntaxError):
    """Invalid or unrecognized command"""


class ParamsSyntaxError(SyntaxError):
    """Invalid params or argument format"""


class NotLoggedInError(PermissionError):
    """The client tried to do something that it needs to be logged in for."""


class FileStoragePermissionError(PermissionError):
    """The client doesn't have permission to store files on this server."""

if __name__ == "__main__":

    import sys

    # Port argument
    port = CONFIG['defaultPort']
    if len(sys.argv) >= 2:
        try:
            port = int(sys.argv[1])
        except Exception:
            log("Need integer in <port> argument.")
            exit()

    # Address argument (should usually be the same as localhost)
    address = CONFIG['defaultAddress']
    if len(sys.argv) >= 3:
        address = sys.argv[2]

    log("Starting FTP server at port {0}".format(port))

    s = Server(address, port)
    s.listen()
