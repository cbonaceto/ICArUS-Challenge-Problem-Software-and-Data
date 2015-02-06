% Generates facilities of the same type for comparison.

clear all; clc
facility_number = 16;
MarkerSize = 6;

% Which layers to plot
IMINT = 1;
SIGINT = 1;
MASINT = 1;

% Load Palettes
%----------------------
load('facility_palette_PILOT.mat'); % Run facility_palette.m to generate.
load('object_palette_PILOT.mat');  % Run object_palette.m to generate.
load('terrain_palette_PILOT.mat'); % Run terrain_palette.m to generate
properties_file() % Sets underlying probabilites (TERRAIN, SIGINT, MASINT; IMINT set in facility_palette.m)

% Used for rendering (Note: for testing only)
facility_color = [102 102 102]/256;
masint_color = [0 0.5 0; 1 0 0]; % Green/Red

% Fixed for now
facility_size = 100;

for (i = 1:2) % Loop through 4-facilities
    figure(i)
    for (j = 1:facility_number)
        subplot(4,4,j)
        
        if (j == 1)
            title(['Facility ' num2str(i)])
        end
        
        IMINT_COORD = [];
        SIGINT_COORD = [];
        MASINT_COORD = [];
        
        r = 0;
        % FACILITY
        for (k = 1:size(facility(i).palette,2))
            building_coord = cell2mat(facility(i).palette(j,k));  % [X,Y,objectID,objectOrientation,Hardware]
            if (~isempty(building_coord))
                if (IMINT); draw_object(object{building_coord(3),building_coord(4)},[building_coord(1) building_coord(2)],building_coord(5),facility_color); hold on; end
                IMINT_COORD(k,:) = [building_coord(3) building_coord(4) building_coord(5) building_coord(1) building_coord(2)]; r = r + 1; %#ok<*SAGROW>
            end
        end
        axis([0 max(facility_size) 0 max(facility_size)]);
        axis square
        set(gca,'XTick',[])
        set(gca,'YTick',[])
        box on
        
        % TERRAIN
        if (rand < p_terrain_facility(i))
            terrain_vector =  cell2mat(facility(i).terrain_vector(j));  % [X,Y,objectID,objectOrientation]
             if (IMINT); draw_object(terrain{terrain_vector(3),terrain_vector(4)},[terrain_vector(1) terrain_vector(2)],0,'c'); end
            IMINT_COORD(r+1,:) = [terrain_vector(3)+200 terrain_vector(4) 0 terrain_vector(1) terrain_vector(2)];
        end
        
        facility_center = round(mean(IMINT_COORD));
        X = facility_center(4);
        Y = facility_center(5);
        
        % SIGINT
        if (rand < p_sigint_facility(i))
            if (rand < p_sigint_high_density(i))
                [x y] = sample_distribution('gauss',ceil(6+randn/3),[facility_size,facility_size],[0,0]);
                SIGINT_COORD =  [x'+X-facility_size/2 y'+Y-facility_size/2 j*ones(length(x),1)];
                if (SIGINT); plot(SIGINT_COORD(:,1)+0.5,SIGINT_COORD(:,2)+0.5,'mo','MarkerSize',MarkerSize); end
            else
                [x y] = sample_distribution('gauss',ceil(3+randn/3),[facility_size,facility_size],[0,0]);
                SIGINT_COORD =  [x'+X-facility_size/2 y'+Y-facility_size/2 j*ones(length(x),1)];
                if (SIGINT); plot(SIGINT_COORD(:,1)+0.5,SIGINT_COORD(:,2)+0.5,'mo','MarkerSize',MarkerSize); end
            end
        end
        
        % MASINT
        if (rand < p_masint_facility(i,1))
            masint_type = 1;
            if (rand < p_masint_high_concentration(i))
                [x y] = sample_distribution('gauss',ceil(6+randn/3),[facility_size,facility_size],[0,0]);
                MASINT_COORD =  [x'+X-facility_size/2 y'+Y-facility_size/2 j*ones(length(x),1)];
                for (k = 1:size(MASINT_COORD,1))
                    if (MASINT); plot(MASINT_COORD(k,1)+0.5,MASINT_COORD(k,2)+0.5,'^','MarkerEdgeColor',masint_color(masint_type,:),'MarkerSize',MarkerSize); end
                end
            else
                [x y] = sample_distribution('gauss',ceil(3+randn/3),[facility_size,facility_size],[0,0]);
                MASINT_COORD =  [x'+X-facility_size/2 y'+Y-facility_size/2 j*ones(length(x),1)];
                for (k = 1:size(MASINT_COORD,1))
                     if (MASINT); plot(MASINT_COORD(k,1)+0.5,MASINT_COORD(k,2)+0.5,'^','MarkerEdgeColor',masint_color(masint_type,:),'MarkerSize',MarkerSize); end
                end
            end
        end
        
        if (rand < p_masint_facility(i,2))
            masint_type = 2;
            if (rand < p_masint_high_concentration(i))
                [x y] = sample_distribution('gauss',ceil(6+randn),[facility_size,facility_size],[0,0]);
                MASINT_COORD =  [x'+X-facility_size/2 y'+Y-facility_size/2 j*ones(length(x),1)];
                for (k = 1:size(MASINT_COORD,1))
                     if (MASINT); plot(MASINT_COORD(k,1)+0.5,MASINT_COORD(k,2)+0.5,'^','MarkerEdgeColor',masint_color(masint_type,:),'MarkerSize',MarkerSize); end
                end
            else
                [x y] = sample_distribution('gauss',ceil(3+randn),[facility_size,facility_size],[0,0]);
                MASINT_COORD =  [x'+X-facility_size/2 y'+Y-facility_size/2 j*ones(length(x),1)];
                for (k = 1:size(MASINT_COORD,1))
                     if (MASINT); plot(MASINT_COORD(k,1)+0.5,MASINT_COORD(k,2)+0.5,'^','MarkerEdgeColor',masint_color(masint_type,:),'MarkerSize',MarkerSize); end
                end
            end
        end
    end
end



