
%% Initialize Netica
import norsys.netica.*;
env = Environ('+WilliamsR/MITRE/310-5/34686');

% kill env by calling:   env.finalize
% get a net
%streamer = Streamer('C:\Users\mcaywood\Documents\ICArUS\NeticaJ_418\examples\Data Files\ChestClinic_WithVisuals.dne');
%streamer = Streamer('C:\Users\mcaywood\Documents\ICArUS\CPD\two-var-sigint-masint.dne');
%net = Net(streamer);
%net.compile;

%% Build Chest Clinic Net
% Converted from examples/BuildNet.java
net = Net;
net.setName('ChestClinic');

visitAsia    = Node ('VisitAsia',   'visit,no_visit',  net);
tuberculosis = Node ('Tuberculosis','present,absent',  net);
smoking      = Node ('Smoking',     'smoker,nonsmoker',net);
cancer       = Node ('Cancer',      'present,absent',  net);
tbOrCa       = Node ('TbOrCa',      'true,false',      net);
xRay         = Node ('XRay',        'abnormal,normal', net);
dyspnea      = Node ('Dyspnea',     'present,absent',  net);
bronchitis   = Node ('Bronchitis',  'present,absent',  net);

% note: use methodsview(tuberculosis) to find methods

visitAsia.setTitle ('Visit to Asia');
cancer.setTitle ('Lung Cancer');
tbOrCa.setTitle ('Tuberculosis or Cancer');

visitAsia.state('visit').setTitle ('Visited Asia within the last 3 years');
	
tuberculosis.addLink (visitAsia); % link from visitAsia to tuberculosis
cancer.addLink (smoking);
tbOrCa.addLink (tuberculosis);
tbOrCa.addLink (cancer);
xRay.addLink (tbOrCa);
dyspnea.addLink (tbOrCa);
bronchitis.addLink (smoking);
dyspnea.addLink (bronchitis);

visitAsia.setCPTable ([0.01 0.99]);
% visitAsia.getCPTable([])

% VisitAsia   present  absent
tuberculosis.setCPTable('visit', [0.05 0.95]);
tuberculosis.setCPTable('no_visit', [0.01 0.99]);

smoking.setCPTable ([0.5, 0.5]);

                 % Smoking      present  absent
cancer.setCPTable ('smoker',     [0.1,     0.9]);
cancer.setCPTable ('nonsmoker',  [0.01,    0.99]);

                     % Smoking      present  absent
bronchitis.setCPTable ('smoker',     [0.6,     0.4]);
bronchitis.setCPTable ('nonsmoker',  [0.3,     0.7]);

tbOrCa.setEquation ('TbOrCa (Tuberculosis, Cancer) = Tuberculosis || Cancer');
tbOrCa.equationToTable (1, false, false);

%the above is a convenient way of doing:
%tbOrCa.setCPTable ('present', 'present', 1.0, 0.0);
%tbOrCa.setCPTable ('present', 'absent',  1.0, 0.0);
%tbOrCa.setCPTable ('absent',  'present', 1.0, 0.0);
%tbOrCa.setCPTable ('absent',  'absent',  0.0, 1.0);

               % TbOrCa    abnormal normal
xRay.setCPTable ('true',    [0.98,    0.02]);
xRay.setCPTable ('false',   [0.05,    0.95]);

                 % TbOrCa   Bronchitis 
dyspnea.setCPTable ('true,present', [0.9, 0.1]);
dyspnea.setCPTable ('true,absent',  [0.7, 0.3]);
dyspnea.setCPTable ('false,present', [0.8, 0.2]);
dyspnea.setCPTable ('false,absent',  [0.1, 0.9]);

stream = Streamer('ChestClinic_Matlab.dne');
net.write (stream);

%% Do inference on Chest Clinic
% Converted from examples/DoInference.java

visitAsia    = net.getNode( 'VisitAsia'    );
tuberculosis = net.getNode( 'Tuberculosis' );
xRay         = net.getNode( 'XRay'         );

net.compile();

belief = tuberculosis.getBelief ('present');          
fprintf('\nThe probability of tuberculosis is %.2f\n',belief);

xRay.finding().enterState ('abnormal');
belief = tuberculosis.getBelief ('present');          
fprintf('\nGiven an abnormal X-ray, the probability of tuberculosis is %.2f\n',belief);

visitAsia.finding().enterState ('visit');
belief = tuberculosis.getBelief ('present');          
fprintf('\nGiven an abnormal X-ray and a visit to Asia, the probability of tuberculosis is %.2f\n',belief);

%% Shut down Netica
net.finalize;
env.finalize;
