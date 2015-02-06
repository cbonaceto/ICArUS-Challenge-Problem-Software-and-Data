function N = grow(M)

[x y] = ind2sub(size(M),find(M));
for (i = 1:length(x))
    M(x(i)+1,y(i)-1) = 1;
    M(x(i)+1,y(i)) = 1;
    M(x(i)+1,y(i)+1) = 1;
    
    M(x(i)-1,y(i)-1) = 1;
    M(x(i)-1,y(i)) = 1;
    M(x(i)-1,y(i)+1) = 1;
    
    M(x(i),y(i)-1) = 1;
    M(x(i),y(i)+1) = 1;
end
N = M;