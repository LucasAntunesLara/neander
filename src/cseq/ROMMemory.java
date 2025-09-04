package cseq;

import ports.InControl;
import ports.OutControl;
import ports.SetPort;
import bus.Bus;
import bus.SetBus;
import contents.Contents;

/*
 *	peE2 eh end, endereco
 */
public class ROMMemory extends Sequential {

	public ROMMemory ( 	int parNwords, String parName, String parE1name, 
						int parSizeData, int parSizeEnd,
						String parS1name) {
		sbName = new StringBuffer ( ).append (parName);

		spIn = new SetPort ( );
		spOut = new SetPort ( );
		sbBus = new SetBus ( );

		peE1 = new InControl ( parE1name, parSizeData);
		spIn.add ( peE1);
		
		psS1 = new OutControl ( parS1name, parSizeData);
		spOut.add ( psS1);
		bBs1 = new Bus ( this, psS1, null, null);
		sbBus.add ( bBs1);
		
		cConteudo = new Contents ( parNwords, parSizeData, INTEGER);
		Nwords = parNwords;
		
		int i;
		for ( i = 0; i < parNwords; i ++) {
			cConteudo.set ( 0, i);
		}
	}

	public void write ( ) {
		// E uma memoria ROM!
	}
	
	public void propagate ( ) {
		int iE1t;
		long lS1t;

		iE1t = peE1.getWord ( );
		lS1t = cConteudo.getDoubleWord ( iE1t);
		psS1.set ( lS1t);
		
		if ( bBs1.isLinked ( ) == true) bBs1.behavior ( );
	}

	public void behavior ( ) {
		this.propagate ( );		
	}

	public void read ( ) {
		this.propagate ( );
	}

	public void debug ( ) {
		System.out.println ( "*** ROMMemory.debug:...BEGIN");

		if ( spIn != null) spIn.debug ( );
		if ( spCtrl != null) spCtrl.debug ( );
		if ( spOut != null) spOut.debug ( );
		if ( spStt != null) spStt.debug ( );
		if ( sbBus != null) sbBus.debug ( );
		if ( cConteudo != null) cConteudo.debug ( );

		System.out.println ( "*** ROMMemory.debug:...END");
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
		System.out.println ( "ROMMemory: e uma memoria ROM");
		System.out.println ( "	Possui uma entradas E1 e uma saida S1");
		System.out.println ( "	Le o conteudo da posicao de memoria E1");
		System.out.println ( "	Para cria-la: create <nome> 19 <npos> <bitsData> <bitsEnd>");
		System.out.println ( "		npos - nro. de posicoes de memoria");
		System.out.println ( );
	}

	public static void test ( ) {
		ROMMemory memR = new ROMMemory ( 16, "tst", "e1", DOUBLEWORD, BYTE, "s1");
		
		memR.cConteudo.set ( 0xf, 0);
		memR.cConteudo.set ( 0xe, 1);		
		memR.set ( "e1", IN, 1);
		memR.read ( );
		memR.debug ( );
	}

	protected InControl peE1;
	protected OutControl psS1;
	protected Bus bBs1;
	protected int Nwords;
}
