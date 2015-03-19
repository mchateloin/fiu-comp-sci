#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <signal.h>

int counter = 0;
int direction = 1;

char *p = NULL;

void user_sig1_handler()
{
	printf("Caught user signal1\n");
	direction = 1;
	//signal(SIGUSR1, user_sig1_handler);
}

void user_sig2_handler()
{
	printf("Caught user signal2\n");
	direction = -1;
	//signal(SIGUSR2, user_sig2_handler);
}

int main()
{
	int pid;

	pid = getpid();
	printf("Hello, I'm pid %d\n", pid);
	signal(SIGHUP, SIG_IGN);
	signal(SIGUSR1, user_sig1_handler);
	signal(SIGUSR2, user_sig2_handler);

	for (;;)
	{
		sleep(2);
		printf("%d\n",counter);
		counter += direction;
	}
}
