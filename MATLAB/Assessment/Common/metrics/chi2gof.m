function [chi2, significant, significance_threshold, actual_p] = chi2gof(O, E, desired_p, use_yates_correction)
%CHI2GOF Summary of this function goes here
%   Detailed explanation goes here

%We should throw a warning if any of the observed or expected frequencies
%are 0, but for now just set them to 1
O(O==0) = 1;
E(E==0) = 1;

if use_yates_correction
    chi2 = sum(sum((abs(O-E)-0.5).^2 ./ E));
else
    chi2 = sum(sum((O-E).^2 ./ E));
end

%Calculate significance using the chi square distribution
df = length(O) - 1; %Degrees of freedom
significance_threshold = chi2inv(1-desired_p, df); %Threshold of significance to meet desired p
significant = chi2 > significance_threshold; %Whether results were significant at desired p
actual_p = 1 - chi2cdf(chi2, df); %Actual p given chi square value

end