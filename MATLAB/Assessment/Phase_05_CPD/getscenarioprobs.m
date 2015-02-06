function probs = getscenarioprobs(evidencenodes,facilitynode,scenario,layersshown)

[allfindings,findingnodenames] = scenario_to_findings_vector(scenario,layersshown);

%% Set findings and compute answer probabilities
setlayerfindings(evidencenodes,findingnodenames,allfindings);
probs = facilitynode.getBeliefs;
clearlayerfindings(evidencenodes);
