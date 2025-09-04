package ccomb;

import ports.Control;
import ports.InControl;
import ports.OutControl;
import ports.SetPort;
import util.Define;
import bus.Bus;
import bus.SetBus;

public class FPUnit extends Combinational implements Define {

	public FPUnit ( 	String parName, String parE1name, String parE2name, 
						String parOpname, String parS1name) { //, String parNeg, String parZero, 
						//String parOverflow, String parCarry) {
		sbName = new StringBuffer ( ).append (parName);

		spIn = new SetPort ( );
		spOut = new SetPort ( );
		spCtrl = new SetPort ( );
		sbBus = new SetBus ( );
		
		peE1 = new InControl ( parE1name, DOUBLEWORD, FP);
		peE2 = new InControl ( parE2name, DOUBLEWORD, FP);
		spIn.add ( peE1);
		spIn.add ( peE2);
		psS1 = new OutControl ( parS1name, DOUBLEWORD, FP);
		//psN = new OutControl ( parNeg, BIT);
		//psZ = new OutControl ( parZero, BIT);
		//psO = new OutControl ( parOverflow, BIT);
		spOut.add ( psS1);
		//spOut.add ( psN);
		//spOut.add ( psZ);
		//spOut.add ( psO);
		bBs1 = new Bus ( this, psS1, null, null);
		//bBN = new Bus ( this, psN, null, null);
		//bBZ = new Bus ( this, psZ, null, null);
		//bBO = new Bus ( this, psO, null, null);
		sbBus.add ( bBs1);
		//sbBus.add ( bBN);
		//sbBus.add ( bBZ);
		//sbBus.add ( bBO);

		//psC = new OutControl ( parCarry, BIT);
		//spOut.add ( psC);
		//bBC = new Bus ( this, psC, null, null);
		//sbBus.add ( bBC);
		
		pcOp = new Control ( parOpname, BYTE);
		spCtrl.add ( pcOp);
	}

	public void behavior ( ) {
		double dE1t, dE2t, dS1t;
		byte byOpt = 0;
		//boolean bNt, bZt, bOt = false, bCt = false;

		dE1t = peE1.getDouble ( );
		dE2t = peE2.getDouble ( );
		byOpt = pcOp.getByte ( );
		//System.out.println ( "dE1t = "+dE1t);
		//System.out.println ( "dE2t = "+dE2t);

		switch ( byOpt) {
			case ADD:
				dS1t = dE1t + dE2t;
				//System.out.println ( "dS1t = "+dS1t);
				break;
			case SUB:
				dS1t = dE1t - dE2t;
				break;
/*			case AND:
				lS1t = lE1t & lE2t;
				break;
			case OR:
				lS1t = lE1t | lE2t;
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
			case INC_E1:
				lS1t = ++ lE1t;
				break;
			case INC_E2:
				lS1t = ++ lE2t;
				break;*/
			case MUL:
				dS1t = dE1t * dE2t;
				break;
			default:
				dS1t = 0L;
				break;
		}

		psS1.set ( dS1t);
		
		if ( bBs1.isLinked ( ) == true) bBs1.behavior ( );
	}

	public void debug ( ) {
		System.out.println ( "*** 	FPUnit.debug:...BEGIN");

		if ( spIn != null) spIn.debug ( );
		if ( spCtrl != null) spCtrl.debug ( );
		if ( spOut != null) spOut.debug ( );
		if ( spStt != null) spStt.debug ( );
		if ( sbBus != null) sbBus.debug ( );
		
		System.out.println ( "*** FPUnit.debug:...END");
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
		System.out.println ( "FPUnit: e uma unidade de execucao de ponto flutuante");
		System.out.println ( "	Possui duas entradas E1 e E2 e uma saida S1");
		System.out.println ( "	Possui um controle OP");
		System.out.println ( "		OP - 	seleciona qual operacao a ser...");
		System.out.println ( "			aplicada nas entradas E1 op E2...");
		System.out.println ( "			10 - soma; 11 - subtracao");
		System.out.println ( "			12 - multiplicacao");

		System.out.println ( "	Funcao: S1 = E1 op E2");
		System.out.println ( "	Para cria-lo: create <nome> 52 0 <bits>");
		System.out.println ( );
	}

	public static void test ( ) {
		FPUnit al = new FPUnit ( 	"tst", "e1", "e2", "op", "s1");
		long l;
		double d = 5.75;
		
		al.set ( "e1", IN, Double.doubleToLongBits(d));
		al.set ( "e2", IN, Double.doubleToLongBits(d));
		al.set ( "op", CONTROL, ADD);
		al.behavior ( );
		al.list ( );
	}

	protected InControl peE1, peE2;
	protected OutControl psS1; // , psN, psZ, psO, psC;
	protected Control pcOp;
	protected Bus bBs1; //, bBN, bBZ, bBO, bBC;
}
