function plot_group_center_dispersion(subjects, normativeGroupCenters, ...
    subjectGroupCenters, attackLocationFiles, taskNum, saveData, showPlots, dataFolder)
%PLOT_GROUP_CENTER_DISPERSION Plots comparision of subject center locations
%and dispersions vs. normative center locations and dispersions for Tasks
%2 and 3.
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

%% Compute distances from subject centers to normative centers for all subjects on each trial.
%  Also compute difference between subject sigmas (circle radii) and normative sigmas for Task 2.
sigmaMultiple = 1.4823; %Multiple of sigma for 2-to-1 boundary for 2D Gaussian
numSubjects = size(subjectGroupCenters, 1);
numTrials = size(subjectGroupCenters, 2);
numGroups = size(subjectGroupCenters, 3);
distanceDelta = zeros(numSubjects, numTrials, numGroups);
if taskNum == 2
    sigmaDelta = zeros(numSubjects, numTrials, numGroups);
    subjectArea = zeros(numSubjects, numTrials, numGroups);
    normativeArea = zeros(numSubjects, numTrials, numGroups);
else
    sigmaDelta = [];
    subjectArea = [];
    normativeArea = [];
end
if ~isempty(attackLocationFiles)     
    subjectPercentCovered = zeros(numSubjects, numTrials, numGroups);
    normativePercentCovered = zeros(numSubjects, numTrials, numGroups);
    %Load the attack locations for each trial
    cumulativeAttacks = cell(numGroups, 1);
    attackLocations = cell(length(attackLocationFiles),1);    
    trialNum = 1;    
    for trial = 1:length(attackLocationFiles)
        files = attackLocationFiles{trial};
        if iscellstr(files)
            attackLocations{trialNum} = cell(numGroups, 1);
            for file = 1:length(files)
                %disp(files{file});
                [~, trialData, ~] = read_feature_vector_file(files{file});
                for i = 1:length(trialData)
                    attackLocations{trialNum}{i} = [attackLocations{trialNum}{i} trialData{i}];
                    %length(attackLocations{trialNum}{i})
                end
                %attackLocations{trialNum} = [attackLocations{trialNum} trialData];
            end
        else   
            %disp(files);
            [~, attackLocations{trialNum}, ~] = read_feature_vector_file(files);
        end
        trialNum = trialNum + 1;
    end
else
    attackLocations = [];
    subjectPercentCovered = [];
    normativePercentCovered = [];
end

for trial = 1:numTrials
    for group = 1:numGroups
        if ~isempty(attackLocations)                  
            cumulativeAttacks{group} = [cumulativeAttacks{group} attackLocations{trial}{group}];
        end
        for subject = 1:numSubjects
            %disp([num2str(subject) ',' num2str(trial) ',' num2str(group)]);            
            subjectData = subjectGroupCenters{subject, trial, group};
            normativeData = normativeGroupCenters{subject, trial, group};   
            % Get the distance in miles between the suject center and the 
            %normative center
            distanceDelta(subject, trial, group) = ...
                grid_units_to_miles(computeDistance(subjectData.x, subjectData.y, normativeData.x, normativeData.y));
            if taskNum == 2
                % Get the difference between the subject radius and the
                % normative radius based on sigmaX and sigmaY
                sigmaDeltaX = grid_units_to_miles(abs(subjectData.radius - (normativeData.sigmaX*sigmaMultiple)));
                sigmaDeltaY = grid_units_to_miles(abs(subjectData.radius - (normativeData.sigmaY*sigmaMultiple)));
                if sigmaDeltaX < sigmaDeltaY
                    sigmaDelta(subject, trial, group) = sigmaDeltaX;
                else
                    sigmaDelta(subject, trial, group) = sigmaDeltaY;
                end
                % Get the subject area in miles^2 based on the subject radius and the
                % normative area based on sigmaX and sigmaY.
                subjectArea(subject, trial, group) = ...
                    computeEllipseArea(grid_units_to_miles(subjectData.radius),...
                    grid_units_to_miles(subjectData.radius));
                normativeArea(subject, trial, group) = ...
                    computeEllipseArea(grid_units_to_miles(normativeData.sigmaX*sigmaMultiple),...
                    grid_units_to_miles(normativeData.sigmaY*sigmaMultiple)); %sigmaX and sigmaY should be the same
                if ~isempty(attackLocations)
                    % Get the percent of attack locations enclosed in the
                    % subject area and the normative area.
                    subjectPercentCovered(subject, trial, group) = ...
                        computePercentAttacksInEllipse(subjectData.x, subjectData.y, ...
                        subjectData.radius, subjectData.radius, cumulativeAttacks{group});
                    normativePercentCovered(subject, trial, group) = ...
                        computePercentAttacksInEllipse(normativeData.x, normativeData.y, ...
                        normativeData.sigmaX*sigmaMultiple, normativeData.sigmaY*sigmaMultiple, cumulativeAttacks{group});
                end
            end
        end
    end
end
%%%%

%% Create plots showing distance/sigma deltas, area size, and attacks covered by trial (trials are separate subplots in a single figure).
figPosition = [60, 60, 800, 600];
figPositionLarge = [60, 60, 850, 600];
taskName = ['Mission ' num2str(taskNum)];
groupNames = getGroupNames(numGroups);
if ~isempty(subjectArea) && ~isempty(subjectPercentCovered)
    figureTypes = [2 3 4];
elseif ~isempty(subjectArea)
    figureTypes = [2 3];
else
    figureTypes = 1;
end
for figureType = figureTypes
    switch figureType
        case 1
            figName = [taskName, ', Center Distance Deltas By Trial  [sample size: ', num2str(numSubjects), ']'];
            fileName = [dataFolder, '\', taskName,'_Center_Distance_Deltas_By_Trial'];
        case 2
            figName = [taskName, ', Center Distance And Sigma Deltas By Trial  [sample size: ', num2str(numSubjects), ']'];
            fileName = [dataFolder, '\', taskName,'_Center_Distance_Sigma_Deltas_By_Trial'];
        case 3            
            figName = [taskName, ', Group Circle Areas By Trial  [sample size: ', num2str(numSubjects), ']'];
            fileName = [dataFolder, '\', taskName,'_Group_Circle_Areas_By_Trial'];
        case 4
            figName = [taskName, ', Group Circle Attack Coverage By Trial  [sample size: ', num2str(numSubjects), ']'];
            fileName = [dataFolder, '\', taskName,'_Group_Circle_Attack_Coverage_By_Trial'];
    end    
    if figureType == 2
        figHandle = figure('name', figName, 'position', figPositionLarge, 'numbertitle', 'off', 'Visible', visible);
    else
        figHandle = figure('name', figName, 'position', figPosition, 'numbertitle', 'off', 'Visible', visible);
    end
    title(figName);
    numRows = ceil(numTrials / 3);
    subplotNum = 1;   
    ymin = Inf;
    ymax = -Inf;
    handles = zeros(numTrials, 1);
    for trial = 1:numTrials
        handles(trial) = subplot(numRows, 3, subplotNum);
        set(gca, 'box', 'on');
        hold on;
        title(['Trial ', num2str(trial)]);
        xlim([0 numGroups+1]);
        set(gca,'xTick', 1:numGroups);
        set(gca,'XTickLabel', groupNames);
        xlabel('Group');
        switch figureType
            case 1
                %Distance deltas by trial figure
                ylabel('Distance (miles)');
                meanDistanceDelta = zeros(1, numGroups);
                distanceDeltaStd = zeros(1, numGroups);
                for group = 1:numGroups
                    %distanceDelta(:, trial, group)
                    meanDistanceDelta(group) = sum(distanceDelta(:, trial, group))/length(distanceDelta(:, trial, group));
                    distanceDeltaStd(group) = std(distanceDelta(:, trial, group));
                end
                bar(1:numGroups, meanDistanceDelta, 'FaceColor', 'b');
                errorbar(1:numGroups, meanDistanceDelta, distanceDeltaStd, 'LineStyle', 'none', 'Color', 'k');
            case 2
                %Distance and sigma deltas by trial figure
                ylabel('Distance (miles)');
                meanDistanceDelta = zeros(1, numGroups);
                meanSigmaDelta = zeros(1, numGroups);
                distanceDeltaStd = zeros(1, numGroups);
                sigmaDeltaStd = zeros(1, numGroups);
                for group = 1:numGroups
                    %distanceDelta(:, trial, group)
                    meanDistanceDelta(group) = sum(distanceDelta(:, trial, group))/length(distanceDelta(:, trial, group));
                    meanSigmaDelta(group) = sum(sigmaDelta(:, trial, group))/length(sigmaDelta(:, trial, group));
                    distanceDeltaStd(group) = std(distanceDelta(:, trial, group));
                    sigmaDeltaStd(group) = std(sigmaDelta(:, trial, group));
                end
                %meanDistanceDelta
                %meanSigmaDelta
                %sigmaDeltaStd
                barHandles = bar(1:numGroups, [meanDistanceDelta', meanSigmaDelta'], 'grouped');
                set(barHandles(1), 'FaceColor', 'b');
                set(barHandles(2), 'FaceColor', 'r');
                errorbar(0.85:1:numGroups, meanDistanceDelta, distanceDeltaStd, 'LineStyle', 'none', 'Color', 'k');
                errorbar(1.15:1:numGroups+1, meanSigmaDelta, sigmaDeltaStd, 'LineStyle', 'none', 'Color', 'k');
                if trial == 1
                    legend(barHandles, {'Distance Delta', 'Sigma Delta'}, 'Location', 'Best');
                end
            case 3
                %Area size figure
                ylabel('Area (miles^2)');
                areaAvg = zeros(numGroups, 2);
                subjectAreaStd = zeros(1, numGroups);
                for group = 1:numGroups                    
                    areaAvg(group, 1) = sum(subjectArea(:, trial, group))/length(subjectArea(:, trial, group));
                    areaAvg(group, 2) = sum(normativeArea(:, trial, group))/length(normativeArea(:, trial, group));
                    subjectAreaStd(group) = std(subjectArea(:, trial, group));
                end
                barHandles = bar(1:numGroups, areaAvg, 'grouped');
                set(barHandles(1), 'FaceColor', 'b');
                set(barHandles(2), 'FaceColor', 'k');
                errorbar((1:numGroups)-.125, areaAvg(:,1), subjectAreaStd, 'LineStyle', 'none', 'Color', 'k');
            case 4
                %Attack coverage figure
                ylabel('% Attacks Covered');
                percentCoveredAvg = zeros(numGroups, 2);
                subjectPercentCoveredStd = zeros(1, numGroups);
                for group = 1:numGroups                    
                    percentCoveredAvg(group, 1) = sum(subjectPercentCovered(:, trial, group))/length(subjectPercentCovered(:, trial, group));
                    percentCoveredAvg(group, 2) = sum(normativePercentCovered(:, trial, group))/length(normativePercentCovered(:, trial, group));
                    subjectPercentCoveredStd(group) = std(subjectPercentCovered(:, trial, group));
                end
                barHandles = bar(1:numGroups, percentCoveredAvg, 'grouped');
                set(barHandles(1), 'FaceColor', 'b');
                set(barHandles(2), 'FaceColor', 'k');
                errorbar((1:numGroups)-.125, percentCoveredAvg(:,1), subjectPercentCoveredStd, 'LineStyle', 'none', 'Color', 'k');
        end
        if figureType == 4
            ymin = 0;
            ymax = 100;
        else
            limy = ylim;
            if limy(1) < ymin
                ymin = limy(1);
            end
            if limy(2) > ymax
                ymax = limy(2);
            end
        end
        subplotNum = subplotNum + 1;
    end
    for handle = handles        
        set(handle, 'YLim', [ymin ymax]);
    end
    %Save figure
    if saveData
        hgsave(fileName, '-v6');
        saveas(figHandle, fileName, 'png');
    end
end
%%%%

%% Create plots showing distance/sigma deltas by group (groups are separate subplots in a single figure).
if ~isempty(sigmaDelta)
    figureTypes = 2;
else
    figureTypes = 1;
end
for figureType = figureTypes
    switch figureType
        case 1
            figName = [taskName, ', Center Distance Deltas By Group  [sample size: ', num2str(numSubjects), ']'];
            fileName = [dataFolder, '\', taskName,'_Center_Distance_Deltas_By_Group'];
        case 2
            figName = [taskName, ', Center Distance And Sigma Deltas By Group  [sample size: ', num2str(numSubjects), ']'];
            fileName = [dataFolder, '\', taskName,'_Center_Distance_Sigma_Deltas_By_Group'];
    end
    if figureType == 2
        figHandle = figure('name', figName, 'position', figPositionLarge, 'numbertitle', 'off', 'Visible', visible);
    else
        figHandle = figure('name', figName, 'position', figPosition, 'numbertitle', 'off', 'Visible', visible);
    end
    title(figName);
    numRows = ceil(numGroups / 3);
    subplotNum = 1;
    ymin = Inf;
    ymax = -Inf;
    handles = zeros(numGroups, 1);
    for group = 1:numGroups
        handles(group) = subplot(numRows, 3, subplotNum);
        set(gca, 'box', 'on');
        hold on;
        title(['Group ', groupNames(group)]);
        xlim([0 numTrials+1]);
        set(gca,'xTick', 1:numTrials);
        %set(gca,'XTickLabel', groupNames);
        xlabel('Trial');
        ylabel('Distance (miles)');
        meanDistanceDelta = zeros(1, numTrials);
        distanceDeltaStd = zeros(1, numTrials);
        switch figureType
            case 1
                %Distance deltas by group figure               
                for trial = 1:numTrials
                    %distanceDelta(:, trial, group)
                    meanDistanceDelta(trial) = sum(distanceDelta(:, trial, group))/length(distanceDelta(:, trial, group));
                    distanceDeltaStd(trial) = std(distanceDelta(:, trial, group));
                end
                bar(1:numTrials, meanDistanceDelta, 'FaceColor', 'b');
                errorbar(1:numTrials, meanDistanceDelta, distanceDeltaStd, 'LineStyle', 'none', 'Color', 'k');
            case 2
                %Distance and sigma deltas by group figure              
                meanSigmaDelta = zeros(1, numTrials);
                sigmaDeltaStd = zeros(1, numTrials);
                for trial = 1:numTrials
                    %distanceDelta(:, trial, group)
                    meanDistanceDelta(trial) = sum(distanceDelta(:, trial, group))/length(distanceDelta(:, trial, group));
                    meanSigmaDelta(trial) = sum(sigmaDelta(:, trial, group))/length(sigmaDelta(:, trial, group));
                    distanceDeltaStd(trial) = std(distanceDelta(:, trial, group));
                    sigmaDeltaStd(trial) = std(sigmaDelta(:, trial, group));
                end
                %meanDistanceDelta
                %meanSigmaDelta
                %sigmaDeltaStd
                barHandles = bar(1:numTrials, [meanDistanceDelta', meanSigmaDelta'], 'grouped');
                set(barHandles(1), 'FaceColor', 'b');
                set(barHandles(2), 'FaceColor', 'r');
                errorbar(0.85:1:numTrials, meanDistanceDelta, distanceDeltaStd, 'LineStyle', 'none', 'Color', 'k');
                errorbar(1.15:1:numTrials+1, meanSigmaDelta, sigmaDeltaStd, 'LineStyle', 'none', 'Color', 'k');
                if group == 1
                    legend(barHandles, {'Distance Delta', 'Sigma Delta'}, 'Location', 'Best');
                end
        end
        
        limy = ylim;
        if limy(1) < ymin
            ymin = limy(1);
        end
        if limy(2) > ymax
            ymax = limy(2);
        end
        subplotNum = subplotNum + 1;
    end
    for handle = handles
        set(handle, 'YLim', [ymin ymax]);
    end
    %Save figure
    if saveData
        hgsave(fileName, '-v6');
        saveas(figHandle, fileName, 'png');
    end
end
%%%%

%% Create plot showing distance/sigma deltas for all trials/groups rolled up
if ~isempty(sigmaDelta)
    figureTypes = 2;
else
    figureTypes = 1;
end
for figureType = figureTypes
    switch figureType
        case 1
            figName = [taskName, ', Center Distance Deltas For All Trials  [sample size: ', num2str(numSubjects), ']'];
            fileName = [dataFolder, '\', taskName,'_Center_Distance_Deltas_All_Trials'];
        case 2
            figName = [taskName, ', Center Distance And Sigma Deltas For All Trials  [sample size: ', num2str(numSubjects), ']'];
            fileName = [dataFolder, '\', taskName,'_Center_Distance_Sigma_Deltas_All_Trials'];
    end
    if figureType == 2
        figHandle = figure('name', figName, 'position', figPositionLarge, 'numbertitle', 'off', 'Visible', visible);
    else
        figHandle = figure('name', figName, 'position', figPosition, 'numbertitle', 'off', 'Visible', visible);
    end
    title(figName);  
    ymin = Inf;
    ymax = -Inf;    
    xlim([0 numGroups+1]);
    set(gca,'xTick', 1:numGroups);
    set(gca,'XTickLabel', groupNames);
    xlabel('Group');
    ylabel('Distance (miles)');    
    hold on;    
    meanDistanceDelta = zeros(1, numGroups);
    distanceDeltaStd = zeros(1, numGroups);
    switch figureType
        case 1
            %Distance deltas for all trials figure    
            for group = 1:numGroups
                allDistanceDeltas = distanceDelta(:, :, group);
                allDistanceDeltas = allDistanceDeltas(:);
                meanDistanceDelta(group) = sum(allDistanceDeltas)/length(allDistanceDeltas);
                distanceDeltaStd(group) = std(allDistanceDeltas);
            end
            bar(1:numGroups, meanDistanceDelta, 'FaceColor', 'b');
            errorbar(1:numGroups, meanDistanceDelta, distanceDeltaStd, 'LineStyle', 'none', 'Color', 'k');
        case 2
            %Distance and sigma deltas for all trials figure           
            meanSigmaDelta = zeros(1, numGroups);
            sigmaDeltaStd = zeros(1, numGroups);
            for group = 1:numGroups
                allDistanceDeltas = distanceDelta(:, :, group);
                allDistanceDeltas = allDistanceDeltas(:);
                allSigmaDeltas = sigmaDelta(:, :, group);
                allSigmaDeltas = allSigmaDeltas(:);
                meanDistanceDelta(group) = sum(allDistanceDeltas)/length(allDistanceDeltas);
                meanSigmaDelta(group) = sum(allSigmaDeltas)/length(allSigmaDeltas);
                distanceDeltaStd(group) = std(allDistanceDeltas);
                sigmaDeltaStd(group) = std(allSigmaDeltas);
            end
            %meanDistanceDelta
            %meanSigmaDelta
            %sigmaDeltaStd
            barHandles = bar(1:numGroups, [meanDistanceDelta', meanSigmaDelta'], 'grouped');
            set(barHandles(1), 'FaceColor', 'b');
            set(barHandles(2), 'FaceColor', 'r');
            errorbar(0.85:1:numGroups, meanDistanceDelta, distanceDeltaStd, 'LineStyle', 'none', 'Color', 'k');
            errorbar(1.15:1:numGroups+1, meanSigmaDelta, sigmaDeltaStd, 'LineStyle', 'none', 'Color', 'k');          
            legend(barHandles, {'Distance Delta', 'Sigma Delta'}, 'Location', 'Best');
    end
    
    limy = ylim;
    if limy(1) < ymin
        ymin = limy(1);
    end
    if limy(2) > ymax
        ymax = limy(2);
    end    
    set(gca, 'YLim', [ymin ymax]);
        
    %Save figure
    if saveData
        hgsave(fileName, '-v6');
        saveas(figHandle, fileName, 'png');
    end
end
%%%%
end

function groupNames = getGroupNames(numGroups)
groupNames = cell(numGroups, 1);
for groupIndex = 1:numGroups
    switch groupIndex
        case 1
            groupName = 'A';
        case 2
            groupName = 'B';
        case 3
            groupName = 'C';
        case 4
            groupName = 'D';
        otherwise
            groupName = '?';
    end
    groupNames{groupIndex} = groupName;
end
end

function area = computeEllipseArea(xRadius, yRadius)
area = pi * xRadius * yRadius;
%disp(['radiusX: ' num2str(xRadius) ', radiusY: ' num2str(yRadius) ', area: ' num2str(area)]);
end

function distance = computeDistance(x1, y1, x2, y2)
distance = sqrt((x1-x2)^2 + (y1-y2)^2);
%disp(['distance from (' num2str(x1) ',' num2Str(y1) ') to (' num2str(x2) ',' num2str(y2) '): ' num2str(distance)]);
end

function percentCovered = computePercentAttacksInEllipse(x, y, xRadius, yRadius, attacks)
xmin = x - xRadius;
xmax = x + xRadius;
ymin = y - yRadius;
ymax = y + yRadius;
percentCovered = 0;
for attack = attacks
    if attack.x >= xmin && attack.x <= xmax && ...
            attack.y >= ymin && attack.y <= ymax
        percentCovered = percentCovered + 1;
    end
end
percentCovered = percentCovered/length(attacks) * 100;
end