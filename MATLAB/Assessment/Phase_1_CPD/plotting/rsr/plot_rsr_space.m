function plot_rsr_space(difference, epsilon, saveData, showPlots, dataFolder)
%PLOT_RSR_SAMPLES Summary of this function goes here
%   Detailed explanation goes here

if ~exist('epsilon', 'var')
  %Use the default value for epsilon (0.001)
  epsilon = 0.001;
end

if ~exist('saveData', 'var')
    saveData = false;
end
if saveData && (~exist('dataFolder', 'var') || isempty(dataFolder))
    dataFolder = '';
end
if ~exist('showPlots', 'var') || showPlots
    visible = 'on';
else 
    visible = 'off';
end

%Create probs vector with values from epsilon to 1-epsilon
probs = zeros(1, 101);
probs(1) = epsilon;
probs(2:100) = .01:0.01:0.99;
probs(101) = 1-epsilon;

%Compute RSR values
humanProbs = zeros(1, 2);
modelProbs = zeros(1, 2);
rsrVals = zeros(2, length(probs));
for i = 1:length(probs)
    humanProbs(1) = probs(i);
    humanProbs(2) = 1 - probs(i);
    modelProbs(1) = humanProbs(1) + difference;
    if modelProbs(1) >= probs(1) && modelProbs(1) <= probs(101)
        modelProbs(2) = 1 - modelProbs(1);
        rsrVals(1,i) = rsr(humanProbs, modelProbs);
        rsrVals(2,i) = rsr(modelProbs, humanProbs);
    else
        %Probabilities out of range
        rsrVals(1,i) = NaN;
        rsrVals(2,i) = NaN;
    end
end

%rsrVals

%Create plot
figPosition = [60, 60, 800, 600];
signChar = '+';
if difference < 0
    signChar = '-';
end
figName = ['RSR Space for Human-Model Difference of ' signChar num2str(abs(difference)*100) '%'];
figHandle = figure('name', figName, 'position', figPosition, 'NumberTitle', 'off', 'Visible', visible);
hold on;
title(figName);

xlabel('Human Prob 1 (Human Prob 2 = 1 - Human Prob 1)');
xlim([0 102]);
tickLocations = 1:10:101;
set(gca,'xtick', tickLocations);
numTicks = length(tickLocations);
ticks = cell(1, numTicks);
index = 1;
for i=tickLocations
   ticks{index} = num2str(probs(i));
   index = index + 1;
end
set(gca,'xticklabel', ticks);

ylabel('RSR (%)');
ylim([-10 110]);

handles = zeros(3,1);
handles(1) = plot(1:101, rsrVals(1,:), '-ob');
handles(2) = plot(1:101, rsrVals(2,:), '-*k');
handles(3) = plot(1:101, repmat(50, 101, 1), '-r');

legend(handles, 'Human-Model', 'Model-Human', 'RSR=50%');

%Save figure
if saveData  
    fileName = [dataFolder, '\', 'RSR_Space_Diff_' signChar num2str(abs(difference)*100) '_Pct'];
    hgsave(fileName, '-v6');
    saveas(figHandle, fileName, 'png');
end

end