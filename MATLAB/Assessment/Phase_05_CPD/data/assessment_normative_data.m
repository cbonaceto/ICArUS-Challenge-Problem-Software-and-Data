% assessment_normative_data

assessment1_norm = [
0.35 0.12 0.35 0.19 % Bldg1 
0.30 0.10 0.30 0.30 % Bldg2
0.36 0.12 0.16 0.36 % Bldg3 
0.33 0.33 0.15 0.19 % Bldg4 
0.08 0.37 0.17 0.38 % Bldg5 
0.07 0.31 0.31 0.31 % Bldg6 
0.08 0.36 0.36 0.20 % Bldg7 
0.06 0.19 0.31 0.44 % Water
0.20 0.23 0.27 0.30 % 1+ BldgHdwr
0.40 0.10 0.40 0.10 % SIGINTDensity = High or Low 
0.10 0.40 0.10 0.40 % MASINT1Density = High or Low
0.10 0.40 0.40 0.10 % MASINT2Density = High or Low 
];

% layers are I = 1, S = 2, M = 3
assessment1_layer = [1 1 1 1 1  1 1 1 1 2  3 3]';
    
assessment2_norm = [
0.54 % Buildings 1 and 2 together?
0.4  % Buildings 4 and 7 together? 
0.35 % Buildings 3 and 5 together? 
0.31 % Buildings 3 and 7 together? 
0.31 % Buildings 1 and 5 together? 
0.34 % Building 6 and water? 
0.27 % Building 7 and water? 
0.57 % Building 2 with hardware? 
0.48 % Building 7 with hardware? 

0.25 % Any MASINT1 and any MASINT2? 
0.34 % Any SIGINT and any MASINT1? 
0.25 % Any SIGINT and any MASINT2? 
0.25 % No MASINT1 and no MASINT2? 
0.34 % No SIGINT and no MASINT1? 
0.25 % No SIGINT and no MASINT2? 

0.31 % Bldg1 and any MASINT2? 
0.4  % Bldg1 and any SIGINT? 
0.32 % Bldg5 and any MASINT2? 
0.21 % Bldg5 and any SIGINT? 
0.2  % Water and any MASINT2? 
0.17 % Water and any SIGINT? 
];

% Each row is Layer1 Layer2; layers are I = 1, S = 2, M = 3
assessment2_layer = [1 1 1 1 1  1 1 1 1 3  2 2 3 2 2  1 1 1 1 1  1 ;
                     1 1 1 1 1  1 1 1 1 3  3 3 3 3 3  3 2 3 2 3  2]';
                 