function posteriors = ab(priors, likelihoods, varargin)
%AB A model that computes posteriors using the A-B model, where A=0.9 and
%B=0.5
%   Detailed explanation goes here

%posteriors.set(i, Math.pow(normalizedPriors.get(i), a) * Math.pow(normalizedLikelihoods.get(i), b));
a = 0.9;
b = 0.5;
posteriors = fillzerobins(priors .^ a .* likelihoods .^ b, 0);

end