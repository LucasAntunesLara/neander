package cseq;

import ports.InControl;
import ports.OutControl;
import ports.SetPort;
import bus.Bus;
import bus.SetBus;
import contents.Contents;

public class Register extends Sequential {

	public Register ( 	String parName, String parE1name, int parSize, String parS1name) {
		sbName = new StringBuffer ( ).append (parName);

		spIn = new SetPort ( );
		spOut = new SetPort ( );
		sbBus = new SetBus ( );
		
		peE1 = new InControl ( parE1name, parSize);
		spIn.add ( peE1);
		psS1 = new OutControl ( parS1name, parSize);
		spOut.add ( psS1);
		bBs1 = new Bus ( this, psS1, null, null);
		sbBus.add ( bBs1);
		
		cConteudo = new Contents ( 1, parSize, INTEGER);
		cConteudo.set ( 0, 0);
	}

	public Register ( 	int parNumber, int parSize) {
		sbName = new StringBuffer ( ).append (parNumber);

		spIn = new SetPort ( );
		spOut = new SetPort ( );
		sbBus = new SetBus ( );
		
		peE1 = new InControl ( "E1", parSize);
		spIn.add ( peE1);
		psS1 = new OutControl ( "S1", parSize);
		spOut.add ( psS1);
		bBs1 = new Bus ( this, psS1, null, null);
		sbBus.add ( bBs1);
		
		cConteudo = new Contents ( 1, parSize, INTEGER);
	}

	public void write ( ) {
		long lE1t;
		
		lE1t = peE1.getDoubleWord ( );
		cConteudo.set ( lE1t, 0);
	}
	
	public void propagate ( ) {
		long lS1t;
		
		lS1t = cConteudo.getDoubleWord ( 0);
		psS1.set ( lS1t);	
		
		if ( bBs1.isLinked ( ) == true) bBs1.behavior ( );	
	}

	public void behavior ( ) {
		this.write ( );
		this.propagate ( );		
	}

	public void read ( ) {
		this.propagate ( );
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
		System.out.println ( "Register: e um registrador");
		System.out.println ( "	Possui uma entrada E1 e uma saida S1");
		System.out.println ( "	Armazena o conteudo da entrada E1");
		System.out.println ( "	Para cria-lo: create <nome> 0 0 <bits>");
		System.out.println ( );
	}

	public static void test ( ) {
		Register reg = new Register ( "tst", "e1", HALFWORD, "s1");
		
		reg.set ( "e1", IN, 1);
		reg.write ( );
		reg.read ( );
		reg.debug ( );
	}

	public InControl peE1;
	public OutControl psS1;
	public Bus bBs1;
}
