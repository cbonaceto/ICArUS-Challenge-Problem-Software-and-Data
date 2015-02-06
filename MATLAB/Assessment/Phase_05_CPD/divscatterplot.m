function divscatterplot(f_human,f_random,f_answers,targetanswers,cols,maxdiv,...
    markersize,figuretitle,filename,divergence)

if ~isempty(targetanswers)
    % MSC color coding currently only works on identify trials; to apply to locate
    % trials, need to load data/KEY_4Fac.xlsx
    for a = 1:length(targetanswers) % plot A, B, C, D
        idx = find(f_answers == targetanswers(a));
        plot(f_human(idx),f_random(idx),'.','MarkerSize',markersize,'Color',cols(a,:));
        hold on; 
    end
else
    plot(f_human,f_random,'.','MarkerSize',15,'Color',cols);
end
    
xlabel(sprintf('Human %s from normative',divergence));
ylabel(sprintf('Random %s from normative',divergence));

axis square;
line([0 maxdiv],[0 maxdiv],'Color',[0 0 0],'LineStyle',':');
xlim([0 maxdiv]); ylim([0 maxdiv]);

if ~isempty(figuretitle)
    title(figuretitle);
end

if ~isempty(filename)
    expfig(gcf,sprintf(filename,divergence),'FontSize',18);
end
