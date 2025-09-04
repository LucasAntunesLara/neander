/*
 * Created on 01/05/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package simdraw.dialogs;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;


import platform.Lang;
import processor.Processor;
import simulator.Simulator;
import util.Define;

/**
 * @author unknown
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SimulateDialog extends JDialog implements Define, ActionListener {

	/**
	 * @throws java.awt.HeadlessException
	 */
	public SimulateDialog(Simulator parSim, Processor parProc) throws HeadlessException {
		super();
		sSim = parSim;
		pProc = parProc;
		Container c = getContentPane();
		c.setLayout ( new FlowLayout ( FlowLayout.LEFT));
		String sTmp = Lang.iLang==ENGLISH?Lang.msgsGUI[170]:Lang.msgsGUI[175];
		label = new JLabel ( sTmp);
		c.add ( label);
		text = new JTextField ("100",10);
		c.add ( text);
		button = new JButton ( "OK");
		c.add ( button);
		button.addActionListener( this);
		setLocation(new Point ( 50,50));
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		String s = text.getText();
		int iUnits;
		try {
			iUnits = Integer.parseInt ( s);
		} catch ( Exception e) {
			System.out.println ( "Argumento nao numerico!");
			return;
		}
		for ( int k = 0; k < iUnits; k ++) sSim.Simulate ( pProc);
		setVisible ( false);
	}

	JLabel label;
	JTextField text;
	JButton button;
	Simulator sSim;
	Processor pProc;
}
