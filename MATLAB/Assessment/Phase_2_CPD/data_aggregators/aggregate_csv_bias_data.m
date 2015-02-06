function [subjects, aaData, pdeData, rrData, avData] = ...
    aggregate_csv_bias_data (bias_csv_file, missionNum)
%AGGREGATE_CSV_BIAS_DATA Aggregtates CSV files containing participant biases exhibited data
%for each mission.
%   Detailed explanation goes here

%Load the CSV file containing biases exhibited data into a cell array
bias_data = csv2cell(bias_csv_file, 'fromfile');
assert(size(bias_data,1) > 1);

aaCol = 0; %AA (Anchoring & Adjustment) column
pdeCol = 0; %PDE (Persitence of Discredited Evidence) column
rrCol = 0; %RR (Representativeness) column
avCol = 0; %AV (Availability) column
numLocations = 1; %The number of Blue locations per trial
for col = 1:size(bias_data, 2)
    str = bias_data{1, col};
    if aaCol == 0 && strncmp(str, 'AA', 2)
        aaCol = col;
    elseif pdeCol == 0 && strncmp(str, 'PDE', 3)
        pdeCol = col;
    elseif rrCol == 0 && strncmp(str, 'RR', 2)
        rrCol = col;
    elseif avCol == 0 && strncmp(str, 'AV', 2)
        avCol = col;
    end    
    
    if strncmp(str, 'RR', 2)    
        num = str2double(str(4));
        if ~isnan(num) && ~isempty(num)
            numLocations = num;
        end
    end
end

%% Determine the number of subjects and trials
currSubject = bias_data{2,2};
currSubjectNumTrials = 0;
numTrials = 0; %The number of trials
numSubjects = 1; %The number of subjects
for row = 2:size(bias_data,1)
    if ~strcmp(bias_data{row, 2}, currSubject)
        %We're at the next subject
        numSubjects = numSubjects + 1;
        currSubject = bias_data{row, 2};
        if numTrials == 0
            numTrials = currSubjectNumTrials;
        else
            assert(currSubjectNumTrials == numTrials);
        end
        currSubjectNumTrials = 1;
    else
        currSubjectNumTrials = currSubjectNumTrials + 1;
    end
end
if numTrials == 0
    numTrials = currSubjectNumTrials;
end
%missionNum
%numTrials
%numLocations
%numSubjects
assert(numTrials > 0);

aaData.totalTrials = 0;
aaData.measured = zeros(numSubjects, numTrials);
aaData.magnitude = zeros(numSubjects, numTrials);
aaData.exhibited = zeros(numSubjects, numTrials);

pdeData.totalTrials = 0;
pdeData.measured = zeros(numSubjects, numTrials);
pdeData.magnitude = zeros(numSubjects, numTrials);
pdeData.exhibited = zeros(numSubjects, numTrials);

rrData.totalTrials = 0;
rrData.measured = zeros(numSubjects, numTrials * numLocations);
rrData.magnitude = zeros(numSubjects, numTrials * numLocations);
rrData.exhibited = zeros(numSubjects, numTrials * numLocations);

avData.totalTrials = 0;
avData.measured = zeros(numSubjects, numTrials);
avData.magnitude = zeros(numSubjects, numTrials);
avData.exhibited = zeros(numSubjects, numTrials);

subjects = cell(numSubjects, 1);
currSubject = bias_data{2,2};
subjects{1} = currSubject;
subjectNum = 1;
trialNum = 0;
rrTrialNum = 1;
for row = 2:size(bias_data, 1)    
    if ~strcmp(bias_data{row, 2}, currSubject)
        %We're at the next subject, reset trial counter and increment
        %subject number
        subjectNum = subjectNum + 1;
        currSubject = bias_data{row, 2};
        subjects{subjectNum} = currSubject;
        trialNum = 1;
        rrTrialNum = 1;
    else
        trialNum = trialNum + 1;
    end
    
    %Get the AA metrics for the trial
    if aaCol > 0
        measured = str2double(bias_data(row, aaCol));
        aaData.magnitude(subjectNum, trialNum) = str2double(bias_data(row, aaCol + 1));
        aaData.exhibited(subjectNum, trialNum) = str2double(bias_data(row, aaCol + 2));
        aaData.measured(subjectNum, trialNum) = ~isnan(measured) && measured && ...
            ~isnan(aaData.magnitude(subjectNum, trialNum)) && ...
            ~isnan(aaData.exhibited(subjectNum, trialNum));
        if aaData.measured(subjectNum, trialNum)
            aaData.totalTrials = aaData.totalTrials + 1;
        end
    end 
    
    %Get the PDE metrics for the trial
    if pdeCol > 0
        measured = str2double(bias_data(row, pdeCol));
        pdeData.magnitude(subjectNum, trialNum) = str2double(bias_data(row, pdeCol + 1));
        pdeData.exhibited(subjectNum, trialNum) = str2double(bias_data(row, pdeCol + 2));
        pdeData.measured(subjectNum, trialNum) = ~isnan(measured) && measured && ...
            ~isnan(pdeData.magnitude(subjectNum, trialNum)) && ...
            ~isnan(pdeData.exhibited(subjectNum, trialNum));
        if pdeData.measured(subjectNum, trialNum)
            pdeData.totalTrials = pdeData.totalTrials + 1;
        end
    end 
    
    %Get the RR metrics for each location for the trial
    if rrCol > 0
        for location = 1:numLocations
            currCol = rrCol + ((location - 1) * 3);
            measured = str2double(bias_data(row, currCol));
            rrData.magnitude(subjectNum, rrTrialNum) = str2double(bias_data(row, currCol + 1));
            rrData.exhibited(subjectNum, rrTrialNum) = str2double(bias_data(row, currCol + 2));
            rrData.measured(subjectNum, rrTrialNum) = ~isnan(measured) && measured && ...
                ~isnan(rrData.magnitude(subjectNum, trialNum)) && ...
                ~isnan(rrData.exhibited(subjectNum, trialNum));
            if rrData.measured(subjectNum, rrTrialNum)
                rrData.totalTrials = rrData.totalTrials + 1;
            end
            rrTrialNum = rrTrialNum + 1;
        end
    end

    %Get the AV metrics for the trial
    if aaCol > 0
        measured = str2double(bias_data(row, avCol));
        avData.magnitude(subjectNum, trialNum) = str2double(bias_data(row, avCol + 1));
        avData.exhibited(subjectNum, trialNum) = str2double(bias_data(row, avCol + 2));
        avData.measured(subjectNum, trialNum) = ~isnan(measured) && measured && ...
            ~isnan(avData.magnitude(subjectNum, trialNum)) && ...
            ~isnan(avData.exhibited(subjectNum, trialNum));
        if avData.measured(subjectNum, trialNum)
            avData.totalTrials = avData.totalTrials + 1;
        end
    end
end

if aaData.totalTrials == 0
    aaData.measured = [];
    aaData.magnitude = [];
    aaData.exhibited = [];
end
if pdeData.totalTrials == 0
    pdeData.measured = [];
    pdeData.magnitude = [];
    pdeData.exhibited = [];
end
if rrData.totalTrials == 0
    rrData.measured = [];
    rrData.magnitude = [];
    rrData.exhibited = [];
end
if avData.totalTrials == 0
    avData.measured = [];
    avData.magnitude = [];
    avData.exhibited = [];
end

end