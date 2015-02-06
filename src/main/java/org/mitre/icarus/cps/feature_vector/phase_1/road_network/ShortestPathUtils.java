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

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;

import org.mitre.icarus.cps.feature_vector.phase_1.GridLocation2D;

public class ShortestPathUtils {
	
	LinkedList<GridLocation2D> computingNodeList;
	LinkedList<RoadMetaLineSegment> computingGraph;
	
	GridLocation2D start;
	GridLocation2D end;
	
	HashSet<GridLocation2D> visitedNodes = new HashSet<GridLocation2D>();
	private HashMap<GridLocation2D, Double> shortestDistances;
	
	public HashMap<GridLocation2D, Double> getShortestDistances() {
		return shortestDistances;
	}

	public HashMap<GridLocation2D, GridLocation2D> getPreviousNodes() {
		return previousNodes;
	}

	private HashMap<GridLocation2D, GridLocation2D> previousNodes;
	
	private final Comparator<GridLocation2D> shortestDistanceComparator = new Comparator<GridLocation2D>()
    {
        public int compare(GridLocation2D left, GridLocation2D right)
        {
            double shortestDistanceLeft = shortestDistances.get(left);
            double shortestDistanceRight = shortestDistances.get(right);

            if (shortestDistanceLeft > shortestDistanceRight)
            {
                return +1;
            }
            else if (shortestDistanceLeft < shortestDistanceRight)
            {
                return -1;
            }
            else // equal
            {
                return left.compareTo(right);
            }
        }
    };
		    
	private PriorityQueue<GridLocation2D> unsettledNodes;
	
	
	public ShortestPathUtils(LinkedList<GridLocation2D> computingNodeList,
			LinkedList<RoadMetaLineSegment> computingGraph, GridLocation2D start, GridLocation2D end) {
		
		this.computingNodeList = computingNodeList;
		this.computingGraph = computingGraph;
		this.start = start;
		this.end = end;		
		
//		for ( GridLocation2D g : computingNodeList ) {
//			System.out.println( "\t" + g.toString() );
//		}
		
		visitedNodes = new HashSet<GridLocation2D>();
		shortestDistances = new HashMap<GridLocation2D, Double>();		
		
		if(computingNodeList.size() == 0) {
			unsettledNodes = new PriorityQueue<GridLocation2D>(1, shortestDistanceComparator); //TODO: Craig added this if check
		} else {
			unsettledNodes = new PriorityQueue<GridLocation2D>(computingNodeList.size(), shortestDistanceComparator);
		}
		previousNodes = new HashMap<GridLocation2D, GridLocation2D>();
		
		// suitable upperbound that isn't max
//		double upperBound = end.gridSize.getGridHeight()*100;
		double upperBound = Double.MAX_VALUE;
		for ( GridLocation2D g : computingNodeList ) {
			shortestDistances.put(g, upperBound);
		}
		// start to start is 0
		shortestDistances.put(start, 0.0);
		
		runDijkstras();
	}
	
	
	private void runDijkstras() {		
		unsettledNodes.add(start);
		
		while ( !unsettledNodes.isEmpty() ) {
			// get the node with the shortest distance
			GridLocation2D u = extractMin();
			
//			System.out.println("min: " + u.toString());
//			System.out.println(shortestDistances.get(u));

	        // destination reached, stop
	        if (u == end) {
//	        	for ( Double d : shortestDistances.values() ) {
//	    			System.out.println("\t***"+d.doubleValue());
//	    		}
//	    		System.out.println( "$$$" +shortestDistances.get(end) );
	        	break;
	        }

	        visitedNodes.add(u);
	        
	        relaxNeighbors(u);
		}
		
//		for ( Entry<GridLocation2D,Double> e : shortestDistances.entrySet()) {
//			System.out.println(e.getKey().toString() + " " +  e.getValue());
//		}
//		for ( Double d : shortestDistances.values() ) {
//			System.out.println("\t***"+d.doubleValue());
//		}
//		System.out.println( "$$$" +shortestDistances.get(end) );
//		
//		for ( Entry<GridLocation2D, GridLocation2D> entry: previousNodes.entrySet() ) {
//			System.out.println( "***" +shortestDistances.get(end) );
//		}
	}
	
	private void relaxNeighbors(GridLocation2D point) {
		
//		System.out.println("considering pt: " + point.toString());
		
		// for each neighbor
		for ( RoadMetaLineSegment ls : computingGraph ) {
			if (( ls.getPointA().equals(point) && !visitedNodes.contains(ls.getPointB())) ){
		 
//				System.out.println("\tneighbor B is " + ls.toString());
				double newDist  = shortestDistances.get(point) + ls.getLengthInUnits();
				if ( shortestDistances.get(ls.getPointB()) > newDist) {
					shortestDistances.put(ls.getPointB(), newDist);
					previousNodes.put(ls.getPointB(), point);
					unsettledNodes.add(ls.getPointB());
				}
					
			}
			
			else if (ls.getPointB().equals(point) && !visitedNodes.contains(ls.getPointA())) {
//				System.out.println("\tneighbor A is " + ls.toString());
				double newDist  = shortestDistances.get(point) + ls.getLengthInUnits();
				if ( shortestDistances.get(ls.getPointA()) > newDist) {
					shortestDistances.put(ls.getPointA(), newDist);
					previousNodes.put(ls.getPointA(), point);
					unsettledNodes.add(ls.getPointA());
				}
			}
				
		}
	}	
	
	private GridLocation2D extractMin()
	{
	    return unsettledNodes.poll();
	}

}
