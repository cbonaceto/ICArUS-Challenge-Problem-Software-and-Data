/* 
 * NOTICE
 * This software was produced for the office of the Director of National Intelligence (ODNI)
 * Intelligence Advanced Research Projects Activity (IARPA) ICArUS program, 
 * BAA number IARPA-BAA-10-04, under contract 2009-0917826-016, and is subject 
 * to the Rights in Data-General Clause 52.227-14, Alt. IV (DEC 2007).
 * 
 * This software and data is provided strictly to support demonstrations of ICArUS challenge problems
 * and to assist in the development of cognitive-neuroscience architectures. It is not intended to be used
 * in operational systems or environments.
 * 
 * Copyright (C) 2015 The MITRE Corporation. All Rights Reserved.
 * 
 */
package org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics;

/**
 * Contains data about the frequency with which SIGINT was selected at a location. 
 * 
 * @author CBONACETO
 */
public class SigintSelectionFrequency extends SelectionFrequency {

    /** The location */
    protected String locationId;
    
    public SigintSelectionFrequency() {}
    
    public SigintSelectionFrequency(String locationId, Double selectionCount,
            Double selectionPercent) {
        this(locationId, selectionCount, selectionPercent, null);
    }
    
    public SigintSelectionFrequency(String locationId, Double selectionCount,
            Double selectionPercent, Double selectionPercent_std) {
        super(selectionCount, selectionPercent, selectionPercent_std);
        this.locationId = locationId;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }    
}
