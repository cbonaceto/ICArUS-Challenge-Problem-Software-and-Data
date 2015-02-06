% learning2_surf.m
clear

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Code to analyze data from AHA Task 2.
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
data = csvread(['data\KevinB\',exam,'\Normative Data\Task_2_Attacks.csv']); % Format is group that attacked (A = 1, B = 2, C = 3, D = 4), x-location, y-location
% Posteriors (Subjective)
Posts_2 = read_probs_file(['data\KevinB\',exam,'\Subject Data\Task_2_Avg_Subject_Probabilities.csv']);
% Posteriors (Normative)
Norms_2 = read_probs_file(['data\KevinB\',exam,'\Normative Data\Task_2_Normative_Probabilities.csv']);


% With four hypotheses...
% Random (uninformative) distribution
R = [0.25, 0.25, 0.25, 0.25];
% Maximum entropy
E_max = 2;

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Compute "actual" normative solutions for trials of Task 2,
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
    %Compute sigma_x and sigma_y, then average 
    for r = 1:size(Bx_data,1); % r is the number of attacks
        Bx_distance_squared(r,1) = (Bx_data(r,1) - Bx_centers(t)) ^ 2;
        By_distance_squared(r,1) = (By_data(r,1) - By_centers(t)) ^ 2;
    end
    Bx_sigma(t) = sqrt( sum(Bx_distance_squared)/size(Bx_data,1) );
    By_sigma(t) = sqrt( sum(By_distance_squared)/size(By_data,1) );
    Bxy_sigma(t) = (Bx_sigma(t) + By_sigma(t)) / 2;
    
    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    % Group C
    % Compute sigma_x and sigma_y, then average 
    for r = 1:size(Cx_data,1); % r is the number of attacks up to trial t
        Cx_distance_squared(r,1) = (Cx_data(r,1) - Cx_centers(t)) ^ 2;
        Cy_distance_squared(r,1) = (Cy_data(r,1) - Cy_centers(t)) ^ 2;
    end
    Cx_sigma(t) = sqrt( sum(Cx_distance_squared)/size(Cx_data,1) );
    Cy_sigma(t) = sqrt( sum(Cy_distance_squared)/size(Cy_data,1) );
    Cxy_sigma(t) = (Cx_sigma(t) + Cy_sigma(t)) / 2;
    
    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    % Group D
    %Compute sigma_x and sigma_y, then average 
    for r = 1:size(Dx_data,1); % r is the number of attacks
        Dx_distance_squared(r,1) = (Dx_data(r,1) - Dx_centers(t)) ^ 2;
        Dy_distance_squared(r,1) = (Dy_data(r,1) - Dy_centers(t)) ^ 2;
    end
    Dx_sigma(t) = sqrt( sum(Dx_distance_squared)/size(Dx_data,1) );
    Dy_sigma(t) = sqrt( sum(Dy_distance_squared)/size(Dy_data,1) );
    Dxy_sigma(t) = (Dx_sigma(t) + Dy_sigma(t)) / 2;
 
end 

% Compute Gaussian Likelihoods at each block
Gauss = [];
for t = 1:5
    row = t*20; % row in data for "probe" attack (i.e., 20, 40, ..., 100)
    
    % Group A
    % 2-D Gaussian with average sigma for sigma_x and sigma_y 
    A_s = Axy_sigma(t);
    A_d = sqrt( (data(row,2) - Ax_centers(t))^2 + (data(row,3) - Ay_centers(t))^2 );
    A_c = 1/(2*pi*A_s^2);
    P_A = A_c .* exp(-(A_d.^2) ./ (2*A_s^2));
        
    % Group B
    % 2-D Gaussian with average sigma for sigma_x and sigma_y 
    B_s = Bxy_sigma(t);
    B_d = sqrt( (data(row,2) - Bx_centers(t))^2 + (data(row,3) - By_centers(t))^2 );
    B_c = 1/(2*pi*B_s^2);
    P_B = B_c .* exp(-(B_d.^2) ./ (2*B_s^2));
    
    % Group C
    % 2-D Gaussian with average sigma for sigma_x and sigma_y 
    C_s = Cxy_sigma(t);
    C_d = sqrt( (data(row,2) - Cx_centers(t))^2 + (data(row,3) - Cy_centers(t))^2 );
    C_c = 1/(2*pi*C_s^2);
    P_C = C_c .* exp(-(C_d.^2) ./ (2*C_s^2));
        
    % Group D
    % 2-D Gaussian with average sigma for sigma_x and sigma_y 
    D_s = Dxy_sigma(t);
    D_d = sqrt( (data(row,2) - Dx_centers(t))^2 + (data(row,3) - Dy_centers(t))^2 );
    D_c = 1/(2*pi*D_s^2);
    P_D = D_c .* exp(-(D_d.^2) ./ (2*D_s^2));
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
    Craig(t,:) = Norms_2{t}(1,:); 
end

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Sage likelihoods (with knowledge of generative model)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% NOTE: Coordinates for center of group D are (75, 25) and NOT (25,75) like in Mike's email
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
SageLike = [];
for t = 1:5
    row = t*20; % row in data for "probe" attack (i.e., 20, 40, ..., 100)
    % Group A
    A_s = 20;
    A_d = sqrt( (data(row,2) - 50)^2 + (data(row,3) - 50)^2 );
    A_c = 1/(2*pi*A_s^2);
    S_A = A_c .* exp(-(A_d.^2) ./ (2*A_s^2));
    % Group B
    B_s = 5;
    B_d = sqrt( (data(row,2) - 25)^2 + (data(row,3) - 25)^2 );
    B_c = 1/(2*pi*B_s^2);
    S_B = B_c .* exp(-(B_d.^2) ./ (2*B_s^2));
    % Group C
    C_s = 20;
    C_d = sqrt( (data(row,2) - 75)^2 + (data(row,3) - 75)^2 );
    C_c = 1/(2*pi*C_s^2);
    S_C = C_c .* exp(-(C_d.^2) ./ (2*C_s^2));
    % Group D
    D_s = 15;
    D_d = sqrt( (data(row,2) - 75)^2 + (data(row,3) - 25)^2 );
    D_c = 1/(2*pi*D_s^2);
    S_D = D_c .* exp(-(D_d.^2) ./ (2*D_s^2));
    % Gaussian
    S_ABCD = [S_A, S_B, S_C, S_D] ./ sum(S_A + S_B + S_C + S_D);
    SageLike = [SageLike; S_ABCD];
end
Prior = [0.1, 0.2, 0.3, 0.4];
SageBase = [];
for t = 1:5
    SageBase = [SageBase; Prior];
end
Sage = [];
for t = 1:5
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
%%End Debug code from Craig
Sage_2 = Sage;
Craig_2 = Craig;

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
    
    Sage_Posts(t,:) = Sage(t,:);
    Sage_PlogP(t,:) = Sage_Posts(t,:) .* log2(Sage_Posts(t,:));
    Sage_E(t) = -sum(Sage_PlogP(t,:));
    Sage_N(t) = (E_max - Sage_E(t))/E_max;
    
end
figure;
t = 1:5;
plot(t, My_N+0.001, 'rs-');
hold on;
plot(t, Craig_N,'bo-');
plot(t, Sage_N, 'ko:');
xlabel('Trial', 'Fontsize', 11);
ylabel('Negentropy', 'Fontsize', 11);
title(['Task 1: Craig in blue. Mine in red. Sage in black.'], 'FontSize', 11, 'FontWeight', 'bold');
set(gca, 'xlim', [1 5], 'xtick', 0:1:5);
set(gca, 'ylim', [0 1]);

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Plot base rates and sigmas vs. trial
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

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

% Sigma, Group A
figure;
t = 1:5;
plot(t, Axy_sigma, 'ko-');
hold on;
xlabel('Trial', 'Fontsize', 11);
ylabel('Sigma, Group A', 'Fontsize', 11);
title(['Task 1: solid is cumulative, dotted is generative'], 'FontSize', 11, 'FontWeight', 'bold');
set(gca, 'xlim', [1 5], 'xtick', 0:1:5);
set(gca, 'ylim', [0 40]);
plot(t, ones(1,5).*20, 'k:');

% Sigma, Group B
figure;
t = 1:5;
plot(t, Bxy_sigma, 'ko-');
hold on;
xlabel('Trial', 'Fontsize', 11);
ylabel('Sigma, Group B', 'Fontsize', 11);
title(['Task 1: solid is cumulative, dotted is generative'], 'FontSize', 11, 'FontWeight', 'bold');
set(gca, 'xlim', [1 5], 'xtick', 0:1:5);
set(gca, 'ylim', [0 40]);
plot(t, ones(1,5).*5, 'k:');

% Sigma, Group C
figure;
t = 1:5;
plot(t, Cxy_sigma, 'ko-');
hold on;
xlabel('Trial', 'Fontsize', 11);
ylabel('Sigma, Group C', 'Fontsize', 11);
title(['Task 1: solid is cumulative, dotted is generative'], 'FontSize', 11, 'FontWeight', 'bold');
set(gca, 'xlim', [1 5], 'xtick', 0:1:5);
set(gca, 'ylim', [0 40]);
plot(t, ones(1,5).*20, 'k:');

% Sigma, Group D
figure;
t = 1:5;
plot(t, Dxy_sigma, 'ko-');
hold on;
xlabel('Trial', 'Fontsize', 11);
ylabel('Sigma, Group D', 'Fontsize', 11);
title(['Task 1: solid is cumulative, dotted is generative'], 'FontSize', 11, 'FontWeight', 'bold');
set(gca, 'xlim', [1 5], 'xtick', 0:1:5);
set(gca, 'ylim', [0 40]);
plot(t, ones(1,5).*15, 'k:');

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Compute human and Bayesian probabilities 
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

for t = 1:5 % 5 trials
    Post = Posts_2{t}(1,:); % human probs (there is only one stage for each trial)
    PlogP = Post .* log2(Post);
    E = -sum(PlogP);
    N = (E_max - E)/E_max;
    P_2(t,:) = Post;
    NP_2(t,1) = N;
    % Bayesian Sage
    Post = Sage_2(t,:);
    Post = max(Post, 0.0001);
    Post = min(Post, 0.9999);
    PlogP = Post .* log2(Post);
    E = -sum(PlogP);
    N = (E_max - E)/E_max;
    S_2(t,:) = Post;
    NS_2(t,1) = N;
    % Bayesian Craig
    Post = Craig_2(t, :);
    Post = max(Post, 0.0001);
    Post = min(Post, 0.9999);
    PlogP = Post .* log2(Post);
    E = -sum(PlogP);
    N = (E_max - E)/E_max;
    Q_2(t,:) = Post;
    NQ_2(t,1) = N;
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
            % Compute posterior as discounted prior (using generative base rates) times discounted likelihood (using generative Gaussian)
            %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
            
            Post = Sage_2(t,:);
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
            M_2(t,:) = Post;
            NM_2(t,1) = N;
            % Similarity
            KPM_2(t,1) = -sum(P_2(t,:) .* log2(M_2(t,:))) + sum(P_2(t,:) .* log2(P_2(t,:)));
            SPM_2(t,1) = 2 .^ -KPM_2(t,1);
            KPR_2(t,1) = -sum(P_2(t,:) .* log2(R)) + sum(P_2(t,:) .* log2(P_2(t,:)));
            SPR_2(t,1) = 2 .^ -KPR_2(t,1);
            KPQ_2(t,1) = -sum(P_2(t,:) .* log2(Q_2(t,:))) + sum(P_2(t,:) .* log2(P_2(t,:)));
            SPQ_2(t,1) = 2 .^ -KPQ_2(t,1);
        end
        RSR_PM_2(:,1) = max(0, (SPM_2(:,1) - SPR_2(:,1)) ./ (1 - SPR_2(:,1)));
        RSR_PQ_2(:,1) = max(0, (SPQ_2(:,1) - SPR_2(:,1)) ./ (1 - SPR_2(:,1)));
        AVG_RSR_M_2 = mean(RSR_PM_2);
        AVG_RSR_Q_2 = mean(RSR_PQ_2);
       
        rsr = AVG_RSR_M_2;
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

%%Debug code from Craig  
a
b
%%End Debug code from Craig

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Repeat calcualtion for best-fit a and b
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

for t = 1:5 % 5 trials
    Post = Posts_2{t}(1,:); % human probs (there is only one stage for each trial)
    PlogP = Post .* log2(Post);
    E = -sum(PlogP);
    N = (E_max - E)/E_max;
    P_2(t,:) = Post;
    NP_2(t,1) = N;
    % Bayesian Sage
    Post = Sage_2(t,:);
    Post = max(Post, 0.0001);
    Post = min(Post, 0.9999);
    PlogP = Post .* log2(Post);
    E = -sum(PlogP);
    N = (E_max - E)/E_max;
    S_2(t,:) = Post;
    NS_2(t,1) = N;
    % Bayesian Craig
    Post = Craig_2(t, :);
    Post = max(Post, 0.0001);
    Post = min(Post, 0.9999);
    PlogP = Post .* log2(Post);
    E = -sum(PlogP);
    N = (E_max - E)/E_max;
    Q_2(t,:) = Post;
    NQ_2(t,1) = N;
    
    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    % Model uses Post from Sage_2, which are the generative Bayesian solution
    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
        
    Post = Sage_2(t,:);
    Like = (Post ./ Prior) ./ sum(Post ./ Prior);
    Prior = max(Prior, min_Prob);
    Prior = min(Prior, max_Prob);
    Like = max(Like, min_Prob);
    Like = min(Like, max_Prob); 
    Post = (Prior .^ a) .* (Like .^ b); % Model discounts Bayesian probabilities
    Post = Post ./ sum(Post); % Normalize the model
    %%Debug code from Craig  
    Prior
    Like
    Post
    %%End Debug code from Craig
    PlogP = Post .* log2(Post);
    E = -sum(PlogP);
    N = (E_max - E)/E_max;
    M_2(t,:) = Post;
    NM_2(t,1) = N;
    % Similarity
    KPM_2(t,1) = -sum(P_2(t,:) .* log2(M_2(t,:))) + sum(P_2(t,:) .* log2(P_2(t,:)));
    SPM_2(t,1) = 2 .^ -KPM_2(t,1);
    KPR_2(t,1) = -sum(P_2(t,:) .* log2(R)) + sum(P_2(t,:) .* log2(P_2(t,:)));
    SPR_2(t,1) = 2 .^ -KPR_2(t,1);
    KPQ_2(t,1) = -sum(P_2(t,:) .* log2(Q_2(t,:))) + sum(P_2(t,:) .* log2(P_2(t,:)));
    SPQ_2(t,1) = 2 .^ -KPQ_2(t,1);
end

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Compute Relative Success Rate (RSR) for Model (PM) and Bayesian (PQ)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

a
b
RSR_PM_2(:,1) = max(0, (SPM_2(:,1) - SPR_2(:,1)) ./ (1 - SPR_2(:,1)));
RSR_PQ_2(:,1) = max(0, (SPQ_2(:,1) - SPR_2(:,1)) ./ (1 - SPR_2(:,1)));
AVG_RSR_M_2 = mean(RSR_PM_2)
AVG_RSR_Q_2 = mean(RSR_PQ_2)

figure;
t = 1:5;
plot(t, NQ_2, 'k*-');
hold on;
plot(t, NS_2, 'k*:');
xlabel('Trial', 'Fontsize', 11);
ylabel('Negentropy', 'Fontsize', 11);
title(['Task 2, a = ', num2str(a,2), ', b = ', num2str(b,2), ', RSR = ', num2str(AVG_RSR_M_2,2)], 'FontSize', 11, 'FontWeight', 'bold');
set(gca, 'xlim', [1 5], 'xtick', 0:1:5);
set(gca, 'ylim', [0 1]);
plot(t, NP_2, 'bo-');
plot(t, NM_2, 'ro:');

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
title(['Task 2, RSR = fn(a, b)'], 'FontSize', 11, 'FontWeight', 'bold');
colormap gray
xlabel('a', 'FontSize', 11);
ylabel('b', 'FontSize', 11);
set(gca, 'XLim', [1 21], 'XTick', [1 6 11 16 21], 'XTickLabel', [0.0 0.5 1.0 1.5 2.0]);
set(gca, 'YLim', [1 21], 'YTick', [1 6 11 16 21], 'YTickLabel', [0.0 0.5 1.0 1.5 2.0]);
set(gca, 'Zlim', [0 1]);