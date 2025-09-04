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
 * @author unknown
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class MultiplexerV2Figure extends TDSimFigure implements LinkSimulatorVisualization, Define {
	/**
	 * 
	 */
	public MultiplexerV2Figure() {
		// TODO Auto-generated constructor stub
		super ( );
	}

	//protected static final int BORDER = 1;
	protected List        fConnectors;
	private boolean     fConnectorsVisible;

	public MultiplexerV2Figure(String parName, String parE1, String parE2, 
							String parS1, String parSEL) {
		initialize();
		fConnectors = null;
		sName = parName;
		sE1 = parE1;
		sE2 = parE2;
		sS1 = parS1;
		sSEL = parSEL;
		sValueE1 = sE1;
		sValueE2 = sE2;
		sValueS1 = sS1;
		sValueSEL = sSEL;
		colorDraw = Color.WHITE;
		colorText = Color.BLACK;
		setBaseSize ( 36, 36);
	}

	public Rectangle displayBox() {
		Rectangle box = super.displayBox();
		//int d = BORDER;
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

		p.addPoint(x,y+(int)(h*0.75));
		p.addPoint(x+w,y+(int)(h*0.75));
		p.addPoint(x+(int)(w*0.65),y+(int)(h*0.25));
		p.addPoint(x+(int)(w*0.35),y+(int)(h*0.25));
		p.addPoint(x,y+(int)(h*0.75));

		return p;
	}

	protected void drawFigure (Graphics g) {
		Rectangle r = displayBox();
		Polygon p = getPolygon();
		int x, y, w, h;
		x = r.x; y = r.y; w = r.width; h = r.height;
		//g.drawRect ( x, y, w, h);
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
		g.drawLine(x,y+(int)(h*0.5),x+(int)(w*0.166),y+(int)(h*0.5));
		g.drawLine(x+(int)(w*0.25),y+(int)(h*0.75),x+(int)(w*0.25),y+h);
		g.drawLine(x+(int)(w*0.75),y+(int)(h*0.75),x+(int)(w*0.75),y+h);
		g.drawLine(x+(int)(w*0.5),y,x+(int)(w*0.5),y+(int)(h*0.25));
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
		g.drawString(sValueE1, x+(int)(w*0.30), y+(int)(h*0.95));
		g.drawString(sValueE2, x+(int)(w*0.80), y+(int)(h*0.95));
		g.drawString(sName, x+(int)(w*0.25), y+(int)(h*0.75));
		g.drawString(sValueS1,x+(int)(w*0.55),y+(int)(h*0.25));
		g.drawString(sValueSEL, x, y+(int)(h*0.50));
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
		handles.add(new ConnectionHandle(this, RelativeLocator.north(), prototype));
//		handles.add(new ConnectionHandle(this, RelativeLocator.west(), prototype));
//		handles.add(new ConnectionHandle(this, RelativeLocator.south(), prototype));
//		handles.add(new ConnectionHandle(this, RelativeLocator.north(), prototype));

//		handles.add(new NullHandle(this, RelativeLocator.southEast()));
		handles.add(new ConnectionHandle(this, new RelativeLocator ( 0.25, 1),prototype));//RelativeLocator.southWest()));
//		handles.add(new NullHandle(this, RelativeLocator.northEast()));
		handles.add(new ConnectionHandle(this, new RelativeLocator ( 0.75, 1),prototype));
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
		fConnectors.add(new PortConnector(this, RelativeLocator.north(),sS1) );
		fConnectors.add(new PortConnector(this, new RelativeLocator(0.25,1),sE1) );
		fConnectors.add(new PortConnector(this, new RelativeLocator(0.75,1),sE2) );
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
		lTmp = dtp.execute ( sName, GET, sE2, IN);
		//sValueE2 = new Long ( lTmp).toString();
		sValueE2 = SistNum.printInformation ( lTmp, INTEGER);
		lTmp = dtp.execute ( sName, GET, sS1, OUT);
		//sValueS1 = new Long ( lTmp).toString();
		sValueS1 = SistNum.printInformation ( lTmp, INTEGER);
		if ( sSEL != null) {
			lTmp = dtp.execute ( sName, GET, sSEL, CONTROL);
			//sValueSEL = new Long ( lTmp).toString();
			sValueSEL = SistNum.printInformation ( lTmp, INTEGER);
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
	
	String sName, sE1, sE2, sS1, sSEL, sValueE1, sValueE2, sValueSEL, sValueS1;
	Color colorDraw, colorText;
}
