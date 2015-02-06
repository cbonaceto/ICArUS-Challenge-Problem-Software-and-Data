function [subjects, cumulativeBayesianProbs, normalizedSubjectProbs, rawSubjectProbs, normalizedTroopAllocations,...
    surpriseData, normativeGroupCenters, subjectGroupCenters, distances, layerSelectionData, bestLayerData,...
    layerSurpriseData, timeDataByTrial, groundTruthData] = aggregate_csv_task_data( task_csv_file, taskNum )
%AGGREGATE_CSV_TASK_DATA Summary of this function goes here
%   Detailed explanation goes here

%Load the CSV file containing task data a cell array
task_data = csv2cell(task_csv_file, 'fromfile');
assert(size(task_data,1)>1);

%% Determine the number of probabilities, probability entry stages, and layer selections (Task 6)
%  Also determine the starting columsn for data to aggregate
surpriseCol = 0; % All Tasks, ground truth surprise column
bayesianProbsCol = 0; % All Tasks, cumulative Bayesian probabilities start column
normalizedProbsCol = 0; % All Tasks, normalized subject probabilities start column
rawProbsCol = 0; % All Tasks, raw subject probabilities start column
normalizedAllocationsCol = 0; % All Tasks, normalized troop allocations start column
numProbs = 0; % All Tasks, number of probabilities in each stage
numProbEntryStages = 0; % All Tasks, number of probability entry stages
layersCol = 0; %Task 6, Layer_type start column
numLayers = 0; % Task 6, number of INT layers
layerSurpriseCol = 0; % Task 4, Layer_Surprise start column
layerBestIntCol = 0; %Task 6, Layer_Best_INT start column
layerBestSigintCol = 0; %Task 6 Layer_Best_SIGINT start column
subjectCentersCol = 0; % Tasks 2-3, Center start column
normativeCentersCol = 0; % Tasks 2-3, normative Center start column
distancesCol = 0; % Task 3, Subject_Distances start column
trialTimeCol = 0; % All Tasks, trial time column
groundTruthCol = 0; %All tasks, ground truth column
numGroups = 0; %All Tasks, number of groups
for col = 1:size(task_data, 2)
    str = task_data{1, col};    
    if surpriseCol == 0 && strncmp(str, 'Surprise', 8)
        surpriseCol = col;
    elseif normalizedProbsCol == 0 && strncmp(str, 'Human', 5)
        normalizedProbsCol = col;
    elseif rawProbsCol == 0 && strncmp(str, 'Raw_h', 5)
        rawProbsCol = col;
    elseif bayesianProbsCol == 0 && strncmp(str, 'Normative_p', 11)
        bayesianProbsCol = col;
    elseif normalizedAllocationsCol == 0 && strncmp(str, 'Norm_alloc', 10)
        normalizedAllocationsCol = col;
    elseif layersCol == 0 && strncmp(str, 'Layer_t', 7)
        layersCol = col;
    elseif layerBestIntCol == 0 && strncmp(str, 'Layer_Best_I', 12)
        layerBestIntCol = col;
    elseif layerBestSigintCol == 0 && strncmp(str, 'Layer_Best_S', 12)
        layerBestSigintCol = col;
    elseif layerSurpriseCol == 0 && strncmp(str, 'Layer_S', 7)
        layerSurpriseCol = col;
    elseif subjectCentersCol == 0 && strncmp(str, 'Center', 6)
        subjectCentersCol = col;
    elseif normativeCentersCol == 0 && strncmp(str, 'Normative_C', 11)
        normativeCentersCol = col;
    elseif distancesCol == 0 && strncmp(str, 'Subject_Dist', 12)
        distancesCol = col;
    elseif numGroups == 0 && strncmp(str, 'Number_gr', 8)
        num = str2double(task_data{2,col});
        if ~isnan(num)
            numGroups = num;
        end
    elseif trialTimeCol == 0 && strncmp(str, 'Trial_time', 11)
        trialTimeCol = col;
    elseif groundTruthCol == 0 && strncmp(str, 'Ground_T', 8)
        groundTruthCol = col;
    end
    
    if strncmp(str, 'Human', 5)
        if ~isnan(str2double(task_data{2,col}))
            numProbs = str2double(str(15));
            numProbEntryStages = str2double(str(13));
        end
    elseif strncmp(str, 'Layer_t', 7)
        if ~strncmp(task_data{2,col}, 'NaN', 3)
            numLayers = str2double(str(12));
        end
    end
end
%numProbs
%numProbEntryStages
assert(numProbs > 0 && numProbEntryStages > 0);
assert(normalizedProbsCol > 0)

%% Determine the number of subjects and trials
currSubject = task_data{2,2};
currSubjectNumTrials = 0;
numTrials = 0;
numSubjects = 1;
for row = 2:size(task_data,1)
    if ~strcmp(task_data{row, 2}, currSubject)
        %We're at the next subject
        numSubjects = numSubjects + 1;
        currSubject = task_data{row, 2};
        if numTrials == 0
            numTrials = currSubjectNumTrials;
        else
            assert(currSubjectNumTrials == numTrials);
        end
        currSubjectNumTrials = 1;
    else
        currSubjectNumTrials = currSubjectNumTrials + 1;
    end
end
if numTrials == 0
    numTrials = currSubjectNumTrials;
end
%numTrials
%numSubjects
assert(numTrials > 0);

%% Aggregate data
% Currently looking at probability data (All Tasks); troop allocation data (All Tasks); 
% base rate data (Task 2); group circles and center data (Tasks 2-3); 
% layer selection data (Task 6); surprise data (All Tasks);
% best SIGINT data (Task 6); trial time data (All Tasks)

%Epsilon is the amount to adjust probabilities that are < epsilon or > 100-epsilon. The default is 0.01 (1%).
%epsilon = 0.01; 
epsilon = 0; %Set to 0 for now. Need to adjust when computing average probs
%epsilon = 1e-3; %1e-300; 

normalizedSubjectProbs = cell(numSubjects, numTrials);
rawSubjectProbs = cell(numSubjects, numTrials);
cumulativeBayesianProbs = cell(numSubjects, numTrials);
normalizedTroopAllocations = cell(numSubjects, numTrials);
surpriseData = zeros(numSubjects, numTrials);
if taskNum == 2 || taskNum == 3
    subjectGroupCenters = cell(numSubjects, numTrials, numGroups);
    normativeGroupCenters = cell(numSubjects, numTrials, numGroups);
    if taskNum == 3 && distancesCol > 0
        distances = zeros(numSubjects, numTrials, numGroups);
    else
        distances = [];
    end
else 
    subjectGroupCenters = [];
    normativeGroupCenters = [];
    distances = [];
end
if taskNum == 4
    layerSurpriseData = zeros(numSubjects, numTrials, numLayers);
else 
    layerSurpriseData = [];
end
if taskNum == 6 && numLayers > 0
    layerSelectionData.subjectLayerSelections = zeros(numSubjects, numTrials);
    layerSelectionData.subjectLayerSelectionsByStage = cell(numSubjects, numTrials, numLayers);    
    layerSelectionData.layers = {'IMINT' 'MOVINT' 'SIGINT' 'SOCINT'};
    %xmlLayerNames = {'ImintLayer' 'MovintLayer' 'SigintLayer' 'SocintLayer'};
    xmlLayerNames = {'imint' 'movint' 'sigint' 'socint'};
    layerSelectionData.permutations = perms(length(xmlLayerNames):-1:1);
    bestLayerData = cell(numSubjects, numTrials, numLayers);
else
    layerSelectionData = [];
    bestLayerData = [];
end
timeDataByTrial = zeros(numSubjects, numTrials);
groundTruthData = zeros(numSubjects, numTrials);
subjects = cell(numSubjects, 1);
currSubject = task_data{2,2};
subjects{1} = currSubject;
subjectNum = 1;
trialNum = 0;
for row = 2:size(task_data,1)
    if ~strcmp(task_data{row, 2}, currSubject)
        %We're at the next subject, reset trial counter and increment
        %subject number
        subjectNum = subjectNum + 1;
        currSubject = task_data{row, 2};
        subjects{subjectNum} = currSubject;
        trialNum = 1;
    else
        trialNum = trialNum + 1;
    end
    
    %Get the timing data for the trial
    if trialTimeCol > 0
        timeDataByTrial(subjectNum, trialNum) = str2double(task_data(row, trialTimeCol));
    end    
    
    %Get the ground truth data for the trial (Groups A-D map to numbers
    %1-4, Locations 1-4 map to numbers 1-4)
    if groundTruthCol > 0
        groundTruthData(subjectNum, trialNum) = groundtruth2double(task_data{row, groundTruthCol});        
    end

    %Get the ground truth surprise data for the trial   
    if surpriseCol > 0
        surpriseData(subjectNum, trialNum) = str2double(task_data(row, surpriseCol));
    end
    
    %Get the probability data for the trial
    currNormalizedProbs = zerodata(numProbEntryStages, numProbs);
    currRawProbs = zerodata(numProbEntryStages, numProbs);
    currBayesianProbs = zerodata(numProbEntryStages, numProbs);
    for probSet = 1:numProbEntryStages
        %Get the normalized subject probs for each trial stage
        if normalizedProbsCol > 0
            currNormalizedProbs(probSet,:) = ...
                fillzerobins(str2double(task_data(row, ...
                normalizedProbsCol+(probSet-1)*numProbs:normalizedProbsCol+probSet*numProbs-1)) .* .01, epsilon);
        end
        
        %Get the raw subject probs for each trial stage
        if rawProbsCol > 0
            currRawProbs(probSet,:) = ...
                fillzerobins(str2double(task_data(row, ...
                rawProbsCol+(probSet-1)*numProbs:rawProbsCol+probSet*numProbs-1)) .* .01, epsilon);               
        end
        
        %Get the cumulative bayesian probs for each trial stage
        if bayesianProbsCol > 0
            currBayesianProbs(probSet,:) = ...
                fillzerobins(str2double(task_data(row, ...
                bayesianProbsCol+(probSet-1)*numProbs:bayesianProbsCol+probSet*numProbs-1)) .* .01, epsilon);
        end
    end    
    normalizedSubjectProbs{subjectNum, trialNum} = currNormalizedProbs;
    rawSubjectProbs{subjectNum, trialNum} = currRawProbs;
    cumulativeBayesianProbs{subjectNum, trialNum} = currBayesianProbs;
    
    %Get the troop allocation data for the trial
    if normalizedAllocationsCol > 0
        normalizedTroopAllocations{subjectNum, trialNum} = str2double(task_data(row, ...
            normalizedAllocationsCol:normalizedAllocationsCol+numProbs-1)) .* .01;
        %normalizedTroopAllocations{subjectNum, trialNum}
    end
    
    %Get the group circle/center and base rate data for the trial (Tasks 2-3 only)
    if taskNum == 2 || taskNum == 3
        if subjectCentersCol > 0
            for group = 0:numGroups-1
                subjectGroupCenters{subjectNum, trialNum, group+1}.x = str2double(task_data(row, subjectCentersCol + group));
                subjectGroupCenters{subjectNum, trialNum, group+1}.y = str2double(task_data(row, subjectCentersCol + group + numGroups));
                if taskNum == 2
                    subjectGroupCenters{subjectNum, trialNum, group+1}.radius = str2double(task_data(row, subjectCentersCol + group + (numGroups*2)));
                end
            end
        end
        if normativeCentersCol > 0
            for group = 0:numGroups-1
                normativeGroupCenters{subjectNum, trialNum, group+1}.x = str2double(task_data(row, normativeCentersCol + group));
                normativeGroupCenters{subjectNum, trialNum, group+1}.y = str2double(task_data(row, normativeCentersCol + group + numGroups));
                normativeGroupCenters{subjectNum, trialNum, group+1}.base_rate = str2double(task_data(row, normativeCentersCol + group + (numGroups*2)));
                if taskNum == 2                    
                    normativeGroupCenters{subjectNum, trialNum, group+1}.sigmaX = str2double(task_data(row, normativeCentersCol + group + (numGroups*3)));
                    normativeGroupCenters{subjectNum, trialNum, group+1}.sigmaY = str2double(task_data(row, normativeCentersCol + group + (numGroups*4)));
                end
            end
        end
    end
    
    %Get the distances data (Task 3 only, used in base rate negelect calculations)
    if taskNum == 3 && distancesCol > 0
        for group = 1:numGroups
            distances(subjectNum, trialNum, group) = str2double(task_data(row, distancesCol + group - 1));
        end
    end
    
    %Get the layer surprise data (Task 4 only)
    if taskNum == 4 && layerSurpriseCol > 0 && numLayers > 0
        for layer = 1:numLayers
            layerSurpriseData(subjectNum, trialNum, layer) = str2double(task_data(row, layerSurpriseCol + layer - 1));
        end
    end
    
    %Get the layer selection data, best INT data, and best SIGINT data for the trial (Task 6 only)       
    if taskNum == 6 && layersCol > 0 && numLayers > 0
        layerSelections = task_data(row, layersCol:layersCol+numLayers-1);      
        layerSelectionData.subjectLayerSelections(subjectNum, trialNum) = ...
            getLayerSelectionIndex(layerSelectionData.permutations, layerSelections, xmlLayerNames);
        %DEBUG CODE
        %disp(strcat('subject: ', num2str(subjectNum), ', trial:', num2str(trialNum), ', path: ',...
        %    layerSelections{1}, '-', layerSelections{2}, '-', layerSelections{3}, ', path index: ',...
        %    num2str(layerSelectionData.subjectLayerSelections(subjectNum, trialNum))));
        %END DEBUG CODE
        
        %Get the Best INT and Best SIGINT data for each stage of the trial (Task 6 only)
        for layer = 1:numLayers
            layerSelectionData.subjectLayerSelectionsByStage{subjectNum, trialNum, layer} = layerSelections{layer};
            if(layerBestIntCol > 0)
                bestLayerData{subjectNum, trialNum, layer}.bestInt = task_data(row, layerBestIntCol + layer - 1);
                bestLayerData{subjectNum, trialNum, layer}.bestSigint = task_data(row, layerBestSigintCol + layer - 1);
            end
        end
    end       
end
end

% Given a ground truth group (A-D) or location (1-4), return the number
% corresponsing to the group or location (e.g., A=1, 1=1)
function groundTruthIndex = groundtruth2double(groundTruth)
groundTruthIndex = 0;
switch lower(groundTruth)
    case {'a', '1'}
        groundTruthIndex = 1;
    case {'b', '2'}
        groundTruthIndex = 2;
    case {'c', '3'}
        groundTruthIndex = 3;
    case {'d', '4'}
        groundTruthIndex = 4;
end
end