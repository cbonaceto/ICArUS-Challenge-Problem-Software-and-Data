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
package org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractTrialMetrics;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.CFAMetric;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.CPAMetric;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.MetricSignificance;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.Probabilities;

/**
 * Contains Phase 1 assessment metrics for a trial in a task in a Phase 1 exam.
 *
 * @author CBONACETO
 *
 */
@Entity
public class TrialMetrics extends AbstractTrialMetrics implements Serializable {

    private static final long serialVersionUID = -5410927443698639372L;

    protected List<Probabilities> probs;
    protected List<Probabilities> probs_std;
    protected List<Probabilities> normative_probs;
    protected List<Probabilities> normative_probs_std;

    protected List<Double> allocation;
    protected List<Double> allocation_std;

    protected List<Double> Np;
    protected List<Double> Np_std;
    protected List<Double> Nq;
    protected List<Double> Nq_std;

    protected List<Double> Np_delta;
    protected List<Double> Np_delta_std;
    protected List<Double> Nq_delta;
    protected List<Double> Nq_delta_std;

    protected Double Fh;
    protected Double Ph;
    protected Double Fh_Ph;
    protected Double Fh_Ph_std;
    protected Double Fh_1;
    protected Double Fh_1_std;

    protected Double RMS_F_P;
    protected Double RMS_F_P_std;
    protected Double RMS_F_I;
    protected Double RMS_F_I_std;

    protected Integer sigint_selections;
    protected Integer sigint_highest_group_selections;
    protected Integer sigint_highest_best_selections;
    protected Double C;
    protected Double C_std;
    protected Double C_highest_best;
    protected Double C_highest_best_std;

    protected CFAMetric RR;
    protected List<CFAMetric> AI;
    protected List<CFAMetric> CW;
    protected CFAMetric PM;

    protected List<Integer> F_LS_count;
    protected List<Double> F_LS_percent;

    protected List<MetricSignificance> HR_probs_difference; //Not used
    protected List<MetricSignificance> HB_probs_difference; //Not used		

    protected List<Double> Spm;
    protected List<Double> Spr;
    protected List<Double> Spq;

    protected List<CPAMetric> RSR;
    protected List<CPAMetric> RSR_Bayesian;
    protected List<CPAMetric> RSR_alt_1; //Will be RSR(RMSE)
    protected List<CPAMetric> RSR_alt_2; //Will be RSR(RMSE)_Bayesian

    protected List<CPAMetric> ASR;

    protected CPAMetric RMR;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "trialMetricsProbs")
    @Fetch(value = FetchMode.SUBSELECT)
    public List<Probabilities> getProbs() {
        return probs;
    }

    public void setProbs(List<Probabilities> probs) {
        this.probs = probs;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "trialMetricsProbsStd")
    @Fetch(value = FetchMode.SUBSELECT)
    public List<Probabilities> getProbs_std() {
        return probs_std;
    }

    public void setProbs_std(List<Probabilities> probs_std) {
        this.probs_std = probs_std;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "trialMetricsNormativeProbs")
    @Fetch(value = FetchMode.SUBSELECT)
    public List<Probabilities> getNormative_probs() {
        return normative_probs;
    }

    public void setNormative_probs(List<Probabilities> normative_probs) {
        this.normative_probs = normative_probs;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "trialMetricsNormativeProbsStd")
    @Fetch(value = FetchMode.SUBSELECT)
    public List<Probabilities> getNormative_probs_std() {
        return normative_probs_std;
    }

    public void setNormative_probs_std(List<Probabilities> normative_probs_std) {
        this.normative_probs_std = normative_probs_std;
    }

    @ElementCollection
    public List<Double> getAllocation() {
        return allocation;
    }

    public void setAllocation(List<Double> allocation) {
        this.allocation = allocation;
    }

    @ElementCollection
    public List<Double> getAllocation_std() {
        return allocation_std;
    }

    public void setAllocation_std(List<Double> allocation_std) {
        this.allocation_std = allocation_std;
    }

    @ElementCollection
    public List<Double> getNp() {
        return Np;
    }

    public void setNp(List<Double> np) {
        Np = np;
    }

    @ElementCollection
    public List<Double> getNp_std() {
        return Np_std;
    }

    public void setNp_std(List<Double> np_std) {
        Np_std = np_std;
    }

    @ElementCollection
    public List<Double> getNq() {
        return Nq;
    }

    public void setNq(List<Double> nq) {
        Nq = nq;
    }

    @ElementCollection
    public List<Double> getNq_std() {
        return Nq_std;
    }

    public void setNq_std(List<Double> nq_std) {
        Nq_std = nq_std;
    }

    @ElementCollection
    public List<Double> getNp_delta() {
        return Np_delta;
    }

    public void setNp_delta(List<Double> np_delta) {
        Np_delta = np_delta;
    }

    @ElementCollection
    public List<Double> getNp_delta_std() {
        return Np_delta_std;
    }

    public void setNp_delta_std(List<Double> np_delta_std) {
        Np_delta_std = np_delta_std;
    }

    @ElementCollection
    public List<Double> getNq_delta() {
        return Nq_delta;
    }

    public void setNq_delta(List<Double> nq_delta) {
        Nq_delta = nq_delta;
    }

    @ElementCollection
    public List<Double> getNq_delta_std() {
        return Nq_delta_std;
    }

    public void setNq_delta_std(List<Double> nq_delta_std) {
        Nq_delta_std = nq_delta_std;
    }

    public Double getFh() {
        return Fh;
    }

    public void setFh(Double fh) {
        Fh = fh;
    }

    public Double getPh() {
        return Ph;
    }

    public void setPh(Double ph) {
        Ph = ph;
    }

    public Double getFh_Ph() {
        return Fh_Ph;
    }

    public void setFh_Ph(Double fh_Ph) {
        Fh_Ph = fh_Ph;
    }

    public Double getFh_Ph_std() {
        return Fh_Ph_std;
    }

    public void setFh_Ph_std(Double fh_Ph_std) {
        Fh_Ph_std = fh_Ph_std;
    }

    public Double getFh_1() {
        return Fh_1;
    }

    public void setFh_1(Double fh_1) {
        Fh_1 = fh_1;
    }

    public Double getFh_1_std() {
        return Fh_1_std;
    }

    public void setFh_1_std(Double fh_1_std) {
        Fh_1_std = fh_1_std;
    }

    public Double getRMS_F_P() {
        return RMS_F_P;
    }

    public void setRMS_F_P(Double rMS_F_P) {
        RMS_F_P = rMS_F_P;
    }

    public Double getRMS_F_P_std() {
        return RMS_F_P_std;
    }

    public void setRMS_F_P_std(Double rMS_F_P_std) {
        RMS_F_P_std = rMS_F_P_std;
    }

    public Double getRMS_F_I() {
        return RMS_F_I;
    }

    public void setRMS_F_I(Double rMS_F_I) {
        RMS_F_I = rMS_F_I;
    }

    public Double getRMS_F_I_std() {
        return RMS_F_I_std;
    }

    public void setRMS_F_I_std(Double rMS_F_I_std) {
        RMS_F_I_std = rMS_F_I_std;
    }

    public Integer getSigint_selections() {
        return sigint_selections;
    }

    public void setSigint_selections(Integer sigint_selections) {
        this.sigint_selections = sigint_selections;
    }

    public Integer getSigint_highest_group_selections() {
        return sigint_highest_group_selections;
    }

    public void setSigint_highest_group_selections(
            Integer sigint_highest_group_selections) {
        this.sigint_highest_group_selections = sigint_highest_group_selections;
    }

    public Integer getSigint_highest_best_selections() {
        return sigint_highest_best_selections;
    }

    public void setSigint_highest_best_selections(
            Integer sigint_highest_best_selections) {
        this.sigint_highest_best_selections = sigint_highest_best_selections;
    }

    public Double getC() {
        return C;
    }

    public void setC(Double c) {
        C = c;
    }

    public Double getC_std() {
        return C_std;
    }

    public void setC_std(Double c_std) {
        C_std = c_std;
    }

    public Double getC_highest_best() {
        return C_highest_best;
    }

    public void setC_highest_best(Double c_highest_best) {
        C_highest_best = c_highest_best;
    }

    public Double getC_highest_best_std() {
        return C_highest_best_std;
    }

    public void setC_highest_best_std(Double c_highest_best_std) {
        C_highest_best_std = c_highest_best_std;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public CFAMetric getRR() {
        return RR;
    }

    public void setRR(CFAMetric rR) {
        RR = rR;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "trialMetricsAi")
    @Fetch(value = FetchMode.SUBSELECT)
    public List<CFAMetric> getAI() {
        return AI;
    }

    public void setAI(List<CFAMetric> aI) {
        AI = aI;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "trialMetricsCw")
    @Fetch(value = FetchMode.SUBSELECT)
    public List<CFAMetric> getCW() {
        return CW;
    }

    public void setCW(List<CFAMetric> cW) {
        CW = cW;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public CFAMetric getPM() {
        return PM;
    }

    public void setPM(CFAMetric pM) {
        PM = pM;
    }

    @ElementCollection
    public List<Integer> getF_LS_count() {
        return F_LS_count;
    }

    public void setF_LS_count(List<Integer> f_LS_count) {
        F_LS_count = f_LS_count;
    }

    @ElementCollection
    public List<Double> getF_LS_percent() {
        return F_LS_percent;
    }

    public void setF_LS_percent(List<Double> f_LS_percent) {
        F_LS_percent = f_LS_percent;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "trialMetricsHrProbsDifference")
    @Fetch(value = FetchMode.SUBSELECT)
    public List<MetricSignificance> getHR_probs_difference() {
        return HR_probs_difference;
    }

    public void setHR_probs_difference(List<MetricSignificance> hR_probs_difference) {
        HR_probs_difference = hR_probs_difference;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "trialMetricsHbProbsDifference")
    @Fetch(value = FetchMode.SUBSELECT)
    public List<MetricSignificance> getHB_probs_difference() {
        return HB_probs_difference;
    }

    public void setHB_probs_difference(List<MetricSignificance> hB_probs_difference) {
        HB_probs_difference = hB_probs_difference;
    }

    @ElementCollection
    public List<Double> getSpm() {
        return Spm;
    }

    public void setSpm(List<Double> spm) {
        Spm = spm;
    }

    @ElementCollection
    public List<Double> getSpr() {
        return Spr;
    }

    public void setSpr(List<Double> spr) {
        Spr = spr;
    }

    @ElementCollection
    public List<Double> getSpq() {
        return Spq;
    }

    public void setSpq(List<Double> spq) {
        Spq = spq;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "trialMetricsRsr")
    @Fetch(value = FetchMode.SUBSELECT)
    public List<CPAMetric> getRSR() {
        return RSR;
    }

    public void setRSR(List<CPAMetric> rSR) {
        RSR = rSR;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "trialMetricsRsrBayesian")
    @Fetch(value = FetchMode.SUBSELECT)
    public List<CPAMetric> getRSR_Bayesian() {
        return RSR_Bayesian;
    }

    public void setRSR_Bayesian(List<CPAMetric> rSR_Bayesian) {
        RSR_Bayesian = rSR_Bayesian;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "trialMetricsRsrAlt1")
    @Fetch(value = FetchMode.SUBSELECT)
    public List<CPAMetric> getRSR_alt_1() {
        return RSR_alt_1;
    }

    public void setRSR_alt_1(List<CPAMetric> rSR_alt_1) {
        RSR_alt_1 = rSR_alt_1;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "trialMetricsRsrAlt2")
    @Fetch(value = FetchMode.SUBSELECT)
    public List<CPAMetric> getRSR_alt_2() {
        return RSR_alt_2;
    }

    public void setRSR_alt_2(List<CPAMetric> rSR_alt_2) {
        RSR_alt_2 = rSR_alt_2;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "trialMetricsAsr")
    @Fetch(value = FetchMode.SUBSELECT)
    public List<CPAMetric> getASR() {
        return ASR;
    }

    public void setASR(List<CPAMetric> aSR) {
        ASR = aSR;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public CPAMetric getRMR() {
        return RMR;
    }

    public void setRMR(CPAMetric rMR) {
        RMR = rMR;
    }
}
