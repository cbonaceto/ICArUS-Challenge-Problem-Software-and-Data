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
import java.util.LinkedList;
import java.util.List;

import org.mitre.icarus.cps.feature_vector.phase_1.GridLocation2D;
import org.mitre.icarus.cps.feature_vector.phase_1.GridSize;
import org.mitre.icarus.cps.feature_vector.phase_1.Road;

public class PathGraph {

	LinkedList<GridLocation2D> nodes;
	LinkedList<RoadMetaLineSegment> edges;
	
	GridLocation2D end;	
	GridLocation2D endA;
	GridLocation2D endB;
	RoadMetaLineSegment segmentEndA;
	RoadMetaLineSegment segmentEndB;
	RoadMetaLineSegment directSegment;
	
	Double distance;
	HashMap<GridLocation2D, GridLocation2D> paths;
	
	public PathGraph(StartGraph startGraph, GridLocation2D end, GridSize gridSize) {
		// populate around end point
//		StartGraph endGraph = new StartGraph(startGraph.roadCsv, end);
		this.end = end;
		this.nodes = new LinkedList<GridLocation2D>();
		this.edges = new LinkedList<RoadMetaLineSegment>();		
		
		// if direct start to end is a shorter distance, use it instead.
		
		findPointsAroundEnd(startGraph.roads, startGraph, gridSize);
		
		// run Dijkstra's
		ShortestPathUtils spu = new ShortestPathUtils(nodes,edges,startGraph.start, end);
		this.distance = spu.getShortestDistances().get(end);
		this.paths = spu.getPreviousNodes();
	}
	
	private void findPointsAroundEnd(List<Road> roads, StartGraph startGraph, GridSize gridSize) {
		//HashMap<String,LinkedList<IntersectionPoint>> partitionedPoints = startGraph.getMetaSegmentPoints();		
		// copied from StartGraph findPoints()
		Road endRoad = null;
		String endRoadId = null;
		for( Road road : roads ) {			
			// find road that end point is on
			for ( GridLocation2D g : road.getVertices() ) {
				if (g.equals(end)) {
					// we now have the road
					endRoad = road;
					endRoadId = road.getId();
					
					// determine if the target is the first or last pt on road
					// don't need to partition anything, target is already an intersection pt
					if ( end.equals(road.getVertices().get(0)) ||
							end.equals(road.getVertices().get(road.getVertices().size()-1))) {
						
						// calculate direct path if the start and end are on the same road
						if ( endRoadId.equals(startGraph.startRoadId) ) {
							// don't need to add any segments, just add the direct route
							directSegment = new RoadMetaLineSegment(endRoad, startGraph.start, end, gridSize);
							startGraph.edges.add(directSegment);
						}
						// add to PathGraph
						this.nodes.addAll(startGraph.nodes);
						this.edges.addAll(startGraph.edges);
						return;
					}

					break;
				}		
			}
			if(endRoad != null) {
				break;
			}
		}
			
		if(endRoad != null) {			
			// grab this road's meta points
			LinkedList<IntersectionPoint> metaPoints = startGraph.metaSegmentPoints.get(endRoadId);
			
			GridLocation2D targetPrev = null;	
						
			int index = 0;
			for ( GridLocation2D g : endRoad.getVertices() ) {
				if (g.equals(metaPoints.get(index))) {
					// keep track of possible targetA points
					targetPrev = g;
					index++;
				} //else if (g.equals(end)) { //TODO: Craig made else-if(g.equals(start)) check and if check instead
				if (g.equals(end) && index < metaPoints.size()) {
					// calculate direct path if the start and end are on the same road
					if ( g.getLocationId().equals(startGraph.startRoadId) ) {
						directSegment = new RoadMetaLineSegment(endRoad, startGraph.start, end, gridSize);
					}
					
					// create partition segments 
					endA = targetPrev;
					segmentEndA = new RoadMetaLineSegment(endRoad, end, endA, gridSize);
					
					endB = metaPoints.get(index).toGridLocation2D();
					segmentEndB = new RoadMetaLineSegment(endRoad, end, endB, gridSize);
					
					// add to PathGraph
					this.nodes.addAll(startGraph.nodes);
					this.edges.addAll(startGraph.edges);
					
					// remove larger line segment
					RoadMetaLineSegment comparison = new RoadMetaLineSegment(endRoad, endA, endB, gridSize);
					edges.remove(comparison);
					
					// update edges and nodes
					nodes.add(new IntersectionPoint(end));
					edges.add(segmentEndA);
					edges.add(segmentEndB);	
					
					// add segment between start and end, if any
					if ( directSegment != null )
						edges.add(directSegment);
					
					if ( endA == null ) {
						throw new IllegalStateException("Start A should have been found, pts not in order");
					}
					return;
				}
			}
		}
	}
}
