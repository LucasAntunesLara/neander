package simdraw.dialogs;

import java.awt.Point;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import platform.Lang;
import util.Define;

public class teste extends JDialog{

	public teste(DefaultCategoryDataset c){
		super();
		setTitle("Teste");
		setLocation(new Point ( 100,100));
		setSize(550, 450);
		
		JFreeChart chart3 = ChartFactory.createBarChart3D(
				"b", "a", null,
				c,PlotOrientation.VERTICAL, true, true, false);
		ChartPanel chartPanel = new ChartPanel(chart3, 500, 400, 500, 400, 500, 400, false, false, false, false, false, true);
		getContentPane().add(chartPanel);
	}
}
