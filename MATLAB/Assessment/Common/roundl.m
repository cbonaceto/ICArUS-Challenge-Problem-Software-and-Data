function y= roundl(x, mode, n)
%
% roundl is a MATLAB function that does base-10 logarithmic rounding.
%
%   Syntax:
%
% y= roundl(x, mode, n)
%
% The value y is x rounded to a number of the form a*10^n, where n is an
% integer and a is one of a small number of multipliers.
%
% x, the number to be logarithmically rounded, must be positive.
%
% mode (optional) specifies the rounding direction.  If specified, mode
% must be 'u' (round up), 'd' (round down), or 'n' (round to nearest);
% 'n' is the default.
%
% n (optional) specifies the number of values per decade and thus
% determines the allowed multipliers.  Allowed values of n are 1-7.  The
% default value for n is 3; n= 5 and n= 7 are not commonly used.  The
% following table shows the set of multipliers for each value of n:
%
% 1: 1
% 2: 1, 3
% 3: 1, 2, 5
% 4: 1, 2, 3, 5
% 5: 1, 2, 3, 5, 7
% 6: 1, 1.5, 2, 3, 5, 7
% 7: 1, 1.5, 2, 3, 4, 6, 8
%
% Dr. Phillip M. Feldman, 1 March 2004


% REVISION HISTORY

% Version 2, Phillip M. Feldman, 5-11-2009:

% Modified code so that input x can be an array of arbitrary shape; the
% output y will match the shape of x.

% Version 1, Phillip M. Feldman, 3-1-2004: Initial version.


% Section 1: Check input arguments and assign default values if
% necessary.

if nargin < 1 | nargin > 3
   error('The number of calling arguments must be between 1 and 3.');
end

% Assign default arguments:

if nargin < 3, n= 3; end
if nargin < 2, mode= 'n'; end

% Check n and x:

if n < 1 | n > 7, error('n must be between 1 and 7.'); end

if any(x <= 0), error('x must be positive.'); end


% Section 2: Search table for bounding multipliers.

multiplier= [
1 10 10 10 10 10 10 10;  % 1 step per decade
1  3 10 10 10 10 10 10;  % 2 steps
1  2  5 10 10 10 10 10;  % 3 steps
1  2  3  5 10 10 10 10;  % 4 steps
1  2  3  5  7 10 10 10;  % 5 steps
1 1.5 2  3  5  7 10 10;  % 6 steps
1 1.5 2  3  4  6  8 10]; % 7 steps

log10_multiplier= log10(multiplier);

% Separate log10(x) into integer and fractional parts:

log10x= log10(x);
log10x_intg= floor(log10x);
log10x_frac= log10x - log10x_intg;

% Search nth row of table to find bounding multipliers.  <col> stores the
% relevant column number.

if strcmp(mode,'u')
   offset= eps;
else
   offset= 0;
end

col= zeros(size(x));
last_done= false(size(x));

for i= 2 : size(multiplier,2)

   % Compute logical array done:
   done= log10x_frac < log10_multiplier(n,i) + offset;

   % For each x such that done is true for the first time on this iteration
   % of the loop, set col= i:
   col(done & ~last_done)= i;

   % Copy done to last_done:
   last_done= done;
end

if strcmp(mode,'d') % round down
   m= reshape( multiplier(n,col-1), size(x) );

elseif strcmp(mode,'u') % round up
   m= reshape( multiplier(n,col), size(x) );

elseif strcmp(mode,'n') % round to nearest
   m_lower= reshape( multiplier(n,col-1), size(x) );
   m_upper= reshape( multiplier(n,col), size(x) );
   select= 10.^log10x_frac <= sqrt( m_lower .* m_upper );
   m= select .* m_lower + (1-select) .* m_upper;

else
   error(['Invalid rounding mode: ' mode]);
end

% Combine multipliers with powers of 10 to generate the final result:
y= m .* 10.^log10x_intg;
