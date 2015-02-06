function plot_blue_actions(subjects, normativeBayesianActions,...
    notDivertFrequency, blueActionFrequencies, blueActionOptimalFrequencies,...
    blueActionParticipantNormativeFrequencies, blueActionParticipantNormativeOptimalFrequencies,...
    missionNum, saveData, showPlots, dataFolder)
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
numTrials = size(blueActionFrequencies{1}, 1);
numLocations = length(blueActionFrequencies);
missionName = ['Mission ', num2str(missionNum)];
figPosition = [60, 60, 1000, 600];
numPlots = 1;
if ~isempty(blueActionParticipantNormativeFrequencies)
    numPlots = 2;
end
for plotNum = 1:numPlots
    for location = 1:numLocations
        actionFrequencies = blueActionFrequencies{location};
        if plotNum == 1            
            %actionFrequencies = blueActionFrequencies{location};
            if ~isempty(blueActionOptimalFrequencies)
                optimalFrequencies = blueActionOptimalFrequencies(location, :);                
            else
                optimalFrequencies = [];
            end
        else
            %actionFrequencies = blueActionParticipantNormativeFrequencies{location};
            if ~isempty(blueActionParticipantNormativeOptimalFrequencies)
                optimalFrequencies = blueActionParticipantNormativeOptimalFrequencies(location, :);                
            else
                optimalFrequencies = [];
            end
        end
        
        %Compute the overall frequency  of choices that were optimal
        if ~isempty(optimalFrequencies)
            optimalFrequency = mean(optimalFrequencies);
        else
            optimalFrequency = [];
        end        
        
        %Indicate the overall not divert frequency and the overall frequency 
        %of choices that were optimal in the title        
        if plotNum == 1
           if ~isempty(normativeBayesianActions) && ~isempty(normativeBayesianActions{1, 1})
                if numLocations > 1
                    figName = [missionName, ', Participant and Bayesian Optimal Choices, Location ', num2str(location),...
                        '  [Avg Not Divert Frequency: ', num2str(notDivertFrequency), ', N: ',...
                        num2str(optimalFrequency), ', Sample Size: ', num2str(numSubjects), ']'];
                else
                    figName = [missionName, ', Participant and Bayesian Optimal Choices',...
                        '  [Avg Not Divert Frequency: ', num2str(notDivertFrequency), ', N: ',...
                        num2str(optimalFrequency), ', Sample Size: ', num2str(numSubjects), ']'];
                end
            else
                if numLocations > 1
                    figName = [missionName, ', Participant Choices, Location ', num2str(location),...
                        '  [Avg Not Divert Freqency: ', num2str(notDivertFrequency),...
                        ', Sample Size: ', num2str(numSubjects), ']'];
                else
                    figName = [missionName, ', Participant Choices',...
                        '  [Avg Not Divert Freqency: ', num2str(notDivertFrequency),...
                        ', Sample Size: ', num2str(numSubjects), ']'];
                end
            end
        else
           if ~isempty(blueActionParticipantNormativeFrequencies)
                if numLocations > 1
                    figName = [missionName, ', Participant and Participant Optimal Choices, Location ', num2str(location),...
                        '  [Avg Not Divert Frequency: ', num2str(notDivertFrequency), ', N: ',...
                        num2str(optimalFrequency), ', Sample Size: ', num2str(numSubjects), ']'];
                else
                    figName = [missionName, ', Participant and Participant Optimal Choices',...
                        '  [Avg Not Divert Frequency: ', num2str(notDivertFrequency), ', N: ',...
                        num2str(optimalFrequency), ', Sample Size: ', num2str(numSubjects), ']'];
                end
            else
                if numLocations > 1
                    figName = [missionName, ', Participant Choices, Location ', num2str(location),...
                        '  [Avg Not Divert Freqency: ', num2str(notDivertFrequency),...
                        ', Sample Size: ', num2str(numSubjects), ']'];
                else
                    figName = [missionName, ', Participant Choices',...
                        '  [Avg Not Divert Freqency: ', num2str(notDivertFrequency),...
                        ', Sample Size: ', num2str(numSubjects), ']'];
                end
            end
        end
        figHandle = figure('name', figName, 'position', figPosition, 'NumberTitle', 'off', 'Visible', visible);
        hold on;
        title(figName);
        xlabel('Trial');
        xlim([0 numTrials+1]);
        set(gca,'xtick', 1:numTrials);
        ylim([0 101]);
        if plotNum == 1
            if ~isempty(normativeBayesianActions) && ~isempty(normativeBayesianActions{1, 1})
                ylabel('Percent Not Divert Chosen (Human Blue o, Bayesian Optimal Black *)');
            else
                ylabel('Percent Not Divert Chosen');
            end
        else
            if ~isempty(blueActionParticipantNormativeFrequencies)
                ylabel('Percent Not Divert Chosen (Human Blue o, Human Optimal Black *)');
            else
                ylabel('Percent Not Divert Chosen');
            end
        end
        
        %Plot frequency of subjects who chose not divert on each trial
        plot(1:numTrials, actionFrequencies(:, 2) * 100, '-ob');
        
        %Plot optimal choice frequencies if available
        %if ~isempty(optimalActions)
        %    plot(1:numTrials, optimalActions * 100, '-*k');
        %end     
        
        if plotNum == 1
            %Compute and plot the optimal choices (given Bayesian probabilities)
            %for each trial as 1 if not divert should have been chosen and 0 if divert should
            %have been chosen
            if ~isempty(normativeBayesianActions) && ~isempty(normativeBayesianActions{1, 1})
                optimalActions = zeros(1, numTrials);
                for trial = 1:numTrials
                    if normativeBayesianActions{1, trial}(location) == 2
                        optimalActions(trial) = 1;
                    else
                        optimalActions(trial) = 0;
                    end
                end
                plot(1:numTrials, optimalActions * 100, '-*k');
                %else
                %    optimalActions = [];
            end
        else
            %Plot the optimal choices (given the participants'
            %probabilities) for each trial
            if ~isempty(blueActionParticipantNormativeFrequencies)
                plot(1:numTrials, blueActionParticipantNormativeFrequencies{location}(:, 2) * 100, '-*k');
            end
        end
        
        %Save the figure
        if saveData
            if plotNum == 1
                if numLocations > 1
                    fileName = [dataFolder, '\', missionName, '_Participant_Choices_Location_',...
                        num2str(location)];
                else
                    fileName = [dataFolder, '\', missionName, '_Participant_Choices'];
                end
            else
                if numLocations > 1
                    fileName = [dataFolder, '\', missionName, '_Participant_Optimal_Choices_Location_',...
                        num2str(location)];
                else
                    fileName = [dataFolder, '\', missionName, '_Participant_Optimal_Choices'];
                end
            end
            saveas(figHandle, fileName, 'png');
        end
    end
end

end