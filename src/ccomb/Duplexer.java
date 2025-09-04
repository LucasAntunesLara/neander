package ccomb;

import ports.InControl;
import ports.OutControl;
import ports.SetPort;
import bus.Bus;
import bus.SetBus;

public class Duplexer extends Combinational {

	public Duplexer ( 	String parName, String parE1name, int parSize,
						String parS1name, String parS2name) {
		sbName = new StringBuffer ( ).append (parName);

		spIn = new SetPort ( );
		spOut = new SetPort ( );
		sbBus = new SetBus ( );
		
		peE1 = new InControl ( parE1name, parSize);
		spIn.add ( peE1);
		psS1 = new OutControl ( parS1name, parSize);
		spOut.add ( psS1);
		psS2 = new OutControl ( parS2name, parSize);
		spOut.add ( psS2);
		bBs1 = new Bus ( this, psS1, null, null);
		sbBus.add ( bBs1);
		bBs2 = new Bus ( this, psS2, null, null);
		sbBus.add ( bBs2);
	}

	public void behavior ( ) {
		long lE1t, lS1t, lS2t;

		lE1t = peE1.getDoubleWord ( );
		lS1t = lE1t;
		lS2t = lE1t;

		psS1.set ( lS1t);
		psS2.set ( lS2t);

//System.out.println ( "Duplicando saida...");
		
		if ( bBs1.isLinked ( ) == true) bBs1.behavior ( );
		if ( bBs2.isLinked ( ) == true) bBs2.behavior ( );
	}

	public void debug ( ) {
		System.out.println ( "*** Duplexer.debug:...BEGIN");

		if ( spIn != null) spIn.debug ( );
		if ( spCtrl != null) spCtrl.debug ( );
		if ( spOut != null) spOut.debug ( );
		if ( spStt != null) spStt.debug ( );
		if ( sbBus != null) sbBus.debug ( );
		
		System.out.println ( "*** Duplexer.debug:...END");
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
		
		System.out.println ( "\n** FIM **");
		System.out.println ( );
	}

	public static void help ( ) {
		System.out.println ( "Duplexer: duplica um barramento");
		System.out.println ( "	Possui uma entrada E1 e duas saidas S1 e S2");
		System.out.println ( "	Copia o valor de entrada nas duas saidas");
		System.out.println ( "	Para cria-lo: create <nome> 11 0 <bits>");
		System.out.println ( );
	}

	public static void test ( ) {
		Duplexer dpx = new Duplexer ( "teste", "e1", HALFWORD, "s1", "s2");
		
		dpx.set ( "e1", IN, 15);
		dpx.behavior ( );
		dpx.debug ( );
	}

	protected InControl peE1;
	protected OutControl psS1, psS2;
	protected Bus bBs1, bBs2;

}
