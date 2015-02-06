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

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.CFAMetricSignificanceReport;

/**
 * Contains significance reports for each mission in the exam.
 * 
 * @author CBONACETO
 */
public class ExamSignificanceReport {
    
    /** Report generation date/time */
    private Long time_stamp;    
    
    /** The exam ID */
    private String exam_id;
    
    /** Significance reports for each mission */
    private List<MissionSignificanceReport> missionSignificanceReports;
    
    protected static final String newline = "\n";
    
    protected static final NumberFormat nf = NumberFormat.getInstance();

    static {
        nf.setMaximumFractionDigits(5);
    }
    
    public ExamSignificanceReport() {}
    
    public ExamSignificanceReport(String exam_id) {
        this.exam_id = exam_id;
    }

    public Long getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(Long time_stamp) {
        this.time_stamp = time_stamp;
    }    

    public String getExam_id() {
        return exam_id;
    }

    public void setExam_id(String exam_id) {
        this.exam_id = exam_id;
    }    

    public List<MissionSignificanceReport> getMissionSignificanceReports() {
        return missionSignificanceReports;
    }

    public void setMissionSignificanceReports(List<MissionSignificanceReport> missionSignificanceReports) {
        this.missionSignificanceReports = missionSignificanceReports;
    }
    
    /**
     * Outputs any metrics that were not significant in the returned formatted string.
     * 
     * @return
     */
    public String generateExamSignificanceReport(boolean outputSignificantTrials) {        
        StringBuilder sb = new StringBuilder("----- CFA Significance Report -----");
        sb.append(newline);
        sb.append("Data Set: " + exam_id);        
        sb.append(newline);
        sb.append("Date Generated: ");
        if (time_stamp != null) {
            DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
            sb.append(df.format(new Date(time_stamp)));
        } else {
            sb.append("null");
        }
        sb.append(newline);
        sb.append(newline);
        if (missionSignificanceReports != null && !missionSignificanceReports.isEmpty()) {
            nf.setMaximumFractionDigits(5);            
            for (MissionSignificanceReport missionReport : missionSignificanceReports) {
                sb.append("----- Mission " + missionReport.getMission_num());
                sb.append(" (N = " + missionReport.getNum_subjects() + ") -----");
                sb.append(newline);
                sb.append(newline);
                
                //Output probability matching significance
                if(missionReport.getPM_significance() != null) {                    
                    sb.append("Probability Matching Results:");
                    sb.append(newline);
                    appendCFAMetricSignificance(sb, missionReport.getPM_significance(),
                            "nH", null, false, "nH < 1");
                    sb.append(newline);
                    sb.append(newline);
                }
                
                //Output confirmation bias significance
                if(missionReport.getCS_significance() != null) {                    
                    sb.append("Confiramtion Bias Results:");
                    sb.append(newline);
                    appendCFAMetricSignificance(sb, missionReport.getCS_significance(),
                            "fH", null, false, "fH < 1");
                    sb.append(newline);
                    sb.append(newline);
                }                
                
                //Output satisfaction of search significance
                if(missionReport.getSS_significance() != null) {                    
                    sb.append("Satisfaction of Search Results:");
                    sb.append(newline);
                    appendCFAMetricSignificance(sb, missionReport.getSS_significance(),
                            "sH", null, false, "sH < 1");
                    sb.append(newline);
                    sb.append(newline);
                }                
                
                //Output trial metric significance
                if (missionReport.getTrialSignificanceReports() != null
                        && !missionReport.getTrialSignificanceReports().isEmpty()) {
                    if(outputSignificantTrials) {
                        sb.append("Trials:");
                    } else {
                        sb.append("Problematic Trials:");
                    }
                    sb.append(newline);
                    int trialNum = 1;
                    for(TrialSignificanceReport trialReport : 
                            missionReport.getTrialSignificanceReports()) {
                        boolean metricFound = false;                        
                        if (trialReport.getAA_significance() != null
                                && (outputSignificantTrials ||
                                !trialReport.getAA_significance().getSignificance().isSignificant())) {
                            //Output AA significance
                            if(!metricFound) {
                                sb.append("- Trial " + trialNum);
                                sb.append(newline);
                                metricFound = true;
                            }
                            sb.append("     AA: ");
                            appendCFAMetricSignificance(sb, trialReport.getAA_significance(),
                                "Np", "Nq", true, "Np < Nq");   
                            sb.append(newline);
                        }
                        if (trialReport.getPDE_significance() != null
                                && (outputSignificantTrials ||
                                !trialReport.getPDE_significance().getSignificance().isSignificant())) {
                            //Output PDE significance
                            if(!metricFound) {
                                sb.append("- Trial " + trialNum);
                                sb.append(newline);
                                metricFound = true;
                            }
                            sb.append("     PDE: " );
                            appendCFAMetricSignificance(sb, trialReport.getPDE_significance(),
                                "Np", "Nq", true, "Np < Nq");   
                            sb.append(newline);
                        }                                                
                        if (trialReport.getRR_significance() != null
                                && !trialReport.getRR_significance().isEmpty()) {
                            int locationNum = 1;
                            for (CFAMetricSignificanceReport rrSignificance :
                                    trialReport.getRR_significance() ) {                                
                                if (rrSignificance != null
                                        && (outputSignificantTrials ||
                                        !rrSignificance.getSignificance().isSignificant())) {
                                    //Output RR significance
                                    if (!metricFound) {
                                        sb.append("- Trial " + trialNum);
                                        sb.append(newline);
                                        metricFound = true;
                                    }
                                    sb.append("     RR at location " + locationNum + ": " );
                                    appendCFAMetricSignificance(sb, rrSignificance,
                                            "P", "Q", true, "P > Q");
                                    sb.append(newline);
                                }
                                locationNum++;
                            }
                        }                        
                        if (trialReport.getAV_significance() != null
                                && (outputSignificantTrials ||
                                !trialReport.getAV_significance().getSignificance().isSignificant())) {
                            //Output AV significance
                            if(!metricFound) {
                                sb.append("- Trial " + trialNum);
                                sb.append(newline);                                
                            }
                            sb.append("     AV: " );
                            appendCFAMetricSignificance(sb, trialReport.getAV_significance(),
                                "Np", "Nq", true, "Np < Nq");   
                            sb.append(newline);
                        }                                                
                        trialNum++;
                    }
                }
                sb.append(newline);
            }
        }
        return sb.toString();
    }
    
    /**
     *
     * @param sb
     * @param metricSignificance
     * @param humanValueName
     * @param comparisonValueName
     * @param showComparisonValue
     * @param testDescription
     */
    protected static void appendCFAMetricSignificance(StringBuilder sb,
            CFAMetricSignificanceReport metricSignificance,
            String humanValueName, String comparisonValueName,
            boolean showComparisonValue, String testDescription) {
        if (metricSignificance != null) {
            sb.append(humanValueName + " = " + metricSignificance.getHumanValue());
            if(showComparisonValue) {
                sb.append(", " + comparisonValueName + " = " + 
                        metricSignificance.getComparisonValue());
            }
            if(testDescription != null) {
                sb.append(", Test: " + testDescription);
            }
            sb.append(", Bias Exhibited: ");            
            sb.append(metricSignificance.getMetric() != null
                    && metricSignificance.getMetric().exhibited != null ? 
                    metricSignificance.getMetric().exhibited ? "YES" : "NO" : null);
            sb.append(", Bias Significant: ");
            sb.append(metricSignificance.getSignificance().isSignificant()
                ? "YES" : "NO");
            //sb.append(metricSignificance.getSignificance().isSignificant()
             //       ? ", significant" : ", NOT significant");
            sb.append(", p = ");
            sb.append(nf.format(metricSignificance.getSignificance().getP_value()));
        }
    }
}

