package uk.ac.sheffield.assignment2021.codeprovided;

import java.util.Iterator;

/**
 * Class designed to be used to create objects from wine samples.
 *
 * @version 1.1  09/02/2021
 *
 * @author Maria-Cruz Villa-Uriol (m.villa-uriol@sheffield.ac.uk)
 * @author Ben Clegg
 *
 * Copyright (c) University of Sheffield 2021
 */

public class WineSample {

    private final int id;
    private final WineType wineType;

    private final WinePropertyMap winePropertyMap;

    /**
     * Constructor
     * @param id an id of the wine; automatically generated by AbstractWineSampleCellar.readWineFile()
     * @param wineType the type of the wine (RED or WHITE)
     * @param winePropertyMap the measured properties of the wine (from WineSampleCellar.parseWineFileLine())
     */
    public WineSample(int id, WineType wineType, WinePropertyMap winePropertyMap) {
        this.id = id;
        this.wineType = wineType;
        this.winePropertyMap = winePropertyMap;
    }

    /**
     * Get the value of a given property of the wine sample
     * @param property the wine property to select
     * @return the value of the chosen wine property
     */
    public double getProperty(WineProperty property) {
        return winePropertyMap.get(property);
    }

    public int getId() {
        return this.id;
    }

    public WineType getWineType() {
        return this.wineType;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("WineSample{");

        sb.append("ID=");
        sb.append(getId());
        sb.append(", ");

        sb.append("Type=");
        sb.append(getWineType().getName());
        sb.append(", ");

        Iterator<WineProperty> propertyIterator = winePropertyMap.propertySet().iterator();
        while (propertyIterator.hasNext()) {
            WineProperty p = propertyIterator.next();
            sb.append(p.getName());
            sb.append("=");
            sb.append(getProperty(p));

            if(propertyIterator.hasNext()) {
                sb.append(", ");
            }
        }

        sb.append("}");
        return sb.toString();
    }
    
}
