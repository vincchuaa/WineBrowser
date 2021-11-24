package uk.ac.sheffield.assignment2021.gui;

import uk.ac.sheffield.assignment2021.codeprovided.*;
import uk.ac.sheffield.assignment2021.codeprovided.gui.*;
import java.util.*;
import java.util.List;

public class Histogram extends AbstractHistogram {
    /**
     * Constructor. Called by AbstractWineSampleBrowserPanel
     *
     * @param cellar              to allow for getting min / max / avg values
     * @param filteredWineSamples a List of WineSamples to generate a histogram for.
     *                            These have already been filtered by the GUI's queries.
     * @param property            the WineProperty to generate a histogram for.
     */
    public Histogram(AbstractWineSampleCellar cellar, List<WineSample> filteredWineSamples, WineProperty property)
    {
        super(cellar, filteredWineSamples, property);
    }

    /**
     * Updates the histogram based on a newly selected property and wine samples.
     */
    @Override
    public void updateHistogramContents(WineProperty property, List<WineSample> filteredWineSamples) {

    	List<HistogramBin> histogramBinList = new ArrayList<>();
    	List<Double> binRangeList = new ArrayList<>();
    	Map<String, Integer> counters = new LinkedHashMap<>();
    	
    	//Updates and remove previous data
    	this.property = property;
    	this.filteredWineSamples = filteredWineSamples;
    	wineCountsPerBin.clear();
    	
    	minPropertyValue = cellar.getMinimumValue(property, filteredWineSamples);
    	maxPropertyValue = cellar.getMaximumValue(property, filteredWineSamples);
    	
    	double value = minPropertyValue;
    	
    	//Find class interval 
    	double interval =(maxPropertyValue-minPropertyValue)/(NUMBER_BINS);
    	
    	//Add each bin range values into list
    	for(int i=0;i<11;i++) {
    		binRangeList.add(value);
    		value = value + interval;
    	}
    	binRangeList.add(maxPropertyValue+interval);
    	
    	//Instantiate histogram bins
    	//No bins are creataed if provided empty list
    	if(filteredWineSamples.size()==0) {
    		wineCountsPerBin.clear();
    	}
    	//Only one bin will be created if provided same wine samples
    	else if(verifyEqualContents(filteredWineSamples)) {
    		HistogramBin histogramBin = new HistogramBin(minPropertyValue, maxPropertyValue, true);
    		wineCountsPerBin.put(histogramBin,filteredWineSamples.size());
    	}
    	else {
	    	for(int i=0;i<NUMBER_BINS;i++) {
	    		//Create counters for each histogram bins
	    		counters.put("c"+i, 1);
	    		
	        	//Instantiate and add histogram bins to list
	    		if(i==NUMBER_BINS-1) {
	    			histogramBinList.add(new HistogramBin(binRangeList.get(i),binRangeList.get(i+1), true));
	    		}
	    		else
	    			histogramBinList.add(new HistogramBin(binRangeList.get(i),binRangeList.get(i+1), false));
	    	}
	    	
	    	for(int i=0;i<histogramBinList.size();i++) {
	    		wineCountsPerBin.put(histogramBinList.get(i), 0);
	    	}

	    	//Add histogram bins into wineCountsPerBin
	    	for(int i=0;i<filteredWineSamples.size();i++) {
	    		for(int j=0;j<histogramBinList.size();j++) {
	    			HistogramBin histogramBin = histogramBinList.get(j);
	    			double propertyValue = filteredWineSamples.get(i).getProperty(property);
	    			
	    			//Increase counter by 1 if property value fall into corresponding bin's range
	    			if(histogramBin.valueInBin(propertyValue)) {
	    				wineCountsPerBin.put(histogramBin, counters.put("c"+j, counters.get("c"+j)+1));
	    			}
	    		}
	    	}
    	}
    }
    
    /**
     * Are the contents of the list the same?
     * @param filteredWineSamples the wine samples list that needs to be checked
     * @return true if the list contents are the same
     */
    public boolean verifyEqualContents(List<WineSample> filteredWineSamples) {
    	for(WineSample ws : filteredWineSamples) {
    		if(!ws.equals(filteredWineSamples.get(0)))
    			return false;
    	}
		return true;
    }
    
    /**
     * Calculate the average value of a given property from the current filteredWineSamples
     */
    @Override
    public double getAveragePropertyValue() throws NoSuchElementException {
    	List<Double> valueList = new ArrayList<Double>();
    	double total = 0;
    	
        for(WineSample ws : filteredWineSamples) 
        	valueList.add(ws.getProperty(property));
        
        for(double d : valueList) 
        	total = total + d;
        
        return total/valueList.size();
    }
}
