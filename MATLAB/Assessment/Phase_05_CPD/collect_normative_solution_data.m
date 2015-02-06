%   Normative data file format
% 
% See loadsubjecttabledesign.m for explanation.

normative_headings = [...
    'Phase_number' 'Trial_number' 'Query_type' 'Layer_type' 'Specified_answer' 'Presentation_order' ...
    'Number_probability_sets' 'Number_facilities' 'Max_probs_in_set' ...
    probs_headings('Probs_norm_1',4) probs_headings('Probs_norm_2',4) probs_headings('Probs_norm_3',4) ...
    ];
[numbasefields, Phase_number_idx, Trial_number_idx, Query_type_idx, Layer_type_idx, Specified_answer_idx, ...
    Presentation_order_idx, Number_probability_sets_idx, Number_facilities_idx, Max_probs_in_set_idx] = enum;

normativefilename = 'results/normative_solution_PILOT.csv';

%% Test design
loadtestdesign;

% load palettes and populate scene vectors
load('data/facility_palette_PILOT.mat');
scene2 = load('data/scene_palette_PILOT_2FAC.mat'); scene2 = scene2.scene;
scene3 = load('data/scene_palette_PILOT_3FAC.mat'); scene3 = scene3.scene;
scene4 = load('data/scene_palette_PILOT_4FAC.mat'); scene4 = scene4.scene;
scene2 = pullinfacilitydata(scene2,facility,2);
scene3 = pullinfacilitydata(scene3,facility,3);
scene4 = pullinfacilitydata(scene4,facility,4);

%% load and setup Netica net
netfilename = 'pilot-study-final.neta';
import norsys.netica.*;
env = Environ('+WilliamsR/MITRE/310-5/34686');

streamer = Streamer(netfilename);
net = Net(streamer);
net.compile

evidencenodes = getNodeList(net,NodeList(net),'Evidence');
numevidencenodes = length(evidencenodes);
facilitynode = net.getNode('FacilityType');

%% Build solution table
solutiontable = {};

% difficulty assessment
[facilitymeans, cardinality] = get_facility_category_mean(4,facilitynode,evidencenodes);
points = {};
unscaledL1distance = [];
scaledL1distance = [];
unscaledL2distance = [];
scaledL2distance = [];

for phasenum = 1:length(examfile.Set)
    test = examfile.Set(phasenum).Test;
    numfacilities = numfacilitiesintroduced(phasenum);
    facilitiesshown = 1:numfacilities;

    if ~isempty(test)
        numquestions = length(test.QuestionType);
        for trial = 1:numquestions
            %% get information from exam file 
            querytype =    test.QuestionType{trial}{1}; % 'Identify' or 'Locate'
            specifiedanswer = test.QuestionType{trial}{2}; % 'A' 'B' 'C' 'D'
            layertype =       test.QuestionType{trial}{3}; % 'Simultaneous' 'Sequential' 'User Choice'
            presentationorder = test.QuestionType{trial}(4:end); % 'I', 'IS', 'IMS', 'I' 'MS', 'I' 'M' 'S', 'I' 'S' 'M', etc.
            if ~ischar(presentationorder{end}), presentationorder = presentationorder(1:end-1); end
                        
            if strcmp(layertype,'User Choice')
                numlayers = 3;
                presentationorder_numeric_code = 0;
                % currently only {'I' 'MS'} = {'I' 'SM'} user choice is supported
                assert(isequal(presentationorder,{'I' 'MS'}) || ...
                       isequal(presentationorder,{'I' 'SM'}));
            elseif strcmp(layertype,'Sequential')
                % MSC simplify
                if isequal(presentationorder,{'I' 'S' 'M'})
                    presentationorder_numeric_code = 123;
                elseif isequal(presentationorder,{'I' 'M' 'S'})
                    presentationorder_numeric_code = 132;
                elseif isequal(presentationorder,{'I' 'S'})
                    presentationorder_numeric_code = 12;
                elseif isequal(presentationorder,{'I' 'M'})
                    presentationorder_numeric_code = 13;
                end
                numlayers = length(presentationorder);
            elseif strcmp(layertype,'Simultaneous')
                presentationorder_numeric_code = 0;
                numlayers = length(presentationorder);
            else assert('Unknown layer type');
            end
            assert(numlayers > 0);

            %% get facility 
            fvid = test.FVID(trial);
            fvid = mod(fvid,500);
            sectorid = test.SectorId(trial);

            %% fill table
            solutiontablevector = cell(1,numbasefields + max_probsets*max_probs_in_set);
            solutiontablevector{Phase_number_idx} = phasenum;
            solutiontablevector{Trial_number_idx} = trial;
            solutiontablevector{Query_type_idx} = querytype;
            solutiontablevector{Layer_type_idx} = layertype;
            solutiontablevector{Specified_answer_idx} = specifiedanswer;
            solutiontablevector{Presentation_order_idx} = presentationorder_numeric_code;
            solutiontablevector{Number_facilities_idx} = numfacilities;
            solutiontablevector{Max_probs_in_set_idx} = max_probs_in_set;
            solutiontablevector{Number_probability_sets_idx} = numlayers;

            probs = zerodata(max_probsets,max_probs_in_set);
            if strcmp(querytype,'Identify')
                scenario = getscenario(numfacilities,fvid,sectorid,scene2,scene3,scene4);
                
                % difficulty assessment
                [allfindings,findingnodenames] = scenario_to_findings_vector(scenario,'ISM');
                setlayerfindings(evidencenodes,findingnodenames,allfindings);
                point = findings_vector_to_point(evidencenodes);
                clearlayerfindings(evidencenodes);
                
                prototype = facilitymeans(specifiedanswer - 'A' + 1,:);
                unscaledL1distance = [unscaledL1distance sum(abs(prototype - point))];
                scaledL1distance = [scaledL1distance sum(abs(prototype - point) ./ cardinality)];
                unscaledL2distance = [unscaledL2distance sqrt(sum((prototype - point).^2))];
                scaledL2distance = [scaledL2distance sqrt(sum( ((prototype - point) ./ cardinality).^2))];

                if strcmp(layertype,'User Choice')
                    scprobs1 = getscenarioprobs(evidencenodes,facilitynode,scenario,'I');
                    scprobs2 = getscenarioprobs(evidencenodes,facilitynode,scenario,'IS');
                    scprobs3 = getscenarioprobs(evidencenodes,facilitynode,scenario,'IM');
                    probs(1,facilitiesshown) = scprobs1(facilitiesshown);
                    probs(2,facilitiesshown) = scprobs2(facilitiesshown);
                    probs(3,facilitiesshown) = scprobs3(facilitiesshown);
                else
                    for l = 1:numlayers
                        layersshown = [presentationorder{1:l}];
                        scprobs = getscenarioprobs(evidencenodes,facilitynode,scenario,layersshown);
                        probs(l,facilitiesshown) = scprobs(facilitiesshown);
                    end
                end
                
            elseif strcmp(querytype,'Locate')
                specifiedfacility = test.QuestionType{trial}{2};
                point = zeros(1,numevidencenodes);
                unscaledL1distance = [unscaledL1distance 0];
                scaledL1distance = [scaledL1distance 0];
                unscaledL2distance = [unscaledL2distance 0];
                scaledL2distance = [scaledL2distance 0];
                
                numscenarios = numfacilities;                
                for sc = 1:numscenarios
                    scenario = getscenario(numfacilities,fvid,sc,scene2,scene3,scene4);

                    if strcmp(layertype,'User Choice')
                        scprobs1 = getscenarioprobs(evidencenodes,facilitynode,scenario,'I');
                        scprobs2 = getscenarioprobs(evidencenodes,facilitynode,scenario,'IS');
                        scprobs3 = getscenarioprobs(evidencenodes,facilitynode,scenario,'IM');
                        probs(1,sc) = getfacilityprobability(scprobs1,specifiedfacility);
                        probs(2,sc) = getfacilityprobability(scprobs2,specifiedfacility);
                        probs(3,sc) = getfacilityprobability(scprobs3,specifiedfacility);
                    else
                        for l = 1:numlayers
                            layersshown = [presentationorder{1:l}];
                            scprobs = getscenarioprobs(evidencenodes,facilitynode,scenario,layersshown);
                            probs(l,sc) = getfacilityprobability(scprobs,specifiedfacility);
                        end
                    end
                end
            else assert('Unknown query type'); 
            end

            % normalize probability sets
            for p = 1:max_probsets
                if any(probs(p,:) > 0)
                    probs(p,:) = probs(p,:) / sum(probs(p,facilitiesshown)); 
                end
            end

            solutiontablevector(numbasefields+1:end) = num2cell(reshape(probs',[1 max_probsets*max_probs_in_set]));
            solutiontable = [solutiontable ; solutiontablevector]; %#ok
            points = [points ; point]; %#ok
        end
    end
end

%% close
net.finalize;
env.finalize;

%% write file
writeCSVheadings(normativefilename,normative_headings);
cell2csv(normativefilename,solutiontable,',',[],'.',1);
