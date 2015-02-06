function [allfindings,findingnodenames] = scenario_to_findings_vector(scenario,layersshown)

findingnodenames = {};
allfindings = {};

%% IMINT
if strfind(layersshown,'I')
    layerfindings = cell(1,10);
    
    %% Set BLDG tables
    for bldgnum = 1:7
        layerfindings{bldgnum} = 'false';
    end
    
    bldgs = scenario.facility_palette; % [X,Y,objectID,objectOrientation,Hardware] -- see generate_facility_palette
    maxbldgs = 7;
    for b = 1:maxbldgs
        if ~isempty(bldgs{b})
            bldgnum = bldgs{b}(3);
            layerfindings{bldgnum} = 'true';
        end
    end
            
    %% Terrain
    layerfindings{8} = 'NoWater';
    if ~isempty(scenario.terrain)
        if any(strcmp(scenario.terrain,'Water'))
            layerfindings{8} = 'Water';
        end
    end
    
    %% Dispersion
    if scenario.facility_dispersion == 1
        layerfindings{9} = 'd1';
    elseif scenario.facility_dispersion == 2
        layerfindings{9} = 'd2';
    elseif scenario.facility_dispersion == 3
        layerfindings{9} = 'd3';
    else error('unknown dispersion'); 
    end
     
    %% Hardware -- count
    numhardware = 0;
    for b = 1:maxbldgs
        if ~isempty(bldgs{b})
            numhardware = numhardware + bldgs{b}(5);
        end
    end
    if numhardware == 0; 
        layerfindings{10} = 'none';
    elseif numhardware == 1; 
        layerfindings{10} = 'one';
    elseif numhardware > 1; 
        layerfindings{10} = 'many';
    else error('unknown hardware');
    end
    
    findingnodenames = [findingnodenames ...
        'Bldg1'    'Bldg2'    'Bldg3'    'Bldg4'    'Bldg5'    'Bldg6'    'Bldg7'...
        'Terrain'   'Dispersion'    'BldgHdwr'];    
    allfindings = [allfindings layerfindings];
end

%% SIGINT
if strfind(layersshown,'S')
    layerfindings = 'none';
    incidx = find(strcmp(scenario.incident,'SIGINT'));
    if ~isempty(incidx)
        assert(length(incidx) == 1);
        if strcmp(scenario.incident_conc{incidx},'High'), layerfindings = 'high';
        elseif strcmp(scenario.incident_conc{incidx},'Low'), layerfindings = 'low';
        else error('unknown int');
        end
    end
    
    findingnodenames = [findingnodenames 'SIGINTDensity'];
    allfindings = [allfindings layerfindings];    
end

%% MASINT
if strfind(layersshown,'M')
    layerfindings = {'none' 'none'};
    
    incidx = find(strcmp(scenario.incident,'MASINT'));
    for i = 1:length(incidx)
        if strcmp(scenario.incident_type{incidx(i)},'Chemical X') % MASINT1
            if strcmp(scenario.incident_conc{incidx(i)},'High')
                layerfindings{1} = 'high';
            elseif strcmp(scenario.incident_conc{incidx(i)},'Low')
                layerfindings{1} = 'low';
            else error('unknown int');
            end
        end
        
        if strcmp(scenario.incident_type{incidx(i)},'Chemical Y') % MASINT2
            if strcmp(scenario.incident_conc{incidx(i)},'High')
                layerfindings{2} = 'high';
            elseif strcmp(scenario.incident_conc{incidx(i)},'Low')
                layerfindings{2} = 'low';
            else error('unknown int');
            end
        end
    end
    
    findingnodenames = [findingnodenames 'MASINT1Density' 'MASINT2Density'];
    allfindings = [allfindings layerfindings];    
end
