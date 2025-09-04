package cseq;

import ports.SetPort;
import ports.Status;

public class RI extends Register {

	public RI ( 	String parName, String parE1name, int parSize, String parS1name,
					String parATR1name) {

		super ( parName, parE1name, parSize, parS1name);
		
		spStt = new SetPort ( );
		
		stATR1 = new Status ( parATR1name);
		stATR1.set ( 0);
		spStt.add ( stATR1);
	}

	public void write ( ) {
		long lE1t;
		
		lE1t = peE1.getDoubleWord ( );
		cConteudo.set ( lE1t, 0);
		
		stATR1.set ( 1);
	}
	
	public void debug ( ) {
		System.out.println ( "*** register.debug:...BEGIN");

		if ( spIn != null) spIn.debug ( );
		if ( spCtrl != null) spCtrl.debug ( );
		if ( spOut != null) spOut.debug ( );
		if ( spStt != null) spStt.debug ( );
		if ( sbBus != null) sbBus.debug ( );
		if ( cConteudo != null) cConteudo.debug ( );
		
		System.out.println ( "*** register.debug:...END");
		System.out.println ( );
	}

	public static void help ( ) {
		System.out.println ( "RI: e um registrador de instruções");
		System.out.println ( "	Possui uma entrada E1 e uma saida S1");
		System.out.println ( "	E um atributo FETCH que possui o valor 1");
		System.out.println ( "	quando de uma nova instrucao. 0, caso contrario");
		System.out.println ( "	Armazena o conteudo da entrada E1");
		System.out.println ( "	Para cria-lo: create <nome> 14 0 <bits>");
		System.out.println ( );
	}

	public static void test ( ) {
		RI reg = new RI ( "RI", "e1", HALFWORD, "s1", "FETCH");
		
		reg.set ( "e1", IN, 1);
		reg.write ( );
		reg.read ( );
		reg.debug ( );
	}

	protected Status stATR1;
}
