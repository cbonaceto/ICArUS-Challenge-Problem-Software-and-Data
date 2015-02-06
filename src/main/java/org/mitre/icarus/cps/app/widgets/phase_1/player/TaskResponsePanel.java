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
package org.mitre.icarus.cps.app.widgets.phase_1.player;

import java.awt.Color;
import java.util.Collection;
import java.util.List;

import javax.swing.JPanel;

import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.app.widgets.phase_1.SurpriseEntryPanel;
import org.mitre.icarus.cps.app.widgets.phase_1.experiment.TaskInputPanel;
import org.mitre.icarus.cps.app.widgets.probability_entry.IProbabilityEntryContainer;
import org.mitre.icarus.cps.app.widgets.probability_entry.ProbabilityEntryComponentFactory.ProbabilityEntryComponentType;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;

/**
 * @author CBONACETO
 *
 */
public class TaskResponsePanel extends TaskInputPanel {	
	private static final long serialVersionUID = 1L;
	
	/** Participant type label (default is "participant") */
	protected String participantLabel = "participant";
	
	/** The normative probs probability entry component type */
	protected ProbabilityEntryComponentType normativeProbsComponentType;
	
	/** The average human probability entry component type */
	protected ProbabilityEntryComponentType avgHumanProbsComponentType;
	
	/** The normative troop allocation component type */
	protected ProbabilityEntryComponentType normativeAllocationComponentType;
	
	/** The average human troop allocation component type */
	protected ProbabilityEntryComponentType avgHumanAllocationComponentType;	
	
	/** The normative probs component */
	protected IProbabilityEntryContainer normativeProbsComponent;
	
	/** The average human probs component */
	protected IProbabilityEntryContainer avgHumanProbsComponent;

	/** The normative troop allocation component */
	protected IProbabilityEntryContainer normativeTroopAllocationComponent;
	
	/** The average human troop allocation component */
	protected IProbabilityEntryContainer avgHumanTroopAllocationComponent;
	
	/** The average human surprise probe component */
	protected SurpriseEntryPanel avgHumanSurpriseEntryComponent;	

	public TaskResponsePanel(int numSubPanels) {
		super(numSubPanels);	
		previousSettingsComponentType = ProbabilityEntryComponentType.Text;
		previousTroopAllocationComponentType = ProbabilityEntryComponentType.Text;
		normativeProbsComponentType = ProbabilityEntryComponentType.Text;		
		avgHumanProbsComponentType = ProbabilityEntryComponentType.Text;
		normativeAllocationComponentType = ProbabilityEntryComponentType.Text;
		avgHumanAllocationComponentType = ProbabilityEntryComponentType.Text;	
	}

	public String getParticipantLabel() {
		return participantLabel;
	}

	public void setParticipantLabel(String participantLabel) {
		this.participantLabel = participantLabel;
	}

	public ProbabilityEntryComponentType getNormativeProbsComponentType() {
		return normativeProbsComponentType;
	}

	public void setNormativeProbsComponentType(ProbabilityEntryComponentType normativeProbsComponentType) {
		this.normativeProbsComponentType = normativeProbsComponentType;
	}

	public ProbabilityEntryComponentType getAvgHumanProbsComponentType() {
		return avgHumanProbsComponentType;
	}

	public void setAvgHumanProbsComponentType(ProbabilityEntryComponentType avgHumanProbsComponentType) {
		this.avgHumanProbsComponentType = avgHumanProbsComponentType;
	}

	public ProbabilityEntryComponentType getNormativeAllocationComponentType() {
		return normativeAllocationComponentType;
	}

	public void setNormativeAllocationComponentType(ProbabilityEntryComponentType normativeAllocationComponentType) {
		this.normativeAllocationComponentType = normativeAllocationComponentType;
	}

	public ProbabilityEntryComponentType getAvgHumanAllocationComponentType() {
		return avgHumanAllocationComponentType;
	}

	public void setAvgHumanAllocationComponentType(ProbabilityEntryComponentType avgHumanAllocationComponentType) {
		this.avgHumanAllocationComponentType = avgHumanAllocationComponentType;
	}

	public IProbabilityEntryContainer getNormativeProbsComponent() {
		return normativeProbsComponent;
	}

	public void setNormativeProbsComponent(IProbabilityEntryContainer normativeProbsComponent) {
		if(normativeProbsComponent != null && normativeProbsComponent != this.normativeProbsComponent) {
			normativeProbsComponent.setComponentId("np-trp");			
			components.put(normativeProbsComponent.getComponentId(), normativeProbsComponent);
			JPanel containingPanel = getContainingPanelForComponent(this.normativeProbsComponent);
			this.normativeProbsComponent = normativeProbsComponent;
			if(containingPanel != null) {
				containingPanel.removeAll();
				containingPanel.add(normativeProbsComponent.getComponent());
				revalidate();
			}
		}
	}
	
	public IProbabilityEntryContainer setGroupsForNormativeProbsComponent(Collection<GroupType> groups) {
		normativeProbsComponent = setGroupsForProbabilityEntryComponent(groups, normativeProbsComponent, "np-trp", 
				normativeProbsComponentType, false, false);
		if(normativeProbsComponent != null) {
			normativeProbsComponent.setTopTitle("normative probabilities");	
			normativeProbsComponent.showConfirmedProbabilities();
			normativeProbsComponent.setSumVisible(false);
		}
		return normativeProbsComponent;
	}
	
	public IProbabilityEntryContainer setLocationsForNormativeProbsComponent(List<String> locations) {
		normativeProbsComponent = setLocationsForProbabilityEntryComponent(locations, normativeProbsComponent, "np-trp", 
				normativeProbsComponentType, false, false);
		if(normativeProbsComponent != null) {
			normativeProbsComponent.setTopTitle("normative probabilities");
			normativeProbsComponent.showConfirmedProbabilities();
			normativeProbsComponent.setSumVisible(false);
			for(int index = 0; index < locations.size(); index++) {
				normativeProbsComponent.setProbabilityEntryTitleColor(index, Color.BLACK);
				if(normativeProbsComponent.getProbabilityEntryTitleIcon(index) != null) {
					normativeProbsComponent.setProbabilityEntryTitleIcon(index, null);
				}
			}			
			if(WidgetConstants.USE_GROUP_COLORS) {
				normativeProbsComponent.restoreDefaultProbabilityEntryColors();
			}
		}
		return normativeProbsComponent;
	}	

	public IProbabilityEntryContainer getAvgHumanProbsComponent() {
		return avgHumanProbsComponent;
	}

	public void setAvgHumanProbsComponent(IProbabilityEntryContainer avgHumanProbsComponent) {
		if(avgHumanProbsComponent != null && avgHumanProbsComponent != this.avgHumanProbsComponent) {
			avgHumanProbsComponent.setComponentId("ahp-trp");			
			components.put(avgHumanProbsComponent.getComponentId(), avgHumanProbsComponent);
			JPanel containingPanel = getContainingPanelForComponent(this.avgHumanProbsComponent);
			this.avgHumanProbsComponent = avgHumanProbsComponent;
			if(containingPanel != null) {
				containingPanel.removeAll();
				containingPanel.add(avgHumanProbsComponent.getComponent());
				revalidate();
			}
		}
	}
	
	public IProbabilityEntryContainer setGroupsForAvgHumanProbsComponent(Collection<GroupType> groups) {
		avgHumanProbsComponent = setGroupsForProbabilityEntryComponent(groups, avgHumanProbsComponent, "ahp-trp", 
				avgHumanProbsComponentType, false, false);
		if(avgHumanProbsComponent != null) {
			avgHumanProbsComponent.setTopTitle("human probabilities");
			avgHumanProbsComponent.showConfirmedProbabilities();
			avgHumanProbsComponent.setSumVisible(false);
		}
		return avgHumanProbsComponent;
	}
	
	public IProbabilityEntryContainer setLocationsForAvgHumanProbsComponent(List<String> locations) {
		avgHumanProbsComponent = setLocationsForProbabilityEntryComponent(locations, avgHumanProbsComponent, "ahp-trp", 
				normativeProbsComponentType, false, false);
		if(avgHumanProbsComponent != null) {
			avgHumanProbsComponent.setTopTitle("human probabilities");
			avgHumanProbsComponent.showConfirmedProbabilities();
			avgHumanProbsComponent.setSumVisible(false);
			for(int index = 0; index < locations.size(); index++) {				
				avgHumanProbsComponent.setProbabilityEntryTitleColor(index, Color.BLACK);
				if(avgHumanProbsComponent.getProbabilityEntryTitleIcon(index) != null) {
					avgHumanProbsComponent.setProbabilityEntryTitleIcon(index, null);
				}
			}			
			if(WidgetConstants.USE_GROUP_COLORS) {
				avgHumanProbsComponent.restoreDefaultProbabilityEntryColors();
			}
		}
		return avgHumanProbsComponent;
	}

	public IProbabilityEntryContainer getNormativeTroopAllocationComponent() {
		return normativeTroopAllocationComponent;
	}

	public void setNormativeTroopAllocationComponent(IProbabilityEntryContainer normativeTroopAllocationComponent) {
		if(normativeTroopAllocationComponent != null && normativeTroopAllocationComponent != this.normativeTroopAllocationComponent) {
			normativeTroopAllocationComponent.setComponentId("nta-trp");			
			components.put(normativeTroopAllocationComponent.getComponentId(), normativeTroopAllocationComponent);
			JPanel containingPanel = getContainingPanelForComponent(this.normativeTroopAllocationComponent);
			this.normativeTroopAllocationComponent = normativeTroopAllocationComponent;
			if(containingPanel != null) {
				containingPanel.removeAll();
				containingPanel.add(normativeTroopAllocationComponent.getComponent());
				revalidate();
			}
		}
	}
	
	public IProbabilityEntryContainer setGroupsForNormativeTroopAllocationComponent(Collection<GroupType> groups) {
		normativeTroopAllocationComponent = setGroupsForProbabilityEntryComponent(groups, normativeTroopAllocationComponent, "nta-trp", 
				normativeAllocationComponentType, false, true);		
		if(normativeTroopAllocationComponent != null) {
			normativeTroopAllocationComponent.setTopTitle("normative troop allocations");
			normativeTroopAllocationComponent.showConfirmedProbabilities();
			normativeTroopAllocationComponent.setSumVisible(false);
		}
		return normativeTroopAllocationComponent;
	}
	
	public IProbabilityEntryContainer setLocationsForNormativeTroopAllocationComponent(List<String> locations) {
		normativeTroopAllocationComponent = setLocationsForProbabilityEntryComponent(locations, normativeTroopAllocationComponent, "nta-trp", 
				normativeAllocationComponentType, false, true);		
		if(normativeTroopAllocationComponent != null) {
			normativeTroopAllocationComponent.setTopTitle("normative troop allocations");
			normativeTroopAllocationComponent.showConfirmedProbabilities();
			normativeTroopAllocationComponent.setSumVisible(false);
			for(int index = 0; index < locations.size(); index++) {
				normativeTroopAllocationComponent.setProbabilityEntryTitleColor(index, Color.BLACK);
				if(normativeTroopAllocationComponent.getProbabilityEntryTitleIcon(index) != null) {
					normativeTroopAllocationComponent.setProbabilityEntryTitleIcon(index, null);
				}
			}
			if(WidgetConstants.USE_GROUP_COLORS) {
				normativeTroopAllocationComponent.restoreDefaultProbabilityEntryColors();
			}
		}		
		return normativeTroopAllocationComponent;
	}

	public IProbabilityEntryContainer getAvgHumanTroopAllocationComponent() {
		return avgHumanTroopAllocationComponent;
	}

	public void setAvgHumanTroopAllocationComponent(IProbabilityEntryContainer avgHumanTroopAllocationComponent) {
		if(avgHumanTroopAllocationComponent != null && avgHumanTroopAllocationComponent != this.avgHumanTroopAllocationComponent) {
			avgHumanTroopAllocationComponent.setComponentId("ahta-trp");			
			components.put(avgHumanTroopAllocationComponent.getComponentId(), avgHumanTroopAllocationComponent);
			JPanel containingPanel = getContainingPanelForComponent(this.avgHumanTroopAllocationComponent);
			this.avgHumanTroopAllocationComponent = avgHumanTroopAllocationComponent;
			if(containingPanel != null) {
				containingPanel.removeAll();
				containingPanel.add(avgHumanTroopAllocationComponent.getComponent());
				revalidate();
			}
		}
	}
	
	public IProbabilityEntryContainer setGroupsForAvgHumanTroopAllocationComponent(Collection<GroupType> groups) {
		avgHumanTroopAllocationComponent = setGroupsForProbabilityEntryComponent(groups, avgHumanTroopAllocationComponent, "ahta-trp", 
				avgHumanAllocationComponentType, false, true);		
		if(avgHumanTroopAllocationComponent != null) {
			avgHumanTroopAllocationComponent.setTopTitle("human troop allocations");
			avgHumanTroopAllocationComponent.showConfirmedProbabilities();
			avgHumanTroopAllocationComponent.setSumVisible(false);
		}
		return avgHumanTroopAllocationComponent;
	}
	
	public IProbabilityEntryContainer setLocationsForAvgHumanTroopAllocationComponent(List<String> locations) {
		avgHumanTroopAllocationComponent = setLocationsForProbabilityEntryComponent(locations, avgHumanTroopAllocationComponent, "ahta-trp", 
				avgHumanAllocationComponentType, false, true);		
		if(avgHumanTroopAllocationComponent != null) {
			avgHumanTroopAllocationComponent.setTopTitle("human troop allocations");
			avgHumanTroopAllocationComponent.showConfirmedProbabilities();
			avgHumanTroopAllocationComponent.setSumVisible(false);
			for(int index = 0; index < locations.size(); index++) {
				avgHumanTroopAllocationComponent.setProbabilityEntryTitleColor(index, Color.BLACK);
				if(avgHumanTroopAllocationComponent.getProbabilityEntryTitleIcon(index) != null) {
					avgHumanTroopAllocationComponent.setProbabilityEntryTitleIcon(index, null);
				}
			}
			if(WidgetConstants.USE_GROUP_COLORS) {
				avgHumanTroopAllocationComponent.restoreDefaultProbabilityEntryColors();
			}
		}		
		return avgHumanTroopAllocationComponent;
	}

	public SurpriseEntryPanel getAvgHumanSurpriseEntryComponent() {
		return avgHumanSurpriseEntryComponent;
	}

	public void setAvgHumanSurpriseEntryComponent(SurpriseEntryPanel avgHumanSurpriseEntryComponent) {
		if(avgHumanSurpriseEntryComponent != null && avgHumanSurpriseEntryComponent != this.avgHumanSurpriseEntryComponent) {
			avgHumanSurpriseEntryComponent.setComponentId("ahse-trp");
			components.put(avgHumanSurpriseEntryComponent.getComponentId(), avgHumanSurpriseEntryComponent);
			JPanel containingPanel = getContainingPanelForComponent(this.avgHumanSurpriseEntryComponent);
			this.avgHumanSurpriseEntryComponent = avgHumanSurpriseEntryComponent;
			if(containingPanel != null) {
				containingPanel.removeAll();
				containingPanel.add(avgHumanSurpriseEntryComponent.getComponent());
				revalidate();
			}
		}
	}	
	
	public SurpriseEntryPanel configureAvgHumanSurpriseEntryComponent(int minSurprise, int maxSurprise, int surpriseIncrement) {		
		if(avgHumanSurpriseEntryComponent == null) {
			avgHumanSurpriseEntryComponent = new SurpriseEntryPanel(minSurprise, maxSurprise, surpriseIncrement);
		}
		else if(avgHumanSurpriseEntryComponent.getMinSurprise() != minSurprise || 
				avgHumanSurpriseEntryComponent.getMaxSurprise() != maxSurprise ||
						avgHumanSurpriseEntryComponent.getSurpriseIncrement() != surpriseIncrement) {
			//We need to create a new surprise entry component
			JPanel containingPanel = getContainingPanelForComponent(avgHumanSurpriseEntryComponent);
			avgHumanSurpriseEntryComponent = new SurpriseEntryPanel(minSurprise, maxSurprise, surpriseIncrement);
			if(containingPanel != null) {
				//Add the surprise entry component back to its sub panel
				containingPanel.removeAll();
				containingPanel.add(avgHumanSurpriseEntryComponent);
				revalidate();
			}
		}
		avgHumanSurpriseEntryComponent.clearSelection();
		avgHumanSurpriseEntryComponent.setEnabled(false);
		
		
		
		
		avgHumanSurpriseEntryComponent.setComponentId("ahse-trp");
		components.put(avgHumanSurpriseEntryComponent.getComponentId(), avgHumanSurpriseEntryComponent);		
		return avgHumanSurpriseEntryComponent;
	}
}