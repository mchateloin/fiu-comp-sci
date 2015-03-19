#include <stdio.h>

#define DEFAULT_LOWER 0
#define DEFAULT_UPPER 300
#define DEFAULT_STEP 20
#define DEG 0xB0

float fahr_to_celc(float);
float celc_to_kelv(float);
float fahr_to_kelv(float);

void main(int argc, char *argv[])
{
	float fahr;
	float upper = DEFAULT_UPPER;
	float lower = DEFAULT_LOWER;
	float step = DEFAULT_STEP;
	
	for(fahr = lower; fahr <= upper; fahr += step)
	{
		printf("%.1f%cF\t%.1f%cC\t%.1fK\n", fahr, DEG, fahr_to_celc(fahr), DEG, fahr_to_kelv(fahr));
	}
}

float fahr_to_celc(float fahr)
{
	return (5.0/9.0) * (fahr-32);
}

float celc_to_kelv(float celc)
{
	return celc + 273.15;
}

float fahr_to_kelv(float fahr)
{
	return celc_to_kelv(fahr_to_celc(fahr));
}
