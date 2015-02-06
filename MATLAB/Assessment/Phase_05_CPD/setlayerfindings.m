function setlayerfindings(layernodes,nodenames,findings)

% set all findings for all nodes specified by nodenames

for n = 1:length(layernodes)
    node = layernodes(n);
    nodename = getNodeArrayNames(node);
    idx = findnodenamein(nodename,nodenames);
    if ~isempty(idx)
        node.finding.enterState(findings{idx});
    end
end        
