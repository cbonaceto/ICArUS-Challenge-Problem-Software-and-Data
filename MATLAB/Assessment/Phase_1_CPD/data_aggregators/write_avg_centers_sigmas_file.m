function write_avg_centers_sigmas_file(subjectGroupCenters, taskNum, separator, writeHeader)
%WRITE_AVG_CENTERS_SIGMAS_FILE Summary of this function goes here
%   Detailed explanation goes here

%% Compute average subject centers and sigmas across all subjects for each trial and each group
numSubjects = size(subjectGroupCenters,1);
numTrials = size(subjectGroupCenters,2);
numGroups = size(subjectGroupCenters, 3);
avgX = zeros(numTrials, numGroups);
avgY = zeros(numTrials, numGroups);
if taskNum == 2
    avgSigma = zeros(numTrials, numGroups);
end
for trial = 1:numTrials
    for group = 1:numGroups        
        for subject = 1:numSubjects
             subjectData = subjectGroupCenters{subject, trial, group};
             avgX(trial, group) = avgX(trial, group) + subjectData.x;
             avgY(trial, group) = avgY(trial, group) + subjectData.y;
             %subjectData.y
             if taskNum == 2
                avgSigma(trial, group) = avgSigma(trial, group) + subjectData.radius/1.33;
             end
        end
    end
end
for trial = 1:numTrials
    for group = 1:numGroups
        avgX(trial, group) = avgX(trial, group)/numSubjects;
        avgY(trial, group) = avgY(trial, group)/numSubjects;        
        if taskNum == 2
            avgSigma(trial, group) = avgSigma(trial, group)/numSubjects;
        end
    end
end
%%%%

%% Write results to CSV file
fileName = ['Task_' num2str(taskNum) '_Avg_Subject_Centers_Sigmas.csv'];
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
    if taskNum == 2        
        fprintf(fid, '%s%s%s%s%s%s%s%s%s\n', 'trial', separator, 'group', separator,...
            'centerX', separator, 'centerY', separator, 'sigma');
    else        
        fprintf(fid, '%s%s%s%s%s%s%s\n', 'trial', separator, 'group', separator,...
            'centerX', separator, 'centerY');
    end        
end

%Write results
for trial = 1:numTrials
    for group = 1:numGroups
        if taskNum == 2            
            fprintf(fid, '%s\n',...
                num2separatedStr([trial group avgX(trial, group) avgY(trial, group) avgSigma(trial, group)], separator));
        else
            fprintf(fid, '%s\n',...
                num2separatedStr([trial group avgX(trial, group) avgY(trial, group)], separator));
        end
    end
end
fclose(fid);

end
