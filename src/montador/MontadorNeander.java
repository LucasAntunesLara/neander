package montador;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;
import util.SistNum;

import processor.Processor;
import simdraw.TDBenchApp;

// Classe para guardar as informaoes em uma linha do programa Assembly
class LineOfCodeNeander extends LineOfCode {
	int bytesInLine;
	String label;
	
	public LineOfCodeNeander ( String parLine) {
		super ( parLine);
		// line = parLine;
	}
	
	public void list ( ) {
//TDBenchApp.messagesToUser ( null, line+"\t,\t"+lineNumber+"\t,\t"+type+"\t,\t"+argument+"\t,\t"+bytesInLine+"\t,\t"+messageError+"\t,\t"+label);
	}
}

public class MontadorNeander {

	public MontadorNeander ( Processor proc, String parFileNameOrigin, String parFileNameTarget,String parMemName) {
		mnemonicos = proc.getMnemonicosList();
		intOpcodes = proc.getOpcodesList();
		sizeBytes = proc.getSizeInBytesList();
		fileOrigin = parFileNameOrigin;
		fileTarget = parFileNameTarget;
		sMemName = parMemName;
		alLines = new ArrayList ( );
		// Para guardar os labels de dados e rotulos de endereo
		htAddressLabels = new Hashtable ( );
		// Para guardar mnemonicos e seus respectivos valores decimais
		htOpcodes = new Hashtable ( );
		for ( int i = 0; i < mnemonicos.length; i ++) {
			htOpcodes.put ( mnemonicos [i], new Integer ( (int)intOpcodes [ i]));
		}		
		// Para guardar mnemonicos e tamanhos em bytes das respectivas instruoes
		htSize = new Hashtable ( );
		for ( int i = 0; i < mnemonicos.length; i ++) {
			htSize.put ( mnemonicos [i], new Integer ( sizeBytes [ i]));
		}
		htAssemblyCode = new Hashtable ( );
		try {
			assembler ( );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	private void firstStep ( LineOfCodeNeander pLoC, int pLineNumber) {
		StringTokenizer stLine;
		String [ ] sTokens = new String [ MAXTOKENS];
		int iNtokens = 0, i;
		String line = pLoC.line;
		pLoC.type = UNDEFINED;
		pLoC.lineNumber = pLineNumber;

		stLine = new StringTokenizer ( line, " \t=", false);

		while (stLine.hasMoreTokens()) {
			sTokens [ iNtokens] = stLine.nextToken();
			iNtokens  ++;
			if ( iNtokens == MAXTOKENS) break;
        }	
   
		// Identifica as 4 classes de instruoes ou diretivas do prog.Assembly, atribuindo um tipo
     	for ( i = 0; i < iNtokens; i ++) {
			if ( sTokens [ i].equalsIgnoreCase( "org") && pLineNumber == 1) {
				pLoC.type = isORG;
				break;
			} else if ( sTokens [ i].equalsIgnoreCase( "def")) {
				pLoC.type = isDATA;
				break;
			} else if ( sTokens [ i].endsWith ( ":")) {
				pLoC.type = isLABELandINST;
				break;
			}
        }
     	if ( pLoC.type == UNDEFINED) pLoC.type = isINSTRUCTION;
     	
     	// Faz a consistencia das linhas do programa conforme o seu tipo
     	switch ( pLoC.type) {
     		case isORG:
     			if ( iNtokens == 2) { 
     				try {
     					pLoC.argument = Integer.parseInt ( sTokens [1]);
     					pLoC.bytesInLine = 0;
     					iAddressOfThisLine = pLoC.argument;
					} catch (Exception e) {
						pLoC.messageError = "Invalid ORG argument at line "+pLoC.lineNumber;
					}				
     			}
     			else pLoC.type = UNDEFINED;
     			break;
     			
     		case isDATA:
     			if ( 	iNtokens == 4 && sTokens [1].equalsIgnoreCase("def") && 
     					sTokens [2].equalsIgnoreCase("byte")) { 
     				try {
     					pLoC.argument = Integer.parseInt ( sTokens [3]);
						htAddressLabels.put ( sTokens[0], new Integer (iAddressOfThisLine));
     					pLoC.bytesInLine = 1;
					} catch (Exception e) {
						pLoC.messageError = "Invalid DEF BYTE argument at line "+pLoC.lineNumber;
					}				
     			}
     			else pLoC.type = UNDEFINED;     			
     			break;
  
     		case isLABELandINST:
     			if ( 	iNtokens >= 2) {
   					Integer Iopcode = (Integer) htOpcodes.get(sTokens[1]);
System.out.println ( sTokens[1]+","+Iopcode.intValue());
					if ( Iopcode != null) { 
						pLoC.opcode = Iopcode.intValue();
						htAddressLabels.put ( sTokens[0].replaceAll (":", ""), new Integer (iAddressOfThisLine));
     					Integer ISize = (Integer) htSize.get(sTokens[1]);
     					pLoC.bytesInLine = ISize.intValue();
     					// Para instruoes com o segundo byte: endereo de memoria
     					if ( iNtokens == 3) {
     						try {
         						pLoC.argument = Integer.parseInt ( sTokens [2]);
         					} catch (Exception e) {
    							pLoC.argument = - 1;
    							pLoC.label = sTokens [2];
    						}		   			
     					}
     				} else pLoC.messageError = "Invalid instruction at line  "+pLoC.lineNumber;
     			}
     			else pLoC.type = UNDEFINED;     			    			
     			break;
 
     		case isINSTRUCTION:
     			if ( 	iNtokens == 1 || iNtokens == 2) {
   					Integer Iopcode = (Integer) htOpcodes.get(sTokens[0]);
					if ( Iopcode != null) { 
						pLoC.opcode = Iopcode.intValue();
     					Integer ISize = (Integer) htSize.get(sTokens[0]);
     					pLoC.bytesInLine = ISize.intValue();
   						try {
       						pLoC.argument = Integer.parseInt ( sTokens [1]);
       					} catch (Exception e) {
  							pLoC.argument = - 1;
							pLoC.label = sTokens [1];
   						}		   			
     				} else pLoC.messageError = "Invalid instruction at line  "+pLoC.lineNumber;
     			}
     			else pLoC.type = UNDEFINED;     			    			
     			break;

     		default:
     			pLoC.messageError = "Fatal error at line  "+pLoC.lineNumber;
     			break;
     	}
     	
     	// Guarda o endereo de inicio da instruao corrente
     	pLoC.addressOfThisLine = iAddressOfThisLine;
     	// Incrementa o endereo conforme o nro. de bytes da instruao corrente
     	iAddressOfThisLine += pLoC.bytesInLine;
     	if ( pLoC.type == UNDEFINED) pLoC.messageError = "Invalid instruction at line  "+pLoC.lineNumber;
	}

	private void secondStep ( LineOfCodeNeander pLoC, PrintWriter output) {
     	
		switch ( pLoC.type) {
 			case isORG:
 				output.println ( "PC="+pLoC.argument);
 				iBaseProgramAddress = pLoC.argument;
 				break;
 			
 			case isDATA:
				htAssemblyCode.put ( new Integer ( pLoC.addressOfThisLine - iBaseProgramAddress), pLoC); // new String ( pLoC.line));
 				output.println ( sMemName+"["+pLoC.addressOfThisLine+"]="+pLoC.argument);
 				break;

 			case isLABELandINST:
				htAssemblyCode.put ( new Integer ( pLoC.addressOfThisLine - iBaseProgramAddress), pLoC); //new String ( pLoC.line));
 				output.println ( sMemName+"["+pLoC.addressOfThisLine+"]="+pLoC.opcode);
 				if ( pLoC.bytesInLine > 1) {
 					if ( pLoC.argument == -1) {
 						Integer Iaddress = (Integer) htAddressLabels.get ( pLoC.label);
 						if ( Iaddress != null) {
 							pLoC.argument = Iaddress.intValue();
 						}
 					}
 					output.println ( sMemName+"["+(pLoC.addressOfThisLine+1)+"]="+pLoC.argument);					
 				}
 				break;

 			case isINSTRUCTION:
				htAssemblyCode.put ( new Integer ( pLoC.addressOfThisLine - iBaseProgramAddress), pLoC); // new String ( pLoC.line));
 				output.println ( sMemName+"["+pLoC.addressOfThisLine+"]="+pLoC.opcode);
 				if ( pLoC.bytesInLine > 1) {
 					if ( pLoC.argument == -1) {
 						Integer Iaddress = (Integer) htAddressLabels.get ( pLoC.label);
 						if ( Iaddress != null) {
 							pLoC.argument = Iaddress.intValue();
 						}
 					}
 					output.println ( sMemName+"["+(pLoC.addressOfThisLine+1)+"]="+pLoC.argument);	
 				}
 				break;
		}
	}
	
	private int secondStepBin( LineOfCodeNeander pLoC, PrintWriter output, int nL) {
     	
		switch ( pLoC.type) {
 			case isORG:
 				break;
 			
 			case isDATA:
 				output.println ((SistNum.toBinString(pLoC.argument, 8)).substring(0,8));
 				nL++;
 				break;

 			case isLABELandINST:
 				output.println ((SistNum.toBinString(pLoC.opcode, 8)).substring(0, 8));
 				nL++;
 				if ( pLoC.bytesInLine > 1) {
 					if ( pLoC.argument == -1) {
 						Integer Iaddress = (Integer) htAddressLabels.get ( pLoC.label);
 						if ( Iaddress != null) {
 							pLoC.argument = Iaddress.intValue();
 							System.out.println(pLoC.argument);
 						}
 					}
 					output.println ((SistNum.toBinString((pLoC.argument - iBaseProgramAddress), 8)).substring(0, 8));
 					nL++;
 				}
 				break;

 			case isINSTRUCTION:
 				output.println ((SistNum.toBinString(pLoC.opcode, 8)).substring(0, 8));
 				nL++;
 				if ( pLoC.bytesInLine > 1) {
 					if ( pLoC.argument == -1) {
 						Integer Iaddress = (Integer) htAddressLabels.get ( pLoC.label);
 						if ( Iaddress != null) {
 							pLoC.argument = Iaddress.intValue();
 							System.out.println(pLoC.argument);
 						}
 					}
 					output.println ((SistNum.toBinString((pLoC.argument - iBaseProgramAddress), 8)).substring(0, 8));
 					nL++;
 				}
 				break;
		}
		return nL;
	}
	
	private void assembler ( ) throws Exception {	
			BufferedReader d = new BufferedReader ( new FileReader ( fileOrigin));
			File file = new File(fileTarget);
			FileWriter writer = new FileWriter(fileTarget);
			PrintWriter output = new PrintWriter(writer,true);
			//
			File file2 = new File("code.data");
			FileWriter writer2 = new FileWriter("code.data");
			PrintWriter output2 = new PrintWriter(writer2, true);
			int nLines = 0;
			//
			String sInput;
			int line = 0;
			boolean error = false;

			while ( ( sInput = d.readLine ( )) != null) {
				if (!(sInput.startsWith("//"))) alLines.add ( new LineOfCodeNeander ( sInput));
			}	
			
			for ( int i = 0; i < alLines.size(); i ++) {
				LineOfCodeNeander loc = (LineOfCodeNeander) alLines.get( i);
				firstStep(loc,i+1);
				if ( loc.messageError != null) error = true;
			}

			if ( ! error) {
				for ( int i = 0; i < alLines.size(); i ++) {
					LineOfCodeNeander loc = (LineOfCodeNeander) alLines.get( i);
					secondStep (loc, output);
					//
					nLines = secondStepBin(loc, output2, nLines);
					//
TDBenchApp.messagesToUser ( null, loc.line);
				}
				Montador.setAssemblyCode(htAssemblyCode);
			} else {
				for ( int i = 0; i < alLines.size(); i ++) {
					LineOfCodeNeander loc = (LineOfCodeNeander) alLines.get( i);
TDBenchApp.messagesToUser ( null, loc.line);
if (loc.messageError != null) TDBenchApp.messagesToUser ( "ERROR", loc.messageError);
				}	
TDBenchApp.messagesToUser ( null, "Errors during assembling!");
				output.close ();
				writer.close ();
				//
				output2.close();
				writer2.close();
				//
			}
			//
			for (int i = nLines; i <= 256; i++){
				output2.println("11111111");
			}
			//
			TDBenchApp.messagesToUser ( null, "\n\n");
	}

	public static void main(String[] args) throws Exception{
		int i;
		String sNameFileOrigin, sNameFileTarget;

		for ( i = 0; i < args.length; i ++) {
			System.out.println( "argumentos: "+args [i]);
		}
		
		sNameFileOrigin = ".\\processors\\Neander\\programs\\"+args[0];
		sNameFileTarget = ".\\processors\\Neander\\programs\\"+args[1];
			
		MontadorNeander mmn = new MontadorNeander ( null, sNameFileOrigin, sNameFileTarget, "MEM");
		mmn.assembler ( );
	}	

	String fileOrigin, fileTarget, sMemName;
	ArrayList alLines;
	Hashtable htAddressLabels, htOpcodes, htSize; 
	static Hashtable htAssemblyCode; 
	int iAddressOfThisLine = 0, iBaseProgramAddress = 0;
	
	final int isORG 			= 100;
	final int isINSTRUCTION 	= 101;
	final int isDATA			= 102;
	final int isLABELandINST	= 103;
	final int UNDEFINED			= -1;
	final int MAXTOKENS			= 4;
	
	String mnemonicos [ ];// 	= {"STA", "LDA", "ADD", "OR", "AND", "NOT", "JMP", "JN", "JZ", "JNZ", "NOP", "HLT", "Fetch"};
	long intOpcodes [ ];// 		= {16, 32, 48, 64, 80, 96, 128, 144, 160, 176, 0, 240, 112};
	int sizeBytes [ ];// 		= {2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 1, 1, 0};
	//
	int typeOfLines [ ] 	= { isORG, isINSTRUCTION, isDATA, isLABELandINST};
	//int numberOfTokens [ ] 	= { 2, 2, 4, 4};
}