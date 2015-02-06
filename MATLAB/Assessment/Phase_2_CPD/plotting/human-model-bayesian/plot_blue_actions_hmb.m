function plot_blue_actions_hmb(subjects, subjectNotDivertFrequency,...
    subjectBlueActionFrequencies, subjectBlueActionParticipantNormativeFrequencies,...
    subjectBlueActionParticipantNormativeOptimalFrequencies,...
    modelBlueActionFrequencies, modelBlueActionParticipantNormativeFrequencies,...
    modelBlueActionParticipantNormativeOptimalFrequencies,...
    modelName, missionNum, saveData, showPlots, dataFolder)
%PLOT_BLUE_ACTIONS Summary of this function goes here
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

%% Plot percent of subjects who choose divert vs. trial number
numSubjects = length(subjects);
numTrials = size(subjectBlueActionFrequencies{1}, 1);
numLocations = length(subjectBlueActionFrequencies);
missionName = ['Mission ', num2str(missionNum)];
figPosition = [60, 60, 1000, 600];
for location = 1:numLocations
    actionFrequencies = subjectBlueActionFrequencies{location};
    modelActionFrequencies = modelBlueActionFrequencies{location};
    if ~isempty(subjectBlueActionParticipantNormativeOptimalFrequencies)
        optimalFrequencies = subjectBlueActionParticipantNormativeOptimalFrequencies(location, :);
        modelOptimalFrequencies = modelBlueActionParticipantNormativeOptimalFrequencies(location, :);
    else
        optimalFrequencies = [];
        modelOptimalFrequencies = [];
    end
    
    %Compute the overall frequency  of choices that were optimal
    if ~isempty(optimalFrequencies)
        optimalFrequency = mean(optimalFrequencies);
        modelOptimalFrequency = mean(modelOptimalFrequencies);
    else
        optimalFrequency = [];
        modelOptimalFrequency = [];
    end
    
    %Indicate the overall not divert frequency and the overall frequency
    %of choices that were optimal in the title
    if ~isempty(subjectBlueActionParticipantNormativeFrequencies)
        if numLocations > 1
            figName = [missionName, ', Participant and Participant Optimal Choices, Location ', num2str(location),...
                '  [Avg Not Divert Frequency: ', num2str(subjectNotDivertFrequency), ', Nh: ',...
                num2str(optimalFrequency), ', Nm: ', num2str(modelOptimalFrequency),...
                ', Sample Size: ', num2str(numSubjects), ']'];
        else
            figName = [missionName, ', Participant and Participant Optimal Choices',...
                '  [Avg Not Divert Frequency: ', num2str(subjectNotDivertFrequency), ', Nh: ',...
                num2str(optimalFrequency), ', Nm: ', num2str(modelOptimalFrequency),......
                ', Sample Size: ', num2str(numSubjects), ']'];
        end
    else
        if numLocations > 1
            figName = [missionName, ', Participant Choices, Location ', num2str(location),...
                '  [Avg Not Divert Freqency: ', num2str(subjectNotDivertFrequency),...
                ', Sample Size: ', num2str(numSubjects), ']'];
        else
            figName = [missionName, ', Participant Choices',...
                '  [Avg Not Divert Freqency: ', num2str(subjectNotDivertFrequency),...
                ', Sample Size: ', num2str(numSubjects), ']'];
        end
    end
    figHandle = figure('name', figName, 'position', figPosition, 'NumberTitle', 'off', 'Visible', visible);
    hold on;
    title(figName);
    xlabel('Trial');
    xlim([0 numTrials+1]);
    set(gca,'xtick', 1:numTrials);
    ylim([0 101]);        
    ylabel('Percent Not Divert Chosen');
    
    if ~isempty(subjectBlueActionParticipantNormativeFrequencies)
        legendText = cell(1, 4);
    else
        legendText = cell(1, 2);
    end
    
    %Plot frequency of subjects who chose not divert on each trial
    plot(1:numTrials, actionFrequencies(:, 2) * 100, '-ob');
    legendText{1} = 'Human Overall';
    
    %Plot frequency of model choices that were not divert on each trial
    plot(1:numTrials, modelActionFrequencies(:, 2) * 100, '-om');
    legendText{2} = [modelName ' Overall'];
    
    %Plot the optimal choices (given the participants'
    %probabilities) for each trial
    if ~isempty(subjectBlueActionParticipantNormativeFrequencies)
        plot(1:numTrials, subjectBlueActionParticipantNormativeFrequencies{location}(:, 2) * 100, '-*k');
        legendText{3} = 'Human Optimal'; 
        plot(1:numTrials, modelBlueActionParticipantNormativeFrequencies{location}(:, 2) * 100, '-*r');
        legendText{4} = [modelName ' Optimal']; 
    end
    
    legend(legendText, 'Location', 'SouthOutside');
    
    %Save the figure
    if saveData
        if numLocations > 1
            fileName = [dataFolder, '\', missionName, '_Participant_Optimal_Choices_Location_',...
                num2str(location) '_HMB'];
        else
            fileName = [dataFolder, '\', missionName, '_Participant_Optimal_Choices_HMB'];
        end
        saveas(figHandle, fileName, 'png');
    end
end

end