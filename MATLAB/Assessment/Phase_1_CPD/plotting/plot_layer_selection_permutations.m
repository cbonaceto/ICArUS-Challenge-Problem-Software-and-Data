function plot_layer_selection_permutations(subjects, layers, permutations, numLayersToSelect,...
    subjectLayerSelections, normativeLayerSelections, taskNum, saveData, showPlots, dataFolder)
%PLOT_LAYER_SELECTION_PERMUTATIONS Plots the distributuion of layer
%selection permutations for Task 6.

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

%% Aggregate layer selection permutation data by trial 
numSubjects = size(subjectLayerSelections, 1);
numTrials = size(subjectLayerSelections, 2);
numPermutations = size(permutations, 1);
permutationCount = zeros(numTrials, size(permutations, 1));
for subject = 1:numSubjects
    for trial = 1:numTrials
        permutation = subjectLayerSelections(subject, trial);
        % DEBUG CODE
        %permutationName = createPermutationName(layers, numLayersToSelect, permutations(permutation,:));
        %disp(strcat('subject: ', num2str(subject), ', trial: ', num2str(trial), ', path: ',...
         %   permutationName, ', path index: ', num2str(permutation)));
        % END DEBUG CODE
        permutationCount(trial, permutation) = permutationCount(trial, permutation) + 1;
    end  
end
permutationPercent = zeros(numTrials, size(permutations, 1));
for trial = 1:numTrials
    permutationPercent(trial,:) = permutationCount(trial,:) ./ sum(permutationCount(trial,:)) * 100;
end
allTrialsCount = sum(permutationCount);
allTrialsPercent = allTrialsCount ./ sum(allTrialsCount) * 100;

allTrialsNormativeLayerSelections = [];
if ~isempty(normativeLayerSelections)    
    allTrialsNormativeLayerSelections = zeros(numPermutations, 1);
    for trial = 1:numTrials
        permutationIndex = normativeLayerSelections(trial);
        allTrialsNormativeLayerSelections(permutationIndex) = ...
            allTrialsNormativeLayerSelections(permutationIndex) + 1;
    end
    allTrialsNormativeLayerSelections = ...
        allTrialsNormativeLayerSelections/sum(allTrialsNormativeLayerSelections) * 100;
end
%maxCount = max(max(permutationCount));
%permutationCount

%% Create list of all layer selection permutations names
permutationNames = cell(1, numPermutations);
for permutationIndex = 1:numPermutations
    permutationNames{permutationIndex} = createPermutationName(layers, numLayersToSelect,...
        permutations(permutationIndex,:));
end
%permutationNames

%% Plot layer selection permutations for all trials and for each trial as separate figures
figPosition = [100, 100, 800, 640];
taskName = ['Mission ' num2str(taskNum)];
for trial = 0:numTrials
    if trial == 0;
        figName = [taskName, ', All Trials, Layer Selection Paths  [Sample Size: ', num2str(numSubjects), ']'];        
    else
        figName = [taskName, ', Trial ', num2str(trial), ', Layer Selection Paths  [Sample Size: ', num2str(numSubjects), ']'];
    end
    figHandle = figure('name', figName, 'position', figPosition, 'NumberTitle', 'off', 'Visible', visible);
    hold on;
    title(figName);
    
    %Plot bars for layer selection path permutations
    if trial == 0        
        if ~isempty(allTrialsNormativeLayerSelections)            
            %Show normative path percents and subject layer selection path permutations
            %barh(1:numPermutations, allTrialsPercent(:));
            h = barh(1:numPermutations, [allTrialsPercent(:), allTrialsNormativeLayerSelections]);
            set(h(1), 'FaceColor', 'b');
            set(h(2), 'FaceColor', 'k');
            legend({'Human', 'Bayesian'}, 'Location', 'NorthEast');
        else             
            barh(1:numPermutations, allTrialsPercent(:), 'b');
        end
    else
        barh(1:numPermutations, permutationPercent(trial, :), 'b');
    end
    
    xlabel('Percent');   
    xlim([0 100]);
    set(gca,'xtick', 0:10:100);
    %if trial > 0        
       %xlim([0 maxCount]);
       %set(gca,'xtick', 0:maxCount);
    %end  
    normativePermutation = 0;
    if trial > 0 && ~isempty(normativeLayerSelections) 
        %Star the optimal layer selection path
        normativePermutation = normativeLayerSelections(trial);
        if normativePermutation > 0
            origName = permutationNames{normativePermutation};
            permutationNames{normativePermutation} = ['**' origName '**'];
        end
        ylabel('Path (**Path** is normative path)');
    else
        ylabel('Path');    
    end
    ylim([0 numPermutations+1]);
    set(gca,'ytick', 1:numPermutations);    
    set(gca, 'yticklabel', permutationNames);
    if normativePermutation > 0
        permutationNames{normativePermutation} = origName;
    end
   
    %Save figure
    if saveData
        if trial == 0;
            fileName = [dataFolder, '\', taskName, '_All_Trials_Layer_Selection_Paths'];
        else
            fileName = [dataFolder, '\', taskName, '_Trial_', num2str(trial), '_Layer_Selection_Paths'];
        end        
        hgsave(fileName, '-v6');
        saveas(figHandle, fileName, 'png');
    end
    %%%%
end
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