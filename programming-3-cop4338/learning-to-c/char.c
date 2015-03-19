#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>

int main()
{
    char * line = NULL;
    size_t len;

    char chars[] = { 
        ' ',
        '\t',
        '\0',
	'\007',
        'a',
        'A',
        '1'
    };

    int num_chars = sizeof(chars)/sizeof(char);

    for (int i = 0; i < num_chars; i++)
    {
	char c = chars[i];
        printf("Char %d = '%c'\n", i, c);
        printf(" isalpha(%c) = %s\n", c, isalpha(c) ? "true" : "false");
        printf(" isalnum(%c) = %s\n", c, isalnum(c) ? "true" : "false");
        printf(" iscntrl(%c) = %s\n", c, iscntrl(c) ? "true" : "false");
        printf(" isdigit(%c) = %s\n", c, isdigit(c) ? "true" : "false");
        printf(" isxdigit(%c) = %s\n", c, isxdigit(c) ? "true" : "false");
        printf(" isgraph(%c) = %s\n", c, isgraph(c) ? "true" : "false");
        printf(" islower(%c) = %s\n", c, islower(c) ? "true" : "false");
        printf(" isupper(%c) = %s\n", c, isupper(c) ? "true" : "false");
        printf(" isprint(%c) = %s\n", c, isprint(c) ? "true" : "false");
        printf(" ispunct(%c) = %s\n", c, ispunct(c) ? "true" : "false");
        printf(" isspace(%c) = %s\n", c, isspace(c) ? "true" : "false");

	getline(&line, &len, stdin);
    }

    free(line);


    return 0;
}

