function DKL = KLdivergenceSymmetric1(probs1,probs2,zerofactor)
% from S. Kullback, R. A. Leibler: On information and sufficiency, Annals
% of Mathematical Statistics, 22(1):79-86, 1951. 

DKL = (KLdivergence(probs1,probs2,zerofactor) + KLdivergence(probs2,probs1,zerofactor))/2;
