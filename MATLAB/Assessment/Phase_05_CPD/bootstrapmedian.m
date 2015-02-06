function [med, pvalue] = bootstrapmedian(original_dist,numbootstraps)
        
boot_meds = zeros(1,numbootstraps);
for b = 1:numbootstraps
    boot_meds(b) = median(samplewithreplacement(original_dist));
end

sorted_boot_meds = sort(boot_meds);

% one-sided test on bootstrap median
med = median(boot_meds);
pvalue = getpercentile(0,sorted_boot_meds)/100;

% original_med = median(original_dist);
% a = sorted_boot_meds(numbootstraps * cipercent/100 + 1);
% b = sorted_boot_meds(numbootstraps * (100 - cipercent)/100 + 1);
% 
% bins = -maxdiv/3:maxdiv/30:maxdiv/3;
% 
% figure;
% subplot(1,2,1);
% boot_hist = hist(boot_meds,bins)/numbootstraps;
% bar(bins,boot_hist);
% line([0 0],[0 1],'Color',[0 0 1],'LineWidth',2);
% line([m m],[0 1],'Color',[1 0 0],'LineWidth',2,'LineStyle','-');
% line([a a],[0 1],'Color',[1 0 0],'LineWidth',1,'LineStyle',':');
% line([b b],[0 1],'Color',[1 0 0],'LineWidth',1,'LineStyle',':');
% ylim([0 0.4]);
% xlim([min(bins) max(bins)]);
% title('Bootstrapped median distribution');
% 
% subplot(1,2,2);
% orig_hist = hist(original_dist,bins)/length(original_dist); 
% bar(bins,orig_hist);
% line([0 0],[0 1],'Color',[0 0 1],'LineWidth',2);
% line([original_med original_med],[0 1],'Color',[1 0 0],'LineWidth',2,'LineStyle','-');
% line([original_med-(m-a) original_med-(m-a)],[0 1],'Color',[1 0 0],'LineWidth',1,'LineStyle',':');
% line([original_med+(b-m) original_med+(b-m)],[0 1],'Color',[1 0 0],'LineWidth',1,'LineStyle',':');
% ylim([0 0.4]);
% xlim([min(bins) max(bins)]);
% suptitle(sprintf('S#%d',subjectnums(s)));
% title('Original distribution');
