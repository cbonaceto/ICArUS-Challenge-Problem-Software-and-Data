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
package org.mitre.icarus.cps.exam.phase_1.response;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.AttackLocationProbeResponse_MultiGroup;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.SurpriseReportProbeResponse;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.TroopSelectionResponse_MultiGroup;


/**
 * Response to a Task 1 probe trial.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="Task_1_ProbeTrialResponse", namespace="IcarusCPD_1",
		propOrder={"attackLocationResponse", "troopSelectionResponse", "groundTruthSurpriseResponse"})
public class Task_1_ProbeTrialResponse extends Task_1_2_3_ProbeTrialResponseBase {
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_1.response.Task_1_2_3_ProbeTrialResponseBase#getAttackLocationResponse()
	 */
	@XmlElement(name="GroupResponse")	
	@Override
	public AttackLocationProbeResponse_MultiGroup getAttackLocationResponse() {
		return super.getAttackLocationResponse();
	}	

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_1.response.Task_1_2_3_ProbeTrialResponseBase#setAttackLocationResponse(org.mitre.icarus.cps.exam.phase_1.response.AttackLocationProbeResponse_MultiGroup)
	 */
	@Override
	public void setAttackLocationResponse(AttackLocationProbeResponse_MultiGroup attackLocationResponse) {
		super.setAttackLocationResponse(attackLocationResponse);
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_1.response.Task_1_2_3_ProbeTrialResponseBase#getTroopSelectionResponse()
	 */
	@Override
	@XmlElement(name="TroopSelectionResponse")
	public TroopSelectionResponse_MultiGroup getTroopSelectionResponse() {
		return super.getTroopSelectionResponse();
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_1.response.Task_1_2_3_ProbeTrialResponseBase#setTroopSelectionResponse(org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.TroopSelectionResponse_MultiGroup)
	 */
	@Override
	public void setTroopSelectionResponse(TroopSelectionResponse_MultiGroup troopSelectionResponse) {
		super.setTroopSelectionResponse(troopSelectionResponse);
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_1.response.Task_1_2_3_ProbeTrialResponseBase#getGroundTruthSurpriseResponse()
	 */
	@XmlElement(name="GroundTruthSurpriseResponse")
	@Override
	public SurpriseReportProbeResponse getGroundTruthSurpriseResponse() {	
		return super.getGroundTruthSurpriseResponse();
	}	
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_1.response.Task_1_2_3_ProbeTrialResponseBase#setGroundTruthSurpriseResponse(org.mitre.icarus.cps.exam.phase_1.response.SurpriseReportProbeResponse)
	 */
	@Override
	public void setGroundTruthSurpriseResponse(SurpriseReportProbeResponse groundTruthSurpriseResponse) {
		super.setGroundTruthSurpriseResponse(groundTruthSurpriseResponse);
	}
}