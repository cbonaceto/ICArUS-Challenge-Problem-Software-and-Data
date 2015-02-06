function create_all_metrics_plots(tasks, plotNegentropy, plotProbabilityMatching,...
    plotGroupLocations, plotBaseRateNeglect, plotSigintChoices, plotLayerSelections,...
    plotLayerUtilities, plotTotalTime, dataInputFolder, saveData, showPlots)
%CREATE_ALL_METRICS_PLOTS Summary of this function goes here
%   Detailed explanation goes here

if ~exist('saveData', 'var')
    saveData = false;
end
if ~exist('showPlots', 'var')
    showPlots = true;
end
if ~exist('dataInputFolder', 'var') || isempty(dataInputFolder)
    dataInputFolder = '';
end

%% Aggregate subject data for tasks
taskData = cell(length(tasks),1);
timeData = cell(length(tasks),1);
i = 1;
for task = tasks    
    [d.subjects, d.cumulativeBayesianProbs, d.normalizedSubjectProbs, d.rawSubjectProbs,...
        d.normalizedTroopAllocations, d.surpriseData, d.normativeGroupCenters, d.subjectGroupCenters,... 
        d.distances, d.layerSelectionData, d.bestLayerData, d.layerSurpriseData, d.timeDataByTrial, ~] = ...
        aggregate_csv_task_data(strcat(dataInputFolder,'\subject_data\allresponses_task_', num2str(task), '.csv'), task);
    taskData{i} = d;
    timeData{i} = d.timeDataByTrial;
    if i == 1
        subjects = d.subjects;
    end
    i = i + 1;
end

%% Create Negentropy plots for all tasks
if plotNegentropy
    %Create folder to store figure if it doesn't exist
    dataFolder = 'figures\negentropy_figures';
    if saveData && ~isdir(dataFolder)
        mkdir(dataFolder);
    end
    i = 1;
    for task = tasks
        if task < 7
            d = taskData{i};
            plot_negentropy(d.subjects, d.normalizedSubjectProbs,...
                d.cumulativeBayesianProbs, task, saveData, showPlots, dataFolder);
            if task > 3
                plot_delta_negentropy(d.normalizedSubjectProbs,...
                    d.cumulativeBayesianProbs, task, saveData, showPlots, dataFolder);
            end
        end
        i = i + 1;
    end
end

%% Create probability matching plots for all tasks
if plotProbabilityMatching
    %Create folder to store figure if it doesn't exist
    dataFolder = 'figures\probability_matching_figures';
    if saveData && ~isdir(dataFolder)
        mkdir(dataFolder);
    end
    i = 1;
    for task = tasks
        if task < 7
            d = taskData{i};
            plot_probability_matching(d.subjects, d.normalizedSubjectProbs,...
                d.normalizedTroopAllocations, task, saveData, showPlots, dataFolder);           
        end
        i = i + 1;
    end
end

%% Create Task 2-3 group location dispersion plots and base rate neglect plots
if plotGroupLocations || plotBaseRateNeglect
    %Create folder to store figure if it doesn't exist    
    dispersionDataFolder = 'figures\group_center_dispersion_figures';
    if saveData && plotGroupLocations && ~isdir(dispersionDataFolder)
        mkdir(dispersionDataFolder);
    end
    baseRateNeglectDataFolder = 'figures\base_rate_neglect_figures';
    if saveData && plotBaseRateNeglect && ~isdir(baseRateNeglectDataFolder)
        mkdir(baseRateNeglectDataFolder);
    end
    i = 1;
    for task = tasks
        d = taskData{i};
        if task == 2 || task == 3
            %Get attack location feature vector file names
            if isfield(d, 'normativeGroupCenters') && isfield(d, 'subjectGroupCenters')
                numTrials = size(d.subjectGroupCenters,2);              
                allAttackLocationFiles = cell(numTrials, 1);
                probeAttackLocationFiles = cell(numTrials, 1);
                for trial = 1:numTrials
                    probeAttackLocationFiles{trial} = [dataInputFolder '\feature_vector_data\task' num2str(task) '_' num2str(trial) '_probe.csv'];
                    if trial > 1                         
                        allAttackLocationFiles{trial} = {[dataInputFolder '\feature_vector_data\task' num2str(task) '_' num2str(trial) '.csv'],...
                            ['task' num2str(task) '_' num2str(trial-1) '_probe.csv']};
                    else
                        allAttackLocationFiles{trial} = [dataInputFolder '\feature_vector_data\task' num2str(task) '_' num2str(trial) '.csv'];
                    end
                end
                if plotGroupLocations
                    plot_group_center_dispersion(d.subjects, d.normativeGroupCenters,...
                        d.subjectGroupCenters, allAttackLocationFiles, task, saveData, showPlots, dispersionDataFolder);
                end
                if plotBaseRateNeglect
                    plot_base_rate_neglect(d.subjects, d.normalizedSubjectProbs, d.cumulativeBayesianProbs,...
                        d.subjectGroupCenters, d.normativeGroupCenters,...
                        d.distances, probeAttackLocationFiles,...
                        task, saveData, showPlots, baseRateNeglectDataFolder)
                end
            end
        end
        i = i + 1;
    end
end

%% Create confirmation bias plots based on SIGINT layer selections (Task 6 only)
if plotSigintChoices
    %Create folder to store figure if it doesn't exist
    dataFolder = 'figures\confirmation_bias_figures';
    if saveData && ~isdir(dataFolder)
        mkdir(dataFolder);
    end
    i = 1;
    for task = tasks
        if task < 7
            d = taskData{i};
            if isfield(d, 'layerSelectionData') && ~isempty(d.layerSelectionData)
                numLayers = size(d.layerSelectionData.subjectLayerSelectionsByStage, 3);
                plot_sigint_choices(d.normalizedSubjectProbs, numLayers,...
                    d.layerSelectionData.subjectLayerSelectionsByStage, d.bestLayerData,...
                    task, saveData, showPlots, dataFolder)
            end
        end
        i = i +1 ;
    end
end

%% Create Task 6 layer selections plot
if plotLayerSelections || plotLayerUtilities
    %Create folder to store figures if it doesn't exist
    dataFolder = 'figures\layer_selection_figures';
    if saveData && ~isdir(dataFolder)
        mkdir(dataFolder);
    end
    i = 1;
    for task = tasks
        if task < 7
            d = taskData{i};
            if isfield(d, 'layerSelectionData') && ~isempty(d.layerSelectionData)
                %Load normative layer selection data and layer selection utility data
                if task == 6
                    [layerSelectionStrategies, normativeLayerSelections, allLayerUtilities, layers, permutations] = ...
                        read_layer_choices_files(...
                        [dataInputFolder '\normative_data\Task_' num2str(task) '_Normative_Layer_Choices.csv'],...
                        [dataInputFolder '\normative_data\Task_' num2str(task) '_All_Layer_Utilities.csv'],...
                        [dataInputFolder '\normative_data\Task_' num2str(task) '_BB_Layer_Utilities.csv'],...
                        [dataInputFolder '\normative_data\Task_' num2str(task) '_BH_Layer_Utilities.csv'],...
                        [dataInputFolder '\normative_data\Task_' num2str(task) '_HB_Layer_Utilities.csv']);
                end
                
                %Plot subject and normative layer selections
                if plotLayerSelections
                    numLayers = size(d.layerSelectionData.subjectLayerSelectionsByStage, 3);
                    plot_layer_selection_permutations(d.subjects, d.layerSelectionData.layers,...
                        d.layerSelectionData.permutations, numLayers,...
                        d.layerSelectionData.subjectLayerSelections, normativeLayerSelections,...
                        task, saveData, showPlots, dataFolder);
                end
				
				%Plot layer selection strategies
				if plotLayerUtilities && ~isempty(layerSelectionStrategies)
					plot_layer_selection_strategies(layers, permutations, numLayers,...
						layerSelectionStrategies, task, saveData, showPlots, dataFolder)
				end
                
                %Plot layer selection utilitiy data for all permutations
                if plotLayerUtilities && ~isempty(allLayerUtilities)
                    plot_layer_utility(layers, permutations, numLayers, allLayerUtilities,...
                        task, saveData, showPlots, dataFolder);
                end
            end
        end
        i = i + 1;
    end
end

%% Create total time plot (shows time taken on all tasks)
if plotTotalTime
    %Create folder to store figure if it doesn't exist
    dataFolder = 'figures\timing_figures';
    if saveData && ~isdir(dataFolder)
        mkdir(dataFolder);
    end
    plot_time_metrics(timeData, subjects, tasks, saveData, showPlots, dataFolder);
end

end