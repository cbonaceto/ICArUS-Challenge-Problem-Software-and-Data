function output_individual_difference_metrics(subjects, inferencingScore,...
    decisionMakingScore, foragingScore, dataFolder)
%OUTPUT_INDIVIDUAL_DIFFERENCE_METRICS Summary of this function goes here
%   Detailed explanation goes here

%% Output subject score data to a CSV file
separator = ',';
fileName = [dataFolder, '\', 'individual_differences_metrics.csv'];
fid = fopen(fileName, 'wt');
assert(fid ~= -1, ['Cannot open file: ' fileName]);

%Write the header
fprintf(fid, '%s%s%s%s%s%s%s%s\n', 'Subject', separator, 'Inferencing Score', separator,...
    'Decision Making Score', separator, 'Foraging Score', separator);

%Write the scores
numSubjects = length(subjects);
for subject = 1:numSubjects
    fprintf(fid, '%s%s%s\n', subjects{subject}, separator,...
        num2separatedStr([inferencingScore(subject),...
        decisionMakingScore(subject), foragingScore(subject)], separator));
end
fclose(fid);
end