function posteriors = averaging(priors, likelihoods, varargin)
%AVERAGING A model that computes posteriors by averaging the priors and
%likelihoods

posteriors = (priors + likelihoods) / 2;

end