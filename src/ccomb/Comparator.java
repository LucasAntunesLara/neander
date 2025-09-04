package ccomb;

import ports.Control;
import ports.InControl;
import ports.OutControl;
import ports.SetPort;
import bus.Bus;
import bus.SetBus;

public class Comparator extends Combinational {

	public Comparator ( 	String parName, int parSize) {
		int i;
		
		sbName = new StringBuffer ( ).append (parName);

		spIn = new SetPort ( );
		spOut = new SetPort ( );
		spCtrl = new SetPort ( );
		sbBus = new SetBus ( );
	
		for ( i = 0; i < 2; i ++) {
			peE = new InControl ( "E"+i, parSize);
			spIn.add ( peE);
		}
		pcSel = new Control ( "OPR", BYTE);
		spCtrl.add ( pcSel);
		psS = new OutControl ( "S0", parSize);
		spOut.add ( psS);
		bBs = new Bus ( this, psS, null, null);
		sbBus.add ( bBs);
		
		iSize = parSize;
	}

	private long testBytes ( int oper, long l1, long l2) {
		byte b1, b2;
		long ls;
		
		b1 = new Long ( l1).byteValue ( ); 
		b2 = new Long ( l2).byteValue ( ); 
		
		switch ( oper) {
			case LESSTHAN:
				if ( b1 < b2) ls = 1;
				else ls = 0;
				break;
			case EQUAL:
				if ( b1 == b2) ls = 1;
				else ls = 0;
				break;
			case NOTEQUAL:
				if ( b1 != b2) ls = 1;
				else ls = 0;
				break;
			case GREATERTHEN_OR_EQUAL_ZERO:
				if ( b1 >= 0) ls = 1;
				else ls = 0;
				break;
			case GREATERTHEN_ZERO:
				if ( b1 > 0) ls = 1;
				else ls = 0;
				break;
			case LESSTHEN_OR_EQUAL_ZERO:
				if ( b1 <= 0) ls = 1;
				else ls = 0;
				break;
			case LESSTHEN_ZERO:
				if ( b1 < 0) ls = 1;
				else ls = 0;
				break;
			default:
				ls = 0;
				break;
		}
		
		return ( ls);
	}

	private long testWords ( int oper, long l1, long l2) {
		int i1, i2;
		long ls;
		
		i1 = new Long ( l1).intValue ( ); 
		i2 = new Long ( l2).intValue ( ); 
		
		switch ( oper) {
			case LESSTHAN:
				if ( i1 < i2) ls = 1;
				else ls = 0;
				break;
			case EQUAL:
				if ( i1 == i2) ls = 1;
				else ls = 0;
				break;
			case NOTEQUAL:
				if ( i1 != i2) ls = 1;
				else ls = 0;
				break;
			case GREATERTHEN_OR_EQUAL_ZERO:
				if ( i1 >= 0) ls = 1;
				else ls = 0;
				break;
			case GREATERTHEN_ZERO:
				if ( i1 > 0) ls = 1;
				else ls = 0;
				break;
			case LESSTHEN_OR_EQUAL_ZERO:
				if ( i1 <= 0) ls = 1;
				else ls = 0;
				break;
			case LESSTHEN_ZERO:
				if ( i1 < 0) ls = 1;
				else ls = 0;
				break;
			default:
				ls = 0;
				break;
		}
		
		return ( ls);
	}	
	
	private long testDoubleWords ( int oper, long l1, long l2) {
		long ls;
		
		switch ( oper) {
			case LESSTHAN:
				if ( l1 < l2) ls = 1;
				else ls = 0;
				break;
			case EQUAL:
				if ( l1 == l2) ls = 1;
				else ls = 0;
				break;
			case NOTEQUAL:
				if ( l1 != l2) ls = 1;
				else ls = 0;
				break;
			case GREATERTHEN_OR_EQUAL_ZERO:
				if ( l1 >= 0) ls = 1;
				else ls = 0;
				break;
			case GREATERTHEN_ZERO:
				if ( l1 > 0) ls = 1;
				else ls = 0;
				break;
			case LESSTHEN_OR_EQUAL_ZERO:
				if ( l1 <= 0) ls = 1;
				else ls = 0;
				break;
			case LESSTHEN_ZERO:
				if ( l1 < 0) ls = 1;
				else ls = 0;
				break;
			default:
				ls = 0;
				break;
		}
		
		return ( ls);
	}	
	
	public void behavior ( ) {
		long [] lEt;
		int lSelt;
		long lSt;
		int i;

		lEt = new long [2];
		
		lSelt = pcSel.getWord ( );

		for ( i = 0; i < 2; i ++) {
			peE = ( InControl) spIn.traverse ( i);
			lEt [i] = peE.getDoubleWord ( );
		}
		
		//System.out.println ( "lEt [0] ="+ lEt [0]);
		//System.out.println ( "lEt [1] ="+ lEt [1]);
		
		switch ( iSize) {
			case BYTE:
				lSt = testBytes ( lSelt, lEt [0], lEt [1]);
				break;

			case HALFWORD:
				lSt = 0;
System.out.println ( "Err in Comparator class: operations for halfwords not implemented yet");
				break;
	
			case WORD:
				lSt = testWords ( lSelt, lEt [0], lEt [1]);
				break;
	
			default:
				lSt = testDoubleWords ( lSelt, lEt [0], lEt [1]);
				break;
		}
		
		//System.out.println ( "lSt ="+ lSt);
		
		psS.set ( lSt);
		
		if ( bBs.isLinked ( ) == true) bBs.behavior ( );
	}

	public void debug ( ) {
		System.out.println ( "*** Comparator.debug:...BEGIN");

		if ( spIn != null) spIn.debug ( );
		if ( spCtrl != null) spCtrl.debug ( );
		if ( spOut != null) spOut.debug ( );
		if ( spStt != null) spStt.debug ( );
		if ( sbBus != null) sbBus.debug ( );
		
		System.out.println ( "*** Comparator.debug:...END");
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
		System.out.println ( "Comparator: e um comparador");
		System.out.println ( "	Possui duas entradas E0 e E1 e uma saida S0");
		System.out.println ( "	Possui um controle C0");
		System.out.println ( "		C0 - 	seleciona o operador relacional a ser usado...");
		System.out.println ( "			0 - maior, igual ou menor");
		System.out.println ( "	Compara as entradas e gera a saida");
		System.out.println ( "	Para cria-lo: create <nome> 51 0 <bits> ");
		System.out.println ( );
	}

	public static void test ( ) {
		Comparator cmp1 = new Comparator ( "Comparator", HALFWORD);

		cmp1.set ( "E0", IN, 255);
		cmp1.set ( "E1", IN, 15);
		cmp1.set ( "C0", CONTROL, 0);
		cmp1.behavior ( );
		cmp1.list ( );
	}

	protected InControl peE;
	protected Control pcSel;
	protected OutControl psS;
	protected Bus bBs;
	protected int iSize;
}
