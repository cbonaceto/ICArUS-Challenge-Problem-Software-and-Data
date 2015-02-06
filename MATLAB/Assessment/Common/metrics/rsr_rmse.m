function rsr_rmse_val = rsr_rmse(humanProbs, modelProbs, nullModelProbs)
%RMS Compute Root Mean Square Error [RSR(RMSE)] match between human and model probs
%   Detailed explanation goes here

if ~exist('nullModelProbs', 'var') || isempty(nullModelProbs)
    %Use random (uniform) probs as the null model probs
    numProbs = length(humanProbs);
    nullModelProbs = repmat(1/numProbs, 1, numProbs);
end

%Compute RMSE of model to human (RMSE(model-to-data))
rmse_model_to_data = rmse(modelProbs, humanProbs);

%Compute RMSE of null model to human (RMSE(random-to-data))
%rmse_random_to_data = rmse(modelProbs, uniformProbs);
rmse_random_to_data = rmse(nullModelProbs, humanProbs);

%Compute RER(RMSE) as RMSE(model-to-data) / RMSE(random-to_data)
rer_rmse = 100 * (rmse_model_to_data / rmse_random_to_data);

%Comptue RSR(RMSE) as 1 - RER(RMSE)
rsr_rmse_val = max([0, 100-rer_rmse]);

end