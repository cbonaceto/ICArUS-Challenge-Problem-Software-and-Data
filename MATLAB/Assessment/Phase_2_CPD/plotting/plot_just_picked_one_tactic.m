function plot_just_picked_one_tactic(subjects, bluebookData,...
    redTacticsData, subjectProbs, attackProbStages,...
    missionNum, saveData, showPlots, dataFolder)
%PLOT_JUST_PICKED_ONE For missions with multiple possible Red tactics, plot
%the percent of subjects who just picked one tactic or the other when
%indicating P(Attack|IMINT,OSINT).
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

selectedTactic = [];
tacticProbs = [];
if ~isempty(redTacticsData)
    if isfield(redTacticsData, 'selectedTactic')
        selectedTactic = redTacticsData.selectedTactic;
    elseif isfield(redTacticsData, 'tacticProbs')
        tacticProbs = redTacticsData.tacticProbs;
    end
end

numSubjects = length(subjects);
numTrials = size(subjectProbs, 2);
numLocations = size(subjectProbs{1,1}, 2) - 1;

ppStage = find(strcmp('Pp', attackProbStages));
assert(~isempty(ppStage) && ppStage > 0);

%% Compute the percent of subjects who "just picked one" BLUEBOOK value when reporting P(Attack|IMINT,OSINT)
% Also compute the fraction of the percent that is consitent with the
% subject's indication of the most likely Red tactic.
justPickedOneFreq = zeros(numTrials, numLocations);
if ~isempty(selectedTactic) || ~isempty(tacticProbs)
    justPickedOneMatchesTacticFreq = zeros(numTrials, numLocations);    
else
    justPickedOneMatchesTacticFreq = [];
end
for subject = 1:numSubjects
    for trial = 1:numTrials
        for location = 1:numLocations
            %Get the BLUEBOOK probs and the subject prob, and, if the subject
            %prob matches one of the BLUEBOOK probs, increment the "just picked one" frequency
            bluebookProbs = bluebookData.bluebookProbs{subject, trial}(:, location);
            subjectProb = subjectProbs{subject, trial}(ppStage, location);
            tacticIndex = find(bluebookProbs == subjectProb, 1);
            if ~isempty(tacticIndex)
                %The subject "just picked on" BLUEBOOK value
                justPickedOneFreq(trial, location) = ...
                    justPickedOneFreq(trial, location) + 1;
                
                %Determine whether the BLUEBOOK value chosen is consistent
                %with the most likely Red tactic the subject indicated
                if trial > 1 && ~isempty(justPickedOneMatchesTacticFreq)
                    if ~isempty(selectedTactic)
                       mostLikelyTactic = selectedTactic(subject, trial);
                    else
                       mostLikelyTactic = getMostLikelyTactic(tacticProbs{subject, trial});
                    end                    
                    if mostLikelyTactic == 0 || subjectProb == bluebookProbs(mostLikelyTactic) 
                        justPickedOneMatchesTacticFreq(trial, location) = ...
                            justPickedOneMatchesTacticFreq(trial, location) + 1;
                    end
                end
            end
        end
    end
end
justPickedOneFreq = justPickedOneFreq / numSubjects;
if ~isempty(justPickedOneMatchesTacticFreq)
    justPickedOneMatchesTacticFreq = justPickedOneMatchesTacticFreq / numSubjects;
end

%% Create plot showing percent of subjects who "just picked one" BLUEBOOK value on each trial
missionName = ['Mission ', num2str(missionNum)];
figPosition = [60, 60, 1000, 600];
for location = 1:numLocations
    % Compute the average and display in the figure title
    justPickedOneFreqAvg = mean(justPickedOneFreq(:, location)) * 100;
    
    if numLocations > 1
        figName = [missionName, ', Location ', num2str(location),...
            ', Percent "Just Picked One" BLUEBOOK Value   [Avg: ',...
            num2str(justPickedOneFreqAvg), '%, Sample Size: ',...
            num2str(numSubjects), ']'];
    else
        figName = [missionName, ', Percent "Just Picked One" BLUEBOOK Value   [Avg: ',...
            num2str(justPickedOneFreqAvg), '%, Sample Size: ',...
            num2str(numSubjects), ']'];
    end
    figHandle = figure('name', figName, 'position', figPosition, 'NumberTitle', 'off', 'Visible', visible);
    
    hold on;
    title(figName);
    xlabel('Trial');
    xlim([0 numTrials+1]);
    set(gca,'xtick', 1:numTrials);
    ylim([0 100]);   
    ylabel('Percent');
    
    %Plot frequency of subjects who "just picked one" BLUEBOOK value on each trial
    if ~isempty(justPickedOneMatchesTacticFreq)
        h = bar(1:numTrials, [justPickedOneFreq(:, location),...
            justPickedOneMatchesTacticFreq(:, location)] * 100);
        legend(h, '"Just Picked One" %', 'Pick Matched Subject Selection %');
    else
        bar(1:numTrials, justPickedOneFreq(:, location) * 100);
    end
    
    %Save the figure
    if saveData
        if numLocations > 1
            fileName = [dataFolder, '\', missionName, '_Location_',...
                num2str(location), '_Just_Picked_One'];
        else
            fileName = [dataFolder, '\', missionName, '_Just_Picked_One'];
        end
        saveas(figHandle, fileName, 'png');
    end
end

end

function tactic = getMostLikelyTactic(tacticProbs)
    maxProb = 0;
    tactic = 0;
    maxEntropyProb = 1 / length(tacticProbs);
    for i= 1:length(tacticProbs)
        if tacticProbs(i) ~= maxEntropyProb && tacticProbs(i) > maxProb
            tactic = i;
            maxProb = tacticProbs(i);
        end
    end    
end