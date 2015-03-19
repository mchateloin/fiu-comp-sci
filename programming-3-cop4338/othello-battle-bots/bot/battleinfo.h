#ifndef BATTLEINFO_H
#define BATTLEINFO_H

#include<stdio.h>

typedef struct CELL
{
    int row;
    int col;
} CELL_T;

typedef struct BATTLEINFO 
{
    int size;
    int num_players;
    int num_empty;
    CELL_T* first_empty;
    CELL_T* last_empty;
    int *player_ranks;
    int *board;
} BATTLEINFO_T;

BATTLEINFO_T read_file_battleinfo(const char *filename);

BATTLEINFO_T read_fd_battleinfo(int fd);

void print_battleinfo(BATTLEINFO_T *bi);

#endif /* BATTLEINFO_H */