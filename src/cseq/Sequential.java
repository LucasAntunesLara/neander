package cseq;

import ccomb.Circuit;
import contents.Contents;

public abstract class Sequential extends Circuit {

	public abstract void write ( );
	
//	public abstract void propagate ( );
	
	public abstract void read ( );

	public Contents cConteudo;
}
