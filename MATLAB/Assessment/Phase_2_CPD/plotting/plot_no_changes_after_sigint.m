function plot_no_changes_after_sigint(subjects, subjectProbs,...
    attackProbStages, sigintSelectionData, missionNum, saveData,...
    showPlots, dataFolder)
%PLOT_NO_CHANGES_AFTER_SIGINT Summary of this function goes here
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
numTrials = size(subjectProbs, 2);
numLocations = size(subjectProbs{1,1}, 2) - 1;
assert(numLocations == 2);

%% Compute the frequency of subjects who made no change to P(Attack) from
%  before SIGINT to after SIGINT at the non-chosen location (location that
%  SIGINT was not obtained at) for each trial.
pTpcStage = find(strcmp('Ptpc', attackProbStages));
assert(~isempty(pTpcStage) && pTpcStage > 1);
ptStage = find(strcmp('Pt', attackProbStages));
if ~isempty(ptStage)
    prevStage = ptStage - 1;
else
    prevStage = pTpcStage - 1;
end
assert(prevStage > 0 && prevStage < pTpcStage);
%pTpcStage
%prevStage
noChangeFreq = zeros(numTrials, 1);
for subject = 1:numSubjects
    for trial = 1:numTrials
        if sigintSelectionData.selectedLocation(subject, trial) == 1
            nonSigintLocation = 2;
        else
            nonSigintLocation = 1;
        end
        if subjectProbs{subject, trial}(prevStage, nonSigintLocation) == ...
            subjectProbs{subject, trial}(pTpcStage, nonSigintLocation)
            noChangeFreq(trial) = noChangeFreq(trial) + 1;
        end
    end
end
noChangeFreq = noChangeFreq / numSubjects;

%% Create plot
missionName = ['Mission ', num2str(missionNum)];
figPosition = [60, 60, 1000, 600];
figName = [missionName, ', Percent No Change at Non-SIGINT Location   [Sample Size: ',...
    num2str(numSubjects), ']'];
figHandle = figure('name', figName, 'position', figPosition, 'NumberTitle', 'off', 'Visible', visible);

hold on;
title(figName);
xlabel('Trial');
xlim([0 numTrials+1]);
set(gca,'xtick', 1:numTrials);
ylim([0 100]);
ylabel('Percent');

 bar(1:numTrials, noChangeFreq * 100);

%Save the figure
if saveData    
    fileName = [dataFolder, '\', missionName, '_No_Change_At_Non_SIGINT_Location'];
    saveas(figHandle, fileName, 'png');
end

end