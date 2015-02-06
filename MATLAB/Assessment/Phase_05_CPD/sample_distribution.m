function [x y pxy] = sample_distribution(distr_name,pts,n,center_coord_value)

% [x y] = sample_distribution(D,10,[facility_size,facility_size],[X,Y]);
% distr_name = D;
% pts = 10;
% n = [facility_size,facility_size];
% center_coord_value=[X,Y];

% For testing only:
% s = RandStream('mcg16807','Seed', 0);
% RandStream.setDefaultStream(s)

nx=n(1); ny=n(2);

% Get the data
[~,~,~,~,pxy]=spatial_distribution_palette(distr_name,n,center_coord_value);

% Determine the size of the sample pool
N=round(1/mean(pxy(:)));

sample_dist = [];
integer_probabilities = round(N*pxy);
for (i = 1:(nx*ny))
    sample_dist = [sample_dist, i*ones(1, integer_probabilities(i))]; % Sample distribution
end

% Randomly draw from the sample distribution
temp = sortrows([sample_dist' randperm(length(sample_dist))'],2);
rs = temp(1:pts);

% Convert the indices into x,y-coordinates
[x,y]=ind2sub([nx,ny],rs(1:pts));