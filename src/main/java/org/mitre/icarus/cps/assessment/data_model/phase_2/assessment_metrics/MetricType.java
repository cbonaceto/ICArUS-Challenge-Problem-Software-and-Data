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
 * Defines metric type Integer constants for Phase 2.
 *
 * @author CBONACETO
 *
 */
public enum MetricType {
    AA, PDE, RR, AV, PM, CS, CB, SS,
    RSR_Standard, RSR_Bayesian, RSR_Spm_Spr_avg, RSR_Spm_Spq_avg,
    RSR_alt_1, RSR_alt_2, ASR, RMR;

    public static final int AA_ID = AA.ordinal();
    public static final int PDE_ID = PDE.ordinal();
    public static final int RR_ID = RR.ordinal();
    public static final int AV_ID = AV.ordinal();    
    public static final int PM_ID = PM.ordinal();    
    public static final int CS_ID = CS.ordinal();
    public static final int CB_ID = CS.ordinal();
    public static final int SS_ID = CS.ordinal();
    public static final int RSR_Standard_ID = RSR_Standard.ordinal();
    public static final int RSR_Bayesian_ID = RSR_Bayesian.ordinal();
    public static final int RSR_Spm_Spr_avg_ID = RSR_Spm_Spr_avg.ordinal();
    public static final int RSR_Spm_Spq_avg_ID = RSR_Spm_Spq_avg.ordinal();
    public static final int RSR_alt_1_ID = RSR_alt_1.ordinal();
    public static final int RSR_alt_2_ID = RSR_alt_2.ordinal();
    public static final int ASR_ID = ASR.ordinal();
    public static final int RMR_ID = RMR.ordinal();
}