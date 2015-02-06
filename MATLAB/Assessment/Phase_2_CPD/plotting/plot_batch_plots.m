function plot_batch_plots(subjects, batchPlotCreationTrials, batchPlotSearchDepths,...    
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

%batchPlotCreationTrialsBySubject{subject} = zeros(1, numSubjects);
%batchPlotSearchDepthsBySubject{subject} = zeros(1, numTrials);
%batchPlotCreationTrials{batchPlotNum} = zeros(numSubjects, 1);
%batchPlotSearchDepths{batchPlotNum} = zeros(numSubjects, 1);

numSubjects = length(subjects);
numBatchPlots = length(batchPlotCreationTrials);

%% For each batch plot, plot the depth of search for each subject.
%  Display the average depth of search and average trial the batch plot was
%  created on in the figure title.
missionName = ['Mission ', num2str(missionNum)];
for batchPlot = 1:numBatchPlots
    %Compute the average creation trial and average depth of search for
    %each subject
    %avgCreationTrial = mean(batchPlotCreationTrials{batchPlot}(batchPlotCreationTrials{batchPlot} > 0));
    %avgSearchDepth = mean(batchPlotSearchDepths{batchPlot}(batchPlotSearchDepths{batchPlot} > 0)) * 100;
    avgSearchDepth = mean(batchPlotSearchDepths{batchPlot}) * 100;
    
    figPosition = [60, 60, 1000, 600];
    %figName = [missionName, ', Batch Plot ' num2str(batchPlot) '  [Avg Creation Trial: '...
    %    num2str(avgCreationTrial) ', Avg Search Depth: ' num2str(avgSearchDepth) ...
    %    '%, Sample Size: ', num2str(numSubjects), ']'];
    figName = [missionName, ', Batch Plot ' num2str(batchPlot)...
        '  [Avg Search Depth: ' num2str(avgSearchDepth) '%, Sample Size: ',...
        num2str(numSubjects), ']'];
    figHandle = figure('name', figName, 'position', figPosition, 'NumberTitle', 'off', 'Visible', visible);
    hold on;
    title(figName);
    
    %xlabel('Subject');
    %xlim([0 numSubjects + 1]);
    %set(gca,'xtick', 1:numSubjects);
    xlabel('Subject');
    xlim([0 numSubjects + 1]);
    xlabels = cell(1, numSubjects);
    for i = 1:numSubjects
        xlabels{i} = subjects{i};
    end    
    set(gca,'xtick', 1:numSubjects, 'xticklabel', xlabels);    
    %Orient tick marks labels vertically
    %get current tick labels
    a=get(gca,'XTickLabel');
    %erase current tick labels from figure
    set(gca,'XTickLabel',[]);
    %get tick label positions
    b=get(gca,'XTick');
    c=get(gca,'YTick');
    %make new tick labels
    rot = 70;
    if rot<180
        text(b,repmat(c(1)-.1*(c(2)-c(1)),length(b),1),a,'HorizontalAlignment','right','rotation',rot);
    else
        text(b,repmat(c(1)-.1*(c(2)-c(1)),length(b),1),a,'HorizontalAlignment','left','rotation',rot);
    end
    
    ylim([0 100]);
    ylabel('Search Depth (% Trials Reviewed)');
    
    %plot(batchPlotSearchDepths{batchPlot} * 100, '-ob');
    bar(batchPlotSearchDepths{batchPlot} * 100);
    
    %Save the figure
    if saveData
        fileName = [dataFolder, '\', missionName, '_Batch_Plot_' num2str(batchPlot)];
        saveas(figHandle, fileName, 'png');
    end
end