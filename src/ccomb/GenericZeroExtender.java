package ccomb;

import ports.InControl;
import ports.OutControl;
import ports.SetPort;
import bus.Bus;
import bus.SetBus;

public class GenericZeroExtender extends Combinational{
	
	public GenericZeroExtender(String parName, int parSizeIn, int parSizeOut){
		sbName = new StringBuffer().append(parName);
		
		spIn = new SetPort();
		spOut = new SetPort();
		sbBus = new SetBus();

		peE1 = new InControl("E0", parSizeIn);
		spIn.add(peE1);
		psS1 = new OutControl("S0", parSizeOut);
		spOut.add ( psS1);
		bBs1 = new Bus(this, psS1, null, null);
		sbBus.add(bBs1);
	}
	
	public void behavior(){
		
	}
	
	public void debug(){
		
	}
	
	public void list (){
		
	}
	
	public static void help (){
		
	}
	
	public static void test (){
		
	}
	
	private InControl peE1;
	private OutControl psS1;
	private Bus bBs1;

}
