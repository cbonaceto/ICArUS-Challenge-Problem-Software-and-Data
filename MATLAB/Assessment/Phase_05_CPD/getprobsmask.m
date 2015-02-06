function probsmask = getprobsmask(numprobsets,maxprobsets,numfacilities,maxfacilities)

assert(numprobsets >= 0);
assert(numprobsets <= maxprobsets);
assert(numfacilities >= 0);
assert(numfacilities <= maxfacilities);

probsmask = zeros(1,maxprobsets*maxfacilities);
for p = 1:numprobsets
    probsmask(maxfacilities*(p-1)+(1:numfacilities)) = 1;
end
