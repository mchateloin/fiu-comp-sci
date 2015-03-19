#include <stdio.h>
#include <stdlib.h>
#include <string.h>


#define SHORT_LENGTH	10
int main()
{
    char *short_string;
    char *long_string;
    char *short_target;
    char *s2;
    char *long_target;
    int long_length;

    char *line=NULL;
    size_t len;

    short_string = "Short Str";
    long_string  = "123456789012345678901234567890";

    s2 = malloc(SHORT_LENGTH+1);
    short_target = malloc(SHORT_LENGTH+1);
    long_length = strlen(long_string);
    long_target = malloc(strlen(long_string) + 1);

    printf("ss %p; ls %p; st %p; s2 %p lt %p\n",short_string, long_string, 
		                        short_target, s2, long_target);

    //
    //  This set of copies is just fine
    //
    strcpy(short_target, short_string);
    strcpy(long_target, long_string);

    printf("short target: %s\n", short_target);
    printf("long target: %s\n", long_target);
    getline(&line, &len,stdin);


    //
    //This copy introduces a problem
    //
    strcpy(short_target, long_string);
    printf("short target: %s\n", short_target);
    printf("long target: %s\n", long_target);
    getline(&line, &len,stdin);


    strcpy(long_target, long_string);
    strncpy(short_target, short_string, SHORT_LENGTH);
    short_target[SHORT_LENGTH] = '\0';
    printf("short target: %s\n", short_target);
    printf("long target: %s\n", long_target);
    getline(&line, &len,stdin);

    strcpy(long_target, long_string);
    strncpy(short_target, long_string, SHORT_LENGTH);
    short_target[SHORT_LENGTH] = '\0';
    printf("short target: %s\n", short_target);
    printf("long target: %s\n", long_target);
    getline(&line, &len,stdin);


    sprintf(long_target, "hello");
    printf("long target: %s\n", long_target);
    getline(&line, &len,stdin);
    strncat(long_target, "bye", long_length);
    printf("long target: %s\n", long_target);
    getline(&line, &len,stdin);

    //  strncmp()
    s2 = strchr(long_target, 'l');   
    printf("Found target: %s\n", s2);
    getline(&line, &len,stdin);

    sprintf(long_target, "abc ; x y z ; 123 ; more ;");

    s2 = strtok(long_target, "; ");
    printf("Found token: %s\n", s2);
    getline(&line, &len,stdin);

    while ((s2 = strtok(NULL, "; ")) != NULL)
    {
        printf("Found token: %s\n", s2);
        getline(&line, &len,stdin);
    }

    return 0;
}

