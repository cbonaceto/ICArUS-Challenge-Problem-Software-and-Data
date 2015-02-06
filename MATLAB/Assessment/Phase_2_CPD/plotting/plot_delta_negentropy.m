function plot_delta_negentropy(subjects, subjectNeAvg, normativeNeAvg,...
    subjectDeltaNe, normativeDeltaNe, stage1, stage2,...
    showSubjectsLegend, missionNum, saveData, showPlots, dataFolder)
%PLOT_DELTA_NEGENTROPY Plot the change in subject and normative negentropy from
%one stage to the next.

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
numTrials = size(subjectNeAvg, 2);

%% Compute average delta Ne from stage 1 to stage 2 based on the average subject
%  and normative negentropies.
subjectDeltaNeAvg = zeros(1, numTrials);
normativeDeltaNeAvg = zeros(1, numTrials);
for trial = 1:numTrials    
    subjectDeltaNeAvg(trial) = subjectNeAvg(stage2, trial) - subjectNeAvg(stage1, trial);
    normativeDeltaNeAvg(trial) = normativeNeAvg(stage2, trial) - normativeNeAvg(stage1, trial);    
end

%% Plot subject and normative delta Ne from stage 1 to stage 2
missionName = ['Mission ' num2str(missionNum)];
showNormativeVariance = 1;
figName = [missionName, ', Delta-Ne from Stage ', num2str(stage1), ' to Stage ', num2str(stage2),...
        '  [Sample Size: ', num2str(numSubjects), ']'];
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
    xlabel('Trial');
    xlim([0 numTrials+1]);
    set(gca,'xtick', 1:numTrials);
    ymax = ylim;
    ylim([-.2 ymax(2)]);
    ylabel('Delta-Ne');
    
    %Plot average subject delta Ne as a blue solid line
    h(1) = plot(abs(subjectDeltaNeAvg), '-ob', 'LineWidth', lineWidth);
    legendText{1} = 'Human Avg. Delta-Ne';
    
    %Plot normative delta Ne as a black solid line
    h(2) = plot(abs(normativeDeltaNeAvg), '-*k', 'LineWidth', normativeLineWidth);
    legendText{2} = 'Bayesian Delta-Ne';
    
    if errorVarianceFormat == 1
        %Create scatter plot that shows subject probability distribution at each trial
        colormap(lines(numSubjects));
        colors = colormap;
        numColors = size(colors, 1);
        markers = {'x', 's', 'd', '^', 'v', '>', '<', 'h'};
        numMarkers = length(markers);
        colorIndex = 1;
        markerIndex = 1;
        %h = zeros(numSubjects, 1);
        for subject = 1:numSubjects
            if colorIndex > numColors
                colorIndex = 1;
            end
            if markerIndex > numMarkers
                markerIndex = 1;
            end
            handle = plot(1:numTrials, abs(subjectDeltaNe{stage2}(subject, :)),...
                ['-' markers{markerIndex}], 'Color', colors(colorIndex,:));
            if showSubjectsLegend
                h(length(h)+1) = handle;
                legendText{length(legendText)+1} = subjects{subject}; %#ok<*AGROW>            '
            end
            colorIndex = colorIndex + 1;
            markerIndex = markerIndex + 1;
        end
        %Create the legend
        if showSubjectsLegend
            legend(h, legendText, 'Location', 'NorthEastOutside');
        elseif(length(h) > 2)
            legend(h, legendText, 'Location', 'SouthOutside');
        end
    else
        %Plot error bars that show the std for the subject delta Ne at each trial
        subject_e = zeros(1, numTrials);
        normative_e = zeros(1, numTrials);
        for trial = 1:numTrials
            %subjectDeltaNe{stage}(subject, trial)
            subject_e(trial) = sem(abs(subjectDeltaNe{stage2}(:, trial)), numSubjects);
            normative_e(trial) = sem(abs(normativeDeltaNe{stage2}(:, trial)), numSubjects);
        end
        errorbar(1:numTrials, abs(subjectDeltaNeAvg), subject_e, '-b');
        %Also show the normative delta netentropy distribution
        if showNormativeVariance
            errorbar(1:numTrials, abs(normativeDeltaNeAvg), normative_e, '-k'); %#ok<*UNRCH>            
        end
        %Create the legend
        if(length(h) > 2)
            legend(h, legendText, 'Location', 'SouthOutside');
        end
    end
    
    %Decrease figure margins
    tightfig;
    
    %Save the figure
    if saveData        
        if errorVarianceFormat == 1
            fileName = [dataFolder, '\', missionName, '_Delta_Ne_From_', num2str(stage1),...
                '_To_' num2str(stage2) '_Scatter'];
        else
            fileName = [dataFolder, '\', missionName, '_Delta_Ne_From_', num2str(stage1),...
                '_To_' num2str(stage2) '_ErrorBar'];            
        end
        saveas(figHandle, fileName, 'png');
    end
end

%% Plot delta Ne separately for trials with delta Ne > 0 for average subject and normative negentropies,
%  and trials with delta Ne < 0 for average subject and normative negentropies
for posNeg = 1:2
    if posNeg == 1
        figName = [missionName, ', Delta-Ne from Stage ', num2str(stage1), ' to Stage ', num2str(stage2),...
            ', Positive Trials  [Sample Size: ', num2str(numSubjects), ']'];
    else
        figName = [missionName, ', Delta-Ne from Stage ', num2str(stage1), ' to Stage ', num2str(stage2),...
            ', Negative Trials  [Sample Size: ', num2str(numSubjects), ']'];
    end
    figHandle = figure('name', figName, 'position', figPosition, 'NumberTitle', 'off', 'Visible', visible);
    h = zeros(1, 2);
    legendText = cell(1, 2);
    hold on;
    title(figName);
    xlabel('Trial');
    xlim([0 numTrials+1]);
    set(gca,'xtick', 1:numTrials);
    ymax = ylim;
    ylim([-.2 ymax(2)]);
    ylabel('Delta-Ne');
    
    lineWidth = 1.5;
    normativeLineWidth = 1.25;
    
    if posNeg == 1
        %Plot average subject delta Ne as a blue solid line
        deltaNePosTrials = find(subjectDeltaNeAvg > 0);
        h(1) = plot(deltaNePosTrials, abs(subjectDeltaNeAvg(deltaNePosTrials)), '-ob',...
            'LineWidth', lineWidth);
        legendText{1} = 'Human Avg. Delta-Ne Positive Trials';
        errorbar(deltaNePosTrials, abs(subjectDeltaNeAvg(deltaNePosTrials)),...
            subject_e(deltaNePosTrials), '-b');
        %errorbar(1:numTrials, abs(subjectDeltaNeAvg), subject_e, '-b');
        
        %Plot normative delta Ne as a black solid line
        deltaNePosTrials = find(normativeDeltaNeAvg > 0);
        h(2) = plot(deltaNePosTrials, abs(normativeDeltaNeAvg(deltaNePosTrials)),...
            '-*k', 'LineWidth', normativeLineWidth);
        legendText{2} = 'Bayesian Delta-Ne Positive Trials';
    else
        %Plot average subject delta Ne as a blue solid line
        deltaNeNegTrials = find(subjectDeltaNeAvg < 0);
        h(1) = plot(deltaNeNegTrials, abs(subjectDeltaNeAvg(deltaNeNegTrials)), '-ob',...
            'LineWidth', lineWidth);
        legendText{1} = 'Human Avg. Delta-Ne Negative Trials';
        errorbar(deltaNeNegTrials, abs(subjectDeltaNeAvg(deltaNeNegTrials)),...
            subject_e(deltaNeNegTrials), '-b');
        
        %Plot normative delta Ne as a black solid line
        deltaNeNegTrials = find(normativeDeltaNeAvg < 0);
        h(2) = plot(deltaNeNegTrials, abs(normativeDeltaNeAvg(deltaNeNegTrials)),...
            '-*k', 'LineWidth', normativeLineWidth);
        legendText{2} = 'Bayesian Delta-Ne Negative Trials';
    end
    
    %Create the legend
    legend(h, legendText, 'Location', 'SouthOutside');
    
    %Decrease figure margins
    tightfig;
    
    %Save the figure
    if saveData
        if posNeg == 1
            fileName = [dataFolder, '\', missionName, '_Delta_Ne_From_', num2str(stage1),...
            '_To_' num2str(stage2) '_Pos_Trials'];
        else
            fileName = [dataFolder, '\', missionName, '_Delta_Ne_From_', num2str(stage1),...
            '_To_' num2str(stage2) '_Neg_Trials'];
        end        
        saveas(figHandle, fileName, 'png');
    end
end

end