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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.mitre.icarus.cps.feature_vector.phase_1.GridLocation2D;
import org.mitre.icarus.cps.feature_vector.phase_1.GridSize;
import org.mitre.icarus.cps.feature_vector.phase_1.Road;
import org.mitre.icarus.cps.feature_vector.phase_1.road_network.RoadLineSegment;

import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import de.micromata.opengis.kml.v_2_2_0.Data;
import de.micromata.opengis.kml.v_2_2_0.ExtendedData;
import de.micromata.opengis.kml.v_2_2_0.Feature;
import de.micromata.opengis.kml.v_2_2_0.LineString;
import de.micromata.opengis.kml.v_2_2_0.Placemark;

/**
 * Parses the KML road format.
 * 
 * @author Lily Wong
 *
 */
public class RoadKmlParser extends FeatureVectorKmlParser {

	private ArrayList<Road> roads = new ArrayList<Road>();
	//private boolean hasIntersections;
	
	/**
	 * Instantiates a new road kml.
	 * @param url
	 * @throws IOException
	 * @throws ParseException 
	 */
	public RoadKmlParser( URL url ) throws IOException, ParseException {
		super( url );
		parseKml();
	}
	
	/**
	 * Instantiates a new road kml.
	 * @param url
	 * @throws IOException
	 * @throws ParseException 
	 */
	public RoadKmlParser( URL url, GridSize gridSize ) throws IOException, ParseException {
		super( url, gridSize );
		parseKml();
	}
	
	/**
	 * Instantiates a new road kml.
	 *
	 * @param filepath the filepath
	 * @throws IOException
	 * @throws ParseException
	 */
	public RoadKmlParser( String filepath ) throws IOException, ParseException {
		super( filepath );
		parseKml();
	}
	
	/**
	 * Instantiates a new road kml.
	 *
	 * @param filepath the filepath
	 * @throws IOException
	 * @throws ParseException
	 */
	public RoadKmlParser( String filepath, GridSize gridSize ) throws IOException, ParseException {
		super( filepath, gridSize );
		parseKml();
	}
	
	/* Constructor and methods to convert a TaskCsv to a TaskKml object */
	/**
	 * Constructor for a new TaskKML object. Used by TaskCsv.toKML() methods.
	 *
	 * @param roadCsv the road csv
	 */
	public RoadKmlParser( RoadCsvParser roadCsv ) {
		super();
		new KmlStylesheet( doc, roadCsv.getFeatureType() );
		addPlacemarks( roadCsv );
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
	
	/**
	 * Adds the placemarks.
	 *
	 * @param roadCsv the road csv
	 */
	private void addPlacemarks( RoadCsvParser roadCsv ) {
		if(roadCsv == null || roadCsv.getRoads() == null) {
			return;
		}
		
		Iterator<Road> iter = roadCsv.getRoads().iterator();
		while ( iter.hasNext() ) {
			Road road = iter.next();
			// add extended data info
			ExtendedData ed = new ExtendedData();
			ed.addToData( createTag( "ObjectId", road.getId() ) );
			ed.addToData( createTag( "ObjectType", roadCsv.getFeatureType() ) );
						
			doc.createAndAddPlacemark()
				.withStyleUrl("#default")
				.withExtendedData(ed)
				.createAndSetLineString()
				.withCoordinates( road.getCoordinateList() );
		}
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.feature_vector.cpd1.FeatureVectorKml#parseKml()
	 */
	@Override
	protected void parseKml() throws ParseException {
		if(doc == null || doc.getFeature() == null) {
			return;
		}
		
		// iterate through list of Placemarks
		List<Feature> placemarks = doc.getFeature();
		Iterator<Feature> iter = placemarks.iterator();
		while ( iter.hasNext() ) {
			Placemark placemark = (Placemark) iter.next();
			ExtendedData extendedData = placemark.getExtendedData();
			List<Data> dataList = extendedData.getData();
			// gather object id, type, intel info
			String id = null;
			String type = null;
			for ( Data data : dataList ) {
				if ( data.getName().equals("ObjectId") )
					id = data.getValue();
				else if ( data.getName().equals("ObjectType"))
					type = data.getValue();
				else {}
			}
			if(id == null || type == null) {
				throw new ParseException("Error in " + kmlFile.getFile() + ": Must contain an ObjectId and ObjectType in ExtendedData.", 0);
			}
			
			// get KML coordinates
			if(placemark.getGeometry() == null || !(placemark.getGeometry() instanceof LineString)) {
				throw new ParseException("Error in " + kmlFile.getFile() + ": Must contain a LineString.", 0);
			}
			LineString linestring = (LineString) placemark.getGeometry();
			List<Coordinate> coordinates = linestring.getCoordinates();
			
			// create Road
			Road road = new Road( id );
			for ( Coordinate coordinate : coordinates ) {
				road.getVertices().add( new GridLocation2D( id, type, coordinate, gridSize ));
			}
			
			// add road to list
			roads.add(road);
		}
		
		// sort the points
		sortRoadPoints();
		
		// prune points that form a right angle for display
		pruneRoadPoints();
	}
	
	/**
	 * Sorts the points in the road to ensure that they will display as continuous
	 * line segments.
	 */
	/*private void sortRoadPoints() {		
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
			
			road.setVertices(roadPoints);
		}
	}*/
	
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
	 * @see org.mitre.icarus.cps.feature_vector.cpd1.FeatureVectorKml#toCsvFile(java.lang.String)
	 */
	@Override
	public void writeCsvToFile( String filepath ) throws IOException {
		
		FileWriter fw = new FileWriter( filepath );
		BufferedWriter csv = new BufferedWriter(fw);
		// write header
		csv.write( "ObjectId,ObjectType,X,Y\n" );
		if(roads != null) {
			for ( Road road : roads ) {
				for ( GridLocation2D loc : road.getVertices() ) {
					csv.write( buildCsvString( loc ));
				}
			}
		}
		csv.close();
	}
}