function plot_mean_median_comparison(subjects, subjectProbsByStage, normativeProbsByStage,...
    subjectProbsAvgByStage, subjectProbsMedianByStage, normativeProbsAvgByStage,...
    subjectNe, normativeNe, subjectNeAvg, subjectNeMedian, normativeNeAvg,...
    normativeStrategyName, missionNum, saveData, showPlots, dataFolder) %#ok<INUSL>
%PLOT_MEAN_MEDIAN_COMPARISON Plots a comparison of the mean human
%attack probabilities (on the last stage of each trial) to the median human probabilities.

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

numSubjects = size(subjectProbsByStage{1, 1}, 1);
numTrials = size(subjectProbsByStage{1, 1}, 2);
numLocations = size(subjectProbsByStage, 2);
stage = size(subjectProbsByStage, 1) - 1; % Always the last stage (final posteriors)

%% Plot Human Probabilities (Mean/Median/Dist) and Bayesian Probabilities
missionName = ['Mission ', num2str(missionNum)];
figPosition = [60, 60, 1000, 800];
showNormativeVariance = false;
lineWidth = 1.5;
normativeLineWidth = 1.25;
for location = 1:numLocations
    if numLocations > 1
        figName = [missionName, ', Stage ', num2str(stage), ', Location ', num2str(location),...
            ' Mean/Median Probs Comparison   [Sample Size: ', num2str(numSubjects), ']'];
    else
        figName = [missionName, ', Stage ', num2str(stage),...
            ' Mean/Median Probs Comparison   [Sample Size: ', num2str(numSubjects), ']'];
    end
    
    figHandle = figure('name', figName, 'position', figPosition, 'NumberTitle', 'off', 'Visible', visible);
    hold on;
    legendText = cell(3, 1);
    h = zeros(3, 1);
    title(figName);
    xlabel('Trial');
    xlim([0 numTrials+1]);
    set(gca,'xtick', 1:numTrials);    
    ylim([0 1]);
    ylabel('P(Attack)');
    
    %Plot average subject probabilities as a blue solid line    
    h(1) = plot(subjectProbsAvgByStage{stage, location}(:),...
        '-ob', 'LineWidth', lineWidth);
    legendText{1} = 'Human Mean';
    
    %Plot median subject probabilities as a purple solid line
    h(2) = plot(subjectProbsMedianByStage{stage, location}(:),...
        '-om', 'LineWidth', lineWidth);
    legendText{2} = 'Human Median';
    
    %Plot normative probabilities as a black solid line
    h(3) = plot(normativeProbsAvgByStage{stage, location}(:),...
        '-*k', 'LineWidth', normativeLineWidth);
    legendText{3} = normativeStrategyName;    
    
    %Plot error bars that show the std for the subject probability distrubutions at each trial
    subject_e = zeros(1, numTrials);
    normative_e = zeros(1, numTrials);
    for trial = 1:numTrials
        subject_e(trial) = sem(subjectProbsByStage{stage, location}(:, trial), numSubjects);
        normative_e(trial) = sem(normativeProbsByStage{stage, location}(:, trial), numSubjects);
    end
    errorbar(1:numTrials, subjectProbsAvgByStage{stage, location}(:), subject_e, '-b');
    %Also show normative probability distribution
    if showNormativeVariance
        errorbar(1:numTrials,...
            normativeProbsAvgByStage{stage, location}(:), normative_e, '-k'); %#ok<UNRCH>
    end
    
    %Create the legend  
    legend(h, legendText, 'Location', 'SouthOutside');
    
    %Decrease figure margins
    tightfig;
    
    %Save the figure
    if saveData
        if numLocations > 1
            fileName = [dataFolder, '\', missionName, '_Stage_', num2str(stage), '_Location_',...
                num2str(location), '_Mean_Median_Probs_Comparison'];
        else
            fileName = [dataFolder, '\', missionName, '_Stage_', num2str(stage),...
                '_Mean_Median_Probs_Comparison'];
        end
        saveas(figHandle, fileName, 'png');
    end
end

%% Plot Human Ne (Using Avg Probs and Median Probs, Dist) and Bayesian Ne
figName = [missionName, ', Stage ', num2str(stage),...
    ' Mean/Median Ne Comparison  [Sample Size: ', num2str(numSubjects), ']'];
figHandle = figure('name', figName, 'position', figPosition, 'NumberTitle', 'off', 'Visible', visible);
hold on;
legendText = cell(3, 1);
h = zeros(3, 1);
title(figName);
xlabel('Trial');
xlim([0 numTrials+1]);
set(gca,'xtick', 1:numTrials);
ymax = ylim;
ylim([0 ymax(2)]);
%ylim([0 1]);
ylabel('Negentropy(P(Attack), P(~Attack))');

%Plot average subject negentropy as a blue solid line
h(1) = plot(subjectNeAvg(stage, :), '-ob', 'LineWidth', lineWidth);
legendText{1} = 'Human Mean';

%Plot median subject negentropy as a purble solid line
h(2) = plot(subjectNeMedian(stage, :), '-om', 'LineWidth', lineWidth);
legendText{2} = 'Human Median';

%Plot normative negentropy as a black solid line
h(3) = plot(normativeNeAvg(stage, :), '-*k', 'LineWidth', normativeLineWidth);
legendText{3} = normativeStrategyName;

%Plot error bars that show the std for the subject negentropy distrubution at each trial
subject_e = zeros(1, numTrials);
normative_e = zeros(1, numTrials);
for trial = 1:numTrials
    subject_e(trial) = sem(subjectNe{stage}(:, trial), numSubjects);
    normative_e(trial) = sem(normativeNe{stage}(:, trial), numSubjects);
end
errorbar(1:numTrials, subjectNeAvg(stage, :), subject_e, '-b');
%Also show normative negentropy distribution
if showNormativeVariance
    errorbar(1:numTrials,...
        normativeNeAvg(stage, :), normative_e, '-k'); %#ok<UNRCH>
end

%Create the legend
legend(h, legendText, 'Location', 'SouthOutside');

%Decrease figure margins
tightfig;

%Save the figure
if saveData
    fileName = [dataFolder, '\', missionName, '_Stage_', num2str(stage),...
        '_Mean_Median_Ne_Comparison'];
    saveas(figHandle, fileName, 'png');
end

end