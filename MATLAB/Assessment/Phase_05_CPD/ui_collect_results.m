% ui_collect_results

max_probsets = 3;
max_probs_in_set = 4;
ui_loadsubjecttabledesign;
UI_parameters;

zerofactor = 0.0001; 

datadirectory = '../../UI Study/data/';
studybasefilename = 'studies/UIStudy';
subjectbasefilename = 'results/cache/responses_subject_%d_task_%d_block_%d.csv';
alldatafilename = 'results/cache/allresponses.mat';
alldata = {};

[subjectnums, subjects, initials, numsubjects] = read_subjects_directory(datadirectory);

%%
etimer = elapsedtimer([],taskparams.numtasks);
for task = 1:taskparams.numtasks
    tasktype = gettasktype(taskdef(task).trialtype);
    for exp_gui = 1:taskparams.numguis(task);
        allsubjectsdatatable = [];

        for sidx = 1:numsubjects
            xmlfilename = sprintf('%s%s/S%s_UI_Task %d.%d.xml',datadirectory,subjects{sidx},sprintf('%0.3d',subjectnums(sidx)),task,exp_gui);
            if ~exist(xmlfilename,'file')
                continue;
            end
            
            [humannormalizedprobs, humanrawprobs, normativeprobs, num_probsets, numbins, ...
                trialtimetable, modality, widget] = ...
                ui_parseXMLdatafile(xmlfilename,tasktype,max_probs_in_set,max_probsets);

            % undo counterbalancing
            studymatfilename = sprintf('%s_%03d.mat',studybasefilename,subjectnums(sidx));
            assert(exist(studymatfilename,'file') == 2,'No study design file');
            load(studymatfilename,'counterbalanceorder','defaultsorder');
            cborder = counterbalanceorder{task}(subjectnums(sidx),:);
            design_gui = cborder(exp_gui);
            
            %% header
            numtrials = size(humannormalizedprobs,1);
            subjectdatatable = cell(numtrials,enddata);
            subjectdatatable(:) = {NaN};
            subjectdatatable(:,Subject_number_idx) = num2cell(subjectnums(sidx));
            subjectdatatable(:,Phase_number_idx) = num2cell(task);
            subjectdatatable(:,Experiment_block_number_idx) = num2cell(exp_gui);
            subjectdatatable(:,Design_block_number_idx) = num2cell(design_gui);
            subjectdatatable(:,Modality_idx) = {modality};
            subjectdatatable(:,Widget_idx) = {widget};
            % subjectdatatable(:,Normalization_idx) % MSC
            subjectdatatable(:,Trial_number_idx) = num2cell(1:numtrials)';
            subjectdatatable(:,Trial_time_idx) = num2cell(trialtimetable);

            if ~isempty(taskdef(task).defaults)
                subjectdatatable(:,Default_idx) = num2cell(taskdef(task).defaults(defaultsorder{task}));
            else
                subjectdatatable(:,Default_idx) = {0}; % MSC default always zero unless set
            end
            
            assert(strcmp(taskdef(task).widget{design_gui},widget),'Counterbalancing order wrong?');
            assert(strcmp(taskdef(task).modality{design_gui},modality),'Counterbalancing order wrong?');
            % MSC check normalization if possbible?
            
            subjectdatatable(:,Number_probability_sets_idx) = num2cell(num_probsets);
            subjectdatatable(:,Number_bins_idx) = num2cell(numbins);
            subjectdatatable(:,Max_probs_in_set_idx) = num2cell(max_probs_in_set);

            %% probs
            probsmask = getprobsmask(num_probsets, max_probsets, numbins, max_probs_in_set);
            subjectdatatable(:,humanprobsoffset    + find(probsmask)) = num2cell(humannormalizedprobs(:,find(probsmask))); %#ok
            subjectdatatable(:,rawhumanprobsoffset + find(probsmask)) = num2cell(humanrawprobs(:,find(probsmask))); %#ok
            subjectdatatable(:,normativeprobsoffset + find(probsmask)) = num2cell(normativeprobs(:,find(probsmask))); %#ok

            for i = 1:numtrials
                for la = 1:num_probsets
                    np =       normativeprobs(i,numbins*(la-1) + (1:numbins));
                    rp =        humanrawprobs(i,numbins*(la-1) + (1:numbins));
                    hp = humannormalizedprobs(i,numbins*(la-1) + (1:numbins));

                    % clip hp to deal with bad normalization that can produce -0.01 values
                    % in the StackedBars widget
                    hp = max(0,min(1,hp));
                    
                    % MSC for deviation, tasks 3-5 we want to use normalized?
                    subjectdatatable{i,DEV_idx + (la-1)} = feval(@Deviation,np,rp,zerofactor);

                    % for abs, use normalized
                    subjectdatatable{i,ABS_idx + (la-1)} = feval(@ABSdistance,np,hp,zerofactor);
                end
            end
            
            %% store data
            alldata{subjectnums(sidx),task,design_gui} = subjectdatatable; %#ok

            %% write single subject - single task data file
            subjecttaskfilename = sprintf(subjectbasefilename,subjectnums(sidx),task,exp_gui);
            writeCSVheadings(subjecttaskfilename,subject_data_headings);
            cell2csv(subjecttaskfilename,subjectdatatable,',',[],'.',1);
        end
    end
    steptimer(etimer);
end

save(alldatafilename,'alldata'); % save Matlab data file
