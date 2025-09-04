package ccomb;

import ports.InControl;
import ports.OutControl;
import ports.SetPort;
import bus.Bus;
import bus.SetBus;

public class ZeroTest extends Combinational {

	public ZeroTest ( 	String parName, String parE1name, int parE1size,
						String parS1name, int parS1size) {
		sbName = new StringBuffer ( ).append (parName);

		spIn = new SetPort ( );
		spOut = new SetPort ( );
		sbBus = new SetBus ( );
		
		peE1 = new InControl ( parE1name, parE1size);
		spIn.add ( peE1);
		psS1 = new OutControl ( parS1name, parS1size);
		spOut.add ( psS1);
		bBs1 = new Bus ( this, psS1, null, null);
		sbBus.add ( bBs1);
	}

	public void behavior ( ) {
		long lE1t, lS1t;

		//bBs1.debug ( );
		
		lE1t = peE1.getDoubleWord ( );
		if ( lE1t == 0L) lS1t = 1L;
		else lS1t = 0L;

		psS1.set ( lS1t);
		
		if ( bBs1.isLinked ( ) == true) bBs1.behavior ( );
	}

	public void debug ( ) {
		System.out.println ( "*** ZeroTest.debug:...BEGIN");

		if ( spIn != null) spIn.debug ( );
		if ( spCtrl != null) spCtrl.debug ( );
		if ( spOut != null) spOut.debug ( );
		if ( spStt != null) spStt.debug ( );
		if ( sbBus != null) sbBus.debug ( );

		System.out.println ( "*** ZeroTest.debug:...END");
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
		System.out.println ( "ZeroTest: testa se a entrada e 0");
		System.out.println ( "	Possui uma entrada E1 e uma saida S1");
		System.out.println ( "	Se E1 e 0, a saida e 1 (true)...");
		System.out.println ( "	Caso contrario, a saida e 0 (false)");
		System.out.println ( "	Para cria-lo: create <nome> 10 0 <bits>");
		System.out.println ( );
	}

	public static void test ( ) {
		ZeroTest zt = new ZeroTest ( "teste", "e1", DOUBLEWORD, "s1", BIT);
		
		zt.set ( "e1", IN, 0);
		zt.behavior ( );
		zt.debug ( );
	}

	private InControl peE1;
	private OutControl psS1;
	private Bus bBs1;
}
