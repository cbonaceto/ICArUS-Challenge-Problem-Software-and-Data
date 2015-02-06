function plot_delta_negentropy(subjectProbs, normativeProbs, taskNum,...
    saveData, showPlots, dataFolder)
%PLOT_DELTA_NEGENTROPY Plot the change in subject and normative negentropy.
%Subject negentropy is calculated based on the average subject probs.
%from one stage to the next

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

%% Compute subject negentropy and average subject and normative probs for all subjects on each trial stage
numSubjects = size(subjectProbs, 1);
numTrials = size(subjectProbs, 2);
numStages = size(subjectProbs{1,1},1);
numProbs = size(subjectProbs{1,1},2);
epsilon = 0.01;

if numStages <= 1
    return;
end

subjectNe = cell(numTrials, numStages); %Subject negentropy for each subject, trial, and stage
normativeNe = cell(numTrials, numStages); %Normative negentropy for each subject, trial, and stage
subjectProbsAvg = cell(numTrials, 1);
normativeProbsAvg = cell(numTrials, 1);

for trial = 1:numTrials
    subjectProbsAvg{trial} = zeros(numStages, numProbs);
    normativeProbsAvg{trial} = zeros(numStages, numProbs);
    for stage = 1:numStages
        subjectNe{trial, stage} = zeros(numSubjects, 1);
        normativeNe{trial, stage} = zeros(numSubjects, 1);
        for subject = 1:numSubjects
            subjectNe{trial, stage}(subject) = ... 
                negentropy(fillzerobins(subjectProbs{subject, trial}(stage, :), epsilon));            
             normativeNe{trial, stage}(subject) = ... 
                negentropy(fillzerobins(normativeProbs{subject, trial}(stage, :), epsilon));             
        end        
    end
end

for subject = 1:numSubjects
    for trial = 1:numTrials
        subjectProbsAvg{trial} = subjectProbsAvg{trial} + subjectProbs{subject, trial};
        normativeProbsAvg{trial} = normativeProbsAvg{trial} + normativeProbs{subject, trial};
    end
end
%%%%

%% Compute average subject and normative negentropy across all subjects for each trial and each trial stage based on the average subject and normative probs
subjectNeByStage = zeros(numTrials, numStages); %Avg subject Ne at each trial/stage computed using the avg. subject probs at the trial/stage
normativeNeByStage = zeros(numTrials, numStages); %Avg. normative Ne at each trial/stage

subjectDeltaNe = cell(numTrials, numStages); %Delta Negentropy of subject probabilities for each stage
subjectDeltaNeAvg = zeros(numTrials, numStages); %Delta negentropy of average subject probabilities for each stage
subjectDeltaNeErr = zeros(numTrials, numStages); %STD of delta subject negentropy
normativeDeltaNe = cell(numTrials, numStages); %Delta Negentropy of normative probabilities for each stage
normativeDeltaNeAvg = zeros(numTrials, numStages); %Delta negentropy of average normative probabilities for each stage
normativeDeltaNeErr = zeros(numTrials, numStages); %STD of delta normative negentropy (should be 0 for all Tasks except Task 6)
for trial = 1:numTrials    
    %Compute delta subject and normative negentropy based on average subject and normative probs    
    subjectProbsAvg{trial} = subjectProbsAvg{trial}/numSubjects;
    normativeProbsAvg{trial} = normativeProbsAvg{trial}/numSubjects;
    for stage = 1:numStages
        subjectProbsAvg{trial}(stage, :) = fillzerobins(subjectProbsAvg{trial}(stage, :), epsilon);
        normativeProbsAvg{trial}(stage, :) = fillzerobins(normativeProbsAvg{trial}(stage, :), epsilon);
    end
    subjectNeByStage(trial, :) = negentropy(subjectProbsAvg{trial});
    normativeNeByStage(trial, :) = negentropy(normativeProbsAvg{trial});    
    
    for stage = 2:numStages    
        %Compute delta subject and normative negentropy based on the subject and normative Ne distributions
        subjectDeltaNe{trial, stage} = subjectNe{trial, stage} - subjectNe{trial, stage-1};
        normativeDeltaNe{trial, stage} = normativeNe{trial, stage} - normativeNe{trial, stage-1};
        
        %Compute delta negentropy based on the average subject and normative probs
        subjectDeltaNeAvg(trial, stage) = subjectNeByStage(trial, stage) - subjectNeByStage(trial, stage-1);                
        subjectDeltaNeErr(trial, stage) = ...
            sqrt(sum((subjectDeltaNe{trial, stage} - subjectDeltaNeAvg(trial, stage)).^2 )/(numSubjects-1));
        normativeDeltaNeAvg(trial, stage) = normativeNeByStage(trial, stage) - normativeNeByStage(trial, stage-1);
        normativeDeltaNeErr(trial, stage) = ...
            sqrt(sum((normativeDeltaNe{trial, stage} - normativeDeltaNeAvg(trial, stage)).^2 )/(numSubjects-1));
    end
end
minNe = min([min(min(subjectDeltaNeAvg)) min(min(normativeDeltaNeAvg))]);
maxNe = max([max(max(subjectDeltaNeAvg)) max(max(normativeDeltaNeAvg))]);
%%%%

%% Create Delta Negentropy plot showing Negnetropy for all stages in each trial (trials are subplots in a single figure)
figPositionLarge = [60, 60, 800, 800];
taskName = ['Mission ' num2str(taskNum)];
typeFigName = 'Computed from avg. probabilities';
typeFileName = 'Avg_Probs';
numRows = ceil(numTrials / 3);
showNormativeVariance = false;
normativeXOffset = 0;
if taskNum == 6
    showNormativeVariance = true;
    normativeXOffset = .2;
end
if numRows > 4
    numRows = 4;
end
trialsPerFigure = 10;
if trialsPerFigure < numTrials
    figName = [taskName, ', Trials 1-', num2str(trialsPerFigure),', All Stages: Delta Negentropy (', typeFigName, ')  [Sample Size: ', num2str(numSubjects), ']'];
    fileName = [dataFolder, '\', taskName, '_Trials_1-', num2str(trialsPerFigure), '_All_Stages_Delta_Negentropy_', typeFileName, '_ErrorBar'];
else
    figName = [taskName, ', All Trials, All Stages: Delta Negentropy (', typeFigName, ')  [Sample Size: ', num2str(numSubjects), ']'];
    fileName = [dataFolder, '\', taskName,'_All_Trials_All_Stages_Delta_Negentropy_', typeFileName, '_ErrorBar'];
end
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
        figName = [taskName, ', Trials ', num2str(trial), '-', num2str(lastTrial), ', All Stages: Delta Negentropy  [Sample Size: ', num2str(numSubjects), ']'];
        fileName = [dataFolder, '\', taskName, '_Trials_', num2str(trial), '-', num2str(lastTrial), '_All_Stages_Delta_Negentropy_ErrorBar'];
        figHandle = figure('name', figName, 'position', figPositionLarge, 'NumberTitle', 'off');
        title(figName);
        subplotNum = 1;
    end
    subplot(numRows, 3, subplotNum);
    set(gca, 'box', 'on');
    hold on;
    title(['Trial ' num2str(trial)]);
    xlim([1 numStages+1]);
    set(gca,'xtick', 1:numStages);
    %ymax = ylim;
    %ylim([0 ymax(2)]);    
    ylim([minNe - 0.05 maxNe + 0.05]);
    
    plot(1:numStages + 1, zeros(1, numStages+1), ':k');
    
    %Plot subject and normative negentroy at each stage of the current trial    
    plot(2:numStages, subjectDeltaNeAvg(trial, 2:numStages), '-ob');
    plot((2+normativeXOffset):(numStages+normativeXOffset), normativeDeltaNeAvg(trial, 2:numStages), '-*k');
    
    %Plot error bars that show the std for the subject negentropy at each stage   
    errorbar(2:numStages, subjectDeltaNeAvg(trial, 2:numStages), subjectDeltaNeErr(trial, 2:numStages), '-b');
    
    if showNormativeVariance
        %Plot error bars that show the std for the normative negentropy at each stage    
        errorbar((2+normativeXOffset):(numStages+normativeXOffset),...
            normativeDeltaNeAvg(trial, 2:numStages), normativeDeltaNeErr(trial, 2:numStages), '-k');
    end
    
    subplotNum = subplotNum + 1;    
end

%Save figure
if saveData
    saveas(figHandle, fileName, 'png');
end
%%%%

end