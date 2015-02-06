function [cases, caseheadings] = readcasefile(casefilename,numstates)

% parse headings
fid = fopen(casefilename,'r');
t = textscan(fid,repmat('%s',1,numstates+1),1);
fclose(fid);

caseheadings = cell(1,numstates);
for i = 1:length(caseheadings)
    caseheadings{i} = t{i+1}{1};
end

%% parse file
fid = fopen(casefilename,'r');
t = textscan(fid,repmat('%s',1,numstates+1),'Headerlines',1);
fclose(fid);

% convert to cell array
numcases = length(t{1});
assert(length(t) == numstates+1);
cases = cell(numstates,numcases);
for i = 1:numstates
    for c = 1:numcases
        cases{i,c} = t{i+1}{c};
    end
end
