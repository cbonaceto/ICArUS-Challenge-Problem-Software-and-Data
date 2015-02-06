function rmse_val = rmse(vec_1, vec_2)
%RMSE Computes root mean square error between two vectors
%   Detailed explanation goes here

rmse_val = sqrt(sum((vec_1-vec_2).^2)/length(vec_1));

end