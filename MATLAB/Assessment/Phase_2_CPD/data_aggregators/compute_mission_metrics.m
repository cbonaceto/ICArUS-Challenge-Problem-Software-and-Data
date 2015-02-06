function [mission_metrics] = compute_mission_metrics(intData, mostLikelyRedTacticsData,...
        redTacticProbsData, batchPlotData, sigintSelectionData, subjectProbs, normativeProbs,...
        attackProbStages, blueActionSelectionData)
%COMPUTE_MISSION_METRICS Summary of this function goes here
%   Detailed explanation goes here

numSubjects = size(subjectProbs, 1); %The number of subjects
numTrials = size(subjectProbs, 2); %The number of trials in the mission
numStages = length(attackProbStages); %The number of Red attack probability assessment stages
numLocations = size(subjectProbs{1,1}, 2) - 1; %The number of locations on each trial

if ~isempty(mostLikelyRedTacticsData)
    numTactics = length(mostLikelyRedTacticsData.tacticTypes);
    mostLikelyTacticSelections = zeros(numTrials, numTactics); %The frequency of subjects who selected each Red tactic type as most likely (by trial->tactic) (Mission 2 only)
else
    mostLikelyTacticSelections = [];
end

if ~isempty(redTacticProbsData)
    numTactics = length(redTacticProbsData.tacticTypes);        
    redTacticProbsAvg = zeros(numTrials, numTactics); %The average probability (accross subjects) of each Red tactic    
    redTacticNe = zeros(numSubjects, numTrials);
    redTacticNeAvg = zeros(1, numTrials);    
    if isfield(redTacticProbsData, 'normativeTacticProbs')
        normativeRedTacticProbsAvg = zeros(numTrials, numTactics);
        normativeRedTacticNe = zeros(numSubjects, numTrials);
        normativeRedTacticNeAvg = zeros(1, numTrials);    
    else 
        normativeRedTacticProbsAvg = [];
        normativeRedTacticNe = [];
        normativeRedTacticNeAvg = [];    
    end
else
    redTacticProbsAvg = [];
    redTacticNe = [];
    redTacticNeAvg = [];
    normativeRedTacticProbsAvg = [];
    normativeRedTacticNe = [];
    normativeRedTacticNeAvg = [];    
end

if ~isempty(batchPlotData)     
    %Total number of trials on which a batch plot could have been created
    numBatchPlotsPossible = 0; 
    %Trials on which a batch plot could have been created    
    batchPlotPossibleTrials = zeros(1, numTrials);
    %numBatchPlots = 0;    
    batchPlotCreationTrialsBySubject = cell(numSubjects, 1);    
    batchPlotSearchDepthsBySubject = cell(numSubjects, 1);    
    for subject = 1:numSubjects
        batchPlotCreationTrialsBySubject{subject} = zeros(1, numSubjects);
        batchPlotSearchDepthsBySubject{subject} = zeros(1, numTrials);        
    end
else
    batchPlotCreationTrialsBySubject = [];
    batchPlotSearchDepthsBySubject = [];
end

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
    subjectPtProbs = [];
    normativePtProbs = [];
    subjectPtProbsAvg = [];
    normativePtProbsAvg = [];
    chatterAvg = [];
    silentAvg = [];
end

epsilon = 0.01;
subjectProbsAvg = cell(numTrials, 1); %Average subject probabilities for each stage
normativeProbsAvg = cell(numTrials, 1); %Average normative probabilities for each stage
subjectProbsMedian = cell(numTrials, 1); %Median of the subject probabilities for each stage
subjectProbsByStage = cell(numStages, numLocations);
normativeProbsByStage = cell(numStages, numLocations);
subjectProbsAvgByStage = cell(numStages, numLocations);
subjectProbsMedianByStage = cell(numStages, numLocations);
normativeProbsAvgByStage = cell(numStages, numLocations);
subjectNe = cell(1, numStages);
subjectNeAvg = zeros(numStages, numTrials); %Subject negentropy based on the mean subject probabilities
subjectNeMedian = zeros(numStages, numTrials); %Subject negentropy based on the median subject probabilities
normativeNe = cell(1, numStages);
normativeNeAvg = zeros(numStages, numTrials);
subjectDeltaNe = cell(1, numStages);
normativeDeltaNe = cell(1, numStages);
for trial = 1:numTrials
    subjectProbsAvg{trial} = zeros(numStages, numLocations + 1);
    subjectProbsMedian{trial} = zeros(numStages, numLocations + 1);
    normativeProbsAvg{trial} = zeros(numStages, numLocations + 1);    
end
for stage = 1:numStages    
    subjectNe{stage} = zeros(numSubjects, numTrials);
    subjectDeltaNe{stage} = zeros(numSubjects, numTrials);
    normativeNe{stage} = zeros(numSubjects, numTrials);  
    normativeDeltaNe{stage} = zeros(numSubjects, numTrials);
    for location = 1:numLocations
        subjectProbsByStage{stage, location} = zeros(numSubjects, numTrials);
        normativeProbsByStage{stage, location} = zeros(numSubjects, numTrials);
        subjectProbsAvgByStage{stage, location} = zeros(1, numTrials);
        subjectProbsMedianByStage{stage, location} = zeros(1, numTrials);
        normativeProbsAvgByStage{stage, location} = zeros(1, numTrials);
    end
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

for trial = 1:numTrials
    for stage = 1:numStages
        subjectProbsMedianTemp = zeros(numSubjects, numLocations+1);       
        for subject = 1:numSubjects            
            %Compute average and median attack probabilities           
            subjectProbsMedianTemp(subject, :) = subjectProbs{subject, trial}(stage, :);
            if stage == 1
                subjectProbsAvg{trial} = subjectProbsAvg{trial} + subjectProbs{subject, trial};               
                normativeProbsAvg{trial} = normativeProbsAvg{trial} + normativeProbs{subject, trial};
            end
            for location = 1:numLocations                
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
                subjectProbsAvgByStage{stage, location}(trial) = subjectProbsAvgByStage{stage, location}(trial) + ...
                    subjectProbsByStage{stage, location}(subject, trial);
                normativeProbsAvgByStage{stage, location}(trial) = normativeProbsAvgByStage{stage, location}(trial) + ...
                    normativeProbsByStage{stage, location}(subject, trial);                
                %subjectProbsAvgByStage{stage, location} = subjectProbsAvgByStage{stage, location} + ...
                %    subjectProbsByStage{stage, location}(subject, :);
                %normativeProbsAvgByStage{stage, location} = normativeProbsAvgByStage{stage, location} + ...
                %    normativeProbsByStage{stage, location}(subject, :);
                if stage == ptStage                    
                    for sigintType = 1:2
                        subjectPtProbsAvg{sigintType, location}(trial) = ...
                            subjectPtProbsAvg{sigintType, location}(trial) + ...
                            subjectPtProbs{sigintType, location}(subject, trial);
                        normativePtProbsAvg{sigintType, location}(trial) = ...
                            normativePtProbsAvg{sigintType, location}(trial) + ...
                            normativePtProbs{sigintType, location}(subject, trial);
                        %subjectPtProbsAvg{sigintType, location}(1, :) = ...
                        %    subjectPtProbsAvg{sigintType, location}(1, :) + subjectPtProbs{sigintType, location}(subject, :);
                        %normativePtProbsAvg{sigintType, location}(1, :) = ...
                        %    normativePtProbsAvg{sigintType, location}(1, :) + normativePtProbs{sigintType, location}(subject, :);
                    end
                end
            end
            %End of compute average attack probabilities
            
            %Compute negentropies of the attack probabilities                        
            p_subj = subjectProbs{subject, trial}(stage, :);
            p_subj = fillzerobins(p_subj, epsilon);
            subjectNe{stage}(subject, trial) = negentropy(p_subj);            
            p_norm = normativeProbs{subject, trial}(stage, :);
            p_norm = fillzerobins(p_norm, epsilon);
            normativeNe{stage}(subject, trial) = negentropy(p_norm);
            %Compute delta Ne
            if stage > 1
                subjectDeltaNe{stage}(subject, trial) = ...
                    subjectNe{stage}(subject, trial) - subjectNe{stage-1}(subject, trial);
                normativeDeltaNe{stage}(subject, trial) = ...
                    normativeNe{stage}(subject, trial) - normativeNe{stage-1}(subject, trial);
            end
            %End of compute negentropies of the attack probabilities
            
            if stage == 1
                %Compute frequency of subjects who selected each Red tactic type as most likely
                if ~isempty(mostLikelyRedTacticsData)
                    tactic = mostLikelyRedTacticsData.selectedTactic(subject, trial);
                    if ~isnan(tactic) && tactic > 0
                        mostLikelyTacticSelections(trial, tactic) = mostLikelyTacticSelections(trial, tactic) + 1;
                    end
                end
                
                %Compute average Red tactic probabilities
                if ~isempty(redTacticProbsData)
                    redTacticProbs = redTacticProbsData.tacticProbs{subject, trial};
                    redTacticProbsAvg(trial, :) = redTacticProbsAvg(trial, :) + redTacticProbs;                    
                    redTacticNe(subject, trial) = negentropy(redTacticProbs);
                    if ~isempty(normativeRedTacticProbsAvg)
                        normativeRedTacticProbs = redTacticProbsData.normativeTacticProbs{subject, trial};
                        normativeRedTacticProbsAvg(trial, :) = ...
                            normativeRedTacticProbsAvg(trial, :) + normativeRedTacticProbs;                    
                        normativeRedTacticNe(subject, trial) = ...
                            negentropy(normativeRedTacticProbs);                        
                    end
                end
                
                %For batch plots, aggregate the trials on which subjects made each
                %batch plot (e.g., first batch blot was created on trials 1, 4, 3, etc...)
                %, and compute the average trial. Also compute the depth of search
                %(percent of trials reviewed) for each batch plot created and the
                %average depth of search.
                if ~isempty(batchPlotData)
                    numTrialsReviewed = batchPlotData{subject, trial}(1);
                    numTrialsPossibleToReview = batchPlotData{subject, trial}(2);
                    %if ~isnan(numTrialsReviewed) && numTrialsReviewed > 0
                    if ~isnan(numTrialsPossibleToReview) && numTrialsPossibleToReview > 0
                        if isnan(numTrialsReviewed) 
                            numTrialsReviewed = 0;                        
                        elseif numTrialsReviewed > 0
                            batchPlotCreationTrialsBySubject{subject}(trial) = 1;
                        end
                        batchPlotSearchDepthsBySubject{subject}(trial) = ...
                            numTrialsReviewed / numTrialsPossibleToReview;                        
                        if subject == 1
                            numBatchPlotsPossible = numBatchPlotsPossible + 1;
                            batchPlotPossibleTrials(trial) = 1;
                        end
                        %numBatchPlots = numBatchPlots + 1;
                    end                    
                end
                
                %Compute SIGINT selection frequencies
                %Compute percent of subjects who chose to get SIGINT at each location on
                %each trial. Also compute the percent of participant SIGINT choices that were
                %optimal given the Bayesian probabilities, and the percent of participant
                %SIGINT selections that were optimal given the participant probabilities.
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
                
                %Compute Blue action selection frequencies.
                %Compute percent of subjects who chose divert and not divert for each
                %trial, and the overall average frequency of not divert selections, and
                %the percent of participant choices that were optimal given the Bayesian probabilities.
                %Also, compute the percent of participant normative choices that were divert and
                %not divert for each trial, and the percent of participant choices that were optimal
                %given the participant probabilities.
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
            end %End if stage==1
        end %subjects loop
        
        %Compute average and median attack probabilities for the current stage
        %disp(['Subject probs for trial ' num2str(trial) ', stage ' num2str(stage) ':']);
        %subjectProbsMedianTemp(:, 1)
        subjectProbsMedian{trial}(stage, :) = fillzerobins(median(subjectProbsMedianTemp), epsilon);        
        for location = 1:numLocations            
            subjectProbsAvgByStage{stage, location}(trial) = subjectProbsAvgByStage{stage, location}(trial)/numSubjects;
            subjectProbsMedianByStage{stage, location}(trial) = subjectProbsMedian{trial}(stage, location);
            normativeProbsAvgByStage{stage, location}(trial) = normativeProbsAvgByStage{stage, location}(trial)/numSubjects;
            %subjectProbsAvgByStage{stage, location} = subjectProbsAvgByStage{stage, location}/numSubjects;
            %normativeProbsAvgByStage{stage, location} = normativeProbsAvgByStage{stage, location}/numSubjects;
            if stage == ptStage
                for sigintType = 1:2
                    subjectPtProbsAvg{sigintType, location}(trial) = ...
                        subjectPtProbsAvg{sigintType, location}(trial)/numSubjects;
                    normativePtProbsAvg{sigintType, location}(trial) = ...
                        normativePtProbsAvg{sigintType, location}(trial)/numSubjects;
                    %subjectPtProbsAvg{sigintType, location}(1, :) = ...
                    %    subjectPtProbsAvg{sigintType, location}(1, :)/numSubjects;
                    %normativePtProbsAvg{sigintType, location}(1, :) = ...
                    %    normativePtProbsAvg{sigintType, location}(1, :)/numSubjects;
                end
            end            
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
        end %locations loop
        %End compute average attack probabilities for the current stage        
    end %stages loop    
    
    %Compute average attack probabilities and negentropies for the current trial    
    subjectProbsAvg{trial} = subjectProbsAvg{trial} / numSubjects;
    normativeProbsAvg{trial} = normativeProbsAvg{trial} / numSubjects;
    for stage = 1:numStages        
        subjectProbsAvg{trial}(stage, :) = fillzerobins(subjectProbsAvg{trial}(stage, :), epsilon);
        normativeProbsAvg{trial}(stage, :) = fillzerobins(normativeProbsAvg{trial}(stage, :), epsilon);
        %subjectNeAvgByStage(trial, stage) = sum(subjectNe(:, trial, stage)) / numSubjects;
        %normativeNeAvgByStage(trial, stage) = sum(normativeNe(:, trial, stage)) / numSubjects;
        
        %Compute average negentropies for each stage of the current trial       
        p_subj = subjectProbsAvg{trial}(stage, :);
        p_subj = fillzerobins(p_subj, epsilon);        
        subjectNeAvg(stage, trial) = negentropy(p_subj);
        subjectNeMedian(stage, trial) = negentropy(subjectProbsMedian{trial}(stage, :));
        p_norm = normativeProbsAvg{trial}(stage, :);        
        p_norm = fillzerobins(p_norm, epsilon);
        normativeNeAvg(stage, trial) = negentropy(p_norm);
    end
    %End compute average attack probabilities and negentropies for the current trial    
end %trials loop

%DEBUG CODE
% for trial = 1:numTrials
%     disp(['Subject probs avg for trial ' num2str(trial) ':']);
%     subjectProbsAvg{trial}
%     disp(['Subject probs median for trial ' num2str(trial) ':']);
%     subjectProbsMedian{trial}
%     for stage = 1:numStages
%         disp(['Subject Ne median for trial ' num2str(trial) ', stage ' num2str(stage) ':']);
%         subjectNeMedian(stage, trial)
%     end
% end
% for stage = 1:numStages
%     for location = 1:numLocations
%         disp(['Subject probs avg for stage ' num2str(stage) ', location ' num2str(location) ':']);
%         subjectProbsAvgByStage{stage, location}
%         disp(['Subject probs median for stage ' num2str(stage) ', location ' num2str(location) ':']);
%         subjectProbsMedianByStage{stage, location}
%         %if ~isempty(subjectPtProbsAvg) 
%         %    subjectPtProbsAvg{1, location}
%         %    normativePtProbsAvg{1, location}
%         %end
%     end
% end      
% %END DEBUG CODE

if ~isempty(mostLikelyRedTacticsData)
    mostLikelyTacticSelections = mostLikelyTacticSelections / numSubjects;
end

if ~isempty(redTacticProbsData)
    redTacticProbsAvg = redTacticProbsAvg / numSubjects;        
    redTacticNeAvg = negentropy(redTacticProbsAvg);
    if ~isempty(normativeRedTacticProbsAvg)
        normativeRedTacticProbsAvg = normativeRedTacticProbsAvg / numSubjects;        
        normativeRedTacticNeAvg = negentropy(normativeRedTacticProbsAvg);
    end
end

if ~isempty(batchPlotData)
    %Compute average batch plot creation trials and search depths        
    maxNumBatchPlotsPerSubject = 0;
    for subject = 1:numSubjects
        batchPlotCreationTrialsBySubject{subject} = find(batchPlotCreationTrialsBySubject{subject} > 0);        
        %subjectNumBatchPlots = length(batchPlotCreationTrialsBySubject{subject});        
        subjectNumBatchPlots = length(find(batchPlotSearchDepthsBySubject{subject} > 0));        
        if subjectNumBatchPlots > maxNumBatchPlotsPerSubject
            maxNumBatchPlotsPerSubject = subjectNumBatchPlots;
        end
    end
    %Batch plot search depths on all trials on which a batch plot could have been created
    batchPlotSearchDepthsAllTrials = cell(numBatchPlotsPossible, 1);   
    %Batch plot search depths on trials on which a batch plot was created    
    batchPlotSearchDepths = cell(maxNumBatchPlotsPerSubject, 1); 
    %Trials on which a batch plot was created
    batchPlotCreationTrials = cell(maxNumBatchPlotsPerSubject, 1);
    batchPlotNum = 1;
    %for batchPlotNum = 1:maxNumBatchPlotsPerSubject
    %for batchPlotNum = 1:numBatchPlotsPossible    
    for batchPlotTrial = find(batchPlotPossibleTrials == 1)    
        batchPlotSearchDepthsAllTrials{batchPlotNum} = zeros(numSubjects, 1);
        if batchPlotNum <= maxNumBatchPlotsPerSubject
            batchPlotSearchDepths{batchPlotNum} = zeros(numSubjects, 1);
            batchPlotCreationTrials{batchPlotNum} = zeros(numSubjects, 1);
        end
        for subject = 1:numSubjects
            batchPlotSearchDepthsAllTrials{batchPlotNum}(subject) = ...
                batchPlotSearchDepthsBySubject{subject}(batchPlotTrial);            
            if batchPlotNum <= maxNumBatchPlotsPerSubject
                searchDepths = batchPlotSearchDepthsBySubject{subject}(...
                    batchPlotSearchDepthsBySubject{subject} > 0);
                if length(searchDepths) >= batchPlotNum
                    batchPlotSearchDepths{batchPlotNum}(subject) = searchDepths(batchPlotNum);
                end
                if length(batchPlotCreationTrialsBySubject{subject}) >= batchPlotNum
                    batchPlotCreationTrials{batchPlotNum}(subject) = ...
                        batchPlotCreationTrialsBySubject{subject}(batchPlotNum);
                end
            end
        end
        batchPlotNum = batchPlotNum + 1;
    end    
else
    batchPlotSearchDepthsAllTrials = [];
    batchPlotSearchDepths = [];    
    batchPlotCreationTrials = [];
end

if ~isempty(sigintSelectionData)
    sigintSelectionFrequencies = sigintSelectionFrequencies / numSubjects;
    sigintSelectionOptimalFrequencies = sigintSelectionOptimalFrequencies / numSubjects;
    sigintSelectionParticipantNormativeFrequencies = ...
        sigintSelectionParticipantNormativeFrequencies / numSubjects;
    sigintSelectionParticipantNormativeOptimalFrequencies = ...
        sigintSelectionParticipantNormativeOptimalFrequencies / numSubjects;
    if ~isempty(chatterAvg)
        chatterAvg = chatterAvg / (numSubjects * length(chatterTrials));
        silentAvg = silentAvg / (numSubjects * length(silentTrials));
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
notDivertFrequency = notDivertFrequency / (numSubjects * numTrials * numLocations);

mission_metrics.numSubjects = numSubjects;
mission_metrics.numTrials = numTrials;
mission_metrics.numStages = numStages;
mission_metrics.numLocations = numLocations; 
mission_metrics.mostLikelyTacticSelections = mostLikelyTacticSelections;
mission_metrics.redTacticProbsAvg = redTacticProbsAvg;
mission_metrics.redTacticNe = redTacticNe;
mission_metrics.redTacticNeAvg = redTacticNeAvg;
mission_metrics.normativeRedTacticProbsAvg = normativeRedTacticProbsAvg;
mission_metrics.normativeRedTacticNe = normativeRedTacticNe;
mission_metrics.normativeRedTacticNeAvg = normativeRedTacticNeAvg;
mission_metrics.batchPlotCreationTrials = batchPlotCreationTrials;
mission_metrics.batchPlotCreationTrialsBySubject = batchPlotCreationTrialsBySubject;
mission_metrics.batchPlotSearchDepthsAllTrials = batchPlotSearchDepthsAllTrials;
mission_metrics.batchPlotSearchDepths = batchPlotSearchDepths;
mission_metrics.batchPlotSearchDepthsBySubject = batchPlotSearchDepthsBySubject;
mission_metrics.sigintSelectionFrequencies = sigintSelectionFrequencies;
mission_metrics.sigintSelectionOptimalFrequencies = sigintSelectionOptimalFrequencies;
mission_metrics.sigintSelectionParticipantNormativeFrequencies = ...
    sigintSelectionParticipantNormativeFrequencies;
mission_metrics.sigintSelectionParticipantNormativeOptimalFrequencies = ...
    sigintSelectionParticipantNormativeOptimalFrequencies;
mission_metrics.subjectProbsAvg = subjectProbsAvg;
mission_metrics.subjectProbsMedian = subjectProbsMedian;
mission_metrics.subjectProbsByStage = subjectProbsByStage;
mission_metrics.normativeProbsByStage = normativeProbsByStage;
mission_metrics.subjectProbsAvgByStage = subjectProbsAvgByStage;
mission_metrics.subjectProbsMedianByStage = subjectProbsMedianByStage;
mission_metrics.normativeProbsAvgByStage = normativeProbsAvgByStage;
mission_metrics.subjectPtProbs = subjectPtProbs;
mission_metrics.normativePtProbs = normativePtProbs;
mission_metrics.subjectPtProbsAvg = subjectPtProbsAvg;
mission_metrics.normativePtProbsAvg = normativePtProbsAvg;
mission_metrics.chatterAvg = chatterAvg;
mission_metrics.silentAvg = silentAvg;
mission_metrics.subjectNe = subjectNe;
mission_metrics.subjectNeAvg = subjectNeAvg; 
mission_metrics.subjectNeMedian = subjectNeMedian;
mission_metrics.subjectDeltaNe = subjectDeltaNe;
mission_metrics.normativeNe = normativeNe;
mission_metrics.normativeNeAvg = normativeNeAvg; 
mission_metrics.normativeDeltaNe = normativeDeltaNe;
mission_metrics.notDivertFrequency = notDivertFrequency;
mission_metrics.blueActionFrequencies = blueActionFrequencies; 
mission_metrics.blueActionOptimalFrequencies = blueActionOptimalFrequencies;
mission_metrics.blueActionParticipantNormativeFrequencies = ...
    blueActionParticipantNormativeFrequencies;
mission_metrics.blueActionParticipantNormativeOptimalFrequencies = ...
    blueActionParticipantNormativeOptimalFrequencies;
end