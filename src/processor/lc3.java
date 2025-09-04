package processor;

import java.lang.reflect.Array;

import montador.Montador;

import platform.Platform;
import ports.Field;
import ports.FieldString;
import ports.SetPort;
import ports.Status;
import control.ExecutionPath;
import control.Instruction;
import cseq.Sequential;
import datapath.Datapath;
import shell.Shell;
import simdraw.lc3App;
import simulator.Clock;
import simulator.Simulator;
import util.SistNum;

public class lc3 extends Processor {

	public lc3 ( Simulator parSim) {
		// deve indicar aqui a pasta onde estao os arquivos de descricao
		super ( ".\\processors\\lc3", parSim);
		dtp = new Datapath ( );
		sSim = parSim;

// Customizar aqui - BEGIN
		String [ ] asStatus = { };
		// devem ser listados aqui os campos da instrucao cujo valor sera
		//    fornecido na decodificacao da instrucao
		// OP e um campo que define a operacao a ser executada pela ALU do
		//    processador Example
		String [ ] asFields = { "OPCODE", "OP", "NR0", "NR1", "NW0", "trapV"};
		// NAME e uma descricao textual que ira conter o nome do processador
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
		// deve ser indicado aqui o nome do processador
		set ( "NAME", 	STRING, "LC-3");
// - END
		setArchitecturalRegistersNames(archRegs);
	}

/*******************************************************************************
 *	A SER FORNECIDA PELO PROJETISTA.
 *	Retorna uma instrucao conforme ela foi lida.
 *******************************************************************************/
	private long fetch ( ) {
		Sequential ir;
		
		ir = (Sequential) dtp.search("IR");
		if ( ir.isUsed() && ir.getMethod() == WRITE) {
			long lOp = dtp.execute ( "IR", GET, "E1", IN);
//lc3App.messagesToUser ( this.getClass().toString(), " valor lido do IR: "+lOp);
			return ( lOp);
		}
//lc3App.messagesToUser ( this.getClass().toString(), " retornando -1");
		
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
		int iOp = -1, iNr0 = -1, iNr1 = -1, iNw0 = -1, iOpcode = -1, iTrapVect8 = -1, iSel = -1, iSel2 = -1;
		int iInst;

		Instruction itctNew = new Instruction ( );
		iInst = new Long ( parInst).intValue ( );
		iOpcode = SistNum.getBitRange ( iInst, 16, 19);
		iNr0 = SistNum.getBitRange ( iInst, 23, 25);
		iNr1 = SistNum.getBitRange ( iInst, 29, 31);
		iNw0 = SistNum.getBitRange ( iInst, 20, 22);
		iTrapVect8 = SistNum.getBitRange ( iInst, 24, 31);
		iSel = SistNum.getBitRange ( iInst, 26, 26);
		iSel2 = SistNum.getBitRange ( iInst, 20, 20);
		//
		itctNew.set ( "OPCODE", FIELD, iOpcode);
		
		switch ( iOpcode){
			case 0x01: // ADDR e ADDI
				if ( iSel == 0)
					itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\INSTRUCTIONS\\ALUR");
				else
					itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\INSTRUCTIONS\\ALUI");
				iOp = ADD;
				iNStepsForNextFetch = 5;
				break;
			case 0x05: // ANDR e ANDI
				if ( iSel == 0)
					itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\INSTRUCTIONS\\ALUR");
				else
					itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\INSTRUCTIONS\\ALUI");
				iOp = AND;
				iNStepsForNextFetch = 5;
				break;
			case 0x00: // BR
				long ln = dtp.execute ( "N", GET, "S1", OUT);
				long lz = dtp.execute ( "Z", GET, "S1", OUT);
				long lp = dtp.execute ( "P", GET, "S1", OUT);
				long lbn = SistNum.getBitRange ( iInst, 20, 20);
				long lbz = SistNum.getBitRange ( iInst, 21, 21);
				long lbp = SistNum.getBitRange ( iInst, 22, 22);
System.out.println("n = " + ln + "z = " + lz + "p = " + lp + "bit n = " + lbn + "bit z = " + lbz + "bit p = " + lbp);
				if (lbn == 1 && ln == lbn || lbz == 1 && lz == lbz || lbp == 1 && lp == lbp){
					itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\INSTRUCTIONS\\BRY");
					iNStepsForNextFetch = 3;
				} else{
					itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\INSTRUCTIONS\\BRN");
					iNStepsForNextFetch = 1;
				}
				break;
			case 0x0C: // JMP e RET
				itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\INSTRUCTIONS\\JMP");
				iNStepsForNextFetch = 3;
				break;
			case 0x04: // JSR e JSRR
				iNw0 = 0x07;
				if (iSel2 == 1){
					itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\INSTRUCTIONS\\JSR");
					iNStepsForNextFetch = 4;
				} else {
					itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\INSTRUCTIONS\\JSRR");
					iNStepsForNextFetch = 4;
				}
				break;
			case 0x02: // LD
				itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\INSTRUCTIONS\\LD");
				iNStepsForNextFetch = 4;
				break;
			case 0x0A: // LDI
				itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\INSTRUCTIONS\\LDI");
				iNStepsForNextFetch = 5;
				break;
			case 0x06: // LDR
				itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\INSTRUCTIONS\\LDR");
				iNStepsForNextFetch = 4;
				break;
			case 0x0E: // LEA
				itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\INSTRUCTIONS\\LEA");
				iNStepsForNextFetch = 3;
				break;
			case 0x09: // NOT
				itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\INSTRUCTIONS\\ALUI");
				iOp = NOT_E2;
				iNStepsForNextFetch = 5;
				break;
			case 0x08: // RTI
				/**
				 * Necessita da parte de interupções que ainda não foi implementada.
				 */
				itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\INSTRUCTIONS\\RTI");
				break;
			case 0x03: // ST
				itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\INSTRUCTIONS\\ST");
				iOp = E1;
				iNStepsForNextFetch = 4;
				break;
			case 0x0B: // STI
				itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\INSTRUCTIONS\\STI");
				iOp = E1;
				iNStepsForNextFetch = 5;
				break;
			case 0x07: // STR
				itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\INSTRUCTIONS\\STR");
				iOp = E1;
				iNStepsForNextFetch = 4;
				break;
			case 0x0F: // TRAP <<< falta incluir rotinas
				iNw0 = 0x07;
				itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\INSTRUCTIONS\\TRAP");
				iNStepsForNextFetch = 5;
				break;
			case 0x0D: // FETCH
				itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\INSTRUCTIONS\\FETCH");
				break;
				
			default:
				break;
		}
		
		
		
		
		
		////////////////////////////////////////////
		/*if ( iOpcode == 0x0F && iTrapVect8 == 0x25) {
			// 
		}*/

		itctNew.set ( "OP",  FIELD, iOp);
		itctNew.set ( "NR0",  FIELD, iNr0);
		if (iOpcode == 0x03 || iOpcode == 0x0B || iOpcode == 0x07)
			itctNew.set ( "NR1",  FIELD, iNw0);
		else
			itctNew.set ( "NR1",  FIELD, iNr1);
		itctNew.set ( "NW0",  FIELD, iNw0);
		itctNew.set ( "trapV",  FIELD, iTrapVect8);
		decodeInternal ( itctNew);
		// itctNew.list();
		//itctNew.set ( "mnem",STRING,Montador.getAssemblyCode(iAddress));

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
 *******************************************************************************
	private void decodeAfter ( MsgDatapath parMd, Instruction parIt)
	{
		MsgDatapath mdAux = ( MsgDatapath) parMd;

		int iMethod = mdAux.getMethodId ( );
		switch ( iMethod) {
			case BEHAVIOR:
				break;
			case READ:
				if ( mdAux.getComponentName().compareToIgnoreCase("mem") == 0){
					System.out.println ( "Lendo da memoria");
				}
				break;
			case WRITE:
				break;
			case PROPAGATE:
				break;
			case SET:
				String sPortName = mdAux.getPortName ( );
				if ( parIt.exist ( sPortName, FIELD)) {
					int iActualValue = parIt.get ( sPortName, FIELD);
					mdAux.setPortValue ( iActualValue);
				}
				break;			
		}
	}*/

/*******************************************************************************
*	A SER FORNECIDA PELO PROJETISTA.
*	Deve prever a inicializacao do processador
*******************************************************************************/
private void initialize ( ) {
	ExecutionPath pPipeCurrent = sePipes.search ( null);
	
System.out.println ( "INICIALIZANDO!!!!!!!!!!!!!!!");
// Cria uma instrucao Fetch "artificial" para simplesmente ler
//    a primeira instrucao. Este tipo de instrucao so e usada neste
//    momento
//  1101 e' opcode reservado para uso futuro no LC-3
	iFetch = decode ( 0xD000);
// 
	pPipeCurrent.walk ( iFetch, "FETCH_1");
// 
	Clock.setInitialTime ( 0.0F);
	bBeginProgram = false;			
}

/*	
 *	EH UM metodo INTERNO.
 *	Percorre os estagios de pipeline.
 *	Os passos de cada instrucao (representados por mensagens MSGDATAPATH) referentes
 *		ao estagio onde se encontra sao decodificados e enviados para execucao.
 *
	protected void executeForward ( String whichPipe) {
		Pipeline pPipeCurrent = sePipes.search ( whichPipe);
		PipeStage psAux;
		Instruction itctAux;
		for ( int i = 0; i < pPipeCurrent.getPipeStages().getNelems ( ); i ++) {
			psAux = ( PipeStage) pPipeCurrent.spStages.traverse ( i);
			if ( psAux.isActive ( ) == false) continue;
			itctAux = psAux.getCurrentInst ( );
			if ( itctAux != null) {
				if ( itctAux.isActive ( ) == false) continue;
				SetMsg smAux = itctAux.smMops;
				for ( int j = 0; j < smAux.getNelems ( ); j ++) {
					MsgDatapath mdAux = (MsgDatapath) smAux.traverse ( j);
					//if ( mdAux.fTime == psAux.getPipeStageId ( )) {
					if ( pPipeCurrent.getTargetStage ( ( int) mdAux.fTime) == psAux.getPipeStageId ( )) {
						decodeAfter ( mdAux, itctAux);
//System.out.println ( "Executa...");
						execute ( mdAux);
					}
				}
			}
		}
	}*/
	
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
		
		if ( bBeginProgram == true) initialize ( );
		
		execute ( );	
		// Busca uma instrucao
		long lOpcode = fetch ( );
		//
//System.out.println ( lOpcode);
		if ( lOpcode != -1L) {
			if ( lOpcode == -4059) { // trap Halt
				System.out.println ( "FINAL DE PROGRAMA!!!");
				// para nao continuar simulando instrucoes
				bEndProgram = true;
				bIsNewInstruction = true;
			}
			else iNew = decode ( lOpcode);
		} else {
			iNew = null;
		}

		if ( bEndProgram == false) {
			if ( iNew != null) {
				pPipeCurrent.insert ( null, "FETCH_3");		
				pPipeCurrent.walk ( iNew, "DECODE");
			} else {
				pPipeCurrent.walk ( null, "FETCH_1");	
			}
		
			if ( iNStepsForNextFetch -- == 0) {
				pPipeCurrent.insert ( iFetch, "FETCH_1");
				bIsNewInstruction = true;
			} else bIsNewInstruction = false;

			sSim.advanceTime ( );
		}

		return ( bEndProgram);
	}
	
	public void setReset ( String parProgram) throws Exception {
		String sProgram = null;
		Simulator SIM_NEW = new Simulator ( );
		lc3 	lc 	= new lc3 	( SIM_NEW);
		Shell.initialize ( "# LC3> ",		SIM_NEW,( Processor) lc);
		if ( parProgram == null) sProgram = "program.bin"/*"programs"+ Platform.getSeparatorPath()+"_tempBin.bin"*/;
		else sProgram = parProgram;
		System.out.println ( "file name: "+sProgram);
		lc.initialize ( 	"lc3Org.txt",sProgram,	"InstructionSet.txt","Tempos.txt");
		SIM_NEW.resetTime ( );
		SIM_NEW.Initializations ( lc);
		bFirstInstruction = true;
		bBeginProgram = true;
		Shell.decodifica ( "reset lc3");
	}
	
	public void resetIsNewInstruction ( ) {
		bIsNewInstruction = false;
	}

	public boolean isNewInstruction ( ) {
		return ( bIsNewInstruction);
	}

	protected String archRegs [ ] = {"R0", "R1", "R2", "R3", "R4", "R5", "R6", "R7"};
	
	protected Instruction iFetch;
	private boolean bIsNewInstruction = true;
	private boolean bFirstInstruction = true;
	private boolean bBeginProgram = true;
	private boolean bEndProgram = false;
	
	private int iNStepsForNextFetch = 100;
	
	private String mnemonicos [ ] = { "ADD", "AND", "BR", "JMP", "JSR", "JSRR", "LD", "LDI", "LDR", "LEA", "NOT", "RET", "RTI", "ST", "STI", "STR", "TRAP", "FETCH"};
	private long intOpcodes [ ] =   {   1,     5,     0,    12,    4,      4,     2,    10,    6,     14,    9,     12,    8,     3,    11,    7,     15,      13  };
	private int sizeBytes [ ] =     {   1,     1,     1,     1,    1,      1,     1,     1,    1,      1,    1,      1,    1,     1,     1,    1,      1,       1  };
}
