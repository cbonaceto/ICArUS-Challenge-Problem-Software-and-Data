function [humannormalizedprobs, humanrawprobs, normativeprobs, num_probsets, numbins, ...
    trialtimetable, modality, widget] = ...
        ui_parseXMLdatafile(xmlfilename,tasktype,max_probs_in_set,max_probsets)

x1 = xml2struct(xmlfilename);
x1 = x1.(sprintf('ns3_%s',tasktype));

modality = x1.Attributes.modality;
widget = x1.Attributes.widget;

numtrials = length(x1.Trial);

[humannormalizedprobs, humanrawprobs, normativeprobs] = zerodata(numtrials,max_probs_in_set*max_probsets);
trialtimetable = zeros(numtrials,1);

%% parse
for b = 1:numtrials
    tr = x1.Trial{b};

    trialtimetable(b) = str2double(tr.TrialResponse.Attributes.trialTime_ms);

    if isfield(tr,'ProbabilityData')
        % task 1,2,3 or 5
        numbins = length(tr.ProbabilityData.ItemProbability);
        num_probsets = 1; % MSC what about task 4
    
        for bin = 1:numbins
            normativeprobs(b,bin) =               str2double(tr.ProbabilityData.ItemProbability{bin}.Attributes.Probability);
            humannormalizedprobs(b,bin) = 1/100 * str2double(tr.TrialResponse.ProbabilityResponseData.ItemProbabilityResponse{bin}.Attributes.probability);
            humanrawprobs(b,bin) =        1/100 * str2double(tr.TrialResponse.ProbabilityResponseData.ItemProbabilityResponse{bin}.Attributes.rawProbability);
        end
    else
        numbins = length(tr.TrialResponse.ProbabilityResponseData{1}.ItemProbabilityResponse);
        num_probsets = length(tr.LayerPresentation);
        
        for layr = 1:num_probsets
            for bin = 1:numbins
                normativeprobs(b,bin + (layr-1)*numbins) =               str2double(tr.LayerPresentation{layr}.ProbabilityData.ItemProbability{bin}.Attributes.Probability);
                humannormalizedprobs(b,bin + (layr-1)*numbins) = 1/100 * str2double(tr.TrialResponse.ProbabilityResponseData{layr}.ItemProbabilityResponse{bin}.Attributes.probability);
                humanrawprobs(b,bin + (layr-1)*numbins) =        1/100 * str2double(tr.TrialResponse.ProbabilityResponseData{layr}.ItemProbabilityResponse{bin}.Attributes.rawProbability);
            end
        end        
    end
end
