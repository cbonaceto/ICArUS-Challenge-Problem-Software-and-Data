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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mitre.icarus.cps.feature_vector.phase_1.GridLocation2D;
import org.mitre.icarus.cps.feature_vector.phase_1.GridSize;
import org.mitre.icarus.cps.feature_vector.phase_1.Road;

/**
 * Default road distance calculator implementation.
 * 
 * @author CBONACETO
 *
 */
public class RoadDistanceCalculator implements IRoadDistanceCalculator {
	
	protected List<Road> roads;
	
	protected GridSize gridSize;	
	
	protected HashMap<GridLocation2D, StartGraph> startLocationGraphs;
	
	public RoadDistanceCalculator(List<Road> roads, GridSize gridSize) {
		this.roads = roads;
		this.gridSize = gridSize;
		this.startLocationGraphs = new HashMap<GridLocation2D, StartGraph>(8);
	}	
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.feature_vector.phase_1.road_network.IRoadDistanceCalculator#clearStartLocationCache()
	 */
	@Override
	public void clearStartLocationCache() {
		startLocationGraphs.clear();
	}	
	
	@Override
	public List<Road> getRoads() {
		return roads;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.feature_vector.phase_1.road_network.IRoadDistanceCalculator#computeShortestDistanceFromStartLocationToEndLocation(org.mitre.icarus.cps.feature_vector.phase_1.GridLocation2D, org.mitre.icarus.cps.feature_vector.phase_1.GridLocation2D)
	 */
	@Override
	public Double computeShortestDistanceFromStartLocationToEndLocation(GridLocation2D startLocation, GridLocation2D endLocation) {
		//Get the existing StartGraph or create a new one for the given startLocation
		StartGraph startGraph = getStartGraphForLocation(startLocation);
		
		if(startGraph !=  null && endLocation != null) {
			/*System.out.println("Roads After:");
			for(Road road : startGraph.roads) {							
				String roadId = road.getId();
				for(GridLocation2D vertex : road.getVertices()) {
					System.out.println(roadId + "," + "Road," + vertex.getX() + "," + vertex.getY());
				}
			}*/
			return new PathGraph(startGraph, endLocation, gridSize).distance;
		}		
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.feature_vector.phase_1.road_network.IRoadDistanceCalculator#computeShortestDistanceFromStartLocationToEndLocations(org.mitre.icarus.cps.feature_vector.phase_1.GridLocation2D, java.util.ArrayList)
	 */
	@Override
	public Map<GridLocation2D, Double> computeShortestDistanceFromStartLocationToEndLocations(GridLocation2D startLocation,
			List<GridLocation2D> endLocations) {
		StartGraph startGraph = getStartGraphForLocation(startLocation);
		if(startGraph != null && endLocations != null && !endLocations.isEmpty()) {
			Map<GridLocation2D, Double> distances = new HashMap<GridLocation2D, Double>(endLocations.size());
			for(GridLocation2D endLocation : endLocations) {
				distances.put(endLocation, new PathGraph(startGraph, endLocation, gridSize).distance);
			}
		}
		return null;
	}
	
	/**
	 * Get the StartGrapch for the given start location, or create a new StartGraph if it isn't found.
	 * 
	 * @param startLocation the start location
	 * @return the StartGraph for the start location
	 */
	protected StartGraph getStartGraphForLocation(GridLocation2D startLocation) {
		//Get the existing StartGraph or create a new one for the given startLocation
		StartGraph startGraph = startLocationGraphs.get(startLocation);
		if(startGraph == null) {
			startGraph = new StartGraph(roads, gridSize, startLocation);
			startLocationGraphs.put(startLocation, startGraph);
		} else {
			GridLocation2D previousStart = startGraph.getStart();
			if(previousStart.getX() != startLocation.getX() || previousStart.getY() != startLocation.getY()) {
				startGraph.setStart(startLocation);
			}
		}
		return startGraph;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.feature_vector.phase_1.road_network.IRoadDistanceCalculator#getClosestLocationOnRoad(org.mitre.icarus.cps.feature_vector.phase_1.GridLocation2D)
	 */
	@Override
	public GridLocation2D getClosestLocationOnRoad(GridLocation2D location) {
		double shortestDist = Double.MAX_VALUE;
		GridLocation2D closest = null;
		for(Road road : roads) {
			GridLocation2D closestRoadPoint = road.getClosestPoint(location);
			if(closestRoadPoint != null) {
				double dist = Math.sqrt(Math.pow(location.x - closestRoadPoint.x, 2) + 
						Math.pow(location.y - closestRoadPoint.y, 2));
				if(dist < shortestDist) {
					shortestDist = dist;
					closest = closestRoadPoint;
				}
			}							
		}
		return closest;
	}	
}