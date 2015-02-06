function plot_exam_data_sets_comparison(data, tasks, dataSetNames, abModelTitle,...
    taskWeights, matchedTrials, rsrExcludedTrials, probsComparison, rsrComparison,...
    layersComparison, layerComparisionMultipleTrials, layers, permutations, numLayersToSelect,...
    showRSRComparison, showRSRABComparison, useRSRBayesianResults,...
    showPlots, saveData, dataFolder)
%PLOT_EXAM_DATA_SETS_COMPARISON Summary of this function goes here
%   Detailed explanation goes here

if ~exist('saveData', 'var')
    saveData = false;
end
if saveData && (~exist('dataFolder', 'var') || isempty(dataFolder))
    dataFolder = '';
end
if ~exist('showPlots', 'var') || showPlots
    visible = 'on';
else
    visible = 'off';
end

numTasks = max(tasks);
task_6_index = find(tasks == 6, 1);

%% Plot average RSR for the A-B model compared to the final and pilot data for each task
%  Compute average RSR of the A-B model to data sets 1 and 2 for each task
%  Also compute average RSR across all tasks using given task weighting factors
%  Trials in the rsrExcludedTrials array are not included
if useRSRBayesianResults
    rsrTypes = [1 2];
else 
    rsrTypes = 1;
end
for rsrType = rsrTypes    
    rsr_ab_1_avg = zeros(numTasks, 1);
    rsr_ab_2_avg = zeros(numTasks, 1);
    if rsrType == 1
        fieldName = 'rsr_ab_1';
    else
        fieldName = 'rsr_bayesian_ab_1';
    end
    for task = tasks
        numTrials = size(data{1}.subjectProbsAvg{task}, 1);
        numStages = size(data{1}.subjectProbsAvg{task}, 2);
        firstStage = 1;
        if task == 5 || task == 6
            firstStage = 2;
        end        
        if isfield(rsrComparison{task}{1, 1}, fieldName);
            rsr_ab_1_sum = 0;
            rsr_ab_2_sum = 0;
            stageCount = 0;
            for trial = 1:numTrials
                for stage = firstStage:numStages
                    if ~stageExcluded(task, trial, stage, rsrExcludedTrials)
                        if rsrType == 1
                            rsr_ab_1_sum = rsr_ab_1_sum + rsrComparison{task}{trial, stage}.rsr_ab_1;
                            rsr_ab_2_sum = rsr_ab_2_sum + rsrComparison{task}{trial, stage}.rsr_ab_2;
                        else
                            rsr_ab_1_sum = rsr_ab_1_sum + rsrComparison{task}{trial, stage}.rsr_bayesian_ab_1;
                            rsr_ab_2_sum = rsr_ab_2_sum + rsrComparison{task}{trial, stage}.rsr_bayesian_ab_2;
                        end
                        stageCount = stageCount + 1;
                    end
                end
            end
            rsr_ab_1_avg(task) = rsr_ab_1_sum / stageCount;
            rsr_ab_2_avg(task) = rsr_ab_2_sum / stageCount;
            disp(['Task: ' num2str(task) ', Stage Count: ', num2str(stageCount) ', RSR ' num2str(rsr_ab_2_avg(task))]);            
        else
            rsr_ab_1_avg(task) = 0;
            rsr_ab_2_avg(task) = 0;
        end
    end
    % Compute average rsr across all tasks using task weighting factors
    rsr_ab_1_weighted_avg = sum(rsr_ab_1_avg .* taskWeights(1:numTasks)');
    rsr_ab_2_weighted_avg = sum(rsr_ab_2_avg .* taskWeights(1:numTasks)');
    disp([dataSetNames{1} ' Avg RSR: ' num2str(rsr_ab_1_weighted_avg) ', ' dataSetNames{2} ' Avg RSR: ' num2str(rsr_ab_2_weighted_avg)]);
    
    %Create Plot
    figPosition = [100, 200, 880, 660];
    figPositionLarge = [100, 50, 1000, 900];    
    if rsrType == 1
        figName = [dataSetNames{1}, '-', dataSetNames{2}, ' A-B Model Comparison (', abModelTitle, ') (Random Null Model)'];
    else
         figName = [dataSetNames{1}, '-', dataSetNames{2}, ' A-B Model Comparison (', abModelTitle, ') (Bayesian Null Model)'];
    end
    figHandle = figure('name', figName, 'position', figPosition, 'NumberTitle', 'off', 'Visible', visible);
    title(figName);
    hold on;
    
    xlabel('Task');
    xlim([0 numTasks+1]);
    set(gca,'xtick', 1:numTasks);
    if rsrType == 1
        ylabel('Avg. RSR (Random Null Model)');
    else
         ylabel('Avg. RSR (Bayesian Null Model)');
    end
    ylim([0 100]);
    set(gca, 'ytick', 0:10:100);
    
    rsr_data = [rsr_ab_1_avg, rsr_ab_2_avg];
    if numTasks == 1
        h = bar(1:2, [rsr_ab_1_avg, rsr_ab_2_avg; NaN, NaN]);
        set(h(1), 'FaceColor', 'b', 'EdgeColor', 'none');
        set(h(2), 'FaceColor', 'r', 'EdgeColor', 'none');
    else
        for i = 1:numTasks
            h = bar(i:i+1, [rsr_data(i,:); NaN, NaN], taskWeights(i));
            set(h(1), 'FaceColor', 'b', 'EdgeColor', 'none');
            set(h(2), 'FaceColor', 'r', 'EdgeColor', 'none');
        end
    end   
    
    %Plot lines at weighted RSR values for data sets 1 and 2
    plot(0:numTasks+1, repmat(rsr_ab_1_weighted_avg, 1, numTasks+2), 'b:');
    plot(0:numTasks+1, repmat(rsr_ab_2_weighted_avg, 1, numTasks+2), 'r:');
    
    %Label bars and add legend
    x = zeros(1, numTasks*2);
    y = reshape(rsr_data', 1, numTasks*2) + 2;
    for i = 1:numTasks
        x(i*2-1) = i - .18;
        x(i*2) = i + .18;
    end
    label_text = cell(1, numTasks*2);
    i = 1;
    for r = y - 2
        label_text{i} = num2str(round(r));
        i = i+1;
    end
    text(x, y, label_text,...
        'HorizontalAlignment','center',...
        'FontSize',8,...
        'FontWeight','bold');
    hleg = legend({['A-B Model - ', dataSetNames{1}, '  (N = ', num2str(max(data{1}.numSubjects)), ')'],...
        ['A-B Model - ', dataSetNames{2}, '  (N = ', num2str(max(data{2}.numSubjects)), ')']},...
        'Location', 'NorthWest');
    set(hleg, 'FontSize', 8);
    
    if saveData
        if rsrType == 1
            fileName = [dataFolder, '\', dataSetNames{1}, '_', dataSetNames{2} '_AB_Model_Comparison_Random_Null_Model'];
        else
            fileName = [dataFolder, '\', dataSetNames{1}, '_', dataSetNames{2} '_AB_Model_Comparison_Bayesian_Null_Model'];
        end
        %hgsave(figHandle, fileName, '-v6');
        saveas(figHandle, fileName, 'png');
    end
end
%%%%%%%%%%

%% Plot data for matched trials comparsions (bar graphs showing probs +
%  error bars, chi^2 test results, RSR and RSR_RMSE results, RSR
%  A-B Model Results)
if ~isempty(matchedTrials)
    for i = 1:length(matchedTrials)
        task = matchedTrials{i}.task;
        trial = matchedTrials{i}.trial;
        if task <= numTasks && ~isempty(data{1}.subjectProbsAvg{task})
            %subjectProbsAvg{task} = cell(numTrials, numStages);
            numProbs = length(data{1}.subjectProbsAvg{task}{1, 1});
            numStages = size(data{1}.subjectProbsAvg{task}, 2);
            firstStage = 1;
            if task == 5 || task == 6
                firstStage = 2;
            end
            
            figName = [dataSetNames{1}, '-', dataSetNames{2}, ', Task ', num2str(task), ', Trial ', num2str(trial), ' Comparison'];
            figHandle = figure('name', figName, 'position', figPositionLarge, 'NumberTitle', 'off', 'Visible', visible);
            title(figName);
            
            %Create subplots for each stage
            subplotNum = 0;
            numColumns = 1;
            if showRSRComparison
                numColumns = numColumns + 1;
            end
            if showRSRABComparison && isfield(rsrComparison{task}{trial, 1}, 'rsr_ab_1') && isfield(rsrComparison{task}{trial, 1}, 'rsr_ab_2') 
                numColumns = numColumns + 1;
            end
            numRows = numStages-firstStage+1;
            if numColumns == 1 && numStages > 1
                numColumns = 2;
                numRows = 2;
            end
            for stage = firstStage:numStages
                %Create subplot showing average probs comparison
                subplotNum = subplotNum + 1;
                subplot(numRows, numColumns, subplotNum);
                set(gca, 'box', 'on');
                hold on;
                t = (['Stage ' num2str(stage) ' Probs (p = ', num2str(probsComparison{task}{trial, stage}.p_chi2),')']);
                if ~showRSRComparison
                    t = [t '  (RSR = ' num2str(round(rsrComparison{task}{trial, stage}.rsr)) ')']; %#ok<AGROW>
                end
                title(t);
                xlim([0 numProbs+1]);
                set(gca,'xtick', 1:numProbs);
                ylim([0 100]);
                subjectProbs_1 = data{1}.subjectProbsAvg{task}{trial, stage}*100;
                subjectProbs_2 = data{2}.subjectProbsAvg{task}{trial, stage}*100;
                probs_data = [subjectProbs_1', subjectProbs_2'];
                h = bar(1:numProbs, probs_data);
                set(h(1), 'FaceColor', 'b');
                set(h(2), 'FaceColor', 'r');                
                %Add legend
                hleg = legend({[dataSetNames{1}, '  (N = ', num2str(max(data{1}.numSubjects)), ')'],...
                        [dataSetNames{2}, '  (N = ', num2str(max(data{2}.numSubjects)), ')']}, 'Location', 'North');               
                set(hleg, 'FontSize', 8);
                
                %Show error bars (using standard error of the mean, STD/sqrt(N))
                show_error_bars = true;
                if show_error_bars
                    subjectProbs_1_err = std(data{1}.subjectProbs{task}{trial, stage}*100)/sqrt(data{1}.numSubjects(task));
                    subjectProbs_2_err = std(data{2}.subjectProbs{task}{trial, stage}*100)/sqrt(data{2}.numSubjects(task));
                    x = zeros(1, numProbs*2);
                    %y = reshape(probs_data', 1, numProbs*2) + 2;
                    y = reshape(probs_data', 1, numProbs*2);
                    for index = 1:numProbs
                        x(index*2-1) = index - .18;
                        x(index*2) = index + .18;
                    end
                    %x                    
                    %y
                    %reshape([subjectProbs_1_err', subjectProbs_2_err'], 1, numProbs*2)
                    errorbar(x, y, reshape([subjectProbs_1_err', subjectProbs_2_err'], 1, numProbs*2), 'LineStyle', 'none', 'Color', 'k');
                end
                
                if showRSRComparison
                    %Create subplot showing RSR comparison (RSR_KLD and RSR_RMSE for Pilot
                    %as Model to Final)
                    subplotNum = subplotNum + 1;
                    subplot(numRows, numColumns, subplotNum);
                    set(gca, 'box', 'on');
                    hold on;
                    title(['Stage ' num2str(stage) ' RSR (Pilot to Final)']);
                    xlim([0 3]);
                    set(gca, 'xtick', 1:2);
                    set(gca, 'xticklabel', {'KLD', 'RMSE'});
                    ylim([0 100]);
                    bar(1:2, [rsrComparison{task}{trial, stage}.rsr rsrComparison{task}{trial, stage}.rsr_rmse], 'k');
                end
                
                if showRSRABComparison && isfield(rsrComparison{task}{trial, stage}, 'rsr_ab_1') && isfield(rsrComparison{task}{trial, stage}, 'rsr_ab_2')
                    %Create subplot showing A-B model comparison (RSR_KLD and RSR_RMSE for A-B Model
                    %to Pilot and A-B Model to final)
                    subplotNum = subplotNum + 1;
                    subplot(numRows, numColumns, subplotNum);
                    set(gca, 'box', 'on');
                    hold on;
                    title(['Stage ' num2str(stage) ' A-B Model RSR']);
                    xlim([0 3]);
                    set(gca, 'xtick', 1:2);
                    set(gca, 'xticklabel', {'KLD', 'RMSE'});
                    ylim([0 100]);
                    
                    h = bar(1:2, [rsrComparison{task}{trial, stage}.rsr_ab_1, rsrComparison{task}{trial, stage}.rsr_ab_2;...
                        rsrComparison{task}{trial, stage}.rsr_rmse_ab_1, rsrComparison{task}{trial, stage}.rsr_rmse_ab_2]);
                    set(h(1), 'FaceColor', 'b');
                    set(h(2), 'FaceColor', 'r');
                end
            end
            
            %Save Data
            if saveData
                fileName = [dataFolder, '\', dataSetNames{1}, '_', dataSetNames{2}, '_Task_', num2str(task), '_Trial_', num2str(trial), '_Comparison'];
                saveas(figHandle, fileName, 'png');
            end
            %%%%%%%%%%
            
            %Plot layer frequency comparison for matched trials in Task 6
            if task == 6 && ~isempty(data{1}.layerFrequenciesByTrial{task})
                figName = [dataSetNames{1}, '-', dataSetNames{2}, ', Task ', num2str(task), ', Trial ', num2str(trial), ' Layers Comparison'];                
                figHandle = figure('name', figName, 'position', figPosition, 'NumberTitle', 'off', 'Visible', visible);                
                title([figName, ' (p = ', num2str(layersComparison{task}{trial}.p_chi2), ')']);
                hold on;                                                
              
                allTrialsPercent = ...
                    [data{1}.layerFrequenciesByTrial{task}(trial, :)/sum(data{1}.layerFrequenciesByTrial{task}(trial, :));...
                     data{2}.layerFrequenciesByTrial{task}(trial, :)/sum(data{2}.layerFrequenciesByTrial{task}(trial, :))];
                
                numPermutations = size(allTrialsPercent, 2);          
                %Create list of all layer selection permutations names
                permutationNames = cell(1, numPermutations);
                for permutationIndex = 1:numPermutations
                    permutationNames{permutationIndex} = createPermutationName(layers, numLayersToSelect,...
                        permutations(permutationIndex,:));
                end
                
                %Plot bars for layer selection path permutations for each subject group
                h = barh(1:numPermutations, allTrialsPercent'*100);
                set(h(1), 'FaceColor', 'b');
                set(h(2), 'FaceColor', 'r');
                legend({[dataSetNames{1}, ' (', num2str(data{1}.numSubjects(task)), ')'],...
                        [dataSetNames{2}, ' (', num2str(data{2}.numSubjects(task)), ')']}, 'Location', 'NorthEast');
                
                xlabel('Percent');
                xlim([0 50]);
                set(gca,'xtick', 0:5:50);                
                ylabel('Path');
                ylim([0 numPermutations+1]);
                set(gca,'ytick', 1:numPermutations);
                set(gca, 'yticklabel', permutationNames);
                
                %Save Data
                if saveData
                    fileName = [dataFolder, '\', dataSetNames{1}, '_', dataSetNames{2}, '_Task_', num2str(task), '_Trial_', num2str(trial), '_Layers_Comparison'];
                    saveas(figHandle, fileName, 'png');
                end
            end
            %%%%%%%%%%
        end
    end
end

%% Plot layer frequency comparison that aggregates matched trials in Task 6
if ~isempty(task_6_index) && ~isempty(data{1}.layerFrequenciesMatchedTrials{task})
    figName = [dataSetNames{1}, '-', dataSetNames{2}, ', Task ', num2str(task), ', Matched Trials Layers Comparison'];
    figHandle = figure('name', figName, 'position', figPosition, 'NumberTitle', 'off', 'Visible', visible);
    title([figName, ' (p = ', num2str(layerComparisionMultipleTrials{task}.p_chi2_matched_trials), ')']);
    hold on;       
    
    allTrialsPercent = ...
        [data{1}.layerFrequenciesMatchedTrials{task}/sum(data{1}.layerFrequenciesMatchedTrials{task});...
         data{2}.layerFrequenciesMatchedTrials{task}/sum(data{2}.layerFrequenciesMatchedTrials{task})];
    
    numPermutations = size(allTrialsPercent, 2);
    %Create list of all layer selection permutations names
    permutationNames = cell(1, numPermutations);
    for permutationIndex = 1:numPermutations
        permutationNames{permutationIndex} = createPermutationName(layers, numLayersToSelect,...
            permutations(permutationIndex,:));
    end
    
    %Plot bars for layer selection path permutations for each subject group
    h = barh(1:numPermutations, allTrialsPercent'*100);
    set(h(1), 'FaceColor', 'b');
    set(h(2), 'FaceColor', 'r');
    legend({[dataSetNames{1}, ' (', num2str(data{1}.numSubjects(task)), ')'],...
        [dataSetNames{2}, ' (', num2str(data{2}.numSubjects(task)), ')']}, 'Location', 'NorthEast');
    
    xlabel('Percent');
    xlim([0 50]);
    set(gca,'xtick', 0:5:50);
    ylabel('Path');
    ylim([0 numPermutations+1]);
    set(gca,'ytick', 1:numPermutations);
    set(gca, 'yticklabel', permutationNames);
    
    %Save Data
    if saveData
        fileName = [dataFolder, '\', dataSetNames{1}, '_', dataSetNames{2}, '_Task_', num2str(task), '_Matched_Trials_Layers_Comparison'];
        saveas(figHandle, fileName, 'png');
    end
end
%%%%%%%%%%

end


%% Helper Functions
function excluded = stageExcluded(task, trial, stage, rsrExcludedTrials) 
    if ~isempty(rsrExcludedTrials) && rsrExcludedTrials(task, trial, stage) == 1
        excluded = true;
    else 
        excluded = false;
    end    
end

function name = createPermutationName(layers, numLayersToSelect, permutation)
for columnIndex = 1:numLayersToSelect
    if columnIndex > 1
        name = strcat(name, '-', layers(permutation(columnIndex)));
    else
        name = layers(permutation(columnIndex));
    end
end
name = char(name);
end