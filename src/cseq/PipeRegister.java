package cseq;

import ports.InControl;
import ports.OutControl;
import ports.SetPort;
import bus.Bus;
import bus.SetBus;
import contents.Contents;

public class PipeRegister extends Sequential {

	public PipeRegister ( 	String parName, String parE1name, int parSize1,
							String parE2name, int parSize2, String parE3name, 
							int parSize3, String parE4name, int parSize4,
							String parS1name, String parS2name, String parS3name, 
							String parS4name) {
		sbName = new StringBuffer ( ).append (parName);

		spIn = new SetPort ( );
		spOut = new SetPort ( );
		sbBus = new SetBus ( );
		
		peE1 = new InControl ( parE1name, parSize1);
		spIn.add ( peE1);
		peE2 = new InControl ( parE2name, parSize2);
		spIn.add ( peE2);
		peE3 = new InControl ( parE3name, parSize3);
		spIn.add ( peE3);
		peE4 = new InControl ( parE4name, parSize4);
		spIn.add ( peE4);

		psS1 = new OutControl ( parS1name, parSize1);
		spOut.add ( psS1);
		psS2 = new OutControl ( parS2name, parSize2);
		spOut.add ( psS2);
		psS3 = new OutControl ( parS3name, parSize3);
		spOut.add ( psS3);
		psS4 = new OutControl ( parS4name, parSize4);
		spOut.add ( psS4);
		
		bBs1 = new Bus ( this, psS1, null, null);
		sbBus.add ( bBs1);
		bBs2 = new Bus ( this, psS2, null, null);
		sbBus.add ( bBs2);
		bBs3 = new Bus ( this, psS3, null, null);
		sbBus.add ( bBs3);
		bBs4 = new Bus ( this, psS4, null, null);
		sbBus.add ( bBs4);
		
		cConteudo = new Contents ( 4, DOUBLEWORD, INTEGER);
	}

	public void write ( ) {
		long lE1t, lE2t, lE3t, lE4t;
		
		lE1t = peE1.getDoubleWord ( );
		cConteudo.set ( lE1t, 0);
		lE2t = peE2.getDoubleWord ( );
		cConteudo.set ( lE2t, 1);
		lE3t = peE3.getDoubleWord ( );
		cConteudo.set ( lE3t, 2);
		lE4t = peE4.getDoubleWord ( );
		cConteudo.set ( lE4t, 3);
	}
	
	public void propagate ( ) {
		long lS1t, lS2t, lS3t, lS4t;
		
		lS1t = cConteudo.getDoubleWord ( 0);
		psS1.set ( lS1t);	
		
		if ( bBs1.isLinked ( ) == true) bBs1.behavior ( );
		
		lS2t = cConteudo.getDoubleWord ( 1);
		psS2.set ( lS2t);	
		
		if ( bBs2.isLinked ( ) == true) bBs2.behavior ( );
		
		lS3t = cConteudo.getDoubleWord ( 2);
		psS3.set ( lS3t);	
		
		if ( bBs3.isLinked ( ) == true) bBs3.behavior ( );	

		lS4t = cConteudo.getDoubleWord ( 3);
		psS4.set ( lS4t);

		if ( bBs4.isLinked ( ) == true) bBs4.behavior ( );	
	}

	public void behavior ( ) {
		this.write ( );
		this.propagate ( );		
	}

	public void read ( ) {
		this.propagate ( );
	}

	public void debug ( ) {
		System.out.println ( "*** PipeRegister.debug:...BEGIN");

		if ( spIn != null) spIn.debug ( );
		if ( spCtrl != null) spCtrl.debug ( );
		if ( spOut != null) spOut.debug ( );
		if ( spStt != null) spStt.debug ( );
		if ( sbBus != null) sbBus.debug ( );
		if ( cConteudo != null) cConteudo.debug ( );

		System.out.println ( "*** PipeRegister.debug:...END");
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
		if ( cConteudo != null) cConteudo.list ( );
		
		System.out.println ( "\n** FIM **");
		System.out.println ( );
	}

	public static void help ( ) {
		System.out.println ( "PipeRegister: e um registrador de pipeline");
		System.out.println ( "	Possui as entrada E1, E2, E3 e E4...");
		System.out.println ( "	E as saidas S1, S2, S3 e S4");
		System.out.println ( "	Armazena o conteudo das entradas...");
		System.out.println ( "	E copia para a saida respectiva...");
		System.out.println ( "	E1 p/ S1, E2 p/ S2, etc");
		System.out.println ( "	Para cria-lo: create <nome> 12 0 <bits>");
		System.out.println ( );
	}

	public static void test ( ) {
		PipeRegister preg = new PipeRegister ( 	"tst", "e1", BYTE, "e2", HALFWORD, 
												"e3", WORD, "e4", DOUBLEWORD, 
												"s1", "s2", "s3","s4");
		
		preg.set ( "e1", IN, 15);
		preg.set ( "e2", IN, 16);
		preg.set ( "e3", IN, 17);
		preg.set ( "e4", IN, 18);
		preg.write ( );
		preg.read ( );
		preg.debug ( );
	}

	public InControl peE1, peE2, peE3, peE4;
	public OutControl psS1, psS2, psS3, psS4;
	public Bus bBs1, bBs2, bBs3, bBs4;
	private int ipS1t, ipS2t, ipS3t, ipS4t;

}
