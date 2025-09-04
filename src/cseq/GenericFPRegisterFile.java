package cseq;

import ports.Control;
import ports.InControl;
import ports.OutControl;
import ports.SetPort;
import bus.Bus;
import bus.SetBus;
import contents.Contents;

public class GenericFPRegisterFile extends Sequential {

	public GenericFPRegisterFile ( 	int parNregs, String parName, int parEntries, 
									int parSize, int parOuts) {
		sbName = new StringBuffer ( ).append (parName);

		spIn = new SetPort ( );
		spOut = new SetPort ( );
		spCtrl = new SetPort ( );
		sbBus = new SetBus ( );

		int i;
		
		for ( i = 0; i < parEntries; i ++) {
			peE = new InControl ( "E"+i, parSize, FP);
			spIn.add ( peE);
			pcC = new Control ( "NW" + i, BYTE); 
			spCtrl.add ( pcC);
		}
		for ( i = 0; i < parOuts; i ++) {
			psS = new OutControl ( "S"+i, parSize, FP);
			spOut.add ( psS);
			pcC = new Control ( "NR" + i, BYTE); 
			spCtrl.add ( pcC);
			bBs = new Bus ( this, psS, null, null);
			sbBus.add ( bBs);
		}

		cConteudo = new Contents ( parNregs, parSize, FP);
		iNregs = parNregs;
		
		for ( i = 0; i < parNregs; i ++) {
			cConteudo.set ( 0, i);
		}
		
		iEntries = parEntries;
		iOuts = parOuts;
	}

	private void resetAfterWrite ( ) {
		int i;
		
		for ( i = 0; i < iEntries; i ++) {
			pcC = ( Control) spCtrl.traverse ( i);
			pcC.set( - 1);
		}		
	}
	
	public void write ( ) {
		int i;
		int iNwt;
		double dEt;
		
		for ( i = 0; i < iEntries; i ++) {
			pcC = ( Control) spCtrl.traverse ( i);
			//pcC.list ( );
			peE = ( InControl) spIn.traverse( i);
			//peE.list ( );
			iNwt = pcC.getWord ( );
			dEt = peE.getDouble ( );
			
			//System.out.println ( "d="+dEt);
		
			if ( iNwt != 0 && iNwt != -1) cConteudo.set ( dEt, iNwt);
		}
		
		resetAfterWrite ( );
	}

	private void resetAfterRead ( ) {
		int i;
		
		for ( i = 0; i < iOuts; i ++) {
			pcC = ( Control) spCtrl.traverse ( i+iEntries);
			pcC.set( - 1);
		}		
	}
	
	public void read ( ) {
		int i;
		int iNrt;
		long lSt;
		
		for ( i = 0; i < iOuts; i ++) {
			pcC = ( Control) spCtrl.traverse ( i+iEntries);
			psS = ( OutControl) spOut.traverse( i);
			bBs = ( Bus)sbBus.traverse( i);
			iNrt = pcC.getWord ( );
			if ( iNrt != -1) {
				lSt = cConteudo.getDoubleWord ( iNrt);
				psS.set ( lSt);
				if ( bBs.isLinked ( ) == true) bBs.behavior ( );
			}
		}
		
		resetAfterRead ( );
	}
	
	public void behavior ( ) {
		this.write ( );
		this.read ( );		
	}

	public void debug ( ) {
		int i;

		System.out.println ( "*** GenericRegisterFile.debug:...BEGIN");

		if ( spIn != null) spIn.debug ( );
		if ( spCtrl != null) spCtrl.debug ( );
		if ( spOut != null) spOut.debug ( );
		if ( spStt != null) spStt.debug ( );
		if ( sbBus != null) sbBus.debug ( );
		if ( cConteudo != null) cConteudo.debug ( );
		
		System.out.println ( "*** GenericRegisterFile.debug:...END");
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
		System.out.println ( "GenericFPRegisterFile: e um banco de registradores com valores em ponto flutuante");
		System.out.println ( "	Possui M entradas (E0..EM-1) e N saidas (S0..SN-1)");
		System.out.println ( "	Possui os respectivos controles NR0..NRM e NW0..NWN");
		System.out.println ( "		NRM - nro. do registrador a ser lido");
		System.out.println ( "		NWN - nro. do registrador a ser escrito");
		System.out.println ( "	Armazena o conteudo da entrada EM no registrador reg[NWN]");
		System.out.println ( "	Ou le o conteudo do registrador reg[NRM]");
		System.out.println ( "	Para cria-lo: create <nome> 53 <nregs> <bits> <Nentradas> <Nsaidas>");
		System.out.println ( "		nregs - nro. de registradores no banco");
		System.out.println ( );
	}

	public static void test ( ) {
		GenericFPRegisterFile rf = new GenericFPRegisterFile ( 32, "tst", 2, DOUBLEWORD, 2);
		double d = 5.678;
		
		rf.set ( "e1", IN, Double.doubleToLongBits(d));
		rf.set ( "nr1", CONTROL, 5);
		rf.set ( "nw1", CONTROL, 5);
		rf.write ( );
		rf.read ( );
		rf.debug ( );
	}

	protected InControl peE;
	protected Control pcC;
	protected OutControl psS;
	protected Bus bBs;
	protected int iNregs;
	protected int iEntries, iOuts;
}
