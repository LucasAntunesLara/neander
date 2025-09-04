package ports;

import primitive.Primitive;
import util.Define;
import util.SistNum;

public class Port extends Primitive implements Define {

	public Port ( String parName, int parType) {
		sbName = new StringBuffer ( ).append (parName);
		iTypePort = parType;
		iSize = WORD;
		iTypeValue = INTEGER;
	}

	public Port ( String parName, int parType, int parSize) {
		sbName = new StringBuffer ( ).append (parName);
		iTypePort = parType;
		iSize = parSize;
		iTypeValue = INTEGER;
	}

	public Port ( String parName, int parType, int parSize, int parTypeValue) {
		sbName = new StringBuffer ( ).append (parName);
		iTypePort = parType;
		iSize = parSize;
		iTypeValue = FP;
	}

	public int getType ( ) {
		return ( iTypePort);
	}

	public int getSize ( ) {
		return ( iSize);
	}

	public int getTypeValue ( ) {
		return ( iTypeValue);
	}

	public void set ( boolean parValue) {
		if ( parValue == true) lValue = 1;
		else lValue = 0;
	}

	public void set ( byte parValue) {
		lValue = new Byte ( parValue).longValue ( );
	}

	public void set ( short parValue) {
		lValue = new Short ( parValue).longValue ( );
	}

	public void set ( int parValue) {
		lValue = new Integer ( parValue).longValue ( );
	}
	
	public void set ( long parValue) {
		lValue = parValue;
	}

	public void set ( float parValue) {
		lValue = ( long) Float.floatToIntBits(parValue);
	}

	public void set ( double parValue) {
		lValue = Double.doubleToLongBits(parValue);
	}

	public boolean getBit ( ) {
		if ( lValue == 0) return false;
		else return true;
	}

	public byte getByte ( ) {
		return ( new Long ( lValue).byteValue ( ));
	}

	public short getHalfWord ( ) {
		return ( new Long ( lValue).shortValue ( ));
	}

	public int getWord ( ) {
		return ( new Long ( lValue).intValue ( ));
	}

	public long getDoubleWord ( ) {
		return ( lValue);
	}

	public float getFloat ( ) {
		return ( Float.intBitsToFloat(new Long(lValue).intValue()));
	}

	public double getDouble ( ) {
		return ( Double.longBitsToDouble(lValue));
	}

	public void debug ( ) {
//		System.out.println ( 	"** porta.debug: ...BEGIN");
		System.out.print ( "* porta.debug: name: "+sbName);
		System.out.print ( ", type: "+iTypePort+", size: ");
		if ( iTypeValue == INTEGER) {
			System.out.println ( iSize+" and value: "+SistNum.toHexString ( lValue, iSize));
		} else if ( iTypeValue == FP) {
			double d = getDouble();
			System.out.println ( iSize+" and value: "+d);
		}
//		System.out.println ( 	"** porta.debug: ...END");	
//		System.out.println ( );		
	}

	public void list ( ) {
		String sTmp;
		int iTmp = 0;
		
		if ( iTypeValue == INTEGER) {
			sTmp = sbName + " = " + SistNum.toHexString ( lValue, iSize);
			iTmp = sTmp.length ( );
			System.out.print ( sTmp);
		} else if ( iTypeValue == FP) {
			double d = getDouble();
			sTmp = sbName + " = " + d;
			iTmp = sTmp.length ( );
			System.out.print ( sTmp);
		}

		switch ( iTypePort) {
			case IN:
				sTmp = "Entrada";
				break;
				
			case OUT:
				sTmp = "Saida";
				break;

			case CONTROL:
				sTmp = "Controle";
				break;
/*	Temporario: parece desnecessario este codigo
			case FIELD:
				sTmp = "Field";
				break;
								
			case STRING:
				sTmp = "Field String";
				break;
								
			case STATUS:
				sTmp = "Status";
				break;
*/
			default:
				sTmp = "Undefined";
				break;
		}
		
		for ( int i = 0; i < COLUNAS - iTmp; i ++) System.out.print ( " ");
		System.out.println ( sTmp + " de " + iSize + " bits.");
	}

	public static void test ( ) {
		Port pTeste = new Port ( "tst", IN, DOUBLEWORD);
		
		pTeste.set ( -15);
		pTeste.debug ( );
		pTeste.iSize = WORD;
		pTeste.debug ( );
		pTeste.iSize = HALFWORD;
		pTeste.debug ( );
		pTeste.iSize = BYTE;
		pTeste.debug ( );
		pTeste.iSize = BIT;
		pTeste.debug ( );
	}

	protected int iTypePort, iSize, iTypeValue;
	protected long lValue;
}
