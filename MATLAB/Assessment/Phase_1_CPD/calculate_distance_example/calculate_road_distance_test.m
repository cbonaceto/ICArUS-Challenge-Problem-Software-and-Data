clear all; clc; clf
load('ROAD.mat')
R = ROAD{1}; % Choose the first road network as an example

netXloc = R(:,2);
netYloc = R(:,3);
noOfNodes = length(R);
farthestNextHop = 1:length(R);
farthestPreviousHop = 1:length(R);

% Generate link matrix
matrix = zeros(noOfNodes,noOfNodes);
for (i =1:noOfNodes)
    for (j = 1:noOfNodes)
        distance = sqrt((netXloc(i) - netXloc(j))^2 + (netYloc(i) - netYloc(j))^2);
        if (distance <= sqrt(2))
            matrix(i,j) = 1;   % There is a link
            line([netXloc(i) netXloc(j)], [netYloc(i) netYloc(j)], 'LineStyle', '-');
        else
            matrix(i,j) = Inf;
        end
    end
end

% Calculate distance between two random points on the road network
start_point = ceil(length(R)*rand);
end_point = ceil(length(R)*rand);
[path, totalCost, farthestPreviousHop, farthestNextHop] = dijkstra(noOfNodes,matrix,start_point,end_point,farthestPreviousHop,farthestNextHop);
path %#ok<*NOPTS>
totalCost
if (~isempty(path))
    for i = 1:(length(path)-1)
        line([netXloc(path(i)) netXloc(path(i+1))], [netYloc(path(i)) netYloc(path(i+1))], 'Color','r','LineWidth', 2, 'LineStyle', '-.');
    end;
end;