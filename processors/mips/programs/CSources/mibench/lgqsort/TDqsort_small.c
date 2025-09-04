#define UNLIMIT
#define MAXARRAY	200 /* this number, if too large, will cause a seg. fault!! */
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
  char *find_strings[] = {
"Kurt",
"Vonneguts",
"Commencement",
"Address",
"at",
"MIT",
"Ladies",
"and",
"gentlemen",
"of",
"the",
"class",
"of",
"97",
"Wear",
"sunscreen",
"If",
"I",
"could",
"offer",
"you",
"only",
"one",
"tip",
"for",
"the",
"future",
"sunscreen",
"would",
"be",
"it",
"The",
"longterm",
"benefits",
"of",
"sunscreen",
"have",
"been",
"proved",
"by",
"scientists",
"whereas",
"the",
"rest",
"of",
"my",
"advice",
"has",
"no",
"basis",
"more",
"reliable",
"than",
"my",
"own",
"meandering",
"experience",
"I",
"will",
"dispense",
"this",
"advice",
"now",
"Enjoy",
"the",
"power",
"and",
"beauty",
"of",
"your",
"youth",
"Oh",
"never",
"mind",
"You",
"will",
"not",
"understand",
"the",
"power",
"and",
"beauty",
"of",
"your",
"youth",
"until",
"theyve",
"faded",
"But",
"trust",
"me",
"in",
"20",
"years",
"youll",
"look",
"back",
"at",
"photos",
"of",
"yourself",
"and",
"recall",
"in",
"a",
"way",
"you",
"cant",
"grasp",
"now",
"how",
"much",
"possibility",
"lay",
"before",
"you",
"and",
"how",
"fabulous",
"you",
"really",
"looked",
"You",
"are",
"not",
"as",0};
/*
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
"They",
"will",
"only",
"make",
"you",
"feel",
"ugly",
"Get",
"to",
"know",
"your",
"parents",
"You",
"never",
"know",
"when",
"theyll",
"be",
"gone",
"for",
"good",
"Be",
"nice",
"to",
"your",
"siblings",
"Theyre",
"your",
"best",
"link",
"to",
"your",
"past",
"and",
"the",
"people",
"most",
"likely",
"to",
"stick",
"with",
"you",
"in",
"the",
"future",
"Understand",
"that",
"friends",
"come",
"and",
"go",
"but",
"with",
"a",
"precious",
"few",
"you",
"should",
"hold",
"on",
"Work",
"hard",
"to",
"bridge",
"the",
"gaps",
"in",
"geography",
"and",
"lifestyle",
"because",
"the",
"older", 0};
*/

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

