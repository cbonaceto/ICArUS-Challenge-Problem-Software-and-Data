% score_with_training_data

% Train net on data, then compute score using resulting net

% learner = NewLearner_bn (GRADIENT_DESCENT_LEARNING, const char* info, environ_ns* env);
% % method = COUNTING_LEARNING, EM_LEARNING, GRADIENT_DESCENT_LEARNING
% % can SetLearnerMaxIters(learner,10000) or SetLearnerMaxTol(learner,1e-5)
% 
% cases = NewCaseset
% AddFileToCaseset_cs (cases, datafile, 1.0, NULL);
% 
% degree = 1; % assume each case is shown once
% LearnCPTs (learner, const nodelist_bn* nodes, const caseset_cs* cases, double degree);

DeleteCaseset (cases);
DeleteLearner(learner);
