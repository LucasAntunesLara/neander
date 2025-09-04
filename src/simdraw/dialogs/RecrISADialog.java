package simdraw.dialogs;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridBagConstraints;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import platform.Lang;
import processor.Processor;
import processor.MIPS;
import simulator.Simulator;
import util.Define;


public class RecrISADialog extends JDialog  implements ActionListener, Define {
	//Lang.iLang==ENGLISH?Lang.msgsGUI[276]:Lang.msgsGUI[281]
	public RecrISADialog(JDialog owner, Simulator parSim, Processor parProc) throws HeadlessException {
		super();
		setTitle(Lang.iLang==ENGLISH?Lang.msgsGUI[307]:Lang.msgsGUI[306]);
		setLocation(new Point (50, 50));
		sSim = parSim;
		pProc = parProc;
		
		Container c = getContentPane();
		panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		
		lFormato = new JLabel(Lang.iLang==ENGLISH?Lang.msgsGUI[309]:Lang.msgsGUI[308]);
		risa8 = new JRadioButton("rISA_8ops", true);
		risa16 = new JRadioButton("rISA_16ops", false);
		bg = new ButtonGroup();
		bg.add(risa8);
		bg.add(risa16);
		restricoes.gridwidth = 4;
		restricoes.insets = new Insets(3,3,10,3);
		addGridBag(panel, lFormato, 0, 0);
		restricoes.gridwidth = 2;
		addGridBag(panel, risa8, 4, 0);
		risa8.addActionListener(this);
		addGridBag(panel, risa16, 6, 0);
		risa16.addActionListener(this);
		
		lInstrucoes = new JLabel(Lang.iLang==ENGLISH?Lang.msgsGUI[313]:Lang.msgsGUI[312]);
		cbadd = new JCheckBoxMenuItem("add");
		cbsll = new JCheckBoxMenuItem("sll");
		cbsrl = new JCheckBoxMenuItem("srl");
		cbsra = new JCheckBoxMenuItem("sra");
		cbsllv = new JCheckBoxMenuItem("sllv"); //repete
		cbsrlv = new JCheckBoxMenuItem("srlv"); //repete
		cbsrav = new JCheckBoxMenuItem("srav");
		cbjalr = new JCheckBoxMenuItem("jalr");
		cbjr = new JCheckBoxMenuItem("jr");
		cbmult = new JCheckBoxMenuItem("mult");
		cbmultu = new JCheckBoxMenuItem("multu");
		cbdiv = new JCheckBoxMenuItem("div");
		cbdivu = new JCheckBoxMenuItem("divu");
		cbaddu = new JCheckBoxMenuItem("addu");
		cbsub = new JCheckBoxMenuItem("sub");
		cbsubu = new JCheckBoxMenuItem("subu");
		cband = new JCheckBoxMenuItem("and");
		cbor = new JCheckBoxMenuItem("or");
		cbxor = new JCheckBoxMenuItem("xor");
		cbnor = new JCheckBoxMenuItem("nor");
		cbslt = new JCheckBoxMenuItem("slt");
		cbsltu = new JCheckBoxMenuItem("sltu");
		cblw = new JCheckBoxMenuItem("lw");
		cbsw = new JCheckBoxMenuItem("sw");
		cbaddi = new JCheckBoxMenuItem("addi");
		cbandi = new JCheckBoxMenuItem("andi");
		cbori = new JCheckBoxMenuItem("ori");
		cbbeq = new JCheckBoxMenuItem("beq");
		cbbltz = new JCheckBoxMenuItem("bltz");
		cbbgtz = new JCheckBoxMenuItem("bgtz");
		cbblez = new JCheckBoxMenuItem("blez");
		cbbne = new JCheckBoxMenuItem("bne");
		cbsllv2 = new JCheckBoxMenuItem("sllv"); //repete
		cbsrlv2 = new JCheckBoxMenuItem("srlv"); //repete
		cbslti = new JCheckBoxMenuItem("slti");
		cbj = new JCheckBoxMenuItem("j");
		cbjal = new JCheckBoxMenuItem("jal");
		//cbnop = new JCheckBoxMenuItem("nop");
		//cbBUBBLE = new JCheckBoxMenuItem("BUBBLE");
		//cbcm = new JCheckBoxMenuItem("cm");
		cblb = new JCheckBoxMenuItem("lb");
		cbsb = new JCheckBoxMenuItem("sb");
		cbrem = new JCheckBoxMenuItem("rem");
		cbsrli = new JCheckBoxMenuItem("srli");
		cbslli = new JCheckBoxMenuItem("slli");
		cbsrai = new JCheckBoxMenuItem("srai");
		cblwl = new JCheckBoxMenuItem("lwl");
		cblwr = new JCheckBoxMenuItem("lwr");
		cbswl = new JCheckBoxMenuItem("swl");
		cbswr = new JCheckBoxMenuItem("swr");
		//cbhlt = new JCheckBoxMenuItem("hlt");
		
		restricoes.insets = new Insets(3,3,3,3);
		restricoes.gridwidth = 10;
		addGridBag(panel, lInstrucoes, 0, 1);
		restricoes.gridwidth = 1;
		addGridBag(panel, cbadd, 0, 2);
		cbadd.addActionListener(this);
		addGridBag(panel, cbsll, 1, 2);
		cbsll.addActionListener(this);
		addGridBag(panel, cbsrl, 2, 2);
		cbsrl.addActionListener(this);
		addGridBag(panel, cbsra, 3, 2);
		cbsra.addActionListener(this);
		addGridBag(panel, cbsllv, 4, 2);
		cbsllv.addActionListener(this);
		addGridBag(panel, cbsrlv, 5, 2);
		cbsrlv.addActionListener(this);
		addGridBag(panel, cbsrav, 6, 2);
		cbsrav.addActionListener(this);
		addGridBag(panel, cbjalr, 7, 2);
		cbjalr.addActionListener(this);
		addGridBag(panel, cbjr, 8, 2);
		cbjr.addActionListener(this);
		addGridBag(panel, cbmult, 9, 2);
		cbmult.addActionListener(this);
		addGridBag(panel, cbmultu, 0, 3);
		cbmultu.addActionListener(this);
		addGridBag(panel, cbdiv, 1, 3);
		cbdiv.addActionListener(this);
		addGridBag(panel, cbdivu, 2, 3);
		cbdivu.addActionListener(this);
		addGridBag(panel, cbaddu, 3, 3);
		cbaddu.addActionListener(this);
		addGridBag(panel, cbsub, 4, 3);
		cbsub.addActionListener(this);
		addGridBag(panel, cbsubu, 5, 3);
		cbsubu.addActionListener(this);
		addGridBag(panel, cband, 6, 3);
		cband.addActionListener(this);
		addGridBag(panel, cbor, 7, 3);
		cbor.addActionListener(this);
		addGridBag(panel, cbxor, 8, 3);
		cbxor.addActionListener(this);
		addGridBag(panel, cbnor, 9, 3);
		cbnor.addActionListener(this);
		addGridBag(panel, cbslt, 0, 4);
		cbslt.addActionListener(this);
		addGridBag(panel, cbsltu, 1, 4);
		cbsltu.addActionListener(this);
		addGridBag(panel, cblw, 2, 4);
		cblw.addActionListener(this);
		addGridBag(panel, cbsw, 3, 4);
		cbsw.addActionListener(this);
		addGridBag(panel, cbaddi, 4, 4);
		cbaddi.addActionListener(this);
		addGridBag(panel, cbandi, 5, 4);
		cbandi.addActionListener(this);
		addGridBag(panel, cbori, 6, 4);
		cbori.addActionListener(this);
		addGridBag(panel, cbbeq, 7, 4);
		cbbeq.addActionListener(this);
		addGridBag(panel, cbbltz, 8, 4);
		cbbltz.addActionListener(this);
		addGridBag(panel, cbbgtz, 9, 4);
		cbbgtz.addActionListener(this);
		addGridBag(panel, cbblez, 0, 5);
		cbblez.addActionListener(this);
		addGridBag(panel, cbbne, 1, 5);
		cbbne.addActionListener(this);
		addGridBag(panel, cbsllv2, 2, 5);
		cbsllv2.addActionListener(this);
		addGridBag(panel, cbsrlv2, 3, 5);
		cbsrlv2.addActionListener(this);
		addGridBag(panel, cbslti, 4, 5);
		cbslti.addActionListener(this);
		addGridBag(panel, cbj, 5, 5);
		cbj.addActionListener(this);
		addGridBag(panel, cbjal, 6, 5);
		cbjal.addActionListener(this);
		//addGridBag(panel, cbnop, 7, 5);
		//cbnop.addActionListener(this);
		//addGridBag(panel, cbBUBBLE, 8, 5);
		//cbBUBBLE.addActionListener(this);
		//addGridBag(panel, cbcm, 9, 5);
		//cbcm.addActionListener(this);
		addGridBag(panel, cblb, 0, 6);
		cblb.addActionListener(this);
		addGridBag(panel, cbsb, 1, 6);
		cbsb.addActionListener(this);
		addGridBag(panel, cbrem, 2, 6);
		cbrem.addActionListener(this);
		addGridBag(panel, cbsrli, 3, 6);
		cbsrli.addActionListener(this);
		addGridBag(panel, cbslli, 4, 6);
		cbslli.addActionListener(this);
		addGridBag(panel, cbsrai, 5, 6);
		cbsrai.addActionListener(this);
		addGridBag(panel, cblwl, 6, 6);
		cblwl.addActionListener(this);
		addGridBag(panel, cblwr, 7, 5);
		cblwr.addActionListener(this);
		addGridBag(panel, cbswl, 8, 5);
		cbswl.addActionListener(this);
		addGridBag(panel, cbswr, 9, 5);
		cbswr.addActionListener(this);
		//addGridBag(panel, cbhlt, 0, 7);
		//cbhlt.addActionListener(this);
		
		restricoes.gridwidth = 3;
		ok = new JButton("Ok");
		addGridBag(panel, ok, 0, 8);
		ok.addActionListener(this);
		cancel = new JButton(Lang.iLang==ENGLISH?Lang.msgsGUI[210]:Lang.msgsGUI[215]);
		addGridBag(panel, cancel, 3, 8);
		cancel.addActionListener(this);
		clear = new JButton(Lang.iLang==ENGLISH?Lang.msgsGUI[317]:Lang.msgsGUI[316]);
		addGridBag(panel, clear, 6, 8);
		clear.addActionListener(this);
		
		c.add(panel);
	}
	
	public void actionPerformed(ActionEvent arg0) {
		if ( arg0.getSource() == (Object) ok) {
			if (cbadd.isSelected()){
				cont++;
			}if (cbsll.isSelected()){
				cont++;
			}if (cbsrl.isSelected()){
				cont++;
			}if (cbsra.isSelected()){
				cont++;
			}if (cbsllv.isSelected()){
				cont++;
			}if (cbsrlv.isSelected()){
				cont++;
			}if (cbsrav.isSelected()){
				cont++;
			}if (cbjalr.isSelected()){
				cont++;
			}if (cbjr.isSelected()){
				cont++;
			}if (cbmult.isSelected()){
				cont++;
			}if (cbmultu.isSelected()){
				cont++;
			}if (cbdiv.isSelected()){
				cont++;
			}if (cbdivu.isSelected()){
				cont++;
			}if (cbaddu.isSelected()){
				cont++;
			}if (cbsub.isSelected()){
				cont++;
			}if (cbsubu.isSelected()){
				cont++;
			}if (cband.isSelected()){
				cont++;
			}if (cbor.isSelected()){
				cont++;
			}if (cbxor.isSelected()){
				cont++;
			}if (cbnor.isSelected()){
				cont++;
			}if (cbslt.isSelected()){
				cont++;
			}if (cbsltu.isSelected()){
				cont++;
			}if (cblw.isSelected()){
				cont++;
			}if (cbsw.isSelected()){
				cont++;
			}if (cbaddi.isSelected()){
				cont++;
			}if (cbandi.isSelected()){
				cont++;
			}if (cbori.isSelected()){
				cont++;
			}if (cbbeq.isSelected()){
				cont++;
			}if (cbbltz.isSelected()){
				cont++;
			}if (cbbgtz.isSelected()){
				cont++;
			}if (cbblez.isSelected()){
				cont++;
			}if (cbbne.isSelected()){
				cont++;
			}if (cbsllv2.isSelected()){
				cont++;
			}if (cbsrlv2.isSelected()){
				cont++;
			}if (cbslti.isSelected()){
				cont++;
			}if (cbj.isSelected()){
				cont++;
			}if (cbjal.isSelected()){
				cont++;
			}/*if (cbnop.isSelected()){
				cont++;
			}if (cbBUBBLE.isSelected()){
				cont++;
			}if (cbcm.isSelected()){
				cont++;
			}*/if (cblb.isSelected()){
				cont++;
			}if (cbsb.isSelected()){
				cont++;
			}if (cbrem.isSelected()){
				cont++;
			}if (cbsrli.isSelected()){
				cont++;
			}if (cbslli.isSelected()){
				cont++;
			}if (cbsrai.isSelected()){
				cont++;
			}if (cblwl.isSelected()){
				cont++;
			}if (cblwr.isSelected()){
				cont++;
			}if (cbswl.isSelected()){
				cont++;
			}if (cbswr.isSelected()){
				cont++;
			}/*if (cbhlt.isSelected()){
				cont++;
			}*/
			if (cont > maxcont){
				String tmp = Lang.iLang==ENGLISH?Lang.msgsGUI[328]:Lang.msgsGUI[327];
				String tmp1 = Lang.iLang==ENGLISH?Lang.msgsGUI[330]:Lang.msgsGUI[329];
				JOptionPane.showMessageDialog(this,tmp + maxcont + tmp1);
			}else if (cont < maxcont){
				String tmp = Lang.iLang==ENGLISH?Lang.msgsGUI[332]:Lang.msgsGUI[331];
				String tmp1 = Lang.iLang==ENGLISH?Lang.msgsGUI[330]:Lang.msgsGUI[329];
				JOptionPane.showMessageDialog(this,tmp + maxcont + tmp1);
			}else{
				dispose();
			}
			cont = 0;
		}else if(arg0.getSource() == (Object) cancel){
			dispose();
		}else if(arg0.getSource() == (Object) risa8){
			maxcont = 8;
			lInstrucoes.setText(Lang.iLang==ENGLISH?Lang.msgsGUI[313]:Lang.msgsGUI[312]);
		}else if(arg0.getSource() == (Object) risa16){
			maxcont = 16;
			lInstrucoes.setText(Lang.iLang==ENGLISH?Lang.msgsGUI[315]:Lang.msgsGUI[314]);
		}else if(arg0.getSource() == (Object) clear){
			cbadd.setSelected(false);
			cbsll.setSelected(false);
			cbsrl.setSelected(false);
			cbsra.setSelected(false);
			cbsllv.setSelected(false);
			cbsrlv.setSelected(false);
			cbsrav.setSelected(false);
			cbjalr.setSelected(false);
		    cbjr.setSelected(false);
		    cbmult.setSelected(false);
		    cbmultu.setSelected(false);
		    cbdiv.setSelected(false);
		    cbdivu.setSelected(false);
		    cbaddu.setSelected(false);
		    cbsub.setSelected(false);
		    cbsubu.setSelected(false);
		    cband.setSelected(false);
		    cbor.setSelected(false);
		    cbxor.setSelected(false);
		    cbnor.setSelected(false);
		    cbslt.setSelected(false);
		    cbsltu.setSelected(false);
		    cblw.setSelected(false);
		    cbsw.setSelected(false);
		    cbaddi.setSelected(false);
		    cbandi.setSelected(false);
		    cbori.setSelected(false);
		    cbbeq.setSelected(false);
		    cbbltz.setSelected(false);
		    cbbgtz.setSelected(false);
		    cbblez.setSelected(false);
		    cbbne.setSelected(false);
		    cbsllv2.setSelected(false);
		    cbsrlv2.setSelected(false);
		    cbslti.setSelected(false);
		    cbj.setSelected(false);
		    cbjal.setSelected(false);
		    //cbnop.setSelected(false);
		    //cbBUBBLE.setSelected(false);
		    //cbcm.setSelected(false);
		    cblb.setSelected(false);
		    cbsb.setSelected(false);
		    cbrem.setSelected(false);
		    cbsrli.setSelected(false);
		    cbslli.setSelected(false);
		    cbsrai.setSelected(false);
		    cblwl.setSelected(false);
		    cblwr.setSelected(false);
		    cbswl.setSelected(false);
		    cbswr.setSelected(false);
		    //cbhlt.setSelected(false);
		}
	}
	
	public int getOpcodes(){
		int opcodes = 8;
		if(risa8.isSelected() == true){
			opcodes = 8;
		}else if(risa16.isSelected() == true){
			opcodes = 16;
		}
		return opcodes;
	}
	
	public int[] getInstructions(){
		int[] risaSet = null;
		if (risa8.isSelected() == true){
			int[] risaSet8 = {-1, -1, -1, -1, -1, -1, -1, -1};
			risaSet = risaSet8;
		}
		if (risa16.isSelected() == true){
			int[] risaSet16 = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
			risaSet = risaSet16;
		}
		int i = 0;
		if (cbadd.isSelected()){
			risaSet[i] = 0;
			i++;
		}if (cbsll.isSelected()){
			risaSet[i] = 1;
			i++;
		}if (cbsrl.isSelected()){
			risaSet[i] = 2;
			i++;
		}if (cbsra.isSelected()){
			risaSet[i] = 3;
			i++;
		}if (cbsllv.isSelected()){
			risaSet[i] = 4;
			i++;
		}if (cbsrlv.isSelected()){
			risaSet[i] = 5;
			i++;
		}if (cbsrav.isSelected()){
			risaSet[i] = 6;
			i++;
		}if (cbjalr.isSelected()){
			risaSet[i] = 7;
			i++;
		}if (cbjr.isSelected()){
			risaSet[i] = 8;
			i++;
		}if (cbmult.isSelected()){
			risaSet[i] = 9;
			i++;
		}if (cbmultu.isSelected()){
			risaSet[i] = 10;
			i++;
		}if (cbdiv.isSelected()){
			risaSet[i] = 11;
			i++;
		}if (cbdivu.isSelected()){
			risaSet[i] = 12;
			i++;
		}if (cbaddu.isSelected()){
			risaSet[i] = 13;
			i++;
		}if (cbsub.isSelected()){
			risaSet[i] = 14;
			i++;
		}if (cbsubu.isSelected()){
			risaSet[i] = 15;
			i++;
		}if (cband.isSelected()){
			risaSet[i] = 16;
			i++;
		}if (cbor.isSelected()){
			risaSet[i] = 17;
			i++;
		}if (cbxor.isSelected()){
			risaSet[i] = 18;
			i++;
		}if (cbnor.isSelected()){
			risaSet[i] = 19;
			i++;
		}if (cbslt.isSelected()){
			risaSet[i] = 20;
			i++;
		}if (cbsltu.isSelected()){
			risaSet[i] = 21;
			i++;
		}if (cblw.isSelected()){
			risaSet[i] = 22;
			i++;
		}if (cbsw.isSelected()){
			risaSet[i] = 23;
			i++;
		}if (cbaddi.isSelected()){
			risaSet[i] = 24;
			i++;
		}if (cbandi.isSelected()){
			risaSet[i] = 25;
			i++;
		}if (cbori.isSelected()){
			risaSet[i] = 26;
			i++;
		}if (cbbeq.isSelected()){
			risaSet[i] = 27;
			i++;
		}if (cbbltz.isSelected()){
			risaSet[i] = 28;
			i++;
		}if (cbbgtz.isSelected()){
			risaSet[i] = 29;
			i++;
		}if (cbblez.isSelected()){
			risaSet[i] = 30;
			i++;
		}if (cbbne.isSelected()){
			risaSet[i] = 31;
			i++;
		}if (cbsllv2.isSelected()){
			risaSet[i] = 32;
			i++;
		}if (cbsrlv2.isSelected()){
			risaSet[i] = 33;
			i++;
		}if (cbslti.isSelected()){
			risaSet[i] = 34;
			i++;
		}if (cbj.isSelected()){
			risaSet[i] = 35;
			i++;
		}if (cbjal.isSelected()){
			risaSet[i] = 36;
			i++;
		}/*if (cbnop.isSelected()){
			risaSet[i] = 37;
			i++;
		}if (cbBUBBLE.isSelected()){
			risaSet[i] = 38;
			i++;
		}if (cbcm.isSelected()){
			risaSet[i] = 39;
			i++;
		}*/if (cblb.isSelected()){
			risaSet[i] = 40;
			i++;
		}if (cbsb.isSelected()){
			risaSet[i] = 41;
			i++;
		}if (cbrem.isSelected()){
			risaSet[i] = 42;
			i++;
		}if (cbsrli.isSelected()){
			risaSet[i] = 43;
			i++;
		}if (cbslli.isSelected()){
			risaSet[i] = 44;
			i++;
		}if (cbsrai.isSelected()){
			risaSet[i] = 45;
			i++;
		}if (cblwl.isSelected()){
			risaSet[i] = 46;
			i++;
		}if (cblwr.isSelected()){
			risaSet[i] = 47;
			i++;
		}if (cbswl.isSelected()){
			risaSet[i] = 48;
			i++;
		}if (cbswr.isSelected()){
			risaSet[i] = 49;
			i++;
		}/*if (cbhlt.isSelected()){
			risaSet[i] = 50;
			i++;
		}*/
		return risaSet;
	}
	
	void addGridBag(JPanel pnl, Component obj, int x, int y)
	{
		restricoes.gridx = x;
		restricoes.gridy = y;
		pnl.add(obj, restricoes);
	}
	
	JButton ok, cancel, clear;
	Simulator sSim;
	Processor pProc;
	JLabel lFormato, lInstrucoes;
	JPanel panel;
	JRadioButton risa8, risa16;
	ButtonGroup bg;
	GridBagConstraints restricoes = new GridBagConstraints();
	JCheckBoxMenuItem cbadd, cbsll, cbsrl, cbsra, cbsllv, cbsrlv, cbsrav, cbjalr,
                      cbjr, cbmult, cbmultu, cbdiv, cbdivu, cbaddu, cbsub, cbsubu,
                      cband, cbor, cbxor, cbnor, cbslt, cbsltu, cblw, cbsw, cbaddi,
                      cbandi, cbori, cbbeq, cbbltz, cbbgtz, cbblez, cbbne, cbsllv2,
                      cbsrlv2, cbslti, cbj, cbjal, /*cbnop, cbBUBBLE, cbcm,*/ cblb, cbsb,
                      cbrem, cbsrli, cbslli, cbsrai, cblwl, cblwr, cbswl, cbswr/*, cbhlt*/;
	int cont = 0, maxcont = 8;
}
