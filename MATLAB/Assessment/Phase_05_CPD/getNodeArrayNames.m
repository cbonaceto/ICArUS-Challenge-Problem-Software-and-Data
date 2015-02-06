function allnodenames = getNodeArrayNames(allnodes)

allnodenames = cell(1,length(allnodes));
for n = 1:length(allnodes)
    allnodenames{n} = char(allnodes(n).getName); 
end
