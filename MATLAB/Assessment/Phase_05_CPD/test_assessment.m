% test_assessment

%% Joint probabilities for each pair of evidence nodes
for i1 = 1:numevidencenodes
    for i2 = i1+1:numevidencenodes
        n1 = evidencenodes(i1);
        n2 = evidencenodes(i2);
        statetable = zeros(n1.getNumStates,n2.getNumStates);
        statenames = getStateList([n1 n2]);

        % iterate over facilities and get findings
        for f = 1:numfacilities
            FacilityType.finding.enterState(f-1);
            b1 = n1.getBeliefs();
            b2 = n2.getBeliefs();
            FacilityType.finding.clear;
            statetable = statetable + facilityprior(f) * (b1 * b2');
        end
        fprintf('\n%s,%s\n',evidencenodenames{i1},evidencenodenames{i2});
        displaytable(statetable,{evidencenodenames{i1} evidencenodenames{i2}},statenames);
        
        % combine all states that aren't "none"
        %   convert "one" + "many" to "any"
        %   convert "high" + "low" to "any"
        sl = getStateList([n1 n2]); sl1 = sl{1}; sl2 = sl{2};
        noneidx1 = find(strcmp(sl1,'none'));
        noneidx2 = find(strcmp(sl2,'none'));
        if ~isempty(noneidx1) || ~isempty(noneidx2)
            newstatenames = statenames;
            if ~isempty(noneidx1)
                anyidx1 = setdiff(1:n1.getNumStates,noneidx1);
                newstatetable = zeros(2,n2.getNumStates);
                newstatetable(1,:) = statetable(noneidx1,:);
                newstatetable(2,:) = sum(statetable(anyidx1,:),1);
                newstatenames{1} = {'None' 'Any'};
            else
                newstatetable = statetable;
            end

            if ~isempty(noneidx2)
                newnewstatetable = zeros(size(newstatetable,1),2);
                anyidx2 = setdiff(1:n2.getNumStates,noneidx2);
                newnewstatetable(:,1) = newstatetable(:,noneidx2);
                newnewstatetable(:,2) = sum(newstatetable(:,anyidx2),2);
                newstatenames{2} = {'None' 'Any'};
            else
                newnewstatetable = newstatetable;
            end
            displaytable(newnewstatetable,{evidencenodenames{i1} evidencenodenames{i2}},newstatenames);
        end
        
        % to find which facility this state is most associated with, set
        % n1.finding.enterState(value) and n2.finding.enterState(value) and
        % read off FacilityType.
    end
end

%% Likelihoods P(H | Di) for each node i -- related to cue validity
for n = 1:numevidencenodes
    node = evidencenodes(n);
    nodename = char(node.getName);
    numstates = node.getNumStates;
    
    nodePriorBeliefs = node.getBeliefs;
    
    for s = 1:numstates
        state = node.state(s-1);
        
        node.finding.enterState(s-1);
        hypothesisBeliefs = FacilityType.getBeliefs;
        node.finding.clear;

        fprintf('%s = %s prior %s\tlikelihoods\t%s\n',nodename,char(state),sprintf('%.2f',nodePriorBeliefs(s)),sprintf('%.2f ',hypothesisBeliefs));
    end
end
