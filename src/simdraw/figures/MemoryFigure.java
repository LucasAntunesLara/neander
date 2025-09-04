/*
 * Created on 01/05/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package simdraw.figures;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;

import org.jhotdraw.framework.HandleEnumeration;
import org.jhotdraw.standard.ConnectionHandle;
import org.jhotdraw.standard.HandleEnumerator;
import org.jhotdraw.standard.RelativeLocator;
import org.jhotdraw.util.CollectionsFactory;

import processor.Processor;
import simulator.Simulator;
import util.SistNum;
import ccomb.Circuit;
import datapath.Datapath;


/**
 * @author unknown
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class MemoryFigure extends MultiplexerV2Figure {

	/**
	 * 
	 */
	public MemoryFigure() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param parName
	 * @param parE1
	 * @param parE2
	 * @param parS1
	 * @param parSEL
	 */
	public MemoryFigure(
		String parName,
		String parE1,
		String parE2,
		String parS1) {
		super(parName, parE1, parE2, parS1, null);
		setBaseSize ( 45, 48);
		// TODO Auto-generated constructor stub
	}

	public Rectangle displayBox() {
		Rectangle box = super.displayBox();
		//int d = BORDER;
		box.grow(getSizeX ( ), getSizeY ( ));
		return box;
	}
	
	protected Polygon getPolygon() {
		Rectangle r = displayBox();
		int x, y, w, h;
		x = r.x; y = r.y; w = r.width; h = r.height;		
		Polygon p = new Polygon();

		p.addPoint(x+(int)(w*0.25),y+(int)(h*0.25));
		p.addPoint(x+(int)(w*0.75),y+(int)(h*0.25));
		p.addPoint(x+(int)(w*0.75),y+(int)(h*0.75));
		p.addPoint(x+(int)(w*0.25),y+(int)(h*0.75));
		p.addPoint(x+(int)(w*0.25),y+(int)(h*0.25));

		return p;
	}

	protected void drawPorts (Graphics g) {
		Rectangle r = displayBox();
		Polygon p = getPolygon();
		int x, y, w, h;
		x = r.x; y = r.y; w = r.width; h = r.height;
		g.setColor ( colorDraw);	
		g.drawLine(x,y+(int)(h*0.5),x+(int)(w*0.25),(int) (y+(h*0.5)));
		g.drawLine(x+(int)(w*0.75),y+(int)(h*0.5),x+w,(int) (y+(h*0.5)));
		g.drawLine(x+(int)(w*0.5),y+(int)(h*0.75),x+(int)(w*0.5),y+h);
		g.setColor(getFrameColor());
	}
	
	protected void drawStrings (Graphics g) {
		Rectangle r = displayBox();
		int x, y, w, h;
		x = r.x; y = r.y; w = r.width; h = r.height;
		g.setColor ( colorText);
		Font fb = new Font("Helvetica", Font.PLAIN, 9);
		g.setFont(fb);
		g.drawString(sValueE2, x, y+(int)(h*0.5));
		g.drawString(sValueE1, x+(int)(w*0.53), y+(int)(h*0.95));
		g.drawString(sName, x+(int)(w*0.25), y+(int)(h*0.5));
		g.drawString(sValueS1,x+(int)(w*0.78),y+(int)(h*0.5));
		g.setColor(getFrameColor());
	}
		
	protected void drawMemory(Graphics g) {
		drawFigure ( g);
		drawPorts ( g);
		drawStrings ( g);
	}

	public void draw(Graphics g) {
//		super.draw(g);
		drawMemory (g);
		drawConnectors(g);
	}
	
	public HandleEnumeration handles() {
		ConnectionFigure prototype = new ConnectionFigure();
		java.util.List handles = CollectionsFactory.current().createList();
//		handles.add(new ConnectionHandle(this, RelativeLocator.north(), prototype));
//		handles.add(new ConnectionHandle(this, RelativeLocator.west(), prototype));
		handles.add(new ConnectionHandle(this, RelativeLocator.south(), prototype));
//		handles.add(new ConnectionHandle(this, RelativeLocator.north(), prototype));

//		handles.add(new NullHandle(this, RelativeLocator.southEast()));
		handles.add(new ConnectionHandle(this, new RelativeLocator ( 0, 0.5),prototype));
//		handles.add(new NullHandle(this, RelativeLocator.northEast()));
		handles.add(new ConnectionHandle(this, new RelativeLocator ( 1, 0.5),prototype));
		return new HandleEnumerator(handles);
	}
	
	protected void createConnectors() {
		fConnectors = CollectionsFactory.current().createList(3);
		fConnectors.add(new PortConnector(this, RelativeLocator.south(),sE1) );
		fConnectors.add(new PortConnector(this, new RelativeLocator(0,0.5),sE2) );
		fConnectors.add(new PortConnector(this, new RelativeLocator(1,0.5),sS1) );
	}

	public void refreshDisplay ( Simulator parSim, Processor parProc) {
		long lTmp;
		Circuit cCircuit;
		Datapath dtp = parProc.getDatapath ( );
		
		lTmp = dtp.execute ( sName, GET, sE1, IN);
		sValueE1 = SistNum.printInformation ( lTmp, INTEGER);
		lTmp = dtp.execute ( sName, GET, sE2, IN);
		sValueE2 = SistNum.printInformation ( lTmp, INTEGER);
		lTmp = dtp.execute ( sName, GET, sS1, OUT);
		sValueS1 = SistNum.printInformation ( lTmp, INTEGER);
		cCircuit = dtp.search ( sName);
		if ( cCircuit != null) {
			if ( cCircuit.isUsed ( ) == true) {
				//System.out.println ( sName+" , "+cCircuit.getMethod ());
				if ( cCircuit.getMethod() == WRITE) colorDraw = colorWRITE;
				else if ( cCircuit.getMethod() == READ) colorDraw = colorREAD;
				else if ( cCircuit.getMethod() == BEHAVIOR) colorDraw = colorBEHAVIOR;
			}
			else colorDraw = colorINACTIVE;
		} else colorDraw = colorINACTIVE;
		invalidate ( );
	}
}
