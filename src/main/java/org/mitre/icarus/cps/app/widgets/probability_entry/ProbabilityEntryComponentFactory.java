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
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import org.mitre.icarus.cps.app.widgets.probability_entry.ProbabilityContainerLayout.ProbabilityContainerLayoutType;
import org.mitre.icarus.cps.app.widgets.probability_entry.distinct_bars.ProbabilityBoxContainer;
import org.mitre.icarus.cps.app.widgets.probability_entry.distinct_bars.BoxContainer.BoxOrientation;
import org.mitre.icarus.cps.app.widgets.probability_entry.distinct_bars.SettingsBoxWithDecorators.EditControlType;
import org.mitre.icarus.cps.app.widgets.probability_entry.spinners.ProbabilitySpinnerPanel;
import org.mitre.icarus.cps.app.widgets.probability_entry.stacked_bars.StackedBarPanel;
import org.mitre.icarus.cps.app.widgets.probability_entry.text_entry.ProbabilityTextFieldPanel;

/**
 * Factory class for creating different types of probability entry components.
 * 
 * @author CBONACETO
 *
 */
public class ProbabilityEntryComponentFactory {	
	/** Probability entry component types */
	public static enum ProbabilityEntryComponentType {Boxes, Boxes_With_Spinners, Stacked_Bars, Spinners, Text, Text_With_Spinners};	
	
	/** The default probability entry type */
	protected static ProbabilityEntryComponentType DEFAULT_COMPONENT_TYPE = ProbabilityEntryComponentType.Boxes;
	
	/** The default box size to use for boxes components */
	protected static Dimension BOXES_SIZE = ProbabilityEntryConstants.BOX_SIZE;
	
	/**
	 * @param componentType
	 */
	public static void setDefaultProbabilityEntryComponentType(ProbabilityEntryComponentType componentType) {
		 DEFAULT_COMPONENT_TYPE = componentType;
	}
	
	/**
	 * @param boxesSize
	 */
	public static void setDefaultBoxesSize(Dimension boxesSize) {
		BOXES_SIZE = boxesSize;
	}
	
	/**
	 * @param componentType
	 * @param titles
	 * @param autoNormalize
	 * @param sumVisible
	 * @param topTitleVisible
	 * @param editable
	 * @return
	 */
	public static IProbabilityEntryContainer createProbabilityEntryComponent(ProbabilityEntryComponentType componentType,
			List<String> titles, boolean autoNormalize, boolean sumVisible, boolean topTitleVisible, boolean editable) {
		return creatProbabilityEntryComponent(componentType, null, titles, autoNormalize, sumVisible, topTitleVisible, 
				editable, ProbabilityEntryConstants.minPercentLabel, ProbabilityEntryConstants.maxPercentLabel);
	}
	
	/**
	 * @param componentType
	 * @param titles
	 * @param autoNormalize
	 * @param sumVisible
	 * @param topTitleVisible
	 * @param editable
	 * @param minLabel
	 * @param maxLabel
	 * @return
	 */
	public static IProbabilityEntryContainer creatProbabilityEntryComponent(ProbabilityEntryComponentType componentType, 
			List<String> titles, boolean autoNormalize, boolean sumVisible, 
			boolean topTitleVisible, boolean editable, 
			String minLabel, String maxLabel) {
		return creatProbabilityEntryComponent(componentType, null, titles, autoNormalize, sumVisible, 
				topTitleVisible, editable, minLabel, maxLabel);
	}
	
	/**
	 * @param componentType
	 * @param layout
	 * @param titles
	 * @param autoNormalize
	 * @param sumVisible
	 * @param topTitleVisible
	 * @param editable
	 * @param minLabel
	 * @param maxLabel
	 * @return
	 */
	public static IProbabilityEntryContainer creatProbabilityEntryComponent(ProbabilityEntryComponentType componentType, 
			ProbabilityContainerLayout layout, List<String> titles, boolean autoNormalize, boolean sumVisible, 
			boolean topTitleVisible, boolean editable, 
			String minLabel, String maxLabel) {
		IProbabilityEntryContainer component = null;
		layout = layout != null ? layout : new ProbabilityContainerLayout(ProbabilityContainerLayoutType.HORIZONTAL, titles.size());
		switch(componentType) {		
		case Boxes:
			component = createBoxesProbabilityEntryComponent(titles, BOXES_SIZE,
					ProbabilityEntryConstants.COLOR_CURRENT_SETTING_FILL, ProbabilityEntryConstants.COLOR_CURRENT_SETTING_FILL_DISABLED,
					autoNormalize, sumVisible, EditControlType.PercentDisplay, editable, minLabel, maxLabel);			
			break;
		case Boxes_With_Spinners:
			component = createBoxesProbabilityEntryComponent(titles, BOXES_SIZE,
					ProbabilityEntryConstants.COLOR_CURRENT_SETTING_FILL, ProbabilityEntryConstants.COLOR_CURRENT_SETTING_FILL_DISABLED,
					autoNormalize, sumVisible, EditControlType.Spinner, editable, minLabel, maxLabel);			
			break;
		case Stacked_Bars:
			component = createStackedBarsProbabilityEntryComponent(titles, minLabel, maxLabel);
			component.setSumVisible(sumVisible);
			break;
		case Spinners:
			component = createSpinnersProbabilityEntryComponent(layout, titles, autoNormalize, minLabel, maxLabel);
			component.setSumVisible(sumVisible);
			break;
		case Text:
			component = createTextProbabilityEntryComponent(layout, titles, autoNormalize, false, minLabel, maxLabel);
			component.setSumVisible(sumVisible);
			break;
		case Text_With_Spinners:
			component = createTextProbabilityEntryComponent(layout, titles, autoNormalize, true, minLabel, maxLabel);
			component.setSumVisible(sumVisible);
			break;
		}
		if(component != null) {
			component.setTopTitleVisible(topTitleVisible);
			if(!editable) {
				component.showConfirmedProbabilities();
			}		
		}
		return component;
	}
	
	/**
	 * Create a default probability entry component (now the "boxes" component with no spinner).
	 * 
	 * @param titles
	 * @param autoNormalize
	 * @return
	 */
	public static IProbabilityEntryContainer createDefaultProbabilityEntryComponent(List<String> titles, 
			boolean autoNormalize) {
		return createBoxesProbabilityEntryComponent(titles, autoNormalize, true, EditControlType.PercentDisplay, 
				true, ProbabilityEntryConstants.minPercentLabel, ProbabilityEntryConstants.maxPercentLabel);
	}
	
	public static IProbabilityEntryContainer createDefaultProbabilityEntryComponent(List<String> titles, 
			boolean autoNormalize, String minLabel, String maxLabel) {
		return createBoxesProbabilityEntryComponent(titles, autoNormalize, true, EditControlType.PercentDisplay, 
				true, minLabel, maxLabel);
	}
	
	/**
	 * Create a default probability entry component for displaying previous settings.
	 * 
	 * @param titles
	 * @param minLabel
	 * @param maxLabel
	 * @return
	 */
	public static IProbabilityEntryContainer createDefaultPreviousSettingsComponent(List<String> titles) {
		return createDefaultPreviousSettingsComponent(titles, ProbabilityEntryConstants.minPercentLabel, 
				ProbabilityEntryConstants.maxPercentLabel);
	}
	
	/**
	 * Create a default probability entry component for displaying previous settings.
	 * 
	 * @param titles
	 * @param minLabel
	 * @param maxLabel
	 * @return
	 */
	public static IProbabilityEntryContainer createDefaultPreviousSettingsComponent(List<String> titles,
			String minLabel, String maxLabel) {
		//ProbabilityTextFieldPanel component = createTextProbabilityEntryComponent(titles, false, minLabel, maxLabel);
		ProbabilityBoxContainer component = createBoxesProbabilityEntryComponent(titles, false, false, 
				EditControlType.PercentDisplay, false, minLabel, maxLabel);
		component.setTopTitleVisible(true);
		component.showConfirmedProbabilities();
		return component;
	}
	
	/**
	 * Create a "classic boxes" probability entry component with original default box size, original current
	 * setting fill color, original disabled current setting fill color, and a spinner control.
	 *
	 * @param titles
	 * @param minLabel
	 * @param maxLabel
	 * @return
	 */
	public static ProbabilityBoxContainer createClassicBoxesProbabilityEntryComponent(List<String> titles, 
			String minLabel, String maxLabel) {
		ProbabilityBoxContainer component = createBoxesProbabilityEntryComponent(titles, 
				ProbabilityEntryConstants.BOX_SIZE_CLASSIC,
				ProbabilityEntryConstants.COLOR_CURRENT_SETTING_FILL_CLASSIC, 
				ProbabilityEntryConstants.COLOR_CURRENT_SETTING_FILL_DISABLED,
				false, true, EditControlType.Spinner, true,
				ProbabilityEntryConstants.minPercentLabel, ProbabilityEntryConstants.maxPercentLabel);
		component.setBoxesDraggable(true);
		component.setBoxesClickable(false);
		component.setCurrentSettingLineDraggable(false);
		return component;
	}
	
	/**
	 * Create the "boxes" probability entry component with default box size, default current setting fill color,
	 * and default current setting fill color when disabled.
	 *	 
	 * @param titles
	 * @param autoNormalize
	 * @param sumVisible
	 * @param editControlType
	 * @param editControlEditable
	 * @param minLabel
	 * @param maxLabel
	 * @return
	 */
	public static ProbabilityBoxContainer createBoxesProbabilityEntryComponent(List<String> titles,
			boolean autoNormalize, boolean sumVisible, EditControlType editControlType, boolean editControlEditable,
			String minLabel, String maxLabel) {
		return createBoxesProbabilityEntryComponent(titles, BOXES_SIZE,
				ProbabilityEntryConstants.COLOR_CURRENT_SETTING_FILL, ProbabilityEntryConstants.COLOR_CURRENT_SETTING_FILL_DISABLED,
				autoNormalize, sumVisible, editControlType, editControlEditable, minLabel, maxLabel);
	}
	
	/**
	 * Create the "boxes" probability entry component with specified box size, current setting fill color,
	 * and current setting fill color when disabled.
	 *
	 * @param titles
	 * @param boxSize
	 * @param currentSettingFillColor
	 * @param currentSettingFillColorDisabled
	 * @param autoNormalize
	 * @param sumVisible
	 * @param editControlType
	 * @param editControlEditable
	 * @param minLabel
	 * @param maxLabel
	 * @return
	 */
	public static ProbabilityBoxContainer createBoxesProbabilityEntryComponent(List<String> titles, Dimension boxSize,			
			Color currentSettingFillColor, Color currentSettingFillColorDisabled, boolean autoNormalize, 
			boolean sumVisible, EditControlType editControlType, boolean editControlEditable,
			String minLabel, String maxLabel) {
		ProbabilityBoxContainer component = new ProbabilityBoxContainer(
				BoxOrientation.HORIZONTAL_LINE,				
				boxSize,
				titles.size(),
				editControlType, editControlEditable, true, sumVisible, 
				titles, minLabel, maxLabel);		
		component.setBoxesDraggable(true);
		component.setBoxesClickable(true);
		component.setDisplaySettingAtMouseLocation(false);
		component.setTopTitleVisible(true);
		component.setTopTitle("your probabilities");
		component.setAutoNormalize(autoNormalize);
		component.setCurrentSettingFillColor(currentSettingFillColor);
		component.setCurrentSettingFillColorDisabled(currentSettingFillColorDisabled);
		return component;
	}
	
	/**
	 * @param titles
	 * @param autoNormalize
	 * @param minLabel
	 * @param maxLabel
	 * @return
	 */
	public static ProbabilitySpinnerPanel createSpinnersProbabilityEntryComponent(ProbabilityContainerLayout layout,
			List<String> titles, boolean autoNormalize, String minLabel, String maxLabel) {
		ProbabilitySpinnerPanel component = new ProbabilitySpinnerPanel("SpinnerPanel", layout, titles.size(), true, true, titles);
		component.setAutoNormalize(autoNormalize);
		return component;
	}
	
	/**
	 * @param titles
	 * @param minLabel
	 * @param maxLabel
	 * @return
	 */
	public static StackedBarPanel createStackedBarsProbabilityEntryComponent(List<String> titles, String minLabel, String maxLabel) {
		return createStackedBarsProbabilityEntryComponent(titles, ProbabilityEntryConstants.STACKED_BARS_SIZE, 
				createDefaultBarColors(titles.size()), minLabel, maxLabel);
	}
	
	/**
	 * @param titles
	 * @param barSize
	 * @param minLabel
	 * @param maxLabel
	 * @return
	 */
	public static StackedBarPanel createStackedBarsProbabilityEntryComponent(List<String> titles, Dimension barSize, 
			String minLabel, String maxLabel) {
		return createStackedBarsProbabilityEntryComponent(titles, barSize, 
				createDefaultBarColors(titles.size()), minLabel, maxLabel);
	}
	
	/**
	 * @param titles
	 * @param barSize
	 * @param regionColors
	 * @param minLabel
	 * @param maxLabel
	 * @return
	 */
	public static StackedBarPanel createStackedBarsProbabilityEntryComponent(List<String> titles, 
			Dimension barSize, List<Color> regionColors, 
			String minLabel, String maxLabel) {
		StackedBarPanel component = new StackedBarPanel("StackedBarPanel", titles.size(), barSize, new Dimension(100, 5), regionColors);
		component.setProbabilityEntryTitles(titles);
		return component;
	}
	
	protected static List<Color> createDefaultBarColors(int numProbabilities) {
		List<Color> colors = new ArrayList<Color>(numProbabilities);
		for(int i = 0; i<numProbabilities; i++) {
			switch(i % 4) {
			case 0: colors.add(new Color(255, 102, 51)); break;
			case 1: colors.add(new Color(0, 184, 245)); break;
			case 2: colors.add(new Color(61, 245, 0)); break;
			case 3: colors.add(new Color(219, 112, 255)); break;
			}
		}
		return colors;
	}	
	
	/**
	 * @param titles
	 * @param autoNormalize
	 * @param minLabel
	 * @param maxLabel
	 * @return
	 */
	public static ProbabilityTextFieldPanel createTextProbabilityEntryComponent(ProbabilityContainerLayout layout,
			List<String> titles, boolean autoNormalize, boolean showSpinners, String minLabel, String maxLabel) {
		ProbabilityTextFieldPanel component = new ProbabilityTextFieldPanel("TextEntryPanel", layout, titles.size(), showSpinners);
		component.setProbabilityEntryTitles(titles);
		component.setAutoNormalize(autoNormalize);
		return component;
	}
}