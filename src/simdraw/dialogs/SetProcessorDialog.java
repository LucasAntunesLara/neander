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
public class SetProcessorDialog extends JDialog implements ItemListener, ActionListener, Define {

	/**
	 * @throws java.awt.HeadlessException
	 */
	public SetProcessorDialog(Simulator parSim, Processor parProc) throws HeadlessException {
		super();
		sSim = parSim;
		pProc = parProc;
		spAux = pProc.getStatus ( );
		
		sTypeAttribs [ 0] = Lang.iLang==ENGLISH?Lang.msgsGUI[160]:Lang.msgsGUI[165];
		sTypeAttribs [ 1] = Lang.iLang==ENGLISH?Lang.msgsGUI[161]:Lang.msgsGUI[166];
		sTypeAttribs [ 2] = Lang.iLang==ENGLISH?Lang.msgsGUI[162]:Lang.msgsGUI[167];

		BorderLayout bL = new BorderLayout ( 5, 5);
		Container c = getContentPane();
		c.setLayout ( bL);
		JPanel a = new JPanel ( );
		a.setLayout ( new GridLayout ( 4, 2, 10, 10));
		JPanel b = new JPanel ( );	
		// Espaços para permitir a exibicao do status
		lType = new JLabel ( (Lang.iLang==ENGLISH?Lang.msgsGUI[151]:Lang.msgsGUI[156]));
		cbType = new JComboBox ( sTypeAttribs);
		cbType.addItemListener ( this);
		lAttribs = new JLabel ( Lang.iLang==ENGLISH?Lang.msgsGUI[154]:Lang.msgsGUI[159]);
		cbAttribs = new JComboBox ( );
		vLabel = new JLabel ( Lang.iLang==ENGLISH?Lang.msgsGUI[153]:Lang.msgsGUI[158]);
		vText = new JTextField ( );
		okButton = new JButton ( "OK");
		okButton.addActionListener(this);
		cancelButton = new JButton ( Lang.iLang==ENGLISH?Lang.msgsGUI[210]:Lang.msgsGUI[215]);
		cancelButton.addActionListener(this);
		lStatus = new JLabel();
		
		int iNelem = spAux.getNelems ( );
		for ( int i = 0; i < iNelem; i ++) {
			String sName = spAux.traverse( i).getName ( );
			if ( sName.startsWith( "TDCF_")) {
				cbAttribs.addItem ( sName);
			}
		}
		if ( cbAttribs.getItemCount() > 0) {
			String sComp = cbAttribs.getItemAt( 0).toString();
			if ( sComp !=null) {
				long lValue = pProc.get ( sComp, 	STATUSorCONF);
				vText.setText ( new Long (lValue).toString());
			}
		}

		a.add ( lType);
		a.add ( cbType);
		a.add ( lAttribs);
		a.add ( cbAttribs);
		a.add ( vLabel);
		a.add ( vText);
		a.add ( lStatus);
		b.add ( okButton);
		b.add ( cancelButton);
		c.add ( a, BorderLayout.NORTH);
		c.add ( b, BorderLayout.SOUTH);
		setLocation(new Point ( 50,50));
		cbAttribs.addItemListener ( this);
	}

	public void actionPerformed(ActionEvent arg0) {
		if ( arg0.getSource() == (Object) okButton) {
			int indexType = cbType.getSelectedIndex ( );
			String sComp;
			String sValue = vText.getText ( );
			if (sValue.length() == 0) return;
		
			if ( cbAttribs.getItemCount() > 0) {
				sComp = cbAttribs.getItemAt( cbAttribs.getSelectedIndex ( )).toString();
			} else return;
		
			if ( indexType == 2) {
				pProc.set ( sComp, 	STRING, sValue);
			} else {
					long lValue = SistNum.getValue ( sValue);
					if ( 	Processor.getMessageError ( ).equalsIgnoreCase(Lang.msgsGUI[183]) ||
							Processor.getMessageError ( ).equalsIgnoreCase(Lang.msgsGUI[193])) {
						Processor.setMessageError ( "");
						lStatus.setText(Lang.iLang==ENGLISH?Lang.msgsGUI[181]:Lang.msgsGUI[191]);
						vText.setText("");
						return;						
					}
					else pProc.set ( sComp, 	indexType == 0 ? STATUSorCONF : FIELD, (int) lValue);
			}
			lStatus.setText ( sComp+" = "+sValue+" (OK)");
			//System.out.println ( "Pressionou OK: "+indexType+","+sComp+","+sValue);
		} else {
			lStatus.setText("");
			setVisible ( false);			
		}
		vText.setText("");
	}
	
	public void itemStateChanged(ItemEvent arg0) {
		String sComp, sValue;
		int indexType = cbType.getSelectedIndex ( );
		if ( indexType == 0) spAux = pProc.getStatus ( );
		else if ( indexType == 1) spAux = pProc.getFields ( );
		else spAux = pProc.getFieldStrings();
		
		if ( arg0.getSource() == (Object) cbType) {
			cbAttribs.removeAllItems();
			int iNelem = spAux.getNelems ( );
			for ( int i = 0; i < iNelem; i ++) {
				String sName = spAux.traverse( i).getName ( );
				if ( sName.startsWith( "TDCF_")) {
					cbAttribs.addItem ( sName);
				}
			}
		}
		
		if ( cbAttribs.getItemCount() > 0) {
			sComp = cbAttribs.getItemAt( cbAttribs.getSelectedIndex ( )).toString();
			if ( sComp !=null) {
				if ( indexType == 2) {
					sValue = pProc.getString ( sComp, 	STRING);
				} else {
					long lValue = pProc.get ( sComp, 	indexType == 0 ? STATUSorCONF : FIELD);
					sValue = new Long (lValue).toString();
				}
				vText.setText ( sValue);
			}
		} else vText.setText ( "");
	}

	String sTypeAttribs [ ] = { "", "", ""};
	JLabel lType, lAttribs, vLabel;
	JComboBox cbType, cbAttribs;
	JButton okButton, cancelButton;
	JTextField vText;
	JLabel lStatus;
	Simulator sSim;
	Processor pProc;
	SetPort spAux;
}
