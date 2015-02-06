function [facility_feature_vector facility_global_map object_vector_out orientation_vector_out dispersion_out dispersion_coefficent_out terrain_feature_vector] ...
    = generate_facility_palette(facility_type,facility,object,samples,N,terrain)

% Match facility string
%---------------------------
for (i = 1:length(facility))
    if (strcmp(facility_type,facility(i).name))
        ix = i;
    end
end
if (isempty(ix))
    error('Facility not defined');
end
target_spatial_distr = cell2mat(facility(ix).spatial_distr);

% Generate facilities
%---------------------------
i = 1;
while (i <= samples)
    try
        if (mod(i,100)==0)
            disp(i)
        end
        
        % Create a global facility map; buffer the perimeter
        M = zeros(N,N);
        M(1,:) = ones(size(M(1,:)));
        M(end,:) = ones(size(M(end,:)));
        M(:,1) = ones(size(M(:,1)));
        M(:,end) = ones(size(M(:,end)));
        
        % Determine which objects to include
        z = 1; object_vector = [];
        for (j = 1:length(facility(ix).object_occurrence))
            if (facility(ix).object_occurrence(j) == 1)
                object_vector(z) = facility(ix).object_type(j); z=z+1; %#ok<*SAGROW>
            else
                if (rand < facility(ix).object_occurrence(j))
                    object_vector(z) = facility(ix).object_type(j); z=z+1;
                end
            end
        end
        object_vector = sortrows([object_vector; randperm(length(object_vector))]',2);
        object_vector = object_vector(:,1);
        object_vector_out{i} = object_vector';
        
        % Determine each object's orientation (randomly for now)
        orientation_vector = ceil(4*rand(length(object_vector),1));
        orientation_vector_out{i} = orientation_vector';
        
        % Determine the facility's dispersion
        list = cat(2,ones(1,floor(100*facility(ix).dispersion(1))),2*ones(1,floor(100*facility(ix).dispersion(2))), ...
            3*ones(1,floor(100*facility(ix).dispersion(3))));
        temp = list(ceil(length(list)*rand));
        if (temp == 1)
            dispersion_coefficient = 0.2+0.1/6*randn(1,length(object_vector));
            dispersion = 1;
        elseif (temp == 2)
            dispersion_coefficient = 0.3+0.1/6*randn(1,length(object_vector));
            dispersion = 2;
        else
            dispersion_coefficient = 0.4+0.1/6*randn(1,length(object_vector));
            dispersion = 3;
        end
        dispersion_coefficent_out{i} = dispersion_coefficient;
        dispersion_out(i) = dispersion;
        
        % Determine if hardware should be added
        object_hardware = [];
        list = cat(2,ones(1,floor(100*facility(ix).object_hardware(1))),2*ones(1,floor(100*facility(ix).object_hardware(2))), ...
            3*ones(1,floor(100*facility(ix).object_hardware(3))));
        temp = list(ceil(length(list)*rand));
        if (temp == 1) % No hardware
            object_hardware = zeros(1,length(object_vector));
        elseif (temp == 2) % Hardware on 1 building
            object_hardware = zeros(1,length(object_vector));
            object_hardware(ceil(rand*length(object_vector))) = 1;
        else % Hardware on more than 1 building
            object_hardware = zeros(1,length(object_vector));
            temp1 = randperm(length(object_vector));
            temp2 = ceil(rand*(length(object_vector)-1))+1; % Random number between 2 and length(object_vector);
            object_hardware(temp1(1:temp2)) = ones(1,length(temp1(1:temp2)));
        end
        
        %  Place the objects
        for (j = 1:length(object_vector)+1)
            constraint_loop = 1; p = 1;
            while (constraint_loop == 1) % Loop until the constraints are met
                
                % Error if the constraints can't be met
                if (p > 1000)
                    error('Cannot satistfy constraints!');
                end
                
                if (j <= length(object_vector))
                    % Find object loctions
                    switch target_spatial_distr
                        case {'config1'} % Distance from the center
                            temp = object{object_vector(j),orientation_vector(j)};
                            object_center = floor([max(temp(:,1))/2 max(temp(:,2))/2]);
                            
                            if (j == 1)
                                A(j) = NaN; %#ok<*AGROW>
                                X(j) = N/2-object_center(1);
                                Y(j) = N/2-object_center(2);
                            else
                                D(j) =  N*dispersion_coefficient(j); % Distance from center
                                A(j) = ceil(360*rand); % Angle from the center
                                X(j) = ceil(N/2+D(j)*cos(A(j)*(pi/180)))-object_center(1);
                                Y(j) = ceil(N/2+D(j)*sin(A(j)*(pi/180)))-object_center(2);
                            end
                            
                            temp(:,1) = temp(:,1) + X(j);
                            temp(:,2) = temp(:,2) + Y(j);
                            
                        case {'config2'} % Distance from the last object
                            temp = object{object_vector(j),orientation_vector(j)};
                            object_center = floor([max(temp(:,1))/2 max(temp(:,2))/2]);
                            
                            if (j == 1)
                                A(j) = NaN;
                                X(j) = N/2-object_center(1);
                                Y(j) = N/2-object_center(2);
                            else
                                D(j) =  N*dispersion_coefficient(j);  % Distance from object j-1
                                A(j) = ceil(360*rand); % Angle from object 1
                                X(j) = ceil(X(j-1)+D(j)*cos(A(j)*(pi/180)))-object_center(1);
                                Y(j) = ceil(Y(j-1)+D(j)*sin(A(j)*(pi/180)))-object_center(2);
                            end
                            
                            temp(:,1) = temp(:,1) + X(j);
                            temp(:,2) = temp(:,2) + Y(j);
                    end
                    
                elseif (j > length(object_vector))
                    % Add terrain
                    terrain_vector = ceil(size(terrain,1)*rand);
                    terrain_orientation = ceil(4*rand);
                    
                    temp = terrain{terrain_vector,terrain_orientation};
                    terrain_center = floor([max(temp(:,1))/2 max(temp(:,2))/2]);
                    
                    Xt = ceil(N/2+ceil(N*rand)*cos(ceil(360*rand)*(pi/180)))-terrain_center(1);
                    Yt = ceil(N/2+ceil(N*rand)*sin(ceil(360*rand)*(pi/180)))-terrain_center(2);
                    
                    temp(:,1) = temp(:,1) + Xt;
                    temp(:,2) = temp(:,2) + Yt;
                end
                
                % CHECKS
                % Check boundary conditions
                if (max(temp(:)) >= N || min(temp(:)) <= 1)
                    p = p + 1;
                    continue
                end
                
                % Create an object amp
                objectM = zeros(N,N);
                for (k = 1:length(temp))
                    objectM(temp(k,1),temp(k,2)) = 1;
                end
                objectM = grow(objectM); % Buffer
                
                % Compare global facility map (M) the object map (objectM)
                if (isempty(intersect(find(M==1),find(objectM==1)))) % If overlap
                    constraint_loop = 0; % Stop looping
                else % Loop again
                    p = p + 1;
                end
            end
            
            % If everything checks out, update the global map and generate
            % the feature vector inputs
            M(union(find(M),find(objectM))) = ones(length(union(find(M),find(objectM))),1);
            
            if (j <= length(object_vector))
                facility_feature_vector(i,j) = {[X(j) Y(j) object_vector(j) orientation_vector(j) object_hardware(j)]};  % [X,Y,objectID,objectOrientation,Hardware]
            elseif (j > length(object_vector))
                terrain_feature_vector(i) = {[Xt Yt terrain_vector terrain_orientation]};
            end
        end
        
        facility_global_map(i,:,:) = M(1:N,1:N); % Global facility map
        i = i + 1;
        
    catch me
        % Try again
    end
end

