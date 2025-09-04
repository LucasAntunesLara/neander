/*
 * Created on 06/02/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 * 
 * TODO: become possible the creation of blocks without nops
 * 
 */
package montador;

import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.StringTokenizer;

import processor.Processor;
import processor.RodadaDeSimulacaoRisa;
import processor.MIPS;

import util.Define;
import util.SistNum;

import montador.MontadorMIPS;
import primitive.Primitive;

class NormalBinaryInstruction {
	LineOfCodeDLX locOrigin;
	int iAddress;
	boolean bTranslatable;
	boolean bBeginOfBlock;
	int iSizeBlock;
	// int iWhichRisa;
	
	public NormalBinaryInstruction ( int address, LineOfCodeDLX loc) {
		iAddress = address;
		// iWhichRisa = -1;
		locOrigin = loc;
	}
	
	public static void setProcessor ( Processor proc) {
		mips = proc;
	}
	
	public void list ( ) {
String sMnem = mips.getMnemonico ( locOrigin.instruction);
//TDBenchApp.messagesToUser ( null, line+"\t,\t"+lineNumber+"\t,\t"+type+"\t,\t"+argument+"\t,\t"+bytesInLine+"\t,\t"+messageError+"\t,\t"+label);
if (sMnem.startsWith("b") || sMnem.startsWith("j")) System.out.println ( "BRANCH!");
System.out.println ( "NInst "+iAddress+" opc.="+sMnem+", transl.="+bTranslatable+", begin="+bBeginOfBlock+", size="+iSizeBlock+(locOrigin.isNewFunction ? ", new function "+locOrigin.methodName:"")+", method="+locOrigin.methodID+", which rISA="+locOrigin.iWhichRisa);
	}
	
	private static Processor mips;
}

/**
 * @author Owner
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ASMrISAforMIPS implements Define {

	public ASMrISAforMIPS ( Processor proc) {
		mips = proc;
		htNInstructions = new Hashtable ( );
		htRISA = new Hashtable ( );
		NormalBinaryInstruction.setProcessor ( proc);
		iCont = 0;
		regsUsed = new boolean [ 32];
		for ( int i = 0; i < 32; i ++) regsUsed [ i] = false;
		//htCodeLabels = new Hashtable ( );
		htFinalCode = new Hashtable ( );
		// add Nops to the end of the blocks of reduced instructions?
		bAddNops = true;
	}

	public void setListOfLabels ( ListOfLabels listOfLabels) {
		llCodeLabels = listOfLabels;
//llCodeLabels.list();
	}
	
	public void addOpcode ( LineOfCodeDLX loc) {
		htNInstructions.put ( new Integer (iCont), new NormalBinaryInstruction ( iCont, loc));
		iCont ++;
	}
	
	public void delimitFunctions ( ) {
		NormalBinaryInstruction instDescr;
		int iMethod = -1;

		for ( int i = 0; i < htNInstructions.size(); i ++) {
			instDescr = (NormalBinaryInstruction) htNInstructions.get ( new Integer (i));
			if ( iMethod != -1 && iMethod != instDescr.locOrigin.methodID) {
				instDescr.locOrigin.isNewFunction = true;
			} else {
				if ( i == 0) instDescr.locOrigin.isNewFunction = true;	// primeira instruçao da main
			}
			iMethod = instDescr.locOrigin.methodID;
//instDescr.list();
		}
	}

	public void delimitForcedRisaBlocks ( ) {
		NormalBinaryInstruction instDescr, antInstDescr = null;
		boolean insideBlock = false;

		for ( int i = 0; i < htNInstructions.size(); i ++) {
			instDescr = (NormalBinaryInstruction) htNInstructions.get ( new Integer (i));
			if ( instDescr.locOrigin.iForcedRisa != -1 && ! insideBlock) {
				insideBlock = true;
				instDescr.locOrigin.isForcedRBegin = true;
				if ( antInstDescr != null) antInstDescr.locOrigin.isForcedREnd = true;
			}
			if ( insideBlock && instDescr.locOrigin.iForcedRisa == -1) {
				insideBlock = false;
				instDescr.locOrigin.isForcedREnd = true;
			}
			// guarda a anterior para desmarca'-la para evitar juntar blocos de conjuntos rISA diferentes
			antInstDescr = instDescr;
		}
	}
	
	boolean bRecRisa = false;
	
	public void setReconfigurableRisa ( boolean recRisa) {
		bRecRisa = recRisa;
	}
	
	public boolean getReconfigurableRisa ( ) {
		
		return ( bRecRisa);
	}
	
	public int iTmp = 0;
	
	public void setRisaSet ( int srs)
	{
		iTmp = srs;
	}

	public void reconfigurableRisa ( boolean recRisa) {
		NormalBinaryInstruction instDescr;
		// metodos sao identificados numericamente, iniciando com o valor 1
		int indice = 0;
		
		int [] methods      = {   1,   2,   3,   4,   5,   6,   7,   8,   9,  10,  11,  12,  13,  14,   15};
		int [] uniqueRisa    = { -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,   -1};
		//int [] reconfRisa   = {   2,   2,   10,   11,   2,   2,   2,   2};			// bitcount - OK
		//int [] reconfRisa   = { 20, 20 };                                       		// CRC32 - OK
		//
		//int [] reconfRisa   = { 0, 2, 9, 2, 2, 2 };                                   // stringsearch - OK
		//
		//int [] reconfRisa   = { iTmp, iTmp, iTmp, iTmp, iTmp, iTmp };
		//
		//int [] reconfRisa   = { 2, 2, 2, 2, 2, 2, 2 };      		                    // qsort - OK
		//
		int [] reconfRisa   = { 2,   2,   10,   11,   2,   2,   2,   2, 20, 2, 2,   2 }; // => 12bitCRC - OK
		//
		//int [] reconfRisa   = { 21,   21,   21,   21,   21,   21,   21, /**/  2,   10,  11,   2,   2,   2,   2}; // => 13bitSort - OK 
		//
		//int [] reconfRisa   = { 2,   2,   2,   2,   2,   2,   2, 20, 20, 20 }; 		// => 23CRCSort - OK
		//
		//int [] reconfRisa   = { 2,   2,   10,   11,   2,   2,   2,   2, /**/  2,   9,   2,   2,   2}; // => 14bitSearch - OK 
		//
		//int [] reconfRisa   = { 0,   2,   9,   2,   2,  2, 20}; // => 24CRCSearch - OK
		//
		//int [] reconfRisa   = { 0,   /**/ 2,   2,   2, /**/  2,  9, /**/ 2, 2, 2, 2}; // => 24sortSearch - OK
		//
		                        //
		                        //1    2    3    4    5    6    7    8    9   10   11   12   13   14    15
System.out.println ( "iTmp = "+iTmp);
		for ( int i = 0; i < htNInstructions.size(); i ++) {
			instDescr = (NormalBinaryInstruction) htNInstructions.get ( new Integer (i));
			if ( instDescr.locOrigin.methodID == methods [indice]) {
				if ( recRisa) instDescr.locOrigin.iWhichRisa = reconfRisa [ indice];
				else instDescr.locOrigin.iWhichRisa = uniqueRisa [ indice];
			} else {
				indice ++;
				if ( recRisa) instDescr.locOrigin.iWhichRisa = reconfRisa [ indice];
				else instDescr.locOrigin.iWhichRisa = uniqueRisa [ indice];
			}
		}
	}
	
	public void forceRisa ( int iMethod, int iWhichRisa) {
		NormalBinaryInstruction instDescr;

		for ( int i = 0; i < htNInstructions.size(); i ++) {
			instDescr = (NormalBinaryInstruction) htNInstructions.get ( new Integer (i));
			if ( instDescr.locOrigin.methodID == iMethod) instDescr.locOrigin.iWhichRisa = iWhichRisa; 
		}
	}
	
	public void markCandidates ( ) {
		NormalBinaryInstruction instDescr;

		for ( int i = 0; i < htNInstructions.size(); i ++) {
			instDescr = (NormalBinaryInstruction) htNInstructions.get ( new Integer (i));
			// reconfigurable rISA
			if ( instDescr.locOrigin.isNewFunction || instDescr.locOrigin.isForcedREnd) {
//if ( instDescr.locOrigin.isForcedREnd) System.out.println ( "*** retomando risa do metodo: "+ instDescr.locOrigin.iWhichRisa);
				mips.rISA.setRisaConfiguration(instDescr.locOrigin.iWhichRisa);
			} else if ( instDescr.locOrigin.isForcedRBegin) {
//System.out.println ( "*** forçando risa: "+ instDescr.locOrigin.iForcedRisa);
				mips.rISA.setRisaConfiguration(instDescr.locOrigin.iForcedRisa);
			}
			//
			// instruçao seguinte a um bloco risa forçado com as diretivas nao deve ser marcada
			if ( instDescr.locOrigin.isForcedREnd)
				// necessario criar uma outra constante para este motivo de nao marcaçao
				instDescr.bTranslatable = false;
			else
//				 test if the instruction is in the rISA set
				instDescr.bTranslatable = mips.isrISAble ( instDescr.locOrigin.instruction) == -1 ? false : true;
			//
			if ( instDescr.bTranslatable == false) instDescr.locOrigin.reason = ISnotINtheSET;
			else instDescr.locOrigin.reason = ISinTheSET;
//instDescr.list();
		}
	}
	
	public void isPossibleToReduceCandidates ( ) {
		NormalBinaryInstruction instDescr;
		boolean bNotFull;

		// para 8 ou 16 opcodes, este range possui igual valor, i.e., 8, PELO MENOS POR ENQUANTO
		mips.createRegZipNumberForItypeInstr ( rangeForITypeRegDest);	// for ITYPE instructions: zip the RS1 bitfield, the respective bit field use 2 bits: 2^2
		
		for ( int i = 0; i < htNInstructions.size(); i ++) {
			bNotFull = true;
			instDescr = (NormalBinaryInstruction) htNInstructions.get ( new Integer (i));
			// reconfigurable rISA
			if ( instDescr.locOrigin.isNewFunction || instDescr.locOrigin.isForcedREnd) {
				mips.rISA.setRisaConfiguration(instDescr.locOrigin.iWhichRisa);
			} else if ( instDescr.locOrigin.isForcedRBegin) {
				mips.rISA.setRisaConfiguration(instDescr.locOrigin.iForcedRisa);
			}
			//
			// test if it is possible to reduce the instruction
			if ( instDescr.bTranslatable) {
				LineOfCodeDLX loc = instDescr.locOrigin;
				int iRet = 0;
				switch ( loc.typeInst) {
					case RTYPE:
						iRet = assembleRType( loc, RTypeBitFields);
						break;
					case ITYPE:
						if ( mips.getRegZipNumberForItypeInstr(loc.rs1)==-1) {
							bNotFull = mips.setRegZipNumberForItypeInstr ( loc.rs1);
						}
						if ( ! bNotFull) iRet = - 1;
						else iRet = assembleIType( loc, ITypeBitFields);
						break;
					case JTYPE:
						iRet = assembleJType( loc, JTypeBitFields);
						break;
					default:
						loc.messageError = "Instruction type undefined "+loc.lineNumber+":"+loc.allLine;
						break;
				}
				instDescr.bTranslatable = (iRet == -1) ? false : true;
				if ( instDescr.bTranslatable == false) {
					if ( ! bNotFull) instDescr.locOrigin.reason = ISRegAccess;
					else instDescr.locOrigin.reason = ISOverflow;
				}
//instDescr.list();
//if ( iRet == - 1) System.out.println ( "Reduction is impossible!");
				//loc.bTranslatable = instDescr.bTranslatable;
			}
		}
	}
	
	public void discardSmallBlocks ( ) {
		NormalBinaryInstruction instDescr, markBegin;
		int iSize = 0, iFirst;

		for ( int i = 0; i < htNInstructions.size(); i ++) {
			instDescr = (NormalBinaryInstruction) htNInstructions.get ( new Integer (i));
			if ( instDescr != null && instDescr.bTranslatable == true) {
				instDescr.bBeginOfBlock = true;
				markBegin = instDescr;
				iSize = 0;
				iFirst = i;
				while  ( instDescr != null && instDescr.bTranslatable == true) {
					iSize ++;
					instDescr = (NormalBinaryInstruction) htNInstructions.get ( new Integer (++i));
				}
				if ( iSize < LIMIT_SIZE_OF_BLOCKS) {
//System.out.println ( "Discarding block... "+iSize);
					markBegin.bBeginOfBlock = false;
					markBegin.iSizeBlock = 0;
					for ( int j = iFirst; j < iFirst + iSize; j ++) {
						instDescr = (NormalBinaryInstruction) htNInstructions.get ( new Integer (j));
						instDescr.bTranslatable = false;
						instDescr.locOrigin.reason = ISSmallBlock;
						//cancelBranchesAndJumpsToThisTarget(j);
					}
				} else {
//System.out.println ( "Validating block... "+iSize);
					markBegin.iSizeBlock = iSize;
				}
			}
		}
		//listInstDescrNormal ( );		
	}

	public void countFinalBlocks ( ) {
		NormalBinaryInstruction instDescr, markBegin;
		int iSize = 0, iFirst;

		for ( int i = 0; i < htNInstructions.size(); i ++) {
			instDescr = (NormalBinaryInstruction) htNInstructions.get ( new Integer (i));
//instDescr.list ( );	
			if ( instDescr != null && instDescr.bTranslatable == true) {
				instDescr.bBeginOfBlock = true;
				markBegin = instDescr;
				iSize = 0;
				iFirst = i;
				while  ( instDescr != null && instDescr.bTranslatable == true) {
					iSize ++;
					instDescr = (NormalBinaryInstruction) htNInstructions.get ( new Integer (++i));
//instDescr.list ( );	
				}
				markBegin.iSizeBlock = iSize;
			}
		}	
	}
/*
	private void cancelBranchesAndJumpsToThisTarget ( int iAddr) {
		NormalBinaryInstruction instDescr;
		LineOfCodeDLX loc;
		int i, iNormalAddr;
/*
		for ( i = 0; i < htNInstructions.size(); i ++) {
			instDescr = (NormalBinaryInstruction) htNInstructions.get ( new Integer (i));
			loc = instDescr.locOrigin;
			if ( loc.label != null && llCodeLabels.getNormalAddress(loc.label) == iAddr) {
//System.out.println ( "cancelBranchesAndJumpsToThisTarget");
				instDescr.bTranslatable = false;
				instDescr.bBeginOfBlock = false;
				instDescr.locOrigin.reason = ISJumps;
//if ( loc.opcode == 5) loc.list ();
			}		
		}

	}
*/	
	public boolean treatBranchesAndJumps ( ) {
		NormalBinaryInstruction instDescr, targetInstDescr;
		LineOfCodeDLX loc;
		int i, iNormalAddr;
		boolean bAct = false;

		for ( i = 0; i < htNInstructions.size(); i ++) {
			instDescr = (NormalBinaryInstruction) htNInstructions.get ( new Integer (i));
			loc = instDescr.locOrigin;
			if ( loc.targetLabel != null && ( ( iNormalAddr = llCodeLabels.getNormalAddress(loc.targetLabel)) != - 1)) {
				targetInstDescr = (NormalBinaryInstruction) htNInstructions.get ( new Integer (iNormalAddr));
				if ( instDescr.bTranslatable != targetInstDescr.bTranslatable) {
					if ( targetInstDescr.bTranslatable == true && targetInstDescr.bBeginOfBlock == true) {
//System.out.println ( "(1) De bloco normal para reduzido eh permitido se saltar para o início do bloco: mantendo habilitação da tradução...");	
//System.out.println ( "(1) De: ");
//loc.list ( );
//System.out.println ( "Para: ");
//loc = targetInstDescr.locOrigin;
//loc.list ( );
					} else {
//System.out.println ( "(2) Saltos entre blocos diferentes: normal para reduzido e vice-versa: desabilitando a tradução...");
						//instDescr.list();
						//targetInstDescr.list();						
						instDescr.bTranslatable = targetInstDescr.bTranslatable = false;
						instDescr.locOrigin.reason = ISJumps;
						instDescr.bBeginOfBlock = targetInstDescr.bBeginOfBlock = false;
						targetInstDescr.locOrigin.reason = ISJumps;
						//cancelBranchesAndJumpsToThisTarget(iNormalAddr);
						bAct = true;
					}
				} else {
					if ( targetInstDescr.bTranslatable == true && targetInstDescr.bBeginOfBlock == false) {
						boolean bTmp = false;
						if ( instDescr.locOrigin.iForcedRisa != - 1) {
							if ( targetInstDescr.locOrigin.iForcedRisa != - 1) {
								bTmp = ( instDescr.locOrigin.iForcedRisa != targetInstDescr.locOrigin.iForcedRisa ? true : false);
							} else {
								bTmp = ( instDescr.locOrigin.iForcedRisa != targetInstDescr.locOrigin.iWhichRisa ? true : false);
							}
						} else if ( targetInstDescr.locOrigin.iForcedRisa != - 1) {
							bTmp = ( instDescr.locOrigin.iWhichRisa != targetInstDescr.locOrigin.iForcedRisa ? true : false);
						}
						
						if ( bTmp) {
System.out.println ( "********** Blocos com rISAs diferentes ! Desabilitando Jumps **********");
							instDescr.bTranslatable = targetInstDescr.bTranslatable = false;
							instDescr.locOrigin.reason = ISJumps;
							instDescr.bBeginOfBlock = targetInstDescr.bBeginOfBlock = false;
							targetInstDescr.locOrigin.reason = ISJumps;
							bAct = true;
						}
						
						if ( instDescr.locOrigin.iWhichRisa != targetInstDescr.locOrigin.iWhichRisa) {
							System.out.println ( "blocos com rISAs diferentes ! ");
						}
//System.out.println ( "(3) Por enquanto, nao pode saltar para dentro de um bloco reduzido (de outro bloco reduzido): desabilitando a tradução...");	
						//instDescr.list ( );
						//targetInstDescr.list ( );
						//instDescr.bTranslatable = targetInstDescr.bTranslatable = false;
						//instDescr.bBeginOfBlock = targetInstDescr.bBeginOfBlock = false;
						//cancelBranchesAndJumpsToThisTarget(iNormalAddr);
					} else {
//System.out.println ( "(4) Salto normal p/ normal, ou reduzido p/ reduzido: mantendo habilitação da tradução...");	
//System.out.println ( "(4) De: ");
//loc.list ( );
//System.out.println ( "Para: ");
//loc = targetInstDescr.locOrigin;
//loc.list ( );
					}
				}
			}
		}
		
		return ( bAct);
	}
	
	/*
	public void markBeginAndSizeOfBlocks ( ) {
		NormalBinaryInstruction instDescr, markBegin;
		int iSize = 0;
//System.out.println("na markBeginAndSizeOfBlocks");
//System.out.println (htCodeLabels);
		for ( int i = 0; i < htNInstructions.size(); i ++) {
			instDescr = (NormalBinaryInstruction) htNInstructions.get ( new Integer (i));
			if ( instDescr != null && instDescr.bTranslatable == true) {
				instDescr.bBeginOfBlock = true;
				markBegin = instDescr;
				iSize = 0;
				while  ( instDescr.bTranslatable == true) {
					iSize ++;
					instDescr = (NormalBinaryInstruction) htNInstructions.get ( new Integer (++i));
					// If it finds a label it stops the block delimitation
					if ( instDescr == null) break;
// if ( instDescr.locOrigin.hasLabel) break;
				}
				markBegin.iSizeBlock = iSize;
			}
		}
		//listInstDescrNormal ( );
	}
	*/

	public void listBlocks () {
		NormalBinaryInstruction instDescr, markBegin;
		LineOfCodeDLX loc;
		int cont = 0;
		int contBlocks = 0, acumBlocks = 0;
		int iA, iB, iC, iD, iE, iF, iG, iH, iTotal;
		iA = iB = iC = iD = iE = iF = iG = iH = iTotal = 0;
		//System.out.println("#################### checkBlocks");
		iTotal = htNInstructions.size();
		for ( int i = 0; i < iTotal; i ++) {
			instDescr = (NormalBinaryInstruction) htNInstructions.get ( new Integer (i));
			loc = instDescr.locOrigin;
			// Parece desnecessario este teste - loc.isNormal == true
			if ( loc.isNormal == true) {
				switch ( loc.reason) {
					case ISNORMAL:
						iE ++;
						break;
					case ISnotINtheSET:
						iA ++;
						break;
					case ISinTheSET:
						iH ++;
						iG ++;
						break;
					case ISOverflow:
						iB ++;
						iG ++;
						break;
					case ISJumps:
						iC ++;
						iG ++;
						break;
					case ISSmallBlock:
						iD ++;
						iG ++;
						break;
					case ISRegAccess:
						iF ++;
						iG ++;
						break;
					default:
						break;
				}
			}
			cont = instDescr.iSizeBlock;
			if ( cont > 0) {
				//System.out.println("\n########## begin of Block of size: "+cont);
				contBlocks ++;
				acumBlocks += cont;
			}
			while ( cont > 0) {
				cont --;
				if (cont > 0) {
					instDescr = (NormalBinaryInstruction) htNInstructions.get ( new Integer (++ i));
					loc = instDescr.locOrigin;
					switch ( loc.reason) {
						case ISNORMAL:
							iE ++;
							break;
						case ISnotINtheSET:
							iA ++;
							break;
						case ISinTheSET:
							iH ++;
							iG ++;
							break;
						case ISOverflow:
							iB ++;
							iG ++;
							break;
						case ISJumps:
							iC ++;
							iG ++;
							break;
						case ISSmallBlock:
							iD ++;
							iG ++;
							break;
						case ISRegAccess:
							iF ++;
							iG ++;
							break;
						default:
							break;
					}
				}
			}
		}
		
		mips.rodadaRisa.inicializa();
		mips.rodadaRisa.setTotalInstructions(iTotal);
		mips.rodadaRisa.setNumberOfInstructionsMarked(iG);
		mips.rodadaRisa.setNumberOfInstructionsStillMarked(iH);
		mips.rodadaRisa.setNumberOfInstructionsNotMarked(iA);
		mips.rodadaRisa.setOverflow(iB);
		mips.rodadaRisa.setHandling(iC);
		mips.rodadaRisa.setSmallsize(iD);
		mips.rodadaRisa.setNumberOfBlocks(contBlocks);
		mips.rodadaRisa.setAverageSizeOfBlocks((float)acumBlocks/contBlocks);
		mips.setRodadaRisa.set(0, mips.rodadaRisa);
		
		System.out.println ( "total de instruoes : " + mips.rodadaRisa.getTotalInstructions());
		System.out.println ( "total de instruoes marcadas : " + mips.rodadaRisa.getNumberOfInstructionsMarked());
		System.out.println ( "total de instruoes que continuam marcadas: " + mips.rodadaRisa.getNumberOfInstructionsStillMarked());
		//System.out.println ( "total de instruoes que marcadas (calculado): "+(iH+iB+iC+iD));
		System.out.println ( "total de instruoes nao marcadas: " + mips.rodadaRisa.getNumberOfInstructionsNotMarked());
		//System.out.println ( "instruao normal e razao normal: "+iE);
		System.out.println ( "\ncontBlocks = " + mips.rodadaRisa.getNumberOfBlocks() + ", average size = " + mips.rodadaRisa.getAverageSizeOfBlocks());
		System.out.println ( "is normal ...");
		System.out.println ( "| Reason | Number | % total | % marked |");
		System.out.println ( "because it was not in the reduced set: " + mips.rodadaRisa.getNumberOfInstructionsNotMarked() + " | "
				             + (float) mips.rodadaRisa.getNumberOfInstructionsNotMarked()*100/mips.rodadaRisa.getTotalInstructions()+"% | - |");
		System.out.println ( "due to overflow: " + mips.rodadaRisa.getOverflow() + " | "
				             + (float)mips.rodadaRisa.getOverflow()*100/mips.rodadaRisa.getTotalInstructions() + "% | "
				             + mips.rodadaRisa.getOverflowPercent() + "% |");
		System.out.println ( "due to jump handling: " + mips.rodadaRisa.getHandling() + " | "
				             + (float)mips.rodadaRisa.getHandling()*100/mips.rodadaRisa.getTotalInstructions() + "% | "
				             + mips.rodadaRisa.getHandlingPercent() + "% |");
		System.out.println ( "due to the small size of the block: " + mips.rodadaRisa.getSmallsize() + " | "
				             + (float)mips.rodadaRisa.getSmallsize()*100/mips.rodadaRisa.getTotalInstructions() + "% | "
				             + mips.rodadaRisa.getSmallPercent() + "% |");
		System.out.println ( "due to the register accessibility: " + iF + " | " + (float) iF*100/mips.rodadaRisa.getTotalInstructions() + "% | - |");
		System.out.println ( "or: "+ (float) iE*100/iTotal + "%");
		System.out.println ( );
	}

	private int assembleCM ( LineOfCodeDLX loc) { //int opcode, int address) {
		int iInstruction = 0, iOpcode = 0x3f, iTmp;
		int nRInst;

		nRInst = loc.immediate & 0x03ffffff;
		
		iTmp = iOpcode << 26;
		iInstruction = iInstruction | iTmp;
		iInstruction = iInstruction | nRInst;

		return ( iInstruction);
	}
/*
	private boolean hasLabel ( String parLine) {
		String newLine = parLine, sRet = null;
		StringTokenizer stLine = new StringTokenizer ( newLine, " \t,", false);
		String [ ] sTokens = new String [ 1];
		if (stLine.hasMoreTokens()) sTokens [ 0] = stLine.nextToken();
		if ( sTokens [ 0].endsWith ( ":")) return ( true);
		return ( false);
	}
	
	private String getLabel ( String parLine, int pAddrLine) {
		String newLine = parLine, sRet = null;
		StringTokenizer stLine = new StringTokenizer ( newLine, " \t,", false);
		String [ ] sTokens = new String [ 1];
		if (stLine.hasMoreTokens()) sTokens [ 0] = stLine.nextToken();
		if ( sTokens [ 0].endsWith ( ":")) {
			sRet = sTokens[0].replaceAll (":", "");
			htCodeLabels.put ( sTokens[0].replaceAll (":", ""), new Integer (pAddrLine));
		}
		
		//System.out.println ( sRet);
		//System.out.println ( htCodeLabels);
		
		return ( sRet);
	}
*/
	private void updateListOfLabels ( LineOfCodeDLX loc, int addr, boolean isNormal) {
//System.out.println ( loc.hasLabel);
		if ( loc.hasLabel) llCodeLabels.setRISAAddress( loc.inlineLabel, addr, isNormal);
	}
	
	private LineOfCodeDLX generateChangeModeInstruction ( int address, int ninst) {
		LineOfCodeDLX loc = new LineOfCodeDLX ( "");
//System.out.println ( "montando change mode com rISA: "+ninst);
		loc = new LineOfCodeDLX ( "***** CM "+ninst);
		loc.typeInst = JTYPE;
		loc.immediate=ninst;
		loc.RISAaddressOfThisLine = address;
		loc.instruction = assembleCM ( loc);
		loc.isNormal = true;
		
		return ( loc);
	}
/*
	private LineOfCodeDLX generateChModForReducedInstructionOpt3(int address) {
		LineOfCodeDLX loc = new LineOfCodeDLX ( "");
		
		loc = new LineOfCodeDLX ( "***** CM "+-1);
		loc.typeInst = CMTYPE;
		loc.RISAaddressOfThisLine = address;
		loc.instruction = 0xffffffff;
		loc.isNormal = false;
		
		return ( loc);
	}
*/
	public boolean bSpecialNops = true;
	
	private LineOfCodeDLX generateNopInstruction ( int address) {
		LineOfCodeDLX loc = new LineOfCodeDLX ( "");
		
		
		loc = new LineOfCodeDLX ( "***** nop");
		if ( bSpecialNops) loc.typeInst = BUBBLE;
		else loc.typeInst = RTYPE;
		loc.instruction = 0;
		if ( ! bSpecialNops) { 
			loc.rs1=16;loc.rs2=16;loc.rd=16;
		}
		loc.RISAaddressOfThisLine = address;
		loc.isNormal = false;
		//loc.bTranslatable = true;

		return ( loc);
	}

	private LineOfCodeDLX generateChModForNormalInstruction ( int address) {
		LineOfCodeDLX loc = new LineOfCodeDLX ( "");
		
		loc = new LineOfCodeDLX ( "***** CM to normal");
		if ( bSpecialNops) loc.typeInst = CMTYPE;
		else loc.typeInst = RTYPE;
		if ( bSpecialNops) loc.instruction = 57344;
		else loc.instruction = 0;
		if ( ! bSpecialNops) { 
			loc.rs1=17;loc.rs2=17;loc.rd=17;
		}
		loc.RISAaddressOfThisLine = address;
		loc.isNormal = false;
		//loc.bTranslatable = true;

		return ( loc);
	}

	// Instruçao Normal Change Mode to RISA carregando o numero de instruçoes reduzidas no bloco
	// O bloco nao pode incluir branches nem jumps
	/*
	public void translateToRISAstep1_Opt1 ( ) {
		NormalBinaryInstruction nInstDescr;
		LineOfCodeDLX loc;
		int iCont = 0, iAddress = 0, nRinstActual, nRinstWithNops, i, j;

		for ( i = 0; i < htNInstructions.size(); i ++) {
			nInstDescr = (NormalBinaryInstruction) htNInstructions.get ( new Integer (i));
			if ( nInstDescr.bBeginOfBlock == true && nInstDescr.iSizeBlock >= 4) {
				int iPairs = 0;
				nRinstActual = nInstDescr.iSizeBlock;
				nRinstWithNops = nInstDescr.iSizeBlock + ( nInstDescr.iSizeBlock % 2);
				// insert change mode instruction
				htRISA.put ( new Integer (iCont ++), generateChangeModeInstruction(iAddress++,nRinstWithNops/2) );
				nInstDescr.locOrigin.RISAaddressOfThisLine = iAddress;
				// If it has a label in the first instruction of the block, the label goes to the CM instruction
				//nInstDescr.locOrigin.labelRISA = 
				//getLabel ( nInstDescr.locOrigin.allLine, iAddress - 1);
				updateListOfLabels( nInstDescr.locOrigin, iAddress-1);
				nInstDescr.locOrigin.isNormal = false;
				htRISA.put ( new Integer (iCont ++), nInstDescr.locOrigin);
				iPairs ++;
				for ( j = i+1; j < i+nRinstActual; j ++) {
					nInstDescr = (NormalBinaryInstruction) htNInstructions.get ( new Integer (j));
					nInstDescr.locOrigin.RISAaddressOfThisLine = iAddress;
					if ( ++ iPairs % 2 == 0) iAddress ++; 
					nInstDescr.locOrigin.isNormal = false;
					htRISA.put ( new Integer (iCont ++), nInstDescr.locOrigin);
				}
				i = j-1;
				if ( nRinstWithNops > nRinstActual) {
					// insert nop instruction
					htRISA.put ( new Integer (iCont ++), generateNopInstruction(iAddress));
					if ( ++ iPairs % 2 == 0) iAddress ++; 
				}
			} else {
				// employ the same data structure - LineOfCodeDLX - used to describe normal instructions
				nInstDescr.locOrigin.RISAaddressOfThisLine = iAddress ++;
				//nInstDescr.locOrigin.labelRISA = 
				//getLabel ( nInstDescr.locOrigin.allLine, iAddress-1);
				updateListOfLabels( nInstDescr.locOrigin, iAddress-1);
				nInstDescr.locOrigin.isNormal = true;
				htRISA.put ( new Integer (iCont++), nInstDescr.locOrigin);
			}
		}		
	}
	*/

	// Instruçao Normal Change Mode to RISA acompanhada de uma Reduced Change Mode to Normal no final do bloco
	public void translateToRISAstep1_Opt2 ( ) {
		NormalBinaryInstruction nInstDescr, markBegin;
		LineOfCodeDLX loc;
		int iCont = 0, iAddress = 0, iPairs, nRinstActual, nRinstWithChInstrs, nInsertedNops, i, j;

		for ( i = 0; i < htNInstructions.size(); i ++) {
			nInstDescr = (NormalBinaryInstruction) htNInstructions.get ( new Integer (i));
			if ( nInstDescr.bBeginOfBlock == true) { // && nInstDescr.iSizeBlock >= LIMIT_SIZE_OF_BLOCKS) {
				markBegin = nInstDescr;
				iPairs = nInsertedNops = 0;
				nRinstActual = nInstDescr.iSizeBlock;
				//nInstDescr.iSizeBlock = nRinstWithChInstrs;
				// nRinstWithNops = nInstDescr.iSizeBlock + ( nInstDescr.iSizeBlock % 2);
				// insert change mode instruction
				htRISA.put ( new Integer (iCont ++), generateChangeModeInstruction(iAddress++, ( nInstDescr.locOrigin.iForcedRisa != -1 ? nInstDescr.locOrigin.iForcedRisa : nInstDescr.locOrigin.iWhichRisa)));
				nInstDescr.locOrigin.RISAaddressOfThisLine = iAddress;
				//
				//updateListOfLabels( nInstDescr.locOrigin, iAddress - 1);
				updateListOfLabels( nInstDescr.locOrigin, iAddress, false);
				nInstDescr.locOrigin.isNormal = false;
				if ( 	( nInstDescr.locOrigin.opcode >= 1 &&
						nInstDescr.locOrigin.opcode <= 7)
						&& iPairs % 2 == 0) {
							// branches and jumps sao sempre a segunda instruçao num bloco reduzido
							htRISA.put ( new Integer (iCont ++), generateNopInstruction(iAddress));
							if ( ++ iPairs % 2 == 0) iAddress ++; 
							nInsertedNops ++;
//nInstDescr.locOrigin.list();
//System.out.println ( "inserindo nops first of block..."+iAddress);
				}
//if ( nInstDescr.locOrigin.labelOfLine != null) System.out.println ( "label reduzido no inicio: "+nInstDescr.locOrigin.labelOfLine);
				htRISA.put ( new Integer (iCont ++), nInstDescr.locOrigin);
//if ( iAddress == 17) nInstDescr.list ( );
				if ( ++ iPairs % 2 == 0) iAddress ++; 
				for ( j = i+1; j < i+nRinstActual; j ++) {
					nInstDescr = (NormalBinaryInstruction) htNInstructions.get ( new Integer (j));
					if ( iPairs % 2 == 0) {
						if ( nInstDescr.locOrigin.opcode >= 1 && nInstDescr.locOrigin.opcode <= 7)  {
							// branches and jumps sao sempre a segunda instruçao num bloco reduzido
							htRISA.put ( new Integer (iCont ++), generateNopInstruction(iAddress));
							if ( ++ iPairs % 2 == 0) iAddress ++; 
							nInsertedNops ++;
						}
					} else {
						if ( 	! ( nInstDescr.locOrigin.opcode >= 1 && nInstDescr.locOrigin.opcode <= 7) &&
								nInstDescr.locOrigin.inlineLabel != null) {
							// Sendo a segunda instruçao no bloco a que contem o label
							// insere-se um nop na frente
//System.out.println ( "label "+nInstDescr.locOrigin.labelOfLine+" no end. "+iAddress+" impar(segunda): "+iPairs);
							htRISA.put ( new Integer (iCont ++), generateNopInstruction(iAddress));
							if ( ++ iPairs % 2 == 0) iAddress ++; 
							nInsertedNops ++;						
						}
					}
					nInstDescr.locOrigin.RISAaddressOfThisLine = iAddress;
					updateListOfLabels( nInstDescr.locOrigin, iAddress, false);
					nInstDescr.locOrigin.isNormal = false;
					htRISA.put ( new Integer (iCont ++), nInstDescr.locOrigin);
//if ( iAddress == 17) nInstDescr.list ( );
					if ( ++ iPairs % 2 == 0) iAddress ++; 
				}
				i = j-1;
				//if ( nRinstWithNops > nRinstActual) {
				// insert cmNormal instruction
				nRinstActual += nInsertedNops;
//System.out.println ( "nInsertedNops = "+nInsertedNops);
				if ( nRinstActual % 2 == 1) {
					nRinstWithChInstrs = nRinstActual + 1;
				} else {
					nRinstWithChInstrs = nRinstActual + 2;
				}
				htRISA.put ( new Integer (iCont ++), generateChModForNormalInstruction(iAddress));
				if ( ++ iPairs % 2 == 0) iAddress ++; 
				//}
				if ( nRinstWithChInstrs - nRinstActual == 2) {
					htRISA.put ( new Integer (iCont ++), generateChModForNormalInstruction(iAddress));
					if ( ++ iPairs % 2 == 0) iAddress ++; 
				}
				markBegin.iSizeBlock = nRinstWithChInstrs;
			} else {
				// employ the same data structure - LineOfCodeDLX - used to describe normal instructions
				nInstDescr.locOrigin.RISAaddressOfThisLine = iAddress ++;
				//nInstDescr.locOrigin.labelRISA = 
				//getLabel ( nInstDescr.locOrigin.allLine, iAddress-1);
				updateListOfLabels( nInstDescr.locOrigin, iAddress-1, true);
				nInstDescr.locOrigin.isNormal = true;
				htRISA.put ( new Integer (iCont++), nInstDescr.locOrigin);
			}
		}		
	}
	
	// DESCARTADA: Instruçao Reduced Change Mode to RISA acompanhada de uma Reduced Change Mode to Normal no final do bloco
	/*
	public void translateToRISAstep1_Opt3 ( ) {
		NormalBinaryInstruction nInstDescr;
		LineOfCodeDLX loc;
		int iCont = 0, iAddress = 0, nRinstActual, nRinstWithChInstrs, nInsertedNops, i, j;

		for ( i = 0; i < htNInstructions.size(); i ++) {
			nInstDescr = (NormalBinaryInstruction) htNInstructions.get ( new Integer (i));
			if ( nInstDescr.bBeginOfBlock == true && nInstDescr.iSizeBlock >= LIMIT_SIZE_OF_BLOCKS) {
				int iPairs = 0;
				nRinstActual = nInstDescr.iSizeBlock;
				// insert change mode instruction
				htRISA.put ( new Integer (iCont ++), generateChModForReducedInstructionOpt3(iAddress));
				nInstDescr.locOrigin.RISAaddressOfThisLine = iAddress;
				// If it has a label in the first instruction of the block, the label goes to the CM instruction
				//nInstDescr.locOrigin.labelRISA = 
				//getLabel ( nInstDescr.locOrigin.allLine, iAddress - 1);
				updateListOfLabels( nInstDescr.locOrigin, iAddress);
				nInstDescr.locOrigin.isNormal = false;
				htRISA.put ( new Integer (iCont ++), nInstDescr.locOrigin);
				iAddress ++;
				nInsertedNops = 0;
				for ( j = i+1; j < i+nRinstActual; j ++) {
					nInstDescr = (NormalBinaryInstruction) htNInstructions.get ( new Integer (j));
					if ( nInstDescr.locOrigin.opcode == 5 && iPairs % 2 == 0) {
//System.out.println ( "inserindo um nop bubble...");
						htRISA.put ( new Integer (iCont ++), generateNopInstruction(iAddress));
						if ( ++ iPairs % 2 == 0) iAddress ++; 
//System.out.println ( "address = "+iAddress);
						nInsertedNops ++;
					}
					nInstDescr.locOrigin.RISAaddressOfThisLine = iAddress;
					if ( ++ iPairs % 2 == 0) iAddress ++; 
					nInstDescr.locOrigin.isNormal = false;
					htRISA.put ( new Integer (iCont ++), nInstDescr.locOrigin);
//System.out.println ( "address = "+iAddress);
				}
				i = j-1;
				// insert cmNormal instruction
				htRISA.put ( new Integer (iCont ++), generateChModForNormalInstructionOpt3(iAddress));
				if ( ++ iPairs % 2 == 0) iAddress ++; 
				//
				nRinstActual += nInsertedNops;
				if ( nRinstActual % 2 == 0) {
					nRinstWithChInstrs = nRinstActual + 1;
				} else {
					nRinstWithChInstrs = nRinstActual + 2;
				}
				//
				if ( nRinstWithChInstrs - nRinstActual == 2) {
					htRISA.put ( new Integer (iCont ++), generateChModForNormalInstructionOpt3(iAddress));
					if ( ++ iPairs % 2 == 0) iAddress ++; 
				}
				
			} else {
				// employ the same data structure - LineOfCodeDLX - used to describe normal instructions
				nInstDescr.locOrigin.RISAaddressOfThisLine = iAddress ++;
				//nInstDescr.locOrigin.labelRISA = 
				//getLabel ( nInstDescr.locOrigin.allLine, iAddress-1);
				updateListOfLabels( nInstDescr.locOrigin, iAddress-1);
				nInstDescr.locOrigin.isNormal = true;
				htRISA.put ( new Integer (iCont++), nInstDescr.locOrigin);
			}
		}		
	}
	*/

	private int assembleRType ( LineOfCodeDLX loc, int splitFields []) { //int opcode, int rs1, int rs2, int rd, int shamt, int func) {
		int iOpcode = mips.isrISAble ( loc.instruction);
		int iInstruction = 0, iTmp;
		boolean overflow = false;
		int supLimit;
		int bitsLeft;
		int rs1, rs2, rd;

		/*if ( loc.allLine.compareToIgnoreCase("***** nop")==0) {
			loc.list ();
		}*/

		supLimit = (int) Math.pow ( 2, splitFields [0]) - 1;
		bitsLeft = SIZE_RINST-splitFields[0];
//System.out.println ( "opcode = "+iOpcode+ ", supLimit [0] = "+supLimit+" e deslocamento [0] = "+bitsLeft);
		if ( iOpcode > supLimit) {
			overflow = true;
		}
		else {
			iTmp = iOpcode << bitsLeft; // 13
			iInstruction = iInstruction | iTmp;
		}
		supLimit = (int) Math.pow ( 2, splitFields [1]) - 1;
		bitsLeft = bitsLeft-splitFields[1];
//System.out.println ( "supLimit [1] = "+supLimit+" e deslocamento [1] = "+bitsLeft);
		rs1 = loc.rs1 - 16;
		if ( rs1 < 0 || rs1 > supLimit) {
			overflow = true;
		}
		else {
			iTmp = rs1 << bitsLeft; //9;
			iInstruction = iInstruction | iTmp;
		}
		supLimit = (int) Math.pow ( 2, splitFields [2]) - 1;
		bitsLeft = bitsLeft-splitFields[2];
//System.out.println ( "supLimit [2] = "+supLimit+" e deslocamento [2] = "+bitsLeft);
		rs2 = loc.rs2 - 16;
		if ( rs2 < 0 || rs2 > supLimit) {
			overflow = true;
		}
		else {
			iTmp = rs2 << bitsLeft; //5;
			iInstruction = iInstruction | iTmp;
		}
		supLimit = (int) Math.pow ( 2, splitFields [3]) - 1;
		bitsLeft = bitsLeft-splitFields[3];
//System.out.println ( "supLimit [3] = "+supLimit+" e deslocamento [3] = "+bitsLeft);
		rd = loc.rd - 16;
		if ( rd < 0 || rd > supLimit) {
			overflow = true;
		}
		else {
			iTmp = rd << bitsLeft; //1;
			iInstruction = iInstruction | iTmp;
		}
/*
		if ( shamt > 31) return ( -1);
		else {
			iTmp = shamt << 6;
			iInstruction = iInstruction | iTmp;
		}
		if ( func > 63) return ( -1);
		else {
			iInstruction = iInstruction | func;
		}
*/
		if ( overflow) {
//System.out.println ( "overflow  risa RType");
//System.out.println ( iOpcode+","+loc.allLine+",rs1:"+loc.rs1+",rs2:"+loc.rs2+",rd:"+loc.rd);		
//System.out.println ( SistNum.toBinString ( iInstruction, 16));
			return ( -1);
		}
		
		return ( iInstruction);
	}

	private int assembleIType ( LineOfCodeDLX loc, int splitFields []) { //int opcode, int rs, int rd, int immediate) {
		int iOpcode = mips.isrISAble ( loc.instruction);
		int iInstruction = 0, iTmp;
		int immediate;
		boolean overflow = false;
		int supLimit;
		int bitsLeft;
		int rd, rs1;

// immediate = loc.immediate & 0x0000007f;
		
		supLimit = (int) Math.pow ( 2, splitFields [0]) - 1;
		bitsLeft = SIZE_RINST-splitFields[0];
//System.out.println ( "opcode = "+iOpcode+ ", supLimit [0] = "+supLimit+" e deslocamento [0] = "+bitsLeft);
		if ( iOpcode > supLimit) {
			overflow = true;
		}
		else {
			iTmp = iOpcode << bitsLeft;
			iInstruction = iInstruction | iTmp;
//if ( iInstruction == 49152) loc.list ( );
		}
		bitsLeft = bitsLeft-splitFields[1];
		rs1 = mips.getRegZipNumberForItypeInstr ( loc.rs1);
//System.out.print( "translating from: "+loc.rs1+" to "+rs1);
		if ( rs1 == - 1) overflow = true;
		iTmp = rs1 << bitsLeft;
		iInstruction = iInstruction | iTmp;
		//
		supLimit = (int) Math.pow ( 2, splitFields [2]) - 1;
		bitsLeft = bitsLeft-splitFields[2];
//System.out.println ( "supLimit [2] = "+supLimit+" e deslocamento [2] = "+bitsLeft);
//System.out.println( ", writing in: "+loc.rd);
		rd = loc.rd - 16;
		if ( rd < 0 || rd > supLimit) {
			overflow = true;
		}
		else {
			iTmp = rd << bitsLeft;
			iInstruction = iInstruction | iTmp;
		} 
		supLimit = (int) Math.pow ( 2, splitFields [3]) - 1;
		immediate = loc.immediate & supLimit;
		bitsLeft = bitsLeft-splitFields[3];
//System.out.println ( "supLimit [3] = "+supLimit+" e deslocamento [3] = "+bitsLeft);
//if ( loc.immediate < 0) {
	//System.out.println ( "********** imediato negativo: "+loc.immediate);
	//System.out.println ( "supLimit [3] = "+supLimit+" e deslocamento [3] = "+bitsLeft);
//}
		int supLimit1 = ( (supLimit + 1) / 2) - 1;
		int infLimit2 = ( (supLimit + 1) / 2) * - 1;
//System.out.println ( "limite superior = "+supLimit1+" e limite inferior = "+infLimit2);
		if ( loc.immediate < infLimit2 || loc.immediate > supLimit1) {
			overflow = true;
		}
		else {
			iInstruction = iInstruction | immediate;
		}

		if ( overflow) {
//System.out.println ( "overflow risa IType");
//System.out.println ( iOpcode+","+loc.allLine+",rs1:"+loc.rs1+",rd:"+loc.rd+",imm:"+loc.immediate);
//System.out.println ( SistNum.toBinString ( iInstruction, 16));
			return ( -1);
		}

		return ( iInstruction);
	}

	private int assembleJType ( LineOfCodeDLX loc, int splitFields []) { //int opcode, int address) {
		int iOpcode = mips.isrISAble ( loc.instruction);
		int iInstruction = 0, iTmp;
		int address;
		boolean overflow = false;
		int supLimit;
		int bitsLeft;
		
//		address = loc.immediate & 0x00001fff;
		
		supLimit = (int) Math.pow ( 2, splitFields [0]) - 1;
		bitsLeft = SIZE_RINST-splitFields[0];
//System.out.println ( "opcode = "+iOpcode+" supLimit [0] = "+supLimit+" e deslocamento [0] = "+bitsLeft);
		if ( iOpcode > supLimit) {
			overflow = true;
		}
		else {
			iTmp = iOpcode << bitsLeft;
			iInstruction = iInstruction | iTmp;
//if ( iInstruction ==49152) loc.list ( );
		}
		supLimit = (int) Math.pow ( 2, splitFields [1]) - 1;
//System.out.println ( "supLimit = "+supLimit);
		address = loc.immediate & supLimit;
		bitsLeft = bitsLeft-splitFields[1];
//System.out.println ( "address = "+address+" deslocamento [1] = "+bitsLeft);
		int supLimit1 = ( (supLimit + 1) / 2) - 1;
		int infLimit2 = ( (supLimit + 1) / 2) * - 1;
//System.out.println ( "supLimit1 = "+supLimit1+" infLimit2 = "+infLimit2);
		if ( loc.immediate < infLimit2 || loc.immediate > supLimit1) {
//		if ( address > supLimit) {
			overflow = true;
		}
		else {
			iInstruction = iInstruction | address;
		}
		
		if ( overflow) {
System.out.println ( "overflow risa JType");
System.out.println ( iOpcode+","+loc.allLine+","+loc.immediate);
System.out.println ( SistNum.toBinString ( iInstruction, 16));
			return ( -1);
		}

		return ( iInstruction);
	}

	public boolean isPossibleMapRegisters ( ) {
		int i, iCont = 0;
		NormalBinaryInstruction nbiTmp;
		LineOfCodeDLX loc;

		for ( i = 0; i < htNInstructions.size(); i ++) {
			nbiTmp = ( NormalBinaryInstruction) htNInstructions.get ( new Integer ( i));
			loc = ( LineOfCodeDLX) nbiTmp.locOrigin;
				switch ( loc.typeInst) {
					case RTYPE:
						regsUsed [ loc.rs1] = true;
						regsUsed [ loc.rs2] = true;
						regsUsed [ loc.rd] = true;
						break;
					case ITYPE:
						regsUsed [ loc.rs1] = true;
						regsUsed [ loc.rd] = true;
						break;
					default:
						break;
				}
		}

		for ( i = 0; i < 32; i ++) if ( regsUsed [ i] == true) iCont++;
//System.out.println ( "isPossibleMapRegisters = "+iCont);
		if ( iCont > 16) return ( false);
		else return ( true);
	}
	
	public void mapRegisters ( ) {
		LineOfCodeDLX loc;
		int i, j, k;
		NormalBinaryInstruction nbiTmp;

		// it will use only registers 16 to 31
		for ( i = 0; i < 16; i ++) {
			if ( regsUsed [ i] == false) continue;
			for ( j = 16; j < 32; j ++) {
				if ( regsUsed [ j] == false) break;
			}
			for ( k = 0; k < htNInstructions.size(); k ++) {
				nbiTmp = ( NormalBinaryInstruction) htNInstructions.get ( new Integer ( k));
				loc = ( LineOfCodeDLX) nbiTmp.locOrigin;
				switch ( loc.typeInst) {
						case RTYPE:
							if ( loc.rs1 == i) loc.rs1 = j;
							if ( loc.rs2 == i) loc.rs2 = j;
							if ( loc.rd == i) loc.rd = j;
							break;
						case ITYPE:
							if ( loc.rs1 == i) loc.rs1 = j;
							if ( loc.rd == i) loc.rd = j;
							break;
						case JTYPE:
							break;
				}
			}
			regsUsed [ i] = false;
			regsUsed [ j] = true;
		}
	}

	public void translateToRISAstep2 ( int iProgramBaseAddress) {
		LineOfCodeDLX loc, nextloc;
		int i;
		//Integer Iimm;
		int imm, iAddress;

		// adjust address labels: htCodeLabels saves all target labels and respective addresses
		// loc.label is the label in the Assembly mnemonic
		for ( i = 0; i < htRISA.size(); i ++) {
			loc = ( LineOfCodeDLX) htRISA.get ( new Integer ( i));
			if ((loc.type == MontadorMIPS.isLABELandINST || loc.type == MontadorMIPS.isINSTRUCTION)&&loc.targetLabel != null) {
				if (loc.typeInst == ITYPE || loc.typeInst == JTYPE) {
					//Iimm = (Integer) htCodeLabels.get ( loc.label);
					//if ( Iimm != null) {
					iAddress = llCodeLabels.getRISAAddress( loc.targetLabel);
					if ( iAddress != - 1) {
//if (loc.opcode == 2) System.out.println ( "label = "+loc.label+ ", address = "+iAddress);
						//llCodeLabels.list ( );
						//listInstDescrReduced();
						int iOffset = 0;
						if ( mips.getClass ( ).getName ( ).equals ( "processor.MIPSMulti")) {
							if ( loc.typeInst == JTYPE) iOffset = 2;
							else iOffset = 1;
						} else iOffset = 1;
						// System.out.println ( iAddress+" , "+loc.RISAaddressOfThisLine);
						// Se eh branch ou jump normal, salta para a instrucao change mode
						// Senao salta para a instruçao seguinte ao change mode
						if ( loc.isNormal == true) {
							if ( llCodeLabels.getConditionNormalOrReduced(loc.targetLabel) == false) iAddress -= 1;
						}
			 			loc.immediate = iAddress - (loc.RISAaddressOfThisLine + iOffset);
//if (loc.opcode == 2) System.out.println ( "adress of this line = "+loc.RISAaddressOfThisLine);
//if (loc.opcode == 2) System.out.println ( "imediato = "+loc.immediate);
						// loc.immediate = imm;
						if ( loc.isNormal == true) {
							if (loc.typeInst == ITYPE) loc.instruction = MontadorMIPS.assembleIType (loc.opcode, loc.rs1, loc.rd, loc.immediate);
							else loc.instruction = MontadorMIPS.assembleJType (loc.opcode, loc.immediate);
						}
					} else {
						loc.messageError = "Inexistent label at line  "+loc.lineNumber+":"+loc.targetLabel;
					}
				}
			}
		}
		
/* register the number of registers used by ITYPE instructions
 System.out.println ( "(((((((((((((((((****************))))))))))))))))))))))");
		mips.createRegZipNumberForItypeInstr ( 4);	// the respective bit field use 2 bits: 2^2
		for ( i = 0; i < htRISA.size(); i ++) {
			loc = ( LineOfCodeDLX) htRISA.get ( new Integer ( i));
			if ( loc.isNormal == false && loc.typeInst == ITYPE) {
System.out.println ( ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> chamando a setRegZipNumberForItypeInstr...");
				mips.setRegZipNumberForItypeInstr ( loc.rs1);
			}
		}
*/		
//System.out.println ( "Versao definitiva das instrucoes reduzidas!");
		// create 16 bits instructions
		// updateIf8or16();
//System.out.println ( ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> aqui ...");
		for ( i = 0; i < htRISA.size(); i ++) {
			loc = ( LineOfCodeDLX) htRISA.get ( new Integer ( i));
			if ( loc.isNormal == false) {
				// reconfigurable rISA
				if ( loc.iForcedRisa != -1)
					mips.rISA.setRisaConfiguration(loc.iForcedRisa);
				else 
					mips.rISA.setRisaConfiguration(loc.iWhichRisa);
				//}
				//
				switch ( loc.typeInst) {
					case CMTYPE:
						// do nothing
						break;
					//case BUBBLE:
						// do nothing
						//break;
					case RTYPE:
						loc.instruction = assembleRType( loc, RTypeBitFields);
						break;
					case ITYPE:
						loc.instruction = assembleIType( loc, ITypeBitFields);
//if ( loc.RISAaddressOfThisLine == 17) loc.list();
						break;
					case JTYPE:
						loc.instruction = assembleJType( loc, JTypeBitFields);
						break;
					default:
						loc.messageError = "Instruction type undefined "+loc.lineNumber+":"+loc.allLine;
						break;
				}
			}
		}
		
		// create final code
		int iRISAddress, iMemAddress;
		boolean isRISA = false;
		for ( i = 0; i < htRISA.size(); i ++) {
			isRISA = false;
			loc = ( LineOfCodeDLX) htRISA.get ( new Integer ( i));
			iRISAddress = loc.RISAaddressOfThisLine;
			iMemAddress = iRISAddress + iProgramBaseAddress;
//System.out.println ( "iRISAddres ="+ iRISAddress);
			nextloc = ( LineOfCodeDLX) htRISA.get ( new Integer ( i+1));
			if ( nextloc != null) isRISA = iRISAddress == nextloc.RISAaddressOfThisLine ? true : false;
			if ( isRISA) {
				int iInstruction=0;
/*if ( loc.RISAaddressOfThisLine == 18) {
	loc.list ( );
	nextloc.list ( );
	System.out.println ( SistNum.toBinString( loc.instruction << 16, 32));
	System.out.println ( SistNum.toBinString( nextloc.instruction, 32));
}*/
				iInstruction = ( loc.instruction << 16) | ( nextloc.instruction & 0x0000ffff);
 				htFinalCode.put ( new Integer ( iRISAddress), new String ("imemory["+iMemAddress+"]="+iInstruction));
/*if ( loc.RISAaddressOfThisLine == 18) {
System.out.println ( "imemory["+iRISAddress+"]="+iInstruction);
System.out.println ( SistNum.toBinString( iInstruction, 32));
}*/
 				i += 1;
			} else {
 				htFinalCode.put ( new Integer ( iRISAddress), new String ("imemory["+iMemAddress+"]="+loc.instruction));
			}
		}
		//System.exit ( 1);
	}
	
	public void generateFinalCode(PrintWriter output) {
		for ( int i = 0; i < htFinalCode.size(); i ++) {
			String sTmp = (String) htFinalCode.get ( new Integer ( i));
			output.println ( sTmp);
		}
	}

	public void listNormalOpcodes ( ) {
		System.out.println ( "***** Normal Instructions *****");
		for ( int i = 0; i < htNInstructions.size(); i ++) {
			NormalBinaryInstruction opcode = (NormalBinaryInstruction) htNInstructions.get ( new Integer ( i));
			opcode.list ( );
		}
	}
	
	public void listRISAOpcodes ( ) {
		System.out.println ( "***** Reduced Instructions *****");
		for ( int i = 0; i < htRISA.size(); i ++) {
			LineOfCodeDLX loc = (LineOfCodeDLX) htRISA.get ( new Integer ( i));
			System.out.print ( "%%% -> RISA LINE: "+loc.RISAaddressOfThisLine+" ");
			loc.list ( );
		}
	}

	public void listRISAOpcodes ( int iBegin, int iEnd) {
		System.out.println ( "***** Reduced Instructions *****");
		for ( int i = 0; i < htRISA.size(); i ++) {
			LineOfCodeDLX loc = (LineOfCodeDLX) htRISA.get ( new Integer ( i));
			if ( loc.RISAaddressOfThisLine >= iBegin && loc.RISAaddressOfThisLine <= iEnd) {
				System.out.print ( "%%% -> RISA LINE: "+loc.RISAaddressOfThisLine+" ");
				loc.list ( );
			}
		}
	}

	public void listRISAOpcodes ( int iArgument) {
		System.out.println ( "***** Reduced Instructions *****");
		for ( int i = 0; i < htRISA.size(); i ++) {
			LineOfCodeDLX loc = (LineOfCodeDLX) htRISA.get ( new Integer ( i));
			if ( loc.instruction == iArgument) {
				System.out.print ( "%%% -> RISA LINE: "+loc.RISAaddressOfThisLine+" ");
				loc.list ( );
			}
		}
	}
	
	public void updateIf8or16(){
		if (((MIPS)mips).get("TDCF_8or16", STATUSorCONF) == 0){
			bitsForITypeRegDest = 3;
			rangeForITypeRegDest = 8;
			int [] r = { 3, 4, 4, 4};
			int [] i = { 3, bitsForITypeRegDest, 4, 16-(3+bitsForITypeRegDest+4)};
			int [] j = { 3, 13};
			RTypeBitFields = r;
			ITypeBitFields = i;
			JTypeBitFields = j;
		}else /*if (((MIPS)mips).get("TDCF_8or16", STATUSorCONF) == 1)*/{
			bitsForITypeRegDest = 3;
			rangeForITypeRegDest = 8;
			int [] r = { 4, 4, 4, 4};
			int [] i = { 4, bitsForITypeRegDest, 4, 16-(4+bitsForITypeRegDest+4)};
			int [] j = { 4, 12};
			RTypeBitFields = r;
			ITypeBitFields = i;
			JTypeBitFields = j;
//System.out.println ( "setando bitfields para 16...");
		}
	}
	
	public void setRisaConfiguration ( int risaConf) {
//System.out.println ( "... reconfigurable rISA: changing configuration !");
		// risaConf igual a -1 usa a configuração da GUI
		if ( risaConf >= 0) {
			// risas 8 opcodes iniciam em zero
			// risas 16 opcodes iniciam em cem
			if ( risaConf < 100) {
				((MIPS)mips).set("TDCF_8or16", STATUSorCONF, 0);
				((MIPS)mips).set("TDCF_WhichRISA", STATUSorCONF, risaConf);	
			} else {
				((MIPS)mips).set("TDCF_8or16", STATUSorCONF, 1);
				((MIPS)mips).set("TDCF_WhichRISA", STATUSorCONF, risaConf - 100);
			}
		}
		((MIPS) mips).updateRisaInstructionMatrix ();
		updateIf8or16();
	}

	public Processor mips;
	public RodadaDeSimulacaoRisa rs;
	public Hashtable htNInstructions, htRISA, htFinalCode;
	private int iCont;
	private boolean [] regsUsed;
	//private Hashtable htCodeLabels;
	private ListOfLabels llCodeLabels;
	private boolean bAddNops;
	
	public final int SIZE_RINST = 16;
	public final int LIMIT_SIZE_OF_BLOCKS = 4;
	// Opcode de 3 bits
	///*
	public int RTypeBitFields []/* = { 3, 4, 4, 4}*/;
	public int bitsForITypeRegDest;
	public int rangeForITypeRegDest;	// 2 ^ bitsForITypeRegDest
	public int ITypeBitFields [] /*= { 3, bitsForITypeRegDest, 4, 16-(3+bitsForITypeRegDest+4)}*/;
	public int JTypeBitFields [] /*= { 3, 13}*/;
	//*/
	// Opcode de 4 bits
	/*
	public int RTypeBitFields [] = { 4, 4, 4, 4};
	public final int bitsForITypeRegDest = 3;
	public final int rangeForITypeRegDest = 8;	// 2 ^ bitsForITypeRegDest
	public int ITypeBitFields [] = { 4, bitsForITypeRegDest, 4, 16-(4+bitsForITypeRegDest+4)};
	public int JTypeBitFields [] = { 4, 12};
	*/
}
