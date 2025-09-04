package ports;

public class Control extends Port {

	public Control ( String parName) {
		super ( parName, CONTROL);
	}

	public Control ( String parName, int parSize) {
		super ( parName, CONTROL, parSize);
	}

}
