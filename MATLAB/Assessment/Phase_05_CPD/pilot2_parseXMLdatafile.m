function [humannormalizedprobs, humanrawprobs, normativeprobs, normalizedallocation, rawallocation, circlestable, num_probsets, numgroups, ...
    forcedchoicetable, surprisetable, ...
    layertypes, surprisereport, surprisereporttime, ...
    trialtimetable, grouptimetable, surprisetimetable, allocationtimetable, ...
    Probabilities_score_table, Troop_allocation_score_table] = ...
        pilot2_parseXMLdatafile(xmlfilename,task,max_probs_in_set,max_probsets)

x1 = xml2struct(xmlfilename);
x1 = x1.(sprintf('ns3_Task_%d_Phase',task));

if task == 1 || task == 2 || task == 3
    numtrials = length(x1.TrialBlock);
elseif task == 4 || task == 5 || task == 6
    numtrials = length(x1.Trial);
end

%% initialize tables

% probs etc.
[humannormalizedprobs, humanrawprobs, normativeprobs] = zerodata(numtrials,max_probs_in_set*max_probsets);
[normalizedallocation, rawallocation] = zerodata(numtrials,max_probs_in_set);
circlestable = zeros(numtrials,3*max_probs_in_set);

% per-trial
[surprisetable, forcedchoicetable] = zerodata(numtrials,1);
[trialtimetable, grouptimetable, surprisetimetable, allocationtimetable] = zerodata(numtrials,1);
[Probabilities_score_table, Troop_allocation_score_table] = zerodata(numtrials,1);

% per-layer
layertypes = cell(numtrials,max_probsets);
[surprisereport, probstimetable, surprisereporttime] = zerodata(numtrials,max_probsets);

%% parse
for b = 1:numtrials
    if task == 1 || task == 2 || task == 3
        trial = str2double(x1.TrialBlock{b}.Attributes.trialBlockNum);
        tr = x1.TrialBlock{b}.ProbeTrial.TrialResponse;
    elseif task == 4 || task == 5 || task == 6
        trial = str2double(x1.Trial{b}.Attributes.trialNum);
        tr = x1.Trial{b}.TrialResponse;
    end
    assert(b == trial);
    
    %% Parse group circles in task 2, and centers in task 3
    tempcircles = NaN(3,max_probs_in_set);
    if task == 2 || task == 3
        if task == 2, gc = tr.GroupCirclesResponse.GroupCircle; end
        if task == 3, gc = tr.GroupCentersResponse.GroupCenter; end
        numgroups = length(gc);
        for group = 1:numgroups
            tempcircles(1,group) = str2double(gc{group}.CenterLocation.Attributes.x);
            tempcircles(2,group) = str2double(gc{group}.CenterLocation.Attributes.y);
            if task == 2, tempcircles(3,group) = str2double(gc{group}.Attributes.radius); end
        end
    end
    circlestable(trial,1:3*max_probs_in_set) = reshape(tempcircles',[1 3*max_probs_in_set]);

    if task == 1 || task == 2 || task == 3
        gap = tr.GroupResponse.GroupAttackProbability;
        numgroups = length(gap);
        num_probsets = 1;

        [humannormalizedprobs(trial,1:numgroups),humanrawprobs(trial,1:numgroups)] = readprobs(gap);
        for group = 1:numgroups
            humannormalizedprobs(trial,group) = str2double(gap{group}.Attributes.probability);
            humanrawprobs(trial,group) = str2double(gap{group}.Attributes.rawProbability);
        end
        normativeprobs(trial,1:numgroups) = cumulative(tr.GroupResponse);
        grouptimetable(trial) = str2double(tr.GroupResponse.Attributes.trialPartTime_ms);
        
    elseif task == 4 || task == 5 || task == 6
        if task == 4
            gap = tr.LocationResponse.GroupAttackProbability;
            numgroups = length(gap);
            numlayers = length(tr.LocationResponse_afterINT);
            num_probsets = numlayers + 1;

            [humannormalizedprobs(trial,1:numgroups),humanrawprobs(trial,1:numgroups)] = readprobs(gap);
            normativeprobs(trial,1:numgroups) = cumulative(tr.LocationResponse); 
            probstimetable(trial,1) = str2double(tr.LocationResponse.Attributes.trialPartTime_ms);
            
            layer = 1;
            trl = tr.LocationResponse_afterINT;
            [humannormalizedprobs(trial,layer*numgroups+(1:numgroups)),humanrawprobs(trial,layer*numgroups+(1:numgroups))] = ...
                readprobs(trl.LocationResponse.GroupAttackProbability);
            normativeprobs(trial,layer*numgroups+(1:numgroups)) = cumulative(trl.LocationResponse);
                
            probstimetable(trial,layer+1) = str2double(trl.LocationResponse.Attributes.trialPartTime_ms);
            layertypes{trial,layer} = trl.INTLayerShown.LayerType.Attributes.xsi_type;

            forcedchoicetable(trial) = tr.LocationResponse.Attributes.Group;
            grouptimetable(trial) = str2double(tr.LocationResponse.Attributes.trialPartTime_ms);
        elseif task == 5 || task == 6
            gap = tr.GroupResponse.GroupAttackProbability;
            numgroups = length(gap);
            numlayers = length(tr.GroupResponse_afterINT);
            num_probsets = numlayers + 1;

            [humannormalizedprobs(trial,1:numgroups),humanrawprobs(trial,1:numgroups)] = readprobs(gap);
            normativeprobs(trial,1:numgroups) = cumulative(tr.GroupResponse); 
            probstimetable(trial,1) = str2double(tr.GroupResponse.Attributes.trialPartTime_ms);
            
            for layer = 1:numlayers
                trl = tr.GroupResponse_afterINT{layer};
                [humannormalizedprobs(trial,layer*numgroups+(1:numgroups)),humanrawprobs(trial,layer*numgroups+(1:numgroups))] = ...
                    readprobs(trl.GroupResponse.GroupAttackProbability);
                normativeprobs(trial,layer*numgroups+(1:numgroups)) = cumulative(trl.GroupResponse); 
                probstimetable(trial,layer+1) = str2double(trl.GroupResponse.Attributes.trialPartTime_ms);
                layertypes{trial,layer} = trl.INTLayerShown.LayerType.Attributes.xsi_type;
                surprisereport(trial,layer) = str2double(trl.SurpriseReportResponse.Attributes.surprise);
                surprisereporttime(trial,layer) = str2double(trl.SurpriseReportResponse.Attributes.trialPartTime_ms);
            end
            
            forcedchoicetable(trial) = tr.GroupResponse.Attributes.locationId;
            grouptimetable(trial) = str2double(tr.GroupResponse.Attributes.trialPartTime_ms);
        end
        
        ta = tr.TroopAllocationResponse.TroopAllocation;
        for group = 1:numgroups
            normalizedallocation(trial,group) = str2double(ta{group}.Attributes.allocation);
            rawallocation(trial,group) = str2double(ta{group}.Attributes.allocation_raw);
        end
        allocationtimetable(trial) = str2double(tr.TroopAllocationResponse.Attributes.trialPartTime_ms);

        Probabilities_score_table(trial) = str2double(tr.ResponseFeedback.ProbabilitiesScore_s1.Text);
        Troop_allocation_score_table(trial) = str2double(tr.ResponseFeedback.TroopAllocationScore_s2.Text);  
    end
    
    %% Return tables
    trialtimetable(trial) = str2double(tr.Attributes.trialTime_ms);
    surprisetable(trial) = str2double(tr.GroundTruthSurpriseResponse.Attributes.surprise);
    surprisetimetable(trial) = str2double(tr.GroundTruthSurpriseResponse.Attributes.trialPartTime_ms);
end

end

%%
function [normprobs,rawprobs] = readprobs(GroupAttackProbabilities)

numgroups = length(GroupAttackProbabilities);
[normprobs,rawprobs] = zerodata(1,numgroups);

for group = 1:numgroups
    normprobs(group) = str2double(GroupAttackProbabilities{group}.Attributes.probability);
    rawprobs(group) = str2double(GroupAttackProbabilities{group}.Attributes.rawProbability);
end

end

%%

function normativeprobs = cumulative(response)

if isfield(response,'NormativeProbsCumalitive')
    normativeprobs = str2num(response.NormativeProbsCumalitive.Text); %#ok
elseif isfield(response,'NormativeProbsCumulative') 
    normativeprobs = str2num(response.NormativeProbsCumulative.Text); %#ok
end
%         numgroups = (length(pt.GroupProbe.Groups.Text)+1)/2;
%         alx = str2num(pt.GroupProbe.AttackLocation.Attributes.x);
%         aly = str2num(pt.GroupProbe.AttackLocation.Attributes.y);
%         responsibleGroup = pt.GroundTruth.Attributes.responsibleGroup;
        
end