function examfile =  create_xml_2(filename,examfile)
movefile = 0;
diary('out.txt')
eval(['load ''' examfile '.mat''']);

W = {'In this section you will be trained to distinguish 2 factories, first Ketchup, then Mustard.  Only the IMINT layer will be shown.'
'Identify the factory in the specified sector.'
'Now we''ll add the SIGINT layer.  Study the following examples.'
'Identify the factory in the specified sector.'
'Now we''ll add the MASINT layer.  Study the following examples.'
'Identify the factory in the specified sector.'
'In this section you will be trained to distinguish a new factory, Salt.  For reference, you''ll be shown the same examples of Ketchup and Mustard Factories.  Only the IMINT layer will be shown.'
'Identify the factory in the specified sector.'
'Now we''ll add the SIGINT layer.  Study the following examples.'
'Identify the factory in the specified sector.'
'Now we''ll add the MASINT layer.  Study the following examples.'
'Identify the factory in the specified sector.'
'In this section you will be trained to distinguish a new factory, Pepper.  For reference, you''ll also see the same examples of Ketchup, Mustard, and Salt Factories.  Only the IMINT layer will be shown.'
'Identify the factory in the specified sector.'
'Now we''ll add the SIGINT layer.  Study the following examples.'
'Identify the factory in the specified sector.'
'Now we''ll add the MASINT layer.  Study the following examples.'
'Identify the factory in the specified sector.'
'Locate the factory in the scene.  It may appear in one or more sectors.'
'Please answer the following questions.'};


% Load scene palette; reformat
% -----------------------------------

for (z = 1:length(examfile.Set)); % Set
    disp(z)

    if (z == 1)
        load('scene_palette_PILOT_2FAC.mat')
    elseif (z == 7)
        load('scene_palette_PILOT_3FAC.mat')
    elseif (z == 13)
        load('scene_palette_PILOT_4FAC.mat')
    end
    
    if (z == 1 || z == 7 || z == 13)
        for (k = 1:length(scene))
            for (p = 1:length(scene(k).scenario))
                if (strcmp(cell2mat(scene(k).scenario(p).facility),'Facility A'));
                    F(k,p) = 1; %#ok<*AGROW>
                elseif (strcmp(cell2mat(scene(k).scenario(p).facility),'Facility B'));
                    F(k,p) = 2;
                elseif (strcmp(cell2mat(scene(k).scenario(p).facility),'Facility C'));
                    F(k,p) = 3;
                else (strcmp(cell2mat(scene(k).scenario(p).facility),'Facility D'));
                    F(k,p) = 4;
                end
            end
        end
    end
    
    if (z == 1) % 2-Facility
        Fp{1} = sortrows(cat(1,[find(F(:,1)==1) ones(length(find(F(:,1)==1)),1)],[find(F(:,2)==1) 2*ones(length(find(F(:,2)==1)),1)]),1);
        Fp{2} = sortrows(cat(1,[find(F(:,1)==2) ones(length(find(F(:,1)==2)),1)],[find(F(:,2)==2) 2*ones(length(find(F(:,2)==2)),1)]),1);
        Fp{3} = [];
        Fp{4} = [];
    elseif (z == 7) % 3-Facility
        Fp{1} = sortrows(cat(1,[find(F(:,1)==1)+500 ones(length(find(F(:,1)==1)),1)],[find(F(:,2)==1)+500 2*ones(length(find(F(:,2)==1)),1)], ...
            [find(F(:,3)==1)+500 3*ones(length(find(F(:,3)==1)),1)]),1);
        Fp{2} = sortrows(cat(1,[find(F(:,1)==2)+500 ones(length(find(F(:,1)==2)),1)],[find(F(:,2)==2)+500 2*ones(length(find(F(:,2)==2)),1)], ...
            [find(F(:,3)==2)+500 3*ones(length(find(F(:,3)==2)),1)]),1);
        Fp{3} = sortrows(cat(1,[find(F(:,1)==3)+500 ones(length(find(F(:,1)==3)),1)],[find(F(:,2)==3)+500 2*ones(length(find(F(:,2)==3)),1)], ...
            [find(F(:,3)==3)+500 3*ones(length(find(F(:,3)==3)),1)]),1);
        Fp{4} = [];
    elseif (z == 13) % 4-Facility
        Fp{1} = sortrows(cat(1,[find(F(:,1)==1)+1000 ones(length(find(F(:,1)==1)),1)],[find(F(:,2)==1)+1000 2*ones(length(find(F(:,2)==1)),1)], ...
            [find(F(:,3)==1)+1000 3*ones(length(find(F(:,3)==1)),1)],[find(F(:,4)==1)+1000 4*ones(length(find(F(:,4)==1)),1)]),1);
        Fp{2} = sortrows(cat(1,[find(F(:,1)==2)+1000 ones(length(find(F(:,1)==2)),1)],[find(F(:,2)==2)+1000 2*ones(length(find(F(:,2)==2)),1)], ...
            [find(F(:,3)==2)+1000 3*ones(length(find(F(:,3)==2)),1)],[find(F(:,4)==2)+1000 4*ones(length(find(F(:,4)==2)),1)]),1);
        Fp{3} = sortrows(cat(1,[find(F(:,1)==3)+1000 ones(length(find(F(:,1)==3)),1)],[find(F(:,2)==3)+1000 2*ones(length(find(F(:,2)==3)),1)], ...
            [find(F(:,3)==3)+1000 3*ones(length(find(F(:,3)==3)),1)],[find(F(:,4)==3)+1000 4*ones(length(find(F(:,4)==3)),1)]),1);
        Fp{4} = sortrows(cat(1,[find(F(:,1)==4)+1000 ones(length(find(F(:,1)==4)),1)],[find(F(:,2)==4)+1000 2*ones(length(find(F(:,2)==4)),1)], ...
            [find(F(:,3)==4)+1000 3*ones(length(find(F(:,3)==4)),1)],[find(F(:,4)==4)+1000 4*ones(length(find(F(:,4)==4)),1)]),1);
    end
    
    % Write file
    % -----------------------------------
    
    if (z == 1)
        eval(['fid = fopen(''' filename '.xml'',''w'');']);
        
        fprintf(fid,'<?xml version="1.0" encoding="UTF-8" standalone="yes"?> \n');
        fprintf(fid,'<IcarusEvaluation xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"  name="Pilot1"> \n');
        fprintf(fid,'<NormalizationMode>NormalizeAfterAndConfirm</NormalizationMode> \n');
        fprintf(fid,'<TutorialUrl>tutorial.pdf</TutorialUrl> \n \n');
        
        fprintf(fid,'<Facilities> \n');
        fprintf(fid,'<Facility ItemName="Ketchup Factory" ItemId="1"/> \n');
        fprintf(fid,'<Facility ItemName="Mustard Factory" ItemId="2"/> \n');
        fprintf(fid,'<Facility ItemName="Salt Factory" ItemId="3"/> \n');
        fprintf(fid,'<Facility ItemName="Pepper Factory" ItemId="4"/> \n');
        fprintf(fid,' </Facilities> \n \n');
    end
    
    fprintf(fid,'<ExamPhase xsi:type="Pause" Required="true" ShowCountdown="false"> \n');
    fprintf(fid,'<InstructionText>');
    fprintf(fid,W{z});
    fprintf(fid,'</InstructionText> \n');
    fprintf(fid,'</ExamPhase> \n \n');
    
    % Reset indexes
%     if (z == 1 || z == 7 || z == 13 || z == 19 || z == 20)
    if (z == 1 || z == 19 || z == 20)
        IND = [];
    end
    
    % Training
    % -----------------------------------
    
    if(~isempty(examfile.Set(z).Training))
        
        fprintf(fid,'<!-- BLOCK %s --> \n',num2str(z));
        fprintf(fid,'<ExamPhase name="Training" xsi:type="Training">   \n');
        for (j = 1:length(examfile.Set(z).Training.QuestionType))
            
            % Read examfile
            QuestionType = cell2mat(examfile.Set(z).Training.QuestionType{j}(1));
            
            if(strcmp(QuestionType,'Annotation'))
                SceneItem = cell2mat(examfile.Set(z).Training.QuestionType{j}(2));
                
                if (strcmp(SceneItem,'A'))
                    ItemID = 1;
                elseif (strcmp(SceneItem,'B'))
                    ItemID = 2;
                elseif (strcmp(SceneItem,'C'))
                    ItemID = 3;
                elseif (strcmp(SceneItem,'D'))
                    ItemID = 4;
                end
                
                % Choose a scene; find the SectorId
                FVID = Fp{ItemID}(1,1);
                SectorId = Fp{ItemID}(1,2);
                
                % Remove from vector; also remove from other vectors
                for (k = 1:4)
                    if (~isempty(Fp{k}))
                        ix = find(Fp{k}(:,1)==FVID);
                        
                        if (~isempty(ix))
                            Fp{k} = Fp{k}(ix(end)+1:end,:);
                        end
                    end
                end
                
                training(fid,j,QuestionType,SectorId,ItemID,FVID,z)
                
                if (movefile == 1)
                    if (z < 7)
                        cd('../2-Facility Palette');
                        string1 = ['image' num2str(FVID) '.jpg'];
                        string2 = ['feature_vector' num2str(FVID) '.xls'];
                        string3 = ['feature_vector' num2str(FVID) '.csv'];
                        eval(['dos(''copy ' string1 ' "C:\Users\mfine\Documents\5_ICArUS\CPD\output\Pilot\Training 1 (1-6)\FeatureVector"'')']);
                        eval(['dos(''copy ' string2 ' "C:\Users\mfine\Documents\5_ICArUS\CPD\output\Pilot\Training 1 (1-6)\FeatureVector"'')']);
                        eval(['dos(''copy ' string3 ' "C:\Users\mfine\Documents\5_ICArUS\CPD\output\Pilot\Training 1 (1-6)\FeatureVector"'')']);
                        cd('../Pilot');
                    elseif (z >= 7 && z < 13)
                        cd('../3-Facility Palette');
                        string1 = ['image' num2str(FVID) '.jpg'];
                        string2 = ['feature_vector' num2str(FVID) '.xls'];
                        string3 = ['feature_vector' num2str(FVID) '.csv'];
                        eval(['dos(''copy ' string1 ' "C:\Users\mfine\Documents\5_ICArUS\CPD\output\Pilot\Training 2 (7-12)\FeatureVector"'')']);
                        eval(['dos(''copy ' string2 ' "C:\Users\mfine\Documents\5_ICArUS\CPD\output\Pilot\Training 2 (7-12)\FeatureVector"'')']);
                        eval(['dos(''copy ' string3 ' "C:\Users\mfine\Documents\5_ICArUS\CPD\output\Pilot\Training 2 (7-12)\FeatureVector"'')']);
                        cd('../Pilot');
                    elseif (z >= 13 && z < 19)
                        cd('../4-Facility Palette');
                        string1 = ['image' num2str(FVID) '.jpg'];
                        string2 = ['feature_vector' num2str(FVID) '.xls'];
                        string3 = ['feature_vector' num2str(FVID) '.csv'];
                        eval(['dos(''copy ' string1 ' "C:\Users\mfine\Documents\5_ICArUS\CPD\output\Pilot\Training 3 (13-18)\FeatureVector"'')']);
                        eval(['dos(''copy ' string2 ' "C:\Users\mfine\Documents\5_ICArUS\CPD\output\Pilot\Training 3 (13-18)\FeatureVector"'')']);
                        eval(['dos(''copy ' string3 ' "C:\Users\mfine\Documents\5_ICArUS\CPD\output\Pilot\Training 3 (13-18)\FeatureVector"'')']);
                        cd('../Pilot');
                    elseif  (z == 19)
                        cd('../4-Facility Palette');
                        string1 = ['image' num2str(FVID) '.jpg'];
                        string2 = ['feature_vector' num2str(FVID) '.xls'];
                        string3 = ['feature_vector' num2str(FVID) '.csv'];
                        eval(['dos(''copy ' string1 ' "C:\Users\mfine\Documents\5_ICArUS\CPD\output\Pilot\Testing 1 (19)\FeatureVector"'')']);
                        eval(['dos(''copy ' string2 ' "C:\Users\mfine\Documents\5_ICArUS\CPD\output\Pilot\Testing 1 (19)\FeatureVector"'')']);
                        eval(['dos(''copy ' string3 ' "C:\Users\mfine\Documents\5_ICArUS\CPD\output\Pilot\Testing 1 (19)\FeatureVector"'')']);
                        cd('../Pilot');
                    elseif (z == 20)
                        cd('../4-Facility Palette');
                        string1 = ['image' num2str(FVID) '.jpg'];
                        string2 = ['feature_vector' num2str(FVID) '.xls'];
                        string3 = ['feature_vector' num2str(FVID) '.csv'];
                        eval(['dos(''copy ' string1 ' "C:\Users\mfine\Documents\5_ICArUS\CPD\output\Pilot\Testing 2 (20)\FeatureVector"'')']);
                        eval(['dos(''copy ' string2 ' "C:\Users\mfine\Documents\5_ICArUS\CPD\output\Pilot\Testing 2 (20)\FeatureVector"'')']);
                        eval(['dos(''copy ' string3 ' "C:\Users\mfine\Documents\5_ICArUS\CPD\output\Pilot\Testing 2 (20)\FeatureVector"'')']);
                        cd('../Pilot');
                    end
                end
                
            elseif(strcmp(QuestionType,'AnnotationGrid'))
                SceneItem = cell2mat(examfile.Set(z).Training.QuestionType{j}(2));
                index = cell2mat(examfile.Set(z).Training.QuestionType{j}(3));
                
                fprintf(fid,'<Training TrainingNum="%d" xsi:type="%s"> \n',j,QuestionType);
                fprintf(fid,'<BaseLayers> \n');
                if (z == 1 || z == 7 || z == 13)
                    fprintf(fid,'<LayerId>1</LayerId> \n');
                elseif (z == 3 || z == 9 || z == 15)
                    fprintf(fid,'<LayerId>1</LayerId> \n');
                    fprintf(fid,'<LayerId>2</LayerId> \n');
                elseif (z == 5 || z == 11 || z == 17)
                    fprintf(fid,'<LayerId>1</LayerId> \n');
                    fprintf(fid,'<LayerId>2</LayerId> \n');
                    fprintf(fid,'<LayerId>3</LayerId> \n');
                end
                fprintf(fid,'</BaseLayers> \n');
                
                for (i = 1:length(SceneItem))
                    
                    ItemID = SceneItem(i);
                    indexID = index(i);
                    
                    if (mod(i,4)==1)
                        if (i < 5)
                            row = 1;
                        elseif (i >=5 && i < 9)
                            row = 2;
                        elseif (i >= 9 && i < 13)
                            row = 3;
                        else
                            row = 4;
                        end
                        fprintf(fid,'<AnnotationGridRow Row="%s"> \n',num2str(row));
                        fprintf(fid,'<ItemId>%s</ItemId> \n',num2str(ItemID));
                    end
                    
                    % Choose a scene; find the SectorId
                    FVID = Fp{ItemID}(1,1);
                    SectorId = Fp{ItemID}(1,2);
                    
                    % Check against the index
                    if (~isempty(indexID))
                        if (~isempty(IND))
                            if(~isempty(IND(IND(:,1)==indexID,2)))
                                FVID = IND(IND(:,1)==indexID,2); FVID = FVID(1);
                                SectorId = IND(IND(:,1)==indexID,3); SectorId = SectorId(1);
                            end
                        end
                        IND = cat(1,IND,[indexID FVID SectorId]);
                    end
                    
                    % Remove from vector; also remove from other vectors
                    for (k = 1:4)
                        if (~isempty(Fp{k}))
                            ix = find(Fp{k}(:,1)==FVID);
                            
                            if (~isempty(ix))
                                Fp{k} = Fp{k}(ix(end)+1:end,:);
                            end
                        end
                    end
                    
                    training(fid,j,QuestionType,SectorId,ItemID,FVID,z,mod(i-1,4)+1)
                    if (mod(i,4)==0)
                        fprintf(fid,'</AnnotationGridRow> \n');
                    end
                    
                    if (movefile == 1)
                        if (z < 7)
                            cd('../All Palettes');
                            string1 = ['image' num2str(FVID) '.jpg'];
                            string2 = ['feature_vector' num2str(FVID) '.xls'];
                            string3 = ['feature_vector' num2str(FVID) '.csv'];
                            eval(['dos(''copy ' string1 ' "C:\Users\mfine\Documents\5_ICArUS\CPD\output\Pilot\Training 1 (1-6)\FeatureVector"'')']);
                            eval(['dos(''copy ' string2 ' "C:\Users\mfine\Documents\5_ICArUS\CPD\output\Pilot\Training 1 (1-6)\FeatureVector"'')']);
                            eval(['dos(''copy ' string3 ' "C:\Users\mfine\Documents\5_ICArUS\CPD\output\Pilot\Training 1 (1-6)\FeatureVector"'')']);
                            cd('../Pilot');
                        elseif (z >= 7 && z < 13)
                            cd('../All Palettes');
                            string1 = ['image' num2str(FVID) '.jpg'];
                            string2 = ['feature_vector' num2str(FVID) '.xls'];
                            string3 = ['feature_vector' num2str(FVID) '.csv'];
                            eval(['dos(''copy ' string1 ' "C:\Users\mfine\Documents\5_ICArUS\CPD\output\Pilot\Training 2 (7-12)\FeatureVector"'')']);
                            eval(['dos(''copy ' string2 ' "C:\Users\mfine\Documents\5_ICArUS\CPD\output\Pilot\Training 2 (7-12)\FeatureVector"'')']);
                            eval(['dos(''copy ' string3 ' "C:\Users\mfine\Documents\5_ICArUS\CPD\output\Pilot\Training 2 (7-12)\FeatureVector"'')']);
                            cd('../Pilot');
                        elseif (z >= 13 && z < 19)
                            cd('../All Palettes');
                            string1 = ['image' num2str(FVID) '.jpg'];
                            string2 = ['feature_vector' num2str(FVID) '.xls'];
                            string3 = ['feature_vector' num2str(FVID) '.csv'];
                            eval(['dos(''copy ' string1 ' "C:\Users\mfine\Documents\5_ICArUS\CPD\output\Pilot\Training 3 (13-18)\FeatureVector"'')']);
                            eval(['dos(''copy ' string2 ' "C:\Users\mfine\Documents\5_ICArUS\CPD\output\Pilot\Training 3 (13-18)\FeatureVector"'')']);
                            eval(['dos(''copy ' string3 ' "C:\Users\mfine\Documents\5_ICArUS\CPD\output\Pilot\Training 3 (13-18)\FeatureVector"'')']);
                            cd('../Pilot');
                        elseif  (z == 19)
                            cd('../All Palettes');
                            string1 = ['image' num2str(FVID) '.jpg'];
                            string2 = ['feature_vector' num2str(FVID) '.xls'];
                            string3 = ['feature_vector' num2str(FVID) '.csv'];
                            eval(['dos(''copy ' string1 ' "C:\Users\mfine\Documents\5_ICArUS\CPD\output\Pilot\Testing 1 (19)\FeatureVector"'')']);
                            eval(['dos(''copy ' string2 ' "C:\Users\mfine\Documents\5_ICArUS\CPD\output\Pilot\Testing 1 (19)\FeatureVector"'')']);
                            eval(['dos(''copy ' string3 ' "C:\Users\mfine\Documents\5_ICArUS\CPD\output\Pilot\Testing 1 (19)\FeatureVector"'')']);
                            cd('../Pilot');
                        elseif (z == 20)
                            cd('../All Palettes');
                            string1 = ['image' num2str(FVID) '.jpg'];
                            string2 = ['feature_vector' num2str(FVID) '.xls'];
                            string3 = ['feature_vector' num2str(FVID) '.csv'];
                            eval(['dos(''copy ' string1 ' "C:\Users\mfine\Documents\5_ICArUS\CPD\output\Pilot\Testing 2 (20)\FeatureVector"'')']);
                            eval(['dos(''copy ' string2 ' "C:\Users\mfine\Documents\5_ICArUS\CPD\output\Pilot\Testing 2 (20)\FeatureVector"'')']);
                            eval(['dos(''copy ' string3 ' "C:\Users\mfine\Documents\5_ICArUS\CPD\output\Pilot\Testing 2 (20)\FeatureVector"'')']);
                            cd('../Pilot');
                        end
                    end
                end
            end
            
            fprintf(fid,'</Training> \n');
        end
        fprintf(fid,' </ExamPhase> \n \n');
    end
    
    if(~isempty(examfile.Set(z).Test))
        fprintf(fid,'<!-- BLOCK %s --> \n',num2str(z));
        fprintf(fid,'<!--  Trials -->   \n');
        fprintf(fid,'<ExamPhase name="Testing" xsi:type="Test">   \n');
        
        for (j = 1:length(examfile.Set(z).Test.QuestionType))
            
            % Read examfile
            QuestionType = cell2mat(examfile.Set(z).Test.QuestionType{j}(1));
            SceneItem = cell2mat(examfile.Set(z).Test.QuestionType{j}(2));
            
            if (strcmp(SceneItem,'A'))
                ItemID = 1;
            elseif (strcmp(SceneItem,'B'))
                ItemID = 2;
            elseif (strcmp(SceneItem,'C'))
                ItemID = 3;
            elseif (strcmp(SceneItem,'D'))
                ItemID = 4;
            end
            
            PresentationType = cell2mat(examfile.Set(z).Test.QuestionType{j}(3));
            
            switch PresentationType
                case {'Simultaneous'}
                    BaseLayers_temp = cell2mat(examfile.Set(z).Test.QuestionType{j}(4));
                    %                     disp(BaseLayers_temp)
                    AdditionalLayers_temp = [];
                    
                    if (length(examfile.Set(z).Test.QuestionType{j}) > 4)
                        index = cell2mat(examfile.Set(z).Test.QuestionType{j}(5));
                        %                         index = str2double(index(7:end));
                    else
                        index = [];
                    end
                    
                case {'Sequential'}
                    BaseLayers_temp = cell2mat(examfile.Set(z).Test.QuestionType{j}(4));
                    AdditionalLayers_temp = [cell2mat(examfile.Set(z).Test.QuestionType{j}(5)) cell2mat(examfile.Set(z).Test.QuestionType{j}(6))];
                    
                    if (length(examfile.Set(z).Test.QuestionType{j}) > 6)
                        index = cell2mat(examfile.Set(z).Test.QuestionType{j}(7));
                        %                         index = str2double(index(7:end));
                    else
                        index = [];
                    end
                    
                case {'User Choice'}
                    BaseLayers_temp = cell2mat(examfile.Set(z).Test.QuestionType{j}(4));
                    AdditionalLayers_temp = cell2mat(examfile.Set(z).Test.QuestionType{j}(5));
                    
                    if (length(examfile.Set(z).Test.QuestionType{j}) > 5)
                        index = cell2mat(examfile.Set(z).Test.QuestionType{j}(6));
                        %                         index = str2double(index(7:end));
                    else
                        index = [];
                    end
            end
            
            BaseLayers = [];
            if (exist('BaseLayers_temp','var') ==  1)
                for (k = 1:length(BaseLayers_temp))
                    if (strcmp(BaseLayers_temp(k),'I'))
                        BaseLayers(k) = 1;
                    elseif (strcmp(BaseLayers_temp(k),'S'))
                        BaseLayers(k) = 2;
                    elseif (strcmp(BaseLayers_temp(k),'M'))
                        BaseLayers(k) = 3;
                    end
                end
            end
            
            AdditionalLayers = [];
            if (exist('AdditionalLayers_temp','var') == 1)
                for (k = 1:length(AdditionalLayers_temp))
                    if (strcmp(AdditionalLayers_temp(k),'I'))
                        AdditionalLayers(k) = 1;
                    elseif (strcmp(AdditionalLayers_temp(k),'S'))
                        AdditionalLayers(k) = 2;
                    elseif (strcmp(AdditionalLayers_temp(k),'M'))
                        AdditionalLayers(k) = 3;
                    end
                end
            end
            
            % Choose a scene; find the SectorId (for Identify)
            FVID = Fp{ItemID}(1,1);
            SectorId = Fp{ItemID}(1,2);
            examfile.Set(z).Test.FVID(j) = FVID;
            examfile.Set(z).Test.SectorId(j) = SectorId;
            
            % Check against the index
            if (~isempty(index))
                if (~isempty(IND))
                    if(~isempty(IND(IND(:,1)==index,2)))
                        FVID = IND(IND(:,1)==index,2); FVID = FVID(1);
                        SectorId = IND(IND(:,1)==index,3); SectorId = SectorId(1);
                    end
                end
                IND = cat(1,IND,[index FVID SectorId]);
            end
            
            % Remove from vector; also remove from other vectors
            for (k = 1:4)
                if (~isempty(Fp{k}))
                    ix = find(Fp{k}(:,1)==FVID);
                    
                    if (~isempty(ix))
                        Fp{k} = Fp{k}(ix(end)+1:end,:);
                    end
                end
            end
            
            %             disp(BaseLayers)
            testing(fid,j,QuestionType,SectorId,ItemID,PresentationType,BaseLayers,AdditionalLayers,FVID,z)
            
            if (movefile == 1)
                if (z < 7)
                    cd('../2-Facility Palette');
                    string1 = ['image' num2str(FVID) '.jpg'];
                    string2 = ['feature_vector' num2str(FVID) '.xls'];
                    string3 = ['feature_vector' num2str(FVID) '.csv'];
                    eval(['dos(''copy ' string1 ' "C:\Users\mfine\Documents\5_ICArUS\CPD\output\Pilot\Training 1 (1-6)\FeatureVector"'')']);
                    eval(['dos(''copy ' string2 ' "C:\Users\mfine\Documents\5_ICArUS\CPD\output\Pilot\Training 1 (1-6)\FeatureVector"'')']);
                    eval(['dos(''copy ' string3 ' "C:\Users\mfine\Documents\5_ICArUS\CPD\output\Pilot\Training 1 (1-6)\FeatureVector"'')']);
                    cd('../Pilot');
                elseif (z >= 7 && z < 13)
                    cd('../3-Facility Palette');
                    string1 = ['image' num2str(FVID) '.jpg'];
                    string2 = ['feature_vector' num2str(FVID) '.xls'];
                    string3 = ['feature_vector' num2str(FVID) '.csv'];
                    eval(['dos(''copy ' string1 ' "C:\Users\mfine\Documents\5_ICArUS\CPD\output\Pilot\Training 2 (7-12)\FeatureVector"'')']);
                    eval(['dos(''copy ' string2 ' "C:\Users\mfine\Documents\5_ICArUS\CPD\output\Pilot\Training 2 (7-12)\FeatureVector"'')']);
                    eval(['dos(''copy ' string3 ' "C:\Users\mfine\Documents\5_ICArUS\CPD\output\Pilot\Training 2 (7-12)\FeatureVector"'')']);
                    cd('../Pilot');
                elseif (z >= 13 && z < 19)
                    cd('../4-Facility Palette');
                    string1 = ['image' num2str(FVID) '.jpg'];
                    string2 = ['feature_vector' num2str(FVID) '.xls'];
                    string3 = ['feature_vector' num2str(FVID) '.csv'];
                    eval(['dos(''copy ' string1 ' "C:\Users\mfine\Documents\5_ICArUS\CPD\output\Pilot\Training 3 (13-18)\FeatureVector"'')']);
                    eval(['dos(''copy ' string2 ' "C:\Users\mfine\Documents\5_ICArUS\CPD\output\Pilot\Training 3 (13-18)\FeatureVector"'')']);
                    eval(['dos(''copy ' string3 ' "C:\Users\mfine\Documents\5_ICArUS\CPD\output\Pilot\Training 3 (13-18)\FeatureVector"'')']);
                    cd('../Pilot');
                elseif  (z == 19)
                    cd('../4-Facility Palette');
                    string1 = ['image' num2str(FVID) '.jpg'];
                    string2 = ['feature_vector' num2str(FVID) '.xls'];
                    string3 = ['feature_vector' num2str(FVID) '.csv'];
                    eval(['dos(''copy ' string1 ' "C:\Users\mfine\Documents\5_ICArUS\CPD\output\Pilot\Testing 1 (19)\FeatureVector"'')']);
                    eval(['dos(''copy ' string2 ' "C:\Users\mfine\Documents\5_ICArUS\CPD\output\Pilot\Testing 1 (19)\FeatureVector"'')']);
                    eval(['dos(''copy ' string3 ' "C:\Users\mfine\Documents\5_ICArUS\CPD\output\Pilot\Testing 1 (19)\FeatureVector"'')']);
                    cd('../Pilot');
                elseif (z == 20)
                    cd('../4-Facility Palette');
                    string1 = ['image' num2str(FVID) '.jpg'];
                    string2 = ['feature_vector' num2str(FVID) '.xls'];
                    string3 = ['feature_vector' num2str(FVID) '.csv'];
                    eval(['dos(''copy ' string1 ' "C:\Users\mfine\Documents\5_ICArUS\CPD\output\Pilot\Testing 2 (20)\FeatureVector"'')']);
                    eval(['dos(''copy ' string2 ' "C:\Users\mfine\Documents\5_ICArUS\CPD\output\Pilot\Testing 2 (20)\FeatureVector"'')']);
                    eval(['dos(''copy ' string3 ' "C:\Users\mfine\Documents\5_ICArUS\CPD\output\Pilot\Testing 2 (20)\FeatureVector"'')']);
                    cd('../Pilot');
                end
            end
        end
        fprintf(fid,' </ExamPhase> \n \n');
    end
end

fprintf(fid,'</IcarusEvaluation>');
fclose(fid);
diary off

function training(fid,TrainingNum,TrainingType,SectorId,ItemId,FVID,z,column)
FeatureVectorUrl = ['feature_vector' num2str(FVID) '.csv'];
ObjectPaletteUrl = 'object_palette.csv';

if (strcmp(TrainingType,'Annotation'))
    fprintf(fid,'<Training TrainingNum="%d" xsi:type="%s"> \n',TrainingNum,TrainingType);
    fprintf(fid, '<FeatureVectorUrl>%s \n',FeatureVectorUrl);
    fprintf(fid,'</FeatureVectorUrl> \n');
    fprintf(fid, '<ObjectPaletteUrl>%s \n',ObjectPaletteUrl);
    fprintf(fid,'</ObjectPaletteUrl> \n');
    
    fprintf(fid,'<BaseLayers> \n');
    if (z == 1 || z == 7 || z == 13)
        fprintf(fid,'<LayerId>1</LayerId> \n');
    elseif (z == 3 || z == 9 || z == 15)
        fprintf(fid,'<LayerId>1</LayerId> \n');
        fprintf(fid,'<LayerId>2</LayerId> \n');
    elseif (z == 5 || z == 11 || z == 17)
        fprintf(fid,'<LayerId>1</LayerId> \n');
        fprintf(fid,'<LayerId>2</LayerId> \n');
        fprintf(fid,'<LayerId>3</LayerId> \n');
    end
    fprintf(fid,'</BaseLayers> \n');
    
    fprintf(fid,'<Annotations> \n');
    fprintf(fid,'<Annotation> \n');
    fprintf(fid,'<SectorId>%d</SectorId>  \n',SectorId);
    fprintf(fid,'<ItemId>%d</ItemId> \n',ItemId);
    fprintf(fid,'</Annotation> \n');
    fprintf(fid,'</Annotations> \n');
    fprintf(fid,'</Training> \n \n');
    
elseif(strcmp(TrainingType,'AnnotationGrid'))
    FeatureVectorUrl = ['feature_vector' num2str(FVID) '.csv'];
    ObjectPaletteUrl = 'object_palette.csv';
    
    fprintf(fid,'<AnnotationGridColumn Column="%s"> \n',num2str(column));
    fprintf(fid, '<FeatureVectorUrl>%s \n',FeatureVectorUrl);
    fprintf(fid,'</FeatureVectorUrl> \n');
    fprintf(fid, '<ObjectPaletteUrl>%s \n',ObjectPaletteUrl);
    fprintf(fid,'</ObjectPaletteUrl> \n');
    fprintf(fid,'<SectorId>%d</SectorId>  \n',SectorId);
    fprintf(fid,'</AnnotationGridColumn> \n');
end
function testing(fid,TrialNum,QuestionType,SectorId,SceneItem,PresentationType,BaseLayers,AdditionalLayers,FVID,z)
FeatureVectorUrl = ['feature_vector' num2str(FVID) '.csv'];
ObjectPaletteUrl = 'object_palette.csv';

% IdentifyItem
if(strcmp(QuestionType,'Identify'))
    fprintf(fid,'<Trial TrialNum="%d" xsi:type="ScenePresentation"> \n',TrialNum);
    fprintf(fid, '<FeatureVectorUrl>%s \n',FeatureVectorUrl);
    fprintf(fid,'</FeatureVectorUrl> \n');
    fprintf(fid, '<ObjectPaletteUrl>%s \n',ObjectPaletteUrl);
    fprintf(fid,'</ObjectPaletteUrl> \n');
    
    fprintf(fid,'<Question xsi:type="IdentifyItem"> \n');
    fprintf(fid,'<SceneItemsToProbe> \n');
    
    if (z < 7)
        fprintf(fid,'<ItemId>1</ItemId> \n');
        fprintf(fid,'<ItemId>2</ItemId> \n');
    elseif (z >=7 && z < 13)
        fprintf(fid,'<ItemId>1</ItemId> \n');
        fprintf(fid,'<ItemId>2</ItemId> \n');
        fprintf(fid,'<ItemId>3</ItemId> \n');
    elseif (z >=13)
        fprintf(fid,'<ItemId>1</ItemId> \n');
        fprintf(fid,'<ItemId>2</ItemId> \n');
        fprintf(fid,'<ItemId>3</ItemId> \n');
        fprintf(fid,'<ItemId>4</ItemId> \n');
    end
    
    fprintf(fid,'</SceneItemsToProbe> \n');
    fprintf(fid,'<SectorToProbe>%d</SectorToProbe> \n',SectorId);
    fprintf(fid,'</Question> \n');
    
    switch PresentationType
        case {'Simultaneous'}
            fprintf(fid,'<BaseLayers> \n');
            for (i = 1:length(BaseLayers))
                fprintf(fid,'<LayerId>%d</LayerId> \n',BaseLayers(i));
            end
            fprintf(fid,'</BaseLayers> \n');
            
        case {'Sequential'}
            fprintf(fid,'<BaseLayers> \n');
            
            for (i = 1:length(BaseLayers))
                fprintf(fid,'<LayerId>%d</LayerId> \n',BaseLayers(i));
            end
            fprintf(fid,'</BaseLayers> \n');
            
            fprintf(fid,'<AdditionalLayers> \n');
            fprintf(fid,'<LayerPresentation xsi:type="SequentialPresentation"> \n');
            for (i = 1:length(AdditionalLayers))
                fprintf(fid,'<Layers> \n');
                fprintf(fid,'<LayerId>%d</LayerId> \n',AdditionalLayers(i));
                fprintf(fid,'</Layers> \n');
            end
            fprintf(fid,'</LayerPresentation> \n');
            fprintf(fid,'</AdditionalLayers> \n');
            
        case {'User Choice'}
            fprintf(fid,'<BaseLayers> \n');
            
            for (i = 1:length(BaseLayers))
                fprintf(fid,'<LayerId>%d</LayerId> \n',BaseLayers(i));
            end
            fprintf(fid,'</BaseLayers> \n');
            
            fprintf(fid,'<AdditionalLayers> \n');
            fprintf(fid,'<LayerPresentation xsi:type="UserChoicePresentation"> \n');
            fprintf(fid,'<numOptionalLayersToShow>1</numOptionalLayersToShow> \n');
            fprintf(fid,' <OptionalLayers> \n');
            for (i = 1:length(AdditionalLayers))
                fprintf(fid,'<LayerId>%d</LayerId> \n',AdditionalLayers(i));
            end
            fprintf(fid,'</OptionalLayers> \n');
            fprintf(fid,'</LayerPresentation> \n');
            fprintf(fid,'</AdditionalLayers> \n');
    end
    
    fprintf(fid,'</Trial> \n \n');
end

% LocateItem
if(strcmp(QuestionType,'Locate'))
    fprintf(fid,'<Trial TrialNum="%d" xsi:type="ScenePresentation"> \n',TrialNum);
    fprintf(fid, '<FeatureVectorUrl>%s \n',FeatureVectorUrl);
    fprintf(fid,'</FeatureVectorUrl> \n');
    fprintf(fid, '<ObjectPaletteUrl>%s \n',ObjectPaletteUrl);
    fprintf(fid,'</ObjectPaletteUrl> \n');
    
    fprintf(fid,'<Question xsi:type="LocateItem"> \n');
    fprintf(fid,'<SceneItemToProbe>%d</SceneItemToProbe> \n',SceneItem);
    fprintf(fid,'<SectorsToProbe> \n');
    if (z < 7)
        fprintf(fid,'<SectorId>1</SectorId> \n');
        fprintf(fid,'<SectorId>2</SectorId> \n');
    elseif (z >=7 && z < 13)
        fprintf(fid,'<SectorId>1</SectorId> \n');
        fprintf(fid,'<SectorId>2</SectorId> \n');
        fprintf(fid,'<SectorId>3</SectorId> \n');
    elseif (z >=13)
        fprintf(fid,'<SectorId>1</SectorId> \n');
        fprintf(fid,'<SectorId>2</SectorId> \n');
        fprintf(fid,'<SectorId>3</SectorId> \n');
        fprintf(fid,'<SectorId>4</SectorId> \n');
    end
    fprintf(fid,'</SectorsToProbe> \n');
    fprintf(fid,'</Question> \n');
    
    switch PresentationType
        case {'Simultaneous'}
            fprintf(fid,'<BaseLayers> \n');
            for (i = 1:length(BaseLayers))
                fprintf(fid,'<LayerId>%d</LayerId> \n',BaseLayers(i));
            end
            fprintf(fid,'</BaseLayers> \n');
            
        case {'Sequential'}
            fprintf(fid,'<BaseLayers> \n');
            
            for (i = 1:length(BaseLayers))
                fprintf(fid,'<LayerId>%d</LayerId> \n',BaseLayers(i));
            end
            fprintf(fid,'</BaseLayers> \n');
            
            fprintf(fid,'<AdditionalLayers> \n');
            fprintf(fid,'<LayerPresentation xsi:type="SequentialPresentation"> \n');
            for (i = 1:length(AdditionalLayers))
                fprintf(fid,'<Layers> \n');
                fprintf(fid,'<LayerId>%d</LayerId> \n',AdditionalLayers(i));
                fprintf(fid,'</Layers> \n');
            end
            fprintf(fid,'</LayerPresentation> \n');
            fprintf(fid,'</AdditionalLayers> \n');
            
        case {'User Choice'}
            fprintf(fid,'<BaseLayers> \n');
            
            for (i = 1:length(BaseLayers))
                fprintf(fid,'<LayerId>%d</LayerId> \n',BaseLayers(i));
            end
            fprintf(fid,'</BaseLayers> \n');
            
            fprintf(fid,'<AdditionalLayers> \n');
            fprintf(fid,'<LayerPresentation xsi:type="UserChoicePresentation"> \n');
            fprintf(fid,'<numOptionalLayersToShow>1</numOptionalLayersToShow> \n');
            fprintf(fid,'<OptionalLayers> \n');
            for (i = 1:length(AdditionalLayers))
                fprintf(fid,'<LayerId>%d</LayerId> \n',AdditionalLayers(i));
            end
            fprintf(fid,'</OptionalLayers> \n');
            fprintf(fid,'</LayerPresentation> \n');
            fprintf(fid,'</AdditionalLayers> \n');
    end
    
    fprintf(fid,'</Trial> \n \n');
end

