function [chi2, significant, significance_threshold, p] = chi2test(data, desired_p, use_yates_correction)
%CHI2TEST2 Summary of this function goes here
%   Detailed explanation goes here

%Calculate expected frequencies for each cell
RT = sum(data, 2); %Row totals
CT = sum(data, 1); %Column totals
N = sum(RT);
E = (RT*CT)/N; %Expected frequencies
df = (length(RT)-1) * (length(CT)-1); %Degrees of freedom

%Calculate chi square statistic
if use_yates_correction
   %abs(data-E)
   %(abs(data-E)-0.5).^2 ./ E   
   %(abs(data-E)-(df/2)).^2 ./ E   
   %chi2 = sum(sum((abs(data-E)-(df/2)).^2 ./ E));
   chi2 = sum(sum((abs(data-E)-0.5).^2 ./ E));
else
   chi2 = sum(sum((data-E).^2 ./ E));
end

%Calculate significance using the chi square distribution
significance_threshold = chi2inv(1-desired_p, df); %Threshold of significance to meet desired p
significant = chi2 > significance_threshold; %Whether results were significant at desired p
p = 1 - chi2cdf(chi2, df); %Actual p given chi square value