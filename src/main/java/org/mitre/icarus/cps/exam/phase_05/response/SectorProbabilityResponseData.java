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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;


/**
 * Contains probability response data for each sector in a locate item trial.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="SectorProbabilityResponses", namespace="IcarusCPD_05")
public class SectorProbabilityResponseData extends ProbabilityResponseData {	
	/** The probabilities that the scene item that was probed
	 *  was present in each each sector that was probed */
	private List<SectorProbabilityData> sectorProbabilities;
	
	/** The score.
	 * FOR HUMAN SUBJECT USE ONLY. */
	private Double score;
	
	/**
	 * Get the probabilities that the scene item that was probed
	 * was present in each each sector that was probed. 
	 * 
	 * @return
	 */
	@XmlElementWrapper(name="SectorProbabilities")
	@XmlElement(name="SectorProbability")
	public List<SectorProbabilityData> getSectorProbabilities() {
		return sectorProbabilities;
	}

	/**
	 * Set the probabilities that the scene item that was probed
	 * was present in each each sector that was probed. 
	 * 
	 * @param sectorProbabilities
	 */
	public void setSectorProbabilities(List<SectorProbabilityData> sectorProbabilities) {
		this.sectorProbabilities = sectorProbabilities;
	}
	
	public ArrayList<Integer> toIntArray() {
		ArrayList<Integer> list = new ArrayList<Integer>(sectorProbabilities.size());
		
		for (SectorProbabilityData data : sectorProbabilities) {
			double value = (data.getProbability() == null) ?
					0 : data.getProbability().doubleValue();
			
			list.add((int) (value * 100));
		}
		
		return list;
	}
	
	public void fromIntList(List<Integer> normalized, List<Integer> raw) {
		
		int numProbs = 0;
		if(raw != null && !raw.isEmpty()) {
			numProbs = raw.size();
		}
		else {
			numProbs = normalized.size();
		}
		sectorProbabilities = new ArrayList<SectorProbabilityData>(numProbs);
		
		for (int i = 0; i < numProbs; i++) {			
			Integer id = Integer.valueOf(i + 1);
			
			Double normalizedProb = null;
			if(normalized != null && !normalized.isEmpty()) {
				 normalizedProb = normalized.get(i) / 100.0D;			
			}
			
			Double rawProb = null;
			if(raw != null && !raw.isEmpty()) {
				rawProb = raw.get(i) / 100.0D;
			}
			
			sectorProbabilities.add(new SectorProbabilityData(id, normalizedProb, rawProb));			
		}
	}

	/**
	 * Get the score, FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @return
	 */
	@XmlElement(name="Score")
	public Double getScore() {
		return score;
	}

	/**
	 * Set the score, FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @param score
	 */
	public void setScore(Double score) {
		this.score = score;
	}	
}
