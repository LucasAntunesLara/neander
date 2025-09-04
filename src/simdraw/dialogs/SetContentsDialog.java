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


import platform.Lang;
import processor.Processor;
import simulator.Simulator;
import util.Define;
import util.SistNum;
import ccomb.Circuit;
import cseq.Sequential;
import datapath.Datapath;

/**
 * @author Sandro
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SetContentsDialog extends JDialog implements ItemListener, ActionListener, Define {

	/**
	 * @throws java.awt.HeadlessException
	 */
	public SetContentsDialog(Simulator parSim, Processor parProc) throws HeadlessException {
		super();
		sSim = parSim;
		pProc = parProc;
		Datapath dtp = pProc.getDatapath ( );
		
		BorderLayout bL = new BorderLayout ( 5, 5);
		Container c = getContentPane();
		c.setLayout ( bL);
		JPanel a = new JPanel ( );
		a.setLayout ( new GridLayout ( 6, 2, 10, 10));
		JPanel b = new JPanel ( );		
		// Espaços para permitir a exibicao do status
		lComp = new JLabel ( (Lang.iLang==ENGLISH?Lang.msgsGUI[150]:Lang.msgsGUI[155])+"                    ");
		cbComp = new JComboBox ( );
		cbComp.addItemListener ( this);
		lX = new JLabel ( "     X");
		xText = new JTextField ( );
		lY = new JLabel ( "     Y");
		yText = new JTextField ( );
		lZ = new JLabel ( "     Z");
		zText = new JTextField ( );
		lbValue = new JLabel ( Lang.iLang==ENGLISH?Lang.msgsGUI[153]:Lang.msgsGUI[158]);
		txValue = new JTextField ( );
		okButton = new JButton ( "OK");
		okButton.addActionListener(this);
		xText.setEnabled ( false);
		yText.setEnabled ( false);
		zText.setEnabled ( false);		
		cancelButton = new JButton ( Lang.iLang==ENGLISH?Lang.msgsGUI[210]:Lang.msgsGUI[215]);
		cancelButton.addActionListener(this);
		lStatus = new JLabel();
		
		int iNelem = dtp.scEsquematico.getNelems ( );
		for ( int i = 0; i < iNelem; i ++) {
			Circuit cComponent = ( Circuit) dtp.scEsquematico.traverse ( i);
			String sClass = cComponent.getClass ( ).getName ( );
			if ( sClass.startsWith("cseq") == true) cbComp.addItem ( cComponent.getName ( ));
		}
		
		a.add ( lComp);
		a.add ( cbComp);
		a.add ( lX);
		a.add ( xText);
		a.add ( lY);
		a.add ( yText);
		a.add ( lZ);
		a.add ( zText);
		a.add ( lbValue);
		a.add ( txValue);
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
			String sComp = cbComp.getItemAt( cbComp.getSelectedIndex ( )).toString();
			int x = 0, y = 0, z = 0;
			long lValue = 0;
			boolean error = false;

			String sValue = xText.getText ( );
			try {
				if (sValue.length() > 0) x = new Integer ( sValue).intValue();
			} catch ( Exception e) {
				error = true;
			}
			sValue = yText.getText ( );
			try {
				if (sValue.length() > 0) y = new Integer ( sValue).intValue();
			} catch ( Exception e) {
				error = true;
			}
			sValue = zText.getText ( );
			try {
				if (sValue.length() > 0) z = new Integer ( sValue).intValue();
			} catch ( Exception e) {
				error = true;
			}
			sValue = txValue.getText ( );
			try {
				if (sValue.length() > 0) lValue = SistNum.getValue ( sValue);
			} catch ( Exception e) { 
				error = true;
			}
			if ( ! error) {
				dtp.execute ( sComp, 	SET, x, y, lValue);
				String sStatus = sComp;
				sStatus += (xText.isEnabled()?"["+xText.getText()+"]":"");
				sStatus += (yText.isEnabled()?"["+yText.getText()+"]":"");
				sStatus += (zText.isEnabled()?"["+zText.getText()+"]":"");
				sStatus += " = "+lValue+" (OK)";
				lStatus.setText (sStatus);
			}
		} else {
			lStatus.setText("");
			setVisible ( false);			
		}
		xText.setText("");
		yText.setText("");
		zText.setText("");
		txValue.setText("");
	}

	public void itemStateChanged(ItemEvent arg0) {
		Datapath dtp = pProc.getDatapath();
		int x, y, z;
		
		int index = cbComp.getSelectedIndex ( );
		String sTmp = (String) cbComp.getItemAt ( index).toString ( );
		Sequential cTmp = (Sequential) dtp.search(sTmp);
		x = cTmp.cConteudo.getSizeX ( );
		y = cTmp.cConteudo.getSizeY ( );
		z = cTmp.cConteudo.getSizeZ ( );
		if ( x > 0) xText.setEnabled ( true); else xText.setEnabled ( false);
		if ( y > 0) yText.setEnabled ( true); else yText.setEnabled ( false);
		if ( z > 0) zText.setEnabled ( true); else zText.setEnabled ( false);
	}
	
	JLabel lComp, lX, lY, lZ, lbValue;
	JComboBox cbComp;
	JButton okButton, cancelButton;
	JTextField cText, xText, yText, zText, txValue;
	JLabel lStatus;
	Simulator sSim;
	Processor pProc;
}
