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
import org.mitre.icarus.cps.feature_vector.phase_1.parser.RoadCsvParser;

/**
 * Represents the roads as a series of line segments from start points to intersection points to end points.
 */
public class BaseRoadGraph {

	List<Road> roads;
	
	GridSize gridSize;
	
	LinkedList<RoadMetaLineSegment> edges;
	
	LinkedList<IntersectionPoint> nodes;
	
	HashMap<String,LinkedList<IntersectionPoint>> metaSegmentPoints;	
	
	public BaseRoadGraph(RoadCsvParser roads) {
		this(roads.getRoads(), roads.getGridSize());
	}

	
	public BaseRoadGraph(List<Road> roads, GridSize gridSize) {
		//this.roadCsv = roadCsv;
		//this.roads = roads;
		this.gridSize = gridSize;
		this.edges = new LinkedList<RoadMetaLineSegment>();
		this.nodes = new LinkedList<IntersectionPoint>();
		buildGraph(roads);
	}	

	/**
	 * Builds the graph.
	 *
	 * @param roadCsv the sample csv
	 */
	private void buildGraph(List<Road> roads) {
		// populate start, intersect, end points hashed by road ID
		SweepLineAlgorithm alg = new SweepLineAlgorithm(roads, gridSize);
		
		this.metaSegmentPoints = alg.getIntersectionMap();
		
		// propagate any vertex insertions (non-roadpoint intersections)
		this.roads = alg.getNewRoads();
		
		createMetaLineSegments(); // edges
		populateNodes();
	}
	
	/**
	 * HashMap to List, distinct nodes only.
	 */
	private void populateNodes() {
		for ( LinkedList<IntersectionPoint> nodeList : metaSegmentPoints.values() ) {
			for ( IntersectionPoint node : nodeList ) {
				if ( !this.nodes.contains(node) ) 
					this.nodes.add(node);
			}
		}
	}
	
	private void createMetaLineSegments() {
		// create meta segments
		for ( LinkedList<IntersectionPoint> pList : metaSegmentPoints.values()) {
			Road road = null;
			String roadId = pList.peek().getRoadA(); // first point of road
			for ( Road r : roads ) {
				if ( r.getId().equals(roadId)) {
					road = r;
					break;
				}
			}
			
			IntersectionPoint prev = null;
			for ( IntersectionPoint p: pList) {				
				if ( prev == null ) {
					prev = p;
					continue;
				}
				GridLocation2D start = null;
				GridLocation2D end = null;
				for ( GridLocation2D g : road.getVertices() ) {
					if ( prev.equals(g)) {
						start = g;
					}
					if ( p.equals(g)) {
						end = g;
					}
				}
				//System.out.println("start: " + start + ", end: " + end);
				RoadMetaLineSegment segment = new RoadMetaLineSegment(road, start, end, gridSize);
				// unique segments only
				if ( !edges.contains(segment) )
					edges.add( segment );
				// move on to the next point
				prev = p;
			}
		}
	}
	
	public LinkedList<RoadMetaLineSegment> getEdges() {
		return edges;
	}


	public LinkedList<IntersectionPoint> getNodes() {
		return nodes;
	}


	public HashMap<String, LinkedList<IntersectionPoint>> getMetaSegmentPoints() {
		return metaSegmentPoints;
	}
}