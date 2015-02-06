function plot_most_likely_red_tactics_hmb(subjects, subjectMostLikelyTacticSelections,...
    modelMostLikelyTacticSelections, tacticTypes, modelName,...
    missionNum, saveData, showPlots, dataFolder)
%PLOT_MOST_LIKELY_RED_TACTICS Summary of this function goes here
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

%% Plot percent of subjects who choose each tactic type vs. trial as a bar graph
numSubjects = length(subjects);
numTrials = size(subjectMostLikelyTacticSelections, 1);
missionName = ['Mission ', num2str(missionNum)];
figPosition = [60, 60, 1000, 600];
figName = [missionName, ', Most Likely Red Tactic Selections' '  [Sample Size: ', num2str(numSubjects), ']'];
figHandle = figure('name', figName, 'position', figPosition, 'NumberTitle', 'off', 'Visible', visible);
hold on;
title(figName);
xlabel('Trial');
xlim([0 numTrials+1]);
set(gca,'xtick', 1:numTrials);
ylim([0 100]);
ylabel('Percent Selected');

data = [subjectMostLikelyTacticSelections modelMostLikelyTacticSelections];
%bar(subjectMostLikelyTacticSelections * 100);
bar(data * 100);

legendText = cell(1, length(tacticTypes) * 2);
i = 1;
for j = 1:length(tacticTypes)
    legendText{i} = ['Human ' tacticTypes{j} ' %'];
    i = i + 1;
end
for j = 1:length(tacticTypes)       
    legendText{i} = [modelName ' ' tacticTypes{j} ' %'];
    i = i + 1;
end
legend(legendText);
%legend(tacticTypes);

%Save the figure
if saveData
    fileName = [dataFolder, '\', missionName, '_Most_Likely_Red_Tactic_Selections_HM'];
    saveas(figHandle, fileName, 'png');
end

end