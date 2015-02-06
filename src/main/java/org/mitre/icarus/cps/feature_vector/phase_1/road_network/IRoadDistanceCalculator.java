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

import java.util.List;
import java.util.Map;

import org.mitre.icarus.cps.feature_vector.phase_1.GridLocation2D;
import org.mitre.icarus.cps.feature_vector.phase_1.Road;

/**
 * Interface for implementations that compute distances (shortest paths) along road networks.
 * 
 * @author CBONACETO
 *
 */
public interface IRoadDistanceCalculator {
	
	/**
	 * 
	 */
	public void clearStartLocationCache();
	
	/**
	 * Get the roads that make up the road network.
	 * 
	 * @return the roads
	 */
	public List<Road> getRoads();
	
	/**
	 * Compute the shortest distance along the road network from the given start location to end location.
	 * 
	 * @param startLocation the start location 
	 * @param endLocation the end location
	 * @return the distance from the start location to the end location
	 */
	public Double computeShortestDistanceFromStartLocationToEndLocation(GridLocation2D startLocation, GridLocation2D endLocation);
	
	/**
	 * Compute the shortest distance along the road network from the given start location to each end location.
	 * 
	 * @param startLocation the start location
	 * @param endLocations the end locations
	 * @return a map where the key is an end location and the value is the distance from the start location to that end location
	 */
	public Map<GridLocation2D, Double> computeShortestDistanceFromStartLocationToEndLocations(GridLocation2D startLocation,
			List<GridLocation2D> endLocations);

	
	/**
	 * Get the location on one of the roads in the road network that is closest to the given location.
	 * 
	 * @param location the location
	 * @return a location on a road that is closest to the location
	 */
	public GridLocation2D getClosestLocationOnRoad(GridLocation2D location);
}
