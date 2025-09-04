package processor;

import java.lang.reflect.Array;

import ports.Field;
import ports.FieldString;
import ports.SetPort;
import ports.Status;
import simulator.Clock;
import simulator.Simulator;
import control.Instruction;
import control.ExecutionPath;
import datapath.Datapath;

/*******************************************************************************
 *	A SER CUSTOMIZADA PELO PROJETISTA.
 *	Implementa a classe principal do ambiente: a classe PROCESSOR.
 *	Insere atributos de STATUS (estado), FIELDS (campos - de instrucao) e
 *		STRINGS ( descricoes textuais).
 *	Os atributos relacionam-se ao objeto processor. 
 *	FIELDS, por sua vez, e usado para criar os campos de cada objeto instruction
 *******************************************************************************/
public class Example extends Processor {

	public Example ( Simulator parSim) {
		// deve indicar aqui a pasta onde estao os arquivos de descricao
		super ( ".\\processors\\example", parSim);
		dtp = new Datapath ( );
		sSim = parSim;

// Customizar aqui - BEGIN
		String [ ] asStatus = { };
		// devem ser listados aqui os campos da instrucao cujo valor sera
		//    fornecido na decodificacao da instrucao
		// OP e um campo que define a operacao a ser executada pela ALU do
		//    processador Example
		String [ ] asFields = { "OP"};
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
		set ( "NAME", 	STRING, "Example");
// - END
	}

/*******************************************************************************
 *	A SER FORNECIDA PELO PROJETISTA.
 *	Retorna uma instrucao conforme ela foi lida.
 *******************************************************************************/
	private long fetch ( ) {
		// no caso do processador Example, ha, a cada ciclo do relogio, uma
		//    nova instrucao na saida de dados da memoria
		long lOp = dtp.execute ( "MEM", GET, "S1", OUT);

		return ( lOp);
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

		// Testa aqui o codigo devolvido pelo metodo fetch
		// Ha apenas dois tipos de instrucao no processador Example
		// A instrucao Fetch, codigo 0, e usada apenas para buscar o primeiro
		//    codigo da memoria. Depois nao e mais usada
		// A instrucao Unique implementa todo o trabalho do processador Example
		if ( parInst == 0L)
			// Seta o atributo DESCRIPTION da instrucao criada
			// Este atributo define o arquivo que descreve as microoperacoes a
			//    serem executadas: Fetch.txt
			itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\INSTRUCTIONS\\FETCH");		
		else
			// Ou Unique.txt
			itctNew.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\INSTRUCTIONS\\UNIQUE");

		// Seta o campo de instrucao OP com o codigo lido da memoria
		// OP define a operacao a ser executada pela ALU
		itctNew.set ( "OP",  FIELD, new Long ( parInst).intValue ( ));

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
	iFetch = decode ( 0L);
// Insere a instrucao no unico estagio de pipeline
	pPipeCurrent.walk ( iFetch, "UNIQUE");
// Seta o tempo de relogio para -1. A instrucao lida pelo fetch sera
//    a primeira a ser executada no tempo 0
	Clock.setInitialTime ( -1.0F);
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
		if ( bBeginProgram == true) initialize ( );
		
		execute ( );	
		// Busca uma instrucao
		long lOpcode = fetch ( );
		// Se codigo igual a 255, no Example, encerra o programa
		if ( lOpcode == 255L) {
			// ESTE CODIGO CONTINUARA A SER EXECUTADO INDEFINIDAMENTE PELO SIMULADOR
			if ( bEndProgram == false) {
				// AVANCO DE TEMPO RELACIONADO AA ULTIMA INSTRUCAO EXECUTADA
				sSim.advanceTime ( );
				bEndProgram = true;
				// Insere um null no unico estagio de pipeline para que nada mais
				//    seja executado
				pPipeCurrent.walk ( null, "UNIQUE");
			}
		} else {
			// Decodifica a instrucao
			iNew = decode ( lOpcode);
			// Insere a instrucao no unico estagio de pipeline
			pPipeCurrent.walk ( iNew, "UNIQUE");
			// Avanca o tempo
			sSim.advanceTime ( );
		}

		return ( bEndProgram);
	}

	protected Instruction iFetch;

	private boolean bBeginProgram = true;
	private boolean bEndProgram = false;
	
	private String mnemonicos [ ] = { "Fetch", "add", "sub", "and", "or", "hlt"};
	private long intOpcodes [ ] = { 0, 10, 11, 12, 13, 255};
	private int sizeBytes [ ] = { 1, 1, 1, 1, 1};
}
