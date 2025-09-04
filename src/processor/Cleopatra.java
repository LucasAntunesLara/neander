package processor;

import java.lang.reflect.Array;

import message.MicroOperation;
import message.SetMsg;
import ports.Field;
import ports.FieldString;
import ports.SetPort;
import ports.Status;
import simulator.Clock;
import simulator.Simulator;
import util.SistNum;
import control.Instruction;
import control.ExecutionStage;
import control.ExecutionPath;
import datapath.Datapath;

/*******************************************************************************
 *	A SER CUSTOMIZADA PELO PROJETISTA.
 *	Implementa a classe principal do ambiente: a classe PROCESSOR.
 *	Insere atributos de STATUS (estado), FIELDS (campos - de instrucao) e
 *		STRINGS ( descricoes textuais).
 *
 *******************************************************************************/
public class Cleopatra extends Processor {

	public Cleopatra ( Simulator parSim) {
		super ( ".\\processors\\cleopatra", parSim);
		dtp = new Datapath ( );
		sSim = parSim;

// Customizar aqui - BEGIN
		String [ ] asStatus = { };
		String [ ] asFields = { "OPCODE", "OP"};
		String [ ] asStrings = { "NAME"};
// - END
		
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

		Instruction.defineFieldsOfInstructions ( asFields);

// Customizar aqui - BEGIN
//		set ( "BRANCH",	STATUS, 0);
		set ( "NAME", 	STRING, "Cleopatra");
// - END
	}

/*******************************************************************************
 *	A SER FORNECIDA PELO PROJETISTA.
 *	Retorna uma instrucao conforme ela foi lida.
 *	Neste caso, estah retornando um valor inteiro lido do componente IMEMORY,
 *		que eh uma memoria de instrucoes.
 *******************************************************************************/
	private long fetch ( ) {
		if ( dtp.execute ( "IR", GET, "FETCH", STATUSorCONF) == 1L) {
			long lOp = dtp.execute ( "IR", GET, "E1", IN);
			dtp.execute ( "IR", SET, "FETCH", STATUSorCONF, 0);

			return ( lOp);
		}
		
		return ( -1L);
	}

/*******************************************************************************
 *	A SER FORNECIDA PELO PROJETISTA.
 *	Deve receber o retorno do metodo FETCH.
 *	O codigo a ser fornecido deve decodificar o argumento recebido e setar
 *		as propriedades STATUS e FIELD que foram definidos para um objeto Instruction.
 *		Deve fornecer tambem uma descricao para a instrucao (propriedade DESCRIPTION).
 *	CHAMA A DECODEINTERNAL OBRIGATORIAMENTE.
 *******************************************************************************/
	private Instruction decode ( long parInst) {
		String sBinAux = SistNum.toBinString ( parInst, BYTE);
		String sOp, sME;
		int iOp = 0;

		Instruction itctNew = new Instruction ( );

		itctNew.set ( "OPCODE", FIELD, (int) parInst);
		sOp = sBinAux.substring ( 0, 4);
		sME = sBinAux.substring ( 4, 6);
		
		if ( parInst == 112L) {
			itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\FETCH");
		} else {
			if ( sOp.equals ( "0101")) {
				iOp = ADD;
				if ( sME.equals ( "00")) {
					itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\ALI_IM");
					iNStepsForNextFetch = 3;
				} else if ( sME.equals ( "01")) {
					itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\ALI_DIR");
					iNStepsForNextFetch = 5;
				} else if ( sME.equals ( "10")) {
					itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\ALI_INDIR");
					iNStepsForNextFetch = 7;
				} else if ( sME.equals ( "11")) {
					itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\ALI_REL");
					iNStepsForNextFetch = 5;
				}
System.out.println ( "Decodificou um ADD"+sME);
			}
			if ( sOp.equals ( "0110")) {
				iOp = OR;
				if ( sME.equals ( "00")) {
					itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\ALI_IM");
					iNStepsForNextFetch = 3;
				} else if ( sME.equals ( "01")) {
					itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\ALI_DIR");
					iNStepsForNextFetch = 5;
				} else if ( sME.equals ( "10")) {
					itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\ALI_INDIR");
					iNStepsForNextFetch = 7;
				} else if ( sME.equals ( "11")) {
					itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\ALI_REL");
					iNStepsForNextFetch = 5;
				}
System.out.println ( "Decodificou um OR"+sME);
			}
			if ( sOp.equals ( "0111")) {
				iOp = AND;
				if ( sME.equals ( "00")) {
					itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\ALI_IM");
					iNStepsForNextFetch = 3;
				} else if ( sME.equals ( "01")) {
					itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\ALI_DIR");
					iNStepsForNextFetch = 5;
				} else if ( sME.equals ( "10")) {
					itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\ALI_INDIR");
					iNStepsForNextFetch = 7;
				} else if ( sME.equals ( "11")) {
					itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\ALI_REL");
					iNStepsForNextFetch = 5;
				}
System.out.println ( "Decodificou um AND"+sME);
			}
			if ( sOp.equals ( "0100")) {
				iOp = E1;
				if ( sME.equals ( "00")) {
					itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\ALI_IM");
					iNStepsForNextFetch = 3;
				} else if ( sME.equals ( "01")) {
					itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\ALI_DIR");
					iNStepsForNextFetch = 5;
				} else if ( sME.equals ( "10")) {
					itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\ALI_INDIR");
					iNStepsForNextFetch = 7;
				} else if ( sME.equals ( "11")) {
					itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\ALI_REL");
					iNStepsForNextFetch = 5;
				}
System.out.println ( "Decodificou um LDA"+sME);
			}
			if ( sOp.equals ( "0000") || sOp.equals ( "0001")) {
				iOp = NOT_E2;
				itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\NOT");
				iNStepsForNextFetch = 1;
System.out.println ( "Decodificou um NOT");
			}
			if ( sOp.equals ( "0010") || sOp.equals ( "0011")) {
				iOp = 0;
				itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\STA");
				iNStepsForNextFetch = 4;
System.out.println ( "Decodificou um STA");
			}
			if ( sOp.equals ( "1000")) {
				iOp = 0;
				itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\JMP");
				iNStepsForNextFetch = 3;
System.out.println ( "Decodificou um JMP");
			}
			if ( sOp.equals ( "1100")) {
				iOp = 0;
				itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\JSR");
				iNStepsForNextFetch = 4;
System.out.println ( "Decodificou um JSR");
			}
			if ( sOp.equals ( "1101")) {
				iOp = 0;
				itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\RST");
				iNStepsForNextFetch = 1;
System.out.println ( "Decodificou um RST");
			}
			if ( sOp.equals ( "1010")) { // JN
				if ( dtp.execute ( "N", GET, "S1", OUT) == 1L) {
					itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\JMP");
					iNStepsForNextFetch = 3;
System.out.println ( "Decodificou um taken JN.");
				} else {
					itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\JMP_NOTTAKEN");
					iNStepsForNextFetch = 2;
System.out.println ( "Decodificou um notaken JN.");
				}
			}
			if ( sOp.equals ( "1011")) { // JZ
				if ( dtp.execute ( "Z", GET, "S1", OUT) == 1L) {
					itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\JMP");
					iNStepsForNextFetch = 3;
System.out.println ( "Decodificou um taken JZ.");
				} else {
					itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\JMP_NOTTAKEN");
					iNStepsForNextFetch = 2;
System.out.println ( "Decodificou um notaken JZ.");
				}
			}
			if ( sOp.equals ( "1001")) { // JC
				if ( dtp.execute ( "C", GET, "S1", OUT) == 1L) {
					itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\JMP");
					iNStepsForNextFetch = 3;
System.out.println ( "Decodificou um taken JC.");
				} else {
					itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\JMP_NOTTAKEN");
					iNStepsForNextFetch = 2;
System.out.println ( "Decodificou um notaken JC.");
				}
			}
			if ( sOp.equals ( "1110")) { // JV
				if ( dtp.execute ( "V", GET, "S1", OUT) == 1L) {
					itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\JMP");
					iNStepsForNextFetch = 3;
System.out.println ( "Decodificou um taken JV.");
				} else {
					itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\JMP_NOTTAKEN");
					iNStepsForNextFetch = 2;
System.out.println ( "Decodificou um notaken JV.");
				}
			}
		}

		itctNew.set ( "OP",  FIELD, iOp);

		decodeInternal ( itctNew);

		return ( itctNew);
	}

/*******************************************************************************
 *	A SER CUSTOMIZADA PELO PROJETISTA.
 *	Eh chamada durante a execucao da instrucao.
 *	Recebe a mensagem a ser enviada PARMD para execucao ao objeto DATAPATH.
 *		E a instrucao PARIT.
 *	O codigo a ser fornecido pode alterar o comportamento da instrucao.
 *		Para isso, os atributos STATUS e FIELD de PARIT devem ser consultados e,
 *		sendo o caso, alteracoes podem ser executadas na mensagem a ser enviada.
 *	O metodo pre-existente consulta os atributos FIELD de PARIT referentes a 
 *		portas de controle e seta-os na mensagem a ser enviada para execucao 
 *		( codigo de operacao da ULA por exemplo).
 *
 *	OBSERVACAO:
 *	As mensagens que constam na instrucao sao provenientes de um objeto
 *		INSTRUCTIONTYPE que prove um roteiro de execucao para um tipo de
 *		instrucao (aritmeticas, por exemplo). Porem, instrucoes que executam
 *		funcoes diferentes podem pertencer a um mesmo tipo (add e sub, por 
 *		exemplo). Este eh o local para fazer a diferenciacao entre elas.
 *******************************************************************************/
	protected void decodeAfter ( MicroOperation parMd, Instruction parIt)
	{
		MicroOperation mdAux = ( MicroOperation) parMd;
		int iMethod = mdAux.getMethodId ( );
		switch ( iMethod) {
			case BEHAVIOR:
				break;
			case READ:
				break;
			case WRITE:
				break;
			case PROPAGATE:
				break;
			case SET:
				String sPortName = mdAux.getPortName ( );
				if ( 	parIt.exist ( sPortName, FIELD)) {
					int iActualValue = (int) parIt.get ( sPortName, FIELD);
		//			System.out.println ( mdAux.getComponentName ( )+mdAux.getPortValue ( ));
					if ( 	mdAux.getComponentName ( ).equals ( "ALU") &&
							mdAux.getPortValue ( ) == 0L) {
		//				System.out.println ( "Setando operacao da ULA");
						mdAux.setPortValue ( iActualValue);
					}
				}
				break;			
		}
	}

/*
 *	COPIADA DE PROCESSOR PARA EXECUTAR A DECODEAFTER DE CLEOPATRA
 */
	protected void executeForward ( String whichPipe) {
		ExecutionPath pPipeCurrent = sePipes.search ( whichPipe);
		ExecutionStage psAux;
		Instruction itctAux;

		for ( int i = 0; i < pPipeCurrent.getExecutionStages ( ).getNelems ( ); i ++) {
			psAux = ( ExecutionStage) pPipeCurrent.spStages.traverse ( i);
			if ( psAux.isActive ( ) == false) continue;
			itctAux = psAux.getCurrentInst ( );
			if ( itctAux != null) {
				if ( itctAux.isActive ( ) == false) continue;
				SetMsg smAux = itctAux.smMops;
				for ( int j = 0; j < smAux.getNelems ( ); j ++) {
					MicroOperation mdAux = (MicroOperation) smAux.traverse ( j);
					if ( mdAux.fTime == psAux.getExecutionStageId ( )) {
						decodeAfter ( mdAux, itctAux);
						execute ( mdAux);
					}
				}
			}
		}
	}

/*******************************************************************************
 *	A SER FORNECIDA PELO PROJETISTA.
 *	Deve percorrer os estagios de pipeline executando as instrucoes.
 *	Ha metodos pre-definidos que podem ser chamados.
 *	Deve prever a inicializacao do processador.
 *******************************************************************************/
	public void execute ( ) {
		ExecutionPath pPipeCurrent = sePipes.search ( null);
		
		if ( bBeginProgram == true) {
			iFetch = decode ( 112L);	// 70h - opcode inexistente no Neander
			pPipeCurrent.walk ( iFetch, "ZERO__");
			Clock.setInitialTime ( 0.0F);
			bBeginProgram = false;			
		}

		executeForward ( null);		
	}

/*******************************************************************************
 *	A SER FORNECIDA PELO PROJETISTA.
 *	Eh o metodo principal de PROCESSOR.
 *	Deve chamar o metodo FETCH para buscar novas instrucoes.
 *	Deve chamar o metodo DECODE para criar objetos Instruction.
 *	Deve inserir instrucoes no pipeline e movimentah-las.
 *	Deve avancar o tempo de simulacao.
 *
 *	OBSERVACAO:
 *		Esta funcao eh chamada pelo Simulador sempre apos a execucao do metodo
 *			execute.
 *******************************************************************************/
	public boolean behavior ( ) {
		ExecutionPath pPipeCurrent = sePipes.search ( null);
		boolean bEndProgram = false;
		
		execute ( );

		long lOpcode = fetch ( );
		if ( lOpcode != -1L) {
			if ( lOpcode == 240L) {
				System.out.println ( "FINAL DE PROGRAMA!!!");
				bEndProgram = true;
			}
			else iNew = decode ( lOpcode);
		} else {
			iNew = null;
		}

		if ( bEndProgram == false) {
			if ( iNew != null) {
				pPipeCurrent.insert ( null, "DOIS__");		
				pPipeCurrent.walk ( iNew, "TRES__");
			} else {
				pPipeCurrent.walk ( null, "ZERO__");	
			}
		
			if ( iNStepsForNextFetch -- == 0) {
System.out.println ( "Inserindo um novo FETCH.");
				pPipeCurrent.insert ( iFetch, "ZERO__");
			}
		
			sSim.advanceTime ( );
		}

		return ( bEndProgram);
	}

	protected Instruction iFetch;

	private boolean bFirstInstruction = true;
	private boolean bBeginProgram = true;
	
	private int iNStepsForNextFetch = 100;
}
