/*
 * @(#)NetApp.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package simdraw;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import montador.MontadorAMIPS;

import org.jhotdraw.contrib.Desktop;
import org.jhotdraw.framework.DrawingView;
import org.jhotdraw.standard.AbstractCommand;
import org.jhotdraw.standard.NullTool;
import org.jhotdraw.util.Command;
import org.jhotdraw.util.CommandMenu;
import org.jhotdraw.util.UndoManager;

import platform.Lang;
import processor.Processor;
import processor.acesMIPS;
import simdraw.figures.ConnectionFigure;
import simdraw.figures.GenericFigure;
import simdraw.figures.GenericMultiplexerFigure;
import simdraw.figures.GenericRegisterBankFigure;
import simdraw.figures.MemoryFigure;
import simdraw.figures.MultiplexerFigure;
import simdraw.figures.PipeRegisterFigure;
import simdraw.figures.QueueFigure;
import simdraw.figures.RegisterFigure;
import simdraw.figures.TimerFigure;
import simdraw.panels.ComponentPanel;
import simdraw.panels.InstructionQueuesPanel;
import simdraw.panels.MemoryPanel;
import simdraw.panels.ProcessorPanel;
import simdraw.panels.SuperescalarPanel;
import simulator.Simulator;
import util.Define;
import util.SistNum;

/**
 * @version <$CURRENT_VERSION$>
 */
public  class acesMIPSApp extends TDBenchApp implements Define {

	class AMipsSimulateAction extends AbstractAction {
		public AMipsSimulateAction(	acesMIPSApp parNa) {
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
		
		acesMIPSApp na;
	}
/*
	class DecimalNumericSystemAction extends AbstractAction {
		public DecimalNumericSystemAction(	acesMIPSApp parNa, String sName) {
			super ( sName);
			na = parNa;
		}
		public void actionPerformed(ActionEvent e) {
			SistNum.setDefaultNumberFormat ( "d");
			System.out.println ( "Choose decimal!");
			na.atualizaVisualizacao ( );
		}
		
		acesMIPSApp na;
	}

	class HexaDecimalNumericSystemAction extends AbstractAction {
		public HexaDecimalNumericSystemAction(	acesMIPSApp parNa, String sName) {
			super ( sName);
			na = parNa;
		}
		public void actionPerformed(ActionEvent e) {
			SistNum.setDefaultNumberFormat ( "h");
			System.out.println ( "Choose hexa decimal!");
			na.atualizaVisualizacao ( );
		}
		
		acesMIPSApp na;
	}
	
	class BinaryNumericSystemAction extends AbstractAction {
		public BinaryNumericSystemAction(	acesMIPSApp parNa,String sName) {
			super ( sName);
			na = parNa;
		}
		public void actionPerformed(ActionEvent e) {
			SistNum.setDefaultNumberFormat ( "b");
			System.out.println ( "Choose binary!");
			na.atualizaVisualizacao ( );
		}
		
		acesMIPSApp na;
	}
*/	
	public acesMIPSApp(Simulator parSim, Processor parProc) {
		super("acesMIPS - Processor Model");
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
		jInstQueues = null;
		jProcessor = null;
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
		TimerFigure t = new TimerFigure ( "acesMIPS", 20);
		this.view().drawing().add ( t);
		
		RegisterFigure pc = new RegisterFigure ( "pc", "E1", "S1");
		pc.setBaseSize ( 36, 8);
		this.view().drawing().add ( pc);
		
		MemoryFigure imem = new MemoryFigure ( "imemory", "E1", "E2", "S1");
		imem.setBaseSize ( 20, 36);
		this.view().drawing().add ( imem);
		
		GenericRegisterBankFigure gRegisters = new GenericRegisterBankFigure ( "GPRFile", pProc);
		gRegisters.setBaseSize ( 48, 96);
		this.view().drawing().add ( gRegisters);
		
		QueueFigure qf = new QueueFigure ( "fetched", pProc);
		qf.setBaseSize ( 24, 24);
		this.view().drawing().add ( qf);

		GenericMultiplexerFigure mx1 = new GenericMultiplexerFigure ( "mx1", pProc);
		mx1.setBaseSize ( 16, 24);
		this.view().drawing().add ( mx1);

		GenericMultiplexerFigure mx2 = new GenericMultiplexerFigure ( "mx2", pProc);
		mx2.setBaseSize ( 16, 24);
		this.view().drawing().add ( mx2);

		GenericMultiplexerFigure mx3 = new GenericMultiplexerFigure ( "mx3", pProc);
		mx3.setBaseSize ( 16, 24);
		this.view().drawing().add ( mx3);

		MultiplexerFigure alu1 = new MultiplexerFigure ( "ALU1_EX", "E1", "E2", "S1", "OP");
		alu1.setBaseSize ( 36, 36);
		this.view().drawing().add ( alu1);
		
		MultiplexerFigure alu2 = new MultiplexerFigure ( "ALU2_EX", "E1", "E2", "S1", "OP");
		alu2.setBaseSize ( 36, 36);
		this.view().drawing().add ( alu2);

		MemoryFigure l1 = new MemoryFigure ( "dCacheL1", "E1", "E2", "S1");
		l1.setBaseSize ( 25, 25);
		this.view().drawing().add ( l1);

		MemoryFigure l2 = new MemoryFigure ( "uCacheL2", "E1", "E2", "S1");
		l2.setBaseSize ( 25, 25);
		this.view().drawing().add ( l2);

		MemoryFigure mem = new MemoryFigure ( "MainMem", "E1", "E2", "S1");
		mem.setBaseSize ( 25, 25);
		this.view().drawing().add ( mem);

		GenericFigure gfComp = new GenericFigure ( "comp", pProc);
		gfComp.setBaseSize ( 40, 40);
		this.view().drawing().add ( gfComp);
		
		QueueFigure qfwb = new QueueFigure ( "writeback", pProc);
		qfwb.setBaseSize ( 30, 24);
		this.view().drawing().add ( qfwb);

		GenericRegisterBankFigure gFPRegisters = new GenericRegisterBankFigure ( "FPRFile", pProc);
		gFPRegisters.setBaseSize ( 48, 96);
		this.view().drawing().add ( gFPRegisters);
		
		MultiplexerFigure fpu = new MultiplexerFigure ( "FPUNIT", "E0","E1","S0","C0");
		fpu.setBaseSize ( 36, 36);
		this.view().drawing().add ( fpu);

		RegisterFigure hi = new RegisterFigure ( "hi", "E0", "S0");
		hi.setBaseSize ( 36, 6);
		this.view().drawing().add ( hi);

		RegisterFigure lo = new RegisterFigure ( "lo", "E0", "S0");
		lo.setBaseSize ( 36, 6);
		this.view().drawing().add ( lo);

		RegisterFigure $ra = new RegisterFigure ( "$ra", "E0", "S0");
		$ra.setBaseSize ( 36, 6);
		this.view().drawing().add ( $ra);
		//
		PipeRegisterFigure busd = new PipeRegisterFigure ( "DBUS", "E0", "S0", "E1", "S1", "E2", "S2", "E3", "S3");
		this.view().drawing().add ( busd);
		busd.setBaseSize ( 10, 150);
		//
		PipeRegisterFigure buse = new PipeRegisterFigure ( "ABUS", "E0", "S0", "E1", "S1", "E2", "S2", "E3", "S3");
		this.view().drawing().add ( buse);
		buse.setBaseSize ( 10, 150);		
		//
		ConnectionFigure cx;
				
		cx = createConnection ( this.view(),"pc","S1","E2",pc,imem);

		cx = createConnection ( this.view(),"GPRFile","S0","E1",gRegisters,alu1);
		cx = createConnection ( this.view(),"GPRFile","S1","E0",gRegisters,mx1);
		cx = createConnection ( this.view(),"mx1","S0","E2",mx1,alu1);
		
		cx = createConnection ( this.view(),"GPRFile","S2","E1",gRegisters,alu2);
		cx = createConnection ( this.view(),"GPRFile","S3","E0",gRegisters,mx2);
		cx = createConnection ( this.view(),"mx2","S0","E2",mx2,alu2);
		
//		cx = createConnection ( this.view(),"GPRFile","S4","E1",gRegisters,dmem);
//		cx.insertPointAt ( new Point ( 570, 270), 1);
		
		cx = createConnection ( this.view(),"GPRFile","S6","E0",gRegisters,gfComp);
		cx.insertPointAt ( new Point ( 560, 291), 1);
		cx = createConnection ( this.view(),"GPRFile","S7","E0",gRegisters,mx3);
		cx = createConnection ( this.view(),"mx3","S0","E1",mx3,gfComp);
		
		cx = createConnection ( this.view(),"FPRFile","S0","E0",gFPRegisters,fpu);
		cx = createConnection ( this.view(),"FPRFile","S1","E1",gFPRegisters,fpu);
		
		t.moveBy ( 85,30);
		pc.moveBy( 44, 300);
		imem.moveBy ( 120, 180);
		qf.moveBy ( 225, 180);
		gRegisters.moveBy( 380,110);
		qfwb.moveBy ( 950, 150);
		gFPRegisters.moveBy( 380,350);
		mx1.moveBy ( 550, 75);
		mx2.moveBy ( 550, 165);
		mx3.moveBy ( 550, 318);

		alu1.moveBy ( 650, 45);
		alu2.moveBy ( 650, 135);
		l1.moveBy ( 900, 220);
		l2.moveBy ( 915, 310);
		mem.moveBy ( 930, 400);
		gfComp.moveBy ( 650, 305);
		fpu.moveBy ( 650, 395);
		
		hi.moveBy( 750, 25);
		lo.moveBy( 750, 75);
		$ra.moveBy( 850, 25);
//
		busd.moveBy ( 760, 295);
		buse.moveBy ( 810, 275);		
	}

	private void createPanels ( ) {
		if ( jMemContents == null) {
			jMemContents = new MemoryPanel ( sSim, pProc, "imemory", "pc");				
		}
		if ( jSuperescalar == null) {
			jSuperescalar = new SuperescalarPanel ( sSim, pProc);
		}
		if ( jComponent == null) {
			jComponent = new ComponentPanel ( sSim, pProc);				
		}	
		if ( jInstQueues == null) {
			jInstQueues = new InstructionQueuesPanel ( sSim, pProc);
		}
		if ( jProcessor == null) {
			jProcessor = new ProcessorPanel ( sSim, pProc);
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
		pContents.add ( jProcessor);
		pContents.add ( jMemContents);

		JPanel activePanel = new JPanel();
		activePanel.setAlignmentX(LEFT_ALIGNMENT);
		activePanel.setAlignmentY(TOP_ALIGNMENT);
		activePanel.setLayout(new BorderLayout());
		activePanel.add((Component)getDesktop(), BorderLayout.CENTER);

		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();
		int screenHeight = d.height;
		int screenWidth = d.width;
		pContents.setPreferredSize( new Dimension ( screenWidth, (int) (screenHeight*0.20)));
		activePanel.add(pContents, BorderLayout.SOUTH);
		
		JPanel controlPanel = new JPanel ( );
		controlPanel.setLayout( new BorderLayout ( 5, 5));
		controlPanel.add(jSuperescalar, BorderLayout.CENTER);

		JPanel controlPanel2 = new JPanel ( );
		controlPanel2.setLayout( new BorderLayout ( 5, 5));
		controlPanel2.add(jInstQueues, BorderLayout.CENTER);

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
/*
		tabbedPane.addTab("Block Diagram", null, activePanel, "Block Diagram");
		tabbedPane.setSelectedIndex(0);
		tabbedPane.addTab("Execution Paths", null, controlPanel, "Pipeline and Instructions");
		tabbedPane.addTab("Datapath Components", null, jComponent, "Components");
		tabbedPane.addTab("Instruction Queues", null, controlPanel2, "Instruction Queues");
*/
		tabbedPane.addTab(Lang.iLang==ENGLISH?Lang.msgsGUI[2]:Lang.msgsGUI[32], null, activePanel, Lang.iLang==ENGLISH?Lang.msgsGUI[3]:Lang.msgsGUI[33]);
		tabbedPane.setSelectedIndex(0);
		tabbedPane.addTab(Lang.iLang==ENGLISH?Lang.msgsGUI[4]:Lang.msgsGUI[34], null, controlPanel, Lang.iLang==ENGLISH?Lang.msgsGUI[5]:Lang.msgsGUI[35]);
		tabbedPane.addTab(Lang.iLang==ENGLISH?Lang.msgsGUI[6]:Lang.msgsGUI[36], null, jComponent, Lang.iLang==ENGLISH?Lang.msgsGUI[7]:Lang.msgsGUI[37]);
		tabbedPane.addTab(Lang.iLang==ENGLISH?Lang.msgsGUI[8]:Lang.msgsGUI[38], null, scrollPane, Lang.iLang==ENGLISH?Lang.msgsGUI[9]:Lang.msgsGUI[39]);
		tabbedPane.addTab(Lang.iLang==ENGLISH?Lang.msgsGUI[28]:Lang.msgsGUI[58], null, controlPanel2, Lang.iLang==ENGLISH?Lang.msgsGUI[28]:Lang.msgsGUI[58]);

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
		
		JButton jSim = new JButton ( new AMipsSimulateAction ( this));
		jSim.setIcon ( new ImageIcon ( ".\\images\\simulate.gif"));
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

	private void promptFileName ( ) throws Exception {
		jOFC.setCurrentDirectory( new File ( Processor.getProcessorName()+"\\programs"));

		int returnVal = jOFC.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			String pathName = jOFC.getCurrentDirectory().getAbsolutePath()+"\\"+jOFC.getSelectedFile().getName();
			System.out.println("You chose to open this file: " + pathName);
			MontadorAMIPS mmn = new MontadorAMIPS (pProc, pathName, Processor.getProcessorName()+"\\programs\\_amipsBin.bin");
			try {
				((acesMIPS) pProc).setReset ( "programs\\_amipsBin.bin");				
				File f = new File ( Processor.getProcessorName()+"\\programs\\_amipsBin.bin");
				f.delete ( );
				tabbedPane.setSelectedIndex(3);
			} catch (Exception e) {
				e.printStackTrace();
			}
			atualizaVisualizacao();
		}
	}
	
	private void promptReset ( ) {
		try {
			acesMIPS nProc = (acesMIPS) pProc;
			nProc.setReset ( null);
			tabbedPane.setSelectedIndex(3);
			atualizaVisualizacao ( );
		} catch ( Exception e) {
			//e.printStackTrace();
			System.out.println ( "Erro no comando reset!");
		}		
	}

/*
	protected JMenu createViewMenu() {
		CommandMenu menu = new CommandMenu("View");
		ButtonGroup bg = new ButtonGroup ( );

		JRadioButtonMenuItem rbA = new JRadioButtonMenuItem ( new DecimalNumericSystemAction ( this, "Decimal"));
		JRadioButtonMenuItem rbB = new JRadioButtonMenuItem ( new HexaDecimalNumericSystemAction ( this, "Hexa"));
		JRadioButtonMenuItem rbC = new JRadioButtonMenuItem ( new BinaryNumericSystemAction ( this, "Binary"));

		bg.add(rbA);
		bg.add(rbB);
		bg.add(rbC);
		bg.setSelected( rbA.getModel ( ), true);
		menu.add ( rbA);
		menu.add ( rbB);
		menu.add ( rbC);

		return menu;
	}

	protected JMenu createTrackingMenu() {
		CommandMenu menu = new CommandMenu("Tracking");
		Command cmd = new AbstractCommand("Simulate", this, false) {
			public void execute() {
				promptSimulate();
			}
		};
		menu.add(cmd, new MenuShortcut('s'));

		cmd = new AbstractCommand("Simulate...", this, true) {
			public void execute() {
				promptSimulateDialog();
			}
		};
		menu.add(cmd, new MenuShortcut('i'));

		return menu;
	}

	protected JMenu createSteeringMenu() {
		CommandMenu menu = new CommandMenu("Steering");

		Command cmd = new AbstractCommand("Set processor...", this, false) {
			public void execute() {
				promptSetProcessorDialog();
			}
		};
		menu.add(cmd, new MenuShortcut('p'));

		cmd = new AbstractCommand("Set port value...", this, false) {
			public void execute() {
				promptSetPortDialog();
			}
		};
		menu.add(cmd, new MenuShortcut('s'));

		cmd = new AbstractCommand("Set contents...", this, false) {
			public void execute() {
				promptSetContentsDialog();
			}
		};
		menu.add(cmd, new MenuShortcut('e'));

		cmd = new AbstractCommand("Execute...", this, false) {
			public void execute() {
				promptExecuteDialog();
			}
		};
		menu.add(cmd, new MenuShortcut('e'));

		cmd = new AbstractCommand("Test...", this, false) {
			public void execute() {
				promptTimingAssocDialog();
			}
		};
		menu.add(cmd, new MenuShortcut('t'));

		return menu;
	}
		
	public void atualizaVisualizacao ( ) {
		LinkSimulatorVisualization f = null;
		ConnectionFigure cx = null;
		String strTmp;
		long lTmp;
		dv = view ( );
		dw = dv.drawing ( );
		fe = dw.figures ( );
		while ( fe.hasNextFigure()) {
			f = (LinkSimulatorVisualization) fe.nextFigure();
			f.refreshDisplay( sSim, pProc);
		}
		f = (LinkSimulatorVisualization) jComponent;
		f.refreshDisplay( sSim, pProc);
		f = (LinkSimulatorVisualization) jSuperescalar;
		f.refreshDisplay( sSim, pProc);
		f = (LinkSimulatorVisualization) jInstQueues;
		f.refreshDisplay( sSim, pProc);
		f = (LinkSimulatorVisualization) jProcessor;
		f.refreshDisplay( sSim, pProc);
		f = (LinkSimulatorVisualization) jMemContents;
		f.refreshDisplay( sSim, pProc);
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

	private void promptSetProcessorDialog ( ) {
		if ( jSetProcessorDialog == null) {
			jSetProcessorDialog = new SetProcessorDialog ( sSim, pProc);				
			jSetProcessorDialog.pack();
		}
		if ( jSetProcessorDialog.isVisible ( ) == false) {
			jSetProcessorDialog.setModal ( true);
			jSetProcessorDialog.setVisible ( true);
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

	private void promptTimingAssocDialog ( ) {
		if ( jTimingAssocDialog == null) {
			jTimingAssocDialog = new TimingAssocDialog ( sSim, pProc);				
			jTimingAssocDialog.pack();
		}
		if ( jTimingAssocDialog.isVisible ( ) == false) {
			jTimingAssocDialog.setModal ( true);
			jTimingAssocDialog.setVisible ( true);
			atualizaVisualizacao();
		}
	}
	
	public static void main(String[] args) {
		DrawApplication window = new acesMIPSApp(null, null);

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
	JDialog jNroUnitsSim, jSetProcessorDialog, jSetPortDialog, jSetContentsDialog, jExecuteDialog, jTimingAssocDialog;
	JPanel jMemContents, jPipelineContents, jInstructions, jRFContents, jDMemContents, jComponent;
	JPanel jSuperescalar, jInstQueues, jProcessor;
	JTextField fStatusLine;*/
}
