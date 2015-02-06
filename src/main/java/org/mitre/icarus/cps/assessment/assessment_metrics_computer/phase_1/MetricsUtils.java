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
package org.mitre.icarus.cps.assessment.assessment_metrics_computer.phase_1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.mitre.icarus.cps.app.util.CPSUtils;
import org.mitre.icarus.cps.app.util.ProbabilityUtils;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.Probabilities;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.ImintLayer;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.IntLayer;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.MovintLayer;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.SigintLayer;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.SocintLayer;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;
import org.mitre.icarus.cps.feature_vector.phase_1.IntType;

public class MetricsUtils {
	
	/** Standard layer sequence permutations for three INT layers */
	public static final List<int[]> LAYER_SEQUENCES = Collections.unmodifiableList(createLayerSequencePermutations());
	
	/**
	 * Create a distribution I where 1 is assigned to group or location 
	 * with the highest subject probability.			
	 * 
	 * @return the I distribution
	 */
	public static ArrayList<Double> createIDistribution(List<Double> probs) {
		//TODO: IF 2 OR MORE GROUPS TIED, SHOULD WE MODIFY THE I DISTRIBUTION?
		ArrayList<Double> I = new ArrayList<Double>(probs.size());
		double maxProb = Double.MIN_VALUE;
		int maxProbIndex = 0;		
		int i = 0;
		for(Double prob : probs) {
			if(prob > maxProb) {
				maxProb = prob;
				maxProbIndex = i;
			}
			i++;
		}
		for(i=0; i<probs.size(); i++) {
			if(i == maxProbIndex) {
				I.add(1d);
			} else {
				I.add(0d);
			}
		}
		return I;
	}
	
	
	/**
	 * @param vals
	 * @return
	 */
	public static int getMaxValIndex(List<Double> vals) {
		Double maxVal = Double.MIN_VALUE;
		int maxValIndex = 0;
        int i = 0;
        for(Double val : vals) {
        	if(val > maxVal) {
        		maxVal = val;
        		maxValIndex = i;
        	}
        	i++;
        }
        return maxValIndex;
	}
	
	public static Set<Integer> getMaxValIndices(List<Double> vals) {
		return getTargetValIndices(vals, Collections.max(vals));
	}
	
	/**
	 * @param vals
	 * @return
	 */
	public static Set<Integer> getTargetValIndices(List<Double> vals, double targetVal) {		
		Set<Integer> indices = new HashSet<Integer>();
		int i = 0;
		for(Double val : vals) {
			if(val == targetVal) {
				indices.add(i);
			}
			i++;
		}
		return indices;
	}	
	
	/**
	 * @param layer
	 * @return
	 */
	public static String formatLayerName(IntLayer layer) {
		if(layer != null) {
			if(layer instanceof SigintLayer) {
				SigintLayer sigintLayer = (SigintLayer)layer;
				return sigintLayer.getGroup() != null ? layer.getLayerType() + "-" + sigintLayer.getGroup().toString() :
					layer.getLayerType().toString();
			} else {
				return layer.getLayerType().toString();
			}
		}
		return null;
	}
	
	/**
	 * @param lsIndex
	 * @return
	 */
	public static String formatLayerSequenceName(int lsIndex, int numLayersToSelect) {
		if(lsIndex >= 0 && lsIndex < LAYER_SEQUENCES.size()) {
			StringBuilder sb = new StringBuilder();
			int[] sequence = LAYER_SEQUENCES.get(lsIndex);
			for(int i=0; i<numLayersToSelect && i<sequence.length; i++) {
				IntType intType = IntType.values()[sequence[i] + 1];
				if(intType != null) {
					sb.append(intType.toString());
					if(i < (numLayersToSelect - 1)) {
						sb.append("-");
					}
				}
			}
			return sb.toString();
		}
		return null;
	}
	
	/**
	 * @param formattedLayerName
	 * @return
	 */
	public static IntLayer parseIntLayer(String formattedLayerName) {
		if(formattedLayerName.contains("-")) {
			return new SigintLayer(parseSigintGroup(formattedLayerName));
		} else {
			try {
				IntType intType = IntType.valueOf(formattedLayerName);
				if(intType != null) {
					switch(intType) {
					case IMINT:
						return new ImintLayer();
					case MOVINT:
						return new MovintLayer();
					case SOCINT:
						return new SocintLayer();
					default: return null;
					}
				}
			} catch(Exception ex) {
				return null;
			}
		}
		return null;
	}
	
	/**
	 * @param formattedLayerName
	 * @return
	 */
	public static GroupType parseSigintGroup(String formattedLayerName) {
		String[] strings = formattedLayerName.split("-");
		if(strings != null && strings.length > 1) {
			try {
				return GroupType.valueOf(strings[1]);
			} catch(Exception ex) {
				return null;
			}
		}
		return null;
	}
	
	/**
	 * @return
	 */
	public static ArrayList<int[]> createLayerSequencePermutations() {		
		IntType[] intLayers = new IntType[] {IntType.IMINT, IntType.MOVINT, IntType.SIGINT, IntType.SOCINT};
		int[] currentPerm = new int[] {0, 1, 2, 3};
		int numPerms = CPSUtils.factorial(intLayers.length);			
		ArrayList<int[]> permutations = new ArrayList<int[]>(numPerms);
		permutations.add(currentPerm);
		for(int layerPerm = 1; layerPerm < numPerms; layerPerm++) {
			//Add the next permutation
			currentPerm = CPSUtils.nextPermutation(currentPerm);			
			permutations.add(currentPerm);
		}
		return permutations;
	}
	
	/**
	 * @param layers
	 * @return
	 */
	public static Integer getLayerSequenceIndex_String(List<String> layers, List<int[]> sequences) {
		if(layers != null && !layers.isEmpty() && layers.size() == 3) {
			List<IntLayer> intLayers = new LinkedList<IntLayer>();
			for(String layer : layers) {
				intLayers.add(parseIntLayer(layer));
			}
			return getLayerSequenceIndex(intLayers, sequences);
		}
		return null;
	}	
	
	/**
	 * @param layers
	 * @return
	 */
	public static Integer getLayerSequenceIndex(List<IntLayer> layers, List<int[]> sequences) {		
		if(sequences == null || sequences.isEmpty()) {
			sequences = createLayerSequencePermutations();
		}		
		if(layers != null && !layers.isEmpty() && layers.size() <= sequences.get(0).length) {
			int numLayers = layers.size();
			int[] layerIndices = new int[numLayers];
			int i = 0;
			for(IntLayer layer : layers) {
				if(layer == null) {
					return null;
				}
				layerIndices[i] = layer.getLayerType().ordinal() - 1;
				i++;
			}
			for(int sequenceIndex = 0; sequenceIndex < sequences.size(); sequenceIndex++) {
				int[] sequence = sequences.get(sequenceIndex);
				boolean foundSequence = true;
				for(i=0; i<numLayers; i++) {
					if(layerIndices[i] != sequence[i]) {
						foundSequence = false;
						break;
					}
				}
				if(foundSequence) {
					return sequenceIndex;
				}
			}
		}
		return null;
	}	
	
	/**
	 * Compute root mean squared error (RMSE) between two distributions.
	 * 
	 * @param dist1 distribution 1
	 * @param dist2 distribution 2
	 * @return RMSE between the two distributions
	 */
	public static Double computeRMSE(List<Double> dist1, List<Double> dist2) {
		double squaredSum = 0;
		for(int i=0; i<dist1.size(); i++) {
			squaredSum += StrictMath.pow((dist1.get(i)-dist2.get(i)), 2);
		}
		return StrictMath.sqrt(squaredSum/dist1.size());
	}
	
	/**
	 * Returns the sigma value for the given circle radius in miles.
	 * 
	 * @param circleRadius
	 * @return
	 */
	public static Double computeSigmaFromCircleRadius(Double circleRadius) {
		return (circleRadius != null ? circleRadius : 0d)/1.4823D;
	}
	
	/**
	 * Returns the circle radius in miles for the given sigma value.
	 * 
	 * @param sigma
	 * @return
	 */
	public static Double computeCircleRadiusFromSigma(Double sigma) {
		return (sigma != null ? sigma : 0d) * 1.4823D;
	}
	
	/**
	 * @param vec1
	 * @param vec2
	 * @param sum
	 * @return
	 */
	public static List<Double> add(List<Double> vec1, List<Double> vec2, List<Double> sum) {
		if(vec1 != null && !vec1.isEmpty() && vec2 != null && vec1.size() == vec2.size()) {
			if(sum == null || sum.size() != vec1.size()) {
				sum = ProbabilityUtils.createProbabilities_Double(vec1.size(), 0d);
			}
			for(int i=0; i<vec1.size(); i++) {
				//sum.set(i, (vec1.get(i) != null ? vec1.get(i) : 0d) + (vec2.get(i) != null ? vec2.get(i) : 0d));
				sum.set(i, vec1.get(i) + vec2.get(i));
			}
		}
		return sum;
	}
	
	/**
	 * @param vec1
	 * @param vec2
	 * @param sum
	 * @return
	 */
	public static Probabilities add(Probabilities vec1, Probabilities vec2, Probabilities sum) {
		if(vec1 != null && vec2 != null) {
			if(sum == null) {
				sum = new Probabilities();
			}
			sum.setProbs(add(vec1.getProbs(), vec2.getProbs(), sum.getProbs()));
		}
		return sum;
	}
	
	/**
	 * @param vec
	 * @param count
	 */
	public static void mean(List<Double> vec, int count) {
		if(vec != null && !vec.isEmpty()) {
			for(int i=0; i<vec.size(); i++) {
				vec.set(i, vec.get(i)/count);
			}
		} 
	}
	
	/**
	 * @param probs
	 * @param count
	 */
	public static void mean(Probabilities probs, int count) {
		if(probs != null) {
			mean(probs.getProbs(), count);
		}
	}
	
	/**
	 * @param counts
	 * @param percents
	 * @return
	 */
	public static List<Double> computePercentsFromCounts(List<Integer> counts, List<Double> percents) {
		if(counts != null && !counts.isEmpty()) {
			Double sum = (double)ProbabilityUtils.computeSum(counts);
			if(sum > 0) {
				if(percents == null || percents.size() != counts.size()) {
					percents = ProbabilityUtils.createProbabilities_Double(counts.size(), 0d);					
				}
				int i = 0;
				for(Integer count : counts) {
					percents.set(i, count/sum);
					i++;
				}
			}
		}
		return percents;
	}
	
	
	/**
	 * @param probs
	 * @param desiredNumStages
	 * @param desiredNumProbs
	 * @return
	 */
	public static List<Probabilities> initializeProbabilities(List<Probabilities> probs, int desiredNumStages, int desiredNumProbs) {
		if(probs == null || probs.size() != desiredNumStages) {
			probs = new ArrayList<Probabilities>(desiredNumStages);
			for(int i=0; i<desiredNumStages; i++) {
				probs.add(new Probabilities(initializeDoubleList(null, desiredNumProbs)));
			}
		} else {
			for(int i=0; i<desiredNumStages; i++) {
				Probabilities p = probs.get(i);
				if(p == null) {
					p = new Probabilities();
				}
				p.setProbs(initializeDoubleList(p.getProbs(), desiredNumProbs));
				probs.set(i, p);
			}
		}
		return probs;
	}
	
	/**
	 * @param data
	 * @param desiredSize
	 * @return
	 */
	public static List<Double> initializeDoubleList(List<Double> data, int desiredSize) {
		if(data == null || data.size() != desiredSize) {
			data = ProbabilityUtils.createProbabilities_Double(desiredSize, 0d);
		} else {
			for(int i=0; i<data.size(); i++) {
				data.set(i, 0d);
			}
		}
		return data;
	}
	
	/**
	 * @param data
	 * @param desiredSize
	 * @return
	 */
	public static List<Integer> initializeIntegerList(List<Integer> data, int desiredSize) {
		if(data == null || data.size() != desiredSize) {
			data = ProbabilityUtils.createProbabilities(desiredSize, 0);
		} else {
			for(int i=0; i<data.size(); i++) {
				data.set(i, 0);
			}
		}
		return data;
	}
	
	public static void main(String[] args) {
		ArrayList<Double> d1 = new ArrayList<Double>(Arrays.asList(.2d, .3d, .4d, .5d));
		ArrayList<Double> d2 = new ArrayList<Double>(Arrays.asList(.1d, .5d, .4d, .5d));
		System.out.println(computeRMSE(d1, d2));
		
		String layer = "SIGINT-C";
		System.out.println(MetricsUtils.parseSigintGroup(layer));
		
		ArrayList<int[]> perms = MetricsUtils.createLayerSequencePermutations();
		System.out.println(perms.size());
		IntType[] intLayers = new IntType[] {IntType.IMINT, IntType.MOVINT, IntType.SIGINT, IntType.SOCINT};
		for(int[] perm : perms) {
			//System.out.println((perm[0]+1) + ", " + (perm[1]+1) + ", " + (perm[2]+1));
			System.out.println(intLayers[perm[0]] + ", " + intLayers[perm[1]] + ", " + 
					intLayers[perm[2]]);
		}
		
		System.out.println("Sequence: " + getLayerSequenceIndex(Arrays.asList(new ImintLayer(), new MovintLayer(), new SocintLayer()), 
				MetricsUtils.createLayerSequencePermutations()));		
	}
}