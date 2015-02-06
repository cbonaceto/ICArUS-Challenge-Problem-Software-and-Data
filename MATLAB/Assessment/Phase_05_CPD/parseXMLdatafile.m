function [table, trialtypes, trialtimes, trialscores, layerchoices] = parseXMLdatafile(xmlfilename,maxprobs,finalfield)

x1 = xml2struct(xmlfilename);
trials = x1.TestPhaseResponse.TrialResponse;

trialtypes = cell(1,length(trials));
trialtimes = zeros(1,length(trials));
trialscores = zeros(1,length(trials));
layerchoices = zeros(1,length(trials));

for t = 1:length(trials)

    %% Trial type
    if strcmp(trials{t}.Attributes.xsi_type,'IdentifyItemResponse')
        trialtypes{t} = 'Identify'; 
        resp = trials{t}.SceneItemProbabilityResponse;
        trialscores(t) = str2double(trials{t}.TrialScore.Text);
    elseif strcmp(trials{t}.Attributes.xsi_type,'LocateItemResponse')
        trialtypes{t} = 'Locate';
        resp = trials{t}.SectorProbabilityResponse; 
        trialscores(t) = str2double(trials{t}.TrialScore.Text);
    elseif strcmp(trials{t}.Attributes.xsi_type,'AssessmentResponse')
        trialtypes{t} = 'Assessment';
        resp = trials{t}.SceneItemProbabilities;
    else
        error('Unknown response type');
    end
   
    %% Timing
    trialtimes(t) = str2double(trials{t}.Attributes.TrialTime_ms);

    %% Collect user choices and all user probabilities
    for r = 1:length(resp)
        if length(resp) == 1, resp = {resp}; end
        
        if strcmp(trialtypes{t},'Assessment')
            probs = resp{r}.SceneItemProbability;
        else
            layers = resp{r}.LayersShown.Layer;
            if length(layers) == 1, layers = {layers}; end

            if strcmp(trialtypes{t},'Identify')
                probs = resp{r}.SceneItemProbabilities.SceneItemProbability;
            elseif strcmp(trialtypes{t},'Locate')
                probs = resp{r}.SectorProbabilities.SectorProbability;
            end

            for l = 1:length(layers)
                if strcmp(layers{l}.Attributes.PresentationType,'UserChoice')
                    layerchoices(t) = str2double(layers{l}.Attributes.LayerId);
                end
            end
        end
        
        if length(probs) == 1, probs = {probs}; end
        for p = 1:length(probs)
            table(t,(r-1)*maxprobs + p) = str2double(probs{p}.Attributes.(finalfield));
        end
        % table(t,(r-1)*maxprobs + (1:4))
    end    
end

