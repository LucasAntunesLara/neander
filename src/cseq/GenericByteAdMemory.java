package cseq;

import ports.InControl;
import ports.OutControl;
import ports.SetPort;
import ports.Status;
import util.SistNum;
import bus.Bus;
import bus.SetBus;
import contents.Contents;

/*
 *	peE2 eh end, endereco
 */
public class GenericByteAdMemory extends Sequential {

	public GenericByteAdMemory ( 	String parName, int parLatency) {
		sbName = new StringBuffer ( ).append (parName);

		spIn = new SetPort ( );
		spOut = new SetPort ( );
		sbBus = new SetBus ( );
		spStt = new SetPort ( );
		
		peE1 = new InControl ( "E0", DOUBLEWORD);	// data
		spIn.add ( peE1);
		peE2 = new InControl ( "E1", DOUBLEWORD);	// address
		spIn.add ( peE2);
		peE3 = new InControl ( "ACCESS_TYPE", NIBBLE);		// define the access size: byte, halfword, word
		spIn.add ( peE3);
		
		psS1 = new OutControl ( "S0", DOUBLEWORD);
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

		//cConteudo = new Contents ( Integer.MAX_VALUE, BYTE, INTEGER);
		cConteudo = new Contents ( 10, BYTE, INTEGER);
	}

	protected void write1st ( ) {
		long lE0t;
		int iE1t, iE2t, i;
		byte bAux [];
		
		lE0t = peE1.getDoubleWord ( );
		iE1t = peE2.getWord ( );
//System.out.println ( "dado = "+lE0t);
//System.out.println ( "endereço = "+iE1t);
		// test
		//iE1t += 1000;
		bAux = SistNum.splitDoubleInto8Bytes( lE0t);
		iE2t = peE3.getWord ( );
		switch ( iE2t) {
			case BYTE:
				cConteudo.set ( bAux [ 0], iE1t);
				break;
			case HALFWORD:
			case BITSLEFT16:
				iE1t += 2;
				for ( i = 2; i < 4; i ++) cConteudo.set ( bAux [ i], iE1t ++);
//System.out.println("na write:");
//for ( i = 0; i < 2; i ++) System.out.print ( bAux [ i]+"\t");
//System.out.println();
				break;
			case WORD:
				for ( i = 0; i < 4; i ++) {
//System.out.println ( "endereço = "+iE1t);
					cConteudo.set ( bAux [ i], iE1t ++);
//System.out.println ( "dado byte = "+bAux [i]);
				}
				break;
			case DOUBLEWORD:
				for ( i = 0; i < 8; i ++) cConteudo.set ( bAux [ i], iE1t ++);
				break;
			case BITSRIGHT16:
				for ( i = 0; i < 2; i ++) cConteudo.set ( bAux [ i], iE1t ++);
//System.out.println("na write:");
//for ( i = 2; i < 4; i ++) System.out.print ( bAux [ i]+"\t");
//System.out.println();
				break;
			default:
				break;
		}
	}
	
	public void write ( ) {
		if ( bFirstOperation == true) {
			// caso o usuario mude o atributo na GUI antes de comecar a simulacao 
			iDelay = stATR1.getWord ( );
			bFirstOperation = false;
		}
		if ( iDelay == 0) {
			this.write1st ( );
			stATR2.set ( 1);	// READY
			iDelay = stATR1.getWord ( );
		} else {
			iDelay --;
			stATR2.set ( 0);	// NOT READY
		}
	}
	
	protected void read1st ( ) {
		int iE1t, iE2t, i;
		byte [] bAux = new byte [ 8];
		long lAux;

		iE1t = peE2.getWord ( );
		// test
		//iE1t += 1000;
		iE2t = peE3.getWord ( );

		switch ( iE2t) {
			case BYTE:
				bAux [ 0] = cConteudo.getByte ( iE1t);
				break;
			case HALFWORD:
			case BITSLEFT16:
				for ( i = 0; i < 2; i ++) bAux [ i] = cConteudo.getByte ( iE1t ++);
				break;
			case WORD:
				for ( i = 0; i < 4; i ++) bAux [ i] = cConteudo.getByte ( iE1t ++);
				break;
			case DOUBLEWORD:
				for ( i = 0; i < 8; i ++) bAux [ i] = cConteudo.getByte ( iE1t ++);
				break;
			case BITSRIGHT16:
				iE1t += 2;
				for ( i = 0; i < 2; i ++) bAux [ i] = cConteudo.getByte ( iE1t ++);
				break;
			default:
				break;
		}
		
		lAux = SistNum.join8BytesInDouble( bAux);

		psS1.set ( lAux);
//System.out.println("na read:");
//if (iE2t == BITSLEFT16 || iE2t == BITSRIGHT16) for ( i = 0; i < 8; i ++) System.out.print ( bAux [ i]+"\t");
//System.out.println();
		if ( bBs1.isLinked ( ) == true) bBs1.behavior ( );
	}

	public void read ( ) {
		if ( bFirstOperation == true) {
			// caso o usuario mude o atributo na GUI antes de comecar a simulacao 
			iDelay = stATR1.getWord ( );
			bFirstOperation = false;
		}
		if ( iDelay == 0) {
			this.read1st ( );
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
		System.out.println ( "Memory: e uma memoria generica enderecada a byte");
		System.out.println ( "	Possui tres entradas E0, E1 e E2 e uma saida S0");
		System.out.println ( "	Armazena o conteudo da entrada E0...");
		System.out.println ( "	na posicao de memoria de endereco E1...");
		System.out.println ( "	Ou le o conteudo da posicao de memoria E1");
		System.out.println ( "	A entrada E2 define se o acesso e' a um byte, meia palavra ou palavra inteira");
		System.out.println ( "	Para cria-la: create <nome> 101 <latency>");
		System.out.println ( );
	}

	public static void test ( ) {
		GenericByteAdMemory mem = new GenericByteAdMemory ( "tst", 0);
		
		mem.set ( "e0", IN, -256);
		mem.set ( "e1", IN, 5);
		mem.set ( "e2", IN, WORD);
		mem.write ( );
		mem.set ( "e1", IN, 6);
		mem.read ( );
		mem.list ( );
	}

	protected InControl peE1, peE2, peE3;
	protected OutControl psS1;
	protected Status stATR1, stATR2;
	protected Bus bBs1;
	protected int Nwords, iDelay;
	protected boolean bFirstOperation = true;
}
