function plot_detailed_time_metrics(subjects, examTrainingTimes, examTimes,...
    missionTimes, missionTrainingTimes, saveData, showPlots, dataFolder)
%PLOT_DETAILED_TIME_METRICS Summary of this function goes here
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

%% Aggregate time data for each subject on each mission
%examTrainingTimes = zeros(numSubjects, 1);
%examTimes = zeros(numSubjects, 1);
%missionTimes = zeros(numSubjects, numMissions);
%missionTrainingTimes = zeros(numSubjects, numMissions);

numMissions = size(missionTimes, 2);
assert(numMissions > 0);
numSubjects = size(missionTimes, 1);
assert(numSubjects > 0);

missionTimesTotal = missionTimes + missionTrainingTimes;
missionTimesTotalAvg = zeros(1, numMissions);
legendText = cell(numMissions, 1);
for mission = 1:numMissions
    legendText{mission} = ['Mission ' num2str(mission)];    
    timeData = missionTimesTotal(:, mission);
    missionTimesTotalAvg(mission) = mean(timeData(timeData > 0));
end
disp(['Average Time on Missions: ', num2str((missionTimesTotalAvg / 1000 / 60))]);

%% Plot the time each subject spent on each mission as stacked bars
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
barHandle = bar([1:numSubjects numSubjects+2],...
    [missionTimesTotal; missionTimesTotalAvg] ./1000 ./60, 'stacked');
%Create the legend
legend(barHandle, legendText, 'Location', 'NorthEastOutside');

%Save the figure
if saveData
    fileName = [dataFolder, '\', 'Time_On_Mission_Including_Training'];
    saveas(fighandle, fileName, 'png');
end

end