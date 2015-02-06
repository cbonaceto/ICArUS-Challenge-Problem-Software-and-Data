function plot_red_tactic_probs(subjects, redTacticProbs,...
    redTacticProbsAvg, redTacticNe, redTacticNeAvg,...
    normativeRedTacticProbsAvg, normativeRedTacticNeAvg,...
    actualRedTactics, redTacticTypes, normativeStrategyName,...
    showSubjectsLegend, missionNum, saveData, showPlots, dataFolder)
%PLOT_RED_TACTIC_PROBS Summary of this function goes here
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

%Create vector of probabilities for each trial with a 1 when the actual Red
%tactic is tactic 1, and a 0 otherwise
numTrials = size(actualRedTactics, 2);
groundTruthProbs = actualRedTactics(1, :) == 1;

%Find the trial on which the tactics change (a vertical line will be drawn
%at this trial)
changeTrial = 0;
lastGroundTruthProb = groundTruthProbs(1);
trial = 2;
while changeTrial == 0 && trial <= numTrials;
    if lastGroundTruthProb ~= groundTruthProbs(trial)
        changeTrial = trial;
    else
        lastGroundTruthProb = groundTruthProbs(trial);
        trial = trial + 1;
    end    
end

%Get the probability of Red tactic 1 for each subject
numSubjects = size(redTacticProbs, 1);
redTactic1Probs = zeros(numSubjects, numTrials);
for subject = 1:numSubjects
    for trial = 1:numTrials
        redTactic1Probs(subject, trial) = redTacticProbs{subject, trial}(1);
    end
end

%% Plot P(Red tactic 1) and Negentropy of P(each Red tactic) vs. trial number
%  using the scatter plot and error bar formats to show variance.
missionName = ['Mission ', num2str(missionNum)];
for plotType = 1:2
    if plotType == 1
        %Plot probabilities        
        dataDist = redTactic1Probs;
        dataAvg = redTacticProbsAvg;        
        normativeDataAvg = normativeRedTacticProbsAvg;
        figName = [missionName, ', Red Tactic Probabilities' '  [Sample Size: ', num2str(numSubjects), ']'];
        xLabelStr = 'Trial';
        yLabelStr = ['P(' redTacticTypes{1} '), Human Avg. (blue), Ground Truth (green)'];        
    else
        %Plot negentropies
        dataDist = redTacticNe;
        dataAvg = redTacticNeAvg;   
        normativeDataAvg = normativeRedTacticNeAvg;
        figName = [missionName, ', Red Tactic Negentropy' '  [Sample Size: ', num2str(numSubjects), ']'];
        xLabelStr = 'Trial (Line at Change Trial)';
        yLabelStr = ['Ne(P(' redTacticTypes{1} '), P(' redTacticTypes{2} ')), Human Avg. (blue), Ground Truth (green)'];        
    end
    for errorVarianceFormat = 1:2        
        if errorVarianceFormat == 1
            %Scatter format
            figPosition = [60, 60, 1200, 800];
            lineWidth = 2;
            normativeLineWidth = 1.5;
        else
            %Error bar format
            figPosition = [60, 60, 1000, 800];
            lineWidth = 1.5;
            normativeLineWidth = 1.25;
        end
        
        figHandle = figure('name', figName, 'position', figPosition, 'NumberTitle', 'off', 'Visible', visible);
        h = [];
        legendText = {};
        hold on;
        title(figName);
        xlabel(xLabelStr);
        xlim([0 numTrials+1]);
        set(gca,'xtick', 1:numTrials);
        ymax = ylim;
        ylim([0 ymax(2)]);        
        ylabel(yLabelStr);        
        
        %Plot average subject probabilities or negentropies as a blue solid line        
        h(1) = plot(dataAvg(:, 1), '-ob', 'LineWidth', lineWidth);
        legendText{1} = 'Human Avg.';
        
        %Plot normative probabilities or negentropies as a black solid line
        if ~isempty(normativeDataAvg)            
            h(2) = plot(normativeDataAvg(:, 1), '-*k', 'LineWidth', normativeLineWidth);
            legendText{2} = normativeStrategyName;
            hIndex = 3;
        else
            hIndex = 2;
        end
        
        if plotType == 1
            %Plot ground truth probabilities as a black solid line
            h(hIndex) = plot(groundTruthProbs, '-xg', 'LineWidth', normativeLineWidth);
            legendText{hIndex} = 'Ground Truth';
        else
            %Draw a vertical line at the change trial
            if changeTrial > 0
                h(hIndex) = line([changeTrial, changeTrial], ylim, 'LineStyle', ':', 'Color', [0.2 0.2 0.2]);
                legendText{hIndex} = 'Change Trial';
            end
        end
        
        if errorVarianceFormat == 1
            %Create scatter plot that shows subject probability or negentropy distribution at each trial
            colormap(lines(numSubjects));
            colors = colormap;
            numColors = size(colors, 1);
            markers = {'x', 's', 'd', '^', 'v', '>', '<', 'h'};
            numMarkers = length(markers);
            colorIndex = 1;
            markerIndex = 1;
            for subject = 1:numSubjects
                if colorIndex > numColors
                    colorIndex = 1;
                end
                if markerIndex > numMarkers
                    markerIndex = 1;
                end
                handle = plot(1:numTrials, dataDist(subject, :),...
                    ['-' markers{markerIndex}], 'Color', colors(colorIndex,:));
                if showSubjectsLegend
                    h(length(h)+1) = handle;
                    legendText{length(legendText)+1} = subjects{subject}; %#ok<*AGROW>
                end
                colorIndex = colorIndex + 1;
                markerIndex = markerIndex + 1;
            end
            %Create the legend
            if showSubjectsLegend
                legend(h, legendText, 'Location', 'NorthEastOutside');
                %legend(h, subjects, 'Location', 'NorthEastOutside');
            elseif length(h) > 2
                legend(h, legendText, 'Location', 'SouthOutside');
            end
        else
            %Plot error bars that show the std for the subject probability distrubution at each trial
            %redTactic1Probs = zeros(numSubjects, numTrials);
            subject_e = zeros(1, numTrials);
            for trial = 1:numTrials
                subject_e(trial) = sem(dataDist(:, trial), numSubjects);
            end
            errorbar(1:numTrials, dataAvg(:, 1), subject_e, '-b');
            %Create the legend
            if length(h) > 2
                legend(h, legendText, 'Location', 'SouthOutside');
            end
        end
        
        %Decrease figure margins
        tightfig;
        
        %Save the figure
        if saveData
            if plotType == 1
                if errorVarianceFormat == 1
                    fileName = [dataFolder, '\', missionName, '_Red_Tactic_Probabilities_Scatter'];
                else
                    fileName = [dataFolder, '\', missionName, '_Red_Tactic_Probabilities_ErrorBar'];
                end
            else
                if errorVarianceFormat == 1
                    fileName = [dataFolder, '\', missionName, '_Red_Tactic_Ne_Scatter'];
                else
                    fileName = [dataFolder, '\', missionName, '_Red_Tactic_Ne_ErrorBar'];
                end
            end
            saveas(figHandle, fileName, 'png');
        end
    end
end

end