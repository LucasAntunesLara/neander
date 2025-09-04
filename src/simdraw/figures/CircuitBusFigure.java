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

import ports.Control;
import ports.InControl;
import ports.OutControl;
import ports.SetPort;
import ports.Port;
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
public class CircuitBusFigure extends TDSimFigure implements LinkSimulatorVisualization, Define {
	/**
	 * 
	 */
	public CircuitBusFigure() {
		// TODO Auto-generated constructor stub
		super ( );
	}

	//protected static final int BORDER = 1;
	protected List        fConnectors;
	private boolean     fConnectorsVisible;

	public CircuitBusFigure(String parName, Processor parProc) {
		int i;
		SetPort spAux;
		
		initialize();
		fConnectors = null;
		sName = parName;
		pProc = parProc;

		Circuit cCircuit;
		Datapath dtp = parProc.getDatapath ( );
		
		cCircuit = dtp.search ( sName);
		spAux = cCircuit.getSetPort ( IN);
		iEntries = spAux.getNelems();
		sE = new String [ iEntries];
		sValueE = new String [ iEntries];
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

/*
		sC = new String [ iEntries+iOuts+1];
		sValueC = new String [ iEntries+iOuts+1];
		spAux = cCircuit.getSetPort ( CONTROL);
		for ( i = 0; i < iEntries; i ++) {
			Control c = ( Control) spAux.traverse ( i);
			sC [ i] = c.getName ( );
			sValueC [ i] = ""; //sC [ i];
		}	
		for ( i = 0; i < iOuts + 1; i ++) {
			Control c = ( Control) spAux.traverse ( i+iEntries);
			sC [ i+iEntries] = c.getName ( );
			sValueC [ i+iEntries] = ""; //sC [ i+iEntries];
		}
*/
		
		colorDraw = Color.WHITE;
		colorText = Color.BLACK;
		setBaseSize ( 24, 240);
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
		w = (int) (r.width); 
		h = (int) (r.height); 
		x = r.x; 
		y = r.y; 
		g.setColor ( colorDraw);
		
		int i;
		//System.out.println ( "iEntries = "+iEntries);
		float fDistance = ( float) 1 / (iEntries+1);
		//System.out.println ( "fDistance = "+fDistance);
		for ( i = 0; i < iEntries; i ++) {
			int mult=i+1;
			int yDes = y + (int)(h*0.25);
			int hDes = (int) (h*0.5);
			g.drawLine(x,yDes+(int)(hDes*fDistance*mult),x+(int)(w*0.25),yDes+(int)(hDes*fDistance*mult));
		}
		fDistance = ( float) 1 / (iOuts+1);
		for ( i = 0; i < iOuts; i ++) {
			int mult=i+1;
			int yDes = y + (int)(h*0.25);
			int hDes = (int) (h*0.5);
			g.drawLine(x+(int)(w*0.75),yDes+(int)(hDes*fDistance*mult),x+w,yDes+(int)(hDes*fDistance*mult));
		}
/*
		fDistance = ( float) 1 / (iEntries+1);
		for ( i = 0; i < iEntries; i ++) {
			int mult=i+1;
			int xDes = x + (int)(w*0.25);
			int wDes = (int) (w*0.5);
			g.drawLine(xDes+(int)(wDes*fDistance*mult),y,xDes+(int)(wDes*fDistance*mult),y+(int)(h*0.25));
		}
		fDistance = ( float) 1 / (iOuts+2);
		for ( i = 0; i < iOuts+1; i ++) {
			int mult=i+1;
			int xDes = x + (int)(w*0.25);
			int wDes = (int) (w*0.5);
			g.drawLine(xDes+(int)(wDes*fDistance*mult),y+(int)(h*0.75),xDes+(int)(wDes*fDistance*mult),y+h);
		}
*/
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
		
		g.drawString(sName, x+(int)(w*0.25), y+(int)(h*0.5));
		int i;
		float fDistance = ( float) 1 / (iEntries+1);
		for ( i = 0; i < iEntries; i ++) {
			int mult=i+1;
			int yDes = y + (int)(h*0.25);
			int hDes = (int) (h*0.5);
			g.drawString(sValueE[i],x,yDes+(int)(hDes*fDistance*mult));
//			g.drawString(sValueC[i],x+(int)(w*fDistance*mult),y);
		}		
		fDistance = ( float) 1 / (iOuts+2);
		float fDistanceNew = ( float) 1 / (iOuts+1);
		for ( i = 0; i < iOuts; i ++) {
			int mult=i+1;
			int yDes = y + (int)(h*0.25);
			int hDes = (int) (h*0.5);
			// if ( i < iOuts) 
			g.drawString(sValueS[i],x+(int)(w*0.75),yDes+(int)(hDes*fDistanceNew*mult));
			//g.drawString(sValueC[i+iEntries],x+(int)(w*fDistance*mult),y+h);
		}	
		g.setColor(getFrameColor());
	}
		
	protected void drawRegisterBankFigure(Graphics g) {
		drawFigure ( g);
		drawPorts ( g);
		drawStrings ( g);
	}

	public void draw(Graphics g) {
		drawRegisterBankFigure(g);
		drawConnectors(g);
	}

	public HandleEnumeration handles() {
		ConnectionFigure prototype = new ConnectionFigure();
		List handles = CollectionsFactory.current().createList();

		int i;
		float fDistance = ( float) 1 / (iEntries+1);
		for ( i = 0; i < iEntries; i ++) {
			int mult=i+1;
			handles.add(new ConnectionHandle(this, new RelativeLocator ( 0, ((fDistance*mult)*0.5)+0.25),prototype));
		}		
		fDistance = ( float) 1 / (iOuts+1);
		for ( i = 0; i < iOuts; i ++) {
			int mult=i+1;
			handles.add(new ConnectionHandle(this, new RelativeLocator ( 1, ((fDistance*mult)*0.5)+0.25),prototype));
		}	

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
		fConnectors = CollectionsFactory.current().createList(iEntries+iOuts);

		int i;
		float fDistance = ( float) 1 / (iEntries+1);
		for ( i = 0; i < iEntries; i ++) {
			int mult=i+1;
			fConnectors.add(new PortConnector(this, new RelativeLocator(0, ((fDistance*mult)*0.5)+0.25),sE[i]) );
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
		Port pAux;
		int iTypeValue = INTEGER;
		
		cCircuit = dtp.search ( sName);
		for ( i = 0; i < iEntries; i ++) {
			if ( ( pAux = cCircuit.getPort( sE[i], IN)) != null) {
				iTypeValue = pAux.getTypeValue();
				lTmp = dtp.execute ( sName, GET, sE[i], IN);
				sValueE[i] = SistNum.printInformation ( lTmp, iTypeValue);
			}
		}
		for ( i = 0; i < iOuts; i ++) {
			if ( ( pAux = cCircuit.getPort( sS[i], OUT)) != null) {
				iTypeValue = pAux.getTypeValue();
				lTmp = dtp.execute ( sName, GET, sS[i], OUT);
				sValueS[i] = SistNum.printInformation ( lTmp, iTypeValue);
			}
		}
/*
		for ( i = 0; i < iEntries+iOuts+1; i ++) {
			lTmp = dtp.execute ( sName, GET, sC[i], CONTROL);
			sValueC[i] = SistNum.printInformation ( lTmp, INTEGER);
		}
*/		
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
	
	String sName;
	Processor pProc;
	int iEntries;
	String [] sE, sValueE;
	int iOuts;
	String [] sS, sValueS;
//	String [] sC, sValueC;
	
	Color colorDraw, colorText;
}
