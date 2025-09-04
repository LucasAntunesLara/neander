package simulator;

import message.Msg;

public class Event {

	public Event ( float parTime, Msg parMesg) {
		fTime = parTime;
		msgMesg = parMesg;
	}

	public float getTime ( ) {
		return ( this.fTime);
	}

	public Msg getMsg ( ) {
		return ( this.msgMesg);
	}

	public void debug ( ) {
//		System.out.println ( "*** evento.debug:...BEGIN");
		System.out.println ( "Tempo do evento: "+fTime+ "e mensagem: ");
//		System.out.println ( "*** evento.debug:...END");
		msgMesg.debug ( );
//		System.out.println ( );
	}

	protected float fTime;
	protected Msg msgMesg;
}
