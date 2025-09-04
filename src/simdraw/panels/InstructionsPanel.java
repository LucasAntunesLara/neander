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


import montador.Montador;

import org.jhotdraw.framework.Connector;

import platform.Lang;
import processor.Processor;
import simdraw.LinkSimulatorVisualization;
import simulator.Simulator;
import util.Define;
import util.SistNum;
import control.Instruction;
import control.ExecutionStage;
import control.ExecutionPath;

/**
 * @author unknown
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class InstructionsPanel extends JPanel implements LinkSimulatorVisualization, Define {

	/**
	 * @throws java.awt.HeadlessException
	 */
	public InstructionsPanel(Simulator parSim, Processor parProc, String whichPipe) throws HeadlessException {
		super();
		sSim = parSim;
		pProc = parProc;

		pipe = pProc.getSuperescalar ( ).search ( whichPipe);
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
		JLabel jTitle = new JLabel ( Lang.iLang==ENGLISH?Lang.msgsGUI[82]:Lang.msgsGUI[87]);
		JPanel pTitle = new JPanel ( );
		pTitle.setBackground ( Color.LIGHT_GRAY);
		pTitle.add ( jTitle);
		add ( pTitle, BorderLayout.NORTH);
		add ( scroll, BorderLayout.CENTER);
	}

	public void setPipeline ( String whichPipe) {
		if ( whichPipe != null) pipe = pProc.getSuperescalar ( ).search ( whichPipe);
		else pipe = (ExecutionPath) pProc.getSuperescalar ( ).getPipelines ( ).traverse ( 0);
		tableModel = new MyTableModel ( );
		if ( table != null) table.setModel ( tableModel);
	}

	//
	// TABLE MODEL BEGIN
	//
	class MyTableModel extends AbstractTableModel {
		private String[] columnNames;
		private Object[][] tmp, data; 
		
		MyTableModel ( ) {
			//pipe = pProc.getPipeline ( );
			ExecutionStage psAux;
			int nStages = pipe.getExecutionStages ( ).getNelems ( );
			int i,j, iNelems = 0;

			Instruction inst = new Instruction();
			if ( inst != null) {
				if ( iNelems == 0) {
					iNelems = inst.getNelemsAll ( );
					columnNames = new String [ iNelems];
				}
				tmp = inst.getState ( );
				for ( j = 0; j < iNelems; j ++)
					columnNames [ j] = (String) tmp [ j] [ 0].toString ( );			
			}
			data = new Object [ nStages] [ iNelems];
			for ( i = 0; i < nStages; i ++) {
				psAux = ( ExecutionStage) pipe.getExecutionStages ( ).traverse ( i);
				inst = psAux.getCurrentInst();
				if ( inst != null) {
					tmp = inst.getState ( );
					for ( j = 0; j < inst.getNelemsAll ( ); j ++)
						data [ i] [ j] = tmp [ j] [ 1];
				} else {
					for ( j = 0; j < iNelems; j ++)
						data [ i] [ j] = new String ( "");
				}
			}			
			
			iNelemsByInstruction = iNelems;
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
		ExecutionStage psAux;
		int i, j, cont = 0;
			
		int nStages = pipe.getExecutionStages ( ).getNelems ( );
			
		data = new Object [ nStages] [ iNelemsByInstruction];
		for ( i = 0; i < nStages; i ++) {
			psAux = ( ExecutionStage) pipe.getExecutionStages ( ).traverse ( i);
			Instruction inst = psAux.getCurrentInst();
			if ( inst != null) {
				tmp = inst.getState ( );
				for ( j = 0; j < iNelemsByInstruction; j ++) {
					data [ i] [ j] = tmp [ j] [ 1];
					//data [ i] [ j] = new Integer ( j);
					if ( tmp [ j] [ 0].toString().equals( "DESCRIPTION") ) {
						//Long lTmp = ( Long) tmp [ 0] [ 1];
						//data [ i] [ j] = parProc.getMnemonico ( lTmp.longValue());
						if ( 	pProc.getClass().getName().equalsIgnoreCase("processor.DLX")
								|| pProc.getClass().getName().equalsIgnoreCase("processor.DLXMulti")) {
							data [ i] [ j] = new String ( "");;
						}
						else data [ i] [ j] = parProc.getMnemonico ( SistNum.getValue ( (String) tmp [0][1]));
					}
					if ( tmp [ j] [ 0].toString().equalsIgnoreCase( "mnem") ) {
						if ( 	pProc.getClass().getName().equalsIgnoreCase("processor.DLX")
								|| pProc.getClass().getName().equalsIgnoreCase("processor.DLXMulti")) {
							String sTmp = null;
							long lpc = inst.get ( "PC", FIELD);
							if ( lpc != INVALID_PC) sTmp = Montador.getAssemblyCode ( (int) lpc);
							if ( sTmp != null) data [i] [j] = new String ( sTmp);
							else data [ i] [ j] = new String ( "");
						}
					}
					tableModel.setValueAt( data [i] [j], i, j);
				}			
			} else {
				for ( j = 0; j < iNelemsByInstruction; j ++) {
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
	Simulator sSim;
	Processor pProc;
	ExecutionPath pipe;
	String sComp;
	int iNelemsByInstruction;
}
