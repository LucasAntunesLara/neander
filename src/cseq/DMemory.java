package cseq;

/*
 *	peE2 eh end, endereco
 */
public class DMemory extends Memory {

	public DMemory ( 	int parNwords, String parName, String parE1name, 
						int parSizeData, String parE2name, int parSizeEnd,
						String parS1name, int parLatency) {
		super ( parNwords, parName, parE1name, parSizeData, 
				parE2name, parSizeEnd, parS1name, parLatency);
	}

	public void debug ( ) {
		System.out.println ( "*** DMemory.debug:...BEGIN");

		if ( spIn != null) spIn.debug ( );
		if ( spCtrl != null) spCtrl.debug ( );
		if ( spOut != null) spOut.debug ( );
		if ( spStt != null) spStt.debug ( );
		if ( sbBus != null) sbBus.debug ( );
		if ( cConteudo != null) cConteudo.debug ( );
	
		System.out.println ( "*** DMemory.debug:...END");
		System.out.println ( );
	}

	public static void test ( ) {
		DMemory mem = new DMemory ( 15, "tst", "e1", DOUBLEWORD, "e2", BYTE, "s1", 0);
		
		mem.set ( "e1", IN, 1);
		mem.set ( "e2", IN, 5);
		mem.write ( );
		mem.read ( );
		mem.debug ( );
	}

	public static void help ( ) {
		System.out.println ( "DMemory: e uma memoria de dados");
		System.out.println ( "	Possui duas entradas E1 e E2 e uma saida S1");
		System.out.println ( "	Armazena o conteudo da entrada E1...");
		System.out.println ( "	na posicao de memoria de endereco E2...");
		System.out.println ( "	Ou le o conteudo da posicao de memoria E2");
		System.out.println ( "	Para cria-la: create <nome> 5 <npos> <bitsData> <bitsEnd>");
		System.out.println ( "		npos - nro. de posicoes de memoria");
		System.out.println ( );
	}
}
