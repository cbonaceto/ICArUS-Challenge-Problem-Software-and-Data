sum = 0;
iterations = 100000;
vals = zeros(iterations, 1);
for i=1:iterations
    vals(i) = gaussian1D(0.4,0,10,i);
    sum = sum + vals(i);
end

disp(['mean: ' num2str(sum/length(vals))]);
disp(['sigma: '  num2str(std(vals))]);