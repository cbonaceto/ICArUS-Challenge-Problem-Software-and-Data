% cue_validity

%% Compute cue validity; iterate over 'Evidence' nodes
[meancuevalidity, maxcuevalidity, weightedcuevalidity] = zerodata(1,numevidencenodes);
[countmostprobable, priorweightedmostprobable, learnability] = zerodata(1,numfacilities);
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

        cuevalidity(n,s) = max(hypothesisBeliefs);

        % count most probable facilities -- split the vote in case of ties
        mostprobidx = find(hypothesisBeliefs == max(hypothesisBeliefs));
        countmostprobable(mostprobidx) = countmostprobable(mostprobidx) + 1/length(mostprobidx);
        priorweightedmostprobable(mostprobidx) = priorweightedmostprobable(mostprobidx) + nodePriorBeliefs(s)/length(mostprobidx);
        learnability(mostprobidx) = learnability(mostprobidx) + (hypothesisBeliefs(mostprobidx) - facilityprior(mostprobidx))';
    end
    
    meancuevalidity(n) = mean(cuevalidity(n,1:numstates));
    maxcuevalidity(n) = max(cuevalidity(n,1:numstates));
    weightedcuevalidity(n) = cuevalidity(n,1:numstates) * nodePriorBeliefs;
end
learnability = learnability ./ countmostprobable;

%% Cue validity summary
fprintf('\nCue validity\n');
for n = 1:numevidencenodes
    node = evidencenodes(n);
    nodename = char(node.getName);
    fprintf('Node %15s mean: %3.0f%% wtd: %3.0f%% max: %3.0f%%\n',nodename,meancuevalidity(n)*100,weightedcuevalidity(n)*100,maxcuevalidity(n)*100);
end

countmostprobablepercents = 100 * (countmostprobable./sum(countmostprobable));
priorweightedmostprobablepercents = 100 * (priorweightedmostprobable./sum(priorweightedmostprobable));
fprintf('\nMost probable facility after each observation:\n');
fprintf('    Unweighted: %12s\n',num2str(fround(countmostprobablepercents,2)));
fprintf('Prior Weighted: %12s\n',num2str(fround(priorweightedmostprobablepercents,2)));

fprintf('\nFacility learnability (peak height of P(H|D):\n');
fprintf(' %12s\n',num2str(fround(learnability,2)));
