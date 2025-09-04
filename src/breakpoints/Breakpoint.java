package breakpoints;

import primitive.Primitive;
import util.Define;
import message.MicroOperation;

public class Breakpoint extends Primitive implements Define {
	
	public Breakpoint() {
		active = true;
	}
	
	public void debug() {
		
	}
	
	public void list() {
		
	}
	
	public boolean active;
	public MicroOperation mp;
}
