/*
 * Created on 19/08/2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package simdraw.figures;

import org.jhotdraw.figures.RectangleFigure;

/**
 * @author Sandro
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TDSimFigure extends RectangleFigure {
	public void setBaseSize ( int sX, int sY) {
		iSizeX = sX;
		iSizeY = sY;
	}
	
	public int getSizeX ( ) {
		return iSizeX;
	}

	public int getSizeY ( ) {
		return iSizeY;
	}
	
	protected int iSizeX, iSizeY;
}
