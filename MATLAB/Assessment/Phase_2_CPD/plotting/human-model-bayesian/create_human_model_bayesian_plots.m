function create_human_model_bayesian_plots(missions,...
    plotRedTactics, plotAttackProbs, plotAttackProbsNegentropy,...
    plotSigintChoices, plotBlueActions, subjectDataInputFolder,...
    modelDataInputFolder, siteName, modelName, figureOutputFolder, saveData, showPlots)
%CREATE_HUMAN_MODEL_BAYESIAN_PLOTS Create plots comparing human responses,
%model responses, and normative (Bayesian) responses

if ~exist('saveData', 'var')
    saveData = false;
end
if ~exist('showPlots', 'var')
    showPlots = true;
end
if ~exist('subjectDataInputFolder', 'var') || isempty(subjectDataInputFolder)
    subjectDataInputFolder = '';
end
if ~exist('modelDataInputFolder', 'var') || isempty(modelDataInputFolder)
    modelDataInputFolder = '';
end
if ~exist('figureOutputFolder', 'var') || isempty(figureOutputFolder)
    figureOutputFolder = '';
end

%% Aggregate human subject data and model data for missions
modelDisplayName = [siteName ' Model'];
subjectMissionData = cell(length(missions),1);
subjectMissionMetrics = cell(length(missions),1);
modelMissionData = cell(length(missions),1);
modelMissionMetrics = cell(length(missions),1);
subjectChatterAvg = [];
subjectSilentAvg = [];
%modelChatterAvg = [];
%modelSilentAvg = [];
i = 1;
for mission = missions
    %Get subject data and compute metrics
    [d.subjects, d.intData, d.actualRedTactics, d.mostLikelyRedTacticsData, d.redTacticProbsData,...
        d.batchPlotData, d.bluebookData, d.attackProbStages, d.subjectAttackProbs,...
        d.normativeAttackProbs, d.sigintSelectionData, d.blueActionSelectionData, d.timeDataByTrial] = ...
        aggregate_csv_mission_data(strcat(subjectDataInputFolder,'\Aggregated Data\allresponses_mission_',...
        num2str(mission), '.csv'), mission);
    subjectMissionData{i} = d;
    subjectMissionMetrics{i} = ...
        compute_mission_metrics(d.intData, d.mostLikelyRedTacticsData,...
        d.redTacticProbsData,...
        d.batchPlotData,d.sigintSelectionData,...
        d.subjectAttackProbs, d.normativeAttackProbs, d.attackProbStages,...
        d.blueActionSelectionData);
    if ~isempty(subjectMissionMetrics{i}.chatterAvg)
        subjectChatterAvg = subjectMissionMetrics{i}.chatterAvg;
        subjectSilentAvg = subjectMissionMetrics{i}.silentAvg;
    end
    if i == 1
        subjects = d.subjects;
    end
    
    %Get model data and compute metrics    
    [d.subjects, d.intData, d.actualRedTactics, d.mostLikelyRedTacticsData, d.redTacticProbsData,...
        d.batchPlotData, d.bluebookData, d.attackProbStages, d.subjectAttackProbs,...
        d.normativeAttackProbs, d.sigintSelectionData, d.blueActionSelectionData, d.timeDataByTrial] = ...
        aggregate_csv_mission_data(strcat(modelDataInputFolder, '\', siteName, '\',...
        siteName, '_', modelName, '_mission_', num2str(mission), '.csv'), mission);
    modelMissionData{i} = d;
    modelMissionMetrics{i} = ...
        compute_mission_metrics(d.intData, d.mostLikelyRedTacticsData,...
        d.redTacticProbsData,...
        d.batchPlotData,d.sigintSelectionData,...
        d.subjectAttackProbs, d.normativeAttackProbs, d.attackProbStages,...
        d.blueActionSelectionData);
    %if ~isempty(modelMissionMetrics{i}.chatterAvg)
        %modelChatterAvg = modelMissionMetrics{i}.chatterAvg;
        %modelSilentAvg = modelMissionMetrics{i}.silentAvg;
    %end    
    i = i + 1;
end

%% Create most likely Red tactic plots (Mission 2), Red tactic probability plots
%  (Missions 4-5), and batch plot plots (Missions 4-5)
if plotRedTactics
    dataFolder = strcat(figureOutputFolder, '\red_tactics_figures_hmb');
    if saveData && ~isdir(dataFolder)
        mkdir(dataFolder);
    end
    i = 1;
    for mission = missions
        sd = subjectMissionData{i};
        sm = subjectMissionMetrics{i};
        mm = modelMissionMetrics{i};
        if ~isempty(sm.mostLikelyTacticSelections)                        
            plot_most_likely_red_tactics_hmb(subjects, sm.mostLikelyTacticSelections,...
                mm.mostLikelyTacticSelections, sd.mostLikelyRedTacticsData.tacticTypes,...
                modelDisplayName, mission, saveData, showPlots, dataFolder);
        elseif ~isempty(sd.redTacticProbsData)
            plot_red_tactic_probs_hmb(sd.subjects, sd.redTacticProbsData.tacticProbs,...
                sm.redTacticProbsAvg, sm.redTacticNe, sm.redTacticNeAvg,...
                mm.redTacticProbsAvg, mm.redTacticNeAvg,...
                sm.normativeRedTacticProbsAvg, sm.normativeRedTacticNeAvg,...
                sd.actualRedTactics, sd.redTacticProbsData.tacticTypes,...
                'Bayesian (Assuming No Red Change)', modelDisplayName,...
                mission, saveData, showPlots, dataFolder);
        end        
        if ~isempty(sm.batchPlotCreationTrials)
            plot_batch_plots_hmb(sd.subjects, sm.batchPlotSearchDepthsAllTrials,...
                mm.batchPlotSearchDepthsAllTrials, modelDisplayName,...
                mission, saveData, showPlots, dataFolder);            
        end
        i = i + 1;
    end
end

%% Create attack probability plots (Missions 1-2, 4-5), "just picked one"
%  plots (Missions 2, 4, 5), and "no change after SIGINT" plot (Mission 3)
if plotAttackProbs
    dataFolder = strcat(figureOutputFolder, '\probability_figures_hmb');
    if saveData && ~isdir(dataFolder)
        mkdir(dataFolder);
    end
    i = 1;
    for mission = missions        
        sd = subjectMissionData{i};
        sm = subjectMissionMetrics{i};        
        mm = modelMissionMetrics{i};       
        normativeStrategyName = 'Bayesian';
        if mission == 4 || mission == 5
            normativeStrategyName = 'Bayesian (Assuming No Red Change)';
        end
        plot_attack_probs_hmb(sd.subjects, sd.intData, sd.bluebookData,...
            sm.subjectProbsByStage, sm.normativeProbsByStage, mm.subjectProbsByStage,...
            sm.subjectProbsAvgByStage, sm.normativeProbsAvgByStage,...
            sm.subjectPtProbs, sm.normativePtProbs, mm.subjectPtProbs,...
            sm.subjectPtProbsAvg, sm.normativePtProbsAvg,...
            subjectChatterAvg, subjectSilentAvg,...
            sd.attackProbStages, sd.actualRedTactics, normativeStrategyName,...
            modelDisplayName, mission, saveData, showPlots, dataFolder);       
        i = i + 1;
    end
end

%% Create attack probability Negentropy plots (Missions 1-5)
if plotAttackProbsNegentropy
    %Create folder to store figure if it doesn't exist
    dataFolder = strcat(figureOutputFolder, '\negentropy_figures_hmb');
    if saveData && ~isdir(dataFolder)
        mkdir(dataFolder);
    end
    i = 1;
    for mission = missions       
        sd = subjectMissionData{i};
        sm = subjectMissionMetrics{i};        
        mm = modelMissionMetrics{i};       
        normativeStrategyName = 'Bayesian';
        if mission == 4 || mission == 5
            normativeStrategyName = 'Bayesian (Assuming No Red Change)';
        end
        if mission == 1 || mission == 2 || mission == 3 || mission == 4 || mission == 5;
            %Plot negentropy (Missions 1 - 5)           
            plot_negentropy_hmb(sd.subjects, sm.subjectNe, sm.normativeNe, mm.subjectNeAvg,...
                sm.subjectNeAvg, sm.normativeNeAvg, sd.attackProbStages, normativeStrategyName,...
                modelDisplayName, mission, saveData, showPlots, dataFolder);
            
            %Plot delta negentropy (Missions 1 - 3)
            pTpcStage = find(strcmp('Ptpc', sd.attackProbStages));
            if ~isempty(pTpcStage)
                ptStage = find(strcmp('Pt', sd.attackProbStages));
                if ~isempty(ptStage)
                    prevStage = ptStage - 1;
                else
                    prevStage = pTpcStage - 1;
                end
                if prevStage > 0 && prevStage < pTpcStage
                    plot_delta_negentropy_hmb(sd.subjects, sm.subjectNeAvg,...
                        sm.normativeNeAvg, mm.subjectNeAvg, sm.subjectDeltaNe,...
                        sm.normativeDeltaNe, prevStage, pTpcStage, modelDisplayName,...
                        mission, saveData, showPlots, dataFolder);
                end
            end
        end
        i = i + 1;
    end
end

%% Create confirmation bias plots based on SIGINT location selections (Mission 3 only)
if plotSigintChoices
    %Create folder to store figure if it doesn't exist
    dataFolder = strcat(figureOutputFolder, '\sigint_selection_figures_hmb');
    if saveData && ~isdir(dataFolder)
        mkdir(dataFolder);
    end
    i = 1;
    for mission = missions
        sd = subjectMissionData{i};        
        if ~isempty(sd.sigintSelectionData)
            sm = subjectMissionMetrics{i};
            mm = modelMissionMetrics{i};
            plot_sigint_choices_hmb(sd.subjects, sm.sigintSelectionFrequencies,...
                sm.sigintSelectionParticipantNormativeFrequencies,...
                sm.sigintSelectionParticipantNormativeOptimalFrequencies,...
                mm.sigintSelectionFrequencies,...
                mm.sigintSelectionParticipantNormativeFrequencies,...
                mm.sigintSelectionParticipantNormativeOptimalFrequencies,...
                modelDisplayName, mission, saveData, showPlots, dataFolder);
        end
        i = i + 1;
    end
end

%% Create Blue action selection plots (Missions 2-5)
if plotBlueActions
    %Create folder to store figure if it doesn't exist
    dataFolder = strcat(figureOutputFolder, '\blue_action_choices_figures_hmb');
    if saveData && ~isdir(dataFolder)
        mkdir(dataFolder);
    end
    i = 1;
    for mission = missions
        if mission ~= 1
            sm = subjectMissionMetrics{i};
            if ~isempty(sm.blueActionFrequencies)
                sd = subjectMissionData{i};
                mm = modelMissionMetrics{i};
                plot_blue_actions_hmb(sd.subjects, sm.notDivertFrequency,...
                    sm.blueActionFrequencies, sm.blueActionParticipantNormativeFrequencies,...
                    sm.blueActionParticipantNormativeOptimalFrequencies,...
                    mm.blueActionFrequencies, mm.blueActionParticipantNormativeFrequencies,...
                    mm.blueActionParticipantNormativeOptimalFrequencies,...
                    modelDisplayName, mission, saveData, showPlots, dataFolder);
            end
        end
        i = i + 1;
    end
end

end