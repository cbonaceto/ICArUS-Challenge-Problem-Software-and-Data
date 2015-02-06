function plot_subject_demographics(subjects, ages, genders,...
    currentDegrees, highestDegrees, probsAndStatsTraining, probsAndStatsUsage,...
    geoDataUsage, geoIntExperience, saveData, showPlots, dataFolder)
%PLOT_SUBJECT_DEMOGRAPHICS Displays subject demographic data

if ~exist('saveData', 'var')
    saveData = false;
end
if saveData && (~exist('dataFolder', 'var') || isempty(dataFolder))
    dataFolder = '';
end
if ~exist('showPlots', 'var') || showPlots
    visible = 'on';
else 
    visible = 'off';
end

numSubjects = length(subjects);

%Compute mean, min, and max age
meanAge = mean(ages);
minAge = min(ages);
maxAge = max(ages);

%Compute number of male and female subjects
numMale = 0;
for i = 1:numSubjects
    if strncmp(genders{i}, 'M', 1)
        numMale = numMale + 1;
    end
end
numFemale = numSubjects - numMale;

%% Plot subject age, (show mean/min/max age and num male/num female in title) 
figPosition = [60, 60, 1000, 600];
figName = 'Subject Ages and Genders';
figHandle = figure('name', figName, 'position', figPosition,...
    'NumberTitle', 'off', 'Visible', visible);
title([figName '   (mean age=' num2str(meanAge), ', min age=' num2str(minAge)...
    ', max age=' num2str(maxAge), ', male N=' num2str(numMale),...
    ', female N=', num2str(numFemale), ')']);
hold on;
xlabel('Age');
ylabel('Number of Subjects');
hist(ages);
if saveData
    fileName = [dataFolder, '\', 'Subject_Ages_Genders'];
    saveas(figHandle, fileName, 'png');
end

%% Plot highest degrees earned, level of training in probs and stats, frequency
%  of probs and stats usage, frequency of geospatial data usage, and
%  proficiency in GeoInt analysis
for plotType = 1:6
    switch plotType
        case 1
            % Plot highest degrees earned
            % Levels: 0=High School, 1=Assoc, 2=Bachelor, 3=Mas, 4=Phd
            levels = 0:4;
            dataCounts = histc(highestDegrees, levels);
            %dataCounts(1) = numSubjects - sum(dataCounts);
            %levels = [0 levels];
            dataTypes = {'High School', 'Associate', 'Bachelor', 'Master', 'PhD'};
            figName = 'Highest Degree Earned';
            figFile = 'Subject_Highest_Degree_Earned';
        case 2
            % Plot current degree programs
            % Levels: 0=None/Prof, 1=Assoc, 2=Bachelor, 3=Cert, 4=Mas, 5=Phd
            levels = 0:5;
            dataCounts = histc(currentDegrees, levels);            
            dataTypes = {'None', 'Associate', 'Bachelor', 'Master', 'Certificate', 'PhD'};
            figName = 'Current Degree Program';
            figFile = 'Subject_Current_Degree_Program';            
        case 3
            % Plot level of training in probability and statistics
            levels = 0:3;
            dataCounts = histc(probsAndStatsTraining, levels);
            dataTypes = {'None', 'Elementary', 'Intermediate', 'Advanced'};
            figName = 'Training in Probability and Statistics';
            figFile = 'Training_Probs_Stats';
        case 4
            % Plot frequency of usage of probability and statistics
            levels = 0:3;
            dataCounts = histc(probsAndStatsUsage, levels);
            dataTypes = {'Never', 'Rareley', 'Occasionally', 'Often'};
            figName = 'Usage of Probability and Statistics';
            figFile = 'Usage_Probs_Stats';
        case 5
            % Plot frequency of usage of geospatial data
            levels = 0:3;
            dataCounts = histc(geoDataUsage, levels);
            dataTypes = {'Never', 'Rareley', 'Occasionally', 'Often'};
            figName = 'Usage of Geospatial Data';
            figFile = 'Usage_Geospatial_Data';
        case 6
            % Plot expertise in geospatial intelligence analysis
            levels = 0:3;
            dataCounts = histc(geoIntExperience, levels);
            dataTypes = {'None', 'Novice', 'Intermediate', 'Expert'};
            figName = 'GeoINT Analysis Expertise';
            figFile = 'Geoint_Analysis_Expertise';
    end    
    figHandle = figure('name', figName, 'position', figPosition,...
        'NumberTitle', 'off', 'Visible', visible);
    title(figName);
    hold on;
    numLevels = length(levels);
    set(gca, 'XTick', 1:numLevels);
    set(gca, 'XTickLabel', dataTypes);
    ylabel('Number of Subjects');
    bar(dataCounts);
    if saveData
        fileName = [dataFolder, '\', figFile];
        saveas(figHandle, fileName, 'png');
    end
end

end