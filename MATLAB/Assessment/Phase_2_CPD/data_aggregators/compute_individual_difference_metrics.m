function [inferencingScore, decisionMakingScore, foragingScore] = ...
    compute_individual_difference_metrics(subjects, missionData,...
    inferencingMissions, decisionMakingMissions, foragingMissions)
%COMPUTE_INDIVIDUAL_DIFFERENCE_METRICS Computes the inferencing score,
%decision-making score, and foraging score for each subject

%% Compute the dependent variables (inferencing skill, decision-making skill,
%  and foraging skill) for each subject
numMissions = length(missionData);
numSubjects = length(subjects);
inferencingScore = zeros(numSubjects, 1);
numInferencingTrials = 0;
decisionMakingScore = zeros(numSubjects, 1);
numDecisionMakingTrials = 0;
foragingScore = zeros(numSubjects, 1);
numForagingTrials = 0;
epsilon = 0.01;
for i = 1:numMissions
    numTrials = size(missionData{i}.timeDataByTrial, 2);
    if ~isempty(find(inferencingMissions == i, 1))
        %subjectAttackProbs{subjectNum, trialNum} = zeros(numAttackProbsStages, numLocations);
        subjectProbs = missionData{i}.subjectAttackProbs;
        normativeProbs = missionData{i}.normativeAttackProbs;
        lastStage = size(subjectProbs{1, 1}, 1);
        numInferencingTrials = numInferencingTrials + numTrials;
    else
        subjectProbs = [];
        normativeProbs = [];
    end
    
    if ~isempty(find(decisionMakingMissions == i, 1))
        %blueActionSelectionData.selectedActions{subject, trial} = zeros(1, numLocations);
        %blueActionSelectionData.normativeBayesianActions{subjectNum,trialNum} = zeros(1, numLocations);
        %blueActionSelectionData.normativeParticipantActions{subjectNum,trialNum} = zeros(1, numLocations);
        subjectActions = missionData{i}.blueActionSelectionData.selectedActions;
        normativeActions = missionData{i}.blueActionSelectionData.normativeParticipantActions;
        numLocations = size(subjectActions{1, 1}, 2);
        numDecisionMakingTrials = numDecisionMakingTrials + (numTrials * numLocations);
    else
        subjectActions = [];
        normativeActions = [];
    end
    
    if ~isempty(find(foragingMissions == i, 1))
        %sigintSelectionData.selectedLocation = zeros(numSubjects, numTrials);
        %sigintSelectionData.selectionBayesianOptimal = zeros(numSubjects, numTrials);
        %sigintSelectionData.selectionParticipantOptimal = zeros(numSubjects, numTrials);
        subjectSigintLocations = missionData{i}.sigintSelectionData.selectedLocation;
        normativeSigintLocations = missionData{i}.sigintSelectionData.selectionParticipantOptimal;
        numForagingTrials = numForagingTrials + numTrials;
    else
        subjectSigintLocations = [];
        normativeSigintLocations = [];
    end
    
    for subject = 1:numSubjects
        for trial = 1:numTrials
            if ~isempty(subjectProbs)
                %Inferencing skill is computed as the similarity of subject attack
                %probabilities on the last stage of a trial to the Bayesian attack
                %probabilities
                sim = similarity(subjectProbs{subject, trial}(lastStage, :),...
                    epsilon, normativeProbs{subject, trial}(lastStage, :));
                %sim = similarity(addNullHypothesisToProbs(subjectProbs{subject, trial}(lastStage, :)),...
                %    epsilon, addNullHypothesisToProbs(normativeProbs{subject, trial}(lastStage, :)));
                inferencingScore(subject) = inferencingScore(subject) + sim;
            end
            
            if ~isempty(subjectActions)
                for location = 1:numLocations
                    %The decision making score is computed as the % of trials on which the subject
                    %made an optimal decision (not divert or divert) based on the subject's probabilities.
                    if subjectActions{subject, trial}(location) == ...
                            normativeActions{subject, trial}(location)
                        decisionMakingScore(subject) = decisionMakingScore(subject) + 1;
                    end
                end
            end
            
            if ~isempty(subjectSigintLocations)
                %The foraging score is computed as the % of trials on which the subject made an optimal
                %decision about the location (1 or 2) at which to seek
                %SIGINT at based on the subject's probabilities.
                if subjectSigintLocations(subject, trial) == ...
                        normativeSigintLocations(subject, trial)
                    foragingScore(subject) = foragingScore(subject) + 1;
                end
            end
        end
    end
    
end
inferencingScore = inferencingScore / numInferencingTrials;
decisionMakingScore = decisionMakingScore / numDecisionMakingTrials;
foragingScore = foragingScore / numForagingTrials;

%%
    function p = addNullHypothesisToProbs(probs)
        p = [probs 1 - sum(probs)];
    end

end