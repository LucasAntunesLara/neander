package message;


public interface MsgInterpreter {

	//public void execute ( );
	public void execute ( Msg parMesg);
	public boolean behavior ( );
	public boolean mustStop ( );
	public void resetCircuitAndBusAndContents ( );
}
