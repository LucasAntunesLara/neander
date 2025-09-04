/*
 * Created on 01/05/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
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

import processor.Processor;
import simdraw.LinkSimulatorVisualization;
import simulator.Simulator;
import util.Define;
import util.SistNum;
import ccomb.Circuit;
import datapath.Datapath;

/**
 * @author unknown
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class RegisterBankFigure extends TDSimFigure implements LinkSimulatorVisualization, Define {
	/**
	 * 
	 */
	public RegisterBankFigure() {
		// TODO Auto-generated constructor stub
		super ( );
	}

	//protected static final int BORDER = 1;
	protected List        fConnectors;
	private boolean     fConnectorsVisible;

	public RegisterBankFigure(String parName, String parE1, String parS1, 
							String parS2, String parNW1, String parNR1, String parNR2) {
		initialize();
		fConnectors = null;
		sName = parName;
		sE1 = parE1;
		sS1 = parS1;
		sS2 = parS2;
		sNW1 = parNW1;
		sNR1 = parNR1;
		sNR2 = parNR2;
		sValueE1 = sE1;
		sValueS1 = sS1;
		sValueS2 = sS2;
		sValueNW1 = sNW1;
		sValueNR1 = sNR1;
		sValueNR2 = sNR2;
		colorDraw = Color.WHITE;
		colorText = Color.BLACK;
		setBaseSize ( 48, 60);
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

		p.addPoint(x+(int)(w*0.25),y+(int)(h*0.25));
		p.addPoint(x+(int)(w*0.75),y+(int)(h*0.25));
		p.addPoint(x+(int)(w*0.75),y+(int)(h*0.75));
		p.addPoint(x+(int)(w*0.25),y+(int)(h*0.75));
		p.addPoint(x+(int)(w*0.25),y+(int)(h*0.25));

		return p;
	}

	protected void drawFigure (Graphics g) {
		Rectangle r = displayBox();
		Polygon p = getPolygon();
		int x, y, w, h;
		x = r.x; y = r.y; w = r.width; h = r.height;
		g.setColor ( colorDraw);
		g.fillPolygon( p);
		g.drawPolygon(p);		
		g.setColor(getFrameColor());
	}
	
	protected void drawPorts (Graphics g) {
		Rectangle r = displayBox();
		Polygon p = getPolygon();
		int x, y, w, h;
		x = r.x; y = r.y; w = r.width; h = r.height;
		g.setColor ( colorDraw);	
		g.drawLine(x,y+(int)(h*0.5),x+(int)(w*0.25),y+(int)(h*0.5));
		g.drawLine(x+(int)(w*0.75),y+(int)(h*0.33),x+w,y+(int)(h*0.33));
		g.drawLine(x+(int)(w*0.75),y+(int)(h*0.66),x+w,y+(int)(h*0.66));
		g.drawLine(x+(int)(w*0.33),y,x+(int)(w*0.33),y+(int)(h*0.25));
		g.drawLine(x+(int)(w*0.66),y,x+(int)(w*0.66),y+(int)(h*0.25));
		g.drawLine(x+(int)(w*0.5),y+(int)(h*0.75),x+(int)(w*0.5),y+h);
		g.setColor(getFrameColor());
	}
	
	protected void drawStrings (Graphics g) {
		Rectangle r = displayBox();
		Polygon p = getPolygon();
		int x, y, w, h;
		x = r.x; y = r.y; w = r.width; h = r.height;
		g.setColor ( colorText);
		Font fb = new Font("Helvetica", Font.PLAIN, 9);
		g.setFont(fb);
		g.drawString(sValueE1, x, y+(int)(h*0.5));
		g.drawString(sValueS1, x+(int)(w*0.79), y+(int)(h*0.33));
		g.drawString(sName, x+(int)(w*0.25), y+(int)(h*0.5));
		g.drawString(sValueS2,x+(int)(w*0.79),y+(int)(h*0.66));
		g.drawString(sValueNW1, x+(int)(w*0.54), y+(int)(h*0.85));
		g.drawString(sValueNR1,x+(int)(w*0.37),y+(int)(h*0.05));
		g.drawString(sValueNR2, x+(int)(w*0.69), y+(int)(h*0.05));
		g.setColor(getFrameColor());
	}
		
	protected void drawRegisterBankFigure(Graphics g) {
		drawFigure ( g);
		drawPorts ( g);
		drawStrings ( g);
	}

	public void draw(Graphics g) {
//		super.draw(g);
		drawRegisterBankFigure(g);
		drawConnectors(g);
	}

	public HandleEnumeration handles() {
		ConnectionFigure prototype = new ConnectionFigure();
		List handles = CollectionsFactory.current().createList();
		handles.add(new ConnectionHandle(this, new RelativeLocator ( 0, 0.5),prototype));
		handles.add(new ConnectionHandle(this, new RelativeLocator ( 1, 0.33),prototype));
		handles.add(new ConnectionHandle(this, new RelativeLocator ( 1, 0.66),prototype));
		return new HandleEnumerator(handles);
	}

	protected void drawConnectors(Graphics g) {
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

	protected void createConnectors() {
		fConnectors = CollectionsFactory.current().createList(3);
		fConnectors.add(new PortConnector(this, new RelativeLocator(0,0.5),sE1) );
		fConnectors.add(new PortConnector(this, new RelativeLocator(1,0.33),sS1) );
		fConnectors.add(new PortConnector(this, new RelativeLocator(1,0.66),sS2) );
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
		sValueE1 = SistNum.printInformation ( lTmp, INTEGER);
		lTmp = dtp.execute ( sName, GET, sS1, OUT);
		sValueS1 = SistNum.printInformation ( lTmp, INTEGER);
		lTmp = dtp.execute ( sName, GET, sS2, OUT);
		sValueS2 = SistNum.printInformation ( lTmp, INTEGER);
		if ( sNW1 != null) {
			lTmp = dtp.execute ( sName, GET, sNW1, CONTROL);
			sValueNW1 = SistNum.printInformation ( lTmp, INTEGER);
		}
		if ( sNR1 != null) {
			lTmp = dtp.execute ( sName, GET, sNR1, CONTROL);
			sValueNR1 = SistNum.printInformation ( lTmp, INTEGER);
		}
		if ( sNR2 != null) {
			lTmp = dtp.execute ( sName, GET, sNR2, CONTROL);
			sValueNR2 = SistNum.printInformation ( lTmp, INTEGER);
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
	
	String sName, sE1, sS1, sS2, sNW1, sNR1, sNR2, sValueE1, sValueS1, sValueS2, sValueNW1,sValueNR1,sValueNR2;
	Color colorDraw, colorText;
}
