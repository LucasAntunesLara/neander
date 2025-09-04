#define TAM 10

int res1 = -1;
int res2 = -1;

main ( )
{
    int x [] = {2, -113, -11, -12, -13, -14, -15, -16, -17, -18, -19, -10, -1, -3, -5, -7, -9, -2, -4, -6, -2, 113, 11, 12, 13, 14, 15, 16, 17, 18, 19, 10, 1, 3, 5, 7, 9, 2, 4, 6,
		21,22,31,32,23,24,33,34,25,26,35,36,27,28,37,38,29,30,39,40};
    int i;

    res1 = (int) & x[0];
    res2 = (int) & x[TAM-1];    

    ordena ( x, TAM);

    for ( i = 0; i < TAM; i++) printf ( " %d ", x [i]);
}

ordena ( int vet[], int n)
{
    int i, temp, troca = 1;
    
    while ( troca == 1) {
	troca = 0;
        for ( i = 0; i < n-1; i ++) {
		if ( vet[i] > vet[i+1]) {
	    	    temp = vet[i];
		    vet [i] = vet [i+1];
		    vet [i+1] = temp;
    	    	    troca = 1;
		} 
        }
    }
}

memcpy2 ( char * dest, char * org, int size)
{
    int i;
    
    for ( i = 0; i < size; i ++) * dest ++ = * org ++;
}
