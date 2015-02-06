function draw(location,color)
% Location should be defined as [x y], where x and y identify a cell in a
% matrix.
% Color is given as 'b','r','g' etc.

x = location(1); y = location(2);
patch([x x+1 x+1 x],[y y y+1 y+1],color,'FaceAlpha',0.5,'EdgeColor',color,'EdgeColor','none')