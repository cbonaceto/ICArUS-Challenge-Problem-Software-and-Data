function [subjects, examTrainingTimes, examTimes, missionTimes, missionTrainingTimes] = ...
    aggregate_csv_time_data(time_csv_file)
%AGGREGATE_CSV_TIME_DATA Summary of this function goes here
%   Detailed explanation goes here
time_data = csv2cell(time_csv_file, 'fromfile');
assert(size(time_data,1) > 1);

%Determine the number of missions and the number of subjects
numMissions = (size(time_data, 2) - 4) / 2;
numSubjects = size(time_data, 1) - 1;

%Get the time data for each subject
subjects = cell(numSubjects, 1);
examTrainingTimes = zeros(numSubjects, 1);
examTimes = zeros(numSubjects, 1);
missionTimes = zeros(numSubjects, numMissions);
missionTrainingTimes = zeros(numSubjects, numMissions);
for subject = 1:numSubjects
     subjects{subject} = time_data{subject+1, 2};
     col = 3;
     for mission = 1:numMissions
         missionTrainingTimes(subject, mission) = str2double(time_data{subject+1, col});
         missionTimes(subject, mission) = str2double(time_data{subject+1, col+1});
         col = col + 2;
     end
     examTimes(subject) = str2double(time_data{subject+1, col});
     examTrainingTimes(subject) = str2double(time_data{subject+1, col + 1});
end

end