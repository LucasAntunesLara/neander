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

/**
 * @author unknown
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ProcessorPanel extends JPanel implements LinkSimulatorVisualization, Define {

	/**
	 * @throws java.awt.HeadlessException
	 */
	public ProcessorPanel(Simulator parSim, Processor parProc) throws HeadlessException {
		super();
		sSim = parSim;
		pProc = parProc;
		
		tableModel = new MyTableModel ( );
		table = new JTable ( tableModel);
		table.setAutoResizeMode ( JTable.AUTO_RESIZE_ALL_COLUMNS);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();
		int screenHeight = d.height;
		int screenWidth = d.width;
		//table.setPreferredScrollableViewportSize(new Dimension ( (int) (screenWidth*0.48), (int) (screenHeight*0.5)));
		scroll = new JScrollPane ( table);
		scroll = new JScrollPane ( table);
		setLayout ( new BorderLayout ( ));
		String sTmp = Lang.iLang==ENGLISH?Lang.msgsGUI[110]:Lang.msgsGUI[115];
		JLabel jTitle = new JLabel ( pProc.getString("NAME",STRING)+sTmp);
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
		private String[] columnNames;
		private Object[][] tmp, data; 
		
		MyTableModel ( ) {
			int i,j, iNelems = 0;

			if ( pProc != null) {
				if ( iNelems == 0) {
					iNelems = pProc.getNelemsAll ( );
					columnNames = new String [ iNelems];
				}
				tmp = pProc.getState ( );
				for ( j = 0; j < iNelems; j ++)
					columnNames [ j] = (String) tmp [ j] [ 0].toString ( );			
			}
			data = new Object [ 1] [ iNelems];
			if ( pProc != null) {
				tmp = pProc.getState ( );
				for ( j = 0; j < iNelems; j ++)
					data [ 0] [ j] = tmp [ j] [ 1];			
			} else {
				for ( j = 0; j < iNelems; j ++)
					data [ 0] [ j] = new String ( "");
			}
			
			iNelemsInProc = iNelems;
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
		Object[][] tmp, data; 
		int i, j, cont = 0;

		data = new Object [ 1] [ iNelemsInProc];
		if ( pProc != null) {
//System.out.println ( "refreshDisplay de processor");
			tmp = pProc.getState ( );
			for ( j = 0; j < iNelemsInProc; j ++) {
				data [ 0] [ j] = tmp [ j] [ 1];		
//System.out.println ( "refresh data:"+data[0][j]);
				tableModel.setValueAt( data [0] [j], 0, j);
			}
		} else {
			for ( j = 0; j < iNelemsInProc; j ++) {
				data [ 0] [ j] = new String ( "");
				tableModel.setValueAt( data [0] [j], 0, j);
			}
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
	//JButton button;
	//JLabel jTitle;
	Simulator sSim;
	Processor pProc;
	int iNelemsInProc;
}
