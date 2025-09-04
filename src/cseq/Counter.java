package cseq;

import ports.Control;
import ports.SetPort;

public class Counter extends Register {

	public Counter ( 	String parName, String parE1name, int parSize, 
						String parIncname, String parS1name) {
		super ( parName, parE1name, parSize, parS1name);

		spCtrl = new SetPort ( );
		
		pcInc = new Control ( parIncname, BIT);
		pcInc.set ( 1L);
		spCtrl.add ( pcInc);
	}

	public void write ( ) {
		long lE1t, lInct, lCt;

		lInct = pcInc.getDoubleWord ( );

		if ( lInct == 0L) {
			lE1t = peE1.getDoubleWord ( );
			cConteudo.set ( lE1t, 0);
		} else {
			lCt = cConteudo.getDoubleWord ( 0);
			cConteudo.set ( ++ lCt, 0);
		}
	}

	public void debug ( ) {
		System.out.println ( "*** counter.debug:...BEGIN");

		if ( spIn != null) spIn.debug ( );
		if ( spCtrl != null) spCtrl.debug ( );
		if ( spOut != null) spOut.debug ( );
		if ( spStt != null) spStt.debug ( );
		if ( sbBus != null) sbBus.debug ( );
		if ( cConteudo != null) cConteudo.debug ( );
		
		System.out.println ( "*** counter.debug:...END");
		System.out.println ( );
	}

	public static void help ( ) {
		System.out.println ( "Counter: e um registrador contador");
		System.out.println ( "	Possui uma entrada E1, uma saida S1");
		System.out.println ( "	E um controle INC");
		System.out.println ( "	Se o controle INC esta' habilitado (default)...");
		System.out.println ( "		incrementa o valor do conteudo");	
		System.out.println ( "	Caso contrario, ");
		System.out.println ( "		armazena o conteudo da entrada E1");	
		System.out.println ( "	Para cria-lo: create <nome> 16 0 <bits>");
		System.out.println ( );
	}

	public static void test ( ) {
		Counter count = new Counter ( "countertst", "e1", HALFWORD, "inc", "s1");
		
		count.set ( "e1", IN, 1);
		count.write ( );
		count.read ( );
		count.debug ( );
	}

	private Control pcInc;
}
