function probs = adjust_probs(probs, epsilon, renormalize)
%ADJUST_PROBS Replace all probabilities less than epsilon with epsilon
%and all probabilities greater than 1 - epsilon with 1 - epsilon. Then
%re-normalize probabilities.

probs = probs / sum(probs); %initial normalization
lowValues = find(probs(:)<epsilon);
if lowValues, probs(lowValues) = epsilon;  end
highValues = find(probs(:)>(1-epsilon));
if highValues, probs(highValues) = 1 - epsilon;  end
if renormalize, probs = probs / sum(probs); end % renormalize
