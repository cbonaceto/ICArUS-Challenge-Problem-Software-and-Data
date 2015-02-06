function create_current_metrics_plots(tasks, saveData, showPlots)
%CREATE_CURRENT_METRICS_PLOTS Create plots for the current set of metrics
%being looked at

%STANDARD CALL
create_all_metrics_plots(tasks, true, true, true, true, true, true, false, false, true,...
    'data\Final-Exam-1\Human_Data', 'figures\Final-Exam-1', saveData, showPlots);

%DEBUG CALL
%create_all_metrics_plots(tasks, true, true, true, true, true, true, false, false, true,...
%    'data\Final-Exam-1\Human_Data', 'figures\Final-Exam-1', saveData, showPlots);

end

