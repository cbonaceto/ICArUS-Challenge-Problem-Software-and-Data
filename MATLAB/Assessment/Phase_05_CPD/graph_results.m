plotcolor = 0;

numbootstraps = 2000; % testing
% numbootstraps = 25000; % final
confidence_interval_fraction = .05;

% user choice analysis numbers obtained using node_layer_information.m
relativeentropySIGgivenIM = 0.132;
relativeentropyMASgivenIM = 0.292;

% pick which divergence to graph

% divergence = 'KLD-S'; fdivergence = @KLdivergenceSymmetric1; maxdiv = 6; hidx = KLDSnh_idx; ridx = KLDSnr_idx;
% divergence = 'KLD'; fdivergence = @KLdivergence; maxdiv = 4; hidx = KLDnh_idx; ridx = KLDnr_idx;
% divergence = 'TVD'; fdivergence = @Totalvariationdistance; maxdiv = 1; hidx = TVDnh_idx; ridx = TVDnr_idx; 
% divergence = 'RMS'; fdivergence = @RMSdivergence; maxdiv = 1; hidx = RMSnh_idx; ridx = RMSnr_idx;
divergence = 'WTA'; fdivergence = @Winnertakealldivergence; maxdiv = 1; hidx = WTAnh_idx; ridx = WTAnr_idx;

%%
if plotcolor
    testtrials = find([subjectdatatable{:,2}] == 19); %#ok
    cols = [1 0 0 ; 1 0.8 0 ; 0 1 0 ; 0 0 0]; % KMSP
else
    testtrials = find([subjectdatatable{:,2}] == 19 | [subjectdatatable{:,2}] == 20);
    cols = [0 0 0 ; 0 0 0 ; 0 0 0 ; 0 0 0]; % KMSP
end

%% filter data

% future: flag trials where user did not change probabilities MSC

analyzetrials = testtrials;
numanalyzetrials = length(analyzetrials);
userchoicetrials = find(strcmp(subjectdatatable(:,Layer_type_idx),'User Choice'));
sequentialtrials = find(strcmp(subjectdatatable(:,Layer_type_idx),'Sequential'));

[scenetable, uniquescenes, numuniquescenes] = createscenetable(testscenes,testtrials,subjectdatatable,Layer_type_idx);

f_answers = [subjectdatatable{analyzetrials,Specified_answer_idx}];

f_DIVhuman = squeeze(divergencetable(hidx-basedivergenceidx+1,:,analyzetrials)); % subjects x trials
f_DIVrandom = squeeze(divergencetable(ridx-basedivergenceidx+1,1,analyzetrials))'; % 1 x trials
f_meanhuman = mean(f_DIVhuman,1); % mean over subjects; 1 x numtrials
f_medianhuman = median(f_DIVhuman,1); % median over subjects; 1 x numtrials
f_normativeprobs = normativeprobs(analyzetrials,:);
f_randomprobs = randomprobs(analyzetrials,:);

% %% trial-by-trial difficulty measurement
% f0 = maxfigure;
% idtesttrials = find([subjectdatatable{:,2}] == 19);
% idhuman = median(squeeze(divergencetable(hidx-basedivergenceidx+1,:,idtesttrials)),1);
% subplot(2,2,1); plot(idhuman,unscaledL1distance(idtesttrials),'o'); xlim([0 maxdiv]); ylabel('U L1'); xlabel(divergence);
% subplot(2,2,2); plot(idhuman,scaledL1distance(idtesttrials),'o'); xlim([0 maxdiv]); ylabel('S L1'); xlabel(divergence);
% subplot(2,2,3); plot(idhuman,unscaledL2distance(idtesttrials),'o'); xlim([0 maxdiv]); ylabel('U L2'); xlabel(divergence);
% subplot(2,2,4); plot(idhuman,scaledL2distance(idtesttrials),'o'); xlim([0 maxdiv]); ylabel('S L2'); xlabel(divergence);
% suptitle('Subject performance vs. query distance from category mean');
% expfig(f0,sprintf('results/distance_vs_%s',divergence),16);

%% Scatterplots for divergence analysis
fprintf('\nDivergence: %s\n',divergence);
nc =  5; nr = ceil(numsubjects/nc);
if ~isequal(divergence,'WTA')
    %% Mean scatterplot, averaged over subjects
    maxfigure;
    divscatterplot(f_meanhuman,f_DIVrandom,f_answers,targetanswers,cols,maxdiv,15,'Mean over subjects','results/mean_scatterplot_%s',divergence);

    %% Median scatterplot, averaged over subjects
    maxfigure;
    divscatterplot(f_medianhuman,f_DIVrandom,f_answers,targetanswers,cols,maxdiv,15,'Median over subjects','results/median_scatterplot_%s',divergence);

    %% demo scatterplot
    maxfigure;
    divscatterplot([zeros(size(f_DIVrandom)) f_DIVrandom repmat(maxdiv,size(f_DIVrandom))],[f_DIVrandom f_DIVrandom f_DIVrandom],[],[],[0 0 1],maxdiv,25,'','results/demo_scatterplot_%s',divergence);

    %% bootstrap over subjects: test significance of all-subjects median
    boot_meds = zeros(1,numbootstraps);
    includesubjects = (subjectnums ~= 11);
    for b = 1:numbootstraps
        boot_median_trial = zeros(1,numanalyzetrials);
        for t = 1:numanalyzetrials
            boot_median_trial(t) = median(samplewithreplacement(f_DIVhuman(includesubjects,t)));
        end
        boot_meds(b) = median(f_DIVrandom - boot_median_trial);
    end
    med = median(boot_meds);
    pvalue = getpercentile(0,sort(boot_meds))/100;
    fprintf('Median human advantage over all subjects (one-sided bootstrap), p = %f\n',pvalue);
    % maxfigure; hist(boot_meds,40); title('Bootstrapped all-trials median of human - random');

    %%
    f1 = maxfigure; f2 = maxfigure; 
    for s = 1:numsubjects
        if plotcolor, edges = -maxdiv:0.5:maxdiv; else edges = -maxdiv:0.25:maxdiv; end %#ok

        %% scatterplot
        figure(f1);
        subplot(nr,nc,s);
        divscatterplot(f_DIVhuman(s,:),f_DIVrandom,f_answers,targetanswers,cols,maxdiv,12,sprintf('S#%d',subjectnums(s)),'',divergence);
        xlabel('H');
        ylabel('R');

        %% histogram
        figure(f2);
        subplot(nr,nc,s);
        dist = f_DIVrandom - f_DIVhuman(s,:);
        n = histc(dist,edges);
        bar(edges,n,'histc');
        xlim([-maxdiv maxdiv]);
        ylim([0 max(n)+5]);
        title(sprintf('S#%d',subjectnums(s)));
        hold on;
        line([0 0],[0 max(n)+5],'Color',[0 0 0],'LineWidth',1.5);
        line([mean(dist) mean(dist)],[0 max(n)+5],'Color',[1 0 0],'LineWidth',1.5);
    end
    expfig(f1,sprintf('results/scatterplot_%s',divergence),'FontSize',10);
    expfig(f2,sprintf('results/histogram_%s',divergence),'FontSize',10);

    %% Boxplot
    f3 = maxfigure;
    dist = repmat(f_DIVrandom,[numsubjects 1]) - f_DIVhuman;
    dist = [dist ; mean(dist)];
    boxplot(dist','labels',[num2cellstring(subjectnums) 'Overall'],'plotstyle','compact');
    hold on;
    xlabel('Subject');
    ylabel('Difference from random');
    for s = 1:numsubjects
        plot(s,dist(s,:),'k.');
    end
    line([0 numsubjects+2],[0 0],'Color',[0 0 0],'LineWidth',1);
    expfig(f3,sprintf('results/boxplot_%s',divergence),'FontSize',10);

    %% Bootstrap over trials: test whether a particular subject's 
    % distribution is significant
    [significant, pvalue] = zerodata(1,numsubjects);
    for s = 1:numsubjects
        [~, pvalue(s)] = bootstrapmedian(f_DIVrandom - f_DIVhuman(s,:),numbootstraps);
        significant(s) = (pvalue(s) <= confidence_interval_fraction);
    end
    fprintf('Subjects with significant advantage (p < %0.2f), median over all trials, (one-sided bootstrap): ',confidence_interval_fraction);
    disp(subjectnums(find(significant)));

    %% Response probabilities scatterplot
    xbins = 0:0.1:1;
    f4 = maxfigure;
    for s = 1:numsubjects
        f_humanprobs = humanprobs(s,analyzetrials,:);

        subplot(2,numsubjects+1,s);
        plot(f_humanprobs(:),f_normativeprobs(:),'o');
        xlabel(sprintf('S#%d',subjectnums(s)));
        ylabel('Normative');
        xlim([0 1]);
        ylim([0 1]);
        axis square;
        hold on;
        line([0 0],[1 1],'Color',[0 0 0],'LineWidth',1.5);

        subplot(2,numsubjects+1,numsubjects+1+s);
        hist(f_humanprobs(:),xbins);
        xlim([0 1]);
        xlabel(sprintf('S#%d',subjectnums(s)));
        ylabel('#');
    end

    %     subplot(2,numsubjects+1,numsubjects+1);
    %     plot(f_randomprobs(:),f_normativeprobs(:),'o');
    %     xlabel('Naive');
    %     ylabel('Normative');
    %     xlim([0 1]);
    %     ylim([0 1]);
    %     axis square;
    %     hold on;
    %     line([0 0],[1 1],'Color',[0 0 0],'LineWidth',1.5);

    subplot(2,numsubjects+1,2*(numsubjects+1));
    hist(f_normativeprobs(:),xbins);
    xlim([0 1]);
    xlabel('Normative');
    ylabel('#');
    expfig(f4,sprintf('results/allprobs_%s',divergence),'FontSize',10);
end

%% peakedness of test trials
f5 = subfig(1,2,1);
plot(sort(f_normativeprobs','descend')); %#ok
ylabel('Probability');
xlabel('Bin rank (1 = most probable, 4 = least probable)');
set(gca,'XTick',[1 2 3 4]);
set(gca,'YTick',[0 0.5 1]);
ylim([0 1]);
title('Normative');
expfig(f5,sprintf('results/peakedness_normative'),'FontSize',24);

f6 = maxfigure;
for s = 1:numsubjects
    f_humanprobs = humanprobs(s,analyzetrials,:);
    subplot(nr,nc,s);
    plot(sort(squeeze(f_humanprobs)','descend')); %#ok
    set(gca,'XTick',[1 2 3 4]);
    set(gca,'YTick',[]);
    ylim([0 1]);
    title(sprintf('S#%d',subjectnums(s)));
end
expfig(f6,sprintf('results/peakedness_subjects'),'FontSize',16);

%%
normativeginis = ginicoeff(f_normativeprobs');
subjectginis = zeros(numsubjects,numanalyzetrials);
[medianginis, medianginis_nonzero] = zerodata(numsubjects,1);
for s = 1:numsubjects
    f_humanprobs = humanprobs(s,analyzetrials,:);
    sg = ginicoeff(squeeze(f_humanprobs)');
    
    subjectginis(s,:) = sg;
    medianginis(s) = median(sg);
    medianginis_nonzero(s) = median(sg(sg > 0));
end

f = maxfigure;
edges = 0:0.1:1; n = histc(medianginis,edges); g = bar(edges,n,'histc'); set(g,'FaceColor',[0.5 0.5 0.5]);
xlim([0 1]);
ylim([0 8]);
xlabel('Gini coefficient');
ylabel('# subjects');
line([median(normativeginis) median(normativeginis)],[0 8],'Color',[0 1 0],'LineStyle','--');
expfig(f,'results/ginis_subjects_hist','FontSize',16);

f = maxfigure;
edges = 0:0.1:1; n = histc(medianginis_nonzero,edges); g = bar(edges,n,'histc'); set(g,'FaceColor',[0.5 0.5 0.5]);
xlim([0 1]);
ylim([0 8]);
xlabel('Gini coefficient excluding zeros');
ylabel('# subjects');
line([median(normativeginis) median(normativeginis)],[0 8],'Color',[0 1 0],'LineStyle','--');
expfig(f,'results/ginis_subjects_nonzero_hist','FontSize',16);

f = maxfigure;
[~, sortorder] = sort(medianginis,'ascend');
sortedsubjectginis = subjectginis(sortorder,:)';
boxplot([sortedsubjectginis normativeginis'],'labels',[num2cellstring(subjectnums(sortorder)) 'Normative'],'plotstyle','compact');
ylabel('Median Gini coefficient');
xlabel('Subject');
expfig(f,'results/ginis_subjects_boxplot','FontSize',16);

f = maxfigure;
[~, sortorder] = sort(medianginis_nonzero,'ascend');
sortedsubjectginis = subjectginis(sortorder,:)';
sortedsubjectginis(sortedsubjectginis == 0) = NaN;
boxplot([sortedsubjectginis normativeginis'],'labels',[num2cellstring(subjectnums(sortorder)) 'Normative'],'plotstyle','compact');
ylabel('Median Gini coefficient excluding zeros');
xlabel('Subject');
expfig(f,'results/ginis_subjects_nonzero_boxplot','FontSize',16);

%% WTA analysis
if isequal(divergence,'WTA')
    [percentcorrect, percentsem] = zerodata(1,numsubjects);
    f7 = maxfigure;
    for s = 1:numsubjects
        p = mean(f_DIVhuman(s,:) == 0);
        percentcorrect(s) = 100 * p;
        % standard error of proportion is sqrt[P(1 - P)/n]
        percentsem(s) = 100 * sqrt(p * (1-p) / numanalyzetrials);
    end
    
    [h,p] = ttest(percentcorrect,25,confidence_interval_fraction,'right');
    fprintf('Subject advantage over random in correct facility choice (winner-take-all analysis, one-sided t-test on proportion): p < %s\n',displaypvalue(p));

    sortedbargraph(percentcorrect,percentsem,subjectnums,'descend','Winner-take-all',{25 0 'Naive' [1 0 0]});
    expfig(f7,'results/WTA','FontSize',16);
    
    %% Second probability analyses
    [secondWTAmean, secondWTAsem, thirdWTAmean, thirdWTAsem] = zerodata(1,numsubjects);
    for s = 1:numsubjects
        fWTA = firstWTA(s,analyzetrials);
        sWTA = secondWTA(s,analyzetrials);
        tWTA = thirdWTA(s,analyzetrials);
        
        sp = mean(sWTA(fWTA == 1));
        secondWTAmean(s) = sp * 100;
        secondWTAsem(s) = 100 * sqrt(sp * (1-sp) / sum(fWTA)); % standard error of proportion
        
        tp = mean(tWTA(sWTA == 1));
        thirdWTAmean(s) = tp * 100;
        thirdWTAsem(s) = 100 * sqrt(tp * (1-tp) / sum(sWTA)); % standard error of proportion
    end
    f = maxfigure;
    sortedbargraph(secondWTAmean,secondWTAsem,subjectnums,'descend','Second winner-take-all',{33 0 'Naive' [1 0 0]});
    expfig(f,'results/2WTA','FontSize',16);

    f = maxfigure;
    sortedbargraph(thirdWTAmean,thirdWTAsem,subjectnums,'descend','Third winner-take-all',{50 0 'Naive' [1 0 0]});
    expfig(f,'results/3WTA','FontSize',16);
    
    %% correlate first and second choice across subjects
    f = maxfigure;
    plot(percentcorrect,secondWTAmean,'.','MarkerSize',16);
    axis([0 100 0 100]); axis square;
    inliers = (percentcorrect < 75);
    [b, dev, stats] = glmfit(percentcorrect(inliers),secondWTAmean(inliers));
%     [b, ~, ~, ~, stats] = regress(percentcorrect(inliers)',[secondWTAmean(inliers)' ones(size(secondWTAmean(inliers)',1),1)]); % regress with linear component to get r^2
%     rsq = stats(1); r = sqrt(rsq);
    yhat = glmval(b,[min(percentcorrect) max(percentcorrect)],'identity');
    cc = corrcoef([secondWTAmean(inliers)' glmval(b,percentcorrect(inliers),'identity')]);
    r = cc(1,2);
    
    line([min(percentcorrect) max(percentcorrect)],yhat,'Color',[0 0 0]); 
    xlabel('First choice %');
    ylabel('Second choice % given correct first choice');
    expfig(f,'results/1vs2WTA','FontSize',16);
end

%% histogram of subject response probabilities for normatively correct bin
f8 = maxfigure;
hps = [];
for s = 1:numsubjects
    f_humanprobs = squeeze(humanprobs(s,analyzetrials,:));
    for i = 1:numanalyzetrials
        np = f_normativeprobs(i,:);
        [~, maxidx] = max(np);
        hps = [hps f_humanprobs(i,maxidx)]; %#ok
    end
end
hist(hps,0.0625:0.125:1-0.0625);
bax = get(gca,'Children');
set(bax,'FaceColor',[0.2 0.2 0.2]);
set(gca,'XTick',[]);
set(gca,'YTick',[]);
% xlabel('Observed subject judgments of correct facility');
expfig(f8,'results/correct_bin','FontSize',16);

%% user choice analyses
[subjectmeans, subjectsems] = zerodata(1,numsubjects);
for s = 1:numsubjects
    subjectchoices = userchoicetable(s,userchoicetrials);
    p = mean(subjectchoices == 3); %   1,2,3 for I,S,M

    subjectmeans(s) = 100 * p;
    subjectsems(s) = 100 * sqrt(p * (1-p) / length(userchoicetrials));
end
relativeinfoMASINT = 100 * relativeentropyMASgivenIM/(relativeentropyMASgivenIM + relativeentropySIGgivenIM);

f9 = maxfigure;
sortedbargraph(subjectmeans,subjectsems,subjectnums,'descend','Percent MASINT layer choices',{relativeinfoMASINT 0 'Relative' [1 0 0]});
hold on; line([0 numsubjects],[50 50],'Color',[0 0 0],'LineWidth',1,'LineStyle',':');
expfig(f9,'results/userchoice_M','FontSize',18);

%% sequential vs. user choice vs. simultaneous analyses
% how different are layer answers, and how similar to normative
layernormativeD = zeros(numsubjects,numuniquescenes,3); % Norm-Sim Norm-Seq Norm-UC
layertypeD = zeros(numsubjects,numuniquescenes,3); % (simult, seq), (simult, user choice), (seq, user choice)
for s = 1:numsubjects
    for sc = 1:numuniquescenes
        tridx = scenetable(sc,:);
        
        % We need to use different normative probabilities since the user
        % choice layer is different (only two layers vs. three for the
        % others)
        np1 = normativeprobs(tridx(1),:);
        np2 = normativeprobs(tridx(2),:);
        np3 = normativeprobs(tridx(3),:);
        assert(sum(abs(np1 - np2)) < 0.01); % Simult = Seq
        
        h1 = squeeze(humanprobs(s,tridx(1),:))'; 
        h2 = squeeze(humanprobs(s,tridx(2),:))'; 
        h3 = squeeze(humanprobs(s,tridx(3),:))';
        
        layernormativeD(s,sc,1) = feval(fdivergence,np1,h1,zerofactor);
        layernormativeD(s,sc,2) = feval(fdivergence,np2,h2,zerofactor);
        layernormativeD(s,sc,3) = feval(fdivergence,np3,h3,zerofactor);
        
        layertypeD(s,sc,1) = feval(fdivergence,h1,h2,zerofactor);
        layertypeD(s,sc,2) = feval(fdivergence,h1,h3,zerofactor);
        layertypeD(s,sc,3) = feval(fdivergence,h2,h3,zerofactor);
    end
end

f10 = maxfigure;
normdiff = squeeze(mean(layernormativeD,2));
typediff = squeeze(mean(layertypeD,2));
boxplot(normdiff,'labels',{'Simultaneous' 'Sequential' 'User Choice'});
ylabel(sprintf('Median subject %s from normative',divergence));
% boxplot([normdiff typediff],'labels',{'Norm-Sim' 'Norm-Seq' 'Norm-UC' 'Sim-Seq' 'Sim-UC' 'Seq-UC'});
expfig(f10,sprintf('results/sequential_comparisons_%s',divergence),16);

%% for sequential, plot Dkl(final normative answer, human sequential stage answer) vs. stage
layerstageD = zeros(numsubjects,length(sequentialtrials),3);
for s = 1:numsubjects
    for i = 1:length(sequentialtrials)
        for l = 1:3
            hp = squeeze(humanlayerprobs(s,sequentialtrials(i),l,:))';
            np = squeeze(normativeprobs(sequentialtrials(i),:));
            assert(~all(hp == 0)); 
            layerstageD(s,i,l) = feval(fdivergence,np,hp,zerofactor);
        end
    end
end

%% plot by each question
% ignoring layer reveal order for now
layerstagemedian = squeeze(median(layerstageD,1));
f11 = maxfigure;
nc = 7;
nr = ceil(length(sequentialtrials)/nc);
for i = 1:length(sequentialtrials)
    subplot(nr,nc,i);
    plot(1:3,layerstagemedian(i,:),'.-','MarkerSize',15);
    xlim([1 3]);
    ylim([0 1]);
    set(gca,'XTick',[]); % [1 2 3]);
    title(sprintf('Tr #%d',sequentialtrials(i)));
end    
% subtitle('Layer #');
expfig(f11,sprintf('results/sequential_trial_trends_%s',divergence),14);

%% compare peak normative probability vs. specified answer
specifiedanswers = [subjectdatatable{:,Specified_answer_idx}] - 'A' + 1;
[~, normativeanswers] = max(normativeprobs');
map = zeros(4,4);
for i = 1:length(specifiedanswers)
    map(specifiedanswers(i),normativeanswers(i)) = map(specifiedanswers(i),normativeanswers(i)) + 1;
end
figure;
show(map);
xlabel('Normative');
ylabel('Specified');
set(gca,'XTick',[1:4]);
set(gca,'YTick',[1:4]);
set(gca,'XTickLabel',{'A' 'B' 'C' 'D'});
set(gca,'YTickLabel',{'A' 'B' 'C' 'D'});
set(gca,'YDir','normal');
axis on;
title('Specified answer - normative answer correlation');
fprintf('Specified and normative same: %d. Different: %d.\n',sum(diag(map)),sum(map(:)) - sum(diag(map)));

% %%
% for i = 1:25
%     qtype = examfile.Set(19).Test.QuestionType{i}{3};
%     scene = examfile.Set(19).Test.QuestionType{i}{end};
%     scenecorr = testscenes(i); 
%     fv = examfile.Set(19).Test.FVID(i);
%     fprintf('%20s\t%d\t%d\t%d\n',qtype,fv,scene,scenecorr);
% end

% % peak only analysis
% f_DIVpeakonly = peakonlytable(:,analyzetrials);
% f_medianpeakonly = median(f_DIVpeakonly,1);
% maxfigure;
% divscatterplot(f_medianhuman,f_medianpeakonly,f_answers,targetanswers,cols,maxdiv,15,'Median over subjects','results/peakonly_scatterplot_%s',divergence);
% 
% % friedman's rank analysis
% for s = 1:numsubjects
%     p = median(friedp(s,analyzetrials));
%     fprintf('Subject %d Friedman rank test median %.4f\n',subjectnums(s),p);
% end
% 
%% sequential WTA analysis
% maxfigure;
% subjectwtatable = zeros(numsubjects,max_probs_in_set);
% for s = 1:numsubjects
%     wtatable = squeeze(sequentialWTAtable(:,s,analyzetrials));
%     subjectwtatable(s,:) = mean(wtatable,2);
%     plot(1:max_probs_in_set,mean(wtatable,2),'b.-','MarkerSize',8); hold on;
% end
% boxplot(subjectwtatable,'labels',1:max_probs_in_set);
% hold on; plot(1:max_probs_in_set,(1:max_probs_in_set)*1/max_probs_in_set,'g+');
% xlim([0 max_probs_in_set]);
% ylim([0 1]);

