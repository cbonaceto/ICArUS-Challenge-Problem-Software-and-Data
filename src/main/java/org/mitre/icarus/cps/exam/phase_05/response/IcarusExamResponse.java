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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Contains subject or model response data for one or more phases
 * in an exam.
 * 
 * @author CBONACETO
 *
 */
@XmlRootElement(name="IcarusEvaluationResponse", namespace="IcarusCPD_05")
@XmlType(name="IcarusEvaluationResponse")
public class IcarusExamResponse {
	
	/** The name of the exam the data is for */
	private String examName;
	
	/** The response data for each phase in the exam */
	private List<IcarusExamPhaseResponse> phaseResponses;

	/**
	 * Get the name of the exam the responses are for.
	 * 
	 * @return
	 */
	@XmlAttribute(name="name")
	public String getExamName() {
		return examName;
	}

	/**
	 * Set the name of the exam the responses are for.
	 * 
	 * @param examName
	 */
	public void setExamName(String examName) {
		this.examName = examName;
	}
	
	/**
	 * Get the responses to each phase in the exam.
	 * 
	 * @return
	 */
	@XmlElement(name="ExamPhaseResponse")
	public List<IcarusExamPhaseResponse> getPhaseResponses() {
		return phaseResponses;
	}

	/**
	 * Set the responses to each phase in the exam.
	 * 
	 * @param phaseResponses
	 */
	public void setPhaseResponses(List<IcarusExamPhaseResponse> phaseResponses) {
		this.phaseResponses = phaseResponses;
	}

	/*protected static IcarusExamResponse createSampleExamResponseData() {
		IcarusExamResponse response = new IcarusExamResponse();
		response.examName = "scenario1";
		
		IcarusTestPhaseResponse_Phase05 testResponse = new IcarusTestPhaseResponse_Phase05();
		testResponse.phaseName = "test";
		testResponse.trialResponses = new ArrayList<IcarusTrialResponse_Phase05>();
		
		IdentifyItemTrialResponse response1 = new IdentifyItemTrialResponse();
		response1.setTrialNum(1);
		response1.setSectorID(1);		
		SceneItemProbabilityResponseData sceneItemData1 = new SceneItemProbabilityResponseData();	
		sceneItemData1.setLayersShown(Arrays.asList(
				new LayerData(1, PresentationType.Base),
				new LayerData(2, PresentationType.Base),
				new LayerData(3, PresentationType.Base)));				
		sceneItemData1.setSceneItemProbabilities(Arrays.asList(
				new SceneItemProbabilityData(1, .25),
				new SceneItemProbabilityData(2, .25),
				new SceneItemProbabilityData(3, .25),
				new SceneItemProbabilityData(4, .25)
		));			
		response1.setSceneItemProbabilityData(Arrays.asList(sceneItemData1));
		testResponse.trialResponses.add(response1);
		
		LocateItemTrialResponse response2 = new LocateItemTrialResponse();
		response2.setTrialNum(2);	
		response2.setSceneItemID(1);
		SectorProbabilityResponseData sectorData1 = new SectorProbabilityResponseData();
		sectorData1.setLayersShown(Arrays.asList(new LayerData(1, PresentationType.Base)));
		sectorData1.setSectorProbabilities(Arrays.asList(new SectorProbabilityData(1, .25),
				new SectorProbabilityData(2, .25),
				new SectorProbabilityData(3, .25),
				new SectorProbabilityData(4, .25)
		));		
		SectorProbabilityResponseData sectorData2 = new SectorProbabilityResponseData();
		sectorData2.setLayersShown(Arrays.asList(
				new LayerData(1, PresentationType.Base),
				new LayerData(2, PresentationType.Sequential)));
		sectorData2.setSectorProbabilities(sectorData1.getSectorProbabilities());		
		SectorProbabilityResponseData sectorData3 = new SectorProbabilityResponseData();
		sectorData3.setLayersShown(Arrays.asList(
				new LayerData(1, PresentationType.Base),
				new LayerData(2, PresentationType.Sequential),
				new LayerData(3, PresentationType.Sequential)));
		sectorData3.setSectorProbabilities(sectorData1.getSectorProbabilities());
		response2.setSectorProbabilityData(Arrays.asList(sectorData1, sectorData2, sectorData3));
		testResponse.trialResponses.add(response2);		
		
		LocateItemTrialResponse response3 = new LocateItemTrialResponse();
		response3.setTrialNum(3);	
		response3.setSceneItemID(1);
		sectorData1 = new SectorProbabilityResponseData();
		sectorData1.setLayersShown(Arrays.asList(new LayerData(1, PresentationType.Base)));
		sectorData1.setSectorProbabilities(Arrays.asList(new SectorProbabilityData(1, .25),
				new SectorProbabilityData(2, .25),
				new SectorProbabilityData(3, .25),
				new SectorProbabilityData(4, .25)
		));		
		sectorData2 = new SectorProbabilityResponseData();
		sectorData2.setLayersShown(Arrays.asList(
				new LayerData(1, PresentationType.Base),
				new LayerData(2, PresentationType.UserChoice)));
		sectorData2.setSectorProbabilities(sectorData1.getSectorProbabilities());
		response3.setSectorProbabilityData(Arrays.asList(sectorData1, sectorData2));
		testResponse.trialResponses.add(response3);
		
		response.phaseResponses = new ArrayList<IcarusExamPhaseResponse>(1);
		response.phaseResponses.add(testResponse);
		
		return response;				
	}*/
}
