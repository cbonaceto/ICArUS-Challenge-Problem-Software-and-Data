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
package org.mitre.icarus.cps.assessment.model_simulator.phase_1;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Random;

import org.mitre.icarus.cps.app.util.ProbabilityUtils;
import org.mitre.icarus.cps.assessment.assessment_metrics_computer.phase_1.MetricsUtils_Phase1;
import org.mitre.icarus.cps.assessment.score_computer.phase_1.ScoreComputer;
import org.mitre.icarus.cps.assessment.score_computer.phase_1.ScoreComputerException;
import org.mitre.icarus.cps.exam.phase_1.IcarusExam_Phase1;
import org.mitre.icarus.cps.exam.phase_1.response.Task_1_ProbeTrialResponse;
import org.mitre.icarus.cps.exam.phase_1.response.Task_2_ProbeTrialResponse;
import org.mitre.icarus.cps.exam.phase_1.response.Task_3_ProbeTrialResponse;
import org.mitre.icarus.cps.exam.phase_1.response.Task_4_TrialResponse;
import org.mitre.icarus.cps.exam.phase_1.response.Task_5_6_TrialResponse;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.AttackLocationProbeResponse_MultiGroup;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.AttackLocationProbeResponse_MultiLocation;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.GroupAttackProbabilityResponse;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.GroupCentersProbeResponse;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.GroupCirclesProbeResponse;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.GroupCirclesProbeResponse.GroupCircle;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.INTLayerData;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.SurpriseReportProbeResponse;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.Task_4_7_AttackLocationProbeResponseAfterINT;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.Task_5_6_AttackLocationProbeResponseAfterINT;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.TroopAllocation;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.TroopAllocationResponse_MultiGroup;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.TroopAllocationResponse_MultiLocation;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.TroopSelectionResponse_MultiGroup;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.GroupCentersProbeResponse.GroupCenterResponse;
import org.mitre.icarus.cps.exam.phase_1.testing.TaskTestPhase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_1_2_3_AttackDispersionParameters;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_1_ProbeTrial;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_2_ProbeTrial;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_3_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_3_ProbeTrial;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_4_Trial;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_5_Trial;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_6_Trial;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.HumintLayer;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.SigintLayer;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.Task_4_INTLayerPresentationProbe;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.Task_5_6_INTLayerPresentationProbe;
import org.mitre.icarus.cps.exam.phase_1.testing.probability_rules.ProbabilityRules;
import org.mitre.icarus.cps.exam.phase_1.testing.probability_rules.update_strategy.AbUpdateStrategy;
import org.mitre.icarus.cps.exam.phase_1.testing.probability_rules.update_strategy.BayesianUpdateStrategy;
import org.mitre.icarus.cps.exam.phase_1.testing.probability_rules.update_strategy.ProbabilityUpdateStrategy;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.SurpriseReportProbe;
import org.mitre.icarus.cps.feature_vector.phase_1.GridLocation2D;
import org.mitre.icarus.cps.feature_vector.phase_1.GridSize;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupCenter;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;
import org.mitre.icarus.cps.feature_vector.phase_1.IntType;
import org.mitre.icarus.cps.feature_vector.phase_1.road_network.RoadDistanceCalculator;

/**
 * A Phase 1 model that uses the A-B strategy to update probabilities for each
 * task.
 *
 * @author CBONACETO
 *
 */
public class AbModel extends AbstractModel_Phase1 {

    /**
     * A-B Update strategy to use for each Task (contains the A-B parameters for
     * the Task)
     */
    protected ProbabilityUpdateStrategy task_1_Strategy;
    protected ProbabilityUpdateStrategy task_2_Strategy;
    protected ProbabilityUpdateStrategy task_3_Strategy;
    protected ProbabilityUpdateStrategy task_4_1_Strategy;
    protected ProbabilityUpdateStrategy task_4_N_Strategy;
    protected ProbabilityUpdateStrategy task_5_Strategy;
    protected ProbabilityUpdateStrategy task_6_Strategy;

    /**
     * A Bayesian update strategy is always used to compute probabilities at
     * stage 1 for Tasks 5-6 because humans are given the Bayesian probabilities
     */
    protected ProbabilityUpdateStrategy bayesianStrategy = new BayesianUpdateStrategy();

    /**
     * The road network distance calculator for the road network in Task 3
     */
    protected RoadDistanceCalculator task3DistanceCalculator;

    /**
     * The INT layers to select in Task 6 (default is IMINT-MOVINT-SIGINT)
     */
    protected List<IntType> task6Layers;

    protected ProbabilityRules rules;

    protected GridSize gridSize;

    protected Random rand;

    /**
     * Create an A-B model with the default A-B parameters for each Task.
     */
    public AbModel() {
        this(new AbParameters(.3, .3), new AbParameters(0, .4), new AbParameters(.7, .8),
                new AbParameters(1, .5), new AbParameters(1, 1),
                new AbParameters(.9, .5), new AbParameters(.9, .5));
    }

    /**
     * Create an A-B model with the given A-B parameters for each Task.
     * 
     * @param task_1_params
     * @param task_2_params
     * @param task_3_params
     * @param task_4_1_params
     * @param task_4_N_params
     * @param task_5_params
     * @param task_6_params
     */
    public AbModel(AbParameters task_1_params, AbParameters task_2_params, AbParameters task_3_params,
            AbParameters task_4_1_params, AbParameters task_4_N_params, AbParameters task_5_params, AbParameters task_6_params) {
        task_1_Strategy = new AbUpdateStrategy(task_1_params.a, task_1_params.b);
        task_2_Strategy = new AbUpdateStrategy(task_2_params.a, task_2_params.b);
        task_3_Strategy = new AbUpdateStrategy(task_3_params.a, task_3_params.b);
        task_4_1_Strategy = new AbUpdateStrategy(task_4_1_params.a, task_4_1_params.b);
        task_4_N_Strategy = new AbUpdateStrategy(task_4_N_params.a, task_4_N_params.b);
        task_5_Strategy = new AbUpdateStrategy(task_5_params.a, task_5_params.b);
        task_6_Strategy = new AbUpdateStrategy(task_6_params.a, task_6_params.b);
        rules = ProbabilityRules.createDefaultProbabilityRules();
        rand = new Random(1L);
        task6Layers = new ArrayList<IntType>(Arrays.asList(IntType.IMINT, IntType.MOVINT, IntType.SIGINT));
    }

    @Override
    protected void initializeExam(IcarusExam_Phase1 exam) {
        gridSize = exam.getGridSize();
        if (exam.getTasks() != null && !exam.getTasks().isEmpty()) {
            //Create distance calculator for Tasks 3 if present
            for (TaskTestPhase<?> task : exam.getTasks()) {
                if (task instanceof Task_3_Phase) {
                    task3DistanceCalculator = new RoadDistanceCalculator(
                            ((Task_3_Phase) task).getRoads(), gridSize);
                    break;
                }
            }
        }
    }

    @Override
    protected void processTask_1_Trial(Task_1_ProbeTrial trial) {
        Task_1_ProbeTrialResponse response = new Task_1_ProbeTrialResponse();
        trial.setTrialResponse(response);

        //Compute probabilities
        rules.setProbabilityUpdateStrategy(task_1_Strategy);
        EnumMap<GroupType, Task_1_2_3_AttackDispersionParameters> dispersionParameters
                = new EnumMap<GroupType, Task_1_2_3_AttackDispersionParameters>(GroupType.class);
        for (Task_1_2_3_AttackDispersionParameters parameters : trial.getAttackDispersionParameters()) {
            dispersionParameters.put(parameters.getGroup(), parameters);
        }
        ArrayList<Double> probs = null;
        try {
            probs = ScoreComputer.computeGroupProbabilities_Task_1_2(
                    trial.getAttackLocationProbe().getAttackLocation(),
                    trial.getAttackLocationProbe().getGroups(), rules,
                    dispersionParameters);
            ProbabilityUtils.normalizePercentProbabilities_Double(probs, probs);
        } catch (ScoreComputerException e) {
            e.printStackTrace();
        }
        //System.out.println(probs);
        response.setAttackLocationResponse(new AttackLocationProbeResponse_MultiGroup(
                createGroupAttackProbabilities(trial.getAttackLocationProbe().getGroups(), probs),
                trial.getAttackLocationProbe().getAttackLocation().getLocationId()));

        //Generate all-in troop allocation response
        response.setTroopSelectionResponse(generateTask_1_2_3_TroopSelectionResponse(probs));

        //Generate random surprise response
        response.setGroundTruthSurpriseResponse(generateSurpriseResponse(trial.getGroundTruthSurpriseProbe()));
    }

    @Override
    protected void processTask_2_Trial(Task_2_ProbeTrial trial) {
        Task_2_ProbeTrialResponse response = new Task_2_ProbeTrialResponse();
        trial.setTrialResponse(response);

        //Generate random group circles
        response.setGroupCirclesResponse(generateGroupCirclesResponse(trial.getAttackLocationProbe().getGroups()));

        //Compute probabilities
        rules.setProbabilityUpdateStrategy(task_2_Strategy);
        EnumMap<GroupType, Task_1_2_3_AttackDispersionParameters> dispersionParameters
                = new EnumMap<GroupType, Task_1_2_3_AttackDispersionParameters>(GroupType.class);
        for (Task_1_2_3_AttackDispersionParameters parameters : trial.getAttackDispersionParameters()) {
            dispersionParameters.put(parameters.getGroup(), parameters);
        }
        ArrayList<Double> probs = null;
        try {
            probs = ScoreComputer.computeGroupProbabilities_Task_1_2(
                    trial.getAttackLocationProbe().getAttackLocation(),
                    trial.getAttackLocationProbe().getGroups(), rules,
                    dispersionParameters);
            ProbabilityUtils.normalizePercentProbabilities_Double(probs, probs);
        } catch (ScoreComputerException e) {
            e.printStackTrace();
        }
		//DEBUG CODE 
		/*System.out.println(trial.getAttackLocationProbe().getAttackLocation());
         System.out.println(rules.getProbabilityUpdateStrategy());
         for(Task_1_2_3_AttackDispersionParameters parameters : trial.getAttackDispersionParameters()) {
         System.out.println(parameters.getGroup() + ", " + parameters.getBaseRate() + ", " + parameters.getSigmaX());							
         }
         System.out.println(probs);*/
        //END DEBUG CODE		
        response.setAttackLocationResponse(new AttackLocationProbeResponse_MultiGroup(
                createGroupAttackProbabilities(trial.getAttackLocationProbe().getGroups(), probs),
                trial.getAttackLocationProbe().getAttackLocation().getLocationId()));

        //Generate all-in troop allocation response
        response.setTroopSelectionResponse(generateTask_1_2_3_TroopSelectionResponse(probs));

        //Generate random surprise response
        response.setGroundTruthSurpriseResponse(generateSurpriseResponse(trial.getGroundTruthSurpriseProbe()));
    }

    @Override
    protected void processTask_3_Trial(Task_3_ProbeTrial trial) {
        Task_3_ProbeTrialResponse response = new Task_3_ProbeTrialResponse();
        trial.setTrialResponse(response);

        //Generate random group centers
        response.setGroupCentersResponse(generateGroupCentersResponse(trial.getAttackLocationProbe().getGroups()));

        //Compute probabilities
        rules.setProbabilityUpdateStrategy(task_3_Strategy);
        EnumMap<GroupType, Task_1_2_3_AttackDispersionParameters> dispersionParameters
                = new EnumMap<GroupType, Task_1_2_3_AttackDispersionParameters>(GroupType.class);
        for (Task_1_2_3_AttackDispersionParameters parameters : trial.getAttackDispersionParameters()) {
            dispersionParameters.put(parameters.getGroup(), parameters);
        }
        ArrayList<Double> probs = null;
        try {
            probs = ScoreComputer.computeGroupProbabilities_Task_3(
                    trial.getAttackLocationProbe().getAttackLocation(),
                    trial.getAttackLocationProbe().getGroups(),
                    rules, gridSize, task3DistanceCalculator, dispersionParameters, null);
            ProbabilityUtils.normalizePercentProbabilities_Double(probs, probs);
        } catch (ScoreComputerException e) {
            e.printStackTrace();
        }
        //System.out.println(probs);
        response.setAttackLocationResponse(new AttackLocationProbeResponse_MultiGroup(
                createGroupAttackProbabilities(trial.getAttackLocationProbe().getGroups(), probs),
                trial.getAttackLocationProbe().getAttackLocation().getLocationId()));

        //Generate all-in troop allocation response
        response.setTroopSelectionResponse(generateTask_1_2_3_TroopSelectionResponse(probs));

        //Generate random surprise response
        response.setGroundTruthSurpriseResponse(generateSurpriseResponse(trial.getGroundTruthSurpriseProbe()));
    }

    @Override
    protected void processTask_4_Trial(Task_4_Trial trial) {
        Task_4_TrialResponse response = new Task_4_TrialResponse();
        trial.setTrialResponse(response);

        //Compute center to attack distances if they haven't been computed yet				
        ArrayList<Double> centerToAttackDistances = trial.getCenterToAttackDistances();
        if (centerToAttackDistances == null || centerToAttackDistances.isEmpty()) {
            try {
                centerToAttackDistances = ScoreComputer.computeCenterToAttackDistances(trial.getGroupCenter().getLocation(),
                        trial.getPossibleAttackLocations(), gridSize,
                        new RoadDistanceCalculator(trial.getRoads(), gridSize));
            } catch (ScoreComputerException e) {
                e.printStackTrace();
            }
            trial.setCenterToAttackDistances(centerToAttackDistances);
        }

        //Compute initial probabilities from HUMINT
        ArrayList<Double> currentProbs = ProbabilityUtils.createDefaultInitialProbabilities_Double(4);
        try {
            rules.setProbabilityUpdateStrategy(task_4_1_Strategy);
            ScoreComputer.updateLocationProbabilities_Task_4(trial.getPossibleAttackLocations(),
                    trial.getGroupCenter(), centerToAttackDistances, new HumintLayer(),
                    currentProbs, null, rules);
            ArrayList<Double> probs = ProbabilityUtils.cloneProbabilities_Double(currentProbs);
            ProbabilityUtils.normalizePercentProbabilities_Double(probs, probs);
            response.setAttackLocationResponse_initial(new AttackLocationProbeResponse_MultiLocation(
                    createLocationAttackProbabilities(trial.getAttackLocationProbe_initial().getLocations(), probs),
                    trial.getAttackLocationProbe_initial().getAttackGroup()));
        } catch (ScoreComputerException e) {
            e.printStackTrace();
        }

        //Compute likelihoods and updated posteriors for each INT layer based on rules for the layer
        if (trial.getIntLayers() != null && !trial.getIntLayers().isEmpty()) {
            rules.setProbabilityUpdateStrategy(task_4_N_Strategy);
            ArrayList<Task_4_7_AttackLocationProbeResponseAfterINT> intResponses
                    = new ArrayList<Task_4_7_AttackLocationProbeResponseAfterINT>(trial.getIntLayers().size());
            response.setAttackLocationResponses_afterINTs(intResponses);
            for (Task_4_INTLayerPresentationProbe layer : trial.getIntLayers()) {
                try {
                    ScoreComputer.updateLocationProbabilities_Task_4(trial.getPossibleAttackLocations(),
                            trial.getGroupCenter(), centerToAttackDistances,
                            layer.getLayerType(),
                            currentProbs, null, rules);
                    ArrayList<Double> probs = ProbabilityUtils.cloneProbabilities_Double(currentProbs);
                    ProbabilityUtils.normalizePercentProbabilities_Double(probs, probs);
                    intResponses.add(new Task_4_7_AttackLocationProbeResponseAfterINT(
                            new INTLayerData(layer.getLayerType(), false),
                            new AttackLocationProbeResponse_MultiLocation(
                                    createLocationAttackProbabilities(trial.getAttackLocationProbe_initial().getLocations(), probs),
                                    trial.getAttackLocationProbe_initial().getAttackGroup())));
                } catch (ScoreComputerException e) {
                    e.printStackTrace();
                }
            }
        }

        //Generate probability-matched troop allocation response
        response.setTroopAllocationResponse(generate_Task_4_TroopAllocationResponse(
                trial.getAttackLocationProbe_initial().getLocations(), currentProbs));

        //Generate random surprise response
        response.setGroundTruthSurpriseResponse(generateSurpriseResponse(trial.getGroundTruthSurpriseProbe()));
    }

    @Override
    protected void processTask_5_6_Trial(Task_5_Trial trial) {
        Task_5_6_TrialResponse response = new Task_5_6_TrialResponse();
        trial.setTrialResponse(response);

        //Compute centers to attack distances if they haven't been computed yet				
        ArrayList<Double> centersToAttackDistances = trial.getCentersToAttackDistances();
        if (centersToAttackDistances == null || centersToAttackDistances.isEmpty()) {
            try {
                centersToAttackDistances = ScoreComputer.computeCentersToAttackDistances(trial.getAttackLocation().getLocation(),
                        trial.getGroupCenters(), gridSize,
                        new RoadDistanceCalculator(trial.getRoads(), gridSize));
            } catch (ScoreComputerException e) {
                e.printStackTrace();
            }
            trial.setCentersToAttackDistances(centersToAttackDistances);
        }

        //Compute initial probabilities from HUMINT		
        rules.setProbabilityUpdateStrategy(bayesianStrategy);
        ArrayList<Double> currentProbs = ProbabilityUtils.createDefaultInitialProbabilities_Double(4);
        try {
            ScoreComputer.updateGroupProbabilities_Task_5_6(trial.getGroupCenters(),
                    trial.getAttackLocation(), centersToAttackDistances, new HumintLayer(),
                    currentProbs, null, null, rules);
        } catch (ScoreComputerException e) {
            e.printStackTrace();
        }

        //Compute likelihoods for each INT layer based on rules for the layer
        boolean task6Trial = (trial instanceof Task_6_Trial);
        if (task6Trial) {
            rules.setProbabilityUpdateStrategy(task_6_Strategy);
        } else {
            rules.setProbabilityUpdateStrategy(task_5_Strategy);
        }
        if (trial.getIntLayers() != null && !trial.getIntLayers().isEmpty()) {
            ArrayList<Task_5_6_AttackLocationProbeResponseAfterINT> intResponses
                    = new ArrayList<Task_5_6_AttackLocationProbeResponseAfterINT>(trial.getIntLayers().size());
            response.setAttackLocationResponses_afterINTs(intResponses);
            //Get or select the INT layers (task 6)
            int numLayers = trial.getIntLayers().size();
            if (task6Trial) {
                numLayers = ((Task_6_Trial) trial).getNumLayersToShow();
            }
            for (int layerIndex = 0; layerIndex < numLayers; layerIndex++) {
                Task_5_6_INTLayerPresentationProbe layer = null;
                if (task6Trial) {
                    //Select the next layer					
                    layer = selectLayer(trial.getIntLayers(), task6Layers.get(layerIndex), currentProbs);
                } else {
                    //Get the next layer
                    layer = trial.getIntLayers().get(layerIndex);
                }
                try {
                    ScoreComputer.updateGroupProbabilities_Task_5_6(trial.getGroupCenters(),
                            trial.getAttackLocation(), centersToAttackDistances,
                            layer.getLayerType(),
                            currentProbs, null, null, rules);
                    ArrayList<Double> probs = ProbabilityUtils.cloneProbabilities_Double(currentProbs);
                    ProbabilityUtils.normalizePercentProbabilities_Double(probs, probs);
                    intResponses.add(new Task_5_6_AttackLocationProbeResponseAfterINT(
                            new INTLayerData(layer.getLayerType(), task6Trial),
                            new AttackLocationProbeResponse_MultiGroup(
                                    createGroupAttackProbabilities(layer.getAttackLocationProbe().getGroups(), probs),
                                    layer.getAttackLocationProbe().getAttackLocation().getLocationId())));
                } catch (ScoreComputerException e) {
                    e.printStackTrace();
                }
            }
        }

        //Generate probability-matched troop allocation response
        response.setTroopAllocationResponse(generate_Task_5_6_TroopAllocationResponse(
                getGroups(trial.getGroupCenters()), currentProbs));

        //Generate random surprise response
        response.setGroundTruthSurpriseResponse(generateSurpriseResponse(trial.getGroundTruthSurpriseProbe()));
    }

    protected ArrayList<GroupAttackProbabilityResponse> createGroupAttackProbabilities(List<GroupType> groups, List<Double> probs) {
        ArrayList<GroupAttackProbabilityResponse> groupAttackProbabilities = new ArrayList<GroupAttackProbabilityResponse>(groups.size());
        for (int i = 0; i < groups.size(); i++) {
            groupAttackProbabilities.add(new GroupAttackProbabilityResponse(groups.get(i), probs.get(i)));
        }
        return groupAttackProbabilities;
    }

    protected ArrayList<GroupAttackProbabilityResponse> createLocationAttackProbabilities(List<String> locations, List<Double> probs) {
        ArrayList<GroupAttackProbabilityResponse> groupAttackProbabilities = new ArrayList<GroupAttackProbabilityResponse>(locations.size());
        for (int i = 0; i < locations.size(); i++) {
            groupAttackProbabilities.add(new GroupAttackProbabilityResponse(locations.get(i), probs.get(i)));
        }
        return groupAttackProbabilities;
    }

    /**
     * Generate a random group circles response for Task 2.
     *
     * @param groups
     * @return 
     */
    protected GroupCirclesProbeResponse generateGroupCirclesResponse(List<GroupType> groups) {
        GroupCirclesProbeResponse groupCirclesResponse = new GroupCirclesProbeResponse();
        ArrayList<GroupCircle> groupCircles = new ArrayList<GroupCircle>(groups.size());
        groupCirclesResponse.setGroupCircles(groupCircles);
        for (GroupType group : groups) {
            //Generate a random point for the group circle center with a random radius
            groupCircles.add(new GroupCircle(group,
                    new GridLocation2D(rand.nextDouble() * 100, rand.nextDouble() * 100),
                    rand.nextDouble() * 60));

        }
        return groupCirclesResponse;
    }

    /**
     * Generate a random group centers response for Task 3.
     *
     * @param groups
     * @return
     */
    protected GroupCentersProbeResponse generateGroupCentersResponse(List<GroupType> groups) {
        GroupCentersProbeResponse groupCentersResponse = new GroupCentersProbeResponse();
        ArrayList<GroupCenterResponse> groupCenters = new ArrayList<GroupCenterResponse>(groups.size());
        groupCentersResponse.setGroupCenters(groupCenters);
        for (GroupType group : groups) {
            //Generate a random point for the group center
            groupCenters.add(new GroupCenterResponse(group,
                    new GridLocation2D(rand.nextDouble() * 100, rand.nextDouble() * 100)));
        }
        return groupCentersResponse;
    }

    /**
     * Select an INT layer for Task 6 based on the given layer type. For SIGINT,
     * selects SIGINT on the group with the current highest probability.
     *
     * @param layers
     * @param layerType
     * @param probs
     * @return 
     */
    protected Task_5_6_INTLayerPresentationProbe selectLayer(
            List<Task_5_6_INTLayerPresentationProbe> layers, IntType layerType, List<Double> probs) {
        if (layerType == IntType.SIGINT) {
            GroupType group = GroupType.values()[MetricsUtils_Phase1.getMaxValIndex(probs)];
            for (Task_5_6_INTLayerPresentationProbe layer : layers) {
                if (layer.getLayerType().getLayerType() == IntType.SIGINT) {
                    SigintLayer sigint = (SigintLayer) layer.getLayerType();
                    if (sigint.getGroup() == group) {
                        //System.out.println(probs);
                        return layer;
                    }
                }
            }
        } else {
            for (Task_5_6_INTLayerPresentationProbe layer : layers) {
                if (layer.getLayerType().getLayerType() == layerType) {
                    return layer;
                }
            }
        }
        return null;
    }

    /**
     * Generates an all-in troop selection response for Tasks 1-3.
     *
     * @param probs
     * @return
     */
    protected TroopSelectionResponse_MultiGroup generateTask_1_2_3_TroopSelectionResponse(List<Double> probs) {
        //Allocate troops against the group with the highest probability
        TroopSelectionResponse_MultiGroup troopSelectionResponse = new TroopSelectionResponse_MultiGroup();
        int groupIndex = MetricsUtils_Phase1.getMaxValIndex(probs);
        troopSelectionResponse.setGroup(GroupType.values()[groupIndex]);
        return troopSelectionResponse;
    }

    /**
     * Generates a probability-matched troop allocation for Task 4.
     *
     * @param locations
     * @param probs
     * @return
     */
    protected TroopAllocationResponse_MultiLocation generate_Task_4_TroopAllocationResponse(
            List<String> locations, List<Double> probs) {
        TroopAllocationResponse_MultiLocation troopAllocationResponse = new TroopAllocationResponse_MultiLocation();
        ArrayList<TroopAllocation> allocations = new ArrayList<TroopAllocation>(probs.size());
        troopAllocationResponse.setTroopAllocations(allocations);
        for (int i = 0; i < probs.size(); i++) {
            allocations.add(new TroopAllocation(locations.get(i), probs.get(i)));
        }
        return troopAllocationResponse;
    }

    /**
     * Generates a probability-matched troop allocation for Tasks 5-6.
     *
     * @param groups
     * @param probs
     * @return
     */
    protected TroopAllocationResponse_MultiGroup generate_Task_5_6_TroopAllocationResponse(
            List<GroupType> groups, List<Double> probs) {
        TroopAllocationResponse_MultiGroup troopAllocationResponse = new TroopAllocationResponse_MultiGroup();
        ArrayList<TroopAllocation> allocations = new ArrayList<TroopAllocation>(probs.size());
        troopAllocationResponse.setTroopAllocations(allocations);
        for (int i = 0; i < probs.size(); i++) {
            allocations.add(new TroopAllocation(groups.get(i), probs.get(i)));
        }
        return troopAllocationResponse;
    }

    /**
     * Generates a random surprise response.
     * 
     * @param surpriseProbe
     * @return 
     */
    protected SurpriseReportProbeResponse generateSurpriseResponse(SurpriseReportProbe surpriseProbe) {
        return new SurpriseReportProbeResponse(
                rand.nextInt(surpriseProbe.getMaxSurpriseValue() - surpriseProbe.getMinSurpriseValue() + 1));
    }

    protected ArrayList<GroupType> getGroups(ArrayList<GroupCenter> groupCenters) {
        ArrayList<GroupType> groups = new ArrayList<GroupType>(groupCenters.size());
        for (GroupCenter center : groupCenters) {
            groups.add(center.getGroup());
        }
        return groups;
    }

    public static class AbParameters {

        public double a;
        public double b;

        public AbParameters() {
        }

        public AbParameters(double a, double b) {
            this.a = a;
            this.b = b;
        }
    }
}
