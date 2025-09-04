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


import platform.Lang;
import processor.Processor;
import simulator.Simulator;
import util.Define;
import ccomb.Circuit;
import datapath.Datapath;

/**
 * @author Sandro
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ExecuteDialog extends JDialog implements ItemListener, ActionListener, Define {

	/**
	 * @throws java.awt.HeadlessException
	 */
	public ExecuteDialog(Simulator parSim, Processor parProc) throws HeadlessException {
		super();
		sSim = parSim;
		pProc = parProc;
		Datapath dtp = pProc.getDatapath ( );
		
		BorderLayout bL = new BorderLayout ( 5, 5);
		Container c = getContentPane();
		c.setLayout ( bL);
		JPanel a = new JPanel ( );
		a.setLayout ( new GridLayout ( 2, 2, 10, 10));
		JPanel b = new JPanel ( );		
		lComp = new JLabel ( Lang.iLang==ENGLISH?Lang.msgsGUI[140]:Lang.msgsGUI[145]);
		cbComp = new JComboBox ( );
		cbComp.addItemListener ( this);
		lMethod = new JLabel ( Lang.iLang==ENGLISH?Lang.msgsGUI[141]:Lang.msgsGUI[146]);
		cbMethod = new JComboBox ( );
		okButton = new JButton ( "OK");
		okButton.addActionListener(this);
		
		int iNelem = dtp.scEsquematico.getNelems ( );
		for ( int i = 0; i < iNelem; i ++) {
			Circuit cComponent = ( Circuit) dtp.scEsquematico.traverse ( i);
			cbComp.addItem ( cComponent.getName ( ));
		}
		
		a.add ( lComp);
		a.add ( cbComp);
		a.add ( lMethod);
		a.add ( cbMethod);
		b.add ( okButton);
		c.add ( a, BorderLayout.NORTH);
		c.add ( b, BorderLayout.SOUTH);
		setLocation(new Point ( 50,50));
	}

	public void actionPerformed(ActionEvent arg0) {
		Datapath dtp = pProc.getDatapath();
		int iMethod;
		String sComp = cbComp.getItemAt( cbComp.getSelectedIndex ( )).toString();
		String sMethod = cbMethod.getItemAt( cbMethod.getSelectedIndex ( )).toString();
		if ( sMethod.equals ( "BEHAVIOR") == true) iMethod = 0;
		else if ( sMethod.equals ( "READ") == true) iMethod = 1;
		else iMethod = 2;	// READ
		try {
			dtp.execute ( sComp, 	iMethod);
		} catch ( Exception e) {
			return;
		}
		setVisible ( false);
		System.out.println ( "Pressionou OK: "+sComp+","+iMethod+","+sMethod);
	}

	public void itemStateChanged(ItemEvent arg0) {
		Datapath dtp = pProc.getDatapath();
		int iNelem = 0;
		
		int index = cbComp.getSelectedIndex ( );
		String sTmp = (String) cbComp.getItemAt ( index).toString ( );
		Circuit cTmp = dtp.search(sTmp);
		String sClass = cTmp.getClass ( ).getName ( );
		cbMethod.removeAllItems ( );
		if ( sClass.startsWith("cseq") == true) {
			cbMethod.addItem ( sMethods [ 1]);
			cbMethod.addItem ( sMethods [ 2]);
		} else {
			cbMethod.addItem ( sMethods [ 0]);
		}
	}
	
	String sMethods [] = { "BEHAVIOR", "READ", "WRITE"};
	JLabel lComp, lMethod;
	JComboBox cbComp, cbMethod;
	JButton okButton;
	Simulator sSim;
	Processor pProc;
}
