CC = cc
#CC = gcc
CFLAGS = 

SRC_CORE = battleinfo.c game.c utils.c
SRC  = main.c $(SRC_CORE)

OBJ  = $(SRC:.c=.o)

all: bot

bot:	$(OBJ)
	$(CC) $(CFLAGS) $^ -o $@

%.o:	%.c $(HDR)
	$(CC) $(CFLAGS) -c $< -o $@

clean:
	rm bot *.o
	find . -name '[0-9]*_[0-9]*[i|o]' -delete 
