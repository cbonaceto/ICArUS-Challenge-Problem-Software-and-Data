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

import java.util.LinkedList;
import java.util.List;

import org.mitre.icarus.cps.feature_vector.phase_1.GridLocation2D;
import org.mitre.icarus.cps.feature_vector.phase_1.GridSize;
import org.mitre.icarus.cps.feature_vector.phase_1.Road;

public class StartGraph extends BaseRoadGraph {

	GridLocation2D start;
	GridLocation2D startA;
	GridLocation2D startB;
	RoadMetaLineSegment segmentStartA;
	RoadMetaLineSegment segmentStartB;
	
	String startRoadId = null;
	
	public StartGraph(List<Road> roads, GridSize gridSize, GridLocation2D start) {
		super(roads, gridSize);
		this.start = start;
		findPointsAroundStart();
	}

	// find bookending points and create meta line segments, update nodes and edges
	private void findPointsAroundStart() {
		// find previous and next intersection points
		// look through all roads because start ID could be a letter.		
		
		Road startRoad = null;
		//for( Road road : roads ) {			
		for( Road road : roads ) {
			// find road that start point is on
			for ( GridLocation2D g : road.getVertices() ) {
				if (g.equals(start)) {
					// we now have the road
					startRoad = road;
					startRoadId = road.getId(); 
					
					// determine if the target is the first or last pt on road
					// don't need to partition anything, target is already an intersection pt
					if ( start.equals(road.getVertices().get(0)) ||
							start.equals(road.getVertices().get(road.getVertices().size()-1))) {
						return;
					}
					
					break;					
				}
			}
			if(startRoad != null) {
				break;
			}
		}
		
		if(startRoad != null) {			 
			// grab this road's meta points
			LinkedList<IntersectionPoint> metaPoints = this.metaSegmentPoints.get(startRoadId);		
			
			GridLocation2D targetPrev = null;	
						
			int index = 0;
			for ( GridLocation2D g : startRoad.getVertices() ) {				
				if(g.equals(metaPoints.get(index))) {
					// keep track of possible targetA points
					targetPrev = g;
					index++;
				} //else if ( g.equals(start) ) { //TODO: Craig made else-if(g.equals(start)) check and if check instead
				if(g.equals(start) && index < metaPoints.size()) {
					// create // create partition segments 
					startA = targetPrev;
					segmentStartA = new RoadMetaLineSegment(startRoad, start, startA, gridSize);
					
					startB = metaPoints.get(index).toGridLocation2D();
					segmentStartB = new RoadMetaLineSegment(startRoad, start, startB, gridSize);
					
					// remove larger line segment
					RoadMetaLineSegment comparison = new RoadMetaLineSegment(startRoad, startA, startB, gridSize);
					edges.remove(comparison);
					
					// update edges and nodes
					nodes.add(new IntersectionPoint(start));
					edges.add(segmentStartA);
					edges.add(segmentStartB);
					
					if ( startA == null )
						throw new IllegalStateException("Start A should have been found, pts not in order");
					return;
				}
			}			
		}		
	}	

	public GridLocation2D getStart() {
		return start;
	}
	
	public void setStart(GridLocation2D start) {		
		this.start = start;
		findPointsAroundStart();
	}

	public GridLocation2D getStartA() {
		return startA;
	}

	public GridLocation2D getStartB() {
		return startB;
	}
}