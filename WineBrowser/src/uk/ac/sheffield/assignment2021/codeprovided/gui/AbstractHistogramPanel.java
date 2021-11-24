package uk.ac.sheffield.assignment2021.codeprovided.gui;

import javax.swing.*;

/**
 * Abstract class for displaying a Histogram.
 * Please pay attention to the note comment below the constructor.
 * This should display the axes and bars of the histogram, and a vertical line to show the average measurement.
 * Should be updated when the histogram is updated
 *
 * Should be implemented as uk.ac.sheffield.assignment2021.gui.HistogramPanel
 *
 * @version 1.0 02/03/2021
 *
 * @author Ben Clegg
 *
 * Copyright (c) University of Sheffield 2021
 */
public class AbstractHistogramPanel extends JPanel {

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final AbstractWineSampleBrowserPanel parentPanel;
    private final AbstractHistogram histogram;

    public AbstractHistogramPanel(AbstractWineSampleBrowserPanel parentPanel, AbstractHistogram histogram) {
        super();
        this.parentPanel = parentPanel;
        this.histogram = histogram;
    }

    public AbstractHistogram getHistogram()
    {
        return histogram;
    }
}
