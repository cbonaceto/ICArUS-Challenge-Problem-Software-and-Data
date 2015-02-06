function [layerSelectionStrategies, normativeLayerChoices, allLayerSelectionUtilities, layerNames, permutations] = ...
    read_layer_choices_files(normativeLayerChoicesFileName, allLayerUtilitiesFileName, varargin)
%READ_LAYER_CHOICES_FILES Parse CSV files containing layer utility and
%normative layer selection data for Task 6.
%   Detailed explanation goes here

layerNames = {'IMINT' 'MOVINT' 'SIGINT' 'SOCINT'};
layers = {'imint' 'movint' 'sigint' 'socint'};
permutations = perms(length(layers):-1:1);

%Get the layer selections and utilities at each stage for each layer selection and update
%strategy
layerSelectionStrategies = [];
if ~isempty(varargin)
    layerSelectionStrategies = cell(length(varargin), 1);
    for i = 1:length(varargin)
        fileName = varargin{i};
        data = csv2cell(fileName, 'fromfile');
        layerSelectionStrategies{i}.layerSelectionName = data{1, 2};
        layerSelectionStrategies{i}.updateName = data{1, 3};
        numTrials = size(data, 1); 
        numLayers = (size(data, 2) - 4)/2;
        layerSelectionStrategies{i}.utilities = zeros(numTrials, numLayers);
        layerSelectionStrategies{i}.permutations = zeros(numTrials, 1);
        row = 1;
        for trial = 1:numTrials
            layerSelections = cell(numLayers, 1);
            for layer = 1:numLayers
                layerSelections{layer} = ...
                    data{row, 3 + (layer*2) - 1};
                layerSelectionStrategies{i}.utilities(trial, layer) = ...               
                    str2double(data{row, 3 + (layer*2)});
            end
            layerSelectionStrategies{i}.permutations(trial) = ...
                getLayerSelectionIndex(permutations, layerSelections, layers);            
            row = row+1;
        end
    end
end

%Get the normative layer selection permutation index for each trial
normativeLayerChoices = [];
if exist('normativeLayerChoicesFileName', 'var')    
    data = csv2cell(normativeLayerChoicesFileName, 'fromfile');    
    numTrials = size(data, 1);
    numLayers = size(data, 2) - 1;
    normativeLayerChoices = zeros(numTrials, 1);
    for trial = 1:numTrials
        layerSelections = cell(numLayers, 1);
        for layer = 1:numLayers
            layerSelections{layer} = data{trial, layer+1};
        end
        %layerSelections
        normativeLayerChoices(trial) = ...
            getLayerSelectionIndex(permutations, layerSelections, layers);    
    end
end

%Get the layer selection utilities for each layer selection permutation of
%each trial
allLayerSelectionUtilities = [];
if exist('allLayerUtilitiesFileName', 'var')
    data = csv2cell(allLayerUtilitiesFileName, 'fromfile');
    numPermutations = size(permutations, 1);
    numTrials = size(data, 1)/numPermutations;
    numLayers = size(data, 2) - 2;
    allLayerSelectionUtilities = zeros(numTrials, numPermutations);
    row = 1;
    for trial = 1:numTrials
        for permutation = 1:numPermutations
            layerSelections = cell(numLayers, 1);
            for layer = 1:numLayers
                layerSelections{layer} = data{row, layer+1};
            end
            permutationIndex = ...
                getLayerSelectionIndex(permutations, layerSelections, layers);
            allLayerSelectionUtilities(trial, permutationIndex) = ...
                str2double(data{row, numLayers + 2});
            row = row+1;
        end
    end
end
end