function [varargout] = zerodata( m, n )
%ZERODATA Summary of this function goes here
%   Detailed explanation goes here
arr = zeros(m,n);
for x = 1:m
    for y = 1:n
        arr(x,y) = NaN;
    end
end
nout = max(nargout,1);
for k = 1:nout
    varargout{k} = arr;
end
end

