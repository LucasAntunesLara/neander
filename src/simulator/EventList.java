package simulator;

import java.util.Vector;

import message.Msg;
import util.Define;

public class EventList implements Define {

	public EventList ( ) {
		vLe = new Vector ( );
	}

	public void add ( float parTime, Msg parMesg) {
		Event evAux = new Event ( parTime, parMesg);
		int iPos;
		
		iPos = this.search ( parTime);
		vLe.insertElementAt ( evAux, iPos);
	}
	
	public Event remove ( float parTime)
	{
		Event evAux = null;
		
		if ( vLe.size ( ) > 0) {
			evAux = ( Event) vLe.firstElement ( );
			if ( evAux.getTime ( ) <= parTime) vLe.remove ( 0);
			else evAux = null;
		}
			
		return ( evAux);
	}	

	private int search ( float parTime) {
		Event evAux;
		int i;

		if ( vLe.size ( ) == 0) return ( 0);
	
		for ( i = 0; i < vLe.size ( ); i ++) {
			evAux = ( Event) vLe.elementAt ( i);
			if ( parTime < evAux.getTime ( )) break;
		}
		
		return ( i);
	}

	public void debug ( ) {
		Event evAux;
		int i;
		
		for ( i = 0; i < vLe.size ( ); i ++) {
			evAux = ( Event) vLe.elementAt ( i);
			evAux.debug ( );
		}
	}

	protected Vector vLe;

}
