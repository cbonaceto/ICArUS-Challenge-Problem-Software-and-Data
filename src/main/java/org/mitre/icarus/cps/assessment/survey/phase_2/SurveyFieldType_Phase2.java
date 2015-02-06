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
package org.mitre.icarus.cps.assessment.survey.phase_2;

/**
 * Enumeration of survey field types for the Phase 2 survey.
 *
 * @author CBONACETO
 *
 */
public enum SurveyFieldType_Phase2 {
    Age, Gender, 
    AssociatesDegreeDiscipline, BachelorsDegreeDiscipline, CertificateDegreeDiscipline, 
    MastersDegreeDiscipline, PhDDiscipline, 
    CurrentDegree, CurrentDegreeDiscipline, HighestDegree, 
    Job_1_Type, Job_1_Employer, Job_1_Title, Job_1_Years,
    Job_2_Type, Job_2_Employer, Job_2_Title, Job_2_Years,
    Job_3_Type, Job_3_Employer, Job_3_Title, Job_3_Years,
    Job_4_Type, Job_4_Employer, Job_4_Title, Job_4_Years,
    Job_5_Type, Job_5_Employer, Job_5_Title, Job_5_Years,
    Job_6_Type, Job_6_Employer, Job_6_Title, Job_6_Years,
    Job_7_Type, Job_7_Employer, Job_7_Title, Job_7_Years,
    Job_8_Type, Job_8_Employer, Job_8_Title, Job_8_Years,
    Job_9_Type, Job_9_Employer, Job_9_Title, Job_9_Years,
    Job_10_Type, Job_10_Employer, Job_10_Title, Job_10_Years,
    YearsExperience, YearsGeoIntExperience,
    ProbabilityAndStatsTraining,
    ProbabilityAndStatsFrequency,
    GeospatialDataFrequency,
    GeoIntAnalysisLevel,
    SpatialAbility,
    BIS, BASDrive, BASFunSeeking, BASRewardResponsiveness, BASTotal, 
    FoxHedgehogScore,
    SBSDTScore,
    WLTScore,
    CRTScore,
    VGE
}
