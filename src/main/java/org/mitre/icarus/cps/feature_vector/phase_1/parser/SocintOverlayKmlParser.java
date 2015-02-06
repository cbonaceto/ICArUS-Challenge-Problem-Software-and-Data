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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.mitre.icarus.cps.feature_vector.phase_1.GridLocation2D;
import org.mitre.icarus.cps.feature_vector.phase_1.GridSize;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;
import org.mitre.icarus.cps.feature_vector.phase_1.SocintOverlay;

import de.micromata.opengis.kml.v_2_2_0.Boundary;
import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import de.micromata.opengis.kml.v_2_2_0.Data;
import de.micromata.opengis.kml.v_2_2_0.ExtendedData;
import de.micromata.opengis.kml.v_2_2_0.Feature;
import de.micromata.opengis.kml.v_2_2_0.LinearRing;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Polygon;

/**
 * Parses the KML SOCINT overlay format.
 * 
 * @author Lily Wong
 *
 */
public class SocintOverlayKmlParser extends FeatureVectorKmlParser {

	/** The socint pts. */
	SocintOverlay socintPts = new SocintOverlay();	
	
	/**
	 * 
	 */
	public SocintOverlayKmlParser() {}

	/**
	 * @param url
	 * @throws IOException
	 * @throws ParseException
	 */
	public SocintOverlayKmlParser(URL url) throws IOException, ParseException {
		super(url);
		parseKml();
	}

	/**
	 * @param url
	 * @param gridSize
	 * @throws IOException
	 * @throws ParseException
	 */
	public SocintOverlayKmlParser(URL url, GridSize gridSize) throws IOException,
			ParseException {
		super(url, gridSize);
		parseKml();
	}

	/**
	 * @param filepath
	 * @throws IOException
	 * @throws ParseException
	 */
	public SocintOverlayKmlParser(String filepath) throws IOException, ParseException {
		super(filepath);
		parseKml();
	}

	/**
	 * @param filepath
	 * @param gridSize
	 * @throws IOException
	 * @throws ParseException
	 */
	public SocintOverlayKmlParser(String filepath, GridSize gridSize) throws IOException, ParseException {
		super(filepath, gridSize);
		parseKml();
	}
	
	public SocintOverlayKmlParser( SocintOverlayCsvParser socintCsv ) {
		super();
		new KmlStylesheet( doc, socintCsv.getFeatureType() );
		addPlacemarks( socintCsv );
	}	

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.feature_vector.phase_1.parser.FeatureVectorKml#parseKml()
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
			// gather socint info
			GroupType socint = null;	
			for ( Data data : dataList ) {
				if ( data.getName().equals("SOCINT") ) {
					try {
						//socint = GroupType.values()[Integer.parseInt(data.getValue())-1];
						socint = GroupType.valueOf(data.getValue());
					} catch ( NumberFormatException nfe ) {
						//throw new ParseException("SOCINT value should be an integer.", 0);
						throw new ParseException("SOCINT value should a group type.", 0);
					}
				}
			}
			if(socint == null) {
				throw new ParseException("Error in " + kmlFile.getFile() + ": Must contain a SOCINT value in ExtendedData.", 0);
			}
			
			// create new list if first placemark in region
			if ( !socintPts.getSocintPts().containsKey(socint)) {
				socintPts.getSocintPts().put( socint, new ArrayList<GridLocation2D>() );
			}
			
			// get KML coordinates
			if(placemark.getGeometry() == null || !(placemark.getGeometry() instanceof Polygon)) {
				throw new ParseException("Error in " + kmlFile.getFile() + ": Must contain a Polygon.", 0);
			}			
			Polygon polygon = (Polygon) placemark.getGeometry();
			Boundary outerBoundary = polygon.getOuterBoundaryIs();
			LinearRing linearRing = outerBoundary.getLinearRing();
			List<Coordinate> coordinates = linearRing.getCoordinates();
			
			// grab first coordinate
			Coordinate pt = coordinates.get(0);
			
			// add to appropriate list
			socintPts.getSocintPts().get(socint).add( new GridLocation2D(null, null, pt, gridSize) );
		}		

	}	
	
	/**
	 * Gets the socint pts.
	 *
	 * @return the socint pts
	 */
	public SocintOverlay getSocintPts() {
		return socintPts;
	}
	
	/**
	 * Adds the placemarks.
	 *
	 * @param socintCsv the region csv
	 */
	private void addPlacemarks( SocintOverlayCsvParser socintCsv ) {
		if(socintCsv == null || socintCsv.getSocintPts() == null) {
			return;
		}
		
		HashMap<GroupType, ArrayList<ArrayList<Coordinate>>> coords = socintCsv.calculatePointRegionCoords();
		
		for ( GroupType group : coords.keySet() ) {
			ArrayList<ArrayList<Coordinate>> regionList = coords.get(group);
			ExtendedData ed = new ExtendedData();
			//ed.addToData( createTag( "SOCINT", Integer.toString(group.ordinal() + 1) ) );
			ed.addToData( createTag( "SOCINT", group.toString() ) );
			
			// create a polygon for each region pt bound
			for ( ArrayList<Coordinate> regionBounds : regionList ) {
				doc.createAndAddPlacemark()
				.withStyleUrl("#" + Integer.toString(group.ordinal() + 1) + "pt")
				.withExtendedData(ed)
				.createAndSetPolygon()
				.createAndSetOuterBoundaryIs()
				.createAndSetLinearRing()
				.withTessellate(true)
				.withCoordinates( regionBounds );
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.feature_vector.phase_1.parser.FeatureVectorKml#toCsvFile(java.lang.String)
	 */
	@Override
	public void writeCsvToFile(String filepath ) throws IOException {
		FileWriter fw = new FileWriter( filepath );
		BufferedWriter csv = new BufferedWriter(fw);
		// write header
		csv.write( "X,Y,SOCINT\n" );
		// write data
		if( socintPts != null) {
			for ( GroupType group : socintPts.getSocintPts().keySet() ) {
				for ( GridLocation2D pt : socintPts.getSocintPts().get(group) ) {
					csv.write( pt.getX().intValue() + "," + pt.getY().intValue() + "," + Integer.toString(group.ordinal()+1) );
				}
			}
		}
		csv.close();
	}

}
