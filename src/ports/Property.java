package ports;

import util.SistNum;

public class Property extends Port {

	public Property ( String parName, int parType) {
		super ( parName, parType);
	}

	public int getType ( ) {
		return ( iTypePort);
	}
	
	public void set ( String parValue) {
		sValue = parValue;
	}

	public String getString ( ) {
		return ( sValue);
	}

	public void debug ( ) {
//		System.out.println ( "** porta.debug: ...BEGIN");
		if ( iTypePort == STRING)
			System.out.println("* porta.debug: name: "+sbName+" type: "+iTypePort+" and value: "+sValue);
		else
			System.out.println ( 	"* porta.debug: name: "+sbName+" type: "+iTypePort+" and value: "+
								SistNum.toHexString ( lValue, WORD));

//		System.out.println ( "** porta.debug: ...END");	
//		System.out.println ( );		
	}

	public void list ( ) {
		String sTmp;
		int iTmp;

		if ( iTypePort == STRING)
			sTmp = sbName + " = " + sValue;
		else
			sTmp = sbName + " = " + SistNum.toHexString ( lValue, iSize);

		
		iTmp = sTmp.length ( );
		
		System.out.print ( sTmp);

		switch ( iTypePort) {
			case FIELD:
				sTmp = "Field";
				break;
								
			case STRING:
				sTmp = "Field String";
				break;
								
			case STATUSorCONF:
				sTmp = "Status";
				break;

			default:
				sTmp = "Undefined";
				break;
		}
		
		for ( int i = 0; i < COLUNAS - iTmp; i ++) System.out.print ( " ");
		System.out.println ( sTmp + " de " + iSize + " bits.");
	}

	private String sValue;
}
