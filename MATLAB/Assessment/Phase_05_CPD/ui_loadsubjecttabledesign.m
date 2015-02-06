%% SUBJECT TABLE DESIGN - PILOT STUDY 2
% 
% Subject_number: subject number
% Phase_number: experiment phase
% Design_block_number: block number in design (use for analysis)
% Experiment_Block_number: block number in actual subject's experiment 
% Trial_number: trial number within phase 
% Modality
% Widget
% Normalization
% Default
%
% Trial_time: time spent on trial by user, in ms. 
% Number_probability_sets: the number of probability sets reported in this trial. 
%   Can be 1-3, depending on how many layer presentations occurred.
% Max_probs_in_set: Currently always 4.
% Probs_human: human (subject reported) probabilities
% Raw_probs_human: human (subject reported) probabilities prior to normalization step

subject_data_headings = [...
    'Subject_number' 'Phase_number' 'Design_block_number' 'Experiment_block_number' ...
    'Modality' 'Widget' 'Normalization' 'Default' 'Trial_number' 'Trial_time' ...
    'Number_probability_sets' 'Number_bins' 'Max_probs_in_set' ...
    probs_headings('TVD',3) ...
    probs_headings_set('Human_probs',max_probsets,max_probs_in_set) ...
    probs_headings_set('Raw_human_probs',max_probsets,max_probs_in_set) ...
    probs_headings_set('Normative_probs',max_probsets,max_probs_in_set) ...
    ];

[numbasefields, ...
    Subject_number_idx, Phase_number_idx, Design_block_number_idx, Experiment_block_number_idx, ...
    Modality_idx, Widget_idx, Normalization_idx, Default_idx, Trial_number_idx, Trial_time_idx, ...
    Number_probability_sets_idx, Number_bins_idx, Max_probs_in_set_idx ...
    ] = enum;

ABS_idx = numbasefields;
DEV_idx =                  ABS_idx + 3;
humanprobsoffset =         DEV_idx + 3;
rawhumanprobsoffset =      humanprobsoffset + max_probsets*max_probs_in_set;
normativeprobsoffset =     rawhumanprobsoffset + max_probsets*max_probs_in_set;

enddata =             normativeprobsoffset   + max_probs_in_set;
