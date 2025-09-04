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

import processor.Processor;
import simulator.Simulator;

/**
 * @author unknown
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SimFileDialog extends JDialog implements ActionListener {

	/**
	 * @throws java.awt.HeadlessException
	 */
	public SimFileDialog(Simulator parSim, Processor parProc) throws HeadlessException {
		super();
		sSim = parSim;
		pProc = parProc;
		Container c = getContentPane();
		c.setLayout ( new FlowLayout ( FlowLayout.LEFT));
		label = new JLabel ( "File name: ");
		c.add ( label);
		text = new JTextField ("program.txt",10);
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
		sFileName = text.getText();
		System.out.println ( "1."+sFileName);
		setVisible ( false);
	}

	public String getFileName ( ) {
		System.out.println ( "2."+sFileName);
		return ( sFileName);
	}
	
	JLabel label;
	JTextField text;
	JButton button;
	Simulator sSim;
	Processor pProc;
	String sFileName;
}
