function plot_probability_matching_effect_size(subjectProbs,...
    normalizedTroopAllocations, taskNum, saveData, showPlots, dataFolder)
%PLOT_PROBABILITY_MATCHING_EFFECT_SIZE Plot effect size using probability
%matching metrics.
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

%% Aggregate troop allocation data for all subjects and trials
numSubjects = size(subjectProbs, 1);
numTrials = size(subjectProbs, 2);
numStages = size(subjectProbs{1,1}, 1);
if taskNum < 4
    frequencyHighestChosen = zeros(numSubjects, numTrials);
    maxProb = zeros(numSubjects, numTrials); 
    rmsToSubjectProbabilities = [];
    rmsToHighestAllocationDistribution = [];
else
    rmsToSubjectProbabilities = zeros(numSubjects, numTrials);
    rmsToHighestAllocationDistribution = zeros(numSubjects, numTrials);
    frequencyHighestChosen = [];
    maxProb = [];   
end
for subject = 1:numSubjects
    for trial = 1:numTrials
        allocations = normalizedTroopAllocations{subject, trial};
        probs = subjectProbs{subject, trial}(numStages, :);
        %normalizedTroopAllocations{subject, trial}
        if taskNum < 4
            %Determine if troops were allocated against one of the groups
            %with the highest subject probability   
            maxProb(subject, trial) = max(probs);
            maxProbIndices = find(probs == maxProb(subject, trial));
            [~, maxAllocationIndex] = max(allocations);            
            if ~isempty(find(maxProbIndices == maxAllocationIndex, 1))               
                frequencyHighestChosen(subject, trial) = 1;
            end  
        else
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

%% Create effect size plot showing difference between RMS to subject probabilities and RMS to I distributions (Tasks 4-6)
figPosition = [60, 60, 800, 600];
taskName = ['Mission ' num2str(taskNum)];
if taskNum > 3
    rmsToSubjectProbabilitiesAvg = ...
        sum(rmsToSubjectProbabilities)/size(rmsToSubjectProbabilities, 1);
    rmsToHighestAllocationDistributionAvg = ...
        sum(rmsToHighestAllocationDistribution)/size(rmsToHighestAllocationDistribution, 1);
    Rms_F_P = sum(rmsToSubjectProbabilitiesAvg)/length(rmsToSubjectProbabilitiesAvg);
    Rms_F_I = sum(rmsToHighestAllocationDistributionAvg)/length(rmsToHighestAllocationDistributionAvg);
    
    figName = [taskName, ', Probability Matching Effect Size, RMS(F-P)=', num2str(Rms_F_P), ', RMS(F-I)=', num2str(Rms_F_I),' [Sample Size: ', num2str(numSubjects), ']'];
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
    sigma_F_P = std(rmsToSubjectProbabilities);
    errorbar(1:numTrials, rmsToSubjectProbabilitiesAvg, sigma_F_P, '-b');
    
    %Plot average RMS(F-I) for each trial
    xOffset = 0.1;
    handle2 = plot((1+xOffset):(numTrials+xOffset), rmsToHighestAllocationDistributionAvg, '-ok');   
    
    %Plot error bars that show the std for the RMS(F-I) distrubution at each trial
    sigma_F_I = std(rmsToHighestAllocationDistribution);
    errorbar((1+xOffset):(numTrials+xOffset), rmsToHighestAllocationDistributionAvg,...
       sigma_F_I, '-k');    
    
    %Plot solid red line at threshold of significance using N=100
    %The solid red curve is computed as follows: RMS(F-I) – 0.5 * sigma, 
    %where sigma on each trial is the average of sigma for RMS(F-P) and sigma for RMS(F-I)       
    sigmaAvg = (sigma_F_P + sigma_F_I)/2;
    %0.5 * sigmaAvg
    d = rmsToHighestAllocationDistributionAvg - (0.5 * sigmaAvg);
    handle3 = plot((1+xOffset):(numTrials+xOffset), d, '-r');
    
    %Plot dotted red line at threshold of significance using N=45
    d = rmsToHighestAllocationDistributionAvg - (0.7 * sigmaAvg);
    handle4 = plot((1+xOffset):(numTrials+xOffset), d, ':r');
    
    legend([handle1, handle2, handle3, handle4], {'RMS(F-P)', 'RMS(F-I)', 'N=100 Threshold', 'N=45 Threshold'});  
    
    %Save figure
    if saveData
        fileName = [dataFolder, '\', taskName,'_Probability_Matching_Effect_Size'];
        hgsave(fileName, '-v6');
        saveas(figHandle, fileName, 'png');
    end   
end
end