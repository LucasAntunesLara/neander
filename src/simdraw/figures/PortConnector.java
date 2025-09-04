/*
 * Created on 29/04/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package simdraw.figures;

import org.jhotdraw.framework.Figure;
import org.jhotdraw.framework.Locator;
import org.jhotdraw.standard.LocatorConnector;

/**
 * @author unknown
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class PortConnector extends LocatorConnector {

	/**
	 * 
	 */
	public PortConnector() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param owner
	 * @param l
	 */
	public PortConnector(Figure owner, Locator l, String s) {
		super(owner, l);
		sName = s;
		// TODO Auto-generated constructor stub
	}

	public String getName ( ) {
		return sName;
	}
	
	String sName;
}
