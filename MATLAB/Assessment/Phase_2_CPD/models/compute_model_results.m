function [modelProbs, modelProbsAvg, modelNe, modelNeAvg,...
    modelProbs_subjectLikelihoods, modelProbsAvg_subjectLikelihoods,...
    modelNe_subjectLikelihoods, modelNeAvg_subjectLikelihoods] = ...
    compute_model_results(subjectProbs, subjectProbsAvg,...
        subjectLikelihoodsAvg, subjectLikelihoodsAvgStage,...
        attackProbStages, redActivityDetected, modelFun)
%COMPUTE_MODEL_RESULTS Summary of this function goes here
%   Detailed explanation goes here

%Using the model, compute posteriors for each subject using subject
%likelihoods (if available) and actual likelihoods (from SIGINT). Also,
%compute Ne subject-by-subject and Ne of the average subject posteriors.

numSubjects = size(subjectProbs, 1); %The number of subjects
numTrials = size(subjectProbs, 2); %The number of trials in the mission
numStages = length(attackProbStages); %The number of Red attack probability assessment stages
numLocations = size(subjectProbs{1,1}, 2) - 1; %The number of locations on each trial

assert(isa(modelFun, 'function_handle'));
assert(numSubjects > 0 && numTrials > 0 && numStages > 1);

priorsStage = find(strcmp('Ppc', attackProbStages));
assert(~isempty(priorsStage));

likelihoodsStage = find(strcmp('Pt', attackProbStages));
if isempty(likelihoodsStage)
    likelihoodsStage = 0;
end

epsilon = 0.01;

modelProbs = cell(numSubjects, 1);
modelProbsAvg = zeros(numTrials, numLocations+1);
modelNe = zeros(numSubjects, numTrials);
modelNeAvg = zeros(numTrials, 1);
if (~isempty(subjectLikelihoodsAvg) && subjectLikelihoodsAvgStage > 0) || likelihoodsStage > 0
    modelProbs_subjectLikelihoods = cell(numSubjects, 1);
    modelProbsAvg_subjectLikelihoods = zeros(numTrials, numLocations+1);
    modelNe_subjectLikelihoods = zeros(numSubjects, numTrials);
    modelNeAvg_subjectLikelihoods = zeros(numTrials, 1);
else
    modelProbs_subjectLikelihoods = [];
    modelProbsAvg_subjectLikelihoods = [];
    modelNe_subjectLikelihoods = [];
    modelNeAvg_subjectLikelihoods = [];
end

for trial = 1:numTrials
    modelProbs{trial} = zeros(numSubjects, numLocations+1);
    if likelihoodsStage > 0
        modelProbs_subjectLikelihoods{trial} = zeros(numSubjects, numLocations+1);
    end
    
    %Compute average results using the average human priors and likelihoods
    priorsAvg = subjectProbsAvg{trial}(priorsStage, :);
    if ~isempty(subjectLikelihoodsAvg) && subjectLikelihoodsAvgStage > 0
        likelihoodsAvg = subjectLikelihoodsAvg{trial}(subjectLikelihoodsAvgStage, :);
        posteriors = fillzerobins(modelFun(priorsAvg, likelihoodsAvg), epsilon);
        modelProbsAvg_subjectLikelihoods(trial, :) = posteriors;
        modelNeAvg_subjectLikelihoods(trial) = negentropy(posteriors);
    end
    normativeLikelihoods = compute_sigint_likelihoods(redActivityDetected{1, trial});
    posteriors = fillzerobins(modelFun(priorsAvg, normativeLikelihoods), epsilon);
    modelProbsAvg(trial, :) = posteriors;
    modelNeAvg(trial) = negentropy(posteriors);    
    
    %Compute subject-by-subject results using the priors and likelihoods
    %for each subject
    for subject = 1:numSubjects      
        priors = subjectProbs{subject, trial}(priorsStage, :);
        if likelihoodsStage > 0
        %if ~isempty(subjectLikelihoodsAvg) && subjectLikelihoodsAvgStage > 0
            %subjectLikelihoods = subjectLikelihoodsAvg{trial}(subjectLikelihoodsAvgStage, :);
            subjectLikelihoods = subjectProbs{subject, trial}(likelihoodsStage, :);
            posteriors = fillzerobins(modelFun(priors, subjectLikelihoods), epsilon);
            modelProbs_subjectLikelihoods{trial}(subject, :) = posteriors;
            %modelProbsAvg_subjectLikelihoods(trial, :) = ...
            %    modelProbsAvg_subjectLikelihoods(trial, :) + posteriors;
            %modelNe_subjectLikelihoods(subject, trial) = negentropy(posteriors);
        end
        normativeLikelihoods = compute_sigint_likelihoods(redActivityDetected{subject, trial});
        posteriors = fillzerobins(modelFun(priors, normativeLikelihoods), epsilon);
        modelProbs{trial}(subject, :) = posteriors;
        %modelProbsAvg(trial, :) = modelProbsAvg(trial, :) + posteriors;
        modelNe(subject, trial) = negentropy(posteriors);
    end
    %modelProbsAvg(trial, :) = fillzerobins(modelProbsAvg(trial, :)/numSubjects, epsilon);
    %modelNeAvg(trial) = negentropy(modelProbsAvg(trial, :));
    %if likelihoodsStage > 0
%     if ~isempty(subjectLikelihoodsAvg) && subjectLikelihoodsAvgStage > 0
%         modelProbsAvg_subjectLikelihoods(trial, :) = ...
%             fillzerobins(modelProbsAvg_subjectLikelihoods(trial, :)/numSubjects, epsilon);
%         modelNeAvg_subjectLikelihoods(trial) = ...
%             negentropy(modelProbsAvg_subjectLikelihoods(trial, :));
%     end
end

end

%% Computes the probability of attack at each location given SIGINT.
%  Uses the standard SIGINT likelihoods of 60/20 (attack/
%  ~attack|chatter) and 40/80 (attack/~attack|silence)
function pt = compute_sigint_likelihoods(redActivityData)
chatterL = [.6 .2];
silentL = [.4 .8];
numLocations = length(redActivityData);
pt = zeros(1, numLocations + 1);
for i = 1:numLocations
    if redActivityData(i) == 1
        pt(i) = chatterL(1)/sum(chatterL);
    else
        pt(i) = silentL(1)/sum(silentL);
    end
end
pt(numLocations + 1) = 1 - sum(pt);
end