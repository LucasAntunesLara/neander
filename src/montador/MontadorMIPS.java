package montador;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;

import processor.MIPS;
import processor.Processor;
import simdraw.MIPSApp;
import util.Define;
import util.SistNum;
import montador.ListOfLabels;

// Classe para guardar as informaoes em uma linha do programa Assembly
class LineOfCodeDLX extends LineOfCode implements Define {
	String allLine;
	int [] arguments;
	int wordsInLine;
	int RISAaddressOfThisLine;
	String targetLabel; 		// label referenciado pela instrucao
	String inlineLabel;	// label na linha
	int typeInst, rs1, rs2, rd, shamt, immediate;
	int instruction;
	boolean isNormal;
	// boolean bTranslatable;
	boolean hasLabel;	// para identificar a existencia de labels na linha. O label, propriamente dito, esta' em labelOfLine
	int reason;			// motivo pelo qual foi descartada da redução
	int methodID;		// inteiro para identificar instruçoes de um dado metodo
	boolean isNewFunction;
	String methodName;
	int iWhichRisa;
	int iForcedRisa;
	boolean isForcedRBegin;
	boolean isForcedREnd;
	
	public LineOfCodeDLX ( String parLine) {
		super ( parLine);
		allLine = parLine;
		targetLabel = null;
		//bTranslatable = false;
		isNormal = true;
		reason = ISNORMAL;
		methodID = -1;
		isNewFunction = false;
		iWhichRisa = -1;
		iForcedRisa =-1;
		isForcedRBegin = false;
		isForcedREnd = false;
	}
	
	public void list ( ) {
//TDBenchApp.messagesToUser ( null, line+"\t,\t"+lineNumber+"\t,\t"+type+"\t,\t"+argument+"\t,\t"+bytesInLine+"\t,\t"+messageError+"\t,\t"+label);
//System.out.println ( line+"\t,\topcode="+opcode+"\t,\t"+immediate+"\t,\t"+rs1+"\t,\t"+rs2+"\t,\t"+rd+"\t,\t"+label);
//if (labelRISA != null) System.out.print ( "*****>"+labelRISA+"-->\t");
if ( hasLabel) System.out.print ( "*****>"+inlineLabel+"-->\t");
else System.out.print ( "*****> no label -->\t");
System.out.println ();
System.out.print ( "Normal: "+isNormal+", opcode: "+opcode+", func: "+func+", rs1: "+rs1+", rs2: "+rs2+", rd: "+rd+", immediate: "+immediate+",\nassembly: "+allLine+" , decimal: "+instruction+" , method: "+methodName+" ,\n which method: "+methodID+" , which rISA: "+iWhichRisa+ (isNewFunction == true ? " New Function": "" )+", risa forced: "+iForcedRisa+ (isForcedRBegin == true ? "   BEGIN Forced Block": "" )+ (isForcedREnd == true ? "   END Forced Block": "" ));
if ( isNormal == true) {
	switch ( reason) {
		case ISNORMAL:
			System.out.print ( "\tIS NORMAL");
			break;
		case ISnotINtheSET:
			System.out.print ( "\tIS NOT IN THE REDUCED SET");
			break;
		case ISOverflow:
			System.out.print ( "\tWAS DISCARDED DUE TO AN OVERFLOW");
			break;
		case ISJumps:
			System.out.print ( "\tWAS DISCARDED DUE TO BRANCH HANDLING");
			break;
		case ISSmallBlock:
			System.out.print ( "\tWAS DISCARDED DUE TO THE SMALL SIZE OF THE BLOCK");
			break;
		default:
			break;
	}
}
System.out.println ();
System.out.println ( );
	}
}

public class MontadorMIPS implements Define {

	public MontadorMIPS ( Processor proc, String parFileNameOrigin, String parFileNameTarget) {
		mips = proc;
		mnemonicos = mips.getMnemonicosList();
		intOpcodes = mips.getOpcodesList();
		intFuncs = mips.getSizeInBytesList();
		fileOrigin = parFileNameOrigin;
		fileTarget = parFileNameTarget;
		alLines = new ArrayList ( );
		// Para guardar os labels de dados e de instruoes
		//htCodeLabels = new Hashtable ( );
		llCodeLabels = new ListOfLabels ( );
		htDataLabels = new Hashtable ( );
		// Para guardar mnemonicos e seus respectivos valores decimais
		htOpcodes = new Hashtable ( );
		for ( int i = 0; i < mnemonicos.length; i ++) {
			htOpcodes.put ( mnemonicos [i], new Integer ( (int) intOpcodes [ i]));
		}		
		htFuncs = new Hashtable ( );
		for ( int i = 0; i < mnemonicos.length; i ++) {
			htFuncs.put ( mnemonicos [i], new Integer ( intFuncs [ i]));
		}	
		archRegs = proc.getArchitecturalRegistersNames();
		htArchRegs = new Hashtable ( );
		for ( int i = 0; i < archRegs.length; i ++) {
			htArchRegs.put ( archRegs [i], new Integer ( i));
		}
		htAssemblyCode = new Hashtable ( );
		try {
			assembler ( );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		iMethodID = 0;
	}
		
	public static int assembleRType ( int opcode, int rs1, int rs2, int rd, int shamt, int func) {
		int iInstruction = 0, iTmp;
//System.out.println ( opcode  +","+ rs1 +","+rs2 +","+rd +","+shamt +","+func);
		if ( opcode > 63) return ( -1);
		else {
			iTmp = opcode << 26;
			iInstruction = iInstruction | iTmp;
		}
		if ( rs1 > 31) return ( -1);
		else {
			iTmp = rs1 << 21;
			iInstruction = iInstruction | iTmp;
		}
		if ( rs2 > 31) return ( -1);
		else {
			iTmp = rs2 << 16;
			iInstruction = iInstruction | iTmp;
		}
		if ( rd > 31) return ( -1);
		else {
			iTmp = rd << 11;
			iInstruction = iInstruction | iTmp;
		}
		if ( shamt > 31) return ( -1);
		else {
			iTmp = shamt << 6;
			iInstruction = iInstruction | iTmp;
		}
		if ( func > 63) return ( -1);
		else {
			iInstruction = iInstruction | func;
		}

//System.out.println ( SistNum.toBinString ( iInstruction, 32));
		
		return ( iInstruction);
	}

	public static int assembleIType ( int opcode, int rs, int rd, int immediate) {
		int iInstruction = 0, iTmp;
		
		immediate = immediate & 0x0000ffff;
		
		if ( opcode > 63) return ( -1);
		else {
			iTmp = opcode << 26;
			iInstruction = iInstruction | iTmp;
		}
		if ( rs > 31) return ( -1);
		else {
			iTmp = rs << 21;
			iInstruction = iInstruction | iTmp;
		}
		if ( rd > 31) return ( -1);
		else {
			iTmp = rd << 16;
			iInstruction = iInstruction | iTmp;
		} 
		if ( immediate > 65535) return ( -1);
		else {
			iInstruction = iInstruction | immediate;
		}

//System.out.println ( SistNum.toBinString ( iInstruction, 32));
		
		return ( iInstruction);
	}

	public static int assembleJType ( int opcode, int address) {
		int iInstruction = 0, iTmp;
//System.out.println("address = "+address);
		address = address & 0x03ffffff;
//System.out.println("address = "+address);
//System.out.println("opcode = "+opcode);	
		if ( opcode > 63) return ( -1);
		else {
			iTmp = opcode << 26;
			iInstruction = iInstruction | iTmp;
		}
		if ( address > 67108863) return ( -1);
		else {
			iInstruction = iInstruction | address;
		}

//System.out.println ( SistNum.toBinString ( iInstruction, 32));
		
		return ( iInstruction);
	}

	private String getLabel ( LineOfCodeDLX loc, String parLine, int pAddrLine) {
		String newLine = parLine, sRet = null;
		StringTokenizer stLine = new StringTokenizer ( newLine, " \t,", false);
		String [ ] sTokens = new String [ 1];
//System.out.println ( "..."+ parLine);
		if (stLine.hasMoreTokens()) sTokens [ 0] = stLine.nextToken();
		if ( sTokens [ 0].endsWith ( ":")) {
			sRet = newLine.substring ( newLine.indexOf ( ':')+1);
			if ( iSegMode == isDATAseg) htDataLabels.put ( sTokens[0].replaceAll (":", ""), new Integer (pAddrLine));
			//else htCodeLabels.put ( sTokens[0].replaceAll (":", ""), new Integer (pAddrLine));
			else {
				String sAux = sTokens[0].replaceAll (":", "");
				llCodeLabels.add ( sAux, pAddrLine);
				loc.inlineLabel = sAux;
				loc.hasLabel = true;
			}
		}
		
//System.out.println ( sRet);
//System.out.println ( htDataLabels);
//System.out.println ( htCodeLabels);
		
		return ( sRet);
	}

	private void firstStep ( LineOfCodeDLX pLoC, int pLineNumber) {
		StringTokenizer stLine;
		String [ ] sTokens = new String [ MAXTOKENS];
		int iNtokens = 0, i;
		String line = pLoC.line;
		pLoC.type = UNDEFINED;
		pLoC.lineNumber = pLineNumber;
		int iAddress = (iSegMode == isCODEseg? iAddressOfThisInst: iAddressOfThisData);
		int iSizeData = 4;

		String sTmp = getLabel ( pLoC, line, iAddress);
		if ( sTmp != null) {
			pLoC.line = line = sTmp;
			if ( iSegMode == isCODEseg) pLoC.type = isLABELandINST;
			else if ( iSegMode == isDATAseg) pLoC.type = isLABELandDATA;
		}
		stLine = new StringTokenizer ( line, " \t,()", false);

		if ( line.length() == 0) {
			pLoC.type = isONLYLABEL;
			return;
		} else {
			if ( iSegMode == isCODEseg) {
				int cont = llCodeLabels.getNumberOfLabelsAtNormalAddress(iAddress);
				pLoC.hasLabel = (cont > 0? true: false);
				pLoC.inlineLabel = llCodeLabels.getLabelAtNormalAddress ( iAddress);
			}
			while (stLine.hasMoreTokens()) {
				sTokens [ iNtokens] = stLine.nextToken();
				iNtokens  ++;
				if ( iNtokens == MAXTOKENS) break;
			}	
		}
		
		// Altera a sintaxe da JR para facilitar a montagem
		if ( sTokens [0].compareToIgnoreCase( "jr") == 0) {
			sTokens [ 2] = sTokens [ 1];
			sTokens [ 1] = "$zero";
			sTokens [ 3] = "$zero";
			iNtokens = 4;
//			System.out.println( sTokens[0]+" , "+sTokens[1]+" , "+sTokens[2]+" , "+sTokens[3]);
		}
		
		// Altera a sintaxe da NOP para facilitar a montagem
		if ( sTokens [0].compareToIgnoreCase( "nop") == 0) {
			sTokens [ 0] = "add";
			sTokens [ 1] = "$zero";
			sTokens [ 2] = "$zero";
			sTokens [ 3] = "$zero";
			iNtokens = 4;
		}
		
		// Altera a sintaxe da pseudo-instrucao MOVE para facilitar a montagem
		if ( sTokens [0].compareToIgnoreCase( "move") == 0) {
			sTokens [ 0] = "add";
			sTokens [ 3] = "$zero";
			iNtokens = 4;
		}
   
		// Altera a sintaxe da HLT para facilitar a montagem
		if ( sTokens [0].compareToIgnoreCase( "hlt") == 0) {
			sTokens [ 1] = "$zero";
			sTokens [ 2] = "$zero";
			sTokens [ 3] = "$zero";
			iNtokens = 4;
		}
		
		// Altera a sintaxe da ADDI para facilitar a montagem
		if ( sTokens [0].compareToIgnoreCase( "li") == 0) {
			sTokens [ 0] = "addi";
			sTokens [ 3] = sTokens [ 2];
			sTokens [ 2] = "$zero";
			iNtokens = 4;
		}

		/* Altera a sintaxe da BLTZ para facilitar a montagem - RETIRADO E NAO VERIFICADO EFEITOS COLATERAIS
		if ( sTokens [0].compareToIgnoreCase( "bltz") == 0) {
			sTokens [ 3] = sTokens [ 2];
			sTokens [ 2] = "$zero";
			iNtokens = 4;
		}*/

		// numero que identifica instruçoes pertencentes a um mesmo metodo
		pLoC.methodID = iMethodID;
		pLoC.methodName = sMethod;
		pLoC.iForcedRisa = iForcedRisa;
		//
		// Identifica as 4 classes de instruoes ou diretivas do prog.Assembly, atribuindo um tipo
     	for ( i = 0; i < iNtokens; i ++) {
			if ( sTokens [ i].equalsIgnoreCase( ".data") || sTokens [ i].equalsIgnoreCase( ".rdata") || sTokens [ i].equalsIgnoreCase( ".sdata")) {
				iSegMode = isDATAseg;
				pLoC.type = isORG;
				break;
			} else if ( sTokens [ i].equalsIgnoreCase( ".text") || sTokens [ i].equalsIgnoreCase( ".code")) {
				iSegMode = isCODEseg;
				pLoC.type = isORG;
				break;
			} else if ( sTokens [ i].equalsIgnoreCase( ".word")) {
				pLoC.type = isDATAWORD;
				break;
			} else if ( sTokens [ i].equalsIgnoreCase( ".byte")) {
     			pLoC.type = isDATABYTE;
     			break;
     		} else if ( 	sTokens [ i].equalsIgnoreCase( ".file") ||
     						sTokens [ i].equalsIgnoreCase( ".sdata") ||
     						sTokens [ i].equalsIgnoreCase( ".rdata") ||
							sTokens [ i].equalsIgnoreCase( ".align") ||
							//sTokens [ i].equalsIgnoreCase( ".ascii") ||
							sTokens [ i].equalsIgnoreCase( ".globl") ||
							sTokens [ i].equalsIgnoreCase( ".ent") ||
							//sTokens [ i].equalsIgnoreCase( ".frame") ||
							sTokens [ i].equalsIgnoreCase( ".mask") ||
							sTokens [ i].equalsIgnoreCase( ".fmask") ||
							sTokens [ i].equalsIgnoreCase( ".end") ||
							sTokens [ i].equalsIgnoreCase( ".comm") ||
							sTokens [ i].equalsIgnoreCase( ".lcomm") ||
							sTokens [ i].equalsIgnoreCase( ".set") ||
							sTokens [ i].equalsIgnoreCase( ".space") ||
							sTokens [ i].equalsIgnoreCase( ".risaBegin") ||
							sTokens [ i].equalsIgnoreCase( ".risaEnd")
     				) {
//     			System.out.println ( "-->"+sTokens[i]);
//				para identificar, em LineOfCodeDLX, as instruçoes pertencentes a um dado metodo
     			if ( sTokens [ i].equalsIgnoreCase( ".ent")) {
     				iMethodID ++; 
     				sMethod = sTokens [1];
     				// talvez adicionar tratamento de erro
     			}
     			if ( sTokens [ i].equalsIgnoreCase( ".risaBegin")) {
     				iForcedRisa = Integer.parseInt ( sTokens [ 1], 10);
     			}
     			if ( sTokens [ i].equalsIgnoreCase( ".risaEnd")) {
     				iForcedRisa = - 1;
     			}
     			//
     			pLoC.type = isNOTUSEDYET;
     			break;
     		}
        }
     	if ( pLoC.type == UNDEFINED) pLoC.type = isINSTRUCTION;
     	
     	// Faz a consistencia das linhas do programa conforme o seu tipo
     	switch ( pLoC.type) {
     		case isORG:
				pLoC.wordsInLine = 0;
     			break;
     			
     		case isDATABYTE:
     			iSizeData = 1;
     		case isDATAWORD:
     			if ( 	iNtokens >= 2 && iSegMode == isDATAseg) {
					try {
						pLoC.arguments = new int [iNtokens-1];
						for ( int j = 0; j < iNtokens - 1; j ++) {
							Integer iTmp = (Integer) htDataLabels.get(sTokens[j+1]);	// no caso de .word LABEL
							if ( iTmp != null) {
//System.out.println (sTokens[j+1]+ " = "+ iTmp.intValue());
								pLoC.arguments [j] = iTmp.intValue();
							}
							else pLoC.arguments [j] = Integer.parseInt ( sTokens [j+1]);
//DLXApp.messagesToUser ( null, sTokens [j+1]);
						}
   					} catch (Exception e) {
       					pLoC.messageError = "Invalid arguments at line  "+pLoC.lineNumber;
					}	
   					pLoC.wordsInLine = iNtokens - 1;
   				} else pLoC.messageError = "Invalid instruction at line  "+pLoC.lineNumber;
     			break;
  
     		case isLABELandINST: 			
     		case isINSTRUCTION:
     			int iTypeTmp = RTYPE;
     			if ( 	iNtokens == 4 && iSegMode == isCODEseg) {
   					Integer Iopcode = (Integer) htOpcodes.get(sTokens[0]);
//if (sTokens [0].equalsIgnoreCase("slli")) System.out.println( "opcode ="+Iopcode);
					if ( Iopcode != null) { 
						pLoC.opcode = Iopcode.intValue();
     					Integer IFunc = (Integer) htFuncs.get(sTokens[0]);
//System.out.println (sTokens[0]);
						pLoC.func = IFunc.intValue();
//System.out.println (pLoC.func);
						if (sTokens[1].startsWith("$")) {
							Integer Ireg = (Integer) htArchRegs.get ( sTokens [1]);
							if ( Ireg != null) {
//DLXApp.messagesToUser ( null, sTokens[1]+","+Ireg.toString());
								sTokens [1] = Ireg.toString();
							}
							else sTokens[1]=sTokens[1].substring(1);
						}
						if (sTokens[2].startsWith("$")) {
							Integer Ireg = (Integer) htArchRegs.get ( sTokens [2]);
							if ( Ireg != null) {
//DLXApp.messagesToUser ( null, sTokens[2]+","+Ireg.toString());
								sTokens [2] = Ireg.toString();
							}
							else sTokens[2]=sTokens[2].substring(1);
						} else iTypeTmp = ITYPE;
						if (sTokens[3].startsWith("$")&&htDataLabels.get(sTokens[3])==null&&llCodeLabels.getNormalAddress(sTokens[3])==-1) {
							Integer Ireg = (Integer) htArchRegs.get ( sTokens [3]);
							if ( Ireg != null) {
//DLXApp.messagesToUser ( null, sTokens[3]+","+Ireg.toString());
								sTokens [3] = Ireg.toString();
							} else {
								try {
									Integer.parseInt ( sTokens[3].substring(1)); 
									sTokens[3]=sTokens[3].substring(1);
								} catch ( Exception e) {
									// it is a label whose name begins with $
									iTypeTmp = ITYPE;
									// coloca o imediato em sTokens[2] para um tratamento uniforme
									String sTmpx = sTokens[2];
									sTokens[2]=sTokens[3];
									sTokens[3]=sTmpx;
								}
							}
						} else {
							iTypeTmp = ITYPE;
							// coloca o imediato em sTokens[2] para um tratamento uniforme
							String sTmpx = sTokens[2];
							sTokens[2]=sTokens[3];
							sTokens[3]=sTmpx;
						}
						//
   						try {
   							switch ( iTypeTmp) {
   								case RTYPE:
   									pLoC.typeInst = RTYPE;
   									try {
   										pLoC.rs1 = Integer.parseInt ( sTokens [2]);
   										pLoC.rs2 = Integer.parseInt ( sTokens [3]);
   										pLoC.rd = Integer.parseInt ( sTokens [1]);
   										pLoC.instruction = assembleRType (pLoC.opcode, pLoC.rs1, pLoC.rs2, pLoC.rd, pLoC.shamt, pLoC.func);
   									} catch ( Exception e) {
   				       					pLoC.messageError = "Invalid arguments at line  "+pLoC.lineNumber;
   									}
   									break;
   								case ITYPE:
   									pLoC.typeInst = ITYPE;
   									try {
   										pLoC.rs1 = Integer.parseInt ( sTokens [3]);
   									} catch ( Exception e) {
   				       					pLoC.messageError = "Invalid arguments at line  "+pLoC.lineNumber+","+sTokens[3];
   									}
   									//try {
   										try {
   											pLoC.immediate = Integer.parseInt ( sTokens [2]);
   										} catch (Exception e)  {
   											// o imediato pode estar em hexadecimal
   											if ( sTokens[2].startsWith("0x"))
   												pLoC.immediate = Integer.parseInt ( sTokens [2].substring(2), 16);
   											else pLoC.targetLabel = sTokens [2];
   	   									}
   									//} catch (Exception e)  {
   										//pLoC.label = sTokens [2];
   									//}
   									try {
   										pLoC.rd = Integer.parseInt ( sTokens [1]);
   									} catch ( Exception e) {
   				       					pLoC.messageError = "Invalid arguments at line  "+pLoC.lineNumber+","+sTokens[1];
   									}
   									pLoC.instruction = assembleIType (pLoC.opcode, pLoC.rs1, pLoC.rd, pLoC.immediate);
   									break;
   								default:
   									break;
   							}
       					} catch (Exception e) {
       						pLoC.messageError = "Invalid instruction at line  "+pLoC.lineNumber;
   						}	
       					pLoC.wordsInLine = 1;
     				} else pLoC.messageError = "Invalid instruction at line  "+pLoC.lineNumber+":"+sTokens[0];
     			} else if ( iNtokens == 2 && iSegMode == isCODEseg) {
   					Integer Iopcode = (Integer) htOpcodes.get(sTokens[0]);
 //System.out.println ( sTokens[0]);
 //System.out.println ( Iopcode.intValue());
					if ( Iopcode != null) {
						pLoC.opcode = Iopcode.intValue();
						pLoC.typeInst = JTYPE;
						try {
							pLoC.immediate = Integer.parseInt ( sTokens [1]);
						} catch (Exception e)  {
							pLoC.targetLabel = sTokens [1];
						}
						pLoC.instruction = assembleJType (pLoC.opcode, pLoC.immediate);
     					pLoC.wordsInLine = 1;
					} else pLoC.messageError = "Invalid instruction at line  "+pLoC.lineNumber+":"+sTokens[0];
     			} else {
     				pLoC.type = UNDEFINED;
     				pLoC.messageError = "Instruction in the data segment at line  "+pLoC.lineNumber;
     			}
     			break;

     		case isLABELandDATA:
				pLoC.wordsInLine = 0;	    			
     			break;
     			
     		case isNOTUSEDYET:
				pLoC.wordsInLine = 0;
     			break;
     			
     		default:

     			break;
     	}
     	
     	// Guarda o endereo de inicio da instruao corrente
     	pLoC.addressOfThisLine = iAddress;
     	//pLoC.hasLabel = htCodeLabels.containsValue(new Integer (iAddress));
     	//pLoC.hasLabel = llCodeLabels.hasLabelAtNormalAddress(iAddress);
     	// Incrementa o endereo conforme o nro. de bytes da instruao corrente
     	if ( iSegMode == isCODEseg) iAddressOfThisInst += pLoC.wordsInLine;
     	else iAddressOfThisData += ( pLoC.wordsInLine * iSizeData);
     	if ( pLoC.type == UNDEFINED) pLoC.messageError = "Instruo invlida na linha "+pLoC.lineNumber+":"+line;
	}

	private void secondStep ( LineOfCodeDLX pLoC, PrintWriter output) {
		if ( bTmp) {
			// temp
			// output.println ( "pc="+iProgramBaseAddress);
			bTmp = false;
		}
//DLXApp.messagesToUser ( null, "*******************************************");		
		switch ( pLoC.type) {
			case isONLYLABEL:
 			case isNOTUSEDYET:
			case isORG:

 				break;
 			
 			case isDATAWORD:
 				int iInitialAddress = pLoC.addressOfThisLine + iDataBaseAddress;
//DLXApp.messagesToUser ( null, "-->"+pLoC.arguments.length);
 				for ( int i = 0; i < pLoC.arguments.length; i ++) {
 					byte [] bAux = SistNum.splitDoubleInto4Bytes (pLoC.arguments[i]);
 					for ( int j = 0; j < 4; j ++) {
 						output.println ( "dmemory["+(iInitialAddress+j)+"]="+bAux[j]);
//DLXApp.messagesToUser ( null, "dmemory["+(iInitialAddress+j)+"]="+bAux[j]);
 					}
 					iInitialAddress += 4;
 				}
 				break;
 
 			case isDATABYTE:
 				int iBInitialAddress = pLoC.addressOfThisLine+iDataBaseAddress;
 				for ( int i = 0; i < pLoC.arguments.length; i ++, iBInitialAddress++) {
					output.println ( "dmemory["+(iBInitialAddress)+"]="+pLoC.arguments[i]);
 //DLXApp.messagesToUser ( null, "dmemory["+(iBInitialAddress)+"]="+pLoC.arguments[i]);
 				}
 				break;
 
 			case isLABELandINST:
 			case isINSTRUCTION:
 				if ( pLoC.targetLabel != null) {
 					if (pLoC.typeInst == ITYPE || pLoC.typeInst == JTYPE) {
 						Integer Iimm = (Integer) htDataLabels.get ( pLoC.targetLabel);
 						//if ( Iimm == null)Iimm = (Integer) htCodeLabels.get ( pLoC.label);
 						if ( Iimm == null){
 							int addr = llCodeLabels.getNormalAddress(pLoC.targetLabel);
 							if ( addr == - 1) Iimm = null;
 							else {
 								int iOffset = 0;
 			 					//System.out.println( "label = "+pLoC.label);
 			 					//System.out.println( "linha = " +pLoC.allLine);
 								Iimm = new Integer ( addr);
 			 					//System.out.println( "valor = " +addr);
 			 					//System.out.println( "address desta linha = " +pLoC.addressOfThisLine);
 								if ( mips.getClass ( ).getName ( ).equals ( "processor.MIPSMulti")) {
 									if ( pLoC.typeInst == JTYPE) iOffset = 2;
 									else iOffset = 1;
 								} else iOffset = 1;
 			 					pLoC.immediate = addr - (pLoC.addressOfThisLine + iOffset);
 			 					//pLoC.immediate = Iimm.intValue();
 			 					//System.out.println ( "offset = "+pLoC.immediate);
 							}
 						} else pLoC.immediate = Iimm.intValue();
 						if ( Iimm != null) {
 							// pLoC.immediate = Iimm.intValue();
 							if (pLoC.typeInst == ITYPE) {
 								pLoC.instruction = assembleIType (pLoC.opcode, pLoC.rs1, pLoC.rd, pLoC.immediate);
 							}
 							else pLoC.instruction = assembleJType (pLoC.opcode, pLoC.immediate);
 						} else {
 							// tratamento de labels com deslocamento. Ex.: results + 4
 							StringTokenizer stLine;
 							String [ ] sTokens = new String [ 2];
 							int iNtokens = 0;
 							stLine = new StringTokenizer ( pLoC.targetLabel, "+-", false);
							while (stLine.hasMoreTokens()) {
									sTokens [ iNtokens] = stLine.nextToken();
//System.out.println( iNtokens+" = "+sTokens [iNtokens]);
 									iNtokens  ++;
 									if ( iNtokens == 2) break;
 							}
							if ( iNtokens == 2) {
		 						Iimm = (Integer) htDataLabels.get ( sTokens [0]);
		 						if ( Iimm != null) {
		 							if ( pLoC.targetLabel.indexOf("+") != - 1) {
		 								pLoC.immediate = Iimm.intValue() + Integer.parseInt(sTokens[1]);
//System.out.println ( "label + algo: "+pLoC.label);
		 							}
		 							else {
//System.out.println ( "label - algo: "+pLoC.label);
		 								pLoC.immediate = Iimm.intValue() - Integer.parseInt(sTokens[1]);
		 							}
		 							if (pLoC.typeInst == ITYPE) {
		 								pLoC.instruction = assembleIType (pLoC.opcode, pLoC.rs1, pLoC.rd, pLoC.immediate);
		 							}
		 							else pLoC.instruction = assembleJType (pLoC.opcode, pLoC.immediate);
		 						} else pLoC.messageError = "Inexistent label at line  "+pLoC.lineNumber+":"+pLoC.targetLabel;
							} else pLoC.messageError = "Inexistent label at line  "+pLoC.lineNumber+":"+pLoC.targetLabel;
 						}
 					}
 				}
 				// rISA one line
 				// if using Reduced ISA, it'll generate the code later
 				if ( !mips.usingRISA()) {
 					int iAddressTmp = iProgramBaseAddress + pLoC.addressOfThisLine;
 					output.println ( "imemory["+iAddressTmp+"]="+pLoC.instruction);
 //System.out.println ( "imemory["+iAddressTmp+"]="+pLoC.line);
 					htAssemblyCode.put ( new Integer ( iAddressTmp), pLoC);
//DLXApp.messagesToUser ( null, "imemory["+pLoC.addressOfThisLine+"]="+pLoC.instruction);
 				}
 				break;
		}
	}
	
	private String removeComments ( String sLine) {
		int posComment;
		
		posComment = sLine.indexOf( '#');
		if ( posComment == - 1) return sLine;
		else {
//DLXApp.messagesToUser ( "--> before: ", sLine);	
//DLXApp.messagesToUser ( "--> after: ", sLine.substring( 0, posComment-1));	
			if ( sLine.indexOf( '#') == 0) return "";
			else return sLine.substring( 0, posComment-1);
		}
	}
	
	private boolean isEmpty ( String sLine) {
		if ( sLine.length() == 0) return true;
		sLine = sLine.trim ( );
//System.out.println ( "All line: "+sLine);
//System.out.println ( "First char: "+sLine.charAt(0));
		if ( sLine.indexOf('\t') == 0) {
//DLXApp.messagesToUser ( "retirando tabs...", sLine);
			sLine = sLine.substring ( 1);
		}
		if ( sLine.length() == 0) return true;
		
		return false;
	}
	
	private byte [] changeAsciiToByte ( String str)
	{
		String sAux = str;
		byte [] bAux, bChars;
		int i;
		// remove aspas e '\000' do final
		try {
			sAux = str.substring(1, str.length()-5);
		} catch ( Exception e) {
			// em algumas circunstancias, nao vem com o \000 no final
			sAux = str.substring(1, str.length()-1);
//System.out.println ( "******************* ficou: "+sAux);
		}
//System.out.println ( "******************* ficou: "+sAux);
		
		bAux = sAux.getBytes();
		bChars = new byte [ bAux.length+1];
		for(i=0; i < bAux.length; i++) {
			bChars [i] = bAux [i];
		}
		bChars [i] = 0;
		
		String strTst = new String ( bChars);
//System.out.println("***************** ficou:"+ strTst);		
		
		return ( bChars);
	}
	
	private void getDimensionsFromGccFileFormat ( BufferedReader d) throws IOException {
		String line;
		StringTokenizer stLine;
		String [ ] sTokens = new String [ MAXTOKENS];
		int iNtokens = 0, i;
		
		while ( ( line = d.readLine ( )) != null) {
			stLine = new StringTokenizer ( line, " \t,()", false);

			if ( line.length() == 0) {
				continue;
			} else {
				iNtokens = 0;
				while (stLine.hasMoreTokens()) {
					sTokens [ iNtokens] = stLine.nextToken();
					iNtokens  ++;
					if ( iNtokens == MAXTOKENS) break;
				}
			}	
//System.out.println ( sTokens[0]);		
			// Define o tamanho a ser armazenado para a pilha
			if ( sTokens[0].equalsIgnoreCase(".frame")) {
				iStackSize = iStackSize + Integer.parseInt(sTokens[2]);
				//System.out.println("iStackSize = "+iStackSize);
				continue;
			}
		}
	}
	
	private void processGccFileFormat ( BufferedReader d) throws IOException {
		String line;
		StringTokenizer stLine;
		String [ ] sTokens = new String [ MAXTOKENS];
		int iNtokens = 0, i;
		String fileOriginGCC = fileOrigin+".gcc";
		File fileGCC = new File(fileOriginGCC); 
		FileWriter writerGCC = new FileWriter(fileOriginGCC);
		PrintWriter outputGCC = new PrintWriter(writerGCC,true);
		String sMethod = null;
		// variaveis para tratar as sequencias .ascii, .space
		boolean bIsAscii = false;
		int iZerosAppended = 0;
		
		while ( ( line = d.readLine ( )) != null) {
			stLine = new StringTokenizer ( line, " \t,()", false);

			if ( line.length() == 0) {
				continue;
			} else {
				iNtokens = 0;
				if (line.startsWith("\t.ascii")) {
					sTokens [0] = new String ( ".ascii");
					sTokens [1] = new String ( line.substring( 8));
					iNtokens = 2;
//System.out.println ( "...>" + sTokens[0]);
//System.out.println ( "...>" + sTokens[1]);
				} else {
					while (stLine.hasMoreTokens()) {
						sTokens [ iNtokens] = stLine.nextToken();
						//System.out.println ( sTokens [iNtokens]);
						iNtokens  ++;
						if ( iNtokens == MAXTOKENS) break;
					}
				}	
			}		
			
//System.out.println ( sTokens[0]);				
			if ( sTokens[0].equalsIgnoreCase(".ent") && iNtokens == 2) {
				sMethod = sTokens [ 1];
			}

			// retira o $ do inicio dos labels
			for ( i = 0; i < iNtokens; i ++) {	
				if ( sTokens [i].startsWith("$L")) {
					line = new String ( line.replaceAll("[$]L", "L"));
					break;
				}
			}
		
			// suprime o jal __main
			if ( sTokens[0].equalsIgnoreCase("jal") && sTokens[1].equalsIgnoreCase("__main") ) continue;
		
			// suprime o nop
			if ( sTokens[0].equalsIgnoreCase("nop") ) continue;

			// substitui a inicializacao do stack pointer: subu por addi
			if ( sTokens[0].equalsIgnoreCase("subu") && sTokens[1].equalsIgnoreCase("$sp") && sTokens[2].equalsIgnoreCase("$sp") ) {
				if ( sMethod != null && sMethod.compareToIgnoreCase("main") == 0) {
					// inicializa stack pointer
					String sTmp = new Integer (iStackSize+30720).toString();
					outputGCC.println( "\taddi\t$sp,$zero,"+sTmp);
				}
				line = new String ( line.replaceAll("subu", "addi"));
				line = new String ( line.replaceAll(sTokens[3], "-"+sTokens[3]));
			}

			// substitui addu por addi ou por add
			if ( sTokens[0].equalsIgnoreCase("addu")) {
				if ( sTokens[3].startsWith("$") == false) line = new String ( line.replaceAll("addu", "addi"));
				else line = new String ( line.replaceAll("addu", "add"));
			}
		
			// substitui slt por slti
			if ( sTokens[0].equalsIgnoreCase("slt") && sTokens[3].startsWith("$") == false) line = new String ( line.replaceAll("slt", "slti"));

			// substitui j $31 por hlt na rotina main
			if ( 	sTokens[0].equalsIgnoreCase("j") && sTokens[1].equalsIgnoreCase("$31")) {
				if ( sMethod != null && sMethod.compareToIgnoreCase("main") == 0) line = new String ( "\thlt");
				else {
					line = new String ( line.replaceAll("j", "jr"));
				}
			}

			// substitui la por lw
			if ( sTokens[0].equalsIgnoreCase("la") && iNtokens == 3) {
				line = new String ( line.replaceAll("la", "addi")) + "($zero)";
			}
		
			// suprime o jal printf
			if ( sTokens[0].equalsIgnoreCase("jal") && sTokens[1].equalsIgnoreCase("printf") ) continue;
		
			// suprime o jal putchar
			if ( sTokens[0].equalsIgnoreCase("jal") && sTokens[1].equalsIgnoreCase("putchar") ) continue;

			// substitui lbu por lb
			if ( sTokens[0].equalsIgnoreCase("lbu") && iNtokens == 4) {
				line = new String ( line.replaceAll("lbu", "lb"));
			}
		
			// conserta sw com variavel global apendando $zero
			if ( sTokens[0].equalsIgnoreCase("sw") && iNtokens == 3) line = line + "($zero)";

			// conserta lw com variavel global apendando $zero
			if ( sTokens[0].equalsIgnoreCase("lw") && iNtokens == 3) line = line + "($zero)";

			// substitui sll por slli
			if ( sTokens[0].equalsIgnoreCase("sll")) {
				if ( sTokens[3].startsWith("$") == false) line = new String ( line.replaceAll("sll", "slli"));
			}

			// substitui srl por srli
			if ( sTokens[0].equalsIgnoreCase("srl")) {
				if ( sTokens[3].startsWith("$") == false) line = new String ( line.replaceAll("srl", "srli"));
			}
			
			// substitui sra por srai
			if ( sTokens[0].equalsIgnoreCase("sra")) {
				if ( sTokens[3].startsWith("$") == false) line = new String ( line.replaceAll("sra", "srai"));
			}
			
			// substitui o jal memcpy pela chamada da memcpy2
			if ( sTokens[0].equalsIgnoreCase("jal") && sTokens[1].equalsIgnoreCase("memcpy") ) {
				line = new String ( line.replaceAll("memcpy", "memcpy2"));
			}
			
			// substitui o jal strlen pela chamada da strlen2
			if ( sTokens[0].equalsIgnoreCase("jal") && sTokens[1].equalsIgnoreCase("strlen") ) {
				line = new String ( line.replaceAll("strlen", "strlen2"));
			}
/*
			// substitui o .comm por labels
			if ( sTokens[0].equalsIgnoreCase(".comm") && iNtokens == 3 ) {
				String sType = null;
				if ( sTokens [2].equalsIgnoreCase("4")) sType = ".word";
				line = new String ( "\t.data\n"+sTokens [1]+":\t"+sType+"\t0");
//System.out.println("comm - "+line);
			}
*/			
			// substitui o .lcomm e o .comm por labels
			if ( ( sTokens[0].equalsIgnoreCase(".lcomm") || sTokens[0].equalsIgnoreCase(".comm")) && iNtokens == 3 ) {
				String sType = null;
				int iSize = 0, iTmp, iNtimes = 0;
				try {
					iSize = Integer.parseInt(sTokens[2]);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				iTmp = iSize % 4;
				if ( iTmp == 0) {
					sType = ".word";
					iNtimes = iSize / 4;
				}
				line = new String ( "\t.data\n"+sTokens [1]+":\t"+sType+"\t0");
				for ( i = 0; i < iNtimes - 1; i ++)
					line = line + ",0";
//System.out.println("lcomm - "+iNtimes);
			}
			
			// substitui o .ascii por .byte
			if ( sTokens[0].equalsIgnoreCase(".ascii") && iNtokens == 2 ) {
				StringBuffer sbLine = new StringBuffer ( );
				byte [] bAux = changeAsciiToByte(sTokens[1]);
				sbLine.append( "\t.byte\t");
				for(i=0; i < bAux.length; i++) {
					sbLine.append( bAux[i]);
					sbLine.append( ",");
				}
				// alinha a string
				while ( i % 4 != 0) {
					sbLine.append( "0,");					
					i ++;
					iZerosAppended ++;
				}
				line = sbLine.toString();
//System.out.println("***************** ficou:"+ line);
				bIsAscii = true;
			}
			
			// substitui o .space por .byte. O .space vem logo apos um .ascii. Ele deve considerar os '\0' ja' inseridos pelo .ascii
			if ( sTokens[0].equalsIgnoreCase(".space") && iNtokens == 2 && bIsAscii) {
//System.out.println("***************** era:"+ line);
				StringBuffer sbLine = new StringBuffer ( );
				int iZerosAppendedBySpace = Integer.parseInt(sTokens [1]);
				sbLine.append( "\t.byte\t");
				for(i=0; i < iZerosAppendedBySpace - iZerosAppended; i++) {
					sbLine.append( "0");
					sbLine.append( ",");
				}
				line = sbLine.toString();
				iZerosAppended = 0;
				bIsAscii = false;
//System.out.println("***************** ficou:"+ line);
			}
			
			// conserta os endereos desalinhados das lwl e swl
			if ( ( sTokens[0].equalsIgnoreCase("swl") || sTokens[0].equalsIgnoreCase("lwl")) && iNtokens == 4) {
//System.out.println("iNtokens = "+iNtokens);
				int iAddress = Integer.parseInt(sTokens [2]);
				while ( iAddress % 4 != 0) -- iAddress;
				sTokens [2] = new Integer ( iAddress).toString();
				line = new String ( "\t"+sTokens[0]+"\t"+sTokens[1]+","+sTokens[2]+","+sTokens[3]);
//for ( i = 0; i < iNtokens; i ++) System.out.println(sTokens[i]);
//System.out.println("line = "+line);
			}
			
			// acrescenta -> $zero <- ao blez 
			// acrescenta -> $zero <- ao bgtz, ou ao bltz
			if ( (sTokens[0].equalsIgnoreCase("bgtz") || sTokens[0].equalsIgnoreCase("bltz") || sTokens[0].equalsIgnoreCase("blez")) && iNtokens == 3 ) {
				sTokens [3] = sTokens [2];
				sTokens [2] = sTokens [1];
				sTokens [1] = "$zero";
				line = new String ( "\t"+sTokens[0]+"\t"+sTokens[1]+","+sTokens[2]+","+sTokens[3]);
				line = new String ( line.replaceAll("[$]L", "L"));
//System.out.println("line = "+line);
			}
			
			/* acrescenta -> $zero <- ao bgtz, ou ao bltz
			if ( (sTokens[0].equalsIgnoreCase("bgtz") || sTokens[0].equalsIgnoreCase("bltz")) && iNtokens == 3 ) {
				sTokens [3] = sTokens [2];
				sTokens [2] = "$zero";
				line = new String ( "\t"+sTokens[0]+"\t"+sTokens[1]+","+sTokens[2]+","+sTokens[3]);
				line = new String ( line.replaceAll("[$]L", "L"));
//System.out.println("line = "+line);
			}*/
			
			// suprime o .frame
			if ( sTokens[0].equalsIgnoreCase(".frame")) continue;

			if ( line != null) outputGCC.println( line);
		}

		outputGCC.close ( );
	}
	
	private void assembler ( ) throws Exception {
			BufferedReader d = new BufferedReader ( new FileReader ( fileOrigin));
			File file = new File(fileTarget); 
			FileWriter writer = new FileWriter(fileTarget);
			PrintWriter output = new PrintWriter(writer,true);
			String sInput;
			int line = 0;
			boolean error = false;

			/* para programas Assembly provenientes do GCC
			String fileOriginGCC = fileOrigin+".gcc";
			File fileGCC = new File(fileOriginGCC); 
			FileWriter writerGCC = new FileWriter(fileOriginGCC);
			PrintWriter outputGCC = new PrintWriter(writerGCC,true);
			while ( ( sInput = d.readLine ( )) != null) {
				sInput = processGccFileFormat ( sInput);
				if ( sInput != null) outputGCC.println( sInput);
			}				
			outputGCC.close ( );
			*/
			
			getDimensionsFromGccFileFormat ( d);
			d.close ();
			d = new BufferedReader ( new FileReader ( fileOrigin));
			processGccFileFormat ( d);
			String fileOriginGCC = fileOrigin+".gcc";
			d = new BufferedReader ( new FileReader ( fileOriginGCC));
//System.out.println ( "no montador: "+fileTarget);
			while ( ( sInput = d.readLine ( )) != null) {
				sInput = removeComments ( sInput);
				if ( isEmpty ( sInput)) continue;
				// comentarios antes de integrar com o gcc iniciavam com //
				if (!(sInput.startsWith("//"))) alLines.add ( new LineOfCodeDLX ( sInput));
			}	
			
/*			for ( int i = 0; i < alLines.size(); i ++) {
				LineOfCodeDLX loc = (LineOfCodeDLX) alLines.get( i);
DLXApp.messagesToUser ( null, loc.allLine);
			}*/
			
			for ( int i = 0; i < alLines.size(); i ++) {
				LineOfCodeDLX loc = (LineOfCodeDLX) alLines.get( i);
				firstStep(loc,i);
				if ( loc.messageError != null) error = true;
			}			
		
//System.out.println ( htDataLabels);
//System.out.println ( htCodeLabels);
			
			if ( ! error) {
				// rISA begin ////////////////////////////////////////////////////////////////////////////////////////
				//
				//
				//
				//
				if (mips.usingRISA ( )) {
					for ( int i = 0; i < alLines.size(); i ++) {
						LineOfCodeDLX loc = (LineOfCodeDLX) alLines.get( i);
						if ( loc.type == isLABELandINST || loc.type == isINSTRUCTION) mips.rISA.addOpcode( loc);
					}
					if ( mips.rISA.isPossibleMapRegisters()) {
MIPSApp.messagesToUser ( null, "Using rISA!");	
						mips.rISA.mapRegisters ( );
					} else {
						error = true;
MIPSApp.messagesToUser ( null, "Impossible to use rISA due to the reduced number of registers!");						
					}
					// This code is necessary since the registers employed by instructions were changed
					for ( int i = 0; i < alLines.size(); i ++) {
						LineOfCodeDLX loc = (LineOfCodeDLX) alLines.get( i);
						if ( loc.type == isLABELandINST || loc.type == isINSTRUCTION) {
							if ( loc.typeInst == RTYPE) loc.instruction = assembleRType (loc.opcode, loc.rs1, loc.rs2, loc.rd, loc.shamt, loc.func);
							else if ( loc.typeInst == ITYPE) loc.instruction = assembleIType (loc.opcode, loc.rs1, loc.rd, loc.immediate);
						}
					}
				}
				//
				//
				//
				//
				// rISA end
				for ( int i = 0; i < alLines.size(); i ++) {
					LineOfCodeDLX loc = (LineOfCodeDLX) alLines.get( i);
					// generate code in T&D-Bench internal format
					secondStep (loc, output);
					if ( loc.messageError != null) error = true;
				}
			} 
			
			if ( error) {
				for ( int i = 0; i < alLines.size(); i ++) {
					LineOfCodeDLX loc = (LineOfCodeDLX) alLines.get( i);
					MIPSApp.messagesToUser ( null, loc.allLine);
					if (loc.messageError != null) MIPSApp.messagesToUser ( "ERROR", loc.messageError);
				}	
				MIPSApp.messagesToUser ( null, "Errors during assembling!");
			} else {
				Montador.setArchitecture ( mnemonicos, intOpcodes, intFuncs);
				Montador.setAssemblyCode(htAssemblyCode);
				for ( int i = 0; i < alLines.size(); i ++) {
					LineOfCodeDLX loc = (LineOfCodeDLX) alLines.get( i);
					MIPSApp.messagesToUser ( null, loc.allLine);
				}
				// rISA begin ////////////////////////////////////////////////////////////////////////////////////////
				//
				//
				//
				//
				if (mips.usingRISA ( )) {
					//
					// ((MIPS) mips).updateRisaInstructionMatrix ();
					// mips.rISA.updateIf8or16();
					//
					// mips.rISA.setRisaConfiguration(-1);
					mips.rISA.delimitFunctions();
					mips.rISA.delimitForcedRisaBlocks ( );
					// reconfigurable rISA
					mips.rISA.reconfigurableRisa ( mips.rISA.getReconfigurableRisa ());
					//for ( int ctrlLoop = 9; ctrlLoop <= 11; ctrlLoop ++)
						//mips.rISA.forceRisa ( ctrlLoop, 104);
					//
					mips.rISA.setListOfLabels( llCodeLabels);
					mips.rISA.markCandidates ( );
					mips.rISA.isPossibleToReduceCandidates();
					mips.rISA.discardSmallBlocks ();
					while ( mips.rISA.treatBranchesAndJumps ( )) {
						//System.out.println ( "loop treatBranchesAndJumps");
						mips.rISA.discardSmallBlocks ();
					}
					mips.rISA.countFinalBlocks ();
					//dlx.rISA.markBeginAndSizeOfBlocks ( ); // DESCARTADA: substituida pela DISCARDSMALLBLOCKS
					mips.rISA.listBlocks();
					//
					// System.out.println(htCodeLabels);
					// dlx.rISA.translateToRISAstep1_Opt1 ( );
					mips.rISA.translateToRISAstep1_Opt2 ( );
					// dlx.rISA.translateToRISAstep1_Opt3 ( ); // DESCARTADA
					mips.rISA.translateToRISAstep2 ( iProgramBaseAddress);
					//mips.rISA.listRISAOpcodes ( 0, 200);
					//mips.rISA.listRISAOpcodes ( 400, 450);
					//mips.rISA.listRISAOpcodes ( );
					//mips.rISA.listRISAOpcodes ( 100, 110);
					//dlx.rISA.listRISAOpcodes ( 0);
					mips.rISA.generateFinalCode ( output);
					//System.exit ( 1);
					// 
					//llCodeLabels.list ( );	
					// dlx.listRegZipNumberForItypeInstr ( );
				}
				//
				//
				//
				//
				// rISA end
			}
			MIPSApp.messagesToUser ( null, "\n\n");
			output.close ();
			writer.close ();
	}
	
	public static void main(String[] args) throws Exception{
		int i;
		String sNameFileOrigin=null, sNameFileTarget=null;

		for ( i = 0; i < args.length; i ++) {
			System.out.println( "argumentos: "+args [i]);
		}
		
		sNameFileOrigin = ".\\processors\\DLX\\programs\\test.asm";
		sNameFileTarget = ".\\processors\\DLX\\programs\\test.bin";
			
		MontadorMIPS mmn = new MontadorMIPS ( null, sNameFileOrigin, sNameFileTarget);
	}	

	Processor mips;
	String fileOrigin, fileTarget;
	ArrayList alLines;
	//Hashtable htCodeLabels;
	ListOfLabels llCodeLabels;
	Hashtable htDataLabels, htOpcodes, htSize, htFuncs, htArchRegs;
	static Hashtable htAssemblyCode; 
	int iProgramBaseAddress = 0;
	int iDataBaseAddress = 0;
	int iAddressOfThisInst; // = iProgramBaseAddress;
	int iAddressOfThisData; // = iDataBaseAddress;
	int iStackSize = 0;
	boolean bTmp = true;
	int iSegMode;
	int iMethodID;
	int iForcedRisa = -1;
	String sMethod;
	
	final int isORG 			= 100;
	public static final int isINSTRUCTION 	= 101;
	final int isDATAWORD			= 102;
	final int isDATABYTE			= 103;
	public static final int isLABELandINST	= 104;
	final int isLABELandDATA	= 105;
	final int isONLYLABEL		= 106;
	final int isNOTUSEDYET		= 107;
	final int UNDEFINED			= -1;
	final int MAXTOKENS			= 1500;
	//
	final int isDATAseg			= 200;
	final int isCODEseg			= 201;
	
	String mnemonicos [ ]; 
	long intOpcodes [ ]; 
	int intFuncs [ ];
	//
	//int typeOfLines [ ] 	= { isORG, isINSTRUCTION, isDATAWORD, isLABELandINST, isNOTUSEDYET};
	private String archRegs [ ] = null;
}