package control;

import message.SetMsg;
import platform.Platform;
import primitive.Primitive;
import shell.Shell;
import util.Define;

public class InstructionType extends Primitive implements Define {

	public InstructionType ( String parName, String parFileName) {
		sbName = new StringBuffer ( ).append (parName);
		smMops = new SetMsg ( );

		try {
			String sPathName = Platform.treatPathNames ( parFileName);
//System.out.println ( sPathName);
			Shell.batch ( sPathName, smMops);
		} catch ( Exception e) {
			System.out.println ( "Erro no construtor de InstructionType!");
		}
	}

	public void debug ( ) {
		System.out.println ( );
		System.out.println ( "*** InstructionType.debug:...BEGIN");
		System.out.println ( "* TIPO DE INSTRUCAO: "+sbName);
		smMops.debug ( );
		System.out.println ( "*** InstructionType.debug:...END");
		System.out.println ( );
	}

	public SetMsg smMops;
	
	/* (non-Javadoc)
	 * @see primitive.Primitive#list()
	 */
	public void list() {
		// TODO Auto-generated method stub

	}

}
