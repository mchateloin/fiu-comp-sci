#include<stdio.h>
#include<stdlib.h>

#define INPUT_FILE "input.bmp"

void dump_buffer(char *buffer, int buffer_size);

void main()
{
	char *buffer;
	unsigned long flen;
	FILE *fp = fopen(INPUT_FILE, "rb");
	
	if (!fp)
	{
		fprintf(stderr, "Unable to open input file %s", INPUT_FILE);
		return;
	}

	fseek(fp, 0, SEEK_END);
	flen = ftell(fp);
	fseek(fp, 0, SEEK_SET);
	buffer = (char *) malloc(flen + 1);

	if(!buffer)
	{
		fprintf(stderr, "Memory related error.");
		fclose(fp);
		return;
	}

	fread(buffer, flen, 1, fp);
	dump_buffer(buffer, flen);


	fclose(fp);
	free(buffer);
}

void dump_buffer(char *buffer, int buffer_size)
{
	int c;
 	for (c = 0;c < buffer_size; c++)
	{
	     printf("%.2X ", (int)buffer[c]);

	     if (c % 4 == 3)
	     {
	         printf(" ");
	     }

	     if (c % 16 == 15)
	     {
	         printf("\n");
	     }
	}

	// Add an extra line feed for good measure
	printf("\n");
}

