function plot_ne_effect_size(allSubjectProbs, subjectAbProbs, normativeProbs,...
    taskNum, saveData, showPlots, dataFolder)
%PLOT_EFFECT_SIZE Plot effect size using the negentropy distribution for 
%human and Bayesian populations.
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

useAbProbs = false;
if exist('subjectAbProbs', 'var') && ~isempty(subjectAbProbs)
    useAbProbs = true;
end

%% Compute subject and normative negentropy for all subjects on each trial stage
%% Subject negentropy is computed from probabilities generated by the A-B model with parameters fit to the pilot data.
%% Sigma is computed from the human pilot data.
numSubjects = size(allSubjectProbs, 1);
numTrials = size(allSubjectProbs, 2);
numStages = size(allSubjectProbs{1,1},1);
allSubjectNe = zeros(numSubjects, numTrials, numStages);
subjectAbNe = zeros(numSubjects, numTrials, numStages);
%subjectNeAvg = zeros(numSubjects, numTrials);
normativeNe = zeros(numSubjects, numTrials, numStages);
%normativeNeAvg = zeros(numSubjects, numTrials);
for subject = 1:numSubjects
    for trial = 1:numTrials
        allSubjectNe(subject, trial, :) = negentropy(allSubjectProbs{subject, trial});
        if useAbProbs
            subjectAbNe(subject, trial, :) = negentropy(subjectAbProbs{subject, trial});        
        end
        %subjectNeAvg(subject, trial) = sum(subjectNe(subject,trial,:))/numStages;
        
        normativeNe(subject, trial, :) = negentropy(normativeProbs{subject, trial});
       %normativeNeAvg(subject, trial) = sum(normativeNe(subject,trial,:))/numStages;
    end
end
%%%%

%% Compute the effect size using the distribution of negentropies at each trial stage
% At stage 1 (Tasks 1-4), we compute effect size as:
%  N_B (negentropy of Bayesian probs) - N_H (negentropy of human probs, either average across subjects or from A-B model) using 
% At stages 1 and above (Tasks 4-6), we compute effect size as:
%   |delta-N_B| (negentropy of difference between Bayesian probs from
%   current stage to previous stage) - |delta-N_H| (negentropy of
%   difference of human probs from one stage to the next, either average
%   over all subjects or from the A-B model)
effectSizeByTrial_Ne = zeros(numTrials,1);
effectSizeByStage_Ne = zeros(numTrials, numStages);
effectSizeAvgByTrial_Ne = zeros(numTrials, 1);
N_H = zeros(numTrials,1);
N_B = zeros(numTrials,1);
delta_N_H = zeros(numTrials, numStages);
delta_N_B = zeros(numTrials, numStages);
sigma = zeros(numTrials, numStages);
for trial = 1:numTrials
    effectSizeAvgByTrial_Ne(trial) = 0;
    for stage = 1:numStages        
        %Compute N_H or delta_N_H
        if useAbProbs   
            if stage == 1
                %Compute Ne
                N_H(trial) = sum(subjectAbNe(:, trial, stage))/length(subjectAbNe(:, trial, stage));
                subjectMean = N_H(trial);
            else
                %Compute delta Ne
                delta_N_H(trial,stage) = sum(abs(subjectAbNe(:, trial, stage)-subjectAbNe(:, trial, stage-1)))/...
                    length(subjectAbNe(:, trial, stage));
                subjectMean = delta_N_H(trial,stage);
            end            
        else
            if stage == 1
               %Compute Ne                
               N_H(trial) = sum(allSubjectNe(:, trial, stage))/length(allSubjectNe(:, trial, stage));                
               subjectMean = N_H(trial);
            else
                %Compute delta Ne
                delta_N_H(trial,stage) = sum(abs(allSubjectNe(:, trial, stage)-allSubjectNe(:, trial, stage-1)))/...
                    length(allSubjectNe(:, trial, stage));                
                subjectMean = delta_N_H(trial,stage);
            end            
        end    
        
       %Compute N_B or delta_N_B
        if stage == 1
            %Compute Ne
            N_B(trial) = sum(normativeNe(:, trial, stage))/length(normativeNe(:, trial, stage));
            normativeMean = N_B(trial);
        else
           %Compute delta Ne
           delta_N_B(trial,stage) = sum(abs(normativeNe(:, trial, stage)-normativeNe(:, trial, stage-1)))/...
                length(normativeNe(:, trial, stage));
           normativeMean = delta_N_B(trial,stage);
        end
        
        %Comptue sigma and effect size      
        sigma(trial,stage) = std(allSubjectNe(:, trial, stage));
        effectSizeByStage_Ne(trial, stage) = abs(subjectMean - normativeMean)/sigma(trial,stage);
        if stage == 1
            effectSizeByTrial_Ne(trial) = effectSizeByStage_Ne(trial, stage);
        end
    end
    effectSizeAvgByTrial_Ne(trial) = sum(effectSizeByStage_Ne(trial, :))/numStages;
end
%effectSizeAvgAllTrials_Ne = sum(effectSizeAvgByTrial_Ne)/numTrials;
%%%%

%sigma

%% Compute average subject and normative negentropy across all subjects for each trial and each trial stage
% subjectNeAvgByStage = zeros(numTrials, numStages);
% normativeNeAvgByStage = zeros(numTrials, numStages);
% subjectNeAvgByTrial = zeros(numTrials, 1);
% normativeNeAvgByTrial = zeros(numTrials, 1);
% for trial = 1:numTrials
%     for stage = 1:numStages
%         subjectNeAvgByStage(trial,stage) = sum(subjectNe(:,trial,stage))/numSubjects;
%         normativeNeAvgByStage(trial,stage) = sum(normativeNe(:,trial,stage))/numSubjects;
%     end
%     subjectNeAvgByTrial(trial) = sum(subjectNeAvgByStage(trial,:))/numStages;
%     normativeNeAvgByTrial(trial) = sum(normativeNeAvgByStage(trial,:))/numStages;
% end
%%%%

%% Create effect size plot showing delta_N_H and delta_N_B for each stage of each trial (trials are subplots in a single figure)
taskName = ['Mission ' num2str(taskNum)];
if numStages > 1;
    %maxEffectSize = max(max(effectSizeByStage));    
    numRows = ceil(numTrials / 3);
    if numRows > 4
        numRows = 4;
    end
    %trialsPerFigure = numRows * 3;
    trialsPerFigure = 10;
    if trialsPerFigure < numTrials
        figName = [taskName, ', Trials 1-', num2str(trialsPerFigure),', All Stages: Negentropy Effect Size   [Sample Size: ', num2str(numSubjects), ']'];
        fileName = [dataFolder, '\', taskName, '_Trials_1-', num2str(trialsPerFigure),'_All_Stages_Effect_Size'];
    else
        figName = [taskName, ', All Trials, All Stages: Negentropy Effect Size   [Sample Size: ', num2str(numSubjects), ']'];
        fileName = [dataFolder, '\', taskName,'_All_Trials_All_Stages_Ne_Effect_Size'];
    end
    figPositionLarge = [60, 60, 800, 800];
    figHandle = figure('name', figName, 'position', figPositionLarge, 'NumberTitle', 'off', 'Visible', visible);
    title(figName);
    subplotNum = 1;
    for trial = 1:numTrials
        if subplotNum > trialsPerFigure
            %Create a new figure
            lastTrial = trial + trialsPerFigure;
            if lastTrial > numTrials
                lastTrial = numTrials;
            end
            figName = [taskName, ', Trials ', num2str(trial), '-', num2str(lastTrial), ', All Stages: Negentropy Effect Size   [Sample Size: ', num2str(numSubjects), ']'];
            fileName = [dataFolder, '\', taskName, '_Trials_', num2str(trial), '-', num2str(lastTrial), '_All_Stages_Ne_Effect_Size'];
            figHandle = figure('name', figName, 'position', figPositionLarge, 'NumberTitle', 'off');
            title(figName);
            subplotNum = 1;
        end
        subplot(numRows, 3, subplotNum);
        set(gca, 'box', 'on');
        hold on;
        title(['Trial ' num2str(trial)]);
        xlim([1 numStages+1]);
        set(gca,'xtick', 2:numStages);
        %ymax = ylim;
        %ylim([0 maxEffectSize]);
        
        %handles = zeros(4,1);
        %Plot delta_N_H (with error bars) as solid blue line
        plot(2:numStages, delta_N_H(trial,2:numStages), '-ob');
        errorbar(2:numStages, delta_N_H(trial,2:numStages), sigma(trial,2:numStages), '-b');
        
        %Plot delta_N_B as solid black line
        plot(2:numStages, delta_N_B(trial,2:numStages), '-*k');
        
        %Plot solid red line at threshold of significance using N=100
        ne = delta_N_B(trial,2:numStages) - (0.3 * sigma(trial,2:numStages));
        plot(2:numStages, ne, '-r');
        
        %Plot dotted red line at threshold of significance using N=45
        ne = delta_N_B(trial,2:numStages) - (0.5 * sigma(trial,2:numStages));
        plot(2:numStages, ne, ':r');
        
        %legend(handles, '|delta N_H|', '|delta N_B|', 'N=100 Threshold', 'N=45 Threshold');
        
        %Plot effect size at each stage of the current trial
        %plot(1:numStages, effectSizeByStage(trial,:), '-ob');
        
        subplotNum = subplotNum + 1;
        %Save figure
        if saveData
            hgsave(fileName, '-v6');
            saveas(figHandle, fileName, 'png');
        end
    end
end
%%%%

%% Create effect size plot showing N_H and N_B at Stage 1 (Missions 1-4)
figPosition = [60, 60, 800, 600];
if numStages > 1
    figName = [taskName, ', All Trials: Negentropy Effect Size   [Sample Size: ', num2str(numSubjects), ']'];
    %figName = [taskName, ', All Trials: Effect Size (Avg For Task = ',...
    %    num2str(effectSizeAvgAllTrials_Ne), ')   ', '[Sample Size: ', num2str(numSubjects), ']'];
else
    figName = [taskName, ', All Trial Blocks: Negentropy Effect Size   [Sample Size: ', num2str(numSubjects), ']'];
end
figHandle = figure('name', figName, 'position', figPosition, 'NumberTitle', 'off', 'Visible', visible);
hold on;
title(figName);
if numStages > 1
    xlabel('Trial');
else
    xlabel('Trial Block');
end
ylabel('Negentropy');
xlim([0 numTrials+1]);
set(gca,'xtick', 1:numTrials);
%ymax = ylim;
%ylim([0 ymax(2)]);

%N_H
%sigma(:,1)

handles = zeros(4,1);

%Plot N_H (with error bars) as solid blue line
handles(1) = plot(N_H, '-ob');
errorbar(1:numTrials, N_H, sigma(:,1), '-b');

%Plot N_B as solid black line
handles(2) = plot(N_B, '-*k');

%Plot solid red line at threshold of significance using N=100
ne = N_B - (0.3 * sigma(:,1));
handles(3) = plot(ne, '-r');

%Plot dotted red line at threshold of significance using N=45
ne = N_B - (0.5 * sigma(:,1));
handles(4) = plot(ne, ':r');

legend(handles, 'N_H', 'N_B', 'N=100 Threshold', 'N=45 Threshold');

%Save figure
if saveData    
    fileName = [dataFolder, '\', taskName,'_All_Trials_Ne_Effect_Size'];    
    hgsave(fileName, '-v6');
    saveas(figHandle, fileName, 'png');
end
%%%%

end