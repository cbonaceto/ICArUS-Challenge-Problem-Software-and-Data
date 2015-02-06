function check_Matlab_Netica_models(net)

% todo: load probabilities into net from Matlab code MSC
% for now, just doublecheck the net against the tables

FacilityType = net.getNode('FacilityType');
numfacilities = FacilityType.getNumStates;

properties_file;
facility = facility_properties_file;
facilityprior = FacilityType.getBeliefs;

%% Check Facility prior
assert(approxequal(p_facility,facilityprior'));

%% Check BLDG tables
m_buildings = [facility(1).object_occurrence' facility(2).object_occurrence' facility(3).object_occurrence' facility(4).object_occurrence'];
for bldg = 1:7
    temp = net.getNode(sprintf('Bldg%d',bldg)).getCPTable([]);
    assert(all(approxequal(temp(1:2:end)',m_buildings(bldg,:))));
end

%% Other IMINT features
m_dispersion = [facility(1).dispersion ; facility(2).dispersion ; facility(3).dispersion ; facility(4).dispersion];
temp = net.getNode('Dispersion').getCPTable([]);
assert(all(approxequal(reshape(temp,[3 numfacilities])',m_dispersion)));

m_hardware = [facility(1).object_hardware ; facility(2).object_hardware ; facility(3).object_hardware ; facility(4).object_hardware];
temp = net.getNode('BldgHdwr').getCPTable([]);
assert(all(approxequal(reshape(temp,[3 numfacilities])',m_hardware)));

temp = net.getNode('Terrain').getCPTable([]);
assert(all(approxequal(temp(1:2:end)',p_terrain_facility)));

%% Other INTs
m_sigint = [p_sigint_facility.*p_sigint_high_density ; p_sigint_facility.*p_sigint_low_density ; 1-p_sigint_facility]';
temp = net.getNode('SIGINTDensity').getCPTable([]);
assert(all(approxequal(reshape(temp,[3 numfacilities])',m_sigint)));

m_masint1 = [p_masint_facility(:,1).*p_masint_high_concentration(:,1) p_masint_facility(:,1).*p_masint_low_concentration(:,1) 1-p_masint_facility(:,1)];
temp = net.getNode('MASINT1Density').getCPTable([]);
assert(all(approxequal(reshape(temp,[3 numfacilities])',m_masint1)));

m_masint2 = [p_masint_facility(:,2).*p_masint_high_concentration(:,2) p_masint_facility(:,2).*p_masint_low_concentration(:,2) 1-p_masint_facility(:,2)];
temp = net.getNode('MASINT2Density').getCPTable([]);
assert(all(approxequal(reshape(temp,[3 numfacilities])',m_masint2)));

