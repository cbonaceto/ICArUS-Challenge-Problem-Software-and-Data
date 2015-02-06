function [missionData, missionMetrics] = create_all_metrics_plots(missions,...
    plotRedTactics, plotAttackProbs, plotAttackProbsNegentropy,...
    plotSigintChoices, plotBlueActions,  plotModelComparison,...
    plotIndividualDifferences, plotDetailedTime, plotTotalTime,...
    dataInputFolder, figureOutputFolder, saveData, showPlots)
%CREATE_ALL_METRICS_PLOTS Summary of this function goes here
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

%The cutoff for showing subject number legends in scatter plots
maxSubjectsForLegend = 30;

%% Aggregate survey data if plotting individual differences
if plotIndividualDifferences
    [surveyData.subjects, surveyData.ages, surveyData.genders,...
        surveyData.currentDegrees, surveyData.highestDegrees,...
        surveyData.yearsExperience, surveyData.yearsGeoIntExperience,...
        surveyData.probsAndStatsTraining, surveyData.probsAndStatsUsage,...
        surveyData.geoDataUsage, surveyData.geoIntExperience, surveyData.bis,...
        surveyData.basDrive, surveyData.basFunSeeking, surveyData.basRewardResponsiveness,...
        surveyData.basTotal, surveyData.foxHedgehog, surveyData.sbsdt,...
        surveyData.wlt, surveyData.crt, surveyData.vge] = aggregate_csv_survey_data(...
        [dataInputFolder '\Aggregated Data\processed_survey_responses.csv']);
end

%% Aggregate detailed timing data if plotting detailed time metrics
if plotDetailedTime
    [timeDetails.subjects, timeDetails.examTrainingTimes, timeDetails.examTimes,...
        timeDetails.missionTimes, timeDetails.missionTrainingTimes] = ...
        aggregate_csv_time_data([dataInputFolder '\Aggregated Data\exam_times.csv']);
end

%% Aggregate subject data for missions
missionData = cell(length(missions),1);
timeData = cell(length(missions),1);
missionMetrics = cell(length(missions),1);
chatterAvg = [];
silentAvg = [];
i = 1;
for mission = missions   
    [d.subjects, d.intData, d.actualRedTactics, d.mostLikelyRedTacticsData, d.redTacticProbsData,...
     d.batchPlotData, d.bluebookData, d.attackProbStages, d.subjectAttackProbs,...
     d.normativeAttackProbs, d.sigintSelectionData, d.blueActionSelectionData, d.timeDataByTrial] = ...
        aggregate_csv_mission_data(strcat(dataInputFolder,'\Aggregated Data\allresponses_mission_', num2str(mission), '.csv'), mission);
    missionData{i} = d;    
    timeData{i} = d.timeDataByTrial;    
    
    missionMetrics{i} = ...
        compute_mission_metrics(d.intData, d.mostLikelyRedTacticsData,...
            d.redTacticProbsData,...
            d.batchPlotData,d.sigintSelectionData,...
            d.subjectAttackProbs, d.normativeAttackProbs, d.attackProbStages,...
            d.blueActionSelectionData);
    %missionMetrics{i} = m;
    if ~isempty(missionMetrics{i}.chatterAvg)
        chatterAvg = missionMetrics{i}.chatterAvg;
        silentAvg = missionMetrics{i}.silentAvg;
    end
    
    if i == 1
        subjects = d.subjects;
    end
    i = i + 1;
end

%% Create subject demographics and individual differences plots
if plotIndividualDifferences
    dataFolder = strcat(figureOutputFolder, '\subject_demographics');
    if saveData && ~isdir(dataFolder)
        mkdir(dataFolder);
    end
    plot_subject_demographics(surveyData.subjects, surveyData.ages,...
        surveyData.genders, surveyData.currentDegrees, surveyData.highestDegrees,...
        surveyData.probsAndStatsTraining, surveyData.probsAndStatsUsage,...
        surveyData.geoDataUsage, surveyData.geoIntExperience,...
        saveData, showPlots, dataFolder)
    dataFolder = strcat(figureOutputFolder, '\individual_differences');
    if saveData && ~isdir(dataFolder)
        mkdir(dataFolder);
    end
    %Original Missions: [1, 2], 2, 3
    %if 1 == 2
    plot_individual_differences(surveyData.subjects, missionData,...
        [1, 2, 3], 2, 3, surveyData.highestDegrees, surveyData.yearsExperience,...
        surveyData.probsAndStatsTraining, surveyData.probsAndStatsUsage,...
        surveyData.geoDataUsage, surveyData.geoIntExperience, surveyData.bis,...
        surveyData.basDrive, surveyData.basFunSeeking,...
        surveyData.basRewardResponsiveness, surveyData.basTotal,...
        surveyData.foxHedgehog, surveyData.sbsdt, surveyData.wlt,...
        surveyData.crt, surveyData.vge, saveData, showPlots, dataFolder);    
    %end
end

%% Plot comparison of human probabilities to the averaging model and the A-B model, and a
%  comparison of the mean and median human probabilities (Missions 1-2)
if plotModelComparison    
    %Plot Human Ne, Bayesian Ne, Model 1 Ne, Model 2 Ne
    dataFolder = strcat(figureOutputFolder, '\model_comparison_figures');
    if saveData && ~isdir(dataFolder)
        mkdir(dataFolder);
    end
    modelTypes = {'Averaging', 'A-B'};    
    modelFunctions = {@averaging, @ab};
    i = 1;
    subjectLikelihoodsAvg = [];
    subjectLikelihoodsMedian = [];
    subjectLikelihoodsStage = 0;    
    for mission = missions
        if mission == 1 || mission == 2
            d = missionData{i}; 
            m = missionMetrics{i};            
            plot_mean_median_comparison(d.subjects, m.subjectProbsByStage, m.normativeProbsByStage,...
                m.subjectProbsAvgByStage, m.subjectProbsMedianByStage, m.normativeProbsAvgByStage,...
                m.subjectNe, m.normativeNe, m.subjectNeAvg, m.subjectNeMedian, m.normativeNeAvg,...
                'Bayesian', mission, saveData, showPlots, dataFolder);
            if mission == 1
                subjectLikelihoodsAvg = m.subjectProbsAvg;
                subjectLikelihoodsMedian = m.subjectProbsMedian;
                subjectLikelihoodsStage = find(strcmp('Pt', d.attackProbStages));
            end            
            for probsType = 1:2
                modelData = cell(length(modelTypes),1);
                if probsType == 1
                    probsName = 'Mean';
                    probsAvg = m.subjectProbsAvg;
                    probsAvgByStage = m.subjectProbsAvgByStage;
                    neAvg = m.subjectNeAvg;
                    likelihoodsAvg = subjectLikelihoodsAvg;
                else
                    probsName = 'Median';
                    probsAvg = m.subjectProbsMedian;
                    probsAvgByStage = m.subjectProbsMedianByStage;
                    neAvg = m.subjectNeMedian;
                    likelihoodsAvg = subjectLikelihoodsMedian;
                end
                for j=1:length(modelTypes)                                        
                    [d.modelProbs, d.modelProbsAvg, d.modelNe, d.modelNeAvg,...
                            d.modelProbs_subjectLikelihoods, d.modelProbsAvg_subjectLikelihoods,...
                            d.modelNe_subjectLikelihoods, d.modelNeAvg_subjectLikelihoods] = ...
                            compute_model_results(d.subjectAttackProbs, probsAvg,...
                            likelihoodsAvg, subjectLikelihoodsStage, d.attackProbStages,...
                            d.intData.redActivityDetected, modelFunctions{j});
                    d.modelType = modelTypes{j};
                    modelData{j} = d;
                end
                plot_model_comparisons(d.subjects, m.subjectProbsByStage, m.normativeProbsByStage,...
                    probsAvgByStage, m.normativeProbsAvgByStage,...
                    m.subjectNe, m.normativeNe, neAvg, m.normativeNeAvg, 'Bayesian',...
                    probsName, modelData, mission, saveData, showPlots, dataFolder);
            end            
        end
        i = i + 1;
    end
end

%% Create most likely Red tactic plots (Mission 2), Red tactic probability plots
%  (Missions 4-5), and batch plot plots (Missions 4-5)
if plotRedTactics
    dataFolder = strcat(figureOutputFolder, '\red_tactics_figures');
    if saveData && ~isdir(dataFolder)
        mkdir(dataFolder);
    end
    i = 1;
    for mission = missions
        d = missionData{i};
        m = missionMetrics{i};
        showSubjectsLegend = length(d.subjects) <= maxSubjectsForLegend;
        if ~isempty(m.mostLikelyTacticSelections)                        
            plot_most_likely_red_tactics(d.subjects, m.mostLikelyTacticSelections,...
                d.mostLikelyRedTacticsData.tacticTypes,...
                mission, saveData, showPlots, dataFolder);
        elseif ~isempty(d.redTacticProbsData)
            plot_red_tactic_probs(d.subjects, d.redTacticProbsData.tacticProbs,...
                m.redTacticProbsAvg, m.redTacticNe, m.redTacticNeAvg,...
                m.normativeRedTacticProbsAvg, m.normativeRedTacticNeAvg,...
                d.actualRedTactics, d.redTacticProbsData.tacticTypes, 'Bayesian (Assuming No Red Change)',...
                showSubjectsLegend, mission, saveData, showPlots, dataFolder);
        end        
        if ~isempty(m.batchPlotCreationTrials)
            plot_batch_plots(d.subjects, m.batchPlotCreationTrials, m.batchPlotSearchDepthsAllTrials,...
                mission, saveData, showPlots, dataFolder);
            %plot_batch_plots(d.subjects, m.batchPlotCreationTrials, m.batchPlotSearchDepths,...
            %    mission, saveData, showPlots, dataFolder)
        end
        i = i + 1;
    end
end

%% Create attack probability plots (Missions 1-2, 4-5), "just picked one"
%  plots (Missions 2, 4, 5), and "no change after SIGINT" plot (Mission 3)
if plotAttackProbs
    dataFolder = strcat(figureOutputFolder, '\probability_figures');
    if saveData && ~isdir(dataFolder)
        mkdir(dataFolder);
    end
    i = 1;
    for mission = missions        
        d = missionData{i};
        m = missionMetrics{i};
        showSubjectsLegend = length(d.subjects) <= maxSubjectsForLegend;
        normativeStrategyName = 'Bayesian';
        if mission == 4 || mission == 5
            normativeStrategyName = 'Bayesian (Assuming No Red Change)';
        end
        plot_attack_probs(d.subjects, d.intData, d.bluebookData,...
            m.subjectProbsByStage, m.normativeProbsByStage, m.subjectProbsAvgByStage, ...
            m.normativeProbsAvgByStage, m.subjectPtProbs, m.normativePtProbs,...
            m.subjectPtProbsAvg, m.normativePtProbsAvg, chatterAvg, silentAvg,...
            d.attackProbStages, d.actualRedTactics, normativeStrategyName,...
            showSubjectsLegend, mission, saveData, showPlots, dataFolder);        
        if mission == 2
            plot_just_picked_one_tactic(d.subjects, d.bluebookData,...
                d.mostLikelyRedTacticsData, d.subjectAttackProbs,...
                d.attackProbStages, mission, saveData, showPlots, dataFolder)
        elseif mission == 4 || mission == 5
             plot_just_picked_one_tactic(d.subjects, d.bluebookData,...
                d.redTacticProbsData, d.subjectAttackProbs,...
                d.attackProbStages, mission, saveData, showPlots, dataFolder)
        end
        if mission == 3
            plot_no_changes_after_sigint(d.subjects, d.subjectAttackProbs,...
                d.attackProbStages, d.sigintSelectionData, mission, saveData,...
                showPlots, dataFolder)
        end
        i = i + 1;
    end
end

%% Create attack probability Negentropy plots (Missions 1-5)
if plotAttackProbsNegentropy
    %Create folder to store figure if it doesn't exist
    dataFolder = strcat(figureOutputFolder, '\negentropy_figures');
    if saveData && ~isdir(dataFolder)
        mkdir(dataFolder);
    end
    i = 1;
    for mission = missions       
        d = missionData{i};
        m = missionMetrics{i};
        showSubjectsLegend = length(d.subjects) <= maxSubjectsForLegend;
        normativeStrategyName = 'Bayesian';
        if mission == 4 || mission == 5
            normativeStrategyName = 'Bayesian (Assuming No Red Change)';
        end
        if mission == 1 || mission == 2 || mission == 3 || mission == 4 || mission == 5;
            %Plot negentropy (Missions 1 - 5)
            plot_negentropy(d.subjects, m.subjectNe, m.normativeNe, m.subjectNeAvg,... 
                m.normativeNeAvg, d.attackProbStages, normativeStrategyName,...
                showSubjectsLegend, mission, saveData, showPlots, dataFolder);
            
            %Plot delta negentropy (Missions 1 - 3)
            pTpcStage = find(strcmp('Ptpc', d.attackProbStages));
            if ~isempty(pTpcStage)
                ptStage = find(strcmp('Pt', d.attackProbStages));
                if ~isempty(ptStage)
                    prevStage = ptStage - 1;
                else
                    prevStage = pTpcStage - 1;
                end
                if prevStage > 0 && prevStage < pTpcStage                    
                    plot_delta_negentropy(d.subjects, m.subjectNeAvg, m.normativeNeAvg,...
                        m.subjectDeltaNe, m.normativeDeltaNe, prevStage, pTpcStage,...
                        showSubjectsLegend, mission, saveData, showPlots, dataFolder);
                end
            end
        end
        i = i + 1;
    end
end

%% Create confirmation bias plots based on SIGINT location selections (Mission 3 only)
if plotSigintChoices
    %Create folder to store figure if it doesn't exist
    dataFolder = strcat(figureOutputFolder, '\sigint_selection_figures');
    if saveData && ~isdir(dataFolder)
        mkdir(dataFolder);
    end
    i = 1;
    for mission = missions
        d = missionData{i};        
        if ~isempty(d.sigintSelectionData)
            m = missionMetrics{i};
            plot_sigint_choices(d.subjects, d.sigintSelectionData.selectionBayesianOptimal,...
                m.sigintSelectionFrequencies, m.sigintSelectionOptimalFrequencies,...
                m.sigintSelectionParticipantNormativeFrequencies,...
                m.sigintSelectionParticipantNormativeOptimalFrequencies,...
                mission, saveData, showPlots, dataFolder);
        end
        i = i + 1;
    end
end

%% Create Blue action selection plots (Missions 2-5)
if plotBlueActions
    %Create folder to store figure if it doesn't exist
    dataFolder = strcat(figureOutputFolder, '\blue_action_choices_figures');
    if saveData && ~isdir(dataFolder)
        mkdir(dataFolder);
    end
    i = 1;
    for mission = missions
        if mission ~= 1			
            m = missionMetrics{i};
            if ~isempty(m.blueActionFrequencies)
                d = missionData{i};
                plot_blue_actions(d.subjects, d.blueActionSelectionData.normativeBayesianActions,...
                    m.notDivertFrequency, m.blueActionFrequencies,...
                    m.blueActionOptimalFrequencies, m.blueActionParticipantNormativeFrequencies,...
                    m.blueActionParticipantNormativeOptimalFrequencies,...
                    mission, saveData, showPlots, dataFolder);
            end
        end
        i = i + 1;
    end
end

%% Create detailed time data plots
if plotDetailedTime
    dataFolder = strcat(figureOutputFolder, '\timing_figures');    
    if saveData && ~isdir(dataFolder)
        mkdir(dataFolder);
    end
    plot_detailed_time_metrics(timeDetails.subjects, timeDetails.examTrainingTimes,...
        timeDetails.examTimes, timeDetails.missionTimes, timeDetails.missionTrainingTimes,...
        saveData, showPlots, dataFolder);
end

%% Create total time plot (shows time taken on all missions)
if plotTotalTime
    %Create folder to store figure if it doesn't exist
    dataFolder = strcat(figureOutputFolder, '\timing_figures');
    if saveData && ~isdir(dataFolder)
        mkdir(dataFolder);
    end
    plot_time_metrics(timeData, subjects, missions, saveData, showPlots, dataFolder);
end

end