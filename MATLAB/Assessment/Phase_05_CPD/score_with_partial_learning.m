% score_with_partial_learning

%% test discounting of information by creating nets that only partially
%% learned probabilitiles
discountvalues = 0.1:0.1:1;

correctanswers = zeros(numcases,numfacilities);
for c = 1:numcases
    setlayerfindings(evidencenodes,caseheadings,cases(:,c));
    correctanswers(c,:) = FacilityType.getBeliefs;
    clearlayerfindings(evidencenodes);
end

discountedanswers = zeros(numcases,numfacilities,length(discountvalues));
for d = 1:length(discountvalues)
    discount = discountvalues(d);

    % make copy of true net, and get its evidence nodes
    discountednet = Net(net,'duplicated',env,'');
    discountednet.compile;
    discountednodes = getNodeList(discountednet,NodeList(discountednet),'Evidence');
    discountedFacilityType = discountednet.getNode('FacilityType');
    
    for n = 1:numevidencenodes
        node = discountednodes(n);
        for s = 0:numfacilities-1
            % MSC this only works for 2-layer nets
            cpt = node.getCPTable(s,[]);
            cpt = discountprobability(cpt,discount);
            node.setCPTable(s,cpt);
        end
    end

    for c = 1:numcases
        setlayerfindings(discountednodes,caseheadings,cases(:,c));
        discountedanswers(c,:,d) = discountedFacilityType.getBeliefs;
        clearlayerfindings(discountednodes);
    end

    if discount == 0.1
        discountedfilename = sprintf('pilot-study-discounted-%.1f.neta',discount);
        stream = Streamer(discountedfilename);
        discountednet.write(stream);
        fprintf('Writing discounted net %s\n',discountedfilename);
    end
    
    discountednet.finalize;
    
end

%% Compare scores of discounted nets (true net = 100)

scores = zeros(numcases,length(discountvalues));
for c = 1:numcases
    for d = 1:length(discountvalues)
        DKL = feval(fdivergence,correctanswers(c,:),discountedanswers(c,:,d),epsilon);
        scores(c,d) = feval(Fdivergencetoscore,DKL);
    end
end
meanscore = mean(scores,1);

fprintf('\nDiscount\tScore\n');
fprintf('%.2f\t\t%.2f\n',[discountvalues ; meanscore]);

%%
figure;
plot(discountvalues,meanscore,'o-');
xlabel(sprintf('Probability learning\nDiscount Factor\n(0 = no learning; 1 = complete)'));
ylabel('Score');
axis([0 1 0 100]);
hold on;
plot([0.1 0.1],[0 100],':k')
text(0.1+0.02,5,'iSPIED 2.2')
plot([0.66 0.66],[0 100],':k')
text(0.66+0.02,5,'iSPIED 2.1')
plot([0.94 0.94],[0 100],':k')
text(0.94+0.02,5,'iSPIED 1')
title('Performance vs. Bayesian net probability learning ability');

% Phase 0 iSPIED:
% Task 1 we find almost no discounting, d = 0.94. 
% In Task 2.1 we find moderate discounting, d = 0.66. 
% In Task 2.2 we find very large discounting, d = 0.10. 
% Kevin estimates "upper limit" for pilot study task to be 0.5
