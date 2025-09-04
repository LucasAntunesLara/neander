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

import ports.Control;
import ports.InControl;
import ports.OutControl;
import ports.SetPort;
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
public class GenericMultiplexerFigure extends TDSimFigure implements LinkSimulatorVisualization, Define {
	/**
	 * 
	 */
	public GenericMultiplexerFigure() {
		// TODO Auto-generated constructor stub
		super ( );
	}

	//protected static final int BORDER = 36;
	private List        fConnectors;
	private boolean     fConnectorsVisible;

	public GenericMultiplexerFigure(String parName, Processor parProc) {
		initialize();
		fConnectors = null;
		sName = parName;

		pProc = parProc;

		Circuit cCircuit;
		Datapath dtp = parProc.getDatapath ( );
		
		cCircuit = dtp.search ( sName);
		SetPort spAux = cCircuit.getSetPort ( IN);
		iEntries = spAux.getNelems();
		sE = new String [ iEntries];
		sValueE = new String [ iEntries];
		int i;
		for ( i = 0; i < iEntries; i ++) {
			InControl ip = (InControl) spAux.traverse ( i);
			sE [ i] = ip.getName ( );
			sValueE [ i] = sE [ i];
		}
		
		spAux = cCircuit.getSetPort ( OUT);
		iOuts = spAux.getNelems();
		sS = new String [ iOuts];
		sValueS = new String [ iOuts];
		for ( i = 0; i < iOuts; i ++) {
			OutControl op = (OutControl) spAux.traverse ( i);
			sS [ i] = op.getName ( );
			sValueS [ i] = sS [ i];
		}

		spAux = cCircuit.getSetPort ( CONTROL);
		iControls = spAux.getNelems ( );
		sC = new String [ iControls];
		sValueC = new String [ iControls];
		for ( i = 0; i < 1; i ++) {
			Control c = ( Control) spAux.traverse ( i);
			sC [ i] = c.getName ( );
			sValueC [ i] = sC [i];
		}	
		
		colorDraw = Color.WHITE;
		colorText = Color.BLACK;
		setBaseSize ( 36, 36);
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

		p.addPoint(x+(int)(w*0.25),y);
		p.addPoint(x+(int)(w*0.75),y+(int)(h*0.35));
		p.addPoint(x+(int)(w*0.75),y+(int)(h*0.65));
		p.addPoint(x+(int)(w*0.25),y+h);
		p.addPoint(x+(int)(w*0.25),y);

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
		Polygon p = getPolygon();
		int x, y, w, h;
		x = r.x; y = r.y; w = r.width; h = r.height;
		g.setColor ( colorDraw);
		
		int i;
		//System.out.println ( "iEntries = "+iEntries);
		float fDistance = ( float) 1 / (iEntries+1);
		//System.out.println ( "fDistance = "+fDistance);
		for ( i = 0; i < iEntries; i ++) {
			int mult=i+1;
			int yDes = y;
			int hDes = h;
			g.drawLine(x,yDes+(int)(hDes*fDistance*mult),x+(int)(w*0.25),yDes+(int)(hDes*fDistance*mult));
		}
		fDistance = ( float) 1 / (iOuts+1);
		for ( i = 0; i < iOuts; i ++) {
			int mult=i+1;
			int yDes = y + (int)(h*0.25);
			int hDes = (int) (h*0.5);
			g.drawLine(x+(int)(w*0.75),yDes+(int)(hDes*fDistance*mult),x+w,yDes+(int)(hDes*fDistance*mult));
		}
		fDistance = ( float) 1 / (iControls+1);
		for ( i = 0; i < iOuts; i ++) {
			int mult=i+1;
			int xDes = x + (int)(w*0.25);
			int wDes = (int) (w*0.5);
			g.drawLine(xDes+(int)(wDes*fDistance*mult),y+(int)(h*0.75),xDes+(int)(wDes*fDistance*mult),y+h);
		}

		g.setColor(getFrameColor());
	}
	
	private void drawStrings (Graphics g) {
		Rectangle r = displayBox();
		Polygon p = getPolygon();
		int x, y, w, h;
		x = r.x; y = r.y; w = r.width; h = r.height;
		g.setColor ( colorText);
		Font fb = new Font("Helvetica", Font.PLAIN, 9);
		g.setFont(fb);
		
		g.drawString(sName, x+(int)(w*0.25), y+(int)(h*0.5));
		int i;
		float fDistance = ( float) 1 / (iEntries+1);
		for ( i = 0; i < iEntries; i ++) {
			int mult=i+1;
			int yDes = y;
			int hDes = h;
			g.drawString(sValueE[i],x,yDes+(int)(hDes*fDistance*mult));
		}		
		fDistance = ( float) 1 / (iOuts+1);
		for ( i = 0; i < iOuts; i ++) {
			int mult=i+1;
			int yDes = y + (int)(h*0.25);
			int hDes = (int) (h*0.5);
			g.drawString(sValueS[i],x+(int)(w*0.75),yDes+(int)(hDes*fDistance*mult));
		}	
		
		g.drawString(sValueC [0], x+(int)(w*0.60), y+(int)(h*0.95));
		
		g.setColor(getFrameColor());
	}
		
	protected void drawMultiplexerFigure(Graphics g) {
		drawFigure ( g);
		drawPorts ( g);
		drawStrings ( g);
	}

	public void draw(Graphics g) {
//		super.draw(g);
		drawMultiplexerFigure(g);
		drawConnectors(g);
	}

	public HandleEnumeration handles() {
		ConnectionFigure prototype = new ConnectionFigure();
		List handles = CollectionsFactory.current().createList();
		
		int i;
		float fDistance = ( float) 1 / (iEntries+1);
		for ( i = 0; i < iEntries; i ++) {
			int mult=i+1;
			handles.add(new ConnectionHandle(this, new RelativeLocator ( 0, fDistance*mult),prototype));
		}		
		fDistance = ( float) 1 / (iOuts+1);
		for ( i = 0; i < iOuts; i ++) {
			int mult=i+1;
			handles.add(new ConnectionHandle(this, new RelativeLocator ( 1, ((fDistance*mult)*0.5)+0.25),prototype));
		}
		
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
		fConnectors = CollectionsFactory.current().createList(iEntries+iOuts);
		
		int i;
		float fDistance = ( float) 1 / (iEntries+1);
		for ( i = 0; i < iEntries; i ++) {
			int mult=i+1;
			fConnectors.add(new PortConnector(this, new RelativeLocator(0, fDistance*mult),sE[i]) );
		}		
		fDistance = ( float) 1 / (iOuts+1);
		for ( i = 0; i < iOuts; i ++) {
			int mult=i+1;
			fConnectors.add(new PortConnector(this, new RelativeLocator(1, ((fDistance*mult)*0.5)+0.25),sS[i]) );
		}	
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
		long lTmp;
		int i;
		Circuit cCircuit;
		Datapath dtp = parProc.getDatapath ( );

		for ( i = 0; i < iEntries; i ++) {
			lTmp = dtp.execute ( sName, GET, sE[i], IN);
			sValueE[i] = SistNum.printInformation ( lTmp, INTEGER);
		}
		for ( i = 0; i < iOuts; i ++) {
			lTmp = dtp.execute ( sName, GET, sS[i], OUT);
			sValueS[i] = SistNum.printInformation ( lTmp, INTEGER);
		}
		for ( i = 0; i < iControls; i ++) {
			lTmp = dtp.execute ( sName, GET, sC[i], CONTROL);
			sValueC[i] = SistNum.printInformation ( lTmp, INTEGER);
		}	
		
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
	
	String sName;
	Processor pProc;
	int iEntries;
	String [] sE, sValueE;
	int iOuts;
	String [] sS, sValueS;
	int iControls;
	String [] sC, sValueC;
	
	Color colorDraw, colorText;
}
