function DKL = KLdivergenceSymmetric3(probs1,probs2,zerofactor)
% from B. Fuglede, F. Tops: Jensen-Shannon Divergence and Hilbert space
% embedding, IEEE Int Sym. Information Theory, 2004. 

checkvalidprobs(probs1,probs2);

meanprobs = (probs1 + probs2)/2;

DKL = (KLdivergence(probs1,meanprobs,zerofactor) + KLdivergence(probs2,meanprobs,zerofactor))/2;
