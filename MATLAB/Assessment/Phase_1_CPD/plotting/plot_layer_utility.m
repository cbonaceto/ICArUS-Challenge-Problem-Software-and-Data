function plot_layer_utility(layers, permutations, numLayersToSelect, layerUtilities, taskNum,...
    saveData, showPlots, dataFolder)
%PLOT_LAYER_UTILITY Plots the expected informatic utility of each layer
%selection permutation.
%   Detailed explanation goes here

if ~exist('saveData', 'var')
    saveData = false;
end
if saveData && (~exist('dataFolder', 'var') || isempty(dataFolder))
    dataFolder = '';
end
if ~exist('showPlots', 'var') || showPlots
    visible = 'on';
else 
    visible = 'off';
end

%Calculate average utility for all trials
numTrials = size(layerUtilities, 1);
numPermutations = size(permutations, 1);
avgLayerUtilities = sum(layerUtilities)/numTrials;

%Create list of all layer selection permutations names
permutationNames = cell(1, numPermutations);
for permutationIndex = 1:numPermutations
    permutationNames{permutationIndex} = createPermutationName(layers, numLayersToSelect,...
        permutations(permutationIndex,:));
end

%% Create figure
for trial = 0:numTrials
    figPosition = [100, 100, 800, 600];
    taskName = ['Mission ' num2str(taskNum)];   
    if trial == 0;
        figName = [taskName, ', Average Layer Selection Utilities'];        
    else
        figName = [taskName, ', Trial ', num2str(trial), ', Layer Selection Utilities'];
    end
    figHandle = figure('name', figName, 'position', figPosition, 'NumberTitle', 'off', 'Visible', visible);
    hold on;
    title(figName);
    
    %Plot bars for layer selection path permutations
    if trial==0
        barh(1:numPermutations, avgLayerUtilities);
    else
        barh(1:numPermutations, layerUtilities(trial,:));
    end
    
    xlabel('Informatic Utility');
    xlim([0 1]);    
    %set(gca,'xtick', 0:10:100);
    
    %normativePermutation = 0;
    %if trial > 0 && ~isempty(normativeLayerSelections)
    %    %Star the optimal layer selection path
    %    normativePermutation = normativeLayerSelections(trial);
    %    if normativePermutation > 0
    %        origName = permutationNames{normativePermutation};
    %        permutationNames{normativePermutation} = ['++' origName '++'];
    %    end
    %    ylabel('Path (++Path++ is optimal path)');
    %else
    ylabel('Path');
    %end
    ylim([0 numPermutations+1]);
    set(gca,'ytick', 1:numPermutations);
    set(gca, 'yticklabel', permutationNames);
    %if normativePermutation > 0
    %    permutationNames{normativePermutation} = origName;
    %end
    
    %Save figure
    if saveData
        if trial == 0;
            fileName = [dataFolder, '\', taskName, '_Average_Layer_Selection_Utilities'];
        else
            fileName = [dataFolder, '\', taskName, '_Trial_', num2str(trial), '_Layer_Selection_Utilities'];
        end
        saveas(figHandle, fileName, 'png');
    end
end
%%%%
end

function name = createPermutationName(layers, numLayersToSelect, permutation)
for columnIndex = 1:numLayersToSelect
    if columnIndex > 1
        name = strcat(name, '-', layers(permutation(columnIndex)));
    else
        name = layers(permutation(columnIndex));
    end
end
name = char(name);
end