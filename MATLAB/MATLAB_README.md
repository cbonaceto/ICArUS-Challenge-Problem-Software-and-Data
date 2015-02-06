ICArUS Challenge Problem Software and Data - MATLAB README
Copyright 2015 The MITRE Corporation. All rights reserved.

This software and data is provided strictly to support demonstrations of ICArUS challenge problems
 and to assist in the development of cognitive-neuroscience architectures. It is not intended to be used
in operational systems or environments.

The MATLAB/Assessment folder contains all MATLAB code used to create figures for the Phase 1 Pilot and Final exams, and the Phase 2 Pilot (Sample-Exam-1, Sample-Exam-2) and Final Exams. These figures were used in briefings presented
at various ICArUS PI Meetings.

--------------------------------------------
Creating figures for Phase 1 Human Subject Data

1) Aggregate human subject data files into CSV files by running the Java class org.mitre.icarus.cps.assessment.data_aggregator.phase_1.SubjectDataCsvAggregator_Matlab. You may
edit this source code to aggregate files for the desired exam (one of the Pilot exams or the Final exam).

2) Aggregated CSV files will be placed in the data/Phase_1_CPD/assessment/EXAM_NAME/Human_Data/Aggregated Data folder. Copy these CSV files to the MATLAB/Assessment/Phase_1_CPD/data/EXAM_NAME/subject_data folder.

3) Run the MATLAB/Assessment/Phase_1_CPD/plotting/create_current_metrics_plots.m script to generate the plots. You may edit this script to generate plots for the desired exam.

--------------------------------------------
Creating figures for Phase 2 Human Subject Data

1) Aggregate human subject data files into CSV files by running the Java class org.mitre.icarus.cps.assessment.data_aggregator.phase_2.CsvAggregator. You may edit this source code to aggregate files for the desired exam (one of the Sample (Pilot) exams or the Final exam).

2) Aggregated CSV files will be placed in the data/Phase_2_CPD/assessment/EXAM_NAME/Human_Data/Aggregated Data folder. Copy these CSV files to the MATLAB/Assessment/Phase_2_CPD/data/EXAM_NAME/Human_Data/Aggregated Data folder.

3) Run the MATLAB/Assessment/Phase_2_CPD/plotting/create_current_metrics_plots.m script to generate the plots. You may edit this script to generate plots for the desired exam.

--------------------------------------------
Creating figures for Phase 2 Human Individual Differences

1) Aggregate human subjects survey data files into CSV files by running the Java class org.mitre.icarus.cps.assessment.survey.phase_2.SurveyResponseAggregator_Phase2. You may edit this
source code to aggregate survey files for the desired exam (one of the Sample (Pilot) exams or the Final exam).

2) Aggregated CSV survey files will be placed in the data/Phase_2_CPD/assessment/EXAM_NAME/Human_Data/Aggregated Data folder. Copy these CSV files to the MATLAB/Assessment/Phase_2_CPD/data/EXAM_NAME/Human_Data/Aggregated Data folder.

3) Run the MATLAB/Assessment/Phase_2_CPD/plotting/create_current_metrics_plots.m script with the flag
plotIndividualDifferences set to true in the call create_all_metrics_plots in this script to generate the
individual differences plots. You may edit this script to generate plots for the desired exam.

--------------------------------------------
Creating figures for Phase 2 Performer Model Data

1) Place your model's XML exam response file in 
data/Phase_2_CPD/EXAM_NAME/Model_Data/TEAM_NAME/Run_Final. Run BatchFileProcessor.aggregateModelData
to create aggreggated CSV files for each mission. 

2) These aggregated CSV files will be placed in the folder data/Phase_2_CPD/EXAM_NAME/Model_Data/TEAM_NAME/Run_Final. Copy these CSV files to the folder MATLAB\Assessment\Phase_2_CPD\data\EXAM_NAME\Model_Data\TEAM_NAME.
Run MATLAB\Assessment\Phase_2_CPD\plotting\human-model-bayesian\create_current_hmb_plots.m
to create the plots.

--------------------------------------------
Creating figures for Phase 2 MITRE A-B Model Data

1) Run BatchFileProcessor.buildAndPersistSampleModelDataSet to create the MITRE
model XML exam response file. See BatchFileProcessor.main lines 778-780 for an example.

2) Run BatchFileProcessor.aggregateModelData to create aggreggated CSV files for each mission. 

3) 