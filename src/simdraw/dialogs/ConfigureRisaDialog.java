package simdraw.dialogs;

import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Insets;
import java.awt.Component;
import java.awt.GridBagConstraints;

import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import platform.Lang;
import platform.Platform;
import processor.Processor;
import processor.RodadaDeSimulacaoRisa;
import simulator.Simulator;
import util.Define;
import simdraw.MIPSApp;

public class ConfigureRisaDialog extends JDialog  implements ActionListener, Define {
	
	public ConfigureRisaDialog(MIPSApp owner, Simulator s, Processor p, String sPathName) {
		super();
		setTitle(Lang.iLang==ENGLISH?Lang.msgsGUI[288]:Lang.msgsGUI[287]);
		setLocation(new Point ( 50,50));
		pProc = p;
		sSim = s;
		filePath = Platform.treatPathNames(Processor.getProcessorName()+Platform.getSeparatorPath()+"programs") + Platform.getSeparatorPath()+"_dlxBin.bin";
		pathName = sPathName;
		fileTarget = "programs"+ Platform.getSeparatorPath() + "_dlxBin.bin";
		
		Container c = getContentPane();
		panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		
		restricoes.insets = new Insets(3,3,3,3);
		lRisa = new JLabel(Lang.iLang==ENGLISH?Lang.msgsGUI[290]:Lang.msgsGUI[289]);
		cbnorisa = new JCheckBox(Lang.iLang==ENGLISH?Lang.msgsGUI[292]:Lang.msgsGUI[291]);
		cbrisa0 = new JCheckBox("rISA-0");
		cbrisa1 = new JCheckBox("rISA-1");
		cbrisa2 = new JCheckBox("rISA-2");
		cbrisa3 = new JCheckBox("rISA-3");
		cbrisa4 = new JCheckBox("rISA-4");
		cbrecrisa = new JCheckBox("rec-rISA");
		cbcrisa = new JCheckBox(Lang.iLang==ENGLISH?Lang.msgsGUI[299]:Lang.msgsGUI[298]);
		confrec = new JButton(Lang.iLang==ENGLISH?Lang.msgsGUI[301]:Lang.msgsGUI[300]);
		confrec.setEnabled(false);
		restricoes.gridwidth = 2;
		addGridBag(panel, lRisa, 0, 1);
		restricoes.gridwidth = 1;
		restricoes.anchor = GridBagConstraints.WEST;
		restricoes.insets = new Insets(3,60,3,3);
		addGridBag(panel, cbnorisa, 0, 2);
		addGridBag(panel, cbrisa0, 0, 3);
		addGridBag(panel, cbrisa1, 0, 4);
		restricoes.insets = new Insets(3,20,3,3);
		addGridBag(panel, cbrisa3, 1, 2);
		addGridBag(panel, cbrisa4, 1, 3);
		addGridBag(panel, cbrecrisa, 1, 4);
		restricoes.insets = new Insets(3,60,20,3);
		addGridBag(panel, cbrisa2, 0, 5);
		addGridBag(panel, cbcrisa, 0, 6);
		restricoes.insets = new Insets(3,20,20,3);
		addGridBag(panel, confrec, 1, 6);
		cbnorisa.addActionListener(this);
		cbrisa0.addActionListener(this);
		cbrisa1.addActionListener(this);
		cbrisa2.addActionListener(this);
		cbrisa3.addActionListener(this);
		cbrisa4.addActionListener(this);
		cbrecrisa.addActionListener(this);
		cbcrisa.addActionListener(this);
		confrec.addActionListener(this);
		
		cbgraphic = new JCheckBox(Lang.iLang==ENGLISH?Lang.msgsGUI[303]:Lang.msgsGUI[302]);
		confgra = new JButton(Lang.iLang==ENGLISH?Lang.msgsGUI[305]:Lang.msgsGUI[304]);
		restricoes.insets = new Insets(3,60,20,3);
		addGridBag(panel, cbgraphic, 0, 7);
		restricoes.anchor = GridBagConstraints.CENTER;
		restricoes.insets = new Insets(3,20,20,3);
		addGridBag(panel, confgra, 1, 7);
		confgra.setEnabled(false);
		cbgraphic.addActionListener(this);
		confgra.addActionListener(this);
		
		ok = new JButton("Ok");
		restricoes.insets = new Insets(3,3,3,3);
		addGridBag(panel, ok, 0, 8);
		ok.addActionListener(this);
		cancel = new JButton(Lang.iLang==ENGLISH?Lang.msgsGUI[210]:Lang.msgsGUI[215]);
		addGridBag(panel, cancel, 1, 8);
		cancel.addActionListener(this);
		
		c.add(panel);
	}
	
	public void actionPerformed(ActionEvent arg0) {
		if ( arg0.getSource() == (Object) ok) {
			if (cbrisa0.isSelected() == false && cbrisa1.isSelected() == false
					&& cbrisa2.isSelected() == false && cbrisa3.isSelected() == false
					&& cbrisa4.isSelected() == false && cbcrisa.isSelected() == false
					&& cbrecrisa.isSelected() == false){
				cbnorisa.setSelected(true);
			}
			// simula e pega os resultados
			if (cbnorisa.isSelected()){
//System.out.println ( "risa[0] = true");
				risa[0] = true;
			}else{
				risa[0] = false;
			}
			if (cbrisa0.isSelected()){
//System.out.println ( "risa[1] = true");
				risa[1] = true;
			}else{
				risa[1] = false;
			}
			if (cbrisa1.isSelected()){
//System.out.println ( "risa[2] = true");
				risa[2] = true;
			}else{
				risa[2] = false;
			}
			if (cbrisa2.isSelected()){
//System.out.println ( "risa[3] = true");
				risa[3] = true;
			}else{
				risa[3] = false;
			}
			if (cbrisa3.isSelected()){
//System.out.println ( "risa[4] = true");
				risa[4] = true;
			}else{
				risa[4] = false;
			}
			if (cbrisa4.isSelected()){
//System.out.println ( "risa[5] = true");
				risa[5] = true;
			}else{
				risa[5] = false;
			}
			if (cbcrisa.isSelected()){
//System.out.println ( "risa[6] = true");
				risa[6] = true;
			}else{
				risa[6] = false;
			}
			if (cbrecrisa.isSelected()){
				risa[7] = true;
//System.out.println ( "risa[7] = true");
			}else{
				risa[7] = false;
			}
			if (cbcrisa.isSelected() && cr == null){
				JOptionPane.showMessageDialog(this,Lang.iLang==ENGLISH?Lang.msgsGUI[350]:Lang.msgsGUI[349]);
			}else{
				dispose();
			}
		}else if(arg0.getSource() == (Object) cancel){
			cbgraphic.setSelected(false);
			dispose();
		}else if(arg0.getSource() == (Object) cbcrisa){
			if (cbcrisa.isSelected()){
				confrec.setEnabled(true);
			}else{
				confrec.setEnabled(false);
			}
		}else if(arg0.getSource() == (Object) cbgraphic){
			if (cbgraphic.isSelected()){
				confgra.setEnabled(true);
			}else{
				confgra.setEnabled(false);
			}
		}else if(arg0.getSource() == (Object) confrec){
			if(cr == null){
				cr = new RecrISADialog(this, sSim, pProc);
				cr.setModal(true);
				cr.pack();
			}
			if(cr.isVisible() == false){
				cr.setVisible(true);
				cr.setModal(true);
			}
		}else if(arg0.getSource() == (Object) confgra){
			if(cg == null){
				cg = new GraphicDialog(this, sSim, pProc);
				cg.setModal(true);
				cg.pack();
			}
			if(cg.isVisible() == false){
				cg.setVisible(true);
				cg.setModal(true);
			}
		}
	}
	
	public boolean[] getSelectedRisas(){
		return risa;
	}
	
	public boolean getCBGraphicIsSelected(){
		if (cbgraphic.isSelected() == true){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean[] getGraphicOptions(){
		boolean[] values = {true, true, true, true, true, true};
		if (cg != null){
			values = ((GraphicDialog) cg).getValues();
		}
		return values;
	}
	
	void addGridBag(JPanel pnl, Component obj, int x, int y)
	{
		restricoes.gridx = x;
		restricoes.gridy = y;
		pnl.add(obj, restricoes);
	}
	
	JButton ok, cancel, confrec, confgra;
	Simulator sSim;
	Processor pProc;
	JLabel lRisa, lb;
	JPanel panel;
	GridBagConstraints restricoes = new GridBagConstraints();
	JCheckBox cbnorisa, cbrisa0, cbrisa1, cbrisa2, cbrisa3,
					  cbrisa4, cbrecrisa, cbgraphic, cbcrisa;
	int cont = 0, maxcont = 0;
	public JDialog cr, cg, sg;
	float[][] resultados = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
	String pathName, filePath, fileTarget;
	RodadaDeSimulacaoRisa rod;
	boolean[] risa = {false, false, false, false, false, false, false, false};
}
