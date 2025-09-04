package primitive;

import java.util.Vector;

public abstract class SetPrimitive {

	public SetPrimitive ( ) {
		vArrayElems = new Vector ( );
	}
	
	public void setSize(int newSize){
		vArrayElems.setSize(newSize);
	}

	public void add ( Primitive parPrimitive) {
		vArrayElems.add ( parPrimitive);
		iNelems ++;
	}

	public void add ( int parInd, Primitive parPrimitive) {
		vArrayElems.add ( parInd, parPrimitive);
		iNelems ++;
	}
	
	public void set (int parInd, Primitive parPrimitive){
		vArrayElems.set(parInd, parPrimitive);
		iNelems ++;
	}
	
	public int getNelems ( ) {
		return ( iNelems);		
	}
	
	public Primitive searchPrimitive ( String parName)
	{
		Primitive pAux;
		int i;
		boolean bAchou;

		for ( i = 0; i < vArrayElems.size ( ); i ++) {
			pAux = ( Primitive) vArrayElems.elementAt ( i);
			bAchou = pAux.compare ( parName);
			if ( bAchou == true) return ( pAux);
		}	
		
		return ( null);			
	}

	public int searchPosition ( String parName)
	{
		Primitive pAux;
		int i;
		boolean bAchou;

		for ( i = 0; i < vArrayElems.size ( ); i ++) {
			pAux = ( Primitive) vArrayElems.elementAt ( i);
			bAchou = pAux.compare ( parName);
			if ( bAchou == true) return ( i);
		}	
		
		return ( - 1);			
	}

	public Primitive traverse ( int parElem)
	{
		Primitive pAux;

		pAux = ( Primitive) vArrayElems.elementAt ( parElem);
		
		return ( pAux);
	}
	
	public Primitive get (int parElem){
		Primitive pAux;
		pAux = (Primitive)vArrayElems.get(parElem);
		return pAux;
	}

	public void remove ( String parName) {
		int iInd;
		
		iInd = this.searchPosition ( parName);
		if ( iInd != - 1) {
			vArrayElems.remove ( iInd);
			iNelems --;
		}
	}

	public void remove ( int iInd) {
		try {
			vArrayElems.remove ( iInd);
		} catch ( Exception e) {
			return;
		}
		iNelems --;
	}
	
	public void clear(){
		vArrayElems.clear();
		iNelems = 0;
	}

	public void debug ( ) {
		Primitive pAux;
		int i;
		
		for ( i = 0; i < vArrayElems.size ( ); i ++) {
			pAux = ( Primitive) vArrayElems.elementAt ( i);
			pAux.debug ( );
//			System.out.println ( "Componente "+i+": "+pAux.getName ( ));
		}
	}

	public void list ( ) {
		Primitive pAux;
		int i;
		
		for ( i = 0; i < vArrayElems.size ( ); i ++) {
			pAux = ( Primitive) vArrayElems.elementAt ( i);
			pAux.list ( );
//			System.out.println ( "Componente "+i+": "+pAux.getName ( ));
		}
	}

	protected Vector vArrayElems;
	protected int iNelems = 0;
}
