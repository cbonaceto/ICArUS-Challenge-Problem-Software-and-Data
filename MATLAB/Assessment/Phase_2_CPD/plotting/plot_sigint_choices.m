function plot_sigint_choices(subjects, bayesianOptimalSelections,...
    sigintSelectionFrequencies, sigintSelectionOptimalFrequencies,...
    sigintSelectionParticipantNormativeFrequencies,...
    sigintSelectionParticipantNormativeOptimalFrequencies,...
    missionNum, saveData, showPlots, dataFolder)
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

%% Plot percent of subjects who choose to get SIGINT at location 1 vs. trial number
numSubjects = length(subjects);
numTrials = size(sigintSelectionFrequencies, 1);
missionName = ['Mission ', num2str(missionNum)];
figPosition = [60, 60, 1000, 600];
numPlots = 1;
if ~isempty(sigintSelectionParticipantNormativeFrequencies)
    numPlots = 2;
end
for plotNum = 1:numPlots    
    selectionFrequencies = sigintSelectionFrequencies;
    if plotNum == 1
        %selectionFrequencies = sigintSelectionFrequencies;        
        optimalFrequencies = sigintSelectionOptimalFrequencies;
    else
        %selectionFrequencies = sigintSelectionParticipantNormativeFrequencies;
        optimalFrequencies = sigintSelectionParticipantNormativeOptimalFrequencies;        
    end
    
    %Compute the overall frequency of SIGINT selections that were optimal
    if ~isempty(optimalFrequencies)
        optimalFrequency = mean(optimalFrequencies);
    else
        optimalFrequency = [];
    end
    
    %sigintSelectionFrequencies = zeros(numTrials, numLocations);
    %sigintSelectionOptimalFrequencies = zeros(1, numTrials);
    %sigintSelectionParticipantNormativeFrequencies = zeros(numTrials, numLocations); 
    %sigintSelectionParticipantNormativeOptimalFrequencies = zeros(1, numTrials); 
    
    %Indicate the the overall frequency of SIGINT selections that were optimal in the title
    if plotNum == 1
        if ~isempty(bayesianOptimalSelections)
            figName = [missionName, ', Participant and Bayesian Optimal SIGINT Selections', '  [F: ',...
                num2str(optimalFrequency), ', Sample Size: ', num2str(numSubjects), ']'];
        else
            figName = [missionName, ', Participant and Bayesian Optimal SIGINT Selections',...
                '  [Sample Size: ', num2str(numSubjects), ']'];
        end
    else
        if ~isempty(sigintSelectionParticipantNormativeFrequencies)
            figName = [missionName, ', Participant and Participant Optimal SIGINT Selections', '   [F: ',...
                num2str(optimalFrequency), ', Sample Size: ', num2str(numSubjects), ']'];
        else
            figName = [missionName, ', Participant and Participant Optimal SIGINT Selections',...
                '  [Sample Size: ', num2str(numSubjects), ']'];
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
        if ~isempty(bayesianOptimalSelections)
            ylabel('Percent Location 1 Selected (Human Blue o, Bayesian Optimal Black *)');
        else
            ylabel('Percent Location 1 Selected');
        end
    else
        if ~isempty(sigintSelectionParticipantNormativeFrequencies)
            ylabel('Percent Location 1 Selected (Human Blue o, Human Optimal Black *)');
        else
            ylabel('Percent Location 1 Selected');
        end
    end
    
    %Plot frequency of subjects who chose location 1 on each trial
    plot(1:numTrials, selectionFrequencies(:, 1) * 100, '-ob');
    
    if plotNum == 1
        %Compute and plot the optimal SIGINT selection frequencies (given the Bayesian probabiliteis)
        %for each trial as 1 if location 1 should have been chosen and 0 if location 2 should
        %have been chosen
        if ~isempty(bayesianOptimalSelections)
            %sigintSelectionData.selectionBayesianOptimal = zeros(numSubjects, numTrials);
            optimalActions = zeros(1, numTrials);
            for trial = 1:numTrials
                if bayesianOptimalSelections(1, trial) == 1
                    optimalActions(trial) = 1;
                else
                    optimalActions(trial) = 0;
                end
            end
            %else
            %    optimalActions = [];
            %end
            plot(1:numTrials, optimalActions * 100, '-*k');
        end
    else
        %Plot the optimal SIGINT selection frequencies (given the
        %participants's  probabilities) for each trial
        if ~isempty(sigintSelectionParticipantNormativeFrequencies)
            plot(1:numTrials, sigintSelectionParticipantNormativeFrequencies(:, 1) * 100, '-*k');
        end
    end
    
    %Save the figure
    if saveData
        if plotNum == 1
            if ~isempty(bayesianOptimalSelections)
                fileName = [dataFolder, '\', missionName, '_Participant_And_Bayesian_Optimal_SIGINT_Selections'];
            else
                fileName = [dataFolder, '\', missionName, '_Participant_SIGINT_Selections'];
            end
        else
            if ~isempty(sigintSelectionParticipantNormativeFrequencies)
                fileName = [dataFolder, '\', missionName, '_Participant_And_Participant_Optimal_SIGINT_Selections'];
            else
                fileName = [dataFolder, '\', missionName, '_Participant_SIGINT_Selections'];
            end
        end
        saveas(figHandle, fileName, 'png');
    end
end

end