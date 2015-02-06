clear all; clc

load('object_palette_PILOT.mat')
load('terrain_palette_PILOT.mat')
facility_size = 100;

facility=struct('name','', 'object_qty',[], 'object_type',[], ...
    'object_occurrence',[],'object_hardware',[],'spatial_distr',{}, ...
    'palette',{},'map',{});

facility(1).name='Facility A';
facility(1).object_qty= 7; % 4-7
facility(1).object_type=[1 2 3 4 5 6 7];
facility(1).object_occurrence=[0.9 0.9 0.9 0.9 0.2 0.2 0.2];
facility(1).dispersion = [0.60 0.20 0.20];          % Low, Medium, High
facility(1).object_hardware=[0.40 0.50 0.10]; % None, One, Many
facility(1).spatial_distr={'config1'};                 % Distance from a central object

facility(2).name= 'Facility B';
facility(2).object_qty=7; % 4-7
facility(2).object_type=[1 2 3 4 5 6 7];
facility(2).object_occurrence=[0.3 0.3 0.3 0.9 0.9 0.9 0.9];
facility(2).dispersion = [0.20 0.60 0.20];
facility(2).object_hardware=[0.30 0.50 0.20];
facility(2).spatial_distr={'config1'};         

facility(3).name='Facility C';
facility(3).object_qty= 7; % 4-7
facility(3).object_type=[1 2 3 4 5 6 7];
facility(3).object_occurrence=[0.9 0.9 0.4 0.4 0.4 0.9 0.9];
facility(3).dispersion = [0.20 0.20 0.60];
facility(3).object_hardware=[0.20 0.50 0.30];
facility(3).spatial_distr={'config1'};         

facility(4).name= 'Facility D';
facility(4).object_qty=7; % 4-7
facility(4).object_type=[1 2 3 4 5 6 7];
facility(4).object_occurrence=[0.5 0.9 0.9 0.5 0.9 0.9 0.5];
facility(4).dispersion = [1/3 1/3 1/3];
facility(4).object_hardware=[0.10 0.50 0.40];
facility(4).spatial_distr={'config1'};        

[facility(1).palette facility(1).map facility(1).object_vector facility(1).orientation_vector facility(1).dispersion_vector facility(1).dispersion_coefficient_vector facility(1).terrain_vector] = ...
    generate_facility_palette('Facility A',facility,object,2000,facility_size,terrain);
[facility(2).palette facility(2).map facility(2).object_vector facility(2).orientation_vector facility(2).dispersion_vector facility(2).dispersion_coefficient_vector facility(2).terrain_vector] = ...
    generate_facility_palette('Facility B',facility,object,2000,facility_size,terrain);
[facility(3).palette facility(3).map facility(3).object_vector facility(3).orientation_vector facility(3).dispersion_vector facility(3).dispersion_coefficient_vector facility(3).terrain_vector] = ...
    generate_facility_palette('Facility C',facility,object,2000,facility_size,terrain);
[facility(4).palette facility(4).map facility(4).object_vector facility(4).orientation_vector facility(4).dispersion_vector facility(4).dispersion_coefficient_vector facility(4).terrain_vector] = ...
    generate_facility_palette('Facility D',facility,object,2000,facility_size,terrain);