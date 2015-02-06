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

import java.awt.Color;
import java.awt.Font;
import java.util.List;

import javax.swing.Icon;

import org.mitre.icarus.cps.experiment_core.gui.IConditionComponent;

/**
 * Interface for containers that contain multiple pobability entry components.
 * 
 * @author CBONACETO
 *
 */
public interface IProbabilityEntryContainer extends IConditionComponent {	
	public List<String> getProbabilityEntryTitles();	
	public void setProbabilityEntryTitles(List<String> titles);
	
	public String getProbabilityEntryTitle(int index);	
	public void setProbabilityEntryTitle(int index, String title);
	
	public Icon getProbabilityEntryTitleIcon(int index);	
	public void setProbabilityEntryTitleIcon(int index, Icon icon);
	
	public Color getProbabilityEntryTitleColor(int index);	
	public void setProbabilityEntryTitleColor(int index, Color color);
	
	public Color getProbabilityEntryColor(int index);
	public void setProbabilityEntryColor(int index, Color color);
	
	public void restoreDefaultProbabilityEntryColors();
	
	public boolean isTopTitleVisible();	
	public void setTopTitleVisible(boolean visible);
	
	/** Set the top title */
	public void setTopTitle(String title);
	
	/** Set the font used for the top title and sum */
	public void setTopTitleAndSumFont(Font font);
	
	/** Set the font used for the probability entry titles */
	public void setProbabilityEntryTitleFont(Font font);	
	
	/** Set the font used for the probability entry probability displays */
	public void setProbabilityEntryFont(Font font);
	
	/** Configure the probability entry components to "confirm" mode */
	public void showConfirmedProbabilities();
	
	/** Configure the probability entry components to be editable */
	public void showEditableProbabilities();
	
	//TODO: Revise to use normalization constraints
	public Integer getSumErrorThreshold();
	public void setSumErrorThreshold(Integer threshold);
	
	public boolean isSumVisible();	
	public void setSumVisible(boolean sumVisible);
	
	/** Notify the container that the sum changed */
	public void sumChanged();
	
	public boolean isSumChangeListenerPresent(ProbabilitySumChangeListener listener);
	public void addSumChangeListener(ProbabilitySumChangeListener listener);	
	public void removeSumChangeListener(ProbabilitySumChangeListener listener);
	
	public boolean isAutoNormalize();
	public void setAutoNormalize(boolean autoNormalize);
	
	public void setEnableLocking(boolean enableLocking);	
	public boolean isEnableLocking();	
	
	public List<Boolean> getLockSettings();
	public void getLockSettings(List<Boolean> lockSettings);
	
	public void unlockAllProbabilities();
	
	public void resetInteractionTimes();	
	public List<TimeData> getDetailedTimeData();
	public List<Long> getInteractionTimes();	
	public void getInteractionTimes(List<Long> interactionTimes);
	
	/**
	 * Get whether all settings values being displayed are valid.
	 * 
	 * @return
	 */
	public boolean isAllDisplayValuesValid();
	
	public List<Integer> getCurrentSettings();	
	public void getCurrentSettings(List<Integer> currentSettings);	
	public void setCurrentSettings(List<Integer> currentSettings);	
	
	public List<Integer> getPreviousSettings();	
	public void getPreviousSettings(List<Integer> previousSettings);	
	public void setPreviousSettings(List<Integer> previousSettings);
}