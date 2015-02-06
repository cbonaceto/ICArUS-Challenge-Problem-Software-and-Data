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
import java.util.List;

import org.mitre.icarus.cps.feature_vector.phase_1.GridLocation2D;
import org.mitre.icarus.cps.feature_vector.phase_1.GridSize;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupAttack;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupCenter;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;
import org.mitre.icarus.cps.feature_vector.phase_1.ImintType;
import org.mitre.icarus.cps.feature_vector.phase_1.IntType;
import org.mitre.icarus.cps.feature_vector.phase_1.LocationIntelReport;
import org.mitre.icarus.cps.feature_vector.phase_1.MovintType;
import org.mitre.icarus.cps.feature_vector.phase_1.Phase1Feature;
import org.mitre.icarus.cps.feature_vector.phase_1.RoadMarker;
import org.mitre.icarus.cps.feature_vector.phase_1.SigintType;

import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import de.micromata.opengis.kml.v_2_2_0.Data;
import de.micromata.opengis.kml.v_2_2_0.ExtendedData;
import de.micromata.opengis.kml.v_2_2_0.Feature;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Point;

/**
 * Parses task KML feature vector files.
 * 
 * @author Lily Wong
 *
 */
public class TaskKmlParser extends FeatureVectorKmlParser {
	
	/* Categorization of all entries in this Task */
	/** The attacks. */
	private ArrayList<GroupAttack> attacks = new ArrayList<GroupAttack>();
	
	/** The centers. */
	private ArrayList<GroupCenter> centers = new ArrayList<GroupCenter>();
	
	/** The markers. */
	private ArrayList<RoadMarker> markers  = new ArrayList<RoadMarker>();
	
	/**
	 * Instantiates a new task kml.
	 * @param url
	 * @throws IOException
	 * @throws ParseException 
	 */
	public TaskKmlParser( URL url ) throws IOException, ParseException {
		super( url );
		parseKml();
	}
	
	/**
	 * Instantiates a new task kml.
	 * @param url
	 * @throws IOException
	 * @throws ParseException 
	 */
	public TaskKmlParser( URL url, GridSize gridSize ) throws IOException, ParseException {
		super( url, gridSize );
		parseKml();
	}
	
	/**
	 * Instantiates a new task kml.
	 *
	 * @throws IOException
	 * @throws ParseException
	 */
	public TaskKmlParser( String filepath ) throws IOException, ParseException {
		super( filepath );
		parseKml();
	}
	
	/**
	 * Instantiates a new task kml.
	 *
	 * @throws IOException
	 * @throws ParseException
	 */
	public TaskKmlParser( String filepath, GridSize gridSize ) throws IOException, ParseException {
		super( filepath, gridSize );
		parseKml();
	}
	
	/* Constructor and methods to convert a TaskCsv to a TaskKml object */
	/**
	 * Constructor for a new TaskKML object. Used by TaskCsv.toKML() methods.
	 *
	 * @param taskCsv the task csv
	 */
	public TaskKmlParser( TaskCsvParser taskCsv ) {
		super();
		setFilename( taskCsv.getFilename() );
		new KmlStylesheet( doc, taskCsv.getFeatureType() );
		addPlacemarks( taskCsv );
	}

	/* Accessor methods */
	/**
	 * Gets the list of attacks.
	 *
	 * @return the attacks
	 */
	public ArrayList<GroupAttack> getAttacks() {
		return attacks;
	}
	
	/**
	 * Gets the list of group centers.
	 *
	 * @return the centers
	 */
	public ArrayList<GroupCenter> getCenters() {
		return centers;
	}
	
	/**
	 * Gets the list of road markers.
	 *
	 * @return the markers
	 */
	public ArrayList<RoadMarker> getMarkers() {
		return markers;
	}
	
	/**
	 * Returns the number of features in this Task KML.
	 *
	 * @return the number of features
	 */
	public int size() {
		return attacks.size() + centers.size() + markers.size();
	}
	
	/**
	 * Add placemarks from lists of features.
	 * Used by Constructor to convert a CSV to a KML.
	 *
	 * @param taskCsv the task csv
	 */
	private void addPlacemarks( TaskCsvParser taskCsv) {	
		if(taskCsv == null) {
			return;
		}
		
		if(taskCsv.getAttacks() != null) {
			addTaskPlacemarks( taskCsv.getAttacks() );
		}
		if(taskCsv.getCenters() != null) {
			addTaskPlacemarks( taskCsv.getCenters() );
		}
		if(taskCsv.getMarkers() != null) {
			addTaskPlacemarks( taskCsv.getMarkers() );
		}
	}
	
	/**
	 * Add placemarks for each item in a list of features.
	 * Used by Constructor to convert a CSV to a KML.
	 *
	 * @param features the features
	 */
	private void addTaskPlacemarks( ArrayList<? extends Phase1Feature> features ) {
		if(features == null) {
			return;
		}
		
		Iterator<? extends Phase1Feature> iter = features.iterator();
		while ( iter.hasNext() ) {
			Phase1Feature feature = iter.next();
			// add extended data info
			ExtendedData ed = new ExtendedData();
			ed.addToData( createTag( "ObjectId", 	feature.getLocation().getLocationId() ) );
			ed.addToData( createTag( "ObjectType",  feature.getLocation().getType() ));
			
			// add placemark for the intel report
			if ( feature.hasIntelReport() ) {
				LocationIntelReport lir = feature.getIntelReport();
				
				/* 1 Placemark for every location in the CSV 
				 * Uncommenting the lines below creates 1 Placemark for every piece 
				 * of data about a location. */
				if ( lir.hasImintInfo() ) {
//					ed = new ExtendedData();
					ed.addToData( createTag( IntType.IMINT.toString(), lir.getImintInfo().toString() ));
//					addIntPlacemark( feature, ed, lir, lir.getImintInfo().toString() ); 
				}
				if ( lir.hasMovintInfo() ) {
//					ed = new ExtendedData();
					ed.addToData( createTag( IntType.MOVINT.toString(), lir.getMovintInfo().toString() ));
//					addIntPlacemark( feature, ed, lir, lir.getMovintInfo().toString() );
				}
				if ( lir.hasSigintInfo() ) {
//					ed = new ExtendedData();
					ed.addToData( createTag( IntType.SIGINT.toString(), lir.getSigintInfo().toString() ));
//					addIntPlacemark( feature, ed, lir, lir.getSigintInfo().toString() );
				}
				if ( lir.hasSocintInfo() ) {
//					ed = new ExtendedData();
					ed.addToData( createTag( IntType.SOCINT.toString(), lir.getSocintInfo().toString() ));
//					addIntPlacemark( feature, ed, lir, lir.getSocintInfo().toString() );
				}
			}
			
			// add placemark for the location
			doc.createAndAddPlacemark()
				.withExtendedData(ed)
				.withStyleUrl( "#"+ feature.getId() )
				.createAndSetPoint().addToCoordinates( feature.getLocation().getLon(), 
						feature.getLocation().getLat() );
		}
	}
	
	/**
	 * Add a placemark for an INT, containing extended data about the INT only.
	 * Used by Constructor to convert a CSV to a KML.
	 *
	 * @param feature the feature
	 * @param ed the ed
	 * @param lir the lir
	 * @param styleUrlKey the style url key
	 */
	@SuppressWarnings("unused")
	private void addIntPlacemark( Phase1Feature feature, ExtendedData ed, 
								LocationIntelReport lir, String styleUrlKey ) {
		doc.createAndAddPlacemark()
			.withExtendedData(ed)
			.withStyleUrl( feature.getIcons().get( feature.getIcons().get( styleUrlKey ))) 
			.createAndSetPoint().addToCoordinates( feature.getLocation().getLon(), 
													feature.getLocation().getLat() );
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
			LocationIntelReport locationIntel = null;
			GroupType gt = null;
			ImintType it  = null;
			MovintType mt = null;
			SigintType st = null;
			for ( Data data : dataList ) {
				if ( data.getName().equals("ObjectId") )
					id = data.getValue();
				else if ( data.getName().equals("ObjectType"))
					type = data.getValue();
				else {
					// is an INT
					if ( data.getName().equals(IntType.IMINT.toString()) ) {
						try {
							it = ImintType.valueOf(data.getValue());
						} catch (Exception e) {
							throw new ParseException("Error  in " + kmlFile.getFile() + ": Invalid IMINT type: " + data.getValue() + ".", 
									0);
						}
					}
					else if ( data.getName().equals(IntType.MOVINT.toString())) {
						try {
							mt = MovintType.valueOf(data.getValue());
						} catch (Exception e) {
							throw new ParseException("Error  in " + kmlFile.getFile() + ": Invalid MOVINT type: " + data.getValue() + ".", 
									0);
						}
					}
					else if ( data.getName().equals(IntType.SIGINT.toString())) {
						try {
							st = SigintType.valueOf(data.getValue());
						} catch (Exception e) {
							throw new ParseException("Error  in " + kmlFile.getFile() + ": Invalid SIGINT type: " + data.getValue() + ".", 
									0);
						}
					}
					else if ( data.getName().equals(IntType.SOCINT.toString())) {
						try {
							gt = GroupType.valueOf(data.getValue());
						} catch (Exception e) {
							throw new ParseException("Error in " + kmlFile.getFile() + 
									": Group type must be one of " + GroupType.createGroupListString() + ".", 0);
						}
					}
				}
			}
			
			if(id == null || type == null) {
				throw new ParseException("Error in " + kmlFile.getFile() + ": Must contain an ObjectId and ObjectType in ExtendedData.", 0);
			}
			
			// get KML coordinates
			if(placemark.getGeometry() == null || !(placemark.getGeometry() instanceof Point)) {
				throw new ParseException("Error in " + kmlFile.getFile() + ": Must contain a Point.", 0);
			}	
			Point point = (Point) placemark.getGeometry();
			Coordinate coordinate = point.getCoordinates().get(0);
			
			// get GroupType if exists for location creation
			GroupType groupId = null;
			try { 
				groupId = GroupType.valueOf(id);
			} catch ( IllegalArgumentException e ) {
				// the object id is an int or is not one of the known groups
			}
			
			// create an intel report if there is intel
			if ( gt != null || it != null || mt != null || st != null )
				locationIntel = new LocationIntelReport( gt, it, mt, st );
			
			// create info about the location
			GridLocation2D location = new GridLocation2D( id, type, coordinate, gridSize );
			
			// assign type of this GridLocation2D point, add to a list
			if ( location.isAttackLocation() ) {			
				GroupAttack ga = new GroupAttack( groupId, location, locationIntel );
				attacks.add(ga);
			}
			else if ( location.isGroupCenter() ) {
				GroupCenter gc = new GroupCenter( groupId, location, locationIntel );
				centers.add(gc);
			}
			else if ( location.isRoadMarker() ) {
				RoadMarker rm = new RoadMarker( id, location, locationIntel );
				markers.add(rm);
			}
			else {} 
		}
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.feature_vector.cpd1.FeatureVectorKml#toCsvFile(java.lang.String)
	 */
	@Override
	public void writeCsvToFile( String filepath ) throws IOException {
		// open output file
		FileWriter fw = new FileWriter( filepath );
		BufferedWriter csv = new BufferedWriter(fw);

		// write header
		csv.write( "ObjectId,ObjectType,X,Y,SOCINT,IMINT,MOVINT,SIGINT\n" );

		// write each feature's data in csv form
		if(attacks != null) {
			for ( GroupAttack ga : attacks ) {
				csv.write( buildCsvString(ga) );
			}
		}

		if(centers != null) {
			for ( GroupCenter gc : centers ) {
				csv.write( buildCsvString(gc) );
			}
		}

		if(markers != null) {
			for ( RoadMarker rm : markers ) {
				csv.write( buildCsvString(rm) );
			}
		}

		csv.close();
	}
}