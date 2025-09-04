package util;

public class Util {

	public static StringBuffer sb ( String parName) {
		StringBuffer sbTmp = new StringBuffer ( ).append ( parName);
		
		return ( sbTmp);	
	}
	
	public static int potencia ( int parNumber, int parExp) {
		int iRes, i;
		
		iRes = 1;
		for ( i = 0; i < parExp; i ++) iRes = iRes * parNumber;
		
		return ( iRes);	
	}

}
