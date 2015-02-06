function D = ABSdistance(probs1,probs2,zerofactor) %#ok
%
% computes Absolute value (L1) distance from P1 to P2, D_L1(P1,P2)
%
% result ranges from 0 (same) to 2 (maximally different)
% note zerofactor is unused

% probs1 and probs2 are 1 x nbins and all values are in [0,1]
checkvalidprobs(probs1,probs2);

D = sum(abs(probs1 - probs2));
