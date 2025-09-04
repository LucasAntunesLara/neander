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

import montador.MontadorMIPS;

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
import processor.MIPSMulti;
import processor.Processor;
import simdraw.figures.AdderFigure;
import simdraw.figures.ConnectionFigure;
import simdraw.figures.GenericInOutFigure;
import simdraw.figures.GenericFigure;
import simdraw.figures.GenericMultiplexerFigure;
import simdraw.figures.MemoryFigure;
import simdraw.figures.MultiplexerFigure;
import simdraw.figures.RegisterBankFigure;
import simdraw.figures.RegisterFigure;
import simdraw.figures.TimerFigure;
import simdraw.panels.ByteMemoryPanel;
import simdraw.panels.ComponentPanel;
import simdraw.panels.DataMemoryPanel;
import simdraw.panels.MemoryPanel;
import simdraw.panels.RegisterFilePanel;
import simdraw.panels.SuperescalarPanel;
import simulator.Simulator;
import util.SistNum;

/**
 * @version <$CURRENT_VERSION$>
 */
public  class MIPSNotPipelinedApp extends MIPSApp {

	class DLXNPSimulateAction extends AbstractAction {
		public DLXNPSimulateAction(	MIPSNotPipelinedApp parNa) {
			//super("Sim", null);
			na = parNa;
		}
		public void actionPerformed(ActionEvent e) {
			na.sSim.Simulate ( na.pProc);
			na.atualizaVisualizacao ( );
		}
		
		MIPSNotPipelinedApp na;
	}

	public MIPSNotPipelinedApp(Simulator parSim, Processor parProc) {
		//super("TDSim - Processor Model");
		super(parSim, parProc);
		/*sSim = parSim;
		pProc = parProc;
		dtpVis = pProc.getDatapath ( );
		jNroUnitsSim = null;
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();
		int screenHeight = d.height;
		int screenWidth = d.width;
		setSize(screenWidth, screenHeight);
		
		SistNum.setDefaultSizeFormat( WORD);
		Lang.iLang = Lang.getDefaultLanguage();*/
	}
/*
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
		createPanels ( );
		resetDialogs ( );
	}
*/
	private void createBlockDiagram ( ) {
		TimerFigure t = new TimerFigure ( "NONPIPELINED MIPS I", 20);
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

		RegisterBankFigure registers = new RegisterBankFigure ( "registers", "E1", "S1", "S2", "NW1", "NR1", "NR2");
		this.view().drawing().add ( registers);
		
		GenericInOutFigure se = new GenericInOutFigure ( "se", "E0", "S0");
		this.view().drawing().add ( se);
		
		GenericInOutFigure gfc = new GenericInOutFigure ( "25to0", "E0", "S0");
		this.view().drawing().add ( gfc);

		//GenericCcomb1e1sFigure zt = new GenericCcomb1e1sFigure ( "zt", "E1", "S1");
		//this.view().drawing().add ( zt);
		GenericInOutFigure not = new GenericInOutFigure ( "not", "E1", "S1");
		not.setBaseSize ( 20, 10);
		this.view().drawing().add ( not);
		GenericMultiplexerFigure gfComp = new GenericMultiplexerFigure ( "comp", pProc);
		gfComp.setBaseSize ( 25, 25);
		this.view().drawing().add ( gfComp);
		MultiplexerFigure muxa = new MultiplexerFigure ( "muxa", "E1", "E2", "S1", "SEL");
		muxa.setBaseSize ( 28, 36);
		this.view().drawing().add ( muxa);
		GenericMultiplexerFigure muxb = new GenericMultiplexerFigure ( "muxb", pProc);
		muxb.setBaseSize ( 28, 36);
		this.view().drawing().add ( muxb);
	
		MemoryFigure dmem = new MemoryFigure ( "dmemory", "E0", "E1", "S0");
		dmem.setBaseSize ( 25, 75);
		this.view().drawing().add ( dmem);

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
		
		cx = createConnection ( this.view(),"add","S1","E2",add,muxwb);
		cx.insertPointAt ( new Point ( 168, 100), 1);
		cx.insertPointAt ( new Point ( 866, 100), 2);
		
		cx = createConnection ( this.view(),"pc","S1","E1",pc,add);
		cx.insertPointAt ( new Point ( 98, 251), 1);
		cx.insertPointAt ( new Point ( 32, 251), 2);

		cx = createConnection ( this.view(),"muxpc","S1","E1",muxpc,pc);
		cx.insertPointAt ( new Point ( 186, 6), 1);
		cx.insertPointAt ( new Point ( 2, 6), 2);

		cx = createConnection ( this.view(),"muxpc","S1","E1",muxpc,muxa);
		cx.insertPointAt ( new Point ( 487, 33), 1);

		cx = createConnection ( this.view(),"imemory","S1","E0",imem,se);
		cx.insertPointAt ( new Point ( 275, 200), 1);

		cx = createConnection ( this.view(),"imemory","S1","E0",imem,gfc);

		cx = createConnection ( this.view(),"registers","S1","E2",registers,muxa);
		cx.insertPointAt ( new Point ( 403, 168), 1);
		cx = createConnection ( this.view(),"registers","S2","E0",registers,dmem);
		cx.insertPointAt ( new Point ( 403, 340), 1);
		//
		//cx = createConnection ( this.view(),"registers","S1","E1",registers,zt);
		//cx.insertPointAt ( new Point ( 404, 80), 1);
		cx = createConnection ( this.view(),"registers","S2","E0",registers,muxb);
		cx.insertPointAt ( new Point ( 487, 199), 1);
		cx = createConnection ( this.view(),"registers","S1","E0",registers,gfComp);
		cx.insertPointAt ( new Point ( 469, 65), 1);
		cx = createConnection ( this.view(),"registers","S2","E1",registers,gfComp);
		cx.insertPointAt ( new Point ( 469, 110), 1);
		cx = createConnection ( this.view(),"se","S0","E1",se,muxb);
		cx.insertPointAt ( new Point ( 461, 270), 1);
		cx = createConnection ( this.view(),"comp","S0","E1",gfComp,not);
		cx = createConnection ( this.view(),"25to0","S0","E3",gfc,muxb);
		//
		cx = createConnection ( this.view(),"muxa","S1","E1",muxa, alu);
		cx = createConnection ( this.view(),"muxb","S0","E2",muxb, alu);	
		cx = createConnection ( this.view(),"alu","S1","E1",alu,muxpc);
		cx.insertPointAt ( new Point ( 633, 1), 1);
		cx.insertPointAt ( new Point ( 114, 1), 2);
		cx = createConnection ( this.view(),"alu","S1","E1",alu,dmem);
		cx.insertPointAt ( new Point ( 725, 220), 1);
		cx = createConnection ( this.view(),"alu","S1","E1",alu,muxwb);
		cx.insertPointAt ( new Point ( 633, 352), 1);
		cx.insertPointAt ( new Point ( 792, 352), 2);
		//
		cx = createConnection ( this.view(),"dmemory","S0","E0",dmem,muxwb);
		cx.insertPointAt ( new Point ( 846, 190), 1);
		//
		cx = createConnection ( this.view(),"muxwb","S0","E1",muxwb,registers);
		cx.insertPointAt ( new Point ( 978, 364), 1);
		cx.insertPointAt ( new Point ( 286, 364), 2);
		
		t.moveBy ( 360,30);
		add.moveBy( 50, 100);
		muxpc.moveBy ( 150, 50);
		pc.moveBy( 50, 300);
		imem.moveBy ( 150, 200);
		registers.moveBy ( 355, 180);
		se.moveBy ( 355, 270);
		gfc.moveBy ( 355, 330);
		//zt.moveBy ( 560, 80);
		not.moveBy ( 700, 60);
		gfComp.moveBy ( 540, 60);
		muxa.moveBy ( 515, 150);
		muxb.moveBy ( 515, 290);
		alu.moveBy ( 597, 220);
		dmem.moveBy ( 775, 190);
		muxwb.moveBy ( 950, 180);		
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
		//super.createTools(palette);
		String sAux = Platform.treatPathNames(".\\images\\simulate.gif");
		
		JButton jSim = new JButton ( new DLXNPSimulateAction ( this));
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
	
	private void promptFileName ( ) throws Exception {
		String sBasePath = Platform.treatPathNames(Processor.getProcessorName()+Platform.getSeparatorPath()+"programs");
		String sFilePath = sBasePath + Platform.getSeparatorPath()+"_dlxNP.bin";
		String sFileTarget = "programs"+ Platform.getSeparatorPath()+"_dlxNP.bin";
		jOFC.setCurrentDirectory( new File ( sBasePath));

		int returnVal = jOFC.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			String pathName = jOFC.getCurrentDirectory().getAbsolutePath()+Platform.getSeparatorPath()+jOFC.getSelectedFile().getName();
			System.out.println("You chose to open this file: " + pathName );
			MontadorMIPS mmn = new MontadorMIPS (pProc, pathName, sFilePath);
			try {
				((MIPSMulti) pProc).setReset ( sFileTarget);				
				File f = new File ( sFilePath);
				f.delete ( );
				tabbedPane.setSelectedIndex(3);
			} catch (Exception e) {
				e.printStackTrace();
			}
			atualizaVisualizacao();
		}
	}
	
/*	private void promptFileName ( ) throws Exception {
		jOFC.setCurrentDirectory( new File ( Processor.getProcessorName()+"\\programs"));

		int returnVal = jOFC.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			String pathName = jOFC.getCurrentDirectory().getAbsolutePath()+"\\"+jOFC.getSelectedFile().getName();
			System.out.println("You chose to open this file: " + pathName);
			MontadorDLX mmn = new MontadorDLX (pProc, pathName, Processor.getProcessorName()+"\\programs\\_dlxNP.bin");
			try {
				((DLXMulti) pProc).setReset ( "programs\\_dlxNP.bin");				
				File f = new File ( Processor.getProcessorName()+"\\programs\\_dlxNP.bin");
				//f.delete ( );
				tabbedPane.setSelectedIndex(3);
			} catch (Exception e) {
				e.printStackTrace();
			}
			atualizaVisualizacao();
		}
	}
*/	
	private void promptReset ( ) {
		try {
			MIPSMulti nProc = (MIPSMulti) pProc;
			nProc.setReset ( null);
			tabbedPane.setSelectedIndex(3);
			atualizaVisualizacao ( );
		} catch ( Exception e) {
			System.out.println ( "Erro no comando reset!");
		}		
	}
/*
	private void promptHelp ( ) {
		Help.help ( );
		tabbedPane.setSelectedIndex(3);
	}
	
	private void promptRISA ( ) {
		DLX nProc = (DLX) pProc;
		nProc.listRisas ( );
		tabbedPane.setSelectedIndex(3);
	}
*/	
	private void promptReferencesByAddress ( ) {
		MIPSMulti nProc = (MIPSMulti) pProc;
		//nProc.listReferencesByAddress();
		nProc.listReferencesByAddressOfMethods();
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
		
		cmd = new AbstractCommand(Lang.iLang==ENGLISH?Lang.msgsGUI[204]:Lang.msgsGUI[209], this, true) {
			public void execute() {
				try {
					promptReferencesByAddress();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		menu.add(cmd, new MenuShortcut('a'));
		
		return menu;
	}
	
	/**
	 * Creates the file menu. Clients override this
	 * method to add additional menu items.
	 *
	protected JMenu createFileMenu() {
		CommandMenu menu = new CommandMenu("File");

		Command cmd = new AbstractCommand("Exit", this, true) {
			public void execute() {
				endApp();
			}
		};
		menu.add(cmd);
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

		Command cmd = new AbstractCommand("Set port value...", this, false) {
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

		return menu;
	}
		
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
/*
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
	
	public static void main(String[] args) {
		DrawApplication window = new DLXNotPipelinedApp(null, null);

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
	JPanel jMemContents, jPipelineContents, jInstructions, jRFContents, jDMemContents, jComponent, jSuperescalar;
	JTextField fStatusLine;*/
}
