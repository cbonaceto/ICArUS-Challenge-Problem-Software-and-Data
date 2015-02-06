function plot_model_comparisons(subjects, subjectProbsByStage, normativeProbsByStage,...
    subjectProbsAvgByStage, normativeProbsAvgByStage,...
    subjectNe, normativeNe, subjectNeAvg, normativeNeAvg, normativeStrategyName,...
    subjectProbsName, modelData, missionNum, saveData, showPlots, dataFolder) %#ok<INUSL>
%PLOT_MODEL_COMPARISONS Plots a comparison of human attack probabilities
%at the last stage to attack probabilities computed by one or more models
%(e.g., averaging model, A-B model)

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

numSubjects = size(subjectProbsByStage{1, 1}, 1);
numTrials = size(subjectProbsByStage{1, 1}, 2);
numLocations = size(subjectProbsByStage, 2);
stage = size(subjectProbsByStage, 1); % Always the last stage (final posteriors)
numModels = size(modelData, 1);
assert(numLocations == 1 || numLocations == 2);

%% Plot Human Posterior (Avg/Dist), Bayesian Posterior, Model Posterior(s)
missionName = ['Mission ', num2str(missionNum)];
figPosition = [60, 60, 1000, 800];
colors = {'g', 'r', 'm', 'c', 'y'};
numColors = length(colors);
markers = {'^', 'v', '>', '<'};
numMarkers = length(markers);
showNormativeVariance = false;
lineWidth = 1.5;
normativeLineWidth = 1.25;
for location = 1:numLocations
    if numLocations > 1
        figName = [missionName, ', Stage ', num2str(stage), ', Location ', num2str(location),...
            ' ' subjectProbsName ' Probs Comparison   [Sample Size: ', num2str(numSubjects), ']'];
    else
        figName = [missionName, ', Stage ', num2str(stage),...
            ' ' subjectProbsName ' Probs Comparison   [Sample Size: ', num2str(numSubjects), ']'];
    end
    
    figHandle = figure('name', figName, 'position', figPosition, 'NumberTitle', 'off', 'Visible', visible);
    hold on;
    legendText = cell(2 + numModels, 1);
    h = zeros(2 + numModels, 1);
    title(figName);
    xlabel('Trial');
    xlim([0 numTrials+1]);
    set(gca,'xtick', 1:numTrials);    
    ylim([0 1]);
    ylabel('P(Attack)');
    
    %Plot average subject probabilities as a blue solid line    
    h(1) = plot(subjectProbsAvgByStage{stage, location}(:),...
        '-ob', 'LineWidth', lineWidth);
    legendText{1} = 'Human';
    
    %Plot normative probabilities as a black solid line
    h(2) = plot(normativeProbsAvgByStage{stage, location}(:),...
        '-*k', 'LineWidth', normativeLineWidth);
    legendText{2} = normativeStrategyName;
    
    %Plot model probabilities
    colorIndex = 1;
    markerIndex = 1;
    for i = 1:numModels
        m = modelData{i};
        if colorIndex > numColors
            colorIndex = 1;
        end
        if markerIndex > numMarkers
            markerIndex = 1;
        end
        modelProbsAvg = m.modelProbsAvg;
        if ~isempty(m.modelProbsAvg_subjectLikelihoods) 
            modelProbsAvg = m.modelProbsAvg_subjectLikelihoods;
        end
        h(i+2) = plot(modelProbsAvg(:, location),...
            ['-' markers{markerIndex} colors{colorIndex}],...
            'LineWidth', normativeLineWidth);
        legendText{i+2} = m.modelType;
        colorIndex = colorIndex + 1;
        markerIndex = markerIndex + 1;
    end
    
    %Plot error bars that show the std for the subject probability distrubution at each trial
    subject_e = zeros(1, numTrials);
    normative_e = zeros(1, numTrials);
    for trial = 1:numTrials
        subject_e(trial) = sem(subjectProbsByStage{stage, location}(:, trial), numSubjects);
        normative_e(trial) = sem(normativeProbsByStage{stage, location}(:, trial), numSubjects);
    end
    errorbar(1:numTrials, subjectProbsAvgByStage{stage, location}(:), subject_e, '-b');
    %Also show normative probability distribution
    if showNormativeVariance
        errorbar(1:numTrials,...
            normativeProbsAvgByStage{stage, location}(:), normative_e, '-k'); %#ok<UNRCH>
    end
    
    %Create the legend  
    legend(h, legendText, 'Location', 'SouthOutside');
    
    %Decrease figure margins
    tightfig;
    
    %Save the figure
    if saveData
        if numLocations > 1
            fileName = [dataFolder, '\', missionName, '_Stage_', num2str(stage), '_Location_',...
                num2str(location), '_Human_' subjectProbsName '_Model_Probs_Comparison'];
        else
            fileName = [dataFolder, '\', missionName, '_Stage_', num2str(stage),...
                '_Human_' subjectProbsName '_Model_Probs_Comparison'];
        end
        saveas(figHandle, fileName, 'png');
    end
end

%% Plot Human Ne (Avg/Dist), Bayesian Ne, Model Ne
figName = [missionName, ', Stage ', num2str(stage),...
    ' ' subjectProbsName ' Ne Comparison  [Sample Size: ', num2str(numSubjects), ']'];
figHandle = figure('name', figName, 'position', figPosition, 'NumberTitle', 'off', 'Visible', visible);
hold on;
legendText = cell(2 + numModels, 1);
h = zeros(2 + numModels, 1);
title(figName);
xlabel('Trial');
xlim([0 numTrials+1]);
set(gca,'xtick', 1:numTrials);
ymax = ylim;
ylim([0 ymax(2)]);
%ylim([0 1]);
ylabel('Negentropy(P(Attack), P(~Attack))');

%Plot average subject negentropy as a blue solid line
h(1) = plot(subjectNeAvg(stage, :), '-ob', 'LineWidth', lineWidth);
legendText{1} = 'Human';

%Plot normative negentropy as a black solid line
h(2) = plot(normativeNeAvg(stage, :), '-*k', 'LineWidth', normativeLineWidth);
legendText{2} = normativeStrategyName;

%Plot model negentropy
colorIndex = 1;
markerIndex = 1;
for i = 1:numModels
    m = modelData{i};
    if colorIndex > numColors
        colorIndex = 1;
    end
    if markerIndex > numMarkers
        markerIndex = 1;
    end    
    modelNeAvg = m.modelNeAvg;
    if ~isempty(m.modelNeAvg_subjectLikelihoods)         
        modelNeAvg = m.modelNeAvg_subjectLikelihoods;
    end   
    h(i+2) = plot(modelNeAvg,...
        ['-' markers{markerIndex} colors{colorIndex}],...
        'LineWidth', normativeLineWidth);
    legendText{i+2} = m.modelType;
    colorIndex = colorIndex + 1;
    markerIndex = markerIndex + 1;
end

%Plot error bars that show the std for the subject negentropy distrubution at each trial
subject_e = zeros(1, numTrials);
normative_e = zeros(1, numTrials);
for trial = 1:numTrials
    subject_e(trial) = sem(subjectNe{stage}(:, trial), numSubjects);
    normative_e(trial) = sem(normativeNe{stage}(:, trial), numSubjects);
end
errorbar(1:numTrials, subjectNeAvg(stage, :), subject_e, '-b');
%Also show normative negentropy distribution
if showNormativeVariance
    errorbar(1:numTrials,...
        normativeNeAvg(stage, :), normative_e, '-k'); %#ok<UNRCH>
end

%Create the legend
legend(h, legendText, 'Location', 'SouthOutside');

%Decrease figure margins
tightfig;

%Save the figure
if saveData
    fileName = [dataFolder, '\', missionName, '_Stage_', num2str(stage),...
        '_Human_' subjectProbsName '_Model_Ne_Comparison'];
    saveas(figHandle, fileName, 'png');
end

end