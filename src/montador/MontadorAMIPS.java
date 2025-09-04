package montador;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import processor.Processor;
import simdraw.TDBenchApp;
import util.Define;

public class MontadorAMIPS implements Define {

	public MontadorAMIPS ( Processor proc, String parFileNameOrigin, String parFileNameTarget) {
		dlx = proc;
		mnemonicos = dlx.getMnemonicosList();
		intOpcodes = dlx.getOpcodesList();
		intFuncs = dlx.getSizeInBytesList();
		fileOrigin = parFileNameOrigin;
		fileTarget = parFileNameTarget;
		alLines = new ArrayList ( );
	
		try {
			assembler ( );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	private void assembler ( ) throws Exception {	
			BufferedReader d = new BufferedReader ( new FileReader ( fileOrigin));
			File file = new File(fileTarget); 
			FileWriter writer = new FileWriter(fileTarget);
			PrintWriter output = new PrintWriter(writer,true);
			String sInput;
			int line = 0;
			boolean error = false;
System.out.println ( "no montador: "+fileTarget);
			while ( ( sInput = d.readLine ( )) != null) {
				if (!(sInput.startsWith("//"))) alLines.add ( sInput);
			}	
			
			for ( int i = 0; i < alLines.size(); i ++) {
				output.println ( (String) alLines.get(i));
				TDBenchApp.messagesToUser ( null, (String) alLines.get(i));
			}			

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
			
		MontadorAMIPS mmn = new MontadorAMIPS ( null, sNameFileOrigin, sNameFileTarget);
	}	

	Processor dlx;
	String fileOrigin, fileTarget;
	ArrayList alLines;
	
	String mnemonicos [ ]; 
	long intOpcodes [ ]; 
	int intFuncs [ ];
}