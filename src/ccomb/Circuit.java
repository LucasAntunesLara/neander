package ccomb;

import ports.Port;
import ports.SetPort;
import primitive.Primitive;
import util.Define;
import util.SistNum;
import bus.Bus;
import bus.SetBus;

public abstract class Circuit extends Primitive implements Define {

	public void set ( String parName, int parType, long parValue)
	{
		SetPort spAux = null;
		Port pAux;
		
		if ( parType == IN) spAux = spIn;
		else if ( parType == CONTROL) spAux = spCtrl;
		else if ( parType == STATUSorCONF) spAux = spStt;
		
		if ( spAux != null) {
			pAux = ( Port) spAux.searchPrimitive ( parName);
			if ( pAux != null) pAux.set ( parValue);
		}
	}

	public long get ( String parName, int parType)
	{
		SetPort spAux = null;
		Port pAux;
		long lValue = - 1L;
		
		if ( parType == IN) spAux = spIn;
		else if ( parType == OUT) spAux = spOut;
		else if ( parType == CONTROL) spAux = spCtrl;
		else if ( parType == STATUSorCONF) spAux = spStt;
		
		if ( spAux != null) {
			pAux = ( Port) spAux.searchPrimitive ( parName);
			if ( pAux != null) lValue = pAux.getDoubleWord ( );
		}
			
		return ( lValue);	
	}

	public Port getPort ( String parName, int parType)
	{
		SetPort spAux = null;
		Port pAux = null;
		
		if ( parType == IN) spAux = spIn;
		else if ( parType == OUT) spAux = spOut;
		else if ( parType == CONTROL) spAux = spCtrl;
		else if ( parType == STATUSorCONF) spAux = spStt;
		
		if ( spAux != null) {
			pAux = ( Port) spAux.searchPrimitive ( parName);
		}
			
		return ( pAux);	
	}

	public SetPort getSetPort ( int parType)
	{
		SetPort spAux = null;
		
		if ( parType == IN) spAux = spIn;
		else if ( parType == OUT) spAux = spOut;
		else if ( parType == CONTROL) spAux = spCtrl;
		else if ( parType == STATUSorCONF) spAux = spStt;
		
		return ( spAux);	
	}

	public Bus getBus ( String parName)
	{
		SetBus sbAux = sbBus;
		Bus bAux = null;
		
		bAux = ( Bus) sbAux.searchPrimitive ( parName);
			
		return ( bAux);	
	}

	public SetBus getSetBus ( )
	{
		return ( this.sbBus);
	}
	
/*
	public Port getPort ( String parName, int parType)
	{
		return ( parName, parType));
	}

	public Bus getBus ( String parName)
	{
		return ( this.getBus ( parName));
	}

	public void set ( String parName, int parType, long parValue)
	{
		this.set ( parName, parType, parValue);
	}

	public long get ( String parName, int parType)
	{
		return ( this.get ( Util.sb ( parName), parType));
	}
*/
	public void setUsed ( ) {
		bUsed = true;
	}

	public void resetUsed ( ) {
		bUsed = false;
	}

	public boolean isUsed ( ) {
		return ( bUsed);
	}

	public void setMethod ( int parM) {
		if ( parM == READ || parM == WRITE || parM == BEHAVIOR) iMethod = parM;
	}

	public int getMethod ( ) {
		return ( iMethod);
	}

	public String getMethodDescription ( ) {
		String sDesc = null;

		switch ( iMethod) {
			case BEHAVIOR:
					sDesc = "BEHAVIOR";
					break;
			case READ:
					sDesc = "READ";
					break;
			case WRITE:
					sDesc = "WRITE";
					break;
			case PROPAGATE:
					sDesc = "PROPAGATE";
					break;
			case SET:
					sDesc = "SET";
					break;
		}
		
		return ( sDesc);
	}

	public int getNelemsAll ( ) {
		int soma = 0;
		
		if ( spIn != null) soma += spIn.getNelems ( );
		if ( spOut != null) soma += spOut.getNelems ( );
		if ( spCtrl != null) soma += spCtrl.getNelems ( );
		if ( spStt != null) soma += spStt.getNelems ( );

		return ( soma);		
	}
	
	public Object [] [] getState ( )
	{
		int iNelems = getNelemsAll ( );
		Port pAux;
		int i, cont = 0;
		long lValue;
		String sValue;
		Object [] [] componentPorts = new Object [ iNelems] [ 2];

		if ( spIn != null)
			for ( i = 0; i < spIn.getNelems ( ); i ++) {
				pAux = ( Port) spIn.traverse ( i);
				componentPorts [ cont] [ 0] = pAux.getName ( );
				lValue = pAux.getDoubleWord ( );
				//componentPorts [ cont++] [ 1] = new Long ( lValue);	
				String tmpx = SistNum.printInformation ( lValue, pAux.getTypeValue());
				componentPorts [ cont++] [ 1] = tmpx;	
			}

		if ( spOut != null)
			for ( i = 0; i < spOut.getNelems ( ); i ++) {
				pAux = ( Port) spOut.traverse ( i);
				componentPorts [ cont] [ 0] = pAux.getName ( );
				lValue = pAux.getDoubleWord ( );
				//componentPorts [ cont++] [ 1] = new Long ( lValue);	
				String tmpx = SistNum.printInformation ( lValue, pAux.getTypeValue());
				componentPorts [ cont++] [ 1] = tmpx;	
			}

		if ( spCtrl != null)
			for ( i = 0; i < spCtrl.getNelems ( ); i ++) {
				pAux = ( Port) spCtrl.traverse ( i);
				componentPorts [ cont] [ 0] = pAux.getName ( );
				lValue = pAux.getDoubleWord ( );
				//componentPorts [ cont++] [ 1] = new Long ( lValue);
				String tmpx = SistNum.printInformation ( lValue, INTEGER);
				componentPorts [ cont++] [ 1] = tmpx;	
			}

		if ( spStt != null)
			for ( i = 0; i < spStt.getNelems(); i ++) {
				pAux = ( Port) spStt.traverse ( i);
				componentPorts [ cont] [ 0] = pAux.getName ( );
				lValue = pAux.getDoubleWord ( );
				//componentPorts [ cont++] [ 1] = new Long ( lValue);
				String tmpx = SistNum.printInformation ( lValue, INTEGER);
				componentPorts [ cont++] [ 1] = tmpx;	
			}
/*
		for ( i = 0; i < iNelems; i ++) {
			System.out.println ( componentPorts [i] [0]);			
			System.out.println ( componentPorts [i] [1]);
		}
*/
		return componentPorts;
	}

//	public abstract void behavior ( );

	public static void help ( ) {
		System.out.println ( "Sem Help!!!");
	}

	protected SetPort spIn, spOut, spCtrl, spStt;
	protected SetBus sbBus;
	private boolean bUsed = false;
	private int iMethod = - 1;
}
