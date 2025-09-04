package ccomb;

import ports.InControl;
import ports.OutControl;
import ports.SetPort;
import util.SistNum;
import bus.Bus;
import bus.SetBus;

public class GetFieldComponent extends Combinational {

	public GetFieldComponent ( 	String parName, int parSize, int parFirstBit, int parLastBit) {
		sbName = new StringBuffer ( ).append (parName);

		spIn = new SetPort ( );
		spOut = new SetPort ( );
		sbBus = new SetBus ( );
		
		peE1 = new InControl ( "E0", parSize);
		spIn.add ( peE1);
		psS1 = new OutControl ( "S0", parSize);
		spOut.add ( psS1);
		bBs1 = new Bus ( this, psS1, null, null);
		sbBus.add ( bBs1);
		
		iSize = parSize;
		if ( iSize != WORD && iSize != DOUBLEWORD) iSize = DOUBLEWORD;
		iFBit = parFirstBit;
		iLBit = parLastBit;
	}

	public void behavior ( ) {
		long lE1t, lS1t;
		int iValue, iSignal;

		lE1t = peE1.getDoubleWord ( );

		if ( iSize == WORD) {
			iValue = new Long ( lE1t).intValue();
			lS1t = SistNum.getBitRange( iValue, iFBit, iLBit);
			// sign extension for MIPS models
			iSignal = SistNum.getBitRange( iValue, iFBit, iFBit);
			if ( iSignal == 1) {
				// esta' fazendo apenas para bits 6 a 31
				iValue = iValue | 0xfc000000;
				lS1t = new Long (iValue).longValue();
			}
		} else lS1t = SistNum.getBitRange( lE1t, iFBit, iLBit);

		psS1.set ( lS1t);
		
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
		System.out.println ( "GetFieldComponent: obtem um campo dentro de uma palavra");
		System.out.println ( "	Possui uma entrada E0 e uma saida S0");
		System.out.println ( "	Obtem o campo da entrada e coloca o resultado na saida S0...");
		System.out.println ( "	Para cria-lo: create <nome> 110 <wordSize> <firstBit> <lastBit>");
		System.out.println ( "		wordSize - tamanho da palavra");
		System.out.println ( "		firstBit, lastBit - primeiro e ultimo bit do campo...");
		System.out.println ( "				sao numerados da esquerda para a direita, iniciando em 0");
		System.out.println ( );
	}

	public static void test ( ) {
		GetFieldComponent se = new GetFieldComponent ( "tst", DOUBLEWORD, 0, 7);
		
		se.set ( "E0", IN, -1);
		se.behavior ( );
		se.debug ( );
	}

	private InControl peE1;
	private OutControl psS1;
	private Bus bBs1;
	private int iSize, iFBit, iLBit;
}
