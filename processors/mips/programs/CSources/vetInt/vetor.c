#define CHAR 0
#define INT 1

#define TAM 30

int res1=-1, res2=-1, res3=-1;

main ( )
{
    int x [TAM] = {11, 12, 13, 14, 15, 16, 17, 18, 19, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0, -1, -2,-3,-4,-5,-6,-7,-8,-9,-10};

    res1 = maiorNoVet ( x, TAM);
    res2 = menorNoVet ( x, TAM);
    res3 = somaDoVet ( x, TAM);
    
    printf ( "%d\n", res1);
    printf ( "%d\n", res2);
    printf ( "%d\n", res3);
}

maiorNoVet ( int * vet, int tam)
{
    int maior, i;
    
    maior = vet [0];
    
    for ( i = 1; i < tam; i ++) {
	if ( vet[i] > maior) maior = vet [i];
    }
    
    return ( maior);
}

menorNoVet ( int * vet, int tam)
{
    int menor, i;
    
    menor = vet [0];
    
    for ( i = 1; i < tam; i ++) {
	 if ( vet[i] < menor) menor = vet [i];
    }
    
    return ( menor);
}

somaDoVet ( int * vet, int tam)
{
    int soma = 0, i;
    
    for ( i = 0; i < tam; i ++) {
	 soma = soma + vet[i];
    }
    
    return ( soma);
}

memcpy2 ( char * dest, char * org, int size)
{
    int i;
    
    printf ( "size = %d\n", size);
    
    for ( i = 0; i < size; i ++) * dest ++ = * org ++;
}

int
sizeof2 ( int type)
{
    switch ( type) {
	case 0: return ( 1);
	case 1: return ( 4);
	default: return ( -1);
    }
}
