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
public class FemtoJava extends Processor {

	public FemtoJava ( Simulator parSim) {
		super ( ".\\processors\\femtojava", parSim);
		dtp = new Datapath ( );
		sSim = parSim;

// Customizar aqui - BEGIN
		String [ ] asStatus = { "RESET", "INT"};
		String [ ] asFields = { "OP"};
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
		set ( "NAME", 	STRING, "FemtoJava");
		set ( "RESET", 	STATUSorCONF, 0);
		set ( "INT", 	STATUSorCONF, 0);
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
		String sBinAux = SistNum.toBinString ( parInst, WORD);
		String sAux;
		int iOp = 0;

		Instruction itctNew = new Instruction ( );

		itctNew.set ( "OPCODE", FIELD, (int) parInst);
		sAux = sBinAux.substring ( 0, 8);

		if ( parInst == 112L) {
			itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\FETCH");
		} else {
			if ( sAux.equals ( "00000000")) {
				itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\ALIother");
				iOp = ADD;
				iNStepsForNextFetch = 2;
System.out.println ( "Decodificou um IADD.");
			}
			if ( sAux.equals ( "00000001")) {
				itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\ALIother");
				iOp = SUB;
				iNStepsForNextFetch = 2;
System.out.println ( "Decodificou um ISUB.");
			}
			if ( sAux.equals ( "00000010")) {
				itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\ALIother");
				iOp = MUL;
				iNStepsForNextFetch = 2;
System.out.println ( "Decodificou um IMUL.");
			}
			if ( sAux.equals ( "00000011")) {
				itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\ILOAD");
				iOp = E1;
				iNStepsForNextFetch = 3;
System.out.println ( "Decodificou um ILOAD.");
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
//System.out.println ( sPortName +",");
				if ( parIt.exist ( sPortName, FIELD)) {
					int iActualValue = (int) parIt.get ( sPortName, FIELD);
					mdAux.setPortValue ( iActualValue);
//System.out.println ( sPortName +","+iActualValue);
				}
				break;			
		}
	}

/*
 *  IMPLEMENTA A DETECCAO DE INTERRUPCAO
 */
private void interrupt ( ) {
	// tratar interrupcao
}

/*
 *  IMPLEMENTA O RESET DO PROCESSADOR
 */
private void reset ( ) {
	ExecutionPath pPipeCurrent = sePipes.search ( null);
	
	pPipeCurrent.discard ( "UM_____", "CATORZE");
	iNStepsForNextFetch = 0;
	Clock.setInitialTime ( 0.0F);
	dtp.execute ( "PC", SET, 0,0,0);
	dtp.execute ( "MAR", SET, 0,0,0);
	dtp.execute ( "SP", SET, 0,0,0);
	dtp.execute ( "A", SET, 0,0,0);
	dtp.execute ( "B", SET, 0,0,0);
}

/*	
 *	EH UM metodo INTERNO.
 *	Percorre os estagios de pipeline.
 *	Os passos de cada instrucao (representados por mensagens MSGDATAPATH) referentes
 *		ao estagio onde se encontra sao decodificados e enviados para execucao.
 *
 *	NECESSARIO TRAZER PARA CA' PORQUE, SENAO, ELE EXECUTA ESTES meto-dos 
 *	(decodeAfter tambem) NA CLASSE PROCESSOR!
 */
	protected void executeForward ( ) {
		ExecutionPath pPipeCurrent = sePipes.search ( null);
		ExecutionStage psAux;
		Instruction itctAux;

		for ( int i = 0; i < pPipeCurrent.spStages.getNelems ( ); i ++) {
			psAux = ( ExecutionStage) pPipeCurrent.spStages.traverse ( i);
			if ( psAux.isActive ( ) == false) continue;
// BEGIN - Codigo inserido para tratamento de interrupcao
			if ( 	psAux.getName().toString().compareToIgnoreCase ( "UM_____") == 0 &&	
					get ( "INT", STATUSorCONF) == 1) {
				System.out.println ( "PROCESSADOR FOI INTERROMPIDO!!!\n");
				set ( "INT", 	STATUSorCONF, 0);
				interrupt ( );
				return;
			}
// END   - Codigo inserido para tratamento de interrupcao
			itctAux = psAux.getCurrentInst ( );
			if ( itctAux != null) {
				if ( itctAux.isActive ( ) == false) continue;
				SetMsg smAux = itctAux.smMops;
				for ( int j = 0; j < smAux.getNelems ( ); j ++) {
					MicroOperation mdAux = (MicroOperation) smAux.traverse ( j);
					if ( mdAux.fTime == psAux.getExecutionStageId ( )) {
// BEGIN - Codigo inserido para tratamento de reset
//						if ( -- iTstReset == 0) {
//							set ( "RESET", 	STATUS, 1);
//						}
//System.out.println ( "iTstReset = "+iTstReset);
//System.out.println ( "RESET = "+get ( "RESET", STATUS));
						if ( get ( "RESET", STATUSorCONF) == 1) {
							System.out.println ( "PROCESSADOR FOI RESETADO!!!\n");
							set ( "RESET", 	STATUSorCONF, 0);
							reset ( );
							return;
// END   - Codigo inserido para tratamento de reset
						} else {
							decodeAfter ( mdAux, itctAux);
							execute ( mdAux);
						}
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
			iFetch = decode ( 112L);
			pPipeCurrent.walk ( iFetch, "UM_____");
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
		
		execute ( );

		long lOpcode = fetch ( );
		if ( lOpcode != -1L) {
			if ( lOpcode == 0xeeeeeeee) {
				System.out.println ( "FINAL DE PROGRAMA!!!");
				pPipeCurrent.insert ( null, "UM_____");		
				bEndProgram = true;
			}
			else iNew = decode ( lOpcode);
		} else {
			iNew = null;
		}

		if ( bEndProgram == false) {
			if ( iNew != null) {
				pPipeCurrent.insert ( null, "UM_____");		
				pPipeCurrent.walk ( iNew, "DOIS___");
			} else {
				pPipeCurrent.walk ( null, "UM_____");	
			}
		
			if ( iNStepsForNextFetch -- == 0) {
System.out.println ( "Inserindo um novo FETCH.");
				pPipeCurrent.insert ( iFetch, "UM_____");
			}
		
			sSim.advanceTime ( );
		}

		return ( bEndProgram);
	}

	private boolean bBeginProgram = true;
	private boolean bEndProgram = false;
		
	protected Instruction iFetch;

	private int iNStepsForNextFetch = 100;
	
//  VARIAVEL TEMPORARIA PARA TESTAR UM RESET
	private int iTstReset = 50;
}
