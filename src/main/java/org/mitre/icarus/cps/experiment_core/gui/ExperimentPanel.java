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
package org.mitre.icarus.cps.experiment_core.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.HyperlinkListener;

import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.experiment_core.Experiment;
import org.mitre.icarus.cps.experiment_core.condition.Condition;
import org.mitre.icarus.cps.experiment_core.condition.ConditionConfiguration;
import org.mitre.icarus.cps.experiment_core.condition.InstructionsPage;
import org.mitre.icarus.cps.experiment_core.controller.IExperimentController;
import org.mitre.icarus.cps.experiment_core.event.SubjectActionListener;
import org.mitre.icarus.cps.experiment_core.gui.ConditionPanel.BannerOrientation;
import org.mitre.icarus.cps.experiment_core.gui.INavButtonPanel.ButtonType;

/**
 *  Default experiment panel implementation.
 *  
 * @author CBONACETO
 *
 */
public class ExperimentPanel<
	EC extends IExperimentController<E, ?, ?>,
	E extends Experiment<?>,
	C extends Condition, 
	CP extends ConditionPanel> extends JPanel implements IExperimentPanel<EC, E, C, CP> {
	private static final long serialVersionUID = 1L;
	
	/** The window the panel is in (if any) */
	protected Component parentWindow;
	
	/** The current condition being displayed */
	protected ConditionConfiguration<EC, E, C, CP> currentConfiguration;
	
	/** The condition configurations (mapped by the condition ID) */
	protected HashMap<String, ConditionConfiguration<EC, E, C, CP>> conditionConfigurations;

	/** The condition panel area */
	protected JPanel contentPane;	
	
	/** The navigation buttons panel */
	protected INavButtonPanel navButtonPanel;
	
	/** The internal instruction panel */
	protected IInstructionsPanel instructionPanel;
	
	/** The status panel */
	protected IExperimentStatusPanel statusPanel;
	
	/** The number of (counted) conditions */
	protected int numConditions;
	
	/** The number of trials in the current condition */
	protected int numTrials;	
	
	/** The external instructions window */
	protected JFrame externalInstructionsWindow;
	
	/** The external instructions panel */
	protected IInstructionNavigationPanel externalInstructionsPanel; 
	
	public ExperimentPanel() {
		this(null, null);
	}
	
	public ExperimentPanel(Component parentWindow) {
		this(parentWindow, null);
	}
	
	public ExperimentPanel(Component parentWindow,
			ArrayList<ConditionConfiguration<EC, E, C, CP>> conditionConfigurations) {
		this(parentWindow, conditionConfigurations, 
				new NavButtonPanel(), BannerOrientation.Top, 
				new ExperimentStatusPanel(WidgetConstants.FONT_STATUS),
				BannerOrientation.Bottom,
				new InstructionNavigationPanel());
	}
	
	public ExperimentPanel(Component parentWindow,
			ArrayList<ConditionConfiguration<EC, E, C, CP>> conditionConfigurations,
			INavButtonPanel navButtonPanel, BannerOrientation navButtonPanelOrientation,
			IExperimentStatusPanel statusPanel, BannerOrientation statusPanelOrientation) {
		this(parentWindow, conditionConfigurations, 
				new NavButtonPanel(), BannerOrientation.Top, 
				new ExperimentStatusPanel(WidgetConstants.FONT_STATUS),
				BannerOrientation.Bottom, 
				new InstructionNavigationPanel());
	}
	
	protected ExperimentPanel(Component parentWindow,
			ArrayList<ConditionConfiguration<EC, E, C, CP>> conditionConfigurations,
			INavButtonPanel navButtonPanel, BannerOrientation navButtonPanelOrientation,
			IExperimentStatusPanel statusPanel, BannerOrientation statusPanelOrientation,
			IInstructionNavigationPanel externalInstructionsPanel) {
		super(new GridBagLayout());
		this.parentWindow = parentWindow;
		this.navButtonPanel = navButtonPanel;
		this.statusPanel = statusPanel;
		this.externalInstructionsPanel = externalInstructionsPanel;
		this.conditionConfigurations = new HashMap<String, ConditionConfiguration<EC, E, C, CP>>();		
		numConditions = 0;
		createExperimentPanel(conditionConfigurations, navButtonPanelOrientation, statusPanelOrientation);
	}
	
	@Override
	public Component getParentWindow() {
		return parentWindow;
	}

	public void setConditionConfigurations(ArrayList<ConditionConfiguration<EC, E, C, CP>> conditionConfigurations) {		
		this.conditionConfigurations = new HashMap<String, ConditionConfiguration<EC, E, C, CP>>();
		numConditions = 0;
		
		//Size the content pane to contain the largest condition panel
		Dimension maxSize = new Dimension(1, 1);		
		if(conditionConfigurations != null) {
			for(ConditionConfiguration<EC, E, C, CP> conditionConfiguration : conditionConfigurations) {
				if(conditionConfiguration.condition.isCountCondition()) {
					numConditions++;
				}
				
				this.conditionConfigurations.put(conditionConfiguration.conditionId, conditionConfiguration);
				Dimension size = conditionConfiguration.conditionPanel.getPreferredSize();
				if(size.width > maxSize.width) {
					maxSize.width = size.width;
				}
				if(size.height > maxSize.height) {
					maxSize.height = size.height;
				}
			}
			maxSize.width += 1; maxSize.height += 1;
		}		
		contentPane.setPreferredSize(maxSize);
		instructionPanel.getComponent().setPreferredSize(maxSize);
	}
	
	protected void createExperimentPanel(ArrayList<ConditionConfiguration<EC, E, C, CP>> conditionConfigurations,
			BannerOrientation navButtonPanelOrientation, BannerOrientation statusPanelOrientation) {
		
		//Create the content panel
		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());
		
		//Create the instructions panel
		instructionPanel = new InstructionsPanel("instructions");
		instructionPanel.getComponent().setBackground(WidgetConstants.COLOR_COMPONENT_BACKGROUND);
		
		//Create the nav button panel if it's null
		if(navButtonPanel == null) {
			navButtonPanel = new NavButtonPanel();
			navButtonPanel.setButtonEnabled(ButtonType.Next, false);
			navButtonPanel.setButtonEnabled(ButtonType.Back, false);
			navButtonPanel.setButtonEnabled(ButtonType.Help, false);
		}
		
		setConditionConfigurations(conditionConfigurations);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0; gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		
		if(statusPanel == null) {
			statusPanel = new ExperimentStatusPanel(WidgetConstants.FONT_STATUS);
		}
		
		//Add the status panel at the top
		if(statusPanelOrientation == BannerOrientation.Top) {
			gbc.weighty = 0;
			gbc.fill = GridBagConstraints.HORIZONTAL;		
			add(statusPanel.getExperimentStatusPanelComponent(), gbc);
			
			gbc.gridy++;		
			gbc.fill = GridBagConstraints.HORIZONTAL;
			add(WidgetConstants.createDefaultSeparator(), gbc);
			gbc.gridy++;
		}
		
		//Add the nav button panel at the top
		if(navButtonPanelOrientation == BannerOrientation.Top) {
			gbc.weighty = 0;
			gbc.fill = GridBagConstraints.HORIZONTAL;		
			add(navButtonPanel.getNavButtonPanelComponent(), gbc);
			
			gbc.gridy++;		
			gbc.fill = GridBagConstraints.HORIZONTAL;
			add(WidgetConstants.createDefaultSeparator(), gbc);
			gbc.gridy++;
		}
		
		//Add the content pane		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weighty = 1;
		add(contentPane, gbc);	
		
		//Add the instructions panel
		gbc.gridy++;
		gbc.weighty = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		if(navButtonPanelOrientation == BannerOrientation.Bottom || 
				statusPanelOrientation == BannerOrientation.Bottom) {
			add(WidgetConstants.createDefaultSeparator(), gbc);
		}
		
		//Add the nav button panel at the bottom
		if(navButtonPanelOrientation == BannerOrientation.Bottom) {
			gbc.gridy++;
			gbc.weighty = 0;
			gbc.fill = GridBagConstraints.HORIZONTAL;		
			add(navButtonPanel.getNavButtonPanelComponent(), gbc);
			
			gbc.gridy++;		
			gbc.fill = GridBagConstraints.HORIZONTAL;
			add(WidgetConstants.createDefaultSeparator(), gbc);
		}		
		
		//Add the status panel at the bottom
		if(statusPanelOrientation == BannerOrientation.Bottom) {
			gbc.gridy++;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			add(statusPanel.getExperimentStatusPanelComponent(), gbc);
		}
	}	
	
	@Override
	public void setShowStatusPanel(boolean showStatusPanel) {
		statusPanel.getExperimentStatusPanelComponent().setVisible(showStatusPanel);
	}
	
	@Override
	public boolean isShowStatusPanel() {
		return statusPanel.getExperimentStatusPanelComponent().isVisible();
	}
	
	@Override
	public JComponent getExperimentPanelComponent() {
		return this;
	}

	@Override
	public void showCondition(String conditionId) {		
		showCondition(conditionConfigurations.get(conditionId));
	}	
	
	@Override
	public void showCondition(ConditionConfiguration<EC, E, C, CP> conditionConfiguration) {
		if(conditionConfiguration == null || !conditionConfigurations.containsKey(conditionConfiguration.conditionId)) {
			throw new IllegalArgumentException("Error setting condition: condition not present");
		}
		
		//Show the condition panel
		if(currentConfiguration == null || currentConfiguration != conditionConfiguration) {			
			currentConfiguration = conditionConfiguration;		
			showContent(currentConfiguration.conditionPanel);
		}
	}
	
	@Override
	public void showContent(JComponent content) {
		contentPane.removeAll();
		if(content != null) {
			contentPane.add(content, BorderLayout.CENTER);
		}
		contentPane.revalidate();
		contentPane.repaint();
	}
	
	@Override
	public void clearCurrentConfiguration() {
		currentConfiguration = null;
	}
	
	public void setExternalInstructionsVisible(boolean visible) {
		if(externalInstructionsWindow == null && visible) {
			externalInstructionsWindow = new JFrame("Task Instructions");			
			externalInstructionsWindow.setResizable(true);
			externalInstructionsWindow.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			if(externalInstructionsPanel == null) {
				externalInstructionsPanel = new InstructionNavigationPanel();				
			}
			/*Dimension size = instructionPanel.getComponent().getPreferredSize();			
			externalInstructionsPanel.getComponent().setPreferredSize(
					new Dimension(size.width - 50, size.height + 50)); //new Dimension(size.width - 100, size.height + 50));*/
			externalInstructionsWindow.getContentPane().add(externalInstructionsPanel.getComponent());
			externalInstructionsWindow.pack();
			externalInstructionsWindow.setMinimumSize(externalInstructionsWindow.getSize());
			if(parentWindow != null) {
				externalInstructionsWindow.setLocationRelativeTo(parentWindow);
			}
		}
		if(currentConfiguration != null && visible) {
			ArrayList<InstructionsPage> pages = currentConfiguration.condition.getInstructionPages();
			if(pages != null && 
					!currentConfiguration.condition.getName().equals(externalInstructionsPanel.getInstructionsId())) {
				externalInstructionsPanel.setInstructionsId(currentConfiguration.condition.getName());
				String pagesName = currentConfiguration.condition.getName() + " Instructions";
				externalInstructionsPanel.setInstructionsPages(pagesName, pages);
			}
		}
		if(externalInstructionsWindow != null) {
			externalInstructionsWindow.setVisible(visible);
		}
	}	
	
	@Override
	public void showInternalInstructionPanel() {		
		contentPane.removeAll();		
		contentPane.add(instructionPanel.getComponent(), BorderLayout.CENTER);
		contentPane.revalidate();
		contentPane.repaint();
	}
	
	public void addInstructionsPanelHyperlinkListener(HyperlinkListener listener) {
		instructionPanel.addHyperlinkListener(listener);
		externalInstructionsPanel.addHyperlinkListener(listener);
	}
	
	public void removeInstructionsPanelHyperlinkListener(HyperlinkListener listener) {
		instructionPanel.removeHyperlinkListener(listener);
		externalInstructionsPanel.removeHyperlinkListener(listener);
	} 
	
	@Override
	public void setInstructionsPage(InstructionsPage page) {
		instructionPanel.setInstructionsPage(page);
	}
	
	@Override
	public void setInstructionsURL(URL url) {
		instructionPanel.setInstructionsURL(url);
	}
	
	@Override
	public void setInstructionText(String html) {
		instructionPanel.setInstructionText(html);
	}
	
	@Override
	public void setNavButtonText(ButtonType button, String text) {
		navButtonPanel.setButtonText(button, text);
	}
	
	@Override
	public void setNavButtonFocused(ButtonType button) {
		navButtonPanel.setFocusedButton(button);
	}
	
	@Override
	public void setNavButtonVisible(ButtonType button, boolean visible) {
		navButtonPanel.setButtonVisible(button, visible);
	}	
	
	@Override
	public boolean isNavButtonEnabled(ButtonType button) {
		return navButtonPanel.isButtonEnabled(button);
	}

	@Override
	public void setNavButtonEnabled(ButtonType button, boolean enabled) {
		navButtonPanel.setButtonEnabled(button, enabled);
	}
	
	@Override
	public void setExperimentName(String experimentName) {
		statusPanel.setExperimentInfo(experimentName);
	}
	
	@Override
	public void setSubject(String subject) {
		statusPanel.setSubjectInfo(subject);
	}
	
	@Override
	public void setNumConditions(int numConditions) {
		this.numConditions = numConditions;
	}
	
	@Override
	public void setConditionNumber(int conditionNumber, int numTrials) {
		setConditionNumber(conditionNumber, numTrials);	
	}
	
	@Override
	public void setConditionNumber(String conditionName, int conditionNumber, int numTrials) {
		this.numTrials = numTrials;
		statusPanel.setConditionInfo(conditionNumber, numConditions, conditionName);
		statusPanel.setTrialInfo(0, numTrials);		
	}
	
	@Override
	public void setTrialDescriptor(String trialDescriptor) {
		statusPanel.setTrialDescriptor(trialDescriptor);
	}

	@Override
	public void setTrialNumber(int trialNumber) {
		statusPanel.setTrialInfo(trialNumber, numTrials);
	}	

	@Override
	public void setTrialNumber(int trialNumber, int trialPartNumber, int numTrialParts) {
		statusPanel.setTrialInfo(trialNumber, numTrials, trialPartNumber, numTrialParts);
	}

	@Override
	public boolean isSubjectActionListenerPresent(SubjectActionListener listener) {
		return navButtonPanel.isSubjectActionListenerPresent(listener);
	}

	@Override
	public void addSubjectActionListener(SubjectActionListener listener) {
		navButtonPanel.addSubjectActionListener(listener);
	}
	
	@Override
	public void removeSubjectActionListener(SubjectActionListener listener) {
		navButtonPanel.removeSubjectActionListener(listener);		
	}
	
	/** Add a hyperlink listener to the instruction panel */
	public void addInstructionsHyperlinkListener(HyperlinkListener listener) {
		instructionPanel.addHyperlinkListener(listener);
	}
	
	/** Remove a hyperlink listener from the instructions panel */
	public void removeInstructionsHyperlinkListener(HyperlinkListener listener) {
		instructionPanel.removeHyperlinkListener(listener);	
	}	
}