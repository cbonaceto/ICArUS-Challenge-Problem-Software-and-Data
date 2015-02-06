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
package org.mitre.icarus.cps.exam.phase_05.testing.assessment;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.phase_05.testing.IcarusTestTrial_Phase05;

/**
 * An assessment question is a a question of the form P(scene item | evidence element) 
 * for each scene item to probe.  Note that probabilities don't necessarily have 
 * to sum to 1 unless probalitiesNormalized is true.
 * 
 * FOR HUMAN SUBJECT USE ONLY.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="Assessment", namespace="IcarusCPD_05")
public class AssessmentTrial extends IcarusTestTrial_Phase05 {

	//public static enum HypothesisType {Contains_Rooftop_Hardware};

	/** The question text */
	private String questionText;

	/** The hypothesis being asked about.  Standard hypothesis types
	 * are enumerated above */
	//private HypothesisType hypothesis;

	/** The ids of each scene item to probe about given the evidence.
	 * User indicates P(scene item | evidence) for each scene item.
	 * If null, only a single probability slider will be shown */
	private List<Integer> sceneItemsToProbe;
	
	/** Evidence elements (e.g., a scene item like a building, water, roof-top hardware, SIGINT, etc..) */
	private List<EvidenceElement> evidence;
	
	/** URL to the object palette to use where the evidence elements are located (optional) */
	private String objectPaletteUrl;
	
	/** Whether or not probabilities should be normalized (default is false) */
	private boolean probabilitiesNormalized = false;

	@XmlElement(name="QuestionText")
	public String getQuestionText() {
		return questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	/*@XmlElement(name="Hypothesis")
	public HypothesisType getHypothesis() {
		return hypothesis;
	}

	public void setHypothesis(HypothesisType hypothesis) {
		this.hypothesis = hypothesis;
	}*/

	@XmlElementWrapper(name="SceneItemsToProbe")
	@XmlElement(name="ItemId")
	public List<Integer> getSceneItemsToProbe() {
		return sceneItemsToProbe;
	}

	public void setSceneItemsToProbe(List<Integer> sceneItemsToProbe) {
		this.sceneItemsToProbe = sceneItemsToProbe;
	}

	@XmlElement(name="Evidence")
	public List<EvidenceElement> getEvidence() {
		return evidence;
	}

	public void setEvidence(List<EvidenceElement> evidence) {
		this.evidence = evidence;
	}

	@XmlElement(name="ObjectPaletteUrl")
	public String getObjectPaletteUrl() {
		return objectPaletteUrl;
	}

	public void setObjectPaletteUrl(String objectPaletteUrl) {
		this.objectPaletteUrl = objectPaletteUrl;
	}	
	
	@XmlElement(name="ProbabilitiesNormalized")
	public boolean isProbabilitiesNormalized() {
		return probabilitiesNormalized;
	}

	public void setProbabilitiesNormalized(boolean probabilitiesNormalized) {
		this.probabilitiesNormalized = probabilitiesNormalized;
	}

	@SuppressWarnings("deprecation")
	@Override
	public TestTrialType getTestTrialType() {
		return TestTrialType.Assessment;
	}
}
