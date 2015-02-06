function rms_val = rms(dist1, dist2)
%RMS Compute root mean square difference (RMS) between two distributions
%   Detailed explanation goes here

squaredSum = 0;
for i = 1:length(dist1)
    squaredSum = squaredSum + (dist1(i)-dist2(i))^2;
end
rms_val = sqrt(squaredSum/length(dist1));

end