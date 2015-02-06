function plot_batch_plots_hmb(subjects,subjectBatchPlotSearchDepthsAllTrials,...
    modelBatchPlotSearchDepthsAllTrials, modelName,...
    missionNum, saveData, showPlots, dataFolder)
%PLOT_BATCH_PLOTS Summary of this function goes here
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

numSubjects = length(subjects);
numBatchPlots = length(subjectBatchPlotSearchDepthsAllTrials);

%% Compute the average search depth for each batch plot for subjects and the model
subjectAvgSearchDepths = zeros(numBatchPlots, 1);
modelAvgSearchDepths = zeros(numBatchPlots, 1);
for batchPlot = 1:numBatchPlots
    subjectAvgSearchDepths(batchPlot) = mean(subjectBatchPlotSearchDepthsAllTrials{batchPlot}) * 100;
    modelAvgSearchDepths(batchPlot) = mean(modelBatchPlotSearchDepthsAllTrials{batchPlot}) * 100;
end

%% For each batch plot, plot the avg. depth of search for humans, and the model depth of search.
missionName = ['Mission ', num2str(missionNum)];
%for batchPlot = 1:numBatchPlots
%Compute the average creation trial and average depth of search for
%each subject
avgSearchDepth = mean(subjectAvgSearchDepths);
modelAvgSearchDepth = mean(modelAvgSearchDepths);

figPosition = [60, 60, 1000, 600];
figName = [missionName, ', Batch Plot Search Depths, Sh: ' num2str(avgSearchDepth)...
    '%, Sm: ', num2str(modelAvgSearchDepth), '%, Sample Size: ', num2str(numSubjects), ']'];
figHandle = figure('name', figName, 'position', figPosition, 'NumberTitle', 'off', 'Visible', visible);
hold on;
title(figName);

xlim([0 numBatchPlots + 1])
set(gca, 'xtick', 1:1:numBatchPlots);
xlabel('Batch Plot Number');

ylim([0 100]);
ylabel('Search Depth (% Trials Reviewed)');

%[subjectAvgSearchDepths modelAvgSearchDepths]
h = bar([subjectAvgSearchDepths modelAvgSearchDepths]);
legend(h, {'Humans', modelName});

%Save the figure
if saveData
    fileName = [dataFolder, '\', missionName, '_Batch_Plot_Search_Depths_HMB'];
    saveas(figHandle, fileName, 'png');
end

end