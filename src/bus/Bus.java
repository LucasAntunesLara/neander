package bus;

import ports.Port;
import primitive.Primitive;
import util.SistNum;
import ccomb.Circuit;
import ccomb.Combinational;

public class Bus extends Primitive {

	public Bus ( Circuit parCOrg, Port parPOrg, Circuit parCDest, Port parPDest) {
		cOrigem = parCOrg;
		pOrigem = parPOrg;
		cDestino = parCDest;
		pDestino = parPDest;
		int iAux = 0;
		
		sbName = new StringBuffer ( ).append (cOrigem.getName ( ));
		sbName.append ( "_").append ( pOrigem.getName ( ));
		iSize = pOrigem.getSize ( );
		if ( cDestino != null) {
			sbName.append ( ":").append ( cDestino.getName ( ));
			sbName.append ( "_").append ( pDestino.getName ( ));
			iAux = pDestino.getSize ( );
			if ( iAux > iSize) iSize = iAux;
		}
	}
		
	public void link ( Circuit parCDest, Port parPDest) {
		cDestino = parCDest;
		pDestino = parPDest;
		int iAux = 0;

		if ( cDestino != null) {
			sbName.append ( ":").append ( cDestino.getName ( ));
			sbName.append ( "_").append ( pDestino.getName ( ));
			iAux = pDestino.getSize ( );
			if ( iAux > iSize) iSize = iAux;
		}
	}

	public void deLink ( ) {
		cDestino = null;
		pDestino = null;

		iSize = pOrigem.getSize ( );

		sbName = new StringBuffer ( ).append (cOrigem.getName ( ));
		sbName.append ( "_").append ( pOrigem.getName ( ));
	}

	public boolean isLinked ( ) {
		if ( cDestino == null) return ( false);
		else return ( true);
	}

	public boolean isUsed ( ) {
		return ( bUsed);
	}

	public void resetUsed ( ) {
		bUsed = false;
	}

	public Bus searchBusByDestination ( String sDestino) {
		String sAux = sbName.toString ( ), sAux2;
		int i = sAux.lastIndexOf ( ':');
		sAux2 = sAux.substring ( i+1);

		if ( sAux2.compareToIgnoreCase ( sDestino) == 0) {
			return ( this);
		}
		else return ( null);
	}

	public void behavior ( ) {
		long lAux;
		
		lAux = cOrigem.get ( pOrigem.getName ( ), OUT);
		if ( cDestino != null) {
			if ( cDestino.getPort ( pDestino.getName ( ), IN) != null) 
				cDestino.set ( pDestino.getName ( ), IN, lAux);
			// Preve conexao tambem com portas de controle
			else cDestino.set ( pDestino.getName ( ), CONTROL, lAux);
			lValue = lAux;
			bUsed = true;
			if ( cDestino.getClass ( ).getName ( ).equals ( "ccomb.Duplexer")) {
				Combinational cAux;
				cAux = (Combinational) cDestino;
				cAux.behavior ( );				
			}
			if ( cDestino.getClass ( ).getName ( ).equals ( "ccomb.Triplexer")) {
				Combinational cAux;
				cAux = (Combinational) cDestino;
				cAux.behavior ( );	
			}
			if ( cDestino.getClass ( ).getName ( ).equals ( "ccomb.CircuitBus")) {
				Combinational cAux;
				cAux = (Combinational) cDestino;
				cAux.behavior ( );	
				cAux.setMethod ( BEHAVIOR);
				cAux.setUsed ( );
			}			
		}
	}

	public void debug ( ) {
//		System.out.println ( "** bus.debug:...BEGIN");
		System.out.println ( 	"* bus.debug: name: "+sbName+", \tvalue: "+
								SistNum.toHexString ( lValue, iSize)+", \tativo: "+bUsed);
//		System.out.println ( "** bus.debug:...END");
//		System.out.println ( );
	}

	public void list ( ) {
		String sTmp;
		int iTmp;
		
		sTmp = sbName + " = " + SistNum.toHexString ( lValue, iSize);
		iTmp = sTmp.length ( );
		
		System.out.print ( sTmp);

		for ( int i = 0; i < COLUNAS - iTmp; i ++) System.out.print ( " ");
		System.out.println ( "Conexao");
	}

	private Circuit cOrigem, cDestino;
	private Port pOrigem, pDestino;
	private long lValue;
	private int iSize;
	private boolean bUsed = false;
}
