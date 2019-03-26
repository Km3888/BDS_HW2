
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


//So here you're going to have to do a few little things:
//1. Based on the results of the kmeans part, you're going to go over all the points p which are in a given cluster c,
//then you want to find the corresponding 2-d representation of p that you get from PCA and add them all to a series
//which contains all the points from cluster c. This will all be done in the createDataset method and I added a little 
//example of how it'll work so it should be pretty simple
//2. Now you just have to call the main function, maybe edit a few of the graph settings so they make sense/look nice
//if need be

public class plot extends JFrame{
	private static final long serialVersionUID = 6294689542092367723L;
	
	public plot(String title) {
	    super(title);
	    
	    double[][] fake_matrix= {{1,2},{2,4},{7,-2},{1,2}};
	    XYDataset dataset = createDataset(fake_matrix);
	    
	    //PlotOrientation orientation=new PlotOrientation("Vertical");
	    JFreeChart chart = ChartFactory.createScatterPlot(
	        "Article Classification Examples", 
	        "Feature 1", "Feature 2", dataset,PlotOrientation.VERTICAL,true,false,false);

	    
	    //Changes background color
	    XYPlot plot = (XYPlot)chart.getPlot();
	    plot.setBackgroundPaint(new Color(255,228,196));
	    
	   
	    // Create Panel
	    ChartPanel panel = new ChartPanel(chart);
	    setContentPane(panel);
	  }
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
		      plot example = new plot("BDS Text Analytics Plot");
		      example.setSize(800, 400);
		      example.setLocationRelativeTo(null);
		      example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		      example.setVisible(true);
		    });
		
		
		
	}
	 
	 public XYDataset createDataset(double[][] matrix) {
		 XYSeriesCollection dataset=new XYSeriesCollection();
		 
		 //Replace this with actual matrix data according to type
		 XYSeries series1 = new XYSeries("Boys");
		    series1.add(1, 72.9);
		    series1.add(2, 81.6);
		    series1.add(3, 88.9);
		    series1.add(4, 96);
		    series1.add(5, 102.1);
		    series1.add(6, 108.5);
		    series1.add(7, 113.9);
		    series1.add(8, 119.3);
		    series1.add(9, 123.8);
		    series1.add(10, 124.4);

		    dataset.addSeries(series1);
		    
		   //Girls (Age,weight) series
		    XYSeries series2 = new XYSeries("Girls");
		    series2.add(1, 72.5);
		    series2.add(2, 80.1);
		    series2.add(3, 87.2);
		    series2.add(4, 94.5);
		    series2.add(5, 101.4);
		    series2.add(6, 107.4);
		    series2.add(7, 112.8);
		    series2.add(8, 118.2);
		    series2.add(9, 122.9);
		    series2.add(10, 123.4);
		    dataset.addSeries(series2);
		return dataset; 
	 }
}