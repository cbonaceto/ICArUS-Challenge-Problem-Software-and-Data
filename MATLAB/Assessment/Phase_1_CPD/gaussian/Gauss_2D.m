function Gauss_2D(s)
% Gauss_2D

global sigma;
global sigmaUnits;
if ~exist('s', 'var')
   sigma = 10;
else
   sigma = s;
end

%clear

% 2-D Gaussian
figure;
%s = 10;
d = -30:1.5:30;
[X Y] = meshgrid(d, d);
c = 1/(2*pi*sigma^2);
Z = c .* exp( -(X.^2 + Y.^2) ./ (2*sigma^2));
surfc(X, Y, Z);
colormap white
set(gca, 'xlim', [-30 30]);
set(gca, 'ylim', [-30 30]);
set(gca, 'zlim', [0 0.002]);
set(gca, 'XTickLabel', '');
set(gca, 'YTicklabel', '');
set(gca, 'ZTickLabel', '');
zlabel('Probability of Attack', 'Fontsize', 11);

sigmaMultiples = [1 2 1.33 1.5 1.4822];
for sigmaMultiple = sigmaMultiples
    % Integrate Gaussian
    % First inside sigma * sigmaMultiple
    sigmaUnits = sigma * sigmaMultiple;    
    %d = -sigmaUnits:1:sigmaUnits;
    %X = d;
    %Y = d;
    Xmin = -sigmaUnits;
    Xmax = sigmaUnits;
    Ymin = -sigmaUnits;
    Ymax = sigmaUnits;
    inside = dblquad(@integrnd, Xmin, Xmax, Ymin, Ymax);    
    % Then total
	sigmaUnits = 100;
    %d = -100:1:100;
    %X = d;
    %Y = d;
    Xmin = -sigmaUnits;
    Xmax = sigmaUnits;
    Ymin = -sigmaUnits;
    Ymax = sigmaUnits;    
    total = dblquad(@integrnd, Xmin, Xmax, Ymin, Ymax);    
    
    disp(['Sigma Multiple: ', num2str(sigmaMultiple)]);
    disp(['Total Volumne: ', num2str(total)]);    
    disp(['Inside Volume: ', num2str(inside/total)]);
    disp(['Outside Volume: ', num2str((total-inside)/total)]);
    disp(' ');
end

% 2-1 circle (uses 1.4822*sigma, see above)
sigmaUnits = 1.4822 * sigma;
figure;
% Quadrant 1
x1 = 0:0.1:sigmaUnits;
y1 = sqrt(sigmaUnits^2 - x1.^2);
z1 = x1.*0;
% Quadarant 2
x2 = -sigmaUnits:0.1:0;
y2 = sqrt(sigmaUnits^2 - x2.^2);
z2 = x2.*0;
% Quadarant 3
x3 = -sigmaUnits:0.1:0;
y3 = -sqrt(sigmaUnits^2 - x3.^2);
z3 = x3.*0;
% Quadrant 4
x4 = 0:0.1:sigmaUnits;
y4 = -sqrt(sigmaUnits^2 - x4.^2);
z4 = x4.*0;
% plot3
plot3(x1, y1, z1, '-k');
hold on;
grid on;
plot3(x2, y2, z2, 'k-');
plot3(x3, y3, z3, 'k-');
plot3(x4, y4, z4, 'k-');
set(gca, 'xlim', [-30 30]);
set(gca, 'ylim', [-30 30]);
set(gca, 'zlim', [0 0.002]);
set(gca, 'XTickLabel', '');
set(gca, 'YTickLabel', '');
set(gca, 'XTickLabel', '');
xlabel('2-to-1 Boundary of Attack', 'Fontsize', 11);
end

function Z = integrnd(X,y)
%X is vector, y is scalar
global sigma;
global sigmaUnits;

% 2-D Gaussian
c = 1/(2*pi*sigma^2);
Z = c .* exp( -(X.^2 + y.^2) ./ (2*sigma^2));

%Establish distance cutoff at sigmaUnits. Return 0 if distance of (x,y) point 
%to origin greater than sigmaUnits so that we integrate to create a cylindrical 
%volume instead of a rectangular volume.   
distances = sqrt(X.^2 + y.^2);
Z(distances > sigmaUnits) = 0;
end