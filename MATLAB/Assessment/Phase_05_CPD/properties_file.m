% Properties File

% FACILITY
% -------------------

% Probability of facility
p_facility = [0.25 0.25 0.25 0.25];
assert(sum(p_facility) == 1);

% TERRAIN
% -------------------

% Probability of terrain
p_terrain_facility = [0.1 0.3 0.5 0.7];

% SIGINT
% -------------------

% Probability of SIGINT
p_sigint_facility = [0.8 0.2 0.8 0.2];

% If SIGINT, the probability of high or low density
p_sigint_high_density = [0.8 0.2 0.8 0.2];
p_sigint_low_density = 1 - p_sigint_high_density;

assert(all(p_sigint_high_density + p_sigint_low_density == 1));

% MASINT
% -------------------

% Probability of MASINT
p_masint_facility(:,1) = [0.2 0.8 0.2 0.8]; % Chemical 1
p_masint_facility(:,2) = [0.2 0.8 0.8 0.2]; % Chemical 2

% If MASINT, the probability of high or low concentration
p_masint_high_concentration(:,1) = [0.2 0.8 0.2 0.8]; % Chemical 1
p_masint_high_concentration(:,2) = [0.2 0.8 0.8 0.2]; % Chemical 2

p_masint_low_concentration(:,1) = 1 - p_masint_high_concentration(:,1);
p_masint_low_concentration(:,2) = 1 - p_masint_high_concentration(:,2);

assert(all(p_masint_high_concentration(:) + p_masint_low_concentration(:) == 1));
