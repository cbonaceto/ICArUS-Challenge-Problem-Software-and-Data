% collect_results_pilot2
%
% MSC not yet parsing task 7

max_probsets = 5;
max_probs_in_set = 4;
numtasks = 6; %6
loadsubjecttabledesign_pilot2;

% results
datadirectory = '../../Pilot Study 2/data/';
subjectbasefilename = 'results/responses_subject_%d_task_%d.csv';
allsubjectsbasefilename = 'results/allresponses_task_%d.csv';
alldatafilename = 'results/allresponses.mat';
alldata = {};

%%
%dir('.')
d = dir([datadirectory 'S*']);
subjects = {d.name};
numsubjects = length(subjects);
assert(numsubjects > 0);
subjectnums = zeros(1,numsubjects);
initials = cell(1,numsubjects);
for s = 1:numsubjects
    [first rest] = strtok(subjects{s},'_');
    subjectnums(s) = str2double(first(2:end));
    initials{s} = rest(2:end);
end

%%
for task = 1:numtasks
    
    allsubjectsdatatable = [];
    
    for s = 1:numsubjects
        xmlfilename = sprintf('%s%s/S%s_Pilot Exam_Task%d.xml',datadirectory,subjects{s},sprintf('%0.3d',subjectnums(s)),task);
        [humannormalizedprobs, humanrawprobs, normativeprobs, normalizedallocation, rawallocation, circlestable, num_probsets, numgroups, ...
            forcedchoicetable, surprisetable, ...
            layertypes, surprisereport, surprisereporttime, ...
            trialtimetable, grouptimetable, surprisetimetable, allocationtimetable, ...
            Probabilities_score_table, Troop_allocation_score_table] = ...
                parseXMLdatafile_pilot2(xmlfilename,task,max_probs_in_set,max_probsets);

        %% header
        numtrials = size(humannormalizedprobs,1);
        subjectdatatable = cell(numtrials,enddata);
        subjectdatatable(:) = {NaN};
        subjectdatatable(:,Subject_number_idx) = num2cell(subjectnums(s));
        subjectdatatable(:,Phase_number_idx) = num2cell(task);
        subjectdatatable(:,Trial_number_idx) = num2cell(1:numtrials)';
        subjectdatatable(:,Number_probability_sets_idx) = num2cell(num_probsets);
        subjectdatatable(:,Number_groups_idx) = num2cell(numgroups);
        subjectdatatable(:,Max_probs_in_set_idx) = num2cell(max_probs_in_set);
        
        %% probs
        probsmask = getprobsmask(num_probsets, max_probsets, numgroups, max_probs_in_set);
        subjectdatatable(:,humanprobsoffset    + find(probsmask)) = num2cell(humannormalizedprobs(:,find(probsmask))); %#ok
        subjectdatatable(:,rawhumanprobsoffset + find(probsmask)) = num2cell(humanrawprobs(:,find(probsmask))); %#ok
        subjectdatatable(:,normativeprobsoffset + find(probsmask)) = num2cell(normativeprobs(:,find(probsmask))); %#ok

        %% allocation
        allocationmask = getprobsmask(1, 1, numgroups, max_probs_in_set);
        subjectdatatable(:,normallocationoffset + find(allocationmask)) = num2cell(normalizedallocation(:,find(allocationmask))); %#ok
        subjectdatatable(:,rawallocationoffset + find(allocationmask)) = num2cell(rawallocation(:,find(allocationmask))); %#ok

        if task == 2 || task == 3
            subjectdatatable(:,circledataoffset + (1:3*max_probs_in_set)) = num2cell(circlestable);
        end

        if task == 4 || task == 5 || task == 6
            % per-layer
            subjectdatatable(:,layer_type_offset + (1:num_probsets-1)) = layertypes(:,1:num_probsets-1);
            
            % per-trial
            subjectdatatable(:,Allocation_time_idx) = num2cell(allocationtimetable);
            subjectdatatable(:,Probabilities_score_idx) = num2cell(Probabilities_score_table);
            subjectdatatable(:,Troop_allocation_score_idx) = num2cell(Troop_allocation_score_table);
        end
        
        if task == 5 || task == 6
            % per-layer
            subjectdatatable(:,layer_surprise_offset + (1:num_probsets-1)) = num2cell(surprisereport(:,1:num_probsets-1));
            subjectdatatable(:,layer_surprise_time_offset + (1:num_probsets-1)) = num2cell(surprisereporttime(:,1:num_probsets-1));
        end
        
        %% per-trial
        subjectdatatable(:,Surprise_idx) = num2cell(surprisetable);
        subjectdatatable(:,Trial_time_idx) = num2cell(trialtimetable);
        subjectdatatable(:,Group_time_idx) = num2cell(grouptimetable);
        subjectdatatable(:,Surprise_time_idx) = num2cell(surprisetimetable);
        
        %% store data
        alldata{s,task} = subjectdatatable;
        allsubjectsdatatable = [allsubjectsdatatable ; subjectdatatable];
        
        %% write single subject - single task data file
        subjecttaskfilename = sprintf(subjectbasefilename,subjectnums(s),task);
        writeCSVheadings(subjecttaskfilename,subject_data_headings);
        cell2csv(subjecttaskfilename,subjectdatatable,',',[],'.',1);
    end

    %% for each task, write all subjects data file
    allsubjectsfilename = sprintf(allsubjectsbasefilename,task);
    writeCSVheadings(allsubjectsfilename,subject_data_headings);
    cell2csv(allsubjectsfilename,allsubjectsdatatable,',',[],'.',1);
end

save(alldatafilename,'alldata'); % save Matlab data file
