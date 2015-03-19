#include <stdio.h>
#include <unistd.h>
#include <signal.h>

int counter = 0;
int keep_going = 1;

void user_sig1_handler()
{
	keep_going = 0;
	printf("Caught user signal1\n");
	signal(SIGUSR1, user_sig1_handler);
}

int main()
{
	int pid;

	pid = getpid();
	printf("Hello, I'm pid %d\n", pid);
	signal(SIGHUP, SIG_IGN);
	signal(SIGUSR1, user_sig1_handler);

	for (;keep_going;)
	{
		sleep(2);
		printf("%d\n",counter);
		counter++;
	}
	printf("Now I can clean up and be done\n");
	return 0;
}
