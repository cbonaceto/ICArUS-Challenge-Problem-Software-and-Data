function [nodesarray, nodelist] = getNodeList(net,nodelist,varargin)
% getNodeList(net,NodeList(net),[nodelistname])

% collects nodes both as:
% Matlab cell array (nodesarray) 
% Netica Java nodelist (nodelist)
nodes = net.getNodes;
numnodes = nodes.size;

nodesarray = [];

for n = 1:numnodes
    node = nodes.getNode(n-1);

    if isempty(varargin) || node.isInNodeset(varargin{1})
        nodesarray = [nodesarray node]; %#ok
        nodelist.add(node);
    end
end
