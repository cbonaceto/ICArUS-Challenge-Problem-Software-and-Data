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
package org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.significance;

import java.util.List;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.CFAMetricSignificanceReport;

/**
 * Contains significance reports for mission-level CFA metrics, and significance
 * reports for each trial for trial-level CFA metrics.
 * 
 * @author CBONACETO
 */
public class MissionSignificanceReport {    
    
    /** The mission number */
    private Integer mission_num;
    
    /** The mission id */
    private String mission_id;
    
    /** The number of subjects */
    private Integer num_subjects;
    
    /** PM (Probability Matching) significance report */
    private CFAMetricSignificanceReport PM_significance;
    
    /** CS (Confirmation Bias) significance report */
    private CFAMetricSignificanceReport CS_significance;
    
    /** SS (Satisfaction of Search) significance report */
    private CFAMetricSignificanceReport SS_significance;
    
    /** Trial metric significance reports for each trial */
    private List<TrialSignificanceReport> trialSignificanceReports;
    
    public MissionSignificanceReport() {}
    
    public MissionSignificanceReport(Integer mission_num, String mission_id,
            Integer num_subjects) {
        this.mission_num = mission_num;
        this.mission_id = mission_id;
        this.num_subjects = num_subjects;
    }

    public Integer getMission_num() {
        return mission_num;
    }

    public void setMission_num(Integer mission_num) {
        this.mission_num = mission_num;
    }

    public String getMission_id() {
        return mission_id;
    }

    public void setMission_id(String mission_id) {
        this.mission_id = mission_id;
    }

    public Integer getNum_subjects() {
        return num_subjects;
    }

    public void setNum_subjects(Integer num_subjects) {
        this.num_subjects = num_subjects;
    }

    public CFAMetricSignificanceReport getPM_significance() {
        return PM_significance;
    }

    public void setPM_significance(CFAMetricSignificanceReport PM_significance) {
        this.PM_significance = PM_significance;
    }

    public CFAMetricSignificanceReport getCS_significance() {
        return CS_significance;
    }

    public void setCS_significance(CFAMetricSignificanceReport CS_significance) {
        this.CS_significance = CS_significance;
    }

    public CFAMetricSignificanceReport getSS_significance() {
        return SS_significance;
    }

    public void setSS_significance(CFAMetricSignificanceReport SS_significance) {
        this.SS_significance = SS_significance;
    }

    public List<TrialSignificanceReport> getTrialSignificanceReports() {
        return trialSignificanceReports;
    }

    public void setTrialSignificanceReports(List<TrialSignificanceReport> trialSignificanceReports) {
        this.trialSignificanceReports = trialSignificanceReports;
    }
}