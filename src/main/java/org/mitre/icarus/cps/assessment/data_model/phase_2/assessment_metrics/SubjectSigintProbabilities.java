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

/**
 * Contains participant values of P(Attack|SIGINT) -- P(Attack|Chatter) and
 * P(Attack|Silent) in percent format.
 * 
 * @author CBONACETO
 */
public class SubjectSigintProbabilities {     
    
    /** Participant value of P(Attack|Chatter) */
    protected Double ptChatter;
    protected Integer ptChatter_count;
    protected Double ptChatter_std;
    
    /** Participant value of P(Attack|Silence) */
    protected Double ptSilent;
    protected Integer ptSilent_count;
    protected Double ptSilent_std;
    
    public SubjectSigintProbabilities() {}
    
    public SubjectSigintProbabilities(Double ptChatter, Double ptSilent) {
        this.ptChatter = ptChatter;
        this.ptSilent = ptSilent;
    }
    
    public SubjectSigintProbabilities(Double ptChatter, Integer ptChatter_count,
            Double ptSilent, Integer ptSilent_count) {
        this.ptChatter = ptChatter;
        this.ptChatter_count = ptChatter_count;
        this.ptSilent = ptSilent;
        this.ptSilent_count = ptSilent_count;
    }

    public Double getPtChatter() {
        return ptChatter;
    }

    public void setPtChatter(Double ptChatter) {
        this.ptChatter = ptChatter;
    }

    public Integer getPtChatter_count() {
        return ptChatter_count;
    }

    public void setPtChatter_count(Integer ptChatter_count) {
        this.ptChatter_count = ptChatter_count;
    }

    public Double getPtChatter_std() {
        return ptChatter_std;
    }

    public void setPtChatter_std(Double ptChatter_std) {
        this.ptChatter_std = ptChatter_std;
    }

    public Double getPtSilent() {
        return ptSilent;
    }

    public void setPtSilent(Double ptSilent) {
        this.ptSilent = ptSilent;
    }
    
    public Integer getPtSilent_count() {
        return ptSilent_count;
    }

    public void setPtSilent_count(Integer ptSilent_count) {
        this.ptSilent_count = ptSilent_count;
    }

    public Double getPtSilent_std() {
        return ptSilent_std;
    }

    public void setPtSilent_std(Double ptSilent_std) {
        this.ptSilent_std = ptSilent_std;
    }
}