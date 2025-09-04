/*
 * Created on 01/05/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package simdraw.panels;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;


import org.jhotdraw.framework.Connector;

import platform.Lang;
import processor.Processor;
import simdraw.LinkSimulatorVisualization;
import simulator.Simulator;
import util.Define;
import control.QueuesOfInstructions;
import control.InstructionQueue;

/**
 * @author unknown
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class InstructionQueuesPanel extends JPanel implements ItemListener, LinkSimulatorVisualization, Define {

	/**
	 * @throws java.awt.HeadlessException
	 */
	public InstructionQueuesPanel(Simulator parSim, Processor parProc) throws HeadlessException {
		super();
		sSim = parSim;
		pProc = parProc;

		setLayout ( new BorderLayout ( 5, 5));
System.out.println ( "Criando o painel...");		
		pInstructions = new InstructionsIQPanel ( sSim, pProc, null);

		jbComp = new JComboBox ( );
		jbComp.addItemListener ( this);
		JPanel jCb = new JPanel ( );
		JLabel lChoose = new JLabel ( Lang.iLang==ENGLISH?Lang.msgsGUI[80]:Lang.msgsGUI[85]);
		
		JPanel controlPanel = new JPanel ( );
		controlPanel.setLayout( new GridLayout ( 1, 2, 5, 5));
		controlPanel.add(pInstructions);

		jCb.add ( lChoose);
		jCb.add ( jbComp);
		add ( jCb, BorderLayout.NORTH);
		add ( controlPanel, BorderLayout.CENTER);
	}

	public void itemStateChanged(ItemEvent arg0) {
		String sComp = jbComp.getItemAt( jbComp.getSelectedIndex ( )).toString();
		if ( qoi != null) {
			pInstructions.setQueue ( sComp);
			pInstructions.refreshDisplay ( sSim, pProc);
		}
	}

	public void refreshDisplay(Simulator parSim, Processor parProc) {
		qoi = pProc.getQueuesOfInstructions ( );
		if ( qoi == null) return;
		if ( firstTime) {
			int iNelem = qoi.getQueues ( ).getNelems ( );

			for ( int i = 0; i < iNelem; i ++) {
				String sTmp;
				InstructionQueue iq = (InstructionQueue) qoi.getQueues ( ).traverse ( i);
				sTmp = iq.getName ( );
				if ( sTmp != null) jbComp.addItem ( sTmp);
				else jbComp.addItem ( new Integer ( i).toString ( ));
			}

			firstTime = false;
		}
		String sComp = jbComp.getItemAt( jbComp.getSelectedIndex ( )).toString();
		pInstructions.setQueue ( sComp);
		pInstructions.refreshDisplay ( sSim, pProc);
	}

	/* (non-Javadoc)
	 * @see simdraw.LinkSimulatorVisualization#connectorAt(java.lang.String)
	 */
	public Connector connectorAt(String parName) {
		// TODO Auto-generated method stub
		return null;
	}
	
	JComboBox jbComp;
	InstructionsIQPanel pInstructions;
	Simulator sSim;
	Processor pProc;
	QueuesOfInstructions qoi;
	boolean firstTime = true;
}
