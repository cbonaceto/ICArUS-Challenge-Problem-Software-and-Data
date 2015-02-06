function probabilities = read_probs_file(fileName, ignoreNe)
%READ_PROBS_FILE Parses a CSV file containing probabilities or likelihoods
%for each stage of each trial in a task.
%   Parses a CSV file containing columns for the trial number, stage
%   number, and then a column for each probability (e.g., 1-4, A-D). Returns
%   the results as a cell array of matrices, where each row in the cell
%   array contains the matrix of probabilities for a trial.

if ~exist('ignoreNe', 'var')
    ignoreNe = false;
end

%Read the CSV file
data = csvread(fileName);

%Determine the number of probabilities
if ignoreNe
    offset = 3;
else
    offset = 2;
end
numProbs = size(data, 2) - offset;
assert(numProbs > 0, 'Error, missing probabilities');

%Determine the number of trials and stages
numRows = size(data, 1);
numTrials = data(numRows, 1);
numStages = 1;
currTrial = data(1, 1);
row = 2;
while data(row, 1) == currTrial & row <= numRows %#ok<*AND2>
    numStages = numStages + 1;
    row = row + 1;
end
assert(numTrials > 0, 'Error, missing trials');
%numTrials, numStages, numProbs

%Get the probabilities 
probabilities = cell(numTrials, 1);
row = 1;
for trial = 1:numTrials
    probs = zeros(numStages, numProbs);
    for stage = 1:numStages        
        probs(stage,:) = data(row, offset+1:numProbs+offset);        
        row = row + 1;
    end
    probabilities{trial} = probs;
end
end