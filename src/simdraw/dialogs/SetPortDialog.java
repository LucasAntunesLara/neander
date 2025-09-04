/*
 * Created on 05/06/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package simdraw.dialogs;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


import ccomb.Circuit;

import datapath.Datapath;

import platform.Lang;
import ports.Port;
import ports.SetPort;
import processor.Processor;

import simulator.Simulator;

import util.Define;
import util.SistNum;

/**
 * @author Sandro
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SetPortDialog extends JDialog implements ItemListener, ActionListener, Define {

	/**
	 * @throws java.awt.HeadlessException
	 */
	public SetPortDialog(Simulator parSim, Processor parProc) throws HeadlessException {
		super();
		sSim = parSim;
		pProc = parProc;
		Datapath dtp = pProc.getDatapath ( );
		
		BorderLayout bL = new BorderLayout ( 5, 5);
		Container c = getContentPane();
		c.setLayout ( bL);
		JPanel a = new JPanel ( );
		a.setLayout ( new GridLayout ( 5, 2, 10, 10));
		JPanel b = new JPanel ( );		
		lComp = new JLabel ( Lang.iLang==ENGLISH?Lang.msgsGUI[150]:Lang.msgsGUI[155]);
		cbComp = new JComboBox ( );
		cbComp.addItemListener ( this);
		lType = new JLabel ( Lang.iLang==ENGLISH?Lang.msgsGUI[151]:Lang.msgsGUI[156]);
		cbType = new JComboBox ( sTypePorts);
		cbType.addItemListener ( this);
		lPort = new JLabel ( Lang.iLang==ENGLISH?Lang.msgsGUI[152]:Lang.msgsGUI[157]);
		cbPort = new JComboBox ( );
		vLabel = new JLabel ( Lang.iLang==ENGLISH?Lang.msgsGUI[153]:Lang.msgsGUI[158]);
		vText = new JTextField ( );
		okButton = new JButton ( "OK");
		okButton.addActionListener(this);
		cancelButton = new JButton ( Lang.iLang==ENGLISH?Lang.msgsGUI[210]:Lang.msgsGUI[215]);
		cancelButton.addActionListener(this);
		lStatus = new JLabel();
		
		int iNelem = dtp.scEsquematico.getNelems ( );
		for ( int i = 0; i < iNelem; i ++) {
			Circuit cComponent = ( Circuit) dtp.scEsquematico.traverse ( i);
			cbComp.addItem ( cComponent.getName ( ));
		}
		
		a.add ( lComp);
		a.add ( cbComp);
		a.add ( lType);
		a.add ( cbType);
		a.add ( lPort);
		a.add ( cbPort);
		a.add ( vLabel);
		a.add ( vText);
		a.add ( lStatus);
		b.add ( okButton);
		b.add ( cancelButton);
		c.add ( a, BorderLayout.NORTH);
		c.add ( b, BorderLayout.SOUTH);
		setLocation(new Point ( 50,50));
	}

	public void actionPerformed(ActionEvent arg0) {
		if ( arg0.getSource() == (Object) okButton) {
			Datapath dtp = pProc.getDatapath();
			String sComp = lPort.getText( );
			String sPort = cbPort.getItemAt( cbPort.getSelectedIndex ( )).toString();
			int indexType = cbType.getSelectedIndex ( );
			String sValue = vText.getText ( );
			long lValue;
			try {
				lValue = SistNum.getValue ( sValue);
				dtp.execute ( sComp, 	SET, sPort, indexType, lValue);
			} catch ( Exception e) {
				lStatus.setText(Lang.iLang==ENGLISH?Lang.msgsGUI[181]:Lang.msgsGUI[191]);
				return;
			}
			vText.setText("");
			lStatus.setText (sComp+"."+sPort+" = "+lValue+" (OK)");
		} else {
			vText.setText("");
			lStatus.setText("");
			setVisible ( false);			
		}
	}

	public void itemStateChanged(ItemEvent arg0) {
		Datapath dtp = pProc.getDatapath();
		int iNelem;
		
		int index = cbComp.getSelectedIndex ( );
		String sTmp = (String) cbComp.getItemAt ( index).toString ( );
		int indexType = cbType.getSelectedIndex ( );
		lPort.setText ( sTmp);
		Circuit cTmp = dtp.search(sTmp);
		SetPort sp = cTmp.getSetPort ( indexType);
		if ( sp != null) iNelem = sp.getNelems ( );
		else iNelem = 0;
		cbPort.removeAllItems();
		for ( int i = 0; i < iNelem; i ++) {
			Port pPort = ( Port) sp.traverse ( i);
			cbPort.addItem ( pPort.getName ( ));
		}		
		if ( iNelem == 0) {
			cbPort.setEnabled ( false);
			vText.setEnabled ( false);
		} else {
			cbPort.setEnabled ( true);
			vText.setEnabled ( true);			
		}
	}
	
	String sTypePorts [ ] = { "IN", "CONTROL", "STATUS or CONFIG."};
	JLabel lComp, lType, lPort, vLabel;
	JComboBox cbComp, cbType, cbPort;
	JButton okButton, cancelButton;
	JTextField vText;
	JLabel lStatus;
	Simulator sSim;
	Processor pProc;
}
