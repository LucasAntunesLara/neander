#define CHAR 0
#define INT 1

#define TAM 1024

struct hash {
    int key;
    int value;
};

int globalVar = -1;

main ( )
{
    int ret;
    int i;
    struct hash h [ TAM];
    
    for ( i = 0; i < TAM; i ++)  {
	h [ i].key = i ;
	h [ i].value = i * 2;
    }   
    
    ret = searchHash ( h, TAM, 600);
    globalVar = ret;
    printf ( "%d\n", ret);
}

int
searchHash ( struct hash * pt, int tam, int key)    
{
    int i;
    struct hash * aux = pt;
    
    for ( i = 0; i < tam; i ++, aux ++) 
	if ( aux->key == key) return ( aux->value);
    
    return ( - 1);
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
