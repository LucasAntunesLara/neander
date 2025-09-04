package main;

import java.lang.reflect.Array;
import java.util.Properties;

import platform.Lang;
import processor.CachedNeander;
import processor.Cleopatra;
import processor.MIPS;
import processor.MIPSMulti;
import processor.Example;
import processor.FemtoJava;
import processor.Interactive;
import processor.Neander;
import processor.Processor;
import processor.acesMIPS;
import processor.lc3;
import shell.Shell;
import simulator.Simulator;

/*
 *	Implementa a classe Main que dispara a execuo do simulador.
 */
class Main {

	public static void main(String[] args) throws Exception {
		String sProcessador;
		int nArgs = Array.getLength ( args);
		boolean bShell = false;

		//Simulador SIM = new Simulador ( );
		// Processor usa o argumento SIM para avancar o tempo, exclusivamente
		Simulator SIM_NEW = new Simulator ( );

		/*
		Properties p = new Properties ( ); 
		p = System.getProperties ( );
		p.list( System.out);
		System.exit ( 0);
		*/
		if ( System.getProperty("user.language").equalsIgnoreCase("pt")) {
			Lang.setDefaultLanguage ( "Portuguese");			
		} else {
			Lang.setDefaultLanguage ( "English");
		}
		
		if ( nArgs != 1) {
			System.out.println ( );
			System.out.println ( "Formato: simula <processador>");
			System.out.println ( " onde \"processador\" pode ser:");
			System.out.println ( " - com recurso graficos: ");			
			System.out.println ( "     vneander, vcachedneander, vdlx, vdlxmono, vdlxmulti, vexample");
			System.out.println ( " - sem recurso graficos: ");			
			System.out.println ( "     neander, cachedneander, dlx, dlxmono, dlxmulti, example, cleopatra, femtojava");
			System.out.println ( " - modo interativo:");
			System.out.println ( "     interactive");
			System.out.println ( );
			return;
		}

		sProcessador = args [ 0];

		if ( sProcessador.equals ( "testes")) {
			MIPS 		dlx 		= new MIPS		( SIM_NEW);
			Shell.initialize ( "# DLX> ",			SIM_NEW,( Processor) dlx);
			Shell.decodifica ( "init dlx");
			Shell.decodifica ( "vis testes");	
			bShell = true;
		}

		if ( sProcessador.equals ( "example")) {
			Example 	example 	= new Example	( SIM_NEW);
			Shell.initialize ( "# Example> ",			SIM_NEW,( Processor) example);
			Shell.decodifica ( "init example");
			bShell = true;
		}

		if ( sProcessador.equals ( "vexample")) {
			Example 	example 	= new Example 	( SIM_NEW);
			Shell.initialize ( "# Example> ",		SIM_NEW,( Processor) example);
			Shell.decodifica ( "init example");
			Shell.decodifica ( "vis example");
			bShell = true;
		}
	
		if ( sProcessador.equals ( "cleopatra")) {
			Cleopatra 	cleopatra 	= new Cleopatra	( SIM_NEW);
			Shell.initialize ( "# Cleo> ",			SIM_NEW,( Processor) cleopatra);
			Shell.decodifica ( "init cleopatra");
			bShell = true;
		}

		if ( sProcessador.equals ( "neander")) {
			Neander 	neander 	= new Neander 	( SIM_NEW);
			Shell.initialize ( "# Neander> ",		SIM_NEW,( Processor) neander);
			Shell.decodifica ( "init neander");
			bShell = true;
		}
		if ( sProcessador.equals ( "vneander")) {
			Neander 	neander 	= new Neander 	( SIM_NEW);
			Shell.initialize ( "# Neander> ",		SIM_NEW,( Processor) neander);
			Shell.decodifica ( "init neander");
			Shell.decodifica ( "vis neander");
			bShell = true;
		}

		if ( sProcessador.equals ( "cachedneander")) {
			CachedNeander 	neander 	= new CachedNeander 	( SIM_NEW);
			Shell.initialize ( "# Neander> ",		SIM_NEW,( Processor) neander);
			Shell.decodifica ( "init neander");
			bShell = true;
		}
		if ( sProcessador.equals ( "vcachedneander")) {
			CachedNeander 	neander 	= new CachedNeander 	( SIM_NEW);
			Shell.initialize ( "# Neander> ",		SIM_NEW,( Processor) neander);
			Shell.decodifica ( "init neander");
			Shell.decodifica ( "vis cachedneander");
			bShell = true;
		}

		if ( sProcessador.equals ( "mips")) {
			MIPS 		dlx 		= new MIPS		( SIM_NEW);
			Shell.initialize ( "# MIPS> ",			SIM_NEW,( Processor) dlx);
			Shell.decodifica ( "init dlx");	
			bShell = true;
		}

		if ( sProcessador.equals ( "vmips")) {
			MIPS 		dlx 		= new MIPS		( SIM_NEW);
			Shell.initialize ( "# MIPS> ",			SIM_NEW,( Processor) dlx);
			Shell.decodifica ( "init dlx");
			Shell.decodifica ( "vis dlx");	
			bShell = true;
		}

		if ( sProcessador.equals ( "mipsmulti")) {
			MIPSMulti	dlxmulti	= new MIPSMulti	( SIM_NEW);
			Shell.initialize ( "# MIPS Multi> ",		SIM_NEW,( Processor) dlxmulti);
			Shell.decodifica ( "init dlx");
			bShell = true;
		}

		if ( sProcessador.equals ( "vmipsmulti")) {
			MIPSMulti	dlxmulti	= new MIPSMulti	( SIM_NEW);
			Shell.initialize ( "# MIPS Multi> ",		SIM_NEW,( Processor) dlxmulti);
			Shell.decodifica ( "init dlx");
			Shell.decodifica ( "vis dlxmulti");	
			bShell = true;
		}

		if ( sProcessador.equals ( "femtojava")) {
			FemtoJava	femtojava	= new FemtoJava	( SIM_NEW);
			Shell.initialize ( "# FemtoJava> ",		SIM_NEW,( Processor) femtojava);
			Shell.decodifica ( "init femtojava");
			bShell = true;
		}

		if ( sProcessador.equals ( "acesMIPS")) {
			acesMIPS 		am 		= new acesMIPS		( SIM_NEW);
			Shell.initialize ( "# acesMIPS> ",			SIM_NEW,( Processor) am);
			Shell.decodifica ( "init acesMIPS");	
			bShell = true;
		}

		if ( sProcessador.equals ( "vacesMIPS")) {
			acesMIPS 		am 		= new acesMIPS		( SIM_NEW);
			Shell.initialize ( "# acesMIPS> ",			SIM_NEW,( Processor) am);
			Shell.decodifica ( "init acesMIPS");
			Shell.decodifica ( "vis acesMIPS");	
			bShell = true;
		}
		
		if ( sProcessador.equals ( "vlc3")) {
			lc3 		lproc 		= new lc3		( SIM_NEW);
			Shell.initialize ( "# lc3> ",			SIM_NEW,( Processor) lproc);
			Shell.decodifica ( "init lc3");
			Shell.decodifica ( "vis lc3");	
			bShell = true;
		}
		
		if ( bShell) {
			Shell.command ( );
		} else {
			System.out.println ( );
			System.out.println ( "Modo Interativo...");
			System.out.println ( );
			Shell.initialize ( "#> ",			SIM_NEW,new Interactive ( SIM_NEW));
			Shell.command ( );
	//		CircuitBus.test ( );		
		}
	}
}
