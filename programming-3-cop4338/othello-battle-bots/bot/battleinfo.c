#include<stdio.h>
#include<stdlib.h>
#include"battleinfo.h"
#include"game.h"

void print_battleinfo(BATTLEINFO_T *bi)
{
    printf("Size: %d\n", bi->size);
    printf("Number of players: %d\n", bi->num_players);
    printf("Number of empty cells: %d\n", bi->num_empty);

    if (bi->first_empty && bi->last_empty)
    {
        printf("Location of first empty cell: [%d, %d]\n",
                bi->first_empty->row, bi->first_empty->col);
        printf("Location of last empty cell: [%d, %d]\n",
                bi->last_empty->row, bi->last_empty->col);
    }
}

BATTLEINFO_T _read_battleinfo(FILE *fp);

BATTLEINFO_T read_file_battleinfo(const char *filename)
{
    FILE *fp = fopen(filename, "r+");
    if (!fp)
    {
        error_out("File does not exist.");
    }
    return _read_battleinfo(fp);
}

BATTLEINFO_T read_fd_battleinfo(int fd)
{
    FILE *fp = fdopen(fd, "r+");
    if (!fp)
    {
        error_out("File does not exist.");
    }
    return _read_battleinfo(fp);
}

BATTLEINFO_T _read_battleinfo(FILE *fp)
{
    BATTLEINFO_T bi =
    {
        .size = 0,
        .num_players = 0,
        .num_empty = 0,
        .first_empty = NULL,
        .last_empty = NULL
    };

    bi.size = fgeti(fp);
    bi.num_players = fgeti(fp);
    bi.player_ranks = calloc((bi.num_players + 1), sizeof(int) * (bi.num_players + 1));

    int pid;
    for (pid = 0; pid <= bi.num_players; bi.player_ranks[pid++] = 0);

    int row, col;
    static CELL_T first_empty_read, last_empty_read;
    for (row = 1; row < bi.size + 1; row++)
    {
        for (col = 1; col < bi.size + 1; col++)
        {

            pid = fgeti(fp);
            bi.player_ranks[pid]++;

            if (pid == 0)
            {
                last_empty_read.row = row;
                last_empty_read.col = col;
                bi.last_empty = &last_empty_read;
            }

            if (pid == 0 && !bi.first_empty)
            {
                first_empty_read.row = row;
                first_empty_read.col = col;
                bi.first_empty = &first_empty_read;
            }
        }
    }

    bi.num_empty = bi.player_ranks[0]; //Rank of"player" with pid 0, are empties
    
    return bi;
}