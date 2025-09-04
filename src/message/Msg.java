package message;

import primitive.Primitive;
import util.Define;

public abstract class Msg extends Primitive implements Define {

	public Msg ( int parType) {
		iType = parType;
	}
	
	public int getType ( ) {
		return ( iType);
	}
	
	public abstract void debug ( );
	
	protected int iType;
}
