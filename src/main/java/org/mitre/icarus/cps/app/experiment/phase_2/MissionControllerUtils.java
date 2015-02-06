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
package org.mitre.icarus.cps.app.experiment.phase_2;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.mitre.icarus.cps.app.experiment.phase_2.trial_states.AttackProbabilityReportTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_2.trial_states.BlueActionSelectionOrPresentationTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_2.trial_states.IntPresentationTrialPartState;
import org.mitre.icarus.cps.app.widgets.phase_2.experiment.datum_list.DatumListItem;
import org.mitre.icarus.cps.app.widgets.phase_2.experiment.datum_list.DatumListItemType;
import org.mitre.icarus.cps.exam.phase_2.testing.DatumIdentifier;
import org.mitre.icarus.cps.exam.phase_2.testing.DatumType;
import org.mitre.icarus.cps.exam.phase_2.testing.IcarusTestTrial_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.AttackProbability;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.AttackProbabilityReportProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.BlueAction;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.BlueAction.BlueActionType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.Probability;
import org.mitre.icarus.cps.feature_vector.phase_2.int_datum.HumintDatum;
import org.mitre.icarus.cps.feature_vector.phase_2.int_datum.ImintDatum;
import org.mitre.icarus.cps.feature_vector.phase_2.int_datum.IntDatum;
import org.mitre.icarus.cps.feature_vector.phase_2.int_datum.OsintDatum;
import org.mitre.icarus.cps.feature_vector.phase_2.int_datum.SigintDatum;
import org.mitre.icarus.cps.feature_vector.phase_2.int_datum.SigintDatum.SigintType;

/**
 * @author CBONACETO
 *
 */
public class MissionControllerUtils {
	
	private MissionControllerUtils() {}

	/**
	 * @param intDatum
	 * @return
	 */
	public static String createIntDatumValueString(IntDatum intDatum) {
		if(intDatum != null) {
			switch(intDatum.getDatumType()) {
			case HUMINT:
				HumintDatum humint = (HumintDatum)intDatum;
				return createProbabilityString(humint.getRedCapability_Pc(), false);
			case IMINT:
				ImintDatum imint = (ImintDatum)intDatum;
				return imint.getRedOpportunity_U() != null ? "U = " + imint.getRedOpportunity_U().toString() : "";
			case OSINT:
				OsintDatum osint = (OsintDatum)intDatum;
				return "P = " + createProbabilityString(osint.getRedVulnerability_P(), false);
			case SIGINT:
				SigintDatum sigint = (SigintDatum)intDatum;
				return sigint.isRedActivityDetected() != null && sigint.isRedActivityDetected() ? SigintType.Chatter.toString() : 
					SigintType.Silence.toString();
			default:
				return "";
			}
		}
		return "";
	}

	/**
	 * @param locationIndex
	 * @param numLocations
	 * @return
	 */
	public static String createBlueLocationMapDisplayName(int locationIndex, int numLocations) {
		return numLocations > 1 ? Integer.toString(locationIndex+1) : "";
	}

	/**
	 * @param locationIndex
	 * @param numLocations
	 * @return
	 */
	public static String createBlueLocationProbabilityReportDisplayName(int locationIndex, int numLocations) {
		return numLocations > 1 ? "Location " + Integer.toString(locationIndex+1) : "location";
	}

	/**
	 * @param probability
	 * @return
	 */
	public static String createProbabilityString(Probability probability) {
		return probability != null && probability.getProbability() != null ?
				Integer.toString(probability.getProbability().intValue()) + "%" : "";
	}

	/**
	 * @param probability
	 * @return
	 */
	public static String createProbabilityString(Double probability, boolean isPercentProbability) {
		return probability != null ? Integer.toString(
				isPercentProbability ? probability.intValue() : (int)(probability*100)) + "%" : "";
	}

	/**
	 * @param probability
	 * @return
	 */
	public static String createProbabilityString(Integer probability) {
		return probability != null ? probability.toString() + "%" : "";
	}

	/**
	 * @param numLocations
	 * @return
	 */
	public static List<String> createBlueLocationMapDisplayNames(int numLocations) {
		List<String> displayNames = new LinkedList<String>();
		for(int i=0; i<numLocations; i++) {
			displayNames.add(Integer.toString(i+1));
		}
		return displayNames;
	}

	/**
	 * @param instructions
	 * @param numLocations
	 * @param locationIndex
	 * @param showingAllLocationsAtOnce
	 * @return
	 */
	public static StringBuilder createBlueLocationNamesForInstructions(StringBuilder instructions, int numLocations, int locationIndex,
			boolean showingAllLocationsAtOnce) {
		if(instructions == null) {instructions = new StringBuilder();}
		if(numLocations > 1) {
			if(showingAllLocationsAtOnce) {
				instructions.append("each location");
				//instructions.append("each location.");
			} else {
				instructions.append(createBlueLocationProbabilityReportDisplayName(locationIndex, numLocations));
				//instructions.append(".");
			}
		} else {
			instructions.append("the ");
			instructions.append(createBlueLocationProbabilityReportDisplayName(locationIndex, numLocations));
			instructions.append(" shown");
			//instructions.append(".");
		}
		return instructions;
	}
	
	/**
	 * @param trial
	 * @param datumList
	 * @param sigintPresentationLocations
	 * @param numLocations
	 * @return
	 */
	public static ProbabilityReportDatumList createAttackProbabilityReportDatumList(
			IcarusTestTrial_Phase2 trial, List<DatumIdentifier> datumList, 
			IntPresentationLocations intPresentationLocations, int numLocations) {
		if(trial != null && datumList != null && !datumList.isEmpty()) {
			List<DatumListItem> datumItems = new ArrayList<DatumListItem>();			
			ArrayList<String> datumValues = new ArrayList<String>();
			String currentLocationId = null;
			int locationIndex = 0;
			boolean considerBlueBook = false;
			boolean considerSigint = false;
			boolean considerSigintReliability = false;
			boolean considerBatchPlots = false;
			for(DatumIdentifier datum : datumList) {
				if(numLocations == 1) {
					if(currentLocationId == null) {
						datumItems.add(new DatumListItem("Consider:", null, Color.black, false, true));
						datumValues.add(null);
						currentLocationId = "0";
					}
				} else if(datum.getLocationId() != null && !datum.getLocationId().equals(currentLocationId)){
					datumItems.add(new DatumListItem("At " + createBlueLocationProbabilityReportDisplayName(locationIndex, numLocations)
							+ ", Consider:", null, Color.black, false, true));
					datumValues.add(null);
					currentLocationId = datum.getLocationId();
					locationIndex++;
				}
				switch(datum.getDatumType()) {
				case AttackProbabilityReport_Activity: 
				case AttackProbabilityReport_Activity_Capability_Propensity:
				case AttackProbabilityReport_Capability_Propensity: 
				case AttackProbabilityReport_Propensity:
					AttackProbabilityReportProbe probe = trial.getAttackProbabilityReportProbe(datum);
					AttackProbability probability = probe != null ? probe.getProbabilityAtLocation(datum.getLocationId()) : null;
					if(probe != null) {
						datumItems.add(DatumListItemType.getDatumListItemType(datum.getDatumType()).getDatumListItem());
						datumValues.add(createProbabilityString(probability));
					}
					break;
				case BatchPlots:
					considerBatchPlots = true;
					break;
				case BlueBook:
					considerBlueBook = true;
					break;
				case HUMINT:
					datumItems.add(DatumListItemType.HUMINT.getDatumListItem());
					HumintDatum humint = trial.getHumint(datum.getLocationId());
					datumValues.add(createIntDatumValueString(humint));
					break;
				case IMINT:
					datumItems.add(DatumListItemType.IMINT.getDatumListItem());
					ImintDatum imint = trial.getImint(datum.getLocationId());
					datumValues.add(createIntDatumValueString(imint));
					break;
				case OSINT:
					datumItems.add(DatumListItemType.OSINT.getDatumListItem());
					OsintDatum osint = trial.getOsint(datum.getLocationId());
					datumValues.add(createIntDatumValueString(osint));
					break;
				case PayoffMatrix:
					break;
				case SIGINT:
					considerSigint = true;
					datumItems.add(DatumListItemType.SIGINT.getDatumListItem());
					if(intPresentationLocations.isIntPresented(DatumType.SIGINT, datum.getLocationId())) {
						SigintDatum sigint = trial.getSigint(datum.getLocationId());
						datumValues.add(createIntDatumValueString(sigint));
					} else {
						datumValues.add("No Signal");
					}
					break;
				case SIGINTReliability:
					considerSigintReliability = true;
					break;
				default:
					break;
				}
			}
			return new ProbabilityReportDatumList(datumItems, datumValues, considerBlueBook, 
					considerSigint, considerSigintReliability, considerBatchPlots);
		}
		return null;
	}
	
	/**
	 * @param instructions
	 * @param trialPartState
	 * @param totalNumLocations
	 * @param locationIndex
	 * @param locationId
	 * @param showingAllLocationsAtOnce
	 * @return
	 */
	public static StringBuilder createINTPresentationInstructions(StringBuilder instructions, IntPresentationTrialPartState<?> trialPartState,		
		 int totalNumLocations, int locationIndex, String locationId, boolean showingAllLocationsAtOnce) {
		if(instructions == null) {instructions = new StringBuilder();}
		switch(trialPartState.getIntDatumType()) {
		case OSINT:
			if(ExperimentConstants_Phase2.SHOW_BEGIN_TRIAL_PART_STATE) {
				instructions.append("OSINT has reported the probability (P) that Blue will win (Red will lose) a showdown, if a showdown occurs at ");
				instructions.append(totalNumLocations > 1 ? "each location." : "the location.");
			} else {
				instructions.append("OSINT has reported ");
				instructions.append(totalNumLocations > 1 ? 
						Integer.toString(totalNumLocations) + " Blue locations " : "a Blue location ");
				instructions.append("and the probability (P) that Blue will win (Red will lose) a showdown, if a showdown occurs at ");
				instructions.append(totalNumLocations > 1 ? "each location." : "the location.");								
			}
			/*if(ExperimentConstants_Phase2.SHOW_BEGIN_TRIAL_PART_STATE) {
				instructions.append("OSINT has reported the probability (P) that Blue will win (Red will lose) a showdown, if a showdown occurs, at ");
				instructions.append(totalNumLocations > 1 ? "each location." : "the location.");
			} else {
				instructions.append("A new trial has begun. OSINT has reported ");
				instructions.append(totalNumLocations > 1 ? 
						Integer.toString(totalNumLocations) + " Blue locations " : "a Blue location ");
				instructions.append("and the probability (P) that Blue will win (Red will lose) a showdown, if a showdown occurs at ");
				instructions.append(totalNumLocations > 1 ? "each location." : "the location.");								
			}*/
			break;
		case IMINT:			
			instructions.append("IMINT has reported the utility (U) at ");
			instructions.append(totalNumLocations > 1 ? "each location." : "the location.");
			instructions.append(" U is the number of points to be won or lost in a showdown, if a showdown occurs.");
			break;
		case HUMINT:
			instructions.append("HUMINT has reported the probability that Red is capable of attacking on this trial");
			instructions.append(totalNumLocations > 1 ? ", which applies at each location." : ".");			
			break;		
		case SIGINT:
			instructions.append("SIGINT has reported ");
			IntDatum sigintDatum = trialPartState.getIntDatum(locationId);
			if(sigintDatum != null && sigintDatum instanceof SigintDatum) {
				instructions.append("<b>" + ((SigintDatum)sigintDatum).getSigintType() + "</b>.");
			} else {
				instructions.append("Red's activity.");
			}
			break;
		default: break;
		}
		return instructions;
	}
	
	/**
	 * @param instructions
	 * @param trialPartState
	 * @param numLocations
	 * @param locationIndex
	 * @param locationId
	 * @param showingAllLocationsAtOnce
	 * @return
	 */
	public static StringBuilder createProbabilityReportInstructions(StringBuilder instructions, 
			AttackProbabilityReportTrialPartState trialPartState, String probabilityTitle, 
			int numLocations, int locationIndex, String locationId, 
			boolean showingAllLocationsAtOnce) {
		if(instructions == null) {instructions = new StringBuilder();}
		instructions.append("Please report ");
		instructions.append(probabilityTitle);
		instructions.append(numLocations > 1 ? " at each location." : ".");
		instructions.append(" This is the probability ");		
		switch(trialPartState.getTrialPartType()) {
		case AttackProbabilityReport_Propensity:
			instructions.append("of Red attack given IMINT (U) and OSINT (P).");
			/*instructions.append(" given Red\'s vulnerability, the utility of the ");
			instructions.append(numLocations > 1 ? "locations" : "location");			
			instructions.append(", and knowledge of Red\'s tactics from the BLUEBOOK, assuming Red has the capability to attack.");*/
			break;
		case AttackProbabilityReport_Capability_Propensity:
			instructions.append("of Red attack given Red's capability (from HUMINT) " +
					"and the BLUEBOOK probability (from IMINT and OSINT).");
			//instructions.append("of Red attack, considering both Red\'s Capability and Propensity.");
			break;
		case AttackProbabilityReport_Activity:
			instructions.append("of Red attack given ONLY the SIGINT report of Red\'s Activity.");
			//instructions.append("of Red attack, considering ONLY the SIGINT report of Red\'s Activity.");
			break;
		case AttackProbabilityReport_Activity_Capability_Propensity:
			instructions.append("of Red attack given all intelligence reports.");
			/*instructions.append(" given Red\'s activity near the ");
			instructions.append(numLocations > 1 ? "locations" : "location");
			instructions.append(", Red's capability to attack, and Red's propensity to attack.");*/
			break;				
		default:
			instructions.append(".");
			break;
		}
		return instructions;
	}
	
	/**
	 * @param instructions
	 * @param trialPartState
	 * @return
	 */
	public static StringBuilder createBlueActionSelectionsText(StringBuilder instructions, 
			BlueActionSelectionOrPresentationTrialPartState trialPartState) {
		if(instructions == null) {instructions = new StringBuilder();}		
		instructions.append("Blue chose to ");
		if(trialPartState.getProbe() != null && trialPartState.getProbe().getBlueActions() != null &&
				!trialPartState.getProbe().getBlueActions().isEmpty()) {
			int numBlueActions = trialPartState.getProbe().getBlueActions().size();			
			int i = 0;
			for(BlueAction blueAction : trialPartState.getProbe().getBlueActions()) {
				instructions.append(blueAction.getAction() == null || blueAction.getAction() == BlueActionType.Do_Not_Divert ?
						"not divert" : "divert");
				if(numBlueActions == 1) {
					instructions.append(".");
					/*instructions.append("the ");
					instructions.append(createBlueLocationProbabilityReportDisplayName(i, numBlueActions));
					instructions.append(" shown");*/
				} else {
					instructions.append(" at ");
					instructions.append(createBlueLocationProbabilityReportDisplayName(i, numBlueActions));
					if(numBlueActions == 2) {
						if(i == 0) {
							instructions.append(" and ");
						}
					} else {
						if(i == numBlueActions - 1) {
							instructions.append(", ");
						} else {
							instructions.append(", and ");
						}
					}
				}
				i++;
			}
			if(numBlueActions > 1) {
				instructions.append(".");
			}
		}
		return instructions;
	}
}