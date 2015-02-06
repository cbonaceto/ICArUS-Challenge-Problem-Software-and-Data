function [groupCenters, groupAttacks, locationAttacks] = read_feature_vector_file(fileName)
%READ_FEATURE_VECTOR_FILE Reads a Task 1-7 feature vector file.
%   Detailed explanation goes here

%Read the CSV file
data = csv2cell(fileName, 'fromfile');

%Aggregate group centers and attack locations
groupCenters = [];
groupAttacks = cell(4, 1);
locationAttacks = [];
for row = 2:size(data, 1)
    objectID = data{row, 1};
    objectType = data{row, 2};
    object.x = str2double(data{row, 3});
    object.y = str2double(data{row, 4});
    if strncmp(objectType, 'Gr', 2)
        %Group Center
        object.groupIndex = getGroupIndex(objectID);
        groupCenters = [groupCenters object];         %#ok<*AGROW>
    elseif strncmp(objectType, 'Lo', 2)
        %Attack location
        groupIndex = getGroupIndex(objectID);
        if groupIndex == 0
            object.locationID = objectID;
            locationAttacks = [locationAttacks object];
        else
            object.groupIndex = groupIndex; 
            groupAttacks{groupIndex} = [groupAttacks{groupIndex} object];
        end
    end
end
end

function index = getGroupIndex(groupName)
if groupName == 'A'
    index = 1;
elseif groupName == 'B'
    index = 2;
elseif groupName == 'C'
    index = 3;
elseif groupName == 'D'
    index = 4;
else 
    index = 0;
end
end
