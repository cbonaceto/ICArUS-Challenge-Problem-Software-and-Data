function relativeentropy = runcase(hypothesisnode,datanodes,caseheadings,acase,fdivergence,epsilon)

priorBeliefs = (hypothesisnode.getBeliefs)';

% set layer, then compute entropy reduction from uninformative prior to new beliefs
setlayerfindings(datanodes,caseheadings,acase);
relativeentropy = feval(fdivergence,priorBeliefs,(hypothesisnode.getBeliefs)',epsilon);
clearlayerfindings(datanodes);
