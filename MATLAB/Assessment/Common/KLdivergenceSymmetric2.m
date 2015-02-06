function DKL = KLdivergenceSymmetric2(probs1,probs2,zerofactor)
% from B. Bigi: Using Kullback-Leibler Distance for Text Categorization, In
% Proceedings of the ECIR-2003, volume 2633 of Lecture Notes in Computer Science, 
% pages 305-319, Springer-Verlag, 2003. 

checkvalidprobs(probs1,probs2);

probs1 = fillzerobins(probs1,zerofactor);
probs2 = fillzerobins(probs2,zerofactor);

DKL = sum(sum((probs1 - probs2) .* log2(probs1./probs2)));

