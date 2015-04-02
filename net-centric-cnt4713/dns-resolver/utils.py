"""utils.py: Some basic helper functions used throughout the project."""

import datetime


def log(message):
    print(datetime.datetime.now().strftime("[%Y-%m-%dT%H:%M:%S] ") + message)