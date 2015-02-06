% ui_graph_results

%% trial filtering
min_trialtime = 1 * 1000; % ms
max_trialtime = 60 * 1000; % ms
throwout = 3;

%% filter data
% rawps = nan(taskparams.numtasks,numsubjects,max(taskparams.numguis),max(numtrials),max(taskparams.numbins));
mean_vari = nan(taskparams.numtasks,numsubjects,max(taskparams.numguis));
mean_bias = nan(taskparams.numtasks,numsubjects,max(taskparams.numguis));
mean_time = nan(taskparams.numtasks,numsubjects,max(taskparams.numguis));

for task = 1:taskparams.numtasks
    for gui = 1:taskparams.numguis(task)
        for sidx = 1:numsubjects
            subjectdatatable = alldata{subjectnums(sidx),task,gui};
            numtrials = size(subjectdatatable,1);
            varis = [];
            biases = [];
            trialtimes = [];
            for i = 1:numtrials
                layr = 1;
                % MSC iterate over layers later
                
                rawp = subjectdatatable(i,rawhumanprobsoffset + taskparams.numbins(task)*(layr-1) + (1:taskparams.numbins(task)));
                trialtime = subjectdatatable{i,Trial_time_idx};
                
                % Throw out the first N trials. 
                % Take only trials where some probabilities are been
                % changed from defaults. 
                % Also, filter out bad times
                if i > throwout && ...
                   all(cell2mat(rawp) ~= subjectdatatable{i,Default_idx}) && ...
                   trialtime > min_trialtime && ...
                   trialtime < max_trialtime
                    varis = [varis subjectdatatable{i,ABS_idx + (layr-1)}]; %#ok
                    biases = [biases subjectdatatable{i,DEV_idx + (layr-1)}]; %#ok
                    trialtimes = [trialtimes trialtime]; %#ok
                end
            end
            
            % find means, statistics
            mean_vari(task,sidx,gui) = mean(varis);
            mean_bias(task,sidx,gui) = mean(biases);
            mean_time(task,sidx,gui) = mean(trialtimes);
        end
    end
end

%% Task 1: PercentagesPresentation
% task,subject,gui

task = 1; task_time = mean_time(task,:,:); 
mean_widget1_time_s = 1/1000 * squeeze(mean(task_time,2));
sem_widget1_time_s =  1/1000 * squeeze( std(task_time,[],2)) / sqrt(numsubjects);
widget1_names = getwidgetnames(task,taskdef);

% task 1: barplot time vs. widget
fig = maxfigure; 
set(gcf,'Color',[1 1 1]);
barweb(mean_widget1_time_s,sem_widget1_time_s,1,[],'Percentages Presentation UI Speed','User Interface','Time (s)',[],[],widget1_names,[],'axis');
set(gca,'YLim',[0 30]);
saveas(fig,'Results/UI Percentages Presentation Speed.fig');
export_fig('Results/UI Percentages Presentation Speed.png');

%% task 1: scatterplot time vs. accuracy over all subjects and widgets. Colored by widget. 
fig = maxfigure; hold on;
set(gcf,'Color',[1 1 1]);
colors = 'rygcbk';
for gui = 1:taskparams.numguis(task)
    plot(1/1000 * mean_time(task,:,gui),100 * mean_vari(task,:,gui),colors(gui),'Marker','.','MarkerSize',25,'LineStyle','none');
end
xlabel('Time (s)','FontSize',16); ylabel('Inaccuracy (%)','FontSize',16);
legend(widget1_names,'Location','NorthWest');
saveas(fig,'Results/UI Percentages Presentation, Speed vs Accuracy (by widget).fig');
export_fig('Results/UI Percentages Presentation, Speed vs Accuracy (by widget).png');

% task 1: scatterplot time vs. accuracy over all subjects and widgets. Colored by subject. 
maxfigure; hold on;
set(gcf,'Color',[1 1 1]);
for s = 1:numsubjects
    plot(1/1000 * squeeze(mean_time(task,s,:)),100 * squeeze(mean_vari(task,s,:)),colors(s),'Marker','.','MarkerSize',25,'LineStyle','none');
end
xlabel('Time (s)','FontSize',16); ylabel('Inaccuracy (%)','FontSize',16);
legend(num2cellstring(subjectnums),'Location','NorthWest');
saveas(fig,'Results/UI Percentages Presentation, Speed vs Accuracy (by subject).fig');
export_fig('Results/UI Percentages Presentation, Speed vs Accuracy (by subject).png');

%% Task 2: SpatialPresentation
task = 2; task_time = mean_time(task,:,:); task_vari = mean_vari(task,:,:); task_bias = mean_bias(task,:,:);
mean_widget2_time_s = 1/1000 * mean_excluding_nan(squeeze(task_time),1);
sem_widget2_time_s =  1/1000 * sqrt(var_excluding_nan(squeeze(task_time),1)) / sqrt(numsubjects);
mean_widget2_accuracy = 100 * mean_excluding_nan(squeeze(task_vari),1);
sem_widget2_accuracy = 100 * sqrt(var_excluding_nan(squeeze(task_vari),1)) / sqrt(numsubjects);
mean_widget2_bias = 100 * mean_excluding_nan(squeeze(task_bias),1);
widget2_names = getwidgetnames(task,taskdef);

% task 2: time vs. widget
maxfigure; 
set(gcf,'Color',[1 1 1]);
barweb(mean_widget2_time_s,sem_widget2_time_s,1,[],'UI Spatial Presentation Speed','User Interface','Time (s)',[],[],widget2_names,[],'axis');
set(gca,'YLim',[0 30]);
saveas(fig,'Results/UI Spatial Presentation Speed.fig');
export_fig('Results/UI Spatial Presentation Speed.png');

% task 2: accuracy (variance) vs. widget
maxfigure;
set(gcf,'Color',[1 1 1]);
barweb(mean_widget2_accuracy,sem_widget2_accuracy,1,[],'UI Spatial Presentation Inaccuracy','User Interface','Inaccuracy (%)',[],[],widget2_names,[],'axis');
saveas(fig,'Results/UI Spatial Presentation Accuracy.fig');
export_fig('Results/UI Spatial Presentation Accuracy.png');

% task 2: accuracy (bias) vs. widget
maxfigure; 
set(gcf,'Color',[1 1 1]);
barweb(mean_widget2_bias,zeros(size(mean_widget2_bias)),1,[],'UI Spatial Presentation Bias','User Interface','Inaccuracy (%)',[],[],widget2_names,[],'axis');
saveas(fig,'Results/UI Spatial Presentation Bias.fig');
export_fig('Results/UI Spatial Presentation Bias.png');

% %% task 2: scatterplot time vs. accuracy over all subjects and widgets. Colored by widget. 
% maxfigure; hold on;
% colors = 'rygcbk';
% for gui = 1:taskparams.numguis(task)
%     plot(1/1000 * mean_time(task,:,gui),100 * mean_vari(task,:,gui),colors(gui),'.','MarkerSize',25,'LineStyle','none');
% end
% xlabel('Time (s)','FontSize',16); ylabel('Inaccuracy (%)','FontSize',16);
% legend(widget2_names,'Location','NorthWest');
% saveas(fig,'Results/UI Spatial Presentation Speed vs Accuracy (by widget).fig');
% export_fig('Results/UI Spatial Presentation Speed vs Accuracy (by widget).png');

%% task 1 and 2: barplot time vs. widget
fig = maxfigure; 
set(gcf,'Color',[1 1 1]);
%barweb([mean_widget1_time_s' ; mean_widget2_time_s],zeros(2,size(mean_widget1_time_s,1)),1,[],'UI Speed','User Interface','Time (s)',[],[],widget2_names,[],'axis');
bar(mean_widget2_time_s,'FaceColor',[.8 .8 .8]);
hold on;
bar(mean_widget1_time_s,'FaceColor',[.6 .6 .6]);
title('UI Speed'); xlabel('User Interface'); ylabel('Time (s)');
set(gca,'XTick',1:taskparams.numguis(1));
set(gca,'XTickLabel',widget1_names);
set(gca,'YLim',[0 30]);
saveas(fig,'Results/UI Task Speed.fig');
export_fig('Results/UI Task Speed.png');

% %% Task 3: SpatialPresentation with Normalization
% % task 3: barplot accuracy (variance) vs. normalization
% % task 3: barplot accuracy (bias) vs. normalization
% % task 3: time vs. widget
% 
% %% Task 4: Updating Task
% % task 4: accuracy (variance) vs. normalization, stages 1-3, plus averaged
% % task 4: accuracy (bias) vs. normalization, stages 1-3, plus averaged
% 
% %% Task 5: Anchoring Task
% % task 5: accuracy (variance) vs. anchor value
% % task 5: accuracy (bias) vs. anchor value
% % task 5: time vs. anchor value
% 
%% Individual subject data