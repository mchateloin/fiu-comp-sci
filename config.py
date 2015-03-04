"""config.py: Loads configuration file, which defines some defaults, as well as all our reply codes. Codes include
templates for regularly served messages. Not all of the codes are used though, as we haven't implemented all the
commands. Reply codes are taken directly from RFC959: https://tools.ietf.org/html/rfc959"""

import sys
import json

CONFIG_FILE_PATH = './config.json'
CONFIG = json.load(open(CONFIG_FILE_PATH))