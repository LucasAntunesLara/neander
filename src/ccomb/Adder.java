package ccomb;

import ports.InControl;
import ports.OutControl;
import ports.SetPort;
import util.Define;
import bus.Bus;
import bus.SetBus;

public class Adder extends Combinational implements Define {

	public Adder ( 	int parInc, String parName, String parE1name, int parSize,
					String parE2name, String parS1name, String parNeg, String parZero, 
					String parOverflow) {
		sbName = new StringBuffer ( ).append (parName);

		spIn = new SetPort ( );
		spOut = new SetPort ( );
		spCtrl = new SetPort ( );
		sbBus = new SetBus ( );
		
		peE1 = new InControl ( parE1name, parSize);
		peE2 = new InControl ( parE2name, parSize);
		peE2.set ( parInc);
		spIn.add ( peE1);
		spIn.add ( peE2);
		psS1 = new OutControl ( parS1name, parSize);
		psN = new OutControl ( parNeg, BIT);
		psZ = new OutControl ( parZero, BIT);
		psO = new OutControl ( parOverflow, BIT);
		spOut.add ( psS1);
		spOut.add ( psN);
		spOut.add ( psZ);
		spOut.add ( psO);
		bBs1 = new Bus ( this, psS1, null, null);
		bBN = new Bus ( this, psN, null, null);
		bBZ = new Bus ( this, psZ, null, null);
		bBO = new Bus ( this, psO, null, null);
		sbBus.add ( bBs1);
		sbBus.add ( bBN);
		sbBus.add ( bBZ);
		sbBus.add ( bBO);
	}

	public void behavior ( ) {
		long lE1t, lE2t, lS1t;
		boolean lNt, lZt, lOt = false;
		int iSize;

		iSize = peE1.getSize ( );
		lE1t = peE1.getDoubleWord ( );
		lE2t = peE2.getDoubleWord ( );

		lS1t = lE1t + lE2t;
		if ( lS1t == 0) lZt = true; else lZt = false;
		if ( lS1t < 0) lNt = true; else lNt = false;

		switch ( iSize) {
			case BYTE:
				if ( lS1t < -128L || lS1t > 127L) lOt = true;
				if ( new Long ( lS1t).byteValue ( ) < 0) lNt = true; else lNt = false;
				break;

			case HALFWORD:
				if ( lS1t < -32768L || lS1t > 32767L) lOt = true;
				if ( new Long ( lS1t).shortValue ( ) < 0) lNt = true; else lNt = false;
				break;
				
			case WORD:
				if ( lS1t < -2147483648L || lS1t > 2147483647L) lOt = false;
				if ( new Long ( lS1t).intValue ( ) < 0) lNt = true; else lNt = false;
				break;
				
			default:
				lOt = false;
				if ( lS1t < 0) lNt = true; else lNt = false;
				break;
		}

		psS1.set ( lS1t);
		psN.set ( lNt);
		psZ.set ( lZt);
		psO.set ( lOt);
		
		if ( bBs1.isLinked ( ) == true) bBs1.behavior ( );
		if ( bBN.isLinked ( ) == true) bBN.behavior ( );
		if ( bBZ.isLinked ( ) == true) bBZ.behavior ( );
		if ( bBO.isLinked ( ) == true) bBO.behavior ( );
	}

	public void debug ( ) {
		System.out.println ( "*** Adder.debug:...BEGIN");

		if ( spIn != null) spIn.debug ( );
		if ( spCtrl != null) spCtrl.debug ( );
		if ( spOut != null) spOut.debug ( );
		if ( spStt != null) spStt.debug ( );
		if ( sbBus != null) sbBus.debug ( );
		
		System.out.println ( "*** Adder.debug:...END");
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
		System.out.println ( "Adder: e um somador que soma com um valor constante");
		System.out.println ( "	Possui duas entradas E1 e E2 e uma saida S1");
		System.out.println ( "	Mais tres saidas: Negativo, Zero e Overflow");
		System.out.println ( "	A entrada E2 possui um valor constante");
		System.out.println ( "	Soma o valor da entrada E1 com o valor constante em E2");
		System.out.println ( "	Seta os valores correspondentes de Negativo,Zero e Overflow");
		System.out.println ( "	Para cria-lo: create <nome> 7 <valor> <bits>");
		System.out.println ( "		valor - valor constante da entrada E2");
		System.out.println ( );
	}

	public static void test ( ) {
		Adder ad = new Adder ( 4, "tst", "e1", BYTE, "e2", "s1", "neg","zero","overflow");
		
		ad.set ( "e1", IN, 127);
		ad.set ( "e2", IN, 127);
		ad.behavior ( );
		ad.debug ( );
	}

	protected InControl peE1, peE2;
	protected OutControl psS1, psN, psZ, psO;
	protected Bus bBs1, bBN, bBZ, bBO;
}
