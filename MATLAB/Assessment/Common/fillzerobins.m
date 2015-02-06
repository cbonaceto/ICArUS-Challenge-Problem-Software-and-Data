function probs = fillzerobins(probs, epsilon, varargin)
% replace all probability bins with a value less that epsilon with epsilon
% and renormalize. 

normalize = 1;
if ~isempty(varargin)
    normalize = varargin{1};
end

numProbs = length(probs);
if numProbs > 1 && normalize
    probs = probs / sum(probs); %perform initial normalization
end
zerobins = find(probs(:)<epsilon);
if ~isempty(zerobins)
    nonzerobins = find(probs(:)>=epsilon);
    
    %Replace values < epsilon with epsilon
    if (zerobins), probs(zerobins) = epsilon;  end
    
    %Renormalize remaining values such that bins again sum to 1
    if(numProbs > 1 && normalize)
        overAmt = sum(probs) - 1;
        if overAmt > 0
            probs(nonzerobins) = probs(nonzerobins) - (overAmt*(probs(nonzerobins)/sum(probs(nonzerobins))));
        end
    end
end