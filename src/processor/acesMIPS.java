package processor;

import java.lang.reflect.Array;

import ccomb.Circuit;

import message.MicroOperation;
import message.SetMsg;
import ports.Field;
import ports.FieldString;
import ports.SetPort;
import ports.Status;
import shell.Shell;
import simulator.Simulator;
import util.SistNum;
import control.ExecutionStage;
import control.Instruction;
import control.ExecutionPath;
import control.InstructionQueue;
import control.ItemInstructionQueue;
import control.QueuesOfInstructions;

/*******************************************************************************
 *	A SER CUSTOMIZADA PELO PROJETISTA.
 *	Implementa a classe principal do ambiente: a classe PROCESSOR.
 *	Insere atributos de STATUS (estado), FIELDS (campos - de instrucao) e
 *		STRINGS ( descricoes textuais).
 *
 *******************************************************************************/
public class acesMIPS extends Processor {

	public acesMIPS ( Simulator parSim) {
		super ( ".\\processors\\acesMIPS", parSim);
		
// Customizar aqui - BEGIN
		String [ ] asStatus = { "L1hit", "L2hit"};
		String [ ] asFields = { "FETCH-WIDTH"};
		String [ ] asStrings = { "NAME"};
		//
		/* Modif.MAC */
		String [ ] asFieldsInstructions = { "pc", "type", "OPCODE", "NR0", "NR1", "NR2", "NW0", "FIELD", "FUNC", "IMM", "ADDR", "op", "sel", "unit","OPR"};
		String [ ] asFieldsStrInstructions = { "DESCRIPTION", "mnem"};
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

		Instruction.defineFieldsOfInstructions ( asFieldsInstructions, asFieldsStrInstructions);
		setInstructionSet ( mnemonicos, intOpcodes, sizeBytes);
		processorQueues = new QueuesOfInstructions ( );
		
// Customizar aqui - BEGIN
		set ( "NAME", 	STRING, "acesMIPS");
		set ( "FETCH-WIDTH", FIELD, 2);
// - END
	}

/*******************************************************************************
 * TO DO:
 * Tem que verificar a existencia de instruções nas saidas da iCacheL1
 *******************************************************************************/
	private long [] fetch ( ) {		
		int fetch_width = (int) get ( "FETCH-WIDTH", FIELD);
		long [] aOpcodes = new long [ fetch_width];

		for ( int i = 0; i < fetch_width; i ++) {
			long lAddress = dtp.execute ( "pc", GET, 0, 0);
			dtp.execute ( "pc", READ);
			lAddress ++;
			dtp.execute ( "pc", SET, 0, 0, lAddress);
			dtp.execute ( "imemory", READ);
			aOpcodes [ i] = dtp.execute ( "imemory", GET, "S1", OUT);
		}
			
		return ( aOpcodes);
	}

/*******************************************************************************
 *	A SER FORNECIDA PELO PROJETISTA.
 *	Deve receber o retorno do metodo FETCH.
 *	O codigo a ser fornecido deve decodificar o argumento recebido e setar
 *		as propriedades STATUS e FIELD que foram definidos para um objeto Instruction.
 *		Deve fornecer tambem uma descricao para a instrucao (propriedade DESCRIPTION).
 *	CHAMA A DECODEINTERNAL OBRIGATORIAMENTE.
 *******************************************************************************/
	private Instruction decode ( long [] parOpcodes) {
		int i;
		long lPc = dtp.execute ( "pc", GET, 0, 0);
		lPc = lPc - get ( "FETCH-WIDTH", FIELD);
		
		for ( i = 0; i < Array.getLength( parOpcodes); i ++) {
			int iInst, iType=-1, iOpcode=-1, iNr1=-1, iNr2=-1, iNr3=-1, iNw=-1, iField=-1, iFunc=-1,iOpr=-1;
			int iOp=-1,iSel=-1, iImm=-1,iAddress=-1;
			boolean bTmp = false;
			
			Instruction it = new Instruction ( );
			iInst = new Long ( parOpcodes [ i]).intValue ( );
			iOpcode = SistNum.getBitRange ( iInst, 0, 5);
//System.out.println ( "iOpcode = "+iOpcode);
			// Inserir este teste para novos opcodes
			if ( iOpcode == 0 || iOpcode == 0x11) iType = RTYPE;
			//
			if ( iOpcode == 0x04 || iOpcode == 0x23 || iOpcode == 0x2b || iOpcode == 0x08) iType = ITYPE;
			if ( iOpcode == 0x09 || iOpcode == 0x0c || iOpcode == 0x0d || iOpcode == 0x0e) iType = ITYPE;
			if ( iOpcode == 0x0f || iOpcode == 0x0a || iOpcode == 0x0b || iOpcode == 0x01) iType = ITYPE;
			if ( iOpcode == 0x07 || iOpcode == 0x06 || iOpcode == 0x05) iType = ITYPE;
			//
			if ( iOpcode == 0x02 || iOpcode == 0x03) iType = JTYPE;
			
			switch ( iType) {
				case ITYPE:
					iNr1 = SistNum.getBitRange ( iInst, 6, 10);
					iNw = SistNum.getBitRange ( iInst, 11, 15);
					iAddress = SistNum.getBitRange ( iInst, 16, 31);
					iImm = iAddress;
					iSel = 1;
					switch ( iOpcode) {
						case 0x23: // lw
							iNr2 = iNr1;
							it.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\instructions\\Load");
							it.set ( "mnem",STRING,"lw");
							it.set ( "unit", FIELD, DMEMORY);
							break;
						case 0x2b: // sw
							iNr1 = iNw;
							it.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\instructions\\Store");
							it.set ( "mnem",STRING,"sw");
							it.set ( "unit", FIELD, DMEMORY);
							break;
						case 0x04: // beq
						case 0x01: // bgez ou bgezal ou bltzal
						case 0x07: // bgtz
						case 0x06: // blez
						case 0x05: // bne
							iSel = 0;
							iNr2 = iNw;
							it.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\instructions\\Compare");					
							it.set ( "unit", FIELD, COMPARATOR);
							if ( iOpcode == 0x04) {
								iOpr = EQUAL;
								it.set ( "mnem",STRING,"beq");
							} else if ( iOpcode == 0x01 && iNw == 1) {
								iOpr = GREATERTHEN_OR_EQUAL_ZERO;
								it.set ( "mnem",STRING,"bgez");
								iNw = -1;
							} else if ( iOpcode == 0x01 && iNw == 0x11) {
								iOpr = GREATERTHEN_OR_EQUAL_ZERO;
								it.set ( "mnem",STRING,"bgezal");
								iNw = 31;
							} else if ( iOpcode == 0x07 && iNw == 0) {
								iOpr = GREATERTHEN_OR_EQUAL_ZERO;
								it.set ( "mnem",STRING,"bgtz");
								iNw = -1;
							} else if ( iOpcode == 0x06 && iNw == 0) {
								iOpr = LESSTHEN_OR_EQUAL_ZERO;
								it.set ( "mnem",STRING,"blez");
								iNw = -1;
							} else if ( iOpcode == 0x01 && iNw == 0x10) {
								iOpr = LESSTHEN_ZERO;
								it.set ( "mnem",STRING,"bltzal");
								iNw = 31;
							} else if ( iOpcode == 0x01 && iNw == 0x00) {
								iOpr = LESSTHEN_ZERO;
								it.set ( "mnem",STRING,"bltz");
								iNw = -1;
							} else if ( iOpcode == 0x05) {
								iOpr = NOTEQUAL;
								it.set ( "mnem",STRING,"bne");
							}
							break;
						case 0x08: // addi
						case 0x09: // addiu
						case 0x0c: // andi
						case 0x0d: // ori
						case 0x0e: // xori
						case 0x0f: // lui
						case 0x0a: // slti
						case 0x0b: // sltiu
							it.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\instructions\\AeL");
							it.set ( "unit", FIELD, ALU);
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
								it.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\instructions\\Compare");
								it.set ( "unit", FIELD, COMPARATOR);
								iOp = LESSTHAN;
								it.set ( "mnem",STRING,"slti");
							} else if ( iOpcode == 0x0b) {
								it.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\instructions\\Compare");
								it.set ( "unit", FIELD, COMPARATOR);
								iOp = LESSTHAN;
								it.set ( "mnem",STRING,"sltiu");
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
					iSel = 0;
					switch ( iOpcode) {
						case 0: // Aritmeticas e Logicas
							switch ( iFunc) {
								case 0x00: // sll
								case 0x02: // srl
								case 0x03: // sra
								case 0x04: // sllv
								case 0x06: // srlv
								case 0x07: // srav
								case 0x09: // jalr
								case 0x08: // jr
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
									it.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\instructions\\AeL");
									it.set ( "unit", FIELD, ALU);
									iOp = ADD;
									if ( iFunc == 0x3f) {	/* Modif.MAC - begin */
										iNr3 = iField;
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
										int iTmp = iNr1; iNr1 = iNr2; iNr2 = iTmp; // ver descricao da instrucao
										iImm = iField;
										iSel = 1;
										iOp = SLL;
										it.set ( "mnem",STRING,"sll");
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
										iSel = 1;
										iOp = SLR;
										it.set ( "mnem",STRING,"srl");
									} else if ( iFunc == 0x03) {
										int iTmp = iNr1; iNr1 = iNr2; iNr2 = iTmp; // ver descricao da instrucao
										iImm = iField;
										iSel = 1;
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
										it.set ( "unit", FIELD, ALU);		/* Modif.MULT and others */	
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
										it.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\instructions\\Compare");
										it.set ( "unit", FIELD, COMPARATOR);
										iOp = LESSTHAN;
										it.set ( "mnem",STRING,"slt");
									} else if ( iFunc == 0x2b) {
										it.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\instructions\\Compare");
										it.set ( "unit", FIELD, COMPARATOR);
										iOp = LESSTHAN;
										it.set ( "mnem",STRING,"sltu");
									} else if ( iFunc == 0x08 || iFunc == 0x09) {
										it.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\instructions\\Compare");
										it.set ( "unit", FIELD, COMPARATOR);
										iNr2 = iNr1;		// nao usado realmente
										iOp = EQUAL;		// nao usado realmente
										if ( iFunc == 0x09) it.set ( "mnem",STRING,"jalr");
										else it.set ( "mnem",STRING,"jr");
									}
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
							it.set ( "unit", FIELD, FPU);
							iOp = 10;
							break;
					}
					break;

				case JTYPE:
					it.set ( "DESCRIPTION",STRING,Processor.getProcessorName()+"\\instructions\\Compare");
					it.set ( "unit", FIELD, COMPARATOR);
					iAddress = SistNum.getBitRange ( iInst, 7, 31);
					iNr1 = 0;		// nao usado realmente
					iNr2 = 0;		// nao usado realmente
					iOp = EQUAL;	// nao usado realmente
					iSel = 0;
					if ( iOpcode == 0x02) {
						it.set ( "mnem",STRING,"j");
					} else if ( iOpcode == 0x03) {
						it.set ( "mnem",STRING,"jal");
					}
					break;

				default:
					break;
			}

			it.set ( "pc", FIELD, lPc++);
			it.set ( "OPCODE", FIELD, iOpcode);
			it.set ( "type", FIELD, iType);
			it.set ( "NR0", FIELD, iNr1);
			it.set ( "NR1", FIELD, iNr2);
			/* Modif.MAC */
			it.set ( "NR2", FIELD, iNr3);
			it.set ( "NW0", FIELD, iNw);
			it.set ( "FIELD", FIELD, iField);
			it.set ( "FUNC", FIELD, iFunc);
			it.set ( "op",  FIELD, iOp);
			it.set ( "OPR",  FIELD, iOpr);
			it.set ( "sel",  FIELD, iSel);
			it.set ( "IMM", FIELD, iImm);
			it.set ( "ADDR",  FIELD, iAddress);

			if (bTmp) it.list ( );

			if ( bEndProgram == false && iOpcode != 63) {
				decodeInternal ( it);
				currentIQueue = processorQueues.search ( "fetched");
				currentIQueue.add ( it);
				currentIQueue = processorQueues.search ( "all");
				currentIQueue.add ( it);
			} else bEndProgram = true;
			
		}
		//
		//
		return ( null);
	}

	private void executePutAddress ( int iAddress) {
		dtp.execute ( "atmp", SET, "E1", IN, iAddress);
		dtp.execute ( "atmp", WRITE);
		dtp.execute ( "atmp", READ);
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
		int iUnit;

		iUnit = (int) parIt.get ( "unit", FIELD);
		if ( mdAux.getComponentName().compareToIgnoreCase("aluGeneral")==0) {
			if ( iUnit == ALU1) mdAux.setComponentName( "alu1_ex");
			else if ( iUnit == ALU2) mdAux.setComponentName( "alu2_ex");
		}
		if ( mdAux.getComponentName().compareToIgnoreCase("mxGeneral")==0) {
			if ( iUnit == ALU1) mdAux.setComponentName( "mx1");
			else if ( iUnit == ALU2) mdAux.setComponentName( "mx2");
			else if ( iUnit == COMPARATOR) mdAux.setComponentName( "mx3");
			long lImmed = parIt.get ( "IMM", FIELD);
			if ( lImmed != -1) {
				if ( iUnit == ALU1) dtp.execute ( "mx1", SET, "E1", IN, lImmed);
				else if ( iUnit == ALU2) dtp.execute ( "mx2", SET, "E1", IN, lImmed);
				else if ( iUnit == COMPARATOR) dtp.execute ( "mx3", SET, "E1", IN, lImmed);
			}
		}
		
		int iMethod = mdAux.getMethodId ( );
		switch ( iMethod) {
			case BEHAVIOR:
				break;
			case READ:
				if ( mdAux.getComponentName().compareToIgnoreCase("dCacheL1")==0) {
					int iAddress = (int) parIt.get ( "ADDR", FIELD);
					executePutAddress ( iAddress);
				}
				break;
			case WRITE:
				if ( mdAux.getComponentName().compareToIgnoreCase("dCacheL1")==0) {
					int iAddress = (int) parIt.get ( "ADDR", FIELD);
					executePutAddress ( iAddress);
				}
				break;
			case PROPAGATE:
				break;
			case SET:
				//mdAux.debug();
				String sPortName = mdAux.getPortName ( );
				if ( parIt.exist ( sPortName, FIELD)) {
					int iActualValue = (int) parIt.get ( sPortName, FIELD);
					mdAux.setPortValue ( iActualValue);
				}
				if ( sPortName.compareToIgnoreCase( "NR0")==0) {
					if ( iUnit == ALU1) mdAux.setPortName ( "NR0");
					else if ( iUnit == ALU2) mdAux.setPortName ( "NR2");
					else if ( iUnit == DMEMORY) mdAux.setPortName ( "NR4");
					else if ( iUnit == COMPARATOR) mdAux.setPortName ( "NR6");
				}
				if ( sPortName.compareToIgnoreCase( "NR1")==0) {
					if ( iUnit == ALU1) mdAux.setPortName ( "NR1");
					else if ( iUnit == ALU2) mdAux.setPortName ( "NR3");		
					else if ( iUnit == DMEMORY) mdAux.setPortName ( "NR5");
					else if ( iUnit == COMPARATOR) mdAux.setPortName ( "NR7");
				}
				if ( sPortName.compareToIgnoreCase( "NR2")==0) { 	/* Modif.MAC - begin */
					if ( iUnit == ALU1) mdAux.setPortName ( "NR9");
					else if ( iUnit == ALU2) mdAux.setPortName ( "NR10");		
				}													/* Modif.MAC - end */
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
//	System.out.println ( "Executa...");
							execute ( mdAux);
						}
					}
				}
			}
		}
		
	/*******************************************************************************
	*	A SER FORNECIDA PELO PROJETISTA.
	*	Deve prever a inicializacao do processador
	*******************************************************************************/
	private void initialize ( ) {
		//
		String [] asStr = { "mnem"};
		String [] asTmp = { "type", "OPCODE", "unit"};
		InstructionQueue iq = new InstructionQueue ( "fetched");
		iq.setFieldsOfItem( asTmp, asStr);
		processorQueues.add ( iq);
		//iq.list ( );
		//
		String [] asTmp2 = { "pc", "value", "NW0", "unit"};
		iq = new InstructionQueue ( "writeback");
		iq.setFieldsOfItem( asTmp2, asStr);
		processorQueues.add ( iq);
		//iq.list ( );
		//
		String [] asTmp3 = { "pc", "OPCODE", "value", "NW0", "NR0", "NR1", "unit"};
		iq = new InstructionQueue ( "all");
		iq.setFieldsOfItem( asTmp3, asStr);
		processorQueues.add ( iq);
		//
		ExecutionPath pPipeMem = sePipes.search ( "memory");
		if ( pPipeMem.search( "L2STAGE0") != null) sFirstStageAfterL1 = "L2STAGE0";
		else sFirstStageAfterL1 = "MMSTAGE0";
		//
		sFirstStageAfterL1 = pPipeMem.getNameNext ( "L1EXECUTE");
System.out.println ( "sFirstStageAfterL1 =" + sFirstStageAfterL1);
		sLastStageOfL2 = pPipeMem.getNamePrev ( "MMSTAGE0");
System.out.println ( "sLastStageOfL2 =" + sLastStageOfL2);
	}

	private void executeWriteHiLo ( int i0, int i1) {
		dtp.execute ( "lo", SET, "E1", IN, i0);
		dtp.execute ( "lo", WRITE);
		dtp.execute ( "hi", SET, "E1", IN, i1);
		dtp.execute ( "HI", WRITE);		
	}
	
/*******************************************************************************
 *	A SER FORNECIDA PELO PROJETISTA.
 *	Deve percorrer os estagios de pipeline executando as instrucoes.
 *	Ha metodos pre-definidos que podem ser chamados.
 *	Deve prever a inicializacao do processador.
 *******************************************************************************/
	private void execute ( ) {
		ExecutionPath pPipeAlu1 = sePipes.search ( "alu1");
		ExecutionPath pPipeAlu2 = sePipes.search ( "alu2");	
		ExecutionPath pPipeMem = sePipes.search ( "memory");
		ExecutionPath pPipeBr = sePipes.search ( "branch");
		ExecutionPath pPipeFP = sePipes.search ( "fp");
		ExecutionPath pPipeComp = sePipes.search ( "compare");
		
		if ( pPipeAlu1.getTimeMode() == PIPELINED) executeBackward ( "alu1");
		else executeForward ( "alu1");
		if ( pPipeAlu2.getTimeMode() == PIPELINED) executeBackward ( "alu2");
		else executeForward ( "alu2");
		if ( pPipeMem.getTimeMode() == PIPELINED) executeBackward ( "memory");
		else executeForward ( "memory");
		if ( pPipeBr.getTimeMode() == PIPELINED) executeBackward ( "branch");
		else executeForward ( "branch");
		if ( pPipeFP.getTimeMode() == PIPELINED) executeBackward ( "fp");
		else executeForward ( "fp");
		if ( pPipeComp.getTimeMode() == PIPELINED) executeBackward ( "compare");
		else executeForward ( "compare");
		//
		Instruction it;
		ItemInstructionQueue iiq;
		long lValue;
		
		pPipeCurrent = sePipes.search ( "alu1");
		if ( ( it = pPipeCurrent.getCurrentInst( "ALU1EXECUTE")) != null) {
			String sAux = it.getString( "mnem", STRING);
			if ( 	sAux.compareToIgnoreCase("mult") == 0 || sAux.compareToIgnoreCase("multu") == 0 ||
					sAux.compareToIgnoreCase("div") == 0 || sAux.compareToIgnoreCase("divu") == 0) {
				lValue = dtp.execute ( "alu1_ex", GET, "S1", OUT);
				int [] iValue = SistNum.splitDouble(lValue);
				executeWriteHiLo ( iValue [0], iValue [1]);
			} else { 
				currentIQueue = processorQueues.search ( "writeback");
				iiq = currentIQueue.add ( it);
				lValue = dtp.execute ( "alu1_ex", GET, "S1", OUT);
				iiq.set ( "value", FIELD, lValue);
			}
		}

		pPipeCurrent = sePipes.search ( "alu2");
		if ( ( it = pPipeCurrent.getCurrentInst( "ALU2EXECUTE")) != null) {
			String sAux = it.getString( "mnem", STRING);
			if ( 	sAux.compareToIgnoreCase("mult") == 0 || sAux.compareToIgnoreCase("multu") == 0 ||
					sAux.compareToIgnoreCase("div") == 0 || sAux.compareToIgnoreCase("divu") == 0) {
				lValue = dtp.execute ( "alu2_ex", GET, "S1", OUT);
				int [] iValue = SistNum.splitDouble(lValue);
				executeWriteHiLo ( iValue [0], iValue [1]);
			} else { 
				currentIQueue = processorQueues.search ( "writeback");
				iiq = currentIQueue.add ( it);
				lValue = dtp.execute ( "alu2_ex", GET, "S1", OUT);
				iiq.set ( "value", FIELD, lValue);
			}
		}
		
		//
		// SISTEMA DE MEMORIA - Leituras - INICIO
		//
		pPipeCurrent = sePipes.search ( "memory");
		if ( ( it = pPipeCurrent.getCurrentInst( "L1EXECUTE")) != null) {
			Circuit cDmem = dtp.search( "dCacheL1");
			if ( cDmem.getMethod ( ) == READ) {
				pPipeCurrent.insert ( null, "L1EXECUTE");
				if ( get ( "L1hit", STATUSorCONF) == 1) {
System.out.println ( "Hit na cache L1 de dados");
					currentIQueue = processorQueues.search ( "writeback");
					iiq = currentIQueue.add ( it);
					lValue = dtp.execute ( "dCacheL1", GET, "S1", OUT);
					iiq.set ( "value", FIELD, lValue);
				} else {
					it.set( "unit", FIELD, L2MAIN);
					currentIQueue = processorQueues.search ( "fetched");
					currentIQueue.addAtFirst ( it);
					System.out.println ( "Miss na cache L1 de dados");	
				}
			} else if ( cDmem.getMethod ( ) == WRITE) {
				pPipeCurrent.insert ( null, "L1EXECUTE");
				if ( get ( "L1hit", STATUSorCONF) == 1) {
System.out.println ( "Hit na cache L1 de dados ao escrever");
				} else {
					it.set( "unit", FIELD, L2MAIN);
					currentIQueue = processorQueues.search ( "fetched");
					currentIQueue.addAtFirst ( it);
System.out.println ( "Miss na cache L1 de dados ao escrever");	
				}				
			}
		}
		if ( ( it = pPipeCurrent.getCurrentInst( sLastStageOfL2)) != null) {
System.out.println ( "Instrucao referente a cache L2");
			Circuit cDmem = dtp.search( "uCacheL2");
			if ( cDmem.getMethod ( ) == READ) {
				if ( get ( "L2hit", STATUSorCONF) == 1) {
System.out.println ( "Hit na cache L2 unificada");
					currentIQueue = processorQueues.search ( "writeback");
					iiq = currentIQueue.add ( it);
					lValue = dtp.execute ( "uCacheL2", GET, "S1", OUT);
					iiq.set ( "value", FIELD, lValue);
					pPipeCurrent.insert ( null, sLastStageOfL2);
System.out.println ( "lValue = "+lValue);
					dtp.execute ( "dCacheL1", SET, "E1", IN, lValue);
					dtp.execute ( "dCacheL1", WRITE);
				} else {
System.out.println ( "Miss na cache L2 unificada");	
				}
			} else if ( cDmem.getMethod ( ) == WRITE) {
				if ( get ( "L2hit", STATUSorCONF) == 1) {
					pPipeCurrent.insert ( null, sLastStageOfL2);
System.out.println ( "Hit na cache L2 de dados ao escrever");
				} else {
System.out.println ( "Miss na cache L2 de dados ao escrever");	
				}					
			}
		}
		if ( ( it = pPipeCurrent.getCurrentInst( "MMSTAGE100")) != null) {
System.out.println ( "Instrucao referente a main memory");
			Circuit cDmem = dtp.search( "MainMem");
			if ( cDmem.getMethod ( ) == READ) {
				currentIQueue = processorQueues.search ( "writeback");
				iiq = currentIQueue.add ( it);
				lValue = dtp.execute ( "MainMem", GET, "S1", OUT);
				iiq.set ( "value", FIELD, lValue);
				dtp.execute ( "dCacheL1", SET, "E1", IN, lValue);
				dtp.execute ( "dCacheL1", WRITE);
				dtp.execute ( "uCacheL2", SET, "E1", IN, lValue);
				dtp.execute ( "uCacheL2", WRITE);
			} else if ( cDmem.getMethod ( ) == WRITE) {
				
			}
		}
		//
		// SISTEMA DE MEMORIA - Leituras - Fim
		//
		
		pPipeCurrent = sePipes.search ( "fp");
		if ( ( it = pPipeCurrent.getCurrentInst( "FPEXECUTE")) != null) {
			currentIQueue = processorQueues.search ( "writeback");
			iiq = currentIQueue.add ( it);
			lValue = (long) dtp.execute ( "FPUnit", GET, "S0", OUT);
			iiq.set ( "value", FIELD, lValue);
		}
		
		pPipeCurrent = sePipes.search ( "compare");
		if ( ( it = pPipeCurrent.getCurrentInst( "COMPEXECUTE")) != null) {
			lValue = dtp.execute ( "comp", GET, "S0", OUT);
			long lOpcode = it.get ( "OPCODE", FIELD);
			if ( 	lOpcode == 0x04/*beq*/ || lOpcode == 0x01/*bgez e outros*/ || lOpcode == 0x05/*bne*/ ||
					lOpcode == 0x07/*bgtz*/ || lOpcode == 0x06/*blez*/ || lOpcode == 0x02 || 
					lOpcode == 0x03 || lOpcode == 0x00) {
				if ( lValue == 1) {
					long lAddress = it.get ( "pc", FIELD);
					long lNw = it.get ( "NW0", FIELD);
					long lAddrNext = lAddress + 1;
					//
					if ( lOpcode == 0x02 /*j*/ || lOpcode == 0x03) lAddress = it.get ( "ADDR", FIELD);
					else if ( lOpcode == 0x00 /*jalr*/) lAddress = dtp.execute ( "comp", GET, "E0", IN); // valor do registrador lido
					else lAddress = lAddress + it.get ( "ADDR", FIELD);
					//
					if ( lOpcode == 0x03) dtp.execute ( "$ra", SET, 0, 0, lAddrNext);
					//
					dtp.execute ( "pc", SET, 0, 0, lAddress);
					if ( lNw == 31 && lOpcode == 0x01) { // bgezal ou bltzal
						currentIQueue = processorQueues.search ( "writeback");
						iiq = currentIQueue.add ( it);
						iiq.set ( "value", FIELD, lAddrNext);						
					} else if  ( lOpcode == 0x00) { // jalr, jr
						currentIQueue = processorQueues.search ( "writeback");
						iiq = currentIQueue.add ( it);
						iiq.set ( "value", FIELD, lAddrNext);						
					}
				}				
			} else {
				currentIQueue = processorQueues.search ( "writeback");
				iiq = currentIQueue.add ( it);
				iiq.set ( "value", FIELD, lValue);
			}
		}
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

		if ( bBeginProgram == true) {
			//System.out.println ( "Initialize");
			initialize ( );
			bBeginProgram = false;
		}

		if ( bEndProgram == false) {
			//System.out.println ( "Fetch");
			long [] aOpcodes = fetch ( );
			//System.out.println ( "Decode");
			decode ( aOpcodes);
		}

		//System.out.println ( "dispatch");
		dispatch ( );
		//System.out.println ( "execute");
		// Geracao randomica de hits nos niveis de cache
		double dNro = Math.random ( );
		int iFlagHitL1, iFlagHitL2;
		if ( dNro > 0.5) iFlagHitL1 = 1;
		else iFlagHitL1 = 0;
		dNro = Math.random ( );
		if ( dNro > 0.5) iFlagHitL2 = 1;
		else iFlagHitL2 = 0;
		set ( "L1hit", STATUSorCONF, iFlagHitL1);
		//set ( "L1hit", STATUS, 1);
		set ( "L2hit", STATUSorCONF, iFlagHitL1);
		//
		execute ( );
		//System.out.println ( "writeResults");
		writeResults ( );
		//System.out.println ( "advanceTime");
		sSim.advanceTime ( );

		return ( bEndProgram);
	}

	private void dispatch ( ) {
		ExecutionPath pPipeAlu1 = sePipes.search ( "alu1");
		ExecutionPath pPipeAlu2 = sePipes.search ( "alu2");	
		ExecutionPath pPipeMem = sePipes.search ( "memory");
		ExecutionPath pPipeBr = sePipes.search ( "branch");
		ExecutionPath pPipeFP = sePipes.search ( "fp");
		ExecutionPath pPipeComp = sePipes.search ( "compare");
		boolean bNotUsedAlu1 = true, bNotUsedAlu2 = true, bNotUsedMem = true;
		boolean bNotUsedBr = true, bNotUsedFP = true, bNotUsedComp = true;
		currentIQueue = processorQueues.search ( "fetched");
		Instruction iTmp;
		InstructionQueue notDispatched = new InstructionQueue ( "tmp");

		while ( ( iTmp = currentIQueue.getInstruction ( )) != null) {
			
			if ( iTmp.get( "unit", FIELD) == ALU) {
				if ( pPipeAlu1 != null) {
					if ( pPipeAlu1.walk( iTmp, "ALU1READ") == false) {
						notDispatched.add  ( iTmp);
					} else iTmp.set ( "unit", FIELD, ALU1);
					currentIQueue.removeInstruction ( );
					bNotUsedAlu1 = false;
					pPipeAlu1 = null;
				} else if ( pPipeAlu2 != null) {			// Modif.MULT and others - begin
					if ( pPipeAlu2.walk( iTmp, "ALU2READ") == false) {
						notDispatched.add  ( iTmp);
					} else iTmp.set ( "unit", FIELD, ALU2);
					currentIQueue.removeInstruction ( );
					bNotUsedAlu2 = false;
					pPipeAlu2 = null;
				} else {									/* Modif.MULT and others - end */
					notDispatched.add  ( iTmp);
					currentIQueue.removeInstruction ( );
				}
			} else if ( iTmp.get( "unit", FIELD) == DMEMORY) {
				if ( pPipeMem != null) {
					if ( pPipeMem.walk( iTmp, "L1READ") == false) {
						notDispatched.add  ( iTmp);
					}
					currentIQueue.removeInstruction ( );
					bNotUsedMem = false;
					pPipeMem = null;
				} else {
					notDispatched.add  ( iTmp);
					currentIQueue.removeInstruction ( );
				}
			} else if ( iTmp.get( "unit", FIELD) == COMPARATOR) {
				if ( pPipeComp != null) {
					if ( pPipeComp.walk( iTmp, "COMPREAD") == false) {
						notDispatched.add  ( iTmp);
					}
					currentIQueue.removeInstruction ( );
					bNotUsedComp = false;
					pPipeComp = null;
				} else {
					notDispatched.add  ( iTmp);
					currentIQueue.removeInstruction ( );
				}
			} else if ( iTmp.get( "unit", FIELD) == FPU) {
				if ( pPipeFP != null) {
					if ( pPipeFP.walk( iTmp, "FPREAD") == false) {
						notDispatched.add  ( iTmp);
					}
					currentIQueue.removeInstruction ( );
					bNotUsedFP = false;
					pPipeFP = null;
				} else {
					notDispatched.add  ( iTmp);
					currentIQueue.removeInstruction ( );
				}
			} else if ( iTmp.get( "unit", FIELD) == ALU2) {	/* Modif.MULT and others - begin */
				if ( pPipeAlu2 != null) {
					if ( pPipeAlu2.walk( iTmp, "ALU2READ") == false) {
						notDispatched.add  ( iTmp);
					}
					currentIQueue.removeInstruction ( );
					bNotUsedAlu2 = false;
					pPipeAlu2 = null;
				} else {
					notDispatched.add  ( iTmp);
					currentIQueue.removeInstruction ( );
				}
			}												/* Modif.MULT and others - end */
			else if ( iTmp.get( "unit", FIELD) == L2MAIN) {
				if ( pPipeMem != null) {
					if ( pPipeMem.walk( iTmp, sFirstStageAfterL1) == false) {
						notDispatched.add  ( iTmp);
					}
					currentIQueue.removeInstruction ( );
					bNotUsedMem = false;
					pPipeMem = null;
				} else {
					notDispatched.add  ( iTmp);
					currentIQueue.removeInstruction ( );
				}		
			}	
		}

		//notDispatched.list ( );
		while ( ( iTmp = notDispatched.getInstruction ( )) != null) {
			currentIQueue.add ( iTmp);
			notDispatched.removeInstruction ( );
		}
	
//System.out.println ( "Vou inserir nulls para nao usados!!!"+bNotUsedAlu1+","+bNotUsedAlu2);
		
		if ( bNotUsedAlu1) pPipeAlu1.walk( null, "ALU1READ");
		if ( bNotUsedAlu2) pPipeAlu2.walk( null, "ALU2READ");
		if ( bNotUsedMem) pPipeMem.walk( null, "L1READ");
		if ( bNotUsedBr) pPipeBr.walk( null, "BRREAD");
		if ( bNotUsedFP) pPipeFP.walk( null, "FPREAD");
		if ( bNotUsedComp) pPipeComp.walk( null, "COMPREAD");
	}

	private void writeResults ( ) {
		ItemInstructionQueue iiqTmp;
		int iNw0, iUnit;
		long lValue;
		String sPortC=null, sPortE=null;

		currentIQueue = processorQueues.search ( "writeback");
		iiqTmp = currentIQueue.removeItem ( );
		if ( iiqTmp == null) return;
		
		iNw0 = (int) iiqTmp.get ( "NW0", FIELD);
		iUnit = (int) iiqTmp.get ( "UNIT", FIELD);
		lValue = iiqTmp.get ( "value", FIELD);
		if ( iUnit == ALU1) sPortC = "NW0";
		else if ( iUnit == ALU2) sPortC = "NW1";
		else if ( iUnit == DMEMORY || iUnit == L2MAIN) sPortC = "NW2";
		else if ( iUnit == COMPARATOR) sPortC = "NW3";
		if ( iUnit == ALU1) sPortE = "E0";
		else if ( iUnit == ALU2) sPortE = "E1";
		else if ( iUnit == DMEMORY || iUnit == L2MAIN) sPortE = "E2";
		else if ( iUnit == COMPARATOR) sPortE = "E3";
		
		if ( iUnit == ALU1 || iUnit == ALU2 || iUnit == DMEMORY || iUnit == COMPARATOR || iUnit == L2MAIN) {
			dtp.execute ( "GPRFile", SET, sPortC, CONTROL, iNw0);
			dtp.execute ( "GPRFile", SET, sPortE, IN, lValue);
			dtp.execute ( "GPRFile", WRITE);
		} else if ( iUnit == FPU) {
			dtp.execute ( "FPRFile", SET, "NW0", CONTROL, iNw0);
			dtp.execute ( "FPRFile", SET, "E0", IN, lValue);
			dtp.execute ( "FPRFile", WRITE);			
		}
	}
	
	public String getMnemonico ( long lOpcode) {
		int i;
		String s = "-";
		int iOpcode = ( int) lOpcode;

		iOpcode = SistNum.getBitRange ( iOpcode, 0, 5);

		if ( false) {
			for ( i = 0; i < intOpcodes.length; i ++) {
				System.out.println ( intOpcodes [ i]);
			}
		}
		
		for ( i = 0; i < intOpcodes.length; i ++) {
			if ( iOpcode == intOpcodes [ i]) break;
		}
		
		if ( i == intOpcodes.length) return s;
		else return ( mnemonicos [ i]);
	}

	public void setReset ( String parProgram) throws Exception {
		String sProgram = null;
		Simulator SIM_NEW = new Simulator ( );
		acesMIPS 	am 	= new acesMIPS 	( SIM_NEW);
		Shell.initialize ( "# acesMIPS> ",		SIM_NEW,( Processor) am);
		if ( parProgram == null) sProgram = "Program.bin";
		else sProgram = parProgram;
		System.out.println ( "file name: "+sProgram);
		am.initialize ( 	"acesMIPSorg.txt",sProgram,	"InstructionSet.txt","ExecutionPaths.txt");
		SIM_NEW.resetTime ( );
		SIM_NEW.Initializations ( am);
		bFirstInstruction = true;
		bBeginProgram = false;
		// a initialize precisa da referencia ao novo processor
		am.initialize ( );
		bEndProgram = false;
		Shell.decodifica ( "reset acesMIPS");
	}

	private boolean bFirstInstruction = true;
	private boolean bBeginProgram = true;
	private boolean bEndProgram = false;

	private int iTemp = 0;
	private String mnemonicos [ ] = { "lw", "sw", "beq", "addi", "addiu", "andi", "ori", "alu", "fp", "halt", "xori"};
	private long intOpcodes [ ] = { 0x23, 0x2b, 0x04, 0x08, 0x09, 0x0c, 0x0d, 0x00, 0x11, 0x3f, 0x0e};
	private int sizeBytes [ ] = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
	
	private ExecutionPath pPipeCurrent;
	private InstructionQueue currentIQueue;
	
	private final int ALU 			= 0;
	private final int ALU1			= 1;
	private final int ALU2			= 2;
	private final int DMEMORY 		= 3;
	private final int COMPARATOR	= 4;
	private final int FPU			= 5;
	private final int L2MAIN		= 6;
	
	private String sFirstStageAfterL1 = null, sLastStageOfL2 = null;
}
