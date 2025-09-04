package ccomb;

import ports.Control;
import ports.InControl;
import ports.OutControl;
import ports.SetPort;
import bus.Bus;
import bus.SetBus;

public class Multiplexer extends Combinational {

	public Multiplexer ( 	String parName, String parE1name, int parSize, 
							String parE2name, String parSelname, String parS1name) {
		sbName = new StringBuffer ( ).append (parName);

		spIn = new SetPort ( );
		spOut = new SetPort ( );
		spCtrl = new SetPort ( );
		sbBus = new SetBus ( );
		
		peE1 = new InControl ( parE1name, parSize);
		peE2 = new InControl ( parE2name, parSize);
		spIn.add ( peE1);
		spIn.add ( peE2);
		pcSel = new Control ( parSelname, BIT);
		spCtrl.add ( pcSel);
		psS1 = new OutControl ( parS1name, parSize);
		spOut.add ( psS1);
		bBs1 = new Bus ( this, psS1, null, null);
		sbBus.add ( bBs1);
	}

	public void behavior ( ) {
		long lE1t, lE2t, lSelt, lS1t;

		lE1t = peE1.getDoubleWord ( );
		lE2t = peE2.getDoubleWord ( );
		lSelt = pcSel.getDoubleWord ( );

		if ( lSelt == 0L) lS1t = lE1t;
		else lS1t = lE2t;

		psS1.set ( lS1t);
		
		if ( bBs1.isLinked ( ) == true) bBs1.behavior ( );
	}

	public void debug ( ) {
		System.out.println ( "*** Multiplexer.debug:...BEGIN");

		if ( spIn != null) spIn.debug ( );
		if ( spCtrl != null) spCtrl.debug ( );
		if ( spOut != null) spOut.debug ( );
		if ( spStt != null) spStt.debug ( );
		if ( sbBus != null) sbBus.debug ( );
		
		System.out.println ( "*** Multiplexer.debug:...END");
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
		System.out.println ( "Multiplexer: e um multiplexador");
		System.out.println ( "	Possui duas entradas E1 e E2 e uma saida S1");
		System.out.println ( "	Possui um controle SEL");
		System.out.println ( "		SEL - 	seleciona qual valor de entrada...");
		System.out.println ( "			estara na saida do multiplexador...");
		System.out.println ( "			0 - E1; 1 - E2");
		System.out.println ( "	Copia uma das entradas para a saida");
		System.out.println ( "	Para cria-lo: create <nome> 6 0 <bits>");
		System.out.println ( );
	}

	public static void test ( ) {
		Multiplexer mpx = new Multiplexer ( "teste", "e1", BYTE, "e2", "sel", "s1");
		
		mpx.set ( "e1", IN, 15);
		mpx.set ( "e2", IN, 255);
		mpx.set ( "sel", CONTROL, 1);
		mpx.behavior ( );
		mpx.debug ( );
	}

	private InControl peE1, peE2;
	private Control pcSel;
	private OutControl psS1;
	private Bus bBs1;
}
