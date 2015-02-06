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
package org.mitre.icarus.cps.app.widgets.probability_entry.distinct_bars;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import org.mitre.icarus.cps.app.widgets.events.IcarusGUIEvent;
import org.mitre.icarus.cps.app.widgets.events.IcarusGUIEventListener;
import org.mitre.icarus.cps.app.widgets.events.InteractionComponentType;
import org.mitre.icarus.cps.app.widgets.probability_entry.ProbabilityContainerLayout;
import org.mitre.icarus.cps.app.widgets.probability_entry.Setting;
import org.mitre.icarus.cps.app.widgets.probability_entry.distinct_bars.SettingsBoxWithDecorators.EditControlType;

/**
 * Contains 1 or more settings boxes.
 * 
 * @author CBONACETO
 *
 */
public class SettingsBoxWithDecoratorsContainer extends BoxContainer implements IcarusGUIEventListener {
	
	private static final long serialVersionUID = 1L;	
	
	/** The settings boxes */
	protected ArrayList<SettingsBoxWithDecorators> settingsBoxes;
	
	/** The type of interaction component that was last used (slider, spinner, or text box) */
	protected InteractionComponentType interactionComponent;	
		
	public SettingsBoxWithDecoratorsContainer(BoxOrientation orientation, Dimension boxSize, int numBoxes, 
			int minSetting,	int maxSetting, EditControlType editControlType, boolean editControlEditable,
			boolean boxEditable, boolean formatAsPercent) {
		this(orientation, boxSize, numBoxes, minSetting, maxSetting, editControlType,
				editControlEditable, boxEditable, formatAsPercent, null);
	}
	
	public SettingsBoxWithDecoratorsContainer(BoxOrientation orientation, Dimension boxSize, int numBoxes,
			int minSetting, int maxSetting, EditControlType editControlType, boolean editControlEditable, 
			boolean boxEditable, boolean formatAsPercent, List<String> titles) {
		super(orientation, "settingsEntry");		
		
		//Create the settings boxes		
		// initialize the setting boxes by setting them to be of all
		// equal percentage
		double initValue = 100.0 / numBoxes;
		
		settingsBoxes = new ArrayList<SettingsBoxWithDecorators>(4);
		int titleOrientation = ProbabilityContainerLayout.SOUTH;
		int editControlOrientation = ProbabilityContainerLayout.NORTH;
		
		for(int i=0; i<numBoxes; i++) {			
			Setting setting = new Setting();
			setting.setMaxValue(maxSetting);
			setting.setMinValue(minSetting);
			setting.setValue(initValue);
			setting.setDisplaySetting((int)Math.round(initValue));
			//setting.setDisplaySetting(setting.getIntValue());
			
			if(orientation == BoxOrientation.CROSS) {
				titleOrientation = i;
				editControlOrientation = i;
			}	
			
			SettingsBoxWithDecorators box = null;
			if(editControlType != null && editControlType != EditControlType.None) {	
				box = new SettingsBoxWithDecorators(boxSize, editControlType,  
						editControlEditable, editControlOrientation, boxEditable, 
						setting, formatAsPercent, i);
			} else {
				if(titles != null && i < titles.size()) {
					box = new SettingsBoxWithDecorators(boxSize, titles.get(i), titleOrientation,
							setting, i);
				} else {
					box = new SettingsBoxWithDecorators(boxSize, setting, i);
				}
			}
			
			if(boxEditable) {
				//Add listener to update the interaction component
				box.addIcarusGUIEventListener(this);					
			}
			
			box.setTitleOrientation(titleOrientation);			
			if(titles != null && i < titles.size() && box.getTitle() == null) {								
				box.setTitle(titles.get(i));
				box.setTitleVisible(true);
			}
			settingsBoxes.add(box);
		}		
		super.setBoxes(settingsBoxes);
	}
	
	public InteractionComponentType getInteractionComponent() {
		return interactionComponent;
	}

	@Override
	public void setBackground(Color bg) {
		super.setBackground(bg);
		if(settingsBoxes != null) {
			for(SettingsBoxWithDecorators box : settingsBoxes) {
				box.setBackground(bg);
			}
		}
	}	

	public ArrayList<SettingsBoxWithDecorators> getSettingsBoxes() {
		return settingsBoxes;
	}	
	
	public List<Integer> getPreviousSettings() {
		ArrayList<Integer> previousSettings = new ArrayList<Integer>(boxes.size());
		
		for(int i=0; i<boxes.size(); i++) {
			previousSettings.add(null);
		}
		
		getPreviousSettings(previousSettings);
		return previousSettings;
	}
	
	public void getPreviousSettings(List<Integer> previousSettings) {
		int i = 0;
		for(SettingsBoxWithDecorators probabilityBox : settingsBoxes) {
			previousSettings.set(i, probabilityBox.getPreviousSetting());
			i++;
		}	
	}
	
	public void setPreviousSettings(List<Integer> previousSettings) {		
		int i = 0;
		for(SettingsBoxWithDecorators probabilityBox : settingsBoxes) {
			if(i >= previousSettings.size()) {
				return;
			}
			probabilityBox.setPreviousSetting(previousSettings.get(i));
			i++;
		}
	}	
	
	public List<Integer> getCurrentSettings() {
		ArrayList<Integer> currentSettings = new ArrayList<Integer>(boxes.size());		
		for(int i=0; i<boxes.size(); i++) {
			currentSettings.add(null);
		}		
		getCurrentSettings(currentSettings);
		return currentSettings;
	}
	
	public void getCurrentSettings(List<Integer> currentSettings) {
		int i = 0;
		for(SettingsBoxWithDecorators probabilityBox : settingsBoxes) {
			currentSettings.set(i, probabilityBox.getDisplaySetting());			
			i++;
		}	
	}
	
	public void setCurrentSettings(List<Integer> currentSettings) {
		int i = 0;
		for(SettingsBoxWithDecorators settingsBox : settingsBoxes) {
			if(i >= currentSettings.size()) {
				return;
			}
			settingsBox.setCurrentSetting(currentSettings.get(i));
			/*if(settingsBox.spinner != null) {
				settingsBox.spinner.repaint();
			}*/
			i++;
		}
	}
	
	@Override
	public void icarusGUIActionPerformed(IcarusGUIEvent event) {
		interactionComponent = event.interactionComponent;
	}

	@Override
	public JComponent getComponent() {
		return this;
	}

	@Override
	public String getComponentId() {
		return this.getName();
	}	
	
	public void setComponentId(String componentId) {
		this.setName(componentId);
	}	
}