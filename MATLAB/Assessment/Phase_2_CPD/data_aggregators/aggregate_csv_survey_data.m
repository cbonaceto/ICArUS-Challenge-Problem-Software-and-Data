function [subjects, ages, genders, currentDegrees, highestDegrees, yearsExperience,...
    yearsGeoIntExperience, probsAndStatsTraining, probsAndStatsUsage,...
    geoDataUsage, geoIntExperience, bis, basDrive, basFunSeeking, basRewardResponsiveness,...
    basTotal, foxHedgehog, sbsdt, wlt, crt, vge] = ...
    aggregate_csv_survey_data(survey_csv_file)
%AGGREGATE_CSV_SURVEY_DATA Summary of this function goes here
%   Detailed explanation goes here

%Load the CSV file containing survey data into a cell array
survey_data = csv2cell(survey_csv_file, 'fromfile');
assert(size(survey_data,1) > 1);

%% Determine the starting columns for data to aggregate
ageCol = 0; % The age column
genderCol = 0; % The gender column
currentDegreeCol = 0; % The current degree column
highestDegreeCol = 0; % the highest degree type column
yearsCol = 0; % The years of experience column
yearsGeoIntCol = 0; % The years of GeoInt experience column
probsAndStatsTrainingCol = 0; % The probability and statistics training level column
probsAndStatsUsageCol = 0; % The probability and statistics usage frequency column
geoDataUsageCol = 0; % The geospatial data usage frequency column
geoIntExperienceCol = 0; % The GeoInt experience level column
bisCol = 0; % The BIS column
basDriveCol = 0; % The BAS Drive column
basFunCol = 0; % The BAS Fun Seeking column
basRewardCol = 0; % The BAS Reward Responsiveness column
basTotalCol = 0; % The BAS Total column
foxHedgehogCol = 0; % The Fox/Hedgehog score column
sbsdtCol = 0; %Santa Barbara Sense of Direction Test score column
wltCol = 0; %Water Test Level score column
crtCol = 0; % The Cognitive Reflections Test score column
vgeCol = 0; %Video Game Experience score column
for col = 1:size(survey_data, 2)
    str = survey_data{1, col};
    if ageCol == 0 && strncmp(str, 'Age', 3)
        ageCol = col;    
    end
    if genderCol == 0 && strncmp(str, 'Gender', 6)
        genderCol = col;
    end
    if currentDegreeCol == 0 && strncmp(str, 'CurrentD', 8)
        currentDegreeCol = col;
    end
    if highestDegreeCol == 0 && strncmp(str, 'HighestD', 8)
        highestDegreeCol = col;
    end
    if yearsCol == 0 && strncmp(str, 'YearsE', 6)
        yearsCol = col;
    end
    if yearsGeoIntCol == 0 && strncmp(str, 'YearsG', 6)
        yearsGeoIntCol = col;
    end
    if probsAndStatsTrainingCol == 0 && strncmp(str, 'ProbabilityAndStatsT', 20)
        probsAndStatsTrainingCol = col;
    end
    if probsAndStatsUsageCol == 0 && strncmp(str, 'ProbabilityAndStatsF', 20)
        probsAndStatsUsageCol = col;
    end    
    if geoDataUsageCol == 0 && strncmp(str, 'Geospa', 6)
        geoDataUsageCol = col;
    end
    if geoIntExperienceCol == 0 && strncmp(str, 'Geoint', 6)
        geoIntExperienceCol = col;
    end
    if bisCol == 0 && strncmp(str, 'BIS', 3)
        bisCol = col;
    end
    if basDriveCol == 0 && strncmp(str, 'BASD', 4)
        basDriveCol = col;
    end
    if basFunCol == 0 && strncmp(str, 'BASF', 4)
        basFunCol = col;
    end
    if basRewardCol == 0 && strncmp(str, 'BASR', 4)
        basRewardCol = col;
    end
    if basTotalCol == 0 && strncmp(str, 'BAST', 4)
        basTotalCol = col;
    end
    if foxHedgehogCol == 0 && strncmp(str, 'Fox', 3)
        foxHedgehogCol = col;
    end
    if sbsdtCol == 0 && strncmp(str, 'SBSDT', 5)
        sbsdtCol = col;
    end
    if wltCol == 0 && strncmp(str, 'WLT', 3)
        wltCol = col;
    end
    if crtCol == 0 && strncmp(str, 'CRT', 3)
        crtCol = col;
    end
     if vgeCol == 0 && strncmp(str, 'VGE', 3)
        vgeCol = col;
    end
end

%% Determine the number of subjects
numSubjects = size(survey_data, 1) - 1;
assert(numSubjects > 0);

%% Aggregate data
subjects = cell(numSubjects, 1);
ages = zeros(numSubjects, 1);
genders = cell(numSubjects, 1);
if currentDegreeCol > 0
    currentDegrees = zeros(numSubjects, 1);
else
    currentDegrees = [];
end
if highestDegreeCol > 0
    highestDegrees = zeros(numSubjects, 1);
else
    highestDegrees = [];
end
if yearsCol > 0
    yearsExperience = zeros(numSubjects, 1);
else
    yearsExperience = [];
end
if yearsGeoIntCol > 0
    yearsGeoIntExperience = zeros(numSubjects, 1);
else 
    yearsGeoIntExperience = [];
end
if probsAndStatsTrainingCol > 0
    probsAndStatsTraining = zeros(numSubjects, 1);
else 
    probsAndStatsTraining = [];
end
if probsAndStatsUsageCol > 0
    probsAndStatsUsage = zeros(numSubjects, 1); %A
else 
    probsAndStatsUsage = [];
end
if geoDataUsageCol > 0
    geoDataUsage = zeros(numSubjects, 1);
else
    geoDataUsage = [];
end
if geoIntExperienceCol > 0
    geoIntExperience = zeros(numSubjects, 1);
else
    geoIntExperience = [];
end
bis = zeros(numSubjects, 1);
basDrive = zeros(numSubjects, 1);
basFunSeeking = zeros(numSubjects, 1);
basRewardResponsiveness = zeros(numSubjects, 1);
basTotal = zeros(numSubjects, 1);
if foxHedgehogCol > 0
    foxHedgehog = zeros(numSubjects, 1);
else
    foxHedgehog = [];
end
if sbsdtCol > 0
    sbsdt = zeros(numSubjects, 1);
else
    sbsdt = [];
end
if wltCol > 0
    wlt = zeros(numSubjects, 1);
else
    wlt = [];
end
if crtCol > 0
    crt = zeros(numSubjects, 1);
else
    crt = [];
end
if vgeCol > 0
    vge = zeros(numSubjects, 1);
else
    vge = [];
end
for row = 2:size(survey_data,1)
    subjectNum = row - 1;
    subjects{subjectNum} = survey_data{row, 2};
    if ageCol > 0
        ages(subjectNum) = str2double(survey_data{row, ageCol});
    end
    if genderCol > 0
        genders{subjectNum} = survey_data{row, genderCol};
    end
    if yearsCol > 0
        yearsExperience(subjectNum) = str2double(survey_data{row, yearsCol});
    end
    if yearsGeoIntCol > 0
        yearsGeoIntExperience(subjectNum) = str2double(survey_data{row, yearsGeoIntCol});
    end
    if currentDegreeCol > 0
        currentDegrees(subjectNum) = getCurrentDegreeLevel(survey_data{row, currentDegreeCol});
    end
    if highestDegreeCol > 0
        highestDegrees(subjectNum) = getHighestDegreeLevel(survey_data{row, highestDegreeCol});
    end
    if probsAndStatsTrainingCol > 0
        probsAndStatsTraining(subjectNum) = str2double(survey_data{row, probsAndStatsTrainingCol});
    end    
    if probsAndStatsUsageCol > 0
        probsAndStatsUsage(subjectNum) = str2double(survey_data{row, probsAndStatsUsageCol});
    end
    if geoDataUsageCol > 0
        geoDataUsage(subjectNum) = str2double(survey_data{row, geoDataUsageCol});
    end
    if geoIntExperienceCol > 0
        geoIntExperience(subjectNum) = str2double(survey_data{row, geoIntExperienceCol});
    end    
    if bisCol > 0
        bis(subjectNum) = str2double(survey_data{row, bisCol});
    end
    if basDriveCol > 0
        basDrive(subjectNum) = str2double(survey_data{row, basDriveCol});
    end
    if basFunCol > 0
        basFunSeeking(subjectNum) = str2double(survey_data{row, basFunCol});
    end
    if basRewardCol > 0
        basRewardResponsiveness(subjectNum) = str2double(survey_data{row, basRewardCol});
    end
    if basTotalCol > 0
        basTotal(subjectNum) = str2double(survey_data{row, basTotalCol});
    end    
    if foxHedgehogCol > 0
        foxHedgehog(subjectNum) = str2double(survey_data{row, foxHedgehogCol});
    end
    if sbsdtCol > 0
        sbsdt(subjectNum) = str2double(survey_data{row, sbsdtCol});
    end
    if wltCol > 0
        wlt(subjectNum) = str2double(survey_data{row, wltCol});
    end
    if crtCol > 0
        crt(subjectNum) = str2double(survey_data{row, crtCol});
    end
    if vgeCol > 0
        vge(subjectNum) = str2double(survey_data{row, vgeCol});
    end
end

%% Function to get the degree level number given the educational degree type string
function degreeLevel = getHighestDegreeLevel(degree)
    if strncmp(degree, 'Assoc', 5)
        degreeLevel = 1;
    elseif strncmp(degree, 'Bach', 4)
        degreeLevel = 2;
    %elseif strncmp(degree, 'Cert', 4)
    %    degreeLevel = 3;
    elseif strncmp(degree, 'Mast', 4)
        degreeLevel = 3;
    elseif strncmp(degree, 'PhD', 3)
        degreeLevel = 4;
    else
        degreeLevel = 0;
    end
end

function degreeLevel = getCurrentDegreeLevel(degree)
    if strncmp(degree, 'Assoc', 5)
        degreeLevel = 1;
    elseif strncmp(degree, 'Bach', 4)
        degreeLevel = 2;
    elseif strncmp(degree, 'Cert', 4)
       degreeLevel = 3;
    elseif strncmp(degree, 'Mast', 4)
        degreeLevel = 4;
    elseif strncmp(degree, 'PhD', 3)
        degreeLevel = 5;
    else
        degreeLevel = 0;
    end
end

end