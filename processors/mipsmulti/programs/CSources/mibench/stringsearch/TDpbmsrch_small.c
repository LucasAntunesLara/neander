/* +++Date last modified: 05-Jul-1997 */

/*
**        A Pratt-Boyer-Moore string search, written by Jerry Coffin
**  sometime or other in 1991.  Removed from original program, and
**  (incorrectly) rewritten for separate, generic use in early 1992.
**  Corrected with help from Thad Smith, late March and early
**  April 1992...hopefully it's correct this time. Revised by Bob Stout.
**
**  This is hereby placed in the Public Domain by its author.
**
**  10/21/93 rdg  Fixed bug found by Jeff Dunlop
*/

// #include <stddef.h>
// #include <string.h>
// #include <limits.h>

// Prototipos
int strncmp2 ( char * first, char * second, int length);
int strlen2 ( char * str);
int memcpy2 ( char * dest, char * org, int size);
void init_search(const char *string);
char *strsearch(const char *string);

// Vetor Global para receber dados intermediarios (para depuracao)
int results [5] = {-1, -1, -1, -1, -1};
//
static int table[255 + 1];
static int len;
static char *findme;

main()
{
      char *here;
      char *find_strings[] = {"abb",  "you", "not", "it", "dad", "yoo", "hoo", 0,
                              "oo", "oh", "xx", "xx", "x", "x", "field", "new", "row",
         "regime", "boom", "that", "impact", "and", "zoom", "texture",
         "magnet", "doom", "loom", "freq", "current", "phase",
         "images", 
         "appears", "phase", "conductor", "wavez", 
         "normal", "free", "termed",
         "provide", "for", "and", "struct", "about", "have",
         "proper",
         "involve",
         "describedly",
         "thats",
         "spaces",
         "circumstance",
         "the",
         "member",
         "such",
         "guide",
         "regard",
         "officers",
         "implement",
         "principalities",         
         0};
      char *search_strings[] = {"cabbie", "your", "It isn't here",
                                "But it is here", "hodad", "yoohoo", "yoohoo",
                                "yoohoo", "yoohoo", "yoohoo", "xx", "x", ".", 
    "In recent years, the field of photonic ",
    "crystals has found new",
    "applications in the RF and microwave",
    "regime. A new type of metallic",
    "electromagnetic crystal has been", 
    "developed that is having a",
    "significant impact on the field of", 
    "antennas. It consists of a",
    "conductive surface, covered with a",
    "special texture which alters its",
    "electromagnetic properties. Made of solid",
    "metal, the structure",
    "conducts DC currents, but over a",
    "particular frequency range it does",
    "not conduct AC currents. It does not",
    "reverse the phase of reflected",
    "waves, and the effective image currents",

    "appear in-phase, rather than",
    "out-of-phase as they are on normal",
    "conductors. Furthermore, surface",
    "waves do not propagate, and instead",
    "radiate efficiently into free",
    "space. This new material, termed a",
    "high-impedance surface, provides",
    "a useful new ground plane for novel",
    "low-profile antennas and other",
    "electromagnetic structures.",
    "The recent protests about the Michigamua",
    "student organization have raised an",
    "important question as to the proper nature",
    "and scope of University involvement",
    "with student organizations. Accordingly",
    "the panel described in my Statement of",
    "February 25, 2000 that is considering the",
    "question of privileged space also will",
    "consider under what circumstances and in", 
    "what ways the University, its",
    "administrators and faculty members should",
    "be associated with such organizations",
    "and it will recommend guiding principles",
    "in this regard. The University's",
    "Executive Officers and I will then decide",
    "whether and how to implement such",
    "principles."          
 };
      int i;

      for (i = 0; find_strings[i]; i++)
      {
            init_search( find_strings [i]);
            here = strsearch(search_strings[i]);
            printf("\"%s\" is%s in \"%s\"", find_strings[i], here ? "" : " not", search_strings[i]);
            if (here) printf(" [\"%s\"]", here);
            putchar('\n');
      }

      return 0;
}

int
strncmp2 ( char * first, char * second, int length)
{
    int i;
    
    //printf ( "(Debugging...) INSIDE strncmp2!!!\n");
    
    for ( i = 0; i < length; i ++) {
 if ( first [ i] != second [ i]) return ( (int) first[i]-second[i]);
    }
    
    return ( 0);
}

int
strlen2 ( char * str)
{
    char * aux = str;
    int tam;
    
    for ( tam = 0; * aux ++; ) tam ++;
    
    return ( tam);
}

memcpy2 ( char * dest, char * org, int size)
{
    int i;
    
    // printf ( "size = %d\n", size);
    
    for ( i = 0; i < size; i ++) * dest ++ = * org ++;
}

/*
**  Call this with the string to locate to initialize the table
*/
void init_search(const char *string)
{
    int i;

    len = strlen2(string);
    for (i = 0; i <= 255; i++) {                     /* rdg 10/93 */
	table[i] = len;
    }

    for (i = 0; i < len; i++) {
	table[(unsigned char)string[i]] = len - i - 1;
    }

    findme = (char *)string;
}

/*
**  Call this with a buffer to search
*/
char *strsearch(const char *string)
{
      int shift;
      int pos = len - 1;
      char *here;
      int limit=strlen2(string);

      while (pos < limit)
      {
            while( pos < limit &&
                  (shift = table[(unsigned char)string[pos]]) > 0)
            {
                    pos += shift;
            }
            if (0 == shift)
            {
                  if (0 == strncmp2(findme, here = (char *)&string[pos-len+1], len))
                  {
   results [0] = here;
                        return(here);
                  }
                  else  pos++;
            }
      }
      return 0;
}

// #include <stdio.h>
