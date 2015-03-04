"""utils.py: Some basic helper functions used throughout the project."""

import datetime
import os


def log(message):
    print(datetime.datetime.now().strftime("[%Y-%m-%dT%H:%M:%S] ") + message)


def strip_lines(s):
    return s.replace('\n', '').replace('\r', '')


def hex_concat(h_left, h_right):

    h_right_size = 0

    while (h_right >> h_right_size) > 0:
        h_right_size += 1

    offset = (h_right_size % 4) + h_right_size

    return (h_left << offset) | h_right


def folder_exists(folder_path):
    return os.path.exists(resolve_path(folder_path))


def file_exists(file_path):
    return os.path.isfile(resolve_path(file_path))


def delete_file(file_path):
    os.remove(file_path)


def resolve_path(path):
    return os.path.abspath(path)


def create_dir(path):
    os.makedirs(resolve_path(path))


def get_running_dir():
    return os.getcwd()


def get_filename(path):
    return os.path.split(path)[1]