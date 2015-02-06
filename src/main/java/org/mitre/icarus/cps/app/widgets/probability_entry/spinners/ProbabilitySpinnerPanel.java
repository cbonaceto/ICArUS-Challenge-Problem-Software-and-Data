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
package org.mitre.icarus.cps.app.widgets.probability_entry.spinners;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.mitre.icarus.cps.app.util.ProbabilityUtils;
import org.mitre.icarus.cps.app.widgets.probability_entry.AbstractProbabilityEntryContainer;
import org.mitre.icarus.cps.app.widgets.probability_entry.ProbabilityContainerLayout;
import org.mitre.icarus.cps.app.widgets.probability_entry.ProbabilityEntryConstants;
import org.mitre.icarus.cps.app.widgets.probability_entry.Setting;
import org.mitre.icarus.cps.app.widgets.probability_entry.TimeData;
import org.mitre.icarus.cps.app.widgets.probability_entry.ProbabilityContainerLayout.ProbabilityContainerLayoutType;
import org.mitre.icarus.cps.app.widgets.util.WidgetUtils;

/**
 * 
 * @author CBONACETO
 *
 */
public class ProbabilitySpinnerPanel extends AbstractProbabilityEntryContainer<JSettingSpinner> {
	
	private static final long serialVersionUID = 5187460388787399727L;
	
	/** the controller */
	//protected IProbabilityController controller;
	
	/** The top title label */
	protected JLabel topTitleLabel;
	
	/** Contains the spinners and their title labels */
	protected ArrayList<TitleAndSpinner> spinners;
	
	/** The settings boxes */
	//protected ArrayList<JSettingSpinner> probabilityFields;
	
	/** The sum of all probability entries */ 
	protected int sum = 0;
	
	/** Whether or not to show the sum */
	//protected boolean sumVisible;
	
	/** Amount of error to allow (+/-) in the sum before the sum text changes to the
	 * error color. If null, sum color will never change to the error color. */
	protected Integer sumErrorThreshold;
	
	/** Whether to automatically normalize the entries as the user changes the input */
	//protected boolean autoNormalize;
	
	/** The sum label */
	protected JLabel sumLabel;
	
	/** Listeners registered to be notified when the sum changes */
	//protected List<IcarusGUIEventListener> listeners;	
	//private final IcarusGUIEvent sumChangedEvent;
	
	public ProbabilitySpinnerPanel(String componentId, ProbabilityContainerLayout layout,
			int numProbabilities, boolean spinnerEditable, boolean sumVisible) {
		this(componentId, layout, numProbabilities, spinnerEditable, sumVisible, null);
	}
	
	public ProbabilitySpinnerPanel(String componentId, ProbabilityContainerLayout layout,
			int numProbabilities, boolean spinnerEditable,
			boolean sumVisible,	List<String> titles) {
		//super(new GridBagLayout());
		super(componentId);
		setLayout(new GridBagLayout());
		
		// instantiate the controller
		//controller = new DefaultProbabilityController(this);
		
		probabilityFields = new ArrayList<JSettingSpinner>(4);
		
		//sumChangedEvent = new IcarusGUIEvent(this, IcarusGUIEvent.PROBABILITY_SUM_CHANGED);
		this.sumVisible = sumVisible;		
		
		//Create the probability spinners
		spinners = new ArrayList<TitleAndSpinner>(numProbabilities);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;			
		for(int i=0; i<numProbabilities; i++) {			
			JSettingSpinner spinner = new JSettingSpinner(new Setting(0, 100, 0), i);
			spinner.setHighlight(true);
			//System.out.println("Creating spinner with id: " + spinner.getId());
			spinner.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black),
					BorderFactory.createEmptyBorder(2, 5, 2, 5)));
			spinner.setEditable(true);			
			spinner.setFormatAsPercent(true);
			spinner.setMinLabel(ProbabilityEntryConstants.minPercentLabel);
			spinner.setMaxLabel(ProbabilityEntryConstants.maxPercentLabel);
			//spinner.addActionListener(this);
			spinner.setProbabilityController(controller);
			
			probabilityFields.add(spinner);
			
			String title = (titles != null && i < titles.size()) ? titles.get(i) : null;
			TitleAndSpinner titleAndSpinner = new TitleAndSpinner(spinner, title, title != null);
			spinners.add(titleAndSpinner);
			layout.updateConstraints(i, numProbabilities, 10, 14, gbc);
			gbc.gridy = 1;
			add(titleAndSpinner, gbc);
			//gbc.insets.left = 14;
			//gbc.gridx++;
		}		
		controller.setProbabilityComponents(probabilityFields);
		
		//Show the sum
		if(sumVisible) {			
			setSumText(" ");
		}		
		sumChanged();
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
//		if(spinners != null) {
//			for(TitleAndSpinner spinner : spinners) {
//				spinner.spinner.setFont(font);
//			}
//			revalidate();
//		}
//	}
	
	@Override
	public void setProbabilityEntryTitleFont(Font font) {
		if(spinners != null) {
			for(TitleAndSpinner spinner : spinners) {
				if(spinner.titleLabel != null) {
					spinner.titleLabel.setFont(font);
				}
			}
			revalidate();
		}
	}
	
	/*@Override
	public void icarusGUIActionPerformed(IcarusGUIEvent event) {
		//sumChanged();
	}*/
	
	/*@Override
	public void actionPerformed(ActionEvent e) {
		sumChanged();
	}*/	

//	@Override
//	public String getComponentId() {
//		return getName();
//	}
//
//	@Override
//	public void setComponentId(String id) {
//		setName(id);
//	}
//
//	@Override
//	public JComponent getComponent() {
//		return this;
//	}

	@Override
	public List<String> getProbabilityEntryTitles() {
		if(spinners != null && !spinners.isEmpty()) {
			ArrayList<String> titles = new ArrayList<String>(spinners.size());
			for(TitleAndSpinner spinner : spinners) {
				titles.add(spinner.getTitle());
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
				if(i < spinners.size()) {
					TitleAndSpinner spinner = spinners.get(i);
					spinner.setTitle(title);
					if(!spinner.isTitleVisible()) {
						spinner.setTitleVisible(true);
					}
				}
				i++;				
			}
		}
	}	

	@Override
	public String getProbabilityEntryTitle(int index) {
		return spinners.get(index).getTitle();
	}

	@Override
	public void setProbabilityEntryTitle(int index, String title) {
		spinners.get(index).setTitle(title);
	}

	@Override
	public Icon getProbabilityEntryTitleIcon(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setProbabilityEntryTitleIcon(int index, Icon icon) {
		// TODO Auto-generated method stub		
	}

	@Override
	public Color getProbabilityEntryTitleColor(int index) {
		return spinners.get(index).getTitleColor();
	}

	@Override
	public void setProbabilityEntryTitleColor(int index, Color color) {
		spinners.get(index).setTitleColor(color);
	}
	
//	@Override
//	public Color getProbabilityEntryColor(int index) {
//		if(spinners != null) {
//			return spinners.get(index).spinner.getProbabilityEntryColor();
//		}
//		return null;
//	}
//
//	@Override
//	public void setProbabilityEntryColor(int index, Color color) {
//		if(spinners != null) {
//			spinners.get(index).spinner.setProbabilityEntryColor(color);
//		}
//	}
	
	@Override
	public void restoreDefaultProbabilityEntryColors() {
		if(spinners != null) {
			for(TitleAndSpinner spinner : spinners) {
				spinner.spinner.setProbabilityEntryColor(Color.BLACK);
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
				gbc.gridwidth = spinners.size();
				gbc.insets.top = 3;
				gbc.insets.bottom = 7;				
				gbc.anchor = GridBagConstraints.CENTER;			
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
			for(JSettingSpinner spinner : probabilityFields) {
				spinner.setEditable(false);
			}
		}
	}

	@Override
	public void showEditableProbabilities() {
		if(probabilityFields != null) {
			for(JSettingSpinner spinner : probabilityFields) {
				spinner.setEditable(true);
			}
		}
	}
	
	@Override
	public void setSumVisible(boolean sumVisible) {
		if(this.sumVisible != sumVisible) {
			this.sumVisible = sumVisible;
			
			if(sumVisible) {
				//Show the sum
				setSumText(" ");
				sumChanged();
			}		
			else {
				//Hide the sum
				setSumText(null);
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
	
	protected void setSumText(String text) {
		if(sumLabel == null) {
			sumLabel = new JLabel();
			sumLabel.setFont(ProbabilityEntryConstants.FONT_TITLE);			
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = 0;	
			gbc.gridy = 2;
			gbc.insets.top = 7;	
			gbc.insets.bottom = 3;
			gbc.insets.left = 5;
			gbc.anchor = GridBagConstraints.CENTER;
			gbc.gridwidth = spinners.size();
			add(sumLabel, gbc);
			revalidate();
			repaint();
		}
		sumLabel.setText(text);
		sumLabel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(getForeground()), 
				BorderFactory.createEmptyBorder(2, 3, 2, 3)));
	}
	
	@Override
	public void sumChanged() {
		//TODO: Standardize this logic
		sum = 0;
		for(JSettingSpinner spinner : probabilityFields) {
			sum += spinner.getDisplaySetting() != null ? spinner.getDisplaySetting() : 0;
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
		
		//Notify any listeners that the sum changed
		//fireSumChangedEvent();
	}

//	@Override
//	public boolean isAutoNormalize() {	
//		return autoNormalize;
//	}
//
//	@Override
//	public void setAutoNormalize(boolean autoNormalize) {
//		controller.setAutoNormalize(autoNormalize);
//		this.autoNormalize = autoNormalize;
//	}
//	
//	public void resetInteractionTimes() {
//		for(TitleAndSpinner spinner : spinners) {
//			spinner.spinner.resetInteractionTime();
//		}
//	}
	
	@Override
	public ArrayList<TimeData> getDetailedTimeData() {
		ArrayList<TimeData> timeData = null;
		if(probabilityFields != null) {
			timeData = new ArrayList<TimeData>(probabilityFields.size());
			for(JSettingSpinner spinner : probabilityFields) {
				timeData.add(new TimeData(spinner.getInteractionTime()));
			}
		}
		return timeData;
	}

//	@Override
//	public List<Long> getInteractionTimes() {
//		ArrayList<Long> interactionTimes = new ArrayList<Long>(spinners.size());		
//		for(int i=0; i<spinners.size(); i++) {
//			interactionTimes.add(0L);
//		}
//		getInteractionTimes(interactionTimes);
//		return interactionTimes;
//	}
//	
//	@Override
//	public void getInteractionTimes(List<Long> interactionTimes) {
//		int i = 0;
//		for(TitleAndSpinner spinner : spinners) {
//			interactionTimes.set(i, spinner.spinner.getInteractionTime());			
//			i++;
//		}
//	}

//	@Override
//	public List<Integer> getCurrentSettings() {
//		ArrayList<Integer> currentSettings = new ArrayList<Integer>(spinners.size());
//		for(int i=0; i<spinners.size(); i++) currentSettings.add(null);
//		getCurrentSettings(currentSettings);
//		return currentSettings;
//	}
//
//	@Override
//	public void getCurrentSettings(List<Integer> currentSettings) {
//		int i = 0;
//		for(TitleAndSpinner spinner: spinners) {
//			currentSettings.set(i, spinner.spinner.getDisplaySetting());			
//			i++;
//		}	
//	}

	@Override
	public void setCurrentSettings(List<Integer> currentSettings) {
		super.setCurrentSettings(currentSettings);
		if(spinners != null) {
			for(TitleAndSpinner spinner: spinners) {
				spinner.repaint();
			}
		}
		//sumChanged();
		/*int i = 0;
		for(TitleAndSpinner spinner: spinners) {
			if(i >= currentSettings.size()) {
				return;
			}
			spinner.spinner.setCurrentSetting(currentSettings.get(i));
			spinner.repaint();
			i++;
		}*/
	}		

	/*public synchronized void addIcarusGUIEventListener(IcarusGUIEventListener listener) {
		if(listeners == null) {
			listeners = new LinkedList<IcarusGUIEventListener>();
		}
		listeners.add(listener);
	}	
	public synchronized void removeIcarusGUIEventListener(IcarusGUIEventListener listener) {
		if(listeners != null) {		
			listeners.remove(listener);
		}
	}	
	protected void fireSumChangedEvent() {	
		if(listeners != null) {
			for(IcarusGUIEventListener listener :listeners) {
				listener.icarusGUIActionPerformed(sumChangedEvent);
			}
		}
	}*/
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		ProbabilitySpinnerPanel spinners = new ProbabilitySpinnerPanel("spinner_panel",
				new ProbabilityContainerLayout(ProbabilityContainerLayoutType.HORIZONTAL, 4),
				4, true, true, 
				new ArrayList<String>(Arrays.asList("A", "B", "C", "D")));
		spinners.setCurrentSettings(ProbabilityUtils.createDefaultInitialProbabilities(4));
		//spinners.setAutoNormalize(true);
		frame.getContentPane().add(spinners);
		frame.pack();
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
	}
	
	public static class TitleAndSpinner extends JPanel {
		private static final long serialVersionUID = 1L;
		
		protected String title;
		
		protected Color titleColor;
		
		protected boolean titleVisible;
		
		protected JLabel titleLabel;
		
		protected JSettingSpinner spinner;
		
		private GridBagConstraints gbc;
		
		public TitleAndSpinner(JSettingSpinner spinner) {
			this(spinner, null, false);
		}
		
		public TitleAndSpinner(JSettingSpinner spinner, String title, boolean titleVisible) {
			super(new GridBagLayout());
			this.spinner = spinner;
			
			gbc = new GridBagConstraints();
			gbc.anchor = GridBagConstraints.CENTER;
			gbc.fill = GridBagConstraints.NONE;
			gbc.gridx = 0;
			gbc.gridy = 0;
			add(spinner, gbc);	
			gbc.gridy = 1;
			
			this.title = title;
			this.titleColor = new JLabel().getForeground();
			if(titleVisible) {
				setTitleVisible(true);
			}
		}
		
		public String getTitle() {			
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
			if(titleLabel != null) {
				titleLabel.setText(title);
				titleLabel.setFont(ProbabilityEntryConstants.FONT_PROBABILITY_TITLE);
				//titleLabel.setPreferredSize(new Dimension(box.getPreferredSize().width + 6,
				//		titleLabel.getPreferredSize().height));
				revalidate();
				repaint();
			}
		}
		
		public Color getTitleColor() {
			return titleColor;
		}
		
		public void setTitleColor(Color color) {
			this.titleColor = color;
			if(titleLabel != null) {
				titleLabel.setForeground(color);
			}
		}
		
		public boolean isTitleVisible() {
			return titleVisible;
		}

		public void setTitleVisible(boolean titleVisible) {
			if(titleVisible != this.titleVisible) {
				this.titleVisible = titleVisible;				
				if(titleVisible) {
					if(titleLabel == null) {
						//TODO: Compute characters per line instead of hard-coding at 10
						titleLabel = new JLabel(
								WidgetUtils.formatMultilineString(title, 10, WidgetUtils.CENTER));
						titleLabel.setHorizontalAlignment(JLabel.CENTER);
						titleLabel.setFont(ProbabilityEntryConstants.FONT_TITLE);
						titleLabel.setForeground(titleColor);
						//titleLabel.setPreferredSize(new Dimension(box.getPreferredSize().width + 6,
						//		titleLabel.getPreferredSize().height));
					}
					add(titleLabel, gbc);				
				}
				else {
					if(titleLabel != null) {
						remove(titleLabel);
					}
				}
				revalidate();
				repaint();
			}
		}
	}	
}