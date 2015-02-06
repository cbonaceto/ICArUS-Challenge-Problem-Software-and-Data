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

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

import org.mitre.icarus.cps.feature_vector.phase_1.FeatureType;
import org.mitre.icarus.cps.feature_vector.phase_1.GridLocation2D;
import org.mitre.icarus.cps.feature_vector.phase_1.GridSize;
import org.mitre.icarus.cps.feature_vector.phase_1.Road;
import org.mitre.icarus.cps.feature_vector.phase_1.road_network.RoadLineSegment;

/**
 * Parses the CSV road format.
 *
 * @author LWONG
 */
public class RoadCsvParser extends FeatureVectorCsvParser {
	
	/** The roads. */
	private ArrayList<Road> roads = new ArrayList<Road>();
	
	/**
	 * Instantiates a new road csv.
	 *
	 * @param filepath the filepath
	 * @throws IOException
	 * @throws ParseException
	 */
	public RoadCsvParser(String filepath) throws IOException, ParseException {
		super(filepath);
		parseCsv();
	}
	
	/**
	 * Instantiates a new road csv.
	 *
	 * @param filepath the filepath
	 * @param gridSize the grid size
	 * @throws IOException
	 * @throws ParseException
	 */
	public RoadCsvParser(String filepath, GridSize gridSize) throws IOException, ParseException {
		super(filepath, gridSize);
		parseCsv();
	}
	
	/**
	 * Instantiates a new road csv from URL.
	 *
	 * @param url the url
	 * @throws IOException
	 * @throws ParseException
	 */
	public RoadCsvParser(URL url) throws IOException, ParseException {
		super( url );
		parseCsv();
	}
	
	/**
	 * Instantiates a new road csv from URL.
	 *
	 * @param url the url
	 * @throws IOException
	 * @throws ParseException
	 */
	public RoadCsvParser(URL url, GridSize gridSize) throws IOException, ParseException {
		super( url, gridSize );
		parseCsv();
	}
	
	/**
	 * Gets the number of roads.
	 *
	 * @return the num roads
	 */
	public int getNumRoads() { return roads.size(); }
	
	/**
	 * Gets the roads.
	 *
	 * @return the roads
	 */
	public ArrayList<Road> getRoads() { return roads; }
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.feature_vector.cpd1.FeatureVectorCsv#getFeatureType()
	 */
	public FeatureType getFeatureType() { return featureType; }
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.feature_vector.cpd1.FeatureVectorCsv#parseCsv()
	 */
	public void parseCsv() throws IOException, ParseException {
		// road id, arraylist index
		HashMap<String, Integer> roadIdToIndex = new HashMap<String, Integer>();

		// parse the csv file
		Scanner scan;	
		scan = new Scanner ( csvFile.openStream() );
		try {
			boolean isHeader = true;
			int lineNum = 1;
			//GridLocation2D prevPoint = null;

			while ( scan.hasNextLine() ) {
				// skip header line
				if ( isHeader ) {
					scan.nextLine();
					isHeader = false;
					lineNum++;
				}
				String line = scan.nextLine();
				String[] vals = line.split(",");

				// in case there are empty lines in the CSV
				if ( vals.length == 0 ) 
					return;

				if(vals.length < 4) {
					throw new ParseException("Error at line " + lineNum + " in " + csvFile.getFile() + " +: Must contain 4 columns (Road ID, Road, X, Y).", 
							lineNum);
				}

				String id 	= vals[0];			
				String type = vals[1];

				double x = 0;
				try {
					x = Double.parseDouble(vals[2]);
				} catch(NumberFormatException e) {
					throw new ParseException("Error at line " + lineNum + " in " + csvFile.getFile() + " +: X value must be a number.", 
							lineNum);
				}

				double y = 0;
				try {
					y = Double.parseDouble(vals[3]);	
				} catch(NumberFormatException e) {
					throw new ParseException("Error at line " + lineNum + " in " + csvFile.getFile() + ": Y value must be a number.", 
							lineNum);
				}			

				if ( featureType == null ) {
					determineFeatureType( type );
				}

				// read each different id as a different road
				GridLocation2D roadPoint = new GridLocation2D(id, type, x, y, gridSize);

				// if there is not already an arraylist for this road id, create it
				if ( !roadIdToIndex.containsKey(id) ) {
					roadIdToIndex.put( id, roads.size() );	// value = ArrayList index
					roads.add( new Road(id) );
				} 

				// add point to its list
				roads.get( roadIdToIndex.get(id) ).getVertices().add(roadPoint);

				//			// if the previous point is along the same road
				//			// **this is an end point, create line segment**
				//			if ( prevPoint != null && prevPoint.getLocationId().equals(roadPoint.getLocationId()) ) {
				//				RoadLineSegment segment = new RoadLineSegment( roadPoint.getLocationId(), prevPoint, roadPoint);
				//				roads.get( roadIdToIndex.get(id) ).getLineSegments().add(segment);
				//			}
				//			
				//			// set this point to be the first point of a segment
				//			prevPoint = roadPoint;	

				lineNum++;
			}

			// sort the points
			sortRoadPoints();

			// prune points that form a right angle for display
			pruneRoadPoints();
		} catch(ParseException ex) {
			throw ex;
		} finally {
			if(scan != null) {scan.close();}
		}

	}	

	/**
	 * Sorts the points in the road to ensure that they will display as continuous
	 * line segments.  Also creates line segments between adjacent points.
	 */
	private void sortRoadPoints() {
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
	private void pruneRoadPoints() {
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
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.feature_vector.cpd1.FeatureVectorCsv#toKml()
	 */
	@Override
	public RoadKmlParser toKml() {
		return new RoadKmlParser( this );
	}
}