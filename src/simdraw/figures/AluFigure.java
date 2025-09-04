/*
 * Created on 02/05/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package simdraw.figures;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.List;

import org.jhotdraw.framework.HandleEnumeration;
import org.jhotdraw.standard.ConnectionHandle;
import org.jhotdraw.standard.HandleEnumerator;
import org.jhotdraw.standard.RelativeLocator;
import org.jhotdraw.util.CollectionsFactory;


/**
 * @author unknown
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AluFigure extends MultiplexerV2Figure {

	/**
	 * 
	 */
	public AluFigure() {
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
	public AluFigure(
		String parName,
		String parE1,
		String parE2,
		String parS1,
		String parOP) {
		super(parName, parE1, parE2, parS1, parOP);
		setBaseSize ( 54, 36);
		// TODO Auto-generated constructor stub
	}

	public Rectangle displayBox() {
		Rectangle box = super.displayBox();
		//int d = BORDER;
		//System.out.println ( "-> "+ getSizeX ( ));
		//System.out.println ( "-> "+ getSizeY ( ));
		box.grow(getSizeX(), getSizeY ( ));
		return box;
	}

	protected Polygon getPolygon() {
		Rectangle r = displayBox();
		int x, y, w, h;
		x = r.x; y = r.y; w = r.width; h = r.height;		
		Polygon p = new Polygon();

		p.addPoint(x,y+(int)(h*0.25));
		p.addPoint(x+w,y+(int)(h*0.25));
		p.addPoint(x+(int)(w*0.65),y+(int)(h*0.75));
		p.addPoint(x+(int)(w*0.35),y+(int)(h*0.75));
		p.addPoint(x,y+(int)(h*0.25));

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
		g.drawLine(x,y+(int)(h*0.5),x+(int)(w*0.166),y+(int)(h*0.5));
		g.drawLine(x+(int)(w*0.25),y,x+(int)(w*0.25),y+(int)(h*0.25));
		g.drawLine(x+(int)(w*0.75),y,x+(int)(w*0.75),y+(int)(h*0.25));
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
		g.drawString(sValueE1, x+(int)(w*0.28), y+(int)(h*0.25));
		g.drawString(sValueE2, x+(int)(w*0.78), y+(int)(h*0.25));
		g.drawString(sName, x+(int)(w*0.25), y+(int)(h*0.5));
		g.drawString(sValueS1,x+(int)(w*0.55),y+h);
		g.drawString(sValueSEL, x, y+(int)(h*0.50));
		g.setColor(getFrameColor());
	}
		
	protected void drawAluFigure(Graphics g) {
		drawFigure ( g);
		drawPorts ( g);
		drawStrings ( g);
	}

	public void draw(Graphics g) {
//		super.draw(g);
		drawAluFigure(g);
		drawConnectors(g);
	}

	public HandleEnumeration handles() {
		ConnectionFigure prototype = new ConnectionFigure();
		List handles = CollectionsFactory.current().createList();
		handles.add(new ConnectionHandle(this, RelativeLocator.south(), prototype));
//		handles.add(new ConnectionHandle(this, RelativeLocator.west(), prototype));
//		handles.add(new ConnectionHandle(this, RelativeLocator.south(), prototype));
//		handles.add(new ConnectionHandle(this, RelativeLocator.north(), prototype));

//		handles.add(new NullHandle(this, RelativeLocator.southEast()));
		handles.add(new ConnectionHandle(this, new RelativeLocator ( 0.25, 0),prototype));//RelativeLocator.southWest()));
//		handles.add(new NullHandle(this, RelativeLocator.northEast()));
		handles.add(new ConnectionHandle(this, new RelativeLocator ( 0.75, 0),prototype));
		return new HandleEnumerator(handles);
	}
	
	protected void createConnectors() {
		fConnectors = CollectionsFactory.current().createList(3);
		fConnectors.add(new PortConnector(this, RelativeLocator.south(),sS1) );
		fConnectors.add(new PortConnector(this, new RelativeLocator(0.25,0),sE1) );
		fConnectors.add(new PortConnector(this, new RelativeLocator(0.75,0),sE2) );
	}
}
