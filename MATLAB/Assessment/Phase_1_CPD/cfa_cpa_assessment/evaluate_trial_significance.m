function [data, probsComparison, neComparison, pmComparison, csComparison] = evaluate_trial_significance(subject_data, tasks, displayText)
%EVALUATE_TRIAL_SIGNIFICANCE Evaluate signifinace of CPA and CFA measures for each stage of each trial
%of each task in the exam.
%   Detailed explanation goes here

%Compute metrics for exam data set
data = compute_exam_metrics(subject_data, tasks);
numTasks = max(tasks);

%Compute delta negentropy for tasks with more than 1 stage
subjectDeltaNe = cell(numTasks, 1); %Delta Negentropy of subject probabilities for each stage
subjectDeltaNeAvg = cell(numTasks, 1); %Delta Negentropy of average subject probabilities for each stage
subjectDeltaNeErr = cell(numTasks, 1); %STD of delta subject negentropy
normativeDeltaNe = cell(numTasks, 1); %Delta negentropy of normative probabilities for each stage
normativeDeltaNeAvg = cell(numTasks, 1); %Delta negentropy of average normative probabilities for each stage
normativeDeltaNeErr = cell(numTasks, 1); %STD of delta normative negentropy (should be 0 for all Tasks except Task 6)
for task = 1:numTasks    
    numTrials = size(data.subjectProbs{task}, 1);
    numStages = size(data.subjectProbs{task}, 2);
    if numStages > 1        
        subjectDeltaNe{task} = cell(numTrials, numStages);
        subjectDeltaNeAvg{task} = zeros(numTrials, numStages);
        subjectDeltaNeErr{task} = zeros(numTrials, numStages);
        normativeDeltaNe{task} = cell(numTrials, numStages);
        normativeDeltaNeAvg{task} = zeros(numTrials, numStages);
        normativeDeltaNeErr{task} = zeros(numTrials, numStages);        
        
        subjectNe = data.subjectNe{task};
        subjectNeAvg = data.subjectNeAvg{task};
        normativeNe = data.normativeNe{task};
        normativeNeAvg = data.normativeNeAvg{task};
        numSubjects = data.numSubjects(task);
        for trial = 1:numTrials
            for stage = 2:numStages
                %Compute delta Ne for subject probs using the subject Ne distribution                
                %subjectDeltaNe{task}{trial, stage} = abs(subjectNe{trial, stage}-subjectNe{trial, stage-1});
                subjectDeltaNe{task}{trial, stage} = subjectNe{trial, stage}-subjectNe{trial, stage-1};
                
                %Compute delta Ne for subject probs using Ne computed from avg subject probs
                %subjectDeltaNeAvg{task}(trial, stage) = abs(subjectNeAvg(trial, stage)-subjectNeAvg(trial, stage-1));                
                subjectDeltaNeAvg{task}(trial, stage) = subjectNeAvg(trial, stage)-subjectNeAvg(trial, stage-1);                
                subjectDeltaNeErr{task}(trial, stage) = sqrt(sum((subjectDeltaNe{task}{trial, stage} - subjectDeltaNeAvg{task}(trial, stage)).^2 )/(numSubjects-1));
                
                %Compute delta Ne for normative probs using the normative Ne distribution 
                %normativeDeltaNe{task}{trial, stage} = abs(normativeNe{trial, stage}-normativeNe{trial, stage-1});
                normativeDeltaNe{task}{trial, stage} = normativeNe{trial, stage}-normativeNe{trial, stage-1};
                
                %Compute delta Ne for normative probs using Ne computed from avg normative probs
                %normativeDeltaNeAvg{task}(trial, stage) = abs(normativeNeAvg(trial, stage)-normativeNeAvg(trial, stage-1));
                normativeDeltaNeAvg{task}(trial, stage) = normativeNeAvg(trial, stage)-normativeNeAvg(trial, stage-1);
                normativeDeltaNeErr{task}(trial, stage) = sqrt(sum((normativeDeltaNe{task}{trial, stage} - normativeDeltaNeAvg{task}(trial, stage)).^2 )/(numSubjects-1));
            end
            %DEBUG CODE
            debug = false;
            if debug
                disp(['Trial ' num2str(trial)]); %#ok<UNRCH>
                %subjectDeltaNe{task}{trial, 2}
                meanNe = zeros(numStages,1);
                meanDeltaNe = zeros(numStages, 1);
                for j = 1:numStages
                    meanNe(j) = sum(subjectNe{trial, j})/length(subjectNe{trial, j});
                    if j > 1
                        meanDeltaNe(j) = sum(subjectDeltaNe{task}{trial, j})/length(subjectDeltaNe{task}{trial, j});
                    end
                end
                disp(num2str(meanNe'));
                disp(num2str(meanDeltaNe'));
                %disp(num2str(subjectNeAvg(trial, :)));
                %figure; title(['Trial ' num2str(trial)]); hold on; ylim([0 1]); plot(1:numStages, meanNe);
            end
            %END DEBUG CODE
        end
    end
end
data.subjectDeltaNe = subjectDeltaNe;
data.subjectDeltaNeAvg = subjectDeltaNeAvg;
data.subjectDeltaNeErr = subjectDeltaNeErr;
data.normativeDeltaNe = normativeDeltaNe;
data.normativeDeltaNeAvg = normativeDeltaNeAvg;
data.normativeDeltaNeErr = normativeDeltaNeErr;

%For each stage of each trial, perform a chi^2 test to determine whether
%probs differ significantly from uniform probs. Also perform a t-test to
%determine whether negentropy differs significantly from Bayesian
%negentropy. Also perform t-tests to evaluate the significance of biases
%computed for the trial.
neComparison = cell(numTasks, 1);
probsComparison = cell(numTasks, 1);
pmComparison = cell(numTasks, 1);
csComparison = cell(numTasks, 1);
for task = tasks
    n = data.numSubjects(task); %Number of subjects for the current task
    
    numTrials = size(data.subjectProbsAvg{task}, 1);
    numStages = size(data.subjectProbsAvg{task}, 2);
    numProbs = size(data.subjectProbs{task}{1, 1}, 2);
    uniformProbs = repmat(1/numProbs, 1, numProbs) * 100;
    
    neComparison{task} = cell(numTrials, numStages);
    probsComparison{task} = cell(numTrials, numStages);
    
    if task <= 3
        %Perform t-test to evaluate significance of probability matching
        %for Tasks 1-3
        mean_1 = data.fh_ph_avg(task);
        mean_2 = data.fh_1_avg(task);
        s1 = data.fh_ph_err(task);
        s2 = data.fh_1_err(task);
        if displayText
            disp('  ');
            disp('-----------------------------');
            disp(['Task: ', num2str(task), ', Probability Matching (All Trials)']);
        end
        
        %Peform independent two sample t-test, one-tailed (test |Fh-Ph| > |Fh-1|)
        df = n * 2 - 2;
        t = (mean_2-mean_1)/(sqrt(((n-1)*s1^2 + (n-1)*s2^2)/(n * 2 - 1)) * sqrt(1/n + 1/n));
        pmComparison{task}.p = (1 - tcdf(t, df));
        if displayText
            disp(['|Fh-Ph| = ', num2str(mean_1), ', |Fh-1| = ', num2str(mean_2), ', p(|Fh-Ph| >= |Fh-1|) = ', num2str(pmComparison{task}.p)]);
            if mean_1 >= mean_2
                %Bias not observed
                disp('PM not observed (|Fh-Ph| >= |Fh-1|');
            end
        end
    end
    
    if task == 6
        %Perform t-test to evaluate significance of confirmation bias (seeking) for Task 6
        mean_1 = data.c_avg(task);        
        mean_2 = 50;
        s = data.c_err(task);
        if displayText
            disp('  ');
            disp('-----------------------------');
            disp(['Task: ', num2str(task), ', Confirmation Preference (Seeking) (All Trials)']);
        end
        
        %Perform one-sample t test, one-tailed (test C > 50)
        df = n - 1;
        t = (mean_1-mean_2)/(s/sqrt(n));
        %t = abs((mean_1-mean_2)/(sqrt(((n-1)*s1^2 + (n-1)*s2^2)/(n * 2 - 1)) * sqrt(1/n + 1/n)));
        csComparison{task}.p = (1 - tcdf(t, df));
        if displayText
            disp(['C = ', num2str(mean_1), ', p (C <= 50%) = ', num2str(csComparison{task}.p)]);
            if mean_1 <= mean_2
                %Bias not observed
                disp('CS not observed (C <= 50%)');
            end
        end
    end
    
    for trial = 1:numTrials
        if task >= 4 && task <=6
            %Perform t-test evaluating significance of probability matching
            %for Tasks 4-6
            mean_1 = data.rms_f_p_avg{task}(trial);
            mean_2 = data.rms_f_i_avg{task}(trial);
            if displayText
                disp('  ');
                disp('-----------------------------');
                disp(['Task: ', num2str(task), ', Trial: ', num2str(trial), ', Probability Matching']);
            end
            
            %Perform independent two-sample t-test, one-tailed
            %(test RMS_F_I > RMS_F_P)
            df = n * 2 - 2;
            s1 = data.rms_f_p_err{task}(trial);
            s2 = data.rms_f_i_err{task}(trial);
            t = (mean_2-mean_1)/(sqrt(((n-1)*s1^2 + (n-1)*s2^2)/(n * 2 - 1)) * sqrt(1/n + 1/n));
            %pmComparison{task}.p = (1 - tcdf(t, df)) * 2;
            pmComparison{task}.p = (1 - tcdf(t, df));
            if displayText
                disp(['RMS(F-P) = ', num2str(mean_1), ', RMS(F-I) = ', num2str(mean_2), ', p(RMS(F-P) >= RMS(F-I)) = ', num2str(pmComparison{task}.p)]);
                if mean_1 >= mean_2
                    disp('PM not observed (RMS(F-P) >= RMS(F-I)');
                end
            end
        end
        
        for stage = 1:numStages
            if displayText
                disp('  ');
                disp('-----------------------------');
                disp(['Task: ', num2str(task), ', Trial: ', num2str(trial), ', Stage: ', num2str(stage)]);
            end
            
            %Peform chi^2 goodness of fit test to determine whether average subject probs
            %differ significantly from uniform probs
            observed = data.subjectProbsAvg{task}{trial, stage} * 100;
            [~, ~, ~, p] = chi2gof(observed, uniformProbs, 0.05, false);
            %[~, ~, ~, p] = chi2test(chi2_data, 0.05, false);
            probsComparison{task}{trial, stage}.p_chi2_probs_to_random = p;
            [~, ~, ~, p_yates] = chi2gof(observed, uniformProbs, 0.05, true);
            %[~, ~, ~, p_yates] = chi2test(chi2_data, 0.05, true);
            probsComparison{task}{trial, stage}.p_chi2_yates_probs_to_random = p_yates;
            probsComparison{task}{trial, stage}.p_fisher_probs_to_random = FisherExactTest([round(observed); uniformProbs]);
            if displayText
                disp(['Probs: ', num2str(observed)]);
                disp(['p (chi^2 comparing probs to random) = ', num2str(probsComparison{task}{trial, stage}.p_chi2_probs_to_random)]);
                disp(['p (chi^2 with Yates correction comparing probs to random) = ', num2str(probsComparison{task}{trial, stage}.p_chi2_yates_probs_to_random)]);
                disp(['p (Fisher Exact Test comparing probs to random) = ', num2str(probsComparison{task}{trial, stage}.p_fisher_probs_to_random)]);
                disp('  ');
            end
            
            %Perform t-test to determine whether subject negentropy computed from
            %average subject probs differs significantly from Bayesian ne
            %(p = probability of null hypothesis that there is no difference)
            if stage == 1
                ne_mean_1 = data.subjectNeAvg{task}(trial, stage);
                ne_mean_2 = data.normativeNeAvg{task}(trial, stage);
                s1 = data.subjectNeErr{task}(trial, stage);
                s2 = data.normativeNeErr{task}(trial, stage);
            else
                %Use delta Ne for stages > 1
                ne_mean_1 = data.subjectDeltaNeAvg{task}(trial, stage);
                ne_mean_2 = data.normativeDeltaNeAvg{task}(trial, stage);
                s1 = data.subjectDeltaNeErr{task}(trial, stage);
                s2 = data.normativeDeltaNeErr{task}(trial, stage);
            end
            % First Determine if signs differ (e.g., if humans went in a different direction from Bayesian)
            if sign(ne_mean_1) ~= sign(ne_mean_2)
                %Signs differ, do not do significance test
                neComparison{task}{trial, stage}.p_avg_probs = 1;
                if displayText
                    if stage == 1
                        disp(['Avg ne from avg probs = ', num2str(ne_mean_1), ', Bayesian ne = ', num2str(ne_mean_2)]);
                    else
                        disp(['Delta ne from avg probs = ', num2str(ne_mean_1), ', Bayesian delta ne = ', num2str(ne_mean_2)]);
                    end
                    disp('Signs differ, bias not observed');
                end
            else
                ne_mean_1 = abs(ne_mean_1);
                ne_mean_2 = abs(ne_mean_2);
                if task < 6
                    %Perform one sample t-test for Tasks 4-6,
                    %one-tailed (test human ne or delta ne < normative ne or delta ne)
                    df = n - 1;
                    t = (ne_mean_2-ne_mean_1)/(s1/sqrt(n));
                    %df = n * 2 - 2;
                    %t = abs((ne_mean_1-ne_mean_2)/(sqrt(((n-1)*s1^2 + (n-1)*s2^2)/(n * 2 - 1)) * sqrt(1/n + 1/n)));
                    %neComparison{task}{trial, stage}.p_avg_probs = (1 - tcdf(t, df)) * 2;
                    neComparison{task}{trial, stage}.p_avg_probs = (1 - tcdf(t, df));
                else
                    %Perform independent two sample t-test for Task 6,
                    %one-tailed (test human ne or delta ne < normative ne or delta ne)
                    df = n * 2 - 2;
                    t = (ne_mean_2-ne_mean_1)/(sqrt(((n-1)*s1^2 + (n-1)*s2^2)/(n * 2 - 1)) * sqrt(1/n + 1/n));
                    %neComparison{task}{trial, stage}.p_avg_probs = (1 - tcdf(t, df)) * 2;
                    neComparison{task}{trial, stage}.p_avg_probs = (1 - tcdf(t, df));
                end
                
                if displayText
                    if stage == 1
                        disp(['Avg ne from avg probs = ', num2str(ne_mean_1), ', Bayesian ne = ', num2str(ne_mean_2)]);
                        disp(['p (Ne from avg human probs < Bayesian Ne) = ', num2str(neComparison{task}{trial, stage}.p_avg_probs)]);
                    else
                        disp(['Delta ne from avg probs = ', num2str(ne_mean_1), ', Bayesian delta ne = ', num2str(ne_mean_2)]);
                        disp(['p (delta Ne from avg human probs < Bayesian delta Ne) = ', num2str(neComparison{task}{trial, stage}.p_avg_probs)]);
                    end
                    if ne_mean_1 >= ne_mean_2
                        %Conservative bias (RR or AA) not observed
                        disp('Conservative bias (RR or AA) not observed, Human Ne >= Bayesian Ne');
                    end
                end
            end
            
            %Perform t-test to determine whether subject negentropies computed from
            %the subject negentropy distribution differ significantly from Bayesian ne
            %(p = probability of null hypothesis that there is no difference)
            test_ne_dist = false;
            if test_ne_dist
                if stage == 1 %#ok<UNRCH>
                    ne_mean_1 = sum(data.subjectNe{task}{trial, stage})/data.numSubjects(task);
                    ne_mean_2 = sum(data.normativeNe{task}{trial, stage})/data.numSubjects(task);
                    s1 = std(data.subjectNe{task}{trial, stage});
                    s2 = std(data.normativeNe{task}{trial, stage});
                else
                    %Use delta Ne for stages > 1
                    ne_mean_1 = abs(sum(data.subjectDeltaNe{task}{trial, stage})/data.numSubjects(task));
                    ne_mean_2 = abs(sum(data.normativeDeltaNe{task}{trial, stage})/data.numSubjects(task));
                    s1 = std(data.subjectDeltaNe{task}{trial, stage});
                    s2 = std(data.normativeDeltaNe{task}{trial, stage});
                end
                df = n * 2 - 2;
                t = abs((ne_mean_1-ne_mean_2)/(sqrt(((n-1)*s1^2 + (n-1)*s2^2)/(n * 2 - 1)) * sqrt(1/n + 1/n)));
                neComparison{task}{trial, stage}.p_ne_dist = (1 - tcdf(t, df)) * 2;
                if displayText
                    if stage == 1
                        disp(['Avg ne from ne distribution = ', num2str(ne_mean_1), ', Bayesian ne = ', num2str(ne_mean_2)]);
                        disp(['p (Avg ne from ne distribution compared to Bayesian ne) = ', num2str(neComparison{task}{trial, stage}.p_ne_dist)]);
                    else
                        disp(['Avg delta ne from ne distribution = ', num2str(ne_mean_1), ', Bayesian delta ne = ', num2str(ne_mean_2)]);
                        disp(['p (Avg delta ne from ne distribution compared to Bayesian delta ne) = ', num2str(neComparison{task}{trial, stage}.p_ne_dist)]);
                    end
                end
            end
        end
    end
end

end