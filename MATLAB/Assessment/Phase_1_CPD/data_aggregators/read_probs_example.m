% Parse the average results file for Task 4
avgResults = read_probs_file('Task_4_Avg_Results.csv');

% Get the probabilities for trial 2 stage 2
trial2Stage2 = avgResults{2}(2,:) %#ok<*NASGU>

% Average the probabilities for trial 3 for both stages
trial3Avg = sum(s{3})/2 %#ok<*NOPTS>

% Parse the input file for Task 4
likelihoods = read_probs_file('Task_4_Input_File.csv');

% Get the likelihoods for trial 2 stage 2
trial2Stage2 = likelihoods{2}(2,:)
