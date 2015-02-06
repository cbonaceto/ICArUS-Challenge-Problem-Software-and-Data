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
package org.mitre.icarus.cps.app.experiment.ui_study.exam;

import java.util.ArrayList;

/**
 * @author CBONACETO
 *
 */
public abstract class SingleBlockTrial extends UIStudyTrial {
	
	/** The probabilities for each item */
	protected ArrayList<ItemProbability> itemProbabilities;
	
	/** The response to the trial */
	protected SingleBlockTrialResponse trialResponse;

	public ArrayList<ItemProbability> getItemProbabilities() {
		return itemProbabilities;
	}

	public void setItemProbabilities(ArrayList<ItemProbability> itemProbabilities) {
		this.itemProbabilities = itemProbabilities;
	}
	
	public ArrayList<String> getItemProbabilityIds() {
		if(itemProbabilities != null && !itemProbabilities.isEmpty()) {
			ArrayList<String> ids = new ArrayList<String>(itemProbabilities.size());
			for(ItemProbability item : itemProbabilities) {
				ids.add(item.getItemId());
			}
			return ids;
		}
		return null;
	}

	public SingleBlockTrialResponse getTrialResponse() {
		return trialResponse;
	}

	public void setTrialResponse(SingleBlockTrialResponse trialResponse) {
		this.trialResponse = trialResponse;
	}	
}