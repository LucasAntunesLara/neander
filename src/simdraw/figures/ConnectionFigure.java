/*
 * Created on 27/04/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package simdraw.figures;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import org.jhotdraw.figures.LineConnection;
import org.jhotdraw.figures.PolyLineFigure;
import org.jhotdraw.framework.Connector;
import org.jhotdraw.framework.FigureAttributeConstant;

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
public class ConnectionFigure extends LineConnection implements LinkSimulatorVisualization, Define {
	
	public ConnectionFigure ( ) {
		sContent = new String ();
		setAttribute( FigureAttributeConstant.ARROW_MODE, new Integer(PolyLineFigure.ARROW_TIP_END));
		colorDraw = Color.WHITE;
		colorText = Color.BLACK;
	}

	protected void drawLine ( Graphics g, int x1, int y1, int x2, int y2) {
		g.setColor ( colorDraw);
		g.drawLine( x1, y1, x2, y2);
		g.setColor(getFrameColor());		
	}

	public void draw(Graphics g) {
		Rectangle r = displayBox();
		Point l, p;
		int i;
		super.draw(g);
		g.setColor ( colorText);
		Font fb = new Font("Helvetica", Font.BOLD, 10);
		g.setFont(fb);
//		i = this.pointCount() - 1;
//		l = this.pointAt( i);
//		p = this.pointAt(i-1);
//		g.drawString(sContent, r.x+(r.width/2), r.y+(r.height/2));
		g.setColor(getFrameColor());
	}
	
	public void setPortName ( String s, String p) {
		sName = s;
		sPort = p;
	}
	
	/* (non-Javadoc)
	 * @see simdrawNew.RefreshDisplay#refreshDisplay(datapath.Datapath)
	 */
	public void refreshDisplay ( Simulator parSim, Processor parProc) {
		Circuit cCircuit;
		Datapath dtp = parProc.getDatapath ( );
		long lTmp = dtp.execute ( sName, GET, sPort, OUT);
		//sContent = new Long ( lTmp).toString();
		sContent = SistNum.printInformation ( lTmp, INTEGER);
		cCircuit = dtp.search ( sName);
		if ( cCircuit != null) {
			if ( cCircuit.isUsed ( ) == true) {
				if ( cCircuit.getMethod() == WRITE) colorDraw = colorINACTIVE; // Nao propaga
				else if ( cCircuit.getMethod() == READ) colorDraw = colorREAD;
				else if ( cCircuit.getMethod() == BEHAVIOR) colorDraw = colorBEHAVIOR;
			}
			else colorDraw = colorINACTIVE;
		} else colorDraw = colorINACTIVE;
		invalidate ( );
		/*if ( sName.compareToIgnoreCase("id_ex") == 0) {
			for ( int i = 0; i < this.pointCount ( ); i ++) {
				System.out.println ( i + "->" + this.pointAt( i));
			}
		}*/
	}
	
	/* (non-Javadoc)
	 * @see simdrawNew.LinkSimulatorVisualization#connectorAt(java.lang.String)
	 */
	public Connector connectorAt(String parName) {
		// TODO Auto-generated method stub
		return null;
	}
	String sContent, sName, sPort;
	Color colorDraw, colorText;
}
