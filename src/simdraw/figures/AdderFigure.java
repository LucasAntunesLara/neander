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

/**
 * @author unknown
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AdderFigure extends MultiplexerV2Figure {


	/**
	 * @param parName
	 * @param parE1
	 * @param parE2
	 * @param parS1
	 * @param parSEL
	 */
	public AdderFigure(
		String parName,
		String parE1,
		String parE2,
		String parS1) {
		super(parName, parE1, parE2, parS1, null);
		// TODO Auto-generated constructor stub
	}

	protected void drawPorts (Graphics g) {
		Rectangle r = displayBox();
		Polygon p = getPolygon();
		int x, y, w, h;
		x = r.x; y = r.y; w = r.width; h = r.height;
		g.setColor ( colorDraw);	
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
		g.drawString(sValueE1, x, y+h);
		g.drawString(sValueE2, x+(int)(w*0.5), y+h);
		g.drawString(sName, x+(int)(w*0.25), y+(int)(h*0.75));
		g.drawString(sValueS1,x+(int)(w*0.55),y+(int)(h*0.25));
		g.setColor(getFrameColor());
	}
		
	protected void drawAdder(Graphics g) {
		drawFigure ( g);
		drawPorts ( g);
		drawStrings ( g);
	}

	public void draw(Graphics g) {
//		super.draw(g);
		drawAdder (g);
		drawConnectors(g);
	}
}
