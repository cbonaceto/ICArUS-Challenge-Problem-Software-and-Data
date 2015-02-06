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
package org.mitre.icarus.cps.app.widgets.ui_study;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JPanel;

import org.mitre.icarus.cps.app.experiment.ui_study.UIStudyConstants;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.UIStudyPhase.ModalityType;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.UIStudyPhase.NormalizationMode;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.UIStudyPhase.WidgetType;
import org.mitre.icarus.cps.app.widgets.probability_entry.IProbabilityEntryContainer;
import org.mitre.icarus.cps.app.widgets.probability_entry.ProbabilityContainerLayout;
import org.mitre.icarus.cps.app.widgets.probability_entry.ProbabilityEntryComponentFactory;
import org.mitre.icarus.cps.app.widgets.probability_entry.ProbabilityEntryConstants;
import org.mitre.icarus.cps.app.widgets.probability_entry.TimeData;
import org.mitre.icarus.cps.app.widgets.probability_entry.ProbabilityContainerLayout.ProbabilityContainerLayoutType;
import org.mitre.icarus.cps.app.widgets.probability_entry.distinct_bars.ProbabilityBoxContainer;
import org.mitre.icarus.cps.app.widgets.probability_entry.distinct_bars.SettingsBoxWithDecorators.EditControlType;

/**
 * @author CBONACETO
 *
 */
public class ProbabilityEntryPanel extends JPanel {	
	private static final long serialVersionUID = 1L;
	
	/** The probability entry container */
	protected IProbabilityEntryContainer probabilityEntryContainer;
	
	/** The current probability entry type */
	protected WidgetType widgetType;
	
	/** The top title */
	protected String topTitle;
	
	/** Whether the top title is visible */
	protected boolean topTitleVisible;
	
	/** The probability entry titles */
	protected List<String> titles;
	
	/** The probability entry title colors */
	protected List<Color> colors;
	
	/** Whether the sum is visible */
	protected boolean sumVisible = false;
	
	/** Amount of error to allow (+/-) in the sum before the sum text changes to the
	 * error color. If null, sum color will never change to the error color. */
	protected Integer sumErrorThreshold = null;
	
	protected GridBagConstraints gbc;
	
	public ProbabilityEntryPanel(List<String> titles, String topTitle, boolean topTitleVisible) {
		//super(new BorderLayout());
		super(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.weightx = 1; gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(5, 5, 5, 5);
		this.titles = titles;
		this.topTitle = topTitle;
		this.topTitleVisible = topTitleVisible;
	}
	
	public void setProbabilityEntryType(WidgetType widgetType, ModalityType modalityType, 
			NormalizationMode normalizationType, Double looseNormalizationErrorThreshold) {
		if(probabilityEntryContainer == null || this.widgetType != widgetType) {
			//Create a new probability entry container
			removeAll();
			switch(widgetType) {
			case DistinctBars: case SliderSpinner: case SliderSpinnerOld:
				ProbabilityBoxContainer boxes = ProbabilityEntryComponentFactory.createBoxesProbabilityEntryComponent(titles, 
						(widgetType == WidgetType.SliderSpinnerOld) ? ProbabilityEntryConstants.BOX_SIZE : UIStudyConstants.DISTINCT_BARS_BOX_SIZE,
						(widgetType == WidgetType.SliderSpinnerOld) ? ProbabilityEntryConstants.COLOR_CURRENT_SETTING_FILL : UIStudyConstants.COLOR_CURRENT_SETTING_FILL, 
						(widgetType == WidgetType.SliderSpinnerOld) ? ProbabilityEntryConstants.COLOR_CURRENT_SETTING_FILL_DISABLED : UIStudyConstants.COLOR_CURRENT_SETTING_FILL_DISABLED,
						normalizationType == NormalizationMode.Dynamic || normalizationType == NormalizationMode.DynamicLocking,
						true, EditControlType.Spinner, 
						widgetType == WidgetType.SliderSpinner || widgetType == WidgetType.SliderSpinnerOld,
						ProbabilityEntryConstants.minPercentLabel, ProbabilityEntryConstants.maxPercentLabel);
				boolean boxesDraggable = modalityType == ModalityType.Mouse || 
						modalityType == ModalityType.MouseDrag || modalityType == ModalityType.TouchDrag; 				
				boxes.setBoxesDraggable(boxesDraggable);
				boxes.setBoxesClickable((modalityType == ModalityType.Mouse && widgetType == WidgetType.DistinctBars) || 
						modalityType == ModalityType.MouseClick || modalityType == ModalityType.TouchClick);
				if(widgetType == WidgetType.SliderSpinnerOld) {
					boxes.setCurrentSettingLineDraggable(false);
				}
				boxes.setDisplaySettingAtMouseLocation(!boxesDraggable);
				probabilityEntryContainer = boxes;
				break;
			case StackedBars:
				probabilityEntryContainer = ProbabilityEntryComponentFactory.createStackedBarsProbabilityEntryComponent(titles, 
						UIStudyConstants.STACKED_BARS_SIZE,
						ProbabilityEntryConstants.minPercentLabel, ProbabilityEntryConstants.maxPercentLabel);
				break;
			case Spinner:
				probabilityEntryContainer = ProbabilityEntryComponentFactory.createSpinnersProbabilityEntryComponent(
						new ProbabilityContainerLayout(ProbabilityContainerLayoutType.HORIZONTAL, titles.size()), titles, 
						normalizationType == NormalizationMode.Dynamic || normalizationType == NormalizationMode.DynamicLocking, 
						ProbabilityEntryConstants.minPercentLabel, ProbabilityEntryConstants.maxPercentLabel);
				break;
			case Keypad:
				probabilityEntryContainer = ProbabilityEntryComponentFactory.createTextProbabilityEntryComponent(
						new ProbabilityContainerLayout(ProbabilityContainerLayoutType.HORIZONTAL, titles.size()), titles, 
						normalizationType == NormalizationMode.Dynamic || normalizationType == NormalizationMode.DynamicLocking, false,
						ProbabilityEntryConstants.minPercentLabel, ProbabilityEntryConstants.maxPercentLabel);
				break;
			default:
				probabilityEntryContainer = ProbabilityEntryComponentFactory.createDefaultProbabilityEntryComponent(titles, 
						normalizationType == NormalizationMode.Dynamic || normalizationType == NormalizationMode.DynamicLocking);
			}
			if(colors != null) {
				for(int i = 0; i<colors.size(); i++) {
					probabilityEntryContainer.setProbabilityEntryTitleColor(i, colors.get(i));
				}
			}
			add(probabilityEntryContainer.getComponent(), gbc);
			this.widgetType = widgetType;
		} else {
			//Just set the modality type and whether auto normalization is enabled
			if(widgetType == WidgetType.DistinctBars) {
				ProbabilityBoxContainer boxes = (ProbabilityBoxContainer)probabilityEntryContainer;
				boolean boxesDraggable = modalityType == ModalityType.Mouse || 
						modalityType == ModalityType.MouseDrag || modalityType == ModalityType.TouchDrag; 
				boxes.setBoxesDraggable(boxesDraggable);
				boxes.setBoxesClickable(//modalityType == ModalityType.Mouse || 
						modalityType == ModalityType.MouseClick || modalityType == ModalityType.TouchClick);				
				boxes.setDisplaySettingAtMouseLocation(!boxesDraggable);
				boxes.setEditControlEditable(widgetType == WidgetType.SliderSpinner);				
			}
			probabilityEntryContainer.setAutoNormalize(
					normalizationType == NormalizationMode.Dynamic || normalizationType == NormalizationMode.DynamicLocking);
		}
		//Set whether locking is enabled
		probabilityEntryContainer.setEnableLocking(normalizationType == NormalizationMode.DynamicLocking ||
				normalizationType == NormalizationMode.Delayed);
		
		//Set the sum properties
		probabilityEntryContainer.setSumVisible(sumVisible);
		probabilityEntryContainer.setSumErrorThreshold(sumErrorThreshold);
		
		//Set the top title
		probabilityEntryContainer.setTopTitleVisible(topTitleVisible);
		probabilityEntryContainer.setTopTitle(topTitle);
		
		//Set the fonts 
		probabilityEntryContainer.setTopTitleAndSumFont(UIStudyConstants.FONT_TITLE);
		probabilityEntryContainer.setProbabilityEntryFont(UIStudyConstants.FONT_PROBABILITY);
		probabilityEntryContainer.setProbabilityEntryTitleFont(UIStudyConstants.FONT_PROBABILITY_TITLE);
		
		//Set the sum error threshold
		if(normalizationType == NormalizationMode.Exact) {
			probabilityEntryContainer.setSumErrorThreshold(ProbabilityEntryConstants.EXACT_NORMALIZATION_ERROR_THRESHOLD);
		} else if(normalizationType == NormalizationMode.Loose) {
			if(looseNormalizationErrorThreshold != null) {
				probabilityEntryContainer.setSumErrorThreshold(looseNormalizationErrorThreshold.intValue());
			} else {
				probabilityEntryContainer.setSumErrorThreshold(ProbabilityEntryConstants.LOOSE_NORMALIZATION_ERROR_THRESHOLD);
			}
		} else {
			probabilityEntryContainer.setSumErrorThreshold(null);
		}
		//System.out.println("New preferred size: " +  this.getPreferredSize());
		revalidate();
	}
	
	public boolean isTopTitleVisible() {
		if(probabilityEntryContainer != null) {
			return probabilityEntryContainer.isTopTitleVisible();
		}
		return false;
	}
	
	public void setTopTitleVisible(boolean visible) {
		this.topTitleVisible = visible;
		if(probabilityEntryContainer != null) {
			probabilityEntryContainer.setTopTitleVisible(visible);
		}
	}
	
	public String getTopTitle() {
		return topTitle;
	}
	
	public void setTopTitle(String title) {
		this.topTitle = title;
		if(probabilityEntryContainer != null) {
			probabilityEntryContainer.setTopTitle(title);
		}
	}	
	
	public void setProbabilityEntryTitles(List<String> titles) {
		this.titles = titles;
		if(probabilityEntryContainer != null) {
			probabilityEntryContainer.setProbabilityEntryTitles(titles);
		}
	}
	
	public void setProbabilityEntryTitleColors(List<Color> colors) {
		this.colors = colors;
		if(probabilityEntryContainer != null) {
			for(int i = 0; i<colors.size(); i++) {
				probabilityEntryContainer.setProbabilityEntryTitleColor(i, colors.get(i));
			}
		}
	}
	
	public void setSumVisible(boolean sumVisible) {
		this.sumVisible = sumVisible;
		if(probabilityEntryContainer != null) {
			probabilityEntryContainer.setSumVisible(sumVisible);
		}
	}
	
	public Integer getSumErrorThreshold() {
		return sumErrorThreshold;
	}

	public void setSumErrorThreshold(Integer threshold) {
		this.sumErrorThreshold = threshold;
		if(probabilityEntryContainer != null) {
			probabilityEntryContainer.setSumErrorThreshold(threshold);
		}
	}
	
	public void showConfirmedProbabilities() {
		if(probabilityEntryContainer != null) {
			probabilityEntryContainer.showConfirmedProbabilities();
		}
	}
	
	public void showEditableProbabilities() {
		if(probabilityEntryContainer != null) {
			probabilityEntryContainer.showEditableProbabilities();
		}
	}
	
	public void unlockAllProbabilities() {
		if(probabilityEntryContainer != null) {
			probabilityEntryContainer.unlockAllProbabilities();	
		}	
	}
	
	public List<Integer> getCurrentSettings() {
		if(probabilityEntryContainer != null) {
			return probabilityEntryContainer.getCurrentSettings();
		}
		return null;
	}
	
	public void getCurrentSettings(List<Integer> currentSettings) {
		if(probabilityEntryContainer != null) {
			probabilityEntryContainer.getCurrentSettings(currentSettings);
		}
	}
	
	public void setCurrentSettings(List<Integer> currentSettings) {
		if(probabilityEntryContainer != null) {
			probabilityEntryContainer.setCurrentSettings(currentSettings);
		}
	}
	
	public List<Integer> getPreviousSettings() {
		if(probabilityEntryContainer != null) {
			return probabilityEntryContainer.getPreviousSettings();
		}
		return null;
	}
	
	public void getPreviousSettings(List<Integer> previousSettings) {
		if(probabilityEntryContainer != null) {
			probabilityEntryContainer.getPreviousSettings(previousSettings);
		}
	}
	
	public void setPreviousSettings(List<Integer> previousSettings) {
		if(probabilityEntryContainer != null) {
			probabilityEntryContainer.setPreviousSettings(previousSettings);
		}
	}
	
	public List<Boolean> getLockSettings() {
		if(probabilityEntryContainer != null) {
			return probabilityEntryContainer.getLockSettings();
		}
		return null;
	}
	
	public void getLockSettings(List<Boolean> lockSettings) {
		if(probabilityEntryContainer != null) {
			probabilityEntryContainer.getLockSettings(lockSettings);
		}
	}

	public IProbabilityEntryContainer getProbabilityEntryContainer() {
		return probabilityEntryContainer;
	}
	
	public void resetInteractionTimes() {
		if(probabilityEntryContainer != null) {
			probabilityEntryContainer.resetInteractionTimes();
		}
	}
	
	public List<Long> getInteractionTimes() {
		if(probabilityEntryContainer != null) {
			return probabilityEntryContainer.getInteractionTimes();
		}
		return null;
	}
	
	public void getInteractionTimes(List<Long> interactionTimes) {
		if(probabilityEntryContainer != null) {
			probabilityEntryContainer.getInteractionTimes(interactionTimes);
		}
	}
	
	public List<TimeData> getDetailedTimeData() {
		if(probabilityEntryContainer != null) {
			return probabilityEntryContainer.getDetailedTimeData();
		}
		return null;
	}

	@Override
	public void setBackground(Color bg) {
		super.setBackground(bg);
		if(probabilityEntryContainer != null) {
			probabilityEntryContainer.getComponent().setBackground(bg);
		}
	}	
}