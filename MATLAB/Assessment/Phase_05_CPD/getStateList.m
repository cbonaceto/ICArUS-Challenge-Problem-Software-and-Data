function states = getStateList(nodes)
% nodes is cell array
% states is cell array of cell arrays, 1 for each node

numnodes = length(nodes);
states = cell(1,numnodes);

for n = 1:numnodes
    node = nodes(n);
    numstates = node.getNumStates;
    for s = 1:numstates
        state = node.state(s-1);
        states{n}{s} = char(state.getName);
    end
end
