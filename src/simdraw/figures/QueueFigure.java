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

import org.jhotdraw.standard.HandleEnumerator;

import org.jhotdraw.util.CollectionsFactory;
import org.jhotdraw.util.Geom;
import org.jhotdraw.util.StorableInput;
import org.jhotdraw.util.StorableOutput;

import control.InstructionQueue;
import control.QueuesOfInstructions;

import processor.Processor;
import simdraw.LinkSimulatorVisualization;
import simulator.Simulator;
import util.Define;

/**
 * @version <$CURRENT_VERSION$>
 */
public class QueueFigure extends TDSimFigure implements LinkSimulatorVisualization, Define {
	/**
	 * 
	 */
	public QueueFigure() {
		// TODO Auto-generated constructor stub
		super ( );
	}

	//protected static final int BORDER = 48;
	private List        fConnectors;
	private boolean     fConnectorsVisible;

	public QueueFigure(String parName, Processor parProc) {
		initialize();
		fConnectors = null;
		sName = parName;
		pProc = parProc;
		
		QueuesOfInstructions qoi = null;
		InstructionQueue iq = null;
		qoi = pProc.getQueuesOfInstructions();
		if ( qoi != null) {
			iq = qoi.search(parName);
		}
		if ( iq != null) {
			iNelems = iq.getInstructions().getNelems();
		}
		
		colorDraw = Color.GRAY;
		colorText = Color.BLACK;
		setBaseSize ( 24, 24);
	}

	public Rectangle displayBox() {
		Rectangle box = super.displayBox();
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

		p.addPoint(x, y+(int)(h*0.25));
		p.addPoint(x+w, y+(int)(h*0.25));
		p.addPoint(x+w, y+(int)(h*0.75));
		p.addPoint(x, y+(int)(h*0.75));
		p.addPoint(x, y+(int)(h*0.25));

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

		g.setColor(getFrameColor());
	}
	
	private void drawStrings (Graphics g) {
		Rectangle r = displayBox();
		int x, y, w, h;
		x = r.x; y = r.y; w = r.width; h = r.height;
		g.setColor( colorText);
		Font fb = new Font("Helvetica", Font.PLAIN, 10);
		g.setFont(fb);
		g.drawString(sName+","+iNelems, x+(int)(w*0.05), y+(int)(h*0.6));
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
		fConnectors = CollectionsFactory.current().createList(1);

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
		QueuesOfInstructions qoi = null;
		InstructionQueue iq = null;
		qoi = pProc.getQueuesOfInstructions();
		if ( qoi != null) {
			iq = qoi.search(sName);
		}
		if ( iq != null) {
			iNelems = iq.getInstructions().getNelems();
		}
		if ( iNelems > 0) colorDraw = Color.MAGENTA;
		else colorDraw = Color.GRAY;

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
	
	Processor pProc;
	String sName;
	Color colorDraw, colorText;
	int iNelems;
}
