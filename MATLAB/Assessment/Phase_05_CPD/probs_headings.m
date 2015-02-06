function headings = probs_headings(base,num)

headings = cell(1,num);
for i = 1:num
    headings{i} = sprintf('%s_%d',base,i);
end
