function wfs = plot_probability_metrics_webfigure(subjects, subjectProbs, normativeProbs, taskNum, saveData, showPlots)
%PLOT_PROBABILITY_METRICS Summary of this function goes here
%   Detailed explanation goes here

if ~exist('saveData', 'var')
    saveData = false;
end
if ~exist('showPlots', 'var') || showPlots
    visible = 'on';
else 
    visible = 'off';
end



%% Compute subject and normative negentropy for all subjects on each trial stage
numSubjects = size(subjectProbs, 1);
numTrials = size(subjectProbs, 2);
numStages = size(subjectProbs{1,1},1);
subjectNe = zeros(numSubjects, numTrials, numStages);
subjectNeAvg = zeros(numSubjects, numTrials);
normativeNe = zeros(numSubjects, numTrials, numStages);
normativeNeAvg = zeros(numSubjects, numTrials);
for subject = 1:numSubjects
    for trial = 1:numTrials
        %taskNum, subject, trial
        subjectNe(subject, trial, :) = negentropy(subjectProbs{subject, trial});
        subjectNeAvg(subject, trial) = sum(subjectNe(subject,trial,:))/numStages;
        
        normativeNe(subject, trial, :) = negentropy(normativeProbs{subject, trial});
        normativeNeAvg(subject, trial) = sum(normativeNe(subject,trial,:))/numStages;
    end
end
%%%%

%% Compute average subject and normative negentropy across all subjects for each trial and each trial stage
subjectNeAvgByStage = zeros(numTrials, numStages);
normativeNeAvgByStage = zeros(numTrials, numStages);
subjectNeAvgByTrial = zeros(numTrials, 1);
normativeNeAvgByTrial = zeros(numTrials, 1);
for trial = 1:numTrials
    for stage = 1:numStages
        subjectNeAvgByStage(trial,stage) = sum(subjectNe(:,trial,stage))/numSubjects;
        normativeNeAvgByStage(trial,stage) = sum(normativeNe(:,trial,stage))/numSubjects;
    end
    subjectNeAvgByTrial(trial) = sum(subjectNeAvgByStage(trial,:))/numStages;
    normativeNeAvgByTrial(trial) = sum(normativeNeAvgByStage(trial,:))/numStages;
end
%%%%

%% webfigure
% saveData = false;
% visible = 'off';
if taskNum < 4
    wfs = cell(2,1);
else
    wfs = cell((2*numStages+numTrials+3),1);
end

%% Create Negentropy plot for each trial using the scatter-plot format to show variance
figPosition = [60, 60, 1000, 600];
taskName = ['Mission ' num2str(taskNum)];
showNormativeVariance = false;
normativeXOffset = 0;
if taskNum == 6
    showNormativeVariance = true;
    normativeXOffset = .2;
end
if taskNum > 3
    figName = [taskName, ', Average For All Stages: Negentropy (N)   [Sample Size: ', num2str(numSubjects), ']'];
else
    figName = [taskName, ', All Trial Blocks: Negentropy (N)   [Sample Size: ', num2str(numSubjects), ']'];
end
figHandle = figure('name', figName, 'position', figPosition, 'NumberTitle', 'off', 'Visible', visible);
hold on;
title(figName);
if taskNum > 3
    xlabel('Trial');
else
    xlabel('Trial Block');
end
xlim([0 numTrials+1]);
set(gca,'xtick', 1:numTrials);
ylabel('Human (blue), Bayesian (black)');
ymax = ylim;
ylim([0 ymax(2)]);
%Plot average subject negentropy as a solid line
plot(subjectNeAvgByTrial, '-ob', 'LineWidth', 2);
%Plot normative negentropy as a dotted line
plot((1+normativeXOffset):(numTrials+normativeXOffset), normativeNeAvgByTrial, '-*k', 'LineWidth', 1.5);
%Create scatter plot that shows subject negentropy distribution at each
%trial for each subject
colormap(lines(numSubjects));
colors = colormap;
numColors = size(colors, 1);
markers = {'x', 's', 'd', '^', 'v', '>', '<', 'h'};
numMarkers = length(markers);
colorIndex = 1;
markerIndex = 1;
h = zeros(numSubjects, 1);
for subject = 1:numSubjects
    if colorIndex > numColors
        colorIndex = 1;
    end
    if markerIndex > numMarkers
        markerIndex = 1;
    end
    h(subject) = plot(1:numTrials, subjectNeAvg(subject, :), ['-' markers{markerIndex}], 'Color', colors(colorIndex,:));
    %Also show normative negentropy distribution
    if showNormativeVariance
        plot((1+normativeXOffset):(numTrials+normativeXOffset), normativeNeAvg(subject, :), '*k');
    end
    colorIndex = colorIndex + 1;
    markerIndex = markerIndex + 1;
end
%Create legend for scatter plots
legend(h, subjects, 'Location', 'NorthEastOutside');
%Save figure
if saveData
    if taskNum > 3
        fileName = [taskName,'_AllStages_Negentropy_Scatter'];        
    else
        fileName = [taskName,'_Negentropy_Scatter'];        
    end
    hgsave(fileName, '-v6');
    saveas(figHandle, fileName, 'png');
end

wf_trial_scatter = webfigure(figHandle);
wfs{1} = wf_trial_scatter;
%%%%

%% Create Negentropy plot for each trial using the error-bar format to show variance %%
figPosition = [60, 60, 800, 600];
if taskNum > 3
    figName = [taskName, ', Average For All Stages: Negentropy (N)   [Sample Size: ', num2str(numSubjects), ']'];
else
    figName = [taskName, ', All Trial Blocks: Negentropy (N)   [Sample Size: ', num2str(numSubjects), ']'];
end
figHandle = figure('name', figName, 'position', figPosition, 'NumberTitle', 'off', 'Visible', visible);
hold on;
title(figName);
if taskNum > 3
    xlabel('Trial');
else
    xlabel('Trial Block');
end
xlim([0 numTrials+1]);
set(gca,'xtick', 1:numTrials);
ylabel('Human (blue), Bayesian (black)');
ymax = ylim;
ylim([0 ymax(2)]);
%Plot average subject negentropy as a blue solid line
plot(subjectNeAvgByTrial, '-ob');
%Plot normative negentropy as a black solid line
plot((1+normativeXOffset):(numTrials+normativeXOffset), normativeNeAvgByTrial, '-*k');
%Plot error bars that show the std for the subject negentropy distrubution
%at each trial
subject_e = zeros(1, numTrials);
normative_e = zeros(1, numTrials);
for trial = 1:numTrials
    subject_e(trial) = std(subjectNeAvg(:, trial));
    normative_e(trial) = std(normativeNeAvg(:, trial));
    %errorbar(trial, subjectNeAvgByTrial(trial), e, '-k');
end
errorbar(1:numTrials, subjectNeAvgByTrial(:), subject_e, '-b');
%Also show normative negentropy distribution
if showNormativeVariance
    errorbar((1+normativeXOffset):(numTrials+normativeXOffset), normativeNeAvgByTrial(:), normative_e, '-k');
end
plot(0:1, 0:1, '-r');
%Save figure
if saveData
    if taskNum > 3
        fileName = [taskName,'_AllStages_Negentropy_ErrorBar'];
    else
        fileName = [taskName,'_Negentropy_ErrorBar'];
    end
    hgsave(fileName, '-v6');
    saveas(figHandle, fileName, 'png');
end

wf_trial_bar = webfigure(figHandle);
wfs{2} = wf_trial_bar;
%%%%

global index;
index = 2;
%% Create Negentropy plot for each trial stage using the scatter-plot format to show variance %%
if taskNum > 3
    figPosition = [60, 60, 1000, 600];
    for stage = 1:numStages
        figName = [taskName, ', Stage ', num2str(stage), ': Negentropy (N)   [Sample Size: ', num2str(numSubjects), ']'];
        figHandle = figure('name', figName, 'position', figPosition, 'NumberTitle', 'off', 'Visible', visible);
        hold on;
        title(figName);
        xlabel('Trial');
        xlim([0 numTrials+1]);
        set(gca,'xtick', 1:numTrials);
        ymax = ylim;
        ylim([0 ymax(2)]);
        ylabel('Human (blue), Bayesian (black)');
        %Plot average subject negentropy as a blue solid line
        plot(subjectNeAvgByStage(:,stage), '-ob', 'LineWidth', 2);
        %Plot normative negentropy as a black solid line
        plot((1+normativeXOffset):(numTrials+normativeXOffset), normativeNeAvgByStage(:,stage), '-*k', 'LineWidth', 1.5);
        %Create scatter plot that shows subject negentropy distribution at each
        %stage
        colormap(lines(numSubjects));
        colors = colormap;
        numColors = size(colors, 1);
        markers = {'x', 's', 'd', '^', 'v', '>', '<', 'h'};
        numMarkers = length(markers);
        colorIndex = 1;
        markerIndex = 1;
        h = zeros(numSubjects, 1);
        for subject = 1:numSubjects
            if colorIndex > numColors
                colorIndex = 1;
            end
            if markerIndex > numMarkers
                markerIndex = 1;
            end
            h(subject) = plot(1:numTrials, subjectNe(subject, :, stage), ['-' markers{markerIndex}], 'Color', colors(colorIndex,:));
            %Also show normative negentropy distribution
            if showNormativeVariance
                plot((1+normativeXOffset):(numTrials+normativeXOffset), normativeNe(subject, :, stage), '*k');
            end
            colorIndex = colorIndex + 1;
            markerIndex = markerIndex + 1;
        end
        %Create legend for scatter plots
        legend(h, subjects, 'Location', 'NorthEastOutside');
        %Save figure
        if saveData
            fileName = [taskName,'_Stage_',num2str(stage),'_Negentropy_Scatter'];
            hgsave(fileName, '-v6');
            saveas(figHandle, fileName, 'png');
        end
        % webfigure
        wf_trial_stage_scatter = webfigure(figHandle);
        index = index + 1;
        wfs{index} = wf_trial_stage_scatter;
    end
end
%%%%
% index = index + numStages;
% index = 7;

%% Create Negentropy plot for each trial stage using the error-bar format to show variance %%
if taskNum > 3
    figPosition = [60, 60, 800, 600];
    for stage = 1:numStages
        figName = [taskName, ', Stage ', num2str(stage), ': Negentropy (N)   [Sample Size: ', num2str(numSubjects), ']'];
        figHandle = figure('name', figName, 'position', figPosition, 'NumberTitle', 'off', 'Visible', visible);
        hold on;
        title(figName);
        xlabel('Trial');
        xlim([0 numTrials+1]);
        set(gca,'xtick', 1:numTrials);
        ylabel('Human (blue), Bayesian (black)');
        ymax = ylim;
        ylim([0 ymax(2)]);
        %Plot average subject negentropy as a blue solid line
        plot(subjectNeAvgByStage(:,stage), '-ob');
        %Plot normative negentropy as a black solid line
        plot((1+normativeXOffset):(numTrials+normativeXOffset), normativeNeAvgByStage(:,stage), '-*k');
        %Plot error bars that show the std for the subject negentropy
        %distrubution at each trial
        subject_e = zeros(1, numTrials);
        normative_e = zeros(1, numTrials);
        for trial = 1:numTrials
            subject_e(trial) = std(subjectNe(:, trial, stage));
            normative_e(trial) = std(normativeNe(:, trial, stage));
            %errorbar(trial, subjectNeAvgByStage(trial,stage), e, '-k');
        end
        errorbar(1:numTrials, subjectNeAvgByStage(:,stage), subject_e, '-b');
        %Also show normative negentropy distribution
        if showNormativeVariance
            errorbar((1+normativeXOffset):(numTrials+normativeXOffset), normativeNeAvgByStage(:,stage), normative_e, '-k');
        end
        %Save figure
        if saveData
            fileName = [taskName,'_Stage_',num2str(stage),'_Negentropy_ErrorBar'];
            hgsave(fileName, '-v6');
            saveas(figHandle, fileName, 'png');
        end
        
        % webfigure
        wf_trial_stage_bar = webfigure(figHandle);
        index = index + 1;
        wfs{index} = wf_trial_stage_bar;
    end
end
%%%%
% index = index + numStages;
% index = 12;

%% Create Negentropy plot showing Negnetropy for all stages in each trial (trials are separate figures)
if taskNum > 3
    for trial = 1:numTrials
        figName = [taskName, ', Trial ', num2str(trial), ', All Stages: Negentropy (N)   [Sample Size: ', num2str(numSubjects), ']'];
        figHandle = figure('name', figName, 'position', figPosition, 'NumberTitle', 'off', 'Visible', visible);
        hold on;
        title(figName);
        xlabel('Stage');
        xlim([0 numStages+1]);
        set(gca,'xtick', 1:numStages);
        ylabel('Human (blue), Bayesian (black)');
        ymax = ylim;
        ylim([0 ymax(2)]);
        %Plot subject and normative negentroy at each stage of the current
        %trial
        plot(1:numStages, subjectNeAvgByStage(trial,:), '-ob');
        plot((1+normativeXOffset):(numStages+normativeXOffset), normativeNeAvgByStage(trial,:), '-*k');
        %Plot error bars that show the std for the subject negentropy at each
        %stage
        subject_e = zeros(1, numStages);
        normative_e = zeros(1, numStages);
        for stage = 1:numStages
            subject_e(stage) = std(subjectNe(:, trial, stage));
            normative_e(stage) = std(normativeNe(:, trial, stage));
            %errorbar(stage, subjectNeAvgByStage(trial,stage), e, '-k');
        end
        errorbar(1:numStages, subjectNeAvgByStage(trial,:), subject_e, '-b');
        %Also show normative negentropy distribution
        if showNormativeVariance
            errorbar((1+normativeXOffset):(numStages+normativeXOffset), normativeNeAvgByStage(trial,:), normative_e, '-k');
        end
        %Save figure
        if saveData
            fileName = [taskName, '_Trial_', num2str(trial), '_All_Stages_Negentropy_ErrorBar'];
            hgsave(fileName, '-v6');
            saveas(figHandle, fileName, 'png');
        end
        
        % webfigure
        wf_stages_trial = webfigure(figHandle);
        index = index + 1;
        wfs{index} = wf_stages_trial;
    end
end
%%%%
% index = index + numTrials;
% index = 22;

%% Create Negentropy plot showing Negnetropy for all stages in each trial (trials are subplots in a single figure)
if taskNum > 3
   numRows = ceil(numTrials / 3);
    if numRows > 4
        numRows = 4;
    end
    %trialsPerFigure = numRows * 3;
    trialsPerFigure = 10;
    if trialsPerFigure < numTrials
        figName = [taskName, ', Trials 1-', num2str(trialsPerFigure),', All Stages: Negentropy (N)   [Sample Size: ', num2str(numSubjects), ']'];
        fileName = [taskName, '_Trials_1-', num2str(trialsPerFigure),'_All_Stages_Negentropy_ErrorBar'];
    else
        figName = [taskName, ', All Trials, All Stages: Negentropy (N)   [Sample Size: ', num2str(numSubjects), ']'];
        fileName = [taskName,'_All_Trials_All_Stages_Negentropy_ErrorBar'];
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
            figName = [taskName, ', Trials ', num2str(trial), '-', num2str(lastTrial), ', All Stages: Negentropy (N)   [Sample Size: ', num2str(numSubjects), ']'];
            fileName = [taskName, '_Trials_', num2str(trial), '-', num2str(lastTrial), '_All_Stages_Negentropy_ErrorBar'];
            figHandle = figure('name', figName, 'position', figPositionLarge, 'NumberTitle', 'off');
            title(figName);
            subplotNum = 1;
        end
        subplot(numRows, 3, subplotNum);
        set(gca, 'box', 'on');
        hold on;
        title(['Trial ' num2str(trial)]);
        xlim([0 numStages+1]);
        set(gca,'xtick', 1:numStages);
        ymax = ylim;
        ylim([0 ymax(2)]);
        %Plot subject and normative negentroy at each stage of the current
        %trial
        plot(1:numStages, subjectNeAvgByStage(trial,:), '-ob');
        plot((1+normativeXOffset):(numStages+normativeXOffset), normativeNeAvgByStage(trial,:), '-*k');
        %Plot error bars that show the std for the subject negentropy at each
        %stage
        subject_e = zeros(1, numStages);
        normative_e = zeros(1, numStages);
        for stage = 1:numStages
            subject_e(stage) = std(subjectNe(:, trial, stage));
            normative_e(stage) = std(normativeNe(:, trial, stage));
            %errorbar(stage, subjectNeAvgByStage(trial,stage), e, '-k');
        end
        errorbar(1:numStages, subjectNeAvgByStage(trial,:), subject_e, '-b');
        %Also show normative negentropy distribution
        if showNormativeVariance
            errorbar((1+normativeXOffset):(numStages+normativeXOffset), normativeNeAvgByStage(trial,:), normative_e, '-k');
        end
        subplotNum = subplotNum + 1;
        %Save figure
        if saveData
            hgsave(fileName, '-v6');
            saveas(figHandle, fileName, 'png');
        end
    end
    
    % webfigure
    wf_stages_all = webfigure(figHandle);
    index = index + 1;
    wfs{index} = wf_stages_all;
end
%%%%

end