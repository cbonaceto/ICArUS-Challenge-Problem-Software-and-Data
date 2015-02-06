function scenario = getscenario(numfacilities,fvid,sectorid,scene2,scene3,scene4)

if (numfacilities == 2)
    scenario = scene2(fvid).scenario(sectorid);
elseif (numfacilities == 3)
    scenario = scene3(fvid).scenario(sectorid);
elseif (numfacilities == 4)
    scenario = scene4(fvid).scenario(sectorid);
end
