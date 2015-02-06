function write_avg_probabilities_file(subjectProbs, probTitles, taskNum, numStages, separator, writeHeader)
%WRITE_AVG_RESULTS_FILE Write the average subject probabilities for each
%stage of each trial in a task
%   Detailed explanation goes here

%% Compute average subject probability across all subjects for each trial stage and each group
numSubjects = size(subjectProbs, 1);
numTrials = size(subjectProbs, 2);
subjectProbsAvgByStage = cell(numTrials, 1);
epsilon = 0.01;
for subject = 1:numSubjects
    for trial = 1:numTrials
        if subject == 1
            subjectProbsAvgByStage{trial} = ...
                subjectProbs{subject, trial}(1:numStages,:);
        else
            subjectProbsAvgByStage{trial} = ...
                subjectProbsAvgByStage{trial} + subjectProbs{subject, trial}(1:numStages,:);
        end        
    end
end
for trial = 1:numTrials
    subjectProbsAvgByStage{trial} = subjectProbsAvgByStage{trial} / numSubjects;
    for stage = 1:numStages
        fillzerobins(subjectProbsAvgByStage{trial}(stage,:), epsilon);
    end
end
%%%%

%% Write results to CSV file
fileName = ['Task_' num2str(taskNum) '_Avg_Subject_Probabilities.csv'];
if ~exist('separator', 'var')
   separator = ',';
end
if ~exist('writeHeader', 'var')
    writeHeader = false;
end
fid = fopen(fileName, 'wt');
assert(fid ~= -1, ['Cannot open file: ' fileName]);

%Write header
if writeHeader
    fprintf(fid, '%s%s%s%s', 'trial', separator, 'stage', separator);
    for i = 1:length(probTitles)-1
        fprintf(fid, '%s%s', probTitles{i}, separator);
    end
    fprintf(fid, '%s\n', probTitles{i+1});
end

%Write results
for trial = 1:numTrials
    for stage = 1:numStages        
        fprintf(fid, '%s\n',...
            num2separatedStr([trial stage subjectProbsAvgByStage{trial}(stage,:)], separator));                
    end
end
fclose(fid);

end