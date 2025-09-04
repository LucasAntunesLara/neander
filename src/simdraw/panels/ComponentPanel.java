/*
 * Created on 01/05/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package simdraw.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
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
import ccomb.Circuit;
import datapath.Datapath;

/**
 * @author unknown
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ComponentPanel extends JPanel implements ItemListener, LinkSimulatorVisualization, Define {

	/**
	 * @throws java.awt.HeadlessException
	 */
	public ComponentPanel(Simulator parSim, Processor parProc) throws HeadlessException {
		super();
		sSim = parSim;
		pProc = parProc;
		String sTmp = null;
		int iActive = 0;

		jbComp = new JComboBox ( );
		jbComp.addItemListener ( this);
		JPanel jCb = new JPanel ( );
		JLabel lChoose = new JLabel ( Lang.iLang==ENGLISH?Lang.msgsGUI[70]:Lang.msgsGUI[75]);
		
		Datapath dtp = pProc.getDatapath ( );
		int iNelem = dtp.scEsquematico.getNelems ( );
		for ( int i = 0; i < iNelem; i ++) {
			Circuit cComponent = ( Circuit) dtp.scEsquematico.traverse ( i);
			if ( sTmp == null) {
				String sClass = cComponent.getClass ( ).getName ( );
				if ( sClass.startsWith("cseq") == true) {
					cCurrent = cComponent;
					sTmp = cCurrent.getName ( ).toString ( );
					iActive = i;
				}
			}
			jbComp.addItem ( cComponent.getName ( ));
		}
		jbComp.setSelectedIndex( iActive);
		pSequential = new SequentialPanel ( sSim, pProc);
		try {
			pSequential.setComponent ( sTmp);
		} catch ( Exception e) {
			pSequential.setVisible ( false);
		}

		tableModel = new MyTableModel ( );
		table = new JTable ( tableModel);
		table.setAutoResizeMode ( JTable.AUTO_RESIZE_ALL_COLUMNS);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();
		int screenHeight = d.height;
		int screenWidth = d.width;
		//table.setPreferredScrollableViewportSize(new Dimension ( screenWidth, screenHeight));
		scroll = new JScrollPane ( table);
		setLayout ( new BorderLayout ( 5, 5));

		JLabel jTitle = new JLabel ( Lang.iLang==ENGLISH?Lang.msgsGUI[71]:Lang.msgsGUI[76]);
		JPanel pTitle = new JPanel ( );
		pTitle.setBackground ( Color.LIGHT_GRAY);
		pTitle.add ( jTitle);
		JPanel pTable = new JPanel ( );
		pTable.setLayout ( new BorderLayout ( ));
		pTable.add ( pTitle, BorderLayout.NORTH);
		pTable.add ( scroll, BorderLayout.CENTER);
		
		jCb.add ( lChoose);
		jCb.add ( jbComp);
		add ( jCb, BorderLayout.NORTH);
		add ( pTable, BorderLayout.CENTER);
		add ( pSequential, BorderLayout.EAST);
	}

	public void itemStateChanged(ItemEvent arg0) {
		String sComp = jbComp.getItemAt( jbComp.getSelectedIndex ( )).toString();
		Datapath dtp = pProc.getDatapath ( );
		cCurrent = ( Circuit) dtp.search ( sComp);
		tableModel = new MyTableModel ( );
		if ( table != null) table.setModel ( tableModel);
		if ( pSequential != null) {
			try {
				pSequential.setComponent ( cCurrent.getName ( ).toString ( ));
				pSequential.setVisible ( true);
			} catch ( Exception e) {
				pSequential.setVisible ( false);
			}
		}
	}

	//
	// TABLE MODEL BEGIN
	//
	class MyTableModel extends AbstractTableModel {
		private String[] columnNames;
		private Object[][] tmp, data; 
		
		MyTableModel ( ) {
			int i = 0;
			//String sComp = jbComp.getItemAt( jbComp.getSelectedIndex ( )).toString();
			//Datapath dtp = pProc.getDatapath ( );
			//Circuit cCurrent = ( Circuit) dtp.search ( sComp);
			int iNelems = cCurrent.getNelemsAll();
			tmp = cCurrent.getState ( );
			columnNames = new String [ iNelems];
			for ( i = 0; i < iNelems; i ++)
				columnNames [ i] = (String) tmp [ i] [ 0].toString ( );			
			data = new Object [ 1] [ iNelems];
			for ( i = 0; i < iNelems; i ++) {
				data [ 0] [ i] = tmp [ i] [ 1];
			}	
		}

		public int getColumnCount() {
			return columnNames.length;
		}

		public int getRowCount() {
			return data.length;
		}

		public String getColumnName(int col) {
			return columnNames[col];
		}

		public Object getValueAt(int row, int col) {
			return data[row][col];
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

			data[row][col] = value;
			fireTableCellUpdated(row, col);

			if (false) {
				System.out.println("New value of data:");
				printDebugData();
			}

		}

		private void printDebugData() {
			int numRows = getRowCount();
			int numCols = getColumnCount();

			for (int i=0; i < numRows; i++) {
				System.out.print("    row " + i + ":");
				for (int j=0; j < numCols; j++) {
					System.out.print("  " + data[i][j]);
				}
				System.out.println();
			}
			System.out.println("--------------------------");
		}
		//
		// TABLE MODEL END
		//
	}

	/* (non-Javadoc)
	 * @see simdraw.LinkSimulatorVisualization#refreshDisplay(simulator.Simulador, processor.Processor, datapath.Datapath)
	 */
	public void refreshDisplay(Simulator parSim, Processor parProc) {
		int i = 0;
		int iNelems = cCurrent.getNelemsAll();
		Object [] [] tmp = cCurrent.getState ( ), data;
		
		data = new Object [ 1] [ iNelems];
		for ( i = 0; i < iNelems; i ++) {
			data [ 0] [ i] = tmp [ i] [ 1];
			tableModel.setValueAt( data [0] [i], 0, i);
		}
		
		try {
			pSequential.refreshDisplay ( parSim,parProc);
			pSequential.setVisible ( true);
		} catch ( Exception e) {
			pSequential.setVisible ( false);
		}
	}

	/* (non-Javadoc)
	 * @see simdraw.LinkSimulatorVisualization#connectorAt(java.lang.String)
	 */
	public Connector connectorAt(String parName) {
		// TODO Auto-generated method stub
		return null;
	}
	//Object [ ] [ ] objVetor;
	
	MyTableModel tableModel;
	JTable table;
	JScrollPane scroll;
	JComboBox jbComp;
	SequentialPanel pSequential;
	Simulator sSim;
	Processor pProc;
	Circuit cCurrent;
}
