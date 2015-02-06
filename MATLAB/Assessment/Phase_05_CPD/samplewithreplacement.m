function new_dist = samplewithreplacement(original_dist)

assert(isvector(original_dist));

new_dist = original_dist(ceil(rand(size(original_dist)) * length(original_dist)));
