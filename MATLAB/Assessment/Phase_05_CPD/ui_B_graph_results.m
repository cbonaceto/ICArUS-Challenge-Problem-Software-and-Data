% ui_B_graph_results

%% trial filtering
min_trialtime = 1 * 1000; % ms
max_trialtime = 60 * 1000; % ms
throwout = 3;

cmap = jet;

%% filter data
% rawps = nan(taskparams.numtasks,numsubjects,max(taskparams.numguis),max(numtrials),max(taskparams.numbins));
mean_vari = nan(taskparams.numtasks,numsubjects,max(taskparams.numguis));
mean_bias = nan(taskparams.numtasks,numsubjects,max(taskparams.numguis));
mean_time = nan(taskparams.numtasks,numsubjects,max(taskparams.numguis));

defaulttaskvalues = taskdef(taskparams.defaultstask).defaults;
default_bias = nan(numsubjects,length(defaulttaskvalues));
numdefaulttasktrials = size(alldata{1,taskparams.defaultstask,1},1);

for task = 1:taskparams.numtasks
    for gui = 1:taskparams.numguis(task)
        for sidx = 1:numsubjects
            subjectdatatable = alldata{subjectnums(sidx),task,gui};
            numtrials = size(subjectdatatable,1);
            varis = [];
            biases = [];
            trialtimes = [];
            default_trials = cell(1,length(defaulttaskvalues));
            for i = 1:numtrials
                layr = 1;
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
                    
                    if task == taskparams.defaultstask
                        default_trials_idx = find(subjectdatatable{i,Default_idx} == defaulttaskvalues);
                        default_trials{default_trials_idx} = [default_trials{default_trials_idx} subjectdatatable{i,DEV_idx + (layr-1)}];
                    end
                end
            end

            % find means, statistics
            mean_vari(task,sidx,gui) = mean(varis);
            mean_bias(task,sidx,gui) = mean(biases);
            mean_time(task,sidx,gui) = mean(trialtimes);
            
            if task == taskparams.defaultstask && gui == 1
                for i = 1:length(defaulttaskvalues)
                    default_bias(sidx,i) = mean(default_trials{i});
                end
            end
        end
    end
end

%% Task 1: SpatialPresentation (with normalization)
task = 1; 
widget1_names = getwidgetnames(task,taskdef);
task_time = mean_time(task,:,1:taskparams.numguis(task)); 
task_vari = mean_vari(task,:,1:taskparams.numguis(task));
task_bias = mean_bias(task,:,1:taskparams.numguis(task));

mean_widget1_time_s = 1/1000 * mean_excluding_nan(squeeze(task_time),1);
sem_widget1_time_s =  1/1000 * sqrt(var_excluding_nan(squeeze(task_time),1)) / sqrt(numsubjects);
mean_widget1_accuracy = 100 * mean_excluding_nan(squeeze(task_vari),1);
sem_widget1_accuracy = 100 * sqrt(var_excluding_nan(squeeze(task_vari),1)) / sqrt(numsubjects);
mean_widget1_bias = 100 * mean_excluding_nan(squeeze(task_bias),1);

% task 1: time vs. widget
fig = maxfigure; 
set(gcf,'Color',[1 1 1]);
barweb(mean_widget1_time_s,sem_widget1_time_s,1,[],'Spatial Presentation UI Speed','User Interface','Time (s)',[],[],widget1_names,[],'axis');
set(gca,'YLim',[0 30]);
saveas(fig,'Results/UI B Spatial Presentation Speed.fig');
export_fig('Results/UI B Spatial Presentation Speed.png');

% task 1: accuracy (variance) vs. widget
fig = maxfigure;
set(gcf,'Color',[1 1 1]);
barweb(mean_widget1_accuracy,sem_widget1_accuracy,1,[],'Spatial Presentation UI Inaccuracy','User Interface','Inaccuracy (%)',[],[],widget1_names,[],'axis');
saveas(fig,'Results/UI B Spatial Presentation Accuracy.fig');
export_fig('Results/UI B Spatial Presentation Accuracy.png');

% task 1: scatterplot time vs. accuracy over all subjects and widgets. Colored by widget. 
fig = maxfigure; hold on;
set(gcf,'Color',[1 1 1]);
for gui = 1:taskparams.numguis(task)
    coloridx = gui * floor(size(cmap,1)/taskparams.numguis(task));
    plot(1/1000 * mean_time(task,:,gui),100 * mean_vari(task,:,gui),'Color',cmap(coloridx,:),'Marker','.','MarkerSize',25,'LineStyle','none');
end
xlabel('Time (s)','FontSize',16); ylabel('Inaccuracy (%)','FontSize',16);
legend(widget1_names,'Location','NorthWest');
for gui = 1:taskparams.numguis(task)
    coloridx = gui * floor(size(cmap,1)/taskparams.numguis(task));
    plot(median(1/1000 * mean_time(task,:,gui)),median(100 * mean_vari(task,:,gui)),'Color',cmap(coloridx,:),'Marker','+','MarkerSize',25,'LineStyle','none');
end
title('Time vs. Accuracy, Spatial Presentation','FontSize',24);
saveas(fig,'Results/UI B Spatial Presentation Speed vs Accuracy (by widget).fig');
export_fig('Results/UI B Spatial Presentation Speed vs Accuracy (by widget).png');

% task 1: scatterplot time vs. accuracy over all subjects and widgets. Colored by subject. 
fig = maxfigure; hold on;
set(gcf,'Color',[1 1 1]);
for s = 1:numsubjects
    coloridx = s * floor(size(cmap,1)/numsubjects);
    plot(1/1000 * squeeze(mean_time(task,s,:)),100 * squeeze(mean_vari(task,s,:)),'Color',cmap(coloridx,:),'Marker','.','MarkerSize',25,'LineStyle','none');
end
xlabel('Time (s)','FontSize',16); ylabel('Inaccuracy (%)','FontSize',16);
legend(num2cellstring(subjectnums),'Location','NorthWest');
title('Time vs. Accuracy, Spatial Presentation','FontSize',24);
saveas(fig,'Results/UI B Spatial Presentation Speed vs Accuracy (by subject).fig');
export_fig('Results/UI B Spatial Presentation Speed vs Accuracy (by subject).png');


%% Task 2: PercentagesPresentation
% task,subject,gui

task = 2; 
task_time = mean_time(task,:,1:taskparams.numguis(task)); 
widget2_names = getwidgetnames(task,taskdef);

mean_widget2_time_s = 1/1000 * squeeze(mean(task_time,2));
sem_widget2_time_s =  1/1000 * squeeze( std(task_time,[],2)) / sqrt(numsubjects);

% task 2: barplot time vs. widget
fig = maxfigure; 
set(gcf,'Color',[1 1 1]);
barweb(mean_widget2_time_s,sem_widget2_time_s,1,[],'Percentages Presentation UI Speed','User Interface','Time (s)',[],[],widget2_names,[],'axis');
set(gca,'YLim',[0 30]);
saveas(fig,'Results/UI B Percentages Presentation Speed.fig');
export_fig('Results/UI B Percentages Presentation Speed.png');

% task 2: scatterplot time vs. accuracy over all subjects and widgets. Colored by widget. 
fig = maxfigure; hold on;
set(gcf,'Color',[1 1 1]);
for gui = 1:taskparams.numguis(task)
    coloridx = gui * floor(size(cmap,1)/taskparams.numguis(task));   
    plot(1/1000 * mean_time(task,:,gui),100 * mean_vari(task,:,gui),'Color',cmap(coloridx,:),'Marker','.','MarkerSize',25,'LineStyle','none');
end
legend(widget2_names,'Location','NorthWest');
for gui = 1:taskparams.numguis(task)
    coloridx = gui * floor(size(cmap,1)/taskparams.numguis(task));
    plot(median(1/1000 * mean_time(task,:,gui)),median(100 * mean_vari(task,:,gui)),'Color',cmap(coloridx,:),'Marker','+','MarkerSize',25,'LineStyle','none');
end
xlabel('Time (s)','FontSize',16); ylabel('Inaccuracy (%)','FontSize',16);
title('Time vs. Accuracy, Percentages Presentation','FontSize',24);
saveas(fig,'Results/UI B Percentages Presentation Speed vs Accuracy (by widget).fig');
export_fig('Results/UI B Percentages Presentation Speed vs Accuracy (by widget).png');

% task 2: scatterplot time vs. accuracy over all subjects and widgets. Colored by subject. 
fig = maxfigure; hold on;
set(gcf,'Color',[1 1 1]);
for s = 1:numsubjects
    coloridx = s * floor(size(cmap,1)/numsubjects);
    plot(1/1000 * squeeze(mean_time(task,s,:)),100 * squeeze(mean_vari(task,s,:)),'Color',cmap(coloridx,:),'Marker','.','MarkerSize',25,'LineStyle','none');
end
xlabel('Time (s)','FontSize',16); ylabel('Inaccuracy (%)','FontSize',16);
legend(num2cellstring(subjectnums),'Location','NorthWest');
title('Time vs. Accuracy, Percentages Presentation','FontSize',24);
saveas(fig,'Results/UI B Percentages Presentation Speed vs Accuracy (by subject).fig');
export_fig('Results/UI B Percentages Presentation Speed vs Accuracy (by subject).png');

%% task 1 and 2: barplot time vs. widget

for w1idx = 1:length(taskdef(1).widget)
    w2idx = find(strcmp(taskdef(1).widget(w1idx),taskdef(2).widget));
    aligned_widget2_with_widget1(w1idx) = mean_widget2_time_s(w2idx);
end

fig = maxfigure; 
set(gcf,'Color',[1 1 1]);
bar(mean_widget1_time_s,'FaceColor',[.8 .8 .8]); hold on;
bar(aligned_widget2_with_widget1,'FaceColor',[.6 .6 .6]);
title('UI Speed'); xlabel('User Interface'); ylabel('Time (s)');
set(gca,'XTick',1:taskparams.numguis(1));
set(gca,'XTickLabel',widget1_names);
set(gca,'YLim',[0 30]);
saveas(fig,'Results/UI B Task Speed.fig');
export_fig('Results/UI B Task Speed.png');

%% %% Task 3: Anchoring
% % task 5: accuracy (variance) vs. anchor value
% % task 5: accuracy (bias) vs. anchor value
% % task 5: time vs. anchor value

task = 3; 
mean_default_bias = 100 * mean(default_bias,1);
sem_default_bias =  100 * std(default_bias,[],1) / sqrt(numsubjects);

fig = maxfigure; 
set(gcf,'Color',[1 1 1]);
barweb(mean_default_bias,sem_default_bias,1,[],'Anchoring Bias','Slider default value','Bias (%)',[],[],num2cellstring(defaulttaskvalues),[],'plot');
ylim([-1 1]);
saveas(fig,'Results/UI B Spatial Default Bias.fig');
export_fig('Results/UI B Spatial Default Bias.png');

%% Individual subject data