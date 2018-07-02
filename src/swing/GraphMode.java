/*package principal;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

import java.awt.Checkbox;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.JRadioButton;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import java.awt.CardLayout;

public class GraphMode extends JFrame {

	private JPanel contentPane;
	JPanel graphXPanel;
	JPanel graphYPanel;
	JPanel graphZPanel;

	/**
	 * Launch the application.
	 */

	/**
	 * Create the frame.
	 *//*
	public GraphMode() {
		getContentPane().setLayout(null);

		graphXPanel = new JPanel();
		graphXPanel.setBounds(10, 11, 700, 200);
		getContentPane().add(graphXPanel);

		graphYPanel = new JPanel();
		graphYPanel.setBounds(10, 222, 700, 200);
		getContentPane().add(graphYPanel);

		graphZPanel = new JPanel();
		graphZPanel.setBounds(10, 433, 700, 200);
		getContentPane().add(graphZPanel);

		JPanel cheboxesPanel = new JPanel();
		cheboxesPanel.setBounds(717, 11, 226, 624);
		getContentPane().add(cheboxesPanel);

		try {
			plotGraph();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.setSize(969, 685);
		/*
		 * JPanel panel = new JPanel(); getContentPane().add(panel,
		 * BorderLayout.CENTER); setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		 * setBounds(100, 100, 450, 300); contentPane = new JPanel();
		 * contentPane.setBorder(new EmptyBorder(5, 5, 5, 5)); contentPane.setLayout(new
		 * BorderLayout(0, 0)); setContentPane(contentPane); contentPane.add
		 */
/*
	public void plotGraph() throws Exception {
		final XYChart chartX = new XYChartBuilder().width(700).height(200).title("X GRAPH").build();
		XChartPanel pnlChartX;
		pnlChartX = new XChartPanel(chartX);
		this.graphXPanel.add(pnlChartX);
		this.graphXPanel.validate();
		chartX.addSeries("S1", bluetooth.BluetoothConnection.s1.getGx());
		chartX.addSeries("S2", bluetooth.BluetoothConnection.s2.getGx());
		chartX.addSeries("S3", bluetooth.BluetoothConnection.s3.getGx());
		chartX.addSeries("S4", bluetooth.BluetoothConnection.s4.getGx());
		chartX.addSeries("S5", bluetooth.BluetoothConnection.s5.getGx());
		chartX.addSeries("S6", bluetooth.BluetoothConnection.s6.getGx());
		final XYChart chartY = new XYChartBuilder().width(700).height(200).title("Y GRAPH").build();
		XChartPanel pnlChartY;
		pnlChartY = new XChartPanel(chartY);
		this.graphYPanel.add(pnlChartY);
		this.graphYPanel.validate();
		chartY.addSeries("S1", bluetooth.BluetoothConnection.s1.getGy());
		chartY.addSeries("S2", bluetooth.BluetoothConnection.s2.getGy());
		chartY.addSeries("S3", bluetooth.BluetoothConnection.s3.getGy());
		chartY.addSeries("S4", bluetooth.BluetoothConnection.s4.getGy());
		chartY.addSeries("S5", bluetooth.BluetoothConnection.s5.getGy());
		chartY.addSeries("S6", bluetooth.BluetoothConnection.s6.getGy());
		final XYChart chartZ = new XYChartBuilder().width(700).height(200).title("Z GRAPH").build();
		XChartPanel pnlChartZ;
		pnlChartZ = new XChartPanel(chartZ);
		this.graphZPanel.add(pnlChartZ);
		this.graphYPanel.validate();
		chartZ.addSeries("S1", bluetooth.BluetoothConnection.s1.getGz());
		chartZ.addSeries("S2", bluetooth.BluetoothConnection.s2.getGz());
		chartZ.addSeries("S3", bluetooth.BluetoothConnection.s3.getGz());
		chartZ.addSeries("S4", bluetooth.BluetoothConnection.s4.getGz());
		chartZ.addSeries("S5", bluetooth.BluetoothConnection.s5.getGz());
		chartZ.addSeries("S6", bluetooth.BluetoothConnection.s6.getGz());

		/*
		 * while (true) { int i=0; phase += 2 * Math.PI * 2 / 20.0;
		 *
		 * Thread.sleep(100);
		 *
		 * final double[][] data = getSineData(phase);
		 *
		 * chart.updateXYSeries("sine", data[0], data[1], null); // sw.repaintChart(); }
		 */

		/*final Thread n = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					/*int rs=bluetooth.BluetoothConnection.s1.getGx().size();
					inHC05.s1.getGx()
					ArrayList<Double> l = new ArrayList<>();
					for(int i=0; i<rs; i++) {
						l.add((double) (rs-i));
					}
					Collections.sort(l);
					 chartX.updateXYSeries("S1",l , bluetooth.BluetoothConnection.s1.getGx(), null);
	*//*

					chartX.removeSeries("S1");
					try {
						Thread.currentThread().sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					chartX.addSeries("S1", bluetooth.BluetoothConnection.s1.getGx());
					chartX.append

				}
			}
		});
		n.start();*//*
	}

	private static double[][] getSineData(double phase) {

		double[] xData = new double[100];
		double[] yData = new double[100];
		for (int i = 0; i < xData.length; i++) {
			double radians = phase + (2 * Math.PI / xData.length * i);
			xData[i] = radians;
			yData[i] = Math.sin(radians);
		}
		return new double[][] { xData, yData };
	}
}*/
