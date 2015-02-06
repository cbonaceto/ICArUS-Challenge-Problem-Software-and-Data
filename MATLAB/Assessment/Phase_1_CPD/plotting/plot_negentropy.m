function plot_negentropy(subjects, subjectProbs, normativeProbs, taskNum,...
    saveData, showPlots, dataFolder)
%PLOT_NEGENTROPY Creates negentropy plots. Average negentropy is
%computed by computing the negentropy at each trial/stage and averaging it.
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

%% Compute subject and normative negentropy for all subjects on each trial stage
numSubjects = size(subjectProbs, 1);
numTrials = size(subjectProbs, 2);
numStages = size(subjectProbs{1,1},1);
numProbs = size(subjectProbs{1,1},2);
epsilon = 0.01;

subjectNe = zeros(numSubjects, numTrials, numStages);
subjectNeAvg = zeros(numSubjects, numTrials);
normativeNe = zeros(numSubjects, numTrials, numStages);
normativeNeAvg = zeros(numSubjects, numTrials);

subjectProbsAvg = cell(numTrials, 1);
normativeProbsAvg = cell(numTrials, 1);
for trial = 1:numTrials
    subjectProbsAvg{trial} = zeros(numStages, numProbs);
    normativeProbsAvg{trial} = zeros(numStages, numProbs);
end

for subject = 1:numSubjects
    for trial = 1:numTrials
        for stage = 1:numStages           
            subjectNe(subject, trial, stage) = ...
                negentropy(fillzerobins(subjectProbs{subject, trial}(stage, :), epsilon));
            normativeNe(subject, trial, stage) = ...
                negentropy(fillzerobins(normativeProbs{subject, trial}(stage, :), epsilon));
        end     
        subjectNeAvg(subject, trial) = sum(subjectNe(subject,trial,:))/numStages;
        subjectProbsAvg{trial} = subjectProbsAvg{trial} + subjectProbs{subject, trial};        
      
        normativeNeAvg(subject, trial) = sum(normativeNe(subject,trial,:))/numStages;
        normativeProbsAvg{trial} = normativeProbsAvg{trial} + normativeProbs{subject, trial};
    end
end
%%%%

%% Compute average subject and normative negentropy across all subjects for each trial and each trial stage
subjectNeAvgByStage = zeros(numTrials, numStages); %Avg. subject Ne at each trial/stage computed by averaging subject Ne
normativeNeAvgByStage = zeros(numTrials, numStages); %Avg. normative Ne at each trial/stage
subjectNeAvgByTrial = zeros(numTrials, 1); %Avg. subject Ne at each trial computed by averaging the avg. subject Ne at each stage
normativeNeAvgByTrial = zeros(numTrials, 1); %Avg. normative Ne at each trial

subjectNeByStage = zeros(numTrials, numStages); %Avg subject Ne at each trial/stage computed using the avg. subject probs at the trial/stage
subjectNeByTrial = zeros(numTrials, 1); %Avg. subject Ne at each trial computed using the avg. subject probs for the trial
subjectNeErrByStage = zeros(numTrials, numStages); %STD of subject negentropy by stage computed using avg. subject probs
subjectNeErrByTrial = zeros(numTrials, 1); %STD of subject negentropy by trial computed using avg. subject probs
normativeNeByStage = zeros(numTrials, numStages); %Avg. normative Ne at each trial/stage computed using the avg. normative probs at the trial/stage
normativeNeByTrial = zeros(numTrials, 1); %Avg. normative Ne at each trial computed using the avg. normative probs for the trial
for trial = 1:numTrials    
    subjectProbsAvg{trial} = subjectProbsAvg{trial}/numSubjects;
    normativeProbsAvg{trial} = normativeProbsAvg{trial}/numSubjects;
    for stage = 1:numStages
        subjectProbsAvg{trial}(stage, :) = fillzerobins(subjectProbsAvg{trial}(stage, :), epsilon);
        normativeProbsAvg{trial}(stage, :) = fillzerobins(normativeProbsAvg{trial}(stage, :), epsilon);
        subjectNeAvgByStage(trial, stage) = sum(subjectNe(:, trial, stage))/numSubjects;
        normativeNeAvgByStage(trial, stage) = sum(normativeNe(:, trial, stage))/numSubjects;
    end
    subjectNeAvgByTrial(trial) = sum(subjectNeAvgByStage(trial, :))/numStages;
    normativeNeAvgByTrial(trial) = sum(normativeNeAvgByStage(trial, :))/numStages;    
    
    subjectNeByStage(trial, :) = negentropy(subjectProbsAvg{trial});
    normativeNeByStage(trial, :) = negentropy(normativeProbsAvg{trial});
    if numStages == 1
        subjectNeByTrial(trial) = negentropy(subjectProbsAvg{trial});
        normativeNeByTrial(trial) = negentropy(normativeProbsAvg{trial});
    else
        subjectNeByTrial(trial) = negentropy(sum(subjectProbsAvg{trial})/numStages);
        normativeNeByTrial(trial) = negentropy(sum(normativeProbsAvg{trial})/numStages);
    end
    
    for stage = 1:numStages
        subjectNeErrByStage(trial, stage) = ...
            sqrt(sum((subjectNe(:, trial, stage) - subjectNeByStage(trial, stage)).^2 )/(numSubjects-1));
    end
    subjectNeErrByTrial(trial) = ...
        sqrt(sum((subjectNeAvg(:, trial) - subjectNeByTrial(trial)).^2 )/(numSubjects-1));
end
%%%%

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
%subplot(1, 3, 1);
hold on;
title(figName);
if taskNum > 3
    xlabel('Trial');
else
    xlabel('Trial Block');
end
xlim([0 numTrials+1]);
set(gca,'xtick', 1:numTrials);
ylabel('Human Avg. (blue), Bayesian (black)');
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
if numSubjects > 20
    %Divide legend into two groups    
    legend(h, subjects, 'Location', 'NorthEastOutside');
    %subplot(1, 3, 2);
    %legend(h(1:10), subjects(1:10), 'Location', 'NorthEastOutside');
    %subplot(1, 3, 3);
    %legend(h(2:20), subjects(2:20), 'Location', 'NorthWestOutside');
else
    legend(h, subjects, 'Location', 'NorthEastOutside');
end
%Save figure
if saveData
    if taskNum > 3
        fileName = [dataFolder, '\', taskName,'_AllStages_Negentropy_Scatter'];        
    else
        fileName = [dataFolder, '\', taskName,'_Negentropy_Scatter'];        
    end    
    saveas(figHandle, fileName, 'png');
end
%%%%

%% Create Negentropy plot for each trial using the error-bar format to show variance %%
for i=1:2    
    figPosition = [60, 60, 800, 600];
    if i == 1
        typeFigName = 'Computed from N distribution';
        typeFileName = 'N_Distribution';
    else
        typeFigName = 'Computed from avg. probabilities';
        typeFileName = 'Avg_Probs';
    end
    if taskNum > 3
        figName = [taskName, ', Average For All Stages: Negentropy (N) (', typeFigName, ')   [Sample Size: ', num2str(numSubjects), ']'];
    else
        figName = [taskName, ', All Trial Blocks: Negentropy (N) (', typeFigName, ')   [Sample Size: ', num2str(numSubjects), ']'];
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
    
    if i==1
        %Plot average subject negentropy as a blue solid line
        plot(subjectNeAvgByTrial, '-ob');
        %Plot normative negentropy as a black solid line
        plot((1+normativeXOffset):(numTrials+normativeXOffset), normativeNeAvgByTrial, '-*k');
        
        %Plot error bars that show the std for the subject negentropy distrubution at each trial
        subject_e = zeros(1, numTrials);
        normative_e = zeros(1, numTrials);
        for trial = 1:numTrials
            subject_e(trial) = std(subjectNeAvg(:, trial));
            normative_e(trial) = std(normativeNeAvg(:, trial));
        end
        errorbar(1:numTrials, subjectNeAvgByTrial(:), subject_e, '-b');
        
        %Also show normative negentropy distribution
        if showNormativeVariance
            errorbar((1+normativeXOffset):(numTrials+normativeXOffset), normativeNeAvgByTrial(:), normative_e, '-k');
        end
    else
        %Plot average subject negentropy as a blue solid line
        plot(subjectNeByTrial, '-ob');
        %Plot normative negentropy as a black solid line
        plot((1+normativeXOffset):(numTrials+normativeXOffset), normativeNeByTrial, '-*k');
        
        %Plot error bars that show the std for the subject negentropy distrubution at each trial
        errorbar(1:numTrials, subjectNeByTrial(:), subjectNeErrByTrial(:), '-b');
    end
    
    %Save figure
    if saveData
        if taskNum > 3
            fileName = [dataFolder, '\', taskName,'_AllStages_Negentropy_', typeFileName, '_ErrorBar'];
        else
            fileName = [dataFolder, '\', taskName,'_Negentropy_', typeFileName, '_ErrorBar'];
        end
        saveas(figHandle, fileName, 'png');
    end
end
%%%%

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
        ylabel('Human Avg. (blue), Bayesian (black)');
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
            fileName = [dataFolder, '\', taskName,'_Stage_',num2str(stage),'_Negentropy_Scatter'];           
            saveas(figHandle, fileName, 'png');
        end
    end
end
%%%%

%% Create Negentropy plot for each trial stage using the error-bar format to show variance %%
if taskNum > 3
    for i = 1:2
        figPosition = [60, 60, 800, 600];
        if i == 1
            typeFigName = 'Computed from N distribution';
            typeFileName = 'N_Distribution';
        else
            typeFigName = 'Computed from avg. probabilities';
            typeFileName = 'Avg_Probs';
        end
        for stage = 1:numStages
            figName = [taskName, ', Stage ', num2str(stage), ': Negentropy (N) (', typeFigName, ')   [Sample Size: ', num2str(numSubjects), ']'];
            figHandle = figure('name', figName, 'position', figPosition, 'NumberTitle', 'off', 'Visible', visible);
            hold on;
            title(figName);
            xlabel('Trial');
            xlim([0 numTrials+1]);
            set(gca,'xtick', 1:numTrials);
            ylabel('Human (blue), Bayesian (black)');
            ymax = ylim;
            ylim([0 ymax(2)]);
            
            if i==1
                %Plot average subject negentropy as a blue solid line
                plot(subjectNeAvgByStage(:,stage), '-ob');
                %Plot normative negentropy as a black solid line
                plot((1+normativeXOffset):(numTrials+normativeXOffset), normativeNeAvgByStage(:,stage), '-*k');
                
                %Plot error bars that show the std for the subject negentropy distrubution at each trial
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
            else
                %Plot average subject negentropy as a blue solid line
                plot(subjectNeByStage(:, stage), '-ob');
                %Plot normative negentropy as a black solid line
                plot((1+normativeXOffset):(numTrials+normativeXOffset), normativeNeByStage(:, stage), '-*k');
                
                %Plot error bars that show the std for the subject negentropy distrubution at each trial
                errorbar(1:numTrials, subjectNeByStage(:, stage), subjectNeErrByStage(:, stage), '-b');                
            end
            
            %Save figure
            if saveData
                fileName = [dataFolder, '\', taskName,'_Stage_',num2str(stage),'_Negentropy_', typeFileName, '_ErrorBar'];             
                saveas(figHandle, fileName, 'png');
            end
        end
    end
end
%%%%

%% Create Negentropy plot showing Negnetropy for all stages in each trial (trials are separate figures)
plotTrialsAsSeparateFigures = false;
if taskNum > 3 && plotTrialsAsSeparateFigures
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
            fileName = [dataFolder, '\', taskName, '_Trial_', num2str(trial), '_All_Stages_Negentropy_ErrorBar'];         
            saveas(figHandle, fileName, 'png');
        end
    end
end
%%%%

%% Create Negentropy plot showing Negnetropy for all stages in each trial (trials are subplots in a single figure)
plotTrialsInSingleFigure = true;
if taskNum > 3 && plotTrialsInSingleFigure
    for i=1:2
        numRows = ceil(numTrials / 3);
        if numRows > 4
            numRows = 4;
        end
        trialsPerFigure = 10;
        if i == 1
            typeFigName = 'Computed from N distribution';
            typeFileName = 'N_Distribution';
        else
            typeFigName = 'Computed from avg. probabilities';
            typeFileName = 'Avg_Probs';
        end
        if trialsPerFigure < numTrials            
            figName = [taskName, ', Trials 1-', num2str(trialsPerFigure),', All Stages: Negentropy (N) (', typeFigName, ')  [Sample Size: ', num2str(numSubjects), ']'];            
            fileName = [dataFolder, '\', taskName, '_Trials_1-', num2str(trialsPerFigure), '_All_Stages_Negentropy_', typeFileName, '_ErrorBar'];            
        else
            figName = [taskName, ', All Trials, All Stages: Negentropy (N) (', typeFigName, ')  [Sample Size: ', num2str(numSubjects), ']'];
            fileName = [dataFolder, '\', taskName,'_All_Trials_All_Stages_Negentropy_', typeFileName, '_ErrorBar'];
        end
        figPositionLarge = [60, 60, 800, 800];
        figHandle = figure('name', figName, 'position', figPositionLarge, 'NumberTitle', 'off', 'Visible', visible);
        title(figName);
        subplotNum = 1;
        for trial = 1:numTrials
            if subplotNum > trialsPerFigure
                %Save current figure
                if saveData
                    saveas(figHandle, fileName, 'png');
                end
                
                %Create a new figure
                lastTrial = trial + trialsPerFigure;
                if lastTrial > numTrials
                    lastTrial = numTrials;
                end
                figName = [taskName, ', Trials ', num2str(trial), '-', num2str(lastTrial), ', All Stages: Negentropy (N)   [Sample Size: ', num2str(numSubjects), ']'];
                fileName = [dataFolder, '\', taskName, '_Trials_', num2str(trial), '-', num2str(lastTrial), '_All_Stages_Negentropy_ErrorBar'];
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
            
            if i == 1
                %Plot subject and normative negentroy at each stage of the current trial
                plot(1:numStages, subjectNeAvgByStage(trial,:), '-ob');
                plot((1+normativeXOffset):(numStages+normativeXOffset), normativeNeAvgByStage(trial,:), '-*k');
                
                %Plot error bars that show the std for the subject negentropy at each stage
                subject_e = zeros(1, numStages);
                normative_e = zeros(1, numStages);
                for stage = 1:numStages
                    subject_e(stage) = std(subjectNe(:, trial, stage));
                    normative_e(stage) = std(normativeNe(:, trial, stage));                    
                end
                errorbar(1:numStages, subjectNeAvgByStage(trial,:), subject_e, '-b');
                
                %Also show normative negentropy distribution
                if showNormativeVariance
                    errorbar((1+normativeXOffset):(numStages+normativeXOffset), normativeNeAvgByStage(trial,:), normative_e, '-k');
                end
            else
                %Plot subject and normative negentroy at each stage of the current%trial
                plot(1:numStages, subjectNeByStage(trial,:), '-ob');
                plot((1+normativeXOffset):(numStages+normativeXOffset), normativeNeByStage(trial,:), '-*k');
                
                %Plot error bars that show the std for the subject negentropy at each stage
                errorbar(1:numStages, subjectNeByStage(trial, :), subjectNeErrByStage(trial, :), '-b');                
            end
            
            subplotNum = subplotNum + 1;            
        end
        
        %Save figure
        if saveData
            saveas(figHandle, fileName, 'png');
        end
    end
end
%%%%

end