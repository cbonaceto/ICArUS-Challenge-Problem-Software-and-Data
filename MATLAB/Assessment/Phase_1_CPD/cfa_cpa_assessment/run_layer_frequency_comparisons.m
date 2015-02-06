task = 6;

%Load data from final exam
[final.subjects, ~, ~, ~,...
        ~, ~, ~, ~,... 
        ~, final.layerSelectionData, final.bestLayerData, final.layerSurpriseData, ~, ~] = ...
        aggregate_csv_task_data(strcat('Final Exam August 2012\subject_data\allresponses_task_', num2str(task), '.csv'), task);   

%Load data from pilot exam
[pilot.subjects, ~, ~, ~,...
        ~, ~, ~, ~,... 
        ~, pilot.layerSelectionData, pilot.bestLayerData, pilot.layerSurpriseData, ~, ~] = ...
        aggregate_csv_task_data(strcat('Pilot Exam March 2012\subject_data\allresponses_task_', num2str(task), '.csv'), task);
numLayers = size(final.layerSelectionData.subjectLayerSelectionsByStage, 3);
numSubjectsFinal = size(final.layerSelectionData.subjectLayerSelections, 1);

compare_final_half_half = true;
compare_final_60_rest = true;
compare_final_20_rest = true;
compare_full_final = true;
compare_final_pilot = true;
compare_final_adjusted_pilot = false;
compare_final_chance = true;
compare_pilot_chance = true;

useGoodnessOfFit = false;
saveData = true;

comparePermutations = false;
compareFirstChoiceSelections = true;

%%Compare first half of final exam subjects to second half of final exam
%%subjects
%Partition layer selection data from final exam into two equal-size groups
if compare_final_half_half    
    group_1_size = round(numSubjectsFinal/2);
    subjectLayerSelections_1 = final.layerSelectionData.subjectLayerSelections(1:group_1_size,:);
    subjectLayerSelections_2 = final.layerSelectionData.subjectLayerSelections(group_1_size+1:numSubjectsFinal,:);
    %subjectLayerSelections_2 = subjectLayerSelections_1;
    %Compute chi square goodness of fit test or independence test between groups
    if comparePermutations
        compare_layer_frequencies(final.layerSelectionData.layers, final.layerSelectionData.permutations, numLayers,...
            subjectLayerSelections_1, subjectLayerSelections_2, 'Final40-Final39', useGoodnessOfFit, false,...
            true, saveData, 'layer_comparison');
    end
    if compareFirstChoiceSelections
        compare_layer_frequencies_first_choice(final.layerSelectionData.layers, final.layerSelectionData.permutations,...
            subjectLayerSelections_1, subjectLayerSelections_2, 'Final40-Final39', useGoodnessOfFit,...
            true, saveData, 'layer_comparison');
    end
end
%%%%%%%%

%%Compare first 60 final exam subjects with rest of final exam subjects
if compare_final_60_rest
    if numSubjectsFinal > 60 %#ok<*UNRCH>
        %Partition layer selection data from final exam into one group with 60 subjects and
        %one group with 20 subjects
        group_1_size = 60;
        subjectLayerSelections_1 = final.layerSelectionData.subjectLayerSelections(1:group_1_size,:);
        subjectLayerSelections_2 = final.layerSelectionData.subjectLayerSelections(group_1_size+1:numSubjectsFinal,:);
        %Compute chi square goodness of fit test or independence test between groups
        if comparePermutations
            compare_layer_frequencies(final.layerSelectionData.layers, final.layerSelectionData.permutations, numLayers,...
                subjectLayerSelections_1, subjectLayerSelections_2, 'Final60-Final19', useGoodnessOfFit, false,...
                true, saveData, 'layer_comparison');
        end
        if compareFirstChoiceSelections
            compare_layer_frequencies_first_choice(final.layerSelectionData.layers, final.layerSelectionData.permutations,...
                subjectLayerSelections_1, subjectLayerSelections_2, 'Final60-Final19', useGoodnessOfFit,...
                true, saveData, 'layer_comparison');
        end
    end
end
%%%%%%%%

%%Compare first 20 final exam subjects with rest of final exam subjects
if compare_final_20_rest
    if numSubjectsFinal > 20
        %Partition layer selection data from final exam into one group with 20 subjects and
        %one group with rest of subjects
        group_1_size = 20;
        subjectLayerSelections_1 = final.layerSelectionData.subjectLayerSelections(1:group_1_size,:);
        subjectLayerSelections_2 = final.layerSelectionData.subjectLayerSelections(group_1_size+1:numSubjectsFinal,:);
        %Compute chi square goodness of fit test or independence test between groups
        if comparePermutations
            compare_layer_frequencies(final.layerSelectionData.layers, final.layerSelectionData.permutations, numLayers,...
                subjectLayerSelections_1, subjectLayerSelections_2, 'Final20-Final59', useGoodnessOfFit, false,...
                true, saveData, 'layer_comparison');
        end
        if compareFirstChoiceSelections
            compare_layer_frequencies_first_choice(final.layerSelectionData.layers, final.layerSelectionData.permutations,...
                subjectLayerSelections_1, subjectLayerSelections_2, 'Final20-Final59', useGoodnessOfFit,...
                true, saveData, 'layer_comparison');
        end
    end
end
%%%%%%%%

%%Use full set of 79 final exam subjects as "expected" frequencies and
%%compare with subsets of final exam subjects of size 20, 40, and 60 as
%%"observed" frequencies
if compare_full_final
    subset_sizes = [20 40 60];
    subjectLayerSelections_2 = final.layerSelectionData.subjectLayerSelections;
    for n = subset_sizes
        if numSubjectsFinal > n
            subjectLayerSelections_1 = final.layerSelectionData.subjectLayerSelections(1:n,:);  
            %Compute chi square goodness of fit test or independence test between groups
            if comparePermutations
                compare_layer_frequencies(final.layerSelectionData.layers, final.layerSelectionData.permutations, numLayers,...
                    subjectLayerSelections_1, subjectLayerSelections_2, ['Final79-Final', num2str(n)], useGoodnessOfFit, false,...
                    true, saveData, 'layer_comparison');
            end
            if compareFirstChoiceSelections
                compare_layer_frequencies_first_choice(final.layerSelectionData.layers, final.layerSelectionData.permutations,...
                    subjectLayerSelections_1, subjectLayerSelections_2, ['Final79-Final', num2str(n)], useGoodnessOfFit,...
                    true, saveData, 'layer_comparison');
            end
        end
    end
end
%%%%%%%%

%%Compare pilot exam subjects with final exam subjects
if compare_final_pilot
    subjectLayerSelections_1 = final.layerSelectionData.subjectLayerSelections;
    subjectLayerSelections_2 = pilot.layerSelectionData.subjectLayerSelections;
    %Compute chi square goodness of fit test or independence test between groups
    if comparePermutations
        compare_layer_frequencies(final.layerSelectionData.layers, final.layerSelectionData.permutations, numLayers,...
            subjectLayerSelections_1, subjectLayerSelections_2, 'Final-Pilot', useGoodnessOfFit, false,...
            true, saveData, 'layer_comparison');
    end
    if compareFirstChoiceSelections
        compare_layer_frequencies_first_choice(final.layerSelectionData.layers, final.layerSelectionData.permutations,...
            subjectLayerSelections_1, subjectLayerSelections_2, ['Final-Pilot', num2str(n)], useGoodnessOfFit,...
            true, saveData, 'layer_comparison');
    end
end
%%%%%%%%

%%Compare pilot exam subjects with final exam subjects, but adjust down the
% modal frequency in the pilot data based on the modal frequency of the
% final data
if compare_final_adjusted_pilot
    subjectLayerSelections_1 = final.layerSelectionData.subjectLayerSelections;
    subjectLayerSelections_2 = pilot.layerSelectionData.subjectLayerSelections;
    %Compute chi square goodness of fit test or independence test between groups
    if comparePermutations
        compare_layer_frequencies(final.layerSelectionData.layers, final.layerSelectionData.permutations, numLayers,...
            subjectLayerSelections_1, subjectLayerSelections_2, 'Final-Adjusted_Pilot', useGoodnessOfFit, true,...
            true, saveData, 'layer_comparison');
    end
end

%% Compare the final exam to the frequencies we would expect from chance (e.g., uniform frequencies)
if compare_final_chance
    subjectLayerSelections_1 = final.layerSelectionData.subjectLayerSelections;    
    subjectLayerSelections_2 = [];
    %Compute chi square goodness of fit test or independence test between groups
    if comparePermutations
        compare_layer_frequencies(final.layerSelectionData.layers, final.layerSelectionData.permutations, numLayers,...
            subjectLayerSelections_1, subjectLayerSelections_2, 'Final-Chance', false, false,...
            true, saveData, 'layer_comparison');
    end
    if compareFirstChoiceSelections
        compare_layer_frequencies_first_choice(final.layerSelectionData.layers, final.layerSelectionData.permutations,...
            subjectLayerSelections_1, subjectLayerSelections_2, 'Final-Chance', false,...
            true, saveData, 'layer_comparison');
    end
end

%% Compare the pilot exam to the frequencies we would expect from chance (e.g., uniform frequencies)
if compare_pilot_chance
    subjectLayerSelections_1 = pilot.layerSelectionData.subjectLayerSelections;    
    subjectLayerSelections_2 = [];
    %Compute chi square goodness of fit test or independence test between groups
    if comparePermutations
        compare_layer_frequencies(final.layerSelectionData.layers, final.layerSelectionData.permutations, numLayers,...
            subjectLayerSelections_1, subjectLayerSelections_2, 'Pilot-Chance', false, false,...
            true, saveData, 'layer_comparison');
    end
    if compareFirstChoiceSelections
        compare_layer_frequencies_first_choice(final.layerSelectionData.layers, final.layerSelectionData.permutations,...
            subjectLayerSelections_1, subjectLayerSelections_2, 'Pilot-Chance', false,...
            true, saveData, 'layer_comparison');
    end
end