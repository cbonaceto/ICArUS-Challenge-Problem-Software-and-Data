% node_layer_information

%% Mutual information according to Netica
ENTROPY_SENSV = 2; %0x02
sens = Sensitivity(FacilityType,evidencenodelist,ENTROPY_SENSV);
fprintf('\nMutual information analysis (Netica)\n');
for n = 1:numevidencenodes
    node = evidencenodes(n);
    nodename = char(node.getName);
    mutinfo(n) = sens.getMutualInfo(node);
    fprintf('Node %15s mutual information about facility type: %.2f bits\n',nodename,mutinfo(n));
end
sens.finalize;

%% Single node entropy reduction and relation to mutual information computed by Netica
fprintf('\nNode information analysis (Netica)\n');
nodeinfo = zeros(1,numevidencenodes);
for n = 1:numevidencenodes
    node = evidencenodes(n);
    nodename = getNodeArrayNames(node);
    for c = 1:numcases
        onenodeentropy(c) = runcase(FacilityType,node,caseheadings,cases(:,c),fdivergence,epsilon);
    end
    nodeinfo(n) = mean(onenodeentropy);
    fprintf('Information in node (mean over sample of cases): %s: %.2f\n',nodename{1},nodeinfo(n));
end

%% Layer reduction in entropy about facility
layers = {'IMINT', 'SIGINT', 'MASINT'};
numlayers = length(layers);
layernodes = cell(1,numlayers);
for l = 1:numlayers
    % get all nodes in nodeset for layer
    layernodes{l} = getNodeList(net,NodeList(net),layers{l});
end

relativeentropy = zeros(numlayers,numcases);
[relativeentropySIGgivenIM, relativeentropyMASgivenIM] = zerodata(1,numcases);

% for each case
for c = 1:numcases
    for l = 1:numlayers
        relativeentropy(l,c) = runcase(FacilityType,layernodes{l},caseheadings,cases(:,c),fdivergence,epsilon);
    end
    
    % IMINT --> SIGINT, MASINT
    setlayerfindings(layernodes{strcmp(layers,'IMINT')},caseheadings,cases(:,c));
    relativeentropySIGgivenIM(c) = runcase(FacilityType,layernodes{strcmp(layers,'SIGINT')},caseheadings,cases(:,c),fdivergence,epsilon);
    relativeentropyMASgivenIM(c) = runcase(FacilityType,layernodes{strcmp(layers,'MASINT')},caseheadings,cases(:,c),fdivergence,epsilon);
    clearlayerfindings(layernodes{strcmp(layers,'IMINT')});
end
layerentropy = mean(relativeentropy,2);

Imax = feval(fdivergence,[.25 .25 .25 .25],[1 0 0 0],epsilon);
fprintf('Maximum possible relative entropy value: %.2f\n',Imax); 

fprintf('\nLayer information analysis:\n');
for l = 1:numlayers
    fprintf('%s information about facility (mean over sample of cases): %.2f\n',layers{l},layerentropy(l));
end 

fprintf('Relative entropy of SIGINT | IMINT: %.3f\n',mean(relativeentropySIGgivenIM));
fprintf('Relative entropy of MASINT | IMINT: %.3f\n',mean(relativeentropyMASgivenIM));

