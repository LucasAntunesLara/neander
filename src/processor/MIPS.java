package processor;
import java.io.File;
import java.lang.reflect.Array;

import breakpoints.Breakpoint;

import message.MicroOperation;
import montador.ASMrISAforMIPS;
import montador.Montador;
import montador.MontadorMIPS;
import platform.Platform;
import ports.Field;
import ports.FieldString;
import ports.SetPort;
import ports.Status;
import shell.Shell;
import simdraw.MIPSApp;
import simulator.Clock;
import simulator.Simulator;
import util.SistNum;
import control.ExecutionPath;
import control.Instruction;
import datapath.Datapath;

/*******************************************************************************
 *	A SER CUSTOMIZADA PELO PROJETISTA.
 *	Implementa a classe principal do ambiente: a classe PROCESSOR.
 *	Insere atributos de STATUS (estado), FIELDS (campos - de instrucao) e
 *		STRINGS ( descricoes textuais).
 *
 *******************************************************************************/
public class MIPS extends Processor {

	public MIPS ( Simulator parSim) {
		super ( ".\\processors\\mips", parSim);
// Customizar aqui - BEGIN
		String [ ] asStatus = { "TDCF_TestHazards", "TDCF_TestBranches", "TDCF_UsingRISA", "TDCF_WhichRISA", "TDCF_8or16", "BRANCH"};
		String [ ] asFieldsProc = { "ExecutionCycles", "NroInstFetched", "InRISAmode", "NroRedInstInBlock"};
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
		set ( "NAME", 	STRING, "DLX");
		set ( "TDCF_TestHazards",	STATUSorCONF, 1);
		set ( "TDCF_TestBranches",	STATUSorCONF, 1);
		set ( "TDCF_UsingRISA",	STATUSorCONF, 0);
		set ( "TDCF_WhichRISA",	STATUSorCONF, 0);
		set ( "TDCF_8or16", STATUSorCONF, 0);
		set ( "ExecutionCycles",	FIELD, 0);
		set ( "NroInstFetched",	FIELD, 0);
		set ( "NroRedInstInBlock",	FIELD, 0);
		set ( "InRISAmode",	FIELD, 0);
		set ( "BRANCH",	STATUSorCONF, 0);
// - END
		setArchitecturalRegistersNames(archRegs);
		rISA = new ASMrISAforMIPS ( this);
		updateRisaInstructionMatrix();
	}

	public MIPS(String string, Simulator parSim) {
		super ( string, parSim);
	}
	
	public void updateRisaInstructionMatrix(){
		/*int [][] matrizteste1 = {
				{ 0}, { 1}, { 2}, { 3}, { 4}, { 5}, { 6}, { 7}, { 8}, { 9},
				{10}, {11}, {12}, {13}, {14}, {15}, {16}, {17}, {18}, {19},
				{20}, {21}, {22}, {23}, {24}, {25}, {26}, {27}, {28}, {29},
				{30}, {31}, {32}, {33}, {34}, {35}, {36}, {37}, {38}, {39},
				{40}, {41}, {42}, {43}, {44}, {45}, {46}, {47}, {48}, {49},
				{50}
		};
		rISAinstructionsMatrix = matrizteste1;*/
		if ((get("TDCF_8or16", STATUSorCONF)) == 0){
			// 8 opcodes
			int[][] rISAinstructionsMatrix8 = {
				{ 0, 9, 22, 23, 24, 40, 41, 44},	// generic        rISA 0
				{ 0, 22, 23, 24, 31, 34, 35, 44},	// stringsearch   rISA 1
				{ 0, 20, 22, 23, 24, 31, 35, 40},	// qsort          rISA 2
				{ 0, 16, 22, 23, 24, 31, 35, 40},	// bitcount       rISA 3
				//{ 0, 16, 22, 23, 24, 25, 35, 40},   // lgbitcount     rISA 3
				{ 0, 16, 18, 22, 23, 24, 35, 31},	// CRC32          rISA 4
				{ 0,20, 22, 24, 31, 40, 44},    // espaco para rISA configuravel
				//
				{ 0, 15, 22, 27, 35, 40},	// not used [6]
				{ 0,20,22,/*23,*/24,27,31,35,40 },			// strncmp2 [7]
				{ 0,21,22,23,25,31,35,45 },		// bit_shifter [8]
				{ 0,20,22,23,24,29,40,44},    // strsearch [9]
				{ 0,16,22,23,24,31,35 },		// bit_count [10]
				{0,16,22,23,24,25, 35,43},			// bitcount [11]
				{ 0, 16, 22, 23, 24, 25, 40, 43 },		// ntbl_bitcount [12]
				{ 0,16,18,22,23,24,35,40 },		// crc_32_method1 [13]
				{ 0, 18, 22, 23, 24, 35, 43},	// crc_32_method1 [14]
				{ 0, 19, 22, 23, 24, 31, 44},	// crc32file [15]
				{ 0, 15, 22, 23, 24, 35, 40, 44},	// Initsearch2 [16]
				{ 0, 22, 24, 35, 40, 41, 44},		// [17]
				{ 0, 22, 23, 24, 35, 40, 41},		// [18]
				{ 0, 16, 18, 22, 23, 24, 25},		// [19]
				{ 0, 18, 22, 23, 24, 35, 43, 44},		// [20]
				{ 0,22,23,24,25,35,45},					// [21]
				{ 0,21,22,23,24,27,31,34},				// [22]
				{ 0,22,23,24,25,27,40,45},				// [23]
				{ 0, 20, 22, 23, 24, 31, 35, 44},		// [24]
				{ 0, 20, 22, 23, 24, 27, 40, 44},		// [25]
			};
			rISAinstructionsMatrix = rISAinstructionsMatrix8;
		} else {
		// if ((get("TDCF_8or16", STATUSorCONF)) == 1){
			// 16 opcodes
			int rISAinstructionsMatrix16 [][] = {
				{ 0, 20, 22, 23, 24, 27, 29, 31, 34, 35, 40, 41, 44},               //               rISA 0
				{ 0, 20, 22, 23, 24, 27, 29, 31, 34, 35, 40, 41, 44}, 	            // stringsearch  rISA 1
				{ 0, 20, 22, 23, 24, 27, 28, 30, 31, 34, 35, 40, 41, 44},	        // qsort         rISA 2
				{ 0, 16, 22, 23, 24, 25, 27, 31, 34, 35, 40, 45, 43, 44},        	// bitcount      rISA 3
				{ 0, 16, 17, 18, 19, 22, 23, 24, 25, 31, 34, 35, 43, 44},       	// *** CRC32     rISA 4
				//{ 0, 20, 22, 23, 24, 27, 29, 31, 34, 35, 40, 41, 44}, 	        // stringsearch  rISA 1
				//{ 0, 20, 22, 23, 24, 27, 28, 30, 31, 35, 40, 41, 44},	            // lgqsort       rISA 2
				//{ 0, 16, 22, 23, 24, 25, 27, 31, 34, 35, 43, 40, 44, 45},         // bitcount      rISA 3
				//{ 0, 16, 21, 22, 23, 24, 25, 27, 31, 34, 35, 36, 40, 43, 45},     // lgbitcount    rISA 3
				//{ 0, 16, 17, 18, 19, 22, 23, 24, 25, 31, 34, 35, 43, 44},	        // *** CRC32     rISA 4
				//{ 0, 16, 18, 22, 23, 24, 25, 31, 35, 43, 44},                       // lgcrc32       rISA 4
				{ 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0},            // espaco para rISA configuravel
				//
				{ 0,20,22,23,24,27,31,35,40 },	// strncmp2 [106]
				{ 0,21,22,23,25,27,31,35, 45 },		// bit_shifter [107]


			};
			rISAinstructionsMatrix = rISAinstructionsMatrix16;
//System.out.println ( "setou matriz 16...");
		}
		// reconfigurable rISA
		int iWhichRisa = (int) get("TDCF_WhichRISA", STATUSorCONF);
		rISAinstructions = rISAinstructionsMatrix [ iWhichRisa];
		//
	}
	
	public void setInstructionSetIntoMatriz(int[] intructionSet){
		updateRisaInstructionMatrix();
		rISAinstructionsMatrix[5] = intructionSet;
	}
	
	// 	Usada pela simulateBatch
	public void DLXreset ( ) {
		sSim.resetTime();
		dtp = new Datapath ( );
		
		String [ ] asStatus = { "TDCF_TestHazards", "TDCF_TestBranches", "TDCF_UsingRISA", "TDCF_WhichRISA", "TDCF_8or16", "BRANCH"};
		String [ ] asFieldsProc = { "ExecutionCycles", "NroInstFetched", "InRISAmode", "NroRedInstInBlock"};
		String [ ] asStrings = { "NAME"};
		String [ ] asFieldsInstr = { "OPCODE", "TYPE","NR1", "NR2", "NW1", "OP", "IMM", "ADDRESS", "FIELD", "FUNC", "PC","OPR", "ACCESS_TYPE","RFWrCtrl"};
		String [ ] asFieldsStrInstructions = { "mnem", "DESCRIPTION"};
		
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
		setInstructionSet ( mnemonicos, intOpcodes, sizeBytes);

		set ( "NAME", 	STRING, "DLX");
		set ( "TDCF_TestHazards",	STATUSorCONF, 1);
		set ( "TDCF_TestBranches",	STATUSorCONF, 1);
		set ( "TDCF_UsingRISA",	STATUSorCONF, 0);
		set ( "TDCF_WhichRISA",	STATUSorCONF, 0);
		set ( "TDCF_8or16", STATUSorCONF, 0);
		set ( "ExecutionCycles",	FIELD, 0);
		set ( "NroInstFetched",	FIELD, 0);
		set ( "NroRedInstInBlock",	FIELD, 0);
		set ( "InRISAmode",	FIELD, 0);
		set ( "BRANCH",	STATUSorCONF, 0);

		setArchitecturalRegistersNames(archRegs);
		rISA = new ASMrISAforMIPS ( this);
		updateRisaInstructionMatrix();
	}
	
	private void debugRisaConfiguration ( ) {
		int i;
		
		for ( i = 0; i < rISAinstructions.length; i ++) System.out.print ( rISAinstructions [i]+",");
		System.out.println ( );
		/*
		for ( i = 0; i < this.rISA.RTypeBitFields.length; i ++) System.out.print ( this.rISA.RTypeBitFields [i]+",");
		System.out.println ( );
		for ( i = 0; i < this.rISA.ITypeBitFields.length; i ++) System.out.print ( this.rISA.ITypeBitFields [i]+",");
		System.out.println ( );
		for ( i = 0; i < this.rISA.JTypeBitFields.length; i ++) System.out.print ( this.rISA.JTypeBitFields [i]+",");
		System.out.println ( );	
		*/
	}

	private int translateRedToNormal ( long lOp, boolean first) {
		int iIndex, iType;
		int irOp = -1,  iOp = -1, iNr1 = -1, iNr2 = -1, iNw = -1, iField=-1, iFunc=-1;
		int iInstruction = 0;
		boolean bSpecialNops = this.rISA.bSpecialNops;
		int nopORcm = 0;
		//
		// this.rISA.updateIf8or16();
		//
		int RT [] = this.rISA.RTypeBitFields;
		int IT [] = this.rISA.ITypeBitFields;
		int JT [] = this.rISA.JTypeBitFields;
		int fB = 48;	// first bit of the reduced instruction saved in a variable of type long
		int nfB;		// to be used to indicate the beginning of the field
		//
		//	48-9-   50-1-2-3-4-5-6-7-8-9-   60-1-2-3
		//
		if ( bSpecialNops && ( lOp == 0 || lOp == 57344)) { // || lOp == -8192)) {
//System.out.println("lOp ="+lOp);
			iOp = iFunc = 0;			
			iType = RTYPE;
			nopORcm = (lOp == 0) ? 1 : 0;
		} else {
		//
//if ( lOp == 3206) System.out.println ( "3206L");
//if ( lOp == 2176) System.out.println ( "2176L");
//System.out.println ( "normal opcode: "+lOp);
			irOp = (int) SistNum.getBitRange ( lOp, fB, fB+RT[0]-1);
////System.out.println ( "reduced opcode: "+irOp);
			iIndex = rISAinstructions [ irOp];
//System.out.println ( "index: "+iIndex);
			iOp = (int) intOpcodes [iIndex];
//System.out.println ( "normal opcode: "+iOp);
//System.out.println ( "mnemonico: "+mnemonicos [ iIndex]);
			iFunc = sizeBytes[iIndex];
			iType = instrType [iIndex];
//System.out.println("lOp ="+lOp);
//if ( iOp == 5) System.out.println ( "reduced bne: "+first);
//debugRisaConfiguration ( );
		}
		
		switch ( iType) {
			case ITYPE:
				nfB = fB+IT[0];
				iNr1 = (int) SistNum.getBitRange ( lOp, nfB, nfB+IT[1]-1); // 51, 52);
//System.out.println ( "na decodificaao, enviei: "+iNr1);
				iNr1 = regsForItypeInstr [ iNr1];
//System.out.println ( "na decodificaao, recebi: "+iNr1);
				nfB = nfB+IT[1];
				iNw = (int) SistNum.getBitRange ( lOp, nfB, nfB+IT[2]-1) + 16;	// 53, 56);
				nfB = nfB+IT[2];
				iField = (int) SistNum.getBitRange ( lOp, nfB, nfB+IT[3]-1); // 57, 63);
				//if ( iOp == 8 || iOp == 0x0a || iOp == 5) {	// addi
				int iSignalBit = (int) SistNum.getBitRange ( iField, WORD - IT[3], WORD - IT[3]); //
				if ( iSignalBit == 1) {
					int supLimit = (int) Math.pow ( 2, IT [3]) - 1;
					supLimit = ~ supLimit;
					iField = iField | supLimit; //0xffffff80;
				}
//System.out.println ( "addi: iNr1= "+iNr1+", iNw= "+iNw+", iField= "+iField);
				//}
//System.out.println ( "Opcode: "+iOp + ", iNr1= "+iNr1+", iNw= "+iNw+", iField= "+iField);
				iInstruction = MontadorMIPS.assembleIType(iOp,iNr1,iNw,iField);
				break;
			case RTYPE:
				nfB = fB+RT[0];
				iNr1 = (int) SistNum.getBitRange ( lOp, nfB, nfB+RT[1]-1) + 16;	// 51,54
				nfB = nfB+RT[1];
				iNr2 = (int) SistNum.getBitRange ( lOp, nfB, nfB+RT[2]-1) + 16;	// 55,58
				nfB = nfB+RT[2];
				iNw = (int) SistNum.getBitRange ( lOp, nfB, nfB+RT[3]-1) + 16;	// 59,62
				if ( iOp == 0 && iFunc == 0) {	// nop
					boolean bTest = false;
					if ( bSpecialNops) bTest = nopORcm == 1? true: false;
					else bTest = iNr1 == 16 ? true: false;
					if ( bTest) {
						// nop
//System.out.println ( "reduced nop");
						iNr1 = iNr2 = iNw = 0;
					} else {
						// change mode to Normal
//System.out.println ( "reduced change mode to Normal, first: "+first);
						iNr1 = 1;
						iNr2 = iNw = 0;
					}
					iFunc = 32;
				}
//System.out.println ( ", iNr1= "+iNr1+", iNr2= "+iNr2+", iNw= "+iNw);
				iInstruction = MontadorMIPS.assembleRType(iOp,iNr1,iNr2,iNw,0,iFunc);
//System.out.println ( "iInstruction= "+iInstruction);
				break;
			case JTYPE:
				nfB = fB+JT[0];
				iField = (int) SistNum.getBitRange ( lOp, nfB, nfB+JT[1]-1); // 51, 63);
				iSignalBit = (int) SistNum.getBitRange ( iField, WORD - JT[1], WORD - JT[1]); //
				if ( iSignalBit == 1) {
					int supLimit = (int) Math.pow ( 2, JT [1]) - 1;
					supLimit = ~ supLimit;
					iField = iField | supLimit;
				}
//System.out.println ( "JTYPE iField= "+iField);
				iInstruction = MontadorMIPS.assembleJType(iOp,iField);
				break;
			default:
				break;			
		}
		
		return ( iInstruction);
	}
	
	//////////////////////////////////////////////////
	private long secondInstruction = -1;
	
	private void setSecondInstruction ( long si) {
		secondInstruction = si;
//System.out.println ( secondInstruction);
	}
	
	private long getSecondInstruction ( ) {
		return secondInstruction;
	}
	//////////////////////////////////////////////////
	
	private void insertRInstructionInDataPath ( long lOp, int iIncPC) {
		dtp.execute ( "add", SET, "E2", IN, iIncPC);
		dtp.execute ( "bus6", SET, "E0", IN, lOp);
		dtp.execute ( "bus6", BEHAVIOR);	
		dtp.execute ( "if_id", WRITE);	
	}
	
	private void writeNRIregister ( int nRinstructions) {
		dtp.execute ( "nRI", SET, "E1", IN, nRinstructions);
		dtp.execute ( "nRI", WRITE);	
	}
	
/*******************************************************************************
 *	A SER FORNECIDA PELO PROJETISTA.
 *	Retorna uma instrucao conforme ela foi lida.
 *	Neste caso, estah retornando um valor inteiro lido do componente IMEMORY,
 *		que eh uma memoria de instrucoes.
 *******************************************************************************/
	private long fetch ( ) {
		ExecutionPath pPipeCurrent = sePipes.search ( null);
		long lOp;
		
		if ( getSecondInstruction () != - 1) {
//System.out.println ( "em fetch, fetching second Rinstruction: "+getSecondInstruction());
			lOp = getSecondInstruction ( );
			setSecondInstruction (-1);
			insertRInstructionInDataPath ( lOp, 1);
			return lOp;
		}

		if ( dtp.execute ( "imemory", GET, "FETCH", STATUSorCONF) == 1L) {
			lOp = dtp.execute ( "imemory", GET, "S1", OUT);
			dtp.execute ( "imemory", SET, "FETCH", STATUSorCONF, 0);

			long lFetches = get ( "NroInstFetched",	FIELD);
			set ( "NroInstFetched",	FIELD, ++ lFetches);

			if ( get ( "InRISAmode",	FIELD) == 1) {
//System.out.println ( "em fetch, fetching Rinstruction: "+lOp);
				setSecondInstruction ((int) lOp & 0x0000ffff);
				lOp = (lOp >> 16) & 0x0000ffff;
//System.out.println ( "em fetch, transformando first."+lOp);
				lOp = translateRedToNormal(lOp, true);
//System.out.println ( "em fetch, transformando second."+getSecondInstruction ());
				setSecondInstruction ( translateRedToNormal(getSecondInstruction (), false));
				insertRInstructionInDataPath ( lOp, 0);
			}
			
			return ( lOp);
		}
		
		return ( 0L);
	}
	
/*******************************************************************************
 *	A SER FORNECIDA PELO PROJETISTA.
 *	Deve receber o retorno do metodo FETCH.
 *	O codigo a ser fornecido deve decodificar o argumento recebido e setar
 *		as propriedades STATUS e FIELD que foram definidos para um objeto Instruction.
 *		Deve fornecer tambem uma descricao para a instrucao (propriedade DESCRIPTION).
 *	CHAMA A DECODEINTERNAL OBRIGATORIAMENTE.
 *******************************************************************************/
	protected Instruction decode ( long parInst) {
		String sBinAux = SistNum.toBinString ( parInst, WORD);
		String sAux;
		int iInst;
		int iOpcode=-1, iType = 0;
		int iNr1 = -1, iNr2 = -1, iNw = -1, iOp = -1, iField=-1, iFunc=-1, iOpr = -1;
		int iImm = -1, iAddress = -1;
		int nRInstructions=-1;

		Instruction it = new Instruction ( );
		iInst = new Long ( parInst).intValue ( );
		iOpcode = SistNum.getBitRange ( iInst, 0, 5);
		it.set ( "PC",  FIELD, -1);
//System.out.println ( iOpcode);
		if ( get ( "InRISAmode",	FIELD) == 1) {
			nRInstructions = (int) get ( "NroRedInstInBlock",	FIELD);
//System.out.println ( "Number of RISA instructions? "+nRInstructions);
			if ( nRInstructions > 0) {	// Option 1 - INATIVA
				if ( -- nRInstructions == 0) { 
					set ( "InRISAmode",	FIELD, 0);
//System.out.println ( "Change Mode Instruction to NORMAL...");
				}
				set ( "NroRedInstInBlock",	FIELD, nRInstructions);
				writeNRIregister ( nRInstructions);
			} else {					// Option 2
				if ( iOpcode == 0) { // && iFunc == 32) {
					iNr1 = SistNum.getBitRange ( iInst, 6, 10);
					iNr2 = SistNum.getBitRange ( iInst, 11, 15);
					iNw = SistNum.getBitRange ( iInst, 16, 20);
					iFunc = SistNum.getBitRange ( iInst, 26, 31);
//System.out.println ("\t,\t"+iOpcode+"\t,\t"+iImm+"\t,\t"+iNr1+"\t,\t"+iNr2+"\t,\t"+iNw+"\t,\t");
					if ( iFunc == 32 && iNr1+iNr2+iNw == 1) {
//System.out.println ( "Change Mode Instruction to Normal at time: "+sSim.getTime());
						set ( "InRISAmode",	FIELD, 0);
					}
				}
			}
		}
		
		if ( iOpcode == 0x3f && get ( "InRISAmode",	FIELD) == 0) {
			set ( "InRISAmode",	FIELD, 1);
			iType = BUBBLE;
			int i6to31 = SistNum.getBitRange ( iInst, 6, 31);
			if ( i6to31 == 0x03FFFFFF) i6to31 = -1;
			this.rISA.setRisaConfiguration(i6to31);
//System.out.println ( "Change Mode Instruction to RISA: "+ i6to31 +" at time: "+sSim.getTime());
/*
			if ( i6to15 == 1023) {
				setSecondInstruction ( translateRedToNormal(iInst & 0x0000ffff, false));
				dtp.execute ( "add", SET, "E2", IN, 0);
//System.out.println ( "Change Mode Instruction to RISA..."+getSecondInstruction());
			} else {
				nRInstructions = SistNum.getBitRange ( iInst, 6, 31) * 2;
				set ( "NroRedInstInBlock",	FIELD, nRInstructions);
				writeNRIregister ( nRInstructions);
			}
*/
//System.out.println ( "Number of RISA instructions? "+nRInstructions);
		}
		
		// Inserir este teste para novos opcodes
		if ( iOpcode == 0 || iOpcode == 0x11) iType = RTYPE;
		//
		if ( iOpcode == 0x04 || iOpcode == 0x23 || iOpcode == 0x2b || iOpcode == 0x08) iType = ITYPE;
		if ( iOpcode == 0x09 || iOpcode == 0x0c || iOpcode == 0x0d || iOpcode == 0x0e) iType = ITYPE;
		if ( iOpcode == 0x0f || iOpcode == 0x0a || iOpcode == 0x0b || iOpcode == 0x01) iType = ITYPE;
		if ( iOpcode == 0x07 || iOpcode == 0x06 || iOpcode == 0x05) iType = ITYPE;
		if ( iOpcode == 0x20 || iOpcode == 0x28) iType = ITYPE;
		if ( iOpcode == 0x10 || iOpcode == 0x11 || iOpcode == 0x12) iType = ITYPE; 	// slli e srli e srai - TEMPORARIOS
		if ( iOpcode == 0x22 || iOpcode == 0x26 || iOpcode == 0x2a || iOpcode == 0x2e) iType = ITYPE;
		//
		if ( iOpcode == 0x02 || iOpcode == 0x03) iType = JTYPE;
		if ( iOpcode == 0xff) iType = BUBBLE;
		//
		it.set ( "OPCODE", FIELD, iOpcode);
		it.set ( "TYPE", FIELD, iType);
		it.set ( "ACCESS_TYPE", FIELD, WORD);
		it.set ( "RFWrCtrl", FIELD, WORD);
		it.set ( "mnem",STRING,"null");
		iAddress = ( int) dtp.execute ( "pc", GET, "S1", OUT);
		it.set ( "PC",  FIELD, iAddress);
//if ( iAddress == 35) System.exit(1);
//System.out.println ( "PC = "+iAddress+ " , "+get ( "InRISAmode",	FIELD));
if ( debugging)
System.out.println ("\t,\t"+iOpcode+"\t,\t"+iImm+"\t,\t"+iNr1+"\t,\t"+iNr2+"\t,\t"+iNw+"\t,\t");
		switch ( iType) {
		case ITYPE:
			iNr1 = SistNum.getBitRange ( iInst, 6, 10);
			iNw = SistNum.getBitRange ( iInst, 11, 15);
			iAddress = SistNum.getBitRange ( iInst, 16, 31);
			iImm = iAddress;
			//iSel = 1;
			switch ( iOpcode) {
				case 0x20:	// lb
				case 0x22:	// lwl
				case 0x23: 	// lw
				case 0x26:	// lwr
					iOp = ADD;
					iNr2 = 0;	// para nao ficar com o valor -1
					it.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\instructions\\Load");
					if ( iOpcode == 0x23) it.set ( "mnem",STRING,"lw");
					else if ( iOpcode == 0x20){
						it.set ( "ACCESS_TYPE", FIELD, BYTE);
						it.set ( "mnem",STRING,"lb");
					} else if ( iOpcode == 0x22){
						it.set ( "ACCESS_TYPE", FIELD, BITSLEFT16);
						it.set ( "RFWrCtrl", FIELD, BITSLEFT16);
						it.set ( "mnem",STRING,"lwl");
					} else if ( iOpcode == 0x26){
						it.set ( "ACCESS_TYPE", FIELD, BITSRIGHT16);
						it.set ( "RFWrCtrl", FIELD, BITSRIGHT16);
						it.set ( "mnem",STRING,"lwr");
					}
					break;
				case 0x28:	// sb
				case 0x2a:	// swl
				case 0x2b: 	// sw
				case 0x2e:	// swr
					iOp = ADD;
					iNr2=iNw;	// para nao ficar com o valor -1
					it.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\instructions\\Store");
					if ( iOpcode == 0x2b) it.set ( "mnem",STRING,"sw");
					else if ( iOpcode == 0x28){
						it.set ( "ACCESS_TYPE", FIELD, BYTE);
						it.set ( "mnem",STRING,"sb");
//System.out.println ( "e' um sb!");
//debugging = true;
					} else if ( iOpcode == 0x2a){
						it.set ( "ACCESS_TYPE", FIELD, BITSLEFT16);
						it.set ( "mnem",STRING,"swl");
					} else if ( iOpcode == 0x2e){
						it.set ( "ACCESS_TYPE", FIELD, BITSRIGHT16);
						it.set ( "mnem",STRING,"swr");
					}
					break;
				case 0x01: // bltz					
				case 0x04: // beq
				case 0x05: // bne
				case 0x06: // blez
				case 0x07: // bgtz
					//iSel = 0;
					set ( "BRANCH", STATUSorCONF, 1);
					iNr2 = iNw;
					iOp = ADD;	// soma ( antes passava o imediato: E2)
					it.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\instructions\\Branch");					
					//it.set ( "unit", FIELD, COMPARATOR);
					if ( iOpcode == 0x04) {
						iOpr = EQUAL;
						it.set ( "mnem",STRING,"beq");
					}/* else if ( iOpcode == 0x01) {
						iOpr = GREATERTHEN_OR_EQUAL_ZERO;
						it.set ( "mnem",STRING,"bgez");
						iNw = -1;
					} else if ( iOpcode == 0x01) {
						iOpr = GREATERTHEN_OR_EQUAL_ZERO;
						it.set ( "mnem",STRING,"bgezal");
						iNw = 31;
					}*/ else if ( iOpcode == 0x07) {
						iOpr = GREATERTHEN_ZERO;
						it.set ( "mnem",STRING,"bgtz");
						//iNr2 = SistNum.getBitRange ( iInst, 11, 15);
						//iNr1 = iNr2;
						//iNr2 = 0;
						iNw = -1;
//System.out.println ("\t,\topcode = "+iOpcode+"\t,\timediato = "+iImm+"\t,\tnr1 = "+iNr1+"\t,\tnr2 = "+iNr2+"\t,\tnw = "+iNw+"\t,\t");
					} else if ( iOpcode == 0x06) {
						iOpr = LESSTHEN_OR_EQUAL_ZERO;
						it.set ( "mnem",STRING,"blez");
						iNw = -1;
					} /*else if ( iOpcode == 0x01) {
						iOpr = LESSTHEN_ZERO;
						it.set ( "mnem",STRING,"bltzal");
						iNw = 31;
					}*/ else if ( iOpcode == 0x01) {
						iOpr = LESSTHEN_ZERO;
						it.set ( "mnem",STRING,"bltz");
						//iNr2 = SistNum.getBitRange ( iInst, 11, 15);
						//iNr1 = iNr2;
						//iNr2 = 0;
						iNw = -1;
//System.out.println ("\t,\topcode = "+iOpcode+"\t,\timediato = "+iImm+"\t,\tnr1 = "+iNr1+"\t,\tnr2 = "+iNr2+"\t,\tnw = "+iNw+"\t,\t");
					} else if ( iOpcode == 0x05) {
						iOpr = NOTEQUAL;
						it.set ( "mnem",STRING,"bne");
//System.out.println ("\t,\topcode = "+iOpcode+"\t,\timediato = "+iImm+"\t,\tnr1 = "+iNr1+"\t,\tnr2 = "+iNr2+"\t,\tnw = "+iNw+"\t,\t");
					}
					break;
				case 0x08: // addi
				case 0x09: // addiu
				case 0x0a: // slti
				case 0x0b: // sltiu
				case 0x0c: // andi
				case 0x0d: // ori
				case 0x0e: // xori
				case 0x0f: // lui
				case 0x10: // srli - opcode TEMPORARIO
				case 0x11: // slli - opcode TEMPORARIO
				case 0x12: // srai - opcode TEMPORARIO
					iNr2=0;
					it.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\instructions\\AritITYPE");
					//it.set ( "unit", FIELD, ALU);
					iOp = ADD;
					if ( iOpcode == 0x08) it.set ( "mnem",STRING,"addi");
					else if ( iOpcode == 0x09) it.set ( "mnem",STRING,"addiu");
					else if ( iOpcode == 0x0c) {
						iOp = AND;
						it.set ( "mnem",STRING,"andi");
					} else if ( iOpcode == 0x0d) {
						iOp = OR;
						it.set ( "mnem",STRING,"ori");
					} else if ( iOpcode == 0x0e) {
						iOp = XOR;
						it.set ( "mnem",STRING,"xori");
					} else if ( iOpcode == 0x0f) {
						iOp = E21632;
						it.set ( "mnem",STRING,"lui");
					} else if ( iOpcode == 0x0a) {
						iOp = SLT;
						it.set ( "mnem",STRING,"slti");
					} else if ( iOpcode == 0x0b) {
						it.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\instructions\\Compare");
						//it.set ( "unit", FIELD, COMPARATOR);
						iOp = LESSTHAN;
						//it.set ( "mnem",STRING,"sltiu");
					} else if ( iOpcode == 0x10) {
						iOp = SLR;
						it.set ( "mnem",STRING,"srli");
					} else if ( iOpcode == 0x11) {
						iOp = SLL;
						it.set ( "mnem",STRING,"slli");
//System.out.println ("\t,\topcode = "+iOpcode+"\t,\timediato = "+iImm+"\t,\tnr1 = "+iNr1+"\t,\tnr2 = "+iNr2+"\t,\tnw = "+iNw+"\t,\t");
					} else if ( iOpcode == 0x12) {
						iOp = SAR;
						it.set ( "mnem",STRING,"srai");
//System.out.println ("\t,\topcode = "+iOpcode+"\t,\timediato = "+iImm+"\t,\tnr1 = "+iNr1+"\t,\tnr2 = "+iNr2+"\t,\tnw = "+iNw+"\t,\t");
					}
					break;
				default:
					break;					
			}
			break;

		case RTYPE:
			iNr1 = SistNum.getBitRange ( iInst, 6, 10);
			iNr2 = SistNum.getBitRange ( iInst, 11, 15);
			iNw = SistNum.getBitRange ( iInst, 16, 20);
			iField = SistNum.getBitRange ( iInst, 21, 25);
			iFunc = SistNum.getBitRange ( iInst, 26, 31);
//System.out.println ( "iFunc = "+iFunc);
			//iSel = 0;
			switch ( iOpcode) {
				case 0: // Aritmeticas e Logicas
					switch ( iFunc) {
						case 0x00: // sll
						case 0x02: // srl
						case 0x03: // sra
						case 0x04: // sllv
						case 0x06: // srlv
						case 0x07: // srav
						case 0x08: // jr
						case 0x09: // jalr
						case 0x18: // mult
						case 0x19: // multu
						case 0x1a: // div
						case 0x1b: // divu
						case 0x20: // add
						case 0x21: // addu
						case 0x22: // sub
						case 0x23: // subu
						case 0x24: // and
						case 0x25: // or
						case 0x26: // xor
						case 0x27: // nor
						case 0x2a: // slt
						case 0x2b: // sltu
						case 0x3f: // MAC 	/* Modif.MAC */
							it.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\instructions\\AritRTYPE");
							//it.set ( "unit", FIELD, ALU);
							iOp = ADD;
							if ( iFunc == 0x3f) {	/* Modif.MAC - begin */
								//iNr3 = iField;
								iOp = MAC;
								it.set ( "mnem",STRING,"MAC");
							} else					/* Modif.MAC - end */
							if ( iFunc == 0x20) it.set ( "mnem",STRING,"add");
							else if ( iFunc == 0x21) it.set ( "mnem",STRING,"addu");
							else if ( iFunc == 0x24) {
								iOp = AND;
								it.set ( "mnem",STRING,"and");
							} else if ( iFunc == 0x27) {
								iOp = NOR;
								it.set ( "mnem",STRING,"nor");
							} else if ( iFunc == 0x25) {
								iOp = OR;
								it.set ( "mnem",STRING,"or");
							} else if ( iFunc == 0x26) {
								iOp = XOR;
								it.set ( "mnem",STRING,"xor");
							} else if ( iFunc == 0x04) {
								int iTmp = iNr1; iNr1 = iNr2; iNr2 = iTmp; // ver descricao da instrucao
								iOp = SLL;
								it.set ( "mnem",STRING,"sllv");
							} else if ( iFunc == 0x00) {
								if ( iNr1 == 0 && iNr2 == 0 && iNw == 0 && iField == 0 && iFunc == 0) {
									iOp = ADD;
									it.set ( "mnem",STRING,"nop");									
								} else {
									int iTmp = iNr1; iNr1 = iNr2; iNr2 = iTmp; // ver descricao da instrucao
									iImm = iField;
									//iSel = 1;
									iOp = SLL;
									it.set ( "mnem",STRING,"sll");
								}
							} else if ( iFunc == 0x06) {
								int iTmp = iNr1; iNr1 = iNr2; iNr2 = iTmp; // ver descricao da instrucao
								iOp = SLR;
								it.set ( "mnem",STRING,"srlv");
							} else if ( iFunc == 0x07) {
								int iTmp = iNr1; iNr1 = iNr2; iNr2 = iTmp; // ver descricao da instrucao
								iOp = SAR;
								it.set ( "mnem",STRING,"srav");
							} else if ( iFunc == 0x02) {
								int iTmp = iNr1; iNr1 = iNr2; iNr2 = iTmp; // ver descricao da instrucao
								iImm = iField;
								//iSel = 1;
								iOp = SLR;
								it.set ( "mnem",STRING,"srl");
							} else if ( iFunc == 0x03) {
								int iTmp = iNr1; iNr1 = iNr2; iNr2 = iTmp; // ver descricao da instrucao
								iImm = iField;
								//iSel = 1;
								iOp = SAR;
								it.set ( "mnem",STRING,"sra");
							} else if ( iFunc == 0x22) {
								iOp = SUB;
								it.set ( "mnem",STRING,"sub");
							} else if ( iFunc == 0x23) {
								iOp = SUB;
								it.set ( "mnem",STRING,"subu");
							} else if ( iFunc == 0x18) {
								iOp = MUL;
								it.set ( "mnem",STRING,"mult");
								//it.set ( "unit", FIELD, ALU);		/* Modif.MULT and others */	
							} else if ( iFunc == 0x19) {
								iOp = MUL;
								it.set ( "mnem",STRING,"multu");
							} else if ( iFunc == 0x1a) {
								iOp = DIV;
								it.set ( "mnem",STRING,"div");
							} else if ( iFunc == 0x1b) {
								iOp = DIV;
								it.set ( "mnem",STRING,"divu");
							} else if ( iFunc == 0x2a) {
								//it.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\instructions\\Compare");
								//it.set ( "unit", FIELD, COMPARATOR);
								iOp = SLT;
								it.set ( "mnem",STRING,"slt");
							} else if ( iFunc == 0x2b) {
								//it.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\instructions\\Compare");
								//it.set ( "unit", FIELD, COMPARATOR);
								iOp = SLT;
								it.set ( "mnem",STRING,"sltu");
							} else if ( iFunc == 0x08 || iFunc == 0x09) {
								set ( "BRANCH", STATUSorCONF, 1);
								it.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\instructions\\JumpOnReg");
								//it.set ( "unit", FIELD, COMPARATOR);
								iNr2 = iNr1;		// nao usado realmente
								iOp = E1;
								if ( iFunc == 0x09) it.set ( "mnem",STRING,"jalr");
								else it.set ( "mnem",STRING,"jr");
							} /* else if ( iFunc == 0x28) {
								iOp = REM;
								it.set ( "mnem",STRING,"rem");
							} */
							break;
						default:
							break;
					}
					break;
				case 0x11: // Ponto Flutuante
					it.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\instructions\\FP");
					it.set ( "mnem",STRING,"add.d");
					iNr1 = iNr2;
					iNr2 = iNw;
					iNw = iField;
					//it.set ( "unit", FIELD, FPU);
					iOp = 10;
					break;
			}
			break;

		case JTYPE:
			set ( "BRANCH", STATUSorCONF, 1);
			it.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\instructions\\Jump");
			//it.set ( "unit", FIELD, COMPARATOR);
			iAddress = SistNum.getBitRange ( iInst, 6, 31);
			int iSignalBit = SistNum.getBitRange ( iInst, 6, 6);
//System.out.println( "signal bit = "+iSignalBit);
//System.out.println( "address = "+iAddress);
			if ( iSignalBit == 1) iAddress = iAddress | 0xfc000000;
//System.out.println ( SistNum.toBinString ( iAddress, 32));
//System.out.println( "address = "+iAddress);
			iNr1 = 0;		// nao usado realmente
			iNr2 = 0;		// nao usado realmente
			iOp = ADD;	// 	// soma
			//iSel = 0;
			if ( iOpcode == 0x02) {
				it.set ( "mnem",STRING,"j");
				iNw = 0;
			} else if ( iOpcode == 0x03) {
				it.set ( "mnem",STRING,"jal");
				iNw = 31;
			}
			break;
			
		case BUBBLE:
			it.set ( "DESCRIPTION", STRING, "BUBBLE");
			iNr1 = - 1;
			iNw = - 1;
			iNr2 = - 1;
			iOp = - 1;
			break;
			
		default:
			break;
		}
		
		it.set ( "NR1", FIELD, iNr1);
		it.set ( "NR2", FIELD, iNr2);
		it.set ( "NW1", FIELD, iNw);
		it.set ( "OP",  FIELD, iOp);
		it.set ( "OPR",  FIELD, iOpr);
		it.set ( "IMM", FIELD, iImm);
		it.set ( "ADDRESS",  FIELD, iAddress);
		it.set ( "FIELD", FIELD, iField);
		it.set ( "FUNC", FIELD, iFunc);
		
//	PRESENCA OBRIGATORIA
		try {
			decodeInternal ( it);
		} catch ( Exception E) {
			System.out.println ( get ( "InRISAmode",	FIELD));
			it.list ( );
		}
		
		it.set ( "mnem",STRING,Montador.getAssemblyCode(iAddress));
//		currentIQueue = processorQueues.search( "all");
//		currentIQueue.add( itctNew);
		
		return ( it);
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
			
			//processorQueues = new QueuesOfInstructions ( );
			//processorQueues.add ( new InstructionQueue ( "all"));
			//processorQueues.add ( new InstructionQueue ( "data hazards"));
			//iNop = decode ( 469762048L);
			iNop = decode ( 0L);	// nop
			iNop.set( "PC", FIELD, FETCH);
			pPipeCurrent.walk ( iNop, "FETCH");
			Clock.setInitialTime ( -100.0F);
			iBubble = decode ( 0L);
			iBubble.set( "PC", FIELD, BUBBLE);
			pPipeCurrent.setBubble ( iBubble);
			bBeginProgram = false;
		}

/*******************************************************************************
 *	A SER FORNECIDA PELO PROJETISTA.
 *	Deve percorrer os estagios de pipeline executando as instrucoes.
 *	Ha metodos pre-definidos que podem ser chamados.
 *	Deve prever a inicializacao do processador.
 *******************************************************************************/
	private void execute ( ) {
		if ( iTimeMode == PIPELINED) executeBackward ( null);		
		else executeForward ( null);
	}

/*******************************************************************************
 *	A SER FORNECIDA PELO PROJETISTA.
 *	Testa RAW hazards no DLX.
 *******************************************************************************/
	public boolean dataHazard ( Instruction parIt) {
		ExecutionPath pPipeCurrent = sePipes.search ( null);
		Instruction itctD, itctE, itctM;
		int iNwd = - 1, iNwe = - 1, iNwm = - 1;
		int iNr1, iNr2;

		if ( parIt == null) return ( false);
		
		itctD = pPipeCurrent.getCurrentInst ( "DECODE");
		if ( itctD != null) iNwd = (int) itctD.get ( "NW1", FIELD);
		itctE = pPipeCurrent.getCurrentInst ( "EXECUTE");
		if ( itctE != null) iNwe = (int) itctE.get ( "NW1", FIELD);
		itctM = pPipeCurrent.getCurrentInst ( "MEMORY");
		if ( itctM != null) iNwm = (int) itctM.get ( "NW1", FIELD);

//System.out.print ( "A escrever: "+iNwd+","+iNwe+","+iNwm);

		iNr1 = (int) parIt.get ( "NR1", FIELD);
		iNr2 = (int) parIt.get ( "NR2", FIELD);

//System.out.println ( "	e a ler: "+iNr1+","+iNr2);

		if ( iNwd == - 1 && iNwe == - 1 && iNwm == - 1) return ( false);

		if ( (iNr1 == iNwd || iNr2 == iNwd) && iNwd != 0) return ( true);
		else if ( (iNr1 == iNwe || iNr2 == iNwe) && iNwe != 0) return ( true);
		else if ( (iNr1 == iNwm || iNr2 == iNwm) && iNwm != 0) return ( true);

		return ( false);
	}

	boolean isNOP ( int iInstruction) {
		int iOpcode, iNr1, iNr2, iNw, iField, iFunc;
		
		iOpcode = SistNum.getBitRange ( iInstruction, 0, 5);
		iField = SistNum.getBitRange ( iInstruction, 21, 25);
		iFunc = SistNum.getBitRange ( iInstruction, 26, 31);
		
		if ( iOpcode == 0 && iField == 0 && iFunc == 0) return ( true);
		return ( false);
	}
	
	boolean isHALT ( int iInstruction) {
		int iOpcode, iNr1, iNr2, iNw, iField, iFunc;
		
		iOpcode = SistNum.getBitRange ( iInstruction, 0, 5);
		iFunc = SistNum.getBitRange ( iInstruction, 26, 31);
		
		if ( iOpcode == 0 && iFunc == 0x3f) {
			System.out.println ( "End of Program.");
			set ( "ExecutionCycles",	FIELD, (long) sSim.getTime()+3);
			return ( true);
		}
		return ( false);
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
				break;
			case PIPELINED:
				if ( bFirstInstruction == true) {
//System.out.println("--> Inserindo PRIMEIRA instrucao no pipeline!!!");
					Clock.setInitialTime ( 0.0F);
					bFirstInstruction = false;
				}
				if ( get ( "TDCF_TestBranches", STATUSorCONF) == 1 && get ( "BRANCH", STATUSorCONF) == 1) {
					pPipeCurrent.freeze ( "FETCH", null);
					if ( get ( "TDCF_TestHazards", STATUSorCONF) == 1 && dataHazard ( iNew) == true) {
//System.out.println ( "--> DATA HAZARD on Branches!!!");
						pPipeCurrent.insertBubble ( "DECODE");
						sSim.advanceTime ( );
					} else { 
						pPipeCurrent.walk ( iNew, "DECODE");
						iNew = pPipeCurrent.getBubble ( );
						sSim.advanceTime ( );
						if ( pPipeCurrent.testInstruction("MEMORY","DESCRIPTION",Processor.getProcessorName()+"\\instructions\\Branch")==true) {
//System.out.println ( "Vou liberar pipe!!!");
							pPipeCurrent.release ( "FETCH", null);
							set ( "BRANCH", STATUSorCONF, 0);
						} 
						if ( pPipeCurrent.testInstruction("MEMORY","DESCRIPTION",Processor.getProcessorName()+"\\instructions\\Jump")==true) {
//System.out.println ( "Vou liberar pipe!!!");
							pPipeCurrent.release ( "FETCH", null);
							set ( "BRANCH", STATUSorCONF, 0);
						}
						if ( pPipeCurrent.testInstruction("MEMORY","DESCRIPTION",Processor.getProcessorName()+"\\instructions\\JumpOnReg")==true) {
//							System.out.println ( "Vou liberar pipe!!!");
							pPipeCurrent.release ( "FETCH", null);
							set ( "BRANCH", STATUSorCONF, 0);
						}
					}
				} else if ( get ( "TDCF_TestHazards", STATUSorCONF) == 1 && dataHazard ( iNew) == true) {
//System.out.println ( "--> DATA HAZARD!!!");
					pPipeCurrent.freeze ( "FETCH", null);
					pPipeCurrent.insertBubble ( "DECODE");
					sSim.advanceTime ( );
				} else {
					pPipeCurrent.release ( "FETCH", null);
					pPipeCurrent.walk ( iNew, "DECODE");
					sSim.advanceTime ( );
					if ( pPipeCurrent.isEmpty ( "DECODE") == true) {
						pPipeCurrent.insert ( null, "FETCH");
						bEndProgram = true;
					}
				}
				break;
		}

		return ( bEndProgram);
	}

	public boolean bMustStop = true;
	
	public boolean mustStop ( ) {
		int i, iNelemets;
		if(sBreakpoint != null) {
			iNelemets = sBreakpoint.getNelems();
			for(i=0; i < iNelemets; i++) {
				pBreakpoint = (Breakpoint) sBreakpoint.traverse(i);
				if ( pBreakpoint.mp != null && pBreakpoint.active) {
					//pBreakpoint.mp.list();
					if (dtp.execute ( pBreakpoint.mp.sComponente, GET, pBreakpoint.mp.getPortType(), 0, 0) == pBreakpoint.mp.getContents()) {
						//mp = null;
						//sBreakpoint.remove(i);
						pBreakpoint.active = false;
						return ( true);
					}
				}
			}
		}


/*		if ( bMustStop && dtp.execute ( "pc", GET, "S1", OUT) == 157) {
			bMustStop = false;
			return ( true);
		}*/
		return ( false);
	}

	public String getMnemonico ( long lOpcode) {
		int i;
		String s = "-";
		int iInst, iOpcode, iFunc;

		if ( lOpcode == 0L) return ( "nop");
		
		iInst = new Long ( lOpcode).intValue ( );
		iOpcode = SistNum.getBitRange ( iInst, 0, 5);
		iFunc = SistNum.getBitRange ( iInst, 26, 31);
		
		if ( iOpcode == -1) return ( mnemonicos [ intOpcodes.length - 1]);
		if ( iOpcode == 0) {
			for ( i = 0; i < sizeBytes.length; i ++) {
				if ( iOpcode == intOpcodes [ i] && iFunc == sizeBytes [ i]) break;
			}			
		} else {
			for ( i = 0; i < intOpcodes.length; i ++) {
				if ( iOpcode == intOpcodes [ i]) break;
			}
		}
		
		if ( i == intOpcodes.length) return s;
		else return ( mnemonicos [ i]);
	}

	public boolean usingRISA ( ) {
		if ( get ( "TDCF_UsingRISA", STATUSorCONF) == 0) return false;
		else return true;
	}
	
	public int isrISAble ( long lOpcode) {
		int i, index;
		int iInst, iOpcode, iFunc;
		int iWhichRISA = (int) get ( "TDCF_WhichRISA",	STATUSorCONF);

		try {
//System.out.println ( "usando rISA "+ iWhichRISA);
			rISAinstructions = rISAinstructionsMatrix [ iWhichRISA];
		} catch (Exception e) {
//System.out.println ( "usando rISA "+ 0);
			rISAinstructions = rISAinstructionsMatrix [ 0];
		}
		if ( rISAinstructions.length == 0) return -1;
		
		iInst = new Long ( lOpcode).intValue ( );
		iOpcode = SistNum.getBitRange ( iInst, 0, 5);
		iFunc = SistNum.getBitRange ( iInst, 26, 31);
//System.out.println ( "opcode: "+ iOpcode);		
		if ( iOpcode == 0) {
			for ( i = 0; i < rISAinstructions.length; i ++) {
				index = rISAinstructions [i];
				if ( iOpcode == intOpcodes [ index] && iFunc == sizeBytes [ index]) return i;
			}			
		} else {
			for ( i = 0; i < rISAinstructions.length; i ++) {
				index = rISAinstructions [i];
				if ( iOpcode == intOpcodes [ index]) return i;
			}
		}
		
		return ( -1);
	}
	
	public void createRegZipNumberForItypeInstr ( int iSize) {
		regsForItypeInstr = new int [ iSize];
		iSizeRegsForItypeInstr = iSize;
		iNroRegsForItypeInstr = 0;
	}

	public boolean setRegZipNumberForItypeInstr ( int iNumber) {
		int i;
		
		if ( iNroRegsForItypeInstr == iSizeRegsForItypeInstr) {
//System.out.println ( "overflow "+iNumber);
			/*System.out.println ( iSizeRegsForItypeInstr);
			for ( i = 0; i < iNroRegsForItypeInstr; i ++) {
				System.out.print ("    : ,"+regsForItypeInstr  [ i]);
			}
			System.out.println ( );*/
			return ( false);
		} else {
//System.out.println ( "not overflow "+iNumber);
			for ( i = 0; i < iNroRegsForItypeInstr; i ++) {
				if ( regsForItypeInstr [i] == iNumber) return ( true);
			}
			regsForItypeInstr [iNroRegsForItypeInstr++] = iNumber;
			/*System.out.println ( iSizeRegsForItypeInstr);
			for ( i = 0; i < iNroRegsForItypeInstr; i ++) {
				System.out.print ("    : ,"+regsForItypeInstr  [ i]);
			}
			System.out.println ( );*/
			return ( true);
		}
	}

	public int getRegZipNumberForItypeInstr ( int iNumber) {
//System.out.println ( "recebi: "+iNumber);
//listRegZipNumberForItypeInstr ( );
		for ( int i = 0; i < iNroRegsForItypeInstr; i ++) {
			if ( regsForItypeInstr [i] == iNumber) {
//System.out.println ( "retornei: "+i);
				return i;
			}
		}			
		
		return ( -1);
	}

	public void listRegZipNumberForItypeInstr ( ) {
		for ( int i = 0; i < iNroRegsForItypeInstr; i ++) {
//System.out.println ( "mapeamento: "+i+" , "+ regsForItypeInstr [i]);
		}			
	}
	
	private void keepStatus ( MIPS dlx, int iWhichRISA) {
		dlx.regsForItypeInstr = this.regsForItypeInstr;
		dlx.iSizeRegsForItypeInstr = this.iSizeRegsForItypeInstr;	
		dlx.iNroRegsForItypeInstr = this.iNroRegsForItypeInstr;		
		dlx.rISAinstructions = rISAinstructionsMatrix [ iWhichRISA];
	}
	
	public void setReset ( String parProgram) throws Exception {
		String sProgram = null;
		Simulator SIM_NEW = new Simulator ( );
		//
		int iUsingRISA = (int) get ( "TDCF_UsingRISA",	STATUSorCONF);
		int iWhichRISA = (int) get ( "TDCF_WhichRISA",	STATUSorCONF);
		int iWhichFormat = (int) get ( "TDCF_8or16", STATUSorCONF);
//System.out.println ( "iUsingRISA before reseting = "+iUsingRISA);
		MIPS 	dlx 	= new MIPS 	( SIM_NEW);
		dlx.setRodadaRisa = this.setRodadaRisa;
		dlx.set ( "TDCF_UsingRISA",	STATUSorCONF, iUsingRISA);
		dlx.set ( "TDCF_WhichRISA",	STATUSorCONF, iWhichRISA);
		dlx.set ( "TDCF_8or16", STATUSorCONF, iWhichFormat);
		//iUsingRISA = (int) dlx.get ( "TDCF_UsingRISA",	STATUSorCONF);
		keepStatus ( dlx, iWhichRISA);
//System.out.println ( "iUsingRISA after reseting = "+iUsingRISA);
		//
		Shell.initialize ( "# DLX> ",		SIM_NEW,( Processor) dlx);
		if ( parProgram == null) sProgram = "Program.bin";
		else sProgram = parProgram;
		System.out.println ( "file name: "+sProgram);
		dlx.initialize ( 	"DLXOrg.txt",sProgram,	"InstructionSet.txt","PipelineStages.txt");
		SIM_NEW.resetTime ( );
		SIM_NEW.Initializations ( dlx);
		bFirstInstruction = true;
		bBeginProgram = true;
		bEndProgram = false;
		Shell.decodifica ( "reset dlx");
	}
	
	public void listRisas ( ) {
		int i, j;
		String sTmp;
		
		MIPSApp.messagesToUser ( null, "---------- ---------- ---------- ---------- ----------");
		for ( i = 0; i < rISAinstructionsMatrix.length; i ++) {
			sTmp = "*** rISA "+i;
			MIPSApp.messagesToUser ( null, sTmp);
			sTmp = "";
			for ( j = 0; j < rISAinstructionsMatrix [0].length; j ++) {
				sTmp = sTmp + mnemonicos [rISAinstructionsMatrix [i][j]]+" , ";
			}
			MIPSApp.messagesToUser ( null, sTmp);
		}
	}
	
	public void listFields ( ) {
		int i;
		String sTmp;
		int vet [];
		
		MIPSApp.messagesToUser ( null, "---------- ---------- ---------- ---------- ----------");
		vet = this.rISA.ITypeBitFields;
		sTmp = "I-TYPE:";
		MIPSApp.messagesToUser ( null, sTmp);
		sTmp = "";
		for ( i = 0; i < vet.length; i ++) sTmp = sTmp + vet[i] +" , ";
		MIPSApp.messagesToUser ( null, sTmp);
		//
		vet = this.rISA.RTypeBitFields;
		sTmp = "R-TYPE:";
		MIPSApp.messagesToUser ( null, sTmp);
		sTmp = "";
		for ( i = 0; i < vet.length; i ++) sTmp = sTmp + vet[i] +" , ";
		MIPSApp.messagesToUser ( null, sTmp);
		//
		vet = this.rISA.JTypeBitFields;
		sTmp = "J-TYPE:";
		MIPSApp.messagesToUser ( null, sTmp);
		sTmp = "";
		for ( i = 0; i < vet.length; i ++) sTmp = sTmp + vet[i] +" , ";
		MIPSApp.messagesToUser ( null, sTmp);
	}
	
	private void dumpContents ( String sCompName, int iBegin, int iEnd) {
		int i, iValue, iTmp1, iTmp2;
		String sAux;
		
		for ( i = iBegin; i < iEnd; i ++) {
			iValue = (int) dtp.execute ( sCompName, GET, i, 0);
			iTmp1 = SistNum.getDefaultSizeFormat();
			iTmp2 = SistNum.getDefaultNumberFormat();
			SistNum.setDefaultSizeFormat(BYTE);
			SistNum.setDefaultNumberFormat(HEXADECIMAL);
			if ( Character.isLetterOrDigit ((char) iValue) || ! Character.isISOControl ((char) iValue))
				sAux = new Integer (iValue).toString()+" , "+SistNum.printInformation ( iValue, INTEGER)+" = '"+Character.toString((char) iValue)+"'";
			else
				sAux = new Integer (iValue).toString()+" , "+SistNum.printInformation ( iValue, INTEGER);
			SistNum.setDefaultSizeFormat(iTmp1);
			SistNum.setDefaultNumberFormat(iTmp2);
			System.out.println ( "********** "+sCompName + "[" + i + "] = "+ sAux);
		}
	}
	
	public void simulateBatch ( String sPath, String sPrg) {
		String sProgram;
		String sTarget  = "./processors/mips/target.bin";
		int result = 0, iWhich;
		
		if ( sPath == null) sPath = "./processors/mips/programs/CSources/mibench/";
		//if ( sPath == null) sPath = "./processors/mips/programs/";
		if (sPrg != null) sProgram = sPath + sPrg;
		else return;
		sProgram = Platform.treatPathNames( sProgram);
		sTarget = Platform.treatPathNames( sTarget);
		File f = new File (sTarget);

		System.out.println ( "********** Batch Simulation **********");
		System.out.println ( "**************************************");
		System.out.println ( "**************************************");
		System.out.println ( "**************************************");
		System.out.println ( "**************************************");
		System.out.println ( "SOURCE CODE: "+sProgram);
		
		for ( int i = - 1; i < rISAinstructionsMatrix.length; i ++) {
			int simNro = i + 1;
			
			System.out.println ( "\n******************** Simulation number "+simNro+" - BEGIN **********");
		
			DLXreset ( );
			//---
			if (i == 4){
				this.set("TDCF_8or16", STATUSorCONF, 1);
			}else{
				this.set("TDCF_8or16", STATUSorCONF, 0);
			}
			// updateRisaInstructionMatrix();
			//---
			if ( i < 0) this.set ( "TDCF_UsingRISA",	STATUSorCONF, 0);
			else this.set ( "TDCF_UsingRISA",	STATUSorCONF, 1);
			this.set ( "TDCF_WhichRISA",	STATUSorCONF, i);
			// a classe Shell possui referencias diretas a elementos de Processor (DLX neste caso)
			// e ela e' responsavel pela execuao dos comandos de simulaao da descriao
			Shell.initialize ( "# DLX> ",		sSim,( Processor) this);
			MontadorMIPS mmn = new MontadorMIPS (this, sProgram, sTarget);
			this.initialize ( 	"DLXOrg.txt","target.bin",	"InstructionSet.txt","PipelineStages.txt");
			sSim.Initializations ( this);
		
			bFirstInstruction = true;
			bBeginProgram = true;
			bEndProgram = false;
		
			// aqui se da' a simulaao
			sSim.getTimeStatistics( this);
			
			System.out.println ( "********** rISA number =                    "+i);
			result = (int) this.get ( "ExecutionCycles",	FIELD);
			System.out.println ( "********** execution cycles =               "+result);
			result = (int) this.get ( "NroInstFetched",	FIELD);
			System.out.println ( "********** number of fetched instructions = "+result);
			//dumpContents ( "dmemory", 24, 36);	// p/ multVet e application1
			//dumpContents ( "dmemory", 0, 4); 		// p/ CRC32 e stringsearch
			//dumpContents ( "dmemory", 0, 32);		// p/ bitcount
			dumpContents ( "dmemory", 388, 392);	// p/ qsort 

			System.out.println ( "******************** Simulation number "+simNro+" - END **********");
		}
		//this.listState();
		f.delete ( );
	}

	protected boolean bFirstInstruction = true;
	protected boolean bBeginProgram = true;
	protected boolean bEndProgram = false;

	private int iTemp = 0;
	protected String mnemonicos [ ] = {	"add","sll","srl","sra","sllv","srlv","srav","jalr","jr","mult","multu","div",
//                                        0     1     2     3      4      5      6      7     8     9      10     11
										"divu","addu","sub","subu","and","or","xor","nor","slt","sltu","lw","sw",
//                                        12    13    14    15     16     17    18    19    20    21    22   23 
										"addi","andi","ori","beq","bltz", "bgtz", "blez", "bne",
//                                        24    25    26    27     28       29      30      31  
										"sllv", "srlv", "slti", "j", "jal", "nop", "BUBBLE", "cm", "lb", "sb", 
//                                        32      33      34     35   36     37       38     39    40    41
										"rem", "srli", "slli", "srai", "lwl", "lwr", "swl", "swr", "hlt" };
//  									42		43      44      45     "46"  "47"   "48"   "49"     "50"  
								//= { "ADD", "LD", "ST", "BR", "BUBBLE"};
	protected long intOpcodes [ ] = {	0,0,0,0,0,0,0,0,0,0,0,0,
										0,0,0,0,0,0,0,0,0,0,0x23,0x2b,
										0x08,0x0c,0x0d,0x04,0x01,0x07,0x06,0x05,
										0,0,0x0a, 0x02, 0x03, 0, 0, 0x3f, 0x20, 0x28, 
										0, 0x10, 0x11, 0x12, 0x22, 0x26, 0x2a, 0x2e, 0};
							  //= { 7, 1, 4, 30, -1};
	// sizeBytes sera' usado para guarda o campo Func
	protected int sizeBytes [ ] = {	32,0,2,3,4,6,7,9,8,0x18,0x19,0x1a,0x1b,0x21,0x22,0x23,0x24,0x25,0x26,0x27,0x2a,
									0x2b,0,0,0,0,0,0,0,0,0,0,0x04, 0x06,0,0, 0, 0, 0, 0, 0, 0, 0x28, 0, 0, 0, 0,0,0,0, 0x3f};
							//= { 1, 1, 1, 1, 1};
	protected int instrType [ ] = {	RTYPE,RTYPE,RTYPE,RTYPE,RTYPE,RTYPE, RTYPE,  RTYPE,RTYPE, RTYPE, RTYPE, RTYPE,
									RTYPE,RTYPE,RTYPE,RTYPE, RTYPE,  RTYPE,RTYPE,RTYPE,RTYPE,RTYPE,ITYPE,ITYPE,
									ITYPE,ITYPE,ITYPE,ITYPE,ITYPE,    ITYPE,  ITYPE,  ITYPE,
									RTYPE, RTYPE,  ITYPE,  JTYPE,JTYPE, RTYPE,  RTYPE,   JTYPE, ITYPE, ITYPE,
									RTYPE, ITYPE, ITYPE, ITYPE, ITYPE, ITYPE, ITYPE, ITYPE, RTYPE};

	protected String archRegs [ ] = {	"$zero","notUsed","$v0","$v1","$a0","$a1","$a2","$a3","$t0","$t1","$t2",
			"$t3","$t4","$t5","$t6","$t7","$s0","$s1","$s2","$s3","$s4","$s5","$s6",
			"$s7","$t8","$t9","notUsed","notUsed","$gp","$sp","$fp","$ra"};
	//private InstructionQueue currentIQueue;
	// rISA one line
	protected int rISAinstructions [] = { 0, 9, 20, 22, 23, 24, 25, 37};
	// 8 opcodes
	///*
	protected int[][] rISAinstructionsMatrix;/* = {	//{ 0, 9, 20, 22, 23, 24, 25, 31},
													//{ 0, 9, 16, 20, 22, 23, 24, 31},
													//{ 0, 9, 11, 22, 23, 24, 25, 27},
													//{ 0, 1, 2, 9, 24, 27, 40, 41},
													//{ 0, 22, 23, 27, 45, 46, 47, 48},
													{ 0, 9, 22, 23, 24, 40, 41, 44},	// generic
													//{ 0, 20, 22, 24, 25, 40, 41, 44},
													//{ 0, 18, 22, 23, 24, 34, 40, 44},
													//{ 0, 22, 23, 24, 34, 40, 41, 44},
													//{ 0, 18, 22, 23, 24, 25, 43, 44},
													//{ 0, 18, 22, 23, 24, 25, 27, 44},
													//{ 0, 20, 22, 23, 24, 27, 31, 34},
													//{ 0, 20, 22, 23, 24, 25, 35, 44},	// generic
													//{ 0, 22, 23, 24, 25, 34, 35, 44},
													{ 0, 22, 23, 24, 31, 34, 35, 44},	// stringsearch
													{ 0, 20, 22, 23, 24, 31, 35, 40},	// qsort
													{ 0, 16, 22, 23, 24, 31, 35, 40},	// bitcount
													{ 0, 16, 18, 22, 23, 24, 35, 31},	// CRC32
													{ 0, 16, 22, 23, 24, 25, 35, 40}	// lgbitcount
												};
	//*/
	// 16 opcodes
	/*
	protected int rISAinstructionsMatrix [][] = {	//{ 0, 9, 20, 22, 23, 24, 25, 31},
													//{ 0, 9, 16, 20, 22, 23, 24, 31},
													//{ 0, 9, 11, 22, 23, 24, 25, 27},
													//{ 0, 1, 2, 9, 24, 27, 40, 41},
													//{ 0, 22, 23, 27, 45, 46, 47, 48},
													//{ 0, 9, 20, 22, 23, 24, 31, 34, 35, 40, 41, 44},
													//{ 0, 20, 22, 24, 25, 40, 41, 44},
													//{ 0, 18, 22, 23, 24, 34, 40, 44},
													//{ 0, 20, 22, 23, 24, 31, 35, 34, 40, 41, 44},
													//{ 0, 18, 22, 23, 24, 25, 43, 44},
													//{ 0, 18, 22, 23, 24, 25, 27, 44},
													//{ 0, 20, 22, 23, 24, 27, 31, 34},
													//{ 0, 20, 22, 23, 24, 25, 31, 35, 40, 41, 44},
													//{ 0, 22, 23, 24, 25, 34, 35, 44},
													//{ 0, 20, 22, 23, 24, 27, 29, 31, 34, 35, 40, 41, 44, 50}, 	// stringsearch
													//{ 0, 20, 22, 23, 24, 27, 28, 30, 31, 34, 35, 40, 41, 44},	// qsort
													// 0, 16, 22, 23, 24, 25, 27, 31, 34, 35, 40, 45, 43, 44},	// bitcount
													{ 0, 16, 17, 18, 19, 22, 23, 24, 25, 31, 34, 35, 43, 44}	// *** CRC32
													... // lgbitcount
	};
	*/
	protected int regsForItypeInstr [];
	protected int iSizeRegsForItypeInstr;	
	protected int iNroRegsForItypeInstr;	

	boolean debugging = false;
}

