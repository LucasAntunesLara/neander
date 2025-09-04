int globalVar = -11;
int globalVar2 = 255;

int soma ( int a, int b);

int
main ( )
{
    int x, y;
    x = 1;
    y = 10;
    globalVar = soma ( x, y);
    // printf ( "%d\n", globalVar);
}

int
soma ( int a, int b)
{
    a = a + b;
    return ( a);
}

