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

import java.util.List;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.BlueAction;

/**
 * Contains data about the frequency with which a Blue action permutation was selected.
 * 
 * @author CBONACETO
 *
 */
public class BlueActionSelectionFrequency extends SelectionFrequency {

    /**
     * The blue actions at each location
     */
    protected List<BlueAction> blueActions;        
    
    public BlueActionSelectionFrequency() {}
    
    public BlueActionSelectionFrequency(List<BlueAction> blueActions)  {
        this(blueActions, null, null);
    }
    
    public BlueActionSelectionFrequency(List<BlueAction> blueActions,
           Double selectionCount, Double selectionPercent) {
        this(blueActions, selectionCount, selectionPercent, null);
    }
    
    public BlueActionSelectionFrequency(List<BlueAction> blueActions,
           Double selectionCount, Double selectionPercent, Double selectionPercent_std) {
        super(selectionCount, selectionPercent, selectionPercent_std);
        this.blueActions = blueActions;
    }

    public List<BlueAction> getBlueActions() {
        return blueActions;
    }

    public void setBlueActions(List<BlueAction> blueActions) {
        this.blueActions = blueActions;
    }    
}