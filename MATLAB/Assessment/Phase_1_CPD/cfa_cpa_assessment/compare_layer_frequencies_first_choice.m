function [chi2, significance_threshold actual_p, chi2_yates, significance_threshold_yates actual_p_yates] = ...
    compare_layer_frequencies_first_choice(layers, permutations, subjectLayerSelections_1,...
    subjectLayerSelections_2, dataSetNames, useGoodnessOfFit,...
    showPlots, saveData, dataFolder)
%COMPARE_LAYER_FREQUENCIES Computes chi square statistic comparing the
%"first choice" layer selection frequencies of two subject groups (over all trials in the task). If the second group is
%an empty matrix, the first group will be compared to a uniform
%distribution. By first choice, we mean the number of times each layer type
%(IMINT, MOVINT, SIGINT, SOCINT) was selected first.
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

%% Aggregate first-choice layer selection data by subject for each subject group
%permutationCount = cell(2,1);
%permutationPercent = cell(2,1);
numLayers = length(layers);
allTrialsCount = zeros(2, numLayers);
allTrialsPercent = zeros(2, numLayers);
numSubjects = zeros(2, 1);
for i=1:2
    if i==1
        subjectLayerSelections = subjectLayerSelections_1;
    else
        subjectLayerSelections = subjectLayerSelections_2;
    end
    if isempty(subjectLayerSelections)
        %Compute first choice counts based on chance
        allTrialsCount(i,:) = repmat(sum(allTrialsCount(1,:))/numLayers, 1, numLayers);
        allTrialsPercent(i,:) = allTrialsCount(i,:) ./ sum(allTrialsCount(i,:));
    else
        %Compute first choice counts
        numSubjects(i) = size(subjectLayerSelections, 1);
        numTrials = size(subjectLayerSelections, 2);
        %permutationCount{i} = zeros(numSubjects(i), numPermutations);
        for subject = 1:numSubjects(i)
            for trial = 1:numTrials
                permutation = subjectLayerSelections(subject, trial);
                firstLayer = getFirstLayer(permutation, permutations);
                allTrialsCount(i, firstLayer) = allTrialsCount(i, firstLayer) + 1;
                %permutationCount{i}(subject, permutation) = permutationCount{i}(subject, permutation) + 1;
            end
        end
        %permutationPercent{i} = zeros(numSubjects(i), numPermutations);
        %for subject = 1:numSubjects(i)
        %    permutationPercent{i}(subject,:) = permutationCount{i}(subject,:) ./ sum(permutationCount{i}(subject,:)) * 100;
        %end
        %allTrialsCount(i,:) = sum(permutationCount{i});
        allTrialsPercent(i,:) = allTrialsCount(i,:) ./ sum(allTrialsCount(i,:));
    end
end
%allTrialsCount(1,:)
%allTrialsCount(2,:)
%allTrialsPercent(1,:)
%allTrialsPercent(2,:)
%allTrialsCount
%allTrialsPercent

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

%% Plot results (show comparison of first choice layer selection frequencies for each group and chi square value)
figPosition = [100, 100, 880, 660];
titleName = {[dataSetNames, ' First Choice Layer Selection Frequencies Comparison   [N1: ', num2str(numSubjects(1)), ', N2: ', num2str(numSubjects(2)), ']'];...
           ['p = ', num2str(actual_p), ' (With Yates Correction, p = ', num2str(actual_p_yates), ')']};
figName = 'Layer Selection Frequencies Comparison';
figHandle = figure('name', figName, 'position', figPosition, 'NumberTitle', 'off', 'Visible', visible);
hold on;
title(titleName);

%Plot bars for layer selection path permutations for each subject group
h = bar(1:numLayers, allTrialsPercent'*100);
set(h(1), 'FaceColor', 'b');
%set(h(2), 'FaceColor', 'k');
legend({['Subject Group 1 (', num2str(numSubjects(1)), ')'],...
    ['Subject Group 2 (', num2str(numSubjects(2)), ')']}, 'Location', 'NorthEast');
%allTrialsPercent'*100

xlabel('Layer');
xlim([0 numLayers+1]);
set(gca,'xtick', 1:numLayers);
set(gca, 'xticklabel', layers);

ylabel('% Time Selected First');
ylim([0 60]);
set(gca,'ytick', 0:5:60);

%Save figure
if saveData
    fileName = [dataFolder, '\', dataSetNames '_First_Choice_Layer_Selection_Frequencies_Comparison'];    
    hgsave(fileName, '-v6');
    saveas(figHandle, fileName, 'png');
end
end

function firstLayer = getFirstLayer(permutationIndex, permutations) 
   firstLayer = permutations(permutationIndex, 1); 
end