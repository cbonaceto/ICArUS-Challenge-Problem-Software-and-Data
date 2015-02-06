function pvalue = getpercentile(k,sorted_distribution)
%
% sorted_distribution must be sorted, as the name implies

if k <= sorted_distribution(1)
    pvalue = 0;
elseif k >= sorted_distribution(end)
    pvalue = 100;
else
    % find first occurrence greater than k
    for i = 1:length(sorted_distribution)
        if k <= sorted_distribution(i)
            pvalue = 100 * (i/length(sorted_distribution));
            return;
        end
    end
end
