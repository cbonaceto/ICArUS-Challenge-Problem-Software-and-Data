function [metrics_data] = compute_exam_metrics(subject_data, tasks)
%COMPUTE_EXAM_METRICS Summary of this function goes here
%   Detailed explanation goes here

%% Compute average probs, average negentropy, and layer selection frequencies for the data set
numTasks = max(tasks); %Number of tasks
numSubjects = zeros(numTasks, 1); %Number of subjects for each task
groups = ['A' 'B' 'C' 'D'];
epsilon = 0.01;

subjectProbs = cell(numTasks, 1); %Subject probabilities for each stage (by task->trial->stage)
subjectProbsAvg = cell(numTasks, 1); %Average subject probabilities for each stage
allocationsAvg = cell(numTasks, 1); %Average troop allocations for each trial
normativeProbsAvg = cell(numTasks, 1); %Average normative probabilities for each stage

subjectNe = cell(numTasks, 1); %Negentropy of subject probabilities for each stage
subjectNeAvg = cell(numTasks, 1); %Negentropy of average subject probabilities for each stage
subjectNeErr = cell(numTasks, 1); %STD of subject negentropy
normativeNe = cell(numTasks, 1); %Negentropy of normative probabilities for each stage
normativeNeAvg = cell(numTasks, 1); %Negentropy of average normative probabilities for each stage
normativeNeErr = cell(numTasks, 1); %STD of normative negentropy (should be 0 for all Tasks except Task 6)

fh_by_subject = cell(numTasks, 1); %Frequency with which troops allocated against the group with the highest p for each subject across all trials
ph_by_subject = cell(numTasks, 1); %Avg prob of the group with the highest p for each subject across all trials
fh_ph_by_subject = cell(numTasks, 1); %Fh - Ph for each subject across all trials
fh_ph_avg = zeros(numTasks, 1); %Avg Fh - Ph across all subjects and trials
fh_ph_err = zeros(numTasks, 1); %STD of Fh - Ph
fh_1_by_subject = cell(numTasks, 1); %Fh - 1 for each subject across all trials
fh_1_avg = zeros(numTasks, 1); %Avg Fh - 1 across all subjects and trials
fh_1_err = zeros(numTasks, 1); %STD of Fh - Ph

rms_f_p_by_subject = cell(numTasks, 1); %RMS difference between troop allocations and probs for each subject at each trial
rms_f_p_avg = cell(numTasks, 1); %RMS difference between average troop allocation and average subject probs across all subjects and trials
rms_f_p_err = cell(numTasks, 1);
rms_f_i_by_subject = cell(numTasks, 1); %RMS difference between troop allocations and I distribution for each subject at each trial
rms_f_i_avg = cell(numTasks, 1); %RMS difference between average troop allocation I distriubution (created using average probs) across all subjects and trials
rms_f_i_err = cell(numTasks, 1);

c_by_trial = cell(numTasks, 1); %Percent of time subjects selected SIGINT on group with the highest prob for each trial
c_by_subject = cell(numTasks, 1); %Percent of time each subject selected SIGINT on group with highest prob across all trials 
c_avg = zeros(numTasks, 1); %Avg. percent of time subjects selected SIGINT on group with highest prob across all trials
c_err = zeros(numTasks, 1); %STD of time subjects selected SIGINT on group with highest prob across all trials

layerFrequenciesByTrial = cell(numTasks, 1); %Layer selection frequencies for each trial (Task 6 only)
layerFrequenciesAllTrials = cell(numTasks, 1); %Layer selection frequencies over all trials in a task (Task 6 only)

Spr = cell(numTasks, 1);
Spr_avg = zeros(numTasks, 1);
Spr_err = zeros(numTasks, 1);
%Spr_avg_all_tasks = 0;
%Spr_err_all_tasks = 0;

Spq = cell(numTasks, 1);
Spq_avg = zeros(numTasks, 1);
Spq_err = zeros(numTasks, 1);
%Spq_avg_all_tasks = 0;
%Spq_err_all_tasks = 0;

for task = tasks
    probs_subject = subject_data{task}.normalizedSubjectProbs;
    probs_normative = subject_data{task}.cumulativeBayesianProbs;
    troopAllocations = subject_data{task}.normalizedTroopAllocations;
    
    numSubjects(task) = size(probs_subject, 1);
    numTrials = size(probs_subject, 2);
    numStages = size(probs_subject{1,1}, 1);
    numProbs = size(probs_subject{1,1}, 2);
    
    if task == 6
        layerSelections = subject_data{task}.layerSelectionData.subjectLayerSelections;
        layerSelectionsByStage = subject_data{task}.layerSelectionData.subjectLayerSelectionsByStage;
        numPermutations = size(subject_data{task}.layerSelectionData.permutations, 1);
    else             
        layerSelections = [];
        Spr{task} = zeros(numTrials, numStages);
        Spq{task} = zeros(numTrials, numStages);
    end  
    
    subjectProbs{task} = cell(numTrials, numStages);
    subjectProbsAvg{task} = cell(numTrials, numStages);
    allocationsAvg{task} = zeros(numTrials, numProbs);
    normativeProbsAvg{task} = cell(numTrials, numStages);
    
    subjectNe{task} = cell(numTrials, numStages);
    subjectNeAvg{task} = zeros(numTrials, numStages);
    subjectNeErr{task} = zeros(numTrials, numStages);
    normativeNe{task} = cell(numTrials, numStages);
    normativeNeAvg{task} = zeros(numTrials, numStages);
    normativeNeErr{task} = zeros(numTrials, numStages);
    
    if task <= 3
        fh_by_subject{task} = zeros(numSubjects(task), 1);
        ph_by_subject{task} = zeros(numSubjects(task), 1);
        fh_ph_by_subject{task} = zeros(numSubjects(task), 1);
        fh_1_by_subject{task} = zeros(numSubjects(task), 1);
    end
    
    if task >=4 && task <=6
        rms_f_p_by_subject{task} = zeros(numSubjects(task), numTrials);
        rms_f_i_by_subject{task} = zeros(numSubjects(task), numTrials);
    end
    
    if task == 6 && ~isempty(layerSelections)
        layerFrequenciesByTrial{task} = zeros(numTrials, numPermutations);
        layerFrequenciesAllTrials{task} = zeros(1, numPermutations);
        
        sigintSelectionsByTrial = zeros(numTrials, 1);
        sigintSelectionsBySubject = zeros(numSubjects(task), 1);
        sigintHighestGroupSelectionsByTrial = zeros(numTrials, 1);
        sigintHighestGroupSelectionsBySubject = zeros(numSubjects(task), 1);
        c_by_trial{task} = zeros(numTrials, 1);
        c_by_subject{task} = zeros(numSubjects(task), 1);
    end
    
    for trial = 1:numTrials
        if task == 6 && ~isempty(layerSelections)            
            for subject = 1:numSubjects(task)
                %Aggregate layer frequencies
                permutation = layerSelections(subject, trial);
                layerFrequenciesByTrial{task}(trial, permutation) = ...
                    layerFrequenciesByTrial{task}(trial, permutation) + 1;
            end          
        end
        
        for stage = 1:numStages
            subjectProbs{task}{trial, stage} = zeros(numSubjects(task), numProbs);
            subjectNe{task}{trial, stage} = zeros(numSubjects(task), 1);
            normativeProbs = zeros(numSubjects(task), numProbs);
            normativeNe{task}{trial, stage} = zeros(numSubjects(task), 1);
            for subject = 1:numSubjects(task)
                %Aggregate probabilities and negentropies
                p_subj = probs_subject{subject, trial}(stage, :);
                subjectProbs{task}{trial, stage}(subject, :) = p_subj;
                p_subj = fillzerobins(p_subj, epsilon);
                subjectNe{task}{trial, stage}(subject) = negentropy(p_subj);
                p_norm = probs_normative{subject, trial}(stage, :);
                normativeProbs(subject, :) = p_norm;
                p_norm = fillzerobins(p_norm, epsilon);
                normativeNe{task}{trial, stage}(subject) = negentropy(p_norm);                
                
                if stage == numStages
                    %Aggregate troop allocations
                    allocations = troopAllocations{subject, trial};
                    allocationsAvg{task}(trial, :) = allocationsAvg{task}(trial, :) + allocations;                    
                    if task <= 3                        
                        %Aggregate probability matching data for Tasks 1-3
                        maxProb = max(p_subj);
                        ph_by_subject{task}(subject) = ph_by_subject{task}(subject) + maxProb;
                        maxProbIndices = find(p_subj == maxProb);
                        [~, maxAllocationIndex] = max(allocations);
                        if ~isempty(find(maxProbIndices == maxAllocationIndex, 1))
                            fh_by_subject{task}(subject) = fh_by_subject{task}(subject) + 1;
                        end
                    end                    
                    if task >= 4 && task <=6
                        %Aggregate probability matching data for Tasks 4-6
                        %Create a distribution I where 1 is assigned to the
                        %group or location with the highest subject probability
                        %TODO: IF 2 OR MORE GROUPS TIED, SHOULD WE MODIFY THE I DISTRIBUTION?
                        I = create_I_distribution(p_subj);
                        %Compute RMS between troop allocations and subject probabilities
                        rms_f_p_by_subject{task}(subject, trial) = rmse(allocations, p_subj);
                        %Compute RMS between troop allocations and I distribution
                        rms_f_i_by_subject{task}(subject, trial) = rmse(allocations, I);
                    end
                end
                
                if task == 6 && stage < numStages
                    %Aggregate confirmation bias (seeking) data
                    %Determine if SIGINT was selected                    
                    layerSelection = layerSelectionsByStage{subject, trial, stage};
                    if ~isempty(strfind(lower(layerSelection), 'sigint'))
                        sigintSelectionsByTrial(trial) = sigintSelectionsByTrial(trial) + 1;
                        sigintSelectionsBySubject(subject) = sigintSelectionsBySubject(subject) + 1;
                        %Determine if SIGINT was selected on one of the groups with the current highest subject probability
                        maxGroupProb = 0;
                        highestGroupSigint = '';
                        probs = zeros(1,numProbs);
                        p_subj = fillzerobins(probs_subject{subject, trial}(stage, :), epsilon);
                        for group = 1:numProbs                            
                            prob = p_subj(group);
                            %prob = probs_subject{subject, trial}(stage, group);
                            probs(group) = prob;
                            if prob > maxGroupProb
                                maxGroupProb = prob;
                            end
                        end
                        for group = 1:numProbs
                            prob = p_subj(group);                           
                            if prob >= maxGroupProb
                                highestGroupSigint = [highestGroupSigint ' SIGINT-' groups(group)]; %#ok<AGROW>
                            end
                        end                        
                        if ~isempty(strfind(highestGroupSigint, layerSelection))
                            %SIGINT was selected on the group with the current highest subject probability
                            sigintHighestGroupSelectionsByTrial(trial) = sigintHighestGroupSelectionsByTrial(trial) + 1;
                            sigintHighestGroupSelectionsBySubject(subject) = sigintHighestGroupSelectionsBySubject(subject) + 1;
                        end
                    end
                end
            end
            
            subjectProbsAvg{task}{trial, stage} = fillzerobins(sum(subjectProbs{task}{trial, stage})/numSubjects(task), epsilon);            
            subjectNeAvg{task}(trial, stage) = negentropy(subjectProbsAvg{task}{trial, stage});
            subjectNeErr{task}(trial, stage) = sqrt(sum((subjectNe{task}{trial, stage} - subjectNeAvg{task}(trial, stage)).^2 )/(numSubjects(task)-1));
            normativeProbsAvg{task}{trial, stage} = fillzerobins(sum(normativeProbs)/numSubjects(task), epsilon);
            normativeNeAvg{task}(trial, stage) = negentropy(normativeProbsAvg{task}{trial, stage});
            normativeNeErr{task}(trial, stage) = sqrt(sum((normativeNe{task}{trial, stage} - normativeNeAvg{task}(trial, stage)).^2 )/(numSubjects(task)-1));
            
            %Aggregate Spr and Spq data using subjectProbsAvg and normativeProbsAvg
            Spr{task}(trial, stage) = similarity(subjectProbsAvg{task}{trial, stage}, .01);
            %Spr_err{task}(trial, stage) = blah;
            Spq{task}(trial, stage) = similarity(subjectProbsAvg{task}{trial, stage}, .01, ...
                normativeProbsAvg{task}{trial, stage});
            
            %c_by_trial{task}(trial) = ... 
             %   sigintHighestGroupSelectionsByTrial(:, trial)/sigintSelectionsBySubject(:, trial);
            %DEBUG CODE
            %for subject = 1:numSubjects(task)
            %disp([num2str(normativeNe{task}{trial, stage}(subject)), ', ', num2str(normativeNeAvg{task}(trial, stage))]);
            %disp(num2str(normativeNe{task}{trial, stage}(subject) == normativeNeAvg{task}(trial, stage)));
            %end
            %END DEBUG CODE
        end
    end
    
    allocationsAvg{task} = allocationsAvg{task} ./ numSubjects(task);    
    
    %Aggregate Spr_avg and Spq_avg
    if task < 6
        if task == 1
            trials = [1 2 3 4 5 6 7 8 10];
        else
            trials = 1:numTrials;
        end
        if task == 5
            stages = 2:numStages;
        else 
            stages = 1:numStages;
        end        
        Spr_vec = reshape(Spr{task}(trials, stages).', [], 1);
        Spq_vec = reshape(Spq{task}(trials, stages).', [], 1);        
        Spr_avg(task) = mean(Spr_vec);
        Spr_err(task) = std(Spr_vec);
        Spq_avg(task) = mean(Spq_vec);
        Spq_err(task) = std(Spq_vec);
    end
    
    if task <= 3 
        %Aggregate probability matching data for Tasks 1-3 across all subjects
        fh_by_subject{task} = fh_by_subject{task}/numTrials;
        ph_by_subject{task} = ph_by_subject{task}/numTrials;
        fh_ph_by_subject{task} = fh_by_subject{task} - ph_by_subject{task};
        fh_1_by_subject{task} = fh_by_subject{task} - 1;
        fh_ph_avg(task) = abs(mean(fh_ph_by_subject{task}));
        fh_ph_err(task) = std(fh_ph_by_subject{task});
        fh_1_avg(task) = abs(mean(fh_1_by_subject{task}));
        fh_1_err(task) = std(fh_1_by_subject{task});
    end
    
    if task >=4 && task <= 6
        %Aggregate probability matching data for Tasks 4-6 across all
        %subjects at each trial (based on avg subject probs and avg
        %allocations)
        %allocationsAvg{task}
        rms_f_p_avg{task} = zeros(numTrials, 1);
        rms_f_i_avg{task} = zeros(numTrials, 1);
        rms_f_p_err{task} = zeros(numTrials, 1);
        rms_f_i_err{task} = zeros(numTrials, 1);
        for trial = 1:numTrials
            probs = subjectProbsAvg{task}{trial, numStages};
            allocations = allocationsAvg{task}(trial, :);
            
            %Create a distribution I where 1 is assigned to the
            %group or location with the highest subject probability
            I = create_I_distribution(probs);
            %Compute RMS between troop allocations and subject probabilities
            rms_f_p_avg{task}(trial) = rmse(allocations, probs);
            %Compute RMS between troop allocations and I distribution
            rms_f_i_avg{task}(trial) = rmse(allocations, I);
            
            %Compute standard deviations
            rms_f_p_err{task}(trial) = ...
                sqrt(sum((rms_f_p_by_subject{task}(:, trial) - ...
                rms_f_p_avg{task}(trial)).^2 )/(numSubjects(task)-1));
            rms_f_i_err{task}(trial) = ...
                sqrt(sum((rms_f_i_by_subject{task}(:, trial) - ...
                rms_f_i_avg{task}(trial)).^2 )/(numSubjects(task)-1));
        end
        %rms_f_p_avg{task} = sum(rms_f_p_by_subject{task})/size(rms_f_p_by_subject{task}, 1);
        %rms_f_p_err{task} = std(rms_f_p_by_subject{task});
        %rms_f_i_avg{task} = sum(rms_f_i_by_subject{task})/size(rms_f_i_by_subject{task}, 1);
        %rms_f_i_err{task} = std(rms_f_i_by_subject{task});        
    end
    
    if task == 6 && ~isempty(layerSelections)
        %Aggregate layer frequencies across all trials
        layerFrequenciesAllTrials{task} = sum(layerFrequenciesByTrial{task});
        
        %Aggregate confirmation bias (seeking) data across all trials and subjects
        c_by_trial{task} = sigintHighestGroupSelectionsByTrial ./ sigintSelectionsByTrial * 100;
        c_by_subject{task} = sigintHighestGroupSelectionsBySubject ./ sigintSelectionsBySubject * 100;        
        indices = ~isnan(c_by_subject{task});
        sum(sigintHighestGroupSelectionsByTrial)
        sum(sigintSelectionsByTrial)        
        c_avg(task) = sum(sigintHighestGroupSelectionsByTrial) / sum(sigintSelectionsByTrial) * 100;
        c_err(task) = std(c_by_subject{task}(indices));
    end
end

%Aggregate Spr_avg and Spq_avg across all tasks
for task = tasks    
   %TODO: Add this
end

metrics_data.numSubjects = numSubjects;
metrics_data.subjectProbs = subjectProbs;
metrics_data.subjectProbsAvg = subjectProbsAvg;
metrics_data.allocationsAvg = allocationsAvg;
metrics_data.normativeProbsAvg = normativeProbsAvg;
metrics_data.subjectNe = subjectNe;
metrics_data.subjectNeAvg = subjectNeAvg;
metrics_data.subjectNeErr = subjectNeErr;
metrics_data.normativeNe = normativeNe;
metrics_data.normativeNeAvg = normativeNeAvg;
metrics_data.normativeNeErr = normativeNeErr;

metrics_data.fh_by_subject = fh_by_subject;
metrics_data.ph_by_subject = ph_by_subject;
metrics_data.fh_ph_by_subject = fh_ph_by_subject;
metrics_data.fh_ph_avg = fh_ph_avg;
metrics_data.fh_ph_err = fh_ph_err;
metrics_data.fh_1_by_subject = fh_1_by_subject;
metrics_data.fh_1_avg = fh_1_avg;
metrics_data.fh_1_err = fh_1_err;

metrics_data.rms_f_p_by_subject = rms_f_p_by_subject;
metrics_data.rms_f_p_avg = rms_f_p_avg;
metrics_data.rms_f_p_err = rms_f_p_err;
metrics_data.rms_f_i_by_subject = rms_f_i_by_subject;
metrics_data.rms_f_i_avg = rms_f_i_avg;
metrics_data.rms_f_i_err = rms_f_i_err;

metrics_data.c_by_trial = c_by_trial;
metrics_data.c_by_subject = c_by_subject;
metrics_data.c_avg = c_avg;
metrics_data.c_err = c_err;

metrics_data.layerFrequenciesByTrial = layerFrequenciesByTrial;
metrics_data.layerFrequenciesAllTrials = layerFrequenciesAllTrials;

metrics_data.Spr = Spr;
metrics_data.Spr_avg = Spr_avg;
metrics_data.Spr_err = Spr_err;
%Spr_avg_all_tasks = 0;
%Spr_err_all_tasks = 0;
metrics_data.Spq = Spq;
metrics_data.Spq_avg = Spq_avg;
metrics_data.Spq_err = Spq_err;
%metrics_data.Spq_avg_all_tasks = 0;
%Spq_err_all_tasks = 0;

end