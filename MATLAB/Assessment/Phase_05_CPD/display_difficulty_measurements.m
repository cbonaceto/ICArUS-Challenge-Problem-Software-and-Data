%% Display_difficulty_measurements
% Plots of cue validity vs. mutual information, etc

fperformance = @(x) 0.6435*x + 0.3349;

maxfigure;
nr = 3;
nc = 3;
subplot(nr,nc,1);
plot(meancuevalidity*100,weightedcuevalidity*100,'o');
axis([0 100 0 100]);
axis square;
xlabel('Mean');
ylabel('Weighted');

subplot(nr,nc,2);
plot(meancuevalidity*100,maxcuevalidity*100,'o');
axis([0 100 0 100]);
axis square;
xlabel('Mean');
ylabel('Max');

subplot(nr,nc,3);
plot(meancuevalidity*100,mutinfo,'o');
axis([0 100 0 1]);
axis square;
xlabel('Mean cue validity');
ylabel('Mutual information');

subplot(nr,nc,4);
hist(meancuevalidity*100);
axis([0 100 0 10]);
xlabel('Mean cue validity');
ylabel('# nodes');

subplot(nr,nc,5);
hist(mutinfo);
axis([0 1 0 10]);
xlabel('Mutual information');
ylabel('# nodes');

subplot(nr,nc,7);
plot(nodeinfo,mutinfo,'o');
axis([0 1 0 1]);
xlabel('Node entropy reduction');
ylabel('Node mutual information');
axis square;
