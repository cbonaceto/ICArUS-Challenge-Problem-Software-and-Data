clear all; close all; clc
% Main generates the feature vector and/or renders the image for testing.
% It assumes the object, terrain, and scene palettes have been pre-computed.

facility_count = 2;             % Chooses the facility palette; 2-4 facilities allowed
scene_count = 300;              % How many scenes to make (limited by the facility/scene palettes)
write_feature_vector = 1; % Generate feature vectors? (yes = 1; no = 0)
plot_image = 1;                 % Plot/save image for inspection (yes = 1; no = 0)
annotate = 1;                     % Annotate a saved image? (yes = 1; no = 0)

% Fixed for now
facility_size = 100;
scene_size = 300;

% Used for rendering (Note: for testing only)
facility_color = [102 102 102]/256;
masint_color = [0 0.5 0; 1 0 0]; % Green/Red

s = RandStream('mcg16807','Seed', 0);
RandStream.setDefaultStream(s)

% Load Palettes
%----------------------
load('object_palette_PILOT.mat');  % Run object_palette.m to generate
load('terrain_palette_PILOT.mat'); % Run terrain_palette.m to generate
load('facility_palette_PILOT.mat'); % Run facility_palette.m to generate
eval(['load(''scene_palette_PILOT_' num2str(facility_count) 'FAC.mat'');']); % Run scene_palette.m to generate

if (facility_count == 2)
    facility_number = [0 0 0 0];
elseif (facility_count == 3)
    facility_number = [500 500 500 500];
elseif (facility_count == 4)
    facility_number = [1000 1000 1000 1000];
end
shift = facility_number(1);

% Generate Stimuli
%----------------------

% Scene
for (i = 1:scene_count)
    SCENE(i).OBJ = [];
    SCENE(i).SIG = [];
    SCENE(i).MAS = [];
    
    if (plot_image)
        scrsz = get(0,'ScreenSize');
        figure('Position',[200 scrsz(4)/10 scrsz(3)/1.25 scrsz(4)/1.25])
        hold on
    end
    
    % Sector
    for (j = 1:facility_count)
        
        for (k = 1:length(facility))
            if (strcmp(cell2mat(scene(i).scenario(j).facility),facility(k).name))
                facility_type = k;
                break
            end
        end
        
        % Update the facility "number"; the "pull" from the facility palette
        facility_number(facility_type) = facility_number(facility_type) + 1;
        
        % IMINT
        
        % Add facilities
        %----------------------
        
        % Facility location
        facility_coord = scene(i).scenario(j).facility_coord;
        
        r = 0;
        for (k = 1:size(facility(facility_type).palette,2))
            
            % Building location
            building_coord = cell2mat(facility(facility_type).palette(facility_number(facility_type),k));  % [X,Y,objectID,objectOrientation,Hardware]
            
            if (~isempty(building_coord))
                r = r + 1;
                building_coord(1) = facility_coord(1) + building_coord(1); % Adjust based on facility location
                building_coord(2) = facility_coord(2) + building_coord(2);
                
                if (plot_image)
                    draw_object(object{building_coord(3),building_coord(4)},[building_coord(1) building_coord(2)],building_coord(5),facility_color);
                end
                
                SCENE(i).OBJ(j).COORD(k,:) = [building_coord(3) building_coord(4) building_coord(5) building_coord(1) building_coord(2) j]; %#ok<*SAGROW>
                % [objectID,objectOrientation,Hardware,X,Y]
            end
        end
        
        if (strfind('Water',cell2mat(scene(i).scenario(j).terrain)))
            terrain_vector =  cell2mat(facility(facility_type).terrain_vector(facility_number(facility_type)));  % [X,Y,objectID,objectOrientation]
            if (plot_image)
                draw_object(terrain{terrain_vector(3),terrain_vector(4)},[facility_coord(1)+terrain_vector(1) facility_coord(2)+terrain_vector(2)],0,'b');
            end
            SCENE(i).OBJ(j).COORD(r+1,:) = [terrain_vector(3)+200 terrain_vector(4) 0 facility_coord(1)+terrain_vector(1) facility_coord(2)+terrain_vector(2) j];
        end
        
        % Extract center of facility
        facility_center = round(mean(SCENE(i).OBJ(j).COORD,1));
        X = facility_center(4);
        Y = facility_center(5);
        
        if (plot_image)
            axis([0 scene_size 0 scene_size])
            axis square
            box on
            set(gca,'XTick',[])
            set(gca,'YTick',[])
            
            if (annotate)
                h = text(facility_coord(1)+5,facility_coord(2)+5,['Sector ' num2str(j)]);
                set(h,'FontSize',8);
            end
        end
        
        % SIGINT gereration
        %----------------------
        
        if (strfind(cell2mat(scene(i).scenario(j).incident),'SIGINT'))
            
            for (k = 1:length(scene(i).scenario(j).incident))
                if (strfind(cell2mat(scene(i).scenario(j).incident(k)),'SIGINT'))
                    ix = k;
                    break
                end
            end
            D = cell2mat(scene(i).scenario(j).incident_distr(ix));
            
            if (strfind(cell2mat(scene(i).scenario(j).incident_conc(ix)),'High'))
                [x y] = sample_distribution(D,ceil(6+randn),[facility_size,facility_size],[0,0]);
            elseif (strfind(cell2mat(scene(i).scenario(j).incident_conc(ix)),'Low'))
                [x y] = sample_distribution(D,ceil(3+randn),[facility_size,facility_size],[0,0]);
            end
            
            SCENE(i).SIG(j).COORD =  [x'+X-facility_size/2 y'+Y-facility_size/2 j*ones(length(x),1)];
            
            if (plot_image)
                for (k = 1:size(SCENE(i).SIG(j).COORD,1)) % SIG
                    plot(SCENE(i).SIG(j).COORD(k,1)+0.5,SCENE(i).SIG(j).COORD(k,2)+0.5,'mo','MarkerSize',5);
                end
            end
        end
        
        % MASINT gereration
        %----------------------
        ix = [];
        if (strfind(cell2mat(scene(i).scenario(j).incident),'MASINT'))
            
            for (k = 1:length(scene(i).scenario(j).incident))
                if (strfind(cell2mat(scene(i).scenario(j).incident(k)),'MASINT'))
                    ix(k) = k;
                end
            end
            ix = find(ix);
            
            for (p = 1:length(ix))
                D = cell2mat(scene(i).scenario(j).incident_distr(ix(p)));
                
                if (strcmp('Chemical X',scene(i).scenario(j).incident_type(ix(p))))
                    masint_type = 1;
                elseif (strcmp('Chemical Y',scene(i).scenario(j).incident_type(ix(p))))
                    masint_type = 2;
                end
                
                if (strfind(cell2mat(scene(i).scenario(j).incident_conc(ix(p))),'High'))
                    [x y pxy] = sample_distribution(D,ceil(6+randn/3),[facility_size,facility_size],[0,0]);
                    for (k = 1:length(x))
                        SCENE(i).MAS(j).TYPE{p}(k) = masint_type;
                    end
                    SCENE(i).MAS(j).COORD{p} = [x'+X-facility_size/2 y'+Y-facility_size/2 j*ones(length(x),1)];
                    
                    if (plot_image)
                        for (k = 1:size(SCENE(i).MAS(j).COORD{p},1)) % MAS
                            plot(SCENE(i).MAS(j).COORD{p}(k,1)+0.5,SCENE(i).MAS(j).COORD{p}(k,2)+0.5,'^','MarkerEdgeColor', ...
                                masint_color(masint_type,:),'MarkerSize',5);
                        end
                    end
                else % if Low
                    [x y pxy] = sample_distribution(D,ceil(3+randn/3),[facility_size,facility_size],[0,0]);
                    for (k = 1:length(x))
                        SCENE(i).MAS(j).TYPE{p}(k) = masint_type;
                    end
                    SCENE(i).MAS(j).COORD{p} = [x'+X-facility_size/2 y'+Y-facility_size/2 j*ones(length(x),1)];
                    
                    if (plot_image)
                        for (k = 1:size(SCENE(i).MAS(j).COORD{p},1)) % MAS
                            plot(SCENE(i).MAS(j).COORD{p}(k,1)+0.5,SCENE(i).MAS(j).COORD{p}(k,2)+0.5,'^','MarkerEdgeColor', ...
                                masint_color(masint_type,:),'MarkerSize',5);
                        end
                    end
                end
            end
        end
        
        % Draw borders
        X = facility_coord(1);
        Y = facility_coord(2);
        h = line([X X+facility_size],[Y Y]); set(h,'LineStyle','--','Color','k');
        h = line([X+facility_size X+facility_size],[Y Y+facility_size]); set(h,'LineStyle','--','Color','k');
        h = line([X X+facility_size],[Y+facility_size,Y+facility_size]); set(h,'LineStyle','--','Color','k');
        h = line([X X],[Y Y+facility_size]); set(h,'LineStyle','--','Color','k');
    end
    
    % Feature vector generation
    %----------------------
    
    clear FV fvim temp
    fvsig = {};
    fvmas = {};
    
    % Key
    FV{1,1} = 'LayerID';
    FV{1,2} = 'LayerType';
    FV{1,3} = 'ObjectID';
    FV{1,4} = 'ObjectOrientation';
    FV{1,5} = 'Data';
    FV{1,6} = 'X';
    FV{1,7} = 'Y';
    FV{1,8} = 'Sector';
    
    % Construct feature vector
    fvim(:,[3 4 5 6 7 8])  = num2cell(cat(1,SCENE(i).OBJ.COORD));
    fvim(:,1) = {1};
    fvim(:,2) = {'IMINT'};
    %----------------------
    
    if (~isempty(SCENE(i).SIG)==1)
        fvsig(:,6:8) = num2cell(cat(1,SCENE(i).SIG.COORD));
        fvsig(:,1) = {2};
        fvsig(:,2) = {'SIGINT'};
        fvsig(:,3) = {1};
        fvsig(:,4) = {[]};
        fvsig(:,5) = {1};
    end
    
    %----------------------
    
    if (~isempty(SCENE(i).MAS)==1)
        for (j = 1:length(SCENE(i).MAS))
            for (k = 1:length(SCENE(i).MAS(j).TYPE))
                clear fvmas_temp
                fvmas_temp(:,3) = num2cell(SCENE(i).MAS(j).TYPE{k}');
                fvmas_temp(:,5) = {1};
                fvmas_temp(:,6:8) = num2cell(SCENE(i).MAS(j).COORD{k});
                fvmas = cat(1,fvmas,fvmas_temp);
            end
        end
        fvmas(:,1) = {3};
        fvmas(:,2) = {'MASINT'};
        fvmas(:,4) = {[]};
    end
    
    % Write to file
    FV = cat(1,FV,fvim,fvsig,fvmas);
    if (write_feature_vector)
        cd output
        xlswrite(['feature_vector' num2str(i+shift) '.xls'],FV);
        cd ..
    end
    
    if (plot_image)
        cd output
        saveas(gcf, ['image' num2str(i+shift)], 'jpg');
        close all
        cd ..
    end
    
end

% Object palette generation
%----------------------

% cd output
% OP = [];
% for (i = 1:size(object,1))
%     for (j = 1:size(object,2))
%         OP = cat(1,OP,[i*ones(length(object{i,j}),1) j*ones(length(object{i,j}),1) object{i,j}+1]);
%     end
% end
% OP = cat(1,[{'ObjectType'},{'ObjectOrientation'},{'X'},{'Y'}],num2cell(OP));
%
% WP = [];
% for (i = 1:size(terrain,1))
%     for (j = 1:size(terrain,2))
%         WP = cat(1,WP,[i*ones(length(terrain{i,j}),1)+200 j*ones(length(terrain{i,j}),1) terrain{i,j}+1]);
%     end
% end
% WP = num2cell(WP);
%
% % Write to file
% xlswrite('object_palette_vector.xls',cat(1,OP,WP));
% cd ..

% Plot object palette
% ----------------------
% load_object_palette()
% % for (j = 1:size(object,1))
% for (j = 2)
%     figure
%     for (i = 1:8)
%         subplot(3,3,i)
%
%         % draw_object(ID, Orientation (1-4), Center, Hardware, Color)
%         draw_object(object{j,i},[0 0],0,'b')
%
%         axis([0 50 0 50])
%         axis square
%         box on
%         grid on
%         set(gca,'GridLineStyle',':')
%     end
% end

% Plot distribution palette
%----------------------
% names={'uniform', 'quadCV', 'quadCX', 'tanhCV', 'tanhCX','gauss'};
% facility_size = 100;
% colors={'k' 'b' 'g' 'r' 'm','c'};
%
% fh1=figure;
%  set(gcf,'color','w');
%  set(gcf,'name','marginal spatial probability distributions');
%  hold on;
%
% fh2=figure;
%  set(gcf,'color','w');
%  set(gcf,'name','joint spatial probability distributions');
%
%     for i=1:length(names)
%        [tx,ty,px,py,pxy]=spatial_distribution_palette(names{i},[facility_size,facility_size],[0,0]);
%
%        figure(fh1);
%         plot(tx,px, [colors{i},'.-'], 'linewidth',2); hold on
%
%       figure(fh2);
%        subplot(2,3,i);
%        surf(pxy);
%        title(names{i});
%     end
%
%  figure(fh1);
%   xlabel('x');
%   ylabel('px');
%   legend(names);
%
%  figure(fh2);
%   xlabel('x');
%   ylabel('y');
%   xlabel('pxy');
