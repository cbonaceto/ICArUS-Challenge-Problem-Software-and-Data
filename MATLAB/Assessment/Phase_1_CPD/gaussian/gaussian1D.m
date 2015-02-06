function y = gaussian1D(peakHeight, peakCenter, sigma, x)
%GAUSSIAN1D Summary of this function goes here
%   Detailed explanation goes here

y = peakHeight * exp(-(((x-peakCenter)^2)/(2 * sigma * sigma)));

end

