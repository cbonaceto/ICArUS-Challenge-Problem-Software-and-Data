function plot_sigint_choices_hmb(subjects, subjectSigintSelectionFrequencies,...
    subjectSigintSelectionParticipantNormativeFrequencies,...
    subjectSigintSelectionParticipantNormativeOptimalFrequencies,...
    modelSigintSelectionFrequencies,...
    modelSigintSelectionParticipantNormativeFrequencies,...
    modelSigintSelectionParticipantNormativeOptimalFrequencies,...
    modelName, missionNum, saveData, showPlots, dataFolder)
%PLOT_SIGINT_CHOICES Summary of this function goes here
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

%% Plot percent of subjects who chose to get SIGINT at location 1 vs. trial number
numSubjects = length(subjects);
numTrials = size(subjectSigintSelectionFrequencies, 1);
missionName = ['Mission ', num2str(missionNum)];
figPosition = [60, 60, 1000, 600];

selectionFrequencies = subjectSigintSelectionFrequencies;
optimalFrequencies = subjectSigintSelectionParticipantNormativeOptimalFrequencies;
modelSelectionFrequencies = modelSigintSelectionFrequencies;
modelOptimalFrequencies = modelSigintSelectionParticipantNormativeOptimalFrequencies;

%Compute the overall frequency of SIGINT selections that were optimal
if ~isempty(optimalFrequencies)
    optimalFrequency = mean(optimalFrequencies);
    modelOptimalFrequency = mean(modelOptimalFrequencies);
else
    optimalFrequency = [];
    modelOptimalFrequency = [];
end

%subjectSigintSelectionFrequencies = zeros(numTrials, numLocations);
%sigintSelectionOptimalFrequencies = zeros(1, numTrials);
%subjectSigintSelectionParticipantNormativeFrequencies = zeros(numTrials, numLocations);
%subjectSigintSelectionParticipantNormativeOptimalFrequencies = zeros(1, numTrials);

%Indicate the overall frequency of SIGINT selections that were optimal in the title
if ~isempty(optimalFrequency)
    figName = [missionName, ', Participant and Participant Optimal SIGINT Selections', '   [Fh: ',...
        num2str(optimalFrequency), ', Fm: ', num2str(modelOptimalFrequency), ', Sample Size: ',...
        num2str(numSubjects), ']'];
else
    figName = [missionName, ', Participant and Participant Optimal SIGINT Selections',...
        '  [Sample Size: ', num2str(numSubjects), ']'];
end

figHandle = figure('name', figName, 'position', figPosition, 'NumberTitle', 'off', 'Visible', visible);
hold on;
title(figName);
xlabel('Trial');
xlim([0 numTrials+1]);
set(gca,'xtick', 1:numTrials);
ylim([0 101]);
ylabel('Percent Location 1 Selected');

if ~isempty(subjectSigintSelectionParticipantNormativeFrequencies)    
    legendText = cell(1, 4);
else    
    legendText = cell(1, 2);
end

%Plot frequency of subjects who chose location 1 on each trial
plot(1:numTrials, selectionFrequencies(:, 1) * 100, '-ob');
legendText{1} = 'Human Overall';

%Plot frequency with which model chose location 1 on each trial
plot(1:numTrials, modelSelectionFrequencies(:, 1) * 100, '-om');
legendText{2} = [modelName ' Overall'];

%Plot the optimal SIGINT selection frequencies (given the
%participants's  probabilities and the model's probabilities) for each trial
if ~isempty(subjectSigintSelectionParticipantNormativeFrequencies)
    plot(1:numTrials, subjectSigintSelectionParticipantNormativeFrequencies(:, 1) * 100,...
        '-*k');
    legendText{3} = 'Human Optimal'; 
    plot(1:numTrials, modelSigintSelectionParticipantNormativeFrequencies(:, 1) * 100,...
        '-*r');
    legendText{4} = [modelName ' Optimal']; 
end

legend(legendText, 'Location', 'SouthOutside');

%Save the figure
if saveData
    if ~isempty(subjectSigintSelectionParticipantNormativeFrequencies)
        fileName = [dataFolder, '\', missionName, '_Participant_And_Participant_Optimal_SIGINT_Selections_HMB'];
    else
        fileName = [dataFolder, '\', missionName, '_Participant_SIGINT_Selections_HMB'];
    end
    saveas(figHandle, fileName, 'png');
end

end