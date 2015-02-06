function [numSubjects, numTrials, numStages, numLocations, mostLikelyTacticSelections,...    
    sigintSelectionFrequencies, sigintSelectionOptimalFrequencies,...
    sigintSelectionParticipantNormativeFrequencies,...
    sigintSelectionParticipantNormativeOptimalFrequencies,...
    subjectProbsByStage, normativeProbsByStage, subjectProbsAvgByStage, normativeProbsAvgByStage,...
    subjectPtProbs, normativePtProbs, subjectPtProbsAvg, normativePtProbsAvg, chatterAvg, silentAvg,...
    subjectNe, subjectNeAvg, subjectDeltaNe, normativeNe, normativeNeAvg, normativeDeltaNe,...
    notDivertFrequency, blueActionFrequencies, blueActionOptimalFrequencies,...
    blueActionParticipantNormativeFrequencies, blueActionParticipantNormativeOptimalFrequencies] = ...
    compute_mission_metrics_old(intData, mostLikelyRedTacticsData, sigintSelectionData,...
        subjectProbs, normativeProbs, attackProbStages, blueActionSelectionData)
%COMPUTE_MISSION_METRICS Summary of this function goes here
%   Detailed explanation goes here

numSubjects = size(subjectProbs, 1); %The number of subjects
numTrials = size(subjectProbs, 2); %The number of trials in the mission
numStages = length(attackProbStages); %The number of Red attack probability assessment stages
numLocations = size(subjectProbs{1,1}, 2); %The number of locations on each trial

epsilon = 0.01;
subjectProbsAvg = cell(numTrials, 1); %Average subject probabilities for each stage
normativeProbsAvg = cell(numTrials, 1); %Average normative probabilities for each stage
subjectProbsByStage = cell(numStages, numLocations);
normativeProbsByStage = cell(numStages, numLocations);
subjectProbsAvgByStage = cell(numStages, numLocations);
normativeProbsAvgByStage = cell(numStages, numLocations);

subjectPtProbs = [];
normativePtProbs = [];
subjectPtProbsAvg = [];
normativePtProbsAvg = [];
chatterAvg = [];
silentAvg = [];

subjectNe = cell(1, numStages);
subjectNeAvg = zeros(numStages, numTrials);
normativeNe = cell(1, numStages);
normativeNeAvg = zeros(numStages, numTrials);
subjectDeltaNe = cell(1, numStages);
normativeDeltaNe = cell(1, numStages);

if ~isempty(mostLikelyRedTacticsData)
    numTactics = length(mostLikelyRedTacticsData.tacticTypes);
    mostLikelyTacticSelections = zeros(numTrials, numTactics);
else
    mostLikelyTacticSelections = []; %The frequency of subjects who selected each Red tactic type as most likely (by trial->tactic) (Mission 2 only)
end

if ~isempty(sigintSelectionData)
    sigintSelectionFrequencies = zeros(numTrials, numLocations);
    sigintSelectionOptimalFrequencies = zeros(1, numTrials);
    sigintSelectionParticipantNormativeFrequencies = zeros(numTrials, numLocations);
    sigintSelectionParticipantNormativeOptimalFrequencies = zeros(1, numTrials);
else
    sigintSelectionFrequencies = [];
    sigintSelectionOptimalFrequencies = [];
    sigintSelectionParticipantNormativeFrequencies = [];
    sigintSelectionParticipantNormativeOptimalFrequencies = [];
end

if ~isempty(blueActionSelectionData)
    if isfield(blueActionSelectionData, 'selectedActions')
        notDivertFrequency = 0;
        blueActionFrequencies = cell(1, numLocations);
        for location = 1:numLocations
            blueActionFrequencies{location} = zeros(numTrials, 2);
        end
        if isfield(blueActionSelectionData, 'normativeBayesianActions')
            blueActionOptimalFrequencies = zeros(numLocations, numTrials);
        else
            blueActionOptimalFrequencies = [];
        end
    else
        notDivertFrequency = [];
        blueActionFrequencies = [];
        blueActionOptimalFrequencies = [];
    end    
    if isfield(blueActionSelectionData, 'normativeParticipantActions')
        blueActionParticipantNormativeFrequencies = cell(1, numLocations);
        for location = 1:numLocations
            blueActionParticipantNormativeFrequencies{location} = zeros(numTrials, 2);
        end
        if isfield(blueActionSelectionData, 'normativeBayesianActions')
            blueActionParticipantNormativeOptimalFrequencies = zeros(numLocations, numTrials);
        else
            blueActionParticipantNormativeOptimalFrequencies = [];
        end    
    end    
else
    notDivertFrequency = [];
    blueActionFrequencies = [];
    blueActionOptimalFrequencies = [];
    blueActionParticipantNormativeFrequencies = [];
    blueActionParticipantNormativeOptimalFrequencies = [];
end

%for subject = 1:numSubjects
for trial = 1:numTrials
    for stage = 1:numStages
        for subject = 1:numSubjects
            if stage == 1
                %Compute frequency of subjects who selected each Red tactic type as most likely
                if ~isempty(mostLikelyRedTacticsData)
                    tactic = mostLikelyRedTacticsData.selectedTactic(subject, trial);
                    if ~isnan(tactic) && tactic > 0
                        mostLikelyTacticSelections(trial, tactic) = mostLikelyTacticSelections(trial, tactic) + 1;
                    end
                end
                
                %Compute SIGINT selection frequencies
                if ~isempty(sigintSelectionData)
                    location = sigintSelectionData.selectedLocation(subject, trial);
                    participantOptimalLocation = ...
                        sigintSelectionData.selectionParticipantOptimal(subject, trial);
                    bayesianOptimalLocation = ...
                        sigintSelectionData.selectionBayesianOptimal(subject, trial);
                    sigintSelectionFrequencies(trial, location) = ...
                        sigintSelectionFrequencies(trial, location) + 1;
                    if location == bayesianOptimalLocation
                        sigintSelectionOptimalFrequencies(trial) = ...
                            sigintSelectionOptimalFrequencies(trial) + 1;
                    end
                    sigintSelectionParticipantNormativeFrequencies(trial, participantOptimalLocation) = ...
                        sigintSelectionParticipantNormativeFrequencies(trial, participantOptimalLocation) + 1;
                    if location == participantOptimalLocation
                        sigintSelectionParticipantNormativeOptimalFrequencies(trial) = ...
                            sigintSelectionParticipantNormativeOptimalFrequencies(trial) + 1;
                    end
                end %End compute SIGINT selection frequencies                
                
                %Compute Blue action selection frequencies
                if ~isempty(blueActionFrequencies)                    
                    blueActions = blueActionSelectionData.selectedActions{subject, trial};
                    if ~isempty(blueActionParticipantNormativeFrequencies)
                        participantNormativeActions = blueActionSelectionData.normativeParticipantActions{subject, trial};
                    else
                        participantNormativeActions = [];
                    end
                    if ~isempty(blueActionOptimalFrequencies)
                        optimalActions = blueActionSelectionData.normativeBayesianActions{subject, trial};
                    else
                        optimalActions = [];
                    end
                    for location = 1:numLocations
                        if blueActions(location) == 2
                            notDivertFrequency = notDivertFrequency + 1;
                        end
                        blueActionFrequencies{location}(trial, blueActions(location)) = ...
                            blueActionFrequencies{location}(trial, blueActions(location)) + 1;
                        if ~isempty(optimalActions)
                            if blueActions(location) == optimalActions(location)
                                blueActionOptimalFrequencies(location, trial) = ...
                                    blueActionOptimalFrequencies(location, trial) + 1;
                            end
                        end                        
                        if ~isempty(participantNormativeActions)
                            blueActionParticipantNormativeFrequencies{location}(trial, participantNormativeActions(location)) = ...
                                blueActionParticipantNormativeFrequencies{location}(trial, participantNormativeActions(location)) + 1;                            
                            if blueActions(location) == participantNormativeActions(location)
                                blueActionParticipantNormativeOptimalFrequencies(location, trial) = ...
                                    blueActionParticipantNormativeOptimalFrequencies(location, trial) + 1; %#ok<*AGROW>
                            end
                        end
                    end
                end %End compute Blue action selection frequencies                
            end                        
            
        end
    end
end

if ~isempty(mostLikelyRedTacticsData)
    mostLikelyTacticSelections = mostLikelyTacticSelections / numSubjects;
end

if ~isempty(sigintSelectionData)
    sigintSelectionFrequencies = sigintSelectionFrequencies / numSubjects;
    sigintSelectionOptimalFrequencies = sigintSelectionOptimalFrequencies / numSubjects;
    sigintSelectionParticipantNormativeFrequencies = ...
        sigintSelectionParticipantNormativeFrequencies / numSubjects;
    sigintSelectionParticipantNormativeOptimalFrequencies = ...
        sigintSelectionParticipantNormativeOptimalFrequencies / numSubjects;
end

for location = 1:numLocations
    blueActionFrequencies{location} = blueActionFrequencies{location} / numSubjects;
    if ~isempty(blueActionParticipantNormativeFrequencies)
        blueActionParticipantNormativeFrequencies{location} = ...
            blueActionParticipantNormativeFrequencies{location} / numSubjects;
    end
end
if ~isempty(blueActionOptimalFrequencies)
    blueActionOptimalFrequencies = ...
        blueActionOptimalFrequencies / numSubjects;
end
if ~isempty(blueActionParticipantNormativeOptimalFrequencies)
    blueActionParticipantNormativeOptimalFrequencies = ...
        blueActionParticipantNormativeOptimalFrequencies / numSubjects;
end
notDivertFrequency = notDivertFrequency / (numSubjects * numTrials * numLocations);







%% Compute percent of subjects who selected each Red tactic type as most likely on each most likely Red tactics trial (Mission 2 only)
if ~isempty(mostLikelyRedTacticsData)
    numTactics = length(mostLikelyRedTacticsData.tacticTypes);
    mostLikelyTacticSelections = zeros(numTrials, numTactics);
    for subject = 1:numSubjects
        for trial = 1:numTrials
            tactic = mostLikelyRedTacticsData.selectedTactic(subject, trial);            
            if ~isnan(tactic) && tactic > 0                
                mostLikelyTacticSelections(trial, tactic) = mostLikelyTacticSelections(trial, tactic) + 1;
            end
        end
    end
    mostLikelyTacticSelections = mostLikelyTacticSelections / numSubjects;
end

%% Compute SIGINT selection frequencies (Mission 3 Only)
%Compute percent of subjects who chose to get SIGINT at each location on
%each trial. Also compute the percent of participant SIGINT choices that were 
%optimal given the Bayesian probabilities, and the percent of participant 
%SIGINT selections that were optimal given the participant probabilities.
if ~isempty(sigintSelectionData)
    sigintSelectionFrequencies = zeros(numTrials, numLocations);
    sigintSelectionOptimalFrequencies = zeros(1, numTrials);
    sigintSelectionParticipantNormativeFrequencies = zeros(numTrials, numLocations);
    sigintSelectionParticipantNormativeOptimalFrequencies = zeros(1, numTrials);
    for subject = 1:numSubjects
        for trial = 1:numTrials
            location = sigintSelectionData.selectedLocation(subject, trial);
            participantOptimalLocation = ...
                sigintSelectionData.selectionParticipantOptimal(subject, trial);
            bayesianOptimalLocation = ...
                sigintSelectionData.selectionBayesianOptimal(subject, trial);
            
            sigintSelectionFrequencies(trial, location) = ...
                sigintSelectionFrequencies(trial, location) + 1;
            if location == bayesianOptimalLocation
                sigintSelectionOptimalFrequencies(trial) = ...
                    sigintSelectionOptimalFrequencies(trial) + 1;
            end
            
            sigintSelectionParticipantNormativeFrequencies(trial, participantOptimalLocation) = ...
                sigintSelectionParticipantNormativeFrequencies(trial, participantOptimalLocation) + 1;            
            if location == participantOptimalLocation
                sigintSelectionParticipantNormativeOptimalFrequencies(trial) = ...
                    sigintSelectionParticipantNormativeOptimalFrequencies(trial) + 1;
            end
        end
    end
    sigintSelectionFrequencies = sigintSelectionFrequencies / numSubjects;
    sigintSelectionOptimalFrequencies = sigintSelectionOptimalFrequencies / numSubjects;
    sigintSelectionParticipantNormativeFrequencies = ...
        sigintSelectionParticipantNormativeFrequencies / numSubjects;
    sigintSelectionParticipantNormativeOptimalFrequencies = ...
        sigintSelectionParticipantNormativeOptimalFrequencies / numSubjects;
end

%% Compute average subject attack probs and normative attack probs for each stage
ptStage = find(strcmp('Pt', attackProbStages));
if ~isempty(ptStage) && ptStage > 0
    subjectPtProbs = cell(2, numLocations);
    normativePtProbs = cell(2, numLocations);
    subjectPtProbsAvg = cell(2, numLocations);
    normativePtProbsAvg = cell(2, numLocations);    
    for sigintType = 1:2
        for location = 1:numLocations
            subjectPtProbs{sigintType, location} = zeros(numSubjects, numTrials);
            normativePtProbs{sigintType, location} = zeros(numSubjects, numTrials);
            subjectPtProbsAvg{sigintType, location} = zeros(1, numTrials);
            normativePtProbsAvg{sigintType, location} = zeros(1, numTrials);
        end
    end
    chatterAvg = 0;
    silentAvg = 0;
    silentTrials = zeros(1, numTrials);
    chatterTrials = zeros(1, numTrials);
    for trial = 1:numTrials
        if intData.redActivityDetected{1, trial}
            chatterTrials(trial) = 1;
        else
            silentTrials(trial) = 1;
        end
    end
    chatterTrials = find(chatterTrials > 0);
    silentTrials = find(silentTrials > 0);
else 
    ptStage = -1;
end

for trial = 1:numTrials
    subjectProbsAvg{trial} = zeros(numStages, numLocations);
    normativeProbsAvg{trial} = zeros(numStages, numLocations);
end
for stage = 1:numStages
    for location = 1:numLocations
        subjectProbsByStage{stage, location} = zeros(numSubjects, numTrials);
        normativeProbsByStage{stage, location} = zeros(numSubjects, numTrials);
        subjectProbsAvgByStage{stage, location} = zeros(1, numTrials);
        normativeProbsAvgByStage{stage, location} = zeros(1, numTrials);    
    end    
end

for subject = 1:numSubjects
    for trial = 1:numTrials
        subjectProbsAvg{trial} = subjectProbsAvg{trial} + subjectProbs{subject, trial};
        normativeProbsAvg{trial} = normativeProbsAvg{trial} + normativeProbs{subject, trial};
    end
    for stage = 1:numStages
        for location = 1:numLocations
            for trial = 1:numTrials
                subjectProbsByStage{stage, location}(subject, trial) = ...
                    subjectProbs{subject, trial}(stage, location);
                normativeProbsByStage{stage, location}(subject, trial) = ...
                    normativeProbs{subject, trial}(stage, location);
                
                if stage == ptStage
                    sigintType = intData.redActivityDetected{subject, trial}(location) + 1;
                    subjectPtProbs{sigintType, location}(subject, trial) = ...
                        subjectProbs{subject, trial}(stage, location);
                    normativePtProbs{sigintType, location}(subject, trial) = ...
                        normativeProbs{subject, trial}(stage, location);
                    if sigintType == 2                        
                        chatterAvg = chatterAvg + ...
                            subjectPtProbs{sigintType, location}(subject, trial);
                    else                        
                        silentAvg = silentAvg + ...
                            subjectPtProbs{sigintType, location}(subject, trial);
                    end
                end
            end
        end
    end
end
if ~isempty(chatterAvg)    
    chatterAvg = chatterAvg / (numSubjects * length(chatterTrials));
    silentAvg = silentAvg / (numSubjects * length(silentTrials));
end

for subject = 1:numSubjects
    for stage = 1:numStages
        for location = 1:numLocations
            subjectProbsAvgByStage{stage, location} = subjectProbsAvgByStage{stage, location} + ...
                subjectProbsByStage{stage, location}(subject, :);
            normativeProbsAvgByStage{stage, location} = normativeProbsAvgByStage{stage, location} + ...
                normativeProbsByStage{stage, location}(subject, :);
            
            if stage == ptStage                
                for sigintType = 1:2                                                   
                    subjectPtProbsAvg{sigintType, location}(1, :) = ...
                        subjectPtProbsAvg{sigintType, location}(1, :) + subjectPtProbs{sigintType, location}(subject, :);
                    normativePtProbsAvg{sigintType, location}(1, :) = ...
                        normativePtProbsAvg{sigintType, location}(1, :) + normativePtProbs{sigintType, location}(subject, :);
                end
            end
        end
    end
end

for trial = 1:numTrials
    subjectProbsAvg{trial} = subjectProbsAvg{trial} / numSubjects;
    normativeProbsAvg{trial} = normativeProbsAvg{trial} / numSubjects;
    for stage = 1:numStages
        subjectProbsAvg{trial}(stage, :) = fillzerobins(subjectProbsAvg{trial}(stage, :), epsilon);
        normativeProbsAvg{trial}(stage, :) = fillzerobins(normativeProbsAvg{trial}(stage, :), epsilon);
        %subjectNeAvgByStage(trial, stage) = sum(subjectNe(:, trial, stage)) / numSubjects;
        %normativeNeAvgByStage(trial, stage) = sum(normativeNe(:, trial, stage)) / numSubjects;
    end
end

for stage = 1:numStages
    for location = 1:numLocations
        subjectProbsAvgByStage{stage, location} = subjectProbsAvgByStage{stage, location}/numSubjects;
        normativeProbsAvgByStage{stage, location} = normativeProbsAvgByStage{stage, location}/numSubjects;
        if stage == ptStage
            for sigintType = 1:2
                subjectPtProbsAvg{sigintType, location}(1, :) = ...
                    subjectPtProbsAvg{sigintType, location}(1, :)/numSubjects;
                normativePtProbsAvg{sigintType, location}(1, :) = ...
                    normativePtProbsAvg{sigintType, location}(1, :)/numSubjects;
            end
        end
        for trial = 1:numTrials
            subjectProbsAvgByStage{stage, location}(trial) = ...
                fillzerobins(subjectProbsAvgByStage{stage, location}(trial), epsilon);
            normativeProbsAvgByStage{stage, location}(trial) = ...
                fillzerobins(normativeProbsAvgByStage{stage, location}(trial), epsilon);
            if stage == ptStage
                for sigintType = 1:2
                    subjectPtProbsAvg{sigintType, location}(trial) = ...
                        fillzerobins(subjectPtProbsAvg{sigintType, location}(trial), epsilon);
                    normativePtProbsAvg{sigintType, location}(trial) = ...
                        fillzerobins(normativePtProbsAvg{sigintType, location}(trial), epsilon);
                end
            end
        end
    end
end

%% Compute negentropies of the attack probs
for stage = 1:numStages
    subjectNe{stage} = zeros(numSubjects, numTrials);
    for subject = 1:numSubjects
        for trial = 1:numTrials            
            p_subj = [subjectProbs{subject, trial}(stage, :)...
                1 - sum(subjectProbs{subject, trial}(stage, :))];
            p_subj = fillzerobins(p_subj, epsilon);
            subjectNe{stage}(subject, trial) = negentropy(p_subj);
            
            p_norm = [normativeProbs{subject, trial}(stage, :)...
                1 - sum(normativeProbs{subject, trial}(stage, :))];
            p_norm = fillzerobins(p_norm, epsilon);
            normativeNe{stage}(subject, trial) = negentropy(p_norm);
            
            %Compute delta Ne
            if stage > 1                
                subjectDeltaNe{stage}(subject, trial) = ...
                    subjectNe{stage}(subject, trial) - subjectNe{stage-1}(subject, trial);
                normativeDeltaNe{stage}(subject, trial) = ...
                    normativeNe{stage}(subject, trial) - normativeNe{stage-1}(subject, trial);
            end
        end
    end
    
    for trial = 1:numTrials
        p_subj = [subjectProbsAvg{trial}(stage, :)...
            1 - sum(subjectProbsAvg{trial}(stage, :))];
        p_subj = fillzerobins(p_subj, epsilon);
        subjectNeAvg(stage, trial) = negentropy(p_subj);
        
        p_norm = [normativeProbsAvg{trial}(stage, :)...
            1 - sum(normativeProbsAvg{trial}(stage, :))];
        p_norm = fillzerobins(p_norm, epsilon);
        normativeNeAvg(stage, trial) = negentropy(p_norm);
    end
end

%% Compute Blue action selection frequencies
if ~isempty(blueActionSelectionData)
    if isfield(blueActionSelectionData, 'selectedActions')
        notDivertFrequency = 0;
        blueActionFrequencies = cell(1, numLocations);
        for location = 1:numLocations
            blueActionFrequencies{location} = zeros(numTrials, 2);
        end
        if isfield(blueActionSelectionData, 'normativeBayesianActions')
            blueActionOptimalFrequencies = zeros(numLocations, numTrials);
        else
            blueActionOptimalFrequencies = [];
        end
    else
        notDivertFrequency = [];
        blueActionFrequencies = [];
        blueActionOptimalFrequencies = [];
    end
    
    if isfield(blueActionSelectionData, 'normativeParticipantActions')
        blueActionParticipantNormativeFrequencies = cell(1, numLocations);
        for location = 1:numLocations
            blueActionParticipantNormativeFrequencies{location} = zeros(numTrials, 2);
        end
        if isfield(blueActionSelectionData, 'normativeBayesianActions')
            blueActionParticipantNormativeOptimalFrequencies = zeros(numLocations, numTrials);
        else
            blueActionParticipantNormativeOptimalFrequencies = [];
        end    
    end    
    
    %Compute percent of subjects who chose divert and not divert for each
    %trial, and the overall average frequency of not divert selections, and
    %the percent of participant choices that were optimal given the Bayesian probabilities. 
    %Also, compute the percent of participant normative choices that were divert and
    %not divert for each trial, and the percent of participant choices that were optimal
    %given the participant probabilities.    
    if ~isempty(blueActionFrequencies)
        for subject = 1:numSubjects
            for trial = 1:numTrials
                blueActions = blueActionSelectionData.selectedActions{subject, trial};
                if ~isempty(blueActionParticipantNormativeFrequencies)
                    participantNormativeActions = blueActionSelectionData.normativeParticipantActions{subject, trial};
                else
                    participantNormativeActions = [];
                end
                if ~isempty(blueActionOptimalFrequencies)
                    optimalActions = blueActionSelectionData.normativeBayesianActions{subject, trial};                    
                else
                    optimalActions = [];
                end
                
                for location = 1:numLocations
                    if blueActions(location) == 2
                        notDivertFrequency = notDivertFrequency + 1;
                    end
                    
                    blueActionFrequencies{location}(trial, blueActions(location)) = ...
                        blueActionFrequencies{location}(trial, blueActions(location)) + 1;
                    
                    if ~isempty(optimalActions)
                        if blueActions(location) == optimalActions(location)
                            blueActionOptimalFrequencies(location, trial) = ...
                                blueActionOptimalFrequencies(location, trial) + 1;
                        end
                    end
                    
                    if ~isempty(participantNormativeActions)
                        blueActionParticipantNormativeFrequencies{location}(trial, participantNormativeActions(location)) = ...
                            blueActionParticipantNormativeFrequencies{location}(trial, participantNormativeActions(location)) + 1;                        
                        
                        if blueActions(location) == participantNormativeActions(location)
                            blueActionParticipantNormativeOptimalFrequencies(location, trial) = ...
                                blueActionParticipantNormativeOptimalFrequencies(location, trial) + 1; %#ok<*AGROW>
                        end
                    end                   
                end
            end
        end
        
        for location = 1:numLocations
            blueActionFrequencies{location} = blueActionFrequencies{location} / numSubjects;
            if ~isempty(blueActionParticipantNormativeFrequencies)
                blueActionParticipantNormativeFrequencies{location} = ...
                    blueActionParticipantNormativeFrequencies{location} / numSubjects;
            end
        end
        if ~isempty(blueActionOptimalFrequencies)
            blueActionOptimalFrequencies = ...
                blueActionOptimalFrequencies / numSubjects;
        end
        if ~isempty(blueActionParticipantNormativeOptimalFrequencies)
            blueActionParticipantNormativeOptimalFrequencies = ...
                blueActionParticipantNormativeOptimalFrequencies / numSubjects;
        end
    end
    notDivertFrequency = notDivertFrequency / (numSubjects * numTrials * numLocations);
    %blueActionFrequencies{1}
    %blueActionOptimalFrequencies
    %blueActionParticipantNormativeFrequencies{1}
    %blueActionParticipantNormativeOptimalFrequencies
end

end