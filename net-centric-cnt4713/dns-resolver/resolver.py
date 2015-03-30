'''resolver.py: Some explanation here.'''

import sys
from utils import *
from config import *

if __name__ == "__main__":

    if len(sys.argv) < 3:
        log("Missing arguments. Need root server IP followed by domain name.")
        exit()

    # Root Server IP argument
    root_server_ip = sys.argv[1]
    if root_server_ip == "":
        log("Root server IP argument is empty.")

    # Domain name argument
    domain_name = sys.argv[2]
    if domain_name == "":
        log("Domain name argument is empty.")