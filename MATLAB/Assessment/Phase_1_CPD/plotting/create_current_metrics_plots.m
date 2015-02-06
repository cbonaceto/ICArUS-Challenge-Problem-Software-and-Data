function create_current_metrics_plots(tasks, saveData, showPlots)
%CREATE_CURRENT_METRICS_PLOTS Create plots for the current set of metrics
%being looked at

%STANDARD CALL
create_all_metrics_plots(tasks, true, true, false, false, true, true, false, true,...
   'Final Exam August 2012', saveData, showPlots);

%DEBUG CALL
%create_all_metrics_plots(tasks, true, false, false, false, false, false, false, false,...
%   'Final Exam August 2012', saveData, showPlots);

end

