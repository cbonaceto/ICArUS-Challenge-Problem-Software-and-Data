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
package org.mitre.icarus.cps.app.window.phase_1;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.mitre.icarus.cps.app.util.ProbabilityUtils;
import org.mitre.icarus.cps.assessment.assessment_metrics_computer.phase_1.MetricsUtils;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.Probabilities;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.GroupCirclesProbeResponse.GroupCircle;
import org.mitre.icarus.cps.feature_vector.phase_1.GridLocation2D;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupCenter;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;

/**
 * @author CBONACETO
 *
 */
public class PlayerUtils {
	
	protected static final DecimalFormat decimalFormat = new DecimalFormat(); 
	static {
		decimalFormat.setMinimumFractionDigits(2);
		decimalFormat.setMaximumFractionDigits(2);
	}
	
	/**
	 * @param center_x
	 * @param center_y
	 * @param sigma
	 * @param groups
	 * @param numGroups
	 * @return
	 */
	public static List<GroupCircle> createGroupCircles(List<Double> center_x, List<Double> center_y, 
			List<Double> sigma, List<GroupType> groups, int numGroups) {
		List<GroupCircle> groupCircles = null;
		if(center_x != null && center_y != null && center_x.size() >= numGroups && center_y.size() >= numGroups &&
				sigma != null && sigma.size() >= numGroups && groups != null && groups.size() >= numGroups) {
			groupCircles = new ArrayList<GroupCircle>(numGroups);
			for(int i=0; i<numGroups; i++) {
				//The radius is in grid units
				groupCircles.add(new GroupCircle(groups.get(i), 
						new GridLocation2D(center_x.get(i), center_y.get(i)),
						MetricsUtils.computeCircleRadiusFromSigma(sigma.get(i))));
						//gridSize.toGridUnits(MetricsUtils.computeCircleRadiusFromSigma(sigma.get(i)))));
			}
		} 
		return groupCircles;
	}
	
	/**
	 * @param center_x
	 * @param center_y
	 * @param groups
	 * @param numGroups
	 * @return
	 */
	public static List<GroupCenter> createGroupCenters(List<Double> center_x, List<Double> center_y, 
			List<GroupType> groups, int numGroups) {
		List<GroupCenter> groupCenters = null;
		if(center_x != null && center_y != null && center_x.size() >= numGroups && center_y.size() >= numGroups &&
				groups != null && groups.size() >= numGroups) {
			groupCenters = new ArrayList<GroupCenter>(numGroups);
			for(int i=0; i<numGroups; i++) {
				groupCenters.add(new GroupCenter(groups.get(i),
						new GridLocation2D(center_x.get(i), center_y.get(i))));
			}
		} 
		return groupCenters;
	}
	
	/**
	 * @param probs
	 * @param numProbs
	 * @param stageIndex
	 * @return
	 */
	public static List<Integer> getPercentProbabilities(List<Probabilities> probs, int numProbs, int stageIndex) {
		if(probs != null && stageIndex < probs.size()) {
			Probabilities p = probs.get(stageIndex);
			if(p != null) {
				return getPercentSettings(p.getProbs(), numProbs);
			} else {
				return createPercentProbabilities(numProbs, 0);
			}
		} else {
			return createPercentProbabilities(numProbs, 0);
		}
	}
	
	/**
	 * @param settings
	 * @param numSettings
	 * @return
	 */
	//TODO: Test this
	public static List<Integer> getPercentSettings(List<Double> settings, int numSettings) {
		if(settings != null && settings.size() >= numSettings) {
			return ProbabilityUtils.roundPercentProbabilities(settings);
		} else {
			return createPercentProbabilities(numSettings, 0);
		}
	}	
	
	/**
	 * @param numProbs
	 * @return
	 */
	public static List<Integer> createDefaultPercentProbabilities(int numProbs) {
		return ProbabilityUtils.createProbabilities(numProbs, 0);
	}
	
	public static List<Integer> createPercentProbabilities(int numProbs, Integer setting) {
		return ProbabilityUtils.createProbabilities(numProbs, setting);
	}
	
	/**
	 * Creates a troop allocation where 100% of troops are allocated against the group or location with the highest probability.
	 * 
	 * @param probs
	 * @return
	 */
	public static List<Integer> createNormativeTroopAllocation(List<Integer> probs) {
		if(probs != null && !probs.isEmpty()) {
			ArrayList<Integer> I = new ArrayList<Integer>(probs.size());
			int maxProb = Integer.MIN_VALUE;
			int maxProbIndex = 0;		
			int i = 0;
			for(Integer prob : probs) {
				if(prob > maxProb) {
					maxProb = prob;
					maxProbIndex = i;
				}
				i++;
			}
			for(i=0; i<probs.size(); i++) {
				if(i == maxProbIndex) {
					I.add(100);
				} else {
					I.add(0);
				}
			}
			return I;	
		}
		return null;
	}
	
	/**
	 * @param surprise
	 * @return
	 */
	public static Integer getSurpriseSetting(Double surprise) {
		if(surprise != null) {
			return (int)Math.round(surprise);
		}
		return null;
	}
	
	/**
	 * @param val
	 * @return
	 */
	public static String formatDoubleAsString(Double val) {
		return formatDoubleAsString(val, 2);
	}
	
	/**
	 * @param val
	 * @param decimalPlaces
	 * @return
	 */
	public static String formatDoubleAsString(Double val, int decimalPlaces) {
		if(val != null) {
			if(decimalFormat.getMaximumFractionDigits() != decimalPlaces) {
				decimalFormat.setMinimumFractionDigits(decimalPlaces);
				decimalFormat.setMaximumFractionDigits(decimalPlaces);
			}
			return decimalFormat.format(val);
		}
		return "";
	}
}