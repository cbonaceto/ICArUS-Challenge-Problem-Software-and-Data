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

import java.util.List;

/**
 * Contains negentropies of the probability reports in a trial.
 * 
 * @author CBONACETO
 */
public class NegentropyMetrics {
    
    /** Negentropy of the participant Red tactic probabilities */
    protected Double Np_redTacticProbs;
    protected Double Np_redTacticProbs_std;
    
    /** Negentropy of the Bayesian Red tactic probabilities */
    protected Double Nq_redTacticProbs;
    protected Double Nq_redTacticProbs_std;
    
    /** Negentropy of the participant Red attack probability based on propensity (Pp) */    
    protected Double Np_Pp;
    protected Double Np_Pp_std;
    
    /** Negentropy of the Bayesian Red attack probability based on propensity (Pp) */
    protected Double Nq_Pp;
    protected Double Nq_Pp_std;
    
    /** Negentropy of the participant Red attack probability based on propensity + capability (Ppc) */
    protected Double Np_Ppc;
    protected Double Np_Ppc_std;
    
    /** Negentropy of the Bayesian Red attack probability based on propensity + capability (Ppc) */    
    protected Double Nq_Ppc;
    protected Double Nq_Ppc_std;
    
    /** Negentropy of the participant Red attack probability based on activity (Pt) */
    protected Double Np_Pt;
    protected Double Np_Pt_std;
    
    /** Negentropy of the Bayesian Red attack probability based on activity (Pt) */
    protected Double Nq_Pt;
    protected Double Nq_Pt_std;
    
    /** Negentropy of the participant Red attack probability based on activity, 
     * propensity, and capability (Ptpc) */
    protected Double Np_Ptpc;
    protected Double Np_Ptpc_std;
    
    /** Negentropy of the Bayesian Red attack probability based on activity, 
     * propensity, and capability (Ptpc) */
    protected Double Nq_Ptpc;
    protected Double Nq_Ptpc_std;
    
    /** The changes in participant Red attack negentropies from one stage to the 
     * next of the trial (not including Pt stage) */
    protected List<Double> Np_delta;
    protected List<Double> Np_delta_std;

    /** The changes in Bayesian Red attack negentropies from one stage to the 
     * next of the trial (not including Pt stage) */
    protected List<Double> Nq_delta;
    protected List<Double> Nq_delta_std;

    public Double getNp_redTacticProbs() {
        return Np_redTacticProbs;
    }

    public void setNp_redTacticProbs(Double Np_redTacticProbs) {
        this.Np_redTacticProbs = Np_redTacticProbs;
    }

    public Double getNp_redTacticProbs_std() {
        return Np_redTacticProbs_std;
    }

    public void setNp_redTacticProbs_std(Double Np_redTacticProbs_std) {
        this.Np_redTacticProbs_std = Np_redTacticProbs_std;
    }

    public Double getNq_redTacticProbs() {
        return Nq_redTacticProbs;
    }

    public void setNq_redTacticProbs(Double Nq_redTacticProbs) {
        this.Nq_redTacticProbs = Nq_redTacticProbs;
    }

    public Double getNq_redTacticProbs_std() {
        return Nq_redTacticProbs_std;
    }

    public void setNq_redTacticProbs_std(Double Nq_redTacticProbs_std) {
        this.Nq_redTacticProbs_std = Nq_redTacticProbs_std;
    }

    public Double getNp_Pp() {
        return Np_Pp;
    }

    public void setNp_Pp(Double Np_Pp) {
        this.Np_Pp = Np_Pp;
    }

    public Double getNp_Pp_std() {
        return Np_Pp_std;
    }

    public void setNp_Pp_std(Double Np_Pp_std) {
        this.Np_Pp_std = Np_Pp_std;
    }

    public Double getNq_Pp() {
        return Nq_Pp;
    }

    public void setNq_Pp(Double Nq_Pp) {
        this.Nq_Pp = Nq_Pp;
    }

    public Double getNq_Pp_std() {
        return Nq_Pp_std;
    }

    public void setNq_Pp_std(Double Nq_Pp_std) {
        this.Nq_Pp_std = Nq_Pp_std;
    }

    public Double getNp_Ppc() {
        return Np_Ppc;
    }

    public void setNp_Ppc(Double Np_Ppc) {
        this.Np_Ppc = Np_Ppc;
    }

    public Double getNp_Ppc_std() {
        return Np_Ppc_std;
    }

    public void setNp_Ppc_std(Double Np_Ppc_std) {
        this.Np_Ppc_std = Np_Ppc_std;
    }

    public Double getNq_Ppc() {
        return Nq_Ppc;
    }

    public void setNq_Ppc(Double Nq_Ppc) {
        this.Nq_Ppc = Nq_Ppc;
    }

    public Double getNq_Ppc_std() {
        return Nq_Ppc_std;
    }

    public void setNq_Ppc_std(Double Nq_Ppc_std) {
        this.Nq_Ppc_std = Nq_Ppc_std;
    }

    public Double getNp_Pt() {
        return Np_Pt;
    }

    public void setNp_Pt(Double Np_Pt) {
        this.Np_Pt = Np_Pt;
    }

    public Double getNp_Pt_std() {
        return Np_Pt_std;
    }

    public void setNp_Pt_std(Double Np_Pt_std) {
        this.Np_Pt_std = Np_Pt_std;
    }

    public Double getNq_Pt() {
        return Nq_Pt;
    }

    public void setNq_Pt(Double Nq_Pt) {
        this.Nq_Pt = Nq_Pt;
    }

    public Double getNq_Pt_std() {
        return Nq_Pt_std;
    }

    public void setNq_Pt_std(Double Nq_Pt_std) {
        this.Nq_Pt_std = Nq_Pt_std;
    }

    public Double getNp_Ptpc() {
        return Np_Ptpc;
    }

    public void setNp_Ptpc(Double Np_Ptpc) {
        this.Np_Ptpc = Np_Ptpc;
    }

    public Double getNp_Ptpc_std() {
        return Np_Ptpc_std;
    }

    public void setNp_Ptpc_std(Double Np_Ptpc_std) {
        this.Np_Ptpc_std = Np_Ptpc_std;
    }    

    public Double getNq_Ptpc() {
        return Nq_Ptpc;
    }

    public void setNq_Ptpc(Double Nq_Ptpc) {
        this.Nq_Ptpc = Nq_Ptpc;
    }

    public Double getNq_Ptpc_std() {
        return Nq_Ptpc_std;
    }

    public void setNq_Ptpc_std(Double Nq_Ptpc_std) {
        this.Nq_Ptpc_std = Nq_Ptpc_std;
    }

    public List<Double> getNp_delta() {
        return Np_delta;
    }

    public void setNp_delta(List<Double> Np_delta) {
        this.Np_delta = Np_delta;
    }

    public List<Double> getNp_delta_std() {
        return Np_delta_std;
    }

    public void setNp_delta_std(List<Double> Np_delta_std) {
        this.Np_delta_std = Np_delta_std;
    }

    public List<Double> getNq_delta() {
        return Nq_delta;
    }

    public void setNq_delta(List<Double> Nq_delta) {
        this.Nq_delta = Nq_delta;
    }

    public List<Double> getNq_delta_std() {
        return Nq_delta_std;
    }

    public void setNq_delta_std(List<Double> Nq_delta_std) {
        this.Nq_delta_std = Nq_delta_std;
    }
}
