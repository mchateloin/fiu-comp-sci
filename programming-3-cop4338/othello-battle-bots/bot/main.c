#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<fcntl.h>
#include<signal.h>
#include <sys/poll.h>
#include"battleinfo.h"
#include"game.h"

int main(int argc, char *argv[])
{

    if (argc < 2)
    {
        error_out("Not enough arguments. Need number of pipes.");
    }

    ngames = atoi(argv[1]);
    if (ngames < 1)
    {
        error_out("Invalid parameter. Need positive integer for number of pipes.");
    }

    // Printing our own process ID
    pid_t own_pid = getpid();
    printf("pid=%d\n", own_pid);

    signal(SIGTERM, sigterm_handler);
    signal(SIGINT, sigterm_handler);

    int i;
    char pipe_name_i[64] = {0}, pipe_name_o[64] = {0};
    games = malloc(sizeof(GAME_T) * ngames);
    game_events = malloc(sizeof(struct pollfd) * ngames);

    for (i = 0; i < ngames; i++)
    {
        GAME_T g;

        snprintf(pipe_name_i, 63, "%ld_%di", (long)own_pid, i);
        snprintf(pipe_name_o, 63, "%ld_%do", (long)own_pid, i);
        
        mkfifo(pipe_name_i, 0666);
        mkfifo(pipe_name_o, 0666);
        
        g.player_id = 0;
        g.in = open(pipe_name_i, O_RDWR | O_NONBLOCK);
        g.in_path = malloc(sizeof(char) * (int)strlen(pipe_name_i) + 1);
        g.out_path = malloc(sizeof(char) * (int)strlen(pipe_name_o)) + 1;
        strcpy(g.in_path, pipe_name_i);
        strcpy(g.out_path, pipe_name_o);
        games[i] = g;

        struct pollfd ge_in;
        ge_in.fd = g.in;
        ge_in.events |= POLLIN;
        ge_in.revents = 0;
        game_events[i] = ge_in;
    }

    enter_game_loop();

    return 0;
};



