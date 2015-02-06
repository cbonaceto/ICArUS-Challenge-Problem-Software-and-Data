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
package org.mitre.icarus.cps.exam.phase_1.generation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import org.mitre.icarus.cps.assessment.score_computer.ScoreComputerBase.ProbabilityType;
import org.mitre.icarus.cps.assessment.score_computer.phase_1.ScoreComputer;
import org.mitre.icarus.cps.assessment.score_computer.phase_1.ScoreComputerException;
import org.mitre.icarus.cps.assessment.score_computer.phase_1.GaussianFunction.Gaussian1D;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_1_2_3_AttackDispersionParameters;
import org.mitre.icarus.cps.exam.phase_1.testing.probability_rules.HumintRules;
import org.mitre.icarus.cps.exam.phase_1.testing.probability_rules.ProbabilityRules;
import org.mitre.icarus.cps.feature_vector.phase_1.FeatureVectorManager;
import org.mitre.icarus.cps.feature_vector.phase_1.GridLocation2D;
import org.mitre.icarus.cps.feature_vector.phase_1.GridSize;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupAttack;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;
import org.mitre.icarus.cps.feature_vector.phase_1.Road;
import org.mitre.icarus.cps.feature_vector.phase_1.road_network.IRoadDistanceCalculator;
import org.mitre.icarus.cps.feature_vector.phase_1.road_network.RoadDistanceCalculator;

/**
 * @author CBONACETO
 *
 */
public class AttackGenerator {
	
	public static boolean DEBUG = true;
	
	/** Generates probe attack locations for Task 3. Attempts to locate the probes such that the normative probabilities
	 * are in the target negentropy range (~.3 - ~.9). */
	public static ArrayList<GroupAttack> generateAttackProbes_Task3(			
			Collection<GroupType> probeTrialGroups,			
			ArrayList<Map<GroupType, Task_1_2_3_AttackDispersionParameters>> probeTrialParameters,
			Collection<Task_1_2_3_AttackDispersionParameters> generativeModelParameters,
			GridSize gridSize, IRoadDistanceCalculator distanceCalculator, 
			ProbabilityRules rules, Random rand, ArrayList<Double> desiredMinNe, ArrayList<Double> desiredMaxNe,
			boolean testAllRoadPoints) {		
		//For each group, pre-cache the maximum road network distance for the group and the distances from the group center to all road locations
		HashMap<GroupType, Double> maxDistancesMap = new HashMap<GroupType, Double>();
		HashMap<GroupType, Set<DistanceToLocation>> roadLocationDistancesMap = new HashMap<GroupType, Set<DistanceToLocation>>();
		ArrayList<GroupType> groups = new ArrayList<GroupType>();
		for(Task_1_2_3_AttackDispersionParameters parameters : generativeModelParameters) {
			//System.out.println("Calculating distances for group " + parameters.getGroup());
			groups.add(parameters.getGroup());
			//Compute the maximum road network distance (miles) from the group center to any road point
			maxDistancesMap.put(parameters.getGroup(), 
					computeMaxRoadDistance(distanceCalculator, parameters.getCenterLocation()));
			//Cache distances from the group center to all road locations
			roadLocationDistancesMap.put(parameters.getGroup(), 
					computeAllRoadLocationDistances(distanceCalculator, parameters.getCenterLocation()));
		}
		
		//Create the HUMINT Gaussian
		Gaussian1D gaussian = new Gaussian1D(rules.getHumintRules().getPeakHeight_a(), rules.getHumintRules().getPeakCenter_b(),
				rules.getHumintRules().getSigma_c());		
		
		//Generate the probe attack locations
		ArrayList<GroupAttack> probeAttacks = new ArrayList<GroupAttack>(probeTrialGroups.size());
		int probeTrialNum = 0;
		for(GroupType group : probeTrialGroups) {
			double maxDistance_gridUnits = maxDistancesMap.get(group);
			Set<DistanceToLocation> roadLocationDistances = roadLocationDistancesMap.get(group);		
			
			GridLocation2D bestLocation = null;
			boolean locationFound = false;
			ArrayList<Double> bestProbs = null;
			Double bestNe = null;
			double minNeError = Double.MAX_VALUE;
			double minNe = desiredMinNe.get(probeTrialNum); 
			double maxNe = desiredMaxNe.get(probeTrialNum);
			//int numTries = 0;
			int numTries = 0;
			int maxTries = 200;
			if(testAllRoadPoints) {
				maxTries = distanceCalculator.getRoads().size();
			}
			do {
				//Generate a distance sampled from the HUMINT Gaussian
				double distance = DistanceGenerator.generateDistances(1, 0, 
						gridSize.toMiles(maxDistance_gridUnits - 1), -1, 
						gaussian, rand)[0];
				//System.out.println("generated distance: " + distance + ", max distance: " + gridSize.toMiles(maxDistance_gridUnits - 1));

				Collection<GridLocation2D> locations = null;
				if(testAllRoadPoints) {
					locations = distanceCalculator.getRoads().get(numTries).getVertices();	
				} else {
					//Generate an attack location for the group using the sampled distance				
					locations = findRoadPoints(roadLocationDistances, distance, 1.1d);
				}				
				if(locations != null) {
					for(GridLocation2D location : locations) {
						//Compute negentropy for the given attack location
						Double ne = null;
						ArrayList<Double> probs = null;
						try {
							probs = ScoreComputer.computeGroupProbabilities_Task_3(location, groups, rules, gridSize, 
									distanceCalculator, probeTrialParameters.get(probeTrialNum), null);
							ne = ScoreComputer.computeNegentropy(probs, ProbabilityType.Percent);
							//System.out.println("Ground Truth Group: " + group + ", probs: " + probs + ", negentropy: " + ne);							
						} catch (ScoreComputerException e) {					
							e.printStackTrace();
						}
						if(ne >= minNe && ne <= maxNe) {
							bestLocation = location;
							bestProbs = probs;
							bestNe = ne;
							locationFound = true;
							break;
						} else {
							double error = 0;
							if(ne < minNe) {
								error = minNe - ne;
							} else {
								error = ne - maxNe;
							}
							if(error < minNeError) {
								//System.out.println(minNeError + ", " + ne);
								bestLocation = location;
								bestProbs = probs;
								bestNe = ne;
								minNeError = error;
							}
						}
					}
				}
				numTries++;
			} while(!locationFound && numTries < maxTries);
			probeAttacks.add(new GroupAttack(group, bestLocation));
			System.out.println("Best Location for Probe Trial " + (probeTrialNum+1) + ": " + bestLocation + ", Ground Truth Group: " + group + ", probs: " + 
					bestProbs + ", negentropy: " + bestNe);
			System.out.println(numTries);
			probeTrialNum++;
		}		

		return probeAttacks;
	}

	/** Generates a sequence of attacks on roads for each group for Task 3. No two attacks will occur at the same location. */
	public static ArrayList<GroupAttack> generateGroupAttacks_Task3(int totalAttacks, 
			Collection<Task_1_2_3_AttackDispersionParameters> attackDispersionParameters,
			GridSize gridSize, IRoadDistanceCalculator distanceCalculator, 
			HumintRules humintRules, Random rand) {
		ArrayList<GroupAttack> attacks = new ArrayList<GroupAttack>(totalAttacks);
		Gaussian1D gaussian = new Gaussian1D(humintRules.getPeakHeight_a(), humintRules.getPeakCenter_b(),
				humintRules.getSigma_c());
		Set<GridLocation2D> usedRoadPoints = new HashSet<GridLocation2D>();
		int numAttacks = 0;
		int groupIndex = 0;
		for(Task_1_2_3_AttackDispersionParameters parameters : attackDispersionParameters) {
			//Compute the number of attacks to generate for the current group
			int numGroupAttacks = 0;
			if(groupIndex == attackDispersionParameters.size() - 1) {
				numGroupAttacks = totalAttacks - numAttacks;
			} else {
				numGroupAttacks = (int)Math.round(parameters.getBaseRate() * totalAttacks);
			}		
			
			if(DEBUG) {
				System.out.println("Attempting to generate " + numGroupAttacks + " attacks for Group " + parameters.getGroup());
			}
			
			//Compute the maximum road network distance (miles) from the group center to any road point
			double maxDistance_gridUnits = computeMaxRoadDistance(distanceCalculator, parameters.getCenterLocation());
			
			//Cache distances from the group center to all road locations
			Set<DistanceToLocation> roadLocationDistances = computeAllRoadLocationDistances(
					distanceCalculator, parameters.getCenterLocation());
			
			int attacksGenerated = 0;
			int numTries = 0;
			do {				
				//Generate a distribution of distances sampled from the HUMINT Gaussian function
				double[] distances = DistanceGenerator.generateDistances(numGroupAttacks-attacksGenerated, 0, 
						gridSize.toMiles(maxDistance_gridUnits - 1), -1, 
						gaussian, rand);
				//Convert distances from miles to grid units
				for(int i=0; i<distances.length; i++) {
					distances[i] = gridSize.toGridUnits(distances[i]);
				}				
				
				//Generate attacks for the group using the sampled distances
				for(double distance : distances) {
					//Set<GridLocation2D> locations = findRoadPoints(distanceCalculator, parameters.getCenterLocation(), distance, 1.d);
					Set<GridLocation2D> locations = findRoadPoints(roadLocationDistances, distance, 1.1d);
					if(locations != null) {
						for(GridLocation2D location : locations) {
							if(!usedRoadPoints.contains(location)) {
								attacks.add(new GroupAttack(parameters.getGroup(), location));
								usedRoadPoints.add(location);
								attacksGenerated++;
								break;
							}
						}
					}
				}
				numTries++;
			} while(attacksGenerated < numGroupAttacks && numTries < 100);
			if(DEBUG) {
				System.out.println("Generated " + attacksGenerated + " attacks  for Group " + parameters.getGroup() + " after " + numTries + " tries.");
				System.out.println();
			}
			
			if(attacksGenerated < numGroupAttacks) {
				System.err.println("Warning, only able to generate " + attacksGenerated + "/" + numGroupAttacks + " attacks for group " +
						parameters.getGroup());
			}
			
			numAttacks += attacksGenerated;
			groupIndex++;
		}		
		
		//Shuffle the attack sequence and return it
		Collections.shuffle(attacks, rand);
		return attacks;
	}
	
	/** Test main */
	public static void main(String[] args) {
		//generateTask3AttacksExample();
		generateTask3ProbeAttacksExample();
	}
	
	public static void generateTask3ProbeAttacksExample() {
		Random rand = new Random(3);

		//Load roads file and create distance calculator
		GridSize gridSize = new GridSize();
		ArrayList<Road> roads = null;
		try {
			URL roadsFileUrl = new File("data/Phase_1_CPD/Final_Experiment/ROAD_Task3.csv").toURI().toURL();
			roads = FeatureVectorManager.getInstance().getRoads(roadsFileUrl, gridSize);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		RoadDistanceCalculator distanceCalculator = new RoadDistanceCalculator(roads, gridSize);
		
		//Initialize the generative model parameters
		EnumMap<GroupType, Task_1_2_3_AttackDispersionParameters> generativeModelParams = new EnumMap<GroupType, Task_1_2_3_AttackDispersionParameters>(GroupType.class);
		generativeModelParams.put(GroupType.A, new Task_1_2_3_AttackDispersionParameters(GroupType.A, 0.3, new GridLocation2D("ga", 65, 74))); //22, 73 best road point
		generativeModelParams.put(GroupType.B, new Task_1_2_3_AttackDispersionParameters(GroupType.B, 0.2, new GridLocation2D("gb", 29, 53))); //79, 26 best road point
		generativeModelParams.put(GroupType.C, new Task_1_2_3_AttackDispersionParameters(GroupType.C, 0.4, new GridLocation2D("gc", 50, 25))); //50,25 //22, 16 best road point
		generativeModelParams.put(GroupType.D, new Task_1_2_3_AttackDispersionParameters(GroupType.D, 0.1, new GridLocation2D("gd", 58, 27))); //77, 28 //58,27 //70, 75 best road point		
		
		//Initialize the parameters for probe trial 1 (trial 20)
		ArrayList<Map<GroupType, Task_1_2_3_AttackDispersionParameters>> trialParams = new ArrayList<Map<GroupType, Task_1_2_3_AttackDispersionParameters>>();
		EnumMap<GroupType, Task_1_2_3_AttackDispersionParameters> params = new EnumMap<GroupType, Task_1_2_3_AttackDispersionParameters>(GroupType.class);
		params.put(GroupType.A, new Task_1_2_3_AttackDispersionParameters(GroupType.A, 0.10526315789473686, new GridLocation2D("t1a", 59, 70)));
		params.put(GroupType.B, new Task_1_2_3_AttackDispersionParameters(GroupType.B, 0.31578947368421056, new GridLocation2D("t1b", 23, 73)));
		params.put(GroupType.C, new Task_1_2_3_AttackDispersionParameters(GroupType.C, 0.47368421052631576, new GridLocation2D("t1c", 33, 25)));
		params.put(GroupType.D, new Task_1_2_3_AttackDispersionParameters(GroupType.D, 0.10526315789473686, new GridLocation2D("t1d", 91, 18)));
		trialParams.add(params);

		//Initialize the parameters for probe trial 2 (trial 40)
		params = new EnumMap<GroupType, Task_1_2_3_AttackDispersionParameters>(GroupType.class);
		params.put(GroupType.A, new Task_1_2_3_AttackDispersionParameters(GroupType.A, 0.23076923076923075, new GridLocation2D("t2a", 23, 75)));
		params.put(GroupType.B, new Task_1_2_3_AttackDispersionParameters(GroupType.B, 0.28205128205128205, new GridLocation2D("t2b", 23, 75)));
		params.put(GroupType.C, new Task_1_2_3_AttackDispersionParameters(GroupType.C, 0.4358974358974359, new GridLocation2D("t2c", 39, 25)));
		params.put(GroupType.D, new Task_1_2_3_AttackDispersionParameters(GroupType.D, 0.051282051282051315, new GridLocation2D("t2d", 91, 18)));
		trialParams.add(params);

		//Initialize the parameters for probe trial 3 (trial 60)
		params = new EnumMap<GroupType, Task_1_2_3_AttackDispersionParameters>(GroupType.class);
		params.put(GroupType.A, new Task_1_2_3_AttackDispersionParameters(GroupType.A, 0.23728813559322035, new GridLocation2D("t3a", 26, 84)));
		params.put(GroupType.B, new Task_1_2_3_AttackDispersionParameters(GroupType.B, 0.22033898305084743, new GridLocation2D("t3b", 24, 67)));
		params.put(GroupType.C, new Task_1_2_3_AttackDispersionParameters(GroupType.C, 0.4745762711864407, new GridLocation2D("t3c", 50, 25)));
		params.put(GroupType.D, new Task_1_2_3_AttackDispersionParameters(GroupType.D, 0.06779661016949153, new GridLocation2D("t3d", 84, 18)));
		trialParams.add(params);

		//Initialize the parameters for probe trial 4 (trial 80)
		params = new EnumMap<GroupType, Task_1_2_3_AttackDispersionParameters>(GroupType.class);
		params.put(GroupType.A, new Task_1_2_3_AttackDispersionParameters(GroupType.A, 0.3037974683544304, new GridLocation2D("t4a", 65, 74)));
		params.put(GroupType.B, new Task_1_2_3_AttackDispersionParameters(GroupType.B, 0.189873417721519, new GridLocation2D("t4b", 27, 57)));
		params.put(GroupType.C, new Task_1_2_3_AttackDispersionParameters(GroupType.C, 0.43037974683544306, new GridLocation2D("t4c", 50, 25)));
		params.put(GroupType.D, new Task_1_2_3_AttackDispersionParameters(GroupType.D, 0.0759493670886076, new GridLocation2D("t4d", 50, 25)));
		trialParams.add(params);

		//Initialize the parameters for probe trial 5 (trial 100)
		params = new EnumMap<GroupType, Task_1_2_3_AttackDispersionParameters>(GroupType.class);
		params.put(GroupType.A, new Task_1_2_3_AttackDispersionParameters(GroupType.A, 0.31313131313131315, new GridLocation2D("t5a", 65, 74)));
		params.put(GroupType.B, new Task_1_2_3_AttackDispersionParameters(GroupType.B, 0.1919191919191919, new GridLocation2D("t5b", 27, 55)));
		params.put(GroupType.C, new Task_1_2_3_AttackDispersionParameters(GroupType.C, 0.3939393939393939, new GridLocation2D("t5c", 50, 25)));
		params.put(GroupType.D, new Task_1_2_3_AttackDispersionParameters(GroupType.D, 0.10101010101010101, new GridLocation2D("t5d", 58, 27)));
		trialParams.add(params);
		
		//Create the list of ground truth groups for each probe trial
		//Collection<GroupType> probeTrialGroups = Arrays.asList(GroupType.B, GroupType.C, GroupType.C, GroupType.A, GroupType.C);
		Collection<GroupType> probeTrialGroups = Arrays.asList(GroupType.B, GroupType.A, GroupType.C, GroupType.A, GroupType.B);
		//ArrayList<Double> desiredMinNe = new ArrayList<Double>(Arrays.asList(0.3D, 0.8D, 0.5D, 0.6D, 0.4D));
		//ArrayList<Double> desiredMinNe = new ArrayList<Double>(Arrays.asList(0.8D, 0.85D, 0.8D, 0.8D, 0.8D));
		ArrayList<Double> desiredMinNe = new ArrayList<Double>(Arrays.asList(0.85D, 0.65D, 0.56D, 0.8D, 0.5D));
		ArrayList<Double> desiredMaxNe = new ArrayList<Double>(Arrays.asList(0.9D, 0.66D, 0.57D, 0.83D, 0.53D));
		
		//Generate the probe trial attack locations		
		generateAttackProbes_Task3(probeTrialGroups, trialParams, generativeModelParams.values(),
				gridSize, distanceCalculator, ProbabilityRules.createDefaultProbabilityRules(), 
				rand, desiredMinNe, desiredMaxNe, true);
	}
	
	public static void generateTask3AttacksExample() {
		Random rand = new Random(1);

		//Load roads file and create distance calculator
		GridSize gridSize = new GridSize();
		ArrayList<Road> roads = null;
		try {
			//URL roadsFileUrl = new File("data/Phase_1_CPD/Pilot_Experiment/roads.csv").toURI().toURL();
			URL roadsFileUrl = new File("data/Phase_1_CPD/Final_Experiment/ROAD_Task3.csv").toURI().toURL();
			roads = FeatureVectorManager.getInstance().getRoads(roadsFileUrl, gridSize);
		} catch(Exception ex) {
			ex.printStackTrace();
		}

		if(roads != null) {
			//Generate the attacks
			int numBlocks = 5;
			int trialsPerBlock = 19;
			RoadDistanceCalculator distanceCalculator = new RoadDistanceCalculator(roads, gridSize);
			EnumMap<GroupType, Task_1_2_3_AttackDispersionParameters> task3Parameters = new EnumMap<GroupType, Task_1_2_3_AttackDispersionParameters>(GroupType.class);
			task3Parameters.put(GroupType.A, new Task_1_2_3_AttackDispersionParameters(GroupType.A, 0.3, new GridLocation2D("ga", 65, 74))); //22, 73 best road point
			task3Parameters.put(GroupType.B, new Task_1_2_3_AttackDispersionParameters(GroupType.B, 0.2, new GridLocation2D("gb", 29, 53))); //79, 26 best road point
			task3Parameters.put(GroupType.C, new Task_1_2_3_AttackDispersionParameters(GroupType.C, 0.4, new GridLocation2D("gc", 50, 25))); //22, 16 best road point
			task3Parameters.put(GroupType.D, new Task_1_2_3_AttackDispersionParameters(GroupType.D, 0.1, new GridLocation2D("gd", 67, 28))); //70, 75 best road point
			/*task3Parameters.put(GroupType.A, new Task_1_2_3_AttackDispersionParameters(GroupType.A, 0.10d, new GridLocation2D("ga", 60, 30)));
			task3Parameters.put(GroupType.B, new Task_1_2_3_AttackDispersionParameters(GroupType.B, 0.20d, new GridLocation2D("gb", 43, 69)));
			task3Parameters.put(GroupType.C, new Task_1_2_3_AttackDispersionParameters(GroupType.C, 0.30d, new GridLocation2D("gc", 29, 28)));
			task3Parameters.put(GroupType.D, new Task_1_2_3_AttackDispersionParameters(GroupType.D, 0.40d, new GridLocation2D("gd", 64, 81)));*/
			ArrayList<GroupAttack> attacks = generateGroupAttacks_Task3(numBlocks * trialsPerBlock + numBlocks, 
					task3Parameters.values(), gridSize, distanceCalculator,	new HumintRules(), rand);
			
			//Compute mean and std for each group
			EnumMap<GroupType, DispersionParameters> parameters = new EnumMap<GroupType, DispersionParameters>(GroupType.class);
			ArrayList<Double> distances = new ArrayList<Double>(attacks.size());
			for(GroupAttack attack : attacks) {
				DispersionParameters params = parameters.get(attack.getGroup());
				if(params == null) {
					params = new DispersionParameters();
					parameters.put(attack.getGroup(), params);
				}
				double distance = gridSize.toMiles(distanceCalculator.computeShortestDistanceFromStartLocationToEndLocation(
						task3Parameters.get(attack.getGroup()).getCenterLocation(), attack.getLocation()));
				distances.add(distance);
				params.mean += distance;
				params.numAttacks++;
			}
			for(DispersionParameters params : parameters.values()) {
				params.mean = params.mean/params.numAttacks;				
			}
			int i = 0;
			for(GroupAttack attack : attacks) {
				DispersionParameters params = parameters.get(attack.getGroup());
				params.sigma += Math.pow(distances.get(i) - params.mean, 2);
				i++;
			}
			for(Entry<GroupType, DispersionParameters> entry : parameters.entrySet()) {
				DispersionParameters params = entry.getValue();
				params.sigma = Math.sqrt(params.sigma/params.numAttacks);
				System.out.println("Group " + entry.getKey() + ": mean distance (miles): " + params.mean + ", std: " + params.sigma);
			}			
			
			//Write the attacks to CSV files
			//System.out.println(attacks);
			int trialNum = 0;
			String fileName = "data/Phase_1_CPD/Test/";
			for(int block = 1; block <= numBlocks; block++) {
				try {
					//Write attack presentation block
					writeAttacksToCSVFile(fileName + "task3_" + Integer.toString(block) + ".csv", 
							attacks.subList(trialNum, trialNum + trialsPerBlock));
					//Write probe trial
					writeAttacksToCSVFile(fileName + "task3_" + Integer.toString(block) + "_probe.csv", 
							attacks.subList(trialNum + trialsPerBlock, trialNum + trialsPerBlock + 1));
				} catch (IOException e) {
					e.printStackTrace();
				}				
				trialNum += trialsPerBlock + 1;
			}			
		}
	}	
	
	/** Computes the maximum road network distance (in grid units) from the start location to any location in the road network */
	protected static double computeMaxRoadDistance(IRoadDistanceCalculator distanceCalculator, GridLocation2D startLocation) {
		//The maximum distance will occur at a road end point, so just check the end points
		double maxDistance = 0d;
		for(Road road : distanceCalculator.getRoads()) {
			if(road.getVertexCount() > 0) {
				//System.out.println(startLocation + ", " + road.getVertices().get(0));				
				double dist = distanceCalculator.computeShortestDistanceFromStartLocationToEndLocation(
						startLocation, road.getVertices().get(0)); 
				if(dist > maxDistance) {
					maxDistance = dist;
				}				
				dist = distanceCalculator.computeShortestDistanceFromStartLocationToEndLocation(
						startLocation, road.getVertices().get(road.getVertexCount() - 1)); 
				if(dist > maxDistance) {
					maxDistance = dist;
				}
			}
		}
		return maxDistance;	
	}
	
	/** Computes and returns distances from the start location to all road points. */
	protected static Set<DistanceToLocation> computeAllRoadLocationDistances(IRoadDistanceCalculator distanceCalculator, GridLocation2D startLocation) {
		TreeSet<DistanceToLocation> roadLocationDistances = new TreeSet<DistanceToLocation>();
		for(Road road : distanceCalculator.getRoads()) {
			if(road.getVertexCount() > 0) {
				for(GridLocation2D vertex : road.getVertices()) {
					roadLocationDistances.add(new DistanceToLocation( 
							distanceCalculator.computeShortestDistanceFromStartLocationToEndLocation(startLocation, vertex),
							vertex));
				}
			}
		}
		/*for(DistanceToLocation distance : roadLocationDistances) {
			System.out.println(distance.distance);
		}*/
		return roadLocationDistances;
	}
	
	/**
	 * Finds all road points that are targetDistance grid units +/- errorThreshold in the roadLocationDistances set.
	 * 
	 * @param roadLocationDistances distances to each road location in grid units
	 * @param targetDistance the target distance in grid units
	 * @param errorThreshold the error threshold in grid units
	 * @return
	 */
	protected static Set<GridLocation2D> findRoadPoints(Set<DistanceToLocation> roadLocationDistances, double targetDistance, 
			double errorThreshold) {
		//Search road points in the set
		//System.out.println("searching for distance " + targetDistance);
		Set<GridLocation2D> roadPoints = new HashSet<GridLocation2D>();
		for(DistanceToLocation roadLocationDistance : roadLocationDistances) {
			double distanceDelta = roadLocationDistance.distance - targetDistance;
			//System.out.println("distance delta: " + distanceDelta);
			//System.out.println(roadLocationDistance.distance);
			if(distanceDelta < 0) {
				if(((distanceDelta * -1) < errorThreshold)) {
					//System.out.println("Found location at distance: " + roadLocationDistance.distance);
					roadPoints.add(roadLocationDistance.location);
				}
			} else if(distanceDelta == 0) {
				//System.out.println("Found location at distance: " + roadLocationDistance.distance);
				roadPoints.add(roadLocationDistance.location);
			} else {
				if(distanceDelta <= errorThreshold) {
					//System.out.println("Found location at distance: " + roadLocationDistance.distance);
					roadPoints.add(roadLocationDistance.location);
				} else {
					return roadPoints;
				}
			}			
		}
		return roadPoints;
	}
	
	/**
	 * Finds all road points that are targetDistance grid units +/- errorThreshold from the given start location.
	 * 
	 * @param distanceCalculator
	 * @param startLocation the start location
	 * @param targetDistance the target distance in grid units
	 * @param errorThreshold the error threshold in grid units
	 * @return
	 */
	protected static Set<GridLocation2D> findRoadPoints(IRoadDistanceCalculator distanceCalculator, GridLocation2D startLocation, 
			double targetDistance, double errorThreshold) {
		//We currently do an exhaustive search of all road points
		Set<GridLocation2D> roadPoints = new HashSet<GridLocation2D>();
		for(Road road : distanceCalculator.getRoads()) {
			if(road.getVertexCount() > 0) {
				for(GridLocation2D vertex : road.getVertices()) {
					//System.out.println("computing distance to: " + vertex);
					if(Math.abs(targetDistance -
							distanceCalculator.computeShortestDistanceFromStartLocationToEndLocation(startLocation, vertex)) <= errorThreshold) {
						roadPoints.add(vertex);
					}
				}
			}
		}
		return roadPoints;
	}
	
	/** Serializes group attacks to a feature vector CSV file for Task 1-3 feature vector files **/
	public static void writeAttacksToCSVFile(String filepath, Collection<GroupAttack> attacks) throws IOException {
		// open output file
		BufferedWriter writer = new BufferedWriter(new FileWriter(filepath));

		// write header
		writer.write("ObjectId,ObjectType,X,Y\n" );

		// write attacks
		if(attacks != null) {
			int i = 0;
			for (GroupAttack ga : attacks) {
				writer.write(ga.getGroup().getGroupNameAbbreviated() + ",Location,");
				writer.write(Integer.toString(ga.getLocation().getX().intValue()) + ",");
				writer.write(Integer.toString(ga.getLocation().getY().intValue()));
				if(i < attacks.size() - 1) {
					writer.write("\n");
				}
				i++;
			}
		}	

		writer.flush();
		writer.close();
	}
	
	protected static class DistanceToLocation implements Comparable<DistanceToLocation> {
		public Double distance;
		
		public GridLocation2D location;
		
		public DistanceToLocation() {}
		
		public DistanceToLocation(double distance, GridLocation2D location) {
			this.distance = distance;
			this.location = location;
		}

		@Override
		public int compareTo(DistanceToLocation obj) {			
			return distance.compareTo(obj.distance);
		}

		@Override
		public boolean equals(Object obj) {
			if(obj instanceof DistanceToLocation) {
				return distance.equals(((DistanceToLocation)obj).distance);
			} else if(obj instanceof Double) {
				return distance.equals((Double)obj);
			}
			return false;
		}
	}
	
	protected static class DispersionParameters {
		public double mean;
		public double sigma;
		public int numAttacks;
	}	
}