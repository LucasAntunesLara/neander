package cseq;

import ports.InControl;
import ports.OutControl;
import ports.SetPort;
import bus.Bus;
import bus.SetBus;
import contents.Contents;

public class GenericPipeRegister extends Sequential {

	public GenericPipeRegister ( 	String parName, int parEntSai) {
		int i;
		sbName = new StringBuffer ( ).append (parName);

		spIn = new SetPort ( );
		spOut = new SetPort ( );
		sbBus = new SetBus ( );
		
		iEntSai = parEntSai;
		peAux = new InControl [ iEntSai];
		psAux = new OutControl [ iEntSai];
		bBsAux = new Bus [ iEntSai];
		
		
		for ( i = 0; i < iEntSai; i ++) {
			peAux [i] = new InControl ( "E"+i, DOUBLEWORD);
			spIn.add ( peAux [i]);			
		}
		
		for ( i = 0; i < iEntSai; i ++) {
			psAux [i] = new OutControl ( "S"+i, DOUBLEWORD);
			spOut.add ( psAux [i]);			
		}
		
		for ( i = 0; i < iEntSai; i ++) {
			bBsAux[i] = new Bus ( this, psAux[i], null, null);
			sbBus.add ( bBsAux[i]);
		}
		
		cConteudo = new Contents ( iEntSai, DOUBLEWORD, INTEGER);
	}

	public void write ( ) {
		long lEt;
		int i;
		
		for ( i = 0; i < iEntSai; i ++) {
			lEt = peAux[i].getDoubleWord();
			cConteudo.set ( lEt, i);		
		}
	}
	
	public void read ( ) {
		long lSt;
		int i;
		
		for ( i = 0; i < iEntSai; i ++) {
			lSt = cConteudo.getDoubleWord ( i);
			psAux[i].set ( lSt);	
		
			if ( bBsAux[i].isLinked ( ) == true) bBsAux[i].behavior ( );
		}
	}

	public void debug ( ) {
		System.out.println ( "*** PipeRegister.debug:...BEGIN");

		if ( spIn != null) spIn.debug ( );
		if ( spCtrl != null) spCtrl.debug ( );
		if ( spOut != null) spOut.debug ( );
		if ( spStt != null) spStt.debug ( );
		if ( sbBus != null) sbBus.debug ( );
		if ( cConteudo != null) cConteudo.debug ( );

		System.out.println ( "*** PipeRegister.debug:...END");
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
		System.out.println ( "PipeRegister: e um registrador de pipeline");
		System.out.println ( "	Possui as entrada E0 a En-1...");
		System.out.println ( "	E as saidas S0 a Sn-1");
		System.out.println ( "	Armazena o conteudo das entradas...");
		System.out.println ( "	E copia para a saida respectiva...");
		System.out.println ( "	E0 p/ S0, E1 p/ S1, etc");
		System.out.println ( "	Para cria-lo: create <nome> 112 <nroEntradas>");
		System.out.println ( );
	}

	public static void test ( ) {
		GenericPipeRegister preg = new GenericPipeRegister ( 	"tst", 5);
		
		preg.set ( "E0", IN, 15);
		preg.set ( "E1", IN, 16);
		preg.set ( "E2", IN, 17);
		preg.set ( "E3", IN, 18);
		preg.set ( "E4", IN, 19);
		preg.write ( );
		preg.read ( );
		preg.list ( );
	}

	public InControl [] peAux; 	//peE1, peE2, peE3, peE4;
	public OutControl [] psAux;	//psS1, psS2, psS3, psS4;
	public Bus [] bBsAux;			//bBs1, bBs2, bBs3, bBs4;
	private int iEntSai;			//ipS1t, ipS2t, ipS3t, ipS4t;

}
