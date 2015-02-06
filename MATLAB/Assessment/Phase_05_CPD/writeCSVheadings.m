function writeCSVheadings(csvdatafilename,headings,separator)

if ~exist('separator','var'), separator = ','; end

numheadings = length(headings);

fid = fopen(csvdatafilename,'wt');
assert(fid ~= -1,'Cannot open file');

for i = 1:numheadings-1
    fprintf(fid,'%s%s',headings{i},separator);
end
fprintf(fid,'%s\n',headings{numheadings});

fclose(fid);
