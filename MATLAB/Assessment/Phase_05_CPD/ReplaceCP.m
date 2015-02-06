function ReplaceCP(node,oldval,newval)
%
% replace value in CP Table

cpt = node.getCPTable([]);
cpt(abs(cpt - oldval) < 0.0001) = newval;
node.setCPTable(cpt);
