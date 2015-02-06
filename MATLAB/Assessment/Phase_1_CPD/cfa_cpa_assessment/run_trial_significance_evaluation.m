function run_trial_significance_evaluation()
%RUN_TRIAL_SIGNIFICANCE_EVALUATION Summary of this function goes here
%   Detailed explanation goes here

exam_folder = 'Final Exam August 2012\subject_data'; %Location of exam task files
tasks = 5; %Tasks to use in evaluation
subject_data = cell(length(tasks),1); %Exam data for each task
%layers = [];
%permutations = [];
%numLayers = [];
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

%Evaluate signifinace of CPA and CFA measures for each stage of each trial
%of each task in the exam
evaluate_trial_significance(subject_data, tasks, true);

end