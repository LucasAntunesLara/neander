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
import control.ExecutionPath;
import control.ExecutionStage;
import control.Instruction;
import montador.Montador;

/**
 * @author unknown
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class PipelinePanel extends JPanel implements LinkSimulatorVisualization, Define {

	/**
	 * @throws java.awt.HeadlessException
	 */
	public PipelinePanel(Simulator parSim, Processor parProc, String whichPipe) throws HeadlessException {
		super();
		sSim = parSim;
		pProc = parProc;
		pipe = pProc.getSuperescalar().search ( whichPipe);

		tableModel = new MyTableModel ( );
		table = new JTable ( tableModel);
		table.setAutoResizeMode ( JTable.AUTO_RESIZE_ALL_COLUMNS);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();
		int screenHeight = d.height;
		int screenWidth = d.width;
		//table.setPreferredScrollableViewportSize(new Dimension ( (int) (screenWidth*0.48), (int) (screenHeight*0.5)));
		scroll = new JScrollPane ( table);
		setLayout ( new BorderLayout ( ));
		JLabel jTitle = new JLabel ( Lang.iLang==ENGLISH?Lang.msgsGUI[100]:Lang.msgsGUI[105]);
		JPanel pTitle = new JPanel ( );
		pTitle.setBackground ( Color.LIGHT_GRAY);
		pTitle.add ( jTitle);
		add ( pTitle, BorderLayout.NORTH);
		add ( scroll, BorderLayout.CENTER);
	}

	public void setPipeline ( String whichPipe) {
		if ( whichPipe != null) pipe = pProc.getSuperescalar ( ).search ( whichPipe);
		else pipe = (ExecutionPath) pProc.getSuperescalar ( ).getPipelines ( ).traverse ( 0);
		if ( pipe != null) {
			//System.out.println ( "setPipeline");
			//pipe.debug ( );
		}
		tableModel = new MyTableModel ( );
		if ( table != null) table.setModel ( tableModel);
	}
	
	//
	// TABLE MODEL BEGIN
	//
	class MyTableModel extends AbstractTableModel {
		private String[] columnNames;
		private String[] instructions;
		private Object[][] data; 
		
		MyTableModel ( ) {
			//
			ExecutionStage psAux;

			int nStages = pipe.getExecutionStages ( ).getNelems ( );
			columnNames = new String [ nStages];
			instructions = new String [ nStages];
			
			for ( int i = 0; i < nStages; i ++) {
				psAux = ( ExecutionStage) pipe.getExecutionStages ( ).traverse ( i);
				columnNames [ i] = new String (psAux.getName ( ));
				Instruction inst = psAux.getCurrentInst();
				if ( inst == null) instructions [ i] = "";
				else instructions [ i] = new String ( "instruction");
			}	

			data = new Object [ nStages] [ nStages];
			for ( int i = 0; i < nStages; i ++) {
				for ( int j = 0; j < nStages; j ++) {
					if ( i == j) data [ i] [j] = instructions [ i];
					else data [ i] [ j] = new String ( "");
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
		String[] columnNames;
		String[] instructions;
		Object[][] data; 
		ExecutionStage psAux;

		int nStages = pipe.getExecutionStages ( ).getNelems ( );
		columnNames = new String [ nStages];
		instructions = new String [ nStages];

		for ( int i = 0; i < nStages; i ++) {
			psAux = ( ExecutionStage) pipe.getExecutionStages ( ).traverse ( i);
			columnNames [ i] = new String (psAux.getName ( ));
			Instruction inst = psAux.getCurrentInst();
			if ( inst == null) instructions [ i] = "";
			else {
				String sTmp = null;
				long lpc = inst.get ( "PC", FIELD);
				if ( lpc != INVALID_PC) sTmp = Montador.getAssemblyCode ( (int) lpc);
				if ( sTmp != null) instructions [ i] = sTmp;
				else instructions [ i] = new String ( inst.getDescription ( ));
			} 
		}	

		data = new Object [ nStages] [ nStages];
		for ( int i = 0; i < nStages; i ++) {
			data [ i] [i] = instructions [ i];
			tableModel.setValueAt( data [i] [i], i, i);
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
	String sComp, whichPipeGlobal;
}
