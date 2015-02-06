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
package org.mitre.icarus.cps.app.widgets.phase_2.experiment;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.mitre.icarus.cps.app.util.ProbabilityUtils;
import org.mitre.icarus.cps.app.widgets.probability_entry.IProbabilityEntryContainer;
import org.mitre.icarus.cps.app.widgets.probability_entry.ProbabilityContainerLayout;
import org.mitre.icarus.cps.app.widgets.probability_entry.ProbabilityEntryComponentFactory;
import org.mitre.icarus.cps.app.widgets.probability_entry.ProbabilityEntryConstants;
import org.mitre.icarus.cps.app.widgets.probability_entry.ProbabilityEntryComponentFactory.ProbabilityEntryComponentType;
import org.mitre.icarus.cps.app.widgets.probability_entry.ProbabilitySumChangeEvent;
import org.mitre.icarus.cps.app.widgets.probability_entry.ProbabilitySumChangeListener;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.NormalizationConstraint;
import org.mitre.icarus.cps.experiment_core.gui.IConditionComponent;

/**
 * Convenience class for configuring a panel for reporting one or more probabilities.
 * 
 * @author CBONACETO
 *
 */
public class ProbabilityReportPanel extends JPanel implements IConditionComponent, ProbabilitySumChangeListener{	
	
	private static final long serialVersionUID = 7682683437930898528L;
	
	/** The component ID */
	protected String componentId;
	
	/** The layout */
	protected GridBagLayout gbl;

	/** The probability entry component type */
	protected ProbabilityEntryComponentType probabilityEntryComponentType = 
			ProbabilityEntryComponentType.Text_With_Spinners;
	
	/** Whether auto-normalization is enabled */
	protected boolean autoNormalize = false;
	
	/** The probability entry panel */
	protected IProbabilityEntryContainer probabilityEntryContainer;
	
	/** The probability container layout to use */
	protected final ProbabilityContainerLayout layout;
	
	/** An error message to display if the probabilities do not meet the normalization constraints */
	protected String normalizationErrorMessage;
	
	/** Whether to show the normalization error message */
	protected boolean normalizationErrorMessageVisible = false;
	
	/** The normalization error message label */
	protected JLabel normalizationErrorMessageLabel;	
	
	/** The current normalization constraint */
	protected NormalizationConstraint normalizationConstraint;
	
	protected Boolean normalizationConstraintMet = null;
	
	public ProbabilityReportPanel(String componentId, ProbabilityContainerLayout layout) {
		setLayout(gbl = new GridBagLayout());
		this.componentId = componentId;
		this.layout = layout;
	}

	public IProbabilityEntryContainer getProbabilityEntryContainer() {
		return probabilityEntryContainer;
	}
	
	/**
	 * @return
	 */
	public List<Integer> getCurrentSettings() {
		return probabilityEntryContainer != null ? probabilityEntryContainer.getCurrentSettings() : null;
	}
	
	/**
	 * @param currentSettings
	 */
	public void getCurrentSettings(List<Integer> currentSettings) {
		if(probabilityEntryContainer != null) {
			probabilityEntryContainer.getCurrentSettings(currentSettings);
		}
	}
	
	/**
	 * @param currentSettings
	 */
	public void setCurrentSettings(List<Integer> currentSettings) {
		if(probabilityEntryContainer != null) {
			probabilityEntryContainer.setCurrentSettings(currentSettings);
		}
	}
	
	/**
	 * 
	 */
	public void resetInteractionTimes() {
		if(probabilityEntryContainer != null) {
			probabilityEntryContainer.resetInteractionTimes();
		}
	}
	
	/**
	 * @return
	 */
	public List<Long> getInteractionTimes() {
		return probabilityEntryContainer != null ? probabilityEntryContainer.getInteractionTimes() : null;
	}
	
	/**
	 * @param interactionTimes
	 */
	public void getInteractionTimes(List<Long> interactionTimes) {
		if(probabilityEntryContainer != null) {
			probabilityEntryContainer.getInteractionTimes(interactionTimes);
		}
	}
	
	/**
	 * @param listener
	 * @return
	 */
	public boolean isSumChangeListenerPresent(ProbabilitySumChangeListener listener) {
		if(probabilityEntryContainer != null) {
			return probabilityEntryContainer.isSumChangeListenerPresent(listener);
		}
		return false;
	}
	
	/**
	 * @param listener
	 */
	public void addSumChangeListener(ProbabilitySumChangeListener listener) {
		if(probabilityEntryContainer != null) {
			probabilityEntryContainer.addSumChangeListener(listener);
		}
	}

	/**
	 * @param listener
	 */
	public void removeSumChangeListener(ProbabilitySumChangeListener listener) {
		if(probabilityEntryContainer != null) {
			probabilityEntryContainer.removeSumChangeListener(listener);
		}
	}
	
	/**
	 * @param hypotheses
	 * @param title
	 * @param showTitle
	 * @param showSum
	 * @return
	 */
	public IProbabilityEntryContainer configureProbabilityEntryContainer(Collection<String> hypotheses, 
			String title, boolean showTitle, boolean showSum, boolean normalizationErrorMessageVisible, 
			String normalizationErrorMessage, NormalizationConstraint normalizationConstraint) {		
		configureProbablityEntryContainer(hypotheses, autoNormalize, showTitle, title, showSum, 
				probabilityEntryComponentType, componentId);
		setNormalizationErrorMessageVisible(normalizationErrorMessageVisible, 
				normalizationErrorMessage, normalizationConstraint);
		return probabilityEntryContainer;
	}
	
	/**
	 * @param hypotheses
	 * @param autoNormalize
	 * @param showTitle
	 * @param showSum
	 * @param componentType
	 * @param componentId
	 * @param component
	 * @return
	 */
	protected void configureProbablityEntryContainer(Collection<String> hypotheses, 
			boolean autoNormalize, boolean showTitle, String title, boolean showSum, 
			ProbabilityEntryComponentType componentType, String componentId) {
		if(hypotheses != null && !hypotheses.isEmpty()) {
			ArrayList<String> titles = new ArrayList<String>(hypotheses.size());
			for(String hypothesis : hypotheses) {
				titles.add(hypothesis);
			}
			if(probabilityEntryContainer == null) {
				//Create a new probability entry component
				if(probabilityEntryContainer != null) {
					remove(probabilityEntryContainer.getComponent());
				}
				probabilityEntryContainer = ProbabilityEntryComponentFactory.creatProbabilityEntryComponent(componentType, 
						layout, titles, autoNormalize, showSum, showTitle, true, 
						ProbabilityEntryConstants.minPercentLabel, 
						ProbabilityEntryConstants.maxPercentLabel);				
				probabilityEntryContainer.setTopTitle(title != null ? title : "");
				probabilityEntryContainer.addSumChangeListener(this);
				add(probabilityEntryContainer.getComponent(), createProbabilityEntryContainerConstraints());
				revalidate();
			} else {
				List<String> currentTitles = probabilityEntryContainer.getProbabilityEntryTitles();				
				if(currentTitles == null || currentTitles.size() != titles.size()) {
					//We need to create a new probability entry component
					if(probabilityEntryContainer != null) {
						remove(probabilityEntryContainer.getComponent());
					}
					probabilityEntryContainer = ProbabilityEntryComponentFactory.creatProbabilityEntryComponent(componentType, 
							layout, titles, autoNormalize, showSum, showTitle, true, 
							ProbabilityEntryConstants.minPercentLabel, 
							ProbabilityEntryConstants.maxPercentLabel);
					probabilityEntryContainer.setTopTitle(title != null ? title : "");
					probabilityEntryContainer.addSumChangeListener(this);
					add(probabilityEntryContainer.getComponent(), createProbabilityEntryContainerConstraints());
					revalidate();
				}
				else {
					//We can update the existing probability entry component with the current titles
					probabilityEntryContainer.setProbabilityEntryTitles(titles);
					probabilityEntryContainer.setTopTitleVisible(showTitle);
					probabilityEntryContainer.setTopTitle(title != null ? title : "");
					probabilityEntryContainer.setSumVisible(showSum);
				}
			}			
			probabilityEntryContainer.setComponentId(componentId);			
		}
	}
	
	protected GridBagConstraints createProbabilityEntryContainerConstraints() {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.NORTH;		
		gbc.weighty = normalizationErrorMessageVisible ? 0 : 1;
		return gbc;
	}

	public String getNormalizationErrorMessage() {
		return normalizationErrorMessage;
	}

	public void setNormalizationErrorMessage(String normalizationErrorMessage) {
		this.normalizationErrorMessage = normalizationErrorMessage;
		if(normalizationErrorMessageLabel != null) {
			normalizationErrorMessageLabel.setPreferredSize(null);
			normalizationErrorMessageLabel.setText(normalizationErrorMessage);
			normalizationErrorMessageLabel.setPreferredSize(
					normalizationErrorMessageLabel.getPreferredSize());
			normalizationErrorMessageLabel.setText("");
			updateNormalizationErrorMessage();
		}
	}

	public boolean isNormalizationErrorMessageVisible() {
		return normalizationErrorMessageVisible;
	}

	public void setNormalizationErrorMessageVisible(boolean normalizationErrorMessageVisible, 
			String normalizationErrorMessage, NormalizationConstraint normalizationConstraint) {
		this.normalizationErrorMessage = normalizationErrorMessage;
		this.normalizationConstraint = normalizationConstraint;
		if(this.normalizationErrorMessageVisible != normalizationErrorMessageVisible) {
			this.normalizationErrorMessageVisible = normalizationErrorMessageVisible;
			this.normalizationErrorMessageVisible = normalizationErrorMessageVisible;			
			if(normalizationErrorMessageVisible && normalizationErrorMessageLabel == null) {
				normalizationErrorMessageLabel = new JLabel(normalizationErrorMessage);
				normalizationErrorMessageLabel.setForeground(Color.RED);
				//normalizationErrorMessageLabel.setFont(ProbabilityEntryConstants.FONT_TITLE);
				normalizationErrorMessageLabel.setPreferredSize(
						normalizationErrorMessageLabel.getPreferredSize());
				normalizationErrorMessageLabel.setText("");
				GridBagConstraints gbc = new GridBagConstraints();
				gbc.gridx = 0;	
				gbc.gridy = 1;
				gbc.insets.top = 8;
				gbc.anchor = GridBagConstraints.NORTH;
				gbc.weighty = 1;
				add(normalizationErrorMessageLabel, gbc);
			}
			if(normalizationErrorMessageLabel != null) {
				GridBagConstraints normalizationErrorMessageLabelConstraints = 
						gbl.getConstraints(normalizationErrorMessageLabel);
				GridBagConstraints probabilityEntryContainerConstraints = 
						probabilityEntryContainer != null ? 
						gbl.getConstraints(probabilityEntryContainer.getComponent()) : null;
				if(normalizationErrorMessageVisible) {
					normalizationErrorMessageLabelConstraints.weighty = 1;
					if(probabilityEntryContainerConstraints != null) {
						probabilityEntryContainerConstraints.weighty = 0;
					}
				} else {
					normalizationErrorMessageLabelConstraints.weighty = 0;
					if(probabilityEntryContainerConstraints != null) {
						probabilityEntryContainerConstraints.weighty = 1;
					}
				}
				gbl.setConstraints(normalizationErrorMessageLabel, 
						normalizationErrorMessageLabelConstraints);
				if(probabilityEntryContainerConstraints != null) {
					gbl.setConstraints(probabilityEntryContainer.getComponent(), 
							probabilityEntryContainerConstraints);
				}
				normalizationErrorMessageLabel.setVisible(normalizationErrorMessageVisible);
			}
			revalidate();
			repaint();			
		}
	}
	
	public NormalizationConstraint getNormalizationConstraint() {
		return normalizationConstraint;
	}

	public void setNormalizationConstraint(NormalizationConstraint normalizationConstraint) {
		this.normalizationConstraint = normalizationConstraint;
		updateNormalizationErrorMessage();
	}
	
	protected void updateNormalizationErrorMessage() {
		if(normalizationErrorMessageVisible && probabilityEntryContainer != null) {
			List<Integer> currentSettings = probabilityEntryContainer.getCurrentSettings();
			boolean constraintMet = true;
			if(currentSettings != null) {
				constraintMet = normalizationConstraint.isNormalizationConstraintMet(
						(double)ProbabilityUtils.computeSum(currentSettings));
				
			} 
			if(normalizationConstraintMet == null ||
					constraintMet != normalizationConstraintMet) {
				normalizationConstraintMet = constraintMet;
				normalizationErrorMessageLabel.setText(constraintMet ? "" : normalizationErrorMessage);
			}
		}
	}	
	
	@Override
	public void probabilitySumChanged(ProbabilitySumChangeEvent event) {
		updateNormalizationErrorMessage();
	}	

	@Override
	public String getComponentId() {
		return componentId;
	}

	@Override
	public void setComponentId(String id) {
		this.componentId = id;
		if(probabilityEntryContainer != null) {
			probabilityEntryContainer.setComponentId(id);
		}
	}

	@Override
	public JComponent getComponent() {
		return this;
	}	
}