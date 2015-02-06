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

import org.mitre.icarus.cps.app.widgets.probability_entry.IProbabilityEntryComponent;
import org.mitre.icarus.cps.app.widgets.probability_entry.IProbabilityEntryContainer;
import org.mitre.icarus.cps.app.widgets.probability_entry.ProbabilitySumChangeListener;

import java.util.List;

/**
 * @author Eric Kappotis
 *
 */
public interface IProbabilityController {
	
	public IProbabilityEntryContainer getProbabilityContainer();
	
	public void setProbabilityContainer(IProbabilityEntryContainer probabilityContainer);
	
	/**
	 * @param containers
	 */
	public void setProbabilityComponents(List<? extends IProbabilityEntryComponent> containers);
	
	public List<? extends IProbabilityEntryComponent> getProbabilityComponents();
	
	/**
	 * @return
	 */
	public boolean isAutoNormalize();
	
	/**
	 * @param autoNormalize
	 */
	public void setAutoNormalize(boolean autoNormalize);
	
	/**
	 * @return
	 */
	public boolean isLockAfterAdjust();
	
	/**
	 * @param lockAfterAdjust
	 */
	public void setLockAfterAdjust(boolean lockAfterAdjust);
	
	/**
	 * @return
	 */
	public Integer getSum();
	
	/**
	 * @param componentId
	 * @param value
	 */
	public void updateSettingRequest(int componentId, Integer value);
	
	
	/**
	 * Get whether the given component is lockable. It's lockable if it can be locked
	 * and the unlocked values can still be adjusted such that the sum can be made 100.
	 * 
	 * @param componentId
	 * @return
	 */
	public boolean isLockable(int componentId);	
	
	public boolean isSumChangeListenerPresent(ProbabilitySumChangeListener listener);	
	public void addSumChangeListener(ProbabilitySumChangeListener listener);
	public void removeSumChangeListener(ProbabilitySumChangeListener listener);
}