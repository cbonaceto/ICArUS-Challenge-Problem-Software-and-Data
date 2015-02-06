function plot_delta_negentropy_hmb(subjects, subjectNeAvg, normativeNeAvg, modelNeAvg,...
    subjectDeltaNe, normativeDeltaNe, stage1, stage2, modelName,...
    missionNum, saveData, showPlots, dataFolder)
%PLOT_DELTA_NEGENTROPY Plot the change in subject and normative negentropy from
%one stage to the next.

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
numTrials = size(subjectNeAvg, 2);

%% Compute average delta Ne from stage 1 to stage 2 based on the average subject
%  and normative and model negentropies.
subjectDeltaNeAvg = zeros(1, numTrials);
normativeDeltaNeAvg = zeros(1, numTrials);
modelDeltaNeAvg = zeros(1, numTrials);
for trial = 1:numTrials    
    subjectDeltaNeAvg(trial) = subjectNeAvg(stage2, trial) - subjectNeAvg(stage1, trial);
    normativeDeltaNeAvg(trial) = normativeNeAvg(stage2, trial) - normativeNeAvg(stage1, trial);
    modelDeltaNeAvg(trial) = modelNeAvg(stage2, trial) - modelNeAvg(stage1, trial);
end

%% Plot subject, normative, and model delta Ne from stage 1 to stage 2
missionName = ['Mission ' num2str(missionNum)];
showNormativeVariance = 1;
figName = [missionName, ', Delta-Ne from Stage ', num2str(stage1), ' to Stage ', num2str(stage2),...
    '  [Sample Size: ', num2str(numSubjects), ']'];
figPosition = [60, 60, 1000, 800];
lineWidth = 1.5;
normativeLineWidth = 1.25;
figHandle = figure('name', figName, 'position', figPosition, 'NumberTitle', 'off', 'Visible', visible);
h = [];
legendText = {};
hold on;
title(figName);
xlabel('Trial');
xlim([0 numTrials+1]);
set(gca,'xtick', 1:numTrials);
ymax = ylim;
ylim([-.2 ymax(2)]);
ylabel('Delta-Ne');

%Plot average subject delta Ne as a blue solid line
h(1) = plot(abs(subjectDeltaNeAvg), '-ob', 'LineWidth', lineWidth);
legendText{1} = 'Human Avg. Delta-Ne';

%Plot average model delta Ne as a purple solid line
h(2) = plot(abs(modelDeltaNeAvg), '-om', 'LineWidth', lineWidth);
legendText{2} = [modelName ' Delta-Ne'];

%Plot normative delta Ne as a black solid line
h(3) = plot(abs(normativeDeltaNeAvg), '-*k', 'LineWidth', normativeLineWidth);
legendText{3} = 'Bayesian Delta-Ne';

%Plot error bars that show the std for the subject delta Ne at each trial
subject_e = zeros(1, numTrials);
normative_e = zeros(1, numTrials);
for trial = 1:numTrials
    %subjectDeltaNe{stage}(subject, trial)
    subject_e(trial) = sem(abs(subjectDeltaNe{stage2}(:, trial)), numSubjects);
    normative_e(trial) = sem(abs(normativeDeltaNe{stage2}(:, trial)), numSubjects);
end
errorbar(1:numTrials, abs(subjectDeltaNeAvg), subject_e, '-b');
%Also show the normative delta netentropy distribution
if showNormativeVariance
    errorbar(1:numTrials, abs(normativeDeltaNeAvg), normative_e, '-k'); %#ok<*UNRCH>
end
%Create the legend
if(length(h) > 2)
    legend(h, legendText, 'Location', 'SouthOutside');
end

%Decrease figure margins
tightfig;

%Save the figure
if saveData
    fileName = [dataFolder, '\', missionName, '_Delta_Ne_From_', num2str(stage1),...
        '_To_HMB' num2str(stage2) '_ErrorBar'];
    saveas(figHandle, fileName, 'png');
end

%% Plot delta Ne separately for trials with delta Ne > 0 for average subject, normative, and model negentropies,
%  and trials with delta Ne < 0 for average subject, normative and model negentropies
for posNeg = 1:2
    if posNeg == 1
        figName = [missionName, ', Delta-Ne from Stage ', num2str(stage1), ' to Stage ', num2str(stage2),...
            ', Positive Trials  [Sample Size: ', num2str(numSubjects), ']'];
    else
        figName = [missionName, ', Delta-Ne from Stage ', num2str(stage1), ' to Stage ', num2str(stage2),...
            ', Negative Trials  [Sample Size: ', num2str(numSubjects), ']'];
    end
    figHandle = figure('name', figName, 'position', figPosition, 'NumberTitle', 'off', 'Visible', visible);
    h = zeros(1, 3);
    legendText = cell(1, 3);
    hold on;
    title(figName);
    xlabel('Trial');
    xlim([0 numTrials+1]);
    set(gca,'xtick', 1:numTrials);
    ymax = ylim;
    ylim([-.2 ymax(2)]);
    ylabel('Delta-Ne');
    
    lineWidth = 1.5;
    normativeLineWidth = 1.25;
    
    if posNeg == 1
        %Plot average subject delta Ne as a blue solid line
        deltaNePosTrials = find(subjectDeltaNeAvg > 0);
        h(1) = plot(deltaNePosTrials, abs(subjectDeltaNeAvg(deltaNePosTrials)), '-ob',...
            'LineWidth', lineWidth);
        legendText{1} = 'Human Avg. Delta-Ne Positive Trials';
        errorbar(deltaNePosTrials, abs(subjectDeltaNeAvg(deltaNePosTrials)),...
            subject_e(deltaNePosTrials), '-b');
        %errorbar(1:numTrials, abs(subjectDeltaNeAvg), subject_e, '-b');
        
        %Plot model delta Ne as a purple solid line
        deltaNePosTrials = find(modelDeltaNeAvg > 0);
        h(2) = plot(deltaNePosTrials, abs(modelDeltaNeAvg(deltaNePosTrials)), '-om',...
            'LineWidth', lineWidth);
        legendText{2} = [modelName ' Delta-Ne Positive Trials'];
        
        %Plot normative delta Ne as a black solid line
        deltaNePosTrials = find(normativeDeltaNeAvg > 0);
        h(3) = plot(deltaNePosTrials, abs(normativeDeltaNeAvg(deltaNePosTrials)),...
            '-*k', 'LineWidth', normativeLineWidth);
        legendText{3} = 'Bayesian Delta-Ne Positive Trials';
    else
        %Plot average subject delta Ne as a blue solid line
        deltaNeNegTrials = find(subjectDeltaNeAvg < 0);
        h(1) = plot(deltaNeNegTrials, abs(subjectDeltaNeAvg(deltaNeNegTrials)), '-ob',...
            'LineWidth', lineWidth);
        legendText{1} = 'Human Avg. Delta-Ne Negative Trials';
        errorbar(deltaNeNegTrials, abs(subjectDeltaNeAvg(deltaNeNegTrials)),...
            subject_e(deltaNeNegTrials), '-b');
        
        %Plot model delta Ne as a purple solid line
        deltaNeNegTrials = find(modelDeltaNeAvg < 0);
        h(2) = plot(deltaNeNegTrials, abs(modelDeltaNeAvg(deltaNeNegTrials)), '-om',...
            'LineWidth', lineWidth);
        legendText{2} = [modelName ' Delta-Ne Negative Trials'];
        
        %Plot normative delta Ne as a black solid line
        deltaNeNegTrials = find(normativeDeltaNeAvg < 0);
        h(3) = plot(deltaNeNegTrials, abs(normativeDeltaNeAvg(deltaNeNegTrials)),...
            '-*k', 'LineWidth', normativeLineWidth);
        legendText{3} = 'Bayesian Delta-Ne Negative Trials';
    end
    
    %Create the legend
    legend(h, legendText, 'Location', 'SouthOutside');
    
    %Decrease figure margins
    tightfig;
    
    %Save the figure
    if saveData
        if posNeg == 1
            fileName = [dataFolder, '\', missionName, '_Delta_Ne_From_', num2str(stage1),...
                '_To_' num2str(stage2) '_Pos_Trials_HMB'];
        else
            fileName = [dataFolder, '\', missionName, '_Delta_Ne_From_', num2str(stage1),...
                '_To_' num2str(stage2) '_Neg_Trials_HMB'];
        end
        saveas(figHandle, fileName, 'png');
    end
end

end