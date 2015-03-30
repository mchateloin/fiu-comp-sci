'''config.py: Loads configuration file which defines some defaults. '''


import json

CONFIG_FILE_PATH = './config.json'
CONFIG = json.load(open(CONFIG_FILE_PATH))