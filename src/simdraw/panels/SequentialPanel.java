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

/**
 * @author unknown
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SequentialPanel extends JPanel implements LinkSimulatorVisualization, Define {

	/**
	 * @throws java.awt.HeadlessException
	 */
	public SequentialPanel(Simulator parSim, Processor parProc) throws HeadlessException {
		super();
		sSim = parSim;
		pProc = parProc;

		table = new JTable ( ); 
		table.setAutoResizeMode ( JTable.AUTO_RESIZE_ALL_COLUMNS);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();
		int screenHeight = d.height;
		int screenWidth = d.width;
		scroll = new JScrollPane ( table);
		
		setLayout ( new BorderLayout ( ));
		JLabel jTitle = new JLabel ( Lang.iLang==ENGLISH?Lang.msgsGUI[120]:Lang.msgsGUI[125]);
		JPanel pTitle = new JPanel ( );
		pTitle.setBackground ( Color.LIGHT_GRAY);
		pTitle.add ( jTitle);
		add ( pTitle, BorderLayout.NORTH);
		add ( scroll, BorderLayout.CENTER);
	}

	public void setComponent ( String parComp) {
		sComp = parComp;
		tableModel = new MyTableModel ( );
		table.setModel ( tableModel);
	}

	//
	// TABLE MODEL BEGIN
	//
	class MyTableModel extends AbstractTableModel {
		private String[] columnNames;
		
		MyTableModel ( ) {
			int nCols;
			String sAux;
			Sequential cSeq = ( Sequential) pProc.getDatapath().search ( sComp);
			ctData = cSeq.cConteudo;
			nCols = ctData.getSizeY ( );
			if ( nCols == 0) nCols = 2;
			else nCols ++;		// uma coluna a mais para o indice
			columnNames = new String [nCols];
			columnNames [ 0] = Lang.iLang==ENGLISH?Lang.msgsGUI[121]:Lang.msgsGUI[126];
			if ( nCols > 2) {
				for ( int i = 1; i < nCols; i ++) 
					columnNames [ i] = (Lang.iLang==ENGLISH?Lang.msgsGUI[122]:Lang.msgsGUI[127])+" "+new Integer ( i).toString ( );
			} else {
				columnNames [ 1] = Lang.iLang==ENGLISH?Lang.msgsGUI[122]:Lang.msgsGUI[127];
			}
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
			double dValue;
			Long LTmp;
			int iTmp;
			
			if ( iSizeAKeys != htData.size ( )) {
				AKeys = htData.keySet().toArray();
				Arrays.sort ( AKeys);
				iSizeAKeys = AKeys.length;		
				fireTableStructureChanged();
			}
			
			if ( ctData.getSizeY ( ) != 0) {
				LTmp = (Long) AKeys [row];
				iTmp = LTmp.intValue ( );
				if ( col == 0) {
					sAux = LTmp.toString ( );
				} else if ( iTypeValue == INTEGER) {
					lValue = ctData.getIntegerValue2D ( iTmp, col-1);
					sAux = SistNum.printInformation ( lValue, iTypeValue);;
				} else if ( iTypeValue == FP) {
					dValue = ctData.getFloatingPointValue2D ( iTmp, col-1);
					sAux = new Double ( dValue).toString ( );
				}
			} else {
				LTmp = (Long) AKeys [row];
				iTmp = LTmp.intValue ( );
				if ( col == 0) {
					sAux = LTmp.toString ( );
				}
				else if ( iTypeValue == INTEGER) {
					lValue = ctData.getIntegerValue ( iTmp);
					sAux = SistNum.printInformation ( lValue, iTypeValue);
				} else if ( iTypeValue == FP) {
					dValue = ctData.getFloatingPointValue ( iTmp);
					sAux = new Double ( dValue).toString ( );					
				}
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
		Datapath dtp = parProc.getDatapath();
		String sAux = null;
		long lValue;
		double dValue;
		Object [] AKeysChanged, AKeysInserted;		
		Long LTmp;
		int i, j, iTmp;
		
		AKeysInserted = ctData.getInsertedKeys().keySet().toArray();
		if ( AKeysInserted.length > 0) {
			tableModel.fireTableStructureChanged();
		}
		else {
			AKeysChanged = ctData.getChangedKeys().keySet().toArray();
			Arrays.sort ( AKeysChanged);
			if ( ctData.getSizeY ( ) == 0) {
				for ( i = 0; i < AKeysChanged.length; i ++) {
					LTmp = (Long) AKeysChanged [i];
					iTmp = LTmp.intValue ( );
					if ( iTypeValue == INTEGER) {
						lValue = ctData.getIntegerValue ( iTmp);
						sAux = SistNum.printInformation ( lValue, iTypeValue);
					} else if ( iTypeValue == FP) {
						dValue = ctData.getFloatingPointValue ( iTmp);
						sAux = new Double ( dValue).toString ( );					
					}
					tableModel.setValueAt( sAux, i, 1);
				}			
			} else {
				for ( i = 0; i < AKeysChanged.length; i ++) {
					LTmp = (Long) AKeysChanged [i];
					iTmp = LTmp.intValue ( );
					for ( j = 0; j < ctData.getSizeY ( ); j ++) {
						if ( iTypeValue == INTEGER) {
							lValue = ctData.getIntegerValue2D ( iTmp, j);
							sAux = SistNum.printInformation ( lValue, iTypeValue);;
						} else if ( iTypeValue == FP) {
							dValue = ctData.getFloatingPointValue2D ( iTmp, j);
							sAux = new Double ( dValue).toString ( );
						}
						tableModel.setValueAt( sAux, i, j+1);
					}
				}						
			}			
		}
		
		dtp.resetContents();
	}

	/* (non-Javadoc)
	 * @see simdraw.LinkSimulatorVisualization#connectorAt(java.lang.String)
	 */
	public Connector connectorAt(String parName) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private Sequential cSeq;
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
