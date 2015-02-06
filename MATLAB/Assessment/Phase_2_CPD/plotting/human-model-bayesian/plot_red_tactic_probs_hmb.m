function plot_red_tactic_probs_hmb(subjects, subjectRedTacticProbs,...
    subjectRedTacticProbsAvg, subjectRedTacticNe, subjectRedTacticNeAvg,...
    modelRedTacticProbs, modelRedTacticNe,...
    normativeRedTacticProbsAvg, normativeRedTacticNeAvg,...
    actualRedTactics, redTacticTypes, normativeStrategyName,...
    modelName, missionNum, saveData, showPlots, dataFolder)
%PLOT_RED_TACTIC_PROBS Summary of this function goes here
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

%Create vector of probabilities for each trial with a 1 when the actual Red
%tactic is tactic 1, and a 0 otherwise
numTrials = size(actualRedTactics, 2);
groundTruthProbs = actualRedTactics(1, :) == 1;

%Find the trial on which the tactics change (a vertical line will be drawn
%at this trial)
changeTrial = 0;
lastGroundTruthProb = groundTruthProbs(1);
trial = 2;
while changeTrial == 0 && trial <= numTrials;
    if lastGroundTruthProb ~= groundTruthProbs(trial)
        changeTrial = trial;
    else
        lastGroundTruthProb = groundTruthProbs(trial);
        trial = trial + 1;
    end    
end

%Get the probability of Red tactic 1 for each subject
numSubjects = size(subjectRedTacticProbs, 1);
subjectRedTactic1Probs = zeros(numSubjects, numTrials);
for subject = 1:numSubjects
    for trial = 1:numTrials
        subjectRedTactic1Probs(subject, trial) = subjectRedTacticProbs{subject, trial}(1);
    end
end

%% Plot P(Red tactic 1) and Negentropy of P(each Red tactic) vs. trial number
%  using the error bar format to show variance.
missionName = ['Mission ', num2str(missionNum)];
for plotType = 1:2
    if plotType == 1
        %Plot probabilities
        dataDist = subjectRedTactic1Probs;
        dataAvg = subjectRedTacticProbsAvg;
        modelData = modelRedTacticProbs;
        normativeDataAvg = normativeRedTacticProbsAvg;
        figName = [missionName, ', Red Tactic Probabilities' '  [Sample Size: ', num2str(numSubjects), ']'];
        xLabelStr = 'Trial';
        yLabelStr = ['P(' redTacticTypes{1} '), Human Avg. (blue), Ground Truth (green)'];
    else
        %Plot negentropies
        dataDist = subjectRedTacticNe;
        dataAvg = subjectRedTacticNeAvg;
        modelData = modelRedTacticNe;
        normativeDataAvg = normativeRedTacticNeAvg;
        figName = [missionName, ', Red Tactic Negentropy' '  [Sample Size: ', num2str(numSubjects), ']'];
        xLabelStr = 'Trial (Line at Change Trial)';
        yLabelStr = ['Ne(P(' redTacticTypes{1} '), P(' redTacticTypes{2} ')), Human Avg. (blue), Ground Truth (green)'];
    end
    
    %Error bar format
    figPosition = [60, 60, 1000, 800];
    lineWidth = 1.5;
    normativeLineWidth = 1.25;
    
    figHandle = figure('name', figName, 'position', figPosition, 'NumberTitle', 'off', 'Visible', visible);
    h = [];
    legendText = {};
    hold on;
    title(figName);
    xlabel(xLabelStr);
    xlim([0 numTrials+1]);
    set(gca,'xtick', 1:numTrials);
    ymax = ylim;
    ylim([0 ymax(2)]);
    ylabel(yLabelStr);
    
    %Plot average subject probabilities or negentropies as a blue solid line
    h(1) = plot(dataAvg(:, 1), '-ob', 'LineWidth', lineWidth);
    legendText{1} = 'Human Avg.';
    
    %Plot model probabilities or negentropies as a purple solid line
    h(2) = plot(modelData(:, 1), '-om', 'LineWidth', lineWidth);
    legendText{2} = modelName;
    
    %Plot normative probabilities or negentropies as a black solid line
    if ~isempty(normativeDataAvg)
        h(3) = plot(normativeDataAvg(:, 1), '-*k', 'LineWidth', normativeLineWidth);
        legendText{3} = normativeStrategyName;
        hIndex = 4;
    else
        hIndex = 3;
    end
    
    if plotType == 1
        %Plot ground truth probabilities as a black solid line
        h(hIndex) = plot(groundTruthProbs, '-xg', 'LineWidth', normativeLineWidth);
        legendText{hIndex} = 'Ground Truth'; %#ok<*AGROW>
    else
        %Draw a vertical line at the change trial
        if changeTrial > 0
            h(hIndex) = line([changeTrial, changeTrial], ylim, 'LineStyle', ':', 'Color', [0.2 0.2 0.2]);
            legendText{hIndex} = 'Change Trial';
        end
    end
    
    %Plot error bars that show the std for the subject probability distrubution at each trial
    %redTactic1Probs = zeros(numSubjects, numTrials);
    subject_e = zeros(1, numTrials);
    for trial = 1:numTrials
        subject_e(trial) = sem(dataDist(:, trial), numSubjects);
    end
    errorbar(1:numTrials, dataAvg(:, 1), subject_e, '-b');
    %Create the legend
    if length(h) > 2
        legend(h, legendText, 'Location', 'SouthOutside');
    end
    
    %Decrease figure margins
    tightfig;
    
    %Save the figure
    if saveData
        if plotType == 1
            fileName = [dataFolder, '\', missionName, '_Red_Tactic_Probabilities_HMB_ErrorBar'];
        else
            fileName = [dataFolder, '\', missionName, '_Red_Tactic_Ne_HMB_ErrorBar'];
        end
        saveas(figHandle, fileName, 'png');
    end
end

end