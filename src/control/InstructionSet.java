package control;

import java.io.BufferedReader;
import java.io.FileReader;

import primitive.Primitive;
import util.Define;

public class InstructionSet implements Define {

	public InstructionSet ( String parFileName) throws Exception {
		BufferedReader d = null;
		String sInput, sAux;
		d = new BufferedReader ( new FileReader ( parFileName));
		InstructionType itType;
		int i = 0;
		
		sitTypes = new SetInstructionType ( );

		if ( d != null) {
			while ( ( sInput = d.readLine ( )) != null) {
//				System.out.println ( "--> \""+sInput+"\"---------------- < --");
				if ( sInput.substring ( 0, 2).compareTo ( "//") == 0) {
					continue;
				}
				sAux = sInput + ".txt";
				itType = new InstructionType ( sInput, sAux);
				sitTypes.add ( itType);
			}
		}
	}
	
	public Primitive search ( String parName) {
		//System.out.println ( parName);
		return ( sitTypes.searchPrimitive ( parName));
	}
	
	public void debug ( ) {
		InstructionType itAux;
		int i;

		System.out.println ( );
		System.out.println ( "*** InstructionSet.debug:...BEGIN");
		
		for ( i = 0; i < sitTypes.getNelems ( ); i ++) {
			itAux = ( InstructionType) sitTypes.traverse ( i);
			itAux.debug ( );
		}
		
		System.out.println ( "*** InstructionSet.debug:...END");
		System.out.println ( );
	}

	public SetInstructionType sitTypes;
}
