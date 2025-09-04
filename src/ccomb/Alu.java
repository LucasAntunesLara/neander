package ccomb;

import ports.Control;
import ports.InControl;
import ports.OutControl;
import util.SistNum;
import bus.Bus;

public class Alu extends Adder {

	public Alu ( 	String parName, String parE1name, int parSize, String parE2name, 
					String parOpname, String parS1name, String parNeg, String parZero, 
					String parOverflow, String parCarry) {
		super ( 0, parName, parE1name, parSize, parE2name, parS1name, parNeg, 
				parZero, parOverflow);

		psC = new OutControl ( parCarry, BIT);
		spOut.add ( psC);
		bBC = new Bus ( this, psC, null, null);
		sbBus.add ( bBC);
		
		pcOp = new Control ( parOpname, BYTE);
		spCtrl.add ( pcOp);
		
		peE3 = new InControl ( "E3", parSize);	/* Modif.MAC */
		spIn.add ( peE3);						/* Modif.MAC */
	}

	public void behavior ( ) {
		long lE1t, lE2t, lS1t;
		byte byOpt = 0;
		boolean bNt, bZt, bOt = false, bCt = false;
		int iSize;
		int iSt;

		iSize = peE1.getSize ( );
		lE1t = peE1.getDoubleWord ( );
		lE2t = peE2.getDoubleWord ( );
		long lE3t = peE3.getDoubleWord ( );		/* Modif.MAC */
		byOpt = pcOp.getByte ( );

		switch ( byOpt) {
			case ADD:
				lS1t = lE1t + lE2t;
				break;
			case SUB:
				lS1t = lE1t - lE2t;
				break;
			case REM:
				lS1t = lE1t % lE2t;
				break;
			case AND:
				lS1t = lE1t & lE2t;
				break;
			case OR:
				lS1t = lE1t | lE2t;
				break;
			case NOR:
				lS1t = ~ ( lE1t | lE2t);
				break;
			case XOR:
				lS1t = lE1t ^ lE2t;
				break;
			case NOT_E1:
				lS1t = ~lE1t;
				break;
			case NOT_E2:
				lS1t = ~lE2t;
				break;
			case E1:
				lS1t = lE1t;
				break;
			case E2:
				lS1t = lE2t;
				break;
			case E21632:
				lS1t = lE2t << HALFWORD;
				break;
			case INC_E1:
				lS1t = ++ lE1t;
				break;
			case INC_E2:
				lS1t = ++ lE2t;
				break;
			case MUL:
				lS1t = lE1t * lE2t;
				break;
			case SLL:
				lS1t = lE1t << lE2t;
				break;
			case SLR:
			case SAR:
				switch ( iSize) {
					case BYTE:
						byte b1, b2, bres;
						b1 = new Long ( lE1t).byteValue ( );
						b2 = new Long ( lE2t).byteValue ( );
						if ( byOpt == SLR) bres = (byte) (b1 >>> b2);
						else bres = (byte) (b1 >> b2);
						lS1t = new Byte ( bres).longValue();
						break;

					case HALFWORD:
						short s1, s2, sres;
						s1 = new Long ( lE1t).shortValue ( );
						s2 = new Long ( lE2t).shortValue ( );
						if ( byOpt == SLR) sres = (short) (s1 >>> s2);
						else sres = (short) (s1 >> s2);
						lS1t = new Short ( sres).longValue();
						break;
				
					case WORD:
						int it1, it2, ires;
						it1 = new Long ( lE1t).intValue ( );
						it2 = new Long ( lE2t).intValue ( );
						if ( byOpt == SLR) ires = (int) (it1 >>> it2);
						else ires = (int) (it1 >> it2);
						lS1t = new Integer ( ires).longValue();
						break;
				
					default:
						if ( byOpt == SLR) lS1t = lE1t >>> lE2t;
						else lS1t = lE1t >> lE2t;
						break;
				}
				break;
			case DIV:
				int i1, i2, iQuo, iRes;
				i1 = (int) lE1t;
				i2 = (int) lE2t;
				iQuo = i1 / i2;
				iRes = i1 % i2;
				lS1t = SistNum.joinDouble ( iRes, iQuo);
				break;
			case MAC:
				lS1t = lE1t * lE2t + lE3t;		/* Modif.MAC */
// System.out.println ( "No MAC: lS1t = "+lS1t);
				break;
			case SLT:
				switch ( iSize) {
					case BYTE:
						byte b1, b2;
						b1 = new Long ( lE1t).byteValue ( );
						b2 = new Long ( lE2t).byteValue ( );
						if ( b1 < b2) lS1t = 1;
						else lS1t = 0;							
						break;

					case HALFWORD:
						short s1, s2;
						s1 = new Long ( lE1t).shortValue ( );
						s2 = new Long ( lE2t).shortValue ( );
						if ( s1 < s2) lS1t = 1;
						else lS1t = 0;								
						break;
					
					case WORD:
						int it1, it2;
						it1 = new Long ( lE1t).intValue ( );
						it2 = new Long ( lE2t).intValue ( );
						if ( it1 < it2) lS1t = 1;
						else lS1t = 0;								
						break;
					
					default:
						if ( lE1t < lE2t) lS1t = 1;
						else lS1t = 0;	
						break;
					}
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

		if ( byOpt == ADD || byOpt == SUB ||byOpt==INC_E1||byOpt==INC_E2||byOpt==MUL) {
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
					// Falta calcular OVERFLOW para tipo long
					bOt = false;
					break;
			}
		}

		if ( byOpt == ADD || byOpt == INC_E1 || byOpt == INC_E2) {
			long lCarry;
			//String sVcarry = SistNum.toBinString ( lS1t, DOUBLEWORD);
			switch ( iSize) {
				case BYTE:
					lCarry = SistNum.getBitRange( lS1t, 55, 55);
					//if ( sVcarry.charAt ( 55) == '1') bCt = true;
					if ( lCarry == 1) bCt = true;
					break;

				case HALFWORD:
					lCarry = SistNum.getBitRange( lS1t, 47, 47);
					//if ( sVcarry.charAt ( 47) == '1') bCt = true;
					if ( lCarry == 1) bCt = true;
					break;
				
				case WORD:
					lCarry = SistNum.getBitRange( lS1t, 31, 31);
					//if ( sVcarry.charAt ( 31) == '1') bCt = true;
					if ( lCarry == 1) bCt = true;
					break;
				
				default:
					// Falta calcular CARRY para tipo long
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
//if ( byOpt == SLR) System.out.println( lS1t);
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
		System.out.println ( "*** Alu.debug:...BEGIN");

		if ( spIn != null) spIn.debug ( );
		if ( spCtrl != null) spCtrl.debug ( );
		if ( spOut != null) spOut.debug ( );
		if ( spStt != null) spStt.debug ( );
		if ( sbBus != null) sbBus.debug ( );
		
		System.out.println ( "*** Alu.debug:...END");
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
		System.out.println ( "Alu: e uma unidade logica e aritmetica");
		System.out.println ( "	Possui duas entradas E1 e E2 e uma saida S1");
		System.out.println ( "	Mais as saidas de estado: Negativo, Zero e Overflow");
		System.out.println ( "	Possui um controle OP");
		System.out.println ( "		OP - 	seleciona qual operacao a ser...");
		System.out.println ( "			aplicada nas entradas E1 op E2...");
		System.out.println ( "			10 - soma; 11 - subtracao");
		System.out.println ( "			12 - and;  13 - or");
		System.out.println ( "			14 - xor;  15 - not E1; 16 - not E2");
		System.out.println ( "			17 - passa E1;  18 - passa E2");
		System.out.println ( "			19 - incrementa E1;  20 - inc.E2");
		System.out.println ( "			21 - nor");

		System.out.println ( "	Funcao: S1 = E1 op E2");
		System.out.println ( "	Seta as saidas de estado");
		System.out.println ( "	Para cria-lo: create <nome> 8 0 <bits>");
		System.out.println ( );
	}

	public static void test ( ) {
		Alu al = new Alu ( 	"tst", "e1", BYTE, "e2", "op", "s1", 
							"neg", "zero", "overflow", "carry");
		
		al.set ( "e1", IN, 128);
		al.set ( "e2", IN, 128);
		al.set ( "op", CONTROL, ADD);
		al.behavior ( );
		al.debug ( );
	}

	protected OutControl psC;
	protected Control pcOp;
	protected Bus bBC;
	protected InControl peE3;	/* Modif.MAC */
}
