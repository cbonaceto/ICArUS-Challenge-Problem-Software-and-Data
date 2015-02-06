% show_all_objects

load object_palette_PILOT.mat

for i = 1:7
    for or = 1:4
        draw_object(object{i,or},[i*30 or*30],0,[0 0 0]);
    end
    text(i*30,10,num2str(i))
end
axis equal off;
