%Matched Trials Between Final and Pilot (Task-Trial):
%4-7, 4-8
%5-8, 5-9
%6-8, 6-9
matchedTrials = cell(6, 1);
matchedTrials{1}.task = 4;
matchedTrials{1}.trial = 7;
matchedTrials{2}.task = 4;
matchedTrials{2}.trial = 8;
matchedTrials{3}.task = 5;
matchedTrials{3}.trial = 8;
matchedTrials{4}.task = 5;
matchedTrials{4}.trial = 9;
matchedTrials{5}.task = 6;
matchedTrials{5}.trial = 8;
matchedTrials{6}.task = 6;
matchedTrials{6}.trial = 9;

taskWeights = [.05 .1 .15 .15 .4 0] / .85; %Weights for each task used in average RSR calculation
tasks = 1:6; %Tasks to use in evaluation
final_data = cell(length(tasks),1); %Data from final exam
pilot_data = cell(length(tasks),1); %Data from pilot exam
layers = [];
permutations = [];
numLayers = [];
for task = tasks
    %Load task data from final exam
    clear d;
    [d.subjects, d.cumulativeBayesianProbs, d.normalizedSubjectProbs, ~,...
        d.normalizedTroopAllocations, d.surpriseData, d.normativeGroupCenters, d.subjectGroupCenters,...
        d.distances, d.layerSelectionData, d.bestLayerData, d.layerSurpriseData, ~, ~] = ...
        aggregate_csv_task_data(strcat('Final Exam August 2012\subject_data\allresponses_task_', num2str(task), '.csv'), task);    
    if task == 6
        layers = d.layerSelectionData.layers;
        permutations = d.layerSelectionData.permutations;
        numLayers = size(d.layerSelectionData.subjectLayerSelectionsByStage, 3);
    else    
        %Also load A-B model probabilities for final exam    
        d.abProbs = read_probs_file(strcat('Final Exam August 2012\normative_data\Task_', num2str(task), '_AB_Posteriors.csv'), true);              
    end
    final_data{task} = d;        
    
    %Load task data from pilot exam    
    clear d;
    [d.subjects, d.cumulativeBayesianProbs, d.normalizedSubjectProbs, ~,...
        d.normalizedTroopAllocations, d.surpriseData, d.normativeGroupCenters, d.subjectGroupCenters,...
        d.distances, d.layerSelectionData, d.bestLayerData, d.layerSurpriseData, ~, ~] = ...
        aggregate_csv_task_data(strcat('Pilot Exam March 2012\subject_data\allresponses_task_', num2str(task), '.csv'), task);
    if task ~= 6
        %Also load A-B model probabilities for pilot exam
        d.abProbs = read_probs_file(strcat('Pilot Exam March 2012\normative_data\Task_', num2str(task), '_AB_Posteriors.csv'), true);       
        %d.abProbs{1}
    end    
    pilot_data{task} = d;        
end

%Compare final and pilot probabilities
%When computing RSR, treat the pilot data as the model and the final data
%as the human
[data, probsComparison, neComparison, rsrComparison, layersComparison, layerComparisionMultipleTrials] = ...
    compare_exam_data_sets(pilot_data, final_data, tasks, {'Pilot'; 'Final'}, matchedTrials, false);

%Trials to exclude from calculating RSR on based on significance testing
rsrExcludedTrials = zeros(6, 10, 5);
rsrExcludedTrials(1, 9, 1) = 1;
rsrExcludedTrials(5, 1, 1) = 1;
rsrExcludedTrials(5, 2, 1) = 1;
rsrExcludedTrials(5, 3, 1) = 1;
rsrExcludedTrials(5, 4, 1) = 1;
rsrExcludedTrials(5, 5, 1) = 1;
rsrExcludedTrials(5, 6, 1) = 1;
rsrExcludedTrials(5, 7, 1) = 1;
rsrExcludedTrials(5, 8, 1) = 1;
rsrExcludedTrials(5, 9, 1) = 1;
rsrExcludedTrials(5, 10, 1) = 1;

%Show plots
%layersComparison, layers, permutations, numLayersToSelect
%abTitle = 'A-B Best Fit to Pilot';
%abTitle = 'A-B Bayesian (a=b=1))';
abTitle = 'A-B Constant Across Tasks (a=b=0.5)';
plot_exam_data_sets_comparison(data, tasks, {'Pilot'; 'Final'}, abTitle, taskWeights,...
    matchedTrials, rsrExcludedTrials, probsComparison, rsrComparison, layersComparison,...
    layerComparisionMultipleTrials, layers, permutations, numLayers,...
    false, false, true,...
    false, true, 'final-pilot-comparison');