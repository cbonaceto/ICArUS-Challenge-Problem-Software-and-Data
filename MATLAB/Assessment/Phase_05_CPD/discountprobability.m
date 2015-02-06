function newp = discountprobability(p,d)
% p = 1 x n set of probabilities, summing to 1

assert(size(p,1) == 1 || size(p,2) == 1);
assert(d >= 0 && d <= 1);
assert(abs(sum(p) - 1) < 0.0001); 

newp = (p.^d) ./ sum(p.^d);
    