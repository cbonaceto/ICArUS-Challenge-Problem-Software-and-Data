% Generate training data using properties.m

clear all; clc
scene_count = 500;
facility_count = 4;

if (facility_count == 4)
    facility_number = [0 0 0 0];
elseif (facility_count == 3)
    facility_number = [500 500 500 500];
elseif (facility_count == 2)
    facility_number = [1000 1000 1000 1000];
end

scene_size = 300;
load('facility_palette_PILOT.mat');
load('terrain_palette_PILOT.mat');

% Load properties
properties_file()
scenario=struct('facility',{},'facility_coord',{},'terrain',{}, 'incident',{},'incident_type',{}, ...
    'incident_conc',{},'incident_distr',{});

% "Build" the facility layer by layer
% ----------------------------------------------------
for (i = 1:scene_count) % Scene
    disp(i)
    for (j = 1:facility_count)               % Sector
        
        % Choose a facility
        if (facility_count == 1)
            scenario(i,j).facility = {'Facility A'};     % Facility 1
            ix = 1;
            
        elseif (facility_count == 2)
            
            % Normalize
            facility_sum = sum(p_facility(1)+p_facility(2));
            p_facility(1) = p_facility(1)/facility_sum;
            p_facility(2) = p_facility(2)/facility_sum;
            
            if (rand < p_facility(1))
                scenario(i,j).facility = {'Facility A'}; % Facility 1
                ix = 1;
            else
                scenario(i,j).facility = {'Facility B'}; % Facility 4
                ix = 2;
            end
            
        elseif (facility_count == 3)
            
            % Normalize
            facility_sum = sum(p_facility(1)+p_facility(2)+p_facility(3));
            p_facility(1) = p_facility(1)/facility_sum;
            p_facility(2) = p_facility(2)/facility_sum;
            p_facility(3) = p_facility(3)/facility_sum;
            
            list = cat(2,ones(1,floor(100*p_facility(1))),2*ones(1,floor(100*p_facility(2))),3*ones(1,floor(100*p_facility(4))));
            ix = list(ceil(length(list)*rand));
            
            if (ix == 1)
                scenario(i,j).facility = {'Facility A'}; % Facility 1
            elseif (ix == 2)
                scenario(i,j).facility = {'Facility B'}; % Facility 2
            elseif (ix == 3)
                scenario(i,j).facility = {'Facility C'}; % Facility 4
            end
            
        elseif (facility_count == 4)
            
            list = cat(2,ones(1,100*p_facility(1)),2*ones(1,100*p_facility(2)),3*ones(1,100*p_facility(3)),4*ones(1,100*p_facility(4)));
            ix = list(ceil(100*rand));
            
            if (ix == 1)
                scenario(i,j).facility = {'Facility A'}; % Facility 1
            elseif (ix == 2)
                scenario(i,j).facility = {'Facility B'}; % Facility 2
            elseif (ix == 3)
                scenario(i,j).facility = {'Facility C'}; % Facility 3
            elseif (ix == 4)
                scenario(i,j).facility = {'Facility D'}; % Facility 4
            end
        end
        
        % Define
        scenario(i,j).terrain = [];
        scenario(i,j).incident = [];
        scenario(i,j).incident_distr = [];
        scenario(i,j).incident_type = [];
        scenario(i,j).incident_conc = [];
        
        % Populate
        
        % Terrain
        if (rand < p_terrain_facility(ix))
            scenario(i,j).terrain = cat(2,scenario(i,j).terrain,{'Water'});
        end
        
        % SIGINT
        if (rand < p_sigint_facility(ix))
            scenario(i,j).incident = cat(2,scenario(i,j).incident,{'SIGINT'});
            scenario(i,j).incident_distr = cat(2,scenario(i,j).incident_distr,{'gauss'});
            scenario(i,j).incident_type = cat(2,scenario(i,j).incident_type,{[]});
            
            if (rand < p_sigint_high_density(ix))
                scenario(i,j).incident_conc = cat(2,scenario(i,j).incident_conc,{'High'});
            else
                scenario(i,j).incident_conc = cat(2,scenario(i,j).incident_conc,{'Low'});
            end
        end
        
        % MASINT
        if (rand < p_masint_facility(ix,1))
            scenario(i,j).incident = cat(2,scenario(i,j).incident,{'MASINT'});
            scenario(i,j).incident_distr = cat(2,scenario(i,j).incident_distr,{'gauss'});
            scenario(i,j).incident_type = cat(2,scenario(i,j).incident_type,{'Chemical X'});
            
            if (rand < p_masint_high_concentration(ix))
                scenario(i,j).incident_conc = cat(2,scenario(i,j).incident_conc,{'High'});
            else
                scenario(i,j).incident_conc = cat(2,scenario(i,j).incident_conc,{'Low'});
            end
        end
        
        if (rand < p_masint_facility(ix,2))
            scenario(i,j).incident = cat(2,scenario(i,j).incident,{'MASINT'});
            scenario(i,j).incident_distr = cat(2,scenario(i,j).incident_distr,{'gauss'});
            scenario(i,j).incident_type = cat(2,scenario(i,j).incident_type,{'Chemical Y'});
            
            if (rand < p_masint_high_concentration(ix))
                scenario(i,j).incident_conc = cat(2,scenario(i,j).incident_conc,{'High'});
            else
                scenario(i,j).incident_conc = cat(2,scenario(i,j).incident_conc,{'Low'});
            end
        end
    end
    
    if (facility_count == 2)
        scene(i).scenario= [scenario(i,1),scenario(i,2)]; %#ok<*SAGROW>
    elseif (facility_count == 3)
        scene(i).scenario= [scenario(i,1),scenario(i,2),scenario(i,3)];
    elseif (facility_count == 4)
        scene(i).scenario= [scenario(i,1),scenario(i,2),scenario(i,3),scenario(i,4)];
    end
    
    % Go back and place each facility in the scene.
    % ----------------------------------------------------
    for (j = 1:facility_count)
        for (k = 1:length(facility))
            if (strcmp(cell2mat(scene(i).scenario(j).facility),facility(k).name))
                facility_type = k;
                break
            end
        end
        
        % Update the facility "number"; the "pull" from the facility palette
        facility_number(facility_type) = facility_number(facility_type) + 1;
        
        % Pull the facility map
        temp = squeeze(facility(facility_type).map(facility_number(facility_type),:,:));
        
        %         temp = temp(2:end-1,2:end-1);               % remove 1-cell buffer
        [x,y] = ind2sub(size(temp),find(temp));      % find facility boundaries
        for (k = min(x):max(x))                                % mark up the entire map (to avoid ANY overlap with neighboring facilities)
            for (z = min(y):max(y));
                temp(k,z) = 1;
            end
        end
        F{j} = temp; % save the parsed map
    end
    
    % Randomly place the facilities; save location information to replicate
    % later.
    looping = 1; p = 1;
    while (looping == 1)
        
        if (facility_count < 4) 
            coord = ceil((scene_size-20)*rand(4,2))+10; % Randomly place each facility (with 10 pixel border)
        else
            coord = ceil(50*rand(4,2))+10; % Random placement is too rigid; constrain
            coord(1,:) = coord(1,:);
            coord(2,:) = coord(2,:) + [150 0];
            coord(3,:) = coord(3,:) + [0 150];
            coord(4,:) = coord(4,:) + [150 150];
        end
            
        
        M = zeros(facility_count,scene_size,scene_size);
        for (j = 1:facility_count)
            
            % Add each facility to its own global map
            M(j,coord(j,1):coord(j,1)+size(cell2mat(F(j)),1)-1,coord(j,2):coord(j,2)+size(cell2mat(F(j)),2)-1) = ...
                cell2mat(F(j));
        end
        
        % Check boundary conditions
        %-----------------------------------
        if (size(M,2) > scene_size || size(M,3) > scene_size)
            p = p + 1;
            continue
        end
        
        if (facility_count > 0)
            [x y] = ind2sub(size(squeeze(M(1,:,:))),find(squeeze(M(1,:,:))));
            if (min(x) < 10 || max(x) > scene_size-10 || min(y) < 10 || max(y) > scene_size-10)
                p = p + 1;
                continue
            end
        end
        
        if (facility_count > 1)
            [x y] = ind2sub(size(squeeze(M(2,:,:))),find(squeeze(M(2,:,:))));
            if (min(x) < 10 || max(x) > scene_size-10 || min(y) < 10 || max(y) > scene_size-10)
                p = p + 1;
                continue
            end
        end
        
        if (facility_count > 2)
            [x y] = ind2sub(size(squeeze(M(3,:,:))),find(squeeze(M(3,:,:))));
            if (min(x) < 10 || max(x) > scene_size-10 || min(y) < 10 || max(y) > scene_size-10)
                p = p + 1;
                continue
            end
        end
        
        if (facility_count > 3)
            [x y] = ind2sub(size(squeeze(M(4,:,:))),find(squeeze(M(4,:,:))));
            if (min(x) < 10 || max(x) > scene_size-10 || min(y) < 10 || max(y) > scene_size-10)
                p = p + 1;
                continue
            end
        end
        
        % If boundary conditions are met, check for intersection
        %-----------------------------------
        
        for (j = 1:facility_count)
            % Grow loop to increase the buffer between facilities
            for (k = 1:5) % Minimum 5 pixel inter-facility distance
                M(j,:,:) = grow(squeeze(M(j,:,:)));
            end
        end
        
        % Check for intersection
        if (facility_count == 2)
            if (isempty(intersect(find(squeeze(M(1,:,:))==1),find(squeeze(M(2,:,:))==1))))
                
                scene(i).scenario(1).facility_coord = coord(1,:);
                scene(i).scenario(2).facility_coord = coord(2,:);
                looping = 0; % Stop looping
                
            else % Loop again
                p = p + 1;
                continue
            end
        end
        
        if (facility_count == 3)
            if (isempty(intersect(find(squeeze(M(1,:,:))==1),find(squeeze(M(2,:,:))==1))) && ...
                    isempty(intersect(find(squeeze(M(1,:,:))==1),find(squeeze(M(3,:,:))==1))) && ...
                    isempty(intersect(find(squeeze(M(2,:,:))==1),find(squeeze(M(3,:,:))==1))))
                
                scene(i).scenario(1).facility_coord = coord(1,:);
                scene(i).scenario(2).facility_coord = coord(2,:);
                scene(i).scenario(3).facility_coord = coord(3,:);
                looping = 0; % Stop looping
                
            else % Loop again
                p = p + 1;
                continue
            end
        end
        
        if (facility_count == 4)
            if (isempty(intersect(find(squeeze(M(1,:,:))==1),find(squeeze(M(2,:,:))==1))) && ...
                    isempty(intersect(find(squeeze(M(1,:,:))==1),find(squeeze(M(3,:,:))==1))) && ...
                    isempty(intersect(find(squeeze(M(1,:,:))==1),find(squeeze(M(4,:,:))==1))) && ...
                    isempty(intersect(find(squeeze(M(2,:,:))==1),find(squeeze(M(3,:,:))==1))) && ...
                    isempty(intersect(find(squeeze(M(2,:,:))==1),find(squeeze(M(4,:,:))==1))) && ...
                    isempty(intersect(find(squeeze(M(3,:,:))==1),find(squeeze(M(4,:,:))==1))))
                
                scene(i).scenario(1).facility_coord = coord(1,:);
                scene(i).scenario(2).facility_coord = coord(2,:);
                scene(i).scenario(3).facility_coord = coord(3,:);
                scene(i).scenario(4).facility_coord = coord(4,:);
                looping = 0; % Stop looping
                
            else % Loop again
                p = p + 1;
                continue
            end
        end
        
    end
end

eval(['save scene_palette_PILOT_' num2str(facility_count) 'FAC.mat scene']);
