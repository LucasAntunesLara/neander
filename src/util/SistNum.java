package util;

import platform.Lang;
import processor.Processor;


public class SistNum implements Define {
	public static String toHexString ( long parValue, int parTam) {
		String sAux;
		StringBuffer sbAux = new StringBuffer ( );
		int iHexTam, iTam, i;
		
		if ( 	parTam != DOUBLEWORD && parTam != WORD && parTam != HALFWORD && 
				parTam != BYTE && parTam != NIBBLE && parTam != BIT)
					return ( null);
		
		sAux = Long.toHexString ( parValue);
		iTam = MAXTAMHEX - sAux.length ( );
		iHexTam = parTam / 4;
		if ( iHexTam == 0) iHexTam = 1;

		if ( iTam > 0) {
			for ( i = 0; i < iTam; i ++) sbAux.append ( "0");
		}
		sbAux.append ( sAux);
	
		return ( sbAux.toString ( ).substring ( MAXTAMHEX - iHexTam, MAXTAMHEX)+"h");
	}

	public static String toBinString ( long parValue, int parTam) {
		String sAux;
		StringBuffer sbAux = new StringBuffer ( );
		int iHexTam, iTam, i;
		
		if ( 	parTam != DOUBLEWORD && parTam != WORD && parTam != HALFWORD && 
				parTam != BYTE && parTam != NIBBLE && parTam != BIT)
					return ( null);
		
		sAux = Long.toBinaryString ( parValue);

		iTam = MAXTAMBIN - sAux.length ( );

		if ( iTam > 0) {
			for ( i = 0; i < iTam; i ++) sbAux.append ( "0");
		}
		sbAux.append ( sAux);

		return ( sbAux.toString ( ).substring ( MAXTAMBIN - parTam, MAXTAMBIN)+"b");
	}

	public static long binToInt ( String parBinario) {
		long lTotal;
		int iTam, iPeso, iPos, i;
		String sAux;
		
		iTam = parBinario.length ( );
		lTotal = 0L;
		iPeso = iTam - 1;
		iPos = 0;
		
		for ( i = 0; i < iTam; i ++) {
			sAux = parBinario.substring ( iPos, iPos + 1);
			if ( sAux.equals ( "1")) {
				lTotal = lTotal + Util.potencia ( 2, iPeso);
			} else if ( sAux.equals ( "0") == false) {
				int iValue = Integer.parseInt ( sAux);
			}
			iPeso --;
			iPos ++;
		}
		
		return ( lTotal);
	}

	public static long hexToInt ( String parHexa) {
		long lTotal;
		int iTam, iPeso, iPos, i;
		
		iTam = parHexa.length ( );
		lTotal = 0L;
		iPeso = iTam - 1;
		iPos = 0;
		
		for ( i = 0; i < iTam; i ++) {
			int iValue;
			String sAux;
			
			sAux = parHexa.substring ( iPos, iPos + 1);
			if ( sAux.equals ( "A") || sAux.equals ( "a")) iValue = 10;
			else if ( sAux.equals ( "B") || sAux.equals ( "b")) iValue = 11;
			else if ( sAux.equals ( "C") || sAux.equals ( "c")) iValue = 12;
			else if ( sAux.equals ( "D") || sAux.equals ( "d")) iValue = 13;
			else if ( sAux.equals ( "E") || sAux.equals ( "e")) iValue = 14;
			else if ( sAux.equals ( "F") || sAux.equals ( "f")) iValue = 15;
			else iValue = Integer.parseInt ( sAux);
			
			lTotal = lTotal + ( Util.potencia ( 16, iPeso) * iValue);
			iPeso --;
			iPos ++;
		}
		
		return ( lTotal);
	}

	private static boolean inHexFormat ( String parNumber) {
		if ( parNumber.endsWith ( "h") || parNumber.endsWith ( "H")) {
//			System.out.println ( "Em formato hexa: " + parNumber);
			if ( parNumber.length ( ) == 1) return false;
			else return true;
		}
		else return false;
	}

	private static boolean inDecimalFormat ( String parNumber) {
		if ( parNumber.endsWith ( "d") || parNumber.endsWith ( "D")) {
//			System.out.println ( "Em formato decimal: " + parNumber);
			if ( parNumber.length ( ) == 1) return false;
			else return true;
		}
		else return false;
	}

	private static boolean inBinFormat ( String parNumber) {
		if ( parNumber.endsWith ( "b") || parNumber.endsWith ( "B")) {
//			System.out.println ( "Em formato binario: " + parNumber);
			if ( parNumber.length ( ) == 1) return false;
			else return true;
		}
		else return false;
	}

	public static void setDefaultNumberFormat ( String parFormat) {
		switch ( parFormat.charAt ( 0)) {
			case 'h':
			case 'H':
				DefaultNumberFormat = HEXADECIMAL;
				break;	

			case 'b':
			case 'B':
				DefaultNumberFormat = BINARY;
				break;	

			case 'd':
			case 'D':
				DefaultNumberFormat = DECIMAL;
				break;
				
			default:
				break;
		}
	}
	
	public static void setDefaultNumberFormat ( int parFormat) {
		DefaultNumberFormat = parFormat;
		if ( DefaultNumberFormat != HEXADECIMAL && DefaultNumberFormat != BINARY && DefaultNumberFormat != DECIMAL)
			DefaultNumberFormat = DECIMAL;
	}

	public static int getDefaultNumberFormat ( ) {
		return ( DefaultNumberFormat);
	}

	public static void setDefaultSizeFormat ( int parSizeFormat) {
		DefaultSizeFormat = parSizeFormat;
	}

	public static int getDefaultSizeFormat ( ) {
		return ( DefaultSizeFormat);
	}

	public static long getValue ( String parValue) {
		long lValue = 0;
		int iTam = parValue.length ( );;
		String sAux = parValue.substring ( 0, iTam - 1);;

		//System.out.println ( parValue);
		
		try	{
			if ( inHexFormat ( parValue)) {
				lValue = hexToInt ( sAux);
			} else if ( inDecimalFormat ( parValue)) {
				lValue = Long.parseLong(sAux);
			} else if ( inBinFormat ( parValue)) {
				lValue = binToInt ( sAux);
			} else {
				switch ( DefaultNumberFormat) {
					case DECIMAL:
						lValue = Long.parseLong(parValue);
						break;

					case BINARY:
						lValue = binToInt ( parValue);
						break;

					case HEXADECIMAL:
						lValue = hexToInt ( parValue);
						break;
					
					default:
						break;
				}
			}
		} catch ( Exception e) {
			try { // para valores entrados em ponto flutuante
				parValue = parValue.replaceFirst( ",", ".");
				//System.out.println ( parValue);
				double d = Double.parseDouble( parValue);
				lValue = Double.doubleToLongBits(d);
			} catch ( Exception e2) {
				System.out.println ( "Erro na conversao!!!");
				Processor.setMessageError ( Lang.iLang==ENGLISH?Lang.msgsGUI[183]:Lang.msgsGUI[193]);
			}
		} finally { }
		
		return ( lValue);
	}

	public static String printInformation ( long lValue, int iType) {
		if ( iType == INTEGER) {
			if ( getDefaultNumberFormat() == DECIMAL) {
				switch ( DefaultSizeFormat){
					case DOUBLEWORD:
						return new Long ( lValue).toString ( );
					case WORD:
					case HALFWORD:
						int iWord = new Long (lValue).intValue();
						return new Integer ( iWord).toString ( );						
					case BYTE:
						byte bByte = new Long (lValue).byteValue();
						return new Byte ( bByte).toString ( );	
				}
			}
			else if ( getDefaultNumberFormat() == BINARY) {
				switch ( DefaultSizeFormat){
					case DOUBLEWORD:
						return ( Long.toBinaryString ( lValue)+"b");
					case WORD:
					case HALFWORD:
						int iWord = new Long (lValue).intValue();
						return Integer.toBinaryString ( iWord)+"b";						
					case BYTE:
						byte bByte = new Long (lValue).byteValue();
						return toBinString ( lValue, BYTE);	
				}
			}
			else if ( getDefaultNumberFormat() == HEXADECIMAL) {
				switch ( DefaultSizeFormat){
					case DOUBLEWORD:
						return ( Long.toHexString ( lValue)+"h");
					case WORD:
					case HALFWORD:
						int iWord = new Long (lValue).intValue();
						return Integer.toHexString ( iWord)+"h";	
					case BYTE:
						byte bByte = new Long (lValue).byteValue();
						return toHexString ( lValue, BYTE);		
					}				
			}
		} else if ( iType == FP) {
			if ( getDefaultNumberFormat() == DECIMAL) {
				double d = Double.longBitsToDouble(lValue);
				String sTmp = ""+d;
				//if ( d != 0) System.out.println ( "FP = "+sTmp);
				return sTmp;
			}
			else if ( getDefaultNumberFormat() == BINARY) return ( Long.toBinaryString ( lValue)+"b");
			else if ( getDefaultNumberFormat() == HEXADECIMAL) return ( Long.toHexString ( lValue)+"h");			
		}
		
		return null;
	}

/*
 * 	METODOS PARA A MANIPULACAO DE BITS
 */
	public static int getBitRange ( int iValue, int fBit, int lBit) {
		int iReferenceValue = - 1, iLength = lBit - fBit + 1;
		int iRetValue;
		
		iReferenceValue = ~ ( iReferenceValue <<  iLength);
		iValue = iValue >> 31 - lBit;
		iRetValue = iReferenceValue & iValue;
		//System.out.println ( Integer.toBinaryString ( iReferenceValue));
		//System.out.println ( Integer.toBinaryString ( iValue));
		//System.out.println ( Integer.toBinaryString ( iRetValue));
		
		return ( iRetValue);
	}

	public static long getBitRange ( long lValue, int fBit, int lBit) {
		long lReferenceValue = - 1, iLength = lBit - fBit + 1;
		long lRetValue;
		
		lReferenceValue = ~ ( lReferenceValue <<  iLength);
		lValue = lValue >> 63 - lBit;
		lRetValue = lReferenceValue & lValue;
		//System.out.println ( Long.toBinaryString ( lReferenceValue));
		//System.out.println ( Long.toBinaryString ( lValue));
		//System.out.println ( Long.toBinaryString ( lRetValue));
		
		return ( lRetValue);
	}
	
	public static byte getByte ( long lValue) {
		byte bAux = (byte) getBitRange  ( lValue,  56, 63);
		//System.out.println ( "getByte: "+ toHexString( bAux, BYTE));
		return ( bAux);
	}
	
	public static short getHalfWord ( long lValue) {
		short sAux = ( short) getBitRange ( lValue,  48, 63);
		//System.out.println ( "getHalfWord: "+ toHexString( sAux, HALFWORD));		
		return ( sAux); 		
	}
	
	public static int getWord( long lValue) {
		int iAux = ( int) getBitRange ( lValue,  32, 63);
		//System.out.println ( "getWord: "+ toHexString( iAux, WORD));		
		return ( iAux); 
	}
	
	public static int [] splitDouble ( long lValue) {
		int [] iValues = new int [2];
		long lAux;
		
		//System.out.println ( Long.toBinaryString ( lValue));
		lAux = lValue & 0x00000000FFFFFFFF;
		iValues [ 0] = new Long ( lAux).intValue();
		lAux = lValue >> 32;
		iValues [ 1] = new Long ( lAux).intValue();		
		//System.out.println ( Integer.toBinaryString ( iValues [ 0]));
		//System.out.println ( Integer.toBinaryString ( iValues [ 1]));
		
		return ( iValues);
	}
	
	public static byte [] splitDoubleInto4Bytes ( long lValue) {
		byte [] bValues = new byte [4];
		long lAux;
		int i;
		
		lAux = lValue & 0x00000000FFFFFFFF;
		for ( i = 0; i < 4; i ++) {
			bValues [ i] = new Long ( lAux).byteValue();
			lAux = lAux >> 8;
		}
		//System.out.println ( );
		//for ( i = 3; i >= 0; i --) System.out.print ( toHexString( bValues[i], BYTE)+" , ");
		//System.out.println ( );

		return ( bValues);
	}
	
	public static byte [] splitDoubleInto8Bytes ( long lValue) {
		byte [] bValues = new byte [8];
		long lAux, lByte;
		int i;

//System.out.println ( "lValue = "+lValue);

		lAux = lValue & (long) -1;
		
//System.out.println ( "lAux = "+lAux);
		for ( i = 0; i < 8; i ++) {
			bValues [ i] = new Long ( lAux).byteValue();
			//lByte = lAux & (long) 255;
//System.out.println ( "byte em long: "+lByte);
			//bValues [ i] = (byte) lByte;
//System.out.println ( "byte: "+i+" "+bValues [i]);
			lAux = lAux >> 8;
		}

//System.out.println ( );
//for ( i = 7; i >= 0; i --) System.out.print ( toHexString( bValues[i], BYTE)+" , ");
//System.out.println ( );
		
		return ( bValues);
	}

	public static long joinDouble ( int i1, int i2) {
		long lValue, lAux;

		//System.out.println ( Integer.toBinaryString ( i1));
		//System.out.println ( Integer.toBinaryString ( i2));
		lAux = new Integer ( i1).longValue();
		lAux = lAux << 32;
		lValue = new Integer ( i2).longValue();
		lValue = lValue | lAux;		
		//System.out.println ( Long.toBinaryString ( lValue));
		
		return ( lValue);
	}
	
	public static long join4BytesIntoDouble ( byte [] bArray) {
		long lAux = 0;
		int i;
		
		lAux = lAux | (0x00000000000000FF & new Byte ( bArray [ 3]).longValue ( ));
		//System.out.println ( "byte:"+toHexString( bArray[3], BYTE));
		//System.out.println ( "long:"+Long.toHexString ( lAux));
		for ( i = 2; i >= 0; i --) {
			lAux = lAux << 8;
			lAux = lAux | (0x00000000000000FF & new Byte ( bArray [ i]).longValue ( ));	
			//System.out.println ( "byte:"+toHexString( bArray[i], BYTE));
			//System.out.println ( "long:"+Long.toHexString ( lAux));
		}
		
		//System.out.println ( Long.toHexString ( lAux));
		
		return ( lAux);
	}
	
	public static long join8BytesInDouble ( byte [] bArray) {
		long lAux = 0;
		int i;
		
		lAux = lAux | (0x00000000000000FF & new Byte ( bArray [ 7]).longValue ( ));
		//System.out.println ( "byte:"+toHexString( bArray[7], BYTE));
		//System.out.println ( "long:"+Long.toHexString ( lAux));
		for ( i = 6; i >= 0; i --) {
			lAux = lAux << 8;
			lAux = lAux | (0x00000000000000FF & new Byte ( bArray [ i]).longValue ( ));	
			//System.out.println ( "byte:"+toHexString( bArray[i], BYTE));
			//System.out.println ( "long:"+Long.toHexString ( lAux));
		}
		
		//System.out.println ( Long.toHexString ( lAux));
		
		return ( lAux);
	}
	
	private static final int MAXTAMHEX = 16;
	private static final int MAXTAMBIN = 64;
	private static int DefaultNumberFormat = DECIMAL;
	private static int DefaultSizeFormat = WORD;
}