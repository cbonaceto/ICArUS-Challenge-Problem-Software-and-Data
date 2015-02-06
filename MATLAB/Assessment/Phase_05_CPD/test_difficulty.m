
% netfilename = 'pilot-study.neta'; casefilename = 'testcases.cas';
% netfilename = 'pilot-study-flattened.neta'; casefilename = 'testcases-flattened.cas';
% netfilename = 'pilot-study-flattened-edited.neta'; casefilename = 'testcases-flattened-edited.cas';
netfilename = 'pilot-study-final.neta'; casefilename = 'pilot-testcases-final.cas';

import norsys.netica.*;
env = Environ('+WilliamsR/MITRE/310-5/34686');

%% setup
streamer = Streamer(netfilename);
net = Net(streamer);
net.compile

check_Matlab_Netica_models(net);

% get hypothesis (facility) node
FacilityType = net.getNode('FacilityType');
numfacilities = FacilityType.getNumStates;
facilityprior = FacilityType.getBeliefs;

% load all nodes and states
allnodes = getNodeList(net,NodeList(net));
allnodenames = getNodeArrayNames(allnodes);
numnodes = length(allnodes);
allstates = getStateList(allnodes);

% find evidence nodes
[evidencenodes, evidencenodelist] = getNodeList(net,NodeList(net),'Evidence');
evidencenodenames = getNodeArrayNames(evidencenodes);
numevidencenodes = length(evidencenodes);

% import case sample
[cases, caseheadings] = readcasefile(casefilename,numnodes);
numcases = size(cases,2);
assert(numnodes == size(cases,1),'Wrong case file?');

epsilon = 1e-4;

fdivergence = @KLdivergenceSymmetric1;
Fdivergencetoscore = @(x) 100 * 2.^(-2*x);

% %% Change probabilities in some conditional probability table
% ReplaceCP(net.getNode('SIGINTDensity'),0.7,0.9);
% ReplaceCP(net.getNode('SIGINTDensity'),0.3,0.1);

%% Display tables used for answers to assessment questions
test_assessment;

%% Difficulty measurements

node_layer_information;

cue_validity;

display_difficulty_measurements;

%% Performance prediction

score_with_partial_learning;

% score_with_training_data; % not implemented

%% close
net.finalize;
env.finalize;

