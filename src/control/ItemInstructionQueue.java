package control;

import java.lang.reflect.Array;

import ports.Field;
import ports.FieldString;
import ports.Property;
import ports.SetPort;
import ports.State;
import ports.Status;
import util.Define;

/*
 *	Implementa a classe item de fila de instrucao.
 *	Objetos deste tipo representam as instrucoes em filas de instrucoes.
 */
public class ItemInstructionQueue extends State implements Define {

	public ItemInstructionQueue ( Instruction it) {
		String [ ] asStatus = { };
//		String [ ] asFields = { };
//		String [ ] asStrings = { "DESCRIPTION"};
		SetPort spAux;
		
		iSource = it;
		
		spStt = new SetPort ( );
		spFld = new SetPort ( );
		spFldStr = new SetPort ( );
		
		for ( int i = 0; i < Array.getLength ( asStatus); i ++) {
			spAux = it.getStatus ( );
			int iIndice = spAux.searchPosition( asStatus [ i]);
			Status sttAux;
			if ( iIndice != -1) sttAux = ( Status) spAux.traverse( iIndice);
			else sttAux = new Status ( asStatus [ i]);
			spStt.add ( sttAux);
		}
		
		for ( int i = 0; i < Array.getLength ( asFields); i ++) {
			spAux = it.getFields ( );
			int iIndice = spAux.searchPosition( asFields [ i]);
			Field fldAux;
			if ( iIndice != -1) fldAux = ( Field) spAux.traverse( iIndice);
			else fldAux = new Field ( asFields [ i]);
			spFld.add ( fldAux);
		}

		for ( int i = 0; i < Array.getLength ( asStrings); i ++) {
			spAux = it.getFieldStrings ( );
			int iIndice = spAux.searchPosition( asStrings [ i]);
			FieldString fldSAux;
			if ( iIndice != -1) fldSAux = ( FieldString) spAux.traverse( iIndice);
			else fldSAux = new FieldString ( asStrings [ i]);
			spFldStr.add ( fldSAux);
		}

	}

	public String getDescription ( ) {
		String sDesc;
		int iLi = 0;
		Property pAux = ( Property) spFldStr.searchPrimitive ( "DESCRIPTION");
		if ( pAux != null) {
			sDesc = pAux.getString ( );
			iLi = sDesc.lastIndexOf("\\");
			return ( sDesc.substring( iLi+1));
		} 
		else return null;	
	}
	
/*
 *	Eh chamada na criacao da fila de instrucoes.
 *	Guarda no array de strings ASFIELDS os campos nos itens de filas de instrucoes.
 */
 	public static void defineFieldsOfInstructions ( String [ ] asFld)
 	{
 		int nElems = Array.getLength ( asFld);
 		
 		asFields = new String [ nElems];
 	
 		for ( int i = 0; i < nElems; i ++) {	
 			asFields [ i] = asFld [ i];
 		}
 	}

 /*
  *	Eh chamada na criacao da fila de instrucoes.
  *	Guarda no array de strings ASFIELDS os campos nos itens de filas de instrucoes.
  *	Guarda no array de strings ASSTRINGS os campos com valores textuais nos itens de filas de instrucoes.
  */
  	public static void defineFieldsOfInstructions ( String [ ] asFld, String [ ] asStr)
  	{
  		int nElems = Array.getLength ( asFld);
  		
  		asFields = new String [ nElems];
  	
  		for ( int i = 0; i < nElems; i ++) {	
  			asFields [ i] = asFld [ i];
 	 		//System.out.println ( "asFields "+i+"->"+asFields[i]);
  		}
  		
  		if ( asStr != null) {
  		
  			nElems = Array.getLength ( asStr);
  		
  			asStrings = new String [ nElems];
 	 	
  			for ( int i = 0; i < nElems; i ++) {	
  				asStrings [ i] = asStr [ i];
  	 	 		//System.out.println ( "asStrings "+i+"->"+asStrings[i]);
  			}
  		
  		}
  	}
 
 /*
  *	Testa uma propriedade STRING de um item de instrucao, retornando true se o valor
  *		corresponder ao argumento passado.
  */
 	public boolean testItem ( String parProp, String parValue) {
		if ( this.exist ( parProp, STRING) == false) {
			return ( false);
		} else if ( this.getString ( parProp, STRING).equals (parValue)) {
			return ( true);
		} else return ( false);
 	}

 /*
  *	Testa uma propriedade STATUS ou FIELD de um item de instrucao, retornando true 
  *		se o valor corresponder ao argumento passado.
  */
 	public boolean testItem ( String parProp, int parValue) {
 		int iType;
 		
 		if ( this.exist ( parProp, STATUSorCONF) == true) iType = STATUSorCONF;
 		else if ( this.exist ( parProp, FIELD) == true) iType = FIELD;
 		else return ( false);
 		
 		if ( this.get ( parProp, iType) == parValue) return ( true);
 		else return ( false);
 	}

 /*
 *	Depurador do objeto item de instrucao: apresenta os valores nas variaveis do objeto.
 */
	public void debug ( ) {
//		System.out.println ( );
		System.out.println ( "*** Item Instruction.debug:...BEGIN, Cond.:");
		if ( spFldStr != null) spFldStr.debug ( );
		if ( spStt != null) spStt.debug ( );
		if ( spFld != null) spFld.debug ( );
//		smMops.debug ( );
//		System.out.println ( "*** Instruction.debug:...END");
//		System.out.println ( );		
	}

	public void list ( ) {
//		System.out.println ( );
		if ( spFldStr != null) spFldStr.list ( );
		if ( spStt != null) spStt.list ( );
		if ( spFld != null) spFld.list ( );
//		smMops.debug ( );
//		System.out.println ( "*** Instruction.debug:...END");
//		System.out.println ( );		
	}

	static String [ ] asFields;
	static String [ ] asStrings;
	Instruction iSource;
}
