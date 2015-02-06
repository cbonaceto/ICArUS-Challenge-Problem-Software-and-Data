function clearlayerfindings(layernodes)

% clear findings
for n = 1:length(layernodes)
    node = layernodes(n);
    node.finding.clear;
end    
