function plot_negentropy(subjects, subjectNe, normativeNe, subjectNeAvg,... 
    normativeNeAvg, attackProbStages, normativeStrategyName,...
    showSubjectsLegend, missionNum, saveData, showPlots, dataFolder)
%PLOT_NEGENTROPY Creates negentropy plots. Average negentropy is
%computed by computing the negentropy at each trial/stage and averaging it.
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

numSubjects = size(subjectNe{1}, 1);
numTrials = size(subjectNeAvg, 2);
numStages = length(attackProbStages);

ptStage = find(strcmp('Pt', attackProbStages));
if isempty(ptStage) || ptStage == 0
    ptStage = -1;
end

%% Plot attack probability negentropies using the scatter and error bar formats to show variance
attackProbStageNames = cell(numStages, 1);
for stage = 1:numStages
    attackProbStageNames{stage} = get_attack_prob_stage_name(attackProbStages{stage});
end
missionName = ['Mission ', num2str(missionNum)];
showNormativeVariance = 1;
for stage = 1:numStages
    %if stage ~= ptStage
    for errorVarianceFormat = 1:2
        figName = [missionName, ', Stage ', num2str(stage), ', ', attackProbStageNames{stage},...
            ' Negentropy  [Sample Size: ', num2str(numSubjects), ']'];
        if errorVarianceFormat == 1
            %Scatter format
            figPosition = [60, 60, 1200, 800];
            legendSpacerChar = char(10);
            lineWidth = 2;
            normativeLineWidth = 1.5;
        else
            %Error bar format
            figPosition = [60, 60, 1000, 800];
            legendSpacerChar = ' ';
            lineWidth = 1.5;
            normativeLineWidth = 1.25;
        end
        figHandle = figure('name', figName, 'position', figPosition, 'NumberTitle', 'off', 'Visible', visible);
        h = [];
        legendText = {};
        hold on;
        title(figName);
        xlabel('Trial');
        xlim([0 numTrials+1]);
        set(gca,'xtick', 1:numTrials);
        ymax = ylim;
        ylim([0 ymax(2)]);
        yLabelStr = ['Human Avg. (blue), ' normativeStrategyName ' (black)'];
        ylabel(yLabelStr);
        
        %Plot average subject negentropies as a blue solid line
        h(1) = plot(subjectNeAvg(stage, :), '-ob', 'LineWidth', lineWidth);
        legendText{1} = ['Human Avg. ' legendSpacerChar attackProbStageNames{stage} ' Negentropy'];
        
        %Plot normative negentropies as a black solid line
        h(2) = plot(normativeNeAvg(stage, :), '-*k', 'LineWidth', normativeLineWidth);
        legendText{2} = [normativeStrategyName legendSpacerChar attackProbStageNames{stage} ' Negentropy'];
        
        if errorVarianceFormat == 1
            %Create scatter plot that shows the subject negentropy distribution at each trial
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
                %subjectNe{stage} = zeros(numSubjects, numTrials);
                handle = plot(1:numTrials, subjectNe{stage}(subject, :),...
                    ['-' markers{markerIndex}], 'Color', colors(colorIndex,:));
                if showSubjectsLegend
                    h(length(h)+1) = handle;
                    legendText{length(legendText)+1} = subjects{subject}; %#ok<*AGROW>
                end
                %Also show normative probability distribution
                %if showNormativeVariance
                %    plot(1:numTrials,...
                %       normativeProbsByStage{stage}(subject, :), '*k');
                %end
                colorIndex = colorIndex + 1;
                markerIndex = markerIndex + 1;
            end
            %Create the legend
            if showSubjectsLegend
                legend(h, legendText, 'Location', 'NorthEastOutside');
            elseif length(h) > 2
                legend(h, legendText, 'Location', 'SouthOutside');
            end
        else
            %Plot error bars that show the std for the subject probability distrubution at each trial
            subject_e = zeros(1, numTrials);
            normative_e = zeros(1, numTrials);
            for trial = 1:numTrials
                subject_e(trial) = sem(subjectNe{stage}(:, trial), numSubjects);
                normative_e(trial) = sem(normativeNe{stage}(:, trial), numSubjects);
            end
            errorbar(1:numTrials, subjectNeAvg(stage, :), subject_e, '-b');
            %Also show normative negentropy distribution
            if showNormativeVariance
                errorbar(1:numTrials,...
                    normativeNeAvg(stage, :), normative_e, '-k'); %#ok<*UNRCH>
            end
            %Create the legend
            if(length(h) > 2)
                %legend(h, legendText);
                legend(h, legendText, 'Location', 'SouthOutside');
            end
        end
        
        %Decrease figure margins
        tightfig;
        
        %Save the figure
        if saveData
            if errorVarianceFormat == 1
                fileName = [dataFolder, '\', missionName, '_Stage_', num2str(stage),...
                    '_Attack_Negentropies_Scatter'];
            else
                fileName = [dataFolder, '\', missionName, '_Stage_', num2str(stage),...
                    '_Attack_Negentropies_ErrorBar'];
            end
            saveas(figHandle, fileName, 'png');
        end
    end
    %end
end

end