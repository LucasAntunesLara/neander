/*
 * Created on 04/09/2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cseq;

import bus.Bus;
import bus.SetBus;
import contents.Contents;
import ports.InControl;
import ports.OutControl;
import ports.SetPort;
import ports.Status;



public class Cache extends Sequential {

	public Cache(int parNwords, String parName, String parE1name,
				 int parSizeData, String parE2name, int parSizeEnd,
				 String parS1name, String parHitMissName,int parLatency, int parMapType, int parLinesBySet) {

/*****************/
		sbName = new StringBuffer ( ).append (parName);

		spIn = new SetPort ( );
		spOut = new SetPort ( );
		sbBus = new SetBus ( );
		spStt = new SetPort ( );

		peE1 = new InControl ( parE1name, parSizeData);
		spIn.add ( peE1);
		peE2 = new InControl ( parE2name, parSizeEnd); //parSizeEnd
		spIn.add ( peE2);

		psS1 = new OutControl ( parS1name, parSizeData);
		spOut.add ( psS1);
		bBs1 = new Bus ( this, psS1, null, null);
		sbBus.add ( bBs1);

		stATR2 = new Status ( "WRITEBACK");
		stATR2.set ( 0);
		spStt.add ( stATR2);

		stHIT = new Status ( "HIT");
		stHIT.set ( 0);
		spStt.add ( stHIT);

		stNHit = new Status ( "NHits");
		stNHit.set ( 0);
		spStt.add ( stNHit);

		stNMiss = new Status ( "NMisses");
		stNMiss.set ( 0);
		spStt.add ( stNMiss);
		
		MapType = parMapType;
		stMapping = new Status ( "TDCF_Mapping");
		stMapping.set ( parMapType);		// direto
		spStt.add ( stMapping);
		
		stLinesBySet = new Status ( "TDCF_LinesBySet");
		stLinesBySet.set ( parLinesBySet);
		spStt.add ( stLinesBySet);

		this.cConteudo = new Contents (parNwords, 3, parSizeData, INTEGER);  //por enquanto soh tem rotulo, dado e flag para atualizar
		Nwords = parNwords;
		iLinesBySet = parLinesBySet;
//System.out.println ( "MapType = "+MapType);
//System.out.println ( "Cache size = "+Nwords);
//System.out.println ( "Set size = "+iLinesBySet);
/*		int i;
		for ( i = 0; i < parNwords; i ++) {
//System.out.println ( "i = "+i);
			this.cConteudo.set(-1, i, COL_ROTULO);
			this.cConteudo.set(0, i, COL_DADO);
			this.cConteudo.set(0, i, COL_ATUALIZA);
	}*/
	}
	
	private void incReadAccess ( int iLinha) {
		int nTimes;
		
		nTimes = this.cConteudo.getWord(iLinha, COL_CONTACCESS);
		this.cConteudo.set(++nTimes, iLinha, COL_CONTACCESS);
		//System.out.println ( nTimes);
	}
	
	public void read ( ) {
		if (bFirstRead) {
			this.read1st ();
			bFirstRead = false;
			Status stTmp;
			stTmp = (Status) spStt.searchPrimitive("TDCF_Mapping");
			MapType = stTmp.getWord ( );
			stTmp = (Status) spStt.searchPrimitive("TDCF_LinesBySet");
			iLinesBySet = stTmp.getWord ( );
System.out.println ( "Configuration: "+MapType+" , "+iLinesBySet);
		} else {
			this.readcache ();
			bFirstRead = false;
		}
	}

	protected long read1st () {
		stHIT.set (0);
		MissCount++;
		System.out.println ("PRIMEIRO MISS: "+MissCount);
		
		return 0; //retorno sem significado
	}

	protected void readcache () {
		long lS1t = 0;
		int i, iE2t, iLinha = -1, iRotulo = -1, iAtualiza, iSet = 0, iSetBegin, iSetEnd;

		stHIT.set ( 0);
		
		iE2t = peE2.getWord ();

//System.out.println ( "Endereço de acesso de leitura = "+iE2t);

		if (MapType == DIRETO) {
			iLinha = iE2t % Nwords;
		} else if (MapType == ASSOCIATIVO) {
			for (i=0; i < Nwords; i++) {
				if (this.cConteudo.existIndex(i,COL_ROTULO) && this.cConteudo.getWord(i, COL_ROTULO) == iE2t) {
					iLinha = i;
					break;
				}
			}
		} else if (MapType == SET_ASSOCIATIVO) {
			//iLinha = iE2t % Nwords;
			System.out.println ( "Endereço = "+iE2t);
			iSet = (iE2t % Nwords) / iLinesBySet;
			System.out.println ( "Linha = "+iLinha);
			System.out.println ( "Set = "+iSet);
			iSetBegin = iSet*iLinesBySet;
			iSetEnd = iSet*iLinesBySet+iLinesBySet;
			System.out.println ( "Linha inicial do set = "+iSetBegin);
			System.out.println ( "Linha final do set = "+iSetEnd);
			for (i=iSetBegin; i < iSetEnd; i++) {
				if (this.cConteudo.existIndex(i,COL_ROTULO) && this.cConteudo.getWord(i, COL_ROTULO) == iE2t) {
					iLinha = i;
					break;
				}
			}
		}
		if ( iLinha != - 1) {
			if ( this.cConteudo.existIndex(iLinha,COL_ROTULO)) {
				iRotulo = this.cConteudo.getWord(iLinha, COL_ROTULO);				
			} else iRotulo = - 1;
		}
		if (iRotulo == iE2t) {
			lS1t = this.cConteudo.getDoubleWord (iLinha, COL_DADO);
			psS1.set (lS1t);
			stHIT.set (1);
			if ( iE2t != iMissAddress) HitCount++;
			stNHit.set ( HitCount);
			incReadAccess ( iLinha);
			//System.out.println ("Hit: "+HitCount);
			iMissAddress = - 1;
		} else {
			stHIT.set (0);
			MissCount++;
			iMissAddress = iE2t;
			stNMiss.set ( MissCount);
			//System.out.println ("Miss normal: "+MissCount);
		}
		
		if ( bBs1.isLinked ( ) == true) bBs1.behavior ( );
	}


	public void write ( ) {
		if (bFirstWrite) {
			bFirstWrite = false;
			Status stTmp;
			stTmp = (Status) spStt.searchPrimitive("TDCF_Mapping");
			MapType = stTmp.getWord ( );
			stTmp = (Status) spStt.searchPrimitive("TDCF_LinesBySet");
			iLinesBySet = stTmp.getWord ( );
System.out.println ( "Configuration: "+MapType+" , "+iLinesBySet);
		}
		System.out.println ("CACHE escrevendo...");
		stATR2.set ( 1);  // setando WRITEBACK
		this.write1st();
	}


	public void write1st () {
		long lE1t;
		int iE2t, iLinha = -1, i; //iLinha vai receber a linha mapeada.
		int iAtualiza = 0, iSet = 0, iSetBegin, iSetEnd;
		
		lE1t = peE1.getDoubleWord ();
		iE2t = peE2.getWord ();
		if ( MapType == DIRETO) {
			iLinha = iE2t % Nwords;
			if ( this.cConteudo.existIndex(iLinha,COL_ROTULO) && this.cConteudo.getWord(iLinha, COL_ROTULO) == iE2t) {
				iAtualiza = 1;
			} else {
				iAtualiza = 0;
			}
		} else if ( MapType == ASSOCIATIVO) {
			for (i=0; i < Nwords; i++) {
				if (this.cConteudo.existIndex(i,COL_ROTULO) && this.cConteudo.getWord(i, COL_ROTULO) == iE2t) {
					iLinha = i;
					break;
				}
			}
			if ( iLinha == - 1) {
				int iMenor, iPosicao;
				iAtualiza = 0;
				iMenor = 1000;
				iPosicao = 0;
				for (i=0; i < Nwords; i++) {
					if (this.cConteudo.existIndex(i,COL_CONTACCESS)&&this.cConteudo.getWord(i,COL_CONTACCESS) < iMenor) {
						iMenor = this.cConteudo.getWord(i, COL_CONTACCESS);
						iPosicao = i;
					}
				}
				iLinha = iPosicao;
			} else {
				iAtualiza = 1;
			}
		} else if ( MapType == SET_ASSOCIATIVO) {
			//iLinha = iE2t % Nwords;
			iSet = (iE2t % Nwords) / iLinesBySet;
			System.out.println ( "Set = "+iSet);
			iSetBegin = iSet*iLinesBySet;
			iSetEnd = iSet*iLinesBySet+iLinesBySet;
			System.out.println ( "Linha inicial do set = "+iSetBegin);
			System.out.println ( "Linha final do set = "+iSetEnd);
			for (i=iSetBegin; i < iSetEnd; i++) {
				if (this.cConteudo.existIndex(i,COL_ROTULO) && this.cConteudo.getWord(i, COL_ROTULO) == iE2t) {
					iLinha = i;
					break;
				}
			}
			if ( iLinha == - 1) {
				int iMenor, iPosicao;
				iAtualiza = 0;
				iMenor = 1000;
				iPosicao = 0;
				for (i=iSetBegin; i < iSetEnd; i++) {
					if (this.cConteudo.existIndex(i,COL_CONTACCESS) && this.cConteudo.getWord(i, COL_CONTACCESS) < iMenor) {
						iMenor = this.cConteudo.getWord(i, COL_CONTACCESS);
						iPosicao = i;
					}
				}
				iLinha = iPosicao;
			} else {
				iAtualiza = 1;
			}
		}

		this.cConteudo.set(iE2t, iLinha, COL_ROTULO);  // grava rotulo, ou seja, o endereco completo passado pelo processador.
		this.cConteudo.set(lE1t, iLinha, COL_DADO);
		this.cConteudo.set(iAtualiza, iLinha, COL_ATUALIZA); //Quando setado para 1, devera atualizar na mem principal (tarefa para qdo a integração CACHE X MEM P estiver pronta)
		if ( iAtualiza == 0) {
			this.cConteudo.set(0, iLinha, COL_CONTACCESS);
		}
	}
	
/*	private int mapLine () {
		int iE2t, iLinha = 0; //iLinha vai receber a linha mapeada.
		
		iE2t = peE2.getWord ();
		
		if (MapType == DIRETO) iLinha = iE2t % Nwords;
		if (MapType == ASSOCIATIVO) {
			iLinha = (int) ((double) Nwords * Math.random()); //por enquanto o algoritmo de substituicao e randomico.
		}
		if (MapType == 0) System.out.println ("Nao ha mapeamento definido");
		
		//System.out.println ("Tipo de Mapeamento: "+MapType);
		System.out.println ("Linha Mapeada: " +iLinha);
		
		return iLinha;
	}
*/
	
	/*************************************************************************************/

	public void debug ( ) {
		System.out.println ( "*** Memory.debug:...BEGIN");

		if ( spIn != null) spIn.debug ( );
		if ( spCtrl != null) spCtrl.debug ( );
		if ( spOut != null) spOut.debug ( );
		if ( spStt != null) spStt.debug ( );
		if ( sbBus != null) sbBus.debug ( );
		if ( this.cConteudo != null) this.cConteudo.debug ( );

		System.out.println ( "*** Memory.debug:...END");
		System.out.println ( );
	}

	public void list ( ) {
		System.out.println ( "\n** INICIO **\n");

		System.out.println ( "Componente: " + sbName + "\n");

		if ( spIn != null) spIn.list ( );
		if ( spCtrl != null) spCtrl.list ( );
		if ( spOut != null) spOut.list ( );
		if ( spStt != null) spStt.list ( );
		if ( sbBus != null) sbBus.list ( );
		if ( this.cConteudo != null) this.cConteudo.list ( );
		
		System.out.println ( "\n** FIM **");
		System.out.println ( );
	}

	public static void help ( ) {
		System.out.println ( "Memória Cache: ...");
		System.out.println ( "É uma memoria que aumenta o desempenho da memoria principal, por ser mais veloz");
		System.out.println ( "Possui as entradas E1 e E2, para o dado e o endereco, respectivamente.");
		System.out.println ( "Possui uma saida S1, que armazena o dado lido, e S2, que armazena a condicao de HIT ou MISS durante uma leitura.");
		System.out.println ( "\nPara cria-la: create <nome> 100 <npos> <bitsData> <bitsEnd> <tipoMapeamento> <linhasPorConjunto>\n npos - nro. de posicoes de memoria");
		System.out.println ( "tipoMapeamento - 1 para DIRETO e 2 para ASSOCIATIVO");
		System.out.println ( );
	}

	public static void test ( ) {
		Cache mem = new Cache ( 10, "tst", "e1", WORD, "e2", BYTE, "s1", "flag", 0, 0, 0);
		
		mem.set ( "e1", IN, 1);
		mem.set ( "e2", IN, 5);
		mem.write ( );
		mem.read ( );
		mem.debug ( );
		
		System.out.println ("Randomico para nro DEZ " +(int) ((double) 10 * Math.random()));
		
	}
	
	protected InControl peE1, peE2;
	protected OutControl psS1;
	protected Bus bBs1;
	protected Status stATR2;
	
	//protected OutControl psS2;
	protected Status stHIT, stNHit, stNMiss, stMapping, stLinesBySet;
	protected int Nwords, iLinesBySet;
	//protected Contents cConteudo;
	protected int MapType = 0;
	protected int MissCount = 0, HitCount = 0;
	protected boolean bFirstRead = true, bFirstWrite = true;
	
	protected int iMissAddress;
		
	public final int COL_ROTULO     = 1;
	public final int COL_DADO       = 0;
	public final int COL_ATUALIZA	= 2;
	public final int COL_CONTACCESS	= 3;
	public final int DIRETO			= 1;
	public final int ASSOCIATIVO	= 2;
	public final int SET_ASSOCIATIVO= 3;
}
