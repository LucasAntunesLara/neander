package ccomb;

import ports.Control;
import ports.InControl;
import ports.OutControl;
import ports.SetPort;
import bus.Bus;
import bus.SetBus;

public class GenericMultiplexer extends Combinational {

	public GenericMultiplexer ( 	String parName, int parEntries, int parSize,
								String parSelname, String parS1name) {
		int i;
		
		sbName = new StringBuffer ( ).append (parName);

		spIn = new SetPort ( );
		spOut = new SetPort ( );
		spCtrl = new SetPort ( );
		sbBus = new SetBus ( );
		
		iEntries = parEntries;
		for ( i = 0; i < parEntries; i ++) {
			peE = new InControl ( "E"+i, parSize);
			spIn.add ( peE);
		}
		pcSel = new Control ( parSelname, BIT);
		spCtrl.add ( pcSel);
		psS = new OutControl ( parS1name, parSize);
		spOut.add ( psS);
		bBs = new Bus ( this, psS, null, null);
		sbBus.add ( bBs);
	}

	public void behavior ( ) {
		long lEt = 0, lSelt, lSt;
		int i;

		lSelt = pcSel.getDoubleWord ( );

		for ( i = 0; i < spIn.getNelems ( ); i ++) {
			peE = ( InControl) spIn.traverse ( i);
			if ( lSelt == i) {
				lEt = peE.getDoubleWord ( );
				break;
			}
		}

		lSt = lEt;

		psS.set ( lSt);
		
		if ( bBs.isLinked ( ) == true) bBs.behavior ( );
	}

	public void debug ( ) {
		System.out.println ( "*** MultiplexerNx1.debug:...BEGIN");

		if ( spIn != null) spIn.debug ( );
		if ( spCtrl != null) spCtrl.debug ( );
		if ( spOut != null) spOut.debug ( );
		if ( spStt != null) spStt.debug ( );
		if ( sbBus != null) sbBus.debug ( );
		
		System.out.println ( "*** MultiplexerNx1.debug:...END");
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
		System.out.println ( "	Possui varias entradas E0...EN-1 e uma saida S1");
		System.out.println ( "	Possui um controle SEL");
		System.out.println ( "		SEL - 	seleciona qual valor de entrada...");
		System.out.println ( "			estara na saida do multiplexador...");
		System.out.println ( "			0 - E0; 1 - E1; ...; N-1 - EN-1");
		System.out.println ( "	Copia uma das entradas para a saida");
		System.out.println ( "	Para cria-lo: create <nome> 17 <Nentradas> <bits> ");
		System.out.println ( );
	}

	public static void test ( ) {
		GenericMultiplexer mnx1 = new GenericMultiplexer ( "MNx1", 2, HALFWORD, "SEL", "S1");

		mnx1.set ( "E0", IN, 15);
		mnx1.set ( "E1", IN, 255);
//		mnx1.set ( "E2", IN, 4095);
//		mnx1.set ( "E3", IN, 65535);
		mnx1.set ( "SEL", CONTROL, 0);
		mnx1.behavior ( );
		mnx1.list ( );
		mnx1.set ( "SEL", CONTROL, 1);
		mnx1.behavior ( );
		mnx1.list ( );
		mnx1.set ( "SEL", CONTROL, 10);
		mnx1.behavior ( );
		mnx1.list ( );
	}

	protected InControl peE;
	protected Control pcSel;
	protected OutControl psS;
	protected Bus bBs;
	private int iEntries = 0;
}
