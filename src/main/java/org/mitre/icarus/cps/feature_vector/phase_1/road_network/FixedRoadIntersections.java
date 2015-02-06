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

import org.mitre.icarus.cps.feature_vector.phase_1.GridLocation2D;
import org.mitre.icarus.cps.feature_vector.phase_1.GridSize;

/**
 * Fixed intersection points for the road.
 * 
 * @author Lily Wong
 *
 */
public class FixedRoadIntersections {
	// stopping points while parsing a list of sorted road points
	GridSize gridSize;
	ArrayList<ArrayList<GridLocation2D>> segmentEndPoints = new ArrayList<ArrayList<GridLocation2D>>();
	
	public ArrayList<ArrayList<GridLocation2D>> getSegmentEndPoints() {
		return segmentEndPoints;
	}

	public FixedRoadIntersections ( GridSize gridSize ) {
		this.gridSize = gridSize;
		
		ArrayList<GridLocation2D> road1 = new ArrayList<GridLocation2D>();
		road1.add( new GridLocation2D(null,null,67,40,gridSize) );
		road1.add( new GridLocation2D(null,null,78,61,gridSize) );
		road1.add( new GridLocation2D(null,null,79,83,gridSize) );
		road1.add( new GridLocation2D(null,null,77,99,gridSize) );

		ArrayList<GridLocation2D> road2 = new ArrayList<GridLocation2D>();
		road2.add( new GridLocation2D(null,null,67,40,gridSize) );
		road2.add( new GridLocation2D(null,null,56,44,gridSize) );
		road2.add( new GridLocation2D(null,null,36,79,gridSize) );
		road2.add( new GridLocation2D(null,null,9,99,gridSize) );
		
		ArrayList<GridLocation2D> road3 = new ArrayList<GridLocation2D>();
		road3.add( new GridLocation2D(null,null,56,44,gridSize) );
		road3.add( new GridLocation2D(null,null,78,61,gridSize) );
		road3.add( new GridLocation2D(null,null,99,61,gridSize) );
		
		ArrayList<GridLocation2D> road4 = new ArrayList<GridLocation2D>();
		road4.add( new GridLocation2D(null,null,36,79,gridSize) );
		road4.add( new GridLocation2D(null,null,79,83,gridSize) );
		road4.add( new GridLocation2D(null,null,99,86,gridSize) );
		
		segmentEndPoints.add(road1);
		segmentEndPoints.add(road2);
		segmentEndPoints.add(road3);
		segmentEndPoints.add(road4);
	}
}