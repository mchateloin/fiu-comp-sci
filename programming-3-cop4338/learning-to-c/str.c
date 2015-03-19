#include <stdio.h>
#include <stdlib.h>

int main()
{
	char * s1 = "ABC";
	char s2[6];
	char s3[] = "hello";
	const char *s4;	// Pointer to constant character.
	char * const s5 = "BAMBAM";
	const char * const s6 = "PEBBLES";

	s2[0] = 'X';
	s2[1] = '\0';
	s4 = "FRED";
	//s5 = "WILMA"; // fails to compile

	printf("start:\n");
	printf("s1=>%s<\n",s1);
	printf("s2=>%s<\n",s2);
	printf("s3=>%s<\n",s3);
	printf("s4=>%s<\n",s4);
	printf("s5=>%s<\n",s5);
	printf("s6=>%s<\n",s6);

	//s1[0] = 'D';  // Causes a SEGERROR
	s2[0] = 'Y';
	s3[0] = 'E';	// Why does this work?
	//s4[0] = 'B';  // Causes a compilation error, Read-Only
	s4 = "BARNEY";  // Works fine
	//s5[0] = 'X';  // Causes a SEGERROR
	//s6[0] = 'X';  // Causes a compilation error, Read-Only
	
	//s4 = s3;	// Works fine
	//s4[0] = 'F';	// but this fails to compile, Read-Only
	printf("s1=>%s<\n",s1);
	printf("s2=>%s<\n",s2);
	printf("s3=>%s<\n",s3);
	printf("s4=>%s<\n",s4);
	printf("s5=>%s<\n",s5);
	printf("s6=>%s<\n",s6);
	
	s1 = malloc(10);
	//s2 = malloc(10);  // These are arrays, not char *
	//s3 = malloc(10); // won't compile.
	s4 = malloc(10);
	//s5 = malloc(10); // Fails to compile, Read-Only
	//s6 = malloc(10); // Fails to compile, Read-Only


	printf("s1=>%s<\n",s1);
	printf("s2=>%s<\n",s2);
	printf("s3=>%s<\n",s3);
	printf("s4=>%s<\n",s4);
	printf("s5=>%s<\n",s5);
	printf("s6=>%s<\n",s6);
}

