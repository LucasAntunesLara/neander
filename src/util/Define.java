package util;

import java.awt.Color;

public interface Define {

	// Comentar, descomentar para recompilar todo o projeto
	public final int TEMPORARIO 	= -1;

	public final int BIT		= 1;
	public final int NIBBLE		= 4;
	public final int BYTE		= 8;
	public final int HALFWORD	= 16;
	public final int WORD		= 32;
	public final int DOUBLEWORD	= 64;
	public final int BITSLEFT16	= -16;
	public final int BITSRIGHT16= -17;
	
	public final int IN 			= 0;
	public final int OUT 			= 3;
	public final int CONTROL 		= 1;
	public final int STATUSorCONF 	= 2;
	public final int FIELD	 		= 4;
	public final int STRING	 		= 5;
	
	public final int INTEGER	= 0;
	public final int FP			= 1;

	public final int BEHAVIOR 	= 0;
	public final int READ 		= 1;
	public final int WRITE 		= 2;
	public final int PROPAGATE 	= 3;
	public final int SET 		= 4;
	public final int GET 		= 5;
	public final int BUSBEHAVIOR= 6;
	
	public final int ADD		= 10;
	public final int SUB		= 11;
	public final int AND		= 12;
	public final int OR			= 13;
	public final int XOR		= 14;
	public final int NOT_E1		= 15;
	public final int NOT_E2		= 16;
	public final int E1			= 17;
	public final int E2			= 18;
	public final int E21632		= 19;
	public final int INC_E1		= 20;
	public final int INC_E2		= 21;
	public final int NOR		= 22;
	public final int SLL		= 23; // DESLOCAMENTO LOGICO A ESQUERDA
	public final int SLR		= 24; // DESLOCAMENTO LOGICO A DIREITA
	public final int SAR		= 25; // DESLOCAMENTO ARITMETICO A DIREITA
	public final int MUL		= 26;
	public final int DIV		= 27;
	public final int SUBE2E1	= 28;
	public final int MAC		= 29;	/* Modif.MAC */
	public final int SLT		= 30;	/* set on less than */
	public final int REM		= 31;	/* set on less than */
	public final int LESSTHAN	= 600;
	public final int EQUAL		= 601;
	public final int GREATERTHEN_OR_EQUAL_ZERO 	= 602;
	public final int LESSTHEN_OR_EQUAL_ZERO 	= 603;
	public final int LESSTHEN_ZERO				= 604;
	public final int NOTEQUAL					= 605;
	public final int GREATERTHEN_ZERO 			= 606;

	public final int MSGDATAPATH= 1001;

	final static int MAXTOKENS = 10;
	
	public final int ITYPE 		= 100;
	public final int RTYPE 		= 101;
	public final int JTYPE	 	= 102;
	public final int CMTYPE	 	= 103;
	public final int BUBBLE	 	= -103;
	
	public final int MONOCYCLE 		= 200;
	public final int MULTICYCLE 	= 201;
	public final int PIPELINED		= 202;
	public final int NONPIPELINED	= 203;	
	
	public final int IP			= 0;	// Inteiros positivos
	public final int SM			= 1;	// Sinal Magnitude
	public final int C2			= 2;	// Complemento de 2
	
	public final int COLUNAS	= 55;

	public final int BINARY		= 300;
	public final int DECIMAL	= 301;
	public final int HEXADECIMAL= 302;
	
	public final Color colorACTIVE 		= 	Color.ORANGE;
	public final Color colorINACTIVE 	= 	Color.WHITE;
	public final Color colorTEXT 		= 	Color.BLACK;	
	public final Color colorBEHAVIOR 	= 	Color.YELLOW;
	public final Color colorWRITE 		= 	Color.RED;
	public final Color colorREAD 		= 	Color.ORANGE;	

	public final int BEFORE	= 0;
	public final int AFTER	= 1;
	
	public final int ENGLISH	= 0;
	public final int PORTUGUESE	= 1;
	
	public final String sPathSeparator = null;
	public final char cPathSeparator = '\0';
	
	public final int INVALID_PC	= -111;
	public final int FETCH		= -2;
	
	public final int ISNORMAL		= 0;
	public final int ISnotINtheSET	= -1;
	public final int ISinTheSET		= -2;
	public final int ISOverflow		= -3;
	public final int ISJumps		= -4;
	public final int ISSmallBlock	= -5;
	public final int ISRegAccess	= -6;
}
