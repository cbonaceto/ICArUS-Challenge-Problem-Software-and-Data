function scene = pullinfacilitydata(scene,facility,facility_count)

if (facility_count == 4)
    facility_number = [0 0 0 0];
elseif (facility_count == 3)
    facility_number = [500 500 500 500];
elseif (facility_count == 2)
    facility_number = [1000 1000 1000 1000];
end

for i = 1:length(scene) % Scene
    for j = 1:facility_count
        
        for k = 1:length(facility)
            if (strcmp(cell2mat(scene(i).scenario(j).facility),facility(k).name))
                facility_type = k;
                break
            end
        end
        
        % Update the facility "number"; the "pull" from the facility palette
        facility_number(facility_type) = facility_number(facility_type) + 1;
%         scene(i).scenario(j).facility_id = facility_number(facility_type);
        scene(i).scenario(j).facility_palette = facility(facility_type).palette(facility_number(facility_type),:);
        scene(i).scenario(j).facility_dispersion = facility(facility_type).dispersion_vector(facility_number(facility_type));
    end
end
