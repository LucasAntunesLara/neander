#define UNLIMIT
#define MAXARRAY	300 /* this number, if too large, will cause a seg. fault!! */
#define SIZE_STRING	16

struct myStringStruct {
  char qstring[SIZE_STRING];
};

// Prototipos
int separaS (struct myStringStruct * v, int p, int r);
void quicksortS (struct myStringStruct * v, int p, int r);
int strcmp2 ( char * first, char * second);
int strlen2 ( char * str);
int memcpy2 ( char * dest, char * org, int size);

int results[5] = {-1,-1,-1,-1,-1};
struct myStringStruct array[MAXARRAY];

int
main(int argc, char *argv[]) {
  char *find_strings[] = {"abb", "you", "not", "it", "dad", "yoo", "hoo", 
                          "oo", "oh", "xx", "xx", "x", "x", "field", "new", "row",0};
			  /*
                          "regime", "boom", "that", "impact", "and", "zoom", "texture",
                          "magnet", "doom", "loom", "freq", "current", "phase",
                          "images", "appears", "phase", "conductor", "wavez", 
                          "normal", "free", "termed", "provide", "for", "and", "struct", "about", "have",
                          "proper", "involve", "describedly", "thats", "spaces", "circumstance", "the",
                          "member", "such", "guide", "regard", "officers", "implement", "principalities", 0};
			  
			  "abb", "you", "not", "it", "dad", "yoo", "hoo", 
                          "oo", "oh", "xx", "xx", "x", "x", "field", "new", "row",
                          "regime", "boom", "that", "impact", "and", "zoom", "texture",
                          "magnet", "doom", "loom", "freq", "current", "phase",
                          "images", "appears", "phase", "conductor", "wavez", 
                          "normal", "free", "termed", "provide", "for", "and", "struct", "about", "have",
                          "proper", "involve", "describedly", "thats", "spaces", "circumstance", "the",
                          "member", "such", "guide", "regard", "officers", "implement", "principalities", 0};/*        
                          "abb", "you", "not", "it", "dad", "yoo", "hoo", 
                          "oo", "oh", "xx", "xx", "x", "x", "field", "new", "row",
                          "regime", "boom", "that", "impact", "and", "zoom", "texture",
                          "magnet", "doom", "loom", "freq", "current", "phase",
                          "images", "appears", "phase", "conductor", "wavez", 
                          "normal", "free", "termed", "provide", "for", "and", "struct", "about", "have",
                          "proper", "involve", "describedly", "thats", "spaces", "circumstance", "the",
                          "member", "such", "guide", "regard", "officers", "implement", "principalities",
			  "abb", "you", "not", "it", "dad", "yoo", "hoo", 
                          "oo", "oh", "xx", "xx", "x", "x", "field", "new", "row",
                          "regime", "boom", "that", "impact", "and", "zoom", "texture",
                          "magnet", "doom", "loom", "freq", "current", "phase",
                          "images", "appears", "phase", "conductor", "wavez", 
                          "normal", "free", "termed", "provide", "for", "and", "struct", "about", "have",
                          "proper", "involve", "describedly", "thats", "spaces", "circumstance", "the",
                          "member", "such", "guide", "regard", "officers", "implement", "principalities",
			  "abb", "you", "not", "it", "dad", "yoo", "hoo", 
                          "oo", "oh", "xx", "xx", "x", "x", "field", "new", "row",
                          "regime", "boom", "that", "impact", "and", "zoom", "texture",
                          "magnet", "doom", "loom", "freq", "current", "phase",
                          "images", "appears", "phase", "conductor", "wavez", 
                          "normal", "free", "termed", "provide", "for", "and", "struct", "about", "have",
                          "proper", "involve", "describedly", "thats", "spaces", "circumstance", "the",
                          "member", "such", "guide", "regard", "officers", "implement", "principalities",
			  0};*/
    int i, count=0; 

    while((find_strings[count] != 0) && (count < MAXARRAY)) {
	memcpy2 ( array[count].qstring, find_strings[count], strlen (find_strings[count])+1);
	count++;  
    }

#ifndef TDBENCH
    printf("\nSorting %d elements.\n\n",count);
#endif
    quicksortS(array,0, count-1);

#ifndef TDBENCH
    for(i=0;i<count;i++)
	printf("%d = %s\n", i, array[i].qstring);
#endif
    results [0] = (int) & array[0].qstring[0];
    results [1] = (int) & array[count-1].qstring[0];
    
#ifndef TDBENCH
#endif

    return 0;
}

void troca ( char * str1, char * str2)
{
    char ttmp [SIZE_STRING];

#ifndef TDBENCH
#else
#endif

    memcpy2 ( ttmp, str1, strlen (str1) + 1);
    memcpy2 ( str1, str2, strlen (str2) + 1);
    memcpy2 ( str2, ttmp, strlen (ttmp) + 1);
}

int separaS ( struct myStringStruct * v, int p, int r)
{
    char ctmp [SIZE_STRING], i = p+1, j = r;
    
    memcpy2 ( ctmp, v[p].qstring, strlen ( v[p].qstring) + 1);
    while ( 1) {
	while ( i <= r && strcmp2 ( v[i].qstring, ctmp) <= 0) {
	    ++i;
	}
	while ( strcmp2 ( ctmp, v[j].qstring) < 0) -- j;
	if ( i >= j) break;
	troca ( v[i].qstring, v[j].qstring);
	++i; --j;
    }
    troca ( v[p].qstring, v[j].qstring);
    return j;
}

void quicksortS (struct myStringStruct * v, int p, int r) // , int sizeItem, int (*compare)())
{
    int j; //, p, r;
    
    while (p < r) {      
	j = separaS (v, p, r);    
	if (j - p < r - j) {     
	    quicksortS (v, p, j-1);
	    p = j + 1;            
	} else {                 
	    quicksortS (v, j+1, r);
	    r = j - 1;
	}
    }
}

int
strlen2 ( char * str)
{
    char * aux = str;
    int tam;
    
    for ( tam = 0; * aux ++; ) tam ++;
    
    return ( tam);
}

int
strcmp2 ( char * first, char * second)
{
    int i, l1, l2, len;
    
    l1 = strlen2 ( first);
    l2 = strlen2 ( second);
    len = l1 > l2 ? l2 : l1;

    for ( i = 0; i < len; i ++) {
      if ( first [ i] != second [ i]) return ( (int) first[i]-second[i]);
    }
    
    if ( l1 > l2) return ( 1);
    else if ( l2 > l1) return ( -1);
    else return ( 0);
}

int
memcpy2 ( char * dest, char * org, int size)
{
    int i;
    
    for ( i = 0; i < size; i ++) * dest ++ = * org ++;
}

