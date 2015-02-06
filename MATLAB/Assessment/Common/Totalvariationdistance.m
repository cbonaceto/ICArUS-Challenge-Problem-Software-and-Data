function D = Totalvariationdistance(probs1,probs2,zerofactor)
%
% computes Total Variation distance from P1 to P2 D_H(P1,P2)
%
% result ranges from 0 (same) to 1 (maximally different)
% note zerofactor is unused

% probs1 and probs2 are 1 x nbins and all values are in [0,1]
checkvalidprobs(probs1,probs2);

D = 1/2 * sum(abs(probs1 - probs2));
