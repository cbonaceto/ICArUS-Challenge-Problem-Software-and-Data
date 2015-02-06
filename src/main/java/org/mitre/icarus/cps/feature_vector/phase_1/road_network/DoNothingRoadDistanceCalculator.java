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
 * Road distance calculator implementation that does nothing.
 * 
 * @author CBONACETO
 *
 */
public class DoNothingRoadDistanceCalculator implements IRoadDistanceCalculator {

	@Override
	public void clearStartLocationCache() {}		

	@Override
	public List<Road> getRoads() {
		return null;
	}

	@Override
	public Double computeShortestDistanceFromStartLocationToEndLocation(
			GridLocation2D startLocation, GridLocation2D endLocation) {	
		return null;
	}

	@Override
	public Map<GridLocation2D, Double> computeShortestDistanceFromStartLocationToEndLocations(
			GridLocation2D startLocation, List<GridLocation2D> endLocations) {	
		return null;
	}

	@Override
	public GridLocation2D getClosestLocationOnRoad(GridLocation2D location) {	
		return null;
	}
}