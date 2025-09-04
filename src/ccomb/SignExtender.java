package ccomb;

import ports.InControl;
import ports.OutControl;
import ports.SetPort;
import util.SistNum;
import bus.Bus;
import bus.SetBus;

public class SignExtender extends Combinational {

	public SignExtender ( 	String parName, int parSizein, int parSizeout) {
		sbName = new StringBuffer ( ).append (parName);

		spIn = new SetPort ( );
		spOut = new SetPort ( );
		sbBus = new SetBus ( );

		peE1 = new InControl ( "E0", parSizein);
		spIn.add ( peE1);
		psS1 = new OutControl ( "S0", parSizeout);
		spOut.add ( psS1);
		bBs1 = new Bus ( this, psS1, null, null);
		sbBus.add ( bBs1);
	}

	public void behavior ( ) {
		int iSignal;

		long lE1t, lSAux, lBase = -1;

		lE1t = peE1.getDoubleWord ( );
		iSignal = (int) SistNum.getBitRange( lE1t, DOUBLEWORD-peE1.getSize(),DOUBLEWORD-peE1.getSize());

		lBase = lBase << peE1.getSize();
		if ( iSignal == 1) {
			lSAux = lE1t | lBase;
		} else {
			lBase = ~lBase;
			lSAux = lE1t & lBase;
		}

		psS1.set ( lSAux);
		if ( bBs1.isLinked ( ) == true) bBs1.behavior ( );
	}

	public void debug ( ) {
		System.out.println ( "*** SignExtender.debug:...BEGIN");

		if ( spIn != null) spIn.debug ( );
		if ( spCtrl != null) spCtrl.debug ( );
		if ( spOut != null) spOut.debug ( );
		if ( spStt != null) spStt.debug ( );
		if ( sbBus != null) sbBus.debug ( );
		
		System.out.println ( "*** SignExtender.debug:...END");
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
		System.out.println ( "SignExtender: e um replicador do bit de sinal");
		System.out.println ( "	Possui uma entrada E0 e uma saida S0");
		System.out.println ( "	Replica o bit de sinal do valor na entrada E0...");
		System.out.println ( "	ate TAM bits colocando o resultado na saida S0...");
		System.out.println ( "	TAM e o tamanho em bits da saida S0");
		System.out.println ( "	Para cria-lo: create <nome> 9 0 <bitsOrg> <bitsDest>");
		System.out.println ( "		Org,Dest - tamanho em bits da entrada E0 e saida S0");
		System.out.println ( "			valores validos sao: 1, 8, 16, 32, 64");
		System.out.println ( );
	}

	public static void test ( ) {
		SignExtender se = new SignExtender ( "tst", BYTE, HALFWORD);
		
		se.set ( "E0", IN, 128);
		se.behavior ( );
		se.debug ( );
	}

	private InControl peE1;
	private OutControl psS1;
	private Bus bBs1;
}
