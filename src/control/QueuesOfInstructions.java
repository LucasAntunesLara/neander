/*
 * Created on 22/01/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package control;

/**
 * @author Owner
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class QueuesOfInstructions {

	public QueuesOfInstructions ( ) {
		sqiInstr = new SetQueuesOfInstructions ( );
	}

	public SetQueuesOfInstructions getQueues ( ) { 
		return sqiInstr;
	}

	public void add ( InstructionQueue parQueue) {
		sqiInstr.add ( parQueue);
	}
	
	public InstructionQueue search ( String parName) {
		if ( parName != null) return ( ( InstructionQueue) sqiInstr.searchPrimitive ( parName));
		
		return ( null);
	}

	public void debug ( ) {
		InstructionQueue iqAux;
		int i;

		System.out.println ( );
		System.out.println ( "*** QueuesOfInstructions.debug:...BEGIN");

		for ( i = 0; i < sqiInstr.getNelems ( ); i ++) {
			iqAux = ( InstructionQueue) sqiInstr.traverse ( i);
			iqAux.debug ( );
		}
		
		System.out.println ( "*** QueuesOfInstructions.debug:...END");
		System.out.println ( );
	}

	public void list ( ) {
		InstructionQueue iqAux;
		int i;

		System.out.println ( );
		System.out.println ( "*** QueuesOfInstructions.list:...BEGIN");

		for ( i = 0; i < sqiInstr.getNelems ( ); i ++) {
			iqAux = ( InstructionQueue) sqiInstr.traverse ( i);
			iqAux.list ( );
		}
		
		System.out.println ( "*** QueuesOfInstructions.list:...END");
		System.out.println ( );
	}

	public SetQueuesOfInstructions sqiInstr;
}
