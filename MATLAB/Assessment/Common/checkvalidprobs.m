function checkvalidprobs(probs1,probs2)

assert(size(probs1,1) == 1);
assert(size(probs2,1) == 1);
% assert(size(probs1,1) == size(probs2,1));
assert(size(probs1,2) == size(probs2,2));

assert(all(probs1(:) >= 0));
assert(all(probs1(:) <= 1));
assert(all(probs2(:) >= 0));
assert(all(probs2(:) <= 1));
