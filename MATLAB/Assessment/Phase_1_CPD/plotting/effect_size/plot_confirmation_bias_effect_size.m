function plot_confirmation_bias_effect_size(subjectProbs, numLayersToSelect,...
    subjectLayerSelectionsByStage, taskNum, saveData, showPlots, dataFolder)
%PLOT_CONFIRMATION_BIAS_EFFECT_SIZE  Plot effect size using confirmation
%bias (preference) metric. Confirmation bias is computed based on the
%SIGINT selections in Mission 6.

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

%% Aggregate SIGINT choice data by trial
numSubjects = size(subjectLayerSelectionsByStage,1);
numTrials = size(subjectLayerSelectionsByStage,2);
numGroups = size(subjectProbs{1,1}, 2);
groups = ['A' 'B' 'C' 'D'];
sigintSelections = zeros(numTrials, 1);
sigintSelectionsAllSubjects = zeros(numSubjects, numTrials);
%sigintBestGroupSelections = zeros(numTrials, 1); 
sigintHighestGroupSelections = zeros(numTrials, 1);
sigintHighestGroupSelectionsAllSubjects = zeros(numSubjects, numTrials);
%sigintHighestGroupBestGroupSelections = zeros(numTrials, 1);
for subject = 1:numSubjects
    for trial = 1:numTrials
        for layer = 1:numLayersToSelect
            %Determine if SIGINT was selected
            layerSelection = subjectLayerSelectionsByStage{subject, trial, layer};
            if ~isempty(strfind(lower(layerSelection), 'sigint'))
                sigintSelections(trial) = sigintSelections(trial) + 1;
                sigintSelectionsAllSubjects(subject, trial) = sigintSelectionsAllSubjects(subject, trial) + 1;
                %Determine if SIGINT was selected on one of the groups with the current highest subject probability
                maxGroupProb = 0;
                highestGroupSigint = '';
                probs = zeros(1,numGroups);
                for group = 1:numGroups
                    prob = subjectProbs{subject, trial}(layer, group);
                    probs(group) = prob;
                    if prob > maxGroupProb
                        maxGroupProb = prob;
                    end
                end
                for group = 1:numGroups
                    prob = subjectProbs{subject, trial}(layer, group);
                    if prob >= maxGroupProb
                        highestGroupSigint = [highestGroupSigint ' SIGINT-' groups(group)]; %#ok<AGROW>
                    end
                end               
                if ~isempty(strfind(highestGroupSigint, layerSelection))
                    %SIGINT was selected on the group with the current highest subject probability
                    sigintHighestGroupSelections(trial) = sigintHighestGroupSelections(trial) + 1;                    
                    sigintHighestGroupSelectionsAllSubjects(subject, trial) = sigintHighestGroupSelectionsAllSubjects(subject, trial) + 1;
                end
            end
        end
    end
end

%% Create effect size plot showing confirmation bias
%Compute C (percent of SIGINT choices on group with highest P) for each trial
c = sigintHighestGroupSelections ./ sigintSelections * 100;

%Compute standard deviations for each trial
sigma = zeros(numTrials, 1);
numSigintSubjects = zeros(numTrials, 1);
for trial = 1:numTrials
    indices = sigintSelectionsAllSubjects(:, trial) > 0;
    numSigintSubjects(trial) = length(find(indices == 1));
    %sum(sigintHighestGroupSelectionsAllSubjects(indices, trial) ./ sigintSelectionsAllSubjects(indices, trial) * 100)/numSigintSubjects(trial)
    sigma(trial) = std(sigintHighestGroupSelectionsAllSubjects(indices, trial) ./ sigintSelectionsAllSubjects(indices, trial) * 100);    
end

%Compute average C and overall standard deviation (overall STD computed by
%computing C for each subject, and computing STD on that distribution)
cAvg = sum(c)/length(c);
c(numTrials+1) = cAvg;
e = std(sum(sigintHighestGroupSelectionsAllSubjects,2)/size(sigintHighestGroupSelectionsAllSubjects,2))*100;
%sigintHighestGroupSelections ./ sigintSelections
%eTest = std(sigintHighestGroupSelections./sigintSelections) * 100
sigma(numTrials+1) = e;
%sigma
%numSigintSubjects

figPosition = [100, 100, 800, 600];
taskName = ['Mission ' num2str(taskNum)];
figName = [taskName, ', Confirmation Preference Effect Size   [Sample Size: ', num2str(numSubjects), ']'];
figHandle = figure('name', figName, 'position', figPosition, 'NumberTitle', 'off', 'Visible', visible);
hold on;
title(figName);

xlabel('Trial');
xlim([0 numTrials+2]);
set(gca,'xtick', 1:numTrials+1);
tickLabels = cell(numTrials+1,1);
for trial=1:numTrials
    tickLabels{trial} = num2str(trial);
end
tickLabels{numTrials+1} = 'Avg';
set(gca, 'xticklabel', tickLabels);

ylabel('% of SIGINT Choices (C)');
ylim([0 110]);
set(gca,'ytick', 0:10:100);

handles = zeros(3, 1);

%Plot C for each trial
handles(1) = plot(1:numTrials+1, c, '-ob');

%Plot error bars that show the std of the C distrubution at each trial
errorbar(1:numTrials+1, c, sigma, '-b');

%Plot solid black line at C=50%
threshold = 50;
handles(2) = plot(1:numTrials+1, repmat(threshold, numTrials+1, 1), '-k');

%Plot solid red line at threshold of significance using N=100
%0.5 + 0.3 * sigma
handles(3) = plot(threshold + 0.3 * sigma, '-r');

%Plot dotted red line at threshold of significance using N=45
%0.5 + 0.5 * sigma
handles(4) = plot(threshold + 0.5 * sigma, ':r');

legend(handles, 'C', 'C=50%', 'N=100 Threshold', 'N=45 Threshold');

%Save figure
if saveData
    fileName = [dataFolder, '\', taskName, '_Confirmation_Preference_Effect_Size'];   
    hgsave(fileName, '-v6');
    saveas(figHandle, fileName, 'png');
end

end