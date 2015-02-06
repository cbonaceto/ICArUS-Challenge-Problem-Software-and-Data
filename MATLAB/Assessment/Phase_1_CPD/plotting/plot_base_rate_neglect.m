function plot_base_rate_neglect(subjects, subjectProbs, normativeProbs,...
    subjectGroupCenters, normativeGroupCenters, subjectDistances,...
    probeAttackLocationFiles, taskNum, saveData, showPlots, dataFolder)
%PLOT_BASE_RATE_NEGLECT Plot base rate neglect for Tasks 2 and 3. Plots the
%average negentropy of the subject and normative base rates for each trial.
%   Detailed explanation goes here

if taskNum ~= 2 && taskNum ~= 3
    return;
end

if ~exist('saveData', 'var')
    saveData = false;
end
if saveData && (~exist('dataFolder', 'var') || isempty(dataFolder))
    dataFolder = '';
end
if ~exist('showPlots', 'var') || showPlots
    visible = 'on';
else 
    visible = 'off';
end

%% Load the attack locations for each trial for Task 2
numSubjects = size(subjectProbs, 1);
numTrials = size(subjectProbs, 2);
numGroups = size(subjectGroupCenters, 3);
if taskNum == 2 && ~isempty(probeAttackLocationFiles)
    attackLocations = cell(length(probeAttackLocationFiles),1);
    trialNum = 1;
    for trial = 1:length(probeAttackLocationFiles)    
        %disp(probeAttackLocationFiles{trial});
        [~, attackLocations{trialNum}, ~] = read_feature_vector_file(probeAttackLocationFiles{trial});
        %attackLocations{trialNum}
        for i = 1:length(attackLocations{trialNum})
            if isstruct(attackLocations{trialNum}{i})
                attackLocations{trialNum} = attackLocations{trialNum}{i};
                break;
            end
        end
        %attackLocations{trialNum}
        trialNum = trialNum + 1;
    end
end

%% Compute subject base rates, and compute negentropy of subject and normative base rates
sigmaMultiple = 1.4823; %Multiple of sigma for 2-to-1 boundary for 2D Gaussian
subjectBaseRateNe = zeros(numSubjects, numTrials);
normativeBaseRateNe = zeros(numSubjects, numTrials);
%computeLikelihoodTask2(28, 31, 44.57142857142857, 43.714285714285715, 21.09744157039726, 21.09744157039726)
%computeLikelihoodTask3(10)
%computeBaseRates([.40 .40 .10 .10], [.25 .25 .25 .25])
%posteriors = [.25 .25 .25 .25];
%likelihoods = [.80 .80 .20 .20];
%priors = computeBaseRates(likelihoods, posteriors)
%computePosteriors(priors, likelihoods)
allSubjectBaseRateNe = zeros(1, numSubjects * numTrials);
i = 1;
for subject = 1:numSubjects
    for trial = 1:numTrials
        %disp(['-- Trial ', num2str(trial), ' --']);
        subjectLikelihoods = zeros(1, numGroups);                
        normativeLikelihoods = zeros(1, numGroups);
        normativeBaseRates = zeros(1, numGroups);
        if taskNum == 2
            attackLocation = attackLocations{trial};
            %disp(['Attack Location: (', num2str(attackLocation.x), ',', num2str(attackLocation.y), ')']);
        end
        for group = 1:numGroups
            if taskNum == 2
                % For Task 2, compute subject base rate based on group center location and radius
                normativeData = normativeGroupCenters{subject, trial, group};
                subjectData = subjectGroupCenters{subject, trial, group};   
                subjectSigmaXY = subjectData.radius / sigmaMultiple;
                subjectLikelihoods(group) = computeLikelihoodTask2(subjectData.x, subjectData.y,...
                    attackLocation.x, attackLocation.y, subjectSigmaXY, subjectSigmaXY);                
                %Get normative base rate
                normativeBaseRates(group) = normativeGroupCenters{subject, trial, group}.base_rate;
                %Get normative likelihood
                normativeLikelihoods(group) = computeLikelihoodTask2(normativeData.x, normativeData.y,...
                    attackLocation.x, attackLocation.y, normativeData.sigmaX, normativeData.sigmaY);
%                 disp(['Group ', num2str(group),...
%                     ', Normative Center: (', num2str(normativeData.x), ',', num2str(normativeData.y), ')',...
%                     ', Sigmas: (', num2str(normativeData.sigmaX), ',', num2str( normativeData.sigmaY), ')',...
%                     ', Likelihood: ', num2str(normativeLikelihoods(group)),...
%                     ', Posterior: ', num2str(normativeProbs{subject, trial}(group))]);
            else
                %For Task 3, compute subject base rate based on distance from
                %subject center to attack location using the HUMINT Gaussian to
                %compute the likelihood
                subjectLikelihoods(group) = computeLikelihoodTask3(subjectDistances(subject, trial, group));                
                %Get normative base rate
                normativeBaseRates(group) = normativeGroupCenters{subject, trial, group}.base_rate;
            end
        end
        %disp([subject, trial, subjectLikelihoods]);
        %subjectLikelihoods
        %adjust_probs(subjectLikelihoods, 0.0001, true)
        %subjectProbs{subject, trial}
        subjectBaseRates = computeBaseRates(subjectLikelihoods, subjectProbs{subject, trial});
        %subjectBaseRates*100
        %subjectBaseRates1 = computeBaseRates(adjust_probs(subjectLikelihoods, 0.0001, true),...
        %   subjectProbs{subject, trial})
        %computePosteriors(subjectBaseRates, adjust_probs(subjectLikelihoods, 0.0001, true))
        %computePosteriors(subjectBaseRates, subjectLikelihoods)
        %subjectBaseRates2 = adjust_probs(computeBaseRates(subjectLikelihoods, subjectProbs{subject, trial}), 0.0001, true)
        %disp('--')
        if taskNum == 2
            %normativeBaseRates = computeBaseRates(normativeLikelihoods, normativeProbs{subject, trial});
            %normativeBaseRates = computeBaseRates(adjust_probs(normativeLikelihoods, 0.0001, true),...
            %    normativeProbs{subject, trial});
            %adjust_probs(normativeLikelihoods, 0.0001, true)                      
            %normativeBaseRates*100
            %computePosteriors(normativeBaseRates, adjust_probs(normativeLikelihoods, 0.0001, true))
            %disp('--')
        end
        %normativeBaseRates        
        subjectBaseRateNe(subject, trial) = negentropy(subjectBaseRates);
        allSubjectBaseRateNe(i) = subjectBaseRateNe(subject, trial);
        normativeBaseRateNe(subject, trial) = negentropy(normativeBaseRates);
        i = i+1;
    end
end
subjectBaseRateNeAvg = sum(subjectBaseRateNe)/size(subjectBaseRateNe, 1);
normativeBaseRateNeAvg = sum(normativeBaseRateNe)/size(normativeBaseRateNe, 1);
Bp = sum(subjectBaseRateNeAvg)/length(subjectBaseRateNeAvg);
%subjectBaseRateNe
%std(subjectBaseRateNe)
Bp_e = std(allSubjectBaseRateNe);
Bq = sum(normativeBaseRateNeAvg)/length(normativeBaseRateNeAvg);

%% Create plot showing the negentropy of subject and normative base rates for each trial
figPosition = [60, 60, 800, 600];
taskName = ['Mission ' num2str(taskNum)];
figName = [taskName, ', All Trials, Base Rate Negentropy, Bp=', num2str(Bp), ', STD=', num2str(Bp_e),', Bq=', num2str(Bq),' [Sample Size: ', num2str(numSubjects), ']'];
figHandle = figure('name', figName, 'position', figPosition, 'NumberTitle', 'off', 'Visible', visible);
hold on;
title(figName);
xlabel('Trial');
xlim([0 numTrials+1]);
set(gca,'xtick', 1:numTrials);
ylabel('Negentropy, Human (blue), Bayesian (black)');
%ymax = ylim;
%ylim([0 ymax(2)]);
%Plot average subject base rate negentropy as a blue solid line
plot(subjectBaseRateNeAvg, '-ob');
%Plot normative base rate negentropy as a black solid line
normativeXOffset = 0;
plot((1+normativeXOffset):(numTrials+normativeXOffset), normativeBaseRateNeAvg, '-*k');
%Plot error bars that show the std for the subject base rate negentropy distrubution
%at each trial
errorbar(1:numTrials, subjectBaseRateNeAvg, std(subjectBaseRateNe), '-b');
%Also show normative base rate negentropy distribution
%showNormativeVariance = false;
%if showNormativeVariance
%    errorbar((1+normativeXOffset):(numTrials+normativeXOffset), normativeBaseRateNeAvg,...
%        std(normativeBaseRateNe), '-k');
%end
%Save figure
if saveData  
    fileName = [dataFolder, '\', taskName,'_BaseRate_Negentropy'];
    saveas(figHandle, fileName, 'png');
end
end

%% Helper functions
% Compute subject likelihood for Task 2
function likelihood = computeLikelihoodTask2(subjectX, subjectY, locationX, locationY, sigmaX, sigmaY)
theta = 0;
A = 1/(2 * pi * sigmaX * sigmaY);
%A = 1;
a = cos(theta)^2/2/sigmaX^2 + sin(theta)^2/2/sigmaY^2;
b = -sin(2*theta)/4/sigmaX^2 + sin(2*theta)/4/sigmaY^2;
c = sin(theta)^2/2/sigmaX^2 + cos(theta)^2/2/sigmaY^2;
likelihood = A * exp(-(a*(subjectX-locationX)^2 + 2*b*(subjectX-locationX)*(subjectY-locationY) + c*(subjectY-locationY)^2));
if isnan(likelihood)
    %If the likelihood is not a number, use 0 as the likelihood. This would happen
    %if sigma is undefined if there were fewer than two attacks for the group.
    likelihood = 0;
end
end

%Compute subject likelihood for Task 3
function likelihood = computeLikelihoodTask3(subjectDistance)
%peakHeight_a * Math.exp(-((Math.pow(x-peakCenter_b, 2)/(2 * sigma_c * sigma_c))));
a = 0.4;
b = 0;
c = 10;
likelihood = a * exp(-((subjectDistance-b)^2)/(2 * c^2));
end

% Compute base rates (priors) given likelihoods and posteriors
function baseRates = computeBaseRates(likelihoods, posteriors)
divisor = sum(posteriors./likelihoods);
baseRates = (posteriors./likelihoods)/divisor;
end

% Compute posteriors given likelihoods and priors (base rates)
function posteriors = computePosteriors(priors, likelihoods)
posteriors = priors .* likelihoods;
posteriors = posteriors/sum(posteriors); %normalize 
end