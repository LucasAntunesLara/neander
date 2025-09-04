/*
 * @(#)NodeFigure.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package simdraw.figures;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.Iterator;
import java.util.List;

import org.jhotdraw.framework.Connector;
import org.jhotdraw.framework.Figure;
import org.jhotdraw.framework.HandleEnumeration;
import org.jhotdraw.standard.ConnectionHandle;
import org.jhotdraw.standard.HandleEnumerator;
import org.jhotdraw.standard.RelativeLocator;
import org.jhotdraw.util.CollectionsFactory;
import org.jhotdraw.util.Geom;
import org.jhotdraw.util.StorableInput;
import org.jhotdraw.util.StorableOutput;

import processor.Processor;
import simdraw.LinkSimulatorVisualization;
import simulator.Simulator;
import util.Define;
import util.SistNum;
import ccomb.Circuit;
import datapath.Datapath;

/**
 * @version <$CURRENT_VERSION$>
 */
public class RegisterFigure extends TDSimFigure implements LinkSimulatorVisualization, Define {
	/**
	 * 
	 */
	public RegisterFigure() {
		// TODO Auto-generated constructor stub
		super ( );
	}

	//protected static final int BORDER = 48;
	private List        fConnectors;
	private boolean     fConnectorsVisible;

	public RegisterFigure(String parName, String parE1, String parS1) {
		initialize();
		fConnectors = null;
		sName = parName;
		sE1 = parE1;
		sS1 = parS1;
		sValueE1 = sE1;
		sValueS1 = sS1;
		sContent = "";
		colorDraw = Color.WHITE;
		colorText = Color.BLACK;
		setBaseSize ( 48, 12);
	}

	public Rectangle displayBox() {
		Rectangle box = super.displayBox();
		//int d = BORDER;
		//box.grow(d, d/4);
		box.grow(getSizeX ( ), getSizeY ( ));
		return box;
	}

	public boolean containsPoint(int x, int y) {
		/* add slop for connectors
		if (fConnectorsVisible) {
			Rectangle r = displayBox();
			int d = LocatorConnector.SIZE/2;
			r.grow(d, d);
			return r.contains(x, y);
		}*/
		return super.containsPoint(x, y);
	}

	protected Polygon getPolygon() {
		Rectangle r = displayBox();
		int x, y, w, h;
		x = r.x; y = r.y; w = r.width; h = r.height;		
		Polygon p = new Polygon();

		p.addPoint(x+(int)(w*0.25), y);
		p.addPoint(x+(int)(w*0.75), y);
		p.addPoint(x+(int)(w*0.75), y+h);
		p.addPoint(x+(int)(w*0.25), y+h);
		p.addPoint(x+(int)(w*0.25), y);

		return p;
	}
	
	private void drawFigure (Graphics g) {
		Rectangle r = displayBox();
		Polygon p = getPolygon();
		int x, y, w, h;
		x = r.x; y = r.y; w = r.width; h = r.height;
		g.setColor ( colorDraw);
		g.fillPolygon( p);
		g.drawPolygon(p);		
		g.setColor(getFrameColor());
	}
	
	private void drawPorts (Graphics g) {
		Rectangle r = displayBox();
		int x, y, w, h;
		x = r.x; y = r.y; w = r.width; h = r.height;
		g.setColor ( colorDraw);		
		g.drawLine(x,y+(h/2),x+(int)(w*0.25),(int) (y+(h/2)));
		g.drawLine(x+(int)(w*0.75),y+(h/2),x+w,y+(h/2));
		g.setColor(getFrameColor());
	}
	
	private void drawStrings (Graphics g) {
		Rectangle r = displayBox();
		int x, y, w, h;
		x = r.x; y = r.y; w = r.width; h = r.height;
		g.setColor ( colorText);
		Font fb = new Font("Helvetica", Font.PLAIN, 10);
		g.setFont(fb);
		g.drawString(sValueE1, x+(int)(w*0.05), y+(int)(h*0.4));
		g.drawString(sValueS1,x+(int)(w*0.80),y+(int)(h*0.4));
		g.drawString(sName+"="+sContent, x+(int)(w*0.28), y+(int)(h*0.5));		
		g.setColor(getFrameColor());
	}
	
	protected void drawRegisterFigure(Graphics g) {
		drawFigure ( g);
		drawPorts ( g);
		drawStrings ( g);
	}

	public void draw(Graphics g) {
//		super.draw(g);
		drawRegisterFigure(g);
		drawConnectors(g);
	}

	public HandleEnumeration handles() {
		ConnectionFigure prototype = new ConnectionFigure();
		List handles = CollectionsFactory.current().createList();
		handles.add(new ConnectionHandle(this, RelativeLocator.east(), prototype));
		handles.add(new ConnectionHandle(this, RelativeLocator.west(), prototype));
//		handles.add(new ConnectionHandle(this, RelativeLocator.south(), prototype));
//		handles.add(new ConnectionHandle(this, RelativeLocator.north(), prototype));

//		handles.add(new NullHandle(this, RelativeLocator.southEast()));
//		handles.add(new NullHandle(this, RelativeLocator.southWest()));
//		handles.add(new NullHandle(this, RelativeLocator.northEast()));
//		handles.add(new NullHandle(this, RelativeLocator.northWest()));
		return new HandleEnumerator(handles);
	}

	private void drawConnectors(Graphics g) {
		if (fConnectorsVisible) {
			Iterator iter = connectors();
			while (iter.hasNext()) {
				((Connector)iter.next()).draw(g);
			}
		}
	}

	/**
	 */
	public void connectorVisibility(boolean isVisible, ConnectionFigure courtingConnection) {
		fConnectorsVisible = isVisible;
		invalidate();
	}

	/**
	 */
	public Connector connectorAt(int x, int y) {
		return findConnector(x, y);
	}

	/**
	 */
	public Connector connectorAt(String parName) {
		return findConnector(parName);
	}

	/**
	 */
	private Iterator connectors() {
		if (fConnectors == null) {
			createConnectors();
		}
		return fConnectors.iterator();
	}

	private void createConnectors() {
		fConnectors = CollectionsFactory.current().createList(2);
//		fConnectors.add(new LocatorConnector(this, RelativeLocator.north()) );
//		fConnectors.add(new LocatorConnector(this, RelativeLocator.south()) );
		pcE1 = new PortConnector(this, RelativeLocator.west(),sE1);
		pcS1 = new PortConnector(this, RelativeLocator.east(),sS1);
		fConnectors.add( pcE1);
		fConnectors.add( pcS1);
	}

	private Connector findConnector(int x, int y) {
		// return closest connector
		long min = Long.MAX_VALUE;
		Connector closest = null;
		Iterator iter = connectors();
		while (iter.hasNext()) {
			Connector c = (Connector)iter.next();
			Point p2 = Geom.center(c.displayBox());
			long d = Geom.length2(x, y, p2.x, p2.y);
			if (d < min) {
				min = d;
				closest = c;
			}
		}
		return closest;
	}

	private Connector findConnector(String parName) {
		PortConnector closest = null;
		Iterator iter = connectors();
		while (iter.hasNext()) {
			PortConnector c = (PortConnector)iter.next();
			if (c.getName().compareToIgnoreCase(parName) == 0) {
				closest = c;
			}
		}
		return closest;
	}
	
	private void initialize() {
//		setText("node");
//		Font fb = new Font("Helvetica", Font.PLAIN, 8);
//		setFont(fb);
		createConnectors();
	}

	/**
	 * Usually, a TextHolders is implemented by a Figure subclass. To avoid casting
	 * a TextHolder to a Figure this method can be used for polymorphism (in this
	 * case, let the (same) object appear to be of another type).
	 * Note, that the figure returned is not the figure to which the TextHolder is
	 * (and its representing figure) connected.
	 * @return figure responsible for representing the content of this TextHolder
	 */
	public Figure getRepresentingFigure() {
		return this;
	}

	public void refreshDisplay ( Simulator parSim, Processor parProc) {
		long lTmp;
		Circuit cCircuit;
		Datapath dtp = parProc.getDatapath ( );
		
		lTmp = dtp.execute ( sName, GET, sE1, IN);
		//sValueE1 = new Long ( lTmp).toString();
		sValueE1 = SistNum.printInformation ( lTmp, INTEGER);
		lTmp = dtp.execute ( sName, GET, sS1, OUT);
		//sValueS1 = new Long ( lTmp).toString();
		sValueS1 = SistNum.printInformation ( lTmp, INTEGER);
		lTmp = dtp.execute ( sName, GET, 0, 0);
		//sContent = new Long ( lTmp).toString();
		sContent = SistNum.printInformation ( lTmp, INTEGER);
		cCircuit = dtp.search ( sName);
		if ( cCircuit != null) {
			if ( cCircuit.isUsed ( ) == true) {
				if ( cCircuit.getMethod() == WRITE) colorDraw = colorWRITE;
				else if ( cCircuit.getMethod() == READ) colorDraw = colorREAD;
				else if ( cCircuit.getMethod() == BEHAVIOR) colorDraw = colorBEHAVIOR;
			}
			else colorDraw = colorINACTIVE;
		} else colorDraw = colorINACTIVE;
		invalidate ( );
	}

	public String getName ( ) {
		return sName;	
	}
	
	public void write ( StorableOutput dw) {
		System.out.println ( "Gravando...");
	}
	
	public void read ( StorableInput dr) {
		System.out.println ( "Lendo...");		
	}
	
	String sName, sE1, sS1, sContent, sValueE1, sValueS1;
	PortConnector pcE1, pcS1;
	Color colorDraw, colorText;
}
