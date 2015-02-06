function str = num2separatedStr(num, separator)
%NUMARRAY2STRARRAY Summary of this function goes here
%   Detailed explanation goes here

if ~exist('separator', 'var') || isempty(separator)
   separator = ',';
end
str = '';
i = 1;
for n = num    
    if i < length(num) 
        str = [str num2str(n) separator]; %#ok<*AGROW>
    else
        str = [str num2str(n)]; 
    end
    i = i + 1;
end

end

