% loadtestdesign

% load data/examfile_Pilot2_3_Matt.mat
load data/corrected_set_19_20.mat

% test doesn't say how many facilities have been shown yet, so
% we need this look-up table
numfacilitiesintroduced = [2 2 2 2 2 2 ...
                           3 3 3 3 3 3 ...
                           4 4 4 4 4 4 ...
                           4 4 4 4];
phasetype = [1 2 1 2 1 2 ...
             1 2 1 2 1 2 ...
             1 2 1 2 1 2 ...
             2 2 3 3];

trainingtestblocks = 2:2:18;
testblocks = [19 20];
assessment1block = 21;
assessment2block = 22;

targetanswers = 'ABCD';

max_probsets = 3;
max_probs_in_set = 4;

%% Get scenes in reused queries
offset = 8; % specific to pilot study design
testscenes = [];
for block = 1:length(testblocks)
    test = examfile.Set(testblocks(block)).Test;
    for trial = 1:length(test.QuestionType)
        scnum = test.QuestionType{trial}{end};
        if (testblocks(block) == 20), scnum = scnum + offset; end
        testscenes = [testscenes scnum];
    end
end
