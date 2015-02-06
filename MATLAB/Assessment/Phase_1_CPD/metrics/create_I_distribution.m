function I = create_I_distribution(probs)
%CREATE_I_DISTRIBUTION Create I distribution from the given probs. Used in
%probability matching calculations for Tasks 4-6
%   Detailed explanation goes here

I = zeros(1, length(probs));
[~, maxProbIndex] = max(probs);
I(maxProbIndex) = 1;
end