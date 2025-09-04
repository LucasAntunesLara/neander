/*
 * Created on 24/09/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package simdraw;

import help.Help;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.MenuShortcut;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;


import org.jhotdraw.application.DrawApplication;
import org.jhotdraw.contrib.Desktop;
import org.jhotdraw.framework.Drawing;
import org.jhotdraw.framework.DrawingView;
import org.jhotdraw.framework.FigureEnumeration;
import org.jhotdraw.standard.AbstractCommand;
import org.jhotdraw.standard.NullTool;
import org.jhotdraw.util.Command;
import org.jhotdraw.util.CommandMenu;
import org.jhotdraw.util.UndoManager;

import platform.Lang;
import processor.Processor;
import simdraw.dialogs.ExecuteDialog;
import simdraw.dialogs.NewSimulateDialog;
import simdraw.dialogs.SetBreakpointDialog;
import simdraw.dialogs.SetContentsDialog;
import simdraw.dialogs.SetPortDialog;
import simdraw.dialogs.SetProcessorDialog;
import simdraw.figures.ConnectionFigure;
import simdraw.figures.TimerFigure;
import simdraw.panels.ComponentPanel;
import simdraw.panels.InstructionsPanel;
import simdraw.panels.PipelinePanel;
import simulator.Simulator;
import util.Define;
import util.SistNum;
import datapath.Datapath;

/**
 * @author Owner
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TDBenchApp extends DrawApplication implements Define {

	/**
	 * @param string
	 */
	public TDBenchApp(String string) {
		super( Lang.iLang==ENGLISH?Lang.msgsGUI[0]:Lang.msgsGUI[30]);		
		// TODO Auto-generated constructor stub
	}

	class SimulateAction extends AbstractAction {
		public SimulateAction(	TDBenchApp parNa) {
			na = parNa;
		}
		public void actionPerformed(ActionEvent e) {
			System.out.println ( "simulate de tdbench...");
			try {
				na.sSim.Simulate ( na.pProc);
			} catch ( Exception exc) {
				TDBenchApp.messagesToUser(Lang.iLang==ENGLISH?Lang.msgsGUI[181]:Lang.msgsGUI[191],Processor.getMessageError());
			}
			na.atualizaVisualizacao ( );
		}
		
		TDBenchApp na;
	}

	class DecimalNumericSystemAction extends AbstractAction {
		public DecimalNumericSystemAction(	TDBenchApp parNa, String sName) {
			super ( sName);
			na = parNa;
		}
		public void actionPerformed(ActionEvent e) {
			SistNum.setDefaultNumberFormat ( "d");
			System.out.println ( "Choose decimal!");
			na.atualizaVisualizacao ( );
		}
		
		TDBenchApp na;
	}

	class HexaDecimalNumericSystemAction extends AbstractAction {
		public HexaDecimalNumericSystemAction(	TDBenchApp parNa, String sName) {
			super ( sName);
			na = parNa;
		}
		public void actionPerformed(ActionEvent e) {
			SistNum.setDefaultNumberFormat ( "h");
			System.out.println ( "Choose hexa decimal!");
			na.atualizaVisualizacao ( );
		}
		
		TDBenchApp na;
	}
	
	class BinaryNumericSystemAction extends AbstractAction {
		public BinaryNumericSystemAction(	TDBenchApp parNa,String sName) {
			super ( sName);
			na = parNa;
		}
		public void actionPerformed(ActionEvent e) {
			SistNum.setDefaultNumberFormat ( "b");
			System.out.println ( "Choose binary!");
			na.atualizaVisualizacao ( );
		}
		
		TDBenchApp na;
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

	protected void resetDialogs ( ) {
		jNroUnitsSim = null;
		jSetProcessorDialog = null;
		jSetPortDialog = null;
		jSetContentsDialog = null;
		jExecuteDialog = null;
		jSetBreakDialog = null;
	}

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
	}
	
	public TDBenchApp(Simulator parSim, Processor parProc) {
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
		
		SistNum.setDefaultSizeFormat( WORD);
		Lang.iLang = Lang.getDefaultLanguage();
	}

	void setStatusLine(JTextField newStatusLine) {
		fStatusLine = newStatusLine;
	}

	protected JTextField getStatusLine() {
		return fStatusLine;
	}
	
	private void createBlockDiagram ( ) {
		TimerFigure t = new TimerFigure ( Lang.iLang==ENGLISH?Lang.msgsGUI[1]:Lang.msgsGUI[31], 30);
		this.view().drawing().add ( t);
	}
	
	private void createPanels ( ) {
		/*if ( jMemContents == null) {
			jMemContents = new MemoryPanel ( sSim, pProc, "mem", "rem");				
		}			
		if ( jMemPContents == null) {
			jMemPContents = new MemoryPanel ( sSim, pProc, "memp", "rem");				
		}*/
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
		//jMemContents.setPreferredSize( new Dimension ( (int) (screenWidth*0.170), screenHeight));
		
		//activePanel.add(jMemContents, BorderLayout.EAST);
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

	protected ConnectionFigure createConnection(	DrawingView dw,String sName, String sS, String sE, 
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
	
	protected void createTools(JToolBar palette) {
		super.createTools(palette);
	
		JButton jSim = new JButton ( new SimulateAction ( this));
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

	protected JMenu createSteeringMenu() {
		CommandMenu menu = new CommandMenu(Lang.iLang==ENGLISH?Lang.msgsGUI[21]:Lang.msgsGUI[51]);

		Command cmd = new AbstractCommand(Lang.iLang==ENGLISH?Lang.msgsGUI[27]:Lang.msgsGUI[57], this, false) {
			public void execute() {
				promptSetProcessorDialog();
			}
		};
		menu.add(cmd, new MenuShortcut('p'));

		cmd = new AbstractCommand(Lang.iLang==ENGLISH?Lang.msgsGUI[22]:Lang.msgsGUI[52], this, false) {
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
		return menu;
	}

	protected void atualizaVisualizacao ( ) {
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
		f = (LinkSimulatorVisualization) jMemContents;
		if (f!=null) f.refreshDisplay( sSim, pProc);
		f = (LinkSimulatorVisualization) jMemPContents;
		if (f!=null) f.refreshDisplay( sSim, pProc);
		f = (LinkSimulatorVisualization) jPipelineContents;
		if (f!=null) f.refreshDisplay( sSim, pProc);
		f = (LinkSimulatorVisualization) jInstructions;
		if (f!=null) f.refreshDisplay( sSim, pProc);
		f = (LinkSimulatorVisualization) jComponent;
		if (f!=null) f.refreshDisplay( sSim, pProc);
		f = (LinkSimulatorVisualization) jRFContents;
		if (f!=null) f.refreshDisplay( sSim, pProc);
		f = (LinkSimulatorVisualization) jDMemContents;
		if (f!=null) f.refreshDisplay( sSim, pProc);
		f = (LinkSimulatorVisualization) jSuperescalar;
		if (f!=null) f.refreshDisplay( sSim, pProc);
		f = (LinkSimulatorVisualization) jInstQueues;
		if (f!=null) f.refreshDisplay( sSim, pProc);
		f = (LinkSimulatorVisualization) jProcessor;
		if (f!=null) f.refreshDisplay( sSim, pProc);
		f = (LinkSimulatorVisualization) jBreakpoints;
		if (f!=null) f.refreshDisplay( sSim, pProc);
		setView ( dv);
		// Para forar uma atualizao da tela
		int iTab = tabbedPane.getSelectedIndex();
		tabbedPane.setSelectedIndex(3);
		tabbedPane.setSelectedIndex(iTab);
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

	private void promptFileName ( ) throws Exception {
		jOFC.setCurrentDirectory( new File ( Processor.getProcessorName()+"\\programs"));

		int returnVal = jOFC.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			String pathName = jOFC.getCurrentDirectory().getAbsolutePath()+"\\"+jOFC.getSelectedFile().getName();
			System.out.println("You chose to open this file: " + pathName);
			try {
				tabbedPane.setSelectedIndex(3);
			} catch (Exception e) {
				e.printStackTrace();
			}
			atualizaVisualizacao();
		}
	}
	
	private void promptReset ( ) {
		try {
			tabbedPane.setSelectedIndex(3);
			atualizaVisualizacao ( );
		} catch ( Exception e) {
			System.out.println ( "Erro no comando reset!");
		}		
	}

	private void promptHelp ( ) {
		Help.help ( );
		tabbedPane.setSelectedIndex(3);
	}

	public static void messagesToUser ( String parWhere, String parMsg) {
		if ( textArea != null) {
			if (parWhere != null) textArea.append("<"+parWhere+"> ==>   ");
        	textArea.append(parMsg+"\n");
        	//Make sure the new text is visible, even if there
        	//was a selection in the text area.
        	textArea.setCaretPosition(textArea.getDocument().getLength());		
			tabbedPane.setSelectedIndex(3);
		}
	}

	Simulator sSim;
	Processor pProc;
	Datapath dtpVis;
	DrawingView dv;
	Drawing dw;
	FigureEnumeration fe;
	FileDialog jFileNameDialog;
	static JTabbedPane tabbedPane;
	JDialog jNroUnitsSim, jSetProcessorDialog, jSetPortDialog, jSetContentsDialog, jExecuteDialog, jSetBreakDialog;
	JPanel jMemContents, jMemPContents, jPipelineContents, jInstructions, jComponent, jRFContents, jDMemContents, jSuperescalar;
	JPanel jInstQueues, jProcessor,jBreakpoints;
	JTextField fStatusLine;
	JFileChooser jOFC;
	// indica simulao de ciclos ou de instrues: true para ciclos
	protected boolean bSimulate = true;
	static JTextArea textArea;
}
