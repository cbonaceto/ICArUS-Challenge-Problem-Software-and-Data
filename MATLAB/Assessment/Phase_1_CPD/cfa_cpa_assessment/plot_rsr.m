function plot_rsr(rsr_ab, rsr_model, abModelTitle, modelTitle, examName,... 
    tasks, taskWeights, showPlots, saveData, dataFolder)
%PLOT_RSR Plots RSR results for each task for an A-B model and another
%model.
%   Detailed explanation goes here

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

numTasks = max(tasks);

%% Aggregate data
rsr_ab_avg = zeros(numTasks, 1);
rsr_ab_weighted_avg = 0;
rsr_model_avg = zeros(numTasks, 1);
rsr_variants_model_avg = zeros(numTasks, 4);
rsr_model_weighted_avg = 0;
for task = tasks
    rsr_ab_avg(task) = rsr_ab{task}.rsr;
    rsr_ab_weighted_avg = rsr_ab_weighted_avg + (rsr_ab_avg(task) * taskWeights(task));
    
    rsr_model_avg(task) = rsr_model{task}.rsr;
    rsr_variants_model_avg(task, 1) = rsr_model{task}.rsr;
    rsr_variants_model_avg(task, 2) = rsr_model{task}.rsr_bayesian;
    rsr_variants_model_avg(task, 3) = rsr_model{task}.rsr_rmse;
    rsr_variants_model_avg(task, 4) = rsr_model{task}.rsr_rmse_bayesian;
    rsr_model_weighted_avg = rsr_model_weighted_avg + (rsr_model_avg(task) * taskWeights(task));
end
%%%%%

%% Plot comparision of RSR for the A-B model and the other model for each task
figPosition = [100, 200, 880, 660];
%figPositionLarge = [100, 50, 1000, 900];
figName = [abModelTitle, ' and ', modelTitle, ' RSR Comparison  (', examName, ')'];
figHandle = figure('name', figName, 'position', figPosition, 'NumberTitle', 'off', 'Visible', visible);
title(figName);
hold on;

xlabel('Task');
xlim([0 numTasks+1]);
set(gca,'xtick', 1:numTasks);
ylabel('Avg. RSR');
ylim([0 100]);
set(gca, 'ytick', 0:10:100);

rsr_data = [rsr_ab_avg, rsr_model_avg];
if numTasks == 1
    h = bar(1:2, [rsr_ab_avg, rsr_model_avg; NaN, NaN]);
    set(h(1), 'FaceColor', 'b', 'EdgeColor', 'none');
    set(h(2), 'FaceColor', 'r', 'EdgeColor', 'none');
else
    for i = 1:numTasks
        h = bar(i:i+1, [rsr_data(i,:); NaN, NaN], taskWeights(i));
        set(h(1), 'FaceColor', 'b', 'EdgeColor', 'none');
        set(h(2), 'FaceColor', 'r', 'EdgeColor', 'none');
    end
end

%Plot lines at weighted RSR values for data sets 1 and 2
plot(0:numTasks+1, repmat(rsr_ab_weighted_avg, 1, numTasks+2), 'b:');
plot(0:numTasks+1, repmat(rsr_model_weighted_avg, 1, numTasks+2), 'r:');

%Label bars and add legend
x = zeros(1, numTasks*2);
y = reshape(rsr_data', 1, numTasks*2) + 2;
for i = 1:numTasks
    x(i*2-1) = i - .18;
    x(i*2) = i + .18;
end
label_text = cell(1, numTasks*2);
i = 1;
for r = y - 2
    label_text{i} = num2str(round(r));
    i = i+1;
end
text(x, y, label_text,...
    'HorizontalAlignment','center',...
    'FontSize',8,...
    'FontWeight','bold');
hleg = legend({abModelTitle, modelTitle}, 'Location', 'NorthWest');
set(hleg, 'FontSize', 8);

if saveData    
    %figName = [abModelTitle, ' And ', modelTitle, ' Comparison, ', examName];
    fileName = [dataFolder, '\', abModelTitle, '_', modelTitle, '_RSR_Comparison_' examName];        
    saveas(figHandle, fileName, 'png');
end
%%%%%%%%%%

%% Plot each RSR type for the model for each task
figName = [modelTitle, ' RSR Variants Comparison  (', examName, ')'];
figHandle = figure('name', figName, 'position', figPosition, 'NumberTitle', 'off', 'Visible', visible);
title(figName);
hold on;

xlabel('Task');
xlim([0 numTasks+1]);
set(gca,'xtick', 1:numTasks);
ylabel('Avg. RSR');
ylim([0 100]);
set(gca, 'ytick', 0:10:100);

%rsr_variants_model_avg(task, 1) = rsr_model{task}.rsr;
%rsr_variants_model_avg(task, 2) = rsr_model{task}.rsr_bayesian;
%rsr_variants_model_avg(task, 3) = rsr_model{task}.rsr_rmse;
%rsr_variants_model_avg(task, 4) = rsr_model{task}.rsr_rmse_bayesian;

%rsr_data = [rsr_ab_avg, rsr_model_avg];
if numTasks == 1
    h = bar(1:2, [rsr_variants_model_avg; NaN, NaN, NaN, NaN]);
    set(h(1), 'FaceColor', 'b', 'EdgeColor', 'none');    
    set(h(2), 'FaceColor', 'r', 'EdgeColor', 'none');
    set(h(3), 'FaceColor', 'c', 'EdgeColor', 'none');
    set(h(4), 'FaceColor', 'm', 'EdgeColor', 'none');
else
    for i = 1:numTasks
        %[rsr_variants_model_avg(i,:); NaN, NaN, NaN, NaN]
        h = bar(i:i+1, [rsr_variants_model_avg(i,:); NaN, NaN, NaN, NaN], taskWeights(i));
        set(h(1), 'FaceColor', 'b', 'EdgeColor', 'none');
        set(h(2), 'FaceColor', 'r', 'EdgeColor', 'none');
        set(h(3), 'FaceColor', 'c', 'EdgeColor', 'none');
        set(h(4), 'FaceColor', 'm', 'EdgeColor', 'none');
    end
end

%Label bars and add legend
x = zeros(1, numTasks*4);
y = reshape(rsr_variants_model_avg', 1, numTasks*4) + 2;
index = 1;
for i = 1:numTasks        
    for j = 1:4
        switch j
            case 1, x(index) = i - .28;
            case 2, x(index) = i - .09;
            case 3, x(index) = i + .09;
            case 4, x(index) = i + .28;
        end        
        index = index + 1;
    end    
end
label_text = cell(1, numTasks*2);
i = 1;
for r = y - 2
    label_text{i} = num2str(round(r));
    i = i+1;
end
text(x, y, label_text,...
    'HorizontalAlignment','center',...
    'FontSize',8,...
    'FontWeight','bold');
hleg = legend({'RSR Spec', 'RSR-Bayesian', 'RSR(RMSE)', 'RSR-Bayesian(RMSE)'}, 'Location', 'NorthWest');
set(hleg, 'FontSize', 8);

if saveData    
    %figName = [abModelTitle, ' And ', modelTitle, ' Comparison, ', examName];
    fileName = [dataFolder, '\', modelTitle, '_RSR_Variants_Comparison_' examName];        
    saveas(figHandle, fileName, 'png');
end
%%%%%%%%%%

end