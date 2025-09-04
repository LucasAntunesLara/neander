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
import java.awt.MenuShortcut;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
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

import processor.Processor;
import simdraw.dialogs.ExecuteDialog;
import simdraw.dialogs.SetContentsDialog;
import simdraw.dialogs.SetPortDialog;
import simdraw.dialogs.SimulateDialog;
import simdraw.figures.AluFigure;
import simdraw.figures.ConnectionFigure;
import simdraw.figures.MemoryFigure;
import simdraw.figures.RegisterFigure;
import simdraw.figures.TimerFigure;
import simdraw.panels.ComponentPanel;
import simdraw.panels.InstructionsPanel;
import simdraw.panels.MemoryPanel;
import simdraw.panels.PipelinePanel;
import simulator.Simulator;
import datapath.Datapath;

/**
 * @version <$CURRENT_VERSION$>
 */
public  class ExampleApp extends DrawApplication {

	class SimulateAction extends AbstractAction {
		public SimulateAction(	ExampleApp parNa) {
			//super("Sim", null);
			na = parNa;
		}
		public void actionPerformed(ActionEvent e) {
			na.sSim.Simulate ( na.pProc);
			na.atualizaVisualizacao ( );
		}
		
		ExampleApp na;
	}

	public ExampleApp(Simulator parSim, Processor parProc) {
		super("TDSim - Processor Model");
		sSim = parSim;
		pProc = parProc;
		dtpVis = pProc.getDatapath ( );
		jNroUnitsSim = null;
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();
		int screenHeight = d.height;
		int screenWidth = d.width;
		setSize(screenWidth, screenHeight);
	}

	void setStatusLine(JTextField newStatusLine) {
		fStatusLine = newStatusLine;
	}

	protected JTextField getStatusLine() {
		return fStatusLine;
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

		if ( jMemContents == null) {
			jMemContents = new MemoryPanel ( sSim, pProc, "mem", "pc");				
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
		
		JTabbedPane tabbedPane = new JTabbedPane();
	
		JPanel activePanel = new JPanel();
		//JPanel mainPanel = new JPanel ( );
		//mainPanel.setLayout(new BorderLayout());
		activePanel.setAlignmentX(LEFT_ALIGNMENT);
		activePanel.setAlignmentY(TOP_ALIGNMENT);
		activePanel.setLayout(new BorderLayout());
		
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();
		int screenHeight = d.height;
		int screenWidth = d.width;
		jMemContents.setPreferredSize( new Dimension ( (int) (screenWidth*0.170), screenHeight));

		activePanel.add(jMemContents, BorderLayout.EAST);
		setDesktopListener(createDesktopListener());
		setDesktop(createDesktop());
		activePanel.add((Component)getDesktop(), BorderLayout.CENTER);
		
		JPanel controlPanel = new JPanel ( );
		controlPanel.setLayout( new BorderLayout ( 5, 5));
		controlPanel.add(jPipelineContents, BorderLayout.CENTER);
		controlPanel.add(jInstructions, BorderLayout.EAST);
		//controlPanel.add((Component)getDesktop(), BorderLayout.SOUTH);
		
		tabbedPane.addTab("Datapath", null, activePanel, "Block Diagram");
		tabbedPane.setSelectedIndex(0);
		tabbedPane.addTab("Control", null, controlPanel, "Pipeline and Instructions");
		tabbedPane.addTab("Datapath (2)", null, jComponent, "Components");
		
		getContentPane().add(tabbedPane, BorderLayout.CENTER);

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
	
	protected void createTools(JToolBar palette) {
		super.createTools(palette);
		
		JButton jSim = new JButton ( new SimulateAction ( this));
		jSim.setIcon ( new ImageIcon ( ".\\images\\simulate.gif"));
		palette.addSeparator();
		palette.add( jSim);
		palette.setBackground( new Color ( 238,238,238));

		TimerFigure t = new TimerFigure ( "Example Processor", 30);
		this.view().drawing().add ( t);
	
		RegisterFigure pc = new RegisterFigure ( "PC", "E1", "S1");
		this.view().drawing().add ( pc);
			
		RegisterFigure a = new RegisterFigure ( "A", "E1", "S1");
		this.view().drawing().add ( a);

		RegisterFigure b = new RegisterFigure ( "B", "E1", "S1");
		this.view().drawing().add ( b);

		RegisterFigure c = new RegisterFigure ( "C", "E1", "S1");
		this.view().drawing().add ( c);

		MemoryFigure mem = new MemoryFigure ( "MEM", "E1", "E2", "S1");
		this.view().drawing().add ( mem);

		AluFigure alu = new AluFigure ( "ALU", "E1", "E2", "S1", "OP");
		this.view().drawing().add ( alu);
	
		ConnectionFigure cx;
				
		createConnection ( this.view(),"PC","S1","E2",pc,mem);
		createConnection ( this.view(),"A","S1","E1",a,alu);
		createConnection ( this.view(),"B","S1","E2",b,alu);
		createConnection ( this.view(),"ALU","S1","E1",alu,c);

		t.moveBy ( 610,50);
		pc.moveBy( 100, 200);
		mem.moveBy( 275, 200);
		a.moveBy( 498, 200);
		b.moveBy( 606, 150);
		c.moveBy( 648, 500);
		alu.moveBy( 600, 350);
	}
	
	/**
	 * Creates the standard menus. Clients override this
	 * method to add additional menus.
	 */
	protected void createMenus(JMenuBar mb) {
		addMenuIfPossible(mb, createFileMenu());
		addMenuIfPossible(mb, createTrackingMenu());
		addMenuIfPossible(mb, createSteeringMenu());
	}

	/**
	 * Creates the file menu. Clients override this
	 * method to add additional menu items.
	 */
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
		f = (LinkSimulatorVisualization) jPipelineContents;
		f.refreshDisplay( sSim, pProc);
		f = (LinkSimulatorVisualization) jInstructions;
		f.refreshDisplay( sSim, pProc);
		f = (LinkSimulatorVisualization) jComponent;
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
		DrawApplication window = new ExampleApp(null, null);

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
	JPanel jMemContents, jPipelineContents, jInstructions, jComponent;
	JTextField fStatusLine;
}
