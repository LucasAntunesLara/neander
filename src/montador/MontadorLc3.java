package montador;

import processor.Processor;
import simdraw.lc3App;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;

class LineOfCodeLc3 extends LineOfCode {
	int bytesInLine;
	String label = null;
	/**
	 * BaseR (Base Register), DR (Destination Register), SR (Source Register), SR1, SR2 = contêm endereços de registradores
	 * imm5 (5-bit immediate) = contém valor imediato ou literal
	 */
	int BaseR, DR, imm5, offset6, PCoffset9, PCoffset11, SR, SR1, SR2, trapvect8, n, z, p, instruction;
	byte [] stringz;
	
	public LineOfCodeLc3 (String parLine) {
		super (parLine);
	}
	
	public void list ( ) {

	}
}

public class MontadorLc3 {
	
	public MontadorLc3(Processor proc, String parFileNameOrigin, String parFileNameTarget, String parMemName){
		
		mnemonicos = proc.getMnemonicosList();
		intOpcodes = proc.getOpcodesList();
		sizeBytes = proc.getSizeInBytesList();
		fileOrigin = parFileNameOrigin;
		fileTarget = parFileNameTarget;
		sMemName = parMemName;
		alLines = new ArrayList();
		// Para guardar os labels de dados e rotulos de endereco
		htAddressLabels = new Hashtable ( );
		// Para guardar mnemonicos e seus respectivos valores decimais
		htOpcodes = new Hashtable ( );
		for ( int i = 0; i < mnemonicos.length; i ++) {
			htOpcodes.put ( mnemonicos [i], new Integer ( (int) intOpcodes [ i]));
		}
		// Para guardar mnemonicos e tamanhos em bytes das respectivas instrucoes
		htSize = new Hashtable ( );
		for ( int i = 0; i < mnemonicos.length; i ++) {
			htSize.put ( mnemonicos [i], new Integer ( sizeBytes [ i]));
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
			e.printStackTrace();
		}
		
	}
	
	// Instrucoes ADD, AND e NOT
	public static int assembleOperateInstructions ( int opcode, int dr, int sr1, int sr2, int immediate) {
		int iInstruction = 0, iTmp;
		
		immediate = immediate & 0x01F;
		
		if ( opcode > 15) return ( -1);
		else {
			iTmp = opcode << 12;
			iInstruction = iInstruction | iTmp;
		}
		if ( dr > 7) return ( -1);
		else {
			iTmp = dr << 9;
			iInstruction = iInstruction | iTmp;
		}
		if ( sr1 > 7) return ( -1);
		else {
			iTmp = sr1 << 6;
			iInstruction = iInstruction | iTmp;
		} 
		if (sr2 != -1){
			if (sr2 > 7) return ( -1);
			else {
				iInstruction = iInstruction | sr2;
			}
		} else {
			if ( immediate > 31) return ( -1);
			else {
				iTmp = 1 << 5;
				iInstruction = iInstruction | iTmp;
				iTmp = 0x1f & immediate;
				iInstruction = iInstruction | iTmp;
			}
		}
		
		return ( iInstruction);
	}
	
	// Instrucoes LD, LDI, LEA, ST e STI
	public static int assembleDataMovementInstructions1 ( int opcode, int dsr, int pcoffset9) {
		int iInstruction = 0, iTmp;
		
		if ( opcode > 15) return ( -1);
		else {
			iTmp = opcode << 12;
			iInstruction = iInstruction | iTmp;
		}
		if ( dsr > 7) return ( -1);
		else {
			iTmp = dsr << 9;
			iInstruction = iInstruction | iTmp;
		}
		if ( pcoffset9 > 511) return ( -1);
		else {
			iTmp = 0x1ff & pcoffset9;
			iInstruction = iInstruction | iTmp;
		}
		return ( iInstruction);
	}
	
	// Instrucoes LDR e STR
	public static int assembleDataMovementInstructions2 ( int opcode, int dsr, int baser, int offset6) {
		int iInstruction = 0, iTmp;
		
		if ( opcode > 15) return ( -1);
		else {
			iTmp = opcode << 12;
			iInstruction = iInstruction | iTmp;
		}
		if ( dsr > 7) return ( -1);
		else {
			iTmp = dsr << 9;
			iInstruction = iInstruction | iTmp;
		}
		if ( baser > 7) return ( -1);
		else {
			iTmp = baser << 6;
			iInstruction = iInstruction | iTmp;
		}
		if ( offset6 > 63) return ( -1);
		else {
			iTmp = 0x3f & offset6;
			iInstruction = iInstruction | iTmp;
		}
		
		return ( iInstruction);
	}
	
	// Instrucao BR
	public static int assembleControlInstructions1 (int opcode, int n, int z, int p, int pcoffset9){
		int iInstruction = 0, iTmp;
		
		iTmp = n << 2;
		iInstruction = iInstruction | iTmp;
		iTmp = z << 1;
		iInstruction = iInstruction | iTmp;
		iInstruction = iInstruction | p;
		
		iInstruction = assembleDataMovementInstructions1(opcode, iInstruction, pcoffset9);
		
		return ( iInstruction);
	}
	
	// Instrucoes JMP, RET, JSRR
	public static int assembleControlInstructions2(int opcode, int baser){
		int iInstruction = 0;
		
		iInstruction = assembleDataMovementInstructions2(opcode, 0, baser, 0);
		
		return ( iInstruction);
	}
	
	// Instrucoes JSR
	public static int assembleControlInstructions3(int opcode, int pcoffset11){
		int iInstruction = 0, iTmp;
		
		if ( opcode > 15) return ( -1);
		else {
			iTmp = opcode << 12;
			iInstruction = iInstruction | iTmp;
		}
		if ( pcoffset11 > 2047) return ( -1);
		else {
			iTmp = 1 << 11;
			iInstruction = iInstruction | iTmp;
			iTmp = 0x7ff & pcoffset11;
			iInstruction = iInstruction | iTmp;
		}
		
		return ( iInstruction);
	}
	
	// Instrucoes RTI
	public static int assembleControlInstructions4(int opcode){
		int iInstruction = 32768;
		
		return ( iInstruction);
	}
	
	// Instrucoes TRAP
	public static int assembleControlInstructions5(int opcode, int trapvect8){
		int iInstruction = 0, iTmp;
		
		if ( opcode > 15) return ( -1);
		else {
			iTmp = opcode << 12;
			iInstruction = iInstruction | iTmp;
		}
		if ( trapvect8 > 255) return ( -1);
		else {
			iTmp = 0xff & trapvect8;
			iInstruction = iInstruction | iTmp;
		}
		
		return ( iInstruction);
	}
	
	private void firstStep(LineOfCodeLc3 pLoC, int pLineNumber){
		
		StringTokenizer stLine;
		String [ ] sTokens = new String [ MAXTOKENS];
		int iNtokens = 0, i;
		String line = pLoC.line, tmp = null;
		pLoC.type = UNDEFINED;
		pLoC.lineNumber = pLineNumber;
		
		// Identifica strings utilizadas pelo .STRINGZ
		if (line.contains(".STRINGZ")){
			if (line.contains("\"")){
				tmp = line.substring(line.indexOf("\"") + 1, line.indexOf("\"", line.indexOf("\"") + 1));
			}
		}
		
		stLine = new StringTokenizer ( line, " ,", false);
		
		while (stLine.hasMoreTokens()) {
			String token = stLine.nextToken();
			if ( token.startsWith(";")) break;
			sTokens [ iNtokens] = token;
			iNtokens  ++;
			if ( iNtokens == MAXTOKENS) break;
        }	
   
		// Identifica as classes de instruoes ou diretivas do prog.Assembly, atribuindo um tipo
		for ( i = 0; i < iNtokens; i ++) {
			if ( sTokens [ i].equalsIgnoreCase( ".orig") && pLineNumber == 1) {
				pLoC.type = isORIG;
				break;
			} else if (sTokens[i].equalsIgnoreCase(".end") && i == 0){
				pLoC.type = isEND;
				break;
			} else if (sTokens[i].equalsIgnoreCase(".fill") && i == 0){
				pLoC.type  = isFILL;
				break;
			} else if (sTokens[i].equalsIgnoreCase(".fill") && i == 1){
				pLoC.type  = isLABELandFILL;
				break;
			} else if (sTokens[i].equalsIgnoreCase(".blkw") && i == 0){
				pLoC.type = isBLKW;
				break;
			} else if (sTokens[i].equalsIgnoreCase(".blkw") && i == 1){
				pLoC.type = isLABELandBLKW;
				break;
			} else if (sTokens[i].equalsIgnoreCase(".stringz") && i == 0){
				pLoC.type = isSTRINGZ;
				break;
			} else if (sTokens[i].equalsIgnoreCase(".stringz") && i == 1){
				pLoC.type = isLABELandSTRINGZ;
				break;
			}
        }
     	if ( pLoC.type == UNDEFINED) pLoC.type = isINSTRUCTION;
     	
     	// Altera os mnemonicos para maiusculas
     	for (i = 0; i < MAXTOKENS; i++){
     		if (sTokens[i] == null) break;
     		if (sTokens[i].equalsIgnoreCase("add")) sTokens[i] = "ADD";
     		if (sTokens[i].equalsIgnoreCase("and")) sTokens[i] = "AND";
     		// BR e tratado mais adiante
     		if (sTokens[i].equalsIgnoreCase("jmp")) sTokens[i] = "JMP";
     		if (sTokens[i].equalsIgnoreCase("jsr")) sTokens[i] = "JSR";
     		if (sTokens[i].equalsIgnoreCase("jsrr")) sTokens[i] = "JSRR";
     		if (sTokens[i].equalsIgnoreCase("ld")) sTokens[i] = "LD";
     		if (sTokens[i].equalsIgnoreCase("ldi")) sTokens[i] = "LDI";
     		if (sTokens[i].equalsIgnoreCase("ldr") || sTokens[i].equalsIgnoreCase("ldw")) sTokens[i] = "LDR";
     		if (sTokens[i].equalsIgnoreCase("lea")) sTokens[i] = "LEA";
     		if (sTokens[i].equalsIgnoreCase("not")) sTokens[i] = "NOT";
     		if (sTokens[i].equalsIgnoreCase("ret")) sTokens[i] = "RET";
     		if (sTokens[i].equalsIgnoreCase("rti")) sTokens[i] = "RTI";
     		if (sTokens[i].equalsIgnoreCase("st")) sTokens[i] = "ST";
     		if (sTokens[i].equalsIgnoreCase("sti")) sTokens[i] = "STI";
     		if (sTokens[i].equalsIgnoreCase("str")) sTokens[i] = "STR";
     		if (sTokens[i].equalsIgnoreCase("trap")) sTokens[i] = "TRAP";
     	}
     	
     	// Limpa a linha
     	pLoC.line = "";
     	if (pLoC.type == isSTRINGZ){
			pLoC.line = " " + sTokens[0] + " \"" + tmp + "\"";
		} else if (pLoC.type == isLABELandSTRINGZ){
			pLoC.line = " " + sTokens[0] + sTokens[1] + " \"" + tmp + "\"";
		} else if (pLoC.type == isINSTRUCTION){
			if (sTokens[0] != null){
				if (sTokens[0].equalsIgnoreCase("add") || sTokens[0].equalsIgnoreCase("and")
						|| sTokens[0].equalsIgnoreCase("ldr") || sTokens[0].equalsIgnoreCase("str")){
					try {
						pLoC.line = " " + sTokens[0] + " " + sTokens[1] + ", " + sTokens[2] + ", " + sTokens[3];
					} catch (Exception e){
						pLoC.messageError = "Invalid instruction at line  " + pLoC.lineNumber;
					}
				} else if (sTokens[0].equalsIgnoreCase("ld") || sTokens[0].equalsIgnoreCase("ldi")
						|| sTokens[0].equalsIgnoreCase("lea") || sTokens[0].equalsIgnoreCase("not")
						|| sTokens[0].equalsIgnoreCase("st") || sTokens[0].equalsIgnoreCase("sti")){
					try{
						pLoC.line = " " + sTokens[0] + " " + sTokens[1] + ", " + sTokens[2];
					} catch (Exception e){
						pLoC.messageError = "Invalid instruction at line  " + pLoC.lineNumber;
					}
				} else if (sTokens[0].equalsIgnoreCase("brn") || sTokens[0].equalsIgnoreCase("brz")
						|| sTokens[0].equalsIgnoreCase("brp") || sTokens[0].equalsIgnoreCase("br")
						|| sTokens[0].equalsIgnoreCase("brzp") || sTokens[0].equalsIgnoreCase("brnp")
						|| sTokens[0].equalsIgnoreCase("brnz") || sTokens[0].equalsIgnoreCase("brnzp")
						|| sTokens[0].equalsIgnoreCase("jmp") || sTokens[0].equalsIgnoreCase("ret")
						|| sTokens[0].equalsIgnoreCase("jsr") || sTokens[0].equalsIgnoreCase("jsrr")
						|| sTokens[0].equalsIgnoreCase("rti") || sTokens[0].equalsIgnoreCase("trap")
						|| sTokens[0].equalsIgnoreCase("GETC") || sTokens[0].equalsIgnoreCase("OUT")
						|| sTokens[0].equalsIgnoreCase("PUTS") || sTokens[0].equalsIgnoreCase("IN")
						|| sTokens[0].equalsIgnoreCase("PUTSP") || sTokens[0].equalsIgnoreCase("HALT")){
					try{
						for (i = 0; i < MAXTOKENS; i++){
							if (sTokens[i] == null) break;
							pLoC.line = pLoC.line + " " + sTokens[i];
						}
					} catch (Exception e){
						pLoC.messageError = "Invalid instruction at line  " + pLoC.lineNumber;
					}
				}
			}
			if (sTokens[1] != null){
				if (sTokens[1].equalsIgnoreCase("add") || sTokens[1].equalsIgnoreCase("and")
						|| sTokens[1].equalsIgnoreCase("ldr") || sTokens[1].equalsIgnoreCase("str")){
					try {
						pLoC.line = " " + sTokens[0] + " " + sTokens[1] + " " + sTokens[2] + ", " + sTokens[3] + ", " + sTokens[4];
					} catch (Exception e){
						pLoC.messageError = "Invalid instruction at line  " + pLoC.lineNumber;
					}
				} else if (sTokens[1].equalsIgnoreCase("ld") || sTokens[1].equalsIgnoreCase("ldi")
						|| sTokens[1].equalsIgnoreCase("lea") || sTokens[1].equalsIgnoreCase("not")
						|| sTokens[1].equalsIgnoreCase("st") || sTokens[1].equalsIgnoreCase("sti")){
					try{
						pLoC.line = " " + sTokens[0] + " " + sTokens[1] + " " + sTokens[2] + ", " + sTokens[3];
					} catch (Exception e){
						pLoC.messageError = "Invalid instruction at line  " + pLoC.lineNumber;
					}
				} else if (sTokens[1].equalsIgnoreCase("brn") || sTokens[1].equalsIgnoreCase("brz")
						|| sTokens[1].equalsIgnoreCase("brp") || sTokens[1].equalsIgnoreCase("br")
						|| sTokens[1].equalsIgnoreCase("brzp") || sTokens[1].equalsIgnoreCase("brnp")
						|| sTokens[1].equalsIgnoreCase("brnz") || sTokens[1].equalsIgnoreCase("brnzp")
						|| sTokens[1].equalsIgnoreCase("jmp") || sTokens[1].equalsIgnoreCase("ret")
						|| sTokens[1].equalsIgnoreCase("jsr") || sTokens[1].equalsIgnoreCase("jsrr")
						|| sTokens[1].equalsIgnoreCase("rti") || sTokens[1].equalsIgnoreCase("trap")
						|| sTokens[1].equalsIgnoreCase("GETC") || sTokens[1].equalsIgnoreCase("OUT")
						|| sTokens[1].equalsIgnoreCase("PUTS") || sTokens[1].equalsIgnoreCase("IN")
						|| sTokens[1].equalsIgnoreCase("PUTSP") || sTokens[1].equalsIgnoreCase("HALT")){
					try{
						for (i = 0; i < MAXTOKENS; i++){
							if (sTokens[i] == null) break;
							pLoC.line = pLoC.line + " " + sTokens[i];
						}
					} catch (Exception e){
						pLoC.messageError = "Invalid instruction at line  " + pLoC.lineNumber;
					}
				}
			}
		} else {
			for (i = 0; i < MAXTOKENS; i++){
				if (sTokens[i] == null) break;
				pLoC.line = pLoC.line + " " + sTokens[i];
			}
		}
		
     	
		// Altera os mnemonicos para facilitar a execucao
		for (i = 0; i < 2; i++){
			if (sTokens[i] == null) break;
			
			// Transforma as variacoes do BR
			if (sTokens[i].equalsIgnoreCase("BRn")){
				sTokens[i] = "BR";
				pLoC.n = 1;
				pLoC.z = 0;
				pLoC.p = 0;
			} else if (sTokens[i].equalsIgnoreCase("BRz")){
				sTokens[i] = "BR";
				pLoC.n = 0;
				pLoC.z = 1;
				pLoC.p = 0;
			} else if (sTokens[i].equalsIgnoreCase("BRp")){
				sTokens[i] = "BR";
				pLoC.n = 0;
				pLoC.z = 0;
				pLoC.p = 1;
			} else if (sTokens[i].equalsIgnoreCase("BR")){
				sTokens[i] = "BR";
				pLoC.n = 1;
				pLoC.z = 1;
				pLoC.p = 1;
			} else if (sTokens[i].equalsIgnoreCase("BRzp")){
				sTokens[i] = "BR";
				pLoC.n = 0;
				pLoC.z = 1;
				pLoC.p = 1;
			} else if (sTokens[i].equalsIgnoreCase("BRnp")){
				sTokens[i] = "BR";
				pLoC.n = 1;
				pLoC.z = 0;
				pLoC.p = 1;
			} else if (sTokens[i].equalsIgnoreCase("BRnz")){
				sTokens[i] = "BR";
				pLoC.n = 1;
				pLoC.z = 1;
				pLoC.p = 0;
			} else if (sTokens[i].equalsIgnoreCase("BRnzp")){
				sTokens[i] = "BR";
				pLoC.n = 1;
				pLoC.z = 1;
				pLoC.p = 1;
			}
			
			// Transforma a instrucao GETC em TRAP
			else if (sTokens[i].equalsIgnoreCase("GETC")){
				sTokens[i] = "TRAP";
				sTokens[i + 1] = "x20";
			}
			
			// Transforma a instrucao OUT em TRAP
			else if (sTokens[i].equalsIgnoreCase("OUT")){
				sTokens[i] = "TRAP";
				sTokens[i + 1] = "x21";
			}
			
			// Transforma a instrucao PUTS em TRAP
			else if (sTokens[i].equalsIgnoreCase("PUTS")){
				sTokens[i] = "TRAP";
				sTokens[i + 1] = "x22";
			}
			
			// Transforma a instrucao IN em TRAP
			else if (sTokens[i].equalsIgnoreCase("IN")){
				sTokens[i] = "TRAP";
				sTokens[i + 1] = "x23";
			}
			
			// Transforma a instrucao PUTSP em TRAP
			else if (sTokens[i].equalsIgnoreCase("PUTSP")){
				sTokens[i] = "TRAP";
				sTokens[i + 1] = "x24";
			}
			
			// Transforma a instrucao HALT em TRAP
			else if (sTokens[i].equalsIgnoreCase("HALT")){
				sTokens[i] = "TRAP";
				sTokens[i + 1] = "x25";
			}
			
		}
     	
     	// Altera o formato numerico para o formato utilizado em Java
     	for (i = 0; i < 6; i++){
     		if (sTokens[i] == null) break;
     		if (sTokens[i].startsWith("x")){
     			try{
     				sTokens[i] = sTokens[i].substring(1);
     				int a = Integer.parseInt(sTokens[i], 16);
     				sTokens[i] = "" + a;
     			} catch (Exception e){
     				
     			}
     		}else if (sTokens[i].startsWith("#")){
     			sTokens[i] = sTokens[i].substring(1);
     		}
     		
     	}
     	
     	// Faz a consistencia das linhas do programa conforme o seu tipo
     	switch ( pLoC.type) {
     		case isORIG:
     			if ( iNtokens == 2) {
     				try {
     					pLoC.argument = Integer.parseInt ( sTokens [1]);
     					pLoC.bytesInLine = 0;
     					iBaseProgramAddress = pLoC.argument;
					} catch (Exception e) {
						pLoC.messageError = "Invalid .ORIG argument at line " + pLoC.lineNumber;
					}				
     			}
     			else pLoC.type = UNDEFINED;
     			break;
     		case isEND:
     			break;
     		case isFILL:
     			if (iNtokens == 2){
     				try {
     					pLoC.argument = Integer.parseInt ( sTokens [1]);
     					pLoC.bytesInLine = 1;
					} catch (Exception e) {
						pLoC.label = sTokens[1];
					}		
     			} else pLoC.type = UNDEFINED;
     			break;
     		case isLABELandFILL:
     			if (iNtokens == 3){
     				try {
     					htAddressLabels.put ( sTokens[0], new Integer (iAddressOfThisLine));
     					pLoC.argument = Integer.parseInt ( sTokens [2]);
     					pLoC.bytesInLine = 1;
					} catch (Exception e) {
						pLoC.label = sTokens[2];
					}		
     			} else pLoC.type = UNDEFINED;
     			break;
     		case isBLKW:
     			if (iNtokens == 2){
     				try {
     					pLoC.argument = Integer.parseInt ( sTokens [1]);
     					pLoC.bytesInLine = 1;
     				} catch (Exception e){
     					pLoC.label = sTokens[1];
     				}
     			} else pLoC.type = UNDEFINED;
     			break;
     		case isLABELandBLKW:
     			if (iNtokens == 3){
     				try {
     					htAddressLabels.put ( sTokens[0], new Integer (iAddressOfThisLine));
     					pLoC.argument = Integer.parseInt ( sTokens [2]);
     					pLoC.bytesInLine = 1;
     				} catch (Exception e){
     					pLoC.label = sTokens[2];
     				}
     			} else pLoC.type = UNDEFINED;
     			break;
     		case isSTRINGZ:
     			if (iNtokens > 1){
     				try{
     					pLoC.stringz = tmp.getBytes();
     				} catch (Exception e){
     					
     				}
     			} else pLoC.type = UNDEFINED;
     			break;
     		case isLABELandSTRINGZ:
     			if (iNtokens > 1){
     				try{
     					htAddressLabels.put ( sTokens[0], new Integer (iAddressOfThisLine));
     					pLoC.stringz = tmp.getBytes();
     				} catch (Exception e){
     					
     				}
     			} else pLoC.type = UNDEFINED;
     			break;
     		case isINSTRUCTION:
     			if (iNtokens == 1 || iNtokens == 2 || iNtokens == 3 || iNtokens == 4 || iNtokens == 5) {
     				Integer Iopcode = null;
     				int l = 0;
     				Iopcode = (Integer) htOpcodes.get(sTokens[0]);
     				if (Iopcode != null){
     					l = 0;
     				} else {
     					Iopcode = (Integer) htOpcodes.get(sTokens[1]);
     					if (Iopcode != null){
     						l = 1;
     						htAddressLabels.put ( sTokens[0], new Integer (iAddressOfThisLine));
     					} else {
     						pLoC.messageError = "Invalid instruction at line  " + pLoC.lineNumber;
     					}
     				}
     				if ( Iopcode != null) { 
						pLoC.opcode = Iopcode.intValue();
     					Integer ISize = (Integer) htSize.get(sTokens[0 + l]);
     					pLoC.bytesInLine = ISize.intValue();
     					try {
     						if (sTokens[1 + l] != null)
     							if ( sTokens [1 + l].startsWith("R") || sTokens [1 + l].startsWith("r")){
     								Integer Ireg = (Integer) htArchRegs.get ( sTokens [1 + l]);
     								if ( Ireg != null)
     									sTokens [1 + l] = Ireg.toString();
     								//else sTokens[1 + l]=sTokens[1 + l].substring(1);
     							}
     						if (sTokens[2 + l] != null)
     							if ( sTokens [2 + l].startsWith("R") || sTokens [2 + l].startsWith("r")){
     								Integer Ireg = (Integer) htArchRegs.get ( sTokens [2 + l]);
     								if ( Ireg != null)
     									sTokens [2 + l] = Ireg.toString();
     								//else sTokens[2 + l]=sTokens[2 + l].substring(1);
     							}
     						boolean imm = false;
     						if (sTokens[3 + l] != null)
     							if ( sTokens [3 + l].startsWith("R") || sTokens [3 + l].startsWith("r")){
     								Integer Ireg = (Integer) htArchRegs.get ( sTokens [3 + l]);
     								if ( Ireg != null){
     									sTokens [3 + l] = Ireg.toString();
     								} /*else {
     									sTokens[3 + l]=sTokens[3 + l].substring(1);
     								}*/
     								imm = false;								
     							} else imm = true;
     						switch (pLoC.opcode){
     							case 1: // ADD
     								pLoC.DR = Integer.parseInt( sTokens[1 + l]);
     								pLoC.SR1 = Integer.parseInt( sTokens[2 + l]);
//System.out.println("dr " + pLoC.DR);
//System.out.println("sTokens " + sTokens[1 + l]);
     								if ( imm == false) {
     									pLoC.SR2 = Integer.parseInt( sTokens[3 + l]);
     								}
     								else {
     									pLoC.SR2 = -1;
     									try {
     										pLoC.imm5 = Integer.parseInt( sTokens[3 + l]);
     	     							} catch (Exception e){
     	     								pLoC.label = sTokens [3 + l];
     	     								pLoC.imm5 = 1;
     	     							}
     								}
     								pLoC.instruction = assembleOperateInstructions( pLoC.opcode, pLoC.DR, pLoC.SR1, pLoC.SR2, pLoC.imm5);
//System.out.println("dr " + pLoC.DR);
     								break;
     							case 5: // AND
     								pLoC.DR = Integer.parseInt( sTokens[1 + l]);
     								pLoC.SR1 = Integer.parseInt( sTokens[2 + l]);
     								if ( imm == false) pLoC.SR2 = Integer.parseInt( sTokens[3 + l]);
     								else {
     									pLoC.SR2 = -1;
     									try {
     										pLoC.imm5 = Integer.parseInt( sTokens[3 + l]);
     	     							} catch (Exception e){
     	     								pLoC.label = sTokens [3 + l];
     	     								pLoC.imm5 = 1;
     	     							}
     								}
     								pLoC.instruction = assembleOperateInstructions( pLoC.opcode, pLoC.DR, pLoC.SR1, pLoC.SR2, pLoC.imm5);
     								break;
     							case 0: // BR
     								try {
     									pLoC.PCoffset9 = Integer.parseInt( sTokens[1 + l]);
 	     							} catch (Exception e){
 	     								pLoC.label = sTokens [1 + l];
 	     								pLoC.PCoffset9 = 1;
 	     							}
     								pLoC.instruction = assembleControlInstructions1( pLoC.opcode, pLoC.n, pLoC.z, pLoC.p, pLoC.PCoffset9);
     								break;
     							case 12: // JMP e RET
     								if (sTokens[0 + l].equalsIgnoreCase("RET")){
     			   						pLoC.BaseR = Integer.parseInt((htArchRegs.get ( "R7")).toString());
     			   					} else {
     			   						pLoC.BaseR = Integer.parseInt( sTokens[1 + l]);
     			   					}
     								pLoC.instruction = assembleControlInstructions2( pLoC.opcode, pLoC.BaseR);
     								break;
     							case 4: // JSR e JSRR
     								if ( sTokens[0 + l].equalsIgnoreCase("JSR")){
     									try {
     										pLoC.PCoffset11 = Integer.parseInt( sTokens[1 + l]);
     	     							} catch (Exception e){
     	     								pLoC.label = sTokens [1 + l];
     	     								pLoC.PCoffset11 = 1;
     	     							}
     									pLoC.instruction = assembleControlInstructions3( pLoC.opcode, pLoC.PCoffset11);
     								} else {
     									pLoC.BaseR = Integer.parseInt( sTokens[1 + l]);
     									pLoC.instruction = assembleControlInstructions2( pLoC.opcode, pLoC.BaseR);
     								}
     								break;
     							case 2: // LD
     								pLoC.DR = Integer.parseInt( sTokens[1 + l]);
//System.out.println("dr " + pLoC.DR);
//System.out.println("sTokens " + sTokens[1 + l]);
     								try {
     									pLoC.PCoffset9 = Integer.parseInt( sTokens[2 + l]);
 	     							} catch (Exception e){
 	     								pLoC.label = sTokens [2 + l];
 	     								pLoC.PCoffset9 = 1;
 	     							}
//System.out.println("dr " + pLoC.DR);
     								pLoC.instruction = assembleDataMovementInstructions1( pLoC.opcode, pLoC.DR, pLoC.PCoffset9);
     								break;
     							case 10: // LDI
     								pLoC.DR = Integer.parseInt( sTokens[1 + l]);
     								try {
     									pLoC.PCoffset9 = Integer.parseInt( sTokens[2 + l]);
 	     							} catch (Exception e){
 	     								pLoC.label = sTokens [2 + l];
 	     								pLoC.PCoffset9 = 1;
 	     							}
     								pLoC.instruction = assembleDataMovementInstructions1( pLoC.opcode, pLoC.DR, pLoC.PCoffset9);
     								break;
     							case 6: // LDR
     								pLoC.DR = Integer.parseInt( sTokens[1 + l]);
     								pLoC.BaseR = Integer.parseInt( sTokens[2 + l]);
     								pLoC.offset6 = Integer.parseInt( sTokens[3 + l]);
     								pLoC.instruction = assembleDataMovementInstructions2( pLoC.opcode, pLoC.DR, pLoC.BaseR, pLoC.offset6);
     								break;
     							case 14: // LEA
     								pLoC.DR = Integer.parseInt( sTokens[1 + l]);
     								try {
     									pLoC.PCoffset9 = Integer.parseInt( sTokens[2 + l]);
 	     							} catch (Exception e){
 	     								pLoC.label = sTokens [2 + l];
 	     								pLoC.PCoffset9 = 1;
 	     							}
     								pLoC.instruction = assembleDataMovementInstructions1( pLoC.opcode, pLoC.DR, pLoC.PCoffset9);
     								break;
     							case 9: // NOT
     								pLoC.DR = Integer.parseInt( sTokens[1 + l]);
     								pLoC.SR1 = Integer.parseInt( sTokens[2 + l]);
     								pLoC.SR2 = -1;
     								pLoC.imm5 = -1;
     								pLoC.instruction = assembleOperateInstructions( pLoC.opcode, pLoC.DR, pLoC.SR1, pLoC.SR2, pLoC.imm5);
     								break;
     							case 8: // RTI
     								pLoC.instruction = assembleControlInstructions4( pLoC.opcode);
     								break;
     							case 3: // ST
     								pLoC.SR = Integer.parseInt( sTokens[1 + l]);
     								try {
     									pLoC.PCoffset9 = Integer.parseInt( sTokens[2 + l]);
 	     							} catch (Exception e){
 	     								pLoC.label = sTokens [2 + l];
 	     								pLoC.PCoffset9 = 1;
 	     							}
     								pLoC.instruction = assembleDataMovementInstructions1( pLoC.opcode, pLoC.SR, pLoC.PCoffset9);
     								break;
     							case 11: // STI
     								pLoC.SR = Integer.parseInt( sTokens[1 + l]);
     								try {
     									pLoC.PCoffset9 = Integer.parseInt( sTokens[2 + l]);
 	     							} catch (Exception e){
 	     								pLoC.label = sTokens [2 + l];
 	     								pLoC.PCoffset9 = 1;
 	     							}
     								pLoC.instruction = assembleDataMovementInstructions1( pLoC.opcode, pLoC.SR, pLoC.PCoffset9);
     								break;
     							case 7: // STR
     								pLoC.SR = Integer.parseInt( sTokens[1 + l]);
     								pLoC.BaseR = Integer.parseInt( sTokens[2 + l]);
     								pLoC.offset6 = Integer.parseInt( sTokens[3 + l]);
     								pLoC.instruction = assembleDataMovementInstructions2( pLoC.opcode, pLoC.SR, pLoC.BaseR, pLoC.offset6);
     								break;
     							case 15: // TRAP
     								pLoC.trapvect8 = Integer.parseInt( sTokens[1 + l]);
     								pLoC.instruction = assembleControlInstructions5( pLoC.opcode, pLoC.trapvect8);
     								break;
     							default:
     								break;
     						}
     					} catch (Exception e){
     						pLoC.messageError = "Invalid instruction at line  " + pLoC.lineNumber;
     					}
     				} else pLoC.messageError = "Invalid instruction at line  " + pLoC.lineNumber;
     			} else pLoC.type = UNDEFINED;
     			break;
     		default:
     			pLoC.messageError = "Fatal error at line  " + pLoC.lineNumber;
     			break;
     	}
     	
     	// Guarda o endereco de inicio da instrucao corrente
     	pLoC.addressOfThisLine = iAddressOfThisLine;
     	// Incrementa o endereco conforme o nro. de bytes da instrucao corrente
     	iAddressOfThisLine += pLoC.bytesInLine;
     	if ( pLoC.type == UNDEFINED) pLoC.messageError = "Invalid instruction at line  " + pLoC.lineNumber;
		
	}
	
	private void secondStep(LineOfCodeLc3 pLoC, PrintWriter output){
		
		int iAddressTmp = 0;
		Integer arg;
		
		switch ( pLoC.type) {
			case isORIG:
				output.println ( "PC="+pLoC.argument);
				iBaseProgramAddress = pLoC.argument;
				break;
				
			case isEND:
				break;
				
			case isFILL:				
				iAddressTmp = iBaseProgramAddress + pLoC.addressOfThisLine;
				if (pLoC.label != null){
					arg = (Integer) htAddressLabels.get(pLoC.label);
					if (arg != null){
						pLoC.argument = arg.intValue();
					}
				}
				output.println ( sMemName + "["+iAddressTmp+"]="+pLoC.argument);
				htAssemblyCode.put ( new Integer ( iAddressTmp), pLoC);
				break;
			
			case isLABELandFILL:
				iAddressTmp = iBaseProgramAddress + pLoC.addressOfThisLine;
				if (pLoC.label != null){
					arg = (Integer) htAddressLabels.get(pLoC.label);
					if (arg != null){
						pLoC.argument = arg.intValue();
					}
				}
				output.println ( sMemName + "["+iAddressTmp+"]="+pLoC.argument);
				htAssemblyCode.put ( new Integer ( iAddressTmp), pLoC);
				break;
				
			case isBLKW:			
				iAddressTmp = iBaseProgramAddress + pLoC.addressOfThisLine;
				if (pLoC.label != null){
					arg = (Integer) htAddressLabels.get(pLoC.label);
					if (arg != null){
						pLoC.argument = arg.intValue();
					}
				}
				for (int i = 0; i < pLoC.argument; i++)
				output.println ( sMemName + "["+(iAddressTmp+i)+"]=0");
				htAssemblyCode.put ( new Integer ( iAddressTmp), pLoC);
				break;
				
			case isLABELandBLKW:
				iAddressTmp = iBaseProgramAddress + pLoC.addressOfThisLine;
				if (pLoC.label != null){
					arg = (Integer) htAddressLabels.get(pLoC.label);
					if (arg != null){
						pLoC.argument = arg.intValue();
					}
				}
				for (int i = 0; i < pLoC.argument; i++){
					output.println ( sMemName + "["+(iAddressTmp+i)+"]=0");
				}
				htAssemblyCode.put ( new Integer ( iAddressTmp), pLoC);
				break;
				
			case isSTRINGZ:
				iAddressTmp = iBaseProgramAddress + pLoC.addressOfThisLine;
				for (int i = 0; i <= pLoC.stringz.length; i++){
					if (i == pLoC.stringz.length){
						output.println ( sMemName + "["+(iAddressTmp+i)+"]=0");
					}else{
						output.println ( sMemName + "["+(iAddressTmp+i)+"]=" + pLoC.stringz[i]);
					}
				}
				htAssemblyCode.put ( new Integer ( iAddressTmp), pLoC);
				break;
			
			case isLABELandSTRINGZ:
				iAddressTmp = iBaseProgramAddress + pLoC.addressOfThisLine;
				for (int i = 0; i <= pLoC.stringz.length; i++){
					if (i == pLoC.stringz.length){
						output.println ( sMemName + "["+(iAddressTmp+i)+"]=0");
					}else{
						output.println ( sMemName + "["+(iAddressTmp+i)+"]=" + pLoC.stringz[i]);
					}
				}
				htAssemblyCode.put ( new Integer ( iAddressTmp), pLoC);
				break;

			case isINSTRUCTION:				
				iAddressTmp = iBaseProgramAddress + pLoC.addressOfThisLine;
				if (pLoC.opcode == 1 || pLoC.opcode == 5){
					if (pLoC.label != null){
						arg = (Integer) htAddressLabels.get(pLoC.label);
						if (arg != null){
							pLoC.imm5 = arg.intValue() - (pLoC.addressOfThisLine + 1);
							pLoC.instruction = assembleOperateInstructions(pLoC.opcode, pLoC.DR, pLoC.SR1, pLoC.SR2, pLoC.imm5);
						}
					}
				} else if (pLoC.opcode == 0){
					if (pLoC.label != null){
						arg = (Integer) htAddressLabels.get(pLoC.label);
						if (arg != null){
							pLoC.PCoffset9 = arg.intValue() - (pLoC.addressOfThisLine + 1);
							pLoC.instruction = assembleControlInstructions1( pLoC.opcode, pLoC.n, pLoC.z, pLoC.p, pLoC.PCoffset9);
						}
					}
				} else if (pLoC.opcode == 4){
					if (pLoC.label != null){
						arg = (Integer) htAddressLabels.get(pLoC.label);
						if (arg != null){
							pLoC.PCoffset11 = arg.intValue() - (pLoC.addressOfThisLine + 1);
							pLoC.instruction = assembleControlInstructions3( pLoC.opcode, pLoC.PCoffset11);
						}
					}
				} else if (pLoC.opcode == 2 || pLoC.opcode == 10){
					if (pLoC.label != null){
						arg = (Integer) htAddressLabels.get(pLoC.label);
						if (arg != null){
							pLoC.PCoffset9 = arg.intValue() - (pLoC.addressOfThisLine + 1);
							pLoC.instruction = assembleDataMovementInstructions1( pLoC.opcode, pLoC.DR, pLoC.PCoffset9);
						}
//System.out.println("dr "+ pLoC.DR);
					}
				} else if (pLoC.opcode == 14){
					if (pLoC.label != null){
						arg = (Integer) htAddressLabels.get(pLoC.label);
						if (arg != null){
							pLoC.PCoffset9 = arg.intValue() - (pLoC.addressOfThisLine + 1);
							pLoC.instruction = assembleDataMovementInstructions1( pLoC.opcode, pLoC.DR, pLoC.PCoffset9);
						}
					}
				} else if (pLoC.opcode == 3 || pLoC.opcode == 11){
					if (pLoC.label != null){
						arg = (Integer) htAddressLabels.get(pLoC.label);
						if (arg != null){
							pLoC.PCoffset9 = arg.intValue() - (pLoC.addressOfThisLine + 1);
							pLoC.instruction = assembleDataMovementInstructions1( pLoC.opcode, pLoC.SR, pLoC.PCoffset9);
						}
					}
				}
				output.println ( sMemName + "["+iAddressTmp+"]="+pLoC.instruction);
				htAssemblyCode.put ( new Integer ( iAddressTmp), pLoC);
				break;
				
		}
	}
	
	private void assembler() throws Exception {
		
		BufferedReader d = new BufferedReader (new FileReader (fileOrigin));
		File file = new File(fileTarget); 
		FileWriter writer = new FileWriter(fileTarget);
		PrintWriter output = new PrintWriter(writer,true);
		String sInput;
		int line = 0;
		boolean error = false;

		while ((sInput = d.readLine()) != null) {
			if (!(sInput.startsWith(";")) && !(sInput.equals(""))) alLines.add (new LineOfCodeLc3 (sInput));
		}	
		
		for ( int i = 0; i < alLines.size(); i ++) {
			LineOfCodeLc3 loc = (LineOfCodeLc3) alLines.get( i);
			firstStep(loc,i+1);
			if (loc.messageError != null) error = true;
			if (loc.type == isEND) break;
		}

		if (!error) {
			for ( int i = 0; i < alLines.size(); i ++) {
				LineOfCodeLc3 loc = (LineOfCodeLc3) alLines.get( i);
				secondStep (loc, output);
				lc3App.messagesToUser ( null, loc.line);
				if (loc.type == isEND) break;
			}
			Montador.setAssemblyCode(htAssemblyCode);
		} else {
			for ( int i = 0; i < alLines.size(); i ++) {
				LineOfCodeLc3 loc = (LineOfCodeLc3) alLines.get( i);
				lc3App.messagesToUser ( null, loc.line);
				if (loc.messageError != null) lc3App.messagesToUser ( "ERROR", loc.messageError);
			}	
			lc3App.messagesToUser ( null, "Errors during assembling!");
			output.close ();
			writer.close ();
		}
		lc3App.messagesToUser ( null, "\n\n");
		
	}
	
	public static void main(String[] args) throws Exception{
		
		int i;
		String sNameFileOrigin, sNameFileTarget;

		for ( i = 0; i < args.length; i ++) {
			System.out.println( "argumentos: " + args[i]);
		}
		
		sNameFileOrigin = ".\\processors\\LC3\\programs\\" + args[0];
		//sNameFileOrigin = ".\\processors\\LC3\\programs\\exemplo.txt";
		sNameFileTarget = ".\\processors\\LC3\\programs\\" + args[1];
		//sNameFileTarget = ".\\processors\\LC3\\programs\\a.txt";
		
		// LC3 plc3 = new LC3(null);
		MontadorLc3 mLc3 = new MontadorLc3(null, sNameFileOrigin, sNameFileTarget, "MEM");	
		// mLc3.assembler ( );
		
	}
	
	String mnemonicos [ ];
	long intOpcodes [ ];
	int sizeBytes [ ];
	String fileOrigin, fileTarget, sMemName;
	ArrayList alLines;
	Hashtable htAddressLabels, htOpcodes, htSize, htAssemblyCode, htArchRegs;
	int iAddressOfThisLine = 0, iBaseProgramAddress = 0;
	private String archRegs [ ] = null;
	
	final int isORIG 			  = 100;
	final int isINSTRUCTION       = 101;
	final int isEND               = 102;
	final int isFILL              = 103;
	final int isBLKW              = 104;
	final int isSTRINGZ           = 105;
	final int isLABELandFILL	  = 106;
	final int isLABELandBLKW	  = 107;
	final int isLABELandSTRINGZ   = 108;
	final int UNDEFINED			  = -1;
	final int MAXTOKENS			  = 6;

}
