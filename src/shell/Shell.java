package shell;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import message.MicroOperation;
import message.SetMsg;

import org.jhotdraw.application.DrawApplication;

import platform.Lang;
import platform.Platform;
import ports.Port;
import primitive.Primitive;
import processor.Processor;
import simdraw.CachedNeanderApp;
import simdraw.MIPSApp;
import simdraw.MIPSNotPipelinedApp;
import simdraw.ExampleApp;
import simdraw.NeanderApp;
import simdraw.acesMIPSApp;
import simdraw.lc3App;
import simulator.EventList;
import simulator.Simulator;
import util.Define;
import util.SistNum;
import util.Util;
import bus.Bus;
import ccomb.Adder;
import ccomb.Alu;
import ccomb.AluSimple;
import ccomb.Circuit;
import ccomb.CircuitBus;
import ccomb.Comparator;
import ccomb.Duplexer;
import ccomb.FPUnit;
import ccomb.GenericMultiplexer;
import ccomb.GenericSignExtender;
import ccomb.GetFieldComponent;
import ccomb.Logic;
import ccomb.Multiplexer;
import ccomb.SignExtender;
import ccomb.Triplexer;
import ccomb.ZeroTest;
import cseq.Cache;
import cseq.Counter;
import cseq.DMemory;
import cseq.GenericByteAdMemory;
import cseq.GenericFPRegisterFile;
import cseq.GenericPipeRegister;
import cseq.GenericRegisterFile;
import cseq.IMemoryDLX;
import cseq.Memory;
import cseq.PipeRegister;
import cseq.RI;
import cseq.ROMMemory;
import cseq.Register;
import cseq.RegisterFile;
import cseq.RegisterFile1e2s;
import datapath.Datapath;

public class Shell implements Define {
	
	public Shell ( 	String parPrompt, Simulator parSim, Processor parProc) {
		sbPrompt = new StringBuffer ( ).append ( parPrompt);
		sSim = parSim;
		ldeLe = sSim.getListaDeEventos ( );
		pProc = parProc;
		dtpDt = pProc.getDatapath ( );
	}

	public static void initialize ( String parPrompt, Simulator parSim, Processor parProc) {
		sbPrompt = new StringBuffer ( ).append ( parPrompt);
		sSim = parSim;
		ldeLe = sSim.getListaDeEventos ( );
		pProc = parProc;
		if ( pProc != null) dtpDt = pProc.getDatapath ( );
	}

	private static String readString ( ) {
		System.out.print ( sbPrompt);

		try {
			BufferedReader br = new BufferedReader ( new InputStreamReader(System.in));
			return br.readLine ( );
		} catch ( Exception e) {
			return ( "exit");			
		}
	}

	private static StringBuffer le ( ) {
		StringBuffer sbLinha = new StringBuffer ( );
		int iC = 0;

		System.out.print ( sbPrompt);
		// o 10 eh LineFeed
		while ( iC != 10) {
			try {
				iC = System.in.read ( );
			} catch ( Exception e) { }
			sbLinha.append ( ( char) iC);
		}

		return ( Util.sb ( sbLinha.substring ( 0, sbLinha.length ( ) - 2)));		
	}

	public static void decodifica ( String parLine, SetMsg parSm) throws Exception {
		StringTokenizer stLine;
		String [ ] sTokens = new String [ MAXTOKENS];
		int iNtokens = 0, i;
		boolean bErr = true, bHelp = false;
		MicroOperation mdAux;

		//System.out.println ( parLine);
		
		if ( parLine.startsWith ( "run") || parLine.startsWith ( "RUN")) {
			stLine = new StringTokenizer ( parLine, " \t[]=", false);			
		} else {
			stLine = new StringTokenizer ( parLine, " \t[]=.", false);
		}

     	while (stLine.hasMoreTokens()) {
			sTokens [ iNtokens ++] = stLine.nextToken();
			if ( iNtokens == MAXTOKENS) break;
        }

        //for ( i = 0; i < iNtokens; i ++) {
        	//System.out.print ( "\""+sTokens[i]+"\",");
    	//}
    	//System.out.println ( );

		if ( iNtokens > 0) {

			if ( iNtokens == 2 && sTokens [ 1].compareToIgnoreCase("/h")==0)
				iNtokens = 99;

        	if (sTokens [ 0].compareToIgnoreCase ( "reset") == 0){
        		bErr = false;
        		if ( iNtokens == 2) {
        			if ( sTokens [ 1].compareToIgnoreCase ( "neander") == 0) {
        				NeanderApp wAux = (NeanderApp) winForReset;
						wAux.reset ( sSim, pProc); 
        			}
        			if ( sTokens [ 1].compareToIgnoreCase ( "cachedneander") == 0) {
        				CachedNeanderApp wAux = (CachedNeanderApp) winForReset;
						wAux.reset ( sSim, pProc); 
        			}
        			if ( sTokens [ 1].compareToIgnoreCase ( "dlx") == 0) {
        				MIPSApp wAux = (MIPSApp) winForReset;
						wAux.reset ( sSim, pProc); 
        			}
        			if ( sTokens [ 1].compareToIgnoreCase ( "dlxmulti") == 0) {
        				MIPSNotPipelinedApp wAux = (MIPSNotPipelinedApp) winForReset;
						wAux.reset ( sSim, pProc); 
        			}
        			if ( sTokens [ 1].compareToIgnoreCase ( "acesMIPS") == 0) {
        				acesMIPSApp wAux = (acesMIPSApp) winForReset;
						wAux.reset ( sSim, pProc); 
        			}
        			if ( sTokens [ 1].compareToIgnoreCase ( "lc3") == 0) {
        				lc3App wAux = (lc3App) winForReset;
						wAux.reset ( sSim, pProc); 
        			}
				} else {
					System.out.println ( "FORMATO: reset <processor>");
					System.out.println ( );
				}
			}
        	
        	if (sTokens [ 0].compareToIgnoreCase ( "vis") == 0){
        		bErr = false;
        		if ( iNtokens == 2) {
					if ( sTokens [ 1].compareToIgnoreCase ( "testes") == 0) {

					}
        			if ( sTokens [ 1].compareToIgnoreCase ( "cachedneander") == 0) {
						CachedNeanderApp window = new CachedNeanderApp(sSim, pProc);
						window.open();
						window.toFront ( );
						winForReset = window;
        			}
        			if ( sTokens [ 1].compareToIgnoreCase ( "neander") == 0) {
						NeanderApp window = new NeanderApp(sSim, pProc);
						window.open();
						window.toFront ( );
						winForReset = window;
        			}
					if ( sTokens [ 1].compareToIgnoreCase ( "example") == 0) {
						ExampleApp window = new ExampleApp(sSim, pProc);
						window.open();
						window.toFront ( );
						winForReset = window;
					}
					if ( sTokens [ 1].compareToIgnoreCase ( "dlx") == 0) {
						MIPSApp window = new MIPSApp(sSim, pProc);
						window.open();
						window.toFront ( );
						winForReset = window;
					}
					if ( sTokens [ 1].compareToIgnoreCase ( "dlxmono") == 0) {
						MIPSNotPipelinedApp window = new MIPSNotPipelinedApp(sSim, pProc);
						window.open();
						window.toFront ( );
						winForReset = window;
					}
					if ( sTokens [ 1].compareToIgnoreCase ( "dlxmulti") == 0) {
						MIPSNotPipelinedApp window = new MIPSNotPipelinedApp(sSim, pProc);
						window.open();
						window.toFront ( );
						winForReset = window;
					}
					if ( sTokens [ 1].compareToIgnoreCase ( "acesMIPS") == 0) {
						acesMIPSApp window = new acesMIPSApp(sSim, pProc);
						window.open();
						window.toFront ( );
						winForReset = window;
					}
					if ( sTokens [ 1].compareToIgnoreCase ( "lc3") == 0) {
						lc3App window = new lc3App(sSim, pProc);
						window.open();
						window.toFront ( );
						winForReset = window;
					}
				} else {
					System.out.println ( "FORMATO: vis <processor>");
					System.out.println ( );
				}
			}

        	if (sTokens [ 0].compareToIgnoreCase ( "init") == 0){
        		bErr = false;
        		if ( iNtokens == 2) {
		        	if (sTokens [ 1].compareToIgnoreCase ( "dlx") == 0) {
						pProc.initialize ( 	"DLXOrg.txt","Program.txt",
											"InstructionSet.txt","PipelineStages.txt");
						sSim.Initializations ( pProc);
		        	} else if (sTokens [ 1].compareToIgnoreCase ( "neander") == 0) {
						pProc.initialize ( 	"NeanderOrg.txt","Program.bin",
											"InstructionSet.txt","Tempos.txt");
						sSim.Initializations ( pProc);
		        	} else if (sTokens [ 1].compareToIgnoreCase ( "cleopatra") == 0) {
						pProc.initialize ( 	"CleopatraOrg.txt","Program.txt",
											"InstructionSet.txt","Tempos.txt");
						sSim.Initializations ( pProc);
		        	} else if (sTokens [ 1].compareToIgnoreCase ( "femtojava") == 0) {
						pProc.initialize ( 	"FJavaOrg.txt","FJInitial.txt",
											"FJInstSet.txt","FJTiming.txt");
						sSim.Initializations ( pProc);
		        	} else if (sTokens [ 1].compareToIgnoreCase ( "example") == 0) {
						pProc.initialize ( 	"ExampleOrg.txt","Program.txt",
											"InstructionSet.txt","Timing.txt");
						sSim.Initializations ( pProc);
		        	} else if (sTokens [ 1].compareToIgnoreCase ( "acesMIPS") == 0) {
						pProc.initialize ( 	"acesMIPSorg.txt","Program.txt",
											"InstructionSet.txt","ExecutionPaths.txt");
						sSim.Initializations ( pProc);
		        	} else if (sTokens [ 1].compareToIgnoreCase ( "lc3") == 0) {
						pProc.initialize ( 	"lc3Org.txt","Program.txt",
								"InstructionSet.txt","ExecutionPaths.txt");
						sSim.Initializations ( pProc);
		        	}else {
						System.out.println ( "FORMATO: init <processor>");
						System.out.println ( "	Cria o processador.");
						System.out.println ( );
		        	}
				} else {
					System.out.println ( "FORMATO: init <processor>");
					System.out.println ( "	Cria o processador.");
					System.out.println ( );
				}
			}

        	/* Tratar criao de novo componente aqui (1) */
        	if (sTokens [ 0].compareToIgnoreCase ( "create") == 0) {
        		bErr = false;
        		
        		try {
        		
        		if ( iNtokens == 4||iNtokens == 5||iNtokens == 6||iNtokens == 7||iNtokens == 8||iNtokens == 9) {
        			int iType  = Integer.parseInt ( sTokens [ 2]);
        			int iSize  = Integer.parseInt ( sTokens [ 3]);
        			int iBits1 = 0;
					if ( iNtokens > 4) {
						iBits1 = Integer.parseInt ( sTokens [ 4]);
					}
					int iBits2 = 0;
					int iNins  = 0;
					int iNouts = 0;
					int iLatency = 0;
					int iMap = 1;			// tipo de mapeamento em caches
					int iLBSet = 0;
        			if ( iNtokens == 6) {	// Para memorias e SignExtender
        				iBits2 = Integer.parseInt ( sTokens [ 5]);
        			}
        			if ( iNtokens == 7) {	// Para memorias - arg. delay
        				iLatency = Integer.parseInt ( sTokens [ 6]);
        			}
        			if ( iNtokens == 7 && ( iType == 15 || iType == 50 || iType == 53)) {	// Para Circuit Bus e GenericRegisterFile
        				iNins  = Integer.parseInt ( sTokens [ 5]);
        				iNouts = Integer.parseInt ( sTokens [ 6]);
        			}
        			if ( iNtokens == 9 && iType == 100) {	// Para memorias cache
        				iLatency = Integer.parseInt ( sTokens [ 6]);
        				iMap = Integer.parseInt ( sTokens [ 7]);
        				iLBSet = Integer.parseInt ( sTokens [ 8]);
        			}
        			switch ( iType) {
        				case 0:
        					Register regTmp = new Register(sTokens[1],"E1",iBits1,"S1");
        					dtpDt.add ( regTmp);
        					break;		
        				case 14:
        					RI riTmp = new RI(sTokens[1],"E1",iBits1,"S1","FETCH");
        					dtpDt.add ( riTmp);
        					break;		
        				case 1:
        					RegisterFile rfTmp = new RegisterFile(iSize,sTokens[1],"E1",iBits1,"NR1","NW1","S1");
        					dtpDt.add ( rfTmp);
        					break;
        				case 2:
							RegisterFile1e2s rf1e2sTmp = new RegisterFile1e2s(iSize,sTokens[1],"E1",iBits1,"NR1","NR2","NW1","S1","S2");
        					dtpDt.add ( rf1e2sTmp);
        					break;
        				case 3:
        					Memory memTmp = new Memory(iSize,sTokens[1],"E1",iBits1,"E2",iBits2,"S1",iLatency);
        					dtpDt.add ( memTmp);
        					break;
        				case 4:
        					IMemoryDLX imemTmp = new IMemoryDLX(iSize,sTokens[1],"E1",iBits1,"E2",iBits2,"S1","FETCH",iLatency);
        					dtpDt.add ( imemTmp);
        					break;
        				case 5:
        					DMemory dmemTmp = new DMemory(iSize,sTokens[1],"E1",iBits1,"E2",iBits2,"S1",iLatency);
        					dtpDt.add ( dmemTmp);
        					break;
        				case 6:
        					Multiplexer multTmp = new Multiplexer(sTokens[1],"E1",iBits1,"E2","SEL","S1");
        					dtpDt.add ( multTmp);
        					break;
        				case 7:
							Adder adTmp = new Adder(iSize,sTokens[1],"E1",iBits1,"E2","S1","neg", "zero","ovf");
        					dtpDt.add ( adTmp);
        					break;
        				case 8:
        					Alu aluTmp = new Alu(sTokens[1],"E1",iBits1,"E2","OP","S1","neg", "zero","ovf", "carry");
        					dtpDt.add ( aluTmp);
        					break;
        				case 9:
							SignExtender seTmp = new SignExtender(sTokens[1],iBits1,iBits2);
        					dtpDt.add ( seTmp);
        					break;
        				case 10:
							ZeroTest ztTmp = new ZeroTest(sTokens[1],"E1",iBits1,"S1",BIT);
        					dtpDt.add ( ztTmp);
        					break;
        				case 11:
							Duplexer dupTmp = new Duplexer(sTokens[1],"E1",iBits1,"S1","S2");
        					dtpDt.add ( dupTmp);
        					break;
        				case 12:
							PipeRegister prTmp = new PipeRegister ( sTokens [ 1], "E1",iBits1,"E2",iBits1,
																	"E3",iBits1,"E4",iBits1,"S1","S2","S3","S4");
        					dtpDt.add ( prTmp);
        					break;
        				case 13:
							Triplexer triTmp = new Triplexer ( sTokens [ 1],"E1",iBits1,"S1","S2","S3");
        					dtpDt.add ( triTmp);
        					break;
        				case 15:
							CircuitBus cbTmp = new CircuitBus ( sTokens [ 1],iNins,iBits1,iNouts,dtpDt);
        					dtpDt.add ( cbTmp);
        					break;
        				case 16:
        					Counter counterTmp = new Counter(sTokens[1],"E1",iBits1,"INC","S1");
        					dtpDt.add ( counterTmp);
        					break;
        				case 17:
        					GenericMultiplexer mxnTmp = new GenericMultiplexer(sTokens[1],iSize, iBits1,"SEL","S0");
        					dtpDt.add ( mxnTmp);
        					break;
        				case 18:
        					AluSimple aluSTmp = new AluSimple(sTokens[1],"E1",iBits1,"E2","OPER","S1","neg", "zero","ovf", "carry");
        					dtpDt.add ( aluSTmp);
        					break;
        				case 19:
        					ROMMemory rmemTmp = new ROMMemory(iSize,sTokens[1],"E1",iBits1,iBits2,"S1");
        					dtpDt.add ( rmemTmp);
        					break;	
        				case 50:
							GenericRegisterFile grfTmp = new GenericRegisterFile ( iSize,sTokens [ 1],iNins,iBits1,iNouts);
        					dtpDt.add ( grfTmp);
        					break;
        				case 51:
							Comparator compTmp = new Comparator ( sTokens [ 1], iBits1);
        					dtpDt.add ( compTmp);
        					break;
        				case 52:
							FPUnit fpU = new FPUnit ( sTokens [ 1], "E0","E1","C0","S0");
        					dtpDt.add ( fpU);
        					break;
        				case 53:
							GenericFPRegisterFile gfprfTmp = new GenericFPRegisterFile ( iSize,sTokens [ 1],iNins,iBits1,iNouts);
        					dtpDt.add ( gfprfTmp);
        					break;
        				case 100:
        					//Cache cTmp = new Cache(iSize,sTokens[1],"E1",iBits1,"E2",iBits2,"S1", "flag",iLatency);
        					Cache cTmp = new Cache(iSize,sTokens[1],"E1",iBits1,"E2",iBits2,"S1", "HIT", iLatency, iMap, iLBSet);
        					dtpDt.add ( cTmp);
        					break;
        				case 101:
        					iLatency = iSize;
        					GenericByteAdMemory bmTmp = new GenericByteAdMemory(sTokens[1],iLatency);
        					dtpDt.add ( bmTmp);
        					break;
        				case 110:
							GetFieldComponent gfcTmp = new GetFieldComponent (sTokens[1],iSize, iBits1,iBits2);
        					dtpDt.add ( gfcTmp);
        					break;  
        				case 112:
							GenericPipeRegister gprTmp = new GenericPipeRegister ( sTokens [ 1], iSize);
							dtpDt.add ( gprTmp);
        					break;  
        				case 113:
							GenericSignExtender gseTmp = new GenericSignExtender ( sTokens [ 1], iBits1, iBits2, iLatency);
							dtpDt.add ( gseTmp);
        					break; 
        				case 114:
        					Logic lgcTmp = new Logic (sTokens [ 1], iNins, iBits1, iNouts, iBits2);
        					dtpDt.add( lgcTmp);
        					break;
	      				default:
        					break;	
        			}
				} else {
					System.out.println ( "FORMATO: create <NomeDoComponente-string> <Tipo-int> <Tamanho-int | 0> <Largura em bits-int ...>");
					System.out.println ( " Pode haver variacoes neste formato conforme o componente...");
					System.out.println ( " Leia o help do componente para maiores informacoes.");
					System.out.println ( "");
					System.out.println ( "	0 - Register");
					System.out.println ( "	1 - RegisterFile");
					System.out.println ( "	2 - RegisterFile1e2s");
					System.out.println ( "	3 - Memory");
					System.out.println ( "	4 - IMemory");
					System.out.println ( "	5 - DMemory");
					System.out.println ( "	6 - Multiplexer");
					System.out.println ( "	7 - Adder");
					System.out.println ( "	8 - Alu");
					System.out.println ( "	9 - SignExtender");
					System.out.println ( "	10 - ZeroTest");
					System.out.println ( "	11 - Duplexer");
					System.out.println ( "	12 - PipeRegister");
					System.out.println ( "	13 - Triplexer");
					System.out.println ( "	14 - Instruction Register");
					System.out.println ( "	15 - Circuit Bus");
					System.out.println ( "	16 - Counter");
					System.out.println ( "	17 - MultiplexerNx1");
					System.out.println ( "	18 - AluSimple");
					System.out.println ( "	19 - ROMMemory");
					System.out.println ( "	50 - GenericRegisterFile");
					System.out.println ( "	51 - Comparator");
					System.out.println ( "	52 - FPUnit");
					System.out.println ( "	53 - GenericFPRegisterFile");
					System.out.println ( "	100 - Cache");
					System.out.println ( "	101 - GenericByteAdMemory");
					System.out.println ( "	110 - GetFieldComponent");
					System.out.println ( "	112 - GenericPipeRegister");
					System.out.println ( "  113 - GenericSignExtender");
					System.out.println ( "  114 - Logic");
					System.out.println ( "	Cria componente.");
					System.out.println ( );
				}
				
       			} catch ( Exception e) {
       				e.printStackTrace();
      				System.out.println ( "Erro no comando create: "+sTokens[1]);
       			}

			}

        	if (sTokens [ 0].compareToIgnoreCase ( "remove") == 0) {
        		bErr = false;
        		if ( iNtokens == 2) {
  					dtpDt.remove ( sTokens [ 1]);
				} else {
					System.out.println ( "FORMATO: remove <NomeDoComponente-string>");
					System.out.println ( "	Remove componente.");
					System.out.println ( );
				}
			}

        	if (sTokens [ 0].compareToIgnoreCase ( "link") == 0) {
        		bErr = false;
        		if ( iNtokens == 5) {
        			dtpDt.link ( sTokens[1], sTokens[2], sTokens[3], sTokens[4]);
				} else {
					System.out.println ( "FORMATO: link <NomeDoComponenteA-string> <NomeDaPortaA-string> <NomeDoComponenteB-string> <NomeDaPortaB-string>");
					System.out.println ( "	Cria barramento entre dois componentes.");
					System.out.println ( );
				}
			}

        	if (sTokens [ 0].compareToIgnoreCase ( "delink") == 0) {
        		bErr = false;
        		if ( iNtokens == 5) {
        			dtpDt.deLink ( sTokens[1], sTokens[2], sTokens[3], sTokens[4]);
				} else {
					System.out.println ( "FORMATO: delink <NomeDoComponenteA-string> <NomeDaPortaA-string> <NomeDoComponenteB-string> <NomeDaPortaB-string>");
					System.out.println ( "	Remove barramento entre dois componentes.");
					System.out.println ( );
				}
			}

        	if (sTokens [ 0].compareToIgnoreCase ( "gettime") == 0) {
        		bErr = false;
        		if ( iNtokens == 1) {
					System.out.println ( "O tempo eh: "+ sSim.getTime ( ));
				} else {
					System.out.println ( "FORMATO: gettime");
					System.out.println ( "	Imprime tempo atual de simulacao.");
					System.out.println ( );
				}
			}

        	if (sTokens [ 0].compareToIgnoreCase ( "advancetime") == 0) {
        		bErr = false;
        		if ( iNtokens == 1) {
					sSim.advanceTime ( );
					System.out.println ( "Novo tempo eh: "+ sSim.getTime ( ));
				} else {
					System.out.println ( "FORMATO: advancetime");
					System.out.println ( "	Avanca o tempo de simulacao.");
					System.out.println ( );
				}
			}

        	if (sTokens [ 0].compareToIgnoreCase ( "simulate") == 0) {
	       		bErr = false;
	       		
	       		try {
	       			
        		if ( iNtokens == 1) {
					System.out.println ( "Simulando para o tempo: "+ sSim.getTime ( ));
					sSim.Simulate ( pProc);
					System.out.println ( );
				} else if ( iNtokens == 2) {
        			int k, iNtimes  = Integer.parseInt ( sTokens [ 1]);
					for ( k = 0; k < iNtimes; k ++) {
						sSim.Simulate ( pProc);
					}
					System.out.println ( "O tempo e: "+ sSim.getTime ( ));
				} else {
					System.out.println ( "FORMATO: simulate <UnidadesDeTempo>");
					System.out.println ( "	Simula - executa eventos para aquele tempo de simulacao e avana o tempo.");
					System.out.println ( );
				}
				
				} catch ( Exception e) {
					System.out.println ( "Erro no comando simulate!");
					e.printStackTrace();
				}
			}

        	if (sTokens [ 0].compareToIgnoreCase ( "debug") == 0) {
        		bErr = false;
        		
        		try {
        		
       			if ( iNtokens == 2) {
       				if ( sTokens [ 1].compareToIgnoreCase ( "pipe") == 0) pProc.debug ( );
       				else if ( sTokens [ 1].compareToIgnoreCase ( "datapath") == 0) dtpDt.debug ( sTokens [ 1], true);
       				else if ( sTokens [ 1].compareToIgnoreCase ( "bus") == 0) dtpDt.debug ( sTokens [ 1], true);
       				else {
						Circuit cAux = dtpDt.search ( sTokens [ 1]);
						if ( cAux != null)
							dtpDt.debug ( sTokens [ 1], true);
						else 
							System.out.println ( "Nao encontrou o componente: " + sTokens[1]+"!!!");
       				}
				} else if ( iNtokens == 3) {
					boolean bAll = false;
					
       				if ( sTokens [ 2].compareToIgnoreCase ( "all") == 0) bAll = true;
					dtpDt.debug ( sTokens [ 1], bAll);
				} else {
					System.out.println ( "FORMATO: debug <NomeDoComponente-String: <nome>|datapath|bus|pipe>");
					System.out.println ( "	Imprime um componente. Ou componentes do datapath. Ou os barramentos do sistema. Ou os estagios do pipeline e respectivas instrucoes");
					System.out.println ( "	Se bus for seguido de all, imprime todos os barramentos. Caso contrario, apenas os que foram ativados no ultimo ciclo");
					System.out.println ( "  O mesmo vale para datapath - seguido ou nao de all");
					System.out.println ( "  USADO PARA DEPURACAO!!!");
					System.out.println ( );
				}
				
				} catch ( Exception e) {
					System.out.println ( "Erro no comando debug!");
				}
				
			}

        	if (sTokens [ 0].compareToIgnoreCase ( "list") == 0) {
        		bErr = false;
        		
        		try {
        		
       			if ( iNtokens == 2) {
       				if ( sTokens [ 1].compareToIgnoreCase ( "pipe") == 0) pProc.listPipes ( );
       				else if ( sTokens [ 1].compareToIgnoreCase ( "datapath") == 0) dtpDt.list ( sTokens [ 1], true);
       				else if ( sTokens [ 1].compareToIgnoreCase ( "bus") == 0) dtpDt.list ( sTokens [ 1], true);
       				else if ( sTokens [ 1].compareToIgnoreCase ( "proc") == 0) pProc.listState ( );
       				else if ( sTokens [ 1].compareToIgnoreCase ( "queues") == 0) pProc.listQueues ( );
       				else {
						Circuit cAux = dtpDt.search ( sTokens [ 1]);
						if ( cAux != null)
							dtpDt.list ( sTokens [ 1], true);
						else 
							System.out.println ( "Nao encontrou o componente: " + sTokens[1]+"!!!");
       				}
				} else if ( iNtokens == 3) {
					boolean bAll = false;
					
       				if ( sTokens [ 2].compareToIgnoreCase ( "all") == 0) bAll = true;
					dtpDt.list ( sTokens [ 1], bAll);
				} else {
					System.out.println ( "FORMATO: list <NomeDoComponente-String: <nome>|datapath|bus|pipe|proc>");
					System.out.println ( "	Imprime um componente. Ou componentes do datapath. Ou os barramentos do sistema. Ou os estagios do pipeline e respectivas instrucoes");
					System.out.println ( "  Ou as variaveis de estado do processador");
					System.out.println ( "	Se bus for seguido de all, imprime todos os barramentos. Caso contrario, apenas os que foram ativados no ultimo ciclo");
					System.out.println ( "  O mesmo vale para datapath - seguido ou nao de all");
					System.out.println ( "  USADO PARA ACOMPANHAMENTO DA SIMULACAO!!!");
					System.out.println ( );
				}
				
				} catch ( Exception e) {
					System.out.println ( "Erro no comando list!");
				}
			}

        	if (sTokens [ 0].compareToIgnoreCase ( "behavior") == 0) {
        		bErr = false;
        		
        		try {
        		
       			if ( iNtokens == 3) {
					mdAux = new MicroOperation ( sTokens[2],BEHAVIOR,null,0,0,0,Float.parseFloat ( sTokens [ 1]));
					if ( parSm != null) parSm.add ( ( Primitive) mdAux); 
					else ldeLe.add ( Float.parseFloat ( sTokens [ 1])+ sSim.getTime ( ), mdAux);
				} else {
					System.out.println ( "FORMATO: behavior <Tempo-float> <NomeDoComponente-string>");
					System.out.println ( "	Executara, no tempo informado, um componente|circuito combinacional.");
					System.out.println ( );
				}
				
       			} catch ( Exception e) {
      				System.out.println ( "Erro no comando behavior!");
       			}

			}

        	if (sTokens [ 0].compareToIgnoreCase ( "write") == 0) {
        		bErr = false;
        		
        		try {
        		
       			if ( iNtokens == 3) {
					mdAux = new MicroOperation ( sTokens[2],WRITE,null,0,0,0,Float.parseFloat ( sTokens [ 1]));
					if ( parSm != null) parSm.add ( ( Primitive) mdAux); 
					else ldeLe.add ( Float.parseFloat ( sTokens [ 1])+ sSim.getTime ( ), mdAux);
				} else {
					System.out.println ( "FORMATO: write <Tempo-float> <NomeDoComponente-string>");
					System.out.println ( "	Executara, no tempo informado, uma escrita num componente|circuito sequencial.");
					System.out.println ( );
				}
				
       			} catch ( Exception e) {
      				System.out.println ( "Erro no comando write!");
       			}

			}

        	if (sTokens [ 0].compareToIgnoreCase ( "read") == 0) {
        		bErr = false;
        		
        		try {
        		
       			if ( iNtokens == 3) {
					mdAux = new MicroOperation ( sTokens[2],READ,null,0,0,0,Float.parseFloat ( sTokens [ 1]));
					if ( parSm != null) parSm.add ( ( Primitive) mdAux); 
					else ldeLe.add ( Float.parseFloat ( sTokens [ 1])+ sSim.getTime ( ), mdAux);
				} else {
					System.out.println ( "FORMATO: read <Tempo-float> <NomeDoComponente-string>");
					System.out.println ( "	Executara, no tempo informado, uma leitura num componente|circuito sequencial.");
					System.out.println ( );
				}
				
       			} catch ( Exception e) {
      				System.out.println ( "Erro no comando read!");
       			}

			}

        	if (sTokens [ 0].compareToIgnoreCase ( "propagate") == 0) {
        		bErr = false;
        		
        		try {
        		
       			if ( iNtokens == 3) {
					mdAux = new MicroOperation ( sTokens[2],PROPAGATE,null,0,0,0,Float.parseFloat ( sTokens [ 1]));
					if ( parSm != null) parSm.add ( ( Primitive) mdAux); 
					else ldeLe.add ( Float.parseFloat ( sTokens [ 1])+ sSim.getTime ( ), mdAux);
				} else {
					System.out.println ( "FORMATO: propagate <Tempo-float> <NomeDoComponente-string>");
					System.out.println ( "	Executara, no tempo informado, uma propagacao de conteudo para a saida num componente|circuito sequencial.");
					System.out.println ( );
				}
				
       			} catch ( Exception e) {
      				System.out.println ( "Erro no comando propagate!");
       			}

			}

        	if (sTokens [ 0].compareToIgnoreCase ( "setport") == 0) {
        		long lType, lValue;
        		
        		bErr = false;
        		
        		try {
        		
       			if ( iNtokens == 6 && ( 	sTokens [ 4].compareToIgnoreCase ( "IN") == 0 ||
       										sTokens [ 4].compareToIgnoreCase ( "CONTROL") == 0)) {
       				if ( sTokens [ 4].compareToIgnoreCase ( "IN") == 0) lType = IN;
       				else lType = CONTROL;
					lValue = SistNum.getValue ( sTokens [ 5]);
					mdAux = new MicroOperation ( 	sTokens[2],SET,sTokens[3],
												lType,lValue,0L,
												Float.parseFloat ( sTokens [ 1]));
					if ( parSm != null) parSm.add ( ( Primitive) mdAux); 
					else ldeLe.add ( Float.parseFloat ( sTokens [ 1])+ sSim.getTime ( ), mdAux);
				} else {
					System.out.println ( "FORMATO: setport <Tempo-float> <NomeDoComponente-string> <NomeDaPorta-string> <Tipo-string: IN|CONTROL> <Valor-int>");
					System.out.println ( "	Colocara, no tempo informado, um valor na porta indicada (de entrada ou controle).");
					System.out.println ( );
				}
				
       			} catch ( Exception e) {
      				System.out.println ( "Erro no comando setport!");
       			}
				
			}

        	if (sTokens [ 0].compareToIgnoreCase ( "setcontents") == 0) {
        		long lValue;
        		bErr = false;
        		
        		try {
        		
       			if ( iNtokens == 6) {
					lValue = SistNum.getValue ( sTokens [ 5]);
					mdAux = new MicroOperation ( 	sTokens[2],SET,null,
												Long.parseLong(sTokens[3]),
												Long.parseLong(sTokens[4]),
												lValue,
												Float.parseFloat ( sTokens [ 1]));
					ldeLe.add ( Float.parseFloat ( sTokens [ 1])+ sSim.getTime ( ), mdAux);
				} else {
					System.out.println ( "FORMATO: setcontents <Tempo-float> <NomeDoComponente-string> <CoordenadaX-int> <CoordenadaY-int> <Valor-int>");
					System.out.println ( "	Colocara, no tempo informado, um valor na conteudo do componente (nas coord. x e y).");
					System.out.println ( );
				}
				
       			} catch ( Exception e) {
      				System.out.println ( "Erro no comando setport!");
       			}
			
			}

        	if (sTokens [ 0].compareToIgnoreCase ( "set") == 0) {
        		bErr = false;
        		
        		try {
        		
       			if ( iNtokens == 3) {
					if (sTokens [ 1].compareToIgnoreCase ( "NumberFormat") == 0) {
						SistNum.setDefaultNumberFormat ( sTokens [ 2]);
					} else if (sTokens [ 1].compareToIgnoreCase ( "Language") == 0) {
						Lang.setDefaultLanguage ( sTokens [ 2]);
					} 
				} else {
					System.out.println ( "FORMATO: set NumberFormat <format>");
					System.out.println ( "	Seta o formato de numero a ser usado nos comandos...");
					System.out.println ( "	de entrada interativa");
					System.out.println ( "	Valores possiveis: bin | decimal | hexa");
					System.out.println ( );
					System.out.println ( "FORMATO: Language <language>");
					System.out.println ( "	Seta a linguagem a ser usada na GUI...");
					System.out.println ( "	Valores possiveis: portugues | english");
					System.out.println ( );
				}
				
       			} catch ( Exception e) {
      				System.out.println ( "Erro no comando set!");
       			}
			
			}

        	if (sTokens [ 0].compareToIgnoreCase ( "search") == 0) {
        		bErr = false;
        		
        		try {
        		
       			if ( iNtokens == 3) {
					if (sTokens [ 1].compareToIgnoreCase ( "bus") == 0) {
		        		Bus bBs;
						if ( ( bBs = dtpDt.searchBusByDestination ( sTokens [ 2])) == null) {
							System.out.println ( "Nao encontrado este bus!");
						} else {
							System.out.println ( "Encontrado este bus: ");
							bBs.debug ( );
						}
					}
				} else {
					System.out.println ( "FORMATO: search bus <bus name>");
					System.out.println ( "	Pesquisa um barramento pela porta de destino...");
					System.out.println ( );
				}
				
       			} catch ( Exception e) {
      				System.out.println ( "Erro no comando search!");
       			}
			
			}

        	if (sTokens [ 0].compareToIgnoreCase ( "run") == 0) {
				BufferedReader d = null;
				String sInput;
				
        		bErr = false;
				if ( iNtokens == 2) d = new BufferedReader ( new FileReader ( sTokens [ 1]));
				if ( d != null) {
					while ( ( sInput = d.readLine ( )) != null) {
						if ( sInput.substring ( 0, 2).compareTo ( "//") == 0) {
//							System.out.println ( "Comentario: "+sInput);
							continue;
						}
//						System.out.println ( "--> \""+sInput+"\"");
						decodifica ( sInput, parSm);
					}
				} else {
					System.out.println ( "FORMATO: run <NomeArq-String>");
					System.out.println ( "	Executa arquivo batch.");
					System.out.println ( );					
				}
			}

        	if (sTokens [ 0].compareToIgnoreCase ( "prompt") == 0) {
	       		bErr = false;
       			if ( iNtokens == 2) {
					sbPrompt.replace ( 0, sbPrompt.length ( ) - 1, sTokens [ 1]);
				} else {
					System.out.println ( "FORMATO: prompt <NovoPrompt-String>");
					System.out.println ( "	Muda o prompt do sistema.");
					System.out.println ( );
				}
			}

        	/* Tratar criao de novo componente aqui (2) */
        	if (sTokens [ 0].compareToIgnoreCase ( "teste") == 0) {
        		/*byte bAux [];
        		long lAux;
        		
        		bAux = SistNum.splitDoubleInto4Bytes( -255);
	       		lAux = SistNum.join4BytesIntoDouble( bAux);
        		bAux = SistNum.splitDoubleInto8Bytes( -255);
	       		lAux = SistNum.join8BytesInDouble( bAux);
	       		SistNum.getByte ( lAux);
	       		SistNum.getHalfWord ( lAux);
	       		SistNum.getWord ( lAux);
        		GenericPipeRegister.test ( );
	       		bErr = false;
        		pProc.simulateBatch();*/
			}

        	/* */
        	if (sTokens [ 0].compareToIgnoreCase ( "batch") == 0) {
          		bErr = false;
	       		String arg;
System.out.println ( "iNtokens ="+iNtokens);
	       		// A extensao do arquivo foi separada e ficou em sTokens[2]
	       		if (iNtokens == 3) arg = sTokens[1]+"."+sTokens[2];
	       		else arg = null;
System.out.println ( "sTokens[1] ="+sTokens[1]);
System.out.println ( "arg ="+arg);
        		pProc.simulateBatch(null, arg);
			}
        	
        	/* Tratar criao de novo componente aqui (3) */
        	if (sTokens [ 0].compareToIgnoreCase ( "helpc") == 0) {
        		bErr = false;
        		
        		try {
        		
        		if ( iNtokens == 2) {
        			int iType = Integer.parseInt ( sTokens [ 1]);
        			int iSize = 1;
        			switch ( iType) {
        				case 0:
        					Register.help ( );
        					break;		
        				case 1:
        					RegisterFile.help ( );
        					break;
        				case 2:
							RegisterFile1e2s.help ( );
        					break;
        				case 3:
        					Memory.help ( );
        					break;
        				case 4:
        					IMemoryDLX.help ( );
        					break;
        				case 5:
        					DMemory.help ( );
        					break;
        				case 6:
        					Multiplexer.help ( );
        					break;
        				case 7:
							Adder.help ( );
        					break;
        				case 8:
        					Alu.help ( );
        					break;
        				case 9:
        					SignExtender.help ( );
        					break;
        				case 10:
        					ZeroTest.help ( );
        					break;
        				case 11:
        					Duplexer.help ( );
        					break;
        				case 12:
        					PipeRegister.help ( );
        					break;
        				case 13:
        					Triplexer.help ( );
        					break;
        				case 14:
        					RI.help ( );
        					break;
        				case 15:
        					CircuitBus.help ( );
        					break;
        				case 16:
        					Counter.help ( );
        					break;
        				case 17:
        					GenericMultiplexer.help ( );
        					break;
        				case 18:
        					AluSimple.help ( );
        					break;
        				case 19:
        					ROMMemory.help ( );
        					break;
        				case 50:
        					GenericRegisterFile.help ( );
        					break;
        				case 51:
        					Comparator.help ( );
        					break;
        				case 52:
        					FPUnit.help ( );
        					break;
        				case 53:
        					GenericRegisterFile.help ( );
        					break;
        				case 100:
        					Cache.help ( );
        					break;
        				case 101:
        					GenericByteAdMemory.help ( );
        					break;
        				case 110:
        					GetFieldComponent.help ( );
        					break;
        				case 112:
        					GenericPipeRegister.help ( );
        					break;
        				case 113:
    						GenericSignExtender.help();
            				break; 
            			case 114:
            				Logic.help();
	      				default:
        					break;	
        			}
				} else {
					System.out.println ( "FORMATO: helpc <Tipo-int>");
					System.out.println ( "	0 - Register");
					System.out.println ( "	1 - RegisterFile");
					System.out.println ( "	2 - RegisterFile1e2s");
					System.out.println ( "	3 - Memory");
					System.out.println ( "	4 - IMemory");
					System.out.println ( "	5 - DMemory");
					System.out.println ( "	6 - Multiplexer");
					System.out.println ( "	7 - Adder");
					System.out.println ( "	8 - Alu");
					System.out.println ( "	9 - SignExtender");
					System.out.println ( "	10 - ZeroTest");
					System.out.println ( "	11 - Duplexer");
					System.out.println ( "	12 - PipeRegister");
					System.out.println ( "	13 - Triplexer");
					System.out.println ( "	14 - Instruction Register");
					System.out.println ( "	15 - Circuit Bus");
					System.out.println ( "	16 - Counter");
					System.out.println ( "	17 - MultiplexerNx1");
					System.out.println ( "	18 - AluSimple");
					System.out.println ( "	19 - ROMMemory");
					System.out.println ( "	50 - GenericRegisterFile");
					System.out.println ( "	51 - Comparator");
					System.out.println ( "	52 - FPUnit");
					System.out.println ( "	53 - GenericFPRegisterFile");
					System.out.println ( "	100 - Cache");
					System.out.println ( "	101 - GenericByteAdMemory");
					System.out.println ( "	110 - GetFieldComponent");
					System.out.println ( "	112 - GenericPipeRegister");
					System.out.println ( "  113 - GenericSignExtender");
					System.out.println ( "  114 - Logic");
					System.out.println ( "	Explica o componente.");
					System.out.println ( );
				}
				
				} catch ( Exception e) {
					System.out.println ( "Erro no comando helpc!");
				}
				
			}

        	if (sTokens [ 0].compareToIgnoreCase ( "help") == 0) {
	       		bHelp = true;
	       		bErr = false;
			}

        	if (sTokens [ 0].compareToIgnoreCase ( "mop") == 0) {
	       		bErr = false;
	       		
	       		try {
	       		
	       		//		
	            	if (sTokens [ 2].compareToIgnoreCase ( "include") == 0) {
	    				BufferedReader d = null;
	    				String sInput;
	    				
	            		bErr = false;
//						System.out.println ( "include EEU: "+sTokens[3]+".txt");
	    				if ( iNtokens == 4) {
	    					String sPathName = Platform.treatPathNames ( Processor.getProcessorName()+"\\instructions\\"+sTokens [ 3]+".txt");
	    					d = new BufferedReader(new FileReader(sPathName));
	    				}
	    				if ( d != null) {
	    					while ( ( sInput = d.readLine ( )) != null) {
	    						if ( sInput.substring ( 0, 2).compareTo ( "//") == 0) {
//	    							System.out.println ( "Comentario: "+sInput);
	    							continue;
	    						}
//	    						System.out.println ( "--> \""+sTokens [0] + "," + sTokens [1] + ","+sInput+"\"");
	    						decodifica ( sTokens [0] + "[" + sTokens [1] + "]"+sInput, parSm);
	    					}
	    				} else {
	    					System.out.println ( "FORMATO: include <EEU-String>");
	    					System.out.println ( "	Inclui uma unidade de execuo elementar.");
	    					System.out.println ( );					
	    				}
	    			} else {	       			
	       		// inicio do else - indentacao prejudicada
	       			
				Circuit cAux = dtpDt.search ( sTokens [ 2]);
				if ( cAux != null) {

	       			if ( iNtokens == 4) {
	       				if ( sTokens [ 3].compareToIgnoreCase ( "behavior") == 0) {
							mdAux = new MicroOperation ( sTokens[2],BEHAVIOR,null,0,0,0,Float.parseFloat ( sTokens [ 1]));
							if ( parSm != null) parSm.add ( ( Primitive) mdAux); 
	       				} else if ( sTokens [ 3].compareToIgnoreCase ( "read") == 0) {
							mdAux = new MicroOperation ( sTokens[2],READ,null,0,0,0,Float.parseFloat ( sTokens [ 1]));
							if ( parSm != null) parSm.add ( ( Primitive) mdAux); 
						} else if ( sTokens [ 3].compareToIgnoreCase ( "write") == 0) {
							mdAux = new MicroOperation ( sTokens[2],WRITE,null,0,0,0,Float.parseFloat ( sTokens [ 1]));
							if ( parSm != null) parSm.add ( ( Primitive) mdAux); 
						} else if ( sTokens [ 3].compareToIgnoreCase ( "propagate") == 0) {
							mdAux = new MicroOperation ( sTokens[2],PROPAGATE,null,0,0,0,Float.parseFloat ( sTokens [ 1]));
							if ( parSm != null) parSm.add ( ( Primitive) mdAux); 
						} 
					} else if ( iNtokens == 5) {
						Port pAux;
						long lValue = SistNum.getValue ( sTokens [ 4]);

						if ( ( pAux = cAux.getPort ( sTokens [ 3], IN)) != null) {
							mdAux = new MicroOperation ( 	sTokens[2],SET,sTokens[3],
														IN,lValue,0L,
														Float.parseFloat ( sTokens [ 1]));
							if ( parSm != null) parSm.add ( ( Primitive) mdAux); 
						} else if ( ( pAux = cAux.getPort ( sTokens [ 3], CONTROL)) != null) {
							mdAux = new MicroOperation ( 	sTokens[2],SET,sTokens[3],
														CONTROL,lValue,0L,
														Float.parseFloat ( sTokens [ 1]));
							if ( parSm != null) parSm.add ( ( Primitive) mdAux); 
						} 
					} else {
						System.out.println ( "ERRO NA DEFINICAO DE INSTRUCAO!!!");
					}
				} else {
					System.out.println ( "Nao encontrou o componente: " + sTokens[2]+"!!!");			
				}
	       		// fim do else - indentacao prejudicada
	    		}
				} catch ( Exception e) {
					System.out.println ( "Erro na definicao de microoperacao!");
				}
			}

			if ( bErr == true) {
				
				try {
				
				Circuit cAux = dtpDt.search ( sTokens [ 0]);
				if ( cAux != null) {
//					System.out.println ( "E componente da organizacao");
        
	       			if ( iNtokens == 2) {
	       				if ( sTokens [ 1].compareToIgnoreCase ( "behavior") == 0) {
							mdAux = new MicroOperation ( sTokens[0],BEHAVIOR,null,0,0,0,- 1L);
							ldeLe.add ( - 1L, mdAux);				
	       				} else if ( sTokens [ 1].compareToIgnoreCase ( "read") == 0) {
							mdAux = new MicroOperation ( sTokens[0],READ,null,0,0,0,- 1L);
							ldeLe.add ( - 1L, mdAux);
						} else if ( sTokens [ 1].compareToIgnoreCase ( "write") == 0) {
							mdAux = new MicroOperation ( sTokens[0],WRITE,null,0,0,0,- 1L);
							ldeLe.add ( - 1L, mdAux);
						} else if ( sTokens [ 1].compareToIgnoreCase ( "propagate") == 0) {
							mdAux = new MicroOperation ( sTokens[0],PROPAGATE,null,0,0,0,- 1L);
							ldeLe.add ( - 1L, mdAux);
						} else {
							long lValue = SistNum.getValue ( sTokens [ 1]);

							mdAux = new MicroOperation ( 	sTokens[0],SET,null,
														0L, 0L, lValue, - 1.0F);
							ldeLe.add ( - 1.0F, mdAux);
						}
					} else if ( iNtokens == 3) {
						Port pAux;
						long lValue = SistNum.getValue ( sTokens [ 2]);
						
						if ( ( pAux = cAux.getPort ( sTokens [ 1], IN)) != null) {
							mdAux = new MicroOperation ( 	sTokens[0],SET,sTokens[1],
														IN,lValue,0L,
														- 1L);
							ldeLe.add ( - 1L, mdAux);
						} else if ( ( pAux = cAux.getPort ( sTokens [ 1], CONTROL)) != null) {
							mdAux = new MicroOperation ( 	sTokens[0],SET,sTokens[1],
														CONTROL,lValue,0L,
														- 1L);
							ldeLe.add ( - 1L, mdAux);							
						} else {
							mdAux = new MicroOperation ( 	sTokens[0],SET,null,
														Long.parseLong(sTokens[1]),
														0L,
														lValue,
														- 1.0F);
							ldeLe.add ( - 1.0F, mdAux);
						}
					} else if ( iNtokens == 4) {
						long lValue = SistNum.getValue ( sTokens [ 3]);

						mdAux = new MicroOperation ( 	sTokens[0],SET,null,
													Long.parseLong(sTokens[1]),
													Long.parseLong(sTokens[2]),
													lValue,
													- 1.0F);
						ldeLe.add ( - 1.0F, mdAux);
					} else {
						System.out.println ( "FORMATO: <NomeDoComponente-string> <Valor-int>");
						System.out.println ( "	Colocara, no proximo tempo de simulacao, um valor no conteudo do componente (na coord. 0).");
						System.out.println ( " ou ");
						System.out.println ( "FORMATO: <NomeDoComponente-string> <CoordenadaX-int> <Valor-int>");
						System.out.println ( "	Colocara, no proximo tempo de simulacao, um valor na conteudo do componente (na coord. x).");
						System.out.println ( " ou ");
						System.out.println ( "FORMATO: <NomeDoComponente-string> <CoordenadaX-int> <CoordenadaY-int> <Valor-int>");
						System.out.println ( "	Colocara, no proximo tempo de simulacao, um valor na conteudo do componente (nas coords. x,y).");
						System.out.println ( " ou ");
						System.out.println ( "FORMATO: <NomeDoComponente-string> <MetodoDeComportamento-string>");
						System.out.println ( "	Executara, no proximo tempo de simulacao, o metodo de comportamento do componente.");
						System.out.println ( " ou ");
						System.out.println ( "FORMATO: <NomeDoComponente-string> <NomeDaPorta-string> <Valor-int>");
						System.out.println ( "	Colocara, no proximo tempo de simulacao, um valor na porta de entrada, ou de controle, do componente.");
					}
				}
				else {
					System.out.println ( "Comando Invalido:");
					System.out.println ( "<<<<< " + parLine + " >>>>>");
					System.out.println ( );
					bHelp = true;
				}
				
				} catch ( Exception e) {
					System.out.println ( "Erro no comando relacionado a componentes!");
				}

			}
			
			if ( bHelp == true) {
				System.out.println ( "Comandos Validos sao: ");
				System.out.println ( "	init <proc>");
				System.out.println ( "	create");
				System.out.println ( "	remove");
				System.out.println ( "	link");
				System.out.println ( "	delink");
				System.out.println ( "	gettime");
				System.out.println ( "	advancetime");
				System.out.println ( "	simulate");
				System.out.println ( "	debug");
				System.out.println ( "	list");
				System.out.println ( "	behavior");
				System.out.println ( "	write");
				System.out.println ( "	read");
				System.out.println ( "	propagate");
				System.out.println ( "	setport");
				System.out.println ( "	setcontents");
				System.out.println ( "	set");
				System.out.println ( "	search");
				System.out.println ( "	<NomeDoComponente> ...");
				System.out.println ( "	prompt");
				System.out.println ( "	help");
				System.out.println ( "	helpc");
				System.out.println ( "	run <NomeArq>");
				System.out.println ( "	<comando> /h - ativa o help do comando");
				System.out.println ( );
				bHelp = false;
			}
		}
	}
	
	public static void command ( ) {
		String sInput;
		
		while ( true) {
			sInput = readString ( );
			
			if ( 	sInput.regionMatches ( 0, "fim", 0, 3) == true ||
					sInput.regionMatches ( 0, "exit", 0, 4) == true ||
					sInput.regionMatches ( 0, "FIM", 0, 3) == true ||
					sInput.regionMatches ( 0, "EXIT", 0, 4) == true) {
				break;
			} else {
				try {
					decodifica ( sInput);
				} catch ( Exception e) { 
					e.printStackTrace();
				}
			}
		}		
	}

	public static void decodifica ( String parLine) throws Exception {
		decodifica ( parLine, null);
	}

	public static void batch ( String parFileName) throws Exception {
		BufferedReader d = null;
		String sInput;
				
		d = new BufferedReader ( new FileReader ( parFileName));
		if ( d != null) {
			while ( ( sInput = d.readLine ( )) != null) {
				if ( sInput.substring ( 0, 2).compareTo ( "//") == 0) {
//					System.out.println ( "Comentario: "+sInput);
					continue;
				}
//				System.out.println ( "--> \""+sInput+"\"");
				Shell.decodifica ( sInput);
			}
		}
	}

	public static void batch ( String parFileName, SetMsg parSm) throws Exception {
		BufferedReader d = null;
		String sInput;

		d = new BufferedReader ( new FileReader ( parFileName));
		if ( d != null) {
			while ( ( sInput = d.readLine ( )) != null) {
				if ( sInput.substring ( 0, 2).compareTo ( "//") == 0 || sInput.length() == 0 || sInput.equalsIgnoreCase( "\n")) {
//					System.out.println ( "Comentario: "+sInput);
					continue;
				}
//System.out.println ( "--> \""+sInput+"\"");
				Shell.decodifica ( sInput, parSm);
			}
		}
	}

	static StringBuffer sbPrompt;
	
	static Simulator sSim;;
	static EventList ldeLe;
	static Processor pProc;
	static Datapath dtpDt;
	static DrawApplication winForReset;
}
