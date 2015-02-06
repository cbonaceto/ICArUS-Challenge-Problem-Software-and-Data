function widgetnames = getwidgetnames(task,taskdef)

numguis = length(taskdef(task).widget);

widgetnames = {};
for gui = 1:numguis
    widgetnames{gui} = sprintf('%s\n%s\n%s',...
        taskdef(task).widget{gui},...
        taskdef(task).modality{gui},...
        taskdef(task).normalization{gui}); %#ok

    widgetnames{gui} = strrep(widgetnames{gui},'DistinctBars','Distinct');
    widgetnames{gui} = strrep(widgetnames{gui},'SliderSpinner','S-S');
    widgetnames{gui} = strrep(widgetnames{gui},'StackedBars','Stacked');
    widgetnames{gui} = strrep(widgetnames{gui},'MouseClick','Click');
    widgetnames{gui} = strrep(widgetnames{gui},'MouseDrag','Drag');
    widgetnames{gui} = strrep(widgetnames{gui},'DynamicLocking','Dyno');
    widgetnames{gui} = strrep(widgetnames{gui},'Loose ','L');
end
