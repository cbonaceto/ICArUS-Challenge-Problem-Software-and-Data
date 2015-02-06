%Script to create the file with individual difference metrics for each
%subject

dataInputFolder = 'data\Final-Exam-1\Human_Data';
missions = [1 2 3 4 5];
%missions = 1;
missionData = cell(length(missions),1);
i = 1;
for mission = missions
    [d.subjects, d.intData, d.actualRedTactics, d.mostLikelyRedTacticsData, d.redTacticProbsData,...
        d.batchPlotData, d.bluebookData, d.attackProbStages, d.subjectAttackProbs,...
        d.normativeAttackProbs, d.sigintSelectionData, d.blueActionSelectionData, d.timeDataByTrial] = ...
        aggregate_csv_mission_data(strcat(dataInputFolder,'\Aggregated Data\allresponses_mission_', num2str(mission), '.csv'), mission);
    missionData{i} = d;
    if i == 1
        subjects = d.subjects;
    end
    i = i + 1;
end

[inferencingScore, decisionMakingScore, foragingScore] = ...
    compute_individual_difference_metrics(subjects, missionData,...
    [1, 2, 3], [2, 4, 5], 3);

output_individual_difference_metrics(subjects, inferencingScore,...
    decisionMakingScore, foragingScore, '.');