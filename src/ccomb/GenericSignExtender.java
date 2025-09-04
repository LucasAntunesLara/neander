package ccomb;

import ports.InControl;
import ports.OutControl;
import ports.SetPort;
import util.SistNum;
import bus.Bus;
import bus.SetBus;

// TAREFA 2 - estudar este SignExtender e tentar a criaçao do Generic

public class GenericSignExtender extends Combinational {

	public GenericSignExtender ( 	String parName, int parBitPos, int parSize, int SignOrZeroExtender) {
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
		
		bitPos = DOUBLEWORD-parBitPos;
		SignOrZero = SignOrZeroExtender;
	}

	public void behavior ( ) {
		int iSignal;

		long lE1t, lSAux, lBase = -1;

		lE1t = peE1.getDoubleWord ( );
		iSignal = (int) SistNum.getBitRange( lE1t, bitPos,bitPos);

		lBase = lBase << DOUBLEWORD-bitPos;
		if ( iSignal == 1 && SignOrZero == 1) {
			lSAux = lE1t | lBase;
		} else {
			lBase = ~lBase;
			lSAux = lE1t & lBase;
		}

		psS1.set ( lSAux);
		if ( bBs1.isLinked ( ) == true) bBs1.behavior ( );
	}

	public void debug ( ) {
		System.out.println ( "*** GenericSignExtender.debug:...BEGIN");

		if ( spIn != null) spIn.debug ( );
		if ( spCtrl != null) spCtrl.debug ( );
		if ( spOut != null) spOut.debug ( );
		if ( spStt != null) spStt.debug ( );
		if ( sbBus != null) sbBus.debug ( );
		
		System.out.println ( "*** GenericSignExtender.debug:...END");
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
		System.out.println ( "GenericSignExtender: e um replicador do bit de sinal...");
		System.out.println ( "ou de extensao de zero GENÉRICO");
		System.out.println ( "   Possui uma entrada E0 e uma saida S0");
		System.out.println ( "	 Replica o bit de sinal do valor na entrada E0...");
		System.out.println ( "	 ate TAM bits colocando o resultado na saida S0...");
		System.out.println ( "	 TAM e o tamanho em bits da saida S0");
		System.out.println ( "	 Para cria-lo: create <nome> 113 0 <bitsPos> <bitsSize> <signOrZero>");
		System.out.println ( "		 bitPos - Posicao a partir da qual devem ser lidos os bits");
		System.out.println ( "		 bitSize - Tamanho da entrada e da saida");
		System.out.println ( "		 signOrZero - Identifica extensao de sinal ou de zero");
		System.out.println ( "			 valores validos sao: 0, 1");
		System.out.println ( );
	}

	public static void test ( ) {
		GenericSignExtender se = new GenericSignExtender ( "tst", 8, HALFWORD, 1);
		
		se.set ( "E0", IN, 128);
		se.behavior ( );
		se.debug ( );
	}

	private InControl peE1;
	private OutControl psS1;
	private Bus bBs1;
	private int SignOrZero, bitPos;
}
