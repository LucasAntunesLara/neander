package ccomb;

import ports.OutControl;
import bus.Bus;

public class Triplexer extends Duplexer {

	public Triplexer ( 	String parName, String parE1name, int parSize,
						String parS1name, String parS2name, String parS3name) {
		super ( parName, parE1name, parSize, parS1name, parS2name);
		
		psS3 = new OutControl ( parS3name, parSize);
		spOut.add ( psS3);
		bBs3 = new Bus ( this, psS3, null, null);
		sbBus.add ( bBs3);
	}

	public void behavior ( ) {
		long lE1t, lS1t, lS2t, lS3t;

		lE1t = peE1.getDoubleWord ( );
		lS1t = lE1t;
		lS2t = lE1t;
		lS3t = lE1t;

		psS1.set ( lS1t);
		psS2.set ( lS2t);
		psS3.set ( lS3t);

//System.out.println ( "Triplicando saida...");

		if ( bBs1.isLinked ( ) == true) bBs1.behavior ( );
		if ( bBs2.isLinked ( ) == true) bBs2.behavior ( );
		if ( bBs3.isLinked ( ) == true) bBs3.behavior ( );
	}

	public void debug ( ) {
		System.out.println ( "*** Triplexer.debug:...BEGIN");

		if ( spIn != null) spIn.debug ( );
		if ( spCtrl != null) spCtrl.debug ( );
		if ( spOut != null) spOut.debug ( );
		if ( spStt != null) spStt.debug ( );
		if ( sbBus != null) sbBus.debug ( );
		
		System.out.println ( "*** Triplexer.debug:...END");
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
		System.out.println ( "Triplexer: triplica um barramento");
		System.out.println ( "	Possui uma entrada E1 e tres saidas S1, S2 e S3");
		System.out.println ( "	Copia o valor de entrada nas tres saidas");
		System.out.println ( "	Para cria-lo: create <nome> 13 0 <bits>");
		System.out.println ( );
	}

	public static void test ( ) {
		Triplexer tpx = new Triplexer ( "teste", "e1", WORD, "s1", "s2", "s3");
		
		tpx.set ( "e1", IN, 16);
		tpx.behavior ( );
		tpx.debug ( );
	}

	private OutControl psS3;
	private Bus bBs3;

}
