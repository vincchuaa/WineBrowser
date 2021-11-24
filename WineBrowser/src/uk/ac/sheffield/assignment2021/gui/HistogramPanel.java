package uk.ac.sheffield.assignment2021.gui;

import uk.ac.sheffield.assignment2021.codeprovided.gui.AbstractHistogram;

import uk.ac.sheffield.assignment2021.codeprovided.gui.AbstractHistogramPanel;
import uk.ac.sheffield.assignment2021.codeprovided.gui.AbstractWineSampleBrowserPanel;
import uk.ac.sheffield.assignment2021.codeprovided.gui.HistogramBin;

import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;

public class HistogramPanel extends AbstractHistogramPanel
{
    public HistogramPanel(AbstractWineSampleBrowserPanel parentPanel, AbstractHistogram histogram)
    {
        super(parentPanel, histogram);
    }

    @Override
    protected void paintComponent(Graphics g) {
    	DecimalFormat df = new DecimalFormat("##.###");
        super.paintComponent(g);
        Dimension d = getSize();
        Graphics2D g2 = (Graphics2D) g;
        
        //Variable declaration
        List<HistogramBin> histogramBins = new ArrayList<>(getHistogram().getBinsInBoundaryOrder());
        List<Integer> frequency = new ArrayList<>(getHistogram().getWineCountsPerBin().values());
        List<HistogramBin> wineCountsPerBin = new ArrayList<>(getHistogram().getBinsInBoundaryOrder());
        List<Integer> binRange = new ArrayList<>();
        List<Double> xCoordinates = new ArrayList<>();
        List<Double> yCoordinates = new ArrayList<>();
        double average = getHistogram().getAveragePropertyValue();
        double min = getHistogram().getMinPropertyValue();
        double max =getHistogram().getMaxPropertyValue();
        int width = (int) (d.getWidth()/histogramBins.size());
        int interval = width;
        
        Font myFont = new Font("Helvetica", Font.BOLD,15);
        g.setFont(myFont);
        
        //Setup bin's interval
        binRange.add(0);
        for(int i=0;i<histogramBins.size();i++) {
        	binRange.add(width);
        	width =(int) (width + (d.getWidth()/histogramBins.size()));
        }
        
        //Setup x-coordinates for each bins
        for(int i=0;i<wineCountsPerBin.size();i++) {
        	xCoordinates.add(Double.parseDouble(df.format(wineCountsPerBin.get(i).getLowerBoundary())));
        }
        //Add last x-coordinate
        xCoordinates.add(Double.parseDouble(df.format(wineCountsPerBin.get(wineCountsPerBin.size()-1).getUpperBoundary())));
        
        //Setup y-coordinates for each bins
        for(int i=0;i<frequency.size();i++) {
        	double biggestBin = getHistogram().largestBinCount();
        	double value = (frequency.get(i)/biggestBin)*400;
        	yCoordinates.add(value);
        }
        
        //Fill histogram bins 
        for(int i=0;i<histogramBins.size();i++) {
            g2.setColor(Color.BLUE);
        	g2.fill(new Rectangle2D.Double(binRange.get(i),420-yCoordinates.get(i),interval,yCoordinates.get(i)));
        }
        
        //Draws each histogram bin's outer lines
        for(int i=0;i<histogramBins.size();i++) {
	        g2.setColor(Color.BLACK);
	        g2.draw(new Rectangle2D.Double(binRange.get(i),420-yCoordinates.get(i),interval,yCoordinates.get(i)));
        	
	        //Prints out each frequency for each bins
	        g2.drawString(frequency.get(i).toString(), binRange.get(i+1)-10-interval/2, (int) (415-yCoordinates.get(i)));
	      
	        //Prints out x coordinates
        	g2.drawString(xCoordinates.get(i).toString(),(int) (binRange.get(i)),440);
        }
        g2.drawString(xCoordinates.get(xCoordinates.size()-1).toString(),d.width-40,440);
        
        //Draws out the average vertical line
        g2.setColor(Color.RED);
        double line = d.width*((average-min)/(max-min));
        g2.draw(new Line2D.Double(line,0.0,line,420.0));
        g2.drawString(df.format(average), (int)line-10, d.height);
    }
}
