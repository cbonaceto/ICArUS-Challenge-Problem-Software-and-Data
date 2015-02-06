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
package org.mitre.icarus.cps.app.widgets.probability_entry;

import java.util.ArrayList;
import java.util.List;
/**
 * @author Eric Kappotis
 *
 */
public class NormalizationUtils {
	// this will return the remaining percent that can be
	// destributed
	//public static Integer calculateRemainingPercent(List<Setting> settings) {
	//	int remainingPercent = 0;
	//	for(Setting currSetting : settings) {
	//		if(!currSetting.isLocked()) {
	//			remainingPercent += currSetting.getIntValue();
	//		}
	//	}
	//	return remainingPercent;
	//}
	
	public static void normalize(List<? extends IProbabilityEntryComponent> components) {	
		// variable to keep track of the remaining
		// space that is not the current control or
		// a locked item
		double remainingSpace = 100;
		double remainingItemsTotalHeight = 0;		
		List<IProbabilityEntryComponent> remainingComponents = new ArrayList<IProbabilityEntryComponent>();		
		// find the remaining space after subtracting out the space
		// reserved for the manipulated control as well as locked
		// controls
		for(IProbabilityEntryComponent currComponent : components) {			
			if(currComponent.isFocused() || currComponent.isLocked()) {
				remainingSpace = remainingSpace - currComponent.getDoubleValue();
				//System.out.println("remaining space at: " + remainingSpace);
			}
			else {
				remainingComponents.add(currComponent);
				//System.err.println(currSetting.getValue());
				if(currComponent.getDoubleValue() < .01) {
					currComponent.setDoubleValue(.01);
				}
				remainingItemsTotalHeight = remainingItemsTotalHeight + currComponent.getDoubleValue();
			}
		}
		
		//double totalSpaceLeft = remainingItemsTotalHeight;
		// now we calculate how much remaining space there is and calculate
		// the remaining space that is left
		for(IProbabilityEntryComponent currComponent : remainingComponents) {
			double updatedValue = currComponent.getDoubleValue() / remainingItemsTotalHeight * remainingSpace;			
			currComponent.setDoubleValue(updatedValue);			
			/*
			if(setting != remainingItems.get(remainingItems.size() - 1)) {
				totalSpaceLeft = totalSpaceLeft - updatedValue;				
				
			}
			else {
				System.out.println("Found the last item, totalSpaceRemaining: " + totalSpaceLeft);
				setting.setIntValue((int)Math.round(totalSpaceLeft));
			}
			*/
			//setting.setIntValue((int)Math.round(updatedValue));
			//setting.setIntValue((int)Math.round(updatedValue));
		}		
	}	
}
