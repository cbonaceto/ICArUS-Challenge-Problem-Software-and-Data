function plot_individual_differences(subjects, missionData,...
    inferencingMissions, decisionMakingMissions, foragingMissions,...
    highestDegrees, yearsExperience, probsAndStatsTraining,...
    probsAndStatsUsage, geoDataUsage, geoIntExperience, bis, basDrive,...
    basFunSeeking, basRewardResponsiveness, basTotal, foxHedgehog,...
    sbsdt, wlt, crt, vge, saveData, showPlots, dataFolder)
%PLOT_INDIVIDUAL_DIFFERENCES Summary of this function goes here
%   Detailed explanation goes here

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

%% Compute the dependent variables (inferencing skill, decision-making skill,
%  and foraging skill) for each subject
[inferencingScore, decisionMakingScore, foragingScore] = ...
    compute_individual_difference_metrics(subjects, missionData,...
        inferencingMissions, decisionMakingMissions, foragingMissions);
    
dependentVars = [inferencingScore, decisionMakingScore, foragingScore];
dependentVarNames = {'Inferencing Score', 'Decision Making Score', 'Foraging Score'};

%independentVars1 = [probsAndStatsTraining, yearsExperience];
independentVars1 = yearsExperience;
%independentVarNames1 = {'Probability and Statistics Training', 'Years of Experience'};
independentVarNames1 = {'Years of Experience'};
if ~isempty(foxHedgehog)
    independentVars2 = [bis, basDrive, basFunSeeking, basRewardResponsiveness,...
        basTotal, foxHedgehog, sbsdt, wlt, crt, vge];
    independentVarNames2 = {'BIS', 'BAS Drive', 'BAS Fun Seeking',...
        'BAS Reward Responsiveness', 'BAS Total', 'Fox Hedgehog Score',...
        'Santa Barbara Sense of Direction Test Score', 'Water Level Test Score',...
        'Cognitive Reflections Test Score', 'Video Game Experience Level'};
else
    independentVars2 = [bis, basDrive, basFunSeeking, basRewardResponsiveness,...
        basTotal];
    independentVarNames2 = {'BIS', 'BAS Drive', 'BAS Fun Seeking',...
        'BAS Reward Responsiveness', 'BAS Total'};
end

%% Create linear regression plots
figPosition = [60, 60, 1000, 600];
for independentVarSet = 1:2
    if independentVarSet == 1
        independentVars = independentVars1;
        independentVarNames = independentVarNames1;
    else
        independentVars = independentVars2;
        independentVarNames = independentVarNames2;
    end
    for dependentVarNum = 1:size(dependentVars, 2)
        dependentVarName = dependentVarNames{dependentVarNum};
        
        %Perform multiple regression of the dependent variable onto all
        %independent variables
        onesCol = ones(size(independentVars, 1), 1);        
        [~, ~, ~, ~, stats] = regress(dependentVars(:, dependentVarNum), [onesCol, independentVars]);
        rMulti = stats(1);
        pMulti = stats(3);
        disp(['Multiple Regression of ' dependentVarName ' onto ' independentVarNames{:}...
            ', p=' num2str(pMulti) ', R^2=' num2str(rMulti)]);
        
        %Perform regression of the dependent variable onto each independent
        %variable separately
        for independentVarNum = 1:size(independentVars, 2)
            independentVarName = independentVarNames{independentVarNum};
            
            %Perform a regression of the dependent variable onto the independent variable
            [b, ~, ~, ~, stats] = regress(dependentVars(:, dependentVarNum),...
                [onesCol, independentVars(:, independentVarNum)]);      
            %[onesCol, independentVars(:, independentVarNum)]
            r = stats(1);
            p = stats(3);
            
            figName = [dependentVarName ' vs ' independentVarName];
            figHandle = figure('name', figName, 'position', figPosition,...
                'NumberTitle', 'off', 'Visible', visible);
            title([figName '   (p=' num2str(p), ', R^2=' num2str(r) ', alpha=' num2str(b(1))...
                ', beta=' num2str(b(2)) ')']);
            hold on;
            xlabel(independentVarName);
            %xlim([-1 7]);
            %set(gca,'xtick', 0:6);
            ylabel(dependentVarName);
            
            %Create scatter plot of independent var (X) vs dependent var (Y) and
            %plot the linear regression line
            plot(independentVars(:, independentVarNum),...
                dependentVars(:, dependentVarNum), 'ob');            
            plot(independentVars(:, independentVarNum),...
                b(1) + b(2) * independentVars(:, independentVarNum), ':b');
            %Y = a + bX 
            
            %Save the figure
            if saveData
                fileName = [dataFolder, '\', dependentVarName, '_vs_', independentVarName];
                saveas(figHandle, fileName, 'png');                
            end
        end
    end
end

%% Create ANOVA plots for categorical independent measures (highest degree earned,
%  probability and stats training, probability and status usage, geospatial data usage,
%  GeoInt expertise level)
%probsAndStatsTraining = zeros(numSubjects, 1);
%inferencingScore = zeros(numSubjects, 1);
%decisionMakingScore = zeros(numSubjects, 1);
%foragingScore = zeros(numSubjects, 1);
if ~isempty(highestDegrees)
    independentGroupingVars = [highestDegrees, probsAndStatsTraining,...
        probsAndStatsUsage, geoDataUsage, geoIntExperience, crt, vge];
    independentGroupingVarNames = {'Highest Degree Earned', 'Probs and Stats Training Level',...
        'Probs and Stats Usage Frequency', 'Geospatial Data Usage Frequency',...
        'GeoINT Analysis Expertise', 'Cognitive Reflections Test Score',...
        'Video Game Experience Level'};
    % Levels: 0=High School, 1=Assoc, 2=Bachelor, 3=Mas, 4=Phd
    independentVarCategories{1} = {'HS', 'Assoc', 'Bachelor', 'Master', 'PhD'};
    independentVarCategories{2} = {'None', 'Elementary', 'Intermediate', 'Advanced'};
    independentVarCategories{3} = {'Never', 'Rarely', 'Occasionally', 'Often'};
    independentVarCategories{4} = independentVarCategories{3};
    independentVarCategories{5} = {'None', 'Novice', 'Intermediate', 'Expert'};
    independentVarCategories{6} = {'0/3', '1/3', '2/3', '3/3'};
    independentVarCategories{7} = {'None', 'Yearly', 'Monthly', 'Weekly', 'Daily'};
else
    independentGroupingVars = probsAndStatsTraining;
    independentGroupingVarNames = {'Probs and Stats Training Level'};
    independentVarCategories = {'None', 'Elementary', 'Intermediate', 'Advanced'};
end
alwaysUseOneWayAnova = true;
for dependentVarNum = 1:size(dependentVars, 2)
    %statarray = grpstats(dependentVars(:, dependentVarNum), independentGroupingVars)
    dependentVarName = dependentVarNames{dependentVarNum};
    if size(independentGroupingVars, 2) == 1 || alwaysUseOneWayAnova
        %Perform 1-way ANOVA of dependent variable against each
        %independent variable and save the box plot that is created
        for independentVarNum = 1:size(independentGroupingVars, 2)
            p = anova1(dependentVars(:, dependentVarNum),...
                independentGroupingVars(:, independentVarNum));            
            %p = anova1(dependentVars(:, dependentVarNum), independentGroupingVars);            
            independentVarName = independentGroupingVarNames{independentVarNum};
            figHandle = gcf;
            title([dependentVarName ' Grouped By ' independentVarName ', p = ' num2str(p)]);
            set(gca, 'XTick', 1:length(independentVarCategories{independentVarNum}));
            set(gca, 'XTickLabel', independentVarCategories{independentVarNum});
            xlabel(independentVarName);
            ylabel(dependentVarName);
            fileName = [dataFolder, '\', dependentVarName, '_grouped_by_' independentVarName];
            if saveData
                saveas(figHandle, fileName, 'png');
            end
            if strcmp(visible, 'off')
                close all;
            end
        end
    else
        %Perform N-Way anova of dependent variable against each independent
        %variable
        p = anovan(dependentVars(:, dependentVarNum), independentGroupingVars,...
            'varnames', independentGroupingVarNames);
        if strcmp(visible, 'off')
            close all;
        end
        %Create box plot showing the dependent variable grouped by each
        %independent variable
        for independentVarNum = 1:size(independentGroupingVars, 2)
            independentVarName = independentGroupingVarNames{independentVarNum};           
            figName = [dependentVarName ' Grouped By ' independentVarName];
            figHandle = figure('name', figName, 'position', figPosition,...
                'NumberTitle', 'off', 'Visible', visible);
            hold on;
            %independentVars(:, independentVarNum),...
            %dependentVars(:, dependentVarNum)
            boxplot(dependentVars(:, dependentVarNum),...
                independentGroupingVars(:, independentVarNum));
            title([dependentVarName ' Grouped By ' independentVarName...
                ', p = ' num2str(p(independentVarNum))]);            
            set(gca, 'XTick', 1:length(independentVarCategories{independentVarNum}));
            set(gca, 'XTickLabel', independentVarCategories{independentVarNum});
            xlabel(independentVarName);
            ylabel(dependentVarName);            
            fileName = [dataFolder, '\', dependentVarName, '_grouped_by_' independentVarName];
            if saveData
                saveas(figHandle, fileName, 'png');
            end            
        end
    end
end

%% Also output subject score data to a CSV file
if saveData
    output_individual_difference_metrics(subjects, inferencingScore,...
        decisionMakingScore, foragingScore, dataFolder);
end

end