function [tx,ty,px,py,pxy]=spatial_distribution_palette(distr_name,n,ccv)

% Given a text-label, return all the points defining the specified spatial
% distribution...the function "draw_spatial_samples" (below) then takes pxy
% (the joint distribution) and (pseudo-randomly) draws a few samples.

nx=n(1); ny=n(2);

tx=1:nx; 
ty=1:ny;

if (ccv(1)==0) && (ccv(2)==0)
    fx=tx-(nx+1)/2; 
    fy=ty-(ny+1)/2;
else
    fx=tx-ccv(1); 
    fy=ty-ccv(2);
end

switch distr_name
    case 'uniform'
        px=ones(size(fx))/nx; % uniform distribution
        py=ones(size(fy))/ny; % uniform distribution
    case 'quadCV'
        px=fx.^2/sum(fx.^2); % quadratic (concave)
        py=fy.^2/sum(fy.^2); % quadratic (concave)
    case 'quadCX'
        px=(max(abs(fx)).^2-fx.^2)/sum(max(abs(fx)).^2-fx.^2); % quadratic (convex)
        py=(max(abs(fy)).^2-fy.^2)/sum(max(abs(fy)).^2-fy.^2); % quadratic (convex)
    case 'tanhCV'
        px=abs(tanh(fx))/sum(abs(tanh(fx))); % hyperbolic tangent (concave)
        py=abs(tanh(fy))/sum(abs(tanh(fy))); % hyperbolic tangent (concave)
    case 'tanhCX'
        sc=5; % spreading-coefficient (larger values increase [radial] spread)
        px=(1-abs(tanh(fx/sc)))/sum(1-abs(tanh(fx/sc))); % hyperbolic tangent (convex)
        py=(1-abs(tanh(fy/sc)))/sum(1-abs(tanh(fy/sc))); % hyperbolic tangent (convex)
    case  'gauss'
        sig = 15;
        px = exp(-(fx.^2)./(2*sig^2));
        py = exp(-(fy.^2)./(2*sig^2));
end
    
pxy=px'*py;