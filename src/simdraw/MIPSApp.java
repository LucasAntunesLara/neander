/*
 * @(#)NetApp.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	 by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package simdraw;

import help.Help;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.MenuShortcut;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JDialog;

import montador.MontadorMIPS;

/*//**************************
import simdraw.dialogs.teste;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
//***********************/
import org.jhotdraw.contrib.Desktop;
import org.jhotdraw.framework.DrawingView;
import org.jhotdraw.standard.AbstractCommand;
import org.jhotdraw.standard.NullTool;
import org.jhotdraw.util.Command;
import org.jhotdraw.util.CommandMenu;
import org.jhotdraw.util.UndoManager;

import platform.Lang;
import platform.Platform;
import processor.MIPS;
import processor.Processor;
import processor.RodadaDeSimulacaoRisa;
import simdraw.TDBenchApp.InstructionOrCycleAction;
import simdraw.dialogs.GraphicDialog;
import simdraw.dialogs.NewSimulateDialog;
import simdraw.dialogs.SetBreakpointDialog;
import simdraw.dialogs.ConfigureRisaDialog;
import simdraw.dialogs.ShowGraphicDialog;
import simdraw.dialogs.RecrISADialog;
import simdraw.figures.AdderFigure;
import simdraw.figures.ConnectionFigure;
import simdraw.figures.GenericInOutFigure;
import simdraw.figures.GenericFigure;
import simdraw.figures.GenericMultiplexerFigure;
import simdraw.figures.GenericPipeRegisterFigure;
import simdraw.figures.GenericRegisterBankFigure;
import simdraw.figures.MemoryFigure;
import simdraw.figures.MultiplexerFigure;
import simdraw.figures.PipeRegisterFigure;
import simdraw.figures.RegisterBankFigure;
import simdraw.figures.RegisterFigure;
import simdraw.figures.TimerFigure;
import simdraw.panels.ByteMemoryPanel;
import simdraw.panels.ComponentPanel;
import simdraw.panels.DataMemoryPanel;
import simdraw.panels.MemoryPanel;
import simdraw.panels.ProcessorPanel;
import simdraw.panels.BreakpointPanel;
import simdraw.panels.RegisterFilePanel;
import simdraw.panels.SuperescalarPanel;
import simulator.Simulator;
import util.Define;
import util.SistNum;

/**
 * @version <$CURRENT_VERSION$>
 */
public  class MIPSApp extends TDBenchApp implements Define {

	class DLXSimulateAction extends AbstractAction {
		public DLXSimulateAction(	MIPSApp parNa) {
			//super("Sim", null);
			na = parNa;
		}
		public void actionPerformed(ActionEvent e) {
			try {
				na.sSim.Simulate ( na.pProc);
			} catch ( Exception exc) {
				TDBenchApp.messagesToUser(Lang.iLang==ENGLISH?Lang.msgsGUI[181]:Lang.msgsGUI[191],Processor.getMessageError());
				exc.printStackTrace();
			}
			na.atualizaVisualizacao ( );
		}
		
		MIPSApp na;
	}
/*
	class DecimalNumericSystemAction extends AbstractAction {
		public DecimalNumericSystemAction(	DLXApp parNa, String sName) {
			super ( sName);
			na = parNa;
		}
		public void actionPerformed(ActionEvent e) {
			SistNum.setDefaultNumberFormat ( "d");
			System.out.println ( "Choose decimal!");
			na.atualizaVisualizacao ( );
		}
		
		DLXApp na;
	}

	class HexaDecimalNumericSystemAction extends AbstractAction {
		public HexaDecimalNumericSystemAction(	DLXApp parNa, String sName) {
			super ( sName);
			na = parNa;
		}
		public void actionPerformed(ActionEvent e) {
			SistNum.setDefaultNumberFormat ( "h");
			System.out.println ( "Choose hexa decimal!");
			na.atualizaVisualizacao ( );
		}
		
		DLXApp na;
	}
	
	class BinaryNumericSystemAction extends AbstractAction {
		public BinaryNumericSystemAction(	DLXApp parNa,String sName) {
			super ( sName);
			na = parNa;
		}
		public void actionPerformed(ActionEvent e) {
			SistNum.setDefaultNumberFormat ( "b");
			System.out.println ( "Choose binary!");
			na.atualizaVisualizacao ( );
		}
		
		DLXApp na;
	}
	
	class InstructionOrCycleAction extends AbstractAction {
		public InstructionOrCycleAction(	String sName) {
			super ( sName);
		}
		public void actionPerformed(ActionEvent e) {
			bSimulate = ! bSimulate;
			System.out.println ( "Change simulation mode: "+ (bSimulate==true?"Cycle":"Instruction"));
		}
	}
*/	
	public MIPSApp(Simulator parSim, Processor parProc) {
		super("MIPS I Processor Model");
		sSim = parSim;
		pProc = parProc;
		dtpVis = pProc.getDatapath ( );
		jNroUnitsSim = null;
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();
		int screenHeight = d.height;
		int screenWidth = d.width;
		setSize(screenWidth, screenHeight);
		
		SistNum.setDefaultSizeFormat( WORD);
		Lang.iLang = Lang.getDefaultLanguage();
	}

	public void reset ( Simulator parSim, Processor parProc) {
		sSim = parSim;
		pProc = parProc;
		dtpVis = pProc.getDatapath ( );
		jNroUnitsSim = null;	
		// new for reset - atentar para os dialogos usados pelo simulador
		jMemContents = null;
		jDMemContents = null;
		jRFContents = null;
		jSuperescalar = null;
		jComponent = null;
		jProcessor = null;
		jBreakpoints = null;
		createPanels ( );
		resetDialogs ( );
	}
/*
	void setStatusLine(JTextField newStatusLine) {
		fStatusLine = newStatusLine;
	}

	protected JTextField getStatusLine() {
		return fStatusLine;
	}
*/
	private void createBlockDiagram ( ) {
		TimerFigure t = new TimerFigure ( "Pipelined MIPS I", 20);
		this.view().drawing().add ( t);

		AdderFigure add = new AdderFigure ( "add", "E1", "E2", "S1");
		add.setBaseSize ( 36, 24);
		this.view().drawing().add ( add);
		
		MultiplexerFigure muxpc = new MultiplexerFigure ( "muxpc", "E1", "E2", "S1", "SEL");
		this.view().drawing().add ( muxpc);
		
		RegisterFigure pc = new RegisterFigure ( "pc", "E1", "S1");
		this.view().drawing().add ( pc);
		
		MemoryFigure imem = new MemoryFigure ( "imemory", "E1", "E2", "S1");
		imem.setBaseSize ( 35, 60);
		this.view().drawing().add ( imem);

		PipeRegisterFigure if_id = new PipeRegisterFigure ( "if_id", "E1", "S1", "E2", "S2", "E3", "S3", "E4", "S4");
		this.view().drawing().add ( if_id);
		
		RegisterFigure nRI = new RegisterFigure ( "nRI", "E1", "S1");
		nRI.setBaseSize(30,10);
		this.view().drawing().add ( nRI);

//		RegisterBankFigure registers = new RegisterBankFigure ( "registers", "E1", "S1", "S2", "NW1", "NR1", "NR2");
//		this.view().drawing().add ( registers);

		GenericRegisterBankFigure registers = new GenericRegisterBankFigure ( "registers", pProc);
		this.view().drawing().add ( registers);

		GenericInOutFigure se = new GenericInOutFigure ( "se", "E0", "S0");
		se.setBaseSize(30,10);
		this.view().drawing().add ( se);
		
		GenericInOutFigure gfc = new GenericInOutFigure ( "25to0", "E0", "S0");
		gfc.setBaseSize(30,10);
		this.view().drawing().add ( gfc);

		GenericPipeRegisterFigure id_ex = new GenericPipeRegisterFigure ( "id_ex", 6);
		this.view().drawing().add ( id_ex);
		
		GenericInOutFigure not = new GenericInOutFigure ( "not", "E1", "S1");
		not.setBaseSize ( 30, 10);
		this.view().drawing().add ( not);
		GenericMultiplexerFigure gfComp = new GenericMultiplexerFigure ( "comp", pProc);
		gfComp.setBaseSize ( 25, 25);
		this.view().drawing().add ( gfComp);
		MultiplexerFigure muxa = new MultiplexerFigure ( "muxa", "E1", "E2", "S1", "SEL");
		muxa.setBaseSize ( 28, 36);
		this.view().drawing().add ( muxa);
		//MultiplexerFigure muxb = new MultiplexerFigure ( "muxb", "E1", "E2", "S1", "SEL");
		GenericMultiplexerFigure muxb = new GenericMultiplexerFigure ( "muxb", pProc);
		muxb.setBaseSize ( 28, 36);
		this.view().drawing().add ( muxb);
	
		PipeRegisterFigure ex_mem = new PipeRegisterFigure ( "ex_mem", "E1", "S1", "E2", "S2", "E3", "S3", "E4", "S4");
		this.view().drawing().add ( ex_mem);

		MemoryFigure dmem = new MemoryFigure ( "dmemory", "E0", "E1", "S0");
		dmem.setBaseSize ( 25, 75);
		this.view().drawing().add ( dmem);

		PipeRegisterFigure mem_wb = new PipeRegisterFigure ( "mem_wb", "E1", "S1", "E2", "S2", "E3", "S3", "E4", "S4");
		this.view().drawing().add ( mem_wb);
		
		GenericMultiplexerFigure muxwb = new GenericMultiplexerFigure ( "muxwb", pProc);
		muxwb.setBaseSize ( 36, 36);
		this.view().drawing().add ( muxwb);
		
		MultiplexerFigure alu = new MultiplexerFigure ( "ALU", "E1", "E2", "S1", "OP");
		alu.setBaseSize ( 36, 66);
		this.view().drawing().add ( alu);

		ConnectionFigure cx;
				
		cx = createConnection ( this.view(),"pc","S1","E2",pc,imem);
		cx.insertPointAt ( new Point ( 98, 251), 1);
		cx.insertPointAt ( new Point ( 80, 251), 2);

		cx = createConnection ( this.view(),"add","S1","E2",add,muxpc);
		cx.insertPointAt ( new Point ( 50, 68), 1);
		cx = createConnection ( this.view(),"add","S1","E4",add,if_id);
		cx.insertPointAt ( new Point ( 192, 128), 1);
		
		cx = createConnection ( this.view(),"pc","S1","E1",pc,add);
		cx.insertPointAt ( new Point ( 98, 251), 1);
		cx.insertPointAt ( new Point ( 32, 251), 2);

		cx = createConnection ( this.view(),"muxpc","S1","E1",muxpc,pc);
		cx.insertPointAt ( new Point ( 186, 6), 1);
		cx.insertPointAt ( new Point ( 2, 6), 2);

		cx = createConnection ( this.view(),"muxpc","S1","E1",muxpc,if_id);
		cx.insertPointAt ( new Point ( 241, 50), 1);

		cx = createConnection ( this.view(),"imemory","S1","E2",imem,if_id);
		//cx.insertPointAt ( new Point ( 220, 150), 1);
		cx = createConnection ( this.view(),"imemory","S1","E3",imem,if_id);

		cx = createConnection ( this.view(),"if_id","S1","E0",if_id,id_ex);
		cx = createConnection ( this.view(),"if_id","S2","E0",if_id,se);
		cx.insertPointAt ( new Point ( 307, 232), 1);
		cx = createConnection ( this.view(),"if_id","S3","E0",if_id,gfc);
		cx = createConnection ( this.view(),"if_id","S4","E5",if_id,id_ex);
		cx = createConnection ( this.view(),"registers","S1","E1",registers,id_ex);
		//cx.insertPointAt ( new Point ( 421, 159), 1);
		cx = createConnection ( this.view(),"registers","S2","E2",registers,id_ex);
		//cx.insertPointAt ( new Point ( 403, 220), 1);
		cx = createConnection ( this.view(),"se","S0","E3",se,id_ex);
		cx = createConnection ( this.view(),"25to0","S0","E4",gfc,id_ex);
		//cx.insertPointAt ( new Point ( 421, 330), 1);
		//
		cx = createConnection ( this.view(),"muxa","S1","E1",muxa, alu);
		cx = createConnection ( this.view(),"muxb","S0","E2",muxb, alu);	
		cx = createConnection ( this.view(),"id_ex","S0","E1",id_ex,muxa);
		cx = createConnection ( this.view(),"id_ex","S1","E2",id_ex,muxa);
		cx = createConnection ( this.view(),"id_ex","S2","E0",id_ex,muxb);
		cx = createConnection ( this.view(),"id_ex","S3","E1",id_ex,muxb);
		cx = createConnection ( this.view(),"id_ex","S1","E0",id_ex,gfComp);
		cx.insertPointAt ( new Point ( 469, 104), 1);
		cx = createConnection ( this.view(),"id_ex","S2","E1",id_ex,gfComp);
		cx.insertPointAt ( new Point ( 469, 180), 1);
		cx = createConnection ( this.view(),"id_ex","S2","E3",id_ex,ex_mem);
		cx.insertPointAt ( new Point ( 627, 334), 1);
		cx.insertPointAt ( new Point ( 478, 334), 1);
		cx = createConnection ( this.view(),"comp","S0","E1",gfComp,ex_mem);
		cx = createConnection ( this.view(),"alu","S1","E2",alu,ex_mem);
		cx = createConnection ( this.view(),"id_ex","S4","E3",id_ex,muxb);
		cx = createConnection ( this.view(),"id_ex","S5","E4",id_ex,ex_mem);
		cx.insertPointAt ( new Point ( 510, 352), 1);
		//
		cx = createConnection ( this.view(),"ex_mem","S1","E1",ex_mem,not);
//		cx = createConnection ( this.view(),"not","S1","E1",not,mem_wb);
		cx = createConnection ( this.view(),"ex_mem","S2","E1",ex_mem,dmem);
		cx.insertPointAt ( new Point ( 725, 150), 1);		
		cx = createConnection ( this.view(),"ex_mem","S2","E3",ex_mem,mem_wb);
		cx.insertPointAt ( new Point ( 727, 101), 1);
		cx.insertPointAt ( new Point ( 816, 101), 2);
		cx = createConnection ( this.view(),"ex_mem","S2","E1",ex_mem,muxpc);
		cx.insertPointAt ( new Point ( 699, 2), 1);
		cx.insertPointAt ( new Point ( 114, 2), 2);
		cx = createConnection ( this.view(),"ex_mem","S3","E0",ex_mem,dmem);
		cx = createConnection ( this.view(),"ex_mem","S4","E4",ex_mem,mem_wb);
		//
		cx = createConnection ( this.view(),"dmemory","S0","E2",dmem,mem_wb);
		cx.insertPointAt ( new Point ( 846, 190), 1);
		//
		cx = createConnection ( this.view(),"mem_wb","S2","E0",mem_wb,muxwb);
		cx = createConnection ( this.view(),"mem_wb","S3","E1",mem_wb,muxwb);
		cx = createConnection ( this.view(),"mem_wb","S4","E2",mem_wb,muxwb);
		//
		cx = createConnection ( this.view(),"muxwb","S0","E1",muxwb,registers);
		cx.insertPointAt ( new Point ( 978, 364), 1);
		cx.insertPointAt ( new Point ( 286, 364), 2);
		
		t.moveBy ( 360,30);
		add.moveBy( 50, 100);
		muxpc.moveBy ( 150, 50);
		pc.moveBy( 50, 300);
		imem.moveBy ( 150, 200);
		if_id.moveBy ( 265, 185);
		nRI.moveBy( 355, 92);
		registers.moveBy ( 355, 180);
		se.moveBy ( 355, 270);
		gfc.moveBy ( 345, 330);
		id_ex.moveBy ( 445, 185);
		not.moveBy ( 775, 80);
		gfComp.moveBy ( 550, 60);
		muxa.moveBy ( 534, 150);
		muxb.moveBy ( 515, 290);
		alu.moveBy ( 597, 220);
		ex_mem.moveBy ( 675, 185);
		dmem.moveBy ( 775, 190);
		mem_wb.moveBy ( 870, 185);
		muxwb.moveBy ( 950, 180);
/*		alu.moveBy( 600, 350);*/		
	}
	
	private void createPanels ( ) {
		if ( jMemContents == null) {
			jMemContents = new MemoryPanel ( sSim, pProc, "imemory", "pc");				
		}		
		if ( jDMemContents == null) {
			jDMemContents = new ByteMemoryPanel ( sSim, pProc, "dmemory", null);				
		}	
		if ( jRFContents == null) {
			jRFContents = new RegisterFilePanel ( sSim, pProc, "registers");				
		}	
		if ( jSuperescalar == null) {
			jSuperescalar = new SuperescalarPanel ( sSim, pProc);
		}
		if ( jComponent == null) {
			jComponent = new ComponentPanel ( sSim, pProc);				
		}
		if ( jProcessor == null) {
			jProcessor = new ProcessorPanel ( sSim, pProc);
		}	
		if ( jBreakpoints == null) {
			jBreakpoints = new BreakpointPanel ( sSim, pProc);
		}

		// new for Reset - atentar para a retirada das linhas setDesk... da sua posicao original
		if (tabbedPane != null) getContentPane().remove(tabbedPane);
		else {
			setDesktopListener(createDesktopListener());
			setDesktop(createDesktop());
		}
		tabbedPane = new JTabbedPane();

		JPanel pContents = new JPanel ( );
		pContents.setLayout ( new GridLayout ( 1, 3, 5, 5));
		pContents.add ( jMemContents);
		pContents.add ( jDMemContents);
		pContents.add ( jRFContents);

		JPanel activePanel = new JPanel();
		activePanel.setAlignmentX(LEFT_ALIGNMENT);
		activePanel.setAlignmentY(TOP_ALIGNMENT);
		activePanel.setLayout(new BorderLayout());
		activePanel.add((Component)getDesktop(), BorderLayout.CENTER);

		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();
		int screenHeight = d.height;
		int screenWidth = d.width;
		pContents.setPreferredSize( new Dimension ( screenWidth, (int) (screenHeight*0.275)));
		activePanel.add(pContents, BorderLayout.SOUTH);
		
		JPanel controlPanel = new JPanel ( );
		controlPanel.setLayout( new BorderLayout ( 5, 5));
		controlPanel.add(jSuperescalar, BorderLayout.CENTER);

		JPanel controlPanel2 = new JPanel ( );
		controlPanel2.setLayout( new BorderLayout ( 5, 5));
		controlPanel2.add(jProcessor, BorderLayout.CENTER);

		// new for Reset - testar o null em textArea
		if (textArea == null) textArea = new JTextArea ( );
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setFont(new Font (null, Font.PLAIN, 11));
        JScrollPane scrollPane = new JScrollPane(textArea,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		JPanel messagePanel = new JPanel ( );	
		messagePanel.setLayout( new BorderLayout ( 5, 5));
		messagePanel.add( scrollPane, BorderLayout.CENTER);

		tabbedPane.addTab(Lang.iLang==ENGLISH?Lang.msgsGUI[2]:Lang.msgsGUI[32], null, activePanel, Lang.iLang==ENGLISH?Lang.msgsGUI[3]:Lang.msgsGUI[33]);
		tabbedPane.setSelectedIndex(0);
		tabbedPane.addTab(Lang.iLang==ENGLISH?Lang.msgsGUI[4]:Lang.msgsGUI[34], null, controlPanel, Lang.iLang==ENGLISH?Lang.msgsGUI[5]:Lang.msgsGUI[35]);
		tabbedPane.addTab(Lang.iLang==ENGLISH?Lang.msgsGUI[6]:Lang.msgsGUI[36], null, jComponent, Lang.iLang==ENGLISH?Lang.msgsGUI[7]:Lang.msgsGUI[37]);
		tabbedPane.addTab(Lang.iLang==ENGLISH?Lang.msgsGUI[8]:Lang.msgsGUI[38], null, scrollPane, Lang.iLang==ENGLISH?Lang.msgsGUI[9]:Lang.msgsGUI[39]);
		tabbedPane.addTab(Lang.iLang==ENGLISH?Lang.msgsGUI[29]:Lang.msgsGUI[59], null, controlPanel2, Lang.iLang==ENGLISH?Lang.msgsGUI[28]:Lang.msgsGUI[58]);
		tabbedPane.addTab(Lang.iLang==ENGLISH?Lang.msgsGUI[278]:Lang.msgsGUI[283], null, jBreakpoints, Lang.iLang==ENGLISH?Lang.msgsGUI[278]:Lang.msgsGUI[283]);

		getContentPane().add(tabbedPane, BorderLayout.CENTER);		
	}
	
	/**
	 * Opens a new window with a drawing view.
	 */
	protected void open(final DrawingView newDrawingView) {
		getVersionControlStrategy().assertCompatibleVersion();
		setUndoManager(new UndoManager());
		setIconkit(createIconkit());
		getContentPane().setLayout(new BorderLayout());

		// status line must be created before a tool is set
		setStatusLine(new JTextField ( ""));
		getContentPane().add(getStatusLine(), BorderLayout.SOUTH);

		// create dummy tool until the default tool is activated during toolDone()
		setTool(new NullTool(this), "");
		setView(newDrawingView);

		JToolBar tools = createToolPalette();
		createTools(tools);
		getContentPane().add(tools, BorderLayout.NORTH);

		//
		jOFC = createOpenFileChooser();
		createBlockDiagram ( );
		createPanels();

		JMenuBar mb = new JMenuBar();
		createMenus(mb);
		setJMenuBar(mb);

		addListeners();
		setStorageFormatManager(createStorageFormatManager());

		//no work allowed to be done on GUI outside of AWT thread once
		//setVislble(true) must be called before drawing added to desktop, else 
		//DND will fail. on drawing added before with a NPE.  note however that
		//a nulldrawingView will not fail because it is never really added to the desltop
		setVisible(true);
		Runnable r = new Runnable() {
			public void run() {
				if (newDrawingView.isInteractive()) {
					getDesktop().addToDesktop(newDrawingView , Desktop.PRIMARY);
				}
				toolDone();
			}
		};

		if (java.awt.EventQueue.isDispatchThread() == false) {
			try {
				java.awt.EventQueue.invokeAndWait(r);
			}
			catch(java.lang.InterruptedException ie) {
				System.err.println(ie.getMessage());
				exit();
			}
			catch(java.lang.reflect.InvocationTargetException ite) {
				System.err.println(ite.getMessage());
				exit();
			}
		}
		else {
			r.run();
		}

		toolDone();
	}
/*
	private ConnectionFigure createConnection(	DrawingView dw,String sName, String sS, String sE, 
												LinkSimulatorVisualization f1, LinkSimulatorVisualization f2) {
		ConnectionFigure cx = new ConnectionFigure ();
		cx.setPortName ( sName, sS);
		cx.addPoint ( 0,0);
		cx.addPoint ( 0,0);
		cx.connectStart(f1.connectorAt(sS));
		cx.connectEnd(f2.connectorAt(sE));
		dw.add ( cx);
		return cx;
	}
*/	
	protected void createTools(JToolBar palette) {
		//super.createTools(palette);
		String sAux = Platform.treatPathNames(".\\images\\simulate.gif");
		
		JButton jSim = new JButton ( new DLXSimulateAction ( this));
		jSim.setIcon ( new ImageIcon ( sAux));
		palette.addSeparator();
		palette.add( jSim);
		palette.setBackground( new Color ( 238,238,238));
		
	}
	
	/**
	 * Creates the standard menus. Clients override this
	 * method to add additional menus.
	 */
	protected void createMenus(JMenuBar mb) {
		addMenuIfPossible(mb, createFileMenu());
		addMenuIfPossible(mb, createViewMenu());
		addMenuIfPossible(mb, createTrackingMenu());
		addMenuIfPossible(mb, createSteeringMenu());
		addMenuIfPossible(mb, createHelpMenu());
	}

	/**
	 * Creates the file menu. Clients override this
	 * method to add additional menu items.
	 */
	protected JMenu createFileMenu() {
		CommandMenu menu = new CommandMenu(Lang.iLang==ENGLISH?Lang.msgsGUI[10]:Lang.msgsGUI[40]);
		Command cmd;

		cmd = new AbstractCommand(Lang.iLang==ENGLISH?Lang.msgsGUI[11]:Lang.msgsGUI[41], this, true) {
			public void execute() {
				try {
					promptFileName();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		menu.add(cmd);
		cmd = new AbstractCommand(Lang.iLang==ENGLISH?Lang.msgsGUI[12]:Lang.msgsGUI[42], this, true) {
			public void execute() {
				promptReset();
			}
		};
		menu.add(cmd);

		cmd = new AbstractCommand(Lang.iLang==ENGLISH?Lang.msgsGUI[13]:Lang.msgsGUI[43], this, true) {
			public void execute() {
				endApp();
			}
		};
		menu.add(cmd);
		
		return menu;
	}
/*
	protected JMenu createViewMenu() {
		CommandMenu menu = new CommandMenu(Lang.iLang==ENGLISH?Lang.msgsGUI[14]:Lang.msgsGUI[44]);
		ButtonGroup bg = new ButtonGroup ( );

		JRadioButtonMenuItem rbA = new JRadioButtonMenuItem ( new DecimalNumericSystemAction ( this,Lang.iLang==ENGLISH?Lang.msgsGUI[15]:Lang.msgsGUI[45]));
		JRadioButtonMenuItem rbB = new JRadioButtonMenuItem ( new HexaDecimalNumericSystemAction ( this,Lang.iLang==ENGLISH?Lang.msgsGUI[16]:Lang.msgsGUI[46]));
		JRadioButtonMenuItem rbC = new JRadioButtonMenuItem ( new BinaryNumericSystemAction ( this,Lang.iLang==ENGLISH?Lang.msgsGUI[17]:Lang.msgsGUI[47]));

		bg.add(rbA);
		bg.add(rbB);
		bg.add(rbC);
		bg.setSelected( rbA.getModel ( ), true);
		menu.add ( rbA);
		menu.add ( rbB);
		menu.add ( rbC);

		return menu;
	}*/
	
	/*****************************************************************************************************************\
	| INCIO - Reescreve o mtodo createTrackingMenu() da classe TDBenchApp, adicionando o menu "Simular com rISA..." |
	\*****************************************************************************************************************/
	
	protected JMenu createTrackingMenu() {
		CommandMenu menu = new CommandMenu(Lang.iLang==ENGLISH?Lang.msgsGUI[18]:Lang.msgsGUI[48]);
		Command cmd = new AbstractCommand(Lang.iLang==ENGLISH?Lang.msgsGUI[19]:Lang.msgsGUI[49], this, false) {
			public void execute() {
				promptSimulate();
			}
		};
		menu.add(cmd, new MenuShortcut('s'));

		cmd = new AbstractCommand(Lang.iLang==ENGLISH?Lang.msgsGUI[20]:Lang.msgsGUI[50], this, true) {
			public void execute() {
				promptSimulateDialog();
			}
		};
		menu.add(cmd, new MenuShortcut('i'));
		
		cmd = new AbstractCommand(Lang.iLang==ENGLISH?Lang.msgsGUI[286]:Lang.msgsGUI[285], this, true) {
			public void execute() {
				promptConfigureRisaDialog();
			}
		};
		menu.add(cmd, new MenuShortcut('i'));
		
		cmd = new AbstractCommand(Lang.iLang==ENGLISH?Lang.msgsGUI[276]:Lang.msgsGUI[281], this, true) {
			public void execute() {
				TimeDialog();
			}
		};
		menu.add(cmd, new MenuShortcut('i'));

		cmd = new AbstractCommand(Lang.iLang==ENGLISH?Lang.msgsGUI[277]:Lang.msgsGUI[282], this, false) {
			public void execute() {
				promptBreakpointDialog();
			}
		};
		menu.add(cmd, new MenuShortcut('t'));
	
		menu.add(new JSeparator());

		ButtonGroup bg = new ButtonGroup ( );
		JCheckBoxMenuItem cb1 = new JCheckBoxMenuItem ( new InstructionOrCycleAction ( Lang.iLang==ENGLISH?Lang.msgsGUI[25]:Lang.msgsGUI[55]));
		JCheckBoxMenuItem cb2 = new JCheckBoxMenuItem ( new InstructionOrCycleAction ( Lang.iLang==ENGLISH?Lang.msgsGUI[26]:Lang.msgsGUI[56]));

		bg.add(cb1);
		bg.add(cb2);
		bg.setSelected( cb1.getModel ( ), true);
		menu.add ( cb1);
		menu.add ( cb2);

		return menu;
	}
	
	private void promptSimulate ( ) {
		try {
			sSim.Simulate ( pProc);
		} catch ( Exception exc) {
			TDBenchApp.messagesToUser(Lang.iLang==ENGLISH?Lang.msgsGUI[181]:Lang.msgsGUI[191],Processor.getMessageError());
		}
		atualizaVisualizacao ( );
	}
	
	private void promptSimulateDialog ( ) {
		if ( jNroUnitsSim == null) {
			jNroUnitsSim = new NewSimulateDialog ( sSim, pProc);				
			jNroUnitsSim.pack();
		}
		if ( jNroUnitsSim.isVisible ( ) == false) {
			jNroUnitsSim.setModal ( true);
			jNroUnitsSim.setVisible ( true);
			int iUnits = ((NewSimulateDialog) jNroUnitsSim).getTimes ( );
			for ( int k = 0; k < iUnits; k ++) {
				try {
					sSim.Simulate ( pProc);
				} catch ( Exception exc) {
					TDBenchApp.messagesToUser(Lang.iLang==ENGLISH?Lang.msgsGUI[181]:Lang.msgsGUI[191],Processor.getMessageError());
				}
			}
			atualizaVisualizacao();
		}
	}
	
	private void promptConfigureRisaDialog(){
		try {
			promptFileName();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (jConfigureRisaDialog == null){
			jConfigureRisaDialog = new ConfigureRisaDialog(this, sSim, pProc, sPathName);
			jConfigureRisaDialog.pack();
		}
		if (jConfigureRisaDialog.isVisible() == false){
			jConfigureRisaDialog.setModal(true);
			jConfigureRisaDialog.setVisible(true);
			atualizaVisualizacao();
			double tempodesimulacao = 0;
			boolean[] risas = ((ConfigureRisaDialog)jConfigureRisaDialog).getSelectedRisas();
			String sBasePath = Platform.treatPathNames(Processor.getProcessorName()+Platform.getSeparatorPath()+"programs");
			String sFilePath = sBasePath + Platform.getSeparatorPath()+"_dlxBin.bin";
			String sFileTarget = "programs"+ Platform.getSeparatorPath()+"_dlxBin.bin";
			RodadaDeSimulacaoRisa rod = pProc.rodadaRisa;
			int rodada = 0;
			pProc.setRodadaRisa.clear();
			pProc.setRodadaRisa.setSize(8);
			rod.inicializa();
			pProc.setRodadaRisa.set(rodada, rod);
			if (risas[0] == true){
System.out.println ( "==> 0");
				rodada++;
				((MIPS)pProc).set("TDCF_8or16", STATUSorCONF, 0);
				// ((MIPS)pProc).updateRisaInstructionMatrix();
				((MIPS)pProc).set("TDCF_UsingRISA", STATUSorCONF, 0);
				((MIPS)pProc).set("TDCF_WhichRISA", STATUSorCONF, 0);
				try{
					MontadorMIPS mm = new MontadorMIPS((MIPS)pProc, sPathName, sFilePath);
				} catch (Exception e){
					e.printStackTrace();
				}
				try{
					((MIPS)pProc).setReset(sFileTarget);
					File f = new File(sFilePath);
					f.delete();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try{
					tempodesimulacao = sSim.getTimeStatistics((MIPS)pProc);
				} catch ( Exception exc) {
					exc.printStackTrace();
					MIPSApp.messagesToUser(Lang.iLang==ENGLISH?Lang.msgsGUI[180]:Lang.msgsGUI[190],Processor.getMessageError());
				}
				if (((ConfigureRisaDialog)jConfigureRisaDialog).getCBGraphicIsSelected()){
					rod = (RodadaDeSimulacaoRisa)pProc.setRodadaRisa.get(0);
					rod.setNroInstructionsFetched((int)((MIPS)pProc).get("NroInstFetched",FIELD));
					rod.setExecutionCycles((int)((MIPS)pProc).get("ExecutionCycles", FIELD));
					rod.setTempoDeSimulacao(tempodesimulacao);
					pProc.setRodadaRisa.set(rodada, rod);
				}
				atualizaVisualizacao();
			}
			if (risas[1] == true){
System.out.println ( "==> 1");
				rodada++;
				((MIPS)pProc).set("TDCF_8or16", STATUSorCONF, 0);
				// ((MIPS)pProc).updateRisaInstructionMatrix();
				((MIPS)pProc).set("TDCF_UsingRISA", STATUSorCONF, 1);
				((MIPS)pProc).set("TDCF_WhichRISA", STATUSorCONF, 0);
				try{
					MontadorMIPS mm = new MontadorMIPS((MIPS)pProc, sPathName, sFilePath);
				} catch (Exception e){
					e.printStackTrace();
				}
				try{
					((MIPS)pProc).setReset(sFileTarget);
					File f = new File(sFilePath);
					f.delete();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try{
					tempodesimulacao = sSim.getTimeStatistics((MIPS)pProc);
				} catch ( Exception exc) {
					exc.printStackTrace();
					MIPSApp.messagesToUser(Lang.iLang==ENGLISH?Lang.msgsGUI[180]:Lang.msgsGUI[190],Processor.getMessageError());
				}
				if (((ConfigureRisaDialog)jConfigureRisaDialog).getCBGraphicIsSelected()){
					rod = (RodadaDeSimulacaoRisa)pProc.setRodadaRisa.get(0);
					rod.setNroInstructionsFetched((int)((MIPS)pProc).get("NroInstFetched",FIELD));
					rod.setExecutionCycles((int)((MIPS)pProc).get("ExecutionCycles", FIELD));
					rod.setTempoDeSimulacao(tempodesimulacao);
					pProc.setRodadaRisa.set(rodada, rod);
				}
				atualizaVisualizacao();
			}
			if (risas[2] == true){
System.out.println ( "==> 2");
				rodada++;
				((MIPS)pProc).set("TDCF_8or16", STATUSorCONF, 0);
				// ((MIPS)pProc).updateRisaInstructionMatrix();
				((MIPS)pProc).set("TDCF_UsingRISA", STATUSorCONF, 1);
				((MIPS)pProc).set("TDCF_WhichRISA", STATUSorCONF, 1);
				try{
					MontadorMIPS mm = new MontadorMIPS((MIPS)pProc, sPathName, sFilePath);
				} catch (Exception e){
					e.printStackTrace();
				}
				try{
					((MIPS)pProc).setReset(sFileTarget);
					File f = new File(sFilePath);
					f.delete();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try{
					tempodesimulacao = sSim.getTimeStatistics((MIPS)pProc);
				} catch ( Exception exc) {
					exc.printStackTrace();
					MIPSApp.messagesToUser(Lang.iLang==ENGLISH?Lang.msgsGUI[180]:Lang.msgsGUI[190],Processor.getMessageError());
				}
				if (((ConfigureRisaDialog)jConfigureRisaDialog).getCBGraphicIsSelected()){
					rod = (RodadaDeSimulacaoRisa)pProc.setRodadaRisa.get(0);
					rod.setNroInstructionsFetched((int)((MIPS)pProc).get("NroInstFetched",FIELD));
					rod.setExecutionCycles((int)((MIPS)pProc).get("ExecutionCycles", FIELD));
					rod.setTempoDeSimulacao(tempodesimulacao);
					pProc.setRodadaRisa.set(rodada, rod);
				}
				atualizaVisualizacao();
			}
			if (risas[3] == true){
System.out.println ( "==> 3");
				rodada++;
				((MIPS)pProc).set("TDCF_8or16", STATUSorCONF, 0);
				// ((MIPS)pProc).updateRisaInstructionMatrix();
				((MIPS)pProc).set("TDCF_UsingRISA", STATUSorCONF, 1);
				((MIPS)pProc).set("TDCF_WhichRISA", STATUSorCONF, 2);
				try{
					MontadorMIPS mm = new MontadorMIPS((MIPS)pProc, sPathName, sFilePath);
				} catch (Exception e){
					e.printStackTrace();
				}
				try{
					((MIPS)pProc).setReset(sFileTarget);
					File f = new File(sFilePath);
					f.delete();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try{
					tempodesimulacao = sSim.getTimeStatistics((MIPS)pProc);
				} catch ( Exception exc) {
					exc.printStackTrace();
					MIPSApp.messagesToUser(Lang.iLang==ENGLISH?Lang.msgsGUI[180]:Lang.msgsGUI[190],Processor.getMessageError());
				}
				if (((ConfigureRisaDialog)jConfigureRisaDialog).getCBGraphicIsSelected()){
					rod = (RodadaDeSimulacaoRisa)pProc.setRodadaRisa.get(0);
					rod.setNroInstructionsFetched((int)((MIPS)pProc).get("NroInstFetched",FIELD));
					rod.setExecutionCycles((int)((MIPS)pProc).get("ExecutionCycles", FIELD));
					rod.setTempoDeSimulacao(tempodesimulacao);
					pProc.setRodadaRisa.set(rodada, rod);
				}
				atualizaVisualizacao();
			}
			if (risas[4] == true){
System.out.println ( "==> 4");
				rodada++;
				((MIPS)pProc).set("TDCF_8or16", STATUSorCONF, 0);
				// ((MIPS)pProc).updateRisaInstructionMatrix();
				((MIPS)pProc).set("TDCF_UsingRISA", STATUSorCONF, 1);
				((MIPS)pProc).set("TDCF_WhichRISA", STATUSorCONF, 3);
				try{
					MontadorMIPS mm = new MontadorMIPS((MIPS)pProc, sPathName, sFilePath);
				} catch (Exception e){
					e.printStackTrace();
				}
				try{
					((MIPS)pProc).setReset(sFileTarget);
					File f = new File(sFilePath);
					f.delete();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try{
					tempodesimulacao = sSim.getTimeStatistics((MIPS)pProc);
				} catch ( Exception exc) {
					exc.printStackTrace();
					MIPSApp.messagesToUser(Lang.iLang==ENGLISH?Lang.msgsGUI[180]:Lang.msgsGUI[190],Processor.getMessageError());
				}
				if (((ConfigureRisaDialog)jConfigureRisaDialog).getCBGraphicIsSelected()){
					rod = (RodadaDeSimulacaoRisa)pProc.setRodadaRisa.get(0);
					rod.setNroInstructionsFetched((int)((MIPS)pProc).get("NroInstFetched",FIELD));
					rod.setExecutionCycles((int)((MIPS)pProc).get("ExecutionCycles", FIELD));
					rod.setTempoDeSimulacao(tempodesimulacao);
					pProc.setRodadaRisa.set(rodada, rod);
				}
				atualizaVisualizacao();
			}
			if (risas[5] == true){
System.out.println ( "==> 5");
				rodada++;
				((MIPS)pProc).set("TDCF_8or16", STATUSorCONF, 1);
				// ((MIPS)pProc).updateRisaInstructionMatrix();
				((MIPS)pProc).set("TDCF_UsingRISA", STATUSorCONF, 1);
				((MIPS)pProc).set("TDCF_WhichRISA", STATUSorCONF, 2);
				try{
					MontadorMIPS mm = new MontadorMIPS((MIPS)pProc, sPathName, sFilePath);
				} catch (Exception e){
					e.printStackTrace();
				}
				try{
					((MIPS)pProc).setReset(sFileTarget);
					File f = new File(sFilePath);
					f.delete();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try{
					tempodesimulacao = sSim.getTimeStatistics((MIPS)pProc);
				} catch ( Exception exc) {
					exc.printStackTrace();
					MIPSApp.messagesToUser(Lang.iLang==ENGLISH?Lang.msgsGUI[180]:Lang.msgsGUI[190],Processor.getMessageError());
				}
				if (((ConfigureRisaDialog)jConfigureRisaDialog).getCBGraphicIsSelected()){
					rod = (RodadaDeSimulacaoRisa)pProc.setRodadaRisa.get(0);
					rod.setNroInstructionsFetched((int)((MIPS)pProc).get("NroInstFetched",FIELD));
					rod.setExecutionCycles((int)((MIPS)pProc).get("ExecutionCycles", FIELD));
					rod.setTempoDeSimulacao(tempodesimulacao);
					pProc.setRodadaRisa.set(rodada, rod);
				}
				atualizaVisualizacao();
			}
			if (risas[6] == true){
System.out.println ( "==> 6");
				rodada++;
				if (((RecrISADialog)((ConfigureRisaDialog)jConfigureRisaDialog).cr).getOpcodes() == 8){
					((MIPS)pProc).set("TDCF_8or16", STATUSorCONF, 0);
				}else{
					((MIPS)pProc).set("TDCF_8or16", STATUSorCONF, 1);
				}
				((MIPS)pProc).setInstructionSetIntoMatriz(((RecrISADialog)((ConfigureRisaDialog)jConfigureRisaDialog).cr).getInstructions());
				((MIPS)pProc).set("TDCF_UsingRISA", STATUSorCONF, 1);
				((MIPS)pProc).set("TDCF_WhichRISA", STATUSorCONF, 5);
				try{
					MontadorMIPS mm = new MontadorMIPS((MIPS)pProc, sPathName, sFilePath);
				} catch (Exception e){
					e.printStackTrace();
				}
				try{
					((MIPS)pProc).setReset(sFileTarget);
					File f = new File(sFilePath);
					f.delete();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try{
					tempodesimulacao = sSim.getTimeStatistics((MIPS)pProc);
				} catch ( Exception exc) {
					exc.printStackTrace();
					MIPSApp.messagesToUser(Lang.iLang==ENGLISH?Lang.msgsGUI[180]:Lang.msgsGUI[190],Processor.getMessageError());
				}
				if (((ConfigureRisaDialog)jConfigureRisaDialog).getCBGraphicIsSelected()){
					rod = (RodadaDeSimulacaoRisa)pProc.setRodadaRisa.get(0);
					rod.setNroInstructionsFetched((int)((MIPS)pProc).get("NroInstFetched",FIELD));
					rod.setExecutionCycles((int)((MIPS)pProc).get("ExecutionCycles", FIELD));
					rod.setTempoDeSimulacao(tempodesimulacao);
					pProc.setRodadaRisa.set(rodada, rod);
				}
				atualizaVisualizacao();
			}
			if (risas[7] == true){
System.out.println ( "==> 7");

				rodada++;
				//((MIPS)pProc).set("TDCF_8or16", STATUSorCONF, 0);
				// ((MIPS)pProc).updateRisaInstructionMatrix();
				((MIPS)pProc).set("TDCF_UsingRISA", STATUSorCONF, 1);
				//((MIPS)pProc).set("TDCF_WhichRISA", STATUSorCONF, 0);
				((MIPS)pProc).rISA.setReconfigurableRisa(true);
				try{
					MontadorMIPS mm = new MontadorMIPS((MIPS)pProc, sPathName, sFilePath);
				} catch (Exception e){
					e.printStackTrace();
				}
				try{
					((MIPS)pProc).setReset(sFileTarget);
					File f = new File(sFilePath);
					f.delete();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try{
					tempodesimulacao = sSim.getTimeStatistics((MIPS)pProc);
				} catch ( Exception exc) {
					exc.printStackTrace();
					MIPSApp.messagesToUser(Lang.iLang==ENGLISH?Lang.msgsGUI[180]:Lang.msgsGUI[190],Processor.getMessageError());
				}
				if (((ConfigureRisaDialog)jConfigureRisaDialog).getCBGraphicIsSelected()){
					rod = (RodadaDeSimulacaoRisa)pProc.setRodadaRisa.get(0);
					rod.setNroInstructionsFetched((int)((MIPS)pProc).get("NroInstFetched",FIELD));
					rod.setExecutionCycles((int)((MIPS)pProc).get("ExecutionCycles", FIELD));
					rod.setTempoDeSimulacao(tempodesimulacao);
					pProc.setRodadaRisa.set(rodada, rod);
				}
				atualizaVisualizacao();
				((MIPS)pProc).rISA.setReconfigurableRisa(false);
			} /* else { */
				//
				// Temporario para DSE
				//
/*
				for ( int it = 0; it < 26; it ++) {
					System.out.println ( "i = "+it);
					((MIPS)pProc).rISA.setRisaSet ( it);
					rodada++;
					//((MIPS)pProc).set("TDCF_8or16", STATUSorCONF, 0);
					// ((MIPS)pProc).updateRisaInstructionMatrix();
					((MIPS)pProc).set("TDCF_UsingRISA", STATUSorCONF, 1);
					//((MIPS)pProc).set("TDCF_WhichRISA", STATUSorCONF, 0);
					((MIPS)pProc).rISA.setReconfigurableRisa(true);
					try{
						MontadorMIPS mm = new MontadorMIPS((MIPS)pProc, sPathName, sFilePath);
					} catch (Exception e){
						e.printStackTrace();
					}
					try{
						((MIPS)pProc).setReset(sFileTarget);
						File f = new File(sFilePath);
						f.delete();
					} catch (Exception e) {
						e.printStackTrace();
					}
					try{
						//tempodesimulacao = sSim.getTimeStatistics((MIPS)pProc);
					} catch ( Exception exc) {
						exc.printStackTrace();
						MIPSApp.messagesToUser(Lang.iLang==ENGLISH?Lang.msgsGUI[180]:Lang.msgsGUI[190],Processor.getMessageError());
					}
					if (((ConfigureRisaDialog)jConfigureRisaDialog).getCBGraphicIsSelected()){
						//rod = (RodadaDeSimulacaoRisa)pProc.setRodadaRisa.get(0);
						//rod.setNroInstructionsFetched((int)((MIPS)pProc).get("NroInstFetched",FIELD));
						//rod.setExecutionCycles((int)((MIPS)pProc).get("ExecutionCycles", FIELD));
						//rod.setTempoDeSimulacao(tempodesimulacao);
						//pProc.setRodadaRisa.set(rodada, rod);
					}
					atualizaVisualizacao();
					((MIPS)pProc).rISA.setReconfigurableRisa(false);
				}		
			}
*/
			if (((ConfigureRisaDialog)jConfigureRisaDialog).getCBGraphicIsSelected()){
				showGraphic();
			}
		}
	}
	
	public void showGraphic(){
		boolean[] graphicOptions = ((ConfigureRisaDialog)jConfigureRisaDialog).getGraphicOptions();
		boolean[] risa = ((ConfigureRisaDialog)jConfigureRisaDialog).getSelectedRisas();
		ShowGraphicDialog sg = new ShowGraphicDialog(pProc, graphicOptions, risa);
		sg.setModal(true);
		sg.pack();
		sg.setVisible(true);
	}
	
	private void TimeDialog ( ) {
		double seconds = 0;
		try {
			seconds = sSim.getTimeStatistics( pProc);
		} catch ( Exception exc) {
			exc.printStackTrace();
			TDBenchApp.messagesToUser(Lang.iLang==ENGLISH?Lang.msgsGUI[180]:Lang.msgsGUI[190],Processor.getMessageError());
		}
		String sTmp = seconds + " seconds";
		TDBenchApp.messagesToUser( null, sTmp);
		atualizaVisualizacao ( );
	}
	
	private void promptBreakpointDialog ( ) {
		if ( jSetBreakDialog == null) {
			jSetBreakDialog = new SetBreakpointDialog ( sSim, pProc);				
			jSetBreakDialog.pack();
		}
		if ( jSetBreakDialog.isVisible ( ) == false) {
			jSetBreakDialog.setModal ( true);
			jSetBreakDialog.setVisible ( true);
			atualizaVisualizacao();
			try {
				tabbedPane.setSelectedIndex(5);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	//********************************************************************************************************
	// FIM - Reescreve o mtodo createTrackingMenu() da classe TDBenchApp, adicionando o menu "Simular com rISA..."
	//********************************************************************************************************
	
	/*protected JMenu createTrackingMenu() {
		CommandMenu menu = new CommandMenu(Lang.iLang==ENGLISH?Lang.msgsGUI[18]:Lang.msgsGUI[48]);
		Command cmd = new AbstractCommand(Lang.iLang==ENGLISH?Lang.msgsGUI[19]:Lang.msgsGUI[49], this, false) {
			public void execute() {
				promptSimulate();
			}
		};
		menu.add(cmd, new MenuShortcut('s'));

		cmd = new AbstractCommand(Lang.iLang==ENGLISH?Lang.msgsGUI[20]:Lang.msgsGUI[50], this, true) {
			public void execute() {
				promptSimulateDialog();
			}
		};
		menu.add(cmd, new MenuShortcut('i'));
		
		menu.add(new JSeparator());

		ButtonGroup bg = new ButtonGroup ( );
		JCheckBoxMenuItem cb1 = new JCheckBoxMenuItem ( new InstructionOrCycleAction ( Lang.iLang==ENGLISH?Lang.msgsGUI[25]:Lang.msgsGUI[55]));
		JCheckBoxMenuItem cb2 = new JCheckBoxMenuItem ( new InstructionOrCycleAction ( Lang.iLang==ENGLISH?Lang.msgsGUI[26]:Lang.msgsGUI[56]));

		bg.add(cb1);
		bg.add(cb2);
		bg.setSelected( cb1.getModel ( ), true);
		menu.add ( cb1);
		menu.add ( cb2);

		return menu;
	}

	protected JMenu createSteeringMenu() {
		CommandMenu menu = new CommandMenu(Lang.iLang==ENGLISH?Lang.msgsGUI[21]:Lang.msgsGUI[51]);

		Command cmd = new AbstractCommand(Lang.iLang==ENGLISH?Lang.msgsGUI[22]:Lang.msgsGUI[52], this, false) {
			public void execute() {
				promptSetPortDialog();
			}
		};
		menu.add(cmd, new MenuShortcut('s'));

		cmd = new AbstractCommand(Lang.iLang==ENGLISH?Lang.msgsGUI[23]:Lang.msgsGUI[53], this, false) {
			public void execute() {
				promptSetContentsDialog();
			}
		};
		menu.add(cmd, new MenuShortcut('e'));

		cmd = new AbstractCommand(Lang.iLang==ENGLISH?Lang.msgsGUI[24]:Lang.msgsGUI[54], this, false) {
			public void execute() {
				promptExecuteDialog();
			}
		};
		menu.add(cmd, new MenuShortcut('e'));

		return menu;
	}
		
*/
	protected void promptHelp ( ) {
		Help.help ( );
		tabbedPane.setSelectedIndex(3);
	}
	
	protected void promptRISA ( ) {
		MIPS nProc = (MIPS) pProc;
		nProc.listRisas ( );
		tabbedPane.setSelectedIndex(3);
	}
	
	protected void promptRisaFields ( ) {
		MIPS nProc = (MIPS) pProc;
		nProc.listFields ( );
		tabbedPane.setSelectedIndex(3);
	}

	/**
	 * Creates the file menu. Clients override this
	 * method to add additional menu items.
	 */
	protected JMenu createHelpMenu() {
		CommandMenu menu = new CommandMenu(Lang.iLang==ENGLISH?Lang.msgsGUI[200]:Lang.msgsGUI[205]);
		Command cmd;

		cmd = new AbstractCommand(Lang.iLang==ENGLISH?Lang.msgsGUI[201]:Lang.msgsGUI[206], this, true) {
			public void execute() {
				try {
					promptHelp();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		menu.add(cmd, new MenuShortcut('h'));
		
		cmd = new AbstractCommand(Lang.iLang==ENGLISH?Lang.msgsGUI[203]:Lang.msgsGUI[208], this, true) {
			public void execute() {
				try {
					promptRISA();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		menu.add(cmd, new MenuShortcut('r'));
		
		cmd = new AbstractCommand(Lang.iLang==ENGLISH?Lang.msgsGUI[220]:Lang.msgsGUI[225], this, true) {
			public void execute() {
				try {
					promptRisaFields();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		menu.add(cmd, new MenuShortcut('f'));
		
		return menu;
	}
	
	/*
	public void atualizaVisualizacao ( ) {
		LinkSimulatorVisualization f = null;
		ConnectionFigure cx = null;
		String strTmp;
		long lTmp;
		//if ( sSim.getTime() == 0.0) return;
		dv = view ( );
		dw = dv.drawing ( );
		fe = dw.figures ( );
		while ( fe.hasNextFigure()) {
			f = (LinkSimulatorVisualization) fe.nextFigure();
			f.refreshDisplay( sSim, pProc);
		}
		f = (LinkSimulatorVisualization) jMemContents;
		f.refreshDisplay( sSim, pProc);
		f = (LinkSimulatorVisualization) jRFContents;
		f.refreshDisplay( sSim, pProc);
		f = (LinkSimulatorVisualization) jDMemContents;
		f.refreshDisplay( sSim, pProc);
		f = (LinkSimulatorVisualization) jComponent;
		f.refreshDisplay( sSim, pProc);
		f = (LinkSimulatorVisualization) jSuperescalar;
		f.refreshDisplay( sSim, pProc);
//		f = (LinkSimulatorVisualization) jInstQueues;
//		f.refreshDisplay( sSim, pProc);
		setView ( dv);
	}

	private void promptSimulate ( ) {
		try {
			sSim.Simulate ( pProc);
			atualizaVisualizacao ( );
		} catch ( Exception e) {
			System.out.println ( "Erro no comando simulate!");
		}		
	}

	private void promptSimulateDialog ( ) {
			if ( jNroUnitsSim == null) {
				jNroUnitsSim = new SimulateDialog ( sSim, pProc);				
				jNroUnitsSim.pack();
			}
			if ( jNroUnitsSim.isVisible ( ) == false) {
				jNroUnitsSim.setModal ( true);
				jNroUnitsSim.setVisible ( true);
				atualizaVisualizacao();
			}
	}

	private void promptSetPortDialog ( ) {
			if ( jSetPortDialog == null) {
				jSetPortDialog = new SetPortDialog ( sSim, pProc);				
				jSetPortDialog.pack();
			}
			if ( jSetPortDialog.isVisible ( ) == false) {
				jSetPortDialog.setModal ( true);
				jSetPortDialog.setVisible ( true);
				atualizaVisualizacao();
			}
	}

	private void promptSetContentsDialog ( ) {
			if ( jSetContentsDialog == null) {
				jSetContentsDialog = new SetContentsDialog ( sSim, pProc);				
				jSetContentsDialog.pack();
			}
			if ( jSetContentsDialog.isVisible ( ) == false) {
				jSetContentsDialog.setModal ( true);
				jSetContentsDialog.setVisible ( true);
				atualizaVisualizacao();
			}
	}

	private void promptExecuteDialog ( ) {
			if ( jExecuteDialog == null) {
				jExecuteDialog = new ExecuteDialog ( sSim, pProc);				
				jExecuteDialog.pack();
			}
			if ( jExecuteDialog.isVisible ( ) == false) {
				jExecuteDialog.setModal ( true);
				jExecuteDialog.setVisible ( true);
				atualizaVisualizacao();
			}
	}
*/	
	private void promptFileName ( ) throws Exception {
		String sBasePath2 = Platform.treatPathNames(Processor.getProcessorName()+Platform.getSeparatorPath()+"programs" + Platform.getSeparatorPath() + "CSources" + Platform.getSeparatorPath() + "mibench");
		String sBasePath = Platform.treatPathNames(Processor.getProcessorName()+Platform.getSeparatorPath()+"programs");
		String sFilePath = sBasePath + Platform.getSeparatorPath()+"_dlxBin.bin";
		String sFileTarget = "programs"+ Platform.getSeparatorPath()+"_dlxBin.bin";
		jOFC.setCurrentDirectory( new File ( sBasePath2));

		int returnVal = jOFC.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			String pathName = jOFC.getCurrentDirectory().getAbsolutePath()+Platform.getSeparatorPath()+jOFC.getSelectedFile().getName();
			setPathName(pathName);
			System.out.println("You chose to open this file: " + pathName );
			MontadorMIPS mmn = new MontadorMIPS ((MIPS)pProc, pathName, sFilePath);
			try {
				((MIPS) pProc).setReset ( sFileTarget);				
				File f = new File ( sFilePath);
				f.delete ( );
				tabbedPane.setSelectedIndex(3);
			} catch (Exception e) {
				e.printStackTrace();
			}
			atualizaVisualizacao();
		}
	}
	
	private void setPathName(String PathName){
		sPathName = PathName;
	}
	
	private void promptReset ( ) {
		try {
			MIPS nProc = (MIPS) pProc;
			nProc.setReset ( null);
			tabbedPane.setSelectedIndex(3);
			atualizaVisualizacao ( );
		} catch ( Exception e) {
			System.out.println ( "Erro no comando reset!");
		}		
	}
/*
	public static void messagesToUser ( String parWhere, String parMsg) {
        if (parWhere != null) textArea.append("<"+parWhere+"> ==>   ");
        textArea.append(parMsg+"\n");
        //Make sure the new text is visible, even if there
        //was a selection in the text area.
        textArea.setCaretPosition(textArea.getDocument().getLength());		
	}
	
	public static void main(String[] args) {
		DrawApplication window = new DLXApp(null, null);

		window.setSize(1000,600); 
		window.view().setBackground(Color.white); 		
		window.open();
	}

	Simulator sSim;
	Processor pProc;
	Datapath dtpVis;
	DrawingView dv;
	Drawing dw;
	FigureEnumeration fe;
	JDialog jNroUnitsSim, jSetPortDialog, jSetContentsDialog, jExecuteDialog;
	JPanel jMemContents, jPipelineContents, jInstructions, jRFContents, jDMemContents, jComponent, jSuperescalar; // jInstQueues;
	JTextField fStatusLine;
	JTabbedPane tabbedPane;
	JFileChooser jOFC;
	static JTextArea textArea;
	// indica simulao de ciclos ou de instrues: true para ciclos
	private boolean bSimulate = true;*/
	JDialog jConfigureRisaDialog;
	String sPathName, sFilePath;
}
