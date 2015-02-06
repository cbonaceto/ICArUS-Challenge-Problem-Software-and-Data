function create_biases_exhibited_plots(missions, dataInputFolder,...
    figureOutputFolder, saveData, showPlots)
%CREATE_BIASES_EXHIBITED_PLOTS Summary of this function goes here
%   Detailed explanation goes here

if ~exist('saveData', 'var')
    saveData = false;
end
if ~exist('showPlots', 'var')
    showPlots = true;
end
if ~exist('dataInputFolder', 'var') || isempty(dataInputFolder)
    dataInputFolder = '';
end
if ~exist('figureOutputFolder', 'var') || isempty(figureOutputFolder)
    figureOutputFolder = '';
end

numMissions = length(missions);
aaData = cell(numMissions, 1);
pdeData = cell(numMissions, 1);
rrData = cell(numMissions, 1);
avData = cell(numMissions, 1);
i = 1;
for mission = missions
    %Get the biases exhibited data for the mission
    [d.subjects, d.aaData, d.pdeData, d.rrData, d.avData] = ...
        aggregate_csv_bias_data(strcat(dataInputFolder, '\Aggregated Data\biases_exhibited_mission_',...
        num2str(mission), '.csv'), mission);
    if i == 1
        subjects = d.subjects;
    end
    aaData{i} = d.aaData;
    pdeData{i} = d.pdeData;
    rrData{i} = d.rrData;
    avData{i} = d.avData;
    i = i + 1;
end

dataFolder = strcat(figureOutputFolder, '\biases_exhibited_figures');
if saveData && ~isdir(dataFolder)
    mkdir(dataFolder);
end

plot_biases_exhibited(subjects, missions, aaData, pdeData,...
    rrData, avData, saveData, showPlots, dataFolder);

end