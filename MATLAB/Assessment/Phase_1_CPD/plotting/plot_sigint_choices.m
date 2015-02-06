function plot_sigint_choices(subjectProbs, numLayersToSelect,...
    subjectLayerSelectionsByStage, bestLayerData, taskNum, saveData, showPlots, dataFolder)
%PLOT_SIGINT_CHOICES Plots SIGINT choices for Task 6 to measure confirmation bias.

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
epsilon = 0.01;

groups = ['A' 'B' 'C' 'D'];
sigintSelections = zeros(numTrials, 1);
sigintSelectionsAllSubjects = zeros(numSubjects, numTrials);
sigintBestGroupSelections = zeros(numTrials, 1); 
sigintHighestGroupSelections = zeros(numTrials, 1);
sigintHighestGroupBestGroupSelections = zeros(numTrials, 1);
sigintHighestGroupSelectionsAllSubjects = zeros(numSubjects, numTrials);
for subject = 1:numSubjects
    for trial = 1:numTrials
        for layer = 1:numLayersToSelect
            %Determine if SIGINT was selected
            layerSelection = subjectLayerSelectionsByStage{subject, trial, layer};
            if ~isempty(strfind(lower(layerSelection), 'sigint'))
                sigintSelections(trial) = sigintSelections(trial) + 1;
                sigintSelectionsAllSubjects(subject, trial) = sigintSelectionsAllSubjects(subject, trial) + 1;
                %Determine if SIGINT was selected on one of the best groups
                %bestGroupSelected = strcmp(layerSelection, bestLayerData{subject, trial, layer}.bestSigint);
                bestGroupSelected = ~isempty(strfind(bestLayerData{subject, trial, layer}.bestSigint{1}, layerSelection));
                if bestGroupSelected
                    sigintBestGroupSelections(trial) = sigintBestGroupSelections(trial) + 1;
                end
                %Determine if SIGINT was selected on one of the groups with the current highest subject probability
                maxGroupProb = 0;
                highestGroupSigint = '';
                probs = zeros(1,numGroups);
                p_subj = fillzerobins(subjectProbs{subject, trial}(layer, :), epsilon);
                for group = 1:numGroups
                    prob = p_subj(group);
                    %prob = subjectProbs{subject, trial}(layer, group);
                    probs(group) = prob;
                    if prob > maxGroupProb
                        maxGroupProb = prob;
                        %highestGroupSigint = ['SIGINT-' groups(group)];
                    end
                end
                for group = 1:numGroups
                    prob = p_subj(group);
                    %prob = subjectProbs{subject, trial}(layer, group);
                    if prob >= maxGroupProb
                        highestGroupSigint = [highestGroupSigint ' SIGINT-' groups(group)]; %#ok<AGROW>
                    end
                end
                %if strcmp(layerSelection, highestGroupSigint)
                %strfind(highestGroupSigint, layerSelection)
                if ~isempty(strfind(highestGroupSigint, layerSelection))
                    %SIGINT was selected on the group with the current
                    %highest subject probability
                    sigintHighestGroupSelections(trial) = sigintHighestGroupSelections(trial) + 1;
                    %Determine if the highest group was also the best group
                    if bestGroupSelected
                        sigintHighestGroupBestGroupSelections(trial) = sigintHighestGroupBestGroupSelections(trial) + 1;
                        sigintHighestGroupSelectionsAllSubjects(subject, trial) = sigintHighestGroupSelectionsAllSubjects(subject, trial) + 1;
                    else
                        disp(probs);    
                        disp([layerSelection, subject, trial, layer, bestLayerData{subject, trial, layer}.bestSigint])
                    end
                end
            end
        end
    end
end

%% Create plot of SIGINT choices for each trial
%Compute C
data = [sigintHighestGroupSelections ./ sigintSelections * 100,...
    sigintHighestGroupBestGroupSelections ./ sigintSelections * 100];
c = sum(sigintHighestGroupSelections) / sum(sigintSelections) * 100;
%c = sum(data(:, 1))/size(data, 1);

%Compute overall standard deviation (overall STD computed by
%computing C for each subject, and computing STD on that distribution)
e = std(sum(sigintHighestGroupSelectionsAllSubjects,2)/size(sigintHighestGroupSelectionsAllSubjects,2))*100;
%e = std(data(:, 1));

%Compute standard deviations for each trial
sigma = zeros(numTrials, 1);
for trial = 1:numTrials
    indices = sigintSelectionsAllSubjects(:, trial) > 0;
    %sum(sigintHighestGroupSelectionsAllSubjects(indices, trial) ./ sigintSelectionsAllSubjects(indices, trial) * 100)/numSigintSubjects(trial)
    sigma(trial) = std(sigintHighestGroupSelectionsAllSubjects(indices, trial) ./ sigintSelectionsAllSubjects(indices, trial) * 100);    
end

figPosition = [100, 100, 800, 600];
taskName = ['Mission ' num2str(taskNum)];
figName = [taskName, ', All Trials, SIGINT Choices, C=', num2str(c), '%, STD=', num2str(e),'% [Sample Size: ', num2str(numSubjects), ']'];
%figName = [taskName, ', All Trials, SIGINT Choices, C=', num2str(c), '%   [Sample Size: ', num2str(numSubjects), ']'];
figHandle = figure('name', figName, 'position', figPosition, 'NumberTitle', 'off', 'Visible', visible);
hold on;
title(figName);

barHandle = bar(data, 'grouped');
%errorbar(0.85:1:numTrials, data(:, 1), sigma, 'LineStyle', 'none', 'Color', 'k');

xlabel('Trial');
xlim([0 numTrials+1]);
set(gca,'xtick', 1:numTrials);

ylabel('% of SIGINT Choices');
ylim([0 110]);
set(gca,'ytick', 0:10:100);

legend(barHandle, {'% SIGINT Choices on Highest Group (C)', '% When Highest Group Also Best Group'});

%Save figure
if saveData
    fileName = [dataFolder, '\', taskName, '_All_Trials_SIGINT_Choices']; 
    saveas(figHandle, fileName, 'png');
end

end