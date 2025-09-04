/*
 * Created on 02/05/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package simdraw.figures;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

import org.jhotdraw.framework.HandleEnumeration;
import org.jhotdraw.standard.HandleEnumerator;
import org.jhotdraw.util.CollectionsFactory;

import processor.Processor;
import simdraw.LinkSimulatorVisualization;
import simulator.Simulator;

/**
 * @author unknown
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class TimerFigure extends MultiplexerV2Figure implements LinkSimulatorVisualization {
	/**
	 * 
	 */
	public TimerFigure(String parName, int parFontSize) {
		super();
		sName = parName;
		iFontSize = parFontSize;
		sTime = "0.0";
		setBaseSize ( 36, 36);
	}

	public Rectangle displayBox() {
		Rectangle box = super.displayBox();
		//int d = BORDER;
		box.grow(getSizeX ( ), getSizeY ( ));
		return box;
	}
	
	protected void drawStrings (Graphics g) {
		Rectangle r = displayBox();
		int x, y, w, h;
		x = r.x; y = r.y; w = r.width; h = r.height;
		g.setColor ( colorText);
		Font fb = new Font("Helvetica", Font.PLAIN, iFontSize);
		g.setFont(fb);
		g.drawString(sName, x, y+(int)(h*0.5));
		g.drawString("Time: "+sTime,x,y+(int)(h*0.75));
		g.setColor(getFrameColor());
	}

	protected void drawTimer(Graphics g) {
		drawFigure ( g);
		drawStrings ( g);
	}

	public void draw(Graphics g) {
		drawTimer (g);
	}

	public HandleEnumeration handles() {
		List handles = CollectionsFactory.current().createList();
		return new HandleEnumerator(handles);
	}
	
	protected void createConnectors() {

	}

	public void refreshDisplay ( Simulator parSim, Processor parProc) {
		float fTmp;
		
		fTmp = parSim.getTime();
		sTime = new Float ( fTmp).toString();
		invalidate ( );
	}
		
	String sName, sTime;
	int iFontSize;
}
