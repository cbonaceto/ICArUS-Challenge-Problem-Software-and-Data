function [point, cardinality] = findings_vector_to_point(evidencenodes)
%
% convert a findings vector to a point in N-dimensional feature space
% with feature values in [0, n-1]

[point, cardinality] = zerodata(1,length(evidencenodes));

% for each evidence node
for i = 1:length(evidencenodes)
    node = evidencenodes(i);
    bl = node.getBeliefs();
    
    % take belief-weighted mean over node values 
    cardinality(i) = length(bl);
    point(i) = (0:(cardinality(i)-1)) * bl;
end
