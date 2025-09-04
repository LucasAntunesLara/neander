package simdraw;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
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

//import montador.MontadorNeander;

import montador.MontadorLc3;

import org.jhotdraw.application.DrawApplication;
import org.jhotdraw.contrib.Desktop;
import org.jhotdraw.framework.DrawingView;
import org.jhotdraw.standard.AbstractCommand;
import org.jhotdraw.standard.NullTool;
import org.jhotdraw.util.Command;
import org.jhotdraw.util.CommandMenu;
import org.jhotdraw.util.UndoManager;

import cseq.RegisterFile;

import platform.Lang;
import platform.Platform;
//import processor.Neander;
import processor.Processor;
import processor.lc3;
//import simdraw.NeanderApp.NeanderSimulateAction;
import simdraw.TDBenchApp.InstructionOrCycleAction;
import simdraw.dialogs.NewSimulateDialog;
import simdraw.figures.AdderFigure;
import simdraw.figures.AluFigure;
import simdraw.figures.CircuitBusFigure;
import simdraw.figures.ConnectionFigure;
import simdraw.figures.GenericInOutFigure;
import simdraw.figures.GenericMultiplexerFigure;
import simdraw.figures.GenericRegisterBankFigure;
import simdraw.figures.LogicFigure;
import simdraw.figures.MemoryFigure;
import simdraw.figures.MultiplexerFigure;
import simdraw.figures.MultiplexerV2Figure;
import simdraw.figures.RegisterFigure;
import simdraw.figures.RegisterV2Figure;
import simdraw.figures.TimerFigure;
import simdraw.panels.ComponentPanel;
import simdraw.panels.InstructionsPanel;
import simdraw.panels.MemoryPanel;
import simdraw.panels.PipelinePanel;
import simulator.Simulator;
import util.SistNum;

public class lc3App extends TDBenchApp {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	class lc3SimulateAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public lc3SimulateAction(	lc3App parNa) {
			//super("Sim", null);
			na = parNa;
		}
		public void actionPerformed(ActionEvent e) {
			if ( bSimulate == true) na.sSim.Simulate ( na.pProc);
			else {
				((lc3) na.pProc).resetIsNewInstruction();
				while ( ((lc3) na.pProc).isNewInstruction () == false) {
					na.sSim.Simulate ( na.pProc);
				}
			}
			na.atualizaVisualizacao ( );
		}
		
		lc3App na;
	}

	public void reset ( Simulator parSim, Processor parProc) {
		sSim = parSim;
		pProc = parProc;
		dtpVis = pProc.getDatapath ( );
		jNroUnitsSim = null;		
		jMemContents = null;
		jPipelineContents = null;
		jInstructions = null;
		jComponent = null;
		createPanels ( );
		resetDialogs ( );
	}
	
	public lc3App(Simulator parSim, Processor parProc) {
		super( Lang.iLang==ENGLISH?Lang.msgsGUI[0]:Lang.msgsGUI[30]);
		sSim = parSim;
		pProc = parProc;
		dtpVis = pProc.getDatapath ( );
		jNroUnitsSim = null;
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();
		int screenHeight = d.height;
		int screenWidth = d.width;
		setSize(screenWidth, screenHeight);
		
		SistNum.setDefaultSizeFormat( HALFWORD);
		Lang.iLang = Lang.getDefaultLanguage();
	}
	
	private void createBlockDiagram ( ) {
		
		TimerFigure t = new TimerFigure ( Lang.iLang==ENGLISH?Lang.msgsGUI[235]:Lang.msgsGUI[255], 25);
		t.setBaseSize(25, 25);
		this.view().drawing().add ( t);
		
		RegisterFigure mar = new RegisterFigure ( "MAR", "E1", "S1");
		this.view().drawing().add ( mar);
		
		RegisterFigure mdr = new RegisterFigure ( "MDR", "E1", "S1");
		this.view().drawing().add ( mdr);
		
		MemoryFigure mem = new MemoryFigure ( "MEM", "E1", "E2", "S1");
		this.view().drawing().add ( mem);
		
		RegisterFigure ir = new RegisterFigure ( "IR", "E1", "S1");
		ir.setBaseSize ( 28, 10);
		this.view().drawing().add ( ir);
		
		AluFigure alu = new AluFigure ( "ALU", "E1", "E2", "S1", "OP");
		alu.setBaseSize ( 32, 20);
		this.view().drawing().add ( alu);
		
		GenericMultiplexerFigure sr2mux = new GenericMultiplexerFigure ( "SR2MUX", pProc);
		sr2mux.setBaseSize ( 40, 36);
		this.view().drawing().add ( sr2mux);
		
		GenericInOutFigure zext = new GenericInOutFigure ( "ZEXT", "E0", "S0");
		zext.setBaseSize ( 28, 10);
		this.view().drawing().add ( zext);
		
		GenericInOutFigure sext1 = new GenericInOutFigure ( "SEXT1", "E0", "S0");
		sext1.setBaseSize ( 40, 5);
		this.view().drawing().add ( sext1);
		
		GenericInOutFigure sext2 = new GenericInOutFigure ( "SEXT2", "E0", "S0");
		sext2.setBaseSize ( 40, 5);
		this.view().drawing().add ( sext2);
		
		GenericInOutFigure sext3 = new GenericInOutFigure ( "SEXT3", "E0", "S0");
		sext3.setBaseSize ( 40, 5);
		this.view().drawing().add ( sext3);
		
		GenericInOutFigure sext4 = new GenericInOutFigure ( "SEXT4", "E0", "S0");
		sext4.setBaseSize ( 40, 5);
		this.view().drawing().add ( sext4);
		
		GenericMultiplexerFigure addr1mux = new GenericMultiplexerFigure ( "ADDR1MUX", pProc);
		addr1mux.setBaseSize ( 55, 36);
		this.view().drawing().add ( addr1mux);
		
		GenericMultiplexerFigure addr2mux = new GenericMultiplexerFigure ( "ADDR2MUX", pProc);
		addr2mux.setBaseSize ( 55, 36);
		this.view().drawing().add ( addr2mux);
		
		GenericMultiplexerFigure marmux = new GenericMultiplexerFigure ( "MARMUX", pProc);
		marmux.setBaseSize ( 48, 36);
		this.view().drawing().add ( marmux);

		RegisterFigure pc = new RegisterFigure ( "PC", "E1", "S1");
		this.view().drawing().add ( pc);
		
		GenericMultiplexerFigure pcmux = new GenericMultiplexerFigure ( "PCMUX", pProc);
		pcmux.setBaseSize ( 48, 36);
		this.view().drawing().add ( pcmux);
		
		AdderFigure plus1 = new AdderFigure ( "PLUS_1", "E1", "E2", "S1");
		this.view().drawing().add ( plus1);
		
		AdderFigure adder = new AdderFigure ( "ADDER", "E1", "E2", "S1");
		adder.setBaseSize ( 28, 26);
		this.view().drawing().add ( adder);
		
		GenericRegisterBankFigure regfile = new GenericRegisterBankFigure( "REGFILE", pProc);
		regfile.setBaseSize(50, 50);
		this.view().drawing().add(regfile);
		
		CircuitBusFigure mainbus = new CircuitBusFigure( "BUS", pProc);
		mainbus.setBaseSize(18, 480);
		this.view().drawing().add(mainbus);
		
		GenericMultiplexerFigure mdrmux = new GenericMultiplexerFigure ( "MDRMUX", pProc);
		mdrmux.setBaseSize ( 48, 36);
		this.view().drawing().add ( mdrmux);
		
		LogicFigure logic = new LogicFigure ( "LOGIC", pProc);
		logic.setBaseSize(30, 80);
		this.view().drawing().add( logic);
		
		RegisterFigure n = new RegisterFigure ( "N", "E1", "S1");
		n.setBaseSize ( 30, 7);
		this.view().drawing().add ( n);
		
		RegisterFigure z = new RegisterFigure ( "Z", "E1", "S1");
		z.setBaseSize ( 30, 7);
		this.view().drawing().add ( z);
		
		RegisterFigure p = new RegisterFigure ( "P", "E1", "S1");
		p.setBaseSize ( 30, 7);
		this.view().drawing().add ( p);

		ConnectionFigure cx;
		
		cx = createConnection ( this.view(),"PCMUX","S0","E1",pcmux,pc);
		cx.insertPointAt ( new Point ( 448, 146), 1);
		cx.insertPointAt ( new Point ( 352, 146), 2);
		
		cx = createConnection ( this.view(),"PC","S1","E1",pc,plus1);
		
		cx = createConnection ( this.view(),"PC","S1","E1",pc,addr1mux);
		cx.insertPointAt ( new Point ( 482, 100), 1);
		cx.insertPointAt ( new Point ( 482, 368), 2);
		cx.insertPointAt ( new Point ( 326, 368), 3);
		
		cx = createConnection ( this.view(),"PC","S1","E1",pc,mainbus);
		cx.insertPointAt ( new Point ( 448, 21), 1);
		cx.insertPointAt ( new Point ( 749, 21), 2);
		cx.insertPointAt ( new Point ( 749, 252), 3);
		
		cx = createConnection ( this.view(),"PLUS_1","S1","E0",plus1,pcmux);
		cx.insertPointAt ( new Point ( 335, 28), 1);
		cx.insertPointAt ( new Point ( 335, 182), 2);
		
		cx = createConnection ( this.view(),"ADDER","S1","E1",adder,pcmux);
		cx.insertPointAt ( new Point ( 280, 174), 1);
		cx.insertPointAt ( new Point ( 280, 200), 2);

		cx = createConnection ( this.view(),"ADDER","S1","E1",adder,marmux);
		cx.insertPointAt ( new Point ( 182, 174), 1);
		
		cx = createConnection ( this.view(),"ZEXT","S0","E0",zext,marmux);
		cx.insertPointAt ( new Point ( 78, 88), 1);
		
		cx = createConnection ( this.view(),"IR","S1","E0",ir,sext1);
		
		cx = createConnection ( this.view(),"IR","S1","E0",ir,sext2);
		cx.insertPointAt ( new Point ( 78, 378), 1);
		cx.insertPointAt ( new Point ( 22, 378), 2);
		
		cx = createConnection ( this.view(),"IR","S1","E0",ir,sext3);
		cx.insertPointAt ( new Point ( 78, 378), 1);
		cx.insertPointAt ( new Point ( 22, 378), 2);
		
		cx = createConnection ( this.view(),"IR","S1","E0",ir,sext4);
		cx.insertPointAt ( new Point ( 78, 378), 1);
		cx.insertPointAt ( new Point ( 22, 378), 2);
		
		cx = createConnection ( this.view(),"IR","S1","E0",ir,zext);
		cx.insertPointAt ( new Point ( 78, 378), 1);
		cx.insertPointAt ( new Point ( 22, 378), 2);
		
		cx = createConnection ( this.view(),"SEXT1","S0","E1",sext1,sr2mux);
		cx.insertPointAt ( new Point ( 510, 400), 1);
		
		cx = createConnection ( this.view(),"SEXT2","S0","E2",sext2,addr2mux);
		
		cx = createConnection ( this.view(),"SEXT3","S0","E1",sext3,addr2mux);
		
		cx = createConnection ( this.view(),"SEXT4","S0","E0",sext4,addr2mux);
		
		cx = createConnection ( this.view(),"ADDR1MUX","S0","E2",addr1mux,adder);
		cx.insertPointAt ( new Point ( 436, 255), 1);
		cx.insertPointAt ( new Point ( 264, 255), 2);
		
		cx = createConnection ( this.view(),"ADDR2MUX","S0","E1",addr2mux,adder);
		
		cx = createConnection ( this.view(),"SR2MUX","S0","E1",sr2mux,alu);
		cx.insertPointAt ( new Point ( 638, 200), 1);
		
		cx = createConnection ( this.view(),"REGFILE","S1","E0",regfile,sr2mux);
		cx.insertPointAt ( new Point ( 680, 143), 1);
		cx.insertPointAt ( new Point ( 510, 143), 2);
		
		cx = createConnection ( this.view(),"REGFILE","S0","E2",regfile,alu);
		cx.insertPointAt ( new Point ( 702, 71), 1);
		
		cx = createConnection ( this.view(),"REGFILE","S0","E0",regfile,addr1mux);
		cx.insertPointAt ( new Point ( 702, 71), 1);
		cx.insertPointAt ( new Point ( 702, 240), 2);
		cx.insertPointAt ( new Point ( 326, 240), 3);
		
		cx = createConnection ( this.view(),"MAR","S1","E2",mar,mem);
		
		cx = createConnection ( this.view(),"MEM","S1","E1",mem,mdrmux);
		
		cx = createConnection ( this.view(),"MDRMUX","S0","E1",mdrmux,mdr);
		
		cx = createConnection ( this.view(),"MDR","S1","E3",mdr,mainbus);
		cx.insertPointAt ( new Point ( 752, 488), 1);
		
		cx = createConnection ( this.view(),"MDR","S1","E1",mdr,mem);
		cx.insertPointAt ( new Point ( 648, 596), 1);
		
		cx = createConnection ( this.view(),"BUS","S4","E0",mainbus,mdrmux);
		cx.insertPointAt ( new Point ( 791, 402), 1);
		cx.insertPointAt ( new Point ( 791, 599), 2);
		cx.insertPointAt ( new Point ( 366, 599), 3);
		cx.insertPointAt ( new Point ( 366, 476), 4);
		
		cx = createConnection ( this.view(),"BUS","S2","E1",mainbus,ir);
		cx.insertPointAt ( new Point ( 797, 265), 1);
		cx.insertPointAt ( new Point ( 797, 605), 2);
		cx.insertPointAt ( new Point ( 22, 605), 3);
		
		cx = createConnection ( this.view(),"BUS","S3","E1",mainbus,mar);
		cx.insertPointAt ( new Point ( 794, 334), 1);
		cx.insertPointAt ( new Point ( 794, 602), 2);
		cx.insertPointAt ( new Point ( 52, 602), 3);
		
		cx = createConnection ( this.view(),"BUS","S0","E0",mainbus,regfile);
		cx.insertPointAt ( new Point ( 788, 18), 1);
		cx.insertPointAt ( new Point ( 580, 18), 2);
		
		cx = createConnection ( this.view(),"BUS","S1","E2",mainbus,pcmux);
		cx.insertPointAt ( new Point ( 791, 197), 1);
		cx.insertPointAt ( new Point ( 791, 15), 2);
		cx.insertPointAt ( new Point ( 304, 15), 3);
		cx.insertPointAt ( new Point ( 304, 218), 4);
		
		cx = createConnection ( this.view(),"BUS","S5","E0",mainbus,logic);
		cx.insertPointAt ( new Point ( 788, 593), 1);
		cx.insertPointAt ( new Point ( 530, 593), 2);
		
		cx = createConnection ( this.view(),"LOGIC","S0","E1",logic, n);
		
		cx = createConnection ( this.view(),"LOGIC","S1","E1",logic, z);
		
		cx = createConnection ( this.view(),"LOGIC","S2","E1",logic, p);
		
		cx = createConnection ( this.view(),"ALU","S1","E2",alu,mainbus);
		
		cx = createConnection ( this.view(),"MARMUX","S0","E0",marmux,mainbus);
		cx.insertPointAt ( new Point ( 278, 12), 1);
		cx.insertPointAt ( new Point ( 752, 12), 2);
		cx.insertPointAt ( new Point ( 752, 156), 3);
		
		t.moveBy ( 80,30);
		mar.moveBy( 100, 500);
		mdr.moveBy( 600, 488);
		mem.moveBy( 250, 500);
		ir.moveBy(50, 400);
		alu.moveBy(670, 308);
		sr2mux.moveBy(550, 200);
		zext.moveBy(50, 250);
		sext1.moveBy(400, 400);
		sext2.moveBy(62, 350);
		sext3.moveBy(62, 320);
		sext4.moveBy(62, 290);
		addr1mux.moveBy(381, 312);
		addr2mux.moveBy(181, 312);
		marmux.moveBy(230, 100);
		pc.moveBy(400, 100);
		plus1.moveBy(500, 64);
		pcmux.moveBy(400, 200);
		adder.moveBy(250, 200);
		regfile.moveBy(630, 80);
		mainbus.moveBy ( 770, 300);
		mdrmux.moveBy(450, 488);
		logic.moveBy( 560, 400);
		n.moveBy( 650, 370);
		z.moveBy( 650, 400);
		p.moveBy( 650, 430);
	
	}
	
	private void createPanels ( ) {
		if ( jMemContents == null) {
			jMemContents = new MemoryPanel ( sSim, pProc, "MEM", null);				
		}
		if ( jPipelineContents == null) {
			jPipelineContents = new PipelinePanel ( sSim, pProc, null);				
		}	
		if ( jInstructions == null) {
			jInstructions = new InstructionsPanel ( sSim, pProc, null);				
		}	
		if ( jComponent == null) {
			jComponent = new ComponentPanel ( sSim, pProc);				
		}	

		if (tabbedPane != null) getContentPane().remove(tabbedPane);
		else {
			setDesktopListener(createDesktopListener());
			setDesktop(createDesktop());
		}
		tabbedPane = new JTabbedPane();
	
		JPanel activePanel = new JPanel();
		activePanel.setAlignmentX(LEFT_ALIGNMENT);
		activePanel.setAlignmentY(TOP_ALIGNMENT);
		activePanel.setLayout(new BorderLayout());
		
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();
		int screenHeight = d.height;
		int screenWidth = d.width;
		jMemContents.setPreferredSize( new Dimension ( (int) (screenWidth*0.170), screenHeight));
		
		activePanel.add(jMemContents, BorderLayout.EAST);
		activePanel.add((Component)getDesktop(), BorderLayout.CENTER);	
		
		JPanel controlPanel = new JPanel ( );
		controlPanel.setLayout( new BorderLayout ( 5, 5));
		controlPanel.add(jPipelineContents, BorderLayout.CENTER);
		controlPanel.add(jInstructions, BorderLayout.EAST);
		
		// Nao ha' necessidade de criar novamente
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

	protected void createTools(JToolBar palette) {
		// Comentando esta linha seguinte, desabilita a edicao das figuras do painel
		//super.createTools(palette);
		
		String sAux = Platform.treatPathNames(".\\images\\simulate.gif");

		JButton jSim = new JButton ( new lc3SimulateAction ( this));
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
			if ( bSimulate == true) sSim.Simulate ( pProc);
			else {
				((lc3) pProc).resetIsNewInstruction();
				while ( ((lc3) pProc).isNewInstruction () == false) {
					sSim.Simulate ( pProc);
				}
			}
			atualizaVisualizacao ( );
		} catch ( Exception e) {
			System.out.println ( "Erro no comando simulate!");
		}		
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
				if ( bSimulate == true) {
					for ( int k = 0; k < iUnits; k ++) sSim.Simulate ( pProc);
				} else {
					for ( int k = 0; k < iUnits; k ++) {
						((lc3) pProc).resetIsNewInstruction();
						while ( ((lc3) pProc).isNewInstruction () == false) {
							sSim.Simulate ( pProc);
						}
					}
				}
				atualizaVisualizacao();
			}
	}

	private void promptFileName ( ) throws Exception {
		
		String sBasePath = Platform.treatPathNames(Processor.getProcessorName()+Platform.getSeparatorPath()+"programs");
		String sFilePath = sBasePath + Platform.getSeparatorPath()+"_tempBin.bin";
		String sFileTarget = "programs"+ Platform.getSeparatorPath()+"_tempBin.bin";
		jOFC.setCurrentDirectory( new File ( sBasePath));

		int returnVal = jOFC.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			String pathName = jOFC.getCurrentDirectory().getAbsolutePath()+Platform.getSeparatorPath()+jOFC.getSelectedFile().getName();
			System.out.println("You chose to open this file: " + pathName );
			MontadorLc3 mmn = new MontadorLc3 (pProc, pathName, sFilePath,"MEM");
			try {
				((lc3) pProc).setReset ( sFileTarget);				
				File f = new File ( sFilePath);
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
			lc3 nProc = (lc3) pProc;
			nProc.setReset ( null);
			tabbedPane.setSelectedIndex(3);
			atualizaVisualizacao ( );
		} catch ( Exception e) {
			System.out.println ( "Erro no comando reset!");
		}		
	}
}
