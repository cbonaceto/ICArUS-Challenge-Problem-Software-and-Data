%Create all plots for Pilot Study 1 (Missions 1-3, N=20)
create_all_metrics_plots([1,2,3], true, true, true, true, true, true, false, true, true, 'data\Sample-Exam-1\Human_Data', 'figures\Sample-Exam-1', true, false);

%Create all plots for Pilot Study 2 (Missions 1-5, N=30)
create_all_metrics_plots([1,2,3,4,5], true, true, true, true, true, true, false, true, true, 'data\Sample-Exam-2\Human_Data', 'figures\Sample-Exam-2', true, false);

%Create individual differences plots for combined Pilot Study 1 and Pilot Study 2 data sets
create_all_metrics_plots([1,2,3], false, false, false, false, false, false, true, false, false, 'data\Sample-Exam-1-2-Combined\Human_Data', 'figures\Sample-Exam-1-2-Combined', true, false);

%Create individual differences plots for the Final Exam
create_all_metrics_plots(1:5, false, false, false, false, false, false, true, false, false, 'data\Final-Exam-1\Human_Data', 'figures\Final-Exam-1', true, false);

%Create all plots for the Phase 2 Final Exam
create_all_metrics_plots([1,2,3,4,5], true, true, true, true, true, true, false, false, true, 'data\Final-Exam-1\Human_Data', 'figures\Final-Exam-1', true, false);