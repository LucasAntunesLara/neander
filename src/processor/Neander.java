package processor;

import java.lang.reflect.Array;

import message.MicroOperation;
import message.SetMsg;
import ports.Field;
import ports.FieldString;
import ports.SetPort;
import ports.Status;
import shell.Shell;
import simulator.Clock;
import simulator.Simulator;
import util.SistNum;
import control.ExecutionPath;
import control.ExecutionStage;
import control.Instruction;
import datapath.Datapath;

/*******************************************************************************
 *	A SER CUSTOMIZADA PELO PROJETISTA.
 *	Implementa a classe principal do ambiente: a classe PROCESSOR.
 *	Insere atributos de STATUS (estado), FIELDS (campos - de instrucao) e
 *		STRINGS ( descricoes textuais).
 *
 *******************************************************************************/
public class Neander extends Processor {

	public Neander ( Simulator parSim) {
		super ( ".\\processors\\neander", parSim);
		dtp = new Datapath ( );
		sSim = parSim;

// Customizar aqui - BEGIN
		String [ ] asStatus = { };
		String [ ] asFields = { "OPCODE", "OP", "PC"};
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
		setInstructionSet ( mnemonicos, intOpcodes, sizeBytes);

// Customizar aqui - BEGIN
		set ( "NAME", 	STRING, "Neander");
// - END
	}

/*******************************************************************************
 *	A SER FORNECIDA PELO PROJETISTA.
 *	Retorna uma instrucao conforme ela foi lida.
 *	Neste caso, estah retornando um valor inteiro lido do componente IMEMORY,
 *		que eh uma memoria de instrucoes.
 *******************************************************************************/
	private long fetch ( ) {
		if ( dtp.execute ( "RI", GET, "FETCH", STATUSorCONF) == 1L) {
			long lOp = dtp.execute ( "RI", GET, "E1", IN);
			dtp.execute ( "RI", SET, "FETCH", STATUSorCONF, 0);
//System.out.println("fetch:"+lOp);
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
		String sAux;
		int iOp = 0, iAddress;

		Instruction itctNew = new Instruction ( );

		itctNew.set ( "OPCODE", FIELD, (int) parInst);
		sAux = sBinAux.substring ( 0, 4);
		iAddress = ( int) dtp.execute ( "rem", GET, "S1", OUT);
		itctNew.set ( "PC",  FIELD, iAddress - iBaseProgramAddress);
		
		if ( parInst == 112L) {
			itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\INSTRUCTIONS\\FETCH");
		} else {
			if ( sAux.equals ( "0011")) {
				itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\INSTRUCTIONS\\ALI");
				iOp = ADD;
				iNStepsForNextFetch = 5;
System.out.println ( "Decodificou um ADD.");
			}
			if ( sAux.equals ( "1110")) {
				itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\INSTRUCTIONS\\ALI");
				iOp = SUB;
				iNStepsForNextFetch = 5;
System.out.println ( "Decodificou um SUB.");
			}
			if ( sAux.equals ( "0100")) {
				itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\INSTRUCTIONS\\ALI");
				iOp = OR;
				iNStepsForNextFetch = 5;
System.out.println ( "Decodificou um OR.");
			}
			if ( sAux.equals ( "0101")) {
				itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\INSTRUCTIONS\\ALI");
				iOp = AND;
				iNStepsForNextFetch = 5;
System.out.println ( "Decodificou um AND.");
			}
			if ( sAux.equals ( "0110")) {
				itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\INSTRUCTIONS\\NOT");
				iOp = NOT_E1;
				iNStepsForNextFetch = 1;
System.out.println ( "Decodificou um NOT.");
			}
			if ( sAux.equals ( "0010")) {
				itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\INSTRUCTIONS\\ALI");
				iOp = E2;
				iNStepsForNextFetch = 5;
System.out.println ( "Decodificou um LOAD.");
			}
			if ( sAux.equals ( "0001")) {
				itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\INSTRUCTIONS\\STA");
				iNStepsForNextFetch = 5;
System.out.println ( "Decodificou um STORE.");
			}
			if ( sAux.equals ( "1000")) {
				itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\INSTRUCTIONS\\JMP");
				iNStepsForNextFetch = 3;
System.out.println ( "Decodificou um JUMP.");
			}
			if ( sAux.equals ( "0000")) {
				itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\INSTRUCTIONS\\NOP");
				iNStepsForNextFetch = 1;
System.out.println ( "Decodificou um NOP.");
			}
			if ( sAux.equals ( "1001")) { // JN
				if ( dtp.execute ( "N", GET, "E1", IN) == 1L) {
					itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\INSTRUCTIONS\\JMP");
					iNStepsForNextFetch = 3;
System.out.println ( "Decodificou um taken JN.");
				} else {
					itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\INSTRUCTIONS\\NOTAKEN");
					iNStepsForNextFetch = 1;
System.out.println ( "Decodificou um notaken JN.");
				}
			}
			if ( sAux.equals ( "1010")) { // JZ
				if ( dtp.execute ( "Z", GET, "E1", IN) == 1L) {
					itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\INSTRUCTIONS\\JMP");
					iNStepsForNextFetch = 3;
System.out.println ( "Decodificou um taken JZ.");
				} else {
					itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\INSTRUCTIONS\\NOTAKEN");
					iNStepsForNextFetch = 1;
System.out.println ( "Decodificou um notaken JZ.");
				}
			}
			if ( sAux.equals ( "1011")) { // JNZ
				if ( dtp.execute ( "Z", GET, "E1", IN) == 0L) {
					itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\INSTRUCTIONS\\JMP");
					iNStepsForNextFetch = 3;
System.out.println ( "Decodificou um taken JNZ.");
				} else {
					itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\INSTRUCTIONS\\NOTAKEN");
					iNStepsForNextFetch = 1;
System.out.println ( "Decodificou um notaken JNZ.");
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
				if ( parIt.exist ( sPortName, FIELD)) {
					int iActualValue = (int) parIt.get ( sPortName, FIELD);
					mdAux.setPortValue ( iActualValue);
				}
				break;			
		}
	}

/*******************************************************************************
*	A SER FORNECIDA PELO PROJETISTA.
*	Deve prever a inicializacao do processador
*******************************************************************************/
private void initialize ( ) {
	ExecutionPath pPipeCurrent = sePipes.search ( null);
	
System.out.println ( "INICIALIZANDO!!!!!!!!!!!!!!!");
	iFetch = decode ( 112L);	// 70h - opcode inexistente no Neander
	iFetch.set( "PC", FIELD, FETCH);
	iBaseProgramAddress = ( int) dtp.execute ( "PC", GET, 0, 0);
	pPipeCurrent.walk ( iFetch, "ZERO__");
	Clock.setInitialTime ( 0.0F);
	bBeginProgram = false;
}

/*	
 *	EH UM metodo INTERNO.
 *	Percorre os estagios de pipeline.
 *	Os passos de cada instrucao (representados por mensagens MSGDATAPATH) referentes
 *		ao estagio onde se encontra sao decodificados e enviados para execucao.
 */
	protected void executeForward ( String whichPipe) {
		ExecutionPath pPipeCurrent = sePipes.search ( whichPipe);
		ExecutionStage psAux;
		Instruction itctAux;
		for ( int i = 0; i < pPipeCurrent.getExecutionStages().getNelems ( ); i ++) {
			psAux = ( ExecutionStage) pPipeCurrent.spStages.traverse ( i);
			if ( psAux.isActive ( ) == false) continue;
			itctAux = psAux.getCurrentInst ( );
			if ( itctAux != null) {
				if ( itctAux.isActive ( ) == false) continue;
				SetMsg smAux = itctAux.smMops;
				for ( int j = 0; j < smAux.getNelems ( ); j ++) {
					MicroOperation mdAux = (MicroOperation) smAux.traverse ( j);
					//if ( mdAux.fTime == psAux.getPipeStageId ( )) {
					if ( pPipeCurrent.getTargetStage ( ( int) mdAux.fTime) == psAux.getExecutionStageId ( )) {
						decodeAfter ( mdAux, itctAux);
//System.out.println ( "Executa...");
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
 *******************************************************************************/
	public void execute ( ) {
		executeForward ( null);		
	}

/*******************************************************************************
 *	A SER FORNECIDA PELO PROJETISTA.
 *	Eh o metodo principal de PROCESSOR.
 *	Deve chamar o metodo INITIALIZE
 *	Deve chamar o metodo FETCH para buscar novas instrucoes.
 *	Deve chamar o metodo DECODE para criar objetos Instruction.
 *	Deve chamar o metodo EXECUTE
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

		if ( bBeginProgram == true) initialize ( );
		execute ( );			
		
		long lOpcode = fetch ( );
		if ( lOpcode != -1L) {
			if ( lOpcode == -16L) {
				System.out.println ( "FINAL DE PROGRAMA!!!");
				bEndProgram = true;
				// para nao continuar simulando instruçoes
				bIsNewInstruction = true;
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
//System.out.println ( "Inserindo um novo FETCH.");
				pPipeCurrent.insert ( iFetch, "ZERO__");
				bIsNewInstruction = true;
			} else bIsNewInstruction = false;

			sSim.advanceTime ( );
//NeanderApp.messagesToUser ( this.getClass().toString()," Avançando tempo para: "+sSim.getTime());
		}

		return ( bEndProgram);
	}

	public void setReset ( String parProgram) throws Exception {
		String sProgram = null;
		Simulator SIM_NEW = new Simulator ( );
		Neander 	neander 	= new Neander 	( SIM_NEW);
		Shell.initialize ( "# Neander> ",		SIM_NEW,( Processor) neander);
		if ( parProgram == null) sProgram = "Program.bin";
		else sProgram = parProgram;
		System.out.println ( "file name: "+sProgram);
		neander.initialize ( 	"NeanderOrg.txt",sProgram,	"InstructionSet.txt","Tempos.txt");
		SIM_NEW.resetTime ( );
		SIM_NEW.Initializations ( neander);
		bFirstInstruction = true;
		bBeginProgram = true;
		Shell.decodifica ( "reset neander");
	}
	
	public void resetIsNewInstruction ( ) {
		bIsNewInstruction = false;
	}

	public boolean isNewInstruction ( ) {
		return ( bIsNewInstruction);
	}
	
	protected Instruction iFetch;

	private boolean bFirstInstruction = true;
	private boolean bBeginProgram = true;
	private boolean bIsNewInstruction = true;
	private int iBaseProgramAddress = 0;
	
	private int iNStepsForNextFetch = 100;
	
	private String mnemonicos [ ] = { "SUB", "STA", "LDA", "ADD", "OR", "AND", "NOT", "JMP", "JN", "JZ", "JNZ", "NOP", "HLT", "Fetch"};
	//private long intOpcodes [ ] = { 224, 16, 32, 48, 64, 80, 96, 128, 144, 160, 176, 0, 240, 112};
	private long intOpcodes [ ] = { -32, 16, 32, 48, 64, 80, 96, -128, -112, -96, -80, 0, -16, 112};
	private int sizeBytes [ ] = { 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 1, 1, 0};
	
	//private String sProgram = null;
}
