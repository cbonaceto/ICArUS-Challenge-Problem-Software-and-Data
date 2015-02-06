function D = RMSdistance(probs1,probs2,zerofactor)
%
% computes root mean square distance between P1 and P2
%
% result ranges from 0 (same) to 1 (maximally different)
% note zerofactor is unused

% probs1 and probs2 are 1 x nbins and all values are in [0,1]
checkvalidprobs(probs1,probs2);

D = sqrt(mean((probs1 - probs2).^2));
