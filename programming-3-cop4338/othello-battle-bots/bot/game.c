#include"battleinfo.h"
#include"game.h"
#include <sys/poll.h>
#include <sys/types.h>

GAME_T *games;

int ngames;

struct pollfd *game_events;

void send_chosen_move(GAME_T *game, CELL_T *chosen)
{
    FILE *fp = fopen(game->out_path, "a+");
    if (!fp)
    {
        error_out("File does not exist.");
    }

    fprintf(fp, "[%d, %d, %d]", game->player_id, chosen->row, chosen->col);
    fflush(fp);
    fclose(fp);
}

//State of the art algorithm
CELL_T get_chosen_move(BATTLEINFO_T *bi)
{   
    return *bi->first_empty;
}

void enter_game_loop()
{
	while(1)
    {
        int nupdates = poll(game_events, ngames, -1);

        if (nupdates == -1)
        {
            error_out("poll error"); // error occurred in poll()
        }
        else if (nupdates == 0)
        {
            error_out("poll timeout!");
        } 
        else
        {
        	int i;
            for(i = 0; i < ngames; i++)
            {

                if((game_events[i].revents & POLLIN) == POLLIN)
                {
                    BATTLEINFO_T bi = read_fd_battleinfo(game_events[i].fd);
                    print_battleinfo(&bi);

                    //Set player id for this game
                    if(games[i].player_id == 0)
                    {
                        games[i].player_id = bi.num_players + 1;
                    }

                    CELL_T chosen = get_chosen_move(&bi);
                    send_chosen_move(&games[i], &chosen);
                    fflush(NULL);

                    game_events[i].revents &= 0;
                }

            }
        }

        nupdates = 0;
    }
}

void sigterm_handler(int signum)
{   
    int i;
    for(i = 0; i < ngames; i++)
    {
        close(games[i].in);
        unlink(games[i].in_path);
        unlink(games[i].out_path);
    }
}