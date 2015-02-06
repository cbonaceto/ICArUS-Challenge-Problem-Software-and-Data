function rsrVal = rsr(humanProbs, modelProbs, epsilon, nullModelProbs)
%RSR Compute Relative Success Rate (RSR) match between human and model probs
%   Detailed explanation goes here

if ~exist('epsilon', 'var')
  %Use the default value for epsilon (1%, 0.01)
  epsilon = 0.01;
end

if ~exist('nullModelProbs', 'var') || isempty(nullModelProbs)
    %Use random (uniform) probs as the null model probs
    numProbs = length(humanProbs);
    nullModelProbs = repmat(1/numProbs, 1, numProbs);
end

%Compute Spm (similarity of model to human)
spm = 100 * 2^(-1 * KLdivergence(humanProbs, modelProbs, epsilon));

%Compute Spr (similarity of null model to human)
spr = 100 * 2^(-1 * KLdivergence(humanProbs, nullModelProbs, epsilon));

%RSR = (Spm-Spr)/(100-Spr) * 100
rsrVal = max([0 (spm - spr)/(100 - spr) * 100]);
end