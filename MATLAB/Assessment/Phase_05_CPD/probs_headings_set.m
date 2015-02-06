function headings = probs_headings_set(base,num_sets,num_probs_in_set)

headings = {};
for i = 1:num_sets
    headings = [headings probs_headings(sprintf('%s_%d',base,i),num_probs_in_set)]; %#ok
end
