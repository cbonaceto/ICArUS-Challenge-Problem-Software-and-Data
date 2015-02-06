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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

import org.mitre.icarus.cps.feature_vector.phase_1.GridLocation2D;
import org.mitre.icarus.cps.feature_vector.phase_1.GridSize;
import org.mitre.icarus.cps.feature_vector.phase_1.Road;

/**
 * The Class DistanceCalculation.
 * 
 */
public class DistanceCalculation {
	
	/** The distances. */
	private HashMap<GridLocation2D, Double> distances;
	
	/** The shortest distance paths from start to end. */
	private HashMap<GridLocation2D, LinkedList<GridLocation2D>> paths;
	
	/**
	 * Instantiates a new distance calculation.
	 *
	 * @param roads - the roads
	 * @param locations - the locations to calculate road network distances for
	 */
	public DistanceCalculation(ArrayList<Road> roads, GridSize gridSize, ArrayList<GridLocation2D> locations) {
		this.distances = new HashMap<GridLocation2D, Double>();
		this.paths = new HashMap<GridLocation2D, LinkedList<GridLocation2D>>();
		
		// calculate shortest path distances
		boolean isStart = true;
		StartGraph startGraph = null;
		for ( GridLocation2D g : locations ) {
			if ( isStart ) {
				startGraph = new StartGraph(roads, gridSize, g);
				isStart = false;
			} 
			else {
				PathGraph pathGraph = new PathGraph(startGraph, g, gridSize);
//				System.out.println(g.toString() + " " + pathGraph.distance);
				this.distances.put(g, pathGraph.distance);
				GridLocation2D end = g;
				// traceback
				boolean finished = false;
				LinkedList<GridLocation2D> shortestPath = new LinkedList<GridLocation2D>();
				shortestPath.add(g);	// end point
				
				while ( !finished ) {
					GridLocation2D n = pathGraph.paths.get(g);
					if ( n.equals(startGraph.start)) {
//						System.out.println("\t"+n.toString());
						finished = true;
					} else {
//						System.out.println("\t"+n.toString());
						g = n;
					}
					shortestPath.add(n);
				}
				
				// reverse list 
				Collections.reverse(shortestPath);
				paths.put(end, shortestPath);
			}
		}
	}

	/**
	 * Gets the distances.
	 *
	 * @return the distances
	 */
	public HashMap<GridLocation2D, Double> getDistances() {
		return distances;
	}

	/**
	 * Gets the paths.
	 *
	 * @return the paths
	 */
	public HashMap<GridLocation2D, LinkedList<GridLocation2D>> getPaths() {
		return paths;
	}
}