function DKL = KLdivergenceSymmetric4(probs1,probs2,zerofactor)
% from C.H. Bennett, P. Gács, M. Li, P. Vitányi, and W. Zurek: 
% Information Distance, IEEE Trans. Inform. Theory, 44:4, pages 1407-1423, 1998.  

DKL = max(KLdivergence(probs1,probs2,zerofactor),KLdivergence(probs2,probs1,zerofactor));
