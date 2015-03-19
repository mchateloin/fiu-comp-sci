"""user.py: Maintains the state for a connected client. Includes credentials as well as connection information"""
from utils import *

class User:

    default_username = "Anonymous"

    def __init__(self, conn_control, address):
        self.conn_control = conn_control
        self.address = address
        self.ip = address[0]
        self.port = address[1]
        self.curr_dir = "/"
        self.username = self.default_username
        self.password = None
        self.data_port = None

    def reset_creds(self):
        self.username = self.default_username
        self.password = None

    def is_valid(self):
        return self.username == self.password

    def change_dir(self, path):
        self.curr_dir = self.get_rel_path(path)

    def get_rel_path(self, path):
        if path[0] == '/' or path[0] == '\\':
            return os.path.normpath(path)
        else:
            return os.path.normpath(os.path.join(self.curr_dir, path))

    def get_abs_path(self, path):
        if path[0] == '/' or path[0] == '\\':
            return os.path.normpath(os.path.join(get_running_dir(), path[1:]))
        else:
            return os.path.normpath(os.path.join(self.get_abs_cwd(), path))

    def get_abs_path_from_root(self, path):
        return self.get_abs_path(self.get_rel_path(path))

    def get_rel_cwd(self):
        return self.curr_dir

    def get_abs_cwd(self):
        return os.path.normpath(os.path.join(get_running_dir(), self.curr_dir[1:]))

