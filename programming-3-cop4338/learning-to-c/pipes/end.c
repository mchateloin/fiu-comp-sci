#include <signal.h>
#include <stdlib.h>

int main(int argc, char** argv)
{
	int pid = atoi(argv[1]);

	kill(pid, SIGUSR1);
}



