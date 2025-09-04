package primitive;

import util.Define;

public abstract class Primitive implements Define {

	public void setName ( String parName) {
		sbName = new StringBuffer ( parName);
	}
/*	 
	public StringBuffer getName ( ) {
		return ( sbName);
	}
*/
	public String getName ( ) {
		return ( sbName.toString ( ));
	}
/*	
	public boolean compare ( StringBuffer parName) {
//		if ( this.sbName.toString ( ).contentEquals ( parName)) return ( true);
		if ( this.sbName.toString ( ).compareToIgnoreCase ( parName.toString())==0) return ( true);
		else return ( false);
	}
*/
	public boolean compare ( String parName) {
		if ( this.sbName.toString ( ).compareToIgnoreCase ( parName)==0) return ( true);
		else return ( false);
	}

	public abstract void debug ( );
	public abstract void list ( );

	protected StringBuffer sbName;
}
