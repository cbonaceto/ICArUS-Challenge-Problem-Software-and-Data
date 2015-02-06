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
package org.mitre.icarus.cps.app.widgets.probability_entry.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.mitre.icarus.cps.app.widgets.probability_entry.IProbabilityEntryComponent;
import org.mitre.icarus.cps.app.widgets.probability_entry.IProbabilityEntryContainer;
import org.mitre.icarus.cps.app.widgets.probability_entry.NormalizationUtils;
import org.mitre.icarus.cps.app.widgets.probability_entry.ProbabilitySumChangeEvent;
import org.mitre.icarus.cps.app.widgets.probability_entry.ProbabilitySumChangeListener;

/**
 * @author Eric Kappotis
 *
 */
public class DefaultProbabilityController implements IProbabilityController {
	
	private IProbabilityEntryContainer probabilityContainer;
	
	private List<? extends IProbabilityEntryComponent> probabilityComponents;
	
	private boolean autoNormalize = false;
	
	private boolean lockAfterAdjust = false;
	
	/** Listeners registered to be notified when the sum changes */
	private List<ProbabilitySumChangeListener> sumChangeListeners;
	
	public DefaultProbabilityController(IProbabilityEntryContainer probabilityContainer) {
		this.probabilityContainer = probabilityContainer;
		probabilityComponents = new ArrayList<IProbabilityEntryComponent>();
		sumChangeListeners = Collections.synchronizedList(
				new LinkedList<ProbabilitySumChangeListener>());
	}	
	
	@Override
	public IProbabilityEntryContainer getProbabilityContainer() {
		return probabilityContainer;
	}

	@Override
	public void setProbabilityContainer(IProbabilityEntryContainer probabilityContainer) {
		this.probabilityContainer = probabilityContainer;
	}

	@Override
	public void setProbabilityComponents(List<? extends IProbabilityEntryComponent> components) {		
		this.probabilityComponents = components;		
	}

	@Override
	public List<? extends IProbabilityEntryComponent> getProbabilityComponents() {
		return this.probabilityComponents;
	}

	@Override
	public boolean isAutoNormalize() {
		return this.autoNormalize;
	}

	@Override
	public void setAutoNormalize(boolean autoNormalize) {
		this.autoNormalize = autoNormalize;
	}

	@Override
	public boolean isLockAfterAdjust() {
		return this.lockAfterAdjust;
	}

	@Override
	public void setLockAfterAdjust(boolean lockAfterAdjust) {
		this.lockAfterAdjust = lockAfterAdjust;
	}

	@Override
	public Integer getSum() {
		int sum = 0;	
		if(probabilityComponents != null) {
			for(IProbabilityEntryComponent currContainer : probabilityComponents) {
				sum += currContainer.getIntValue();
			}
		}
		return sum;
	}
	
	private int calculateRemainingSpace(int componentId) {		
		int lockedPercent = 0;		
		for(IProbabilityEntryComponent currContainer : probabilityComponents) {			
			// add up the total reserved percent not including the current component being moved
			if(currContainer.isLocked() && currContainer.getId() != componentId) {
				lockedPercent += currContainer.getDisplaySetting();
			}
		}	
		return 100 - lockedPercent;
	}

	@Override
	public void updateSettingRequest(int componentId, Integer value) {	
		int unlockedCount = 0;		
		Integer updatedContainerIndex = null;
		IProbabilityEntryComponent updatedContainer = null;
		IProbabilityEntryComponent lastUnlockedContainer = null;
		// locate the entry container that was updated
		int i = 0;
		for(IProbabilityEntryComponent currContainer : probabilityComponents) {
			// get a reference to the current component being updated
			if(currContainer.getId() == componentId) {
				updatedContainer = currContainer;
				updatedContainerIndex = i;
				currContainer.setFocused(true);
			}// 
			else { 
				currContainer.setFocused(false);
				if(!currContainer.isLocked()) {
					lastUnlockedContainer = currContainer;
				}
			}
			
			if(!currContainer.isLocked()) {
				unlockedCount++;
			}
			i++;
		}
		
		if(autoNormalize) {			
			// if there is only one unlocked item, do nothing			
			if(unlockedCount == 1) {
				return;
			}
			
			int remainingPercent = calculateRemainingSpace(componentId);
			
			if(remainingPercent - value >= 0) {				
				//updatedContainer.setLocked(true);
				//updatedContainer.setCurrentSetting(value);
				updatedContainer.setDoubleValue(new Double(value));
			}
			NormalizationUtils.normalize(probabilityComponents);
			int totalPercent = 100;	
			for(IProbabilityEntryComponent currContainer : probabilityComponents) {
				if(currContainer != lastUnlockedContainer) {
					totalPercent = totalPercent - (int)Math.round(currContainer.getDoubleValue());
				} 
			}
			
			// update the values with whatever the setting is					
			for(IProbabilityEntryComponent currContainer : probabilityComponents) {				
				if(currContainer != lastUnlockedContainer) {					
					currContainer.setDisplaySetting((int)Math.round(currContainer.getDoubleValue()));
				} else {				
					currContainer.setDisplaySetting(totalPercent);
				}	
				currContainer.getComponent().repaint();
			}						
		}
		else {
			updatedContainer.setCurrentSetting(value);
		}
		
		//Notify container that the sum changed
		if(probabilityContainer != null) {
			probabilityContainer.sumChanged();
		}
		
		//Notify listeners that the sum changed
		fireSumChangedEvent(updatedContainerIndex, componentId);
	}

	@Override
	public boolean isLockable(int componentId) {
		int lockedPercent = 0;	
		boolean allLocked = true;
		for(IProbabilityEntryComponent currContainer : probabilityComponents) {
			//add up the total locked percent including the componentId component
			if(currContainer.isLocked() || currContainer.getId() == componentId) {
				lockedPercent += currContainer.getDisplaySetting();
			} else if(!currContainer.isLocked()) {
				allLocked = false;
			}
		}
		return !(lockedPercent > 100 || (allLocked && lockedPercent != 100));
	}
	
	@Override
	public boolean isSumChangeListenerPresent(ProbabilitySumChangeListener listener) {
		synchronized(sumChangeListeners) {
			return sumChangeListeners.contains(listener);
		}
	}
	
	@Override
	public void addSumChangeListener(ProbabilitySumChangeListener listener) {
		synchronized(sumChangeListeners) {
			sumChangeListeners.add(listener);
		}
	}
	
	@Override
	public void removeSumChangeListener(ProbabilitySumChangeListener listener) {
		synchronized(sumChangeListeners) {
			sumChangeListeners.remove(listener);
		}
	}
	
	/** Notify all registered listeners that the probability sum changed */
	private void fireSumChangedEvent(Integer adjustedControlIndex, Integer adjustedControlId) {
		synchronized(sumChangeListeners) {
			if(!sumChangeListeners.isEmpty()) {
				ProbabilitySumChangeEvent sumChangedEvent = new ProbabilitySumChangeEvent(
						probabilityContainer, null, adjustedControlIndex, adjustedControlId); 
				for(ProbabilitySumChangeListener listener : sumChangeListeners) {
					listener.probabilitySumChanged(sumChangedEvent);
				}
			}
		}
	}
}