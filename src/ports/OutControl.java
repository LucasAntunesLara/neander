package ports;

public class OutControl extends Port {

	public OutControl ( String parName) {
		super ( parName, OUT);
	}

	public OutControl ( String parName, int parSize) {
		super ( parName, OUT, parSize);
	}

	public OutControl ( String parName, int parSize, int parTypeValue) {
		super ( parName, OUT, parSize, parTypeValue);
	}
}
