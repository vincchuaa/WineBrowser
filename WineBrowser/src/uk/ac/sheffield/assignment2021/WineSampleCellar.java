package uk.ac.sheffield.assignment2021;

import java.util.*;
import uk.ac.sheffield.assignment2021.codeprovided.*;

public class WineSampleCellar extends AbstractWineSampleCellar {
    /**
     * Constructor - reads wine sample datasets and list of queries from text file,
     * and initialises the wineSampleRacks Map
     *
     * @param redWineFilename
     * @param whiteWineFilename
     */
    public WineSampleCellar(String redWineFilename, String whiteWineFilename) {
        super(redWineFilename, whiteWineFilename);
    }
    
    /**
     * Parse the properties from a given line from a wine file.
     *
     * @param line the line to parse
     * @return a WinePropertyMap constructed from the parsed row, containing values for every property
     * @throws IllegalArgumentException if the line is malformed (i.e. does not include every property
     * for a single wine, or contains undefined properties)
     */
    @Override
    public WinePropertyMap parseWineFileLine(String line) throws IllegalArgumentException {
    	ArrayList<Double> d = new ArrayList<Double>();
    	WinePropertyMap map = new WinePropertyMap();
    	
    	int size = WineProperty.values().length;
    	String[] data = line.split(";");
        
        if(data.length!=size) 
            throw new IllegalArgumentException("Wine property format is incorrect");
        else {
	    	for(int i=0;i<size;i++) {
	    		d.add(Double.parseDouble(data[i]));
	    		map.put(WineProperty.values()[i],d.get(i));
	    	}
        }
        
        return map;
    }

    /**
     * Updates wineSampleRacks to contain 'also' an additional list
     * containing ALL wine samples (in this case red and white)
     */
    @Override
    public void updateCellar() {
    	List<WineSample> red = new ArrayList<>(wineSampleRacks.get(WineType.RED));
    	List<WineSample> white = new ArrayList<>(wineSampleRacks.get(WineType.WHITE));
		List<WineSample> all = new ArrayList<>(red);
		
    	all.addAll(white);

        wineSampleRacks.put(WineType.ALL, all);
    }

    /**
     * Get the minimum value of the given property for the specified wines
     * @param wineProperty the property to evaluate
     * @param wineList the wines to check
     * @return the minimum measurement of the property present in the specified wines
     * @throws NoSuchElementException if there are no minimum values for the property, because wineList is invalid
     */
    @Override
    public double getMinimumValue(WineProperty wineProperty, List<WineSample> wineList)
            throws NoSuchElementException {
	    ArrayList<Double> d = new ArrayList<Double>();
	    
    	try {
    		if(wineList.isEmpty()) 
    			return 0;
    	    
		    for(WineSample ws: wineList) 
		    	d.add(ws.getProperty(wineProperty));
		    
		    return Collections.min(d);
    	}
    	catch(NoSuchElementException e) {
    		throw new NoSuchElementException("Invalid wine list");
    	}
    }

    /**
     * Get the maximum value of the given property for the specified wines
     * @param wineProperty the property to evaluate
     * @param wineList the wines to check
     * @return the maximum measurement of the property present in the specified wines
     * @throws NoSuchElementException if there are no maximum values for the property, because wineList is invalid
     */
    @Override
    public double getMaximumValue(WineProperty wineProperty, List<WineSample> wineList)
            throws NoSuchElementException {
	    ArrayList<Double> d = new ArrayList<Double>();
	    
    	try {
    		if(wineList.isEmpty()) 
    			return 0;
    		
		    for(WineSample ws: wineList) 
		    	d.add(ws.getProperty(wineProperty));
		    
		    return Collections.max(d);
	    	}
    	catch(NoSuchElementException e) {
    		throw new NoSuchElementException("Invalid wine list");
    	}
    }

    /**
     * Get the mean value of the given property for the specified wines
     * @param wineProperty the property to evaluate
     * @param wineList the wines to check
     * @return the mean measurement of the property present in the specified wines
     * @throws NoSuchElementException if there are no maximum values for the property, because wineList is invalid
     */
    @Override
    public double getMeanAverageValue(WineProperty wineProperty, List<WineSample> wineList)
            throws NoSuchElementException {
    	
	    ArrayList<Double> d = new ArrayList<Double>();
	    double total = 0;
	    int counter = 0;
	    double mean;
	    
	    if(wineList.isEmpty()) 
			return 0;
		
	    if(wineList.size()!=0) {
	    	
		    for(WineSample ws: wineList) 
		    	d.add(ws.getProperty(wineProperty));
		    
		   	for(int i=0;i<d.size();i++) {
		    	total = total + d.get(i);
		    	counter++;
		    }
		    	
		    mean = total/counter;
		    return mean;
	    }
	    else
	   		throw new NoSuchElementException("Invalid wine list");
    }

    /**
     * Get the first 5 wines of the given wine type
     * Note: this will only be applied to RED and WHITE wines; not WineType.ALL
     * @param type the WineType to get the first wines of
     * @return a List of the first 5 wines of the given type
     */
    @Override
    public List<WineSample> getFirstFiveWines(WineType type) {
    	int limit = 5;
    	List<WineSample> list = new ArrayList<WineSample>(wineSampleRacks.get(type));
    	List<WineSample> firstFive = new ArrayList<WineSample>();
    	
    	for(int i=0;i<limit;i++) 
    		firstFive.add(list.get(i));
    	
        return firstFive;
    }
}
