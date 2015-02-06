function draw_object(object,center,hardware,color)

object(:,1) = object(:,1)  + center(1);
object(:,2) = object(:,2)  + center(2);

hw_ix = ceil(length(object)*rand); % If needed
for (i = 1:length(object))
    if (i == hw_ix)
        draw(object(i,:),color); hold on
        
        % Draw hardware
        if (hardware)
            plot(object(i,1)+0.5,object(i,2)+0.5,'r*','MarkerSize',6);
        end
    else
        draw(object(i,:),color); hold on
    end
end