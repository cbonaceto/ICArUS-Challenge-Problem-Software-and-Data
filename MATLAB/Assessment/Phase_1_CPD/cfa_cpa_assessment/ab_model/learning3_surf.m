% learning3_surf.m
clear

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Code to analyze data from AHA Task 3.
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
data = csvread(['data\KevinB\',exam,'\Normative Data\Task_3_Attacks.csv']); % Format is group that attacked (A = 1, B = 2, C = 3, D = 4), x-location, y-location
% Posteriors (Subjective)
Posts_3 = read_probs_file(['data\KevinB\',exam,'\Subject Data\Task_3_Avg_Subject_Probabilities.csv']);
% Posteriors (Normative)
Norms_3 = read_probs_file(['data\KevinB\',exam,'\Normative Data\Task_3_Normative_Probabilities.csv']);
% Normative Distances
Data = csvread(['data\KevinB\',exam,'\Normative Data\Task_3_Normative_Centers_Distances.csv']); % Format is group that attacked (A = 1, B = 2, C = 3, D = 4), x-location, y-location, distance-to-attack

% With four hypotheses...
% Random (uninformative) distribution
R = [0.25, 0.25, 0.25, 0.25];
% Maximum entropy
E_max = 2;

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Compute "actual" normative solutions for trials of Task 3,
% based on cumulative attack data up to each trial.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

An_tot = 0;
Ax_tot = 0;
Ay_tot = 0;
Bn_tot = 0;
Bx_tot = 0;
By_tot = 0;
Cn_tot = 0;
Cx_tot = 0;
Cy_tot = 0;
Dn_tot = 0;
Dx_tot = 0;
Dy_tot = 0;
Ax_data = [];
Ay_data = [];
Bx_data = [];
By_data = [];
Cx_data = [];
Cy_data = [];
Dx_data = [];
Dy_data = [];
A_baserates = [];
B_baserates = [];
C_baserates = [];
D_baserates = [];
Ax_centers = [];
Ay_centers = [];
Bx_centers = [];
By_centers = [];
Cx_centers = [];
Cy_centers = [];
Dx_centers = [];
Dy_centers = [];

row = 1;
for t = 1:5 % 10 trials
    attacks = 20;
    if t == 1
        attacks = 19;
    end
    for i = 1:attacks
        % Update totals and arrays after each trial 
        if data(row,1) == 1
            An_tot = An_tot + 1;
            Ax_tot = Ax_tot + data(row,2);
            Ay_tot = Ay_tot + data(row,3);
            Ax_data = [Ax_data; data(row,2)];
            Ay_data = [Ay_data; data(row,3)];
        elseif data(row,1) == 2
            Bn_tot = Bn_tot + 1;
            Bx_tot = Bx_tot + data(row,2);
            By_tot = By_tot + data(row,3);
            Bx_data = [Bx_data; data(row,2)];
            By_data = [By_data; data(row,3)];
        elseif data(row,1) == 3
            Cn_tot = Cn_tot + 1;
            Cx_tot = Cx_tot + data(row,2);
            Cy_tot = Cy_tot + data(row,3);
            Cx_data = [Cx_data; data(row,2)];
            Cy_data = [Cy_data; data(row,3)];  
        else
            Dn_tot = Dn_tot + 1;
            Dx_tot = Dx_tot + data(row,2);
            Dy_tot = Dy_tot + data(row,3);
            Dx_data = [Dx_data; data(row,2)];
            Dy_data = [Dy_data; data(row,3)];     
        end
        row = row + 1; 
    end
    % Compute base rates, centers, and sigma after each block
    A_base = An_tot / (An_tot + Bn_tot + Cn_tot + Dn_tot);
    B_base = Bn_tot / (An_tot + Bn_tot + Cn_tot + Dn_tot);
    C_base = Cn_tot / (An_tot + Bn_tot + Cn_tot + Dn_tot);
    D_base = Dn_tot / (An_tot + Bn_tot + Cn_tot + Dn_tot);
    
    A_baserates = [A_baserates; A_base];
    B_baserates = [B_baserates; B_base];
    C_baserates = [C_baserates; C_base];
    D_baserates = [D_baserates; D_base];
    
    Ax_cent = Ax_tot/An_tot;
    Ay_cent = Ay_tot/An_tot;
    Bx_cent = Bx_tot/Bn_tot;
    By_cent = By_tot/Bn_tot;
    Cx_cent = Cx_tot/Cn_tot;
    Cy_cent = Cy_tot/Cn_tot;
    Dx_cent = Dx_tot/Dn_tot;
    Dy_cent = Dy_tot/Dn_tot;
    
    Ax_centers = [Ax_centers; Ax_cent];
    Ay_centers = [Ay_centers; Ay_cent];
    Bx_centers = [Bx_centers; Bx_cent];
    By_centers = [By_centers; By_cent];
    Cx_centers = [Cx_centers; Cx_cent];
    Cy_centers = [Cy_centers; Cy_cent];
    Dx_centers = [Dx_centers; Dx_cent];
    Dy_centers = [Dy_centers; Dy_cent];
end 

% Compute Gaussian Likelihoods at each block
Gauss = [];
Row = 1;
for t = 1:5
    
    s = 10; % sigma is 10 miles for all groups
    
    % Group A
    A_s = s;
    A_d = Data(Row,5); 
    A_c = 1/(2*pi*A_s^2);
    P_A = A_c .* exp(-(A_d.^2) ./ (2*A_s^2));
    Row = Row + 1;
        
    % Group B
    B_s = s;
    B_d = Data(Row,5);
    B_c = 1/(2*pi*B_s^2);
    P_B = B_c .* exp(-(B_d.^2) ./ (2*B_s^2));
    Row = Row + 1;
    
    % Group C
    C_s = s;
    C_d = Data(Row,5);
    C_c = 1/(2*pi*C_s^2);
    P_C = C_c .* exp(-(C_d.^2) ./ (2*C_s^2));
    Row = Row + 1;
    
    % Group D
    D_s = s;
    D_d = Data(Row,5);
    D_c = 1/(2*pi*D_s^2);
    P_D = D_c .* exp(-(D_d.^2) ./ (2*D_s^2));
    Row = Row + 1;
    
    % Gaussian
    P_ABCD = [P_A, P_B, P_C, P_D] ./ (P_A + P_B + P_C + P_D);
    Gauss = [Gauss; P_ABCD];
    
end

% Compute normative (actual) probabilities 
for t = 1:5
    Base(t,:) = [A_baserates(t,1), B_baserates(t,1), C_baserates(t,1), D_baserates(t,1)];
    Norm(t,:) = Base(t,:) .* Gauss(t,:);
    Norm(t,:) = Norm(t,:) ./ sum(Norm(t,:));
end

% Craig's normative (actual) probabilities
for t = 1:5
    Craig(t,:) = Norms_3{t}(1,:); 
end

Craig_3 = Craig;

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Note: Craig's probabilities are "actual" normative, based on attacks up to that trial.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
for t = 1:5
    
    My_Posts(t,:) = Norm(t,:);
    My_PlogP(t,:) = My_Posts(t,:) .* log2(My_Posts(t,:));
    My_E(t) = -sum(My_PlogP(t,:));
    My_N(t) = (E_max - My_E(t))/E_max;
    
    Craig_Posts(t,:) = Craig(t,:);
    Craig_PlogP(t,:) = Craig_Posts(t,:) .* log2(Craig_Posts(t,:));
    Craig_E(t) = -sum(Craig_PlogP(t,:));
    Craig_N(t) = (E_max - Craig_E(t))/E_max;
      
end

figure;
t = 1:5;
plot(t, My_N+0.001, 'rs-');
hold on;
plot(t, Craig_N,'bo-');
xlabel('Trial', 'Fontsize', 11);
ylabel('Negentropy', 'Fontsize', 11);
title(['Task 1: Craig in blue. Mine in red.'], 'FontSize', 11, 'FontWeight', 'bold');
set(gca, 'xlim', [1 5], 'xtick', 0:1:5);
set(gca, 'ylim', [0 1]);

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Plot base rates and sigmas vs. trial
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

SageBase = [];
Prior = [0.10, 0.20, 0.30, 0.40];
for t = 1:5
    SageBase = [SageBase; Prior];
end

% Base Rates, Group A
figure;
t = 1:5;
xlabel('Trial', 'Fontsize', 11);
ylabel('Base Rates, Group A', 'Fontsize', 11);
title(['Task 1: solid is cumulative, dotted is generative'], 'FontSize', 11, 'FontWeight', 'bold');
set(gca, 'xlim', [1 5], 'xtick', 0:1:5);
set(gca, 'ylim', [0 1]);
hold on;
plot(t, A_baserates(t,1), 'ko-');
plot(t, SageBase(t,1), 'k:');

% Base Rates, Group B
figure;
t = 1:5;
xlabel('Trial', 'Fontsize', 11);
ylabel('Base Rates, Group B', 'Fontsize', 11);
title(['Task 1: solid is cumulative, dotted is generative'], 'FontSize', 11, 'FontWeight', 'bold');
set(gca, 'xlim', [1 5], 'xtick', 0:1:5);
set(gca, 'ylim', [0 1]);
hold on;
plot(t, B_baserates(t,1), 'ko-');
plot(t, SageBase(t,2), 'k:');

% Base Rates, Group C
figure;
t = 1:5;
xlabel('Trial', 'Fontsize', 11);
ylabel('Base Rates, Group C', 'Fontsize', 11);
title(['Task 1: solid is cumulative, dotted is generative'], 'FontSize', 11, 'FontWeight', 'bold');
set(gca, 'xlim', [1 5], 'xtick', 0:1:5);
set(gca, 'ylim', [0 1]);
hold on;
plot(t, C_baserates(t,1), 'ko-');
plot(t, SageBase(t,3), 'k:');

% Base Rates, Group D
figure;
t = 1:5;
xlabel('Trial', 'Fontsize', 11);
ylabel('Base Rates, Group D', 'Fontsize', 11);
title(['Task 1: solid is cumulative, dotted is generative'], 'FontSize', 11, 'FontWeight', 'bold');
set(gca, 'xlim', [1 5], 'xtick', 0:1:5);
set(gca, 'ylim', [0 1]);
hold on;
plot(t, D_baserates(t,1), 'ko-');
plot(t, SageBase(t,4), 'k:');

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Compute human and Bayesian probabilities 
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

for t = 1:5 % 5 trials
    Post = Posts_3{t}(1,:); % human probs (there is only one stage for each trial)
    PlogP = Post .* log2(Post);
    E = -sum(PlogP);
    N = (E_max - E)/E_max;
    P_3(t,:) = Post;
    NP_3(t,1) = N;
    % Bayesian Craig
    Post = Craig_3(t, :);
    Post = max(Post, 0.0001);
    Post = min(Post, 0.9999);
    PlogP = Post .* log2(Post);
    E = -sum(PlogP);
    N = (E_max - E)/E_max;
    Q_3(t,:) = Post;
    NQ_3(t,1) = N;
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
        for t = 1:5 
                    
            %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
            % Compute posterior as discounted prior (using cumulative base rates) times discounted likelihood (using cumulative Gaussian)
            %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
            
            Post = Craig_3(t,:);
            Like = (Post ./ Prior) ./ sum(Post ./ Prior);
            Prior = max(Prior, min_Prob);
            Prior = min(Prior, max_Prob);
            Like = max(Like, min_Prob);
            Like = min(Like, max_Prob); 
            Post = (Prior .^ a) .* (Like .^ b); % Model discounts Bayesian probabilities
            Post = Post ./ sum(Post); % Normalize the model
            PlogP = Post .* log2(Post);
            E = -sum(PlogP);
            N = (E_max - E)/E_max;
            M_3(t,:) = Post;
            NM_3(t,1) = N;
            % Similarity
            KPM_3(t,1) = -sum(P_3(t,:) .* log2(M_3(t,:))) + sum(P_3(t,:) .* log2(P_3(t,:)));
            SPM_3(t,1) = 2 .^ -KPM_3(t,1);
            KPR_3(t,1) = -sum(P_3(t,:) .* log2(R)) + sum(P_3(t,:) .* log2(P_3(t,:)));
            SPR_3(t,1) = 2 .^ -KPR_3(t,1);
            KPQ_3(t,1) = -sum(P_3(t,:) .* log2(Q_3(t,:))) + sum(P_3(t,:) .* log2(P_3(t,:)));
            SPQ_3(t,1) = 2 .^ -KPQ_3(t,1);
        end
        RSR_PM_3(:,1) = max(0, (SPM_3(:,1) - SPR_3(:,1)) ./ (1 - SPR_3(:,1)));
        RSR_PQ_3(:,1) = max(0, (SPQ_3(:,1) - SPR_3(:,1)) ./ (1 - SPR_3(:,1)));
        AVG_RSR_M_3 = mean(RSR_PM_3);
        AVG_RSR_Q_3 = mean(RSR_PQ_3);
       
        rsr = AVG_RSR_M_3;
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

for t = 1:5 % 5 trials
    Post = Posts_3{t}(1,:); % human probs (there is only one stage for each trial)
    PlogP = Post .* log2(Post);
    E = -sum(PlogP);
    N = (E_max - E)/E_max;
    P_3(t,:) = Post;
    NP_3(t,1) = N;
    % Bayesian Craig
    Post = Craig_3(t, :);
    Post = max(Post, 0.0001);
    Post = min(Post, 0.9999);
    PlogP = Post .* log2(Post);
    E = -sum(PlogP);
    N = (E_max - E)/E_max;
    Q_3(t,:) = Post;
    NQ_3(t,1) = N;
    
    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    % Model uses Post from Craig_3, which is the cumulative Bayesian solution
    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
        
    Post = Craig_3(t,:);
    Like = (Post ./ Prior) ./ sum(Post ./ Prior);
    Prior = max(Prior, min_Prob);
    Prior = min(Prior, max_Prob);
    Like = max(Like, min_Prob);
    Like = min(Like, max_Prob); 
    Post = (Prior .^ a) .* (Like .^ b); % Model discounts Bayesian probabilities
    Post = Post ./ sum(Post); % Normalize the model
    PlogP = Post .* log2(Post);
    E = -sum(PlogP);
    N = (E_max - E)/E_max;
    M_3(t,:) = Post;
    NM_3(t,1) = N;
    % Similarity
    KPM_3(t,1) = -sum(P_3(t,:) .* log2(M_3(t,:))) + sum(P_3(t,:) .* log2(P_3(t,:)));
    SPM_3(t,1) = 2 .^ -KPM_3(t,1);
    KPR_3(t,1) = -sum(P_3(t,:) .* log2(R)) + sum(P_3(t,:) .* log2(P_3(t,:)));
    SPR_3(t,1) = 2 .^ -KPR_3(t,1);
    KPQ_3(t,1) = -sum(P_3(t,:) .* log2(Q_3(t,:))) + sum(P_3(t,:) .* log2(P_3(t,:)));
    SPQ_3(t,1) = 2 .^ -KPQ_3(t,1);
end

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Compute Relative Success Rate (RSR) for Model (PM) and Bayesian (PQ)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

a
b
RSR_PM_3(:,1) = max(0, (SPM_3(:,1) - SPR_3(:,1)) ./ (1 - SPR_3(:,1)));
RSR_PQ_3(:,1) = max(0, (SPQ_3(:,1) - SPR_3(:,1)) ./ (1 - SPR_3(:,1)));
AVG_RSR_M_3 = mean(RSR_PM_3)
AVG_RSR_Q_3 = mean(RSR_PQ_3)

figure;
t = 1:5;
plot(t, NQ_3, 'k*-');
hold on;
xlabel('Trial', 'Fontsize', 11);
ylabel('Negentropy', 'Fontsize', 11);
title(['Task 3, a = ', num2str(a,2), ', b = ', num2str(b,2), ', RSR = ', num2str(AVG_RSR_M_3,2)], 'FontSize', 11, 'FontWeight', 'bold');
set(gca, 'xlim', [1 5], 'xtick', 0:1:5);
set(gca, 'ylim', [0 1]);
plot(t, NP_3, 'bo-');
plot(t, NM_3, 'ro:');

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
title(['Task 3, RSR = fn(a, b)'], 'FontSize', 11, 'FontWeight', 'bold');
colormap gray
xlabel('a', 'FontSize', 11);
ylabel('b', 'FontSize', 11);
set(gca, 'XLim', [1 21], 'XTick', [1 6 11 16 21], 'XTickLabel', [0.0 0.5 1.0 1.5 2.0]);
set(gca, 'YLim', [1 21], 'YTick', [1 6 11 16 21], 'YTickLabel', [0.0 0.5 1.0 1.5 2.0]);
set(gca, 'Zlim', [0 1]);