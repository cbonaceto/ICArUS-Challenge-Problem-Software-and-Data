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
package org.mitre.icarus.cps.app.widgets.probability_entry.text_entry;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.mitre.icarus.cps.app.widgets.probability_entry.AbstractProbabilityEntryContainer;
import org.mitre.icarus.cps.app.widgets.probability_entry.ProbabilityContainerLayout;
import org.mitre.icarus.cps.app.widgets.probability_entry.ProbabilityEntryConstants;
import org.mitre.icarus.cps.app.widgets.probability_entry.TimeData;
import org.mitre.icarus.cps.app.widgets.probability_entry.ProbabilityContainerLayout.ProbabilityContainerLayoutType;

/**
 * @author Eric Kappotis
 *
 */
public class ProbabilityTextFieldPanel extends AbstractProbabilityEntryContainer<ProbabilityTextField> { //extends JPanelConditionComponent implements IProbabilityEntryContainer {	
	private static final long serialVersionUID = 6364843401905679560L;
	
	/** Panel containing the probability text fields */
	protected JPanel probabilityFieldsPanel;
	
	private boolean enableLocking = false;
	
	/** The sum of all probability entries */ 
	protected int sum = 0;
	
	/** Amount of error to allow (+/-) in the sum before the sum text changes to the
	 * error color. If null, sum color will never change to the error color. */
	protected Integer sumErrorThreshold;
	
	/** The top title label */
	private JLabel topTitleLabel;
	
	/** The sum label */
	protected JLabel sumLabel;	
	
	/** The layout manager */
	protected GridBagLayout gbl;
	
	public ProbabilityTextFieldPanel(String componentId, ProbabilityContainerLayout layout, 
			int numBoxes, boolean showSpinners) {
		super(componentId);
		setLayout(gbl = new GridBagLayout());
		if(layout == null) {
			layout = new ProbabilityContainerLayout(ProbabilityContainerLayoutType.HORIZONTAL, numBoxes);
		}
		
		probabilityFieldsPanel = new JPanel(new GridBagLayout());	
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weighty = 0;
		constraints.anchor = GridBagConstraints.NORTH;
	
		probabilityFields = new ArrayList<ProbabilityTextField>();		
		for(int i = 0; i < numBoxes; i++) {
			ProbabilityTextField field = new ProbabilityTextField("", true, showSpinners, i, controller);
			//field.setCurrentSetting(null);
			probabilityFields.add(field);
			layout.updateConstraints(i, numBoxes, 24, 10, constraints);
			probabilityFieldsPanel.add(field, constraints);
		}		
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.insets.left = 0;
		constraints.insets.right = 0;
		constraints.insets.top = 0;
		constraints.insets.bottom = 0;
		constraints.weighty = 1;
		add(probabilityFieldsPanel, constraints);
		
		controller.setProbabilityComponents(probabilityFields);
	}		
	
	@Override
	public void setBackground(Color bg) {
		super.setBackground(bg);
		if(probabilityFields != null) {
			for(ProbabilityTextField field : probabilityFields) {
				field.setBackground(bg);
			}
		}
	}

	@Override
	public void setTopTitleAndSumFont(Font font) {
		if(topTitleLabel != null) {
			topTitleLabel.setFont(font);
		}
		if(sumLabel != null) {
			sumLabel.setFont(font);
		}		
		revalidate();
	}

//	@Override
//	public void setProbabilityEntryFont(Font font) {
//		if(probabilityFields != null) {
//			for(ProbabilityTextField field: probabilityFields) {
//				field.setFont(font);
//			}
//			revalidate();
//		}
//	}
	
	@Override
	public void setProbabilityEntryTitleFont(Font font) {
		if(probabilityFields != null) {
			for(ProbabilityTextField field: probabilityFields) {
				field.setTitleFont(font);
			}
			revalidate();
		}
	}

	@Override
	public List<String> getProbabilityEntryTitles() {
		ArrayList<String> probabilityEntryTitles = new ArrayList<String>();
		if(probabilityFields != null) {
			for(ProbabilityTextField entryField : probabilityFields) {
				probabilityEntryTitles.add(entryField.getTitle());
			}		
		}
		return probabilityEntryTitles;
	}

	@Override
	public void setProbabilityEntryTitles(List<String> titles) {
		Iterator<String> titlesIter = titles.iterator();
		if(probabilityFields != null) {
			for(ProbabilityTextField entryField : probabilityFields) {
				entryField.setTitle(titlesIter.next());
			}			
			revalidate();
		}		
	}	

	@Override
	public String getProbabilityEntryTitle(int index) {
		if(probabilityFields != null) {
			return probabilityFields.get(index).getTitle();
		}
		return null;
	}

	@Override
	public void setProbabilityEntryTitle(int index, String title) {
		if(probabilityFields != null) {
			probabilityFields.get(index).setTitle(title);
		}		
	}

	@Override
	public Icon getProbabilityEntryTitleIcon(int index) {
		if(probabilityFields != null) {
			return probabilityFields.get(index).getTitleIcon();
		}
		return null;
	}

	@Override
	public void setProbabilityEntryTitleIcon(int index, Icon icon) {
		if(probabilityFields != null) {
			probabilityFields.get(index).setTitleIcon(icon);
		}		
	}

	@Override
	public Color getProbabilityEntryTitleColor(int index) {
		if(probabilityFields != null) {
			return probabilityFields.get(index).getTitleColor();
		}
		return null;
	}

	@Override
	public void setProbabilityEntryTitleColor(int index, Color color) {
		if(probabilityFields != null) {
			probabilityFields.get(index).setTitleColor(color);
		}
	}	

//	@Override
//	public Color getProbabilityEntryColor(int index) {
//		if(probabilityFields != null) {
//			return probabilityFields.get(index).getProbabilityEntryColor();
//		}
//		return null;
//	}
//
//	@Override
//	public void setProbabilityEntryColor(int index, Color color) {
//		if(probabilityFields != null) {
//			probabilityFields.get(index).setProbabilityEntryColor(color);
//		}
//	}	

	@Override
	public void restoreDefaultProbabilityEntryColors() {
		Color color = new JTextField().getForeground();
		if(probabilityFields != null) {
			for(ProbabilityTextField entryField : probabilityFields) {
				entryField.setProbabilityEntryColor(color);
			}
		}
	}

	@Override
	public boolean isTopTitleVisible() {
		return (topTitleLabel != null && topTitleLabel.isVisible());
	}

	@Override
	public void setTopTitleVisible(boolean visible) {
		if(visible != isTopTitleVisible()) {
			if(visible && topTitleLabel == null) {
				topTitleLabel = new JLabel();
				topTitleLabel.setHorizontalAlignment(JLabel.CENTER);
				topTitleLabel.setFont(ProbabilityEntryConstants.FONT_TITLE);
				GridBagConstraints gbc = new GridBagConstraints();
				gbc.gridx = 0;	
				gbc.gridy = 0;
				gbc.insets.bottom = 7;				
				gbc.anchor = GridBagConstraints.NORTH;			
				add(topTitleLabel, gbc);
			}
			topTitleLabel.setVisible(visible);
			revalidate();
			repaint();
		}
	}	
	
	@Override
	public void setTopTitle(String title) {
		if(topTitleLabel == null) {
			topTitleLabel = new JLabel();
			topTitleLabel.setFont(ProbabilityEntryConstants.FONT_TITLE);
			topTitleLabel.setVisible(false);
		}		
		topTitleLabel.setText(title);
	}

	@Override
	public void showConfirmedProbabilities() {
		if(probabilityFields != null) {
			for(ProbabilityTextField field : probabilityFields) {
				field.setEditable(false);
				field.setEnableLocking(false);
			}
		}
	}

	@Override
	public void showEditableProbabilities() {
		if(probabilityFields != null) {
			for(ProbabilityTextField field : probabilityFields) {
				field.setEditable(true);
				field.setEnableLocking(enableLocking);
			}
			//Set focus to first unlocked text field
			initializeFocus();
		}
	}	

	@Override
	public void setSumVisible(boolean sumVisible) {
		if(this.sumVisible != sumVisible) {
			this.sumVisible = sumVisible;			
			if(sumVisible && sumLabel == null) {
				sumLabel = new JLabel();
				sumLabel.setBorder(BorderFactory.createCompoundBorder(
						BorderFactory.createLineBorder(getForeground()), 
						BorderFactory.createEmptyBorder(2, 3, 2, 3)));
				sumLabel.setFont(ProbabilityEntryConstants.FONT_TITLE);			
				GridBagConstraints gbc = new GridBagConstraints();
				gbc.gridx = 0;	
				gbc.gridy = 2;
				gbc.insets.top = 22; //was 7
				gbc.anchor = GridBagConstraints.NORTH;
				gbc.weighty = 1;
				add(sumLabel, gbc);
			}
			if(sumLabel != null) {
				GridBagConstraints sumLabelConstraints = gbl.getConstraints(sumLabel);
				GridBagConstraints probabilityPanelConstraints = gbl.getConstraints(probabilityFieldsPanel);
				if(sumVisible) {
					sumLabelConstraints.weighty = 1;
					probabilityPanelConstraints.weighty = 0;
				} else {
					sumLabelConstraints.weighty = 0;
					probabilityPanelConstraints.weighty = 1;
				}
				gbl.setConstraints(sumLabel, sumLabelConstraints);
				gbl.setConstraints(probabilityFieldsPanel, probabilityPanelConstraints);
				sumLabel.setVisible(sumVisible);
			}
			revalidate();
			repaint();
			if(sumVisible) { 
				setSumText(" ");
				sumChanged();
			}
		}		
	}
	
	protected void setSumText(String text) {
		if(sumLabel != null) {
			sumLabel.setText(text);
		}
	}
	
	@Override
	public void sumChanged() {
		//TODO: Standardize this logic
		sum = 0;
		if(probabilityFields != null) {
			for(ProbabilityTextField field : probabilityFields) {
				sum += field.getDisplaySetting() != null ? field.getDisplaySetting() : 0;;
			}
		}

		if(sumVisible) {
			setSumText(Integer.toString(sum) + "%");
			if(sumErrorThreshold == null) {
				sumLabel.setForeground(ProbabilityEntryConstants.COLOR_TEXT_NORMAL);
			} else {
				if(Math.abs(sum-100) <= sumErrorThreshold) {
					sumLabel.setForeground(ProbabilityEntryConstants.COLOR_TEXT_NORMAL);
				} else {
					sumLabel.setForeground(ProbabilityEntryConstants.COLOR_TEXT_ERROR);
				}
			}
		}		
		repaint();			
		/*if(notifyListeners) {
			fireSumChangedEvent();
		}*/
	}
	
	@Override
	public Integer getSumErrorThreshold() {
		return sumErrorThreshold;
	}

	@Override
	public void setSumErrorThreshold(Integer threshold) {
		this.sumErrorThreshold = threshold;
	}

//	@Override
//	public boolean isAutoNormalize() {
//		return this.autoNormalize;
//	}
//
//	@Override
//	public void setAutoNormalize(boolean autoNormalize) {
//		this.autoNormalize = autoNormalize;
//		controller.setAutoNormalize(autoNormalize);
//	}
	
	@Override
	public List<TimeData> getDetailedTimeData() {
		return null;
	}

//	@Override
//	public void resetInteractionTimes() {
//		for(ProbabilityTextField entryField : probabilityFields) {
//			entryField.resetInteractionTime();
//		}
//	}	

//	@Override
//	public List<Long> getInteractionTimes() {
//		ArrayList<Long> interactionTimes = new ArrayList<Long>(probabilityFields.size());
//		if(probabilityFields != null) {
//			for(ProbabilityTextField entryField : probabilityFields) {
//				interactionTimes.add(entryField.getInteractionTime());
//			}
//		}
//		return interactionTimes;
//	}
//
//	@Override
//	public void getInteractionTimes(List<Long> interactionTimes) {
//		int i = 0;
//		if(probabilityFields != null) {
//			for(ProbabilityTextField entryField : probabilityFields) {
//				interactionTimes.set(i, entryField.getInteractionTime());
//				i++;
//			}
//		}
//	}
//
//	@Override
//	public List<Integer> getCurrentSettings() {
//		ArrayList<Integer> currentSettings = null;
//		if(probabilityFields != null) {
//			currentSettings = new ArrayList<Integer>(probabilityFields.size());
//			for(ProbabilityTextField entryField : probabilityFields) {
//				currentSettings.add(entryField.getDisplaySetting());
//			}		
//		}
//		return currentSettings;
//	}
//
//	@Override
//	public void getCurrentSettings(List<Integer> currentSettings) {
//		int i = 0;
//		if(probabilityFields != null) {
//			for(ProbabilityTextField entryField : probabilityFields) {
//				currentSettings.set(i, entryField.getDisplaySetting());
//				i++;
//			}
//		}
//	}
//
//	@Override
//	public void setCurrentSettings(List<Integer> currentSettings) {		
//		Iterator<Integer> settingsIter = currentSettings.iterator();
//		if(probabilityFields != null) {
//			for(ProbabilityTextField entryField : probabilityFields) {
//				entryField.setCurrentSetting(settingsIter.next());
//			}
//		}
//		updateSum(false);
//	}
//
//	@Override
//	public List<Integer> getPreviousSettings() {
//		return null;
//	}
//
//	@Override
//	public void getPreviousSettings(List<Integer> previousSettings) {
//		/*for(ProbabilityEntryField entryField : probabilityFields) {
//			previousSettings.add(entryField.getPreviousValue());
//		}*/
//	}
//
//	@Override
//	public void setPreviousSettings(List<Integer> previousSettings) {
//		/*Iterator<Integer> settingsIter = previousSettings.iterator();		
//		for(ProbabilityEntryField entryField : probabilityFields) {
//			entryField.setPreviousValue(settingsIter.next());
//		}*/
//	}	

//	@Override
//	public void setEnableLocking(boolean enableLocking) {
//		if(this.enableLocking != enableLocking) {
//			this.enableLocking = enableLocking;
//			for(ProbabilityTextField currField : probabilityFields) {
//				currField.setEnableLocking(enableLocking);
//			}
//			revalidate();
//		}
//	}
//
//	@Override
//	public boolean isEnableLocking() {
//		return enableLocking;
//	}	
//
//	@Override
//	public List<Boolean> getLockSettings() {
//		ArrayList<Boolean> lockSettings = null; 	
//		if(probabilityFields != null && enableLocking) {
//			lockSettings = new ArrayList<Boolean>(probabilityFields.size());
//			for(ProbabilityTextField entryField : probabilityFields) {
//				lockSettings.add(entryField.isLocked());
//			}		
//		}
//		return lockSettings;
//	}
//
//	@Override
//	public void getLockSettings(List<Boolean> lockSettings) {
//		int i = 0;
//		if(probabilityFields != null && enableLocking) {
//			for(ProbabilityTextField entryField : probabilityFields) {
//				lockSettings.set(i, entryField.isLocked());
//				i++;
//			}
//		}
//	}
//
//	@Override
//	public void unlockAllProbabilities() {
//		for(ProbabilityTextField currField : probabilityFields) {
//			currField.setLocked(false);
//		}
//	}
	
	public void initializeFocus() {
		if(probabilityFields != null) {
			for(ProbabilityTextField field : probabilityFields) {
				if(!field.isLocked()) {
					field.requestFocusInWindow();
					break;
				}
			}
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		ProbabilityTextFieldPanel panel = new ProbabilityTextFieldPanel("componentId", 
				new ProbabilityContainerLayout(ProbabilityContainerLayoutType.VERTICAL, 4), 4, true);
		panel.setProbabilityEntryTitles(new ArrayList<String>(Arrays.asList("1", "2", "3", "4")));
		panel.setCurrentSettings(Arrays.asList(25, 25, 25, 25));
		//panel.setBackground(Color.blue);
		//panel.setAutoNormalize(true);
		panel.setSumErrorThreshold(0);
		panel.setSumVisible(true);
		panel.setEnableLocking(false);
		panel.setTopTitleVisible(true);
		panel.setTopTitle("A bunch of random probabilities for you to work with down there. Very cool indeed.");
		panel.showEditableProbabilities();
		//panel.showConfirmedProbabilities();
		frame.getContentPane().add(panel);
		
		frame.pack();
		frame.setVisible(true);
	}
}