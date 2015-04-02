'''resolver.py: Some explanation here.'''

import sys
import socket
import struct
import pprint
import codecs
from utils import *
from config import *

class Resolver:

    DEFAULT_PORT = CONFIG["defaultPort"]



    def parse_response(self, response):
        sequence = bytearray(response)
        headers = struct.unpack("!HHHHHH", sequence[0:12])
        x = sequence[0]
        y = sequence[1:3]

        i_questions = 12
        i_answers = i_questions

        while int(sequence[i_answers]) != 0:
            len_label = int(sequence[i_answers])
            i_answers += sequence[i_answers] + 1
        i_answers += 1

        body = sequence[i_answers:]

        answers = []


        return {
            "message_id": headers[0],
            "opcode": headers[1],
            "qdcount": headers[2],
            "ancount": headers[3],
            "nscount": headers[4],
            "arcount": headers[5],
        }

    def build_query(self, host):
        packet = struct.pack("!HHHHHH", 0x0001, 0x0100, 1, 0, 0, 0)

        for label in host.split("."):
            packet += struct.pack("!b" + str(len(label)) + "s", len(label), bytes(label, "utf-8"))

        packet += struct.pack("!bHH",0,1,1)

        return packet

    def get_udp_socket(self):
        return socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

    def find(self, root_server_ip, domain_name):
        query = self.build_query(domain_name)
        #log(query)
        self.send(root_server_ip, query)

    def send(self, ip, datagram):
        sock = self.get_udp_socket()
        #sock.settimeout(2)

        sock.sendto(datagram, (ip, self.DEFAULT_PORT))

        log("----------------------------------")
        log("DNS server to query: %s" % ip)




        response, address = sock.recvfrom(512)
        resp_decoded = self.parse_response(response)

        log("Reply received. Content overview:")

        log("\t%d Answers." %  resp_decoded["ancount"])
        log("\t%d Intermediate Name Servers." % resp_decoded["nscount"])
        log("\t%d Additional Information Records" % resp_decoded["arcount"])




if __name__ == "__main__":

    if len(sys.argv) < 3:
        log("Missing arguments. Need root server IP followed by domain name.")
        exit()

    # Root Server IP argument
    root_server_ip = sys.argv[1]
    if root_server_ip == "":
        log("Root server IP argument is empty.")
        exit()

    # Domain name argument
    domain_name = sys.argv[2]
    if domain_name == "":
        log("Domain name argument is empty.")
        exit()


    client = Resolver()
    client.find(root_server_ip, domain_name)
