function D = Winnertakealldivergence(probs1,probs2,zerofactor)
%
% computes winner-take-all divergence from P1 to P2
% 
% i.e. if argmax(P1) == argmax(P2), D = 1, else D = 0.
% 
% note zerofactor is unused

% probs1 and probs2 are 1 x nbins and all values are in [0,1]
checkvalidprobs(probs1,probs2);

% When there are two equal peak bins, we compare only the first.
% [~,idx1] = max(probs1);
% [~,idx2] = max(probs2);
% D = (idx1 == idx2);

% When there are multiple equal peak bins, we compare all possible matches.
max1 = max(probs1);
max2 = max(probs2);
idx1 = find(probs1 == max1);
idx2 = find(probs2 == max2);

if (length(idx1) == 1 && length(idx2) == 1)
    if (idx1 ~= idx2)
        D = 1; % mismatch
    else
        D = 0; % match
    end
else
    if isempty(intersect(idx1,idx2))
        D = 1; % no overlap
    else
        D = 0;  % overlap
    end
end
