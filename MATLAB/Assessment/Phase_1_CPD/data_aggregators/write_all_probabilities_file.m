function write_all_probabilities_file(subjects, subjectProbs, probTitles, taskNum, numStages, separator, writeHeader)
%WRITE_ALL_PROBABILITIES_FILE Write the the individual subject probabilities for each
%stage of each trial in a task

%% Write results to CSV file
fileName = ['Task_' num2str(taskNum) '_All_Subject_Probabilities.csv'];
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
    fprintf(fid, '%s%s%s%s%s%s', 'subject', separator, 'trial', separator, 'stage', separator);
    for i = 1:length(probTitles)-1
        fprintf(fid, '%s%s', probTitles{i}, separator);
    end
    fprintf(fid, '%s\n', probTitles{i+1});
end

%Write results
numSubjects = size(subjectProbs,1);
numTrials = size(subjectProbs,2);
epsilon = 0.01;
for subject = 1:numSubjects
    %subjects{subject}
    for trial = 1:numTrials
        for stage = 1:numStages            
            %fprintf(fid, '%s%s', subjects{subject}, separator);
            %fprintf(fid, '%s%s', num2str(subject), separator);
            fprintf(fid, '%s%s', subjects{subject}, separator);
            fprintf(fid, '%s\n',...
                num2separatedStr([trial stage fillzerobins(subjectProbs{subject, trial}(stage,:), epsilon)], separator));
        end
    end
end
fclose(fid);

end