ICArUS Challenge Problem Software and Data - ASSESSMENT README
Copyright 2015 The MITRE Corporation. All rights reserved

This document provides instructions on using the software to assess human and model performance
on a Phase 1 or Phase 2 exam. For additional information on the human behavioral data collected,
see ICArUS_Overview_of_Test_and_Evaluation_Materials.pdf (Section 6).

--------------------------------------------------------------------
Phase 1

Results and data for the Phase 1 assessment are in the folders data\Phase_1_CPD\assessment\EXAM_NAME.
These folders contain sub-folders with the Assessment_Results, Exam_Data, Human_Data, and Model_Data.

1) Creating the "average human" file: The "average human" XML file aggregates
and averages data for all human subjects. It is used to assess model performance.
The average human file is placed in data/Phase_1_CPD/EXAM_NAME/Assessment_Results.
To generate this file again for the final exam, run org.mitre.icarus.cps.assessment.assessment_processor.phase_1.BatchFileProcessor.buildFinalExamAverageHumanDataSet.

2) Plotting human subject data: MATLAB is used to plot data. First, create CSV files
that contain the aggregated human data for each mission. Run org.mitre.icarus.cps.assessment.data_aggregator.phase_1.SubjectDataCsvAggregator_Matlab.processFinalExamSubjects
to create the CSV files in the folder data\Phase_1_CPD\assessment\Final_Exam\Human_Data\Aggregated Data. Copy the CSV
files to the folder MATLAB\Assessment\Phase_1_CPD\data\Final Exam August 2012\subject_data. 
Run MATLAB\Assessment\Phase_1_CPD\plotting\create_current_metrics_plots.m to create the plots.

3) Assessing model performance: Place your model's XML exam response file in 
data/Phase_1_CPD/EXAM_NAME/Model_Data/TEAM_NAME/Run_Final. Name your model exam response file 
using the format "TEAMNAME_MODELNAME_EXAMID.xml". Run 
org.mitre.icarus.cps.assessment.assessment_processor.phase_1.BatchFileProcessor.buildFinalExamModelDataSet. 
Note that you must first create the average human data set as outlined in Step (1) above. To create
the assessment results spreadsheet, see lines 733-739 in BatchFileProcessor for an example.

4) Running the MITRE Bayesian and A-B models: See lines 747-754 in BatchFileProcessor.main for an
example on running the MITRE models.

--------------------------------------------------------------------
Phase 2

Results and data for the Phase 2 assessment are in the folders data\Phase_2_CPD\assessment\EXAM_NAME.
These folders contain sub-folders with the Assessment_Results, Exam_Data, Human_Data, and Model_Data.

1) Creating the "average human" file: The "average human" XML file aggregates
and averages data for all human subjects. It is used to assess model performance.
The average human file is placed in data/Phase_2_CPD/EXAM_NAME/Assessment_Results.
To generate this file again for an exam, run org.mitre.icarus.cps.assessment.assessment_processor.phase_2.buildAndPersistAverageHumanDataSet.

2) Plotting human subject data: MATLAB is used to plot data. First, create CSV files
that contain the aggregated human data for each mission. In org.mitre.icarus.cps.assessment.data_aggregator.phase_2.CsvAggregator,
run  aggregateHumanDataSampleExam_1() [for the first Pilot Exam], aggregateHumanDataSampleExam_2() [for the second Pilot Exam],
or aggregateHumanDataFinalExam() [for the Final Exam]. CSV files will be placed in 
the folder data\Phase_2_CPD\assessment\EXAM_NAME\Human_Data\Aggregated Data. Copy the CSV
files to the folder MATLAB\Assessment\Phase_2_CPD\assessment\EXAM_NAME\Human_Data\Aggregated Data. 
Run MATLAB\Assessment\Phase_2_CPD\plotting\create_current_metrics_plots.m to create the plots.

3) Assessing model performance: Place your model's XML exam response file in 
data/Phase_2_CPD/EXAM_NAME/Model_Data/TEAM_NAME/Run_Final. Name your model exam response file 
using the format "TEAMNAME_MODELNAME_EXAMID.xml". Run 
org.mitre.icarus.cps.assessment.assessment_processor.phase_2.BatchFileProcessor.buildAndPersistModelDataSet. 
Note that you must first create the average human data set as outlined in Step (1) above. To create
the assessment results spreadsheet, see lines 784-792 in BatchFileProcessor for an example.

4) Plotting model data: Place your model's XML exam response file in 
data/Phase_2_CPD/EXAM_NAME/Model_Data/TEAM_NAME/Run_Final. Run BatchFileProcessor.aggregateModelData
to create aggreggated CSV files for each mission. These CSV files will be placed in
the folder data/Phase_2_CPD/EXAM_NAME/Model_Data/TEAM_NAME/Run_Final. Copy these
CSV files to the folder MATLAB\Assessment\Phase_2_CPD\data\EXAM_NAME\Model_Data\TEAM_NAME.
Run MATLAB\Assessment\Phase_2_CPD\plotting\human-model-bayesian\create_current_hmb_plots.m
to create the plots. 

5) Running the MITRE A-B model: Run BatchFileProcessor.buildAndPersistSampleModelDataSet. See
BatchFileProcessor.main lines 778-780 for an example.

