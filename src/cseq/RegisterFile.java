package cseq;

import ports.Control;
import ports.InControl;
import ports.OutControl;
import ports.SetPort;
import util.SistNum;
import bus.Bus;
import bus.SetBus;
import contents.Contents;

public class RegisterFile extends Sequential {

	public RegisterFile ( 	int parNregs, String parName, String parE1name, 
							int parSize, String parNr1name, String parNw1name, 
							String parS1name) {
		sbName = new StringBuffer ( ).append (parName);

		spIn = new SetPort ( );
		spOut = new SetPort ( );
		spCtrl = new SetPort ( );
		sbBus = new SetBus ( );
		
		peE1 = new InControl ( parE1name, parSize);
		spIn.add ( peE1);
		
		pcNr1 = new Control ( parNr1name, BYTE);
		spCtrl.add ( pcNr1);
		pcNw1 = new Control ( parNw1name, BYTE);
		spCtrl.add ( pcNw1);

		psS1 = new OutControl ( parS1name, parSize);
		spOut.add ( psS1);
		
		bBs1 = new Bus ( this, psS1, null, null);
		sbBus.add ( bBs1);

		cConteudo = new Contents ( parNregs, parSize, INTEGER);
		iNregs = parNregs;
		
		int i;
		for ( i = 0; i < parNregs; i ++) {
			cConteudo.set ( 0, i);
		}
	}

	protected void write1st ( ) {
		int iNw1t, iWriteControl;
		long lE1t, lAux;
		
		iNw1t = pcNw1.getWord ( );
		iWriteControl = pcWriteControl.getWord ( );
		lE1t = peE1.getDoubleWord ( );
		
		if ( iWriteControl == BITSLEFT16) {
//System.out.println ( "Escrevendo na parte esquerda do registrador!");
			lAux = cConteudo.getWord ( iNw1t);
//System.out.println(SistNum.toHexString ( lAux, WORD));
			lAux = lAux & 0xFFFF0000;
//System.out.println(SistNum.toHexString ( lAux, WORD));
			lE1t = lE1t & 0x0000FFFF;
//System.out.println(SistNum.toHexString ( lE1t, WORD));
			lAux = lAux | lE1t;
//System.out.println(SistNum.toHexString ( lAux, WORD));
			if ( iNw1t != 0) cConteudo.set ( lAux, iNw1t);
		} else if ( iWriteControl == BITSRIGHT16) {
//System.out.println ( "Escrevendo na parte direita do registrador!");
			lAux = cConteudo.getWord ( iNw1t);
			lAux = lAux & 0x0000FFFF;
			lE1t = lE1t << 16;
			lAux = lAux | lE1t;
			if ( iNw1t != 0) cConteudo.set ( lAux, iNw1t);
		} else if ( iNw1t != 0) cConteudo.set ( lE1t, iNw1t);
	}
	
	public void write ( ) {
		this.write1st ( );
	}
	
	protected void propagate1st ( ) {
		int iNr1t;
		long lS1t;
		
		iNr1t = pcNr1.getWord ( );
//if ( iNr1t >= 0 && iNr1t <16) System.out.println ( "##### Lendo REGISTRADORES de 0 a 15: "+iNr1t);
		lS1t = cConteudo.getDoubleWord ( iNr1t);
		psS1.set ( lS1t);
		
		if ( bBs1.isLinked ( ) == true) bBs1.behavior ( );
	}
	
	public void propagate ( ) {
		this.propagate1st ( );
	}
	
	public void behavior ( ) {
		this.write ( );
		this.propagate ( );		
	}

	protected long read1st ( ) {
		int iNr1t;
		long lS1t;
		
		iNr1t = pcNr1.getWord ( );
		lS1t = cConteudo.getDoubleWord ( iNr1t);

		return ( lS1t);
	}

	public void read ( ) {
		this.propagate1st ( );
	}

	public void debug ( ) {
		int i;

		System.out.println ( "*** RegisterFile.debug:...BEGIN");

		if ( spIn != null) spIn.debug ( );
		if ( spCtrl != null) spCtrl.debug ( );
		if ( spOut != null) spOut.debug ( );
		if ( spStt != null) spStt.debug ( );
		if ( sbBus != null) sbBus.debug ( );
		if ( cConteudo != null) cConteudo.debug ( );
		
		System.out.println ( "*** RegisterFile.debug:...END");
		System.out.println ( );
	}

	public void list ( ) {
		System.out.println ( "\n** INICIO **\n");

		System.out.println ( "Componente: " + sbName + "\n");

		if ( spIn != null) spIn.list ( );
		if ( spCtrl != null) spCtrl.list ( );
		if ( spOut != null) spOut.list ( );
		if ( spStt != null) spStt.list ( );
		if ( sbBus != null) sbBus.list ( );
		if ( cConteudo != null) cConteudo.list ( );
		
		System.out.println ( "\n** FIM **");
		System.out.println ( );
	}

	public static void help ( ) {
		System.out.println ( "RegisterFile: e um banco de registradores");
		System.out.println ( "	Possui uma entrada E1 e uma saida S1");
		System.out.println ( "	Possui os controles NR1 e NW1");
		System.out.println ( "		NR1 - nro. do registrador a ser lido");
		System.out.println ( "		NW1 - nro. do registrador a ser escrito");
		System.out.println ( "	Armazena o conteudo da entrada E1 no registrador reg[nw1]");
		System.out.println ( "	Ou le o conteudo do registrador reg[nr1]");
		System.out.println ( "	Para cria-lo: create <nome> 1 <nregs> <bits>");
		System.out.println ( "		nregs - nro. de registradores no banco");
		System.out.println ( );
	}

	public static void test ( ) {
		RegisterFile rf = new RegisterFile ( 10, "tst", "e1", BYTE, "nr1", "nw1", "s1");
		
		rf.set ( "e1", IN, 1);
		rf.set ( "nr1", CONTROL, 5);
		rf.set ( "nw1", CONTROL, 9);
		rf.write ( );
		rf.read ( );
		rf.debug ( );
	}

	protected InControl peE1;
	protected Control pcNr1, pcNw1, pcWriteControl;
	protected OutControl psS1;
	protected Bus bBs1;
	protected int iNregs;
}
