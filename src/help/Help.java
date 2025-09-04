/*
 * Created on 28/09/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package help;

import java.io.BufferedReader;
import java.io.FileReader;

import platform.Lang;
import processor.Processor;
import simdraw.MIPSApp;
import simdraw.TDBenchApp;
import util.Define;

/**
 * @author Owner
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Help implements Define {

	public static void help ( ) {		
		String sFile = Lang.iLang==ENGLISH?"tips.txt":"help.txt";
		String fileOrigin = Processor.getProcessorName()+"\\help"+"\\"+sFile;
		String sInput;

TDBenchApp.messagesToUser ( null, "---------- ---------- ---------- ---------- ----------");
		try {
			BufferedReader d = new BufferedReader ( new FileReader ( fileOrigin));
			while ( ( sInput = d.readLine ( )) != null) {
TDBenchApp.messagesToUser ( null, sInput);
			}	
		} catch ( Exception e) {
TDBenchApp.messagesToUser ( null, Lang.iLang==ENGLISH?Lang.msgsGUI[202]:Lang.msgsGUI[207]);			
		}
TDBenchApp.messagesToUser ( null, "\n");	
	}
}
