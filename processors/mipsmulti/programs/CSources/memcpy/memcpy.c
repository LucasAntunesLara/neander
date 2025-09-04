#define CHAR 0
#define INT 1

int globalInformation = 0;

main ( )
{

    int x [5] = {10,20,30,40,50}, y[5]={-1,-1,-1,-1,-1};
    
    memcpy2 ( y, x, 5 * sizeof2 ( INT));
    
    printf ( "x = %d, y = %d\n", x[4], y[4]);

/*
    int x = 121, y, size;
    
    size = sizeof2 ( INT);
    
    memcpy2 ( & y, & x, size);
    
    globalInformation = y;
    
    printf ( "globalInformation = %d\n", globalInformation);
*/
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
