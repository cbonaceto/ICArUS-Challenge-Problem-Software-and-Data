% graph_assessment

layercols = [0 0 0 ; 1 0.4 0.8 ; 0 1 0]; % ISM = black, pink, green

nc = 7; nr = ceil(numsubjects/nc);

%% 1: individual probs: single facilities
fs = [maxfigure maxfigure maxfigure maxfigure];
for f = 1:num_facilities
    figure(fs(f));
    norm = assessment1_norm(:,f);
    for s = 1:numsubjects
        hum = squeeze(assessment1_human(s,:,f))';
        graph_assessment1;
    end
    subtitle(sprintf('Individual probabilities, Facility %s',targetanswers(f)));
    expfig(fs(f),sprintf('results/assessment_%s',targetanswers(f)),'FontSize',14);
end

%% 1: individual probs: all facilities
f3 = maxfigure;
for s = 1:numsubjects
    for f = 1:num_facilities    
        norm = assessment1_norm(:,f);
        hum = squeeze(assessment1_human(s,:,f))';
        graph_assessment1;
    end
end
subtitle('Individual probabilities, all facilities');
expfig(f3,sprintf('results/assessment_all_facilities'),'FontSize',8);

%% 1: individual probs: average over subjects
f4 = maxfigure;
for i = 1:size(assessment1_norm,1)
    norm = assessment1_norm(i,:);
    meanhum = mean(squeeze(assessment1_human(:,i,:)));
    plot(meanhum,norm,'.','MarkerSize',20,'Color',layercols(assessment1_layer(i),:));
    hold on;
end
axis([0 0.5 0 0.5]);
axis square;
% subtitle('Individual probabilities, mean over subjects');
expfig(f4,sprintf('results/assessment_mean_subjects'),'FontSize',16);

%% 2: joint probs
f2 = maxfigure;
norm = assessment2_norm;
for s = 1:numsubjects
    hum = assessment2_human(s,:)';

    subplot(nr,nc,s);
    for i = 1:length(norm);
        plot(hum(i),norm(i),'o','MarkerSize',5,'Color',layercols(assessment2_layer(i,1),:));
        hold on;
        plot(hum(i),norm(i),'.','MarkerSize',5,'Color',layercols(assessment2_layer(i,2),:));
    end
    axis([0 1 0 1]);
    axis square;
    
%     [~, ~, ~, ~, stats] = regress(norm,[hum ones(size(hum,1),1)]); % regress with linear component to get r^2
%     rsq = stats(1); r = sqrt(rsq);
%     b = regress(norm,hum); % regress without linear component to get slope
    % line([0 1],[0 b]); 
%     title(sprintf('Subject %d r^2 = %.2f',subjectnums(s),rsq));
    title(sprintf('Subject %d',subjectnums(s)));
    expfig(f2,sprintf('results/assessment_2'),'FontSize',14);
end
subtitle('Joint probabilities');
    