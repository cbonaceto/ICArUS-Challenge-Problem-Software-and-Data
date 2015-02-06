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
package org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes.RedTacticProbability;

/**
 * Contains probability data, including the normalized probability (in percent
 * format), the raw probability (in percent format, only present when the GUI
 * normalizes lists of probabilities), and the time spent on the probability
 * judgment.
 *
 * @author CBONACETO
 *
 */
@XmlType(name = "Probability", namespace = "IcarusCPD_2")
@XmlSeeAlso({AttackProbability.class, RedTacticProbability.class})
public class Probability {

    /**
     * The probability in percent format (normalized)
     */
    protected Double probability;

    /**
     * The normative (cumulative Bayesian) probability in decimal format
     * (normalized)
     */
    protected Double normativeProbability;

    /**
     * The normative (incremental Bayesian) probability in decimal format
     * (normalized)
     */
    protected Double normativeIncrementalProbability;

    /**
     * The probability in percent format (only present when the GUI normalizes
     * probabilities)
     */
    protected Double rawProbability;

    /**
     * The time spent on this probability judgment (e.g., time subject spent
     * adjusting probability setting) (milliseconds)
     */
    protected Long time_ms;

    /**
     * Get the probability in percent format (normalized). Response data
     * provided by the participant.
     *
     * @return probability in percent format (normalized)
     */
    @XmlAttribute(name = "probability")
    public Double getProbability() {
        return probability;
    }

    /**
     * Set the probability in percent format (normalized). Response data
     * provided by the participant.
     *
     * @param probability probability in percent format (normalized)
     */
    public void setProbability(Double probability) {
        this.probability = probability;
    }

    /**
     * Get the normative (cumulative Bayesian) probability in decimal format (normalized). This data
     * is not provided by the participant.
     *
     * @return the normative (cumulative Bayesian) probability in decimal format (normalized)
     */
    @XmlAttribute(name = "normativeProbability")
    public Double getNormativeProbability() {
        return normativeProbability;
    }

    /**
     * Set the normative (cumulative Bayesian) probability in decimal format (normalized). This data
     * is not provided by the participant.
     *
     * @param normativeProbability the normative (cumulative Bayesian) probability in percent format
     * (normalized)
     */
    public void setNormativeProbability(Double normativeProbability) {
        this.normativeProbability = normativeProbability;
    }

    /**
     * Set the normative (incremental Bayesian) probability in decimal format (normalized). This data
     * is not provided by the participant.
     * 
     * @return the normative (incremental Bayesian) probability in decimal format (normalized)
     */
    @XmlAttribute(name = "normativeIncrementalProbability")
    public Double getNormativeIncrementalProbability() {
        return normativeIncrementalProbability;
    }

    /**
     * Set the normative (incremental Bayesian) probability in decimal format (normalized). This data
     * is not provided by the participant.
     * 
     * @param normativeIncrementalProbability the normative (incremental Bayesian) 
     * probability in decimal format (normalized)
     */
    public void setNormativeIncrementalProbability(Double normativeIncrementalProbability) {
        this.normativeIncrementalProbability = normativeIncrementalProbability;
    }

    /**
     * Get the "raw" probability in percent format (only present when the GUI
     * normalizes probabilities and the user-entered probability differs from the
     * normalized probability). Response data provided by the participant. FOR
     * HUMAN SUBJECT USE ONLY.
     *
     * @return the "raw" probability in percent format
     */
    @XmlAttribute(name = "rawProbability")
    public Double getRawProbability() {
        return rawProbability;
    }

    /**
     * Set the "raw" probability in percent format (only present when the GUI
     * normalizes probabilities and the user-entered probabilit differs from the
     * normalized probability). Response data provided by the participant. FOR
     * HUMAN SUBJECT USE ONLY.
     *
     * @param rawProbability the "raw" probability in percent format
     */
    public void setRawProbability(Double rawProbability) {
        this.rawProbability = rawProbability;
    }

    /**
     * Get the time spent on this probability judgment (e.g., time subject spent
     * adjusting probability setting) (milliseconds). FOR HUMAN SUBJECT USE
     * ONLY.
     *
     * @return the time in milliseconds
     */
    @XmlAttribute(name = "time_ms")
    public Long getTime_ms() {
        return time_ms;
    }

    /**
     * Set the time spent on this probability judgment (e.g., time subject spent
     * adjusting probability setting) (milliseconds). FOR HUMAN SUBJECT USE
     * ONLY.
     *
     * @param time_ms the time in milliseconds
     */
    public void setTime_ms(Long time_ms) {
        this.time_ms = time_ms;
    }

    @Override
    public String toString() {
        return probability != null ? probability.toString() : null;
    }
}
