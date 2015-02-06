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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.mitre.icarus.cps.app.widgets.probability_entry.controllers.DefaultProbabilityController;
import org.mitre.icarus.cps.app.widgets.probability_entry.controllers.IProbabilityController;
import org.mitre.icarus.cps.experiment_core.gui.JPanelConditionComponent;

/**
 * 
 * @author Eric Kappotis
 */
public abstract class AbstractProbabilityEntryContainer<T extends IProbabilityEntryComponent> 
	extends JPanelConditionComponent implements IProbabilityEntryContainer {	
	
	private static final long serialVersionUID = 4233946931905515053L;

	protected List<T> probabilityFields;	
	
	/** Whether the sum is visible */
	protected boolean sumVisible;
	
	/** Whether locking is enabled */
	protected boolean enableLocking;
	
	/** Whether auto normalization is enabled */
	protected boolean autoNormalize;
	
	/** The controller */
	protected IProbabilityController controller;	
	
	public AbstractProbabilityEntryContainer(String componentId) {
		super(componentId);
		controller = new DefaultProbabilityController(this);
	}	

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.gui.probability_entry.IProbabilityEntrySet#isSumVisible()
	 */
	@Override
	public boolean isSumVisible() {
		return this.sumVisible;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.gui.probability_entry.IProbabilityEntrySet#setSumVisible(boolean)
	 */
	@Override
	public void setSumVisible(boolean sumVisible) {
		this.sumVisible = sumVisible;
	}
	
	@Override
	public boolean isSumChangeListenerPresent(ProbabilitySumChangeListener listener) {
		return controller.isSumChangeListenerPresent(listener);
	}
	
	@Override
	public void addSumChangeListener(ProbabilitySumChangeListener listener) {
		controller.addSumChangeListener(listener);
	}

	@Override
	public void removeSumChangeListener(ProbabilitySumChangeListener listener) {
		controller.removeSumChangeListener(listener);
	}

	@Override
	public boolean isAutoNormalize() {
		return autoNormalize;
	}

	@Override
	public void setAutoNormalize(boolean autoNormalize) {
		this.autoNormalize = autoNormalize;
		controller.setAutoNormalize(autoNormalize);
	}
	
	@Override
	public void resetInteractionTimes() {
		if(probabilityFields != null && !probabilityFields.isEmpty()) {
			for(T currField : probabilityFields) {
				currField.resetInteractionTime();
			}
		}	
	}
	
	@Override
	public List<Long> getInteractionTimes() {
		ArrayList<Long> interactionTimes = null;
		if(probabilityFields != null && !probabilityFields.isEmpty()) {
			interactionTimes = new ArrayList<Long>(probabilityFields.size());
			for(T currField : probabilityFields) {
				interactionTimes.add(currField.getInteractionTime());
			}
		}
		return interactionTimes;
	}

	@Override
	public void getInteractionTimes(List<Long> interactionTimes) {		
		if(probabilityFields != null && !probabilityFields.isEmpty()) {
			int i = 0;
			for(T currField : probabilityFields) {
				interactionTimes.set(i, currField.getInteractionTime());
				i++;
			}
		}
	}

	@Override
	public List<Integer> getCurrentSettings() {
		ArrayList<Integer> currentSettings = null;
		if(probabilityFields != null && !probabilityFields.isEmpty()) {
			currentSettings = new ArrayList<Integer>(probabilityFields.size());
			for(T currField : probabilityFields) {
				currentSettings.add(currField.getDisplaySetting());
			}		
		}
		return currentSettings;
	}

	@Override
	public void getCurrentSettings(List<Integer> currentSettings) {
		if(probabilityFields != null && !probabilityFields.isEmpty()) {
			int i = 0;
			for(T currField : probabilityFields) {
				currentSettings.set(i, currField.getDisplaySetting());
				i++;
			}
		}
	}

	@Override
	public void setCurrentSettings(List<Integer> currentSettings) {
		if(probabilityFields != null && !probabilityFields.isEmpty()) {
			Iterator<Integer> settingsIter = currentSettings.iterator();
			for(T currField : probabilityFields) {
				currField.setCurrentSetting(settingsIter.next());
			}
			sumChanged();
		}
	}

	@Override
	public List<Integer> getPreviousSettings() {
		ArrayList<Integer> previousSettings = null;
		if(probabilityFields != null && probabilityFields.isEmpty()) {
			previousSettings = new ArrayList<Integer>(probabilityFields.size());
			for(int i=0; i<probabilityFields.size(); i++) {
				previousSettings.add(null);
			}
			getPreviousSettings(previousSettings);
			return previousSettings;
		}
		return previousSettings;
	}
	
	@Override
	public void getPreviousSettings(List<Integer> previousSettings) {
		if(probabilityFields != null && !probabilityFields.isEmpty()) {
			int i = 0;
			for(T currField : probabilityFields) {
				previousSettings.set(i, currField.getPreviousSetting());
				i++;
			}	
		}
	}
	
	@Override
	public void setPreviousSettings(List<Integer> previousSettings) {
		if(probabilityFields != null && !probabilityFields.isEmpty()) {
			int i = 0;
			for(T currField : probabilityFields) {
				if(i >= previousSettings.size()) {
					return;
				}
				currField.setPreviousSetting(previousSettings.get(i));
				i++;
			}		
		}
	}	
	
	@Override
	public void setEnableLocking(boolean enableLocking) {
		if(this.enableLocking != enableLocking) {
			this.enableLocking = enableLocking;
			if(probabilityFields != null && !probabilityFields.isEmpty()) {
				for(T currField : probabilityFields) {
					currField.setEnableLocking(enableLocking);
				}
				revalidate();
			}
		}
	}

	@Override
	public boolean isEnableLocking() {
		return enableLocking;
	}	

	@Override
	public List<Boolean> getLockSettings() {
		ArrayList<Boolean> lockSettings = null; 	
		if(probabilityFields != null && enableLocking) {
			lockSettings = new ArrayList<Boolean>(probabilityFields.size());
			for(T currField : probabilityFields) {
				lockSettings.add(currField.isLocked());
			}		
		}
		return lockSettings;
	}

	@Override
	public void getLockSettings(List<Boolean> lockSettings) {
		int i = 0;
		if(probabilityFields != null && enableLocking) {
			for(T currField : probabilityFields) {
				lockSettings.set(i, currField.isLocked());
				i++;
			}
		}
	}

	@Override
	public void unlockAllProbabilities() {
		if(probabilityFields != null && !probabilityFields.isEmpty()) {
			for(T currField : probabilityFields) {
				currField.setLocked(false);
			}
		}
	}
	
	@Override
	public void setProbabilityEntryFont(Font font) {
		if(probabilityFields != null && !probabilityFields.isEmpty()) {
			for(T currField: probabilityFields) {
				currField.setFont(font);
			}
			revalidate();
		}
	}	

	@Override
	public Color getProbabilityEntryColor(int index) {
		if(probabilityFields != null && !probabilityFields.isEmpty()) {
			return probabilityFields.get(index).getProbabilityEntryColor();
		}
		return null;
	}

	@Override
	public void setProbabilityEntryColor(int index, Color color) {
		if(probabilityFields != null && !probabilityFields.isEmpty()) {
			probabilityFields.get(index).setProbabilityEntryColor(color);
		}
	}	
	
	/**
	 * @return the controller
	 */
	public IProbabilityController getController() {
		return controller;
	}	
	
	public void normalize() {
		if(probabilityFields != null) {
			NormalizationUtils.normalize(probabilityFields);
		}
	}

	@Override
	public boolean isAllDisplayValuesValid() {
		if(probabilityFields != null && !probabilityFields.isEmpty()) {
			for(T field : probabilityFields) {
				if(!field.isDisplayedValueValid()) {
					return false;
				}
			}			
		}
		return true;
	}
}