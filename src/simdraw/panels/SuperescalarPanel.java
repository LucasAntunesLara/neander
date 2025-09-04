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
import control.ExecutionPath;

/**
 * @author unknown
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SuperescalarPanel extends JPanel implements ItemListener, LinkSimulatorVisualization, Define {

	/**
	 * @throws java.awt.HeadlessException
	 */
	public SuperescalarPanel(Simulator parSim, Processor parProc) throws HeadlessException {
		super();
		sSim = parSim;
		pProc = parProc;

		setLayout ( new BorderLayout ( 5, 5));
		
		pPipeline = new PipelinePanel ( sSim, pProc, null);
		pInstructions = new InstructionsPanel ( sSim, pProc, null);

		jbComp = new JComboBox ( );
		jbComp.addItemListener ( this);
		JPanel jCb = new JPanel ( );
		JLabel lChoose = new JLabel ( Lang.iLang==ENGLISH?Lang.msgsGUI[130]:Lang.msgsGUI[135]);
		
		int iNelem = pProc.getSuperescalar ( ).getPipelines ( ).getNelems ( );
		
		for ( int i = 0; i < iNelem; i ++) {
			String sTmp;
			ExecutionPath pPipe = (ExecutionPath) pProc.getSuperescalar ( ).getPipelines ( ).traverse ( i);
			sTmp = pPipe.getName ( );
			if ( sTmp != null) jbComp.addItem ( sTmp);
			else jbComp.addItem ( new Integer ( i).toString ( ));
		}

		String sComp = jbComp.getItemAt( jbComp.getSelectedIndex ( )).toString();
		if ( sComp.equals ( "0")) sComp = null;
		pPipeline.setPipeline( sComp);
		pInstructions.setPipeline ( sComp);

		JPanel controlPanel = new JPanel ( );
		controlPanel.setLayout( new GridLayout ( 2, 1, 5, 5));
		controlPanel.add(pPipeline);
		controlPanel.add(pInstructions);

		jCb.add ( lChoose);
		jCb.add ( jbComp);
		add ( jCb, BorderLayout.NORTH);
		add ( controlPanel, BorderLayout.CENTER);
	}

	public void itemStateChanged(ItemEvent arg0) {
		String sComp = jbComp.getItemAt( jbComp.getSelectedIndex ( )).toString();
		if ( sComp.equals ( "0")) sComp = null;
//System.out.println ( "itemStateChanged de SuperescalarPanel " + sComp);
		pPipeline.setPipeline( sComp);
		pInstructions.setPipeline ( sComp);
		pPipeline.refreshDisplay ( sSim, pProc);
		pInstructions.refreshDisplay ( sSim, pProc);
	}

	public void refreshDisplay(Simulator parSim, Processor parProc) {
		pPipeline.refreshDisplay ( sSim, pProc);
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
	PipelinePanel pPipeline;
	InstructionsPanel pInstructions;
	Simulator sSim;
	Processor pProc;
}
