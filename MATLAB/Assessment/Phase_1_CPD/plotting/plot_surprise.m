function plot_surprise(surpriseData, groundTruthData, subjectProbs, troopAllocations,...
    baseTaskSets, saveData, showPlots, dataFolder)
%PLOT_SURPRISE Summary of this function goes here
%   Detailed explanation goes here

% Models: 
% New:
% 1. Surprise vs. 1-Pg where Pg is the probability of the ground truth group
% 2. Surprise vs. Ph-Pg where Ph is the largest probability
% 3. Surprise vs. (1-Pg)/Ph
% 4. Surprise vs. -log(Pg)
% 5. Surprise vs. -log(Pg/Ph)
% 6. Surprise vs. -log(Pg/Pl))
% 7. Surprise vs. -log(Pg/1-Pg)
% 8. Surprise vs. ((1-Pg) - log(Pg/1-Pg))/2 [*New]
% Original
% 1. Surprise vs. 1-Pg where Pg is the probability of the ground truth group
% 2. Surprise vs. -log(Pg)
% 3. Surprise vs. -log(Pg/1-Pg)
% 4. Suprise vs. (1-Pg)/(1-Ph) where Ph is the largest probability
% 5. Suprise vs. (1-Pg)/(1-Pl) where Pl is the smallest probability
% 6. Surprise vs. -log(Pg/Ph)
% 7. Surprise vs. -log(Pg/Pl))

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

%% Aggregate surprise, Pg, Ph, and Pl for each task set
% Create the task sets over which surprise data will be lumped together
maxAllocation = 1;
minAllocation = 0;
ALL = 0; RIGHT = 1; WRONG = 2;
epsilon = 0.01;
%baseTaskSets = {[1 2 3], [4 5 6]};
taskSetIndex = 1;
for index = 1:length(baseTaskSets)
    taskSet = baseTaskSets{index};    
    if isempty(find(taskSet==4, 1)) && isempty(find(taskSet==5, 1)) && ...
            isempty(find(taskSet==6, 1))
        taskSets{taskSetIndex} = taskSet;
        taskSetTypes(taskSetIndex) = RIGHT; %#ok<*AGROW>
        taskSetIndex = taskSetIndex + 1;
        taskSets{taskSetIndex} = taskSet;
        taskSetTypes(taskSetIndex) = WRONG;
        taskSetIndex = taskSetIndex + 1;
    end
    taskSets{taskSetIndex} = taskSet;
    taskSetTypes(taskSetIndex) = ALL;
    taskSetIndex = taskSetIndex + 1;
end

% Aggregate surprise, Pg, Ph, and Pl for each task set
numTaskSets = length(taskSets);
surprise = cell(numTaskSets, 1);
Pg = cell(numTaskSets, 1);
Ph = cell(numTaskSets, 1);
Pl = cell(numTaskSets, 1);
for taskSetIndex = 1:numTaskSets
    taskSet = taskSets{taskSetIndex};
    taskSetType = taskSetTypes(taskSetIndex);
    dataPointIndex = 1;
    for task = taskSet
        numSubjects = size(surpriseData{task}, 1);
        numTrials = size(surpriseData{task}, 2);
        numStages = size(subjectProbs{task}{1,1}, 1);
        for subject = 1:numSubjects
            for trial = 1:numTrials                
                groundTruthIndex = groundTruthData{task}(1, trial);               
                addDataPoint = taskSetType == ALL;
                if taskSetType ~= ALL
                    allocation = troopAllocations{task}{subject, trial};
                    if taskSetType == RIGHT
                        %Determine if troops allocated against the ground truth group
                        if allocation(groundTruthIndex) == maxAllocation
                            addDataPoint = true;
                        end
                    else
                        %Determine if troops not allocated against the ground truth group
                        if allocation(groundTruthIndex) == minAllocation
                            addDataPoint = true;
                        end
                    end
                end
                if addDataPoint
                    p_subj = fillzerobins(subjectProbs{task}{subject, trial}(numStages, :), epsilon);
                    surprise{taskSetIndex}(dataPointIndex) = surpriseData{task}(subject, trial);
                    Pg{taskSetIndex}(dataPointIndex) = p_subj(groundTruthIndex);
                    Ph{taskSetIndex}(dataPointIndex) = max(p_subj);
                    Pl{taskSetIndex}(dataPointIndex) = min(p_subj);
                    dataPointIndex = dataPointIndex + 1;
                end
            end
        end
    end
end

%% Create plots
% 1. Surprise vs. 1-Pg where Pg is the probability of the ground truth group
% 2. Surprise vs. Ph-Pg where Ph is the largest probability
% 3. Surprise vs. (1-Pg)/Ph
% 4. Surprise vs. -log(Pg)
% 5. Surprise vs. -log(Pg/Ph)
% 6. Surprise vs. -log(Pg/Pl))
% 7. Surprise vs. -log(Pg/1-Pg)
figPosition = [60, 200, 840, 600];
taskSetIndex = 1;
%for taskSetIndex = 1:numTaskSets    
while taskSetIndex <= numTaskSets
    %taskSetIndex
    taskSet = taskSets{taskSetIndex};
    plotNextTaskSet = taskSetTypes(taskSetIndex) == RIGHT;
    if plotNextTaskSet
        taskName = createTaskSetName(taskSet, 3);
    else
        taskName = createTaskSetName(taskSet, taskSetTypes(taskSetIndex));
    end
    %Plot the "RIGHT" and "WRONG" data together in a single plot    
    for i = 1:8
        switch i
            case 1
                % 1. Surprise vs. Probability of Ground Truth Group (Pg)
                figName = [taskName ' Surprise vs. 1-Pg'];
                plotType = 'Surprise_vs_Pg';
                probType = '1-Pg';
                probData = 1 - Pg{taskSetIndex};
                if plotNextTaskSet
                    probData2 = 1 - Pg{taskSetIndex+1};
                end
            case 2
                % 2. Surprise vs. Ph-Pg where Ph is the largest probability
                figName = [taskName ' Surprise vs. Pmax-Pg'];
                plotType = 'Surprise_vs_Pmax-Pg';
                probType = 'Pmax-Pg';
                probData = Ph{taskSetIndex} - Pg{taskSetIndex};
                if plotNextTaskSet
                    probData2 = Ph{taskSetIndex+1} - Pg{taskSetIndex+1};
                end
            case 3
                % 3. Surprise vs. (1-Pg)/Ph
                figName = [taskName ' Surprise vs. (1-Pg)/Pmax'];
                plotType = 'Surprise_vs_1-Pg_over_Pmax';
                probType = '(1-Pg)/Pmax';
                probData = (1 - Pg{taskSetIndex}) ./ Ph{taskSetIndex};
                if plotNextTaskSet
                    probData2 = (1 - Pg{taskSetIndex+1}) ./ Ph{taskSetIndex+1};
                end
            case 4
                % 4. Surprise vs. -log(Pg)
                figName = [taskName ' Surprise vs. -log(Pg)'];
                plotType = 'Surprise_vs_-log_Pg';
                probType = '-log(Pg)';
                probData = -log(Pg{taskSetIndex});
                if plotNextTaskSet
                    probData2 = -log(Pg{taskSetIndex+1});
                end
            case 5
                % 5. Surprise vs. -log(Pg/Ph)
                figName = [taskName ' Surprise vs. -log(Pg/Pmax)'];
                plotType = 'Surprise_vs_-log_Pg_over_Pmax';
                probType = '-log(Pg/Pmax)';
                probData = -log((Pg{taskSetIndex}) ./ (Ph{taskSetIndex}));
                if plotNextTaskSet
                    probData2 = -log((Pg{taskSetIndex+1}) ./ (Ph{taskSetIndex+1}));
                end
            case 6
                % 6. Surprise vs. -log(Pg/Pl))
                figName = [taskName ' Surprise vs. -log(Pg/Pmin)'];
                plotType = 'Surprise_vs_-log_Pg_over_Pmin';
                probType = '-log(Pg/Pmin)';
                probData = -log((Pg{taskSetIndex}) ./ (Pl{taskSetIndex}));
                if plotNextTaskSet
                    probData2 = -log((Pg{taskSetIndex+1}) ./ (Pl{taskSetIndex+1}));
                end
            case 7
                % 7. Surprise vs. -log(Pg/1-Pg)
                figName = [taskName ' Surprise vs. -log(Pg/1-Pg)'];
                plotType = 'Surprise_vs_-log_Pg_over_1-Pg';
                probType = '-log(Pg/1-Pg)';
                probData = -log((Pg{taskSetIndex}) ./ (1 - Pg{taskSetIndex}));
                if plotNextTaskSet
                    probData2 = -log((Pg{taskSetIndex+1}) ./ (1 - Pg{taskSetIndex+1}));
                end
            case 8
                % 8. Surprise vs. ((1-Pg) - log(Pg/1-Pg))/2 [*New]
                figName = [taskName ' Surprise vs. ((1-Pg) - log(Pg/1-Pg))/2'];
                plotType = 'Surprise_vs_avg_1-Pg_-log_odds';
                probType = '((1-Pg) - log(Pg/1-Pg))/2';
                probData = ((1-Pg{taskSetIndex}) - log((Pg{taskSetIndex}) ./ (1 - Pg{taskSetIndex})))/2;
                if plotNextTaskSet
                    probData2 = ((1-Pg{taskSetIndex+1}) - log((Pg{taskSetIndex+1}) ./ (1 - Pg{taskSetIndex+1})))/2;
                end
        end
        %probData'
        
        %For each level of surprise, compute mean and STD of probData
        minSurprise = min(surprise{taskSetIndex});
        maxSurprise = max(surprise{taskSetIndex}); 
        surpriseVals = minSurprise:1:maxSurprise;
        probDataAvg = zeros(length(surpriseVals), 1);
        probDataStd = zeros(length(surpriseVals), 1);
        index = 1;
        for s = surpriseVals
            pData = probData(surprise{taskSetIndex}==s);            
            probDataAvg(index) = mean(pData);
            probDataStd(index) = std(pData);
            index = index + 1;
        end
        if plotNextTaskSet
            minSurprise2 = min(surprise{taskSetIndex+1});
            maxSurprise2 = max(surprise{taskSetIndex+1});
            surpriseVals2 = minSurprise2:1:maxSurprise2;
            probDataAvg2 = zeros(length(surpriseVals2), 1);
            probDataStd2 = zeros(length(surpriseVals2), 1);
            index = 1;
            for s = surpriseVals2
                pData = probData2(surprise{taskSetIndex+1}==s);
                probDataAvg2(index) = mean(pData);
                probDataStd2(index) = std(pData);
                index = index + 1;
            end
        end
        
        %Compute linear regression stats (R^2) and show in figure title
        %surprise{taskSetIndex}                
        %[~,~,~,~,stats] = regress(surprise{taskSetIndex}', [ones(length(probData), 1) probData']);
        [b,~,~,~,stats] = regress(probData', [ones(length(surprise{taskSetIndex}), 1) surprise{taskSetIndex}']);
        r = stats(1);
        if plotNextTaskSet
            [b2,~,~,~,stats] = regress(probData2', [ones(length(surprise{taskSetIndex+1}), 1) surprise{taskSetIndex+1}']);
            r2 = stats(1);
        end       
        
        %DEBUG CODE
        cc = corr(surprise{taskSetIndex}', probData');
        if plotNextTaskSet
            cc2 = corr(surprise{taskSetIndex+1}', probData2');
            disp([figName ', R1: ' num2str(cc) ', R2: ', num2str(cc2)]);
        else
            disp([figName ', R: ' num2str(cc)]);
        end
        %END DEBUG CODE
        
        figHandle = figure('name', figName, 'position', figPosition, 'NumberTitle', 'off', 'Visible', visible);
        if plotNextTaskSet
            title([figName '   (R^2 for Right = ' num2str(r) ...
                ', R^2 for Wrong = ' num2str(r2) ')']);
        else
            title([figName '   (R^2 = ' num2str(r) ')']);
        end
        hold on;                
        xlabel('Surprise');
        xlim([-1 7]);
        set(gca,'xtick', 0:6);
        ylabel(probType);       
        
        %Plot surprise vs prob data (prob data binned at each suprirse level with error bars)
        %Also plot the regression lines
        if plotNextTaskSet
            %Plot "Subject Right" surprise data
            h1 = plot(surpriseVals, probDataAvg, 'ok');
            errorbar(surpriseVals, probDataAvg, probDataStd, 'k');
            %Plot regression line
            x1fit = (minSurprise-1):1:(maxSurprise+1);            
            plot(x1fit, b(1) + b(2) * x1fit, ':k');
            
            %Plot "Subject Wrong" surprise data
            h2 = plot(surpriseVals2+.1, probDataAvg2, 'or');
            errorbar(surpriseVals2+.1, probDataAvg2, probDataStd2, 'r');
            x1fit = (minSurprise2-1):1:(maxSurprise2+1);
            plot(x1fit, b2(1) + b2(2) * x1fit, ':r');
        else
            %Plot all surprise data and error bars
            plot(surpriseVals, probDataAvg, 'ob');
            errorbar(surpriseVals, probDataAvg, probDataStd, 'b');
            %Plot regression line            
            x1fit = (minSurprise-1):1:(maxSurprise+1);
            plot(x1fit, b(1) + b(2) * x1fit, ':b');
        end
        %plot(surprise{taskSetIndex}, probData, 'x');
        %plot(probData, surprise{taskSetIndex}, 'x');        
        
        %Create legend
        if plotNextTaskSet
            legend([h1 h2], {'Subject Right', 'Subject Wrong'}, 'Location', 'SouthEast');
        end
        
        %Save figure
        if saveData
            fileName = [dataFolder, '\', taskName, '_', plotType];            
            saveas(figHandle, fileName, 'png');
        end                
    end
    
    if plotNextTaskSet
        taskSetIndex = taskSetIndex + 2;
    else
        taskSetIndex = taskSetIndex + 1;
    end
end

function taskSetName = createTaskSetName(taskSet, taskSetType)
    taskTypeString = '';
    if taskSetType == 1
        taskTypeString = ' (Subject Right)';
    elseif taskSetType == 2
        taskTypeString = ' (Subject Wrong)';
    elseif taskSetType == 3
        taskTypeString = ' (Subject Right+Wrong)';
    end
    taskSetName = ['Missions ' num2separatedStr(taskSet) taskTypeString];
end
end