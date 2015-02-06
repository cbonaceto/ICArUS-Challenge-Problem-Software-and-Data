function plot_negentropy_hmb(subjects, subjectNe, normativeNe, modelNe,...
    subjectNeAvg, normativeNeAvg, attackProbStages, normativeStrategyName,...
    modelName, missionNum, saveData, showPlots, dataFolder)
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

numSubjects = size(subjectNe{1}, 1);
numTrials = size(subjectNeAvg, 2);
numStages = length(attackProbStages);

%% Plot attack probability negentropies using error bar format to show variance
attackProbStageNames = cell(numStages, 1);
for stage = 1:numStages
    attackProbStageNames{stage} = get_attack_prob_stage_name(attackProbStages{stage});
end
missionName = ['Mission ', num2str(missionNum)];
showNormativeVariance = 1;
for stage = 1:numStages
    figName = [missionName, ', Stage ', num2str(stage), ', ', attackProbStageNames{stage},...
        ' Negentropy  [Sample Size: ', num2str(numSubjects), ']'];
    figPosition = [60, 60, 1000, 800];
    legendSpacerChar = ' ';
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
    ylim([0 ymax(2)]);    
    ylabel(['Human Avg. (blue), ' modelName ' (purple), ' normativeStrategyName ' (black)']);        
    
    %Plot average subject negentropies as a blue solid line
    %subjectNeAvg(stage, :)
    h(1) = plot(subjectNeAvg(stage, :), '-ob', 'LineWidth', lineWidth);
    legendText{1} = ['Human Avg.' legendSpacerChar attackProbStageNames{stage} ' Negentropy'];
    
    %Plot model negentropies as a purple solid line
    %modelNe(stage, :)
    h(2) = plot(modelNe(stage, :), '-om', 'LineWidth', lineWidth);
    legendText{2} = [modelName legendSpacerChar attackProbStageNames{stage} ' Negentropy'];
    
    %Plot normative negentropies as a black solid line
    h(3) = plot(normativeNeAvg(stage, :), '-*k', 'LineWidth', normativeLineWidth);
    legendText{3} = [normativeStrategyName legendSpacerChar attackProbStageNames{stage} ' Negentropy'];
    
    %Plot error bars that show the std for the subject probability distrubution at each trial
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
            normativeNeAvg(stage, :), normative_e, '-k'); %#ok<*UNRCH>
    end
    %Create the legend
    if(length(h) > 2)
        %legend(h, legendText);
        legend(h, legendText, 'Location', 'SouthOutside');
    end
    
    %Decrease figure margins
    tightfig;
    
    %Save the figure
    if saveData
        fileName = [dataFolder, '\', missionName, '_Stage_', num2str(stage),...
            '_Attack_Negentropies_HMB_ErrorBar'];
        saveas(figHandle, fileName, 'png');
    end
end

end