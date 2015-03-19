#include <stdio.h>
#include <regex.h>        

#define MAX_MATCHES 10
int main()
{
	regex_t regex;
	int ret;
	char msgbuf[100];
	char *p;

	regmatch_t matches[MAX_MATCHES];

	/* Compile regular expression */
	ret = regcomp(&regex, "a.*z", 0);
	if (ret) {
	    fprintf(stderr, "Could not compile regex\n");
	    return -1;
	}

    	while (scanf("%s", msgbuf))
	{
		/* Execute regular expression */
		printf("execute regex on %s\n",msgbuf);
		ret = regexec(&regex, msgbuf, MAX_MATCHES, matches, 0);
		if (!ret) {
		    puts("Found match");
		    p = msgbuf;
		    for (int i=0; i < MAX_MATCHES; i++)
		    {
			    int start, finish;
			    if (matches[i].rm_so == -1) 
			    {
				    break;
			    }
			    start = matches[i].rm_so + (p - msgbuf);
			    finish = matches[i].rm_eo + (p - msgbuf);
			    printf("%.*s (bytes %d:%d)\n", 
			        (finish-start), msgbuf + start, start, finish);
			    p+= matches[i].rm_eo;
		    }
		    puts("");
		}
		else if (ret == REG_NOMATCH) {
		    puts("No match");
		}
		else {
		    regerror(ret, &regex, msgbuf, sizeof(msgbuf));
		    fprintf(stderr, "Regex match failed: %s\n", msgbuf);
		    return -1;
		}

	}
	// Free compiled regex
	regfree(&regex);
}

