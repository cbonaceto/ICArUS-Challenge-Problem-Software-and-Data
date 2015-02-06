function probabilities = read_all_probs_file(fileName, ignoreNe)
%READ_ALL_PROBS_FILE Parses a CSV file containing probabilities or likelihoods
%for each stage of each trial in a task for all subjects.
%   Parses a CSV file containing columns for the subject number, trial number, stage
%   number, and then a column for each probability (e.g., 1-4, A-D). Returns
%   the results as a 2-D cell array of matrices, where each row/column in the cell
%   array contains the matrix of probabilities for a subject and trial, For
%   example: probabilities{10, 1} would get the probabilities for subject 10
%   on trial 1.

if ~exist('ignoreNe', 'var')
    ignoreNe = false;
end

%Read the CSV file
data = csvread(fileName);

%Determine the number of probabilities
if ignoreNe
    offset = 4;
else
    offset = 3;
end
numProbs = size(data, 2) - offset;
assert(numProbs > 0, 'Error, missing probabilities');

%Determine the number of trials and stages
numRows = size(data, 1);
numTrials = data(numRows, 2);
numStages = 1;
currTrial = data(1, 2);
row = 2;
while data(row, 2) == currTrial & row <= numRows %#ok<*AND2>
    numStages = numStages + 1;
    row = row + 1;
end
numSubjects = numRows / (numTrials * numStages);
assert(numTrials > 0, 'Error, missing trials');
%numSubjects, numTrials, numStages, numProbs

%Get the probabilities
probabilities = cell(numSubjects, numTrials);
row = 1;
for subject = 1:numSubjects    
    for trial = 1:numTrials
        probs = zeros(numStages, numProbs);
        for stage = 1:numStages
            probs(stage, :) = data(row, offset+1:numProbs+offset);
            row = row + 1;
        end
        probabilities{subject, trial} = probs;
    end
end
end