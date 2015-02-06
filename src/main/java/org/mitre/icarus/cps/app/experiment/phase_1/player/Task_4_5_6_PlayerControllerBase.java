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
package org.mitre.icarus.cps.app.experiment.phase_1.player;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.ProbabilityProbeTrialPartState;
import org.mitre.icarus.cps.app.widgets.map.phase_1.MapPanelContainer;
import org.mitre.icarus.cps.app.widgets.map.phase_1.objects.AttackLocationPlacemark;
import org.mitre.icarus.cps.app.widgets.map.phase_1.objects.GroupCenterPlacemark;
import org.mitre.icarus.cps.app.widgets.map.phase_1.objects.Phase1FeaturePlacemark;
import org.mitre.icarus.cps.exam.phase_1.testing.TaskTestPhase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_4_5_6_TrialBase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_4_Trial;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_5_Trial;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.IntLayer;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.SigintLayer;
import org.mitre.icarus.cps.experiment_core.gui.INavButtonPanel.ButtonType;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupCenter;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;
import org.mitre.icarus.cps.feature_vector.phase_1.IntType;
import org.mitre.icarus.cps.feature_vector.phase_1.LocationIntelReport;
import org.mitre.icarus.cps.feature_vector.phase_1.MovintType;
import org.mitre.icarus.cps.feature_vector.phase_1.SocintOverlay;

/**
 * Abstract base class for Task 4, 5, and 6 player controllers.
 * 
 * @author CBONACETO
 *
 * @param <T>
 */
public abstract class Task_4_5_6_PlayerControllerBase<T extends TaskTestPhase<?>> extends PlayerTaskController<T> implements ActionListener {
	
	/** The group center placemarks (mapped by group type) */
	protected Map<GroupType, GroupCenterPlacemark> groupCenters;
	
	/** The attack location placemarks (mapped by location Id) */
	protected Map<String, AttackLocationPlacemark> attackLocations;
	
	/** The ground truth location placemark */
	protected AttackLocationPlacemark groundTruthPlacemark;
			
	/** All INT types in the task (use to populate legend at beginning of task) */
	protected Set<IntType> intLayersPresent = new TreeSet<IntType>();
	
	/** All SOCINT groups used in the task (use to populate legend at beginning of task) */
	protected Set<GroupType> socintGroupsPresent = new TreeSet<GroupType>();
	
	/** Whether the layers should be presented in the reverse order in the GUI */
	protected boolean reverseLayerOrder = false;
	
	/** The INT layers that have been added for the current trial */
	//protected List<IntLayer> intLayersPresentForTrial = new ArrayList<IntLayer>(4);	
	protected int currentLayerIndex = -1;
	protected int maxLayerIndex = -1;	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		examController.setNavButtonEnabled(ButtonType.Next, true);
	}
	
	/** Formats the layer shown or selected for display in the instruction text. */
	protected String formatLayerName(IntLayer layer, Task_4_5_6_TrialBase trial) {
		StringBuilder sb = new StringBuilder();
		LocationIntelReport intelReport = null;
		if(trial instanceof Task_4_Trial) {
			intelReport = ((Task_4_Trial)trial).getGroupCenter().getIntelReport();
		} else if(layer.getLayerType() != IntType.SIGINT) {			
			intelReport = ((Task_5_Trial)trial).getAttackLocation().getIntelReport();
		}
		switch(layer.getLayerType()) {
		case IMINT:
			sb.append("the <b>IMINT layer</b>, which indicates <i>");
			sb.append(intelReport.getImintInfo().toString() + " Facility</i>");
			break;
		case MOVINT:
			sb.append("the <b>MOVINT layer</b>, which indicates <i>");
			sb.append(intelReport.getMovintInfo() == MovintType.DenseTraffic ? "Dense Traffic</i>" : "Sparse Traffic</i>");
			break;
		case SIGINT:
			GroupType group = ((SigintLayer)layer).getGroup();
			intelReport = groupCenters.get(group).getFeature().getIntelReport();
			sb.append("<b>SIGINT on Group " + group);
			sb.append("</b>, which indicates <i>");
			sb.append(intelReport.getSigintInfo().toString() + "</i>");
			break;
		case SOCINT: 
			sb.append("the <b>SOCINT layer</b>, which shows the attack regions for each group");
			break;
		default:
			break;
		}
		return sb.toString();
	}
	
	/**  */
	protected void resetIntLayersForTrial() {		
		//intLayersPresentForTrial.clear();
		currentLayerIndex = -1;
		maxLayerIndex = -1;
	}
	
	/**  */
	protected void showIntLayers(int currentLayerIndex, List<? extends ProbabilityProbeTrialPartState> intLayers, Task_4_5_6_TrialBase trial) {		
		if(this.currentLayerIndex != currentLayerIndex) {			
			MapPanelContainer mapPanel = conditionPanel.getMapPanel();			
			if(currentLayerIndex < this.currentLayerIndex) {
				//Hide layers that were previously shown since they are no longer visible
				for(int i = currentLayerIndex + 1; i <= this.currentLayerIndex; i++) {
					IntLayer layer = intLayers.get(i).getLayerToAdd();
					switch(layer.getLayerType()) {
					case SOCINT:
						mapPanel.setSocintRegionsLayerEnabled(false);
						//mapPanel.setSocintLegendItemVisible(false);	
						break;
					case IMINT:
						mapPanel.setImintLayerEnabled(false);
						//mapPanel.setImintLegendItemVisible(false);
						break;
					case MOVINT:
						mapPanel.setMovintLayerEnabled(false);
						//mapPanel.setMovintLegendItemVisible(false);
						break;
					case SIGINT:
						mapPanel.setSigintLayerEnabled(false);				
						//mapPanel.setSigintLegendItemVisible(false);
						break;
					default:
						break;
					}					
				}
			} else {			
				for(int i=0; i <= currentLayerIndex; i++) {
					IntLayer layer = intLayers.get(i).getLayerToAdd();
					if(i > maxLayerIndex) {
						//This is a new layer that must be added										
						if(layer.getLayerType() == IntType.SOCINT) {
							addRegions(trial.getRegionsOverlay(), false, true);
						} else {
							if(layer.getLayerType() == IntType.SIGINT) {
								addINTLayer(layer, true, false);
							} else {
								addINTLayer(layer, false, true);
							}
						}

					} else {
						//Just make the layer visible, it was added previously
						switch(layer.getLayerType()) {
						case SOCINT:
							mapPanel.setSocintRegionsLayerEnabled(true);
							mapPanel.setSocintLegendItemVisible(true);	
							break;
						case IMINT:
							mapPanel.setImintLayerEnabled(true);
							mapPanel.setImintLegendItemVisible(true);
							break;
						case MOVINT:
							mapPanel.setMovintLayerEnabled(true);
							mapPanel.setMovintLegendItemVisible(true);
							break;
						case SIGINT:
							mapPanel.setSigintLayerEnabled(true);				
							mapPanel.setSigintLegendItemVisible(true);
							break;
						default:
							break;
						}
					}			
				}
			}
			this.currentLayerIndex = currentLayerIndex;
			if(currentLayerIndex > maxLayerIndex) {
				maxLayerIndex = currentLayerIndex;
			}
			mapPanel.redrawMap();
		}		
	}	
	
	/**
	 * Add SOCINT regions to the map.
	 * 
	 * @param regions
	 */
	private void addRegions(SocintOverlay regionsOverlay, boolean addToGroupCenters, boolean addToAttackLocations) {
		if(regionsOverlay != null) {
			MapPanelContainer mapPanel = conditionPanel.getMapPanel();
			mapPanel.setSocintRegionsOverlay(regionsOverlay);
			mapPanel.setSocintRegionsLayerEnabled(true);
			mapPanel.setSocintLegendItemVisible(true);			
			if(addToGroupCenters && groupCenters != null) {
				for(Phase1FeaturePlacemark<?> placemark : groupCenters.values()) {
					mapPanel.addIntToFeaturePlacemark(placemark, IntType.SOCINT, false);
				}
			}
			if(addToAttackLocations && attackLocations != null) {
				for(Phase1FeaturePlacemark<?> placemark : attackLocations.values()) {
					mapPanel.addIntToFeaturePlacemark(placemark, IntType.SOCINT, false);
				}
			}
			mapPanel.redrawMap();
		}		
		//Show the rule for the layer (if any)
		//conditionPanel.getMapPanel().showSocintRegionsLayerInstructions();	
	}
	
	/**
	 * Add the IMINT, MOVINT, or SIGINT layer to the map.
	 * 
	 * @param layer
	 */
	private void addINTLayer(IntLayer layer, boolean addToGroupCenters, boolean addToAttackLocations) {
		if(layer != null) {
			MapPanelContainer mapPanel = conditionPanel.getMapPanel();
			switch(layer.getLayerType()) {
			case IMINT:
				mapPanel.setImintLayerEnabled(true);
				mapPanel.setImintLegendItemVisible(true);
				if(addToGroupCenters && groupCenters != null) {
					for(Phase1FeaturePlacemark<?> placemark : groupCenters.values()) {
						mapPanel.addIntToFeaturePlacemark(placemark, IntType.IMINT, false);
					}
				}
				if(addToAttackLocations && attackLocations != null) {
					for(Phase1FeaturePlacemark<?> placemark : attackLocations.values()) {
						mapPanel.addIntToFeaturePlacemark(placemark, IntType.IMINT, false);
					}
				}				
				//Show the rule for the layer (if any)
				/*SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						conditionPanel.getMapPanel().showImintLayerInstructions();
					}
				});*/				
				break;
			case MOVINT:
				mapPanel.setMovintLayerEnabled(true);
				mapPanel.setMovintLegendItemVisible(true);
				if(addToGroupCenters && groupCenters != null) {
					for(Phase1FeaturePlacemark<?> placemark : groupCenters.values()) {
						mapPanel.addIntToFeaturePlacemark(placemark, IntType.MOVINT, false);
					}
				}
				if(addToAttackLocations && attackLocations != null) {
					for(Phase1FeaturePlacemark<?> placemark : attackLocations.values()) {
						mapPanel.addIntToFeaturePlacemark(placemark, IntType.MOVINT, false);
					}
				}				
				//Show the rule for the layer (if any)
				/*SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						conditionPanel.getMapPanel().showMovintLayerInstructions();
					}
				});*/				
				break;
			case SIGINT:				
				mapPanel.setSigintLayerEnabled(true);				
				mapPanel.setSigintLegendItemVisible(true);
				if(addToGroupCenters && groupCenters != null) {
					SigintLayer sigintLayer = (SigintLayer)layer;
					if(sigintLayer.getGroup() != null) {
						GroupCenterPlacemark placemark = groupCenters.get(sigintLayer.getGroup());
						if(placemark != null) {
							mapPanel.addIntToFeaturePlacemark(placemark, IntType.SIGINT, false);
						}
					}
				}				
				//Show the rule for the layer (if any)
				/*SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						conditionPanel.getMapPanel().showSigintLayerInstructions();
					}
				});*/				
				break;
			default:
				break;
			}
			mapPanel.redrawMap();
		}
	}
	
	protected void addIntLayersToLegend(Collection<IntType> intLayers, boolean reverseLayers) {
		if(intLayers != null && !intLayers.isEmpty()) {
			if(reverseLayers) {
				LinkedList<IntType> reversedIntLayers = new LinkedList<IntType>(intLayers);
				Collections.reverse(reversedIntLayers);
				intLayers = reversedIntLayers;
			}
			MapPanelContainer mapPanel = conditionPanel.getMapPanel();
			for(IntType intLayer : intLayers) {
				switch(intLayer) {
				case IMINT:
					mapPanel.setImintLegendItemVisible(true);
					break;
				case MOVINT:
					mapPanel.setMovintLegendItemVisible(true);
					break;
				case SIGINT:
					mapPanel.setSigintLegendItemVisible(true);
					break;
				case SOCINT:
					mapPanel.setSocintLegendItemVisible(true);
					mapPanel.setSocintGroupsForLegend(socintGroupsPresent);
					break;
				default: 
					break;
				}
			}
		}
	}
	
	protected List<GroupType> getGroupCenterGroups(List<GroupCenter> groupCenters) {
		ArrayList<GroupType> groups = null;
		if(groupCenters != null && !groupCenters.isEmpty()) {
			groups = new ArrayList<GroupType>(groupCenters.size());
			for(GroupCenter groupCenter : groupCenters) {
				groups.add(groupCenter.getGroup());
			}
		}
		return groups;
	}
}