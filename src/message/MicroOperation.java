package message;


public class MicroOperation extends Msg {

	public MicroOperation ( 	String parComponente, int parMethod, 
							String parName, long parV1, long parV2, 
							long parV3, float parTime) {
		super				( MSGDATAPATH);
		sComponente 		= new String ( parComponente);
		iMethod 			= parMethod;
		
		if ( parName != null) sName = new String ( parName);
		else sName = null;

		// TYPE de porta ou COORDENADA X de conteudo
		lV1 			= parV1;
		// VALOR de porta ou COORDENADA Y de conteudo
		lV2 			= parV2;
		// VALOR de conteudo
		lV3 			= parV3;
		fTime			= parTime;
		bActive 		= true;
	}

	public MicroOperation ( ) {
		super				( MSGDATAPATH);
	}
	
	public void setComponentName ( String parCompName) {
		sComponente = new String ( parCompName);
	}

	public String getComponentName ( ) {
		return ( sComponente);
	}

	public void setMethodId ( int parMethod) {
		iMethod = parMethod;
	}

	public int getMethodId ( ) {
		return iMethod;
	}

	public void setPortName ( String parPortName) {
		sName = new String ( parPortName);
	}

	public String getPortName ( ) {
		return ( sName);
	}

	public void setPortType ( long parType) {
		lV1 = parType;
	}

	public long getPortType ( ) {
		return lV1;
	}

	public void setPortValue ( long parValue) {
		lV2 = parValue;
	}

	public long getPortValue ( ) {
		return lV2;
	}

	public void setContents ( long parValue) {
		lV3 = parValue;
	}

	public long getContents ( ) {
		return lV3;
	}

	public void debug ( ) {
//		System.out.println ( "*** msgdatapath.debug:...BEGIN");
		String sTmp;
		
		if ( bActive == true) sTmp = "ACTIVE";
		else sTmp = "INACTIVE";

		if ( sName != null) 
			System.out.println ( sComponente+" , "+iMethod+" , "+sName+" , "+lV1+" , "+lV2+" , "+lV3+" , "+fTime+" , "+sTmp);
		else
			System.out.println ( sComponente+" , "+iMethod+" ,null, "+lV1+" , "+lV2+" , "+lV3+" , "+fTime+" , "+sTmp);
//		System.out.println ( "*** msgdatapath.debug:...END");
	}

	public String sComponente;
	public int iMethod;
	public String sName;
	public long lV1, lV2, lV3;
	public float fTime;
	public boolean bActive;
	
	/* (non-Javadoc)
	 * @see primitive.Primitive#list()
	 */
	public void list() {
//		System.out.println ( "*** msgdatapath.debug:...BEGIN");
		String sTmp;
		
		if ( bActive == true) sTmp = "ACTIVE";
		else sTmp = "INACTIVE";

		if ( sName != null) 
			System.out.println ( sComponente+" , "+iMethod+" , "+sName+" , "+lV1+" , "+lV2+" , "+lV3+" , "+fTime+" , "+sTmp);
		else
			System.out.println ( sComponente+" , "+iMethod+" ,null, "+lV1+" , "+lV2+" , "+lV3+" , "+fTime+" , "+sTmp);
//		System.out.println ( "*** msgdatapath.debug:...END");
	}

}
