function s = similarity(humanProbs, epsilon, nullModelProbs)
%SIMILARITY Compute KLD-based similiarity between humanProbs and
%nullModelProbs (Spr when uniform probs, Spq when normative probs)
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

s = 100 * 2^(-1 * KLdivergence(humanProbs, nullModelProbs, epsilon));

end

