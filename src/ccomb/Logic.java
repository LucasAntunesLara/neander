package ccomb;

import ports.InControl;
import ports.OutControl;
import ports.SetPort;
import util.SistNum;
import bus.Bus;
import bus.SetBus;

public class Logic extends Combinational{
	
	public Logic( String parName, int parEntries, int parSize,
				  int parOuts, int parSizeOuts){
		sbName = new StringBuffer ( ).append (parName);
		
		spIn = new SetPort ( );
		spOut = new SetPort ( );
		sbBus = new SetBus ( );
		
		peE0 = new InControl ( "E0", parSize);
		spIn.add ( peE0);
		
		psS0 = new OutControl ( "S0", parSizeOuts);
		spOut.add ( psS0);
		bBs0 = new Bus ( this, psS0, null, null);
		sbBus.add ( bBs0);
		
		psS1 = new OutControl ( "S1", parSizeOuts);
		spOut.add ( psS1);
		bBs1 = new Bus ( this, psS1, null, null);
		sbBus.add ( bBs1);
		
		psS2 = new OutControl ( "S2", parSizeOuts);
		spOut.add ( psS2);
		bBs2 = new Bus ( this, psS2, null, null);
		sbBus.add ( bBs2);
	}
	
	public void behavior(){
		
		long lE0t, lS0t, lS1t, lS2t;

		lE0t = peE0.getDoubleWord ( );
		
		// implementar aqui o comportamento -- inicio
		
		if (lE0t == 0){
			lS0t = 0;
			lS1t = 1;
			lS2t = 0;
		} else if (lE0t < 0){
			lS0t = 1;
			lS1t = 0;
			lS2t = 0;
		} else {
			lS0t = 0;
			lS1t = 0;
			lS2t = 1;
		}
		
		// implementar aqui o comportamento -- fim

		psS0.set ( lS0t);
		psS1.set ( lS1t);
		psS2.set ( lS2t);
		
		if ( bBs0.isLinked ( ) == true) bBs0.behavior ( );
		if ( bBs1.isLinked ( ) == true) bBs1.behavior ( );
		if ( bBs2.isLinked ( ) == true) bBs2.behavior ( );
		
	}
	
	public void debug(){
		
		System.out.println ( "*** Logic.debug:...BEGIN");

		if ( spIn != null) spIn.debug ( );
		if ( spCtrl != null) spCtrl.debug ( );
		if ( spOut != null) spOut.debug ( );
		if ( spStt != null) spStt.debug ( );
		if ( sbBus != null) sbBus.debug ( );
		
		System.out.println ( "*** Logic.debug:...END");
		System.out.println ( );
		
	}

	public void list(){
		
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
	
	public static void help(){
		
		System.out.println ( "Logic: seta os bits de sinal Z, N e P");
		System.out.println ( "	Possui uma entrada E0 e tres saidas S0, S1 e S2");
		System.out.println ( "	Identifica os bits de sinal e envia para as saidas");
		System.out.println ( "	Para cria-lo: create <nome> 114 0 <nIns> <inSize> <nOuts> <outSize>");
		System.out.println ( "  	nIns - Numero de entradas");
		System.out.println ( "		inSize - Tamanho da entrada");
		System.out.println ( "		nOuts - Numero de saidas");
		System.out.println ( "		outSize - Tamanho da saida");
		System.out.println ( "Obs.: Por enquanto suporta apenas uma entrada e tres saidas");
		System.out.println ( );
		
	}
	
	public static void test(){
		
		Logic lgc = new Logic ( "teste", 1, HALFWORD, 3, 1);
		
		lgc.set ( "E0", IN, 0);
		lgc.behavior ( );
		lgc.debug ( );
		
	}
	
	private InControl peE0;
	private OutControl psS0, psS1, psS2;
	private Bus bBs0, bBs1, bBs2;
	
}
