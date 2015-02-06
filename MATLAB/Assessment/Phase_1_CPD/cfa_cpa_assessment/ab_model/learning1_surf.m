% learning1_surf.m
clear

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Code to analyze data from AHA Task 1.
% The "cumulative" (actual) attack data are used to check normative solutions from Craig.
% The model is a two-parameter discounting of the "generative" prior (Baserate) * likelihood (Gaussian).
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%%%%%%%%%%%%%%
% Read-in Data
%%%%%%%%%%%%%%

%exam = 'March 2012 Pilot';
exam = 'August 2012 Final';
% Attacks
['data\KevinB\',exam,'\Normative Data\Task_1_Attacks.csv']
data = csvread(['data\KevinB\',exam,'\Normative Data\Task_1_Attacks.csv']); % Format is group that attacked (A = 1, B = 2), x-location, y-location
% Posteriors (Subjective)
Posts_1 = read_probs_file(['data\KevinB\',exam,'\Subject Data\Task_1_Avg_Subject_Probabilities.csv']);
% Posteriors (Normative)
Norms_1 = read_probs_file(['data\KevinB\',exam,'\Normative Data\Task_1_Normative_Probabilities.csv']);


% With two hypotheses...
% Random (uninformative) distribution
R = [0.5, 0.5];
% Maximum entropy
E_max = 1;

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Compute "actual" normative solutions for trials of Task 1,
% based on cumulative attack data up to each trial.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

An_tot = 0;
Ax_tot = 0;
Ay_tot = 0;
Bn_tot = 0;
Bx_tot = 0;
By_tot = 0;
Ax_data = [];
Ay_data = [];
A_baserates = [];
B_baserates = [];
Ax_centers = [];
Ay_centers = [];
Bx_data = [];
By_data = [];
Bx_centers = [];
By_centers = [];

row = 1;
for t = 1:10 % 10 trials
    attacks = 10;
    if t == 1
        attacks = 9;
    end
    for i = 1:attacks
        % Update totals and arrays after each trial 
        if data(row,1) == 1
            An_tot = An_tot + 1;
            Ax_tot = Ax_tot + data(row,2);
            Ay_tot = Ay_tot + data(row,3);
            Ax_data = [Ax_data; data(row,2)];
            Ay_data = [Ay_data; data(row,3)];
        else
            Bn_tot = Bn_tot + 1;
            Bx_tot = Bx_tot + data(row,2);
            By_tot = By_tot + data(row,3);
            Bx_data = [Bx_data; data(row,2)];
            By_data = [By_data; data(row,3)];
        end
        row = row + 1; 
    end
    % Compute base rates, centers, and sigma after each block
    A_base = An_tot / (An_tot + Bn_tot);
    B_base = Bn_tot / (An_tot + Bn_tot);
    A_baserates = [A_baserates; A_base];
    B_baserates = [B_baserates; B_base];
    Ax_cent = Ax_tot/An_tot;
    Ay_cent = Ay_tot/An_tot;
    Bx_cent = Bx_tot/Bn_tot;
    By_cent = By_tot/Bn_tot;
    Ax_centers = [Ax_centers; Ax_cent];
    Ay_centers = [Ay_centers; Ay_cent];
    Bx_centers = [Bx_centers; Bx_cent];
    By_centers = [By_centers; By_cent];
    
    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    % Group A
    for r = 1:size(Ax_data,1)
        A_dist(r,1) = sqrt( (Ax_data(r,1) - Ax_centers(t))^2 + (Ay_data(r,1) - Ay_centers(t))^2 );
    end
    A_sigma(t) = sqrt( sum(A_dist.^2)/size(Ax_data,1) );
    A_dist = sort(A_dist); % sort distances in ascending order
    Ai_2to1 = round(0.67 * size(A_dist,1));
    A_2to1(t) = A_dist(Ai_2to1); % Find distance that is 2/3 of the way from min to max
    A2to1_sigma(t) = A_2to1(t) / 1.33;
    % NOTE: sigma is ~1:1 distance, and two-to-one (2:1) distance should be ~1.33 sigma; 9:1 should be ~2 sigma
    %Ai_1to1 = round(0.46 * size(A_dist,1));
    %A_sigma(t,1) = A_dist(Ai_1to1,1);
    %Ai_9to1 = round(0.90 * size(A_dist,1));
    %A_9to1(t,1) = A_dist(Ai_9to1,1);
    
    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    % Group A
    % Compute sigma_x and sigma_y, then average 
    for r = 1:size(Ax_data,1); % r is the number of attacks up to trial t
        Ax_distance_squared(r,1) = (Ax_data(r,1) - Ax_centers(t)) ^ 2;
        Ay_distance_squared(r,1) = (Ay_data(r,1) - Ay_centers(t)) ^ 2;
    end
    Ax_sigma(t) = sqrt( sum(Ax_distance_squared)/size(Ax_data,1) );
    Ay_sigma(t) = sqrt( sum(Ay_distance_squared)/size(Ay_data,1) );
    Axy_sigma(t) = (Ax_sigma(t) + Ay_sigma(t)) / 2;
    
    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    % Group B
    for r = 1:size(Bx_data,1)
        B_dist(r,1) = sqrt( (Bx_data(r,1) - Bx_centers(t))^2 + (By_data(r,1) - By_centers(t))^2 );
    end
    B_sigma(t) = sqrt( sum(B_dist.^2)/size(Bx_data,1) );
    B_dist = sort(B_dist); % sort distances in ascending order
    Bi_2to1 = round(0.67 * size(B_dist,1));
    B_2to1(t) = B_dist(Bi_2to1); % Find distance that is 2/3 of the way from min to max
    B2to1_sigma(t) = B_2to1(t) / 1.33;
    % NOTE: sigma is ~1:1 distance, and two-to-one (2:1) distance should be ~1.33 sigma; 9:1 should be ~2 sigma
    %Bi_1to1 = round(0.50 * size(B_dist,1));
    %B_sigma(t,1) = B_dist(Bi_1to1,1);
    %Bi_9to1 = round(0.90 * size(B_dist,1));
    %B_9to1(t,1) = B_dist(Bi_9to1,1);
    
    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    % Group B
    %Compute sigma_x and sigma_y, then average 
    for r = 1:size(Bx_data,1); % r is the number of attacks
        Bx_distance_squared(r,1) = (Bx_data(r,1) - Bx_centers(t)) ^ 2;
        By_distance_squared(r,1) = (By_data(r,1) - By_centers(t)) ^ 2;
    end
    Bx_sigma(t) = sqrt( sum(Bx_distance_squared)/size(Bx_data,1) );
    By_sigma(t) = sqrt( sum(By_distance_squared)/size(By_data,1) );
    Bxy_sigma(t) = (Bx_sigma(t) + By_sigma(t)) / 2;
    
end 

% Compute Gaussian Likelihoods at each block
Gauss = [];
for t = 1:10
    row = t*10; % row in data for "probe" attack (i.e., 10, 20, ..., 100)
    
    % Group A
    % 2-D Gaussian with average sigma for sigma_x and sigma_y 
    A_s = Axy_sigma(t);
    A_d = sqrt( (data(row,2) - Ax_centers(t))^2 + (data(row,3) - Ay_centers(t))^2 );
    A_c = 1/(2*pi*A_s^2);
    P_A = A_c .* exp(-(A_d.^2) ./ (2*A_s^2));
    % 2-D Gaussian with different sigma for sigma_x and sigma_y
    %Ax_s = Ax_sigma(t);
    %Ay_s = Ay_sigma(t);
    %A_c = 1/(2*pi*Ax_s*Ay_s);
    %A_d = ( (data(row,2) - Ax_centers(t))^2 / (2 * (Ax_s ^2)) ) + ( (data(row,3) - Ay_centers(t))^2 / (2 * (Ay_s ^2)) );
    %P_A = A_c * exp(-A_d);
    
    % Group B
    % 2-D Gaussian with average sigma for sigma_x and sigma_y 
    B_s = Bxy_sigma(t);
    B_d = sqrt( (data(row,2) - Bx_centers(t))^2 + (data(row,3) - By_centers(t))^2 );
    B_c = 1/(2*pi*B_s^2);
    P_B = B_c .* exp(-(B_d.^2) ./ (2*B_s^2));
    % 2-D Gausssian with different sigma for sigma_x and sigma_y
    %Bx_s = Bx_sigma(t);
    %By_s = By_sigma(t);
    %B_c = 1/(2*pi*Bx_s*By_s);
    %B_d = ( (data(row,2) - Bx_centers(t))^2 / (2 * (Bx_s ^2)) ) + ( (data(row,3) - By_centers(t))^2 / (2 * (By_s ^2)) );
    %P_B = B_c * exp(-B_d);
    
    % Gaussian
    P_AB = [P_A, P_B] ./ (P_A + P_B);
    Gauss = [Gauss; P_AB];
    
end

% Compute normative (actual) probabilities 
for t = 1:10
    Base(t,:) = [A_baserates(t,1), B_baserates(t,1)];
    Norm(t,:) = Base(t,:) .* Gauss(t,:);
    Norm(t,:) = Norm(t,:) ./ sum(Norm(t,:));
end

% Craig's normative (actual) probabilities
for t = 1:10
    Craig(t,:) = Norms_1{t}(1,:); 
end

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Sage likelihoods (with knowledge of generative model)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
SageLike = [];
for t = 1:10
    row = t*10; % row in data for "probe" attack (i.e., 10, 20, ..., 100)
    % Group A
    A_s = 20;
    A_d = sqrt( (data(row,2) - 50)^2 + (data(row,3) - 50)^2 );
    A_c = 1/(2*pi*A_s^2);
    S_A = A_c .* exp(-(A_d.^2) ./ (2*A_s^2));
    % Group B
    B_s = 10;
    B_d = sqrt( (data(row,2) - 30)^2 + (data(row,3) - 30)^2 );
    B_c = 1/(2*pi*B_s^2);
    S_B = B_c .* exp(-(B_d.^2) ./ (2*B_s^2));
    % Gaussian
    S_AB = [S_A, S_B] ./ sum(S_A + S_B);
    SageLike = [SageLike; S_AB];
end
for t = 1:10
    SageBase(t,:) = [0.67, 0.33];
    Sage(t,:) = SageBase(t,:) .* SageLike(t,:);
    Sage(t,:) = Sage(t,:) ./ sum(Sage(t,:));
    Sage(t,:) = max(Sage(t,:), 0.0001);
    Sage(t,:) = min(Sage(t,:), 0.9999);
    SageBase(t,:) = max(SageBase(t,:), 0.0001);
    SageBase(t,:) = min(SageBase(t,:), 0.9999);
    SageLike(t,:) = max(SageLike(t,:), 0.0001);
    SageLike(t,:) = min(SageLike(t,:), 0.9999);
    
end
%%Debug code from Craig
Sage
Craig
%%End Debug code from Craig

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Note: Craig's probabilities are "actual" normative, based on attacks up to that trial.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
for t = 1:10
    
    My_Posts(t,:) = Norm(t,:);
    My_PlogP(t,:) = My_Posts(t,:) .* log2(My_Posts(t,:));
    My_E(t) = -sum(My_PlogP(t,:));
    My_N(t) = (E_max - My_E(t))/E_max;
    
    Craig_Posts(t,:) = Craig(t,:);
    Craig_PlogP(t,:) = Craig_Posts(t,:) .* log2(Craig_Posts(t,:));
    Craig_E(t) = -sum(Craig_PlogP(t,:));
    Craig_N(t) = (E_max - Craig_E(t))/E_max;
    
    Sage_Posts(t,:) = Sage(t,:);
    Sage_PlogP(t,:) = Sage_Posts(t,:) .* log2(Sage_Posts(t,:));
    Sage_E(t) = -sum(Sage_PlogP(t,:));
    Sage_N(t) = (E_max - Sage_E(t))/E_max;
    
end
figure;
t = 1:10;
plot(t, My_N+0.001, 'rs-');
hold on;
plot(t, Craig_N,'bo-');
plot(t, Sage_N, 'ko:');
xlabel('Trial', 'Fontsize', 11);
ylabel('Negentropy', 'Fontsize', 11);
title(['Task 1: Craig in blue. Mine in red. Sage in black.'], 'FontSize', 11, 'FontWeight', 'bold');
set(gca, 'xlim', [1 10]);
set(gca, 'ylim', [0 1]);

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Plot base rates and sigmas vs. trial
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% Base Rates, Group A
figure;
t = 1:10;
xlabel('Trial', 'Fontsize', 11);
ylabel('Base Rate, Group A', 'Fontsize', 11);
title(['Task 1: solid is cumulative, dotted is generative'], 'FontSize', 11, 'FontWeight', 'bold');
set(gca, 'xlim', [1 10]);
set(gca, 'ylim', [0 1]);
hold on;
plot(t, A_baserates(t,1), 'ko-');
plot(t, SageBase(t,1), 'k:');

% Base Rates, Group B
figure;
t = 1:10;
xlabel('Trial', 'Fontsize', 11);
ylabel('Base Rate, Group B', 'Fontsize', 11);
title(['Task 1: solid is cumulative, dotted is generative'], 'FontSize', 11, 'FontWeight', 'bold');
set(gca, 'xlim', [1 10]);
set(gca, 'ylim', [0 1]);
hold on;
plot(t, B_baserates(t,1), 'ko-');
plot(t, SageBase(t,2), 'k:');

% Sigma, Group A
figure;
t = 1:10;
plot(t, A_sigma, 'ms-');
hold on;
plot(t, Axy_sigma, 'ko-');
plot(t, A2to1_sigma, 'g*-');
xlabel('Trial', 'Fontsize', 11);
ylabel('Sigma, Group A', 'Fontsize', 11);
title(['Task 1: solid is cumulative, dotted is generative'], 'FontSize', 11, 'FontWeight', 'bold');
set(gca, 'xlim', [1 10]);
set(gca, 'ylim', [0 40]);
plot(t, ones(1,10).*20, 'k:');

% Sigma, Group B
figure;
t = 1:10;
plot(t, B_sigma, 'ms-');
hold on;
plot(t, Bxy_sigma, 'ko-');
plot(t, B2to1_sigma, 'g*-');
xlabel('Trial', 'Fontsize', 11);
ylabel('Sigma, Group B', 'Fontsize', 11);
title(['Task 1: solid is cumulative, dotted is generative'], 'FontSize', 11, 'FontWeight', 'bold');
set(gca, 'xlim', [1 10]);
set(gca, 'ylim', [0 40]);
plot(t, ones(1,10).*10, 'k:');
    
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Compute human and Bayesian probabilities 
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

for t = 1:10 % 5 trials of human data
    Post = Posts_1{t}(1,:); % human probs (there is only one stage for each trial)
    PlogP = Post .* log2(Post);
    E = -sum(PlogP);
    N = (E_max - E)/E_max;
    P_1(t,:) = Post;
    NP_1(t,1) = N;
    % Sage
    PlogP = Sage(t,:) .* log2(Sage(t,:));
    E = -sum(PlogP);
    N = (E_max - E)/E_max;
    S_1(t,:) = Sage(t,:);
    NS_1(t,1) = N;
    % Bayesian
    Post = Norm(t,:); % Bayesian probs (there is only one stage for each trial)
    Post = max(Post, 0.0001);
    Post = min(Post, 0.9999);
    PlogP = Post .* log2(Post);
    E = -sum(PlogP);
    N = (E_max - E)/E_max;
    Q_1(t,:) = Post;
    NQ_1(t,1) = N;
end

min_Prob = 0.01;
max_Prob = 0.99;

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Compute best-fit model parameters
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

g = [];
rsr_max = 0;
a_i = 0;
for a = 0.0:0.1:2.0
    a_i = a_i + 1;
    b_j = 0;
    for b = 0.0:0.1:2.0
        b_j = b_j + 1;
        for t = 1:10 
            
            %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
            % Compute posterior as discounted prior (using generative base rates) times discounted likelihood (using generative Gaussian)
            %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
            
            ModBase(t,:) = SageBase(t,:);
            ModLike(t,:) = max(SageLike(t,:), min_Prob);
            ModLike(t,:) = min(ModLike(t,:), max_Prob);
            Post = (ModBase(t, :) .^ a) .* (ModLike(t,:) .^ b);
            Post = max(Post, 0.01);
            Post = min(Post, 0.99);
            Post = Post ./ sum(Post);
            PlogP = Post .* log2(Post);
            E = -sum(PlogP);
            N = (E_max - E)/E_max;
            M_1(t,:) = Post;
            NM_1(t,1) = N;
            % Similarity
            KPM_1(t,1) = -sum(P_1(t,:) .* log2(M_1(t,:))) + sum(P_1(t,:) .* log2(P_1(t,:)));
            SPM_1(t,1) = 2 .^ -KPM_1(t,1);
            KPR_1(t,1) = -sum(P_1(t,:) .* log2(R)) + sum(P_1(t,:) .* log2(P_1(t,:)));
            SPR_1(t,1) = 2 .^ -KPR_1(t,1);
            KPQ_1(t,1) = -sum(P_1(t,:) .* log2(Q_1(t,:))) + sum(P_1(t,:) .* log2(P_1(t,:)));
            SPQ_1(t,1) = 2 .^ -KPQ_1(t,1);
        end
        RSR_PM_1(:,1) = max(0, (SPM_1(:,1) - SPR_1(:,1)) ./ (1 - SPR_1(:,1)));
        RSR_PQ_1(:,1) = max(0, (SPQ_1(:,1) - SPR_1(:,1)) ./ (1 - SPR_1(:,1)));
        AVG_RSR_M_1 = mean(RSR_PM_1);
        AVG_RSR_Q_1 = mean(RSR_PQ_1);
        
        rsr = AVG_RSR_M_1;
        %%%%%%%%%%%%%%%%%
        g(a_i,b_j) = rsr;
        %%%%%%%%%%%%%%%%%
        if rsr > rsr_max
            rsr_max = rsr;
            a_max = a;
            b_max = b;
        end
    end
end
a = a_max;
b = b_max;

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Repeat calcualtion for best-fit a and b
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

for t = 1:10 % 5 trials of human data
    Post = Posts_1{t}(1,:); % human probs (there is only one stage for each trial)
    PlogP = Post .* log2(Post);
    E = -sum(PlogP);
    N = (E_max - E)/E_max;
    P_1(t,:) = Post;
    NP_1(t,1) = N;
    % Sage
    PlogP = Sage(t,:) .* log2(Sage(t,:));
    E = -sum(PlogP);
    N = (E_max - E)/E_max;
    S_1(t,:) = Sage(t,:);
    NS_1(t,1) = N;
    % Bayesian
    Post = Norm(t,:); % Bayesian probs (there is only one stage for each trial)
    Post = max(Post, 0.0001);
    Post = min(Post, 0.9999);
    PlogP = Post .* log2(Post);
    E = -sum(PlogP);
    N = (E_max - E)/E_max;
    Q_1(t,:) = Post;
    NQ_1(t,1) = N;     
 
    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    % Compute posterior as discounted prior (using generative base rates) times discounted likelihood (using generative Gaussian)
    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%    
    ModBase(t,:) = SageBase(t,:);
    ModLike(t,:) = max(SageLike(t,:), min_Prob);
    ModLike(t,:) = min(ModLike(t,:), max_Prob); %Note from CB: Kevin puts likelihoods in range 1-99   
    Post = (ModBase(t, :) .^ a) .* (ModLike(t,:) .^ b);
    Post = max(Post, 0.01);
    Post = min(Post, 0.99);
    Post = Post ./ sum(Post);
    %%Debug code from Craig  
    %ModBase(t, :)
    %ModLike(t, :)
    Post
    P_1(t,:)
    %%End Debug code from Craig
    PlogP = Post .* log2(Post);
    E = -sum(PlogP);
    N = (E_max - E)/E_max;
    M_1(t,:) = Post;
    NM_1(t,1) = N;
    % Similarity
    KPM_1(t,1) = -sum(P_1(t,:) .* log2(M_1(t,:))) + sum(P_1(t,:) .* log2(P_1(t,:)));
    SPM_1(t,1) = 2 .^ -KPM_1(t,1);
    KPR_1(t,1) = -sum(P_1(t,:) .* log2(R)) + sum(P_1(t,:) .* log2(P_1(t,:)));
    SPR_1(t,1) = 2 .^ -KPR_1(t,1);
    KPQ_1(t,1) = -sum(P_1(t,:) .* log2(Q_1(t,:))) + sum(P_1(t,:) .* log2(P_1(t,:)));
    SPQ_1(t,1) = 2 .^ -KPQ_1(t,1);
end

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Compute Relative Success Rate (RSR) for Model (PM) and Bayesian (PQ)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

a
b
RSR_PM_1(:,1) = max(0, (SPM_1(:,1) - SPR_1(:,1)) ./ (1 - SPR_1(:,1)));
RSR_PQ_1(:,1) = max(0, (SPQ_1(:,1) - SPR_1(:,1)) ./ (1 - SPR_1(:,1)));
AVG_RSR_M_1 = mean(RSR_PM_1);
AVG_RSR_Q_1 = mean(RSR_PQ_1);
   
figure;
t = 1:10;
plot(t, NQ_1, 'k*-');
xlabel('Trial', 'Fontsize', 11);
ylabel('Negentropy', 'Fontsize', 11);
title(['Task 1, a = ', num2str(a,2), ', b = ', num2str(b,2), ', RSR = ', num2str(AVG_RSR_M_1,2)], 'FontSize', 11, 'FontWeight', 'bold');
set(gca, 'xlim', [1 5], 'xtick', 0:1:5);
set(gca, 'ylim', [0 1]);
hold on;
plot(t, NS_1, 'k+:');
plot(t, NP_1, 'bo-');
plot(t, NM_1, 'ro:'); 

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Plot surface of RSR(x-axis) vs. a (x-axis) and b (y-axis)
% Adapted from old IGT code (fall 2009): surf_abc.m
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%%%%%%%%%%%%%%%%
% Plot rotated g
%%%%%%%%%%%%%%%%
figure
g_rot = flipud(rot90(g)); % This rotation to compensate for MATLAB rotation in surf plot, see poker plots. 
surfc(g_rot);
title(['Task 1, RSR = fn(a, b)'], 'FontSize', 11, 'FontWeight', 'bold');
colormap gray
xlabel('a', 'FontSize', 11);
ylabel('b', 'FontSize', 11);
set(gca, 'XLim', [1 21], 'XTick', [1 6 11 16 21], 'XTickLabel', [0.0 0.5 1.0 1.5 2.0]);
set(gca, 'YLim', [1 21], 'YTick', [1 6 11 16 21], 'YTickLabel', [0.0 0.5 1.0 1.5 2.0]);
set(gca, 'Zlim', [0 1]);
