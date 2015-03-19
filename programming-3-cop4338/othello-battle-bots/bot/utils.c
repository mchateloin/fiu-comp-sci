#include<stdio.h>
#include<stdlib.h>
#include<stdbool.h>
#include"utils.h"

// A wrapper around getc. Gets the next positive/zero integer in a file.
// Returns the next positive/zero integer as an int.
int fgeti(FILE *fp)
{
    int c;
    int s = 0;
    bool indigit = false;
    while ((c = getc(fp)) != EOF)
    {
        if (isdigit(c))
        {
            indigit = true;
            s = (s * 10) + (c - 48);
        }
        else if (indigit)
        {
            return s;
        }
    }

    return indigit ? s : -1;
}

void error_out(char *message)
{
    printf("ERROR: %s\n", message);
    exit(EXIT_FAILURE);
}