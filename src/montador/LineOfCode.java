package montador;

public class LineOfCode {
	String line;
	int lineNumber;
	int addressOfThisLine;
	int type;
	int opcode;
	int func;
	int argument;
	String messageError;
	
	public LineOfCode ( String parLine) {
		line = parLine;
	}
	
	public void list ( ) {

	}
}
