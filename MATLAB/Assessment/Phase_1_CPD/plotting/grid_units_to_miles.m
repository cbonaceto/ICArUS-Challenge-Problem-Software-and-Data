function miles = grid_units_to_miles(gridUnits, milesPerGridUnit)
%GRID_UNITS_TO_MILES Converts a given distance in grid units to a distance
%in miles
%   Detailed explanation goes here

if(~exist('milesPerGridUnit', 'var'))
    milesPerGridUnit = 0.3;
end

miles = milesPerGridUnit * gridUnits;

end

