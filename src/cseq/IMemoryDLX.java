package cseq;

import ports.SetPort;
import ports.Status;

public class IMemoryDLX extends IMemory {

	public IMemoryDLX ( 	int parNwords, String parName, String parE1name, 
							int parSizeData, String parE2name, int parSizeEnd,
							String parS1name, String parATR1name, int parLatency) {
		super ( parNwords, parName, parE1name, parSizeData, 
				parE2name, parSizeEnd, parS1name, parLatency);

		spStt = new SetPort ( );
		
		stATR1 = new Status ( parATR1name);
		stATR1.set ( 0);
		spStt.add ( stATR1);
	}

	public void read ( ) {
		this.propagate1st ( );
		stATR1.set ( 1);
//System.out.println ( "memory read");
	}

	public static void help ( ) {
		System.out.println ( "IMemoryDLX: e uma memoria de instrucoes com atributo");
		System.out.println ( "	Possui duas entradas E1 e E2 e uma saida S1");
		System.out.println ( "	E um atributo FETCH que possui o valor 1");
		System.out.println ( "	quando de uma nova leitura. 0, caso contrario");
		System.out.println ( "	Armazena o conteudo da entrada E1...");
		System.out.println ( "	na posicao de memoria de endereco E2...");
		System.out.println ( "	Ou le o conteudo da posicao de memoria E2");
		System.out.println ( "	Para cria-la: create <nome> 4 <npos> <bitsData> <bitsEnd>");
		System.out.println ( "		npos - nro. de posicoes de memoria");
		System.out.println ( );
	}

	public static void test ( ) {
		IMemoryDLX mem = new IMemoryDLX ( 	15, "tst", "e1", DOUBLEWORD, "e2", 
											BYTE, "s1", "atrib", 0);
		
		mem.set ( "e1", IN, 1);
		mem.set ( "e2", IN, 5);
		mem.write ( );
		mem.read ( );
		mem.debug ( );
	}

	protected Status stATR1;
}
