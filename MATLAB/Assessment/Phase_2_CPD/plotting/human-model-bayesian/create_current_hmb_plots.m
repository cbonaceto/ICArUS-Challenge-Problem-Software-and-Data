%Create HMB plots for the Final Exam with the HRL model
create_human_model_bayesian_plots([1, 2, 3, 4, 5], true, true, true, true, true, 'data\Final-Exam-1\Human_Data', 'data\Final-Exam-1\Model_Data', 'HRL',  'aggregatedResponseTest', 'figures\Final-Exam-1\HRL_human_model_bayesian', true, false);

%Create HMB plots for the Final Exam with the MITRE A-B model
create_human_model_bayesian_plots([1, 2, 3, 4, 5], true, true, true, true, true, 'data\Final-Exam-1\Human_Data', 'data\Final-Exam-1\Model_Data', 'MITRE',  'A-B', 'figures\Final-Exam-1\MITRE-A-B_human_model_bayesian', true, false);