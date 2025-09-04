package ccomb;

import util.SistNum;

public class AluSimple extends Alu {

	public AluSimple ( 	String parName, String parE1name, int parSize, String parE2name, 
						String parOpname, String parS1name, String parNeg, String parZero, 
						String parOverflow, String parCarry) {
		super ( parName, parE1name, parSize, parE2name, parOpname, parS1name, parNeg, 
				parZero, parOverflow, parCarry);

	}

	public void behavior ( ) {
		long lE1t, lE2t, lS1t;
		byte byOpt = 0;
		boolean bNt, bZt, bOt = false, bCt = false;
		int iSize;

		iSize = peE1.getSize ( );
		lE1t = peE1.getDoubleWord ( );
		lE2t = peE2.getDoubleWord ( );
		byOpt = pcOp.getByte ( );

		switch ( byOpt) {
			case ADD:
				lS1t = lE1t + lE2t;
				break;
			case SUB:
				lS1t = lE1t - lE2t;
				break;
			case SUBE2E1:
				lS1t = lE2t - lE1t;
				break;
			default:
				lS1t = 0L;
				break;
		}

		switch ( iSize) {
			case BYTE:
				if ( new Long ( lS1t).byteValue ( ) == 0) bZt = true; else bZt = false;
				break;

			case HALFWORD:
				if ( new Long ( lS1t).shortValue ( ) == 0) bZt = true; else bZt = false;
				break;
				
			case WORD:
				if ( new Long ( lS1t).intValue ( ) == 0) bZt = true; else bZt = false;
				break;
				
			default:
				if ( lS1t == 0) bZt = true; else bZt = false;
				break;
		}

		if ( byOpt == ADD || byOpt == SUB || byOpt == SUBE2E1) {
			switch ( iSize) {
				case BYTE:
					if ( lS1t < -128L || lS1t > 127L) bOt = true;
					break;

				case HALFWORD:
					if ( lS1t < -32768L || lS1t > 32767L) bOt = true;
					break;
				
				case WORD:
					if ( lS1t < -2147483648L || lS1t > 2147483647L) bOt = false;
					break;
				
				default:
					bOt = false;
					break;
			}
		}

		if ( byOpt == ADD) {

			String sVcarry = SistNum.toBinString ( lS1t, DOUBLEWORD);
			switch ( iSize) {
				case BYTE:
					if ( sVcarry.charAt ( 55) == '1') bCt = true;
					break;

				case HALFWORD:
					if ( sVcarry.charAt ( 47) == '1') bCt = true;
					break;
				
				case WORD:
					if ( sVcarry.charAt ( 31) == '1') bCt = true;
					break;
				
				default:
					bCt = false;
					break;
			}
		}

		switch ( iSize) {
			case BYTE:
				if ( new Long ( lS1t).byteValue ( ) < 0) bNt = true; else bNt = false;
				break;

			case HALFWORD:
				if ( new Long ( lS1t).shortValue ( ) < 0) bNt = true; else bNt = false;
				break;
				
			case WORD:
				if ( new Long ( lS1t).intValue ( ) < 0) bNt = true; else bNt = false;
				break;
				
			default:
				if ( lS1t < 0) bNt = true; else bNt = false;
				break;
		}

		psS1.set ( lS1t);
		psN.set ( bNt);
		psZ.set ( bZt);
		psO.set ( bOt);
		psC.set ( bCt);
		
		if ( bBs1.isLinked ( ) == true) bBs1.behavior ( );
		if ( bBN.isLinked ( ) == true) bBN.behavior ( );
		if ( bBZ.isLinked ( ) == true) bBZ.behavior ( );
		if ( bBO.isLinked ( ) == true) bBO.behavior ( );
		if ( bBC.isLinked ( ) == true) bBC.behavior ( );
	}

	public void debug ( ) {
		System.out.println ( "*** AluSimple.debug:...BEGIN");

		if ( spIn != null) spIn.debug ( );
		if ( spCtrl != null) spCtrl.debug ( );
		if ( spOut != null) spOut.debug ( );
		if ( spStt != null) spStt.debug ( );
		if ( sbBus != null) sbBus.debug ( );
		
		System.out.println ( "*** AluSimple.debug:...END");
		System.out.println ( );
	}

	public static void help ( ) {
		System.out.println ( "Alu: e uma unidade logica e aritmetica simples...");
		System.out.println ( "que executa apenas as operacoes de soma e subtracao");
		System.out.println ( "	Possui duas entradas E1 e E2 e uma saida S1");
		System.out.println ( "	Mais as saidas de estado: Negativo, Zero e Overflow");
		System.out.println ( "	Possui um controle OP");
		System.out.println ( "		OP - 	seleciona qual operacao a ser...");
		System.out.println ( "			aplicada nas entradas E1 op E2...");
		System.out.println ( "			10 - soma; 11 - subtracao");

		System.out.println ( "	Funcao: S1 = E1 op E2");
		System.out.println ( "	Seta as saidas de estado");
		System.out.println ( "	Para cria-lo: create <nome> 18 0 <bits>");
		System.out.println ( );
	}

	public static void test ( ) {
		AluSimple alS = new AluSimple ( 	"tst", "e1", BYTE, "e2", "op", "s1", 
											"neg", "zero", "overflow", "carry");
		
		alS.set ( "e1", IN, 128);
		alS.set ( "e2", IN, 128);
		alS.set ( "op", CONTROL, ADD);
		alS.behavior ( );
		alS.debug ( );
	}
}
