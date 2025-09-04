/*
 * Created on 01/05/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package simdraw.dialogs;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import processor.Processor;
import simulator.Simulator;

/**
 * @author unknown
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class TimingAssocDialog extends JDialog implements ActionListener {

	/**
	 * @throws java.awt.HeadlessException
	 */
	public TimingAssocDialog(Simulator parSim, Processor parProc) throws HeadlessException {
		super();
		sSim = parSim;
		pProc = parProc;
		Container c = getContentPane();
		c.setLayout ( new BorderLayout ( 5,5));
		//
		JPanel p1 = new JPanel ( );
		JPanel paux11 = new JPanel ( );
		JPanel paux12 = new JPanel ( );
		paux12.setLayout ( new BorderLayout ( ));
		p1.setLayout ( new BorderLayout ( ));
		JComboBox cb1 = new JComboBox ( );
		cb1.addItem ( "UNIQUE");
		JList l1 = new JList ( stages);
		JLabel lb1 = new JLabel ( "Execution Paths");
		paux11.add( lb1);
	    l1.setVisibleRowCount( 3 );
		paux12.add( cb1, BorderLayout.NORTH);
		paux12.add( new JScrollPane ( l1),BorderLayout.CENTER);
		p1.add ( paux11, BorderLayout.NORTH);
		p1.add ( paux12, BorderLayout.CENTER);
		//
		//
		JPanel p2 = new JPanel ( );
		p2.setLayout ( new BorderLayout ( ));
		JLabel lb2 = new JLabel ( "Microoperations em ALI.txt");
		JPanel paux2 = new JPanel ( );
		paux2.add ( lb2);
		JList l2 = new JList ( instrWithout);
	    l2.setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );
	    int [] sel = { 0, 1, 2};
	    l2.setSelectedIndices( sel);
		JButton b2 = new JButton ( "+");
	    //l2.setVisibleRowCount( 10);
		p2.add ( paux2,BorderLayout.NORTH);
		p2.add ( new JScrollPane ( l2),BorderLayout.CENTER);
		p2.add ( new JScrollPane ( b2),BorderLayout.EAST);
		//
		JPanel p3 = new JPanel ( );
		p3.setLayout ( new BorderLayout ( ));
		JLabel lb3 = new JLabel ( "Instruction with timing");
		JPanel paux3 = new JPanel ( );
		paux3.add ( lb3);
		JList l3 = new JList ( instrWith);
		JButton b3 = new JButton ( "==>");
	    //l2.setVisibleRowCount( 10);
		p3.add ( paux3,BorderLayout.NORTH);
		p3.add ( new JScrollPane ( l3),BorderLayout.CENTER);
		p3.add ( new JScrollPane ( b3),BorderLayout.WEST);
		//
		JPanel p4 = new JPanel ( );
		JButton b4 = new JButton ( "Save");
		p4.add ( b4);
		//
		/*
		JPanel p5 = new JPanel ( );
		p5.setLayout ( new FlowLayout ( ));
		JLabel l52 = new JLabel ( "Execution Paths");
		JLabel l53 = new JLabel ( "Instruction with timing");
		p5.add ( l51, FlowLayout.LEFT);
		p5.add ( l52, FlowLayout.CENTER);
		p5.add ( l53, FlowLayout.RIGHT);
		*/
		c.add ( p4, BorderLayout.SOUTH);
		c.add ( p1, BorderLayout.CENTER);
		c.add ( p2, BorderLayout.WEST);
		c.add ( p3, BorderLayout.EAST);
		b4.addActionListener( this);
		setLocation(new Point ( 50,50));
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		System.out.println ( "teste");
		setVisible ( false);
	}

	Simulator sSim;
	Processor pProc;
	String stages[] = { "FETCH    - 0", "DECODE   - 1", "EXECUTE  - 2", "MEMORY    - 3", "WRITEBACK - 4"};
	String instrWithout [] = { "muxpc.SEL = 1", "include fetchInstruction", "if_id.write", "if_id.read",
			"include readRegisterBank", "id_ex.write", "id_ex.read", "muxa.SEL = 1", "muxb.SEL = 0",
			"include executeAluOperation", "ex_mem.write", "ex_mem.read", "mem_wb.write", "mem_wb.read",
			"muxwb.SEL = 1", "include writeRegisterBank"};
	String instrWith [] = { "mop[0] muxpc.SEL = 1", "mop[0] include fetchInstruction", "mop[0] if_id.write"};
}
