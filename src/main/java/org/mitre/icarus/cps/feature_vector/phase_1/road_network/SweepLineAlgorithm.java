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
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentSkipListSet;

import org.mitre.icarus.cps.feature_vector.phase_1.GridLocation2D;
import org.mitre.icarus.cps.feature_vector.phase_1.GridSize;
import org.mitre.icarus.cps.feature_vector.phase_1.Road;

/**
 * SweepLineAlgorithm for line segment intersection detection.
 * http://theory.cs.uiuc.edu/~jeffe/teaching/373/notes/x06-sweepline.pdf
 * 
 * @author Lily Wong
 */
public class SweepLineAlgorithm {
	
	/** The skip list of line segments ordered by y-coordinate. */
	private ConcurrentSkipListSet<AlgLineSegment> skipList;
	
	/** List of segment endpoints sorted by x-coordinate. */
	private PriorityQueue<AlgPoint> points;
	
	/** The map to associate line segment labels with line segments. */
	private HashMap<String,AlgLineSegment> labels; 
	
	/** The sweep line x. For algorithm and debugging purposes. */
	@SuppressWarnings("unused")
	private int sweepLineX;
	
	/** The intersection list. */
	public ArrayList<IntersectionPoint> intersectionList;
	
	/** Maps Road IDs to Intersection Points. */
	public HashMap<String,LinkedList<IntersectionPoint>> intersectionMap;
	
	/** The grid size. */
	private GridSize gridSize;
	
	/** An intersection point that is not a point on any road (i.e. some .5 value) **/
	private boolean hasNonRoadIntersections;
	private HashSet<IntersectionPoint> nonRoadIntersectionSet;
	private List<Road> newRoads;

	/**
	 * Populates the structures and runs the algorithm.
	 *
	 * @param roads the roads
	 */
	public SweepLineAlgorithm(List<Road> roads, GridSize gridSize) {			
		// initialize structures
		skipList 	     = new ConcurrentSkipListSet<AlgLineSegment>();
		points 		     = new PriorityQueue<AlgPoint>();
		labels		     = new HashMap<String,AlgLineSegment>();
		sweepLineX 	     = 0;
		intersectionList = new ArrayList<IntersectionPoint>();
		nonRoadIntersectionSet = new HashSet<IntersectionPoint>();
		newRoads = new ArrayList<Road>();
		intersectionMap  = new HashMap<String,LinkedList<IntersectionPoint>>();
		this.gridSize    = gridSize;		
		
		ArrayList<IntersectionPoint> roadEndInterPtList = new ArrayList<IntersectionPoint>();;
		
		// populate structures with all the road points
		for ( Road road : roads ) {
			ArrayList<RoadLineSegment> roadLineSegments = road.getLineSegments();
			String roadId = road.getId();
			// save off start point, insert onto list
			GridLocation2D roadStartPt = road.getVertices().get(0);
			IntersectionPoint roadStartInterPt = new IntersectionPoint( roadId, roadId, roadStartPt.getX(), roadStartPt.getY(), gridSize );
			putIntersection( roadStartInterPt );
			GridLocation2D roadEndPt = road.getVertices().get( road.getVertices().size()-1 );
			IntersectionPoint roadEndInterPt = new IntersectionPoint( roadId, roadId, roadEndPt.getX(), roadEndPt.getY(), gridSize );
			roadEndInterPtList.add(roadEndInterPt);
			
			// create and insert line segment and point objects for algorithm use
			int index = 0;
			for( RoadLineSegment roadLineSegment : roadLineSegments ) {
				GridLocation2D pointA = roadLineSegment.getPointA();
				GridLocation2D pointB = roadLineSegment.getPointB();
				// assign the segment a label based on its roadId and the index of the segment within the road
				AlgLineSegment algLineSegment = new AlgLineSegment(road.getId(), index, pointA, pointB, gridSize);
				labels.put(algLineSegment.getLabel(), algLineSegment);
				// mark the segment endpoints as the start or end of the segment
				AlgPoint start = new AlgPoint( pointA, algLineSegment.getLabel(), true );
				AlgPoint end   = new AlgPoint( pointB, algLineSegment.getLabel(), false );
				// add segment endpoints 
				points.add(start);
				points.add(end);
				index++;
			}
		}
		
		// sweep line algorithm
		runAlgorithm();
		
		// add last point in each road
		for ( IntersectionPoint roadEndInterPt : roadEndInterPtList )
			putIntersection( roadEndInterPt );
				
		// sort the intersection points by their road vertices order
		sortIntersectionMapByRoadVerticesOrder(roads);
	}
	
	/**
	 * Instantiates a new sweep line algorithm. Used by AlgLineSegment.
	 */
	public SweepLineAlgorithm() {}

	/**
	 * Gets the intersection list.
	 *
	 * @return the intersection list
	 */
	public ArrayList<IntersectionPoint> getIntersections() {
		return intersectionList;
	}
	
	public HashMap<String, LinkedList<IntersectionPoint>> getIntersectionMap() {
		return intersectionMap;
	}

	public boolean hasNonRoadIntersections() {
		return hasNonRoadIntersections;
	}

	public List<Road> getNewRoads() {
		return newRoads;
	}

	/**
	 * Put intersection.
	 *
	 * @param p the p
	 */
	private void putIntersection(IntersectionPoint p) {
		String roadAId = p.getRoadA();
		String roadBId = p.getRoadB();
		
		LinkedList<IntersectionPoint> la = intersectionMap.get( roadAId );
		LinkedList<IntersectionPoint> lb = intersectionMap.get( roadBId );
		
		// add point to lists of both roads of the intersection, or once if is road start/end point
		if ( la == null ) intersectionMap.put( roadAId, la = new LinkedList<IntersectionPoint>() );
		la.add( p );
		if ( !roadAId.equals(roadBId) ) {
			if ( lb == null ) intersectionMap.put( roadBId, lb = new LinkedList<IntersectionPoint>() );
			lb.add( p );
		}
	}

	/**
	 * Insert intersection point if it doesn't already exist.
	 *
	 * @param p the IntersectionPoint p
	 */
	private void insertIntersectionPoint( IntersectionPoint p ) {
		p = orderLabel(p);
		boolean exists = false;
		for ( IntersectionPoint ip : intersectionList ) {
			if ( ip.equals(p) ) 
				exists = true;
		}
		if ( !exists ) {
			intersectionList.add( p );
			putIntersection(p);
		}
		
		// if intersection is not a road point, 
		// add the point to the respective road's vertices
		// how to get back to road obj?  
	}
	
	/**
	 * Order the road labels of the intersection point so that
	 * intersections of the same two road can be picked out.
	 *
	 * @param p the IntersectionPoint p
	 * @return the intersection point
	 */
	private IntersectionPoint orderLabel(IntersectionPoint p) {
		String aStr = p.getRoadA();
		String bStr = p.getRoadB();
		Integer a = Integer.valueOf(aStr);
		Integer b = Integer.valueOf(bStr);
		if ( a > b ) {
			// swap the labels a and b
			p.swapEndPointLabels();
		}
		return p;
	}

	/**
	 * Run sweep line algorithm.
	 */
	private void runAlgorithm() {
		
		while ( !points.isEmpty() ) {
			
			// get the current point and its associated line segment
			AlgPoint point = points.remove();
			AlgLineSegment curSegment = labels.get(point.getLabel());
			
			boolean foundIntersection = false;
			
			// move the sweep line to this current point's x-coordinate
			// there may be more than one point with the same X value
			sweepLineX = point.getGridLocation2D().getX().intValue();
			
			if ( point.isStart() ) {
				// add segment to list of currently considered segments
				skipList.add(curSegment);
				
				// test intersection with either of two neighbors
				
				// neighbor below
				AlgLineSegment below = skipList.lower(curSegment);
				if ( below != null ) {
					if ( isRoadIntersection(curSegment, below) ) {
						IntersectionPoint p = calculateIntersectionPt(curSegment, below);
//						System.out.println(p.toString());
						insertIntersectionPoint( p ); 
						foundIntersection = true;
					}
				}
				
				// neighbor above
				AlgLineSegment above = skipList.higher(curSegment);
				if ( above != null ) {
					if ( isRoadIntersection(curSegment, above) ) {
						IntersectionPoint p = calculateIntersectionPt(curSegment, above);
						insertIntersectionPoint( p ); 
						foundIntersection = true;
					}
				}
			} 
			
			else {	// is end point
				
				// test intersection of the two neighbors
				AlgLineSegment below = skipList.lower(curSegment);
				AlgLineSegment above = skipList.higher(curSegment);
				if ( below != null && above != null ) {
					if ( isRoadIntersection(below, above) ) {
						IntersectionPoint p = calculateIntersectionPt(below, above);
						insertIntersectionPoint( p ); 
						foundIntersection = true;
					}
				}
				
				// delete label from list of currently considered segments
				skipList.remove(curSegment);
			}
			
			// WORKAROUND for not always finding intersection of 2 segments
			// that is not a point (PQ or skiplist ordering issue?)
			if ( !foundIntersection )
				checkAllIntersects(curSegment);
		}
	}
	
	
	/**
	 * Checks if two line segments intersect.
	 *
	 * @param segmentX the segment x
	 * @param segmentY the segment y
	 * @return true, if is intersection
	 */
	private  boolean isRoadIntersection(AlgLineSegment segmentX, AlgLineSegment segmentY) {
		// segments must be from different roads
		if ( segmentX.getRoadId().equals(segmentY.getRoadId()) )
			return false;
		// intersection may be at endpoints
		if ( anyPointsMatch(segmentX,segmentY) )
			return true;
		else
			return isIntersection(segmentX, segmentY);
	}
	
	/**
	 * Checks if the two line segments intersect.
	 *
	 * @param segmentX the segment x
	 * @param segmentY the segment y
	 * @return true, if the segments intersect
	 */
	protected  boolean isIntersection(AlgLineSegment segmentX, AlgLineSegment segmentY) {
		GridLocation2D xA = segmentX.getPointA();
		GridLocation2D xB = segmentX.getPointB();
		GridLocation2D yA = segmentY.getPointA();
		GridLocation2D yB = segmentY.getPointB();
		boolean abc = isCounterClockwise(xA, xB, yA);
		boolean abd = isCounterClockwise(xA, xB, yB);
		boolean acd = isCounterClockwise(xA, yA, yB);
		boolean bcd = isCounterClockwise(xB, yA, yB);
		return (acd != bcd) & (abc != abd);
	}
	
	/**
	 * Checks if the three points are in counterclockwise order.
	 * If yes, the segment s-t is above the segment with u.
	 *
	 * @param s the first point
	 * @param t the second point
	 * @param u the third point
	 * @return true, if the points are in counterclockwise order
	 */
	private boolean isCounterClockwise(GridLocation2D s, GridLocation2D t, GridLocation2D u) {
		double a = s.getX();
		double b = s.getY();
		double c = t.getX();
		double d = t.getY();
		double e = u.getX();
		double f = u.getY();
		
		// counterclockwise inequality
		if ( ((f-b)*(c-a)) > ((d-b)*(e-a)) )
			return true;
		return false;
	}
	
	/**
	 * Test if the line segments share any common points.
	 *
	 * @param segmentX the segment x
	 * @param segmentY the segment y
	 * @return true, if successful
	 */
	private boolean anyPointsMatch(AlgLineSegment segmentX, AlgLineSegment segmentY) {
		GridLocation2D xA = segmentX.getPointA();
		GridLocation2D xB = segmentX.getPointB();
		GridLocation2D yA = segmentY.getPointA();
		GridLocation2D yB = segmentY.getPointB();
		if ( xA.compareTo(yA) == 0 
		  || xA.compareTo(yB) == 0
		  || xB.compareTo(yA) == 0 
		  || xB.compareTo(yB) == 0 )
				return true;
		return false;
	}
	
	/**
	 * Calculate intersection point of two segments.
	 *
	 * @param segmentX the segment x
	 * @param segmentY the segment y
	 * @return the intersection point, or null if any of the segments is vertical.
	 */
	private IntersectionPoint calculateIntersectionPt(AlgLineSegment segmentX, AlgLineSegment segmentY) {
		if ( anyPointsMatch(segmentX,segmentY) ) {
			GridLocation2D xA = segmentX.getPointA();
			GridLocation2D xB = segmentX.getPointB();
			GridLocation2D yA = segmentY.getPointA();
			GridLocation2D yB = segmentY.getPointB();
			if ( (xA.compareTo(yA) == 0) || (xA.compareTo(yB)) == 0)
				return new IntersectionPoint(segmentX.getRoadId(), segmentY.getRoadId(), xA.x, xA.y, gridSize );
			else if ( (xB.compareTo(yA) == 0) || (xB.compareTo(yB) == 0))
				return new IntersectionPoint(segmentX.getRoadId(), segmentY.getRoadId(), xB.x, xB.y, gridSize );
		}
		if ( segmentX.isVertical() || segmentY.isVertical() )
			return null;
		// set ys equal: mx+b = mx+b
		double lhs = segmentX.getSlope() - segmentY.getSlope();
		double rhs = segmentY.getIntercept() - segmentX.getIntercept();
		double x = rhs / lhs;
		double y = segmentX.getSlope() * x + segmentX.getIntercept();
		return new IntersectionPoint(segmentX.getRoadId(), segmentY.getRoadId(), x, y, gridSize);
	}
	
	/**
	 * Sort the intersection points to match the order in which they appear
	 * in Road's getVertices() for accurate segment creation.
	 * 
	 * Considers non-roadpoint intersections.
	 * @param roads
	 */
	private void sortIntersectionMapByRoadVerticesOrder(List<Road> roads) {
		// consider non road intersection points, add to road vertices
		if ( hasNonRoadIntersections ) {
			for( IntersectionPoint ip : nonRoadIntersectionSet ) {				
				for (Road road : roads ) {
					Road newRoad = new Road(road);
					// insert intersection into both roads
					if ( road.getId().equals(ip.getRoadA()) || road.getId().equals(ip.getRoadB()) ) {
						ArrayList<GridLocation2D> newVertices = new ArrayList<GridLocation2D>();
						boolean inserted = false;
						double minDistance = Double.MAX_VALUE;
						for ( GridLocation2D g : road.getVertices() ) {
							double distance = calcDirectDistance(g,ip);
							if ( distance < minDistance || inserted) {
								minDistance = distance;
								newVertices.add(g);
							}
							else {
								GridLocation2D point = new GridLocation2D(road.getId(),null,ip.x,ip.y,g.gridSize);
								newVertices.add(point);
								inserted = true;
							}
						}
						// assign vertices back to roads
						newRoad.setVertices(newVertices);
						//road.setVertices(newVertices);
					} 
					//newRoads.add(road);
					newRoads.add(newRoad);
				}
			}
		} else {
			newRoads = roads;
		}
			
		for ( Road road : newRoads ) {
			LinkedList<IntersectionPoint> iPoints = intersectionMap.get(road.getId());
			LinkedList<IntersectionPoint> sortedIPoints = new LinkedList<IntersectionPoint>();			
			
			for ( GridLocation2D vertex : road.getVertices() ) {
				if ( iPoints.contains(vertex) )
					sortedIPoints.addLast(iPoints.get(iPoints.indexOf(vertex)));
			}
			// assign sorted list back to map
			intersectionMap.put(road.getId(), sortedIPoints);
		}
	}
	
	/**
	 * Test if a segment intersects any segments from the other roads.
	 */
	private void checkAllIntersects(AlgLineSegment s) {
		String segmentRoadId = s.getRoadId();
		
		for (String label : labels.keySet() ) {
			if ( !label.startsWith(segmentRoadId) ) {
				AlgLineSegment candidate = labels.get(label);
				if (isIntersection(s,candidate)) {
					IntersectionPoint ip = calculateIntersectionPt(s,candidate);
//					System.err.println("**Intersection: " + ip.x + ","+ip.y);
					insertIntersectionPoint(ip);
					// check if it is a non-road point
					double iX = ip.x.doubleValue();
					if ( iX != (int) iX ) {	// .5 x value
						nonRoadIntersectionSet.add(ip);
						hasNonRoadIntersections = true;
					}
				}
			}
		}
	}
	
	private double calcDirectDistance(GridLocation2D g, IntersectionPoint ip) {
		return Math.sqrt( Math.pow((ip.x-g.x),2) + (Math.pow((ip.y-g.y),2)));
	}
}

/**
 * Represents a line segment with fields for algorithm computation needs.
 * Any vertical segments are to be handled separately because the fields will be null.
 * @author Lily Wong
 *
 */
class AlgLineSegment extends RoadLineSegment implements Comparable<AlgLineSegment>{
	
	private String label;
	
	public AlgLineSegment(String roadId, int segmentIndex, GridLocation2D pointA, GridLocation2D pointB, GridSize gridSize) {
		super(roadId, pointA, pointB, gridSize);
		this.label = roadId+"-"+segmentIndex;
	}
	
	public String getLabel() {
		return label;
	}

	// comparing to a segment that is to the left of this segment
	@Override
	public int compareTo(AlgLineSegment o) {
		// equivalent, basing of labels only
		if ( this.equals(o) ) {
			return 0;
		}
		
		// consider Y value of start points, then X if needed
		// closer to axes is <
		int aY = this.getPointA().getY().compareTo(o.getPointA().getY());
		switch( aY ) {
		case -1:
			return -1;
		case 1:
			return 1;
		case 0:
			int aX = this.getPointA().getX().compareTo(o.getPointA().getX());
			switch( aX ) {
			case -1:
				return -1;
			case 1:
				return 1;
			case 0:
				// should be caught by if clause above
				return 0;
			}
		}
		
		// default return
		return -1;
	}

	@Override
	public String toString() {
		return "AlgLineSegment [label=" + label + ", RoadLineSegment="
				+ super.toString() + "]";
	}
	
	@Override
	public boolean equals(Object o) {
		AlgLineSegment s = (AlgLineSegment) o;
		if ( this.label.equals(s.label) && this.getPointA().equals(s.getPointA())  
				&& this.getPointB().equals(s.getPointB()) )
			return true;
		return false;
	}

	@Override
	public int hashCode() {
		return label.hashCode() + this.getPointA().hashCode() + this.getPointB().hashCode();
	}
}

/**
 * Represents a GridLocation2D, with a start and end indicator.
 * @author Lily Wong
 *
 */
class AlgPoint implements Comparable<AlgPoint> {

	private GridLocation2D point;
	private boolean isStart;
	private String label;
	
	public AlgPoint(GridLocation2D point, String label, boolean isStart) {
		this.point = point;
		this.isStart = isStart;
		this.label = label;
	}
	
	public boolean isStart() {
		return isStart;
	}

	public GridLocation2D getGridLocation2D() {
		return point;
	}

	public String getLabel() {
		return label;
	}

	@Override
	public int compareTo(AlgPoint o) {
		return this.point.compareTo(o.point);
	}
}



