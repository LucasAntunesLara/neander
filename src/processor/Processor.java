package processor;

import message.MicroOperation;
import message.Msg;
import message.MsgInterpreter;
import message.SetMsg;
import montador.ASMrISAforMIPS;
import platform.Lang;
import platform.Platform;
import ports.State;
import primitive.Primitive;
import primitive.SetPrimitive;
import breakpoints.Breakpoint;
import breakpoints.SetBreakpoint;
import shell.Shell;
import simulator.Simulator;
import util.Define;
import control.ExecutionPath;
import control.ExecutionStage;
import control.Instruction;
import control.InstructionSet;
import control.InstructionType;
import control.QueuesOfInstructions;
import control.Superescalar;
import datapath.Datapath;

/*******************************************************************************
 *	A SER CUSTOMIZADA PELO PROJETISTA.
 *	Implementa a classe principal do ambiente: a classe PROCESSOR.
 *	Insere atributos de STATUS (estado), FIELDS (campos - de instrucao) e
 *		STRINGS ( descricoes textuais).
 *
 *******************************************************************************/
public class Processor extends State implements Define, MsgInterpreter {

	public Processor ( String parName, Simulator parSim) {
		sProcessorName = new String ( parName);
		dtp = new Datapath ( );
		sSim = parSim;
		rodadaRisa = new RodadaDeSimulacaoRisa();
		setRodadaRisa = new SetRodadaDeSimulacaoRisa();

		setInstructionSet ( mnemonicos, intOpcodes, sizeBytes);
/*
// Customizar aqui - BEGIN
		String [ ] asStatus = { };
		String [ ] asFields = { };
		String [ ] asStrings = { };
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
//		set ( "NAME", 	STRING, "DLX");
// - END
*/
	}

	public static String getProcessorName ( ) {
		return ( sProcessorName);
	}

/*
 *	EH UM metodo INTERNO.
 *	Inicializa o processador atraves da leitura dos arquivos gerados
 *		pela ferramenta de modelagem.
 *	Sao eles:
 *		Arquivo com a organizacao do processador - componentes e conexoes.
 *		Arquivo com inicializacoes. Por exemplo: da memoria de instrucoes.
 *		Arquivo com a descricao dos tipos de instrucao disponiveis.
 *		Arquivo com a descricao do pipeline do processador.
 */
	public void initialize ( 	String parFileName1, String parFileName2,
								String parFileName3, String parFileName4) {
		String [] sPathName = new String [4];
		
		//System.out.println ( "O diretorio e: "+System.getProperty ( "user.dir"));
		
		sPathName [0] = Platform.treatPathNames( sProcessorName+"\\"+parFileName1);
		//System.out.println ( "*** A organizacao do processador estah em: "+sPathName[0]);
		sPathName [1] = Platform.treatPathNames( sProcessorName+"\\"+parFileName2);
		//System.out.println ( "*** As inicializacoes estao em:            "+sPathName[1]);
		sPathName [2] = Platform.treatPathNames( sProcessorName+"\\"+parFileName3);
		//System.out.println ( "*** A descricao dos tipos de instrucao em: "+sPathName[2]);
		sPathName [3] = Platform.treatPathNames( sProcessorName+"\\"+parFileName4);
		//System.out.println ( "*** A descricao do pipeline estah em     : "+sPathName[3]);

		try {
			Shell.batch ( sPathName[0]);
		} catch ( Exception e) {
			//System.out.println ( "Erro no metodo initialize de Processor: Organization!");			
		}

		try {
			Shell.batch ( sPathName[1]);
		} catch ( Exception e) {
			//System.out.println ( "Erro no metodo initialize de Processor: Initial State!");			
		}

		try {
			isISet = new InstructionSet ( sPathName[2]);
//			isISet.debug ( );
		} catch ( Exception e) {
			//System.out.println ( "Erro no metodo initialize de Processor: InstructionSet!");
		}
/*		
		try {
			pPipe = new Pipeline ( sProcessorName+Platform.getSeparatorPath()+parFileName4);
			iStepsForNewInstruction = pPipe.getNsteps ( );
			iTimeMode = pPipe.getTimeMode ( );
//			pPipe.debug ( );
		} catch ( Exception e) {
			System.out.println ( "Erro no metodo initialize de Processor: Pipeline!");
		}
*/
		try {
			ExecutionPath pPipeCurrent;
			
			sePipes = new Superescalar ( sPathName[3]);
			//sePipes.debug ( )
			pPipeCurrent = (ExecutionPath) sePipes.search ( null);
			iStepsForNewInstruction = pPipeCurrent.getNsteps ( );
			iTimeMode = pPipeCurrent.getTimeMode ( );
		} catch ( Exception e) {
			//System.out.println ( "Erro no metodo initialize de Processor: Superescalar!");
		}
	}

/*
 *	EH UM metodo INTERNO.
 *	Retorna o objeto DATAPATH do objeto PROCESSOR.
 */
	public Datapath getDatapath ( ) {
		return ( dtp);	
	}

/*
 *	EH UM metodo INTERNO.
 *	Retorna o objeto SUPERESCALAR do objeto PROCESSOR.
 */
public Superescalar getSuperescalar ( ) {
	return ( sePipes);	
}

/*
 *	EH UM metodo INTERNO.
 *	Retorna o objeto QUEUESOFINSTRUCTIONS do objeto PROCESSOR.
 */
public QueuesOfInstructions getQueuesOfInstructions ( ) {
	return ( processorQueues);	
}
/*
public int getNelemsAll ( ) {
	return ( spStt.getNelems()+spFldStr.getNelems ( ));		
}

public Object [] [] getState ( )
{
	int iNelems = spStt.getNelems()+spFldStr.getNelems ( );
	int i, cont = 0;
	long lValue;
	String sValue;
	Port prAux;
	Object [] [] procFields = new Object [ iNelems] [ 2];
	
	for ( i = 0; i < spFldStr.getNelems ( ); i ++) {
		Property pAux = ( Property) spFldStr.traverse ( i);
		procFields [ cont] [ 0] = pAux.getName ( );
		procFields [ cont++] [ 1] = pAux.getString ( );	
	}
	
	for ( i = 0; i < spStt.getNelems(); i ++) {
		prAux = ( Port) spStt.traverse ( i);
		procFields [ cont] [ 0] = prAux.getName ( );
		lValue = prAux.getDoubleWord ( );
		//instrFields [ cont++] [ 1] = new Long ( lValue);
		String tmpx = SistNum.printInformation ( lValue);
		procFields [ cont++] [ 1] = tmpx;	
	}

	return procFields;
}
*/
/*******************************************************************************
 *	A SER FORNECIDA PELO PROJETISTA.
 *	Retorna uma instrucao conforme ela foi lida.
 *	Neste caso, estah retornando um valor inteiro lido do componente IMEMORY,
 *		que eh uma memoria de instrucoes.
 *******************************************************************************/
	private long fetch ( ) {
		
		return ( 0L);
	}

/*
 *	EH UM metodo INTERNO.
 *	Atrubui um roteiro de execucao aa instancia de instrucao PARIT 
 *		que estah sendo criada.
 *	O roteiro eh buscado num objeto INSTRUCTIONTYPE.
 *	O objeto INSTRUCTIONTYPE eh selecionado atraves do campo DESCRIPTION de PARIT.
 *	DESCRIPTION eh setada pelo projetista no metodo DECODE.
 *	Este identificador de INSTRUCTIONTYPE vem do nome do arquivo que o descreve.
 */
	protected void decodeInternal ( Instruction parIt)
	{
		String sDescription = parIt.getString ( "DESCRIPTION", STRING);
//System.out.println ( "->"+sDescription);		
		InstructionType itAux = ( InstructionType) isISet.search ( sDescription);
		if ( itAux != null) {
			SetMsg smAux = itAux.smMops;
			for ( int j = 0; j < smAux.getNelems ( ); j ++) {
				MicroOperation mdAux = ( MicroOperation) smAux.traverse ( j);
				if ( mdAux != null) {
					MicroOperation mdNew;
					mdNew = new MicroOperation ( 	mdAux.sComponente, mdAux.iMethod,
												mdAux.sName, mdAux.lV1, mdAux.lV2,
												mdAux.lV3, mdAux.fTime);
					parIt.smMops.add ( ( MicroOperation) mdNew);
				}
			}
		}
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
		Instruction itctNew = new Instruction ( );

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

/*
 *	EH UM metodo INTERNO.
 *	Recebe uma mensagem PARMESG de um objeto instrucao e a envia para execucao
 *		pelas estruturas do DATAPATH.
 */	
	public void execute ( Msg parMesg) {
		MicroOperation mdAux;
		
		mdAux = ( MicroOperation) parMesg;
		
		if ( mdAux.bActive == false) return;

		switch ( mdAux.iMethod) {
			case BEHAVIOR:
				dtp.execute ( mdAux.sComponente,	BEHAVIOR);
				break;
			case READ:
				dtp.execute ( mdAux.sComponente,	READ);
				break;
			case WRITE:
				dtp.execute ( mdAux.sComponente,	WRITE);
				break;
			case PROPAGATE:
				dtp.execute ( mdAux.sComponente,	PROPAGATE);
				break;
			case SET:
				if ( mdAux.sName == null) {
					dtp.execute ( mdAux.sComponente, 	SET, mdAux.lV1,mdAux.lV2,mdAux.lV3);
				} else {
					dtp.execute ( mdAux.sComponente, 	SET, mdAux.sName,mdAux.lV1,mdAux.lV2);
				}
				break;			
		}
	}

/*	
 *	EH UM metodo INTERNO.
 *	Percorre, de tras para frente, os estagios de pipeline.
 *	Os passos de cada instrucao (representados por mensagens MSGDATAPATH) referentes
 *		ao estagio onde se encontra sao decodificados e enviados para execucao.
 */
	protected void executeBackward ( String whichPipe) {
		ExecutionPath pPipeCurrent = sePipes.search ( whichPipe);
		ExecutionStage psAux;
		Instruction itctAux;

		for ( int i = pPipeCurrent.getExecutionStages().getNelems ( ) - 1; i >= 0; i --) {
			psAux = ( ExecutionStage) pPipeCurrent.spStages.traverse ( i);
			if ( psAux.isActive ( ) == false) continue;
			itctAux = psAux.getCurrentInst ( );
			if ( itctAux != null) {
				if ( itctAux.isActive ( ) == false) continue;
				SetMsg smAux = itctAux.smMops;
				for ( int j = 0; j < smAux.getNelems ( ); j ++) {
					MicroOperation mdAux = (MicroOperation) smAux.traverse ( j);
					if ( pPipeCurrent.getTargetStage ( ( int) mdAux.fTime) == psAux.getExecutionStageId ( )) {
						decodeAfter ( mdAux, itctAux);
						execute ( mdAux);
					}
				}
			}
		}
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
 *	Deve prever a inicializacao do processador.
 *******************************************************************************/
	private void execute ( ) {
		executeBackward ( null);   
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
		long iOpcode = fetch ( );
		if ( iOpcode != 0L) {
			iNew = decode ( iOpcode);
		} else iNew = null;

		return ( false);
	}
	
	public Breakpoint pBreakpoint;
	public SetBreakpoint sBreakpoint;
	public MicroOperation mp;
	public boolean bMustStop;
	
	public boolean mustStop ( ) {		
		return ( false);
	}

	public void resetCircuitAndBusAndContents ( ) {
		dtp.resetUsedCircuit ( );
		dtp.resetUsedBus ( );
		//dtp.resetContents();
	}

/*
 *	EH UM metodo INTERNO.
 *	Depurador do objeto processor: apresenta o pipeline e instrucoes.
 */
	public void debug ( ) {
		String sName = getString ( "NAME", 	STRING);
		System.out.println ( "O processador simulado eh o: "+ sName);
		super.debug ( );
		sePipes.debug ( );
		processorQueues.list ( );
	}

/*
 *	EH UM metodo INTERNO.
 *	Visualizador do objeto processor: apresenta o pipeline e instrucoes.
 */
	public void listPipes ( ) {
		String sName = getString ( "NAME", 	STRING);
		System.out.println ( "\n** INICIO **\n");
		System.out.println ( "O processador simulado eh o: "+ sName);
		super.debug ( );
		sePipes.debug ( );
		System.out.println ( "\n** FIM **");
		System.out.println ( );
	}

	/*
	 *	EH UM metodo INTERNO.
	 *	Visualizador do objeto processor: apresenta as filas de instrucoes.
	 */
		public void listQueues ( ) {
			String sName = getString ( "NAME", 	STRING);
			System.out.println ( "\n** INICIO **\n");
			System.out.println ( "O processador simulado eh o: "+ sName);
			super.debug ( );
			if ( processorQueues != null) processorQueues.debug ( );
			System.out.println ( "\n** FIM **");
			System.out.println ( );
		}
/*
 *	EH UM metodo INTERNO.
 *	Visualizador do objeto processor: apresenta as variaveis de estado.
 */
	public void listState ( ) {
		String sName = getString ( "NAME", 	STRING);
		System.out.println ( "\n** INICIO **\n");
		System.out.println ( "O processador simulado eh o: "+ sName);
		super.debug ( );
		System.out.println ( "\n** FIM **");
		System.out.println ( );
	}

	/*
	 *	EH UM metodo INTERNO.
	 *	Visualizador do objeto processor: apresenta as variaveis de estado.
	 */
		public void list ( ) {
			this.listState ( );
		}
		
	public void setInstructionSet ( String mnem [ ], long op [], int size []) {
		mnemonicos = mnem;
		intOpcodes = op;
		sizeBytes = size;
	}
	
	public String getMnemonico ( long iOpcode) {
		int i;
		String s = "-";

//System.out.println ( "iOpcode ="+iOpcode);
		for ( i = 0; i < intOpcodes.length; i ++) {
			if ( iOpcode == intOpcodes [ i]) break;
		}
		
		if ( i == intOpcodes.length) return s;
		else return ( mnemonicos [ i]);
	}

	public int getSizeInBytes ( long iOpcode) {
		int i;
		
		for ( i = 0; i < intOpcodes.length; i ++) {
			if ( iOpcode == intOpcodes [ i]) break;
		}
		
		return ( sizeBytes [ i]);
	}
	
	public String [] getMnemonicosList ( ) {
		return mnemonicos;
	}

	public long[] getOpcodesList ( ) {
		return intOpcodes;
	}

	public int [] getSizeInBytesList ( ) {
		return sizeBytes;
	}
	
	public void setArchitecturalRegistersNames ( String [] parArchRegs) {
		archRegs = parArchRegs;
	}

	public String [] getArchitecturalRegistersNames ( ) {
		return archRegs;
	}

	public static void setMessageError ( String parError) {
		sMessageError = parError;
	}

	public static String getMessageError ( ) {
		if ( sMessageError != null) {
			return ( sMessageError);
		} else {
			return ( Lang.iLang==ENGLISH?Lang.msgsGUI[180]:Lang.msgsGUI[190]);
		}
	}

	// rISA begin
	public boolean usingRISA() {
		return false;
	}

	public int isrISAble ( long lOpcode) {
		return ( - 1);
	}
	
	public void createRegZipNumberForItypeInstr ( int iSize) {

	}

	public boolean setRegZipNumberForItypeInstr ( int iNumber) {
			
		return ( false);
	}

	public int getRegZipNumberForItypeInstr ( int iNumber) {
		return ( -1);
	}
	
	public void listRegZipNumberForItypeInstr ( ) {

	}
	
	public void simulateBatch ( String sPath, String sPrg) {

	}

	// rISA end

	//private String mnemonicos [ ];
	//private long intOpcodes [ ];
	//private int sizeBytes [ ];

	private static String sProcessorName;
	protected Simulator sSim;
	protected Datapath dtp;
	protected InstructionSet isISet;
	protected Superescalar sePipes;
	protected QueuesOfInstructions processorQueues;
	// rISA begin
	public ASMrISAforMIPS rISA;
	public RodadaDeSimulacaoRisa rodadaRisa;
	public SetRodadaDeSimulacaoRisa setRodadaRisa;
	protected int rISAinstructions [] = {};
	// rISA end

	protected Instruction iNop, iBubble, iNew;

	protected int iStepsForNewInstruction, iTimeMode;
	
	private String mnemonicos [ ] = { "A ser preenchido"};
	private long intOpcodes [ ] = { - 1};
	private int sizeBytes [ ] = { - 1};
	
	private static String sMessageError;
	
	private String archRegs [ ] = null;
}

/*
 * TRECHO DE CODIGO PARA TESTAR DIRETIVAS RELACIONADAS A FUNCTIONALITY LIST
 * deve ser colocada logo apos o primeiro if de behavior
 *
		iNew.addFunctionalityAtFirst ( "registers", "GPU", BEHAVIOR, 0);
		iNew.addFunctionalityAtFirst ( "GPU", "GPU", "E1", IN, 111, 0);
		iNew.addFunctionalityAtFirst ( "GPU", "GPU", "S1", OUT, 222, 0);
		iNew.addFunctionalityAtFirst ( "GPU", "GPU", "CT1", CONTROL, 333, 0);
		iNew.deactivateComponent ( "GPU");
		iNew.substituteComponent ( "GPU", "FloatPU");
		iNew.substitutePortValue ( "FloatPU", "E1", 10000);
		iNew.substitutePortValue ( "FloatPU", "S1", 20000);
		iNew.substitutePortValue ( "FloatPU", "CT1", 30000);
		iNew.removeComponent ( "FloatPU");

		iNew.debug ( );
*/
