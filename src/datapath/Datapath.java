package datapath;

import ports.Port;
import util.Define;
import bus.Bus;
import bus.SetBus;
import ccomb.Circuit;
import ccomb.Combinational;
import ccomb.SetCircuit;
import cseq.Sequential;

public class Datapath implements Define {

	public Datapath ( ) {
		scEsquematico = new SetCircuit ( );
	}

	public void add ( Circuit parComponente) {
		scEsquematico.add ( parComponente);
	}

	public void remove ( String parComponente) {
		scEsquematico.remove ( parComponente);
	}

	public Circuit search ( String parComponente) {
		Circuit cComponente;
		
		cComponente = ( Circuit) scEsquematico.searchPrimitive ( parComponente);
		
		return ( cComponente);	
	}
/*
	public Circuit search ( String parComponente) {
		Circuit cComponente;
		StringBuffer sbTmp = new StringBuffer ( new String ( parComponente));
		
		cComponente = ( Circuit) scEsquematico.searchPrimitive ( sbTmp);
		
		return ( cComponente);	
	}
*/
	public void link ( 	String parCOrg, String parPOrg,
						String parCDest, String parPDest) {
		Circuit cAuxa, cAuxb;
		Bus bAux = null;
		StringBuffer sbAux = new 	StringBuffer ( ).append ( parCOrg).
									append ( "_").append ( parPOrg);
		String sAux = sbAux.toString ( );

		cAuxa = ( Circuit) this.search ( parCOrg);
		cAuxb = ( Circuit) this.search ( parCDest);
		
		if ( cAuxa != null) {
			bAux = cAuxa.getBus ( sAux);
		}
		if ( bAux != null) {
			if ( cAuxb != null) {
				Port pAux = cAuxb.getPort ( parPDest, IN);
				// Acrescenta a possibilidade de linkar com uma porta de controle
				if ( pAux == null) pAux = cAuxb.getPort ( parPDest, CONTROL);
				if ( pAux != null) bAux.link ( cAuxb, pAux);
			}
		}
	}

	public void deLink ( 	String parCOrg, String parPOrg,
							String parCDest, String parPDest) {
		Circuit cAuxa, cAuxb;
		Bus bAux = null;
		StringBuffer sbAux = new 	StringBuffer ( ).append ( parCOrg).
									append ( "_").append ( parPOrg).
									append ( ":").append ( parCDest).
									append ( "_").append ( parPDest);
		String sAux = sbAux.toString ( );

		cAuxa = ( Circuit) this.search ( parCOrg);
		
		if ( cAuxa != null) {
			bAux = cAuxa.getBus ( sAux);
		}
		if ( bAux != null) bAux.deLink ( );
	}

	private long execute_internal ( 	String parComponente, int parMethod, 
										String parName, long parV1, long parV2, 
										long parV3) {
		Circuit cComponente;
		Combinational cComb;
		Sequential sSeq;
		long lRet = - 1L;
		int iV1 = new Long ( parV1).intValue ( );
		int iV2 = new Long ( parV2).intValue ( );
		int iV3 = new Long ( parV3).intValue ( );
		
		cComponente = ( Circuit) scEsquematico.searchPrimitive ( parComponente);
		
		if ( cComponente != null) {
			switch ( parMethod) {
				case BEHAVIOR:
					cComponente.setMethod ( BEHAVIOR);
					cComponente.setUsed ( );
					cComb = ( Combinational) cComponente;
					cComb.behavior ( );
					break;
				case READ:
					cComponente.setMethod ( READ);
					cComponente.setUsed ( );
					sSeq = ( Sequential) cComponente;
					sSeq.read ( );
					break;
				case WRITE:
					cComponente.setMethod ( WRITE);
					cComponente.setUsed ( );
					sSeq = ( Sequential) cComponente;
					sSeq.write ( );
					break;
/*				case PROPAGATE:
					cComponente.setMethod ( PROPAGATE);
					cComponente.setUsed ( );
					sSeq = ( Sequential) cComponente;
					sSeq.propagate ( );
					break;*/
				case BUSBEHAVIOR:
					SetBus sbSetbus = cComponente.getSetBus ( );
					if ( sbSetbus != null) {
						Bus bBus = ( Bus) sbSetbus.searchPrimitive ( parName);
						if ( bBus != null) {
							if ( bBus.isLinked ( ) == true) bBus.behavior ( );
						}
					}
				case SET:
					cComponente.setMethod ( SET);
					cComponente.setUsed ( );
					if ( parName == null) {
						sSeq = ( Sequential) cComponente;
						if ( parV2 == 0) sSeq.cConteudo.set ( parV3, iV1);
						else sSeq.cConteudo.set ( parV3, iV1, iV2);
					} else cComponente.set ( parName, iV1, parV2);
					break;
				case GET:
					if ( parName == null) {
						sSeq = ( Sequential) cComponente;
						if ( parV2 == 0) lRet = sSeq.cConteudo.getDoubleWord ( iV1);
						else lRet = sSeq.cConteudo.getDoubleWord ( iV1, iV2);
					} else lRet = cComponente.get ( parName, iV1);
					break;
			}
		}
		
		return ( lRet);
	}

	// RETORNO SEM SIGNIFICADO
	public long execute ( 	String parComponente, int parMethod) {
//System.out.println ( parComponente);
//System.out.println ( parMethod);
		return(this.execute_internal ( parComponente, parMethod, null, 0L, 0L, 0L));
	}

	// RETORNO SEM SIGNIFICADO
	public long execute ( 	String parComponente, int parMethod,
							String parName, long parType, long parValue) {
		return(this.execute_internal(parComponente,parMethod,parName,parType,parValue,0L));
	}

	// RETORNO SEM SIGNIFICADO
	public long execute ( 	String parComponente, int parMethod,
							long parCx, long parCy, long parValue) {
		return(this.execute_internal(parComponente,parMethod,null,parCx,parCy,parValue));
	}

	// RETORNO SEM SIGNIFICADO
	public long execute ( 	String parComponente, int parMethod, String parName) {
		return(this.execute_internal(parComponente,parMethod,parName,0L,0L,0L));
	}

	public long execute ( 	String parComponente, int parMethod,
							String parName, long parType) {
		return(this.execute_internal(parComponente,parMethod,parName,parType,0L,0L));
	}

	public long execute ( 	String parComponente, int parMethod,
							long parCx, long parCy) {
		return(this.execute_internal(parComponente,parMethod,null,parCx,parCy,0L));
	}

	public void debug ( 	String parComponente, boolean bAll) {
		Circuit cComponente;
		SetBus sbAux;
		Bus bAux;
		int iNelem, jNelem, i, j;
		
       	if (parComponente.compareToIgnoreCase ( "datapath") == 0) {
       		iNelem = scEsquematico.getNelems ( );
       		for ( i = 0; i < iNelem; i ++) {
				cComponente = ( Circuit) scEsquematico.traverse ( i);
				if ( bAll) {
					System.out.println ( 	i+": "+cComponente.getName ( )+
											",\testado: "+cComponente.isUsed()+
											",\tcomportamento: "+
											cComponente.getMethodDescription());
				} else {
					if ( cComponente.isUsed ( )) {
						System.out.println ( 	i+": "+cComponente.getName ( )+
												",\testado: "+cComponente.isUsed()+
												",\tcomportamento: "+
												cComponente.getMethodDescription());			
					}
				}
			}
			if ( iNelem > 0) System.out.println ( );
		} else if (parComponente.compareToIgnoreCase ( "bus") == 0) {
       		iNelem = scEsquematico.getNelems ( );
       		for ( i = 0; i < iNelem; i ++) {
				cComponente = ( Circuit) scEsquematico.traverse ( i);
				sbAux = cComponente.getSetBus ( );
				if ( bAll) {
					for ( j = 0; j < sbAux.getNelems ( ); j ++) {
						bAux = ( Bus) sbAux.traverse ( j);
						bAux.debug ( );
					}			
				}
				else {
					for ( j = 0; j < sbAux.getNelems ( ); j ++) {
						bAux = ( Bus) sbAux.traverse ( j);
						if ( bAux.isUsed ( )) bAux.debug ( );
					}			
				}
			}
			if ( iNelem > 0) System.out.println ( );
		} else {
			cComponente = ( Circuit) scEsquematico.searchPrimitive ( parComponente);
		
			if ( cComponente != null) {		
				cComponente.debug ( );
			}
		}
	}

	public void list ( 	String parComponente, boolean bAll) {
		Circuit cComponente;
		SetBus sbAux;
		Bus bAux;
		int iNelem, jNelem, i, j;
		
       	if (parComponente.compareToIgnoreCase ( "datapath") == 0) {
       		iNelem = scEsquematico.getNelems ( );
       		for ( i = 0; i < iNelem; i ++) {
				cComponente = ( Circuit) scEsquematico.traverse ( i);
				if ( bAll) {
					System.out.println ( 	i+": "+cComponente.getName ( ));
				} else {
					if ( cComponente.isUsed ( )) {
						System.out.println ( 	i+": "+cComponente.getName ( ));
					}
				}
			}
			if ( iNelem > 0) System.out.println ( );
		} else if (parComponente.compareToIgnoreCase ( "bus") == 0) {
       		iNelem = scEsquematico.getNelems ( );
       		for ( i = 0; i < iNelem; i ++) {
				cComponente = ( Circuit) scEsquematico.traverse ( i);
				sbAux = cComponente.getSetBus ( );
				if ( bAll) {
					for ( j = 0; j < sbAux.getNelems ( ); j ++) {
						bAux = ( Bus) sbAux.traverse ( j);
						bAux.list ( );
					}			
				}
				else {
					for ( j = 0; j < sbAux.getNelems ( ); j ++) {
						bAux = ( Bus) sbAux.traverse ( j);
						if ( bAux.isUsed ( )) bAux.list ( );
					}			
				}
			}
			if ( iNelem > 0) System.out.println ( );
		} else {
			cComponente = ( Circuit) scEsquematico.searchPrimitive ( parComponente);
		
			if ( cComponente != null) {		
				cComponente.list ( );
			}
		}
	}

	public Bus searchBusByDestination ( String sDestino) {
		Circuit cComponente;
		SetBus sbBus;
		Bus bBs;
		int iNelem, i, j;
		
   		iNelem = scEsquematico.getNelems ( );
   		for ( i = 0; i < iNelem; i ++) {
			cComponente = ( Circuit) scEsquematico.traverse ( i);
			sbBus = cComponente.getSetBus ( );
			for ( j = 0; j < sbBus.getNelems ( ); j ++) {
				bBs = ( Bus) sbBus.traverse ( j);
				if ( bBs.searchBusByDestination ( sDestino) != null) return ( bBs);;
			}			
		}
		
		return ( null);
	}

	public void resetUsedCircuit ( ) {
		Circuit cComponente;
		int iNelem, i;
		
   		iNelem = scEsquematico.getNelems ( );
   		for ( i = 0; i < iNelem; i ++) {
			cComponente = ( Circuit) scEsquematico.traverse ( i);
			if ( cComponente.isUsed ( )) cComponente.resetUsed ( );
		}
	}

	public void resetUsedBus ( ) {
		Circuit cComponente;
		SetBus sbBus;
		Bus bBs;
		int iNelem, i, j;
		
   		iNelem = scEsquematico.getNelems ( );
   		for ( i = 0; i < iNelem; i ++) {
			cComponente = ( Circuit) scEsquematico.traverse ( i);
			sbBus = cComponente.getSetBus ( );
			for ( j = 0; j < sbBus.getNelems ( ); j ++) {
				bBs = ( Bus) sbBus.traverse ( j);
				if ( bBs.isUsed ( )) bBs.resetUsed ( );
			}			
		}
	}

	public void resetContents ( ) {
		Circuit cComponente;
		int iNelem, i;
		Sequential sAux;
//System.out.println ( "resetContents");		
   		iNelem = scEsquematico.getNelems ( );
   		for ( i = 0; i < iNelem; i ++) {
			cComponente = ( Circuit) scEsquematico.traverse ( i);
			try {
				sAux = (Sequential) cComponente;
				sAux.cConteudo.resetContents();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	public SetCircuit scEsquematico;
}
