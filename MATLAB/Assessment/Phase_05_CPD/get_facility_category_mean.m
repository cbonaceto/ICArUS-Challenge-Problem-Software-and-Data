function [facilitymeans, cardinality] = get_facility_category_mean(numfacilities,facilitynode,evidencenodes)
% 
% outputs in order of evidencenodenames

%% compute category mean for each facility
numevidencenodes = length(evidencenodes);
facilitymeans = zeros(numfacilities,numevidencenodes);
cardinality = zeros(1,numevidencenodes);

% iterate over facilities and get findings
for f = 1:numfacilities
    facilitynode.finding.enterState(f-1);
    [facilitymeans(f,:), cardinality(:)] = findings_vector_to_point(evidencenodes);
    facilitynode.finding.clear;
end
