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
 * Contains significance reports for trial-level CFA metrics.
 * 
 * @author CBONACETO
 */
public class TrialSignificanceReport {
    
    /** AA (Anchoring and Adjustment) significance report*/
    private CFAMetricSignificanceReport AA_significance;

    /** PDE (Persistence of Discredited Evidence) significance report */
    private CFAMetricSignificanceReport PDE_significance;

    /** RR (Representativeness) at each location significance report(s) */
    private List<CFAMetricSignificanceReport> RR_significance;

    /** AV (Availability) significance report  */
    protected CFAMetricSignificanceReport AV_significance;

    public CFAMetricSignificanceReport getAA_significance() {
        return AA_significance;
    }

    public void setAA_significance(CFAMetricSignificanceReport AA_significance) {
        this.AA_significance = AA_significance;
    }    

    public CFAMetricSignificanceReport getPDE_significance() {
        return PDE_significance;
    }

    public void setPDE_significance(CFAMetricSignificanceReport PDE_significance) {
        this.PDE_significance = PDE_significance;
    }

    public List<CFAMetricSignificanceReport> getRR_significance() {
        return RR_significance;
    }

    public void setRR_significance(List<CFAMetricSignificanceReport> RR_significance) {
        this.RR_significance = RR_significance;
    }

    public CFAMetricSignificanceReport getAV_significance() {
        return AV_significance;
    }

    public void setAV_significance(CFAMetricSignificanceReport AV_significance) {
        this.AV_significance = AV_significance;
    }    
}