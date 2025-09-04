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
public  class NeanderApp extends TDBenchApp implements Define {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	class NeanderSimulateAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public NeanderSimulateAction(	NeanderApp parNa) {
			//super("Sim", null);
			na = parNa;
		}
		public void actionPerformed(ActionEvent e) {
			if ( bSimulate == true) na.sSim.Simulate ( na.pProc);
			else {
				((Neander) na.pProc).resetIsNewInstruction();
				while ( ((Neander) na.pProc).isNewInstruction () == false) {
					na.sSim.Simulate ( na.pProc);
				}
			}
			na.atualizaVisualizacao ( );
		}
		
		NeanderApp na;
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
	
	public NeanderApp(Simulator parSim, Processor parProc) {
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
		
		SistNum.setDefaultSizeFormat( BYTE);
		Lang.iLang = Lang.getDefaultLanguage();
	}
	
	private void createBlockDiagram ( ) {
		TimerFigure t = new TimerFigure ( Lang.iLang==ENGLISH?Lang.msgsGUI[1]:Lang.msgsGUI[31], 30);
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

		t.moveBy ( 610,50);
		pc.moveBy( 260, 200);
		mpxpc.moveBy( 110, 200);
		mpxrem.moveBy( 410, 200);
		rem.moveBy( 535, 200);
		acc.moveBy( 60, 325);
		rdm.moveBy( 710, 325);
		mpxrdm.moveBy( 710, 400);
		addpc.moveBy ( 335, 50);
		mem.moveBy( 710, 200);
		alu.moveBy( 260, 450);
		ri.moveBy( 560, 375);		
	}
	
	private void createPanels ( ) {
		if ( jMemContents == null) {
			jMemContents = new MemoryPanel ( sSim, pProc, "mem", "rem");				
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
		String sAux = Platform.treatPathNames(".\\images\\simulate.gif");

		JButton jSim = new JButton ( new NeanderSimulateAction ( this));
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
				((Neander) pProc).resetIsNewInstruction();
				while ( ((Neander) pProc).isNewInstruction () == false) {
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
						((Neander) pProc).resetIsNewInstruction();
						while ( ((Neander) pProc).isNewInstruction () == false) {
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
			MontadorNeander mmn = new MontadorNeander (pProc, pathName, sFilePath,"MEM");
			try {
				((Neander) pProc).setReset ( sFileTarget);				
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
			Neander nProc = (Neander) pProc;
			nProc.setReset ( null);
			tabbedPane.setSelectedIndex(3);
			atualizaVisualizacao ( );
		} catch ( Exception e) {
			System.out.println ( "Erro no comando reset!");
		}		
	}
}
