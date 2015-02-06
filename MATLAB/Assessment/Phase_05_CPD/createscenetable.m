function [scenetable, uniquescenes, numuniquescenes] = createscenetable(testscenes,testtrials,subjectdatatable,Layer_type_idx)
% 
% create scene table containing the trial indices for different layer 
% presentation types
%
% testtrials are the trials in the test block 
% Order of scenetable is Sim Seq UC
%  
% can use any subject's subjectdatatable

uniquescenes = unique(testscenes);
numuniquescenes = length(uniquescenes);

scenetable = zeros(numuniquescenes,3);
for i = 1:length(testscenes)
    for sc = 1:numuniquescenes
        shorttrialidx = find(testscenes == uniquescenes(sc)); % indexing into testtrials
        for layeridx = 1:length(shorttrialidx)
            layer_type = subjectdatatable{testtrials(shorttrialidx(layeridx)),Layer_type_idx};
            
            if strcmp(layer_type,'Simultaneous'), scenetable(sc,1) = shorttrialidx(layeridx);
            elseif strcmp(layer_type,'Sequential'), scenetable(sc,2) = shorttrialidx(layeridx);
            elseif strcmp(layer_type,'User Choice'), scenetable(sc,3) = shorttrialidx(layeridx);
            else error('Unknown layer type'); 
            end
        end
    end
end

% original trial numbers
scenetable = testtrials(scenetable);

% test print
% for i = 1:size(scenetable,1)
%     {subjectdatatable{scenetable(i,1),5} subjectdatatable{scenetable(i,2),5} subjectdatatable{scenetable(i,3),5} testscenes(find(testtrials == scenetable(i,1))) testscenes(find(testtrials == scenetable(i,2))) testscenes(find(testtrials == scenetable(i,3)))}
% end
