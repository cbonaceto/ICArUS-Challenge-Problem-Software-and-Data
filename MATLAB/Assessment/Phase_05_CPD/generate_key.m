clear all; clc

load('scene_palette_PILOT_3FAC.mat')

for (k = 1:length(scene))
    for (p = 1:length(scene(k).scenario))
        if (strcmp(cell2mat(scene(k).scenario(p).facility),'Facility A'));
            F(k,p) = 1; %#ok<*SAGROW>
        elseif (strcmp(cell2mat(scene(k).scenario(p).facility),'Facility B'));
            F(k,p) = 2;
        elseif (strcmp(cell2mat(scene(k).scenario(p).facility),'Facility C'));
            F(k,p) = 3;
        else (strcmp(cell2mat(scene(k).scenario(p).facility),'Facility D'));
            F(k,p) = 4;
        end
    end
end