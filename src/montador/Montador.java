package montador;

import java.util.Hashtable;
import util.Define;

public class Montador implements Define {

	public static void setArchitecture ( String mnem [ ], long intOp [ ], int intF [ ])
	{
		mnemonicos = mnem;
		intOpcodes = intOp;
		intFuncs = intF;
		iReferences = new int [ mnemonicos.length];
	}
	
	public static void setAssemblyCode ( Hashtable ht)
	{
//System.out.println(ht);
		htAssemblyCode = ht;
	}
	
	public static String getAssemblyCode ( int pc) {
		LineOfCode loc;
		String sTmp = null;
//System.out.println("searching.:" + pc);
		if ( pc == BUBBLE) return ( "bubble");
		if ( pc == FETCH) return ( "fetch");
		if ( htAssemblyCode == null) return null;
		else {
			loc = ( (LineOfCode) htAssemblyCode.get ( new Integer ( pc)));
			if ( loc != null) sTmp = loc.line;
			if ( sTmp == null) return ( "???");
			return (sTmp);
		}
	}
	
	// Valida para o MIPS Multi
	public static String getAssemblyCode ( int pc, int method) {
		LineOfCodeDLX loc;
		String sTmp = null;
//System.out.println("searching.:" + pc);
		if ( pc == BUBBLE) return ( "bubble");
		if ( pc == FETCH) return ( "fetch");
		if ( htAssemblyCode == null) return null;
		else {
			loc = ( (LineOfCodeDLX) htAssemblyCode.get ( new Integer ( pc)));
			if ( loc.methodID != method) return ( null);
			if ( loc != null) sTmp = loc.line;
			if ( sTmp == null) return ( "???");
			return (sTmp);
		}
	}
	
	// Valida para o MIPS Multi
	public static String getMethodName ( int pc) {
		LineOfCodeDLX loc;
		String sTmp = null;
//System.out.println("searching.:" + pc);
		if ( pc == BUBBLE) return ( "bubble");
		if ( pc == FETCH) return ( "fetch");
		if ( htAssemblyCode == null) return null;
		else {
			loc = ( (LineOfCodeDLX) htAssemblyCode.get ( new Integer ( pc)));
			if ( loc != null) sTmp = loc.methodName;
			if ( sTmp == null) return ( "???");
			return (sTmp);
		}
	}
	
	public static void settReferencesByMnemonico (int pos, int nrefs) {
		LineOfCode loc;
		int i;

		loc = ( (LineOfCode) htAssemblyCode.get ( new Integer ( pos)));
		for ( i = 0; i < mnemonicos.length; i ++) {
			if ( intOpcodes [ i] == loc.opcode && intFuncs [ i] == loc.func) break;
		}
		
		iReferences [i] += nrefs;
	}

	public static String getReferencesByMnemonico ( ) {
		int i, maior = -1, pos = - 1;
		String sTmp;
		//LineOfCode loc;

		for ( i = 0; i < mnemonicos.length; i ++) {
			if ( iReferences [i] != - 1) {
				if ( iReferences [i] > maior) {
					maior = iReferences [ i];
					pos = i;
				}
			}
		}

		sTmp = pos + " , " + mnemonicos [ pos] + " = " + iReferences [ pos] + " references"; 
		
		iReferences [ pos] = - 1;
		
		return sTmp;
	}

	static Hashtable htAssemblyCode = null;
	static String mnemonicos [ ]; 
	static long intOpcodes [ ]; 
	static int intFuncs [ ];
	static int iReferences [ ];
}
