function write_surprise_file(surpriseData, layerSurpriseData, subjectProbs, groundTruthData,...
    probTitles, taskNum, numStages, separator, writeHeader)
%WRITE_SURPRISE_FILE Write the subject surprise information for all
%subjects after observing ground truth and after observing an INT layer
%presentation.
%   Detailed explanation goes here

if ~exist('separator', 'var')
   separator = ',';
end
if ~exist('writeHeader', 'var')
    writeHeader = false;
end

numSubjects = size(subjectProbs,1);
numTrials = size(subjectProbs,2);

%% Write ground truth surprise file 
fileName = ['Task_' num2str(taskNum) '_All_Subject_Ground_Truth_Surprise.csv'];
fid = fopen(fileName, 'wt');
assert(fid ~= -1, ['Cannot open file: ' fileName]);

%Write header (subject, trial, surprise, ground_truth, P1, P2, P3, P4)
if writeHeader
    fprintf(fid, '%s%s%s%s%s%s%s%s', 'subject', separator, 'trial', separator, 'surprise',...
        separator, 'ground_truth', separator);
    for i = 1:length(probTitles)-1
        fprintf(fid, '%s%s', probTitles{i}, separator);
    end
    fprintf(fid, '%s\n', probTitles{i+1});
end

%Write results
for subject = 1:numSubjects
    for trial = 1:numTrials
        fprintf(fid, '%s%s', num2str(subject), separator);
        fprintf(fid, '%s\n',...
            num2separatedStr([trial surpriseData(subject, trial) groundTruthData(subject, trial)...
            subjectProbs{subject, trial}(numStages,:)], separator));
    end
end
fclose(fid);
%%%%%%%%%%%%%%%

%% Write layer surprise file
if exist('layerSurpriseData', 'var') && ~isempty(layerSurpriseData) && numStages > 1
    fileName = ['Task_' num2str(taskNum) '_All_Subject_Layer_Surprise.csv'];
    fid = fopen(fileName, 'wt');
    assert(fid ~= -1, ['Cannot open file: ' fileName]);
    
    %Write header (subject, trial, surprise, ground_truth, P1_before, P2_before, P3_before, P4_before,
    %P1_after, P2_after, P3_after, P4_after)
    if writeHeader
        fprintf(fid, '%s%s%s%s%s%s%s%s', 'subject', separator, 'trial', separator, 'surprise',...
            separator, 'ground_truth', separator);
        for i = 1:length(probTitles)
            fprintf(fid, '%s%s', [probTitles{i} '_beforeINT'], separator);
        end
        for i = 1:length(probTitles)-1
            fprintf(fid, '%s%s', [probTitles{i} '_afterINT'], separator);
        end
        fprintf(fid, '%s\n', [probTitles{i+1} '_afterINT']);
    end
    
    %Write results
    for subject = 1:numSubjects
        for trial = 1:numTrials
            for stage = 2:numStages
                fprintf(fid, '%s%s', num2str(subject), separator);
                fprintf(fid, '%s\n',...
                    num2separatedStr([trial surpriseData(subject, trial) groundTruthData(subject, trial)...
                    subjectProbs{subject, trial}(stage-1,:) subjectProbs{subject, trial}(stage,:)],...
                    separator));
            end
        end
    end
    fclose(fid);
end
%%%%%%%%%%%%%%%

end