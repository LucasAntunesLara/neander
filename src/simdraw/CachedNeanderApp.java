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

import montador.MontadorNeander;

import org.jhotdraw.contrib.Desktop;
import org.jhotdraw.framework.DrawingView;
import org.jhotdraw.standard.AbstractCommand;
import org.jhotdraw.standard.NullTool;
import org.jhotdraw.util.Command;
import org.jhotdraw.util.CommandMenu;
import org.jhotdraw.util.UndoManager;

import platform.Lang;
import platform.Platform;
import processor.CachedNeander;
import processor.Neander;
import processor.Processor;
import simdraw.dialogs.NewSimulateDialog;
import simdraw.figures.AdderFigure;
import simdraw.figures.AluFigure;
import simdraw.figures.ConnectionFigure;
import simdraw.figures.MemoryFigure;
import simdraw.figures.MultiplexerFigure;
import simdraw.figures.MultiplexerV2Figure;
import simdraw.figures.RegisterFigure;
import simdraw.figures.RegisterV2Figure;
import simdraw.figures.TimerFigure;
import simdraw.panels.CachePanel;
import simdraw.panels.ComponentPanel;
import simdraw.panels.InstructionsPanel;
import simdraw.panels.MemoryPanel;
import simdraw.panels.PipelinePanel;
import simulator.Simulator;
import util.Define;
import util.SistNum;

/**
 * @version <$CURRENT_VERSION$>
 */
public  class CachedNeanderApp extends TDBenchApp implements Define {

	class CNeanderSimulateAction extends AbstractAction {
		public CNeanderSimulateAction(	CachedNeanderApp parNa) {
			na = parNa;
		}
		public void actionPerformed(ActionEvent e) {
			if ( bSimulate == true) na.sSim.Simulate ( na.pProc);
			else {
				((CachedNeander) na.pProc).resetIsNewInstruction();
				while ( ((CachedNeander) na.pProc).isNewInstruction () == false) {
					na.sSim.Simulate ( na.pProc);
				}
			}
			na.atualizaVisualizacao ( );
		}
		
		CachedNeanderApp na;
	}
/*
	class DecimalNumericSystemAction extends AbstractAction {
		public DecimalNumericSystemAction(	CachedNeanderApp parNa, String sName) {
			super ( sName);
			na = parNa;
		}
		public void actionPerformed(ActionEvent e) {
			SistNum.setDefaultNumberFormat ( "d");
			System.out.println ( "Choose decimal!");
			na.atualizaVisualizacao ( );
		}
		
		CachedNeanderApp na;
	}

	class HexaDecimalNumericSystemAction extends AbstractAction {
		public HexaDecimalNumericSystemAction(	CachedNeanderApp parNa, String sName) {
			super ( sName);
			na = parNa;
		}
		public void actionPerformed(ActionEvent e) {
			SistNum.setDefaultNumberFormat ( "h");
			System.out.println ( "Choose hexa decimal!");
			na.atualizaVisualizacao ( );
		}
		
		CachedNeanderApp na;
	}
	
	class BinaryNumericSystemAction extends AbstractAction {
		public BinaryNumericSystemAction(	CachedNeanderApp parNa,String sName) {
			super ( sName);
			na = parNa;
		}
		public void actionPerformed(ActionEvent e) {
			SistNum.setDefaultNumberFormat ( "b");
			System.out.println ( "Choose binary!");
			na.atualizaVisualizacao ( );
		}
		
		CachedNeanderApp na;
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
	public void reset ( Simulator parSim, Processor parProc) {
		sSim = parSim;
		pProc = parProc;
		dtpVis = pProc.getDatapath ( );
		jNroUnitsSim = null;	
		jMemContents = null;
		jMemPContents = null;
		jPipelineContents = null;
		jInstructions = null;
		jComponent = null;
		createPanels ( );
		resetDialogs ( );
	}

	public CachedNeanderApp(Simulator parSim, Processor parProc) {
		super(Lang.iLang==ENGLISH?Lang.msgsGUI[0]:Lang.msgsGUI[30]);
		sSim = parSim;
		pProc = parProc;
		dtpVis = pProc.getDatapath ( );
		jNroUnitsSim = null;
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();
		int screenHeight = d.height;
		int screenWidth = d.width;
		setSize(screenWidth, screenHeight);
		
		SistNum.setDefaultSizeFormat( BYTE);
		Lang.iLang = Lang.getDefaultLanguage();
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
		TimerFigure t = new TimerFigure (  Lang.iLang==ENGLISH?Lang.msgsGUI[1]:Lang.msgsGUI[31], 30);
		this.view().drawing().add ( t);
	
		RegisterFigure pc = new RegisterFigure ( "PC", "E1", "S1");
		this.view().drawing().add ( pc);
		
		MultiplexerFigure mpxpc = new MultiplexerFigure ( "MPXPC", "E1", "E2", "S1", "SEL");
		this.view().drawing().add ( mpxpc);
		
		MultiplexerFigure mpxrem = new MultiplexerFigure ( "MPXREM", "E1", "E2", "S1", "SEL");
		this.view().drawing().add ( mpxrem);
				
		RegisterFigure rem = new RegisterFigure ( "REM", "E1", "S1");
		this.view().drawing().add ( rem);

		RegisterFigure acc = new RegisterFigure ( "ACC", "E1", "S1");
		this.view().drawing().add ( acc);

		RegisterV2Figure rdm = new RegisterV2Figure ( "RDM", "E1", "S1");
		this.view().drawing().add ( rdm);

		MultiplexerV2Figure mpxrdm = new MultiplexerV2Figure ( "MPXRDM", "E1", "E2", "S1", "SEL");
		this.view().drawing().add ( mpxrdm);

		AdderFigure addpc = new AdderFigure ( "ADDPC", "E1", "E2", "S1");
		this.view().drawing().add ( addpc);

		MemoryFigure mem = new MemoryFigure ( "MEM", "E1", "E2", "S1");
		this.view().drawing().add ( mem);

		MemoryFigure memp = new MemoryFigure ( "MEMP", "E1", "E2", "S1");
		this.view().drawing().add ( memp);
		memp.setBaseSize ( 24, 24);

		AluFigure alu = new AluFigure ( "ALU", "E1", "E2", "S1", "OP");
		this.view().drawing().add ( alu);

		RegisterFigure ri = new RegisterFigure ( "RI", "E1", "S1");
		this.view().drawing().add ( ri);
		
		ConnectionFigure cx;
				
		createConnection ( this.view(),"MPXPC","S1","E1",mpxpc,pc);
		cx = createConnection ( this.view(),"RDM","S1","E2",rdm,mpxpc);
		cx.insertPointAt ( new Point ( 74, 301), 1);
		cx = createConnection ( this.view(),"ADDPC","S1","E1",addpc,mpxpc);
		cx.insertPointAt ( new Point ( 74, 14), 1);
		cx = createConnection ( this.view(),"PC","S1","E1",pc,mpxrem);
		cx.insertPointAt ( new Point ( 308, 182), 1);
		cx = createConnection ( this.view(),"PC","S1","E1",pc,addpc);
		cx.insertPointAt ( new Point ( 308, 86), 1);
		cx = createConnection ( this.view(),"RDM","S1","E2",rdm,mpxrem);
		cx.insertPointAt ( new Point ( 374, 301), 1);
		createConnection ( this.view(),"MPXREM","S1","E1",mpxrem,rem);
		createConnection ( this.view(),"REM","S1","E2",rem,mem);
		
		cx = createConnection ( this.view(),"REM","S1","E2",rem,memp);
		cx.insertPointAt ( new Point ( 583, 50), 1);
		cx = createConnection ( this.view(),"MEMP","S1","E1",memp,mem);
		cx.insertPointAt ( new Point ( 788, 296), 1);
		cx = createConnection ( this.view(),"RDM","S1","E1",rdm,memp);
		cx.insertPointAt ( new Point ( 610, 301), 1);
		cx.insertPointAt ( new Point ( 610, 98), 2);

		createConnection ( this.view(),"MPXRDM","S1","E1",mpxrdm,rdm);
		createConnection ( this.view(),"RDM","S1","E1",rdm,mem);
		cx = createConnection ( this.view(),"MEM","S1","E2",mem,mpxrdm);
		cx.insertPointAt ( new Point ( 800, 436), 1);
		cx = createConnection ( this.view(),"ACC","S1","E1",acc,mpxrdm);
		cx.insertPointAt ( new Point ( 653, 325), 1);
		cx.insertPointAt ( new Point ( 653, 436), 2);				
		cx = createConnection ( this.view(),"ACC","S1","E1",acc,alu);
		cx.insertPointAt ( new Point ( 206, 325), 1);	
		cx = createConnection ( this.view(),"RDM","S1","E2",rdm,alu);
		cx.insertPointAt ( new Point ( 314, 301), 1);
		cx = createConnection ( this.view(),"RDM","S1","E1",rdm,ri);
		cx.insertPointAt ( new Point ( 512, 301), 1);
		cx = createConnection ( this.view(),"ALU","S1","E1",alu,acc);
		cx.insertPointAt ( new Point ( 12, 522), 1);

		t.moveBy ( 480,40);
		pc.moveBy( 260, 200);
		mpxpc.moveBy( 110, 200);
		mpxrem.moveBy( 410, 200);
		rem.moveBy( 535, 200);
		acc.moveBy( 60, 325);
		rdm.moveBy( 710, 325);
		mpxrdm.moveBy( 710, 400);
		addpc.moveBy ( 335, 50);
		mem.moveBy( 710, 200);
		memp.moveBy( 740, 50);
		alu.moveBy( 260, 450);
		ri.moveBy( 560, 375);
	}

	private void createPanels ( ) {
		if ( jMemContents == null) {
			jMemContents = new CachePanel ( sSim, pProc, "mem", null);
		}		
		if ( jMemPContents == null) {
			jMemPContents = new MemoryPanel ( sSim, pProc, "memp", "rem");				
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
		jMemContents.setPreferredSize( new Dimension ( (int) (screenWidth*0.170), (int) (screenHeight*0.275)));
		jMemPContents.setPreferredSize( new Dimension ( (int) (screenWidth*0.170), (int) (screenHeight*0.50)));
		JPanel memPanel = new JPanel();
		memPanel.setLayout(new BorderLayout());		
		memPanel.add(jMemContents, BorderLayout.NORTH);
		memPanel.add(jMemPContents, BorderLayout.SOUTH);	
		activePanel.add(memPanel, BorderLayout.EAST);
		
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

		JButton jSim = new JButton ( new CNeanderSimulateAction ( this));
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

		JRadioButtonMenuItem rbA = new JRadioButtonMenuItem ( new DecimalNumericSystemAction ( this, Lang.iLang==ENGLISH?Lang.msgsGUI[15]:Lang.msgsGUI[45]));
		JRadioButtonMenuItem rbB = new JRadioButtonMenuItem ( new HexaDecimalNumericSystemAction ( this, Lang.iLang==ENGLISH?Lang.msgsGUI[16]:Lang.msgsGUI[46]));
		JRadioButtonMenuItem rbC = new JRadioButtonMenuItem ( new BinaryNumericSystemAction ( this, Lang.iLang==ENGLISH?Lang.msgsGUI[17]:Lang.msgsGUI[47]));

		bg.add(rbA);
		bg.add(rbB);
		bg.add(rbC);
		bg.setSelected( rbA.getModel ( ), true);
		menu.add ( rbA);
		menu.add ( rbB);
		menu.add ( rbC);

		return menu;
	}
*/
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
/*
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
		f = (LinkSimulatorVisualization) jMemPContents;
		f.refreshDisplay( sSim, pProc);
		f = (LinkSimulatorVisualization) jPipelineContents;
		f.refreshDisplay( sSim, pProc);
		f = (LinkSimulatorVisualization) jInstructions;
		f.refreshDisplay( sSim, pProc);
		f = (LinkSimulatorVisualization) jComponent;
		f.refreshDisplay( sSim, pProc);
		setView ( dv);
	}
*/
	private void promptSimulate ( ) {
		try {
			if ( bSimulate == true) sSim.Simulate ( pProc);
			else {
				((CachedNeander) pProc).resetIsNewInstruction();
				while ( ((CachedNeander) pProc).isNewInstruction () == false) {
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
						((CachedNeander) pProc).resetIsNewInstruction();
						while ( ((CachedNeander) pProc).isNewInstruction () == false) {
							sSim.Simulate ( pProc);
						}
					}
				}
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
*/
	private void promptFileName ( ) throws Exception {
		String sBasePath = Platform.treatPathNames(Processor.getProcessorName()+Platform.getSeparatorPath()+"programs");
		String sFilePath = sBasePath + Platform.getSeparatorPath()+"_tempBin.bin";
		String sFileTarget = "programs"+ Platform.getSeparatorPath()+"_tempBin.bin";
		jOFC.setCurrentDirectory( new File ( sBasePath));

		int returnVal = jOFC.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			String pathName = jOFC.getCurrentDirectory().getAbsolutePath()+Platform.getSeparatorPath()+jOFC.getSelectedFile().getName();
			System.out.println("You chose to open this file: " + pathName );
			MontadorNeander mmn = new MontadorNeander (pProc, pathName, sFilePath,"MEMP");
			try {
				((CachedNeander) pProc).setReset ( sFileTarget);				
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
			MontadorNeander mmn = new MontadorNeander (pProc, pathName, Processor.getProcessorName()+"\\programs\\_tempBin.bin","MEMP");
			try {
				((CachedNeander) pProc).setReset ( "programs\\_tempBin.bin");
				File f = new File ( Processor.getProcessorName()+"\\programs\\_tempBin.bin");
				f.delete ( );
				tabbedPane.setSelectedIndex(3);
			} catch (Exception e) {
				e.printStackTrace();
			}
			atualizaVisualizacao();
		}
	}*/
	
	private void promptReset ( ) {
		try {
			CachedNeander nProc = (CachedNeander) pProc;
			nProc.setReset ( null);
			atualizaVisualizacao ( );
		} catch ( Exception e) {
			System.out.println ( "Erro no comando reset!");
		}		
	}
/*
	public static void main(String[] args) {
		DrawApplication window = new CachedNeanderApp(null, null);

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
	FileDialog jFileNameDialog;
	JTabbedPane tabbedPane;
	JDialog jNroUnitsSim, jSetPortDialog, jSetContentsDialog, jExecuteDialog;
	JPanel jMemPContents, jMemContents, jPipelineContents, jInstructions, jComponent;
	JTextField fStatusLine;
	JFileChooser jOFC;
	// indica simulação de ciclos ou de instruções: true para ciclos
	private boolean bSimulate = true;*/
}
