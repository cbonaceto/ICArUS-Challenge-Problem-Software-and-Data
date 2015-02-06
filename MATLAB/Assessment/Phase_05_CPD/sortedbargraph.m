function sortedbargraph(percentcorrect,percentsem,subjectnums,sortorder,title,extrabar)
% sort by percent correct

numsubjects = length(percentcorrect);

[~, sortidx] = sort(percentcorrect,sortorder);

meanvalues = [percentcorrect(sortidx) mean(percentcorrect)];
semvalues = [percentsem(sortidx) std(percentcorrect)/sqrt(numsubjects)];
barlabels = [num2cellstring(subjectnums(sortidx)) 'Mean'];
cmap = jet; cmap = [cmap(1:numsubjects,:) ; 0 0 0];

if exist('extrabar','var') && ~isempty(extrabar)
    meanvalues = [meanvalues extrabar{1}];
    semvalues = [semvalues extrabar{2}];
    barlabels = [barlabels extrabar{3}];
    cmap = [cmap ; extrabar{4}];
    line([0 length(meanvalues)],[extrabar{1} extrabar{1}],'Color',extrabar{4},'LineWidth',1,'LineStyle','-');
    hold on;
end

barweb(meanvalues, semvalues, 0.85, [], title, '', '% Correct', ...
    cmap, 'none', barlabels, 2, 'axis');
ylim([0 100]);

% bar([1:numsubjects numsubjects+2 numsubjects+4],yvalues,'BarWidth',0.7,'FaceColor',[0.2 0.2 0.2]);
% set(gca,'XTickLabel',barlabels);
% xlim([0 14]);
% xlabel('Subject');
% ylabel('%');
% title(sprintf('Winner-take-all',subjectnums(s)));
