package control;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.StringTokenizer;

import platform.Lang;
import primitive.Primitive;
import util.Define;

/*
 *	 Implementa a classe PIPELINE que vem a ser um conjunto de PIPESTAGES.
*/
public class ExecutionPath extends Primitive implements Define {

	public ExecutionPath ( String parFileName) throws Exception {
		BufferedReader d = null;
		String sInput;
		d = new BufferedReader ( new FileReader ( parFileName));
		ExecutionStage psStage;
		int oldIT = - 1;
		
		spStages = new SetPipeStages ( );
		spStagesInternal = new SetPipeStages ( );

		if ( d != null) {
			setName ( parFileName);
			while ( ( sInput = d.readLine ( )) != null) {
				StringTokenizer stLine = 	new StringTokenizer ( sInput, ",-= ", false);
				String [ ] sTokens = new String [ MAXTOKENS];
				int iNtokens = 0;

				if ( sInput.substring ( 0, 2).compareTo ( "//") == 0) continue;

		     	while (stLine.hasMoreTokens()) {
					sTokens [ iNtokens ++] = stLine.nextToken();
//System.out.println ( iNtokens-1+","+sTokens [ iNtokens-1]);
					if ( iNtokens == MAXTOKENS) break;
        		}

				if ( iNtokens == 1) {
					if ( sTokens [ 0].equals ( "MONOCYCLE")) iTimeMode = MONOCYCLE;
					else if ( sTokens [ 0].equals ( "MULTICYCLE")) iTimeMode = MULTICYCLE;
					else if ( sTokens [ 0].equals ( "PIPELINED")) iTimeMode = PIPELINED;
					else if ( sTokens [ 0].equals ( "NONPIPELINED")) iTimeMode = NONPIPELINED;
					else iTimeMode = MONOCYCLE;
				} else if ( iNtokens == 2) {
					if ( sTokens [ 0].equals ( "NAME")) {
//System.out.println ( "Name = "+sTokens [1]);
						setName ( sTokens [ 1]);
					} else {
						int iO = Integer.parseInt ( sTokens [ 1]);
						psStage = new ExecutionStage ( sTokens [ 0],iO, -1);
						spStages.add ( psStage);
						iNsteps ++;
					}
				} else if ( iNtokens == 3) {
					int iO = Integer.parseInt ( sTokens [ 1]);
					int iT = Integer.parseInt ( sTokens [ 2]);
					psStage = new ExecutionStage ( sTokens [ 0],iO, iT);
					spStagesInternal.add ( psStage);
//System.out.println ( iO + "," + iT + "," + oldIT);
					if ( iT != oldIT) {
						psStage = new ExecutionStage ( sTokens [ 0],iT, -1);
						spStages.add ( psStage);
						iNsteps ++;
					}
					oldIT = iT;
				}
			}
		}
		
//		list ( );
		
		itctBubble = null;
	}
	
/*
 *	Retorna o nro. de estagios deste pipeline.
 */
	public int getNsteps ( ) {
		return ( iNsteps);
	}

/*
 *	Retorna o tipo de temporizacao a ser usado pelo processador.
 *	Este parametro estah no arquivo descritor do Pipeline.
 */
	public int getTimeMode ( ) {
		return ( iTimeMode);
	}

	public SetPipeStages getExecutionStages ( ) {
		return spStages;
	}

	public ExecutionStage search ( String parName) {
		return ( ( ExecutionStage) spStages.searchPrimitive ( parName));
	}

	public String getNameNext ( String parName) {
		ExecutionStage esStage;
		int iBegin = 0;

		if ( parName != null) {
			iBegin = spStages.searchPosition ( parName);
			if ( iBegin == - 1) return ( null);
		}

		if ( iBegin+1 < spStages.getNelems ( )) {
			esStage = ( ExecutionStage) spStages.traverse ( iBegin+1);
			return ( esStage.getName());
		} else return ( null);
	}

	public String getNamePrev ( String parName) {
		ExecutionStage esStage;
		int iBegin = 0;

		if ( parName != null) {
			iBegin = spStages.searchPosition ( parName);
			if ( iBegin == - 1) return ( null);
		}

		if ( iBegin > 0) {
			esStage = ( ExecutionStage) spStages.traverse ( iBegin-1);
			return ( esStage.getName());
		} else return ( null);
	}

/*
 *	Retorna a instrucao corrente no estagio de pipeline PARNAME
 */
	public Instruction getCurrentInst ( String parName) {
		ExecutionStage psAux;
		int iBegin;

		if ( parName != null) {
			iBegin = spStages.searchPosition ( parName);
			if ( iBegin == - 1) return ( null);
			psAux = ( ExecutionStage) spStages.traverse ( iBegin);
			if ( psAux.iCurrent != null) return ( psAux.iCurrent);
		}

		return ( null);
	}

/*
 *	Seta a instrucao que serah uma BOLHA neste pipeline.
 */
	public void setBubble ( Instruction parBubble) {
		itctBubble = parBubble;
		itctBubble.set ( "DESCRIPTION",STRING,Lang.iLang==ENGLISH?Lang.msgsGUI[275]:Lang.msgsGUI[280]);
		itctBubble.setActiveCondition ( false);
	}

/*
 *	Retorna uma instrucao BOLHA.
 */
	public Instruction getBubble ( ) {
		return ( itctBubble);
	}

/*
 *	Seta a condicao ATIVO/INATIVO de um estagio de PIPELINE.
 */
	public void setActiveCondition ( String parName, boolean parActive) {
		ExecutionStage psAux;
		int iPoint;

		if ( parName != null) {
			iPoint = spStages.searchPosition ( parName);
			if ( iPoint == - 1) return;
			psAux = ( ExecutionStage) spStages.traverse ( iPoint);
			psAux.setActiveCondition ( parActive);
		}
	}

/*
 *	Verifica se um estagio estah ativo ou inativo.
 */
	public boolean isActive ( String parName) {
		ExecutionStage psAux;
		int iPoint;

		if ( parName != null) {
			iPoint = spStages.searchPosition ( parName);
			if ( iPoint == - 1) return ( false);
			psAux = ( ExecutionStage) spStages.traverse ( iPoint);
			return ( psAux.isActive ( ));
		}
		
		return ( false);
	}

/*
 *	Retorna true se o estagio de pipeline PARNAME estiver vazio.
 */
	public boolean isEmpty ( String parName) {
		ExecutionStage psAux;
		int iBegin = 0;

		if ( parName != null) {
			iBegin = spStages.searchPosition ( parName);
			if ( iBegin == - 1) return ( true);
		}

		psAux = ( ExecutionStage) spStages.traverse ( iBegin);

		if ( psAux.iCurrent != null) return ( false);
		else return ( true);
	}

/*
 *	Avanca as instrucoes no pipeline e insere a instrucao PARNEW se nao houver
 *  mais instruçoes nos estagios (simulando um caminho sem pipeline) 
 */
private boolean walkNonPipelined ( Instruction parNew, String parName) {
	ExecutionStage [ ] aPStage = new ExecutionStage [ spStages.getNelems ( )];
	int iBegin = 0;

	if ( parName != null) {
		iBegin = spStages.searchPosition ( parName);
		if ( iBegin == - 1) return ( false);
	}

	for ( int i = iBegin; i < spStages.getNelems ( ); i ++) {
		aPStage [ i] = ( ExecutionStage) spStages.traverse ( i);
	}
		
	for ( int i = spStages.getNelems ( ) - 1; i > iBegin; i --) {
		if ( 	aPStage [ i].isActive ( ) == true &&
				aPStage [ i - 1].isActive ( ) == true) {
					aPStage [ i].iCurrent = aPStage [ i - 1].iCurrent;
		} else if ( aPStage [ i - 1].isActive ( ) == false) {
			aPStage [ i].iCurrent = getBubble ( );
		} else if ( aPStage [ i].isActive ( ) == false) {
			continue;
		}
	}
	
	if ( parNew != null) {
		boolean haInstruction = false;
		for ( int i = iBegin+1; i < spStages.getNelems ( ); i ++) {
			if ( aPStage [ i].getCurrentInst() != null) {
				haInstruction = true;
				break;
			}
		}

		if ( ! haInstruction) {
			aPStage [ iBegin].iCurrent = parNew;
			return ( true);
		} else {
			aPStage [ iBegin].iCurrent = null;
			return ( false);
		}
	} else {
		aPStage [ iBegin].iCurrent = parNew;
		return ( true);		
	}
}

/*
 *	Avanca as instrucoes no pipeline. Insere a instrucao PARNEW no estagio
 *	de pipeline PARNAME. Avanca as instrucoes a partir do estagio PARNAME.
 *	Testa se o estagio estah ativo ou inativo antes de avancar.
 */
	public boolean walk ( Instruction parNew, String parName) {
		ExecutionStage [ ] aPStage = new ExecutionStage [ spStages.getNelems ( )];
		int iBegin = 0;

		if ( this.getTimeMode() == NONPIPELINED) {
			return ( walkNonPipelined ( parNew, parName));
		}
		
		if ( parName != null) {
			iBegin = spStages.searchPosition ( parName);
			if ( iBegin == - 1) return ( false);
		}

		for ( int i = iBegin; i < spStages.getNelems ( ); i ++) {
			aPStage [ i] = ( ExecutionStage) spStages.traverse ( i);
		}
		
		for ( int i = spStages.getNelems ( ) - 1; i > iBegin; i --) {
			if ( 	aPStage [ i].isActive ( ) == true &&
					aPStage [ i - 1].isActive ( ) == true) {
				aPStage [ i].iCurrent = aPStage [ i - 1].iCurrent;
			} else if ( aPStage [ i - 1].isActive ( ) == false) {
				aPStage [ i].iCurrent = getBubble ( );
			} else if ( aPStage [ i].isActive ( ) == false) {
				continue;
			}
		}

		aPStage [ iBegin].iCurrent = parNew;
		
		return ( true);
	}

//	Insere a instrucao PARNEW no estagio PARNAME.
	public void insert ( Instruction parNew, String parName) {
		ExecutionStage psAux;
		int iPoint = 0;

		if ( parName != null) {
			iPoint = spStages.searchPosition ( parName);
			if ( iPoint == - 1) return;
		}
		psAux = ( ExecutionStage) spStages.traverse ( iPoint);
		psAux.iCurrent = parNew;
	}

//	Insere uma BOLHA (instrucao inativa) no estagio PARNAME.
	public void insertBubble ( String parName) {
		walk ( getBubble ( ), parName);
	}

//	Desativa as instrucoes entre os estagios PARPS1 e PARPS2.
	public void discard ( String parPS1, String parPS2) {
		ExecutionStage psAux;
		int iP1 = - 1, iP2 = - 1;
		
		if ( parPS1 != null) iP1 = spStages.searchPosition ( parPS1);
		if ( parPS2 != null) iP2 = spStages.searchPosition ( parPS2);

		if ( iP1 == - 1) return;
		if ( iP2 == - 1) iP2 = iP1;
		
		for ( int i = iP1; i <= iP2; i ++) {
			Instruction itctAux;
			
			psAux = ( ExecutionStage) spStages.traverse ( i);
			itctAux = psAux.getCurrentInst ( );
			if ( itctAux != null) itctAux.setActiveCondition ( false);
		}
	}

//	Desativa os estagios PARPS1 ate PARPS2.
	public void freeze ( String parPS1, String parPS2) {
		ExecutionStage psAux;
		int iP1 = - 1, iP2 = - 1;
		
		if ( parPS1 != null) iP1 = spStages.searchPosition ( parPS1);
		if ( parPS2 != null) iP2 = spStages.searchPosition ( parPS2);

		if ( iP1 == - 1) return;
		if ( iP2 == - 1) iP2 = iP1;
		
		for ( int i = iP1; i <= iP2; i ++) {
			psAux = ( ExecutionStage) spStages.traverse ( i);
			psAux.setActiveCondition ( false);
		}
	}

//	Reativa os estagios PARPS1 ate PARPS2.
	public void release ( String parPS1, String parPS2) {
		ExecutionStage psAux;
		int iP1 = - 1, iP2 = - 1;
		
		if ( parPS1 != null) iP1 = spStages.searchPosition ( parPS1);
		if ( parPS2 != null) iP2 = spStages.searchPosition ( parPS2);

		if ( iP1 == - 1) return;
		if ( iP2 == - 1) iP2 = iP1;
		
		for ( int i = iP1; i <= iP2; i ++) {
			psAux = ( ExecutionStage) spStages.traverse ( i);
			psAux.setActiveCondition ( true);
		}
	}

/*
 *	Testa uma propriedade STRING de uma instrucao, retornando true se o valor
 *		corresponder ao argumento passado.
 */
	public boolean testInstruction ( String parName, String parProp, String parValue) {
		ExecutionStage psAux;
		int iPoint;

		if ( parName != null) {
			iPoint = spStages.searchPosition ( parName);
			if ( iPoint == - 1) return ( false);
			psAux = ( ExecutionStage) spStages.traverse ( iPoint);
			Instruction itctAux = psAux.getCurrentInst ( );
			if ( itctAux == null) return (false);
			else return ( itctAux.testInstruction( parProp, parValue));
		}
		
		return ( false);
	}

/*
 *	Testa uma propriedade STATUS ou FIELD de uma instrucao, retornando true 
 *		se o valor corresponder ao argumento passado.
 */
	public boolean testInstruction ( String parName, String parProp, int parValue) {
		ExecutionStage psAux;
		int iPoint;
		int iType;

		if ( parName != null) {
			iPoint = spStages.searchPosition ( parName);
			if ( iPoint == - 1) return ( false);
			psAux = ( ExecutionStage) spStages.traverse ( iPoint);
			Instruction itctAux = psAux.getCurrentInst ( );
			if ( itctAux == null) return (false);
			else return ( itctAux.testInstruction( parProp, parValue));
		}
		
		return ( false);
	}
	
	public int getTargetStage ( int parId) {
		int iNroStages = spStagesInternal.getNelems ( );
		
		// retorna o nro. do ultimo estagio Target no caso de um nro.maior que os estagio origin existentes
		if ( parId >= iNroStages) {
			int iTmp;
			ExecutionStage pStage;
			pStage = ( ExecutionStage) spStagesInternal.traverse ( iNroStages - 1);
			iTmp = pStage.getExecutionStageTargetId();
//System.out.println ( "Origin = "+ parId+",Target = "+iTmp);
			return iTmp;
		}			
//System.out.println ( "Pipestage source id: "+parId);
		for ( int i = 0; i < iNroStages; i ++) {
			ExecutionStage pStage;
			pStage = ( ExecutionStage) spStagesInternal.traverse ( i);
			if ( pStage.getExecutionStageId() == parId) {
//System.out.println ( "Pipestage target id: "+pStage.getPipeStageTargetId()+"\n");
				return ( pStage.getExecutionStageTargetId());
			}
		}
//System.out.println ( "Pipestage target id: -1");		
		return ( -1);
	}
	
	public void debug ( ) {
		ExecutionStage psAux;
		int i;

		System.out.println ( );
		System.out.println ( "*** Pipeline.debug:...BEGIN");
		System.out.println ( "Pipeline: "+getName ( ));
		System.out.println ( "* Nro.de estagios: "+iNsteps);
		switch ( iTimeMode) {
			case MONOCYCLE:
				System.out.println ( "MONOCYCLE PROCESSOR!");
				break;
			case MULTICYCLE:
				System.out.println ( "MULTICYCLE PROCESSOR!");
				break;
			case PIPELINED:
				System.out.println ( "PIPELINED PROCESSOR!");
				break;
			default:
				System.out.println ( "... PROCESSOR!");
				break;
		}
		
		for ( i = 0; i < spStages.getNelems ( ); i ++) {
			psAux = ( ExecutionStage) spStages.traverse ( i);
			psAux.debug ( );
		}
		
		System.out.println ( "*** Pipeline.debug:...END");
		System.out.println ( );
	}

	public void list ( ) {
		ExecutionStage psAux;
		int i;

		System.out.println ( "Pipeline: "+getName ( ));
		System.out.println ( "Nro.de estagios: "+iNsteps);
		switch ( iTimeMode) {
			case MONOCYCLE:
				System.out.println ( "MONOCYCLE PROCESSOR!");
				break;
			case MULTICYCLE:
				System.out.println ( "MULTICYCLE PROCESSOR!");
				break;
			case PIPELINED:
				System.out.println ( "PIPELINED PROCESSOR!");
				break;
			default:
				System.out.println ( "... PROCESSOR!");
				break;
		}

		System.out.println ( );		
		for ( i = 0; i < spStages.getNelems ( ); i ++) {
			psAux = ( ExecutionStage) spStages.traverse ( i);
			psAux.list ( );
			System.out.println ( );
		}
		/*
		System.out.println ( );		
		for ( i = 0; i < spStagesInternal.getNelems ( ); i ++) {
			psAux = ( ExecutionStage) spStagesInternal.traverse ( i);
			psAux.list ( );
			System.out.println ( );
		}	
		*/
	}

	public SetPipeStages spStages, spStagesInternal;
	private Instruction itctBubble;
	private int iNsteps, iTimeMode;
}
