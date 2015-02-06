function [subjects, intData, actualRedTactics, mostLikelyRedTacticsData, redTacticProbsData,...
    batchPlotData, bluebookData, attackProbStages, subjectAttackProbs, normativeAttackProbs,...
    sigintSelectionData, blueActionSelectionData, timeDataByTrial] = ...
    aggregate_csv_mission_data(mission_csv_file, missionNum)
%AGGREGATE_CSV_MISSION_DATA Aggregtates CSV files containing participant data
%for each mission.
%   Detailed explanation goes here

%Load the CSV file containing mission data into a cell array
mission_data = csv2cell(mission_csv_file, 'fromfile');
assert(size(mission_data,1) > 1);

%Whether to use the incremental Bayesian probabilities
useIncrementalProbs = true;

%Epsilon is the amount to adjust probabilities that are < epsilon or > 100-epsilon. The default is 0.01 (1%).
epsilon = 0.01; 
%epsilon = 0; %Set to 0 for now. Need to adjust when computing average probs
%epsilon = 1e-3;

%% Determine the starting columns for data to aggregate
blueActionRegexp = 'blue_action_[123456789]';
attackProbsRegexp = 'probs_P(p|pc|t|tpc)_[123456789]';
numLocations = 0; % The number of Blue locations per trial
trialTimeCol = 0; % The trial time column
redCapabilityCol = 0; % The Red capability (Pc) column
redVulnerabilityCol = 0; % The Red vulnerability (P) column
redOpportunityCol = 0; % The Red opportunity (U) column
redActivityCol = 0; % The Red activity detected (SIGINT) column
actualRedTacticCol = 0; % The actual Red tactic column
mostLikelyRedTacticsCol = 0; % The first column containg most likely Red tactic probe data (Mission 2)
redTacticProbsCol = 0; % The first columnn containing Red tactics probability data (Missions 4-5)
redTacticNormativeProbsCol = 0; % The first columnn containing normative Red tactics probability data (Missions 4-5)
batchPlotCol = 0; %The first column containing batch plot information (Missions 4-6)
attackProbStages = {};
currAttackProbType = [];
bluebookProbsCol = 0; % The first column containing BLUEBOOK probability data
attackProbsCol = 0; % The first column containing attack probability data (e.g., probs_Pp_time)
sigintSelectionCol = 0; %The first column containing SIGINT selection data (Mission 3)
blueActionSelectionCol = 0; %The first column containing Blue action selection data (Missions 2-5)
blueActionNormativeParticipantCol = 0; %The first column containing normative participant Blue action selection data (Missions 2-5)
blueActionNormativeBayesianCol = 0; %The first column containing normative Bayesian Blue action selection data (Missions 2-5)
for col = 1:size(mission_data, 2)
    str = mission_data{1, col};
    if trialTimeCol == 0 && strncmp(str, 'trial_time', 11)
        trialTimeCol = col;    
    elseif redCapabilityCol == 0 && strncmp(str, 'red_cap', 7)
        redCapabilityCol = col;
    elseif redVulnerabilityCol == 0 && strncmp(str, 'red_vul', 7)
        redVulnerabilityCol = col;
    elseif redOpportunityCol == 0 && strncmp(str, 'red_opp', 7)
        redOpportunityCol = col;
    elseif redActivityCol == 0 && strncmp(str, 'red_act', 7)
        redActivityCol = col;
    elseif actualRedTacticCol == 0 && strncmp(str, 'red_tactics_act', 15)
        actualRedTacticCol = col;
    elseif mostLikelyRedTacticsCol == 0 && strncmp(str, 'red_tactics_most_likely_ta', 26)
        mostLikelyRedTacticsCol = col;
    elseif redTacticProbsCol == 0 && strncmp(str, 'red_tactics_probs_tim', 21)
        redTacticProbsCol = col;
    elseif redTacticNormativeProbsCol == 0 && strncmp(str, 'red_tactics_probs_nor', 21)
        redTacticNormativeProbsCol = col;
    elseif batchPlotCol == 0 && strncmp(str, 'red_tactics_bat', 15)
        batchPlotCol = col;
    elseif bluebookProbsCol == 0 && strncmp(str, 'bluebook', 8)
        bluebookProbsCol = col;
    elseif attackProbsCol == 0 && strncmp(str, 'probs_P', 7)
        attackProbsCol = col;
    elseif sigintSelectionCol == 0 && strncmp(str, 'sigint', 6)
        sigintSelectionCol = col;
    elseif blueActionSelectionCol == 0 && strncmp(str, 'blue_action', 11)
        blueActionSelectionCol = col;
    elseif blueActionNormativeParticipantCol == 0 && strncmp(str, 'blue_action_normative_p', 23)
        blueActionNormativeParticipantCol = col;
    elseif blueActionNormativeBayesianCol == 0 && strncmp(str, 'blue_action_normative_b', 23)
        blueActionNormativeBayesianCol = col;
    end
    
    if ~isempty(regexp(str, attackProbsRegexp, 'once'))        
        if ~isnan(str2double(mission_data{2,col}))          
           strs = strsplit(str, '_');
           if isempty(currAttackProbType) || ~strcmp(currAttackProbType, strs(2))
              currAttackProbType = strs{2};
              attackProbStages{length(attackProbStages)+1} = currAttackProbType; %#ok<*AGROW>
           end
        end
    elseif ~isempty(regexp(str, blueActionRegexp, 'once'))
        if ~strncmp(mission_data{2,col}, 'NaN', 3)
            numLocations = str2double(str(length(str)));
        end
    end
end
numAttackProbsStages = length(attackProbStages);

%% Determine the number of subjects and trials
currSubject = mission_data{2,2};
currSubjectNumTrials = 0;
numTrials = 0;
numSubjects = 1;
for row = 2:size(mission_data,1)
    if ~strcmp(mission_data{row, 2}, currSubject)
        %We're at the next subject
        numSubjects = numSubjects + 1;
        currSubject = mission_data{row, 2};
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
%numTrials
%numSubjects
assert(numTrials > 0);

%% Aggregate data
if trialTimeCol > 0
   timeDataByTrial = zeros(numSubjects, numTrials);
else
   timeDataByTrial = [];
end
if redCapabilityCol > 0
    intData.redCapabilityPc = zeros(numSubjects, numTrials);
else
    intData.redCapabilityPc = [];
end
if redVulnerabilityCol > 0
    intData.redVulnerabilityP = cell(numSubjects, numTrials);
else
    intData.redVulnerabilityP = [];
end
if redOpportunityCol > 0
    intData.redOpportunityU = cell(numSubjects, numTrials);
else
    intData.redOpportunityU = [];
end
if redActivityCol > 0
    intData.redActivityDetected = cell(numSubjects, numTrials);
else
    intData.redActivityDetected = [];
end
if actualRedTacticCol > 0
    actualRedTactics = zeros(numSubjects, numTrials);
else
    actualRedTactics = [];
end
redTacticTypes = getRedTacticTypes(missionNum);
if mostLikelyRedTacticsCol > 0
    mostLikelyRedTacticsData.selectedTactic = zeros(numSubjects, numTrials);    
    mostLikelyRedTacticsData.tacticTypes = redTacticTypes;
else
    mostLikelyRedTacticsData = [];
end
if redTacticProbsCol > 0
    redTacticProbsData.tacticProbs = cell(numSubjects, numTrials);
    if redTacticNormativeProbsCol > 0
        redTacticProbsData.normativeTacticProbs = cell(numSubjects, numTrials);
    end
    redTacticProbsData.tacticTypes = redTacticTypes;    
    numRedTactics = length(redTacticProbsData.tacticTypes);
else
    redTacticProbsData = [];
end
if batchPlotCol > 0    
    batchPlotData = cell(numSubjects, numTrials);
else
    batchPlotData = [];
end
if bluebookProbsCol > 0
    bluebookData.bluebookProbs = cell(numSubjects, numTrials);
    %numRedTactics = 1;
    startCol = bluebookProbsCol;
    col = startCol + 1;    
    while strncmp(mission_data{1, col}, 'bluebook', 8)
        col = col + 1;
        %numRedTactics = numRedTactics + 1; 
    end
    numRedTactics = (col - startCol)/numLocations;
    bluebookData.redTacticTypes = cell(1, numRedTactics);
    i = 1;    
    %for col = bluebookProbsCol : numLocations : bluebookProbsCol + numRedTactics - 1
    for col = bluebookProbsCol : numLocations : bluebookProbsCol + (numRedTactics * numLocations) - 1
        strs = strsplit(mission_data{1, col}, '_');
        bluebookData.redTacticTypes{i} = strs{2};
        i = i + 1;
    end
else
    bluebookData = [];
end
if attackProbsCol > 0
    subjectAttackProbs = cell(numSubjects, numTrials);
    normativeAttackProbs = cell(numSubjects, numTrials);
else
    subjectAttackProbs = [];
    normativeAttackProbs = [];
end
if sigintSelectionCol > 0
    sigintSelectionData.selectedLocation = zeros(numSubjects, numTrials);
    sigintSelectionData.selectionParticipantOptimal = zeros(numSubjects, numTrials);
    sigintSelectionData.selectionBayesianOptimal = zeros(numSubjects, numTrials);
else
    sigintSelectionData = [];
end
if blueActionSelectionCol > 0
    blueActionSelectionData.selectedActions = cell(numSubjects, numTrials);
    blueActionSelectionData.selectedActionsUtility = zeros(numSubjects, numTrials);
    blueActionSelectionData.normativeParticipantActions = cell(numSubjects, numTrials);
    blueActionSelectionData.normativeParticipantUtility = zeros(numSubjects, numTrials);
    blueActionSelectionData.normativeBayesianActions = cell(numSubjects, numTrials);
    blueActionSelectionData.normativeBayesianUtility = zeros(numSubjects, numTrials);    
    blueActionSelectionData.actionTypes = {'Divert', 'Do_Not_Divert'};
else
    blueActionSelectionData = [];
end

subjects = cell(numSubjects, 1);
currSubject = mission_data{2,2};
subjects{1} = currSubject;
subjectNum = 1;
trialNum = 0;
for row = 2:size(mission_data, 1)    
    if ~strcmp(mission_data{row, 2}, currSubject)
        %We're at the next subject, reset trial counter and increment
        %subject number
        subjectNum = subjectNum + 1;
        currSubject = mission_data{row, 2};
        subjects{subjectNum} = currSubject;
        trialNum = 1;
    else
        trialNum = trialNum + 1;
    end
    
    %Get the timing data for the trial
    if trialTimeCol > 0
        timeDataByTrial(subjectNum, trialNum) = str2double(mission_data(row, trialTimeCol));
    end 
    
    %Get the INT data for the trial    
    if redCapabilityCol > 0
        intData.redCapabilityPc(subjectNum, trialNum) = ...
            str2double(mission_data(row, redCapabilityCol));
    end
    if redVulnerabilityCol > 0
        intData.redVulnerabilityP{subjectNum, trialNum} = ...
            str2double(mission_data(row, redVulnerabilityCol : redVulnerabilityCol + numLocations - 1));
    end 
    if redOpportunityCol > 0
        intData.redOpportunityU{subjectNum, trialNum} = ...
            str2double(mission_data(row, redOpportunityCol : redOpportunityCol + numLocations - 1));
    end
    if redActivityCol > 0
        intData.redActivityDetected{subjectNum, trialNum} = ...
            str2double(mission_data(row, redActivityCol : redActivityCol + numLocations - 1));
    end
    
    %Get the actual Red tactic for the trial
    if actualRedTacticCol > 0
        tacticIndex = ...
            find(strcmp(mission_data(row, actualRedTacticCol), redTacticTypes) == 1, 1, 'first');
        if ~isempty(tacticIndex)
            actualRedTactics(subjectNum, trialNum) = tacticIndex;
        end
    end
    
    %Get the most likely Red tactics probe data for the trial
    if mostLikelyRedTacticsCol > 0        
        tacticIndex = ...
            find(strcmp(mission_data(row, mostLikelyRedTacticsCol), redTacticTypes) == 1, 1, 'first');
        if ~isempty(tacticIndex)
            mostLikelyRedTacticsData.selectedTactic(subjectNum, trialNum) = tacticIndex;
        end
    end
    
    %Get the Red tactics probability data for the trial
    if redTacticProbsCol > 0
        redTacticProbsData.tacticProbs{subjectNum, trialNum} = ...
            fillzerobins(str2double(mission_data(row, redTacticProbsCol + 1 : redTacticProbsCol + 1 + numRedTactics - 1)),...
            0, true);
        if redTacticNormativeProbsCol > 0
            redTacticProbsData.normativeTacticProbs{subjectNum, trialNum} = ...
            fillzerobins(str2double(mission_data(row, redTacticNormativeProbsCol : redTacticNormativeProbsCol + numRedTactics - 1)),...
            0, true);
        end
    end
    
    %Get the batch plot data for the trial
    if batchPlotCol > 0        
        batchPlotData{subjectNum, trialNum} = zeros(1, 2);
        batchPlotData{subjectNum, trialNum}(1) = str2double(mission_data(row, batchPlotCol));
        batchPlotData{subjectNum, trialNum}(2) = str2double(mission_data(row, batchPlotCol + 1));
    end
    
    %Get the BLUEBOOK probs for the trial
    if bluebookProbsCol > 0
        currBluebookProbs = zerodata(numRedTactics, numLocations);
        for tacticNum = 1:numRedTactics
            startCol = bluebookProbsCol + ((tacticNum - 1) * numLocations);
            currBluebookProbs(tacticNum, :) = ...
                str2double(mission_data(row, startCol : startCol + numLocations - 1));
        end
        bluebookData.bluebookProbs{subjectNum, trialNum} = currBluebookProbs;
    end
    
    %Get the attack probability data for the trial    
    if attackProbsCol > 0
        currSubjectAttackProbs = zerodata(numAttackProbsStages, numLocations+1);
        currNormativeAttackProbs = zerodata(numAttackProbsStages, numLocations+1);
        for attackProbsStage = 1:numAttackProbsStages
            %Get the subject probs
            startCol = attackProbsCol + attackProbsStage + (attackProbsStage - 1) * ((numLocations+1)*3);  % *3 with incremental probs, *2 without
            currSubjectAttackProbs(attackProbsStage, :) = ...
                fillzerobins(str2double(mission_data(row, startCol : startCol + numLocations)),...
                    epsilon, true);
                
            %Get the normative probs
            if useIncrementalProbs
                %Use the incremental Bayesian probabilities
                startCol = attackProbsCol + attackProbsStage + ((numLocations + 1) * 2) + ...
                    (attackProbsStage - 1) * ((numLocations+1)*3);
            else
                %Use the cumulative Bayesian probabilities
                startCol = attackProbsCol + attackProbsStage + numLocations + 1 + ...
                    (attackProbsStage - 1) * ((numLocations+1)*3); %#ok<UNRCH> % *3 with incremental probs, *2 without
            end
            currNormativeAttackProbs(attackProbsStage, :) = ...
                fillzerobins(str2double(mission_data(row, startCol : startCol + numLocations)),...
                    epsilon, true);
        end
        %currSubjectAttackProbs
        %currNormativeAttackProbs
        subjectAttackProbs{subjectNum, trialNum} = currSubjectAttackProbs;
        normativeAttackProbs{subjectNum, trialNum} = currNormativeAttackProbs;   
    end 
    
    %Get the SIGINT selection data for the trial
    if sigintSelectionCol > 0
        sigintSelectionData.selectedLocation(subjectNum, trialNum) = ...
            str2double(mission_data(row, sigintSelectionCol + 1));
        sigintSelectionData.selectionParticipantOptimal(subjectNum, trialNum) = ...
            str2double(mission_data(row, sigintSelectionCol + 2));
        sigintSelectionData.selectionBayesianOptimal(subjectNum, trialNum) = ...
            str2double(mission_data(row, sigintSelectionCol + 3));        
        %sigintSelectionData.selectedAtHightestPaLocation(subjectNum, trialNum) = ...
        %    str2double(mission_data(row, sigintSelectionCol + 2));
    end
    
    %Get the Blue action selection data for the trial
    if blueActionSelectionCol > 0
        blueActions = zeros(1, numLocations);        
        col = blueActionSelectionCol + 1;
        for location = 1:numLocations            
            actionStr = mission_data(row, col);
            if strncmp(actionStr, 'Di', 2)
                blueActions(location) = 1;               
            else
                blueActions(location) = 2;
            end
            col = col + 1;
        end
        blueActionSelectionData.selectedActions{subjectNum, trialNum} = blueActions;
        blueActionSelectionData.selectedActionsUtility(subjectNum, trialNum) = ...
                str2double(mission_data(row, col));
    end

    %Get the normative participant Blue action selection data for the trial
    if blueActionNormativeParticipantCol > 0
        blueActionsNormative = zeros(1, numLocations);        
        col = blueActionNormativeParticipantCol;
        for location = 1:numLocations            
            actionStr = mission_data(row, col);
            if strncmp(actionStr, 'Di', 2)
                blueActionsNormative(location) = 1;
            else
                blueActionsNormative(location) = 2;
            end
            col = col + 1;
        end
        blueActionSelectionData.normativeParticipantActions{subjectNum, trialNum} = blueActionsNormative;
        blueActionSelectionData.normativeParticipantUtility(subjectNum, trialNum) = ...
                str2double(mission_data(row, col));
    end
    
    %Get the normative Bayesian Blue action selection data for the trial
    if blueActionNormativeBayesianCol > 0
        blueActionsNormative = zeros(1, numLocations);        
        col = blueActionNormativeBayesianCol;
        for location = 1:numLocations            
            actionStr = mission_data(row, col);
            if strncmp(actionStr, 'Di', 2)
                blueActionsNormative(location) = 1;
            else
                blueActionsNormative(location) = 2;
            end
            col = col + 1;
        end
        blueActionSelectionData.normativeBayesianActions{subjectNum, trialNum} = ...
            blueActionsNormative;
        blueActionSelectionData.normativeBayesianUtility(subjectNum, trialNum) = ...
                str2double(mission_data(row, col));
    end
end
end

%% Get the Red tactic type names for the given mission
function redTacticTypes = getRedTacticTypes(missionNum)
switch missionNum
    case 1
        redTacticTypes = {'Neutral'};
    case 2
        redTacticTypes = {'Passive', 'Aggressive'};
    case 3
        redTacticTypes = {'Neutral'};
    case 4
        redTacticTypes = {'Passive', 'Aggressive'};
    case 5
        redTacticTypes = {'P-Sensitive', 'U-Sensitive'};
end
end