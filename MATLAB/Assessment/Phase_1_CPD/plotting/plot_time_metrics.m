function plot_time_metrics(timeDataByTrial, subjects, tasks, saveData, showPlots, dataFolder)
%PLOT_TIME_METRICS Summary of this function goes here
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

%% Aggregate time data for each subject on each task
numTasks = size(timeDataByTrial, 1);
assert(numTasks > 0);
numSubjects = size(timeDataByTrial{1}, 1);
numSubjectsPerTask = zeros(1, numTasks);
assert(numSubjects > 0);

timeDataByTask = zeros(numSubjects, numTasks);
timeDataByTaskAvg = zeros(1, numTasks);
legendText = cell(1, numTasks);
for task = 1:numTasks
    legendText{task} = ['Mission ' num2str(tasks(task))];
    timeData = timeDataByTrial{task};
    numSubjectsPerTask(task) = size(timeData,1);
    %numSubjectsPerTask(task)
    for subject = 1:numSubjectsPerTask(task)
        timeOnTask = sum(timeData(subject,:));
        timeDataByTask(subject, task) = timeOnTask;        
        timeDataByTaskAvg(1, task) = timeDataByTaskAvg(1, task) + timeOnTask;
    end
end
timeDataByTaskAvg = timeDataByTaskAvg ./ numSubjectsPerTask;
disp(['Average Time on Missions: ', num2str(timeDataByTaskAvg ./1000 ./60)]);
%timeDataByTask

%% Plot the time each subject spent on each task as stacked bars
figName = ['Time On Missions   [Sample Size: ', num2str(numSubjects), ']'];
fighandle = figure('name', figName, 'position', [60, 60, 1200, 600], 'NumberTitle', 'off', 'Visible', visible);
hold on;
title(figName);
%xlabel('Subject');
xlim([0 numSubjects+3]);
xlabels = cell(1, numSubjects+1);
for i = 1:numSubjects
    xlabels{i} = subjects{i};
end
xlabels{numSubjects + 1} = 'Avg';
set(gca,'xtick', [1:numSubjects numSubjects+2], 'xticklabel', xlabels);

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

ylabel('Time (Minutes)');
barHandle = bar([1:numSubjects numSubjects+2], vertcat(timeDataByTask, timeDataByTaskAvg) ./1000 ./60, 'stacked');
%Create the legend
legend(barHandle, legendText, 'Location', 'NorthEastOutside');

%Save the figure
if saveData
    fileName = [dataFolder, '\', 'Time_On_Missions'];
    saveas(fighandle, fileName, 'png');
end

end