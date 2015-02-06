function [subjectnums, subjects, initials, numsubjects] = read_subjects_directory(datadirectory)
%
% subjects are strings:     '003.BB'
% subjectnums are numbers:  3
% initials are strings:     'BB'

d = dir([datadirectory 'S*']); 

subjects = {d.name};
numsubjects = length(subjects);
assert(numsubjects > 0);

subjectnums = zeros(1,numsubjects);
initials = cell(1,numsubjects);
for s = 1:numsubjects
    [first rest] = strtok(subjects{s},'_');
    subjectnums(s) = str2double(first(2:end));
    initials{s} = rest(2:end);
end
