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
import java.util.List;

import org.mitre.icarus.cps.feature_vector.phase_1.GridLocation2D;
import org.mitre.icarus.cps.feature_vector.phase_1.GridSize;
import org.mitre.icarus.cps.feature_vector.phase_1.Road;

/**
 * Represents the segments between start/intersection/end points.
 */
public class RoadMetaLineSegment extends RoadLineSegment {

	/**
	 * Instantiates a new road meta line segment.
	 *
	 * @param road the road
	 * @param pointA the point a
	 * @param pointB the point b
	 */
	public RoadMetaLineSegment(Road road, GridLocation2D pointA,
			GridLocation2D pointB, GridSize gridSize) {
		super(road.getId(), pointA, pointB, gridSize);
		
//		System.out.println( pointA.toString() + "\n" + pointB.toString() + "\n");
//		if ( startA == null ) throw new IllegalArgumentException("StartA is null");
		calculateLength( road );
	}
	
	/**
	 * Calculate length.
	 *
	 * @param road the road
	 */
	private void calculateLength(Road road) {
		ArrayList<GridLocation2D> vertices = road.getVertices();
//		int aIndex = -1;
//		int bIndex = -2;
//		int i = 0;
//		for ( GridLocation2D vertex : vertices ) {
//			if ( vertex.equals(getPointA()) )
//				aIndex = i;
//			else if ( vertex.equals(getPointB()) )
//				bIndex = i;
//			i++;
//		}
//		System.out.println("aIndex " + aIndex + " " + road.getId());
//		System.out.println("bIndex " + bIndex);
		int aIndex = vertices.indexOf(pointA);
		int bIndex = vertices.indexOf(pointB);
//		int bIndex = getIndexOf(getPointB(), vertices);
		// index i of vertex = line segment of index i where vertex is starting point
		// want [a,b]
		
		/*if(aIndex == -1) {
			System.err.println("Warning, point A not on road");
		}
		if(bIndex == -1) {
			System.err.println("Warning, point B not on road");
		}*/
		
		if(aIndex != -1 && bIndex != -1) { //If check added by Craig, see if correct
			// swap if neccessary
			if ( aIndex > bIndex) {
				int temp = aIndex;
				aIndex = bIndex;
				bIndex = temp;
			}
			ArrayList<RoadLineSegment> lineSegments = road.getLineSegments();
			List<RoadLineSegment> metaSegments = lineSegments.subList(aIndex, bIndex);
			double sum = 0;
			for ( RoadLineSegment rs : metaSegments ) {
				sum += rs.getLengthInUnits();
			}
			setLengthInUnits(sum);
		}
	}
	
	protected int getIndexOf(GridLocation2D g, ArrayList<GridLocation2D> vertices) {
		for ( GridLocation2D point : vertices) {
			if ( point.equals(g))
				return vertices.indexOf(point);
		}
		return -1;
	}
}