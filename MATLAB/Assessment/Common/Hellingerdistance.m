function D = Hellingerdistance(probs1,probs2,zerofactor)
%
% computes Hellinger distance from P1 to P2 D_H(P1,P2)
%
% result ranges from 0 (same) to 1 (maximally different)
% note zerofactor is unused

% probs1 and probs2 are 1 x nbins and all values are in [0,1]
checkvalidprobs(probs1,probs2);

D = 1/2 * sum((probs1.^0.5 - probs2.^0.5).^2);
