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
import control.Instruction;
import control.InstructionQueue;
import control.ItemInstructionQueue;

/**
 * @author unknown
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class InstructionsIQPanel extends JPanel implements LinkSimulatorVisualization, Define {

	/**
	 * @throws java.awt.HeadlessException
	 */
	public InstructionsIQPanel(Simulator parSim, Processor parProc, String whichQueue) throws HeadlessException {
		super();
		sSim = parSim;
		pProc = parProc;

		if ( whichQueue != null) iq = pProc.getQueuesOfInstructions ( ).search ( whichQueue);
		
		table = new JTable ( );
		table.setAutoResizeMode ( JTable.AUTO_RESIZE_ALL_COLUMNS);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();
		int screenHeight = d.height;
		int screenWidth = d.width;
		//table.setPreferredScrollableViewportSize(new Dimension ( (int) (screenWidth*0.48), (int) (screenHeight*0.5)));
		scroll = new JScrollPane ( table);
		scroll = new JScrollPane ( table);
		setLayout ( new BorderLayout ( ));
		jTitle = new JLabel ( Lang.iLang==ENGLISH?Lang.msgsGUI[81]:Lang.msgsGUI[86]);
		JPanel pTitle = new JPanel ( );
		pTitle.setBackground ( Color.LIGHT_GRAY);
		pTitle.add ( jTitle);
		add ( pTitle, BorderLayout.NORTH);
		add ( scroll, BorderLayout.CENTER);
	}

	public void setQueue ( String whichQueue) {
		if ( whichQueue != null) {
			jTitle.setText( Lang.iLang==ENGLISH?Lang.msgsGUI[81]:Lang.msgsGUI[86]+whichQueue);
			iq = pProc.getQueuesOfInstructions ( ).search ( whichQueue);
			tableModel = new MyTableModel ( );
			if ( table != null) table.setModel ( tableModel);
		}
	}

	//
	// TABLE MODEL BEGIN
	//
	class MyTableModel extends AbstractTableModel {
		private String[] columnNames;
		private Object[][] tmp, data; 
		
		MyTableModel ( ) {
			int nInstructions = iq.getInstructions ( ).getNelems ( );
			int i,j, iNelems = 0;

			ItemInstructionQueue.defineFieldsOfInstructions( iq.getFieldsOfItem ( ),iq.getFieldsStrOfItem ( ));
			ItemInstructionQueue inst = new ItemInstructionQueue( new Instruction ( ));
			if ( inst != null) {
				if ( iNelems == 0) {
					iNelems = inst.getNelemsAll ( );
					columnNames = new String [ iNelems];
				}
				tmp = inst.getState ( );
				for ( j = 0; j < iNelems; j ++)
					columnNames [ j] = (String) tmp [ j] [ 0].toString ( );			
			}
			data = new Object [ nInstructions] [ iNelems];
			for ( i = 0; i < nInstructions; i ++) {
				inst = ( ItemInstructionQueue) iq.getInstructions ( ).traverse( i);
				if ( inst != null) {
					tmp = inst.getState ( );
					for ( j = 0; j < inst.getNelemsAll ( ); j ++)
						data [ i] [ j] = tmp [ j] [ 1];			
				} else {
					for ( j = 0; j < iNelems; j ++)
						data [ i] [ j] = new String ( "");
				}
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
		Object[][] tmp, data; 
		int i, j, cont = 0;
		ItemInstructionQueue.defineFieldsOfInstructions( iq.getFieldsOfItem ( ), iq.getFieldsStrOfItem ( ));
		ItemInstructionQueue inst = new ItemInstructionQueue( new Instruction ( ));
		int iNelems = inst.getNelemsAll ( );
				
		if ( iq == null) return;
		
		int nInstructions = iq.getInstructions ( ).getNelems ( );
			
		data = new Object [ nInstructions] [ iNelems];
		for ( i = 0; i < nInstructions; i ++) {
			inst = ( ItemInstructionQueue) iq.getInstructions ( ).traverse( i);
			if ( inst != null) {
				tmp = inst.getState ( );
				for ( j = 0; j < iNelems; j ++) {
					data [ i] [ j] = tmp [ j] [ 1];
					if ( tmp [ j] [ 0].toString().equals( "DESCRIPTION") ) {
						//Long lTmp = ( Long) tmp [ 0] [ 1];
						//data [ i] [ j] = parProc.getMnemonico ( lTmp.longValue());
						data [ i] [ j] = parProc.getMnemonico ( SistNum.getValue ( (String) tmp [0][1]));
					}
					tableModel.setValueAt( data [i] [j], i, j);
				}			
			} else {
				for ( j = 0; j < iNelems; j ++) {
					data [ i] [ j] = new String ( "");
					tableModel.setValueAt( data [i] [j], i, j);
				}					
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
	JButton button;
	JLabel jTitle;
	Simulator sSim;
	Processor pProc;
	InstructionQueue iq;
	String sComp;
}
