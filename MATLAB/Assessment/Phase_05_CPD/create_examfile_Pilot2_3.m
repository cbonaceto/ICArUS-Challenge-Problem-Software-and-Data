clear all; clc
% BLOCK 1 (2-Facility)
%---------------------------

% IMINT-only
examfile.Set(1).Training.QuestionType{1} = {'AnnotationGrid' [1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1]    1:16 'I'};
examfile.Set(1).Training.QuestionType{2} = {'AnnotationGrid' [1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1]    17:32 'I'};
examfile.Set(1).Training.QuestionType{3} = {'AnnotationGrid' [1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1]    33:48 'I'};
examfile.Set(1).Training.QuestionType{4} = {'AnnotationGrid' [2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2]    49:64 'I'};
examfile.Set(1).Training.QuestionType{5} = {'AnnotationGrid' [2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2]    65:80 'I'};
examfile.Set(1).Training.QuestionType{6} = {'AnnotationGrid' [2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2]    81:96 'I'};

examfile.Set(2).Test.QuestionType{1} = {'Identify'    'A'    'Simultaneous'    'I'};
examfile.Set(2).Test.QuestionType{2} = {'Identify'    'B'    'Simultaneous'    'I'};

% IMINT and SIGINT-only
examfile.Set(3).Training.QuestionType{1} = {'AnnotationGrid' [1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1]    1:16 'IS'};
examfile.Set(3).Training.QuestionType{2} = {'AnnotationGrid' [1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1]    17:32 'IS'};
examfile.Set(3).Training.QuestionType{3} = {'AnnotationGrid' [1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1]    33:48 'IS'};
examfile.Set(3).Training.QuestionType{4} = {'AnnotationGrid' [2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2]    49:64 'IS'};
examfile.Set(3).Training.QuestionType{5} = {'AnnotationGrid' [2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2]    65:80 'IS'};
examfile.Set(3).Training.QuestionType{6} = {'AnnotationGrid' [2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2]    81:96 'IS'};

examfile.Set(4).Test.QuestionType{2} = {'Identify'    'A'    'Simultaneous'    'IS'};
examfile.Set(4).Test.QuestionType{1} = {'Identify'    'B'    'Simultaneous'    'IS'};

% IMINT, SIGINT, and MASINT
examfile.Set(5).Training.QuestionType{1} = {'AnnotationGrid' [1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1]    1:16 'ISM'};
examfile.Set(5).Training.QuestionType{2} = {'AnnotationGrid' [1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1]    17:32 'ISM'};
examfile.Set(5).Training.QuestionType{3} = {'AnnotationGrid' [1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1]    33:48 'ISM'};
examfile.Set(5).Training.QuestionType{4} = {'AnnotationGrid' [2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2]    49:64 'ISM'};
examfile.Set(5).Training.QuestionType{5} = {'AnnotationGrid' [2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2]    65:80 'ISM'};
examfile.Set(5).Training.QuestionType{6} = {'AnnotationGrid' [2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2]    81:96 'ISM'};

examfile.Set(6).Test.QuestionType{1} = {'Identify'    'A'    'Simultaneous'    'ISM'};
examfile.Set(6).Test.QuestionType{2} = {'Identify'    'B'    'Simultaneous'    'ISM'};

% BLOCK 2 (3-Facility)
%---------------------------

examfile.Set(7).Training.QuestionType{1} = {'AnnotationGrid' [1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1]    1:16 'I'};
examfile.Set(7).Training.QuestionType{2} = {'AnnotationGrid' [1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1]    17:32 'I'};
examfile.Set(7).Training.QuestionType{3} = {'AnnotationGrid' [1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1]    33:48 'I'};
examfile.Set(7).Training.QuestionType{4} = {'AnnotationGrid' [2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2]    49:64 'I'};
examfile.Set(7).Training.QuestionType{5} = {'AnnotationGrid' [2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2]    65:80 'I'};
examfile.Set(7).Training.QuestionType{6} = {'AnnotationGrid' [2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2]    81:96 'I'};
examfile.Set(7).Training.QuestionType{7} = {'AnnotationGrid' [3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3]    97:112 'I'};
examfile.Set(7).Training.QuestionType{8} = {'AnnotationGrid' [3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3]    113:128 'I'};
examfile.Set(7).Training.QuestionType{9} = {'AnnotationGrid' [3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3]    129:144 'I'};

examfile.Set(8).Test.QuestionType{1} = {'Identify'    'A'    'Simultaneous'    'I'};
examfile.Set(8).Test.QuestionType{2} = {'Identify'    'B'    'Simultaneous'    'I'};
examfile.Set(8).Test.QuestionType{3} = {'Identify'    'C'    'Simultaneous'    'I'};

examfile.Set(9).Training.QuestionType{1} = {'AnnotationGrid' [1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1]    1:16 'IS'};
examfile.Set(9).Training.QuestionType{2} = {'AnnotationGrid' [1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1]    17:32 'IS'};
examfile.Set(9).Training.QuestionType{3} = {'AnnotationGrid' [1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1]    33:48 'IS'};
examfile.Set(9).Training.QuestionType{4} = {'AnnotationGrid' [2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2]    49:64 'IS'};
examfile.Set(9).Training.QuestionType{5} = {'AnnotationGrid' [2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2]    65:80 'IS'};
examfile.Set(9).Training.QuestionType{6} = {'AnnotationGrid' [2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2]    81:96 'IS'};
examfile.Set(9).Training.QuestionType{7} = {'AnnotationGrid' [3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3]    97:112 'IS'};
examfile.Set(9).Training.QuestionType{8} = {'AnnotationGrid' [3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3]    113:128 'IS'};
examfile.Set(9).Training.QuestionType{9} = {'AnnotationGrid' [3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3]    129:144 'IS'};

examfile.Set(10).Test.QuestionType{2} = {'Identify'    'A'    'Simultaneous'    'IS'};
examfile.Set(10).Test.QuestionType{1} = {'Identify'    'B'    'Simultaneous'    'IS'};
examfile.Set(10).Test.QuestionType{3} = {'Identify'    'C'    'Simultaneous'    'IS'};

examfile.Set(11).Training.QuestionType{1} = {'AnnotationGrid' [1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1]    1:16 'ISM'};
examfile.Set(11).Training.QuestionType{2} = {'AnnotationGrid' [1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1]    17:32 'ISM'};
examfile.Set(11).Training.QuestionType{3} = {'AnnotationGrid' [1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1]    33:48 'ISM'};
examfile.Set(11).Training.QuestionType{4} = {'AnnotationGrid' [2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2]    49:64 'ISM'};
examfile.Set(11).Training.QuestionType{5} = {'AnnotationGrid' [2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2]    65:80 'ISM'};
examfile.Set(11).Training.QuestionType{6} = {'AnnotationGrid' [2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2]    81:96 'ISM'};
examfile.Set(11).Training.QuestionType{7} = {'AnnotationGrid' [3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3]    97:112 'ISM'};
examfile.Set(11).Training.QuestionType{8} = {'AnnotationGrid' [3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3]    113:128 'ISM'};
examfile.Set(11).Training.QuestionType{9} = {'AnnotationGrid' [3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3]    129:144 'ISM'};

examfile.Set(12).Test.QuestionType{3} = {'Identify'    'A'    'Simultaneous'    'ISM'};
examfile.Set(12).Test.QuestionType{1} = {'Identify'    'B'    'Simultaneous'    'ISM'};
examfile.Set(12).Test.QuestionType{2} = {'Identify'    'C'    'Simultaneous'    'ISM'};

% BLOCK 3 (4-Facility)
%---------------------------

examfile.Set(13).Training.QuestionType{1} = {'AnnotationGrid' [1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1]    1:16 'I'};
examfile.Set(13).Training.QuestionType{2} = {'AnnotationGrid' [1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1]    17:32 'I'};
examfile.Set(13).Training.QuestionType{3} = {'AnnotationGrid' [1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1]    33:48 'I'};
examfile.Set(13).Training.QuestionType{4} = {'AnnotationGrid' [2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2]    49:64 'I'};
examfile.Set(13).Training.QuestionType{5} = {'AnnotationGrid' [2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2]    65:80 'I'};
examfile.Set(13).Training.QuestionType{6} = {'AnnotationGrid' [2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2]    81:96 'I'};
examfile.Set(13).Training.QuestionType{7} = {'AnnotationGrid' [3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3]    97:112 'I'};
examfile.Set(13).Training.QuestionType{8} = {'AnnotationGrid' [3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3]    113:128 'I'};
examfile.Set(13).Training.QuestionType{9} = {'AnnotationGrid' [3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3]    129:144 'I'};
examfile.Set(13).Training.QuestionType{10} = {'AnnotationGrid' [4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4]    145:160 'I'};
examfile.Set(13).Training.QuestionType{11} = {'AnnotationGrid' [4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4]    161:176 'I'};
examfile.Set(13).Training.QuestionType{12} = {'AnnotationGrid' [4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4]    177:192 'I'};

examfile.Set(14).Test.QuestionType{2} = {'Identify'    'A'    'Simultaneous'    'I'};
examfile.Set(14).Test.QuestionType{1} = {'Identify'    'B'    'Simultaneous'    'I'};
examfile.Set(14).Test.QuestionType{3} = {'Identify'    'C'    'Simultaneous'    'I'};
examfile.Set(14).Test.QuestionType{4} = {'Identify'    'D'    'Simultaneous'    'I'};

examfile.Set(15).Training.QuestionType{1} = {'AnnotationGrid' [1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1]    1:16 'IS'};
examfile.Set(15).Training.QuestionType{2} = {'AnnotationGrid' [1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1]    17:32 'IS'};
examfile.Set(15).Training.QuestionType{3} = {'AnnotationGrid' [1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1]    33:48 'IS'};
examfile.Set(15).Training.QuestionType{4} = {'AnnotationGrid' [2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2]    49:64 'IS'};
examfile.Set(15).Training.QuestionType{5} = {'AnnotationGrid' [2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2]    65:80 'IS'};
examfile.Set(15).Training.QuestionType{6} = {'AnnotationGrid' [2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2]    81:96 'IS'};
examfile.Set(15).Training.QuestionType{7} = {'AnnotationGrid' [3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3]    97:112 'IS'};
examfile.Set(15).Training.QuestionType{8} = {'AnnotationGrid' [3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3]    113:128 'IS'};
examfile.Set(15).Training.QuestionType{9} = {'AnnotationGrid' [3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3]    129:144 'IS'};
examfile.Set(15).Training.QuestionType{10} = {'AnnotationGrid' [4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4]    145:160 'IS'};
examfile.Set(15).Training.QuestionType{11} = {'AnnotationGrid' [4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4]    161:176 'IS'};
examfile.Set(15).Training.QuestionType{12} = {'AnnotationGrid' [4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4]    177:192 'IS'};

examfile.Set(16).Test.QuestionType{2} = {'Identify'    'A'    'Simultaneous'    'IS'};
examfile.Set(16).Test.QuestionType{4} = {'Identify'    'B'    'Simultaneous'    'IS'};
examfile.Set(16).Test.QuestionType{1} = {'Identify'    'C'    'Simultaneous'    'IS'};
examfile.Set(16).Test.QuestionType{3} = {'Identify'    'D'    'Simultaneous'    'IS'};

examfile.Set(17).Training.QuestionType{1} = {'AnnotationGrid' [1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1]    1:16 'ISM'};
examfile.Set(17).Training.QuestionType{2} = {'AnnotationGrid' [1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1]    17:32 'ISM'};
examfile.Set(17).Training.QuestionType{3} = {'AnnotationGrid' [1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1]    33:48 'ISM'};
examfile.Set(17).Training.QuestionType{4} = {'AnnotationGrid' [2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2]    49:64 'ISM'};
examfile.Set(17).Training.QuestionType{5} = {'AnnotationGrid' [2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2]    65:80 'ISM'};
examfile.Set(17).Training.QuestionType{6} = {'AnnotationGrid' [2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2]    81:96 'ISM'};
examfile.Set(17).Training.QuestionType{7} = {'AnnotationGrid' [3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3]    97:112 'ISM'};
examfile.Set(17).Training.QuestionType{8} = {'AnnotationGrid' [3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3]    113:128 'ISM'};
examfile.Set(17).Training.QuestionType{9} = {'AnnotationGrid' [3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3]    129:144 'ISM'};
examfile.Set(17).Training.QuestionType{10} = {'AnnotationGrid' [4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4]    145:160 'ISM'};
examfile.Set(17).Training.QuestionType{11} = {'AnnotationGrid' [4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4]    161:176 'ISM'};
examfile.Set(17).Training.QuestionType{12} = {'AnnotationGrid' [4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4]    177:192 'ISM'};

examfile.Set(18).Test.QuestionType{4} = {'Identify'    'A'    'Simultaneous'    'ISM'};
examfile.Set(18).Test.QuestionType{3} = {'Identify'    'B'    'Simultaneous'    'ISM'};
examfile.Set(18).Test.QuestionType{1} = {'Identify'    'C'    'Simultaneous'    'ISM'};
examfile.Set(18).Test.QuestionType{2} = {'Identify'    'D'    'Simultaneous'    'ISM'};

% BLOCK 4 (4-Facility)
%---------------------------
% randperm(24)
% ans =
%      8     2     3    23    22    13    19     1     6    10     9    15    14    24    16    21    20     5    17    18    11    12     4     7

examfile.Set(19).Test.QuestionType{8} = {'Identify' 'A' 'Simultaneous' 'ISM' 1};
examfile.Set(19).Test.QuestionType{2} = {'Identify' 'B' 'Simultaneous' 'ISM' 2};
examfile.Set(19).Test.QuestionType{3} = {'Identify' 'C' 'Simultaneous' 'ISM' 3};
examfile.Set(19).Test.QuestionType{23} = {'Identify' 'D' 'Simultaneous' 'ISM' 4};
examfile.Set(19).Test.QuestionType{22} = {'Identify' 'A' 'Sequential' 'I' 'S','M' 1};
examfile.Set(19).Test.QuestionType{13} = {'Identify' 'B' 'Sequential' 'I' 'S','M' 2};
examfile.Set(19).Test.QuestionType{19} = {'Identify' 'C' 'Sequential' 'I' 'M','S' 3};
examfile.Set(19).Test.QuestionType{1} = {'Identify' 'D' 'Sequential' 'I' 'M','S' 4};
examfile.Set(19).Test.QuestionType{6} = {'Identify' 'A' 'User Choice' 'I' 'SM' 1};
examfile.Set(19).Test.QuestionType{10} = {'Identify' 'B' 'User Choice' 'I' 'SM' 2};
examfile.Set(19).Test.QuestionType{9} = {'Identify' 'C' 'User Choice' 'I' 'SM' 3};
examfile.Set(19).Test.QuestionType{15} = {'Identify' 'D' 'User Choice' 'I' 'SM' 4};

examfile.Set(19).Test.QuestionType{14} = {'Identify' 'A' 'Simultaneous' 'ISM' 5};
examfile.Set(19).Test.QuestionType{24} = {'Identify' 'B' 'Simultaneous' 'ISM' 6};
examfile.Set(19).Test.QuestionType{16} = {'Identify' 'C' 'Simultaneous' 'ISM' 7};
examfile.Set(19).Test.QuestionType{21} = {'Identify' 'D' 'Simultaneous' 'ISM' 8};
examfile.Set(19).Test.QuestionType{20} = {'Identify' 'A' 'Sequential' 'I' 'S','M' 5};
examfile.Set(19).Test.QuestionType{5} = {'Identify' 'B' 'Sequential' 'I' 'S','M' 6};
examfile.Set(19).Test.QuestionType{17} = {'Identify' 'C' 'Sequential' 'I' 'M','S' 7};
examfile.Set(19).Test.QuestionType{18} = {'Identify' 'D' 'Sequential' 'I' 'M','S' 8};
examfile.Set(19).Test.QuestionType{11} = {'Identify' 'A' 'User Choice' 'I' 'SM' 5};
examfile.Set(19).Test.QuestionType{12} = {'Identify' 'B' 'User Choice' 'I' 'SM' 6};
examfile.Set(19).Test.QuestionType{4} = {'Identify' 'C' 'User Choice' 'I' 'SM' 7};
examfile.Set(19).Test.QuestionType{7} = {'Identify' 'D' 'User Choice' 'I' 'SM' 8};

examfile.Set(19).Test.QuestionType{25} = {'Identify' 'B' 'Simultaneous' 'ISM' 2};

% BLOCK 5 (4-Facility)
%---------------------------
% randperm(24)
% ans =
%     12    13    21     4    18    22    16     6    14     2    20    24     7    11     3    23     8     5    17    15     9    19    10     1

examfile.Set(20).Test.QuestionType{12} = {'Locate' 'A' 'Simultaneous' 'ISM' 1};
examfile.Set(20).Test.QuestionType{13} = {'Locate' 'B' 'Simultaneous' 'ISM' 2};
examfile.Set(20).Test.QuestionType{21} = {'Locate' 'C' 'Simultaneous' 'ISM' 3};
examfile.Set(20).Test.QuestionType{4} = {'Locate' 'D' 'Simultaneous' 'ISM' 4};
examfile.Set(20).Test.QuestionType{18} = {'Locate' 'A' 'Sequential' 'I' 'S','M' 1};
examfile.Set(20).Test.QuestionType{22} = {'Locate' 'B' 'Sequential' 'I' 'S','M' 2};
examfile.Set(20).Test.QuestionType{16} = {'Locate' 'C' 'Sequential' 'I' 'M','S' 3};
examfile.Set(20).Test.QuestionType{6} = {'Locate' 'D' 'Sequential' 'I' 'M','S' 4};
examfile.Set(20).Test.QuestionType{14} = {'Locate' 'A' 'User Choice' 'I' 'SM' 1};
examfile.Set(20).Test.QuestionType{2} = {'Locate' 'B' 'User Choice' 'I' 'SM' 2};
examfile.Set(20).Test.QuestionType{20} = {'Locate' 'C' 'User Choice' 'I' 'SM' 3};
examfile.Set(20).Test.QuestionType{24} = {'Locate' 'D' 'User Choice' 'I' 'SM' 4};

examfile.Set(20).Test.QuestionType{7} = {'Locate' 'A' 'Simultaneous' 'ISM' 5};
examfile.Set(20).Test.QuestionType{11} = {'Locate' 'B' 'Simultaneous' 'ISM' 6};
examfile.Set(20).Test.QuestionType{3} = {'Locate' 'C' 'Simultaneous' 'ISM' 7};
examfile.Set(20).Test.QuestionType{23} = {'Locate' 'D' 'Simultaneous' 'ISM' 8};
examfile.Set(20).Test.QuestionType{8} = {'Locate' 'A' 'Sequential' 'I' 'S','M' 5};
examfile.Set(20).Test.QuestionType{5} = {'Locate' 'B' 'Sequential' 'I' 'S','M' 6};
examfile.Set(20).Test.QuestionType{17} = {'Locate' 'C' 'Sequential' 'I' 'M','S' 7};
examfile.Set(20).Test.QuestionType{15} = {'Locate' 'D' 'Sequential' 'I' 'M','S' 8};
examfile.Set(20).Test.QuestionType{9} = {'Locate' 'A' 'User Choice' 'I' 'SM' 5};
examfile.Set(20).Test.QuestionType{19} = {'Locate' 'B' 'User Choice' 'I' 'SM' 6};
examfile.Set(20).Test.QuestionType{10} = {'Locate' 'C' 'User Choice' 'I' 'SM' 7};
examfile.Set(20).Test.QuestionType{1} = {'Locate' 'D' 'User Choice' 'I' 'SM' 8};

examfile.Set(20).Test.QuestionType{25} = {'Locate' 'D' 'Sequential' 'I' 'M','S' 4};