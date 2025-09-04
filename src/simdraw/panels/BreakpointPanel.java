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
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


import org.jhotdraw.framework.Connector;

import platform.Lang;
import processor.Processor;
import simdraw.LinkSimulatorVisualization;
import simulator.Simulator;
import util.Define;
import breakpoints.Breakpoint;

/**
 * @author unknown
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class BreakpointPanel extends JPanel implements LinkSimulatorVisualization, Define {

	/**
	 * @throws java.awt.HeadlessException
	 */
	public BreakpointPanel(Simulator parSim, Processor parProc) throws HeadlessException {
		super();
		sSim = parSim;
		pProc = parProc;
		
		tableModel = new MyTableModel ( );
		table = new JTable ( tableModel);
		table.setAutoResizeMode ( JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();
		int screenHeight = d.height;
		int screenWidth = d.width;
		table.setPreferredScrollableViewportSize(new Dimension ( (int) (screenWidth*0.48), (int) (screenHeight*0.5)));
		scroll = new JScrollPane ( table);
		scroll = new JScrollPane ( table);
		setLayout ( new BorderLayout ( ));
		String sTmp = Lang.iLang==ENGLISH?Lang.msgsGUI[279]:Lang.msgsGUI[284];
		JLabel jTitle = new JLabel ( pProc.getString("NAME",STRING)+sTmp);
		JPanel pTitle = new JPanel ( );
		pTitle.setBackground ( Color.LIGHT_GRAY);
		pTitle.add ( jTitle);
		add ( pTitle, BorderLayout.NORTH);
		add ( scroll, BorderLayout.CENTER);
		
		ListSelectionModel rowSM = table.getSelectionModel();
        rowSM.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                //Ignore extra messages.
                if (e.getValueIsAdjusting()) return;

                ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                if (lsm.isSelectionEmpty()) {
                    //System.out.println("No rows are selected.");                	
                } else {
                    int selectedRow = lsm.getMinSelectionIndex();
                    //System.out.println("Row " + selectedRow + " is now selected.");
                    //System.out.println(tableModel.getValueAt(selectedRow, 0).toString());
                    String str = tableModel.getValueAt(selectedRow, 0).toString();
                    
                    if(pProc.sBreakpoint != null && pProc.sBreakpoint.searchPrimitive(str) != null) {                    	
                    	if(table.getSelectedColumn() == 5) {            
                    		bpAux = (Breakpoint) pProc.sBreakpoint.searchPrimitive(str);
                    		bpAux.active = !bpAux.active;                    
                    		//tableModel.setValueAt( new Boolean(bpAux.active), selectedRow, 5);
                    	} else if(table.getSelectedColumn() == 6) {
                    		pProc.sBreakpoint.remove(str);                    		
                    	}
                    	refreshDisplay(sSim, pProc);
                    }
                    table.clearSelection();
                }
            }
        });
	}

	//
	// TABLE MODEL BEGIN
	//
	class MyTableModel extends AbstractTableModel {
		private String[] columnNames;
		private Object[][] tmp, data; 
		
		MyTableModel ( ) {
			int i,j, iNelems = 0, iNrows = 0;			

			if ( pProc != null) {
				if ( iNelems == 0) {
					iNelems = 7;
					columnNames = new String [ iNelems];
				}
				columnNames [ 0] = "Comp";
				columnNames [ 1] = "x";
				columnNames [ 2] = "y";
				columnNames [ 3] = "z";
				columnNames [ 4] = "Value";
				columnNames [ 5] = "Status";
				columnNames [ 6] = "Remove";
			}
						
			bpAux = new Breakpoint();			
			if(pProc.sBreakpoint != null) {
				iNrows = pProc.sBreakpoint.getNelems();
				data = new Object [ iNrows] [ iNelems];
				
				for(i=0; i < iNrows; i++) {
					bpAux = (Breakpoint) pProc.sBreakpoint.traverse(i);
					if ( bpAux.mp != null) {
						//tmp = pProc.getState ( );
				
						data [ i] [ 0] = bpAux.mp.getComponentName();
						data [ i] [ 1] = String.valueOf(bpAux.mp.getPortType());
						data [ i] [ 2] = "";
						data [ i] [ 3] = "";
						data [ i] [ 4] = String.valueOf(bpAux.mp.getContents());
						data [ i] [ 5] = new Boolean(bpAux.active);
						data [ i] [ 6] = new Boolean(false);
					}
				}
				tableModel.fireTableRowsInserted(1, iNrows);
			} else {
				data = new Object [ 1] [ iNelems];
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
		Object[][] data; 
		int i, j, iNrows = 0;
		
		if(pProc.sBreakpoint != null && pProc.sBreakpoint.getNelems() > 0) {
			iNrows = pProc.sBreakpoint.getNelems();
			
			data = new Object [ iNrows] [ iNelemsInProc];
			tableModel.data = data;
			
			for(i=0; i < iNrows; i++) {
				bpAux = (Breakpoint) pProc.sBreakpoint.traverse(i);
				if ( bpAux.mp != null) {
					//mp
					//tmp = pProc.getState ( );			
			
					data [ i] [ 0] = bpAux.mp.getComponentName();
					data [ i] [ 1] = String.valueOf(bpAux.mp.getPortType());
					data [ i] [ 2] = "";
					data [ i] [ 3] = "";
					data [ i] [ 4] = String.valueOf(bpAux.mp.getContents());					
					data [ i] [ 5] = new Boolean(bpAux.active);
					data [ i] [ 6] = new Boolean(false);
	
					for ( j = 0; j < iNelemsInProc; j ++) {			
						tableModel.setValueAt( data [i] [j], i, j);
						//System.out.println(" ########################## " + tableModel.getValueAt(i, j));
					}
				}
			}
			tableModel.fireTableRowsInserted(1, iNrows);
		} else {
			data = new Object [ 1] [ iNelemsInProc];
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
	Breakpoint bpAux;
}
