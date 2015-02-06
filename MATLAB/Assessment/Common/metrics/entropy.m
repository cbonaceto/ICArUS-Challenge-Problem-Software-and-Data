function e = entropy( probs )
%ENTROPY Compute entropy for the given normalized probabilities.
%   Detailed explanation goes here

assert(size(probs, 1) > 0);
e = zeros(size(probs, 1), 1);
for i = 1:size(probs, 1)
    e(i) = -sum(probs(i, :) .* log2(probs(i, :)));
end
end

