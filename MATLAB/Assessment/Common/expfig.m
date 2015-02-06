function expfig(f,basefilename,varargin)
% optional: ['fontsize', fontsize]

assert(length(varargin) == 0 || length(varargin) == 2); %#ok

fontsize = 14;
if (length(varargin) == 2) && strcmpi(varargin{1},'fontsize')
    fontsize = varargin{2};
end

set(f,'color',[1 1 1]);

saveas(f,basefilename,'fig');

dpi = 72;

exportfig(f,basefilename,'Format','png','FontMode', 'fixed', 'FontSize', fontsize, 'resolution', dpi, 'color', 'cmyk');
