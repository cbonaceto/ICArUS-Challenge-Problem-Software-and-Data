function permutationIndex = getLayerSelectionIndex(permutations, layerSelections, layers)
% Get the index of the layer selection sequence in the array of layer
% selection permutations

% Get the index of each layer in the layers array
numLayers = length(layerSelections);
layerIndexes = zeros(numLayers, 1);
for layerSelectionIndex = 1:length(layerSelections)    
    layerSelection = layerSelections{layerSelectionIndex};
    for layerIndex = 1:length(layers)
        layerName = layers{layerIndex};
        %disp([lower(layerSelection) ',' layerName]);
        %disp(strfind(lower(layerSelection), layerName));
        if ~isempty(strfind(lower(layerSelection), layerName))
           layerIndexes(layerSelectionIndex) = layerIndex;
           % DEBUG CODE
           %disp(strcat('layer: ', layerSelection, ', index: ', num2str(layerIndex)));
           % END DEBUG CODE
           break;
        end
    end
end

% Get the index of the layer selection permutation in the permutations array
for permutationIndex = 1:size(permutations, 1)
    foundPermutation = true;
    for columnIndex = 1:numLayers
        if permutations(permutationIndex, columnIndex) ~= layerIndexes(columnIndex)
            foundPermutation = false;
            break;
        end
    end
    if foundPermutation
        % DEBUG CODE
        %disp(['index: ', num2str(permutationIndex), ', permutation: ', num2str(permutations(permutationIndex,:))]);
        % END DEBUG CODE
        return;
    end
end

%The permutation was not found
permutationIndex = -1;

end
