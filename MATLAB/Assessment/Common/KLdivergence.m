function DKL = KLdivergence(probs1,probs2,epsilon)
%
% computes KL divergence from P1 to P2 D_KL(P1 || P2)
% 
% The divergence is not a distance, i.e. it is asymmetric. D_KL(P1 || P2)
% is the one weighted by P1.

% probs1 and probs2 are 1 x nbins and all values are in [0,1]
checkvalidprobs(probs1,probs2);

%% In order for this calculation to be well-defined, there must be no zero bins
% in probability distribution
probs1 = fillzerobins(probs1,epsilon);
probs2 = fillzerobins(probs2,epsilon);

DKL = sum(sum(probs1 .* log2(probs1./probs2)));

