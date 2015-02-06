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

%% Aggregate time data for each subject on each mission
numMissions = size(timeDataByTrial, 1);
assert(numMissions > 0);
numSubjects = size(timeDataByTrial{1}, 1);
numSubjectsPerMission = zeros(1, numMissions);
assert(numSubjects > 0);

timeDataByMission = zeros(numSubjects, numMissions);
timeDataByMissionAvg = zeros(1, numMissions);
legendText = cell(1, numMissions);
for mission = 1:numMissions
    legendText{mission} = ['Mission ' num2str(tasks(mission))];
    timeData = timeDataByTrial{mission};
    numSubjectsPerMission(mission) = size(timeData,1);
    %numSubjectsPerMission(mission)
    for subject = 1:numSubjectsPerMission(mission)
        timeOnMission = sum(timeData(subject,:));
        timeDataByMission(subject, mission) = timeOnMission;        
        timeDataByMissionAvg(1, mission) = timeDataByMissionAvg(1, mission) + timeOnMission;
    end
end
timeDataByMissionAvg = timeDataByMissionAvg ./ numSubjectsPerMission;
disp(['Average Time on Missions: ', num2str(timeDataByMissionAvg ./1000 ./60)]);
%timeDataByMission

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
barHandle = bar([1:numSubjects numSubjects+2], vertcat(timeDataByMission, timeDataByMissionAvg) ./1000 ./60, 'stacked');
%Create the legend
legend(barHandle, legendText, 'Location', 'NorthEastOutside');

%Save the figure
if saveData
    fileName = [dataFolder, '\', 'Time_On_Missions'];
    saveas(fighandle, fileName, 'png');
end

end