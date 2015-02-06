function create_effect_size_plots(tasks, plotNegentropy, plotProbabilityMatching, plotConfirmationBias,...
    useAbProbs, saveData, showPlots)
%CREATE_EFFECT_SIZE_PLOTS Summary of this function goes here
%   Detailed explanation goes here

if ~exist('saveData', 'var')
    saveData = false;
end
if ~exist('showPlots', 'var')
    showPlots = true;
end
dataFolder = 'figures\effect_size_figures';
if saveData && ~isdir(dataFolder)
    mkdir(dataFolder);
end

taskData = cell(length(tasks));
i = 1;
for task = tasks
    %Load human subject data
    [d.subjects, d.cumulativeBayesianProbs, d.normalizedSubjectProbs, d.rawSubjectProbs,...
        d.normalizedTroopAllocations, d.surpriseData, d.normativeGroupCenters, d.subjectGroupCenters,...
        d.distances, d.layerSelectionData, d.bestLayerData, d.layerSurpriseData, d.timeDataByTrial, ~] = ...
        aggregate_csv_task_data(strcat('allresponses_task_', num2str(task), '.csv'), task);
    taskData{i} = d;
    %if i == 1
    %    subjects = d.subjects;        
    %end
    %i = i + 1;    
    numSubjects = size(d.normalizedSubjectProbs, 1);
    numTrials = size(d.normalizedSubjectProbs, 2);  
    numProbs = size(d.normalizedSubjectProbs{1,1}, 2);
    
    %Create effect size plots using negentropy metrics
    if plotNegentropy && task < 6
        %Load normative data and replicate for all subjects, trials, stages
        probs = read_probs_file(['Task_' num2str(task) '_Normative_Posteriors.csv']);
        normativeProbs = cell(numSubjects, numTrials);
        for subject = 1:numSubjects
            for trial = 1:numTrials
                normativeProbs{subject, trial} = probs{trial}(:,2:numProbs+1);
            end
        end
        %normativeProbs{1, 1}
        
        %Load A-B model data and replicate for all subjects, trials, stages
        abProbs = [];
        if useAbProbs
            probs = read_probs_file(['Task_' num2str(task) '_AB_Posteriors.csv']);
            abProbs = cell(numSubjects, numTrials);                        
            for subject = 1:numSubjects
                for trial = 1:numTrials
                    abProbs{subject, trial} = probs{trial}(:,2:numProbs+1);
                end
            end
        end
        
        plot_ne_effect_size(d.normalizedSubjectProbs, abProbs, normativeProbs,...
            task, saveData, showPlots, dataFolder)
    end
    
    %Create effect size plots using probability matching metrics
    if plotProbabilityMatching && task >= 4 && task <= 6
        plot_probability_matching_effect_size(d.normalizedSubjectProbs,...
            d.normalizedTroopAllocations, task, saveData, showPlots, dataFolder);
    end
    
    %Create effect size plots using confirmation bias metrics
    if plotConfirmationBias && task == 6
        numLayers = size(d.layerSelectionData.subjectLayerSelectionsByStage, 3);     
        plot_confirmation_bias_effect_size(d.normalizedSubjectProbs, numLayers,...
                d.layerSelectionData.subjectLayerSelectionsByStage,...
                task, saveData, showPlots, dataFolder);
    end    
end

end