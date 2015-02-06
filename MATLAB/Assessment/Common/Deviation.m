function D = Deviation(probs1,probs2,zerofactor) %#ok
%
% computes deviation from P1 to P2, D(P1,P2)
%
% result ranges from -1 (same) to 2 (maximally different)
% note zerofactor is unused

% probs1 and probs2 are 1 x nbins and all values are in [0,1]
checkvalidprobs(probs1,probs2);

D = sum(probs1 - probs2);
