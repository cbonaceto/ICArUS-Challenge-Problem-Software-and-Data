function create_rsr_space_plots(saveData, showPlots)
%CREATE_RSR_SPACE_PLOTS Summary of this function goes here
%   Detailed explanation goes here

if ~exist('saveData', 'var')
    saveData = false;
end
if ~exist('showPlots', 'var')
    showPlots = true;
end
dataFolder = 'figures\rsr_space';
if saveData && ~isdir(dataFolder)
    mkdir(dataFolder);
end

differences = [.01 -.01 .05 -.05 .10 -.10];
for difference = differences
    plot_rsr_space(difference, 0.001, saveData, showPlots, dataFolder);
end

end