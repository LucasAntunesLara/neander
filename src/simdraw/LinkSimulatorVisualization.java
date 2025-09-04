/*
 * Created on 28/04/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package simdraw;

import org.jhotdraw.framework.Connector;

import processor.Processor;
import simulator.Simulator;

/**
 * @author unknown
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface LinkSimulatorVisualization {
//	public void refreshDisplay ( Simulador parSim, Processor parProc, Datapath parDtp);
	public void refreshDisplay ( Simulator parSim, Processor parProc);
	public Connector connectorAt(String parName);
}
