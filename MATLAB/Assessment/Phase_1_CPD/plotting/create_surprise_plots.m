function create_surprise_plots(dataInputFolder, saveData, showPlots)
%CREATE_SURPRISE_PLOTS Create surprise plots for Tasks 1-6

if ~exist('saveData', 'var')
    saveData = false;
end
if ~exist('showPlots', 'var')
    showPlots = true;
end
if ~exist('dataInputFolder', 'var') || isempty(dataInputFolder)
    dataInputFolder = '';
end

% Aggregate surprise data, probability data, and troop allocation data for each task
tasks = 1:6;
%tasks = 1:3;
%tasks = 4:6;
numTasks = max(tasks);
surpriseData = cell(numTasks, 1);
groundTruthData = cell(numTasks, 1);
subjectProbs = cell(numTasks, 1);
troopAllocations = cell(numTasks, 1);
for task = tasks    
    [~, ~, d.normalizedSubjectProbs, ~,...
        d.normalizedTroopAllocations, d.surpriseData, ~, ~,... 
        ~, ~, ~, ~, ~, d.groundTruthData] = ...
        aggregate_csv_task_data(strcat(dataInputFolder,'\subject_data\allresponses_task_', num2str(task), '.csv'), task);
    surpriseData{task} = d.surpriseData;
    groundTruthData{task} = d.groundTruthData;
    subjectProbs{task} = d.normalizedSubjectProbs;
    troopAllocations{task} = d.normalizedTroopAllocations;    
end
%surpriseData{1}
size(surpriseData{1}, 1);

dataFolder = 'figures\surprise_figures';
if saveData && ~isdir(dataFolder)
    %Create data folder if it doesn't exist
    mkdir(dataFolder);
end

%Create plots
%{[1 2 3], [4 5 6]}
%{[1 2 3]}
%{[4 5 6]}
plot_surprise(surpriseData, groundTruthData, subjectProbs, troopAllocations,...
    {[1 2 3], [4 5 6]}, saveData, showPlots, dataFolder);

end