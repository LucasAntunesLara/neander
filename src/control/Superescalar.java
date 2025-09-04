package control;

import java.io.BufferedReader;
import java.io.FileReader;

import platform.Platform;
import primitive.Primitive;
import shell.Shell;
import util.Define;

public class Superescalar implements Define {

	public Superescalar ( String parFileName) throws Exception {
		BufferedReader d = null;
		String sInput, sAux;
		String sPathName = Platform.treatPathNames ( parFileName);
		d = new BufferedReader ( new FileReader ( sPathName));
		ExecutionPath pPipe;
		int i = 0;
		
		spPipes = new SetPipelines ( );
//System.out.println ( parFileName);
		if ( d != null) {
			while ( ( sInput = d.readLine ( )) != null) {
//System.out.println ( "--> \""+sInput+"\"---------------- < --");
				if ( sInput.substring ( 0, 2).compareTo ( "//") == 0) {
					continue;
				}
				sAux = sInput + ".txt";
				sPathName = Platform.treatPathNames ( sAux);
				pPipe = new ExecutionPath ( sPathName);
				spPipes.add ( ( Primitive) pPipe);
			}
		}
	}

	public SetPipelines getPipelines ( ) { 
		return spPipes;
	}

	public ExecutionPath search ( String parName) {
		if ( parName == null) return (ExecutionPath) spPipes.traverse( 0);
		else return ( ( ExecutionPath) spPipes.searchPrimitive ( parName));
	}

	public void debug ( ) {
		ExecutionPath pAux;
		int i;

		System.out.println ( );
		System.out.println ( "*** Superescalar.debug:...BEGIN");

		for ( i = 0; i < spPipes.getNelems ( ); i ++) {
			pAux = ( ExecutionPath) spPipes.traverse ( i);
			pAux.debug ( );
		}
		
		System.out.println ( "*** Superescalar.debug:...END");
		System.out.println ( );
	}

	public SetPipelines spPipes;
}
