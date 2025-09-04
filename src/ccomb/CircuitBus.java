package ccomb;

import ports.InControl;
import ports.OutControl;
import ports.SetPort;
import bus.Bus;
import bus.SetBus;
import datapath.Datapath;

public class CircuitBus extends Combinational {

	public CircuitBus ( 	String parName, int parEntries, int parSize,
							int parOuts, Datapath parDtp) {
		int i;
		
		sbName = new StringBuffer ( ).append (parName);

		spIn = new SetPort ( );
		spOut = new SetPort ( );
		sbBus = new SetBus ( );
		
		for ( i = 0; i < parEntries; i ++) {
			peE = new InControl ( "E"+i, parSize);
			spIn.add ( peE);
		}
		for ( i = 0; i < parOuts; i ++) {
			psS = new OutControl ( "S"+i, parSize);
			spOut.add ( psS);
			bBs = new Bus ( this, psS, null, null);
			sbBus.add ( bBs);
		}
		
		dtp = parDtp;
	}

	public void behavior ( ) {
		long lEt, lSt;
		int i;
		boolean bExists = false;
// System.out.println ( dtp);		
		if ( dtp == null) return;
		
		for ( i = 0; i < spIn.getNelems ( ); i ++) {
			String sNameBus;
			Bus bBs;
			
			peE = ( InControl) spIn.traverse ( i);
			sNameBus = this.getName ( ) + "_" + peE.getName ( );
// System.out.println ( sNameBus);
			bBs = dtp.searchBusByDestination ( sNameBus);
			if ( bBs != null) {
				if ( bBs.isUsed ( )) {
// System.out.println ( bBs);
					bExists = true;
					break;
				}
			}
		}
		
		if ( ! bExists) return;

		lEt = peE.getDoubleWord ( );
		lSt = lEt;
// System.out.println ( lEt);		
		for ( i = 0; i < spOut.getNelems ( ); i ++) {
			psS = ( OutControl) spOut.traverse ( i);
			psS.set ( lSt);
		}

		for ( i = 0; i < sbBus.getNelems ( ); i ++) {
			bBs = ( Bus) sbBus.traverse ( i);
			if ( bBs.isLinked ( )) bBs.behavior ( );
		}
	}

	public void debug ( ) {
		System.out.println ( "*** CircuitBus.debug:...BEGIN");

		if ( spIn != null) spIn.debug ( );
		if ( spCtrl != null) spCtrl.debug ( );
		if ( spOut != null) spOut.debug ( );
		if ( spStt != null) spStt.debug ( );
		if ( sbBus != null) sbBus.debug ( );
		
		System.out.println ( "*** CircuitBus.debug:...END");
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
		System.out.println ( "CircuitBus: implementa um barramento");
		System.out.println ( "	Possui varias entradas E1...EN e varias saidas S1...SN");
		System.out.println ( "	Copia o valor de uma das entradas nas suas saidas");
		System.out.println ( "	Para cria-lo: create <nome> 15 0 <bits> <Nentradas> <Nsaidas>");
		System.out.println ( );
	}

	public static void test ( ) {
		CircuitBus cb = new CircuitBus ( "CircuitBus", 5, HALFWORD, 5, null);
		
		cb.debug ( );
		cb.behavior ( );
	}

	protected InControl peE;
	protected OutControl psS;
	protected Bus bBs;

	private Datapath dtp;
}
