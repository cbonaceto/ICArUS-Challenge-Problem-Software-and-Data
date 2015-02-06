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
package org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import org.mitre.icarus.cps.assessment.data_model.base.DataType;

import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractTrialData;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.trial_part_data.AttackProbabilityReportData;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.trial_part_data.BlueActionSelectionData;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.trial_part_data.BluebookProbabilitiesData;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.trial_part_data.RedTacticsReportData;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.trial_part_data.SigintSelectionData;
import org.mitre.icarus.cps.exam.base.response.ResponseGeneratorData;
import org.mitre.icarus.cps.exam.phase_2.testing.IcarusTestTrial_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.MissionType;
import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.BlueBook;
import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.RedTactic;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbeType;

/**
 * Contains response data for a trial in a mission in a Phase 2 exam.
 *
 * @author CBONACETO
 *
 */
public class TrialData extends AbstractTrialData<TrialMetrics> {

    private static final long serialVersionUID = -4378561885376897717L;
    
    /** Normative solution types */
    public static enum NormativeSolutionType{Cumulative, Incremental};

    /**
     * The trial the trial data is from
     */
    protected IcarusTestTrial_Phase2 trial;

    /**
     * A flag indicating whether the trial data is stale
     */
    protected Boolean data_stale;

    /**
     * The number of Blue locations
     */
    protected Integer numBlueLocations;

    /**
     * The Blue location IDs
     */
    protected List<String> blueLocationIds;    

    /**
     * The probability that Red has the capability to attack for all locations
     * (Pc, from HUMINT)
     */
    protected Double redCapability_Pc;

    /**
     * The probability that Blue will defeat Red if Red attacks at each location
     * (P, from OSINT)
     */
    protected List<Double> redVulnerability_P;

    /**
     * The utility of attack for Red, based on building density at each location
     * (U, from IMINT)
     */
    protected List<Integer> redOpportunity_U;

    /**
     * Whether Red activity was detected at each location (from SIGINT)
     */
    protected List<Boolean> redActivityDetected;

    /**
     * The Red tactics report
     */
    protected RedTacticsReportData redTacticsReport;

    /**
     * The Red attack probabilities for each possible Red tactic may be playing
     * with (from the BLUEBOOK)
     */
    protected List<BluebookProbabilitiesData> bluebookProbabilities;

    /**
     * The attack probability reports
     */
    protected List<AttackProbabilityReportData> attackProbabilityReports;

    /**
     * The SIGINT selection
     */
    protected SigintSelectionData sigintSelection;

    /**
     * The Blue action selection
     */
    protected BlueActionSelectionData blueActionSelection;

    /**
     * Default constructor required for querying
     */
    public TrialData() {
    }

    /**
     *
     * @param trial
     * @param responseGenerator
     * @param data_type
     * @param exam_id
     * @param mission_id
     * @param mission_number
     */
    public TrialData(IcarusTestTrial_Phase2 trial, ResponseGeneratorData responseGenerator, DataType data_type,
            String exam_id, String mission_id, Integer mission_number) {
        super(responseGenerator, data_type, exam_id, mission_id, mission_number);
        if (trial != null) {
            trial_number = trial.getTrialNum();
        }
        this.trial = trial;
    }    

    @XmlTransient
    public IcarusTestTrial_Phase2 getTrial() {
        return trial;
    }

    /**
     * @param trial
     */
    public void setTrial(IcarusTestTrial_Phase2 trial) {
        this.trial = trial;
    }

    public Boolean isData_stale() {
        return data_stale;
    }

    public void setData_stale(Boolean data_stale) {
        this.data_stale = data_stale;
    }

    public List<String> getBlueLocationIds() {
        return blueLocationIds;
    }

    public void setBlueLocationIds(List<String> blueLocationIds) {
        this.blueLocationIds = blueLocationIds;
    }  

    public Integer getNumBlueLocations() {
        return numBlueLocations;
    }

    public void setNumBlueLocations(Integer numBlueLocations) {
        this.numBlueLocations = numBlueLocations;
    }

    public Double getRedCapability_Pc() {
        return redCapability_Pc;
    }

    public void setRedCapability_Pc(Double redCapability_Pc) {
        this.redCapability_Pc = redCapability_Pc;
    }

    public List<Double> getRedVulnerability_P() {
        return redVulnerability_P;
    }

    public void setRedVulnerability_P(List<Double> redVulnerability_P) {
        this.redVulnerability_P = redVulnerability_P;
    }

    public List<Integer> getRedOpportunity_U() {
        return redOpportunity_U;
    }

    public void setRedOpportunity_U(List<Integer> redOpportunity_U) {
        this.redOpportunity_U = redOpportunity_U;
    }

    public List<Boolean> getRedActivityDetected() {
        return redActivityDetected;
    }

    public void setRedActivityDetected(List<Boolean> redActivityDetected) {
        this.redActivityDetected = redActivityDetected;
    }

    public RedTacticsReportData getRedTacticsReport() {
        return redTacticsReport;
    }

    public void setRedTacticsReport(RedTacticsReportData redTacticsReport) {
        this.redTacticsReport = redTacticsReport;
    }

    /**
     * @param missionType
     * @param blueBook
     * @param numLocations
     * @param redVulnerability_P
     * @param redOpportunity_U
     */
    public void setBlueBookData(MissionType missionType, BlueBook blueBook, int numLocations,
            List<Double> redVulnerability_P, List<Integer> redOpportunity_U) {
        if (missionType != null && blueBook != null) {
            List<RedTactic> redTactics = blueBook.getRedTaticsForMission(missionType);
            if (redTactics != null && !redTactics.isEmpty()) {
                bluebookProbabilities = new ArrayList<BluebookProbabilitiesData>(redTactics.size());
                for (RedTactic redTactic : redTactics) {
                    BluebookProbabilitiesData currBluebookProbs = new BluebookProbabilitiesData(
                            redTactic.getTacticType());
                    List<Double> probs = new ArrayList<Double>(numLocations);
                    for (int i = 0; i < numLocations; i++) {
                        Double currentP = redVulnerability_P != null && i < redVulnerability_P.size() ? redVulnerability_P.get(i) : null;
                        Integer currentU = redOpportunity_U != null && i < redOpportunity_U.size() ? redOpportunity_U.get(i) : null;
                        probs.add(redTactic.getTacticParameters().getAttackProbability(currentP, currentU));
                    }
                    currBluebookProbs.setBluebookProbabilities(probs);
                    bluebookProbabilities.add(currBluebookProbs);
                }
            }
        }
    }

    public List<BluebookProbabilitiesData> getBluebookProbabilities() {
        return bluebookProbabilities;
    }

    public void setBluebookProbabilities(
            List<BluebookProbabilitiesData> bluebookProbabilities) {
        this.bluebookProbabilities = bluebookProbabilities;
    }

    public List<AttackProbabilityReportData> getAttackProbabilityReports() {
        return attackProbabilityReports;
    }

    public void setAttackProbabilityReports(
            List<AttackProbabilityReportData> attackProbabilityReports) {
        this.attackProbabilityReports = attackProbabilityReports;
    }
    
    /**
     * Get the attack probability report probe of the given type from the list
     * of attack probability report probes.
     * 
     * @param probeType the attack probability report probe type
     * @return t he attack probability report probe of the given type, or null
     * if it wasn't present
     */
    public AttackProbabilityReportData getAttackProbabilityReport(TrialPartProbeType probeType) {
        if(attackProbabilityReports != null && !attackProbabilityReports.isEmpty()) {
            for(AttackProbabilityReportData attackProbabilityReport : attackProbabilityReports) {
                if(attackProbabilityReport.getTrialPartType() == probeType) {
                    return attackProbabilityReport;
                }
            }
        }
        return null;
    }

    public SigintSelectionData getSigintSelection() {
        return sigintSelection;
    }

    public void setSigintSelection(SigintSelectionData sigintSelection) {
        this.sigintSelection = sigintSelection;
    }

    public BlueActionSelectionData getBlueActionSelection() {
        return blueActionSelection;
    }

    public void setBlueActionSelection(BlueActionSelectionData blueActionSelection) {
        this.blueActionSelection = blueActionSelection;
    }

    /**
     *
     * @param missionType
     * @param includeTrialPartId
     * @param includeTrialPartTime
     * @param includeParticipantGeneratedResponeFlag
     * @param includeTrialMetrics     
     * @return
     */
    public List<NameValuePair> getDataAndMetricValuesAsString(MissionType missionType,
            boolean includeTrialPartId, boolean includeTrialPartTime,
            boolean includeParticipantGeneratedResponeFlag, boolean includeTrialMetrics) {
        List<NameValuePair> dataValues = new LinkedList<NameValuePair>();
        dataValues.add(new NameValuePair("site_id", site_id));
        dataValues.add(new NameValuePair("response_generator_id", response_generator_id));
        dataValues.add(new NameValuePair("data_type",
                data_type != null ? data_type.toString() : null));
        dataValues.add(new NameValuePair("exam_id", exam_id));
        dataValues.add(new NameValuePair("task_number",
                task_number != null ? task_number.toString() : null));
        dataValues.add(new NameValuePair("trial_number",
                trial_number != null ? trial_number.toString() : null));
        dataValues.add(new NameValuePair("trial_time",
                trial_time != null ? trial_time.toString() : null));
        dataValues.add(new NameValuePair("num_blue_locations",
                numBlueLocations != null ? numBlueLocations.toString() : null));
        dataValues.add(new NameValuePair("red_capability_Pc",
                redCapability_Pc != null ? redCapability_Pc.toString() : null));
        if (redVulnerability_P != null && !redVulnerability_P.isEmpty()) {
            int i = 1;
            for (Double p : redVulnerability_P) {
                dataValues.add(new NameValuePair("red_vulnerability_P_" + i,
                        p != null ? p.toString() : null));
                i++;
            }
        }
        if (redOpportunity_U != null && !redOpportunity_U.isEmpty()) {
            int i = 1;
            for (Integer u : redOpportunity_U) {
                dataValues.add(new NameValuePair("red_opportunity_U_" + i,
                        u != null ? u.toString() : null));
                i++;
            }
        }
        if (redActivityDetected != null && !redActivityDetected.isEmpty()) {
            int i = 1;
            for (Boolean activityDetected : redActivityDetected) {
                dataValues.add(new NameValuePair("red_activity_detected_" + i,
                        activityDetected != null ? activityDetected ? "1" : "0" : null));
                i++;
            }
        }
        if (redTacticsReport != null) {
            redTacticsReport.getDataValuesAsString(dataValues, missionType,
                    includeTrialPartId, includeTrialPartTime,
                    includeParticipantGeneratedResponeFlag);
        }
        if (bluebookProbabilities != null && !bluebookProbabilities.isEmpty()) {
            for (BluebookProbabilitiesData bluebookProbs : bluebookProbabilities) {
                String tacticName = bluebookProbs.getRedTactic() != null ? bluebookProbs.getRedTactic().getName() : "unknownTactic";
                if (bluebookProbs.getBluebookProbabilities() != null) {
                    Integer i = 1;
                    for (Double probability : bluebookProbs.getBluebookProbabilities()) {
                        dataValues.add(new NameValuePair(
                                "bluebook_" + tacticName + "_prob_" + i.toString(),
                                probability != null ? probability.toString() : null));
                        i++;
                    }
                }
            }
        }
        if (attackProbabilityReports != null && !attackProbabilityReports.isEmpty()) {
            for (AttackProbabilityReportData attackProbabilityReport
                    : this.attackProbabilityReports) {
                attackProbabilityReport.getDataValuesAsString(dataValues, missionType,
                        includeTrialPartId, includeTrialPartTime,
                        includeParticipantGeneratedResponeFlag);
            }
        }
        if (sigintSelection != null) {
            sigintSelection.getDataValuesAsString(dataValues, missionType,
                    includeTrialPartId, includeTrialPartTime,
                    includeParticipantGeneratedResponeFlag);
        }
        if (blueActionSelection != null) {
            blueActionSelection.getDataValuesAsString(dataValues, missionType,
                    includeTrialPartId, includeTrialPartTime,
                    includeParticipantGeneratedResponeFlag);
        }
        if (includeTrialMetrics) {
            //TODO: Add the trial metrics	
        }

        return dataValues;
    }

    @Override
    @XmlElement
    public void setMetrics(TrialMetrics metrics) {
        super.setMetrics(metrics);
    }

    @Override
    public TrialMetrics getMetrics() {
        return super.getMetrics();
    }
}
