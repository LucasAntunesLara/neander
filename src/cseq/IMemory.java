package cseq;

/*
 *	peE2 eh end, endereco
 */
public class IMemory extends Memory {

	public IMemory ( 	int parNwords, String parName, String parE1name, 
						int parSizeData, String parE2name, int parSizeEnd,
						String parS1name, int parLatency) {
		super ( parNwords, parName, parE1name, parSizeData, 
				parE2name, parSizeEnd, parS1name, parLatency);
	}

	public void debug ( ) {
		System.out.println ( "*** IMemory.debug:...BEGIN");

		if ( spIn != null) spIn.debug ( );
		if ( spCtrl != null) spCtrl.debug ( );
		if ( spOut != null) spOut.debug ( );
		if ( spStt != null) spStt.debug ( );
		if ( sbBus != null) sbBus.debug ( );
		if ( cConteudo != null) cConteudo.debug ( );

		System.out.println ( "*** IMemory.debug:...END");
		System.out.println ( );
	}

	public static void test ( ) {
		IMemory mem = new IMemory ( 15, "tst", "e1", DOUBLEWORD, "e2", BYTE, "s1", 0);
		
		mem.set ( "e1", IN, 1);
		mem.set ( "e2", IN, 5);
		mem.write ( );
		mem.read ( );
		mem.debug ( );
	}

	public static void help ( ) {
		System.out.println ( "IMemory: e uma memoria de instrucoes");
		System.out.println ( "	Possui duas entradas E1 e E2 e uma saida S1");
		System.out.println ( "	Armazena o conteudo da entrada E1...");
		System.out.println ( "	na posicao de memoria de endereco E2...");
		System.out.println ( "	Ou le o conteudo da posicao de memoria E2");
		System.out.println ( "	Para cria-la: create <nome> 4 <npos> <bitsData> <bitsEnd> <latency>");
		System.out.println ( "		npos - nro. de posicoes de memoria");
		System.out.println ( );
	}

}
