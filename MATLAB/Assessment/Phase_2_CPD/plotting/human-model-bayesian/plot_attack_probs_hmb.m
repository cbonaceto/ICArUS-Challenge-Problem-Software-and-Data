function plot_attack_probs_hmb(subjects, intData, bluebookData,...
    subjectProbsByStage, normativeProbsByStage, modelProbsByStage,...
    subjectProbsAvgByStage, normativeProbsAvgByStage,...
    subjectPtProbs, normativePtProbs, modelPtProbs,...
    subjectPtProbsAvg, normativePtProbsAvg,...
    subjectChatterAvg, subjectSilentAvg,...
    attackProbStages, actualRedTactics, normativeStrategyName, modelName,...
    missionNum, saveData, showPlots, dataFolder)
%PLOT_ATTACK_PROBS Summary of this function goes here
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

numSubjects = size(subjectProbsByStage{1, 1}, 1);
numTrials = size(subjectProbsByStage{1, 1}, 2);
numStages = length(attackProbStages);
numLocations = size(subjectProbsByStage, 2);
assert(numLocations == 1 || numLocations == 2);

ptStage = find(strcmp('Pt', attackProbStages));
if ~isempty(ptStage) && ptStage > 0
    silentTrials = zeros(1, numTrials);
    chatterTrials = zeros(1, numTrials);
    for trial = 1:numTrials      
        if intData.redActivityDetected{1, trial}
            chatterTrials(trial) = 1;
        else
            silentTrials(trial) = 1;
        end
    end
else
    ptStage = -1;
end

%% Plot attack using the error bar format to show variance
attackProbStageNames = cell(numStages, 1);
for stage = 1:numStages
    attackProbStageNames{stage} = get_attack_prob_stage_name(attackProbStages{stage});
end
missionName = ['Mission ', num2str(missionNum)];
showNormativeVariance = 1;
for stage = 1:numStages
    %if stage ~= ptStage
    for location = 1:numLocations
        if numLocations > 1
            figName = [missionName, ', Stage ', num2str(stage), ', Location ', num2str(location),...
                ', ', attackProbStageNames{stage}, '  [Sample Size: ', num2str(numSubjects), ']'];
        else
            figName = [missionName, ', Stage ', num2str(stage), ', ', attackProbStageNames{stage},...
                '  [Sample Size: ', num2str(numSubjects), ']'];
        end
        figPosition = [60, 60, 1000, 800];
        legendSpacerChar = ' ';
        lineWidth = 1.5;
        normativeLineWidth = 1.25;
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
        ylabel(['Human Avg. (blue), ' modelName ' (purple), ' normativeStrategyName ' (black)']);        
        
        %Plot average subject probabilities as a blue solid line
        h(1) = plot(subjectProbsAvgByStage{stage, location}(:),...
            '-ob', 'LineWidth', lineWidth);
        legendText{1} = ['Human Avg.' legendSpacerChar attackProbStageNames{stage}];
        
        %Plot model probabilities as a purple solid line        
        h(2) = plot(modelProbsByStage{stage, location}(:),...
            '-om', 'LineWidth', lineWidth);
        legendText{2} = [modelName legendSpacerChar attackProbStageNames{stage}];
        
        %Plot normative probabilities as a black solid line
        h(3) = plot(normativeProbsAvgByStage{stage, location}(:),...
            '-*k', 'LineWidth', normativeLineWidth);
        legendText{3} = [normativeStrategyName legendSpacerChar attackProbStageNames{stage}];
        
        %Plot previous stage probabilities or BLUEBOOK values depending on the current
        %stage and mission type
        stageType = attackProbStages{stage};
        if strcmp(stageType, 'Pp')
            %Plot the BLUEBOOK values if there are multiple possible
            %Red tactics (Missions 2, 4, 5)
            numRedTactics = length(bluebookData.redTacticTypes);
            if numRedTactics > 1
                %bluebookData.bluebookProbs = cell(numSubjects, numTrials);
                %tactic 1 location 1, tactic 1 location 2; tactic 2
                %location 2, tactic 2 location 2
                tacticStrs = {'v', '^', '<', '>'};
                tacticColors = {'r', 'g', 'r', 'g'};
                for tactic = 1:numRedTactics
                    bluebookProbs = zeros(1, numTrials);
                    for trial = 1:numTrials
                        bluebookProbs(trial) = ...
                            bluebookData.bluebookProbs{1, trial}(tactic, location);
                    end
                    h(length(h)+1) = plot(1:numTrials, bluebookProbs,...
                        ['-' tacticStrs{tactic} tacticColors{tactic}],...
                        'LineWidth', lineWidth);
                    legendText{length(legendText)+1} = ...
                        [bluebookData.redTacticTypes{tactic} ' BLUEBOOK Probability'];
                end
                %If the ground truth Red tactics are known, also
                %plot the ground truth probabilities
                if ~isempty(actualRedTactics)
                    groundTruthProbs = zeros(1, numTrials);
                    for trial = 1:numTrials
                        groundTruthProbs(trial) = ...
                            bluebookData.bluebookProbs{1, trial}(actualRedTactics(1, trial), location);
                    end
                    h(length(h)+1) = plot(1:numTrials, groundTruthProbs,...
                        ':k', 'LineWidth', 1.5);
                    legendText{length(legendText)+1} = 'Ground Truth Probability';
                end
            end
        elseif strcmp(stageType, 'Ppc')
            %Plot HUMINT and subject probs from Pp stage if available
            h(length(h)+1) = plot(1:numTrials, intData.redCapabilityPc(1, :), '-*c', 'LineWidth', lineWidth); %#ok<*AGROW>
            legendText{length(legendText)+1} = 'HUMINT';
            ppStage = find(strcmp('Pp', attackProbStages));
            if ~isempty(ppStage) && ppStage > 0
                h(length(h)+1) = plot(subjectProbsAvgByStage{ppStage, location}(:), '-og', 'LineWidth', lineWidth);
                legendText{length(legendText)+1} = ['Human Avg.' legendSpacerChar 'P(Attack|IMINT, OSINT)'];
                
                %Also plot Bayesian probs from Pp stage if there
                %are multiple possible Red tactics (Missions 2, 4, 5)
                if length(bluebookData.redTacticTypes) > 1
                    h(length(h)+1) = plot(normativeProbsAvgByStage{ppStage, location}(:), '-*g', 'LineWidth', normativeLineWidth);
                    legendText{length(legendText)+1} = [normativeStrategyName legendSpacerChar 'P(Attack|IMINT, OSINT)'];
                end
            end
        elseif strcmp(stageType, 'Ptpc')
            %Plot probs from Pt stage and probs from Ppc stage if available
            if ptStage > 0
                h(length(h)+1) = plot(subjectProbsAvgByStage{ptStage, location}(:), '-or', 'LineWidth', lineWidth);
                legendText{length(legendText)+1} = ['Human Avg.' legendSpacerChar 'P(Attack|SIGINT)'];
            else
                %Plot one of two constant values for Chatter
                %or Silence trials computed as the average of human
                %responses across relevant trials
                if ~isempty(subjectChatterAvg) && ~isempty(subjectSilentAvg)
                    sigintAvg = zeros(1, numTrials);
                    for trial = 1:numTrials
                        if intData.redActivityDetected{1, trial}
                            sigintAvg(trial) = subjectChatterAvg;
                        else
                            sigintAvg(trial) = subjectSilentAvg;
                        end
                    end
                    h(length(h)+1) = plot(1:numTrials, sigintAvg, '-or', 'LineWidth', lineWidth);
                    legendText{length(legendText)+1} = ['Human Avg.' legendSpacerChar 'P(Attack|SIGINT)'];
                end
            end
            pcStage = find(strcmp('Ppc', attackProbStages));
            if ~isempty(pcStage) && pcStage > 0
                h(length(h)+1) = plot(subjectProbsAvgByStage{pcStage, location}(:), '-og', 'LineWidth', lineWidth);
                legendText{length(legendText)+1} = ['Human Avg.' legendSpacerChar 'P(Attack|HUMINT,' legendSpacerChar 'IMINT, OSINT)'];
            end
        end
        
        %Plot error bars that show the std for the subject probability distrubution at each trial
        subject_e = zeros(1, numTrials);
        normative_e = zeros(1, numTrials);
        for trial = 1:numTrials
            subject_e(trial) = sem(subjectProbsByStage{stage, location}(:, trial), numSubjects);
            normative_e(trial) = sem(normativeProbsByStage{stage, location}(:, trial), numSubjects);
        end
        errorbar(1:numTrials, subjectProbsAvgByStage{stage, location}(:), subject_e, '-b');
        %Also show normative probability distribution
        if showNormativeVariance
            errorbar(1:numTrials,...
                normativeProbsAvgByStage{stage, location}(:), normative_e, '-k');
        end
        %Create the legend
        if(length(h) > 2)
            %legend(h, legendText);
            legend(h, legendText, 'Location', 'SouthOutside');
        end
        
        %Decrease figure margins
        tightfig;
        
        %Save the figure
        if saveData
            if numLocations > 1
                fileName = [dataFolder, '\', missionName, '_Stage_', num2str(stage), '_Location_',...
                    num2str(location), '_Attack_Probabilities_HMB_ErrorBar'];
            else
                fileName = [dataFolder, '\', missionName, '_Stage_', num2str(stage),...
                    '_Attack_Probabilities_HMB_ErrorBar'];
            end
            saveas(figHandle, fileName, 'png');
        end
    end
end

%% Create SIGINT probability plots using the error bar format to show variance
figPosition = [60, 60, 1000, 800];
if ptStage > 0
    stage = ptStage;
    for sigintType = 1:2
        for location = 1:numLocations
            if sigintType == 1
                sigintTrials = find(silentTrials);
                sigintName = 'SIGINT = Silent';
                sigint = '_Silent';
            else
                sigintTrials = find(chatterTrials);
                sigintName = 'SIGINT = Chatter';
                sigint = '_Chatter';
            end
            
            if numLocations > 1
                figName = [missionName, ', Stage ', num2str(stage), ', ', ', Location ', num2str(location),...
                    ', ', attackProbStageNames{stage}, ', ', sigintName, '  [Sample Size: ', num2str(numSubjects), ']'];
            else
                figName = [missionName, ', Stage ', num2str(stage), ', ', attackProbStageNames{stage},...
                    ', ', sigintName, '  [Sample Size: ', num2str(numSubjects), ']'];
            end
            figHandle = figure('name', figName, 'position', figPosition, 'NumberTitle', 'off', 'Visible', visible);           
            lineWidth = 1.5;
            normativeLineWidth = 1;
            hold on;
            title(figName);
            xlabel('Trial');
            xlim([0 numTrials+1]);
            set(gca,'xtick', 1:numTrials);
            ymax = ylim;
            ylim([0 ymax(2)]);
            ylabel(['Human Avg. (blue), ' modelName ' (purple), ' normativeStrategyName ' (black)']);
            
            %Plot average subject probabilities as a blue solid line
            plot(sigintTrials,...
                subjectPtProbsAvg{sigintType, location}(1, sigintTrials), '-ob', 'LineWidth', lineWidth);
            
            %Plot model probabilities as a purple solid line
            plot(sigintTrials,...
                modelPtProbs{sigintType, location}(1, sigintTrials), '-om', 'LineWidth', lineWidth);
            
            %Plot normative probabilities as a black solid line
            plot(sigintTrials,...
                normativePtProbsAvg{sigintType, location}(1, sigintTrials), '-*k', 'LineWidth', normativeLineWidth);
            
            %Plot error bars that show the std for the subject probability distrubution at each trial
            subject_e = zeros(1, numTrials);
            normative_e = zeros(1, numTrials);
            for trial = sigintTrials
                subject_e(trial) = sem(subjectPtProbs{sigintType, location}(:, trial), numSubjects);
                normative_e(trial) = sem(normativePtProbs{sigintType, location}(:, trial), numSubjects);
            end
            errorbar(sigintTrials, subjectPtProbsAvg{sigintType, location}(1, sigintTrials),...
                subject_e(sigintTrials), '-b');
            %Also show normative probability distribution
            if showNormativeVariance
                errorbar(sigintTrials,...
                    normativePtProbsAvg{sigintType, location}(1, sigintTrials),...
                    normative_e(sigintTrials), '-k'); %#ok<*UNRCH>
            end
            
            %Decrease figure margins
            tightfig;
            
            %Save the figure
            if saveData
                if numLocations > 1
                    fileName = [dataFolder, '\', missionName,'_Stage_', num2str(stage), '_Location_',...
                        num2str(location), sigint, '_Sigint_Attack_Probabilities_HBM_ErrorBar'];
                else
                    fileName = [dataFolder, '\', missionName,'_Stage_', num2str(stage),...
                        sigint, '_Sigint_Attack_Probabilities_HMB_ErrorBar'];
                end
                saveas(figHandle, fileName, 'png');
            end
        end
    end
end

end