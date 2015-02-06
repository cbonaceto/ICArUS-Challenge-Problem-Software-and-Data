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
package org.mitre.icarus.cps.exam.phase_05.response;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.base.response.IcarusTrialResponse;
import org.mitre.icarus.cps.exam.phase_05.testing.assessment.EvidenceElement;
import org.mitre.icarus.cps.exam.phase_05.testing.scene_presentation.SceneItemProbabilityData;

/**
 * Response to an assessment trial.
 * 
 * FOR HUMAN SUBJECTS ONLY.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="AssessmentResponse", namespace="IcarusCPD_05")
public class AssessmentTrialResponse extends IcarusTrialResponse {		
	
	/** Subject indicated P(scene item | evidence) for each scene item that was probed. */
	private List<SceneItemProbabilityData> sceneItemProbabilities;	
	
	/** Evidence elements (e.g., a scene item like a building, water, roof-top hardware, SIGINT, etc..) */
	private List<EvidenceElement> evidence;

	@XmlElementWrapper(name="SceneItemProbabilities")
	@XmlElement(name="SceneItemProbability")
	public List<SceneItemProbabilityData> getSceneItemProbabilities() {
		return sceneItemProbabilities;
	}

	public void setSceneItemProbabilities(
			List<SceneItemProbabilityData> sceneItemProbabilities) {
		this.sceneItemProbabilities = sceneItemProbabilities;
	}

	@XmlElement(name="Evidence")
	public List<EvidenceElement> getEvidence() {
		return evidence;
	}

	public void setEvidence(List<EvidenceElement> evidence) {
		this.evidence = evidence;
	}	
}
