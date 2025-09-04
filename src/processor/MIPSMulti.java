package processor;

import java.lang.reflect.Array;
import java.util.Hashtable;

import montador.ASMrISAforMIPS;
import montador.Montador;
import ports.Field;
import ports.FieldString;
import ports.SetPort;
import ports.Status;
import shell.Shell;
import simdraw.MIPSApp;
import simulator.Clock;
import simulator.Simulator;
import control.ExecutionPath;
import control.Instruction;

/*******************************************************************************
 *	A SER CUSTOMIZADA PELO PROJETISTA.
 *	Implementa a classe principal do ambiente: a classe PROCESSOR.
 *	Insere atributos de STATUS (estado), FIELDS (campos - de instrucao) e
 *		STRINGS ( descricoes textuais).
 *
 *******************************************************************************/

public class MIPSMulti extends MIPS {

	public MIPSMulti ( Simulator parSim) {
		super ( ".\\processors\\mipsmulti", parSim);
		
//		 Customizar aqui - BEGIN
		String [ ] asStatus = { "TDCF_UsingRISA", "TDCF_WhichRISA", "BRANCH"};
		String [ ] asFieldsProc = { "InRISAmode"};
		String [ ] asStrings = { "NAME"};
		// Campos das instrucoes do processador. OPR eh para o comparador
		String [ ] asFieldsInstr = { "OPCODE", "TYPE","NR1", "NR2", "NW1", "OP", "IMM", "ADDRESS", "FIELD", "FUNC", "PC","OPR", "ACCESS_TYPE","RFWrCtrl"};
		String [ ] asFieldsStrInstructions = { "mnem", "DESCRIPTION"};
// - END
		
		spStt = new SetPort ( );
		spFld = new SetPort ( );
		spFldStr = new SetPort ( );
		
		for ( int i = 0; i < Array.getLength ( asStatus); i ++) {
			Status sttAux = new Status ( asStatus [ i]);
			spStt.add ( sttAux);
		}
		
		for ( int i = 0; i < Array.getLength ( asFieldsProc); i ++) {
			Field fldAux = new Field ( asFieldsProc [ i]);
			spFld.add ( fldAux);
		}

		for ( int i = 0; i < Array.getLength ( asStrings); i ++) {
			FieldString fldSAux = new FieldString ( asStrings [ i]);
			spFldStr.add ( fldSAux);
		}

		Instruction.defineFieldsOfInstructions ( asFieldsInstr, asFieldsStrInstructions);
		//Instruction.defineFieldsOfInstructions ( asFieldsInstr);
		setInstructionSet ( mnemonicos, intOpcodes, sizeBytes);

// Customizar aqui - BEGIN
		set ( "BRANCH",	STATUSorCONF, 0);
		set ( "NAME", 	STRING, "DLX");
		set ( "TDCF_TestHazards",	STATUSorCONF, 1);
		set ( "TDCF_TestBranches",	STATUSorCONF, 1);
		set ( "TDCF_UsingRISA",	STATUSorCONF, 0);
		set ( "TDCF_WhichRISA",	STATUSorCONF, 0);
		set ( "InRISAmode",	FIELD, 0);
// - END
		setArchitecturalRegistersNames(archRegs);
		rISA = new ASMrISAforMIPS ( this);
		
		htAddresses = new Hashtable ( );
	}
	
	private void countInstructionReferences ( ) {
		//
		Integer ICont;
		long lAddr = dtp.execute ( "PC", GET, "S1", OUT);
		if ( lAddr > lBiggerPCAddress) lBiggerPCAddress = lAddr;
		if ( ( ICont = (Integer) htAddresses.get ( new Long ( lAddr))) == null) {
			htAddresses.put ( new Long ( lAddr), new Integer ( 1));
		} else {
			int iCont = ICont.intValue();
			iCont ++;
			htAddresses.put ( new Long ( lAddr), new Integer ( iCont));
		}
//System.out.println(htAddresses);
		//		
	}
	
/*******************************************************************************
 *	A SER FORNECIDA PELO PROJETISTA.
 *	Retorna uma instrucao conforme ela foi lida.
 *	Neste caso, estah retornando um valor inteiro lido do componente IMEMORY,
 *		que eh uma memoria de instrucoes.
 *******************************************************************************/
	private long fetch ( ) {
		if ( dtp.execute ( "imemory", GET, "FETCH", STATUSorCONF) == 1L) {
//
countInstructionReferences ( );
//
			long lOp = dtp.execute ( "imemory", GET, "S1", OUT);
			dtp.execute ( "imemory", SET, "FETCH", STATUSorCONF, 0);
			
			return ( lOp);
		}
		
		return ( 0L);
	}

	/*******************************************************************************
	*	A SER FORNECIDA PELO PROJETISTA.
	*	Deve prever a inicializacao do processador
	*******************************************************************************/
	private void initialize ( ) {
		ExecutionPath pPipeCurrent = sePipes.search ( null);
		iNop = decode ( 0L);	// nop
		iNop.set( "PC", FIELD, FETCH);
		pPipeCurrent.walk ( iNop, "FETCH");
		Clock.setInitialTime ( -100.0F);
		bBeginProgram = false;
//System.out.println ( iStepsForNewInstruction);
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

		if ( bEndProgram == false) {
			if ( pPipeCurrent.isActive ( "FETCH") == true) {
				long iOpcode = fetch ( );
				//if ( iOpcode == 0L) iNew = null;
				//else iNew = decode ( iOpcode);
				if ( isNOP ( (int) iOpcode)) iNew = null;
				else if ( isHALT ( (int) iOpcode)) iNew = null;
				else iNew = decode ( iOpcode);
			} else {
// a variavel iNew mantem a ultima instrucao buscada.
			}
		}

		switch ( iTimeMode) {
			case MONOCYCLE:
				break;
			case MULTICYCLE:
//System.out.println ( iStepsForNewInstruction);
				if ( -- iStepsForNewInstruction == 0) {
					if ( bFirstInstruction == true) {
//System.out.println("--> Inserindo PRIMEIRA instrucao no pipeline!!!");
						Clock.setInitialTime ( -1.0F);
						bFirstInstruction = false;
					}
					if ( iNew != null) {
//System.out.println("--> Inserindo instrucao no pipeline!!!");
						pPipeCurrent.walk ( iNew, "FETCH");
						iStepsForNewInstruction = pPipeCurrent.getNsteps ( );
//System.out.println ( iStepsForNewInstruction);
					} else {
						pPipeCurrent.walk ( null, "FETCH");
						bEndProgram = true;
					}
				} else pPipeCurrent.walk ( null, "FETCH");
				sSim.advanceTime ( );
				break;
			case PIPELINED:
				break;
		}

		return ( bEndProgram);
	}

	public void setReset ( String parProgram) throws Exception {
		String sProgram = null;
		Simulator SIM_NEW = new Simulator ( );
		MIPSMulti 	dlx 	= new MIPSMulti 	( SIM_NEW);
		Shell.initialize ( "# DLXMulti> ",		SIM_NEW,( Processor) dlx);
		if ( parProgram == null) sProgram = "Program.bin";
		else sProgram = parProgram;
		System.out.println ( "file name: "+sProgram);
		dlx.initialize ( 	"DLXOrg.txt",sProgram,	"InstructionSet.txt","PipelineStages.txt");
		SIM_NEW.resetTime ( );
		SIM_NEW.Initializations ( dlx);
		bFirstInstruction = true;
		bBeginProgram = true;
		bEndProgram = false;
		Shell.decodifica ( "reset dlxmulti");
	}
	
	public void listReferencesByAddress ( ) {
		int i;
		String sTmp;
		int nRefs = 0;
		
		MIPSApp.messagesToUser ( null, "---------- ---------- ---------- ---------- ----------"+lBiggerPCAddress);
		for ( i = 0; i < lBiggerPCAddress; i ++) {
			Integer NRefs = (Integer) htAddresses.get( new Long ( i));
			if ( NRefs != null) nRefs = NRefs.intValue ( );
			else nRefs = 0;
			if ( nRefs > 0) {
				sTmp = i+" = "+nRefs+"\t"+Montador.getAssemblyCode ( new Long(i).intValue());
				MIPSApp.messagesToUser ( null, i+" = "+nRefs+"\t"+Montador.getAssemblyCode ( new Long(i).intValue())+"\t"+Montador.getMethodName ( new Long(i).intValue()));
			}
		}
		
		listReferencesByMnemonico ( );
	}
	
	public void listReferencesByMnemonico ( ) {
		int i;
		String sTmp;
		int nRefs = 0;
		
		MIPSApp.messagesToUser ( null, "---------- ---------- ---------- ---------- ----------"+lBiggerPCAddress);
		for ( i = 0; i < lBiggerPCAddress; i ++) {
			Integer NRefs = (Integer) htAddresses.get( new Long ( i));
			if ( NRefs != null) nRefs = NRefs.intValue ( );
			else nRefs = 0;
			if ( nRefs > 0) {
				Montador.settReferencesByMnemonico ( i, (int) nRefs);
			}
		}
		
		for ( i = 0; i < mnemonicos.length; i ++)
			MIPSApp.messagesToUser ( null, Montador.getReferencesByMnemonico() );
	}

	public void listReferencesByAddressOfMethods ( ) {
		int i;
		String sTmpMnem, sTmpMethod;
		int nRefs = 0;
		int iMethod = 0;
		int cont = 100;
		
		MIPSApp.messagesToUser ( null, "---------- ---------- ---------- METHODS ---------- ---------- ----------");

		while ( cont > 0) {
			cont = 0;
			iMethod ++;

			MIPSApp.messagesToUser ( null, "---------- ---------- ---------- Method number "+iMethod+" ---------- ---------- ----------");

			for ( i = 0; i < lBiggerPCAddress; i ++) {
				Integer NRefs = (Integer) htAddresses.get( new Long ( i));
				if ( NRefs != null) nRefs = NRefs.intValue ( );
				else nRefs = 0;
				if ( nRefs > 0) {
					sTmpMnem = Montador.getAssemblyCode ( new Long(i).intValue(), iMethod);
					if ( sTmpMnem != null) {
						MIPSApp.messagesToUser ( null, i+" = "+nRefs+"\t"+sTmpMnem+"\t"+Montador.getMethodName ( new Long(i).intValue()));
						cont ++;
					}
				}
			}

		}
		
		//listReferencesByMnemonico ( );
	}
	
	Hashtable htAddresses;
	private long lBiggerPCAddress = 0;
}
