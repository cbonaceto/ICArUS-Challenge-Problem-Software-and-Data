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
package org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.trial_part_data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.NameValuePair;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.BlueAction;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.BlueAction.BlueActionType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.BlueActionSelectionProbe;

/**
 * Contains response data for a Blue action selection probe.
 *
 * @author CBONACETO
 *
 */
public class BlueActionSelectionData extends TrialPartData<BlueActionSelectionProbe> {

    private static final long serialVersionUID = 4491618320623898726L;

    /**
     * The participant Blue action permutation, which is the Blue action selected at each location
     */
    protected List<BlueActionType> blueActions;
    
    /** 
     * The index of the participant Blue action permutation in the list of all possible Blue action permutations
     */
    //protected Integer blueActionsIndex;

    /**
     * The expected utility of the participant Blue action permutation
     */
    protected Double expectedUtility;

    /**
     * The normative participant Blue action permutation, which is the normative Blue action
     * selection based on the participant's probabilities
     */
    protected List<BlueActionType> blueActions_normativeParticipant;

    /**
     * The expected utility of the normative participant Blue action permutation
     */
    protected Double normativeParticipantExpectedUtility;

    /**
     * The normative Bayesian Blue action permutation, which is the normative Blue action
     * selection based on the Bayesian probabilities
     */
    protected List<BlueActionType> blueActions_normativeBayesian;

    /**
     * The expected utility of the normative Baysian Blue action permutation
     */
    protected Double normativeBayesianExpectedUtility;

    public BlueActionSelectionData() {
    }

    public BlueActionSelectionData(BlueActionSelectionProbe trialPartProbe) {
        super(trialPartProbe);
    }

    public List<BlueActionType> getBlueActions() {
        return blueActions;
    }

    public void setBlueActions(List<BlueActionType> blueActions) {
        this.blueActions = blueActions;
    }

    public Double getExpectedUtility() {
        return expectedUtility;
    }

    public void setExpectedUtility(Double expectedUtility) {
        this.expectedUtility = expectedUtility;
    }

    public List<BlueActionType> getBlueActions_normativeParticipant() {
        return blueActions_normativeParticipant;
    }

    public void setBlueActions_normativeParticipant(
            List<BlueActionType> blueActions_normativeParticipant) {
        this.blueActions_normativeParticipant = blueActions_normativeParticipant;
    }

    public Double getNormativeParticipantExpectedUtility() {
        return normativeParticipantExpectedUtility;
    }

    public void setNormativeParticipantExpectedUtility(
            Double normativeParticipantExpectedUtility) {
        this.normativeParticipantExpectedUtility = normativeParticipantExpectedUtility;
    }

    public List<BlueActionType> getBlueActions_normativeBayesian() {
        return blueActions_normativeBayesian;
    }

    public void setBlueActions_normativeBayesian(
            List<BlueActionType> blueActions_normativeBayesian) {
        this.blueActions_normativeBayesian = blueActions_normativeBayesian;
    }

    public Double getNormativeBaysianExpectedUtility() {
        return normativeBayesianExpectedUtility;
    }

    public void setNormativeBaysianExpectedUtility(
            Double normativeBaysianExpectedUtility) {
        this.normativeBayesianExpectedUtility = normativeBaysianExpectedUtility;
    }

    @Override
    protected void initializeTrialPartData(BlueActionSelectionProbe trialPartProbe) {
        if (trialPartProbe != null) {
            //Get the participant-selected Blue actions
            if (trialPartProbe.getBlueActions() != null
                    && !trialPartProbe.getBlueActions().isEmpty()) {
                blueActions = new ArrayList<BlueActionType>(trialPartProbe.getBlueActions().size());
                for (BlueAction blueAction : trialPartProbe.getBlueActions()) {
                    blueActions.add(blueAction.getAction());
                }
            }
            expectedUtility = trialPartProbe.getExpectedUtility();

            //Get the normative participant Blue actions
            if (trialPartProbe.getNormativeParticipantBlueActions() != null
                    && !trialPartProbe.getNormativeParticipantBlueActions().isEmpty()) {
                blueActions_normativeParticipant = new ArrayList<BlueActionType>(
                        trialPartProbe.getNormativeParticipantBlueActions().size());
                for (BlueAction blueAction : trialPartProbe.getNormativeParticipantBlueActions()) {
                    blueActions_normativeParticipant.add(blueAction.getAction());
                }
            }
            normativeParticipantExpectedUtility = trialPartProbe.getNormativeParticipantExpectedUtility();

            //Get the normative Bayesian Blue actions
            if (trialPartProbe.getNormativeBayesianBlueActions() != null
                    && !trialPartProbe.getNormativeBayesianBlueActions().isEmpty()) {
                blueActions_normativeBayesian = new ArrayList<BlueActionType>(
                        trialPartProbe.getNormativeBayesianBlueActions().size());
                for (BlueAction blueAction : trialPartProbe.getNormativeBayesianBlueActions()) {
                    blueActions_normativeBayesian.add(blueAction.getAction());
                }
            }
            normativeBayesianExpectedUtility = trialPartProbe.getNormativeBayesianExpectedUtility();
        }
    }

    @Override
    protected List<NameValuePair> getAdditionalDataValuesAsString(
            List<NameValuePair> dataValues) {
        String id = trialPartType != null ? trialPartType.getId() : "blue_action";
        if (dataValues == null) {
            dataValues = new LinkedList<NameValuePair>();
        }

        if (blueActions != null && !blueActions.isEmpty()) {
            int locationIndex = 1;
            for (BlueActionType blueAction : blueActions) {
                dataValues.add(new NameValuePair(
                        id + "_" + Integer.toString(locationIndex),
                        blueAction != null ? blueAction.toString() : null));
                locationIndex++;
            }
            dataValues.add(new NameValuePair(
                    id + "_expected_utility",
                    expectedUtility != null ? expectedUtility.toString() : null));
        }

        if (blueActions_normativeParticipant != null && !blueActions_normativeParticipant.isEmpty()) {
            int locationIndex = 1;
            for (BlueActionType blueAction : blueActions_normativeParticipant) {
                dataValues.add(new NameValuePair(
                        id + "_normative_participant_" + Integer.toString(locationIndex),
                        blueAction != null ? blueAction.toString() : null));
                locationIndex++;
            }
            dataValues.add(new NameValuePair(
                    id + "_normative_participant_expected_utility",
                    normativeParticipantExpectedUtility != null ? normativeParticipantExpectedUtility.toString() : null));
        }

        if (blueActions_normativeBayesian != null && !blueActions_normativeBayesian.isEmpty()) {
            int locationIndex = 1;
            for (BlueActionType blueAction : blueActions_normativeBayesian) {
                dataValues.add(new NameValuePair(
                        id + "_normative_bayesian_" + Integer.toString(locationIndex),
                        blueAction != null ? blueAction.toString() : null));
                locationIndex++;
            }
            dataValues.add(new NameValuePair(
                    id + "_normative_bayesian_expected_utility",
                    normativeBayesianExpectedUtility != null ? normativeBayesianExpectedUtility.toString() : null));
        }
        return dataValues;
    }
}
