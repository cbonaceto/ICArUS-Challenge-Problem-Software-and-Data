a = 0;
b = 0;
z = 1;
for (i = 1:500)
    for (j = 1:2)
        if (strcmp(cell2mat(scene(i).scenario(j).facility),'Facility B'))
            for (k = 1:length(scene(i).scenario(j).incident))
                if (strcmp(cell2mat(scene(i).scenario(j).incident(k)),'SIGINT'))
                    v(z) = i;
                    z = z + 1;
                    a = a + 1;
                    b = b + 1;
                    break
                end
            end
        end
    end
end