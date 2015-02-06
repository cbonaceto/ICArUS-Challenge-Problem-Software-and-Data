function max_e = maxentropy( num_hypotheses )
%MAXENTROPY Computes the maximum possible entropy for the given number of
%hypotheses.
%   Detailed explanation goes here

if (num_hypotheses <= 0)
    max_e = 0;
else 
    probs = zeros(1, num_hypotheses);
    probs(:) = 1/num_hypotheses;
    max_e = entropy(probs);    
end

