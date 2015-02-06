function plot_biases_exhibited(subjects, missions, aaData, pdeData,...
    rrData, avData, saveData, showPlots, dataFolder)
%PLOT_BIASES_EXHIBITED Plots data on biases exhibited for each subject.
%   The subjects must be the same (and in the same order) for each mission.

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

numSubjects = length(subjects);
numMissions = length(missions);

%% Compute overall percent of trials each bias was exhbited on over all missions. Also,
%  compute the percent of trials each subject exhibited each bias for each mission, and the
%  percent of subjects who exhibited each bias on each trial (for a mission).
totalBiasTrials = zeros(4, 1);
overallBiasMetrics = zeros(4, 1);
biasMetricsBySubject = cell(4, 1);
biasMetricsByMission = cell(4, numMissions);
biases = {'Anchoring & Adjustment', 'Persistence of Discredited Evidence',...
    'Representativeness', 'Availability'};
biasesAbbrev = {'AA', 'PDE', 'RR', 'AV'};
%aaData.exhibited = zeros(numSubjects, numTrials);
for bias = 1:4
    switch bias
        case 1
            biasData = aaData;
        case 2
            biasData = pdeData;
        case 3
            biasData = rrData;
        case 4
            biasData = avData;
    end
    biasMetricsBySubject{bias} = zeros(numSubjects, 1);
    for mission = 1:numMissions       
        if biasData{mission}.totalTrials > 0
            totalBiasTrials(bias) = totalBiasTrials(bias) + biasData{mission}.totalTrials;
            exhibited = biasData{mission}.exhibited;
            %numSubjects = size(exhibited, 1);
            numTrials = size(exhibited, 2);
            %biasMetricsBySubject{bias} = zeros(numSubjects, 1);
            biasMetricsByMission{bias, mission} = zeros(numTrials, 1);
            for trial = 1:numTrials
                total = length(find(~isnan(exhibited(:, trial))) == 1);
                numTimesBiasExhibited = length(find(exhibited(:, trial) == 1));
                biasMetricsByMission{bias, mission}(trial) = ...
                   numTimesBiasExhibited / total;
                overallBiasMetrics(bias) = overallBiasMetrics(bias) + numTimesBiasExhibited;
            end
            for subject = 1:numSubjects
                total = length(find(~isnan(exhibited(subject, :))) == 1);
                biasMetricsBySubject{bias}(subject) = ...
                    length(find(exhibited(subject, :) == 1)) / total;
            end
        end       
    end
end

%% Plot overall bias metrics (overall % of trials each bias exhibited on over all missions)
figPosition = [60, 60, 1000, 600];
figName = ['Frequency Biases Exhibited Over All Trials',...
    '   [Sample Size: ', num2str(numSubjects), ']'];
figHandle = figure('name', figName, 'position', figPosition, 'NumberTitle', 'off', 'Visible', visible);
hold on;
title(figName);
xlabel('Bias');
ylabel('% Trials Bias Exhibited On')
xlim([0 5]);
set(gca,'XTick', 1:4);
set(gca, 'XTickLabel', biasesAbbrev);
ylim([0 101]);
%overallBiasMetrics
%totalBiasTrials
bar((overallBiasMetrics ./ totalBiasTrials) * 100);
%Save the figure
if saveData
    fileName = [dataFolder, '\', 'Frequency_Biases_Exhibited_All_Trials'];
    saveas(figHandle, fileName, 'png');
end

%% Plot details for each mission (show % of subjects who exhibited the bias on each trial of the mission)
for mission = 1:numMissions
    missionNum = missions(mission);
    missionName = ['Mission ', num2str(missionNum)];
    for bias = 1:4        
        if ~isempty(biasMetricsByMission{bias, mission})
            figName = [missionName, ', ', biases{bias},...
                '   [Sample Size: ', num2str(numSubjects), ']'];
            figHandle = figure('name', figName, 'position', figPosition, 'NumberTitle', 'off', 'Visible', visible);
            numTrials = length(biasMetricsByMission{bias, mission});
            hold on;
            title(figName);
            xlabel('Trial');
            ylabel('% Subjects Who Exhibited Bias')
            xlim([0 numTrials + 1]);
            set(gca,'XTick', 1:numTrials);
            ylim([0 101]);
            bar(biasMetricsByMission{bias, mission} * 100);
            %Save the figure
            if saveData
                fileName = [dataFolder, '\', missionName, '_Frequency_' biasesAbbrev{bias}, '_Exhibited'];
                saveas(figHandle, fileName, 'png');
            end
        end
    end
end

%% Plot details for each bias for each subject (show % of subjects who exhibited the bias)
figPosition = [60, 60, 1200, 600];
for bias = 1:4    
    %biasMetricsBySubject{bias} = zeros(numSubjects, 1);
    numSubjects = length(biasMetricsBySubject{bias});
    figName = [biases{bias},...
        '   [Sample Size: ', num2str(numSubjects), ']'];
    figHandle = figure('name', figName, 'position', figPosition, 'NumberTitle', 'off', 'Visible', visible);    
    hold on;
    title(figName);
    xlabel('Subject');
    ylabel('% Trials Bias Exhibited On')
    xlim([0 numSubjects + 1]);
    set(gca,'XTick', 1:numSubjects);
    ylim([0 101]);
    bar(biasMetricsBySubject{bias} * 100);
    %Save the figure
    if saveData
        fileName = [dataFolder, '\', 'Frequency_' biasesAbbrev{bias}, '_Exhibited_By_Subject'];
        saveas(figHandle, fileName, 'png');
    end
end

end