package processor;

import java.lang.reflect.Array;

import message.MicroOperation;
import ports.Field;
import ports.FieldString;
import ports.SetPort;
import ports.Status;
import simulator.Simulator;
import control.Instruction;
import datapath.Datapath;

/*******************************************************************************
 *	A SER CUSTOMIZADA PELO PROJETISTA.
 *	Implementa a classe principal do ambiente: a classe PROCESSOR.
 *	Insere atributos de STATUS (estado), FIELDS (campos - de instrucao) e
 *		STRINGS ( descricoes textuais).
 *
 *******************************************************************************/
public class Interactive extends Processor {

	public Interactive ( Simulator parSim) {
		super ( "Interactive", parSim);
		dtp = new Datapath ( );
		sSim = parSim;

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
		set ( "NAME", 	STRING, "Interactive");
// - END
	}

/*******************************************************************************
 *	A SER FORNECIDA PELO PROJETISTA.
 *	Retorna uma instrucao conforme ela foi lida.
 *	Neste caso, estah retornando um valor inteiro lido do componente IMEMORY,
 *		que eh uma memoria de instrucoes.
 *******************************************************************************/
	private long fetch ( ) {
		
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

		return ( null);
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

				break;			
		}
	}


/*******************************************************************************
 *	A SER FORNECIDA PELO PROJETISTA.
 *	Deve percorrer os estagios de pipeline executando as instrucoes.
 *	Ha metodos pre-definidos que podem ser chamados.
 *	Deve prever a inicializacao do processador.
 *******************************************************************************/
	public void execute ( ) {
	
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

		return ( false);
	}

	protected Instruction iFetch;

	private boolean bFirstInstruction = true;
	private boolean bBeginProgram = true;
	
	private int iNStepsForNextFetch = 100;
}
