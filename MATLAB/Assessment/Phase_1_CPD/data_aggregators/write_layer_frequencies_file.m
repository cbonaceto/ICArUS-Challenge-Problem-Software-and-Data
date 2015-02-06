function write_layer_frequencies_file(subjects, layers, permutations, numLayersToSelect, subjectLayerSelections,...
    separator, writeHeader)
%WRITE_LAYER_FREQUENCIES_FILE Writes a CSV files with the aggregate layer frequencies
%for each subject over all trials.
%   Detailed explanation goes here

%% Aggregate layer selection permutation data by subject
numSubjects = size(subjectLayerSelections, 1);
numTrials = size(subjectLayerSelections, 2);
numPermutations = size(permutations, 1);
permutationCount = zeros(numSubjects, size(permutations, 1));
for subject = 1:numSubjects
    for trial = 1:numTrials
        permutation = subjectLayerSelections(subject, trial);
        permutationCount(subject, permutation) = permutationCount(subject, permutation) + 1;
    end  
end
permutationPercent = zeros(numSubjects, size(permutations, 1));
for subject = 1:numSubjects
    permutationPercent(subject,:) = permutationCount(subject,:) ./ sum(permutationCount(subject,:)) * 100;
end

%% Write results to CSV file
fileName = 'Layer_Selection_Frequencies.csv';
if ~exist('separator', 'var') || isempty(separator)
   separator = ',';
end
if ~exist('writeHeader', 'var')
    writeHeader = false;
end
fid = fopen(fileName, 'wt');
assert(fid ~= -1, ['Cannot open file: ' fileName]);

%Write header
if writeHeader
    fprintf(fid, '%s%s', 'subject', separator);
    
    %Create list of all layer selection permutations names
    permutationNames = cell(1, numPermutations);
    for permutationIndex = 1:numPermutations
        permutationNames{permutationIndex} = createPermutationName(layers, numLayersToSelect, permutations(permutationIndex,:));
    end
    
    for i = 1:length(permutationNames)-1
        fprintf(fid, '%s%s', permutationNames{i}, separator);
    end
    fprintf(fid, '%s\n', permutationNames{i+1});
end

%Write results
for subject = 1:numSubjects
    fprintf(fid, '%s%s', subjects{subject}, separator);    
    fprintf(fid, '%s\n', num2separatedStr(permutationCount(subject, :), separator));
end
fclose(fid);

end

function name = createPermutationName(layers, numLayersToSelect, permutation)
for columnIndex = 1:numLayersToSelect
    if columnIndex > 1
        name = strcat(name, '-', layers(permutation(columnIndex)));
    else
        name = layers(permutation(columnIndex));
    end
end
name = char(name);
end