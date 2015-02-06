% rename phases of xml data files from old format to new format

cd C:\Users\mcaywood\Documents\ICArUS\PILOT\data\S1_BB;

dos('mkdir new');
d = dir('*.xml');
for i = 1:length(d)
    oldname = d(i).name;
    
    firstchar = strfind(oldname,'Phase')+5;
    if oldname(firstchar+1) == '.' || oldname(firstchar+1) == '_'
        lastchar = firstchar;
    else
        lastchar = firstchar+1;
    end
    fragment = oldname(firstchar:lastchar);

    newfragment = num2str(str2num(fragment)/2);
    newname = [oldname(1:firstchar-1) newfragment oldname(lastchar+1:end)];
    
    dos(sprintf('copy %s new\\%s',oldname,newname));
end
    