#define UNLIMIT
#define MAXARRAY	500 /* this number, if too large, will cause a seg. fault!! */
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

int results[10] = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
struct myStringStruct array[MAXARRAY];

static char bits[256] =
{
      0, 1, 1, 2, 1, 2, 2, 3, 1, 2, 2, 3, 2, 3, 3, 4,  /* 0   - 15  */
      1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5,  /* 16  - 31  */
      1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5,  /* 32  - 47  */
      2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6,  /* 48  - 63  */
      1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5,  /* 64  - 79  */
      2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6,  /* 80  - 95  */
      2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6,  /* 96  - 111 */
      3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7,  /* 112 - 127 */
      1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5,  /* 128 - 143 */
      2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6,  /* 144 - 159 */
      2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6,  /* 160 - 175 */
      3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7,  /* 176 - 191 */
      2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6,  /* 192 - 207 */
      3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7,  /* 208 - 223 */
      3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7,  /* 224 - 239 */
      4, 5, 5, 6, 5, 6, 6, 7, 5, 6, 6, 7, 6, 7, 7, 8   /* 240 - 255 */
};

#define CHAR_BIT 8
#define ITERATIONS 1500

int
main(int argc, char *argv[]) {
  char *find_strings[] = {
/*			  "abb", "you", "not", "it", "dad", "yoo", "hoo", 
                          "oo", "oh", "xx", "xx", "x", "x", "field", "new", "row",
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
                          "member", "such", "guide", "regard", "officers", "implement", "principalities",

			  "abb", "you", "not", "it", "dad", "yoo", "hoo", 
                          "oo", "oh", "xx", "xx", "x", "x", "field", "new", "row",
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
                          "member", "such", "guide", "regard", "officers", "implement", "principalities",
			  0};

"fat",
"as",
"you",
"imagine",
"Dont",
"worry",
"about",
"the",
"future",
"Or",
"worry",
"but",
"know",
"that",
"worrying",
"is",
"as",
"effective",
"as",
"trying",
"to",
"solve",
"an",
"algebra",
"equation",
"by",
"chewing",
"bubble",
"gum",
"The",
"real",
"troubles",
"in",
"your",
"life",
"are",
"apt",
"to",
"be",
"things",
"that",
"never",
"crossed",
"your",
"worried",
"mind",
"the",
"kind",
"that",
"blindside",
"you",
"at",
"4",
"pm",
"on",
"some",
"idle",
"Tuesday",
"Do",
"one",
"thing",
"every",
"day",
"that",
"scares",
"you",
"Sing",
"Dont",
"be",
"reckless",
"with",
"other",
"peoples",
"hearts",
"Dont",
"put",
"up",
"with",
"people",
"who",
"are",
"reckless",
"with",
"yours",
"Floss",
"Dont",
"waste",
"your",
"time",
"on",
"jealousy",
"Sometimes",
"youre",
"ahead",
"sometimes",
"youre",
"behind",
"The",
"race",
"is",
"long",
"and",
"in",
"the",
"end",
"its",
"only",
"with",
"yourself",
"Remember",
"compliments",
"you",
"receive",
"Forget",
"the",
"insults",
"If",
"you",
"succeed",
"in",
"doing",
"this",
"tell",
"me",
"how",
"Keep",
"your",
"old",
"love",
"letters",
"Throw",
"away",
"your",
"old",
"bank",
"statements",
"Stretch",
"Dont",
"feel",
"guilty",
"if",
"you",
*/
"dont",
"know",
"what",
"you",
"want",
"to",
"do",
"with",
"your",
"life",
"The",
"most",
"interesting",
"people",
"I",
"know",
"didnt",
"know",
"at",
"22",
"what",
"they",
"wanted",
"to",
"do",
"with",
"their",
"lives",
"Some",
"of",
"the",
"most",
"interesting",
"40yearolds",
"I",
"know",
"still",
"dont",
"Get",
"plenty",
"of",
"calcium",
"Be",
"kind",
"to",
"your",
"knees",
"Youll",
"miss",
"them",
"when",
"theyre",
"gone",
"Maybe",
"youll",
"marry",
"maybe",
"you",
"wont",
"Maybe",
"youll",
"have",
"children",
"maybe",
"you",
"wont",
"Maybe",
"youll",
"divorce",
"at",
"40",
"maybe",
"youll",
"dance",
"the",
"funky",
"chicken",
"on",
"your",
"75th",
"wedding",
"anniversary",
"Whatever",
"you",
"do",
"dont",
"congratulate",
"yourself",
"too",
"much",
"or",
"berate",
"yourself",
"either",
"Your",
"choices",
"are",
"half",
"chance",
"So",
"are",
"everybody",
"elses",
"Enjoy",
"your",
"body",
"Use",
"it",
"every",
"way",
"you",
"can",
"Dont",
"be",
"afraid",
"of",
"it",
"or",
"of",
"what",
"other",
"people",
"think",
"of",
"it",
"Its",
"the",
"greatest",
"instrument",
"youll",
"ever",
"own",
"Dance",
"even",
"if",
"you",
"have",
"nowhere",
"to",
"do",
"it",
"but",
"your",
"living",
"room",
"Read",
"the",
"directions",
"even",
"if",
"you",
"dont",
"follow",
"them",
"Do",
"not",
"read",
"beauty",
"magazines",
0};

    int i, count=0; 
  int j, n, seed;

    while((find_strings[count] != 0) && (count < MAXARRAY)) {
	memcpy2 ( array[count].qstring, find_strings[count], strlen2 (find_strings[count])+1);
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

    for (j = n = seed = 0; j < ITERATIONS; j++, seed ++) {
#ifndef TDBENCH
	printf ( "seed = %d, varios algoritmos => \t", seed);
#endif
	results[2]=seed;
	results[3]=bit_shifter (seed);
	results[4]=bit_count (seed);
	results[5]=bitcount (seed);
	results[6]=ntbl_bitcnt (seed);
	results[7]=ntbl_bitcount (seed);
	results[8]=BW_btbl_bitcount (seed);
	results[9]=AR_btbl_bitcount (seed);
#ifndef TDBENCH
	for ( i = 2; i < 10; i ++)
	    printf ( " %d, ", results[i]);
	printf ( "\n");
#endif
    }

    return 0;
}

void troca ( char * str1, char * str2)
{
    char ttmp [SIZE_STRING];

#ifndef TDBENCH
#else
#endif

    memcpy2 ( ttmp, str1, strlen2 (str1) + 1);
    memcpy2 ( str1, str2, strlen2 (str2) + 1);
    memcpy2 ( str2, ttmp, strlen2 (ttmp) + 1);
}

int separaS ( struct myStringStruct * v, int p, int r)
{
    char ctmp [SIZE_STRING], i = p+1, j = r;
    
    memcpy2 ( ctmp, v[p].qstring, strlen2 ( v[p].qstring) + 1);
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

int bit_shifter( int x)
{
  int i, n;
  
  for (i = n = 0; x && (i < (sizeof(long) * CHAR_BIT)); ++i, x >>= 1)
    n += (int)(x & 1L);
  return n;
}

int bit_count(int x)
{
        int n = 0;
/*
** The loop will execute once for each bit of x set, this is in average
** twice as fast as the shift/test method.
*/
        if (x) do
              n++;
        while (0 != (x = x&(x-1))) ;
        return(n);
}

int bitcount(int i)
{
      i = ((i & 0xAAAAAAAAL) >>  1) + (i & 0x55555555L);
      i = ((i & 0xCCCCCCCCL) >>  2) + (i & 0x33333333L);
      i = ((i & 0xF0F0F0F0L) >>  4) + (i & 0x0F0F0F0FL);
      i = ((i & 0xFF00FF00L) >>  8) + (i & 0x00FF00FFL);
      i = ((i & 0xFFFF0000L) >> 16) + (i & 0x0000FFFFL);
      return (int)i;
}

/*
**  Count bits in each nybble
**
**  Note: Only the first 16 table entries are used, the rest could be
**        omitted.
*/
int ntbl_bitcnt(int x)
{
      int cnt = bits[(int)(x & 0x0000000FL)];

      if (0L != (x >>= 4))
            cnt += ntbl_bitcnt(x);

      return cnt;
}

/*
**  Count bits in each nybble
**
**  Note: Only the first 16 table entries are used, the rest could be
**        omitted.
*/
int ntbl_bitcount(int x)
{
      return
            bits[ (int) (x & 0x0000000FUL)       ] +
            bits[ (int)((x & 0x000000F0UL) >> 4) ] +
            bits[ (int)((x & 0x00000F00UL) >> 8) ] +
            bits[ (int)((x & 0x0000F000UL) >> 12)] +
            bits[ (int)((x & 0x000F0000UL) >> 16)] +
            bits[ (int)((x & 0x00F00000UL) >> 20)] +
            bits[ (int)((x & 0x0F000000UL) >> 24)] +
            bits[ (int)((x & 0xF0000000UL) >> 28)];
}

/*
**  Count bits in each byte
**
**  by Bruce Wedding, works best on Watcom & Borland
*/
int BW_btbl_bitcount(int x)
{
      union 
      { 
            unsigned char ch[4]; 
            long y; 
      } U; 
 
      U.y = x; 
 
      return bits[ U.ch[0] ] + bits[ U.ch[1] ] + 
             bits[ U.ch[3] ] + bits[ U.ch[2] ]; 
}

/*
**  Count bits in each byte
**
**  by Auke Reitsma, works best on Microsoft, Symantec, and others
*/
int AR_btbl_bitcount(int x)
{
      unsigned char * Ptr = (unsigned char *) &x ;
      int Accu ;

      Accu  = bits[ *Ptr++ ];
      Accu += bits[ *Ptr++ ];
      Accu += bits[ *Ptr++ ];
      Accu += bits[ *Ptr ];
      return Accu;
}


