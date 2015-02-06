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
import org.mitre.icarus.cps.app.util.ProbabilityUtils;
import org.mitre.icarus.cps.assessment.assessment_metrics_computer.phase_2.TrialMetricsComputer;
import org.mitre.icarus.cps.assessment.score_computer.phase_2.ScoreComputer_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.AttackProbabilityReportProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.Probability;

/**
 * Contains response data for an attack probability report probe.
 *
 * @author cbonaceto
 *
 */
public class AttackProbabilityReportData extends ProbabilityReportData<AttackProbabilityReportProbe> {

    private static final long serialVersionUID = -3818526611258892480L;

    public AttackProbabilityReportData() {
    }

    public AttackProbabilityReportData(AttackProbabilityReportProbe trialPartProbe) {
        super(trialPartProbe);
    }

    @Override
    protected void initializeTrialPartData(AttackProbabilityReportProbe trialPartProbe) {
        if (trialPartProbe != null) {
            probabilitiesId = trialPartProbe.getId() != null ? trialPartProbe.getId()
                    : trialPartProbe.getType() != null ? trialPartProbe.getType().getId() : null;
            probabilitiesName = trialPartProbe.getName();
            if (trialPartProbe.getProbabilities() != null
                    && !trialPartProbe.getProbabilities().isEmpty()) {
                probabilities = new ArrayList<Double>();
                probabilities_normative = new ArrayList<Double>();
                probabilities_normative_incremental = new ArrayList<Double>();
                double sum = 0.d;
                double sumNormative = 0.d;
                double sumNormativeIncremental = 0.d;
                for (Probability probability : trialPartProbe.getProbabilities()) {
                    sum += probability.getProbability() != null
                            ? probability.getProbability() : 0.d;
                    sumNormative += probability.getNormativeProbability() != null
                            ? probability.getNormativeProbability() : 0.d;
                    sumNormativeIncremental += probability.getNormativeIncrementalProbability() != null
                            ? probability.getNormativeIncrementalProbability() : 0.d;
                    probabilities.add(probability.getProbability() / 100.d);
                    probabilities_normative.add(probability.getNormativeProbability());
                    probabilities_normative_incremental.add(probability.getNormativeIncrementalProbability());                    
                }
                //Add probability for null hypothesis
                probabilities.add((100.d - sum) / 100.d);
                probabilities_normative.add(1.d - sumNormative);
                probabilities_normative_incremental.add(1.d - sumNormativeIncremental);
                
                //Normalize probabilities
                if (TrialMetricsComputer.checkProbsNotNullOrEmpty(probabilities)) {
                    ProbabilityUtils.normalizeDecimalProbabilities(probabilities, probabilities,
                            ScoreComputer_Phase2.EPSILON_PHASE2);
                }
                if (TrialMetricsComputer.checkProbsNotNullOrEmpty(probabilities_normative)) {
                    ProbabilityUtils.normalizeDecimalProbabilities(probabilities_normative,
                            probabilities_normative, ScoreComputer_Phase2.EPSILON_PHASE2);
                }
                if (TrialMetricsComputer.checkProbsNotNullOrEmpty(probabilities_normative_incremental)) {
                    ProbabilityUtils.normalizeDecimalProbabilities(probabilities_normative_incremental,
                            probabilities_normative_incremental, ScoreComputer_Phase2.EPSILON_PHASE2);
                }
            }
        }
    }
}