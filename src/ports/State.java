package ports;

import primitive.Primitive;
import util.Define;
import util.SistNum;

/*
 *	Implementa a classe State para manter atributos de objetos das classes:
 *		processor, pipeline e instruction.
 */
public abstract class State extends Primitive implements Define {

/*
 *	Verifica a existencia do atributo PARNAME do tipo PARTYPE (STATUS ou FIELD).
 */
	public boolean exist ( String parName, int parType)
	{
		SetPort spAux = null;
		Port pAux;
		int iValue = - 1;
		
		if ( parType == STATUSorCONF) spAux = spStt;
		else if ( parType == FIELD) spAux = spFld;
		else if ( parType == STRING) spAux = spFldStr;
		
		if ( spAux != null) {
			pAux = ( Port) spAux.searchPrimitive ( parName);
			if ( pAux != null) return ( true);
		}
			
		return ( false);	
	}

/*
 *	Seta o atributo PARNAME do tipo PARTYPE (STATUS ou FIELD)
 *		com o valor PARVALUE.
 */
	public void set ( String parName, int parType, int parValue)
	{
		SetPort spAux = null;
		Property pAux;
		
		if ( parType == STATUSorCONF) spAux = spStt;
		else if ( parType == FIELD) spAux = spFld;
		
		if ( spAux != null) {
			pAux = ( Property) spAux.searchPrimitive ( parName);
			if ( pAux != null) pAux.set ( parValue);
		}
	}

/*
 *	Seta o atributo PARNAME do tipo PARTYPE (STATUS ou FIELD)
 *		com o valor PARVALUE (long).
 */
	public void set ( String parName, int parType, long parValue)
	{
		SetPort spAux = null;
		Property pAux;
		
		if ( parType == STATUSorCONF) spAux = spStt;
		else if ( parType == FIELD) spAux = spFld;
		
		if ( spAux != null) {
			pAux = ( Property) spAux.searchPrimitive ( parName);
			if ( pAux != null) pAux.set ( parValue);
		}
	}
		
/*
 *	Retorna o valor no atributo PARNAME do tipo PARTYPE (STATUS ou FIELD).
 */
	public long get ( String parName, int parType)
	{
		SetPort spAux = null;
		Property pAux;
		long lValue = - 1;
		
		if ( parType == STATUSorCONF) spAux = spStt;
		else if ( parType == FIELD) spAux = spFld;
		
		if ( spAux != null) {
			pAux = ( Property) spAux.searchPrimitive ( parName);
			if ( pAux != null) lValue = pAux.getDoubleWord ( );
		}
			
		return ( lValue);	
	}

/*
 *	Retorna o valor (long) no atributo PARNAME do tipo PARTYPE (STATUS ou FIELD).
 */
	public long getDoubleWord ( String parName, int parType)
	{
		SetPort spAux = null;
		Property pAux;
		long lValue = - 1;
			
		if ( parType == STATUSorCONF) spAux = spStt;
		else if ( parType == FIELD) spAux = spFld;
			
		if ( spAux != null) {
			pAux = ( Property) spAux.searchPrimitive ( parName);
			if ( pAux != null) lValue = pAux.getDoubleWord();
		}
			
		return ( lValue);	
	}

/*
 *	Seta o atributo PARNAME do tipo PARTYPE (FIELDSTRING)
 *		com o valor PARVALUE.
 */
	public void set ( String parName, int parType, String parValue)
	{
		SetPort spAux = null;
		Property pAux;
		
		if ( parType == STRING) spAux = spFldStr;
		
		if ( spAux != null) {
			pAux = ( Property) spAux.searchPrimitive ( parName);
			if ( pAux != null) pAux.set ( parValue);
		}
	}

/*
 *	Retorna o valor no atributo PARNAME do tipo PARTYPE (FIELDSTRING).
 */
	public String getString ( String parName, int parType)
	{
		SetPort spAux = null;
		Property pAux;
		String sValue = null;
				
		if ( parType == STRING) spAux = spFldStr;
		
		if ( spAux != null) {
			pAux = ( Property) spAux.searchPrimitive ( parName);
			if ( pAux != null) sValue = pAux.getString ( );
		}
			
		return ( sValue);	
	}

	public int getNelemsAll ( ) {
		return ( spStt.getNelems()+spFld.getNelems ( )+spFldStr.getNelems ( ));		
	}
	
	public Object [] [] getState ( )
	{
		int iNelems = spStt.getNelems()+spFld.getNelems ( )+spFldStr.getNelems ( );
		int i, cont = 0;
		long lValue;
		String sValue;
		Port prAux;
		Object [] [] instrFields = new Object [ iNelems] [ 2];
		
		for ( i = 0; i < spStt.getNelems(); i ++) {
			prAux = ( Port) spStt.traverse ( i);
			instrFields [ cont] [ 0] = prAux.getName ( );
			lValue = prAux.getDoubleWord ( );
			//instrFields [ cont++] [ 1] = new Long ( lValue);
			String tmpx = SistNum.printInformation ( lValue, INTEGER);
			instrFields [ cont++] [ 1] = tmpx;	
		}

		for ( i = 0; i < spFld.getNelems ( ); i ++) {
			prAux = ( Port) spFld.traverse ( i);
			instrFields [ cont] [ 0] = prAux.getName ( );
			lValue = prAux.getDoubleWord ( );
			//instrFields [ cont++] [ 1] = new Long ( lValue);
			String tmpx = SistNum.printInformation ( lValue, INTEGER);
			instrFields [ cont++] [ 1] = tmpx;	
		}
		
		for ( i = 0; i < spFldStr.getNelems ( ); i ++) {
			Property pAux = ( Property) spFldStr.traverse ( i);
			instrFields [ cont] [ 0] = pAux.getName ( );
			instrFields [ cont++] [ 1] = pAux.getString ( );	
		}
/*
		for ( i = 0; i < iNelems; i ++) {
			System.out.println ( instrFields [i] [0]);			
			System.out.println ( instrFields [i] [1]);
		}
*/
		return instrFields;
	}

	public SetPort getStatus ( ) {
		return ( spStt);
	}

	public SetPort getFields ( ) {
		return ( spFld);
	}
	
	public SetPort getFieldStrings ( ) {
		return ( spFldStr);
	}
	
/*
 *	Depurador do objeto estado.
 */
	public void debug ( ) {
		if ( spStt != null) spStt.debug ( );
		if ( spFld != null) spFld.debug ( );
		if ( spFldStr != null) spFldStr.debug ( );
	}

	protected SetPort spStt, spFld, spFldStr;
}
