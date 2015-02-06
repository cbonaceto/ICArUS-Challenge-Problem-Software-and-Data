exam_folder = 'Final Exam August 2012\subject_data'; %Location of exam task files
tasks = 5; %Tasks to use in evaluation
subject_data = cell(length(tasks),1); %Exam data for each task
for task = tasks
    %Load task data from exam
    clear d;
    [d.subjects, d.cumulativeBayesianProbs, d.normalizedSubjectProbs, ~,...
        d.normalizedTroopAllocations, d.surpriseData, d.normativeGroupCenters, d.subjectGroupCenters,...
        d.distances, d.layerSelectionData, d.bestLayerData, d.layerSurpriseData, ~, ~] = ...
        aggregate_csv_task_data(strcat(exam_folder,'\allresponses_task_', num2str(task), '.csv'), task);    
%     if task == 6
%         layers = d.layerSelectionData.layers;
%         permutations = d.layerSelectionData.permutations;
%         numLayers = size(d.layerSelectionData.subjectLayerSelectionsByStage, 3);
%     else    
%         %Also load A-B model probabilities for final exam    
%         d.abProbs = read_probs_file(strcat('Final Exam August 2012\normative_data\Task_', num2str(task), '_AB_Posteriors.csv'), true);              
%     end
    subject_data{task} = d;        
end
metrics_data = compute_exam_metrics(subject_data, tasks);

