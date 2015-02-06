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
import java.awt.Font;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JFrame;

import org.mitre.icarus.cps.app.util.ProbabilityUtils;
import org.mitre.icarus.cps.app.widgets.events.IcarusGUIEvent;
import org.mitre.icarus.cps.app.widgets.events.IcarusGUIEventListener;
import org.mitre.icarus.cps.app.widgets.probability_entry.IProbabilityEntryContainer;
import org.mitre.icarus.cps.app.widgets.probability_entry.ProbabilityEntryConstants;
import org.mitre.icarus.cps.app.widgets.probability_entry.ProbabilitySumChangeListener;
import org.mitre.icarus.cps.app.widgets.probability_entry.TimeData;
import org.mitre.icarus.cps.app.widgets.probability_entry.controllers.DefaultProbabilityController;
import org.mitre.icarus.cps.app.widgets.probability_entry.controllers.IProbabilityController;
import org.mitre.icarus.cps.app.widgets.probability_entry.distinct_bars.SettingsBoxWithDecorators.EditControlType;
import org.mitre.icarus.cps.app.widgets.probability_entry.spinners.JSettingSpinner;
import org.mitre.icarus.cps.app.widgets.probability_entry.text_entry.ProbabilityTextField;

/**
 * 
 * 
 * @author Craig Bonaceto
 *
 */
public class ProbabilityBoxContainer extends SettingsBoxWithDecoratorsContainer 
	implements IProbabilityEntryContainer, IcarusGUIEventListener {
	
	private static final long serialVersionUID = 1L;
	
	/** The controller */
	protected IProbabilityController controller;	
	
	/** The sum of all probability boxes */ 
	protected int sum = 0;
	
	/** Whether to show the sum */
	protected boolean sumVisible;
	
	/** Amount of error to allow (+/-) in the sum before the sum text changes to the
	 * error color. If null, sum color will never change to the error color. */
	protected Integer sumErrorThreshold;	
	
	/** The edit control type */
	protected EditControlType editControlType;
	
	/** Whether the edit controls (if present) are editable */
	protected boolean editControlEditable;
	
	/** Whether locking is enabled */
	protected boolean enableLocking = false;
	
	protected SettingsBoxWithDecorators focusedSelectionBox = null;	
	
	protected SettingsBoxWithDecorators previousFocusedBox = null;
	
	public ProbabilityBoxContainer(BoxOrientation orientation, Dimension boxSize, int numBoxes, 
			EditControlType editControlType, boolean editControlEditable, boolean boxEditable, boolean sumVisible) {
		this(orientation, boxSize, numBoxes, editControlType, editControlEditable, 
				boxEditable, sumVisible, null, ProbabilityEntryConstants.minPercentLabel, 
				ProbabilityEntryConstants.maxPercentLabel);
	}
	
	public ProbabilityBoxContainer(BoxOrientation orientation, Dimension boxSize, int numBoxes,
			EditControlType editControlType, boolean editControlEditable, boolean boxEditable,
			boolean sumVisible, List<String> titles) {
		this(orientation, boxSize, numBoxes, editControlType, editControlEditable, 
				boxEditable, sumVisible, titles, ProbabilityEntryConstants.minPercentLabel, 
				ProbabilityEntryConstants.maxPercentLabel);
	}
	
	public ProbabilityBoxContainer(BoxOrientation orientation, Dimension boxSize, int numBoxes,
			EditControlType editControlType, boolean editControlEditable, boolean boxEditable,
			boolean sumVisible, List<String> titles, String minLabel, String maxLabel) {
		super(orientation, boxSize, numBoxes, 0, 100, editControlType, 
				editControlEditable, boxEditable, true, titles);
		controller = new DefaultProbabilityController(this);
		this.editControlEditable = editControlEditable;
		//sumChangedEvent = new IcarusGUIEvent(this, IcarusGUIEvent.PROBABILITY_SUM_CHANGED);		
		this.sumVisible = sumVisible;
		
		//Configure the probability boxes		
		for(SettingsBoxWithDecorators box : settingsBoxes) {
			box.setProbabilityController(controller);
			JSettingSpinner spinner =  box.spinner;
			if(spinner !=  null) {
				spinner.setFormatAsPercent(true);
				spinner.setMinLabel(minLabel);
				spinner.setMaxLabel(maxLabel);
			} 
			ProbabilityTextField textBox = box.textBox;
			if(textBox != null) {
				textBox.setFormatAsPercent(true);
			}
		}
		controller.setProbabilityComponents(settingsBoxes);
	
		//Show the sum
		if(sumVisible) {
			setBottomTitle(" ", true);
		}		
		sumChanged();
	}
	
	public void setCurrentSettingFillColor(Color color) {
		if(settingsBoxes != null) {
			for(SettingsBoxWithDecorators box : settingsBoxes) {
				box.setCurrentSettingFillColor(color);
			}
		}
	}
	
	public void setCurrentSettingFillColorDisabled(Color color) {
		if(settingsBoxes != null) {
			for(SettingsBoxWithDecorators box : settingsBoxes) {
				box.setCurrentSettingFillColorDisabled(color);
			}
		}
	}
	
	public void setDisplaySettingAtMouseLocation(boolean displaySettingAtMouseLocation) {
		if(settingsBoxes != null) {
			for(SettingsBoxWithDecorators box : settingsBoxes) {
				box.setDisplaySettingAtMouseLocation(displaySettingAtMouseLocation);
			}
		}
	}
	
	public void setBoxesClickable(boolean clickable) {
		if(settingsBoxes != null) {
			for(SettingsBoxWithDecorators box : settingsBoxes) {
				box.setBoxClickable(clickable);
			}
		}
	}
	
	public void setBoxesDraggable(boolean draggable) {
		if(settingsBoxes != null) {
			for(SettingsBoxWithDecorators box : settingsBoxes) {
				box.setBoxDraggable(draggable);
			}
		}
	}
	
	public void setCurrentSettingLineDraggable(boolean draggable) {
		if(settingsBoxes != null) {
			for(SettingsBoxWithDecorators box : settingsBoxes) {
				box.setCurrentSettingLineDraggable(draggable);
			}
		}
	}
	
	public void setEditControlEditable(boolean editControlEditable) {
		if(settingsBoxes != null) {
			for(SettingsBoxWithDecorators box : settingsBoxes) {
				box.setEditControlEditable(editControlEditable);
			}
		}
	}

	@Override
	public void setTopTitleAndSumFont(Font font) {
		if(topTitleLabel != null) {
			topTitleLabel.setFont(font);
		}
		if(bottomTitleLabel != null) {
			bottomTitleLabel.setFont(font);
		}		
		revalidate();
	}

	@Override
	public void setProbabilityEntryFont(Font font) {
		if(settingsBoxes != null) {
			for(SettingsBoxWithDecorators box : settingsBoxes) {
				box.setFont(font);
			}
			revalidate();
		}
	}
	
	@Override
	public void setProbabilityEntryTitleFont(Font font) {
		if(settingsBoxes != null) {
			for(SettingsBoxWithDecorators box : settingsBoxes) {
				box.setTitleFont(font);
			}
			revalidate();
		}
	}	
	
	@Override
	public void resetInteractionTimes() {
		for(SettingsBoxWithDecorators probabilityBox : settingsBoxes) {
			probabilityBox.resetInteractionTime();
		}
	}
	
	@Override
	public ArrayList<Long> getInteractionTimes() {
		ArrayList<Long> interactionTimes = new ArrayList<Long>(boxes.size());		
		for(int i=0; i<settingsBoxes.size(); i++) {
			interactionTimes.add(0L);
		}
		getInteractionTimes(interactionTimes);
		return interactionTimes;
	}
	
	@Override
	public void getInteractionTimes(List<Long> interactionTimes) {
		int i = 0;
		for(SettingsBoxWithDecorators probabilityBox : settingsBoxes) {
			interactionTimes.set(i, probabilityBox.getInteractionTime());			
			i++;
		}
	}	
	
	@Override
	public ArrayList<TimeData> getDetailedTimeData() {
		ArrayList<TimeData> timeData = new ArrayList<TimeData>();
		for(SettingsBoxWithDecorators probabilityBox : settingsBoxes) {
			timeData.add(new TimeData(probabilityBox.getDragTime_ms(),
					probabilityBox.getClickTime_ms(),
					probabilityBox.getSpinnerTime_ms()));
		}
		return timeData;
	}	
	
	@Override
	public ArrayList<String> getProbabilityEntryTitles() {
		if(boxes != null && !boxes.isEmpty()) {
			ArrayList<String> titles = new ArrayList<String>(boxes.size());
			for(BoxWithDecorators box : boxes) {
				titles.add(box.getTitle());
			}
			return titles;
		}
		return null;
	}

	@Override
	public void setProbabilityEntryTitles(List<String> titles) {
		if(titles != null) {
			int i = 0;
			for(String title : titles) {
				if(i < boxes.size()) {
					BoxWithDecorators box = boxes.get(i);
					box.setTitle(title);
					if(!box.isTitleVisible()) {
						box.setTitleVisible(true);
					}
				}
				i++;				
			}
		}
	}	

	@Override
	public String getProbabilityEntryTitle(int index) {
		if(checkIndex(index)) {
			return boxes.get(index).getTitle();
		}
		return null;
	}

	@Override
	public void setProbabilityEntryTitle(int index, String title) {
		if(checkIndex(index)) {
			BoxWithDecorators box = boxes.get(index);
			box.setTitle(title);
			if(!box.isTitleVisible()) {
				box.setTitleVisible(true);
			}
		}
	}

	@Override
	public Icon getProbabilityEntryTitleIcon(int index) {
		if(checkIndex(index)) {
			return boxes.get(index).getTitleIcon();
		}
		return null;
	}

	@Override
	public void setProbabilityEntryTitleIcon(int index, Icon icon) {
		if(checkIndex(index)) {
			boxes.get(index).setTitleIcon(icon);
		}
	}

	@Override
	public Color getProbabilityEntryTitleColor(int index) {
		if(checkIndex(index)) {
			return boxes.get(index).getTitleColor();
		}
		return null;
	}

	@Override
	public void setProbabilityEntryTitleColor(int index, Color color) {
		if(checkIndex(index)) {
			boxes.get(index).setTitleColor(color);
		}
	}	
	
	@Override
	public Color getProbabilityEntryColor(int index) {
		if(checkIndex(index)) {
			return settingsBoxes.get(index).getCurrentSettingFillColor();
		}
		return null;
	}

	@Override
	public void setProbabilityEntryColor(int index, Color color) {
		if(checkIndex(index)) {
			SettingsBoxWithDecorators box = settingsBoxes.get(index);
			box.setCurrentSettingFillColor(color);
			box.setCurrentSettingFillColorDisabled(color);
			box.setCurrentSettingLineColorDisabled(color);
		}
	}
	
	@Override
	public void restoreDefaultProbabilityEntryColors() {
		if(settingsBoxes != null) {
			for(SettingsBoxWithDecorators box : settingsBoxes) {
				box.setCurrentSettingFillColor(ProbabilityEntryConstants.COLOR_CURRENT_SETTING_FILL);
				box.setCurrentSettingFillColorDisabled(ProbabilityEntryConstants.COLOR_CURRENT_SETTING_FILL_DISABLED);
				box.setCurrentSettingLineColorDisabled(ProbabilityEntryConstants.COLOR_CURRENT_SETTING_LINE_DISABLED);
			}
		}
	}

	protected boolean checkIndex(int index) {
		return index >= 0 && index < boxes.size();
	}

	/** Configure the settings boxes to "confirm" mode */
	public void showConfirmedProbabilities() {
		for(SettingsBoxWithDecorators box : settingsBoxes) {
			box.setEditable(false);
			box.setEnableLocking(false);
			/*box.setPercentVisible(true);;
			box.setSpinnerEditable(false);
			box.setBoxEditable(false);
			box.setShowCurrentSetting(true);
			box.setFillCurrentSetting(true);
			box.setShowPreviousSetting(false);*/
		}
	}	
	
	/** Configure the settings boxes to be editable */
	public void showEditableProbabilities() {
		for(SettingsBoxWithDecorators box : settingsBoxes) {
			box.setEditable(true);
			box.setEnableLocking(enableLocking);
			/*box.setPercentVisible(true);
			box.setSpinnerEditable(true);
			box.setBoxEditable(true);
			box.setShowCurrentSetting(true);
			box.setFillCurrentSetting(true);
			box.setShowPreviousSetting(true);*/
		}
	}	
	
	@Override
	public boolean isSumVisible() {
		return sumVisible;
	}

	@Override
	public void setSumVisible(boolean sumVisible) {
		if(this.sumVisible != sumVisible) {
			this.sumVisible = sumVisible;
			
			if(sumVisible) {
				//Show the sum
				setBottomTitle(" ", true);
				sumChanged();
			}		
			else {
				//Hide the sum
				setBottomTitle(null, false);
			}
		}		
	}	
	
	@Override
	public Integer getSumErrorThreshold() {
		return sumErrorThreshold;
	}

	@Override
	public void setSumErrorThreshold(Integer threshold) {
		this.sumErrorThreshold = threshold;
	}
	
	@Override
	public void sumChanged() {
		sum = 0;		
		for(SettingsBoxWithDecorators settingsBox : settingsBoxes) {
			sum += settingsBox.getDisplaySetting() != null ? settingsBox.getDisplaySetting() : 0;
		}
		
		if(sumVisible) {
			setBottomTitle(Integer.toString(sum) + "%");
			if(sumErrorThreshold == null) {
				bottomTitleLabel.setForeground(ProbabilityEntryConstants.COLOR_TEXT_NORMAL);
			} else {
				if(Math.abs(sum-100) <= sumErrorThreshold) {
					bottomTitleLabel.setForeground(ProbabilityEntryConstants.COLOR_TEXT_NORMAL);
				} else {
					bottomTitleLabel.setForeground(ProbabilityEntryConstants.COLOR_TEXT_ERROR);
				}
			}
		}
	}	

	@Override
	public boolean isAutoNormalize() {
		return controller.isAutoNormalize();
	}
	
	@Override
	public void setAutoNormalize(boolean autoNormalize){
		controller.setAutoNormalize(autoNormalize);
	}	

	/**
	 * @return the enableLocking
	 */
	@Override
	public boolean isEnableLocking() {
		return enableLocking;
	}
	
	@Override
	public void setEnableLocking(boolean enableLocking) {
		if(this.enableLocking != enableLocking) {
			this.enableLocking = enableLocking;		
			for(SettingsBoxWithDecorators settingBox : settingsBoxes) {
				settingBox.setEnableLocking(enableLocking);			
			}
			revalidate();
		}
	}	
	
	@Override
	public ArrayList<Boolean> getLockSettings() {
		ArrayList<Boolean> lockSettings = null;
		if(enableLocking) {
			lockSettings = new ArrayList<Boolean>(settingsBoxes.size());
			for(SettingsBoxWithDecorators settingBox : settingsBoxes) {
				lockSettings.add(settingBox.isLocked());
			}
		}
		return lockSettings;
	}

	@Override
	public void getLockSettings(List<Boolean> lockSettings) {
		if(enableLocking) {
			int i = 0;
			for(SettingsBoxWithDecorators settingBox : settingsBoxes) {
				lockSettings.set(i, settingBox.isLocked());
				i++;
			}
		}
	}
	
	@Override
	public void unlockAllProbabilities() {
		for(SettingsBoxWithDecorators settingBox : settingsBoxes) {
			settingBox.setLocked(false);	
		}	
	}
	
	@Override
	public void setCurrentSettings(List<Integer> currentSettings) {
		super.setCurrentSettings(currentSettings);
		sumChanged();
	}
	
	@Override
	public void icarusGUIActionPerformed(IcarusGUIEvent event) {		
		interactionComponent = event.interactionComponent;		
		SettingsBoxWithDecorators manipComponent = (SettingsBoxWithDecorators)event.getSource();		
		focusedSelectionBox = manipComponent;
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
	public boolean isAllDisplayValuesValid() {
		if(settingsBoxes != null && !settingsBoxes.isEmpty()) {
			for(SettingsBoxWithDecorators field : settingsBoxes) {
				if(!field.isDisplayedValueValid()) {
					return false;
				}
			}			
		}
		return true;
	}
	
	public static void griddedBoxesExample() {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		ProbabilityBoxContainer boxes = new ProbabilityBoxContainer(BoxOrientation.GRID, 
				new Dimension(46, 100), 4, EditControlType.Spinner, false, true, true,
				new ArrayList<String>(Arrays.asList("A", "B", "C", "D")));		
		//boxes.setAutoNormalize(true);
		//boxes.setAutoLockAfterAdjust(true);
		boxes.setBoxesDraggable(true);
		//boxes.setEnableLocking(true);
		boxes.setBoxesClickable(true);
		boxes.setCurrentSettings(ProbabilityUtils.createDefaultInitialProbabilities(4));
		boxes.setPreviousSettings(boxes.getCurrentSettings());
		//boxes.showEditableProbabilities();
		frame.getContentPane().add(boxes);
		frame.pack();
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
	}
	
	public static void standardBoxesExample() {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		ProbabilityBoxContainer boxes = new ProbabilityBoxContainer(BoxOrientation.HORIZONTAL_LINE, 
				new Dimension(46, 100), 4, EditControlType.Spinner, false, true, true,
				new ArrayList<String>(Arrays.asList("A", "B", "C", "D")));		
		//boxes.setAutoNormalize(true);
		//boxes.setAutoLockAfterAdjust(true);
		boxes.setBoxesDraggable(true);
		//boxes.setEnableLocking(true);
		boxes.setBoxesClickable(true);
		boxes.setCurrentSettings(ProbabilityUtils.createDefaultInitialProbabilities(4));
		boxes.setPreviousSettings(boxes.getCurrentSettings());
		//boxes.showEditableProbabilities();
		frame.getContentPane().add(boxes);
		frame.pack();
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
	}	

	public static void main(String[] args) {
		standardBoxesExample();
		//griddedBoxesExample();
	}	
}