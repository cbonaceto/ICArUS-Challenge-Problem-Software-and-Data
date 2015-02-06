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
package org.mitre.icarus.cps.assessment.model_simulator.phase_2;

import org.mitre.icarus.cps.assessment.model_simulator.IModel;
import org.mitre.icarus.cps.assessment.model_simulator.phase_2.model_components.IBlueActionSelectionComponent;
import org.mitre.icarus.cps.assessment.model_simulator.phase_2.model_components.IRedAttackProbabilityComponent;
import org.mitre.icarus.cps.assessment.model_simulator.phase_2.model_components.IRedTacticsProbabilityComponent;
import org.mitre.icarus.cps.assessment.model_simulator.phase_2.model_components.ISigintSelectionComponent;
import org.mitre.icarus.cps.exam.phase_2.IcarusExam_Phase2;

/**
 * Interface for Phase 2 model implementations
 * 
 * @author CBONACETO
 */
public interface IModel_Phase2 extends IModel<IcarusExam_Phase2> {    
    
    public String getDescription();
    
    public void setDescription(String description);
    
    public IRedTacticsProbabilityComponent getRedTacticsProbabilityComponent();
    
    public void setRedTacticsProbabilityComponent(
            IRedTacticsProbabilityComponent redTacticsProbabilityComponent);

    public IRedAttackProbabilityComponent getRedAttackProbabilityComponent();
    
    public void setRedAttackProbabilityComponent(
            IRedAttackProbabilityComponent redAttackProbabilityComponent);

    public ISigintSelectionComponent getSigintSelectionComponent();
    
    public void setSigintSelectionComponent(ISigintSelectionComponent sigintSelectionComponent);

    public IBlueActionSelectionComponent getBlueActionSelectionComponent();
    
    public void setBlueActionSelectionComponent(
            IBlueActionSelectionComponent blueActionSelectionComponent);
}