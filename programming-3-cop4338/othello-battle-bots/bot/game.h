#ifndef GAME_H
#define GAME_H

#include<poll.h>
#include"battleinfo.h"

typedef struct GAME
{
    int player_id;
	int in;
	char *in_path;
	char *out_path;
} GAME_T;

extern GAME_T *games;

extern int ngames;

extern struct pollfd *game_events;

void send_chosen_move(GAME_T *game, CELL_T *chosen);

CELL_T get_chosen_move(BATTLEINFO_T *bi);

void enter_game_loop();

void sigterm_handler(int signum);

#endif /* GAME_H */