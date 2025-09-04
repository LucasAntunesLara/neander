#define CHAR 0
#define INT 1

#define TAM	27
#define RUN	27

int varGlobal = 0;

main ( )
{
    // IMPORTANTE: atribuir um nro. de colunas multiplo de 4
    char teste [TAM] [60] = 	{ 	"ab", "b", "teste da nossa funcao strlen aaaaaaaaa bbbbbbbbb",
					"teste da nossa funcao strlen aaaaaaaaa bbbbbbbb",
					"teste da nossa funcao strlen aaaaaaaaa bbbbbbb",
					"teste da nossa funcao strlen aaaaaaaaa bbbbbb",
					"teste da nossa funcao strlen aaaaaaaaa bbbbb",
					"teste da nossa funcao strlen aaaaaaaaa bbbb",
					"teste da nossa funcao strlen aaaaaaaaa bbb",
					"teste da nossa funcao strlen aaaaaaaaa bb",
					"teste da nossa funcao strlen aaaaaaaaa b",
					"teste da nossa funcao strlen aaaaaaaaa ",
					"teste da nossa funcao strlen aaaaaaaaa",
					"teste da nossa funcao strlen aaaaaaaa",
					"teste da nossa funcao strlen aaaaaaa",
					"teste da nossa funcao strlen aaaaaa",
					"teste da nossa funcao strlen aaaaa",
					"teste da nossa funcao strlen aaaa",
					"teste da nossa funcao strlen aaa",
					"teste da nossa funcao strlen aa",
					"teste da nossa funcao strlen a",
					"teste da nossa funcao strlen ",
					"teste da nossa funcao strlen",
					"teste da nossa funcao strle",
					"teste da nossa funcao strl",
					"teste da nossa funcao str",
					"teste da nossa funcao st"

				};

    int i;
    
    for ( i = 0; i < RUN; i ++) {
	varGlobal = strlength ( teste [i]);
        printf ( "%d\n", varGlobal);
    }
    for ( i = 0; i < RUN; i ++) {
	varGlobal = strlength ( teste [i]);
        printf ( "%d\n", varGlobal);
    }
    for ( i = 0; i < RUN; i ++) {
	varGlobal = strlength ( teste [i]);
        printf ( "%d\n", varGlobal);
    }
    for ( i = 0; i < RUN; i ++) {
	varGlobal = strlength ( teste [i]);
        printf ( "%d\n", varGlobal);
    }
}

strlength ( char * str)
{
    char * aux = str;
    int tam;
    
    for ( tam = 0; * aux ++; ) tam ++;
    
    return ( tam);
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
