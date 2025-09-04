package ports;

public class InControl extends Port {

	public InControl ( String parName) {
		super ( parName, IN);
	}

	public InControl ( String parName, int parSize) {
		super ( parName, IN, parSize);
	}

	public InControl ( String parName, int parSize, int parTypeValue) {
		super ( parName, IN, parSize, parTypeValue);
	}
}
