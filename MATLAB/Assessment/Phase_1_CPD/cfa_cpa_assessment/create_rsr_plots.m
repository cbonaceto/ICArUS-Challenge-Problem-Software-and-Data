%% Create RSR plots comparing the best-fit pilot A-B model and the average neuaral model 
%% for the Final exam

%rsr_model{task, stage}.rsr
%rsr_model{task, stage}.rsr_bayesian
%rsr_model{task, stage}.rsr_rmse
%rsr_model{task, stage}.rsr_rmse_bayesian

%Populate data
tasks = 1:5;
numTasks = length(tasks);
taskWeights = [.05 .1 .15 .15 .4 0] / .85; %Weights for each task used in average RSR calculation
rsr_ab = cell(numTasks, 1);
rsr_model = cell(numTasks, 1);

rsr_ab{1}.rsr = 80.95;
rsr_ab{1}.rsr_bayesian = 93.64;
rsr_ab{1}.rsr_rmse = 67.52;
rsr_ab{1}.rsr_rmse_bayesian = 76.44;

rsr_ab{2}.rsr = 82.96;
rsr_ab{2}.rsr_bayesian = 77;
rsr_ab{2}.rsr_rmse = 66.02;
rsr_ab{2}.rsr_rmse_bayesian = 42.06;

rsr_ab{3}.rsr = 67.01;
rsr_ab{3}.rsr_bayesian = 61.11;
rsr_ab{3}.rsr_rmse = 60.58;
rsr_ab{3}.rsr_rmse_bayesian = 36.88;

rsr_ab{4}.rsr = 87.63;
rsr_ab{4}.rsr_bayesian = 34.17;
rsr_ab{4}.rsr_rmse = 69.49;
rsr_ab{4}.rsr_rmse_bayesian = 18.52;

rsr_ab{5}.rsr = 91.56;
rsr_ab{5}.rsr_bayesian = 66.87;
rsr_ab{5}.rsr_rmse = 78.89;
rsr_ab{5}.rsr_rmse_bayesian = 52.78;

rsr_model{1}.rsr = 59.23;
rsr_model{1}.rsr_bayesian = 79.83;
rsr_model{1}.rsr_rmse = 48.24;
rsr_model{1}.rsr_rmse_bayesian = 60.25;

rsr_model{2}.rsr = 58.70;
rsr_model{2}.rsr_bayesian = 43.22;
rsr_model{2}.rsr_rmse = 45.81;
rsr_model{2}.rsr_rmse_bayesian = 27.86;

rsr_model{3}.rsr = 71.80;
rsr_model{3}.rsr_bayesian = 42.30;
rsr_model{3}.rsr_rmse = 57.80;
rsr_model{3}.rsr_rmse_bayesian = 28.66;

rsr_model{4}.rsr = 71.01;
rsr_model{4}.rsr_bayesian = 14.29;
rsr_model{4}.rsr_rmse = 57.89;
rsr_model{4}.rsr_rmse_bayesian = 6.30;

rsr_model{5}.rsr = 82.92;
rsr_model{5}.rsr_bayesian = 51.62;
rsr_model{5}.rsr_rmse = 65.42;
rsr_model{5}.rsr_rmse_bayesian = 35.08;

%Create plots
plot_rsr(rsr_ab, rsr_model, 'A-B Model', 'Avg Neural Model', 'Final Exam',... 
    tasks, taskWeights, true, true, 'figures')