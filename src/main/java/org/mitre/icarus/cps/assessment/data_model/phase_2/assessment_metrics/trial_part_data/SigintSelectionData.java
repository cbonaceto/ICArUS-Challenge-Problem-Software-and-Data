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

import java.util.LinkedList;
import java.util.List;

import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.NameValuePair;
import org.mitre.icarus.cps.assessment.utils.phase_2.AssessmentUtils_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.int_presentation.SigintSelectionProbe;

/**
 * Contains response data for a SIGINT selection probe.
 * 
 * @author CBONACETO
 *
 */
public class SigintSelectionData extends TrialPartData<SigintSelectionProbe> {

    private static final long serialVersionUID = -7474901694052648519L;

    /**
     * The id(s) of the location SIGINT was selected at
     */
    protected List<String> sigintLocations;

    /**
     * The id(s) of the optimal location(s) to obtain SIGINT at using the
     * participant's Red attack probabilities
     */
    protected List<String> sigintLocationsParticipantOptimal;

    /**
     * The id(s) of the optimal location(s) to obtain SIGINT at using the
     * Bayesian Red attack probabilities
     */
    protected List<String> sigintLocationsBayesianOptimal;

    /**
     * Whether SIGINT was selected at the optimal location(s) using the
     * participant's Red attack probabilities
     */
	//protected Boolean sigintSelectionParticipantOptimal;
    
    /**
     * Whether SIGINT was selected at the optimal location(s) using the Bayesian
     * Red attack probabilities
     */
    //protected Boolean sigintSelectionBayesianOptimal;	
    
    public SigintSelectionData() {
    }

    public SigintSelectionData(SigintSelectionProbe trialPartProbe) {
        super(trialPartProbe);
    }

    /*public Boolean getSigintSelectionParticipantOptimal() {
     return sigintSelectionParticipantOptimal;
     }

     public void setSigintSelectionParticipantOptimal(
     Boolean sigintSelectionParticipantOptimal) {
     this.sigintSelectionParticipantOptimal = sigintSelectionParticipantOptimal;
     }

     public Boolean getSigintSelectionBayesianOptimal() {
     return sigintSelectionBayesianOptimal;
     }

     public void setSigintSelectionBayesianOptimal(
     Boolean sigintSelectionBayesianOptimal) {
     this.sigintSelectionBayesianOptimal = sigintSelectionBayesianOptimal;
     }*/
    
    public List<String> getSigintLocations() {
        return sigintLocations;
    }

    public void setSigintLocations(List<String> sigintLocations) {
        this.sigintLocations = sigintLocations;
    }

    public List<String> getSigintLocationsParticipantOptimal() {
        return sigintLocationsParticipantOptimal;
    }

    public void setSigintLocationsParticipantOptimal(
            List<String> sigintLocationsParticipantOptimal) {
        this.sigintLocationsParticipantOptimal = sigintLocationsParticipantOptimal;
    }

    public List<String> getSigintLocationsBayesianOptimal() {
        return sigintLocationsBayesianOptimal;
    }

    public void setSigintLocationsBayesianOptimal(
            List<String> sigintLocationsBayesianOptimal) {
        this.sigintLocationsBayesianOptimal = sigintLocationsBayesianOptimal;
    }

    @Override
    protected void initializeTrialPartData(SigintSelectionProbe trialPartProbe) {
        if (trialPartProbe != null) {
            sigintLocations = trialPartProbe.getSelectedLocationIds();
            sigintLocationsParticipantOptimal = trialPartProbe.getNormativeParticipantLocationIds();
            sigintLocationsBayesianOptimal = trialPartProbe.getNormativeBayesianLocationIds();
            /*if(sigintLocations != null && !sigintLocations.isEmpty()) {				
             if(trialPartProbe.getNormativeParticipantLocationIds() != null &&
             trialPartProbe.getNormativeParticipantLocationIds().size() == sigintLocations.size()) {
             sigintSelectionParticipantOptimal = true;
             Iterator<String> selectedLocationIter = sigintLocations.iterator();
             Iterator<String> optimalLocationIter = trialPartProbe.getNormativeParticipantLocationIds().iterator();
             while(selectedLocationIter.hasNext() && sigintSelectionParticipantOptimal) {
             String selectedLocation = selectedLocationIter.next();
             String optimalLocation = optimalLocationIter.next();
             if(!selectedLocation.equals(optimalLocation)) {
             sigintSelectionParticipantOptimal = false;
             }
             }
             }
             if(trialPartProbe.getNormativeBayesianLocationIds() != null &&
             trialPartProbe.getNormativeBayesianLocationIds().size() == sigintLocations.size()) {
             sigintSelectionBayesianOptimal = true;
             Iterator<String> selectedLocationIter = sigintLocations.iterator();
             Iterator<String> optimalLocationIter = trialPartProbe.getNormativeBayesianLocationIds().iterator();
             while(selectedLocationIter.hasNext() && sigintSelectionBayesianOptimal) {
             String selectedLocation = selectedLocationIter.next();
             String optimalLocation = optimalLocationIter.next();
             if(!selectedLocation.equals(optimalLocation)) {
             sigintSelectionBayesianOptimal = false;
             }
             }
             }
             }*/
        }
    }

    @Override
    protected List<NameValuePair> getAdditionalDataValuesAsString(
            List<NameValuePair> dataValues) {
        String id = trialPartType != null ? trialPartType.getId() : "sigint";
        if (dataValues == null) {
            dataValues = new LinkedList<NameValuePair>();
        }
        if (sigintLocations != null && !sigintLocations.isEmpty()) {
            int i = 1;
            for (String locationId : sigintLocations) {
                dataValues.add(new NameValuePair(
                        id + "_location_" + Integer.toString(i),
                        AssessmentUtils_Phase2.parseLocationIndexAsString(locationId)));
                i++;
            }
        }
        if (sigintLocationsParticipantOptimal != null && !sigintLocationsParticipantOptimal.isEmpty()) {
            int i = 1;
            for (String locationId : sigintLocationsParticipantOptimal) {
                dataValues.add(new NameValuePair(
                        id + "_participant_optimal_location_" + Integer.toString(i),
                        AssessmentUtils_Phase2.parseLocationIndexAsString(locationId)));
                i++;
            }
        }
        if (sigintLocationsBayesianOptimal != null && !sigintLocationsBayesianOptimal.isEmpty()) {
            int i = 1;
            for (String locationId : sigintLocationsBayesianOptimal) {
                dataValues.add(new NameValuePair(
                        id + "_bayesian_optimal_location_" + Integer.toString(i),
                        AssessmentUtils_Phase2.parseLocationIndexAsString(locationId)));
                i++;
            }
        }
        /*dataValues.add(new NameValuePair(
         id + "_participant_optimal", 
         sigintSelectionParticipantOptimal == null || !sigintSelectionParticipantOptimal ? "0" : "1"));
         dataValues.add(new NameValuePair(
         id + "_bayesian_optimal", 
         sigintSelectionBayesianOptimal == null || !sigintSelectionBayesianOptimal ? "0" : "1"));*/
        return dataValues;
    }
}
