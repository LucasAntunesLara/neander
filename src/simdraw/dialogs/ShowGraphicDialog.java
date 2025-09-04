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
import org.jfree.chart.renderer.category.CategoryItemRenderer;

import processor.Processor;
import processor.RodadaDeSimulacaoRisa;
import platform.Lang;
import util.Define;

public class ShowGraphicDialog extends JDialog implements ActionListener, Define{
	
	public ShowGraphicDialog(Processor p, boolean[] v, boolean[] r){
		super();
		setTitle(Lang.iLang==ENGLISH?Lang.msgsGUI[334]:Lang.msgsGUI[333]);
		setLocation(new Point ( 100,100));
		setSize(550, 450);
		pProc = p;
		values = v;
		risa = r;
		createRisaGraphic();
		createSimulationGraphic();
		chartPanel = new ChartPanel(chart, 500, 400, 500, 400, 500, 400, false, false, false, false, false, true);
		Container c = getContentPane();
		JPanel panel = new JPanel();
		JPanel pnorth = new JPanel();
		JPanel psouth = new JPanel();
		panel.setLayout(new BorderLayout());
		brisa = new JButton(Lang.iLang==ENGLISH?Lang.msgsGUI[336]:Lang.msgsGUI[335]);
		brisa.addActionListener(this);
		best = new JButton(Lang.iLang==ENGLISH?Lang.msgsGUI[338]:Lang.msgsGUI[337]);
		best.addActionListener(this);
		tname = new JTextField(30);
		tname.addActionListener(this);
		bsave = new JButton(Lang.iLang==ENGLISH?Lang.msgsGUI[348]:Lang.msgsGUI[347]);
		bsave.addActionListener(this);
		panel.add(chartPanel, BorderLayout.CENTER);
		pnorth.add(brisa);
		pnorth.add(best);
		psouth.add(tname);
		psouth.add(bsave);
		panel.add(pnorth, BorderLayout.NORTH);
		panel.add(psouth, BorderLayout.SOUTH);
		c.add(panel);
	}
	
	public void actionPerformed(ActionEvent arg0){
		if ( arg0.getSource() == (Object) brisa){
			chartPanel.setChart(chart);
		}else if (arg0.getSource() == (Object) best){
			chartPanel.setChart(chart2);
		}else if (arg0.getSource() == (Object) bsave){
			if (chartPanel.getChart() == chart){
				try {
	        		ChartUtilities.saveChartAsJPEG(new File(tname.getText() + ".jpg"), chart, 500, 400);
	        	} catch (Exception e) {
	        		JOptionPane.showMessageDialog(this, "Problem occurred creating chart.");
	        	}
			}else if (chartPanel.getChart() == chart2){
				try {
	        		ChartUtilities.saveChartAsJPEG(new File(tname.getText() + ".jpg"), chart2, 500, 400);
	        	} catch (Exception e) {
	        		JOptionPane.showMessageDialog(this, "Problem occurred creating chart.");
	        	}
			}
		}
	}
	
	protected void createRisaGraphic(){
		DefaultCategoryDataset categoryDataset = new DefaultCategoryDataset();
		int rodada = 0;
		if (risa[0] == true){
			pProc.rodadaRisa = (RodadaDeSimulacaoRisa)pProc.setRodadaRisa.get(++rodada);
			if (values[0] == true){
				categoryDataset.addValue(pProc.rodadaRisa.getOverflow(), "Overflow", Lang.iLang==ENGLISH?Lang.msgsGUI[292]:Lang.msgsGUI[291]);
			}if (values[1] == true){
				categoryDataset.addValue(pProc.rodadaRisa.getHandling(), Lang.iLang==ENGLISH?Lang.msgsGUI[321]:Lang.msgsGUI[293], Lang.iLang==ENGLISH?Lang.msgsGUI[292]:Lang.msgsGUI[291]);
			}if (values[2] == true){
				categoryDataset.addValue(pProc.rodadaRisa.getSmallsize(), Lang.iLang==ENGLISH?Lang.msgsGUI[322]:Lang.msgsGUI[294], Lang.iLang==ENGLISH?Lang.msgsGUI[292]:Lang.msgsGUI[291]);
			}
		}
		if (risa[1] == true){
			pProc.rodadaRisa = (RodadaDeSimulacaoRisa)pProc.setRodadaRisa.get(++rodada);
			if (values[0] == true){
				categoryDataset.addValue(pProc.rodadaRisa.getOverflow(), "Overflow", "rISA-0");
			}if (values[1] == true){
				categoryDataset.addValue(pProc.rodadaRisa.getHandling(), Lang.iLang==ENGLISH?Lang.msgsGUI[321]:Lang.msgsGUI[293], "rISA-0");
			}if (values[2] == true){
				categoryDataset.addValue(pProc.rodadaRisa.getSmallsize(), Lang.iLang==ENGLISH?Lang.msgsGUI[322]:Lang.msgsGUI[294], "rISA-0");
			}
		}
		if (risa[2] == true){
			pProc.rodadaRisa = (RodadaDeSimulacaoRisa)pProc.setRodadaRisa.get(++rodada);
			if (values[0] == true){
				categoryDataset.addValue(pProc.rodadaRisa.getOverflow(), "Overflow", "rISA-1");
				//categoryDataset.addValue(results[2][0], "rISA-1", "Overflow");
			}if (values[1] == true){
				categoryDataset.addValue(pProc.rodadaRisa.getHandling(), Lang.iLang==ENGLISH?Lang.msgsGUI[321]:Lang.msgsGUI[293], "rISA-1");
				//categoryDataset.addValue(results[2][1], "rISA-1", Lang.iLang==ENGLISH?Lang.msgsGUI[321]:Lang.msgsGUI[293]);
			}if (values[2] == true){
				categoryDataset.addValue(pProc.rodadaRisa.getSmallsize(), Lang.iLang==ENGLISH?Lang.msgsGUI[322]:Lang.msgsGUI[294], "rISA-1");
				//categoryDataset.addValue(results[2][2], "rISA-1", Lang.iLang==ENGLISH?Lang.msgsGUI[322]:Lang.msgsGUI[294]);
			}
			System.out.println(rodada);
		}
		if (risa[3] == true){
			pProc.rodadaRisa = (RodadaDeSimulacaoRisa)pProc.setRodadaRisa.get(++rodada);
			if (values[0] == true){
				categoryDataset.addValue(pProc.rodadaRisa.getOverflow(), "Overflow", "rISA-2");
				//categoryDataset.addValue(results[3][0], "rISA-2", "Overflow");
			}if (values[1] == true){
				categoryDataset.addValue(pProc.rodadaRisa.getHandling(), Lang.iLang==ENGLISH?Lang.msgsGUI[321]:Lang.msgsGUI[293], "rISA-2");
				//categoryDataset.addValue(results[3][1], "rISA-2", Lang.iLang==ENGLISH?Lang.msgsGUI[321]:Lang.msgsGUI[293]);
			}if (values[2] == true){
				categoryDataset.addValue(pProc.rodadaRisa.getSmallsize(), Lang.iLang==ENGLISH?Lang.msgsGUI[322]:Lang.msgsGUI[294], "rISA-2");
				//categoryDataset.addValue(results[3][2], "rISA-2", Lang.iLang==ENGLISH?Lang.msgsGUI[322]:Lang.msgsGUI[294]);
			}
		}
		if (risa[4] == true){
			pProc.rodadaRisa = (RodadaDeSimulacaoRisa)pProc.setRodadaRisa.get(++rodada);
			if (values[0] == true){
				categoryDataset.addValue(pProc.rodadaRisa.getOverflow(), "Overflow", "rISA-3");
				//categoryDataset.addValue(results[4][0], "rISA-3", "Overflow");
			}if (values[1] == true){
				categoryDataset.addValue(pProc.rodadaRisa.getHandling(), Lang.iLang==ENGLISH?Lang.msgsGUI[321]:Lang.msgsGUI[293], "rISA-3");
				//categoryDataset.addValue(results[4][1], "rISA-3", Lang.iLang==ENGLISH?Lang.msgsGUI[321]:Lang.msgsGUI[293]);
			}if (values[2] == true){
				categoryDataset.addValue(pProc.rodadaRisa.getSmallsize(), Lang.iLang==ENGLISH?Lang.msgsGUI[322]:Lang.msgsGUI[294], "rISA-3");
				//categoryDataset.addValue(results[4][2], "rISA-3", Lang.iLang==ENGLISH?Lang.msgsGUI[322]:Lang.msgsGUI[294]);
			}
		}
		if (risa[5] == true){
			pProc.rodadaRisa = (RodadaDeSimulacaoRisa)pProc.setRodadaRisa.get(++rodada);
			if (values[0] == true){
				categoryDataset.addValue(pProc.rodadaRisa.getOverflow(), "Overflow", "rISA-4");
				//categoryDataset.addValue(results[5][0], "rISA-4", "Overflow");
			}if (values[1] == true){
				categoryDataset.addValue(pProc.rodadaRisa.getHandling(), Lang.iLang==ENGLISH?Lang.msgsGUI[321]:Lang.msgsGUI[293], "rISA-4");
				//categoryDataset.addValue(results[5][1], "rISA-4", Lang.iLang==ENGLISH?Lang.msgsGUI[321]:Lang.msgsGUI[293]);
			}if (values[2] == true){
				categoryDataset.addValue(pProc.rodadaRisa.getSmallsize(), Lang.iLang==ENGLISH?Lang.msgsGUI[322]:Lang.msgsGUI[294], "rISA-4");
				//categoryDataset.addValue(results[5][2], "rISA-4", Lang.iLang==ENGLISH?Lang.msgsGUI[322]:Lang.msgsGUI[294]);
			}
		}
		if (risa[6] == true){
			pProc.rodadaRisa = (RodadaDeSimulacaoRisa)pProc.setRodadaRisa.get(++rodada);
			if (values[0] == true){
				categoryDataset.addValue(pProc.rodadaRisa.getOverflow(), "Overflow", Lang.iLang==ENGLISH?Lang.msgsGUI[299]:Lang.msgsGUI[298]);
			}if (values[1] == true){
				categoryDataset.addValue(pProc.rodadaRisa.getHandling(), Lang.iLang==ENGLISH?Lang.msgsGUI[321]:Lang.msgsGUI[293], Lang.iLang==ENGLISH?Lang.msgsGUI[299]:Lang.msgsGUI[298]);
			}if (values[2] == true){
				categoryDataset.addValue(pProc.rodadaRisa.getSmallsize(), Lang.iLang==ENGLISH?Lang.msgsGUI[322]:Lang.msgsGUI[294], Lang.iLang==ENGLISH?Lang.msgsGUI[299]:Lang.msgsGUI[298]);
			}
		}
		if (risa[7] == true){
			pProc.rodadaRisa = (RodadaDeSimulacaoRisa)pProc.setRodadaRisa.get(++rodada);
			if (values[0] == true){
				categoryDataset.addValue(pProc.rodadaRisa.getOverflow(), "Overflow", Lang.iLang==ENGLISH?Lang.msgsGUI[299]:Lang.msgsGUI[298]);
			}if (values[1] == true){
				categoryDataset.addValue(pProc.rodadaRisa.getHandling(), Lang.iLang==ENGLISH?Lang.msgsGUI[321]:Lang.msgsGUI[293], Lang.iLang==ENGLISH?Lang.msgsGUI[299]:Lang.msgsGUI[298]);
			}if (values[2] == true){
				categoryDataset.addValue(pProc.rodadaRisa.getSmallsize(), Lang.iLang==ENGLISH?Lang.msgsGUI[322]:Lang.msgsGUI[294], Lang.iLang==ENGLISH?Lang.msgsGUI[299]:Lang.msgsGUI[298]);
			}
		}
	
		chart = ChartFactory.createBarChart3D(
				Lang.iLang==ENGLISH?Lang.msgsGUI[340]:Lang.msgsGUI[339], null, Lang.iLang==ENGLISH?Lang.msgsGUI[344]:Lang.msgsGUI[343],
				categoryDataset,PlotOrientation.VERTICAL, true, true, false);
		
		/*CategoryPlot plot = chart.getCategoryPlot();
		plot.getRangeAxis().setRange(0, 100);*/
		
        try {
        	ChartUtilities.saveChartAsJPEG(new File("x.jpg"), chart, 500, 400);
        } catch (Exception e) {
        	JOptionPane.showMessageDialog(this, "Problem occurred creating chart.");
        }
	}
	
	protected void createSimulationGraphic(){
		DefaultCategoryDataset categoryDataset = new DefaultCategoryDataset();
		int rodada = 0;
		if (risa[0] == true){
			pProc.rodadaRisa = (RodadaDeSimulacaoRisa)pProc.setRodadaRisa.get(++rodada);
			if(values[3] == true){	
				categoryDataset.addValue(pProc.rodadaRisa.getNroInstructionsFetched(), Lang.iLang==ENGLISH?Lang.msgsGUI[323]:Lang.msgsGUI[295], Lang.iLang==ENGLISH?Lang.msgsGUI[292]:Lang.msgsGUI[291]);
				//categoryDataset.addValue(/*pProc.rodadaRisa.getNroInstructionsFetched()*/1, "Sem rISA", "");
			}if (values[4] == true){	
				categoryDataset.addValue(pProc.rodadaRisa.getExecutionCycles(), Lang.iLang==ENGLISH?Lang.msgsGUI[324]:Lang.msgsGUI[296], Lang.iLang==ENGLISH?Lang.msgsGUI[292]:Lang.msgsGUI[291]);
			}if (values[5] == true){
				categoryDataset.addValue(pProc.rodadaRisa.getTempoDeSimulacao(), Lang.iLang==ENGLISH?Lang.msgsGUI[326]:Lang.msgsGUI[325], Lang.iLang==ENGLISH?Lang.msgsGUI[292]:Lang.msgsGUI[291]);
			}
		}
		if (risa[1] == true){
			pProc.rodadaRisa = (RodadaDeSimulacaoRisa)pProc.setRodadaRisa.get(++rodada);
			if (values[3] == true){
				categoryDataset.addValue(pProc.rodadaRisa.getNroInstructionsFetched(), Lang.iLang==ENGLISH?Lang.msgsGUI[323]:Lang.msgsGUI[295], "rISA-0");
			}if (values[4] == true){
				categoryDataset.addValue(pProc.rodadaRisa.getExecutionCycles(), Lang.iLang==ENGLISH?Lang.msgsGUI[324]:Lang.msgsGUI[296], "rISA-0");
			}if (values[5] == true){
				categoryDataset.addValue(pProc.rodadaRisa.getTempoDeSimulacao(), Lang.iLang==ENGLISH?Lang.msgsGUI[326]:Lang.msgsGUI[325], "rISA-0");
			}	
		}
		if(risa[2] == true){
			pProc.rodadaRisa = (RodadaDeSimulacaoRisa)pProc.setRodadaRisa.get(++rodada);
			if (values[3] == true){
				categoryDataset.addValue(pProc.rodadaRisa.getNroInstructionsFetched(), Lang.iLang==ENGLISH?Lang.msgsGUI[323]:Lang.msgsGUI[295], "rISA-1");
			}if (values[4] == true){
				categoryDataset.addValue(pProc.rodadaRisa.getExecutionCycles(), Lang.iLang==ENGLISH?Lang.msgsGUI[324]:Lang.msgsGUI[296], "rISA-1");
			}if (values[5] == true){
				categoryDataset.addValue(pProc.rodadaRisa.getTempoDeSimulacao(), Lang.iLang==ENGLISH?Lang.msgsGUI[326]:Lang.msgsGUI[325], "rISA-1");
			}	
		}
		if (risa[3] == true){
			pProc.rodadaRisa = (RodadaDeSimulacaoRisa)pProc.setRodadaRisa.get(++rodada);
			if (values[3] == true){
				categoryDataset.addValue(pProc.rodadaRisa.getNroInstructionsFetched(), Lang.iLang==ENGLISH?Lang.msgsGUI[323]:Lang.msgsGUI[295], "rISA-2");
			}if (values[4] == true){	
				categoryDataset.addValue(pProc.rodadaRisa.getExecutionCycles(), Lang.iLang==ENGLISH?Lang.msgsGUI[324]:Lang.msgsGUI[296], "rISA-2");
			}if (values[5] == true){	
				categoryDataset.addValue(pProc.rodadaRisa.getTempoDeSimulacao(), Lang.iLang==ENGLISH?Lang.msgsGUI[326]:Lang.msgsGUI[325], "rISA-2");
			}	
		}
		if (risa[4] == true){
			pProc.rodadaRisa = (RodadaDeSimulacaoRisa)pProc.setRodadaRisa.get(++rodada);
			if (values[3] == true){
				categoryDataset.addValue(pProc.rodadaRisa.getNroInstructionsFetched(), Lang.iLang==ENGLISH?Lang.msgsGUI[323]:Lang.msgsGUI[295], "rISA-3");
				//categoryDataset.addValue(/*pProc.rodadaRisa.getNroInstructionsFetched()*/results[1]/results[0], "rISA_8ops", "");
			}if (values[4] == true){	
				categoryDataset.addValue(pProc.rodadaRisa.getExecutionCycles(), Lang.iLang==ENGLISH?Lang.msgsGUI[324]:Lang.msgsGUI[296], "rISA-3");
			}if (values[5] == true){	
				categoryDataset.addValue(pProc.rodadaRisa.getTempoDeSimulacao(), Lang.iLang==ENGLISH?Lang.msgsGUI[326]:Lang.msgsGUI[325], "rISA-3");
			}	
		}
		if (risa[5] == true){
			pProc.rodadaRisa = (RodadaDeSimulacaoRisa)pProc.setRodadaRisa.get(++rodada);
			if (values[3] == true){
				categoryDataset.addValue(pProc.rodadaRisa.getNroInstructionsFetched(), Lang.iLang==ENGLISH?Lang.msgsGUI[323]:Lang.msgsGUI[295], "rISA-4");
				//categoryDataset.addValue(/*pProc.rodadaRisa.getNroInstructionsFetched()*/results[2]/results[0], "rISA_16ops", "");
			}if (values[4] == true){	
				categoryDataset.addValue(pProc.rodadaRisa.getExecutionCycles(), Lang.iLang==ENGLISH?Lang.msgsGUI[324]:Lang.msgsGUI[296], "rISA-4");
			}if (values[5] == true){	
				categoryDataset.addValue(pProc.rodadaRisa.getTempoDeSimulacao(), Lang.iLang==ENGLISH?Lang.msgsGUI[326]:Lang.msgsGUI[325], "rISA-4");
			}	
		}
		if (risa[6] == true){
			pProc.rodadaRisa = (RodadaDeSimulacaoRisa)pProc.setRodadaRisa.get(++rodada);
			if (values[3] == true){
				categoryDataset.addValue(pProc.rodadaRisa.getNroInstructionsFetched(), Lang.iLang==ENGLISH?Lang.msgsGUI[323]:Lang.msgsGUI[295], Lang.iLang==ENGLISH?Lang.msgsGUI[299]:Lang.msgsGUI[298]);
			}if (values[4] == true){
				categoryDataset.addValue(pProc.rodadaRisa.getExecutionCycles(), Lang.iLang==ENGLISH?Lang.msgsGUI[324]:Lang.msgsGUI[296], Lang.iLang==ENGLISH?Lang.msgsGUI[299]:Lang.msgsGUI[298]);
			}if (values[5] == true){	
				categoryDataset.addValue(pProc.rodadaRisa.getTempoDeSimulacao(), Lang.iLang==ENGLISH?Lang.msgsGUI[326]:Lang.msgsGUI[325], Lang.iLang==ENGLISH?Lang.msgsGUI[299]:Lang.msgsGUI[298]);
			}	
		}
		if (risa[7] == true){
			pProc.rodadaRisa = (RodadaDeSimulacaoRisa)pProc.setRodadaRisa.get(++rodada);
			if (values[3] == true){
				categoryDataset.addValue(pProc.rodadaRisa.getNroInstructionsFetched(), Lang.iLang==ENGLISH?Lang.msgsGUI[323]:Lang.msgsGUI[295], Lang.iLang==ENGLISH?Lang.msgsGUI[299]:Lang.msgsGUI[298]);
			}if (values[4] == true){
				categoryDataset.addValue(pProc.rodadaRisa.getExecutionCycles(), Lang.iLang==ENGLISH?Lang.msgsGUI[324]:Lang.msgsGUI[296], Lang.iLang==ENGLISH?Lang.msgsGUI[299]:Lang.msgsGUI[298]);
			}if (values[5] == true){	
				categoryDataset.addValue(pProc.rodadaRisa.getTempoDeSimulacao(), Lang.iLang==ENGLISH?Lang.msgsGUI[326]:Lang.msgsGUI[325], Lang.iLang==ENGLISH?Lang.msgsGUI[299]:Lang.msgsGUI[298]);
			}	
		}
				
		chart2 = ChartFactory.createBarChart3D(
				/*Lang.iLang==ENGLISH?Lang.msgsGUI[346]:Lang.msgsGUI[345]*/"Reduo do consumo de energia", "Instrues buscadas", null,
				categoryDataset,PlotOrientation.VERTICAL, true, true, false);
		
		CategoryItemRenderer ren = chart2.getCategoryPlot().getRenderer();
		ren.setSeriesPaint(0, java.awt.Color.WHITE);
		ren.setSeriesPaint(1, java.awt.Color.GRAY);
		ren.setSeriesPaint(2, java.awt.Color.BLUE);
		
		try {
        	ChartUtilities.saveChartAsJPEG(new File("x1.jpg"), chart2, 500, 400);
        } catch (Exception e) {
        	JOptionPane.showMessageDialog(this, "Problem occurred creating chart.");
        }
		
	}
	
	Processor pProc;
	boolean[] values, risa;
	JFreeChart chart, chart2;
	JButton brisa, best, bsave;
	ChartPanel chartPanel;
	JTextField tname;
}
