% collect_results

loadtestdesign;
loadsubjecttabledesign;

blocks = [trainingtestblocks testblocks];

%% metrics

zerofactor = 0.0001; 

% results
normativefilename = 'results/normative_solution_PILOT.csv';
datadirectory = '../../Pilot Study 1/data/';
csvresultsfilename = 'results/allscores.csv';
subjectbasefilename = 'results/scores_%d';
run('data/assessment_normative_data');

if ~exist('solutiontable','var')
    solutiontable = csv2cell(normativefilename,'fromfile');
    solutiontable = solutiontable(2:end,:); % strip headings
    convertstringsidx = [1:2 6:size(solutiontable,2)]; % take all numeric fields
    solutiontable(:,convertstringsidx) = num2cell(str2double(solutiontable(:,convertstringsidx)));
end

%%
[subjectnums, subjects, initials, numsubjects] = read_subjects_directory(datadirectory);

numtrials = size(solutiontable,1);
[normativeprobs randomprobs] = zerodata(numtrials,4);
humanprobs = zeros(numsubjects,numtrials,4);
humanlayerprobs = zeros(numsubjects,numtrials,3,4);

basedivergenceidx = WTAnh_idx;
numdivergences = RMSnr_idx-basedivergenceidx+1;

divergencetable = zeros(numdivergences,numsubjects,numtrials);
userchoicetable = zeros(numsubjects,numtrials);
[firstWTA, secondWTA, thirdWTA] = zerodata(numsubjects,numtrials);

writeCSVheadings(csvresultsfilename,subject_data_headings);

for s = 1:numsubjects
    subjectdatatable = cell(numtrials,enddata);
    subjectdatatable(:) = {0}; % MSC NaN is better
    subjectdatatable(:,Subject_number_idx) = num2cell(subjectnums(s));

    % copy data from (normative) solution table to subject table
    subjectdatatable(:,...
    [Phase_number_idx, Trial_number_idx, Query_type_idx, Layer_type_idx, Specified_answer_idx, ...
        Presentation_order_idx, Number_probability_sets_idx, Number_facilities_idx, Max_probs_in_set_idx ...
        normdataoffset+1:normdataoffset+max_probsets*max_probs_in_set]) = ...
            solutiontable;
    
    %% Process all phases for each subject (phases are stored as separate files) 
    trialidx = 0;
    for b = 1:length(blocks)
        xmlfilename = sprintf('%s%s/S%d_Pilot1_Phase%d.xml',datadirectory,subjects{s},subjectnums(s),blocks(b));
        [phaseprobs, phasequerytypes, phasetrialtimes, phasetrialscores, phasetrialchoices] = ...
            parseXMLdatafile(xmlfilename,max_probs_in_set,'Probability');
        rawphaseprobs = parseXMLdatafile(xmlfilename,max_probs_in_set,'RawProbability');

        [numnewtrials, numnewprobs] = size(phaseprobs);
        blocktrials = (trialidx+(1:numnewtrials));

        num_facilities = subjectdatatable{blocktrials(1),Number_facilities_idx};
        num_probsets = subjectdatatable{blocktrials(1),Number_probability_sets_idx};
        probsmask = getprobsmask(num_probsets, max_probsets, num_facilities, max_probs_in_set);
                        
        subjectdatatable(blocktrials,humandataoffset  + find(probsmask)) = num2cell(phaseprobs);
        subjectdatatable(blocktrials,rawdataoffset    + find(probsmask)) = num2cell(rawphaseprobs);
        subjectdatatable(blocktrials,randomdataoffset + find(probsmask)) = num2cell(1/num_facilities);

        subjectdatatable(blocktrials,Query_type_idx) = phasequerytypes;
        subjectdatatable(blocktrials,Trial_time_idx) = num2cell(phasetrialtimes);
        subjectdatatable(blocktrials,Trial_score_idx) = num2cell(phasetrialscores);
        subjectdatatable(blocktrials,User_choice_idx) = num2cell(phasetrialchoices);
        subjectdatatable(blocktrials,Phase_type_idx) = num2cell(phasetype(blocks(b)));
        trialidx = trialidx + numnewtrials;
    end

    %% Trial-by-trial processing; compute distances and scores
    for i = 1:numtrials
        layertype = subjectdatatable{i,Layer_type_idx};
        num_facilities = subjectdatatable{i,Number_facilities_idx};

        if strcmp(layertype,'Simultaneous')
            numlayers = 1;
            normlayeroffset = 0;
            humanlayeroffset = 0;
        elseif strcmp(layertype,'Sequential')
            % for sequential trials, take final answer
            numlayers = subjectdatatable{i,Number_probability_sets_idx};
            normlayeroffset = (numlayers-1)*max_probs_in_set;
            humanlayeroffset = (numlayers-1)*max_probs_in_set;
        elseif strcmp(layertype,'User Choice')
            numlayers = 2;
            layerchoice = subjectdatatable{i,User_choice_idx};
            normlayeroffset = (layerchoice-1)*max_probs_in_set; % S (layerchoice 2, offset 4) or M (layerchoice 3, offset 8) 
            humanlayeroffset = max_probs_in_set; % take final answer; offset 4
        else assert('Unknown layer'); 
        end

        normativeprobs(i,:) = [subjectdatatable{i,normdataoffset + normlayeroffset + (1:max_probs_in_set)}];
        randomprobs(i,:) =    [subjectdatatable{i,randomdataoffset + normlayeroffset + (1:max_probs_in_set)}];
        humanprobs(s,i,:) =   [subjectdatatable{i,humandataoffset + humanlayeroffset + (1:max_probs_in_set)}];
        for l = 1:numlayers
            humanlayerprobs(s,i,l,:) = [subjectdatatable{i,humandataoffset + (l-1)*max_probs_in_set + (1:max_probs_in_set)}];
        end
        
		np = normativeprobs(i,1:num_facilities);
        hp = squeeze(humanprobs(s,i,1:num_facilities))';
        rp = randomprobs(i,1:num_facilities);
        rawp = [subjectdatatable{i,rawdataoffset + humanlayeroffset + (1:num_facilities)}];
        assert(~all(hp == 0)); assert(isequal(size(hp),size(rawp)));

        %% Single-subject data
        subjectdatatable{i,Norm_comp_idx} = sum(abs(hp - rawp));
        
        subjectdatatable{i,WTAnh_idx} = feval(@Winnertakealldivergence,np,hp,zerofactor);
        subjectdatatable{i,WTAnr_idx} = 0;
        subjectdatatable{i,TVDnh_idx} = feval(@Totalvariationdistance,np,hp,zerofactor);
        subjectdatatable{i,TVDnr_idx} = feval(@Totalvariationdistance,np,rp,zerofactor);
        subjectdatatable{i,KLDSnh_idx} = feval(@KLdivergenceSymmetric1,np,hp,zerofactor);
        subjectdatatable{i,KLDSnr_idx} = feval(@KLdivergenceSymmetric1,np,rp,zerofactor);
        subjectdatatable{i,KLDnh_idx} = feval(@KLdivergence,np,hp,zerofactor);
        subjectdatatable{i,KLDnr_idx} = feval(@KLdivergence,np,rp,zerofactor);
        subjectdatatable{i,RMSnh_idx} = feval(@RMSdistance,np,hp,zerofactor);
        subjectdatatable{i,RMSnr_idx} = feval(@RMSdistance,np,rp,zerofactor);

        %% Cross-subject data
        divergencetable(:,s,i) = [subjectdatatable{i,WTAnh_idx:RMSnr_idx}];
        userchoicetable(s,i) = [subjectdatatable{i,User_choice_idx}];

        %% Analyses of secondary probability choices (bins 2, 3, 4)
        % Secondary WTA conditioned on primary. 1 is correct, 0 incorrect
        firstWTA(s,i) = feval(@Winnertakealldivergence,np,hp,zerofactor) == 0;
        if firstWTA(s,i)
            [~, nporder] = sort(np(1:num_facilities),'descend');
            secondWTA(s,i) = feval(@Winnertakealldivergence,np(nporder(2:end)),hp(nporder(2:end)),zerofactor) == 0;
            
            if secondWTA(s,i)
                thirdWTA(s,i) = feval(@Winnertakealldivergence,np(nporder(3:end)),hp(nporder(3:end)),zerofactor) == 0;
            end    
        end
    end

    %% write single subject data file
    csvsubjectfilename = sprintf([subjectbasefilename '.csv'],subjectnums(s));
    writeCSVheadings(csvsubjectfilename,subject_data_headings);
    cell2csv(csvsubjectfilename,subjectdatatable,',',[],'.',1);
    % save(sprintf(subjectbasefilename,subjectnums(s)),'subjectdatatable'); % save Matlab data file
    
    %% write all subjects data file
    cell2csv(csvresultsfilename,subjectdatatable,',',[],'.',1);
end

%%
for s = 1:numsubjects
    % Assess 1
    xmlfilename = sprintf('%s%s/S%d_Pilot1_Phase%d.xml',datadirectory,subjects{s},subjectnums(s),assessment1block);
    [assessment1_human(s,:,:), trialtypes, trialtimes] = parseXMLdatafile(xmlfilename,max_probs_in_set,'Probability');

    % Assess 2
    xmlfilename = sprintf('%s%s/S%d_Pilot1_Phase%d.xml',datadirectory,subjects{s},subjectnums(s),assessment2block);
    [assessment2_human(s,:,:), trialtypes, trialtimes] = parseXMLdatafile(xmlfilename,max_probs_in_set,'RawProbability');
end

%%
%         % Peak-only analysis
%         % make peak-only probability set [x, peakval, x, x] where xs are equal
%         % to test hypothesis that subject is only judging top ranked choice
% 		[maxhp, maxhpidx] = max(hp);
%         num_facilities = subjectdatatable{i,Number_facilities_idx};
% 		randomhpidx = setdiff(1:num_facilities,maxhpidx);
%         peakonlyp = zeros(1,max_probs_in_set);
%         peakonlyp(maxhpidx) = maxhp;
%         peakonlyp(randomhpidx) = (1 - maxhp)/length(randomhpidx);
%         peakonlytable(s,i) = feval(@Totalvariationdistance,np,peakonlyp,zerofactor);
%         
%         % Sequential WTA analysis
%         % determine P(normative bin #X is subject bin #1...#X)
%         % if 1 is correct, chance level for bin #X is X/N
%         % we ignore ties in subject responses, etc.
%         for bin = 1:num_facilities
%             hresponse = hporder(bin);
%             nresponses = nporder(1:bin);
%             sequentialWTAtable(bin,s,i) = ismember(hresponse,nresponses);
%         end
        
%         % Friedman test on ranks
%         friedp(s,i) = friedman([np(1:num_facilities) ; hp(1:num_facilities)],1,'off');