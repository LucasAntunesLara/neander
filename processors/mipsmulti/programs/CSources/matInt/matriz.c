#define CHAR 0
#define INT 1

#define LIN	10
#define	COL	10

int res1;

main ( )
{
    int matriz [LIN] [COL] = { 
				{ 121, 25, 123, 21, 22, 23, 24, 25, 26, 27} , 
				{ 134, 333, 316, 37, 36, 35, 34, 33, 32, 31}, 
				{ 117, 118, 119, 11, 12, 13, 14, 15, 16, 17},
				{ 141, 45, 143, 41, 42, 43, 44, 45, 46, 47} , 
				{ 154, 353, 156, 57, 56, 55, 54, 53, 52, 51}, 
				{ 167, 618, 169, 61, 62, 63, 64, 65, 66, 67},
				{ 181, 85, 183, 81, 82, 83, 84, 85, 86, 87} , 
				{ 194, 339, 196, 97, 96, 95, 94, 93, 92, 91}, 
				{ 17, 18, 19, 1, 2, 3, 4, 5, 6, 7},
				{ 177, 178, 179, 71, 72, 73, 74, 75, 76, 77}
			    };
    
    res1 = maiorNaMatriz ( & matriz [0] [0], LIN, COL);
    
    printf ( "%d\n", res1);
}

maiorNaMatriz ( int * mat, int lin, int col) {
    int i, j, maior;
    int * aux;
    
    aux = mat;
    maior = * aux;
    
    for ( i = 0; i < lin; i ++) {
	for ( j = 0; j < col; j ++) {
	    if ( * aux > maior) maior = * aux;
	    aux ++;
	}
    }        
    
    return ( maior);
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
