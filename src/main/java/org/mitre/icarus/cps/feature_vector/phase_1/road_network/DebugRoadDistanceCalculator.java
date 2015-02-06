/* 
 * NOTICE
 * This software was produced for the office of the Director of National Intelligence (ODNI)
 * Intelligence Advanced Research Projects Activity (IARPA) ICArUS program, 
 * BAA number IARPA-BAA-10-04, under contract 2009-0917826-016, and is subject 
 * to the Rights in Data-General Clause 52.227-14, Alt. IV (DEC 2007).
 * 
 * This software and data is provided strictly to support demonstrations of ICArUS challenge problems
 * and to assist in the development of cognitive-neuroscience architectures. It is not intended to be used
 * in operational systems or environments.
 * 
 * Copyright (C) 2015 The MITRE Corporation. All Rights Reserved.
 * 
 */
package org.mitre.icarus.cps.feature_vector.phase_1.road_network;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
//import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.mitre.icarus.cps.app.widgets.dialog.ErrorDlg;
import org.mitre.icarus.cps.app.widgets.map.phase_1.MapConstants_Phase1;
import org.mitre.icarus.cps.app.widgets.map.phase_1.MapPanelContainer;
import org.mitre.icarus.cps.app.widgets.map.phase_1.layers.PlacemarkLayer;
import org.mitre.icarus.cps.app.widgets.map.phase_1.objects.PlacemarkMapObject;
import org.mitre.icarus.cps.app.widgets.map.phase_1.objects.PlacemarkMapObject.PlacemarkShape;
import org.mitre.icarus.cps.feature_vector.phase_1.FeatureVectorManager;
import org.mitre.icarus.cps.feature_vector.phase_1.GridLocation2D;
import org.mitre.icarus.cps.feature_vector.phase_1.GridSize;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupAttack;
import org.mitre.icarus.cps.feature_vector.phase_1.Road;
import org.mitre.icarus.cps.feature_vector.phase_1.TaskData;

/**
 * A road distance calculator that allows for visual debugging using a map panel.
 * 
 * @author CBONACETO
 *
 */
public class DebugRoadDistanceCalculator extends RoadDistanceCalculator {
	
	protected Frame parent;
	
	/** The map panel */
	protected MapPanelContainer mapPanel;
	
	/** The control panel */
	protected ControlPanel controlPanel;
	protected JDialog controlPanelDlg;
	
	/** Swing worker object the calculations are made in */
	protected SwingWorker<Object, Object> calculatorWorker;
	
	protected boolean paused;
	
	protected boolean nextPressed;
	
	protected boolean showControlPanel = true;
	
	protected PlacemarkLayer locationLayer;
	
	protected boolean complete = false;

	public DebugRoadDistanceCalculator(ArrayList<Road> roads, GridSize gridSize, 
			Frame parent, MapPanelContainer mapPanel) {
		super(roads, gridSize);
		this.parent = parent;		
		controlPanel = new ControlPanel();
		setMapPanel(mapPanel);		
	}

	public MapPanelContainer getMapPanel() {
		return mapPanel;
	}

	public void setMapPanel(final MapPanelContainer mapPanel) {
		this.mapPanel = mapPanel;
		if(mapPanel != null) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					mapPanel.getMapPanel().getMapModel().removeLayer("RoadLocations");			
					locationLayer = new PlacemarkLayer("RoadLocations", mapPanel.getMapPanel());
					locationLayer.setEnabled(true);
					locationLayer.setVisible(true);
					mapPanel.getMapPanel().getMapModel().addLayer(locationLayer);
					mapPanel.redrawMap();	
				}
			});			
		}
	}
	
	public boolean isShowControlPanel() {
		return showControlPanel;
	}

	public void setShowControlPanel(boolean showControlPanel) {
		this.showControlPanel = showControlPanel;
	}

	public boolean isComplete() {
		return complete;
	}

	public synchronized void play() {
		paused = false;
		if(controlPanel != null) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					controlPanel.setPaused(false);
				}
			});
		}
	}
	
	public synchronized void pause() {
		paused = true;
		if(controlPanel != null) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					controlPanel.setPaused(true);
				}
			});
		}
	}
	
	public boolean isPaused() {
		return paused;
	}	
	
	public void computeShortestDistanceFromAllLocationsToEndLocations(final ArrayList<GridLocation2D> endLocations) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {				
				if(showControlPanel && controlPanelDlg == null) {			
					controlPanelDlg = new JDialog(parent, "Status");
					controlPanelDlg.setModal(false);
					controlPanelDlg.setResizable(false);
					controlPanelDlg.getContentPane().add(controlPanel);
					controlPanelDlg.pack();
				}
			}
		});		
		
		if(calculatorWorker != null) {
			calculatorWorker.cancel(true);
		}
		complete = false;
		int numLocations = 0;
		if(roads != null) {
			for(Road road : roads) {
				if(road.getVertices() != null) {
					numLocations += road.getVertices().size();
				}
			}
		}
		numLocations *= endLocations.size();
		controlPanel.setNumLocations(numLocations);
		calculatorWorker = new SwingWorker<Object, Object>() {
			@Override
			protected Object doInBackground() throws Exception {
				int currLocation = 0;
				for(GridLocation2D endLocation : endLocations) {
					for(Road road : roads) {
						if(road.getVertices() != null) {						
							for(GridLocation2D vertex : road.getVertices()) {							
								while(paused && !nextPressed && !controlPanel.nextButtonDown && !isCancelled()) {
									try {
										Thread.sleep(10);
									} catch(Exception ex) {}
								}				
								if(controlPanel.nextButtonDown) {
									try {
										Thread.sleep(10);
									} catch(Exception ex) {}
								}								
								if(isCancelled()) break;
								PathGraph pathGraph = null;
								Double distance = null;								
								try {
									pathGraph = computeShortestDistanceFromStartLocationToEndLocation(vertex, endLocation, false);
									if(pathGraph != null) {
										distance = pathGraph.distance;
									}
									if(distance == null || distance.isNaN()) {
										pause();
									}
									else {
										distance *= gridSize.getMilesPerGridUnit();
									}
								} catch(Exception ex) {
									pause();
									ErrorDlg.showErrorDialog(parent, ex, true);
								}
								final PathGraph finalPathGraph = pathGraph;
								final GridLocation2D start = vertex;
								final GridLocation2D end = endLocation;
								final int finalCurrLocation = currLocation;
								final Double finalDistance = distance;
								SwingUtilities.invokeLater(new Runnable() {
									public void run() {									
										controlPanel.setCurrentLocation(finalCurrLocation+1, start, end);
										controlPanel.setCurrentDistance(finalDistance);
										controlPanel.setCurrentDistanceStraightLine(computeStraightLineDistance(start, end));
										clearCurrentLocations();										
										int i = 0;
										for(GridLocation2D node : finalPathGraph.nodes) {
											PlacemarkMapObject nodePlacemark = createPlacemark(node, "N " + i);
											nodePlacemark.setShowMarker(false);
											locationLayer.addMapObject(nodePlacemark);
											i++;
										}										
										addLocationsToMap(start, end);
									}
								});						
								nextPressed = false;							
								currLocation++;
							}						
						}
					}					
				}
				if(!isCancelled()) {
					complete = true;
				}
				return null;
			}			
		};
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if(showControlPanel) {
					controlPanelDlg.setVisible(true);
				}
			}
		});		
		calculatorWorker.execute();
	}
	
	protected double computeStraightLineDistance(GridLocation2D start, GridLocation2D end) {
		return Math.sqrt(Math.pow(start.getX() - end.getX(), 2) + 
				Math.pow(start.getY() - end.getY(), 2)) * gridSize.getMilesPerGridUnit();
	}
	
	protected void clearCurrentLocations() {
		if(mapPanel != null) {
			locationLayer.removeAllMapObjects();
			mapPanel.redrawMap();
		}
	}	 
	
	protected void addLocationsToMap(GridLocation2D start, GridLocation2D end) {
		if(mapPanel != null) {
			locationLayer.addMapObject(createPlacemark(start, "Start"));			
			locationLayer.addMapObject(createPlacemark(end, "End"));
			mapPanel.redrawMap();
		}
	}
	
	protected PlacemarkMapObject createPlacemark(GridLocation2D location, String name) {
		PlacemarkMapObject placemark = new PlacemarkMapObject();
		placemark.setMarkerShape(PlacemarkShape.Circle);
		placemark.setShowMarker(true);
		placemark.setName(name);
		placemark.setShowName(true);
		placemark.setCenterLocation(location);
		placemark.setBorderColor(MapConstants_Phase1.PLACEMARK_MARKER_COLOR);
		placemark.setBorderLineWidth(1.5f);
		placemark.setForegroundColor(MapConstants_Phase1.PLACEMARK_MARKER_COLOR);
		placemark.setBackgroundColor(Color.white);
		return placemark;
	}

	@Override
	public Double computeShortestDistanceFromStartLocationToEndLocation(final GridLocation2D startLocation, 
			final GridLocation2D endLocation) {
		return computeShortestDistanceFromStartLocationToEndLocation(startLocation, endLocation, true).distance;
	}
	
	protected PathGraph computeShortestDistanceFromStartLocationToEndLocation(final GridLocation2D startLocation, 
			final GridLocation2D endLocation, boolean showOnMap) {
		//Get the existing StartGraph or create a new one for the given startLocation
		StartGraph startGraph = getStartGraphForLocation(startLocation);
		if(startGraph !=  null && endLocation != null) {
			final PathGraph pathGraph = new PathGraph(startGraph, endLocation, gridSize);
			if(showOnMap && mapPanel != null) {
				final Double distance = pathGraph.distance;
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {									
						controlPanel.setNumLocations(1);
						controlPanel.setCurrentLocation(1, startLocation, endLocation);
						controlPanel.setCurrentDistance(distance);
						controlPanel.setCurrentDistanceStraightLine(computeStraightLineDistance(startLocation, endLocation));
						clearCurrentLocations();
						addLocationsToMap(startLocation, endLocation);
					}
				});
			}
			return pathGraph;
		}
		return null;
	}

	@Override
	public Map<GridLocation2D, Double> computeShortestDistanceFromStartLocationToEndLocations(
			final GridLocation2D startLocation, List<GridLocation2D> endLocations) {
		StartGraph startGraph = getStartGraphForLocation(startLocation);
		if(startGraph != null && endLocations != null && !endLocations.isEmpty()) {
			Map<GridLocation2D, Double> distances = new HashMap<GridLocation2D, Double>(endLocations.size());
			controlPanel.setNumLocations(endLocations.size());
			int i = 0;
			for(GridLocation2D endLocation : endLocations) {
				final Double distance = new PathGraph(startGraph, endLocation, gridSize).distance;
				distances.put(endLocation, distance);				
				final int finalCurrLocation = i;
				final GridLocation2D end = endLocation;
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						controlPanel.setCurrentLocation(finalCurrLocation+1, startLocation, end);
						controlPanel.setCurrentDistance(distance);
						controlPanel.setCurrentDistanceStraightLine(computeStraightLineDistance(startLocation, end));
						clearCurrentLocations();
						addLocationsToMap(startLocation, end);
					}
				});
				i++;
			}
		}
		return null;
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Distance Calculator Test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridSize gridSize = new GridSize();
		MapPanelContainer mapPanel = new MapPanelContainer(frame, gridSize, true, false);
		mapPanel.setMapPreferredSize(new Dimension(600,600), new Insets(10, 10, 10, 10));
		mapPanel.setRoadLayerEnabled(true);		
		frame.getContentPane().add(mapPanel);
		frame.pack();
		frame.setVisible(true);
		ArrayList<Road> roads = null;
		ArrayList<GridLocation2D> points = new ArrayList<GridLocation2D>();
		//points.add(new GridLocation2D("p1", 32, 37));
		try {
			URL roadsFileUrl = new File("data/Phase_1_CPD/Final_Experiment/ROAD_Task3.csv").toURI().toURL();
			//URL roadsFileUrl = new File("data/Phase_1_CPD/Pilot_Experiment/roads.csv").toURI().toURL();
			//URL roadsFileUrl = new File("data/Phase_1_CPD/Pilot_Experiment/ROAD_2.csv").toURI().toURL();
			roads = FeatureVectorManager.getInstance().getRoads(roadsFileUrl, gridSize);
			mapPanel.setRoads(roads);
			mapPanel.redrawMap();
			for(int trialBlockNum = 0; trialBlockNum < 5; trialBlockNum++) {
			//for(int trialBlockNum = 5; trialBlockNum < 6; trialBlockNum++) {
				URL taskFileUrl = new File("data/Phase_1_CPD/Final_Experiment/task3_" + Integer.toString(trialBlockNum+1) +  ".csv").toURI().toURL();
				//URL taskFileUrl = new File("data/Phase_1_CPD/Pilot_Experiment/task4_" + Integer.toString(trialBlockNum+1) +  ".csv").toURI().toURL();
				TaskData taskData = FeatureVectorManager.getInstance().getTaskData(taskFileUrl, gridSize);
				if(taskData != null && taskData.getAttacks() != null) {
					for(GroupAttack attack : taskData.getAttacks()) {
						//if(attack.getLocation().getY() < 100) {
						points.add(attack.getLocation());
						System.out.println(attack.getLocation());
						//}
					}
				}
			}
		} catch(Exception ex) {ex.printStackTrace();}		
		if(roads != null && !points.isEmpty()) {
			//DEBUG CODE
			/*for(Road road : roads) {
				for(GridLocation2D loc : road.getVertices()) {
					System.out.println("(" + loc.getX() + ", " + loc.getY() + ")");
				}
			}*/
			//END DEBUG CODE
			DebugRoadDistanceCalculator calculator = new DebugRoadDistanceCalculator(roads, gridSize, 
				frame, mapPanel);			
			//System.out.println(calculator.computeShortestDistanceFromStartLocationToEndLocation(points.get(0), new GridLocation2D("p2", 32, 0)));
			calculator.pause();
			calculator.computeShortestDistanceFromAllLocationsToEndLocations(points);						
		}
	}
	
	protected class ControlPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		
		protected JLabel progressLabel;
		protected JLabel startLabel;
		protected JLabel endLabel;
		protected JLabel distanceActualLabel;
		protected JLabel distanceStraightLineLabel;
		
		protected JButton playButton;
		protected JButton pauseButton;
		protected JButton nextButton;
		protected boolean nextButtonDown = false;
		protected SwingWorker<Object, Object> waitTimer;
		
		protected int numLocations;
		
		public ControlPanel() {
			super(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weightx = 0;
			gbc.anchor = GridBagConstraints.WEST;			
			
			JLabel label = new JLabel("Calculating:");
			gbc.weightx = 0;
			gbc.insets.bottom = 10;			
			add(label, gbc);			
			progressLabel = new JLabel("99999/99999");
			Dimension labelPreferredSize = progressLabel.getPreferredSize();
			progressLabel.setPreferredSize(labelPreferredSize);
			progressLabel.setText("");
			gbc.gridx = 1;
			gbc.weightx = 1;
			gbc.insets.left = 5;			
			add(progressLabel, gbc);
			gbc.insets.bottom = 0;
			
			label = new JLabel("Start:");
			gbc.gridx = 0;
			gbc.gridy++;
			gbc.weightx = 0;
			gbc.insets.left = 0;
			add(label, gbc);
			startLabel = new JLabel("(100.000, 100.000)");
			labelPreferredSize = startLabel.getPreferredSize();
			startLabel.setPreferredSize(labelPreferredSize);
			startLabel.setText("");
			gbc.weightx = 1;
			gbc.gridx = 1;
			gbc.insets.left = 5;			
			add(startLabel, gbc);
			
			label = new JLabel("End:");
			gbc.gridx = 0;
			gbc.gridy++;
			gbc.weightx = 0;
			gbc.insets.left = 0;
			add(label, gbc);
			endLabel = new JLabel();
			endLabel.setPreferredSize(labelPreferredSize);
			gbc.gridx = 1;
			gbc.weightx = 1;
			gbc.insets.left = 5;			
			add(endLabel, gbc);
			
			label = new JLabel("Distance (miles):");
			gbc.gridx = 0;
			gbc.gridy++;
			gbc.weightx = 0;
			gbc.insets.left = 0;
			add(label, gbc);
			distanceActualLabel = new JLabel();
			distanceActualLabel.setText("999.9999999999");
			labelPreferredSize = startLabel.getPreferredSize();
			distanceActualLabel.setPreferredSize(labelPreferredSize);
			distanceActualLabel.setText("");
			gbc.gridx = 1;
			gbc.weightx = 1;
			gbc.insets.left = 5;			
			add(distanceActualLabel, gbc);
			
			label = new JLabel("Distance (miles, straight line):");
			gbc.gridx = 0;
			gbc.gridy++;
			gbc.weightx = 0;
			gbc.insets.left = 0;
			add(label, gbc);
			distanceStraightLineLabel = new JLabel();
			distanceStraightLineLabel.setPreferredSize(labelPreferredSize);
			gbc.gridx = 1;
			gbc.weightx = 1;
			gbc.insets.left = 5;			
			add(distanceStraightLineLabel, gbc);
			
			JPanel buttonPanel = new JPanel();
			playButton = new JButton("Play >");
			playButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					play();
				}				
			});
			buttonPanel.add(playButton);
			pauseButton = new JButton("Pause ||");
			pauseButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					pause();
				}				
			});
			buttonPanel.add(pauseButton);
			nextButton = new JButton("Next >>");
			/*nextButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					//nextPressed = true;					
				}				
			});*/
			nextButton.addMouseListener(new MouseAdapter() {				
				@Override
				public void mousePressed(MouseEvent arg0) {
					if(waitTimer != null) {
						waitTimer.cancel(true);
					}
					final long endTime = System.currentTimeMillis() + 100;
					waitTimer = new SwingWorker<Object, Object>() {
						@Override
						protected Object doInBackground() throws Exception {							
							while(!isCancelled() && System.currentTimeMillis() < endTime) {
								try {
									Thread.sleep(25);
								} catch(Exception ex) {}
							}
							if(!isCancelled()) {
								nextButtonDown = true;
							}
							return null;							
						}						
					};
					waitTimer.execute();
				}
				@Override
				public void mouseReleased(MouseEvent e) {
					nextButtonDown = false;
					nextPressed = true;
					if(waitTimer != null) {
						waitTimer.cancel(true);
					}
				}
			});
			buttonPanel.add(nextButton);
			gbc.gridx = 0;
			gbc.gridy++;
			gbc.insets.top = 10;			
			gbc.gridwidth = 2;
			add(buttonPanel, gbc);
			
			setPaused(paused);
		}
		
		public void setCurrentLocation(int locationNum, GridLocation2D start, GridLocation2D end) {
			progressLabel.setText(Integer.toString(locationNum) + "/" + Integer.toString(numLocations));
			startLabel.setText("(" + start.getX() + ", " + start.getY() + ")");
			endLabel.setText("(" + end.getX() + ", " + end.getY() + ")");
		}
		
		public void setNumLocations(int numLocations) {			
			this.numLocations = numLocations;
			progressLabel.setText("0/" + Integer.toString(numLocations));
		}
		
		public void setCurrentDistance(Double distance) {
			distanceActualLabel.setText(distance == null ? "Null" : distance.toString());			
		}
		
		public void setCurrentDistanceStraightLine(Double distance) {
			distanceStraightLineLabel.setText(distance == null ? "Null" : distance.toString());
		}
		
		@Override
		public void setEnabled(boolean enabled) {
			super.setEnabled(enabled);
			playButton.setEnabled(enabled && paused);
			pauseButton.setEnabled(enabled && !paused);
			nextButton.setEnabled(enabled && paused);
		}

		public void setPaused(boolean paused) {
			boolean enabled = isEnabled();
			playButton.setEnabled(enabled && paused);
			pauseButton.setEnabled(enabled && !paused);
			nextButton.setEnabled(enabled && paused);
		}
	}
}