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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.base.response.TrainingTrialResponse;
import org.mitre.icarus.cps.exam.phase_05.loader.IcarusExamLoader_Phase05;

/**
 * Contains timing data for each training trial in a training phase.
 * FOR HUMAN SUBJECT USE ONLY.
 * 
 * @author CBONACETO
 *
 */
@XmlRootElement(name="TrainingPhaseResponse", namespace="IcarusCPD_05")
@XmlType(name="TrainingPhaseResponse", namespace="IcarusCPD_05")
public class IcarusTrainingPhaseResponse extends IcarusExamPhaseResponse {
	
	/** Timing for each training trial */
	protected List<TrainingTrialResponse> trialResponses;	
	
	@XmlElement(name="TrialResponse")
	public List<TrainingTrialResponse> getTrialResponses() {
		return trialResponses;
	}

	public void setTrialResponses(List<TrainingTrialResponse> trialResponses) {
		this.trialResponses = trialResponses;
	}
	
	/** Example use */
	public static void main(String[] args) {
		ArrayList<TrainingTrialResponse> responses = new ArrayList<TrainingTrialResponse>(2);
		
		TrainingTrialResponse trial = new TrainingTrialResponse();
		trial.setTrialNum(1);
		trial.setTrialTime_ms(1000L);
		responses.add(trial);
		
		trial = new TrainingTrialResponse();
		trial.setTrialNum(2);
		trial.setTrialTime_ms(1500L);
		responses.add(trial);
		
		IcarusTrainingPhaseResponse response = new IcarusTrainingPhaseResponse();
		response.setPhaseName("Training");
		response.setTrialResponses(responses);
		
		try {
			System.out.println(IcarusExamLoader_Phase05.marshalExamPhaseResponse(response));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
}
