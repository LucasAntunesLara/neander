package contents;

import java.util.Hashtable;

import platform.Lang;
import processor.Processor;
import util.Define;
import util.SistNum;

public class Contents implements Define {

	public Contents ( int parX, int parSize, int parTypeValue) {
		htVector = new Hashtable ( );
		htChanged = new Hashtable ( );
		htInserted = new Hashtable ( );
		iSizeX = parX;
		iSizeY = 0;
		iSizeZ = 0;
		iSizeBits = parSize;
		iTypeValue = parTypeValue;
	}

	public Contents ( int parX, int parSize, int parTypeValue, long parInitialValue) {
		htVector = new Hashtable ( );
		htChanged = new Hashtable ( );
		htInserted = new Hashtable ( );
		iSizeX = parX;
		iSizeY = 0;
		iSizeZ = 0;
		iSizeBits = parSize;
		iTypeValue = parTypeValue;
		lInitialValue = parInitialValue;
	}

	public Contents ( int parX, int parSize, int parTypeValue, double parInitialValue) {
		htVector = new Hashtable ( );
		htChanged = new Hashtable ( );
		htInserted = new Hashtable ( );
		iSizeX = parX;
		iSizeY = 0;
		iSizeZ = 0;
		iSizeBits = parSize;
		iTypeValue = parTypeValue;
		dInitialValue = parInitialValue;
	}

	public void set ( boolean parValue, int parX) {
		long lValue = 0;
		if ( parValue == true) lValue = 1;
		
		if ( parX < 0 || parX > iSizeX) {
			Processor.setMessageError ( Lang.iLang==ENGLISH?Lang.msgsGUI[182]:Lang.msgsGUI[192]+parX);
		}
				
		if ( existIndex ( parX)) htChanged.put ( new Long ( parX), new Long ( parX));
		else htInserted.put ( new Long ( parX), new Long ( parX));
		htVector.put ( new Long ( parX), new Long ( lValue));
		
	}

	public void set ( byte parValue, int parX) {
		long lValue = new Byte ( parValue).longValue ( );
		
		if ( parX < 0 || parX > iSizeX) {
			Processor.setMessageError ( Lang.iLang==ENGLISH?Lang.msgsGUI[182]:Lang.msgsGUI[192]+parX);
		}
		
		if ( existIndex ( parX)) htChanged.put ( new Long ( parX), new Long ( parX));
		else htInserted.put ( new Long ( parX), new Long ( parX));
		htVector.put ( new Long ( parX), new Long ( lValue));
	}

	public void set ( short parValue, int parX) {
		long lValue = new Short ( parValue).longValue ( );

		if ( parX < 0 || parX > iSizeX) {
			Processor.setMessageError ( Lang.iLang==ENGLISH?Lang.msgsGUI[182]:Lang.msgsGUI[192]+parX);
		}

		if ( existIndex ( parX)) htChanged.put ( new Long ( parX), new Long ( parX));
		else htInserted.put ( new Long ( parX), new Long ( parX));
		htVector.put ( new Long ( parX), new Long ( lValue));
	}

	public void set ( int parValue, int parX) {
		long lValue = new Integer ( parValue).longValue ( );
		
		if ( parX < 0 || parX > iSizeX) {
			Processor.setMessageError ( Lang.iLang==ENGLISH?Lang.msgsGUI[182]:Lang.msgsGUI[192]+parX);
		}

		if ( existIndex ( parX)) htChanged.put ( new Long ( parX), new Long ( parX));
		else htInserted.put ( new Long ( parX), new Long ( parX));
		htVector.put ( new Long ( parX), new Long ( lValue));
	}

	public void set ( long parValue, int parX) {
		if ( parX < 0 || parX > iSizeX) {
			Processor.setMessageError ( Lang.iLang==ENGLISH?Lang.msgsGUI[182]:Lang.msgsGUI[192]+parX);
		}

		if ( existIndex ( parX)) htChanged.put ( new Long ( parX), new Long ( parX));
		else htInserted.put ( new Long ( parX), new Long ( parX));
		htVector.put ( new Long ( parX), new Long ( parValue));
	}

	public void set ( float parValue, int parX) {
		double  dValue = new Float ( parValue).doubleValue();
		
		if ( parX < 0 || parX > iSizeX) {
			Processor.setMessageError ( Lang.iLang==ENGLISH?Lang.msgsGUI[182]:Lang.msgsGUI[192]+parX);
		}

		if ( existIndex ( parX)) htChanged.put ( new Long ( parX), new Long ( parX));
		else htInserted.put ( new Long ( parX), new Long ( parX));
		htVector.put ( new Long ( parX), new Double ( parValue));
	}
	
	public void set ( double parValue, int parX) {
		if ( parX < 0 || parX > iSizeX) {
			Processor.setMessageError ( Lang.iLang==ENGLISH?Lang.msgsGUI[182]:Lang.msgsGUI[192]+parX);
		}

		if ( existIndex ( parX)) htChanged.put ( new Long ( parX), new Long ( parX));
		else htInserted.put ( new Long ( parX), new Long ( parX));
		htVector.put ( new Long ( parX), new Double ( parValue));
	}
	
	public boolean existIndex ( int parX) {
		if ( htVector != null) return htVector.containsKey( new Long ( parX));
		else return htMatrix.containsKey( new Long ( parX));
		/*Long lTmp = (Long) htVector.get ( new Long ( parX));
		if ( lTmp == null) return ( false);
		else return true;*/	
	}
	
	public boolean getBit ( int parX) {
		if ( parX < 0 || parX > iSizeX) {
			Processor.setMessageError ( Lang.iLang==ENGLISH?Lang.msgsGUI[182]:Lang.msgsGUI[192]+parX);
		}

		Long lTmp = (Long) htVector.get ( new Long ( parX));
		if ( lTmp == null) return ( false);
		else {
			if ( lTmp.longValue() == 0) return false;
			else return true;
		}
	}

	public byte getByte ( int parX) {
		if ( parX < 0 || parX > iSizeX) {
			Processor.setMessageError ( Lang.iLang==ENGLISH?Lang.msgsGUI[182]:Lang.msgsGUI[192]+parX);
		}

		Long lTmp = (Long) htVector.get ( new Long ( parX));
		if ( lTmp == null) return ( new Long ( lInitialValue).byteValue());
		else return lTmp.byteValue();
	}

	public short getHalfWord ( int parX) {
		if ( parX < 0 || parX > iSizeX) {
			Processor.setMessageError ( Lang.iLang==ENGLISH?Lang.msgsGUI[182]:Lang.msgsGUI[192]+parX);
		}

		Long lTmp = (Long) htVector.get ( new Long ( parX));
		if ( lTmp == null) return ( new Long ( lInitialValue).shortValue());
		else return lTmp.shortValue();
	}

	public int getWord ( int parX) {
		if ( parX < 0 || parX > iSizeX) {
			Processor.setMessageError ( Lang.iLang==ENGLISH?Lang.msgsGUI[182]:Lang.msgsGUI[192]+parX);
		}

		Long lTmp = (Long) htVector.get ( new Long ( parX));
		if ( lTmp == null) return ( new Long ( lInitialValue).intValue());
		else return lTmp.intValue();
	}

	public long getDoubleWord ( int parX) {
		if ( parX < 0 || parX > iSizeX) {
			Processor.setMessageError ( Lang.iLang==ENGLISH?Lang.msgsGUI[182]:Lang.msgsGUI[192]+parX);
		}

		Long lTmp = (Long) htVector.get ( new Long ( parX));
		if ( lTmp == null) return ( lInitialValue);
		else return lTmp.longValue();
	}

	public float getFloat ( int parX) {
		if ( parX < 0 || parX > iSizeX) {
			Processor.setMessageError ( Lang.iLang==ENGLISH?Lang.msgsGUI[182]:Lang.msgsGUI[192]+parX);
		}

		Double dTmp = (Double) htVector.get ( new Long ( parX));
		if ( dTmp == null) return ( new Double (dInitialValue).floatValue ( ));
		else return dTmp.floatValue();
	}

	public double getDouble ( int parX) {
		if ( parX < 0 || parX > iSizeX) {
			Processor.setMessageError ( Lang.iLang==ENGLISH?Lang.msgsGUI[182]:Lang.msgsGUI[192]+parX);
		}

		Double dTmp = (Double) htVector.get ( new Long ( parX));
		if ( dTmp == null) return ( dInitialValue);
		else return dTmp.doubleValue();
	}

	public Contents ( int parX, int parY, int parSize, int parTypeValue) {
		htMatrix = new Hashtable ( );
		htChanged = new Hashtable ( );
		htInserted = new Hashtable ( );
		iSizeX = parX;
		iSizeY = parY;
		iSizeZ = 0;
		iSizeBits = parSize;
		iTypeValue = parTypeValue;
	}

	public Contents ( int parX, int parY, int parSize, int parTypeValue, long parInitialValue) {
		htMatrix = new Hashtable ( );
		htChanged = new Hashtable ( );
		htInserted = new Hashtable ( );
		iSizeX = parX;
		iSizeY = parY;
		iSizeZ = 0;
		iSizeBits = parSize;
		iTypeValue = parTypeValue;
		lInitialValue = parInitialValue;
	}

	public Contents ( int parX, int parY, int parSize, int parTypeValue, double parInitialValue) {
		htMatrix = new Hashtable ( );
		htChanged = new Hashtable ( );
		htInserted = new Hashtable ( );
		iSizeX = parX;
		iSizeY = parY;
		iSizeZ = 0;
		iSizeBits = parSize;
		iTypeValue = parTypeValue;
		dInitialValue = parInitialValue;
	}

	public void set ( boolean parValue, int parX, int parY) {
		long lValue = 0;
		Hashtable htAux;

		if ( parValue == true) lValue = 1;

		if ( parX < 0 || parX > iSizeX || parY < 0 || parY > iSizeY) {
			Processor.setMessageError ( Lang.iLang==ENGLISH?Lang.msgsGUI[182]:Lang.msgsGUI[192]+parX+","+parY);
		}

		if ( existIndex ( parX)) htChanged.put ( new Long ( parX), new Long ( parX));
		else htInserted.put ( new Long ( parX), new Long ( parX));

		htAux = (Hashtable) htMatrix.get ( new Long ( parX));
		if ( htAux == null) {
			htAux = new Hashtable ( );
			htMatrix.put ( new Long ( parX), htAux);
		}
		htAux.put ( new Long ( parY), new Long ( lValue));
	}

	public void set ( byte parValue, int parX, int parY) {
		long lValue = new Byte ( parValue).longValue ( );
		Hashtable htAux;
		
		if ( parX < 0 || parX > iSizeX || parY < 0 || parY > iSizeY) {
			Processor.setMessageError ( Lang.iLang==ENGLISH?Lang.msgsGUI[182]:Lang.msgsGUI[192]+parX+","+parY);
		}
		
		if ( existIndex ( parX)) htChanged.put ( new Long ( parX), new Long ( parX));
		else htInserted.put ( new Long ( parX), new Long ( parX));
		
		htAux = (Hashtable) htMatrix.get ( new Long ( parX));
		if ( htAux == null) {
			htAux = new Hashtable ( );
			htMatrix.put ( new Long ( parX), htAux);
		}
		htAux.put ( new Long ( parY), new Long ( lValue));
	}

	public void set ( short parValue, int parX, int parY) {
		long lValue = new Short ( parValue).longValue ( );
		Hashtable htAux;

		if ( parX < 0 || parX > iSizeX || parY < 0 || parY > iSizeY) {
			Processor.setMessageError ( Lang.iLang==ENGLISH?Lang.msgsGUI[182]:Lang.msgsGUI[192]+parX+","+parY);
		}
		
		if ( existIndex ( parX)) htChanged.put ( new Long ( parX), new Long ( parX));
		else htInserted.put ( new Long ( parX), new Long ( parX));
		
		htAux = (Hashtable) htMatrix.get ( new Long ( parX));
		if ( htAux == null) {
			htAux = new Hashtable ( );
			htMatrix.put ( new Long ( parX), htAux);
		}
		htAux.put ( new Long ( parY), new Long ( lValue));
	}

	public void set ( int parValue, int parX, int parY) {
		long lValue = new Integer ( parValue).longValue ( );
		Hashtable htAux;

		if ( parX < 0 || parX > iSizeX || parY < 0 || parY > iSizeY) {
			Processor.setMessageError ( Lang.iLang==ENGLISH?Lang.msgsGUI[182]:Lang.msgsGUI[192]+parX+","+parY);
		}
		
		if ( existIndex ( parX)) htChanged.put ( new Long ( parX), new Long ( parX));
		else htInserted.put ( new Long ( parX), new Long ( parX));
		
		htAux = (Hashtable) htMatrix.get ( new Long ( parX));
		if ( htAux == null) {
			htAux = new Hashtable ( );
			htMatrix.put ( new Long ( parX), htAux);
		}
		htAux.put ( new Long ( parY), new Long ( lValue));
	}

	public void set ( long parValue, int parX, int parY) {
		Hashtable htAux;

		if ( parX < 0 || parX > iSizeX || parY < 0 || parY > iSizeY) {
			Processor.setMessageError ( Lang.iLang==ENGLISH?Lang.msgsGUI[182]:Lang.msgsGUI[192]+parX+","+parY);
		}
		
		if ( existIndex ( parX)) htChanged.put ( new Long ( parX), new Long ( parX));
		else htInserted.put ( new Long ( parX), new Long ( parX));
		
		htAux = (Hashtable) htMatrix.get ( new Long ( parX));
		if ( htAux == null) {
			htAux = new Hashtable ( );
			htMatrix.put ( new Long ( parX), htAux);
		}
		htAux.put ( new Long ( parY), new Long ( parValue));
	}

	public void set ( float parValue, int parX, int parY) {
		Hashtable htAux;
		double  dValue = new Float ( parValue).doubleValue();
		
		if ( parX < 0 || parX > iSizeX || parY < 0 || parY > iSizeY) {
			Processor.setMessageError ( Lang.iLang==ENGLISH?Lang.msgsGUI[182]:Lang.msgsGUI[192]+parX+","+parY);
		}
		
		if ( existIndex ( parX)) htChanged.put ( new Long ( parX), new Long ( parX));
		else htInserted.put ( new Long ( parX), new Long ( parX));
		
		htAux = (Hashtable) htMatrix.get ( new Long ( parX));
		if ( htAux == null) {
			htAux = new Hashtable ( );
			htMatrix.put ( new Long ( parX), htAux);
		}
		htAux.put ( new Long ( parY), new Double ( dValue));
	}
	
	public void set ( double parValue, int parX, int parY) {
		Hashtable htAux;
		
		if ( parX < 0 || parX > iSizeX || parY < 0 || parY > iSizeY) {
			Processor.setMessageError ( Lang.iLang==ENGLISH?Lang.msgsGUI[182]:Lang.msgsGUI[192]+parX+","+parY);
		}

		if ( existIndex ( parX)) htChanged.put ( new Long ( parX), new Long ( parX));
		else htInserted.put ( new Long ( parX), new Long ( parX));
		
		htAux = (Hashtable) htMatrix.get ( new Long ( parX));
		if ( htAux == null) {
			htAux = new Hashtable ( );
			htMatrix.put ( new Long ( parX), htAux);
		}
		htAux.put ( new Long ( parY), new Double ( parValue));
	}

	public boolean existIndex ( int parX, int parY) {
		Hashtable htAux;
		
		htAux = (Hashtable) htMatrix.get ( new Long ( parX));
		if ( htAux != null) {
			Long lTmp = (Long) htAux.get ( new Long ( parY));
			if ( lTmp == null) return ( false);
			else return true;
		} else return false;
	}

	public boolean getBit ( int parX, int parY) {
		Hashtable htAux;
		
		if ( parX < 0 || parX > iSizeX || parY < 0 || parY > iSizeY) {
			Processor.setMessageError ( Lang.iLang==ENGLISH?Lang.msgsGUI[182]:Lang.msgsGUI[192]+parX+","+parY);
		}
		
		htAux = (Hashtable) htMatrix.get ( new Long ( parX));
		if ( htAux != null) {
			Long lTmp = (Long) htAux.get ( new Long ( parY));
			if ( lTmp == null) return ( false);
			else {
				if ( lTmp.longValue() == 0) return false;
				else return true;
			}
		}
		
		return false;
	}

	public byte getByte ( int parX, int parY) {
		Hashtable htAux;
		
		if ( parX < 0 || parX > iSizeX || parY < 0 || parY > iSizeY) {
			Processor.setMessageError ( Lang.iLang==ENGLISH?Lang.msgsGUI[182]:Lang.msgsGUI[192]+parX+","+parY);
		}
		
		htAux = (Hashtable) htMatrix.get ( new Long ( parX));
		if ( htAux != null) {
			Long lTmp = (Long) htAux.get ( new Long ( parY));
			if ( lTmp == null) return ( new Long ( lInitialValue).byteValue());
			else return lTmp.byteValue();
		}
		
		return 0;
	}

	public short getHalfWord ( int parX, int parY) {
		Hashtable htAux;
		
		if ( parX < 0 || parX > iSizeX || parY < 0 || parY > iSizeY) {
			Processor.setMessageError ( Lang.iLang==ENGLISH?Lang.msgsGUI[182]:Lang.msgsGUI[192]+parX+","+parY);
		}
		
		htAux = (Hashtable) htMatrix.get ( new Long ( parX));
		if ( htAux != null) {
			Long lTmp = (Long) htAux.get ( new Long ( parY));
			if ( lTmp == null) return ( new Long ( lInitialValue).shortValue());
			else return lTmp.shortValue();
		}
		
		return 0;
	}

	public int getWord ( int parX, int parY) {
		Hashtable htAux;
		
		if ( parX < 0 || parX > iSizeX || parY < 0 || parY > iSizeY) {
			Processor.setMessageError ( Lang.iLang==ENGLISH?Lang.msgsGUI[182]:Lang.msgsGUI[192]+parX+","+parY);
		}

		htAux = (Hashtable) htMatrix.get ( new Long ( parX));
		if ( htAux != null) {
			Long lTmp = (Long) htAux.get ( new Long ( parY));
			if ( lTmp == null) return ( new Long ( lInitialValue).intValue());
			else return lTmp.intValue();
		}
		
		return 0;		
	}

	public long getDoubleWord ( int parX, int parY) {
		Hashtable htAux;
		
		if ( parX < 0 || parX > iSizeX || parY < 0 || parY > iSizeY) {
			Processor.setMessageError ( Lang.iLang==ENGLISH?Lang.msgsGUI[182]:Lang.msgsGUI[192]+parX+","+parY);
		}

		htAux = (Hashtable) htMatrix.get ( new Long ( parX));
		if ( htAux != null) {
			Long lTmp = (Long) htAux.get ( new Long ( parY));
			if ( lTmp == null) return ( lInitialValue);
			else return lTmp.longValue();
		}
		
		return 0;		
	}

	public float getFloat ( int parX, int parY) {
		Hashtable htAux;
		
		if ( parX < 0 || parX > iSizeX || parY < 0 || parY > iSizeY) {
			Processor.setMessageError ( Lang.iLang==ENGLISH?Lang.msgsGUI[182]:Lang.msgsGUI[192]+parX+","+parY);
		}
		
		htAux = (Hashtable) htMatrix.get ( new Long ( parX));
		if ( htAux != null) {
			Double dTmp = (Double) htAux.get ( new Long ( parY));
			if ( dTmp == null) return ( new Double (dInitialValue).floatValue ( ));
			else return dTmp.floatValue();
		}
		
		return 0;	
	}

	public double getDouble ( int parX, int parY) {
		Hashtable htAux;
		
		if ( parX < 0 || parX > iSizeX || parY < 0 || parY > iSizeY) {
			Processor.setMessageError ( Lang.iLang==ENGLISH?Lang.msgsGUI[182]:Lang.msgsGUI[192]+parX+","+parY);
		}
		
		htAux = (Hashtable) htMatrix.get ( new Long ( parX));
		if ( htAux != null) {
			Double dTmp = (Double) htAux.get ( new Long ( parY));
			if ( dTmp == null) return ( dInitialValue);
			else return dTmp.doubleValue();
		}
		
		return 0;
	}

	public void debug ( ) {

	}

	public long getIntegerValue ( int index) {
		long lValue;
		
		switch ( getSize ( )) {
			case BIT:
				if ( getBit ( index)==true) lValue = 1; else lValue = 0;
				break;
			case BYTE:
				lValue = getByte(index);
				break;
			case HALFWORD:
				lValue = getHalfWord(index);
				break;
			case WORD:
				lValue = getWord(index);
				break;
			case DOUBLEWORD:
				lValue = getDoubleWord(index);
				break;
			default:
				lValue = getWord(index);
		}
		
		return ( lValue);
	}

	public double getFloatingPointValue ( int index) {
		double dValue;
		float fValue;
		
		switch ( getSize ( )) {
			case WORD:
				fValue = getFloat(index);
				dValue = (double) fValue;
				break;
			case DOUBLEWORD:
				dValue = getDouble(index);
				break;
			default:
				fValue = getWord(index);
				dValue = (double) fValue;
		}
		
		return ( dValue);
	}

	public long getIntegerValue2D ( int index, int indey) {
		long lValue;
		
		switch ( getSize ( )) {
			case BIT:
				if ( getBit ( index, indey)==true) lValue = 1; else lValue = 0;
				break;
			case BYTE:
				lValue = getByte(index, indey);
				break;
			case HALFWORD:
				lValue = getHalfWord(index, indey);
				break;
			case WORD:
				lValue = getWord(index, indey);
				break;
			case DOUBLEWORD:
				lValue = getDoubleWord(index, indey);
				break;
			default:
				lValue = getWord(index, indey);
		}
		
		return ( lValue);
	}

	public double getFloatingPointValue2D ( int index, int indey) {
		double dValue;
		float fValue;
		
		switch ( getSize ( )) {
			case WORD:
				fValue = getFloat(index, indey);
				dValue = (double) fValue;
				break;
			case DOUBLEWORD:
				dValue = getDouble(index, indey);
				break;
			default:
				fValue = getWord(index, indey);
				dValue = (double) fValue;
		}
		
		return ( dValue);
	}

	public void list ( ) {
		long lValue;
		double dValue;
		int i, j;

		System.out.println ( "\n");
		if ( iSizeY != 0) {
			for ( i = 0; i < iSizeX; i ++) {
				System.out.println("Linha: "+i);
				for ( j = 0; j < iSizeY; j ++) {
					if ( iTypeValue == INTEGER) {
						lValue = getIntegerValue2D ( i, j);
						System.out.print ( j+" -> conteudo: "+ SistNum.toHexString ( lValue, iSizeBits)+"\t");
					} else if ( iTypeValue == FP) {
						dValue = getFloatingPointValue2D ( i, j);
						System.out.print ( j+" -> conteudo: "+ dValue+"\t");
					}
					
				}
				System.out.println ( );
			}
		} else {
				for ( i = 0; i < iSizeX; i ++) {
					if ( iTypeValue == INTEGER) {
						lValue = getIntegerValue ( i);
						System.out.println ( i+" -> conteudo: "+ SistNum.toHexString ( lValue, iSizeBits));
					} else if ( iTypeValue == FP) {
						dValue = getFloatingPointValue ( i);
						System.out.println ( i+" -> conteudo: "+ dValue+"\t");					
					}
				}			
		}
	}

	public int getSize ( ) {
		return ( iSizeBits);
	}

	public int getSizeX ( ) {
		return ( iSizeX);
	}

	public int getSizeY ( ) {
		return ( iSizeY);
	}

	public int getSizeZ ( ) {
		return ( iSizeZ);
	}

	public int getTypeValue ( ) {
		return ( iTypeValue);
	}

	public Hashtable getContents1d ( ) {
		return htVector;
	}
	
	public Hashtable getContents2d ( ) {
		return htMatrix;
	}

	public Hashtable getChangedKeys ( ) {
		return htChanged;
	}

	private void resetChangedKeys ( ) {
		htChanged = new Hashtable ( );
	}

	public Hashtable getInsertedKeys ( ) {
		return htInserted;
	}

	private void resetInsertedKeys ( ) {
		htInserted = new Hashtable ( );
	}

	public void resetContents ( ) {
		resetChangedKeys ( );
		resetInsertedKeys ( );
	}

	public static void test ( ) {
		Contents cTeste = new Contents ( 1000000, 3, BYTE, INTEGER);
		
		cTeste.set ( (byte) 1, 0, 0);
		cTeste.set ( (byte) 15, 0, 1);
		cTeste.set ( (byte) -15, 0, 2);
		cTeste.set ( (byte) 15, 999999, 2);
		cTeste.list ( );
	}

	private long [] lVector;
	private long [][] lMatrix2d;
	private long [][][] lMatrix3d;
	private int iSizeBits, iSizeX, iSizeY, iSizeZ, iTypeValue;
	private long lInitialValue = 0;
	private double dInitialValue = 0.0;
	private Hashtable htVector, htMatrix, htChanged, htInserted;
}
