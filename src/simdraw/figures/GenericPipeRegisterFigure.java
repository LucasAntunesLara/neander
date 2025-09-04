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
public class GenericPipeRegisterFigure extends TDSimFigure implements LinkSimulatorVisualization, Define {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public GenericPipeRegisterFigure() {
		// TODO Auto-generated constructor stub
		super ( );
	}

	//protected static final int BORDER = 48;
	private List        fConnectors;
	private boolean     fConnectorsVisible;

	public GenericPipeRegisterFigure(	String parName, int parEntSai) {
		initialize();
		fConnectors = null;
		sName = parName;
		//
		int i;
		
		iEntSai = parEntSai;
		sE = new String [ iEntSai];
		sS = new String [ iEntSai];
		sValueE = new String [ iEntSai];
		sValueS = new String [ iEntSai];		
		
		for ( i = 0; i < iEntSai; i ++) {
			sE [i] = new String ( "E"+i);
			sValueE [i] = new String ( "E"+i);
			sS [i] = new String ( "S"+i);
			sValueS [i] = new String ( "S"+i);
		}
		
		//
		colorDraw = Color.WHITE;
		colorText = Color.BLACK;
		setBaseSize ( 24, 175);
	}

	public Rectangle displayBox() {
		Rectangle box = super.displayBox();
		box.grow(getSizeX ( ), getSizeY ( ));
		return box;
	}

	public boolean containsPoint(int x, int y) {

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
		int x;
		x = r.x; g.setColor ( colorDraw);
		g.fillPolygon( p);
		g.drawPolygon(p);		
		g.setColor(getFrameColor());
	}
	
	private void drawPorts (Graphics g) {
		Rectangle r = displayBox();
		int x, y, w, h;
		float step = (float) 1 / (iEntSai + 1);
		x = r.x; y = r.y; w = r.width; h = r.height;
		g.setColor ( colorDraw);		
		//
		for ( int i = 1; i < iEntSai+1; i ++) {
			g.drawLine(x,(int)(y+(h*(i*step))),x+(int)(w*0.25),(int)(y+(h*(i*step))));
			g.drawLine(x+(int)(w*0.75),(int)(y+(h*(i*step))),x+w,(int)(y+(h*(i*step))));			
		}
		//
		/*
		g.drawLine(x,(int)(y+(h*0.2)),x+(int)(w*0.25),(int)(y+(h*0.2)));
		g.drawLine(x+(int)(w*0.75),(int)(y+(h*0.2)),x+w,(int)(y+(h*0.2)));
		//
		g.drawLine(x,(int)(y+(h*0.4)),x+(int)(w*0.25),(int)(y+(h*0.4)));
		g.drawLine(x+(int)(w*0.75),(int)(y+(h*0.4)),x+w,(int)(y+(h*0.4)));
		//
		g.drawLine(x,(int)(y+(h*0.6)),x+(int)(w*0.25),(int)(y+(h*0.6)));
		g.drawLine(x+(int)(w*0.75),(int)(y+(h*0.6)),x+w,(int)(y+(h*0.6)));
		//
		g.drawLine(x,(int)(y+(h*0.8)),x+(int)(w*0.25),(int)(y+(h*0.8)));
		g.drawLine(x+(int)(w*0.75),(int)(y+(h*0.8)),x+w,(int)(y+(h*0.8)));
		*/
		g.setColor(getFrameColor());
	}
	
	private void drawStrings (Graphics g) {
		Rectangle r = displayBox();
		int x, y, w, h;
		float step = (float) 1 / (iEntSai + 1);
		x = r.x; y = r.y; w = r.width; h = r.height;
		g.setColor ( colorText);
		Font fb = new Font("Helvetica", Font.PLAIN, 10);
		g.setFont(fb);
		//
		for ( int i = 1; i < iEntSai+1; i ++) {
			g.drawString(sValueE[i-1], x+(int)(w*0.04), y+(int)(h*(i*step-0.01)));
			g.drawString(sValueS[i-1],x+(int)(w*0.80),y+(int)(h*(i*step+0.04)));			
		}
		/*
		g.drawString(sValueE1, x+(int)(w*0.04), y+(int)(h*0.19));
		g.drawString(sValueS1,x+(int)(w*0.80),y+(int)(h*0.24));
		//
		g.drawString(sValueE2, x+(int)(w*0.04), y+(int)(h*0.39));
		g.drawString(sValueS2,x+(int)(w*0.80),y+(int)(h*0.44));
		//
		g.drawString(sValueE3, x+(int)(w*0.04), y+(int)(h*0.59));
		g.drawString(sValueS3,x+(int)(w*0.80),y+(int)(h*0.64));
		//
		g.drawString(sValueE4, x+(int)(w*0.04), y+(int)(h*0.79));
		g.drawString(sValueS4,x+(int)(w*0.80),y+(int)(h*0.84));
		*/
		g.drawString(sName, x+(int)(w*0.28), y+(int)(h*0.5));		
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
		float step = (float) 1 / (iEntSai + 1);
		
		for ( int i = 1; i < iEntSai+1; i ++) {
			handles.add(new ConnectionHandle(this, new RelativeLocator ( 0, step*i),prototype));
			handles.add(new ConnectionHandle(this, new RelativeLocator ( 1, 0.2),prototype));			
		}
		//
		/*
		handles.add(new ConnectionHandle(this, new RelativeLocator ( 0, 0.2),prototype));
		handles.add(new ConnectionHandle(this, new RelativeLocator ( 1, 0.2),prototype));
		//
		handles.add(new ConnectionHandle(this, new RelativeLocator ( 0, 0.4),prototype));
		handles.add(new ConnectionHandle(this, new RelativeLocator ( 1, 0.4),prototype));
		//
		handles.add(new ConnectionHandle(this, new RelativeLocator ( 0, 0.6),prototype));
		handles.add(new ConnectionHandle(this, new RelativeLocator ( 1, 0.6),prototype));
		//
		handles.add(new ConnectionHandle(this, new RelativeLocator ( 0, 0.8),prototype));
		handles.add(new ConnectionHandle(this, new RelativeLocator ( 1, 0.8),prototype));
		*/
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
		fConnectors = CollectionsFactory.current().createList(iEntSai*2);
		float step = (float) 1 / (iEntSai + 1);
		
		for ( int i = 1; i < iEntSai+1; i ++) {
			fConnectors.add(new PortConnector(this, new RelativeLocator(0,step*i),sE[i-1]) );
			fConnectors.add(new PortConnector(this, new RelativeLocator(1,step*i),sS[i-1]) );		
		}		
		//
		/*
		fConnectors.add(new PortConnector(this, new RelativeLocator(0,step*i),sE[i-1]) );
		fConnectors.add(new PortConnector(this, new RelativeLocator(1,step*i),sS[i-1]) );
		//
		fConnectors.add(new PortConnector(this, new RelativeLocator(0,0.4),sE2) );
		fConnectors.add(new PortConnector(this, new RelativeLocator(1,0.4),sS2) );
		//
		fConnectors.add(new PortConnector(this, new RelativeLocator(0,0.6),sE3) );
		fConnectors.add(new PortConnector(this, new RelativeLocator(1,0.6),sS3) );
		//
		fConnectors.add(new PortConnector(this, new RelativeLocator(0,0.8),sE4) );
		fConnectors.add(new PortConnector(this, new RelativeLocator(1,0.8),sS4) );
		*/
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
		
		for ( int i = 0; i < iEntSai; i ++) {
			lTmp = dtp.execute ( sName, GET, sE[i], IN);
			sValueE[i] = SistNum.printInformation ( lTmp, INTEGER);
			lTmp = dtp.execute ( sName, GET, sS[i], OUT);
			sValueS[i] = SistNum.printInformation ( lTmp, INTEGER);			
		}
		//
		/*
		lTmp = dtp.execute ( sName, GET, sE1, IN);
		sValueE1 = SistNum.printInformation ( lTmp, INTEGER);
		lTmp = dtp.execute ( sName, GET, sS1, OUT);
		sValueS1 = SistNum.printInformation ( lTmp, INTEGER);
		//
		lTmp = dtp.execute ( sName, GET, sE2, IN);
		sValueE2 = SistNum.printInformation ( lTmp, INTEGER);
		lTmp = dtp.execute ( sName, GET, sS2, OUT);
		sValueS2 = SistNum.printInformation ( lTmp, INTEGER);
		//
		lTmp = dtp.execute ( sName, GET, sE3, IN);
		sValueE3 = SistNum.printInformation ( lTmp, INTEGER);
		lTmp = dtp.execute ( sName, GET, sS3, OUT);
		sValueS3 = SistNum.printInformation ( lTmp, INTEGER);
		//
		lTmp = dtp.execute ( sName, GET, sE4, IN);
		sValueE4 = SistNum.printInformation ( lTmp, INTEGER);
		lTmp = dtp.execute ( sName, GET, sS4, OUT);
		sValueS4 = SistNum.printInformation ( lTmp, INTEGER);
		*/
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
	
	String sName, sE[], sS[];
	String sValueE[], sValueS[];
	PortConnector pcE1, pcS1;
	Color colorDraw, colorText;
	int iEntSai;
}
