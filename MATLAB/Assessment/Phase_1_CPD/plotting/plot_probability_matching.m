function wfs = plot_probability_matching(subjects, subjectProbs,...
    normalizedTroopAllocations, taskNum, saveData, showPlots, dataFolder)
%PLOT_PROBABILITY_MATCHING Plot probability matching for all Tasks.
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

numSubjects = size(subjectProbs, 1);
numTrials = size(subjectProbs, 2);
numStages = size(subjectProbs{1,1}, 1);
numProbs = size(subjectProbs{1,1},2);
epsilon = 0.01;

%% Aggregate troop allocation data for all subjects and trials
%rms([4 4 4 4], [5 4 4 4])
if taskNum < 4
    subjectProbsAvg = [];
    allocationsAvg = [];
    frequencyHighestChosen = zeros(numSubjects, numTrials);
    maxProb = zeros(numSubjects, numTrials);    
    
    rmsToSubjectProbabilities = [];
    rmsToAvgSubjectProbabilities = [];
    rmsToAvgSubjectProbabilitiesErr = [];
    
    rmsToHighestAllocationDistribution = [];
    rmsToAvgHighestAllocationDistribution = [];
    rmsToAvgHighestAllocationDistributionErr = [];
else
    subjectProbsAvg = zeros(numTrials, numProbs);
    allocationsAvg = zeros(numTrials, numProbs);
    
    rmsToSubjectProbabilities = zeros(numSubjects, numTrials);
    rmsToAvgSubjectProbabilities = zeros(numTrials, 1);
    rmsToAvgSubjectProbabilitiesErr = zeros(numTrials, 1);
    
    rmsToHighestAllocationDistribution = zeros(numSubjects, numTrials);
    rmsToAvgHighestAllocationDistribution = zeros(numTrials, 1);
    rmsToAvgHighestAllocationDistributionErr = zeros(numTrials, 1);
    
    frequencyHighestChosen = [];
    maxProb = [];
end

for subject = 1:numSubjects
    for trial = 1:numTrials
        allocations = normalizedTroopAllocations{subject, trial};         
        p_subj = subjectProbs{subject, trial}(numStages, :);
        probs = fillzerobins(p_subj, epsilon);        
        if taskNum < 4
            %Determine if troops were allocated against one of the groups
            %with the highest subject probability            
            %[maxProb(subject, trial), maxProbIndex] = max(probs);
            maxProb(subject, trial) = max(probs);
            maxProbIndices = find(probs == maxProb(subject, trial));
            [~, maxAllocationIndex] = max(allocations);
            %maxAllocation = max(allocations); 
            %maxAllocationIndices = find(allocations == maxAllocation);
            %if maxProbIndex == maxAllocationIndex
            %disp([probs, maxProbIndices]);
            %disp([allocations, maxProbIndices]);
            if ~isempty(find(maxProbIndices == maxAllocationIndex, 1))               
                frequencyHighestChosen(subject, trial) = 1;
            end           
            %diffFrequencyHighestChosenToHighestP(subject, trial) = ...
            %    abs(frequencyHighestChosen(subject, trial) - probs(maxProbIndex));
            %diffFrequencyHighestChosenTo1(subject, trial) = ...
            %    abs(frequencyHighestChosen(subject, trial) - 1);            
            %FhAvg = FhAvg + frequencyHighestChosen(subject, trial);
            %PhAvg = PhAvg + probs(maxProbIndex);
        else
            %Compute average subject probs and allocations
            subjectProbsAvg(trial, :) = subjectProbsAvg(trial, :) + p_subj;
            allocationsAvg(trial, :) = allocationsAvg(trial, :) + allocations;
            
            %Create a distribution I where 1 is assigned to the
            %group or location with the highest subject probability
            %TODO: IF 2 OR MORE GROUPS TIED, SHOULD WE MODIFY THE I DISTRIBUTION?
            I = create_I_distribution(probs);
            %Compute RMS between troop allocations and subject probabilities
            rmsToSubjectProbabilities(subject, trial) = rmse(allocations, probs); %#ok<*AGROW>
            %Compute RMS between troop allocations and I distribution
            rmsToHighestAllocationDistribution(subject, trial) = rmse(allocations, I);
        end
    end
end
%FhAvg = FhAvg / (numSubjects*numTrials)
%PhAvg = PhAvg / (numSubjects*numTrials)
%abs(FhAvg - PhAvg)
%abs(FhAvg - 1)
%frequencyHighestChosen
%diffFrequencyHighestChosenToHighestP
%diffFrequencyHighestChosenTo1
%rmsToSubjectProbabilities
%rmsToHighestAllocationDistribution

%Compute RMS_F_P and RMS_F_I based on average subject probs and average
%allocations across all subjects for each trial
if taskNum >= 4
    subjectProbsAvg = subjectProbsAvg ./ numSubjects;
    allocationsAvg = allocationsAvg ./ numSubjects;
    for trial = 1:numTrials
        probs = fillzerobins(subjectProbsAvg(trial, :), epsilon);
        allocations = allocationsAvg(trial, :);
        
        %Create a distribution I where 1 is assigned to the
        %group or location with the highest subject probability        
        I = create_I_distribution(probs);
        %Compute RMS between troop allocations and subject probabilities
        rmsToAvgSubjectProbabilities(trial) = rmse(allocations, probs);
        %Compute RMS between troop allocations and I distribution
        rmsToAvgHighestAllocationDistribution(trial) = rmse(allocations, I);
        
        %Compute standard deviations        
        rmsToAvgSubjectProbabilitiesErr(trial) = ...
            sqrt(sum((rmsToSubjectProbabilities(:, trial) - ...
            rmsToAvgSubjectProbabilities(trial)).^2 )/(numSubjects-1));
        rmsToAvgHighestAllocationDistributionErr(trial) = ...
            sqrt(sum((rmsToHighestAllocationDistribution(:, trial) - ...
            rmsToAvgHighestAllocationDistribution(trial)).^2 )/(numSubjects-1));
    end    
end

%% Create plot showing frequency with which the highest group was chosen (Tasks 1-3)
figPosition = [60, 60, 840, 600];
taskName = ['Mission ' num2str(taskNum)];
if taskNum < 4
    frequencyHighestChosenAvg = sum(frequencyHighestChosen)/size(frequencyHighestChosen, 1);
    Fh = sum(frequencyHighestChosenAvg)/length(frequencyHighestChosenAvg);
    maxProbAvg = sum(maxProb)/size(maxProb, 1);
    Fh_Ph_ByTrial = frequencyHighestChosenAvg - maxProbAvg;
    Fh_1_ByTrial = frequencyHighestChosenAvg - 1;    
    Fh_Ph = abs(sum(Fh_Ph_ByTrial)/length(Fh_Ph_ByTrial));
    Fh_1 = abs(sum(Fh_1_ByTrial)/length(Fh_1_ByTrial));
    Fh_Ph_ByTrial = abs(Fh_Ph_ByTrial);
    Fh_1_ByTrial = abs(Fh_1_ByTrial);
    %diffFrequencyHighestChosenToHighestPAvg = ...
    %    sum(diffFrequencyHighestChosenToHighestP)/size( diffFrequencyHighestChosenToHighestP, 1)
    %diffFrequencyHighestChosenTo1Avg = ...
    %    sum(diffFrequencyHighestChosenTo1)/size( diffFrequencyHighestChosenTo1, 1)    
    %Fh_Ph = sum(diffFrequencyHighestChosenToHighestPAvg)/length(diffFrequencyHighestChosenToHighestPAvg);
    %Fh_1 = sum(diffFrequencyHighestChosenTo1Avg)/length(diffFrequencyHighestChosenTo1Avg);    
    
    %Create plot just showing Fh for each trial
    figName = [taskName, ', Probability Matching, Fh=', num2str(Fh), ' [Sample Size: ', num2str(numSubjects), ']'];
    figHandle = figure('name', figName, 'position', figPosition, 'NumberTitle', 'off', 'Visible', visible);
    hold on;
    title(figName);
    xlabel('Trial');
    xlim([0 numTrials+1]);
    set(gca,'xtick', 1:numTrials);
    ylabel('Fh');
    %ymax = ylim;
    %ylim([0 ymax(2)]);
    %Plot average Fh for each trial
    plot(frequencyHighestChosenAvg, '-ob');   
    %Plot error bars that show the std for the Fh distrubution at each trial
    errorbar(1:numTrials, frequencyHighestChosenAvg, std(frequencyHighestChosen), '-b');    
    %Save figure
    if saveData
        fileName = [dataFolder, '\', taskName,'_Probability_Matching_Fh'];        
        saveas(figHandle, fileName, 'png');
    end
    % create webfigure
    wf_fh = webfigure(figHandle);
    
    %Create plot showing |Fh-Ph| and |Fh-1| for each trial
    figName = [taskName, ', Probability Matching, |Fh - Ph|=', num2str(Fh_Ph), ', |Fh - 1|=', num2str(Fh_1),' [Sample Size: ', num2str(numSubjects), ']'];
    figHandle = figure('name', figName, 'position', figPosition, 'NumberTitle', 'off', 'Visible', visible);
    hold on;
    title(figName);
    xlabel('Trial');
    xlim([0 numTrials+1]);
    set(gca,'xtick', 1:numTrials);
    ylabel('|Fh - Ph|, |Fh - 1|');
    %ymax = ylim;
    %ylim([0 ymax(2)]);
    %Plot average |Fh-Ph| for each trial
    %handle1 = plot(diffFrequencyHighestChosenToHighestPAvg, '-ob');   
    handle1 = plot(Fh_Ph_ByTrial, '-ob');   
    %Plot error bars that show the std for the |Fh-Ph| distrubution at each trial
    %errorbar(1:numTrials, diffFrequencyHighestChosenToHighestPAvg, std(diffFrequencyHighestChosenToHighestP), '-b');
    %Plot average |Fh-1| for each trial
    xOffset = 0.1;
    %handle2 = plot((1+xOffset):(numTrials+xOffset), diffFrequencyHighestChosenTo1Avg, '-or');   
    handle2 = plot((1+xOffset):(numTrials+xOffset), Fh_1_ByTrial, '-or');   
    %Plot error bars that show the std for the |Fh-1| distrubution at each trial
    %errorbar((1+xOffset):(numTrials+xOffset), diffFrequencyHighestChosenTo1Avg,...
    %    std( diffFrequencyHighestChosenTo1), '-r');    
    legend([handle1, handle2], {'|Fh - Ph|', '|Fh - 1|'});
    %Save figure
    if saveData
        fileName = [dataFolder, '\', taskName,'_Probability_Matching_Fh_Ph_And_Fh_1'];
        saveas(figHandle, fileName, 'png');
    end
    % create webfigure
    wf_abs = webfigure(figHandle);
    % return cell array of 2 webfigures
    wfs = cell(2,1);
    wfs{1} = wf_fh;
    wfs{2} = wf_abs;
end

%% Create plot showing difference between RMS to subject probabilities and RMS to I distributions (Tasks 4-6)
if taskNum > 3
    for i=1:2
        if i == 1            
            rmsToSubjectProbabilitiesAvg = ...
                sum(rmsToSubjectProbabilities)/size(rmsToSubjectProbabilities, 1);
            rmsToHighestAllocationDistributionAvg = ...
                sum(rmsToHighestAllocationDistribution)/size(rmsToHighestAllocationDistribution, 1);
            rmsToSubjectProbabilitiesErr = std(rmsToSubjectProbabilities);
            rmsToHighestAllocationDistributionErr = std(rmsToHighestAllocationDistribution);
        else           
           rmsToSubjectProbabilitiesAvg = rmsToAvgSubjectProbabilities;
           rmsToHighestAllocationDistributionAvg = rmsToAvgHighestAllocationDistribution;
           rmsToSubjectProbabilitiesErr = rmsToAvgSubjectProbabilitiesErr;
           rmsToHighestAllocationDistributionErr = rmsToAvgHighestAllocationDistributionErr;
        end                
        Rms_F_P = sum(rmsToSubjectProbabilitiesAvg)/length(rmsToSubjectProbabilitiesAvg);
        Rms_F_I = sum(rmsToHighestAllocationDistributionAvg)/length(rmsToHighestAllocationDistributionAvg);
        
        if i == 1
            figName = [taskName, ', Probability Matching, RMS(F-P)=', num2str(Rms_F_P), ', RMS(F-I)=', num2str(Rms_F_I),' [Sample Size: ', num2str(numSubjects), ']'];
        else
            figName = [taskName, ', Probability Matching (Avg Probs and Allocations), RMS(F-P)=', num2str(Rms_F_P), ', RMS(F-I)=', num2str(Rms_F_I),' [Sample Size: ', num2str(numSubjects), ']']; 
        end
        
        figHandle = figure('name', figName, 'position', figPosition, 'NumberTitle', 'off', 'Visible', visible);
        hold on;
        title(figName);
        xlabel('Trial');
        xlim([0 numTrials+1]);
        set(gca,'xtick', 1:numTrials);
        ylabel('RMS Difference');
        %ymax = ylim;
        %ylim([0 ymax(2)]);
        %Plot average RMS(F-P) for each trial
        handle1 = plot(rmsToSubjectProbabilitiesAvg, '-ob');
        %Plot error bars that show the std for the RMS(F-P) distrubution at each trial
        errorbar(1:numTrials, rmsToSubjectProbabilitiesAvg, rmsToSubjectProbabilitiesErr, '-b');
        %Plot average RMS(F-I) for each trial
        xOffset = 0.1;
        handle2 = plot((1+xOffset):(numTrials+xOffset), rmsToHighestAllocationDistributionAvg, '-or');
        %Plot error bars that show the std for the RMS(F-I) distrubution at each trial
        errorbar((1+xOffset):(numTrials+xOffset), rmsToHighestAllocationDistributionAvg,...
            rmsToHighestAllocationDistributionErr, '-r');
        legend([handle1, handle2], {'RMS(F-P)', 'RMS(F-I)'});
        %Save figure
        if saveData
            if i == 1
                fileName = [dataFolder, '\', taskName,'_Probability_Matching_RMS'];
            else
                fileName = [dataFolder, '\', taskName,'_Probability_Matching_Avg_Probs_RMS'];
            end
            saveas(figHandle, fileName, 'png');
        end
        % create webfigure
        wf_diff = webfigure(figHandle);
        % return cell array of 1 webfigure
        wfs = cell(1,1);
        wfs{1} = wf_diff;
    end
end
end