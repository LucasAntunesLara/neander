package cseq;

import ports.InControl;
import ports.OutControl;
import ports.SetPort;
import ports.Status;
import bus.Bus;
import bus.SetBus;
import contents.Contents;

/*
 *	peE2 eh end, endereco
 */
public class Memory extends Sequential {

	public Memory ( 	int parNwords, String parName, String parE1name, 
						int parSizeData, String parE2name, int parSizeEnd,
						String parS1name, int parLatency) {
		sbName = new StringBuffer ( ).append (parName);

		spIn = new SetPort ( );
		spOut = new SetPort ( );
		sbBus = new SetBus ( );
		spStt = new SetPort ( );
		
		peE1 = new InControl ( parE1name, parSizeData);
		spIn.add ( peE1);
		peE2 = new InControl ( parE2name, parSizeEnd);
		spIn.add ( peE2);
		
		psS1 = new OutControl ( parS1name, parSizeData);
		spOut.add ( psS1);
		bBs1 = new Bus ( this, psS1, null, null);
		sbBus.add ( bBs1);
		
		stATR1 = new Status ( "TDCF_Latency");
		stATR1.set ( parLatency);
		spStt.add ( stATR1);
		iDelay = parLatency;

		stATR2 = new Status ( "READY");
		stATR2.set ( 0);
		spStt.add ( stATR2);

		cConteudo = new Contents ( parNwords, parSizeData, INTEGER);
		Nwords = parNwords;
		
/*		int i;
		for ( i = 0; i < parNwords; i ++) {
			cConteudo.set ( 0, i);
		}*/
	}

	protected void write1st ( ) {
		long lE1t;
		int iE2t;
		
		lE1t = peE1.getDoubleWord ( );
		iE2t = peE2.getWord ( );
		// test
		//iE2t += 1000;
		cConteudo.set ( lE1t, iE2t);
	}
	
	public void write ( ) {
		if ( bFirstOperation == true) {
			// caso o usuario mude o atributo na GUI antes de comecar a simulacao 
			iDelay = stATR1.getWord ( );
			bFirstOperation = false;
		}
		if ( iDelay == 0) {
			this.write1st ( );
			stATR2.set ( 1);
			iDelay = stATR1.getWord ( );
		} else {
			iDelay --;
			stATR2.set ( 0);
		}
	}
	
	protected void propagate1st ( ) {
		int iE2t;
		long lS1t;

		iE2t = peE2.getWord ( );
		// test
		//iE2t += 1000;
		lS1t = cConteudo.getIntegerValue ( iE2t);
		psS1.set ( lS1t);
		
		if ( bBs1.isLinked ( ) == true) bBs1.behavior ( );
	}
	
	public void propagate ( ) {
		this.propagate1st ( );
	}

	public void behavior ( ) {
		this.write ( );
		this.propagate ( );		
	}

	protected long read1st ( ) {
		int iE2t;
		long lS1t;

		iE2t = peE2.getWord ( );
		lS1t = cConteudo.getDoubleWord ( iE2t);

		return ( lS1t);
	}

	public void read ( ) {
		if ( bFirstOperation == true) {
			// caso o usuario mude o atributo na GUI antes de comecar a simulacao 
			iDelay = stATR1.getWord ( );
			bFirstOperation = false;
		}
		if ( iDelay == 0) {
			this.propagate1st ( );
			stATR2.set ( 1);
			iDelay = stATR1.getWord ( );
		} else {
			iDelay --;
			stATR2.set ( 0);
		}
	}

	public void debug ( ) {
		System.out.println ( "*** Memory.debug:...BEGIN");

		if ( spIn != null) spIn.debug ( );
		if ( spCtrl != null) spCtrl.debug ( );
		if ( spOut != null) spOut.debug ( );
		if ( spStt != null) spStt.debug ( );
		if ( sbBus != null) sbBus.debug ( );
		if ( cConteudo != null) cConteudo.debug ( );

		System.out.println ( "*** Memory.debug:...END");
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
		System.out.println ( "Memory: e uma memoria generica");
		System.out.println ( "	Possui duas entradas E1 e E2 e uma saida S1");
		System.out.println ( "	Armazena o conteudo da entrada E1...");
		System.out.println ( "	na posicao de memoria de endereco E2...");
		System.out.println ( "	Ou le o conteudo da posicao de memoria E2");
		System.out.println ( "	Para cria-la: create <nome> 3 <npos> <bitsData> <bitsEnd> <latency>");
		System.out.println ( "		npos - nro. de posicoes de memoria");
		System.out.println ( );
	}

	public static void test ( ) {
		Memory mem = new Memory ( 1024, "tst", "e1", DOUBLEWORD, "e2", BYTE, "s1", 0);
		
		mem.set ( "e1", IN, 1);
		mem.set ( "e2", IN, 5);
		mem.write ( );
		mem.read ( );
		mem.debug ( );
	}

	protected InControl peE1, peE2;
	protected OutControl psS1;
	protected Status stATR1, stATR2;
	protected Bus bBs1;
	protected int Nwords, iDelay;
	protected boolean bFirstOperation = true;
}
