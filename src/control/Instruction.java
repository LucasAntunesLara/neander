package control;

import java.lang.reflect.Array;

import message.MicroOperation;
import message.SetMsg;
import ports.Field;
import ports.FieldString;
import ports.Property;
import ports.SetPort;
import ports.State;
import ports.Status;
import util.Define;

/*
 *	Implementa a classe instrucao.
 *	Objetos deste tipo representam as instrucoes em execucao no processador.
 */
public class Instruction extends State implements Define {

/*
 *	A SER CUSTOMIZADA PELO PROJETISTA.
 *	Cria um objeto instrucao.
 *	Insere atributos de STATUS (estado), FIELDS (campos da instrucao) e
 *		STRINGS ( descricoes textuais).
 *	Cria a fila de mensagens que vai receber o roteiro de execucao
 *		para o tipo de instrucao que serah criada.
 *
 *  OBSERVACAO:
 *	As mensagens que constam na instrucao sao provenientes de um objeto
 *		INSTRUCTIONTYPE que prove um roteiro de execucao para um tipo de
 *		instrucao (aritmeticas, por exemplo). Porem, instrucoes que executam
 *		funcoes diferentes podem pertencer a um mesmo tipo (add e sub, por 
 *		exemplo). Mais adiante, esta diferenciacao deve ser estabelecida 
 *		(no metodo decodeAfter de PROCESSOR).
 */
	public Instruction ( ) {
		String [ ] asStatus = { };
//		String [ ] asFields = { };
//		String [ ] asStrings = { "DESCRIPTION", "mnem"};
		
		spStt = new SetPort ( );
		spFld = new SetPort ( );
		spFldStr = new SetPort ( );
		
		for ( int i = 0; i < Array.getLength ( asStatus); i ++) {
			Status sttAux = new Status ( asStatus [ i]);
			spStt.add ( sttAux);
		}
		
		for ( int i = 0; i < Array.getLength ( asFields); i ++) {
			Field fldAux = new Field ( asFields [ i]);
			spFld.add ( fldAux);
		}

		for ( int i = 0; i < Array.getLength ( asStrings); i ++) {
			FieldString fldSAux = new FieldString ( asStrings [ i]);
			spFldStr.add ( fldSAux);
		}

		// mnem foi inserido para o processador acesMIPS
		// Portanto, para nao dar erro na simulacao dos outros processadores, 
		// ele e' inicializado com vazio
		set ( "mnem", STRING, "");

		smMops = new SetMsg ( );
		bActive = true;
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
 *	Ativa (true) ou desativa (false) a instrucao.
 *	Uma instrucao inativa nao envia mensagens ao objeto datapath. Ou seja,
 *		ela nao executa.
 */
	public void setActiveCondition ( boolean parActive) {
		bActive = parActive;
	}
	
/*
 *	Verifica se uma instrucao estah ativa ou nao
 */
	public boolean isActive ( ) {
		return bActive;
	}

/*
 *	Eh chamada na inicializacao de PROCESSOR.
 *	Guarda no array de strings ASFIELDS os campos das instrucoes.
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
 	 *	Eh chamada na inicializacao de PROCESSOR.
 	 *	Guarda no array de strings ASFIELDS os campos das instrucoes.
 	 *	Guarda no array de strings ASSTRINGS os campos com valores textuais das instrucoes.
 	 */
 	 public static void defineFieldsOfInstructions ( String [ ] asFld, String [ ] asStr)
 	 {
 	 	int nElems = Array.getLength ( asFld);
 	 		
 	 	asFields = new String [ nElems];
 	 	
 	 	for ( int i = 0; i < nElems; i ++) {	
 	 		asFields [ i] = asFld [ i];
 	 	}
 	 	
 	 	nElems = Array.getLength ( asStr);
	 		
	 	asStrings = new String [ nElems];
	 	
	 	for ( int i = 0; i < nElems; i ++) {	
	 		asStrings [ i] = asStr [ i];
	 	}
 	 }
 	 	
 /*
  *	Testa uma propriedade STRING de uma instrucao, retornando true se o valor
  *		corresponder ao argumento passado.
  */
 	public boolean testInstruction ( String parProp, String parValue) {
		if ( this.exist ( parProp, STRING) == false) {
			return ( false);
		} else if ( this.getString ( parProp, STRING).equals (parValue)) {
			return ( true);
		} else return ( false);
 	}

 /*
  *	Testa uma propriedade STATUS ou FIELD de uma instrucao, retornando true 
  *		se o valor corresponder ao argumento passado.
  */
 	public boolean testInstruction ( String parProp, int parValue) {
 		int iType;
 		
 		if ( this.exist ( parProp, STATUSorCONF) == true) iType = STATUSorCONF;
 		else if ( this.exist ( parProp, FIELD) == true) iType = FIELD;
 		else return ( false);
 		
 		if ( this.get ( parProp, iType) == parValue) return ( true);
 		else return ( false);
 	}

 /* PARA VISUALIZACAO DE FILAS DE INSTRUCOES - incompleto
 	public void filterFieldsOfInstructions ( String [ ] asStr)
 	{
 		int nElems = Array.getLength ( asStr);
 		int i = 0;
 		boolean isIn = false;

 		while ( i < spFld.getNelems ( )) {
 			String sAux = ( String) spFld.traverse ( i);
 			isIn = false;
 			for ( int j = 0; j < nElems; j ++) {
 				if ( sAux.compareToIgnoreCase ( asStr [ j]) == 0) {
 					isIn = true;
 					break;
 				}
 			}
 			if ( isIn == false) spFld.remove( sAux);
 			i ++;
 		}
 	}
*/
 	
/*******************************************************************************
 *	Desativa a execucao de um componente da lista de funcionalidade da instrucao
 *******************************************************************************/
	public void deactivateComponent ( String sName) {
		for ( int i = 0; i < smMops.getNelems ( ); i ++) {
			MicroOperation mdAux = ( MicroOperation) smMops.traverse ( i);
			if ( mdAux.sComponente.compareTo ( sName) == 0) {
System.out.println ( "Desativando o componente: "+mdAux.sComponente);
				mdAux.bActive = false;
			}
		}
	}

/*******************************************************************************
 *	Substitui a execucao de um componente da lista de funcionalidade da instrucao
 *******************************************************************************/
	public void substituteComponent ( String sName, String sToName) {
		for ( int i = 0; i < smMops.getNelems ( ); i ++) {
			MicroOperation mdAux = ( MicroOperation) smMops.traverse ( i);
			if ( mdAux.sComponente.compareTo ( sName) == 0) {
System.out.println ( "Substituindo o componente: "+mdAux.sComponente+" por "+sToName);
				mdAux.sComponente = new String ( sToName);
			}
		}
	}

/*******************************************************************************
 *	Substitui o valor a ser fornecido para uma porta
 *******************************************************************************/
	public void substitutePortValue ( String cName, String pName, long parValue) {
		for ( int i = 0; i < smMops.getNelems ( ); i ++) {
			MicroOperation mdAux = ( MicroOperation) smMops.traverse ( i);
			if ( mdAux.sComponente.compareTo ( cName) == 0) {
				if ( mdAux.sName != null && mdAux.sName.compareTo ( pName) == 0) {
					mdAux.lV2 = parValue;
System.out.println ( "Substituindo o valor da porta: "+pName+","+cName+" por "+parValue);
				}
			}
		}
	}

/*******************************************************************************
 *	Remove parte da funcionalidade da instruçao relacionada ao componente parName
 *******************************************************************************/
 	public void removeComponent ( String sName) {
 		int i = 0;
 		
		while ( i < smMops.getNelems ( )) {
			MicroOperation mdAux = ( MicroOperation) smMops.traverse ( i);
			if ( mdAux.sComponente.compareTo ( sName) == 0) {
System.out.println ( "Removendo o componente: "+mdAux.sComponente);
				smMops.remove ( i);
			} else i ++;
		}
	}
  
 /*******************************************************************************
 *	Insere um trecho de funcionalidade a partir do componente parName
 *  O estagio de pipeline pode ser o mesmo deste componente ou o proximo (iTime)
 *  Valido para metodos behavior, read, write
 *******************************************************************************
 	public void addFunctionalityAtFirst(String parName,String parNameComp,
 										int iMethod,int iTime) 
 	{
 		MsgDatapath mdIns;
 		float fTime;
 		int i = 0;
 
		while ( i ++ < smMops.getNelems ( )) {
			MsgDatapath mdAux = ( MsgDatapath) smMops.traverse ( i);
			if ( mdAux.sComponente.compareTo ( parName) == 0) {
				fTime = mdAux.fTime + iTime;
				mdIns = new MsgDatapath ( parNameComp,iMethod,null,0L,0L,0L,fTime);
System.out.println ( "Inserindo o componente: "+parNameComp+" apos "+parName+" no tempo + "+iTime);
				smMops.add ( i+1,mdIns);
				break;
			}
		}
 	}*/
 
 /*******************************************************************************
 *	Insere um trecho de funcionalidade a partir da ultima referencia 
 *  ao componente parName
 *  O estagio de pipeline pode ser o mesmo deste componente ou o proximo (iTime)
 *  Valido para metodos behavior, read, write
 *******************************************************************************
 	public void addFunctionalityAtLast(	String parName,String parNameComp,
 										int iMethod,int iTime) 
 	{
 		MsgDatapath mdIns;
 		float fTime;
 		int i = smMops.getNelems ( );
 
		while ( i -- >= 0) {
			MsgDatapath mdAux = ( MsgDatapath) smMops.traverse ( i);
			if ( mdAux.sComponente.compareTo ( parName) == 0) {
				fTime = mdAux.fTime + iTime;
				mdIns = new MsgDatapath ( parNameComp,iMethod,null,0L,0L,0L,fTime);
System.out.println ( "Inserindo o componente: "+parNameComp+" apos "+parName+" no tempo + "+iTime);
				smMops.add ( i+1,mdIns);
				break;
			}
		}
 	}*/

 /*******************************************************************************
 *	Insere um trecho de funcionalidade a partir do componente parName
 *  O estagio de pipeline pode ser o mesmo deste componente ou o proximo (iTime)
 *  Valido para metodos setport
 *******************************************************************************
 	public void addFunctionalityAtFirst(String parName,String parNameComp,
 										String parNamePort,long lType,
 										long lValue,int iTime) 
 	{
 		MsgDatapath mdIns;
 		float fTime;
 		int i = 0;

		while ( i ++ < smMops.getNelems ( )) {
			MsgDatapath mdAux = ( MsgDatapath) smMops.traverse ( i);
			if ( mdAux.sComponente.compareTo ( parName) == 0) {
				fTime = mdAux.fTime + iTime;
				mdIns = new MsgDatapath ( parNameComp,SET,parNamePort,lType,lValue,0L,fTime);
System.out.println ( "Inserindo setport: "+parNameComp+" apos "+parName+" no tempo + "+iTime);
				smMops.add ( i+1,mdIns);
				break;
			}
		}
 	}*/

 /*******************************************************************************
 *	Insere um trecho de funcionalidade a partir da ultima referencia
 *  ao componente parName
 *  O estagio de pipeline pode ser o mesmo deste componente ou o proximo (iTime)
 *  Valido para metodos setport
 *******************************************************************************
 	public void addFunctionalityAtLast (String parName,String parNameComp,
 										String parNamePort,long lType,
 										long lValue,int iTime) 
 	{
 		MsgDatapath mdIns;
 		float fTime;
 		int i = smMops.getNelems ( );
 
		while ( i -- >= 0) {
			MsgDatapath mdAux = ( MsgDatapath) smMops.traverse ( i);
			if ( mdAux.sComponente.compareTo ( parName) == 0) {
				fTime = mdAux.fTime + iTime;
				mdIns = new MsgDatapath ( parNameComp,SET,parNamePort,lType,lValue,0L,fTime);
System.out.println ( "Inserindo setport: "+parNameComp+" apos "+parName+" no tempo + "+iTime);
				smMops.add ( i+1,mdIns);
				break;
			}
		}
 	}*/

/*******************************************************************************
 *	Insere um trecho de funcionalidade a partir de uma microoperacao definida por
 *  por: componente parName, metodo parMethod e tempo parTime
 *  Insere BEFORE ou AFTER
 *  Insere no mesmo estagio de execucao
 *******************************************************************************/
 	public void addFunctionalityAt(	String parName, int iMethod, float fTime,
 									String parNameComp, int iMethodComp, int iWhere) 
 	{
 		MicroOperation mdIns;

 		int i = 0;

 		while ( i < smMops.getNelems ( )) {
 			MicroOperation mdAux = ( MicroOperation) smMops.traverse ( i);
 //mdAux.list();
 //System.out.print ( parName);
 //System.out.print ( iMethod);
 //System.out.println ( fTime);
 			if ( 	mdAux.sComponente.compareToIgnoreCase( parName) == 0 && 
 					mdAux.getMethodId() == iMethod && mdAux.fTime == fTime) {
 				mdIns = new MicroOperation ( parNameComp,iMethodComp,null,0L,0L,0L,fTime);
 //System.out.println ( "Inserindo o componente: "+parNameComp+" apos "+parName+" no tempo + "+fTime);
 				if ( iWhere == AFTER) smMops.add ( i+1,mdIns);
 				else smMops.add ( i-1, mdIns);
 				break;
 			}
 			i ++;
 		}
 	}

 /*******************************************************************************
  *	Remove uma microoperacao definida por: componente parName, metodo parMethod 
  *	e tempo parTime
  *******************************************************************************/
 	public void deactivateFunctionalityAt(	String parName, int iMethod, float fTime) 
  	{
  		MicroOperation mdIns;

  		int i = 0;
 	 	while ( i < smMops.getNelems ( )) {
 			MicroOperation mdAux = ( MicroOperation) smMops.traverse ( i);
 			if ( 	mdAux.sComponente.compareToIgnoreCase( parName) == 0 && 
 					mdAux.getMethodId() == iMethod && mdAux.fTime == fTime) {
 	 			mdAux.bActive = false;
 				break;
 			}
 			i ++;
 		}
 	}

 /*
 *	Depurador do objeto instrucao: apresenta os valores nas variaveis do objeto.
 */
	public void debug ( ) {
//		System.out.println ( );
		System.out.println ( "*** Instruction.debug:...BEGIN, Cond.:"+bActive);
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
		smMops.debug ( );
//		System.out.println ( "*** Instruction.debug:...END");
//		System.out.println ( );		
	}

	static String [ ] asFields;
	static String [ ] asStrings = { "DESCRIPTION"};
	public SetMsg smMops;
	boolean bActive;
}
