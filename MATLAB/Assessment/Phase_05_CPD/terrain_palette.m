% Terrain Palette
% ------------------------------

% Load palette
% ------------------------------

for (i = 1:2)
eval(['[rows,cols] = find(xlsread(''terrain_palette_PILOT.xlsx'',''sheet' num2str(i) ''')==1);']);
    terrain{i,1} = [rows,cols]-1; %#ok<*SAGROW>
end

% Rotate palette
% ------------------------------

center = [0,0];
for (i = 1:length(terrain))
    T = terrain{i};
    for (j = 2:4)
        % Define rotation angle (0, 90, 180, and 270 deg.)
        switch j
            case {1}
                a = pi/180 * 0;
            case {2}
                a = pi/180 * 90;
            case {3}
                a = pi/180 * 180;
            case {4}
                a = pi/180 * 270;
        end
        
        % 2D rotation matrix
        R = [cos(a) -sin(a); sin(a) cos(a)];
        
        rotated_terrain = [];
        for (k = 1:length(T))
            rotated_terrain(k,:) = center+(R*T(k,:)')'; %#ok<*SAGROW> % Rotate
        end
        x_shift = min(rotated_terrain(:,1));
        y_shift = min(rotated_terrain(:,2));
        rotated_terrain(:,1) = rotated_terrain(:,1) - x_shift + center(1);
        rotated_terrain(:,2) = rotated_terrain(:,2) - y_shift + center(2);
        
        terrain{i,j} = sortrows(double(uint32(rotated_terrain))); % Save
    end
end