/*
 * Created on 22/01/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package control;

import java.lang.reflect.Array;

import primitive.Primitive;
import util.Define;

/**
 * @author Owner
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class InstructionQueue extends Primitive implements Define {

	public InstructionQueue ( String parNameQueue) {
		setName ( parNameQueue);
		spItems = new SetInstructionsInAQueue ( );
	}
	
	public void setFieldsOfItem ( String [] parFields, String [] parStrFields) {
		asFieldsOfItems = parFields;
		asFieldsStrOfItems = parStrFields;
	}

	public String [] getFieldsOfItem ( ) {
		return asFieldsOfItems;
	}

	public String [] getFieldsStrOfItem ( ) {
		return asFieldsStrOfItems;
	}

	public SetInstructionsInAQueue getInstructions ( ) {
		return spItems;
	}

	public ItemInstructionQueue add ( Instruction it) {
		ItemInstructionQueue.defineFieldsOfInstructions( getFieldsOfItem ( ), getFieldsStrOfItem ( ));
		ItemInstructionQueue iiq = new ItemInstructionQueue ( it);
		spItems.add ( iiq);
		return ( iiq);
	}

	public ItemInstructionQueue addAtFirst ( Instruction it) {
		ItemInstructionQueue.defineFieldsOfInstructions( getFieldsOfItem ( ), getFieldsStrOfItem ( ));
		ItemInstructionQueue iiq = new ItemInstructionQueue ( it);
		spItems.add ( 0, iiq);
		return ( iiq);
	}

	public void add ( ItemInstructionQueue parInst) {
		spItems.add ( parInst);
	}
	
	public Instruction removeInstruction ( ) {
		int i;
		
		if ( spItems.getNelems ( ) > 0) {
			ItemInstructionQueue iiq = ( ItemInstructionQueue) spItems.traverse ( 0);
			spItems.remove( 0);
			return ( iiq.iSource);
		} else return null;
	}

	public Instruction getInstruction ( ) {
		int i;
		
		if ( spItems.getNelems ( ) > 0) {
			ItemInstructionQueue iiq = ( ItemInstructionQueue) spItems.traverse ( 0);
			return ( iiq.iSource);
		} else return null;
	}

	public ItemInstructionQueue removeItem ( ) {
		int i;
		
		if ( spItems.getNelems ( ) > 0) {
			ItemInstructionQueue iiq = ( ItemInstructionQueue) spItems.traverse ( 0);
			spItems.remove( 0);
			return ( iiq);
		} else return null;
	}

	public void debug ( ) {
		ItemInstructionQueue iAux;
		int i;

		System.out.println ( );
		System.out.println ( "*** InstructionQueue.debug:...BEGIN");

		System.out.println ( "Queue Name: " + getName ( ));
		System.out.print ( "Fields: ");
		for ( i = 0; i < Array.getLength ( asFieldsOfItems); i ++) {
			System.out.print ( asFieldsOfItems [ i] + ", ");
		}
		System.out.println ( );
		for ( i = 0; i < spItems.getNelems ( ); i ++) {
			iAux = ( ItemInstructionQueue) spItems.traverse ( i);
			iAux.debug ( );
		}
		
		System.out.println ( "*** InstructioQueue.debug:...END");
		System.out.println ( );
	}

	public void list ( ) {
		ItemInstructionQueue iAux;
		int i;

		System.out.println ( );

		System.out.println ( "Queue Name: " + getName ( ));
		System.out.print ( "Fields: ");
		for ( i = 0; i < Array.getLength ( asFieldsOfItems); i ++) {
			System.out.print ( asFieldsOfItems [ i] + ", ");
		}
		System.out.println ( );
		for ( i = 0; i < spItems.getNelems ( ); i ++) {
			iAux = ( ItemInstructionQueue) spItems.traverse ( i);
			iAux.list ( );
		}
		
		System.out.println ( );
	}

	SetInstructionsInAQueue spItems;
	String [] asFieldsOfItems = { };
	String [] asFieldsStrOfItems = { };
}
