/*
 * Created on 01/05/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package simdraw.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.HeadlessException;
import java.util.Arrays;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import org.jhotdraw.framework.Connector;

import platform.Lang;
import processor.Processor;
import simdraw.LinkSimulatorVisualization;
import simulator.Simulator;
import util.Define;
import util.SistNum;
import contents.Contents;
import cseq.Sequential;
import datapath.Datapath;
import montador.Montador;
import montador.MontadorMIPS;

/**
 * @author unknown
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class MemoryPanel extends JPanel implements LinkSimulatorVisualization, Define {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @throws java.awt.HeadlessException
	 */
	public MemoryPanel(Simulator parSim, Processor parProc, String parComp, String parEnd) throws HeadlessException {
		super();
		sSim = parSim;
		pProc = parProc;
		sComp = parComp;
		sEndereco = parEnd;

		tableModel = new MyTableModel ( );
		table = new JTable ( tableModel);
		table.setAutoResizeMode ( JTable.AUTO_RESIZE_ALL_COLUMNS);
		scroll = new JScrollPane ( table);
		
		setLayout ( new BorderLayout ( ));
		String sTmp = Lang.iLang==ENGLISH?Lang.msgsGUI[90]:Lang.msgsGUI[95];
		JLabel jTitle = new JLabel ( parComp + sTmp);
		JPanel pTitle = new JPanel ( );
		pTitle.setBackground ( Color.LIGHT_GRAY);
		pTitle.add ( jTitle);
		add ( pTitle, BorderLayout.NORTH);
		add ( scroll, BorderLayout.CENTER);
	}

	//
	// TABLE MODEL BEGIN
	//
	class MyTableModel extends AbstractTableModel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String[] columnNames = {"", "", ""};
		MyTableModel ( ) {
			
			columnNames [0]= Lang.iLang==ENGLISH?Lang.msgsGUI[91]:Lang.msgsGUI[96];
			columnNames [1]= Lang.iLang==ENGLISH?Lang.msgsGUI[92]:Lang.msgsGUI[97];
			columnNames [2]= Lang.iLang==ENGLISH?Lang.msgsGUI[93]:Lang.msgsGUI[98];

			Sequential cSeq = ( Sequential) pProc.getDatapath().search ( sComp);
			ctData = cSeq.cConteudo;
			iTypeValue = cSeq.cConteudo.getTypeValue();
			if ( ctData.getSizeY ( ) == 0) htData = ctData.getContents1d();
			else htData = ctData.getContents2d();
			AKeys = htData.keySet().toArray();
			Arrays.sort ( AKeys);
			iSizeAKeys = AKeys.length;
		}

		public int getColumnCount() {
			return columnNames.length;
		}

		public int getRowCount() {
			return htData.size ( );
		}

		public String getColumnName(int col) {
			return columnNames[col];
		}

		public Object getValueAt(int row, int col) {
			Object oAux = null;
			String sAux = null;
			long lValue;
			Long LTmp;
			int iTmp;

			if ( iSizeAKeys != htData.size ( )) {
				AKeys = htData.keySet().toArray();
				Arrays.sort ( AKeys);
				iSizeAKeys = AKeys.length;		
				fireTableStructureChanged();
			}

			LTmp = (Long) AKeys [row];
			iTmp = LTmp.intValue ( );
			if ( col == 0) {
				sAux = LTmp.toString ( );
			}
			else if ( col == 2){
				if ( ctData.getSizeY ( ) == 0) lValue = ctData.getIntegerValue ( iTmp);
				else lValue = ctData.getIntegerValue2D ( iTmp, 1);
				sAux = Montador.getAssemblyCode ( row);
				if ( sAux == null) sAux = pProc.getMnemonico( lValue);
			} else {
				if ( ctData.getSizeY ( ) == 0) lValue = ctData.getIntegerValue ( iTmp);
				else lValue = ctData.getIntegerValue2D ( iTmp, 0);
				sAux = SistNum.printInformation ( lValue, iTypeValue);
			}
			
			oAux = ( Object) sAux;
			return oAux;

		}

		public Class getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}

		public boolean isCellEditable(int row, int col) {
				return false;
		}

		public void setValueAt(Object value, int row, int col) {
			if (false) {
				System.out.println("Setting value at " + row + "," + col
								   + " to " + value
								   + " (an instance of "
								   + value.getClass() + ")");
			}

			fireTableCellUpdated(row, col);

			if (false) {
				System.out.println("New value of data:");
				printDebugData();
			}

		}

		private void printDebugData() {

		}
		//
		// TABLE MODEL END
		//
	}

	/* (non-Javadoc)
	 * @see simdraw.LinkSimulatorVisualization#refreshDisplay(simulator.Simulador, processor.Processor, datapath.Datapath)
	 */
	public void refreshDisplay(Simulator parSim, Processor parProc) {
		Datapath dtp = parProc.getDatapath ( );
		String sAux = null;
		long lValue;
		Object [] AKeysChanged, AKeysInserted;	
		Object oTmp = null;
		Long LTmp;
		int i, iTmp;

		AKeysInserted = ctData.getInsertedKeys().keySet().toArray();
		if ( AKeysInserted.length > 0) {
			tableModel.fireTableStructureChanged();
		}
		else {
			AKeysChanged = ctData.getChangedKeys().keySet().toArray();	
			Arrays.sort ( AKeysChanged);
			for ( i = 0; i < AKeysChanged.length; i ++) {
				LTmp = (Long) AKeysChanged [i];
				iTmp = LTmp.intValue ( );
				if ( ctData.getSizeY ( ) == 0) lValue = ctData.getIntegerValue ( iTmp);
				else lValue = ctData.getIntegerValue2D ( iTmp, 0);
				oTmp = pProc.getMnemonico ( lValue);
				sAux = SistNum.printInformation ( lValue, iTypeValue);
				tableModel.setValueAt( sAux, i, 1);
				tableModel.setValueAt( oTmp, i, 2);
			}				
		}
		
		dtp.resetContents();

		if ( sEndereco != null) {
			iTmp = (int) dtp.execute ( sEndereco, GET, "S1", OUT);
			iTmp = getVisualizationLine ( iTmp);
//System.out.println ( "****** iTmp = "+iTmp);
//System.out.println ( "****** sizeX = "+ctData.getSizeX());
			if ( ! htData.isEmpty ( ) && iTmp < ctData.getSizeX ( )) table.setRowSelectionInterval( iTmp, iTmp);
		}
	}

	/**
	 * @param tmp
	 * @return
	 */
	private int getVisualizationLine(int iAddress) {
		int i;
		
		for ( i = 0; i < AKeys.length; i ++) {
			if ( ( ( Long) AKeys [i]).intValue ( ) == iAddress) return ( i);
		}
		
		return 0;
	}

	/* (non-Javadoc)
	 * @see simdraw.LinkSimulatorVisualization#connectorAt(java.lang.String)
	 */
	public Connector connectorAt(String parName) {
		// TODO Auto-generated method stub
		return null;
	}
	private Contents ctData;
	private Hashtable htData;
	private Object [] AKeys;
	private int iSizeAKeys;
	private int iTypeValue;
	MyTableModel tableModel;
	JTable table;
	JScrollPane scroll;
	JButton button;
	Simulator sSim;
	Processor pProc;
	String sComp, sEndereco;
}
