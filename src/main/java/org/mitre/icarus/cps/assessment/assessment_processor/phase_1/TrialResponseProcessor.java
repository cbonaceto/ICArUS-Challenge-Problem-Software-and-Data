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
package org.mitre.icarus.cps.assessment.assessment_processor.phase_1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.mitre.icarus.cps.app.util.ProbabilityUtils;
import org.mitre.icarus.cps.assessment.assessment_metrics_computer.phase_1.MetricsUtils_Phase1;
import org.mitre.icarus.cps.assessment.assessment_metrics_computer.phase_1.TrialMetricsComputer;
import org.mitre.icarus.cps.assessment.assessment_processor.ITrialResponseProcessor;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.Probabilities;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.MetricsInfo;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.TrialData;
import org.mitre.icarus.cps.exam.base.response.ResponseGeneratorData;
import org.mitre.icarus.cps.exam.phase_1.IcarusExam_Phase1;
import org.mitre.icarus.cps.exam.phase_1.feedback.TrialFeedback_Phase1;
import org.mitre.icarus.cps.exam.phase_1.response.Task_1_2_3_ProbeTrialResponseBase;
import org.mitre.icarus.cps.exam.phase_1.response.Task_2_ProbeTrialResponse;
import org.mitre.icarus.cps.exam.phase_1.response.Task_3_ProbeTrialResponse;
import org.mitre.icarus.cps.exam.phase_1.response.Task_4_TrialResponse;
import org.mitre.icarus.cps.exam.phase_1.response.Task_5_6_TrialResponse;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.GroupAttackProbabilityResponse;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.GroupCentersProbeResponse;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.GroupCirclesProbeResponse;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.Task_4_7_AttackLocationProbeResponseAfterINT;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.Task_5_6_AttackLocationProbeResponseAfterINT;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.TroopAllocation;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.GroupCentersProbeResponse.GroupCenterResponse;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.GroupCirclesProbeResponse.GroupCircle;
import org.mitre.icarus.cps.exam.phase_1.testing.IcarusTestTrial_Phase1;
import org.mitre.icarus.cps.exam.phase_1.testing.TaskTestPhase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_1_2_3_AttackDispersionParameters;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_1_2_3_ProbeTrialBase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_2_ProbeTrial;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_3_ProbeTrial;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_4_Trial;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_5_Trial;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_6_Trial;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.IntLayer;
import org.mitre.icarus.cps.exam.phase_1.testing.probability_rules.ProbabilityRules;
import org.mitre.icarus.cps.feature_vector.phase_1.GridSize;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;
import org.mitre.icarus.cps.test_harness.TestHarnessScoreComputer_Phase1;

/**
 * Trial response processor for Phase 1 trials. Extracts data from the trial
 * response into a TrialData object.
 *
 * @author CBONACETO
 *
 */
public class TrialResponseProcessor implements ITrialResponseProcessor<IcarusTestTrial_Phase1, TaskTestPhase<?>, IcarusExam_Phase1, TrialFeedback_Phase1, TrialData> {

    protected TrialMetricsComputer trialMetricsComputer;

    public TrialResponseProcessor() {
        trialMetricsComputer = new TrialMetricsComputer();
    }

    /* (non-Javadoc)
     * @see org.mitre.icarus.cpa.phase_1.data_processor.ITrialResponseProcessor#updateTrialData(org.mitre.icarus.cps.exam.phase_1.testing.IcarusTestTrial_Phase1, org.mitre.icarus.cps.exam.phase_1.feedback.TrialFeedback_Phase1, org.mitre.icarus.cpa.phase_1.data_model.TrialData, java.lang.String, java.lang.String, java.lang.Integer, org.mitre.icarus.cps.exam.base.response.ResponseGeneratorData)
     */
    @Override
    public TrialData updateTrialData(IcarusExam_Phase1 exam, TaskTestPhase<?> task, IcarusTestTrial_Phase1 trial, TrialFeedback_Phase1 trialFeedback,
            boolean scoreTrialIfFeedbackMissingOrIncomplete, TrialData trialData, String exam_id, String task_id, Integer task_number,
            ResponseGeneratorData responseGenerator) {
        if (trialData == null) {
            //Lily added TrialData constructor that computes the primary key hash
            //trialData = new TrialData(responseGenerator != null ? responseGenerator.getSiteId() : "Unknown", 
            //responseGenerator != null ? responseGenerator.getResponseGeneratorId() : "Unknown",
            //exam_id, phase_id, trial.getTrialNum());
            trialData = new TrialData();
        }

        if (responseGenerator != null) {
            trialData.setSite_id(responseGenerator.getSiteId());
            trialData.setResponse_generator_id(responseGenerator.getResponseGeneratorId());
            trialData.setHuman(responseGenerator.isHumanSubject());
        }

        trialData.setData_stale(true);
        trialData.setCfa_metrics_stale(true);
        trialData.setCpa_metrics_stale(true);

        //protected Long time_stamp;
        trialData.setExam_id(exam_id);
        trialData.setTask_id(task_id);
        trialData.setTask_number(task_number);
        trialData.setTrial_number(trial.getTrialNum());

        if (scoreTrialIfFeedbackMissingOrIncomplete
                && (trialFeedback == null || trialFeedback.isResponseWellFormed() == null || !trialFeedback.isResponseWellFormed())
                && trial != null && trial.getTrialResponse() != null) {
            //Attempt to score or re-score the trial if the feedback is null or the trial is marked as incomplete
            try {
                trialFeedback = TestHarnessScoreComputer_Phase1.scoreTrial(task, trial,
                        exam != null && exam.getGridSize() != null ? exam.getGridSize() : new GridSize(),
                        exam != null && exam.getProbabilityRules() != null ? exam.getProbabilityRules()
                        : ProbabilityRules.createDefaultProbabilityRules(), null);
                if (trialFeedback != null) {
                    trial.getTrialResponse().setResponseFeedBack(trialFeedback);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (trialFeedback != null) {
            trialData.setTrial_complete(trialFeedback.isResponseWellFormed());
            trialData.setTrial_valid(trialFeedback.isResponseWellFormed());
            trialData.setErrors(trialFeedback.getErrors());
            trialData.setWarnings(trialFeedback.getWarnings());
            trialData.setS1_score(trialFeedback.getProbabilitiesScore_s1());
            trialData.setS2_score(trialFeedback.getTroopAllocationScore_s2());
            trialData.setCredits(trialFeedback.getNumCreditsAwarded());
        } else {
            trialData.setTrial_complete(null);
            trialData.setTrial_valid(null);
            trialData.setErrors(null);
            trialData.setS1_score(null);
            trialData.setS2_score(null);
            trialData.setCredits(null);
        }

        //if(trialData.isTrial_valid() == null || trialData.isTrial_valid()) {			
        if (trial instanceof Task_1_2_3_ProbeTrialBase) {
            extractTrialData((Task_1_2_3_ProbeTrialBase) trial, trialData);
        } else if (trial instanceof Task_4_Trial) {
            extractTrialData((Task_4_Trial) trial, trialData);
        } else if (trial instanceof Task_6_Trial) {
            extractTrialData((Task_6_Trial) trial, trialData);
        } else if (trial instanceof Task_5_Trial) {
            extractTrialData((Task_5_Trial) trial, trialData);
        }
		//} else {
        //	clearTrialData(trialData);
        //}

        trialData.setData_stale(false);

        return trialData;
    }

    /**
     * @param trialData
     */
    protected void clearTrialData(TrialData trialData) {
        trialData.setProbs_time(null);
        trialData.setProbs(null);
        trialData.setNormative_probs(null);
        trialData.setLayer_type(null);
        trialData.setLs_index(null);
        trialData.setNormative_ls_index(null);
        trialData.setCircles_centers_time(null);
        trialData.setCenter_x(null);
        trialData.setCenter_y(null);
        trialData.setNormative_center_x(null);
        trialData.setNormative_center_y(null);
        trialData.setCircle_r(null);
        trialData.setSigma(null);
        trialData.setNormative_sigma(null);
        trialData.setAllocation_time(null);
        trialData.setAllocation(null);
        trialData.setSurprise_time(null);
        trialData.setSurprise(null);
        trialData.setGround_truth(null);
        trialData.setMetrics(null);
    }

    /**
     * @param trial
     * @param trialData
     */
    protected void extractTrialData(Task_1_2_3_ProbeTrialBase trial, TrialData trialData) {
        if (trial.getTrialResponse() != null) {
            Task_1_2_3_ProbeTrialResponseBase response = trial.getTrialResponse();

            //Get trial time
            if (response.getTrialTime_ms() != null) {
                trialData.setTrial_time(response.getTrialTime_ms().doubleValue());
            }

            //Get normalized subject probabilities and normative probabilities (percent format)
            if (response.getAttackLocationResponse() != null) {
                if (response.getAttackLocationResponse().getTrialPartTime_ms() != null) {
                    trialData.setProbs_time(new ArrayList<Double>(
                            Arrays.asList(response.getAttackLocationResponse().getTrialPartTime_ms().doubleValue())));
                }
                if (response.getAttackLocationResponse().getGroupAttackProbabilities() != null
                        && !response.getAttackLocationResponse().getGroupAttackProbabilities().isEmpty()) {
                    ArrayList<Probabilities> probs = new ArrayList<Probabilities>(1);
                    trialData.setProbs(probs);
                    probs.add(new Probabilities(getNormalizedSubjectProbs(response.getAttackLocationResponse().getGroupAttackProbabilities())));
                }
                if (response.getAttackLocationResponse().getNormativeProbs_cumulativeBayesian() != null
                        && !response.getAttackLocationResponse().getNormativeProbs_cumulativeBayesian().isEmpty()) {
                    ArrayList<Probabilities> normative_probs = new ArrayList<Probabilities>(1);
                    trialData.setNormative_probs(normative_probs);
                    normative_probs.add(new Probabilities(response.getAttackLocationResponse().getNormativeProbs_cumulativeBayesian()));
                }
            }

            //Get group circles or centers (Tasks 2-3)						
            if (trial instanceof Task_2_ProbeTrial) {
                GroupCirclesProbeResponse groupCircles = ((Task_2_ProbeTrialResponse) response).getGroupCirclesResponse();
                if (groupCircles != null) {
                    if (groupCircles.getTrialPartTime_ms() != null) {
                        trialData.setCircles_centers_time(groupCircles.getTrialPartTime_ms().doubleValue());
                    }
                    if (groupCircles.getGroupCircles() != null && !groupCircles.getGroupCircles().isEmpty()) {
                        ArrayList<GroupCircle> circles = groupCircles.getGroupCircles();
                        ArrayList<Double> center_x = new ArrayList<Double>(circles.size());
                        ArrayList<Double> center_y = new ArrayList<Double>(circles.size());
                        ArrayList<Double> circle_r = new ArrayList<Double>(circles.size());
                        ArrayList<Double> sigma = new ArrayList<Double>(circles.size());
                        trialData.setCenter_x(center_x);
                        trialData.setCenter_y(center_y);
                        trialData.setCircle_r(circle_r);
                        trialData.setSigma(sigma);
                        for (GroupCircle circle : circles) {
                            center_x.add(circle.getCenterLocation().getX());
                            center_y.add(circle.getCenterLocation().getY());
                            circle_r.add(circle.getRadius());
                            sigma.add(MetricsUtils_Phase1.computeSigmaFromCircleRadius(circle.getRadius()));
                        }
                    }
                }
            } else if (trial instanceof Task_3_ProbeTrial) {
                GroupCentersProbeResponse groupCenters = ((Task_3_ProbeTrialResponse) response).getGroupCentersResponse();
                if (groupCenters != null) {
                    if (groupCenters.getTrialPartTime_ms() != null) {
                        trialData.setCircles_centers_time(groupCenters.getTrialPartTime_ms().doubleValue());
                    }
                    if (groupCenters.getGroupCenters() != null && !groupCenters.getGroupCenters().isEmpty()) {
                        ArrayList<GroupCenterResponse> centers = groupCenters.getGroupCenters();
                        ArrayList<Double> center_x = new ArrayList<Double>(centers.size());
                        ArrayList<Double> center_y = new ArrayList<Double>(centers.size());
                        trialData.setCenter_x(center_x);
                        trialData.setCenter_y(center_y);
                        for (GroupCenterResponse center : centers) {
                            center_x.add(center.getLocation().getX());
                            center_y.add(center.getLocation().getY());
                        }
                    }
                }
            }

            //Get normative base rates and group circles/centers
            if (trial.getAttackDispersionParameters() != null && !trial.getAttackDispersionParameters().isEmpty()) {
                ArrayList<Task_1_2_3_AttackDispersionParameters> parameters = trial.getAttackDispersionParameters();
                ArrayList<Double> normative_base_rate = new ArrayList<Double>(parameters.size());
                ArrayList<Double> normative_center_x = new ArrayList<Double>(parameters.size());
                ArrayList<Double> normative_center_y = new ArrayList<Double>(parameters.size());
                ArrayList<Double> normative_sigma = new ArrayList<Double>(parameters.size());
                trialData.setNormative_base_rate(normative_base_rate);
                trialData.setNormative_center_x(normative_center_x);
                trialData.setNormative_center_y(normative_center_y);
                trialData.setNormative_sigma(normative_sigma);
                for (Task_1_2_3_AttackDispersionParameters params : parameters) {
                    normative_base_rate.add(params.getBaseRate());
                    normative_center_x.add(params.getCenterLocation().getX());
                    normative_center_y.add(params.getCenterLocation().getY());
                    normative_sigma.add(params.getSigmaX());
                }
            }

            //Get troop selection
            if (response.getTroopSelectionResponse() != null) {
                if (response.getTroopSelectionResponse().getTrialPartTime_ms() != null) {
                    trialData.setAllocation_time(response.getTroopSelectionResponse().getTrialPartTime_ms().doubleValue());
                }
                if (trial.getTroopSelectionProbe() != null && trial.getTroopSelectionProbe().getGroups() != null
                        && !trial.getTroopSelectionProbe().getGroups().isEmpty()) {
                    ArrayList<Double> allocation = new ArrayList<Double>(trial.getTroopSelectionProbe().getGroups().size());
                    trialData.setAllocation(allocation);
                    for (GroupType group : trial.getTroopSelectionProbe().getGroups()) {
                        if (group == response.getTroopSelectionResponse().getGroup()) {
                            allocation.add(100d);
                        } else {
                            allocation.add(0d);
                        }
                    }
                }
            }

            //Get surprise
            Integer surpriseVal = null;
            if (response.getGroundTruthSurpriseResponse() != null) {
                if (response.getGroundTruthSurpriseResponse().getTrialPartTime_ms() != null) {
                    trialData.setSurprise_time(response.getGroundTruthSurpriseResponse().getTrialPartTime_ms().doubleValue());
                }
                surpriseVal = response.getGroundTruthSurpriseResponse().getSurpriseVal();
                if (surpriseVal != null) {
                    trialData.setSurprise(response.getGroundTruthSurpriseResponse().getSurpriseVal().doubleValue());
                }
            }
            //Set a warning that ground truth surprise was missing
            if (surpriseVal == null) {
                if (trialData.getWarnings() == null) {
                    trialData.setWarnings("Missing ground truth surprise");
                } else {
                    trialData.setWarnings(trialData.getWarnings().concat("; Missing ground truth surprise"));
                }
            }

            //Get ground truth
            if (trial.getGroundTruth() != null && trial.getGroundTruth().getResponsibleGroup() != null) {
                trialData.setGround_truth(trial.getGroundTruth().getResponsibleGroup().toString());
            }
        }
    }

    /**
     * @param trial
     * @param trialData
     */
    protected void extractTrialData(Task_4_Trial trial, TrialData trialData) {
        if (trial.getTrialResponse() != null) {
            Task_4_TrialResponse response = trial.getTrialResponse();

            //Get trial time
            if (response.getTrialTime_ms() != null) {
                trialData.setTrial_time(response.getTrialTime_ms().doubleValue());
            }

            //Get normalized subject probabilities and normative probabilities (percent format)
            ArrayList<Double> probs_time = new ArrayList<Double>();
            ArrayList<Probabilities> probs = new ArrayList<Probabilities>();
            ArrayList<Probabilities> normative_probs = new ArrayList<Probabilities>();
            trialData.setProbs_time(probs_time);
            trialData.setProbs(probs);
            trialData.setNormative_probs(normative_probs);
            if (response.getAttackLocationResponse_initial() != null) {
                if (response.getAttackLocationResponse_initial().getTrialPartTime_ms() != null) {
                    probs_time.add(response.getAttackLocationResponse_initial().getTrialPartTime_ms().doubleValue());
                } else {
                    probs_time.add(null);
                }
                if (response.getAttackLocationResponse_initial().getGroupAttackProbabilities() != null
                        && !response.getAttackLocationResponse_initial().getGroupAttackProbabilities().isEmpty()) {
                    probs.add(new Probabilities(getNormalizedSubjectProbs(response.getAttackLocationResponse_initial().getGroupAttackProbabilities())));
                } else {
                    probs.add(null);
                }
                if (response.getAttackLocationResponse_initial().getNormativeProbs_cumulativeBayesian() != null
                        && !response.getAttackLocationResponse_initial().getNormativeProbs_cumulativeBayesian().isEmpty()) {
                    normative_probs.add(new Probabilities(response.getAttackLocationResponse_initial().getNormativeProbs_cumulativeBayesian()));
                } else {
                    normative_probs.add(null);
                }
            } else {
                probs_time.add(null);
                probs.add(null);
                probs.add(null);
            }
            if (response.getAttackLocationResponses_afterINTs() != null && !response.getAttackLocationResponses_afterINTs().isEmpty()) {
                for (Task_4_7_AttackLocationProbeResponseAfterINT intProbs : response.getAttackLocationResponses_afterINTs()) {
                    if (intProbs.getAttackLocationResponse() != null) {
                        if (intProbs.getAttackLocationResponse().getTrialPartTime_ms() != null) {
                            probs_time.add(intProbs.getAttackLocationResponse().getTrialPartTime_ms().doubleValue());
                        } else {
                            probs_time.add(null);
                        }
                        if (intProbs.getAttackLocationResponse().getGroupAttackProbabilities() != null
                                && !intProbs.getAttackLocationResponse().getGroupAttackProbabilities().isEmpty()) {
                            probs.add(new Probabilities(getNormalizedSubjectProbs(intProbs.getAttackLocationResponse().getGroupAttackProbabilities())));
                        } else {
                            probs.add(null);
                        }
                        if (intProbs.getAttackLocationResponse().getNormativeProbs_cumulativeBayesian() != null
                                && !intProbs.getAttackLocationResponse().getNormativeProbs_cumulativeBayesian().isEmpty()) {
                            normative_probs.add(new Probabilities(intProbs.getAttackLocationResponse().getNormativeProbs_cumulativeBayesian()));
                        } else {
                            normative_probs.add(null);
                        }
                    }
                }
            }

            //Get INT layers presented (should be just SOCINT)
            if (response.getAttackLocationResponses_afterINTs() != null && !response.getAttackLocationResponses_afterINTs().isEmpty()) {
                ArrayList<String> layer_type = new ArrayList<String>(response.getAttackLocationResponses_afterINTs().size());
                trialData.setLayer_type(layer_type);
                for (Task_4_7_AttackLocationProbeResponseAfterINT intResponse : response.getAttackLocationResponses_afterINTs()) {
                    layer_type.add(MetricsUtils_Phase1.formatLayerName(intResponse.getIntLayerShown().getLayerType()));
                }
            }

            //Get troop allocations
            if (response.getTroopAllocationResponse() != null) {
                if (response.getTroopAllocationResponse().getTrialPartTime_ms() != null) {
                    trialData.setAllocation_time(response.getTroopAllocationResponse().getTrialPartTime_ms().doubleValue());
                }
                if (response.getTroopAllocationResponse().getTroopAllocations() != null
                        && !response.getTroopAllocationResponse().getTroopAllocations().isEmpty()) {
                    ArrayList<Double> allocation = new ArrayList<Double>(response.getTroopAllocationResponse().getTroopAllocations().size());
                    trialData.setAllocation(allocation);
                    for (TroopAllocation alloc : response.getTroopAllocationResponse().getTroopAllocations()) {
                        allocation.add(alloc.getAllocation());
                    }
                }
            }

            //Get surprise
            if (response.getGroundTruthSurpriseResponse() != null) {
                if (response.getGroundTruthSurpriseResponse().getTrialPartTime_ms() != null) {
                    trialData.setSurprise_time(response.getGroundTruthSurpriseResponse().getTrialPartTime_ms().doubleValue());
                }
                if (response.getGroundTruthSurpriseResponse().getSurpriseVal() != null) {
                    trialData.setSurprise(response.getGroundTruthSurpriseResponse().getSurpriseVal().doubleValue());
                }
            }

            //Get ground truth
            if (trial.getGroundTruth() != null) {
                trialData.setGround_truth(trial.getGroundTruth().getAttackLocationId());
            }
        }
    }

    /**
     * @param trial
     * @param trialData
     */
    @SuppressWarnings("deprecation")
    protected void extractTrialData(Task_5_Trial trial, TrialData trialData) {
        if (trial.getTrialResponse() != null) {
            Task_5_6_TrialResponse response = trial.getTrialResponse();

            //Get trial time
            if (response.getTrialTime_ms() != null) {
                trialData.setTrial_time(response.getTrialTime_ms().doubleValue());
            }

            //Get normalized subject probabilities and normative probabilities (percent format)
            ArrayList<Double> probs_time = new ArrayList<Double>();
            ArrayList<Probabilities> probs = new ArrayList<Probabilities>();
            ArrayList<Probabilities> normative_probs = new ArrayList<Probabilities>();
            trialData.setProbs_time(probs_time);
            trialData.setProbs(probs);
            trialData.setNormative_probs(normative_probs);
            if (response.getAttackLocationResponse_initial() != null) {
                if (response.getAttackLocationResponse_initial().getTrialPartTime_ms() != null) {
                    probs_time.add(response.getAttackLocationResponse_initial().getTrialPartTime_ms().doubleValue());
                } else {
                    probs_time.add(null);
                }
                if (response.getAttackLocationResponse_initial().getGroupAttackProbabilities() != null
                        && !response.getAttackLocationResponse_initial().getGroupAttackProbabilities().isEmpty()) {
                    probs.add(new Probabilities(getNormalizedSubjectProbs(response.getAttackLocationResponse_initial().getGroupAttackProbabilities())));
                } else {
                    probs.add(null);
                }
                if (response.getAttackLocationResponse_initial().getNormativeProbs_cumulativeBayesian() != null
                        && !response.getAttackLocationResponse_initial().getNormativeProbs_cumulativeBayesian().isEmpty()) {
                    normative_probs.add(new Probabilities(response.getAttackLocationResponse_initial().getNormativeProbs_cumulativeBayesian()));
                } else {
                    normative_probs.add(null);
                }
            } else if (trial.getInitialHumintReport() != null) {
                probs_time.add(0d);
                probs.add(new Probabilities(trial.getInitialHumintReport().getHumintProbabilities()));
                normative_probs.add(new Probabilities(trial.getInitialHumintReport().getHumintProbabilities()));
            } else {
                probs_time.add(null);
                probs.add(null);
                probs.add(null);
            }
            if (response.getAttackLocationResponses_afterINTs() != null && !response.getAttackLocationResponses_afterINTs().isEmpty()) {
                for (Task_5_6_AttackLocationProbeResponseAfterINT intProbs : response.getAttackLocationResponses_afterINTs()) {
                    if (intProbs != null && intProbs.getAttackLocationResponse() != null) {
                        if (intProbs.getAttackLocationResponse().getTrialPartTime_ms() != null) {
                            probs_time.add(intProbs.getAttackLocationResponse().getTrialPartTime_ms().doubleValue());
                        } else {
                            probs_time.add(null);
                        }
                        if (intProbs.getAttackLocationResponse().getGroupAttackProbabilities() != null
                                && !intProbs.getAttackLocationResponse().getGroupAttackProbabilities().isEmpty()) {
                            probs.add(new Probabilities(getNormalizedSubjectProbs(intProbs.getAttackLocationResponse().getGroupAttackProbabilities())));
                        } else {
                            probs.add(null);
                        }
                        if (intProbs.getAttackLocationResponse().getNormativeProbs_cumulativeBayesian() != null
                                && !intProbs.getAttackLocationResponse().getNormativeProbs_cumulativeBayesian().isEmpty()) {
                            normative_probs.add(new Probabilities(intProbs.getAttackLocationResponse().getNormativeProbs_cumulativeBayesian()));
                        } else {
                            normative_probs.add(null);
                        }
                    }
                }
            }

            //Get INT layers presented (Task 5) or selected (Task 6)
            if (response.getAttackLocationResponses_afterINTs() != null && !response.getAttackLocationResponses_afterINTs().isEmpty()) {
                ArrayList<String> layer_type = new ArrayList<String>(response.getAttackLocationResponses_afterINTs().size());
                ArrayList<IntLayer> layers = new ArrayList<IntLayer>(layer_type.size());
                trialData.setLayer_type(layer_type);
                for (Task_5_6_AttackLocationProbeResponseAfterINT intResponse : response.getAttackLocationResponses_afterINTs()) {
                    IntLayer layer = intResponse != null && intResponse.getIntLayerShown() != null
                            ? intResponse.getIntLayerShown().getLayerType() : null;
                    layers.add(layer);
                    layer_type.add(MetricsUtils_Phase1.formatLayerName(layer));

                }
                //Get the layer sequence index
                trialData.setLs_index(MetricsUtils_Phase1.getLayerSequenceIndex(layers, MetricsUtils_Phase1.LAYER_SEQUENCES));
            }

            //Get troop allocations
            if (response.getTroopAllocationResponse() != null) {
                if (response.getTroopAllocationResponse().getTrialPartTime_ms() != null) {
                    trialData.setAllocation_time(response.getTroopAllocationResponse().getTrialPartTime_ms().doubleValue());
                }
                if (response.getTroopAllocationResponse().getTroopAllocations() != null
                        && !response.getTroopAllocationResponse().getTroopAllocations().isEmpty()) {
                    ArrayList<Double> allocation = new ArrayList<Double>(response.getTroopAllocationResponse().getTroopAllocations().size());
                    trialData.setAllocation(allocation);
                    for (TroopAllocation alloc : response.getTroopAllocationResponse().getTroopAllocations()) {
                        allocation.add(alloc.getAllocation());
                    }
                }
            }

            //Get surprise
            if (response.getGroundTruthSurpriseResponse() != null) {
                if (response.getGroundTruthSurpriseResponse().getTrialPartTime_ms() != null) {
                    trialData.setSurprise_time(response.getGroundTruthSurpriseResponse().getTrialPartTime_ms().doubleValue());
                }
                if (response.getGroundTruthSurpriseResponse().getSurpriseVal() != null) {
                    trialData.setSurprise(response.getGroundTruthSurpriseResponse().getSurpriseVal().doubleValue());
                }
            }

            //Get ground truth
            if (trial.getGroundTruth() != null) {
                trialData.setGround_truth(trial.getGroundTruth().getAttackLocationId());
            }
        }
    }

    /**
     * @param subjectProbs
     * @return
     */
    private ArrayList<Double> getNormalizedSubjectProbs(List<GroupAttackProbabilityResponse> subjectProbs) {
        ArrayList<Double> normalizedProbs = new ArrayList<Double>(subjectProbs.size());
        for (GroupAttackProbabilityResponse prob : subjectProbs) {
            normalizedProbs.add(prob.getProbability());
        }
        //System.out.println("normalized probs before: " + normalizedProbs);
        ProbabilityUtils.normalizePercentProbabilities_Double(normalizedProbs, normalizedProbs);
        //System.out.println("normalized probs after: " + normalizedProbs);
        return normalizedProbs;
    }

    /**
     * @param trialData
     * @param metricsInfo
     * @param comparisonTrialData
     * @return
     */
    public TrialData updateTrialMetrics(TrialData trialData, MetricsInfo metricsInfo, TrialData comparisonTrialData) {
        if (trialData != null) {
            trialData.setMetrics(trialMetricsComputer.updateTrialMetrics(trialData, metricsInfo, comparisonTrialData));
        }
        return trialData;
    }
}
