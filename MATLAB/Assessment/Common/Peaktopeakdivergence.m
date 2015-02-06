function D = Peaktopeakdivergence(probs1,probs2,zerofactor)
%
% computes Peak-to-peak divergence from P1 to P2
% 
% i.e. absolute distance between peak of P1 and same bin in P2
% When there are two equal peak bins, we choose the first one.
%
% result ranges from 0 (same) to 1 (maximally different)
% note zerofactor is unused

% probs1 and probs2 are 1 x nbins and all values are in [0,1]
checkvalidprobs(probs1,probs2);

[max1,idx] = max(probs1);
max2 = probs2(idx);

D = abs(max1 - max2);
