function ne = negentropy(probs)
%NEGENTROPY Computes negative entropy ("negentropy") for the given
%normalized probabilities.
% Detailed explanation goes here

%Process MATLAB probs array
assert(size(probs, 1) > 0);
ne = zeros(size(probs, 1), 1);
me = maxentropy(length(probs(1, :)));
for i = 1:size(probs,1)
    e = entropy(probs(i, :));
    ne(i) = (me - e) / me;
end

end

