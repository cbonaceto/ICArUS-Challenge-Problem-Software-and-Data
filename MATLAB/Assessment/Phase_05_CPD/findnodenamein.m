function nodeidx = findnodenamein(nodename,nodenames)

nodeidx = [];
for i = 1:length(nodenames)
    if strcmp(nodename,nodenames{i}), nodeidx = i; end
end
