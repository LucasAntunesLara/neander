package cseq;

import ports.Control;
import ports.OutControl;
import bus.Bus;

public class RegisterFile1e2s extends RegisterFile {

	public RegisterFile1e2s ( 	int parNregs, String parName, String parE1name, 
								int parSize, String parNr1name, String parNr2name, 
								String parNw1name, String parS1name, String pars2name) {
		super ( parNregs, parName, parE1name, parSize, parNr1name, parNw1name, parS1name);

		pcNr2 = new Control ( parNr2name, BYTE);
		spCtrl.add ( pcNr2);

		psS2 = new OutControl ( pars2name, parSize);
		spOut.add ( psS2);
		
		pcWriteControl = new Control ( "RFWrCtrl", NIBBLE);
		spCtrl.add ( pcWriteControl);
		
		bBs2 = new Bus ( this, psS2, null, null);
		sbBus.add ( bBs2);
	}

	private void propagate2nd ( ) {
		int iNr2t;
		long lS2t;

		iNr2t = pcNr2.getWord ( );
		lS2t = cConteudo.getDoubleWord ( iNr2t);
		psS2.set ( lS2t);
		
		if ( bBs2.isLinked ( ) == true) bBs2.behavior ( );
	}
	
	public void propagate ( ) {
		this.propagate1st ( );
		this.propagate2nd ( );
	}
	
	private long read2nd ( ) {
		int iNr2t;
		long lS2t;

		iNr2t = pcNr2.getWord ( );
//if ( iNr2t >= 0 && iNr2t <16) System.out.println ( "##### Lendo REGISTRADORES de 0 a 15: "+iNr2t);
		lS2t = cConteudo.getDoubleWord ( iNr2t);
		
		return ( lS2t);
	}
		
	public void read ( ) {
		this.propagate1st ( );
		this.propagate2nd ( );
	}

	public void debug ( ) {
		int i;

		System.out.println ( "*** RegisterFile1e2s.debug:...BEGIN");
		if ( spIn != null) spIn.debug ( );
		if ( spCtrl != null) spCtrl.debug ( );
		if ( spOut != null) spOut.debug ( );
		if ( spStt != null) spStt.debug ( );
		if ( sbBus != null) sbBus.debug ( );
		if ( cConteudo != null) cConteudo.debug ( );
		System.out.println ( "*** RegisterFile1e2s.debug:...END");
		System.out.println ( );
	}

	public static void help ( ) {
		System.out.println ( "RegisterFile1e2s: e um banco de registradores");
		System.out.println ( "	Possui uma entrada E1 e duas saidas S1 e S2");
		System.out.println ( "	Possui os controles NR1, NR2 e NW1");
		System.out.println ( "		NR1 - nro. do 1' registrador a ser lido");
		System.out.println ( "		NR2 - nro. do 2' registrador a ser lido");
		System.out.println ( "		NW1 - nro. do registrador a ser escrito");
		System.out.println ( "	Armazena o conteudo da entrada E1 no registrador reg[nw1]");
		System.out.println ( "	Ou le o conteudo dos registradores reg[nr1] e reg[nr2]");
		System.out.println ( "	Para cria-lo: create <nome> 2 <nregs> <bits>");
		System.out.println ( "		nregs - nro. de registradores no banco");
		System.out.println ( );
	}

	public static void test ( ) {
		RegisterFile1e2s rf = new RegisterFile1e2s ( 	10, "tst", "e1", WORD, 
														"nr1", "nr2", "nw1", "s1", "s2");
		
		rf.set ( "e1", IN, 1);
		rf.set ( "nr1", CONTROL, 5);
		rf.set ( "nr2", CONTROL, 9);
		rf.set ( "nw1", CONTROL, 9);
		rf.write ( );
		rf.read ( );
		rf.debug ( );
	}

	private Control pcNr2;
	private OutControl psS2;
	private Bus bBs2;
}
