function [data, probsComparison, neComparison, rsrComparison, layersComparison, layerComparisionMultipleTrials] = ...
    compare_exam_data_sets(subject_data_1, subject_data_2, tasks, dataSetNames, matchedTrials, displayText)
%COMPARE_EXAM_DATA_SETS Compare data between two exam data sets (e.g., Pilot and Final)
%   Detailed explanation goes here

%% Compute average probs, average negentropy, and layer selection frequencies for each data set
data = cell(2, 1);
numTasks = max(tasks);
for i = 1:2
    if i == 1
        subject_data = subject_data_1;
    else
        subject_data = subject_data_2;
    end
    
    %Compute metrics for exam data set
    data{i} = compute_exam_metrics(subject_data, tasks);
    
    %If Task 6 is present, also aggregate Task 6 matched trials layer frequencies
    layerFrequenciesMatchedTrials = cell(numTasks, 1); %Layer selection frequencies aggregated overa matched trials in a task (Task 6 only)    
    layerFrequenciesByTrial = data{i}.layerFrequenciesByTrial;
    if length(layerFrequenciesByTrial) > 5 && ~isempty(layerFrequenciesByTrial{6})
        task_6_matched_trials = [];
        if ~isempty(matchedTrials)
            for index = 1:length(matchedTrials)
                if(matchedTrials{index}.task == 6)
                    task_6_matched_trials = [task_6_matched_trials, matchedTrials{index}.trial]; %#ok<AGROW>
                end
            end
            if ~isempty(task_6_matched_trials)
                %Aggregate layer frequencies across matched trials
                numPermutations = size(subject_data{6}.layerSelectionData.permutations, 1);
                layerFrequenciesMatchedTrials{6} = zeros(1, numPermutations);
                for matched_trial = task_6_matched_trials
                    layerFrequenciesMatchedTrials{6}(1,:) = layerFrequenciesMatchedTrials{6}(1,:) + ...
                        layerFrequenciesByTrial{6}(matched_trial, :);
                end
            end
        end
    end
    data{i}.layerFrequenciesMatchedTrials = layerFrequenciesMatchedTrials;
end

%% Compute RSR by treating data set 1 as the model and data set 2 as the human data set
%  Also, Determine if there are significant differences between the data sets using t-tests to compare negentropies and
%  ANOVA to compare the probabilities
compare_ne_avg = false;
compare_ne_dist = false;
compare_probs_chi2 = true;
compare_probs_anova = false;

rsrComparison = cell(numTasks, 1);
neComparison = cell(numTasks, 1);
probsComparison = cell(numTasks, 1);
layersComparison = cell(numTasks, 1);
layerComparisionMultipleTrials = cell(numTasks, 1);
for task = tasks
    n1 = data{1}.numSubjects(task); %Number of subjects in data set 1 for the current task
    n2 = data{2}.numSubjects(task); %Number of subjects in data set 2 for the current task
    numProbs = size(data{1}.subjectProbs{task}{1, 1}, 2);
    populations = [ones(1, n1*numProbs) repmat(2, 1, n2*numProbs)]; %Used for ANOVA
    factors_length = length(populations); %Used for ANOVA
    factors = zeros(1, factors_length); %Used for ANOVA
    for i = 1:numProbs
        factors(i:numProbs:factors_length) = i;
    end
    df = n1 + n2 - 2; %#ok<NASGU> %Degrees of freedom used for t-tests
    
    abProbsPresent = isfield(subject_data_1{task}, 'abProbs') && isfield(subject_data_2{task}, 'abProbs');
    numTrials = size(data{1}.subjectProbsAvg{task}, 1);
    numStages = size(data{1}.subjectProbsAvg{task}, 2);
    rsrComparison{task} = cell(numTrials, numStages);
    neComparison{task} = cell(numTrials, numStages);
    probsComparison{task} = cell(numTrials, numStages);
    if task == 6 && ~isempty(data{1}.layerFrequenciesByTrial{task}) && ~isempty(data{2}.layerFrequenciesByTrial{task})
        layersComparison{task} = cell(numTrials);
        
        %Perform chi square test comparing layer frequency aggregations across all trials
        if displayText
            disp('  ');
            disp('-----------------------------');
            disp(['Task: ', num2str(task), ' Aggregated Trials Layer Frequencies Comparison']);
        end
        nonzero_bins = (data{1}.layerFrequenciesAllTrials{task}(:) + data{2}.layerFrequenciesAllTrials{task}(:)) ~= 0;
        chi2_data = [data{1}.layerFrequenciesAllTrials{task}(nonzero_bins); data{2}.layerFrequenciesAllTrials{task}(nonzero_bins)];
        [~, ~, ~, p] = chi2test(chi2_data, 0.05, false);
        layerComparisionMultipleTrials{task}.p_chi2_all_trials = p;
        [~, ~, ~, p_yates] = chi2test(chi2_data, 0.05, true);
        layerComparisionMultipleTrials{task}.p_chi2_all_trials_yates = p_yates;
        layerComparisionMultipleTrials{task}.p_fisher_all_trials = FisherExactTest(chi2_data);
        if displayText
            disp(['p (chi^2 comparing layer frequencies all trials) = ', num2str(layerComparisionMultipleTrials{task}.p_chi2_all_trials)]);
            disp(['p (chi^2 comparing layer frequencies all trials, Yates correction) = ', num2str(layerComparisionMultipleTrials{task}.p_chi2_all_trials_yates)]);
            disp(['p (Fisher Exact comparing layer frequencies all trials) = ', num2str(layerComparisionMultipleTrials{task}.p_fisher_all_trials)]);
        end
        
        if ~isempty(data{1}.layerFrequenciesMatchedTrials{task}) && ~isempty(data{2}.layerFrequenciesMatchedTrials{task})
            %Perform chi square tests comparing layer frequency aggregations across matched trials
            nonzero_bins = (data{1}.layerFrequenciesMatchedTrials{task}(:) + data{2}.layerFrequenciesMatchedTrials{task}(:)) ~= 0;
            chi2_data = [data{1}.layerFrequenciesMatchedTrials{task}(nonzero_bins); data{2}.layerFrequenciesMatchedTrials{task}(nonzero_bins)];
            [~, ~, ~, p] = chi2test(chi2_data, 0.05, false);
            layerComparisionMultipleTrials{task}.p_chi2_matched_trials = p;
            [~, ~, ~, p_yates] = chi2test(chi2_data, 0.05, true);
            layerComparisionMultipleTrials{task}.p_chi2_matched_trials_yates = p_yates;
            layerComparisionMultipleTrials{task}.p_fisher_matched_trials = FisherExactTest(chi2_data);
            if displayText
                disp(['p (chi^2 comparing layer frequencies matched trials) = ', num2str(layerComparisionMultipleTrials{task}.p_chi2_matched_trials)]);
                disp(['p (chi^2 comparing layer frequencies matched trials, Yates correction) = ', num2str(layerComparisionMultipleTrials{task}.p_chi2_matched_trials_yates)]);
                disp(['p (Fisher Exact comparing layer frequencies matched trials, Yates correction) = ', num2str(layerComparisionMultipleTrials{task}.p_fisher_matched_trials)]);
            end
        end
    end    
    for trial = 1:numTrials
        if task==6 && ~isempty(layersComparison{task})
            %Perform chi square tests comparing layer frequency
            %distributions for Task 6
            if displayText
                disp('  ');
                disp('-----------------------------');
                disp(['Task: ', num2str(task), ', Trial: ', num2str(trial), ' Layer Frequencies Comparison']);
            end
            %Remove frequency bins where both data sets have a 0 (this is a bit of a hack)
            nonzero_bins = (data{1}.layerFrequenciesByTrial{task}(trial, :) + data{2}.layerFrequenciesByTrial{task}(trial, :)) ~= 0;
            %data{1}.layerFrequenciesByTrial{task}(trial, nonzero_bins)
            %data{2}.layerFrequenciesByTrial{task}(trial, nonzero_bins)
            chi2_data = [data{1}.layerFrequenciesByTrial{task}(trial, nonzero_bins); data{2}.layerFrequenciesByTrial{task}(trial, nonzero_bins)];            
            [~, ~, ~, p] = chi2test(chi2_data, 0.05, false);
            layersComparison{task}{trial}.p_chi2 = p;
            [~, ~, ~, p_yates] = chi2test(chi2_data, 0.05, true);
            layersComparison{task}{trial}.p_chi2_yates = p_yates;            
            %As another hack, we also make any zero bins 1 and compute chi^2 again
            for i = 1:2
                zero_bins = find(chi2_data(i,:)==0);
                if ~isempty(zero_bins)
                     chi2_data(i,:) = chi2_data(i,:) + (chi2_data(i,:)/sum(chi2_data(i,:)))*length(zero_bins);
                     chi2_data(i,zero_bins) = 1;
                end                
            end
            [~, ~, ~, p] = chi2test(chi2_data, 0.05, false);
            layersComparison{task}{trial}.p_chi2_0_to_1 = p;
            if displayText
                %disp([dataSetNames{1}, ' Layer Frequencies: ', num2str(data{1}.layerFrequenciesByTrial{task}(trial,:)/sum(data{1}.layerFrequenciesByTrial{task}(trial,:)))]);
                %disp([dataSetNames{2}, ' Layer Frequencies: ', num2str(data{2}.layerFrequenciesByTrial{task}(trial,:)/sum(data{2}.layerFrequenciesByTrial{task}(trial,:)))]);
                disp(['p (chi^2 comparing layer frequencies) = ', num2str(layersComparison{task}{trial}.p_chi2)]);
                disp(['p (chi^2 comparing layer frequencies, Yates correction) = ', num2str(layersComparison{task}{trial}.p_chi2_yates)]);
                disp(['p (chi^2 comparing layer frequencies, 1 replaces 0 correction) = ', num2str(layersComparison{task}{trial}.p_chi2_0_to_1)]);
            end
        end
        for stage = 1:numStages
            if displayText
                disp('  ');
                disp('-----------------------------');
                disp(['Task: ', num2str(task), ', Trial: ', num2str(trial), ', Stage: ', num2str(stage)]);
            end
            
            %Compute RSR, RSR_Bayesian, RSR(RMSE), and RSR(RMSE)_Bayesian by treating data set 1 as model and data set 2 as
            %average human
            probs1 = data{1}.subjectProbsAvg{task}{trial, stage};
            probs2 = data{2}.subjectProbsAvg{task}{trial, stage};
            normativeProbs = data{2}.normativeProbsAvg{task}{trial, stage};
            rsrComparison{task}{trial, stage}.rsr = rsr(probs2, probs1, .01);
            rsrComparison{task}{trial, stage}.rsr_bayesian = rsr(probs2, probs1, .01, normativeProbs);
            rsrComparison{task}{trial, stage}.rsr_rmse = rsr_rmse(probs2, probs1);            
            rsrComparison{task}{trial, stage}.rsr_rmse_bayesian = rsr_rmse(probs2, probs1, normativeProbs);            
            
            if displayText
                disp([dataSetNames{1}, ' Probs: ', num2str(probs1)]);
                disp([dataSetNames{2}, ' Probs: ', num2str(probs2)]);
                disp('   ');
                disp(['RSR(', dataSetNames{1},' to ', dataSetNames{2},') = ', num2str(rsrComparison{task}{trial, stage}.rsr)]);
                disp(['RSR_Bayesian(', dataSetNames{1},' to ', dataSetNames{2},') = ', num2str(rsrComparison{task}{trial, stage}.rsr_bayesian)]);
                disp(['RSR_RMSE(', dataSetNames{1},' to ', dataSetNames{2},') = ', num2str(rsrComparison{task}{trial, stage}.rsr_rmse)]);
                disp(['RSR_RMSE_Bayesian(', dataSetNames{1},' to ', dataSetNames{2},') = ', num2str(rsrComparison{task}{trial, stage}.rsr_rmse_bayesian)]);
            end
            
            if abProbsPresent
                %Compute RSR, RSR_Bayesian, RSR(RMSE), and RSR(RMSE)_Bayesian between the A-B model probs and data set 1
                abProbs = subject_data_1{task}.abProbs{trial}(stage, :);
                normativeProbs = data{1}.normativeProbsAvg{task}{trial, stage};
                rsrComparison{task}{trial, stage}.rsr_ab_1 = rsr(probs1, abProbs, .01);                
                rsrComparison{task}{trial, stage}.rsr_bayesian_ab_1 = rsr(probs1, abProbs, .01, normativeProbs); 
                rsrComparison{task}{trial, stage}.rsr_rmse_ab_1 = rsr_rmse(probs1, abProbs);
                rsrComparison{task}{trial, stage}.rsr_rmse_bayesian_ab_1 = rsr_rmse(probs1, abProbs, normativeProbs);
                
                %Compute RSR, RSR_Bayesian, RSR(RMSE), and RSR(RMSE)_Bayesian between the A-B model probs and data set 2
                abProbs = subject_data_2{task}.abProbs{trial}(stage, :);
                normativeProbs = data{2}.normativeProbsAvg{task}{trial, stage};
                rsrComparison{task}{trial, stage}.rsr_ab_2 = rsr(probs2, abProbs, .01);
                %disp(['Probs ' num2str(probs2) ', AB Probs: ' num2str(abProbs) ', RSR: ' num2str(rsrComparison{task}{trial, stage}.rsr_ab_2)]);
                rsrComparison{task}{trial, stage}.rsr_bayesian_ab_2 = rsr(probs2, abProbs, .01, normativeProbs);
                rsrComparison{task}{trial, stage}.rsr_rmse_ab_2 = rsr_rmse(probs2, abProbs);
                rsrComparison{task}{trial, stage}.rsr_rmse_bayesian_ab_2 = rsr_rmse(probs2, abProbs, normativeProbs);
                
                if displayText
                    disp(['RSR(AB Model to ', dataSetNames{1} ,') = ', num2str(rsrComparison{task}{trial, stage}.rsr_ab_1)]);
                    disp(['RSR_Bayesian(AB Model to ', dataSetNames{1} ,') = ', num2str(rsrComparison{task}{trial, stage}.rsr_bayesian_ab_1)]);
                    disp(['RSR(AB Model to ', dataSetNames{2} ,') = ', num2str(rsrComparison{task}{trial, stage}.rsr_ab_2)]);
                    disp(['RSR_Bayesian(AB Model to ', dataSetNames{2} ,') = ', num2str(rsrComparison{task}{trial, stage}.rsr_bayesian_ab_2)]);
                end
            end
            
            if compare_ne_avg
                %Perform t-test comparing negentropies of average probabilities for stage (p =
                %probability of null hypothesis that both groups are the same)
                ne_mean_1 = data{1}.subjectNeAvg{task}(trial, stage);
                ne_mean_2 = data{2}.subjectNeAvg{task}(trial, stage);
                s1 = data{1}.subjectNeErr{task}(trial, stage);
                s2 = data{2}.subjectNeErr{task}(trial, stage);
                t = abs((ne_mean_1-ne_mean_2)/(sqrt(((n1-1)*s1^2 + (n2-1)*s2^2)/(n1 + n2 - 1)) * sqrt(1/n1 + 1/n2)));
                neComparison{task}{trial, stage}.p_avg = (1 - tcdf(t, df)) * 2;
                if displayText
                    disp(['p (ne from avg probs) = ', num2str(neComparison{task}{trial, stage}.p_avg)]);
                end
            end
            
            %Perform t-test comparing negentropy distributions stage (p =
            %probability of null hypothesis that both groups are the same)
            if compare_ne_dist
                [~, p] = ttest2(data{1}.subjectNe{task}{trial, stage}, data{2}.subjectNe{task}{trial, stage}, 0.05, 'both', 'equal'); %#ok<*UNRCH>
                neComparison{task}{trial, stage}.p_dist = p;
                if displayText
                    disp(['p (ne from ne distribution) = ', num2str(p)]);
                end
            end
            
            if compare_probs_chi2
                %Perform chi square goodness of fit test comparing average probabilities
                %Treat data set 1 as expected and data set 2 as observed
                %chi2_data = [data{1}.subjectProbsAvg{task}{trial, stage}; data{2}.subjectProbsAvg{task}{trial, stage}]*100;
                observed = data{2}.subjectProbsAvg{task}{trial, stage} * 100;
                expected = data{1}.subjectProbsAvg{task}{trial, stage} * 100;
                [~, ~, ~, p] = chi2gof(observed, expected, 0.05, false);
                %[~, ~, ~, p] = chi2test(chi2_data, 0.05, false);
                probsComparison{task}{trial, stage}.p_chi2 = p;
                [~, ~, ~, p_yates] = chi2gof(observed, expected, 0.05, true);
                %[~, ~, ~, p_yates] = chi2test(chi2_data, 0.05, true);
                probsComparison{task}{trial, stage}.p_chi2_yates = p_yates;
                if displayText
                    disp(['p (chi^2 comparing prob distributions) = ', num2str(probsComparison{task}{trial, stage}.p_chi2)]);
                end
            end
            
            if compare_probs_anova
                %Perform a 2-way ANOVA comparing probability distributions for
                %stage (p = probability of null hypothesis that both groups are the same)
                %reshape(data{1}.subjectProbs{task}{trial, stage}.',1,[])
                probs = [reshape(data{1}.subjectProbs{task}{trial, stage}.',1,[]) reshape(data{2}.subjectProbs{task}{trial, stage}.',1,[])];
                %p, ~, stats] = anovan(reshape(probs.',1,[]), {factors; pops2}, 'model', 'interaction', 'display', 'off')
                p = anovan(probs, {factors; populations}, 'model', 'interaction', 'display', 'off');
                %p = anovan(probs, {populations}, 'display', 'off');
                probsComparison{task}{trial, stage}.p_anova = p(3);
                if displayText
                    disp(['p (anova comparing prob distributions) = ', num2str(probsComparison{task}{trial, stage}.p_anova)]);
                end
            end
        end
    end
end

end