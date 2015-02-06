% Load palette
% ------------------------------

for (i = 1:7)
eval(['[rows,cols] = find(xlsread(''object_palette_PILOT.xlsx'',''sheet' num2str(i) ''')==1);']);
    object{i,1} = [rows,cols]-1; %#ok<*SAGROW>
end

% Rotate palette
% ------------------------------

center = [0,0];
for (i = 1:length(object))
    O = object{i,1};
    
     for (j = 2:4)
        % Define rotation angle (0, 90, 180, and 270 deg.)
        switch j
            case {2}
                a = pi/180 * 90;
            case {3}
                a = pi/180 * 180;
            case {4}
                a = pi/180 * 270;
        end
        
%     for (j = 2:8)
%         switch j
%             case {2}
%                 a = pi/180 * 45;
%             case {3}
%                 a = pi/180 * 90;
%             case {4}
%                 a = pi/180 * 135;
%             case {5}
%                 a = pi/180 * 180;
%             case {6}
%                 a = pi/180 * 225;
%             case {7}
%                 a = pi/180 * 270;
%             case {8}
%                 a = pi/180 * 315;
%         end
        
        % 2D rotation matrix
        R = [cos(a) -sin(a); sin(a) cos(a)];
        
        rotated_object = [];
        for (k = 1:length(O))
            rotated_object(k,:) = center+(R*O(k,:)')'; % Rotate
        end
        x_shift = min(rotated_object(:,1));
        y_shift = min(rotated_object(:,2));
        rotated_object(:,1) = rotated_object(:,1) - x_shift + center(1);
        rotated_object(:,2) = rotated_object(:,2) - y_shift + center(2);
        
        object{i,j} = sortrows(double(uint32(rotated_object))); % Save
    end
end