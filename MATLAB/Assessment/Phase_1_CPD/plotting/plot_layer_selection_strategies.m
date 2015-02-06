function plot_layer_selection_strategies(layers, permutations, numLayersToSelect,...
    layerSelectionStrategies, taskNum, saveData, showPlots, dataFolder)
%PLOT_LAYER_SELECTION_STRATEGIES Plot utility of each layer selection
%strategy/probability update strategy combo at each stage
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

numTrials = size(layerSelectionStrategies{1}.utilities, 1);

%% Compute average informatic utility for each strategy over all trials
averageUtilities = zeros(length(layerSelectionStrategies), numLayersToSelect);
for i = 1:length(layerSelectionStrategies)
    averageUtilities(i,:) = sum(layerSelectionStrategies{i}.utilities)/numTrials;
end
%averageUtilities

%% Plot layer selection strategies for all trials and for each trial as separate figures
figPosition = [100, 100, 1000, 600];
taskName = ['Mission ' num2str(taskNum)];
colors = {'k', 'b', 'r', 'g'};
markers = {'*', 's', 'd', 'h'};
for trial = 0:numTrials
    if trial == 0;
        figName = [taskName, ', Average Over All Trials, Layer Selection/Update Strategies'];
    else
        figName = [taskName, ', Trial ', num2str(trial), ', Layer Selection/Update Strategies'];
    end
    figHandle = figure('name', figName, 'position', figPosition, 'NumberTitle', 'off', 'Visible', visible);
    hold on;
    title(figName);
    
    %Plot lines for layer selection strategy utilities at each stage
    legendStr = cell(length(layerSelectionStrategies), 1);
    if trial == 0
        for i = 1:length(layerSelectionStrategies)
            plot(averageUtilities(i,:), ['-' markers{i} colors{i}]);
            legendStr{i} = [layerSelectionStrategies{i}.layerSelectionName ', ' ...
                layerSelectionStrategies{i}.updateName];
        end
        %barh(1:numPermutations, allTrialsPercent(:));
    else
        for i = 1:length(layerSelectionStrategies)
            plot(layerSelectionStrategies{i}.utilities(trial, :), ['-' markers{i} colors{i}]);
            legendStr{i} = [layerSelectionStrategies{i}.layerSelectionName ', ' ...
                layerSelectionStrategies{i}.updateName];
        end
    end
    
    xlabel('Stage');
    xlim([0 numLayersToSelect + 1]);
    set(gca,'xtick', 1:numLayersToSelect);
    
    ylabel('Informatic Utility');
    ylim([0 0.6]);
    %ylim([0 numPermutations+1]);
    
    %Create legend
    legend(legendStr, 'Location', 'NorthEastOutside');
    
    %Save figure
    if saveData
        if trial == 0;
            fileName = [dataFolder, '\', taskName, '_All_Trials_Layer_Selection_Strategies'];
        else
            fileName = [dataFolder, '\', taskName, '_Trial_', num2str(trial), '_Layer_Selection_Strategies'];
        end
        saveas(figHandle, fileName, 'png');
    end
    %%%%
end

end