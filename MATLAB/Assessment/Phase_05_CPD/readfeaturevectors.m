function [cases, caseheadings] = readfeaturevectors(directory)

directory = '..\PILOT\';
prefix = 'feature_vector';
suffix = '.XLS';

d = dir([directory prefix '*' suffix]);
numfvs = length(d);

for i = numfvs
    [~, ~, xraw] = xlsread([directory d(i).name]);
    xheadings = xraw(1,:); xraw = xraw(2:end,:);

    % convert FV to case-file format
    % using development guide instructions
end

% Name: LayerID
% Description: The layer identification.
% Format: Scalar 
% Range: [1 … N], where N denotes the number of layers.
% 
% Name: LayerType
% Description: The layer type.
% Format: String
% Range:  Layer types include, but are not limited to, IMINT, SIGINT, and MASINT.
% 
% Name: ObjectID
% Description: For IMINT: 1-100 denote a building; 101-200 denote a road; 201-300 denote a water feature (lake, pond, river etc.).  For MASINT, the chemical sampled (e.g., Chemical A = 1, Chemical B = 2).  For SIGINT, the source of the intelligence (e.g., Government = 1, Civilian = 2).
% Format: Scalar
% Range: [1 … N], where N denotes the number of objects.
% 
% 
% Name: ObjectOrientation
% Description: The orientation of the object specified in ObjectID.  Together with ObjectID, ObjectOrientation points to an entry in the object palette (Section 3.4) which specifies the spatial extent of the object.
% Format: Scalar
% Range: [1 … N], where N denotes the number of allowed orientations.
% 
% Name: Data
% Description: For IMINT, the absence ([]) or presence (1) of rooftop hardware.  Multiple data will be specified as a vector (i.e., a rooftop with 2 pieces of hardware would be specified as [1,2]; see Figure 11).  For MASINT, the chemical concentration ([1,2,3,4]).  For SIGINT, a hit (1).
% Format: Vector
% Range: Layer dependent.
% 
% Name: X
% Description: The X-coordinate of the data (global coordinate system).  For IMINT, the X position of the object’s upper-left corner (closest to the origin).
% Format: Scalar
% Range: [0 … N], where N denotes the number of x-scene grid squares.
% 
% Name: Y
% Description: The Y-coordinate of the data (global coordinate system).  For IMINT, the Y position of the object’s upper-left corner (closest to the origin).
% Format: Scalar
% Range: [0 … N], where N denotes the number of y-scene grid squares.
% 
% Name: Sector
% Description: The sector, or region of interest, in which an object resides.
% Format: Scalar
% Range: [0 … N], where N is the number of sectors defined.
% 
% The spatial extent of each object is provided in the object palette (Section 3.4).  An example vector is given in Appendix B.
