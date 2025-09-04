
package simdraw.dialogs;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JCheckBox;

import platform.Lang;
import processor.Processor;
import simulator.Simulator;
import util.Define;

public class GraphicDialog extends JDialog  implements ActionListener, Define {
	
	public GraphicDialog(JDialog owner, Simulator parSim, Processor parProc) throws HeadlessException {
		super();
		setTitle(Lang.iLang==ENGLISH?Lang.msgsGUI[305]:Lang.msgsGUI[304]);
		setLocation(new Point ( 50,50));
		sSim = parSim;
		pProc = parProc;
		
		Container c = getContentPane();
		panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		
		lInfo = new JLabel(Lang.iLang==ENGLISH?Lang.msgsGUI[319]:Lang.msgsGUI[318]);
		restricoes.gridwidth = 2;
		restricoes.insets = new Insets(3,3,3,3);
		addGridBag(panel, lInfo, 0, 0);
		cboverflow = new JCheckBox("Overflow", true);
		restricoes.anchor = GridBagConstraints.WEST;
		restricoes.gridwidth = 1;
		addGridBag(panel, cboverflow, 0, 1);
		cbhandling = new JCheckBox(Lang.iLang==ENGLISH?Lang.msgsGUI[321]:Lang.msgsGUI[293], true);
		addGridBag(panel, cbhandling, 0, 2);
		cbsmallblocks = new JCheckBox(Lang.iLang==ENGLISH?Lang.msgsGUI[322]:Lang.msgsGUI[294], true);
		addGridBag(panel, cbsmallblocks, 0, 3);
		cbinstructions = new JCheckBox(Lang.iLang==ENGLISH?Lang.msgsGUI[323]:Lang.msgsGUI[295], true);
		addGridBag(panel, cbinstructions, 1, 1);
		cbexecution = new JCheckBox(Lang.iLang==ENGLISH?Lang.msgsGUI[324]:Lang.msgsGUI[296], true);
		addGridBag(panel, cbexecution, 1, 2);
		cbtime = new JCheckBox(Lang.iLang==ENGLISH?Lang.msgsGUI[326]:Lang.msgsGUI[325], true);
		addGridBag(panel, cbtime, 1, 3);
		
		ok = new JButton("Ok");
		restricoes.anchor = GridBagConstraints.CENTER;
		addGridBag(panel, ok, 0, 5);
		ok.addActionListener(this);
		cancel = new JButton(Lang.iLang==ENGLISH?Lang.msgsGUI[210]:Lang.msgsGUI[215]);
		addGridBag(panel, cancel, 1, 5);
		cancel.addActionListener(this);
		
		c.add(panel);
	}
	
	public void actionPerformed(ActionEvent arg0) {
		if ( arg0.getSource() == (Object) ok) {
			dispose();
		}else if(arg0.getSource() == (Object) cancel){
			dispose();
		}
	}
	
	boolean[] getValues(){
		boolean[] values = {false, false, false, false, false, false};
		if (cboverflow.isSelected()){
			values[0] = true;
		}else{
			values[0] = false;
		}
		if (cbhandling.isSelected()){
			values[1] = true;
		}else{
			values[1] = false;
		}
		if (cbsmallblocks.isSelected()){
			values[2] = true;
		}else{
			values[2] = false;
		}
		if (cbinstructions.isSelected()){
			values[3] = true;
		}else{
			values[3] = false;
		}
		if (cbexecution.isSelected()){
			values[4] = true;
		}else{
			values[4] = false;
		}
		if (cbtime.isSelected()){
			values[5] = true;
		}else{
			values[5] = false;
		}
		return values;
	}
	
	void addGridBag(JPanel pnl, Component obj, int x, int y)
	{
		restricoes.gridx = x;
		restricoes.gridy = y;
		pnl.add(obj, restricoes);
	}
	
	Simulator sSim;
	Processor pProc;
	JPanel panel;
	GridBagConstraints restricoes = new GridBagConstraints();
	JButton ok, cancel;
	JLabel lInfo;
	JCheckBox cboverflow, cbhandling, cbsmallblocks, cbinstructions, cbexecution, cbtime;
}
