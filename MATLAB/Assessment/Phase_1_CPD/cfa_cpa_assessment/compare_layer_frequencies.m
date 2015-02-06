function [chi2, significance_threshold actual_p, chi2_yates, significance_threshold_yates actual_p_yates] = ...
    compare_layer_frequencies(layers, permutations, numLayersToSelect, subjectLayerSelections_1,...
    subjectLayerSelections_2, dataSetNames, useGoodnessOfFit, applyModalLayerAdjustment,...
    showPlots, saveData, dataFolder)
%COMPARE_LAYER_FREQUENCIES Computes chi square statistic comparing the
%layer selection frequencies of two subject groups (over all trials in the task). If the second group is
%an empty matrix, the first group will be compared to a uniform
%distribution.
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

%% Aggregate layer selection permutation data by subject for each subject group
numPermutations = size(permutations, 1);
permutationCount = cell(2,1);
permutationPercent = cell(2,1);
allTrialsCount = zeros(2, numPermutations);
allTrialsPercent = zeros(2, numPermutations);
numSubjects = zeros(2, 1);
for i=1:2
    if i==1
        subjectLayerSelections = subjectLayerSelections_1;
    else
        subjectLayerSelections = subjectLayerSelections_2;
    end
    if isempty(subjectLayerSelections)
        %Compute counts based on chance
        allTrialsCount(i,:) = repmat(sum(allTrialsCount(1,:))/numPermutations, 1, numPermutations);
        allTrialsPercent(i,:) = allTrialsCount(i,:) ./ sum(allTrialsCount(i,:));
    else
        numSubjects(i) = size(subjectLayerSelections, 1);
        numTrials = size(subjectLayerSelections, 2);
        permutationCount{i} = zeros(numSubjects(i), numPermutations);
        for subject = 1:numSubjects(i)
            for trial = 1:numTrials
                permutation = subjectLayerSelections(subject, trial);
                permutationCount{i}(subject, permutation) = permutationCount{i}(subject, permutation) + 1;
            end
        end
        permutationPercent{i} = zeros(numSubjects(i), numPermutations);
        for subject = 1:numSubjects(i)
            permutationPercent{i}(subject,:) = permutationCount{i}(subject,:) ./ sum(permutationCount{i}(subject,:)) * 100;
        end
        allTrialsCount(i,:) = sum(permutationCount{i});
        allTrialsPercent(i,:) = allTrialsCount(i,:) ./ sum(allTrialsCount(i,:));
    end
end
%allTrialsCount(1,:)
%allTrialsCount(2,:)
%allTrialsPercent(1,:)
%allTrialsPercent(2,:)
%allTrialsCount
%allTrialsPercent

if applyModalLayerAdjustment
    %Determine whether the two groups share the same modal sequence.
    [~, modal_index(1)] = max(allTrialsPercent(1,:));
    [~, modal_index(2)] = max(allTrialsPercent(2,:));
    if modal_index(1) == modal_index(2)
        %If they do, subtract the difference in proportions for the group with
        %the larger modal sequence from the group with the smaller modal
        %sequence and apply that proportion to all remaining proportions in
        %the group with the larger modal sequence
        modal_percent(1) = allTrialsPercent(1, modal_index(1));
        modal_percent(2) = allTrialsPercent(2, modal_index(2));
        difference = abs(modal_percent(1) - modal_percent(2));
        if difference > 0
            if modal_percent(1) > modal_percent(2)
               data_set_index = 1;
            else
                data_set_index = 2;
            end
            %oldCount = sum(allTrialsCount(data_set_index,:))
            allTrialsPercent(data_set_index, modal_index(data_set_index)) = modal_percent(data_set_index) - difference;
            percentToAdd = difference/(numPermutations-1);
            for i = 1:size(allTrialsPercent(data_set_index, :), 2)                               
                if i ~= modal_index(data_set_index)                   
                    allTrialsPercent(data_set_index, i) = allTrialsPercent(data_set_index, i) + percentToAdd;
                end
            end            
            allTrialsCount(data_set_index,:) = allTrialsPercent(data_set_index,:) * sum(allTrialsCount(data_set_index,:));
            %newCount = sum(allTrialsCount(data_set_index,:))
        end
    end
end

%% Compute chi square between subject groups
if useGoodnessOfFit
    %Treat first group as observed, compute expected frequencies based on the proportions of the second group
    %allTrialsPercent(1,:)
    %allTrialsPercent(2,:)
    %allTrialsCount(1,:)
    expected = allTrialsPercent(2,:) * sum(allTrialsCount(1,:));
    expected(expected==0) = 1;
    [chi2, significant, significance_threshold actual_p] = chi2gof(allTrialsCount(1,:), expected, 0.05, false);
    [chi2_yates, significant_yates, significance_threshold_yates actual_p_yates] = chi2gof(allTrialsCount(1,:), expected, 0.05, true);
    %[chi2, significant, significance_threshold actual_p] = chi2gof(allTrialsPercent(1,:), allTrialsPercent(2,:), 0.05, false)
    %[chi2_yates, significant_yates, significance_threshold_yates actual_p_yates] = chi2gof(allTrialsPercent(1,:), allTrialsPercent(2,:), 0.05, true);
else
    %Compute chi square homogeneity of proportions test between groups
    [chi2, significant, significance_threshold actual_p] = chi2test(allTrialsCount, 0.05, false);
    [chi2_yates, significant_yates, significance_threshold_yates actual_p_yates] = chi2test(allTrialsCount, 0.05, true);
    %[chi2, significant, significance_threshold actual_p] = chi2test(allTrialsPercent*100, 0.05, false);
    %[chi2_yates, significant_yates, significance_threshold_yates actual_p_yates] = chi2test(allTrialsPercent*100, 0.05, true);
end
%sum(sum(allTrialsCount))
%cramers_v = sqrt(chi2/(sum(sum(allTrialsCount))));

%% Create list of all layer selection permutations names
permutationNames = cell(1, numPermutations);
for permutationIndex = 1:numPermutations
    permutationNames{permutationIndex} = createPermutationName(layers, numLayersToSelect,...
        permutations(permutationIndex,:));
end

%% Plot results (show comparison of layer selection frequencies for each group and chi square value)
figPosition = [100, 100, 880, 660];
titleName = {[dataSetNames, ' Layer Selection Frequencies Comparison   [N1: ', num2str(numSubjects(1)), ', N2: ', num2str(numSubjects(2)), ']'];...
           ['p=', num2str(actual_p), ' (With Yates Correction, p=', num2str(actual_p_yates), ')']};
% if significant
%     sig = 'yes';
% else
%     sig = 'no';
% end
% if significant_yates
%     sig_yates = 'yes';
% else
%     sig_yates = 'no';
% end
%titleName = {[dataSetNames, ' Layer Selection Frequencies Comparison   [N1: ', num2str(numSubjects(1)), ', N2: ', num2str(numSubjects(2)), ']'];...
%           ['Significantly Different at 0.05: ', sig, ', p=', num2str(actual_p), ' (', sig_yates, ' with Yates correction, p=', num2str(actual_p_yates), ')'],;
%           ['Cramer''s V: ', num2str(cramers_v)]};
figName = 'Layer Selection Frequencies Comparison';
figHandle = figure('name', figName, 'position', figPosition, 'NumberTitle', 'off', 'Visible', visible);
hold on;
title(titleName);

%Plot bars for layer selection path permutations for each subject group
h = barh(1:numPermutations, allTrialsPercent'*100);
set(h(1), 'FaceColor', 'b');
%set(h(2), 'FaceColor', 'k');
legend({['Subject Group 1 (', num2str(numSubjects(1)), ')'],...
    ['Subject Group 2 (', num2str(numSubjects(2)), ')']}, 'Location', 'NorthEast');
%permutationNames
%allTrialsPercent'*100

xlabel('Percent');
xlim([0 50]);
set(gca,'xtick', 0:5:50);

ylabel('Path');
ylim([0 numPermutations+1]);
set(gca,'ytick', 1:numPermutations);
set(gca, 'yticklabel', permutationNames);

%Save figure
if saveData
    fileName = [dataFolder, '\', dataSetNames '_Layer_Selection_Frequencies_Comparison'];    
    hgsave(fileName, '-v6');
    saveas(figHandle, fileName, 'png');
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