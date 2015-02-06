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
package org.mitre.icarus.cps.feature_vector.phase_1.parser;

import java.util.ArrayList;
import java.util.LinkedList;

import org.mitre.icarus.cps.feature_vector.phase_1.GridLocation2D;
import org.mitre.icarus.cps.feature_vector.phase_1.Road;
import org.mitre.icarus.cps.feature_vector.phase_1.road_network.RoadLineSegment;

/**
 * Base class for road CSV and KML road file parsers.
 * 
 * @author CBONACETO
 *
 */
public class AbstractRoadParser {
	
	/** The roads */
	private ArrayList<Road> roads = new ArrayList<Road>();
	
	/**
	 * Sorts the points in the road to ensure that they will display as continuous
	 * line segments.  Also creates line segments between adjacent points.
	 */
	protected void sortRoadPoints() {
		for ( Road road : roads ) {
			ArrayList<GridLocation2D> roadPoints = road.getVertices();
			
			// check whether the first or last point is closest to the x or y axis.
			double min = Double.MAX_VALUE;
			GridLocation2D start = null;
			
			// find the point closest to an axis to be the start
			for ( GridLocation2D point : roadPoints ) {
				if ( point.getX() < min ) {
					min = point.getX();
					start = point;
				}
				if ( point.getY() < min ) {
					min = point.getY();
					start = point;
				}
			}
			
			// put the start point at the beginning
			roadPoints.remove(roadPoints.indexOf(start));
			LinkedList<GridLocation2D> l = new LinkedList<GridLocation2D>(roadPoints);
			l.addFirst(start);
			roadPoints = new ArrayList<GridLocation2D>(l);
			
			// run shortest path algorithm
			SortingUtils.sortShortestPath( roadPoints );
			
			// form line segments from the sorted points
			GridLocation2D prevPoint = null;
			
			for ( GridLocation2D pt : roadPoints ) {
				// **this is an end point, create line segment**
				if ( prevPoint != null  ) {
					RoadLineSegment segment = new RoadLineSegment( pt.getLocationId(), prevPoint, pt, null);
					road.getLineSegments().add(segment);
				}
				
				// set this point to be the first point of a segment
				prevPoint = pt;	
			}
			
			road.setVertices(roadPoints);
		}
	}
	
	/**
	 * Populates points for display.  If there is a set of 3 points that forms 
	 * a right angle, the middle point is removed.
	 */
	protected void pruneRoadPoints() {
		for ( Road road : roads ) {
			ArrayList<RoadLineSegment> segments = road.getLineSegments();
			boolean prevPtPruned = false;	// ensure we don't remove consecutive points
			
			for( int i = 0; i < segments.size()-1; i++ ) {
				// compare each segment's slope to the next one's slope
				RoadLineSegment curSeg  = segments.get(i);
				RoadLineSegment nextSeg = segments.get(i+1);
				
				// add first point on road
				if ( i == 0 ) road.getSmoothPoints().add(curSeg.getPointA());
				
				// end point of current segment is to be removed; forms right angle
				if ( !prevPtPruned && 
					((curSeg.isVertical() && (!nextSeg.isVertical() && nextSeg.getSlope() == 0)) ||
					(!curSeg.isVertical() && curSeg.getSlope() == 0) && nextSeg.isVertical())) {
//					System.out.println("pruned " + curSeg.getPointB().x + "," + curSeg.getPointB().y);
					prevPtPruned = true;
				}
				else {
					// add end point of current segment to list of points to display
//					System.out.println("added " + curSeg.getPointB().x + "," + curSeg.getPointB().y);
					road.getSmoothPoints().add(curSeg.getPointB());
					prevPtPruned = false;
				}
				
				// add last point on road
				if ( i == segments.size()-2 ) road.getSmoothPoints().add(nextSeg.getPointB());
			}
		}
	}
}
