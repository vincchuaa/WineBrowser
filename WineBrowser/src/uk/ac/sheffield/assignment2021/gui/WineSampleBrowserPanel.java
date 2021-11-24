package uk.ac.sheffield.assignment2021.gui;

import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.awt.event.ActionEvent;

import uk.ac.sheffield.assignment2021.codeprovided.*;
import uk.ac.sheffield.assignment2021.codeprovided.gui.AbstractWineSampleBrowserPanel;

public class WineSampleBrowserPanel extends AbstractWineSampleBrowserPanel {
	
	private DecimalFormat df = new DecimalFormat("###.##");
	
    public WineSampleBrowserPanel(AbstractWineSampleCellar cellar) {
        super(cellar);
    }

    /**
     * Add relevant actionListeners to the GUI components
     */
    @Override
    public void addListeners() {
    	//Event listener for add filter button
    	buttonAddFilter.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent e) {
    			addFilter();
    			updateHistogram();
    		}
    	});
    	
    	//Event listener for clear filter button
    	buttonClearFilters.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent e) {
    			clearFilters();
    			updateHistogram();
    		}
    	});
    	
    	//Event listener for comboWineTypes
    	comboWineTypes.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent e) {
    			//Retrieve wine type info from GUI
    		    WineType value = null;
    		    String wineType = (String) comboWineTypes.getSelectedItem();
    			
    		    if(wineType.equals("RED")) 
    		    	value = WineType.RED;
    		    else if(wineType.equals("WHITE")) 
    		    	value = WineType.WHITE;
    		    else 
    		    	value = WineType.ALL;
    		    
    		    //Updates the wine sample list 
    		    filteredWineSampleList = cellar.getWineSampleList(value);
    		    
    		    //Updates the GUI components
    		    executeQuery();
    		    updateStatistics();
    		    updateWineDetailsBox();
    		    updateHistogram();
    		}
    	});
    	
    	//Event listener for comboHistogramProperties
    	comboHistogramProperties.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent e) {
    			updateHistogram();
    			repaint();
    		}
    	});
    }

    /**
     * Adds a new filter and updates the GUI results accordingly
     */
    @Override
    public void addFilter() {
    	//Retrieve each info of the selected properties
    	String wineProperty = (String) comboQueryProperties.getSelectedItem();
    	String operator = (String) comboOperators.getSelectedItem();
    	Double d = Double.parseDouble(value.getText());
    
    	//Convert string format to WineProperty
    	WineProperty w = WineProperty.fromName(wineProperty);
 
    	//Add filter/sub query into the list
    	subQueryList.add(new SubQuery(w,operator,d));
    	
    	//Prints out the filter
        subQueriesTextArea.setText(subQueryList.toString());
         
        //Updates the GUI components 
        executeQuery();
        updateStatistics();
        updateWineDetailsBox();
    }

    /**
     * Clears all filters and updates the relevant GUI components 
     */
    @Override
    public void clearFilters() {
    	//Remove the sub query list
        subQueryList.removeAll(subQueryList);
        //Clear the text area
        subQueriesTextArea.selectAll();
        subQueriesTextArea.replaceSelection("");
        
        //Updates the GUI components 
        executeQuery();
        updateStatistics();
        updateWineDetailsBox();
    }

    /**
     * Updates the data of statistics on text area accordingly
     */
    @Override
    public void updateStatistics() {
    	statisticsTextArea.setText("\tFixedAcidity\tVolatileAcidity\t"
				+ "CitricAcid\tResidualSugar\tChlorides\tFreeSulfurDioxide\tTotalSulfurDioxide\t"
				+ "Density\tpH\tSulphates\tAlcohol\tQuality");
    	
    	//Show minimum,maximum and mean values
    	if(!filteredWineSampleList.isEmpty()) {
	    	//Minimum values for each properties
	    	statisticsTextArea.append("\nMinimum:\t"+ cellar.getMinimumValue(WineProperty.FixedAcidity, filteredWineSampleList)
	    	+ "\t" + cellar.getMinimumValue(WineProperty.VolatileAcidity, filteredWineSampleList)
	    	+ "\t" + cellar.getMinimumValue(WineProperty.CitricAcid, filteredWineSampleList)
	    	+ "\t" + cellar.getMinimumValue(WineProperty.ResidualSugar, filteredWineSampleList)
	    	+ "\t" + cellar.getMinimumValue(WineProperty.Chlorides, filteredWineSampleList)
	    	+ "\t" + cellar.getMinimumValue(WineProperty.FreeSulfurDioxide, filteredWineSampleList)
	    	+ "\t\t" + cellar.getMinimumValue(WineProperty.TotalSulfurDioxide, filteredWineSampleList)
	    	+ "\t\t" + cellar.getMinimumValue(WineProperty.Density, filteredWineSampleList)
	    	+ "\t" + cellar.getMinimumValue(WineProperty.PH, filteredWineSampleList)
	    	+ "\t" + cellar.getMinimumValue(WineProperty.Sulphates, filteredWineSampleList)
	    	+ "\t" + cellar.getMinimumValue(WineProperty.Alcohol, filteredWineSampleList)
	    	+ "\t" + cellar.getMinimumValue(WineProperty.Quality, filteredWineSampleList)
	    	);
	    	
	    	//Maximum values for each properties
	    	statisticsTextArea.append("\nMaximum:\t"+ cellar.getMaximumValue(WineProperty.FixedAcidity, filteredWineSampleList)
	    	+ "\t" + cellar.getMaximumValue(WineProperty.VolatileAcidity, filteredWineSampleList)
	    	+ "\t" + cellar.getMaximumValue(WineProperty.CitricAcid, filteredWineSampleList)
	    	+ "\t" + cellar.getMaximumValue(WineProperty.ResidualSugar, filteredWineSampleList)
	    	+ "\t" + cellar.getMaximumValue(WineProperty.Chlorides, filteredWineSampleList)
	    	+ "\t" + cellar.getMaximumValue(WineProperty.FreeSulfurDioxide, filteredWineSampleList)
	    	+ "\t\t" + cellar.getMaximumValue(WineProperty.TotalSulfurDioxide, filteredWineSampleList)
	    	+ "\t\t" + cellar.getMaximumValue(WineProperty.Density, filteredWineSampleList)
	    	+ "\t" + cellar.getMaximumValue(WineProperty.PH, filteredWineSampleList)
	    	+ "\t" + cellar.getMaximumValue(WineProperty.Sulphates, filteredWineSampleList)
	    	+ "\t" + cellar.getMaximumValue(WineProperty.Alcohol, filteredWineSampleList)
	    	+ "\t" + cellar.getMaximumValue(WineProperty.Quality, filteredWineSampleList)
	    	);
	    	
	    	//Average values for each properties
	    	statisticsTextArea.append("\nMean:\t"+ df.format(cellar.getMeanAverageValue(WineProperty.FixedAcidity, filteredWineSampleList))
	    	+ "\t" + df.format(cellar.getMeanAverageValue(WineProperty.VolatileAcidity, filteredWineSampleList))
	    	+ "\t" + df.format(cellar.getMeanAverageValue(WineProperty.CitricAcid, filteredWineSampleList))
	    	+ "\t" + df.format(cellar.getMeanAverageValue(WineProperty.ResidualSugar, filteredWineSampleList))
	    	+ "\t" + df.format(cellar.getMeanAverageValue(WineProperty.Chlorides, filteredWineSampleList))
	    	+ "\t" + df.format(cellar.getMeanAverageValue(WineProperty.FreeSulfurDioxide, filteredWineSampleList))
	    	+ "\t\t" + df.format(cellar.getMeanAverageValue(WineProperty.TotalSulfurDioxide, filteredWineSampleList))
	    	+ "\t\t" + df.format(cellar.getMeanAverageValue(WineProperty.Density, filteredWineSampleList))
	    	+ "\t" + df.format(cellar.getMeanAverageValue(WineProperty.PH, filteredWineSampleList))
	    	+ "\t" + df.format(cellar.getMeanAverageValue(WineProperty.Sulphates, filteredWineSampleList))
	    	+ "\t" + df.format(cellar.getMeanAverageValue(WineProperty.Alcohol, filteredWineSampleList))
	    	+ "\t" + df.format(cellar.getMeanAverageValue(WineProperty.Quality, filteredWineSampleList))
	    	);
    	}
    	//No values shown for empty wine sample list
	    	else {
	    		statisticsTextArea.append("\nMinimum:\nMaximum:\nMean:");
    	}
    }

    /**
     * Updates the wine details panel when changes are made
     */
    @Override
    public void updateWineDetailsBox() {
	    //Set up each variable on the first line of wine samples text area
		filteredWineSamplesTextArea.setText("WineType\tID\tFixedAcidity\tVolatileAcidity\t"
				+ "CitricAcid\tResidualSugar\tChlorides\tFreeSulfurDioxide\tTotalSulfurDioxide\t"
				+ "Density\tpH\tSulphates\tAlcohol\tQuality");
		
		if(!filteredWineSampleList.isEmpty()) {
			//Prints out each wine sample according to wine type
		    for(WineSample ws:filteredWineSampleList) {
		    	filteredWineSamplesTextArea.append("\n" + ws.getWineType().toString() + "\t" + ws.getId()+ "\t" 
		        + ws.getProperty(WineProperty.FixedAcidity) + "\t" + ws.getProperty(WineProperty.VolatileAcidity) 
		        + "\t" + ws.getProperty(WineProperty.CitricAcid) + "\t" + ws.getProperty(WineProperty.ResidualSugar)
		    	+ "\t" + ws.getProperty(WineProperty.Chlorides) + "\t" + ws.getProperty(WineProperty.FreeSulfurDioxide) 
		    	+ "\t\t" + ws.getProperty(WineProperty.TotalSulfurDioxide) + "\t\t" + ws.getProperty(WineProperty.Density) 
		    	+ "\t" + ws.getProperty(WineProperty.PH) + "\t" + ws.getProperty(WineProperty.Sulphates) + "\t" 
		    	+ df.format(ws.getProperty(WineProperty.Alcohol)) + "\t" + ws.getProperty(WineProperty.Quality));
		    }
		}
    }

    /**
     *  Executes the complete query to the relevant wine list
     */
    @Override
    public void executeQuery() {	
    	WineType wt = null;
    	//Retrieve wine type info
	    String wineType = (String) comboWineTypes.getSelectedItem();
	    
    	//Convert string format to WineType 
	    if(wineType.equals("RED")) 
	    	wt = WineType.RED;
	    else if(wineType.equals("WHITE")) 
	    	wt = WineType.WHITE;
	    else 
	    	wt = WineType.ALL;
	    
	    //Exeecute query filtering
	    Query query = new Query(subQueryList, wt);
        filteredWineSampleList = query.executeQuery(cellar);
    }
    
    /**
     * Updates the histogram according to the selected wine property
     */
    public void updateHistogram() {
		String s = (String) comboHistogramProperties.getSelectedItem();
		//Convert format from string to WineProperty
		WineProperty wineProperty = WineProperty.fromName(s);
	    histogram.updateHistogramContents(wineProperty,filteredWineSampleList);
	    repaint();
    }
}
