#define	CHAR	0
#define INT	1

int globalInformation = 0;

main ( )
{
    int size = sizeof2 ( 1);
    globalInformation = size;
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
