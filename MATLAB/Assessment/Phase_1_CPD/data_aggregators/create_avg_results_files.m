function create_avg_results_files(tasks, dataInputFolder)
%CREATE_AVG_RESULTS_FILES Create a file containing the all subject probability
%entries for each stage of each trial, a file containing the average
%subject probabilities for each stage of each trial, and a file containing
%the average subject centers and sigmas for Tasks 2-3.
%   Detailed explanation goes here

for task = tasks
    %[subjects, cumulativeBayesianProbs, normalizedSubjectProbs, rawSubjectProbs, normalizedTroopAllocations,...
    %surpriseData, normativeGroupCenters, subjectGroupCenters, distances, layerSelectionData, bestLayerData,...
    %layerSurpriseData, timeDataByTrial, groundTruthData]
    [subjects, ~, subjectProbs, ~, ~,...
        surpriseData, ~, subjectGroupCenters, ~, ~, ~,...
        layerSurpriseData, ~, groundTruthData] = ...
        aggregate_csv_task_data([dataInputFolder, '\subject_data\allresponses_task_' num2str(task) '.csv'], task);
    %size(subjectProbs,1)
    if task == 4
        headings = {'1' '2' '3' '4'};
    else
        headings = {'A' 'B' 'C' 'D'};
    end
    stages = 1;
    switch task
        case 4, stages = 2;
        case 5, stages = 5;
        case 6, stages = 1;
    end
    
    % Create all probabilities file
    write_all_probabilities_file(subjects, subjectProbs, headings, task, stages);
    
    % Create average probabilities file
    write_avg_probabilities_file(subjectProbs, headings, task, stages);
    
    % Create average centers and sigmas file (Tasks 2-3 only)
    if task == 2 || task == 3
        write_avg_centers_sigmas_file(subjectGroupCenters, task);
    end
    
    % Create surprise file(s)
    write_surprise_file(surpriseData, layerSurpriseData, subjectProbs, groundTruthData,...
        headings, task, stages);
end
end