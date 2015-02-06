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
import org.mitre.icarus.cps.feature_vector.phase_1.SocintPolygon;

import de.micromata.opengis.kml.v_2_2_0.Boundary;
import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import de.micromata.opengis.kml.v_2_2_0.Data;
import de.micromata.opengis.kml.v_2_2_0.ExtendedData;
import de.micromata.opengis.kml.v_2_2_0.Feature;
import de.micromata.opengis.kml.v_2_2_0.LinearRing;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Polygon;

/**
 * Parses the KML SOCINT polygon format.
 *  
 * @author Lily Wong
 *
 */
public class SocintPolygonKmlParser extends FeatureVectorKmlParser {
	
	private ArrayList<SocintPolygon> regions = new ArrayList<SocintPolygon>();
	
	/**
	 * Instantiates a new region kml.
	 * @param url
	 * @throws IOException
	 * @throws ParseException 
	 */
	public SocintPolygonKmlParser( URL url ) throws IOException, ParseException {
		super( url );
		parseKml();
	}
	
	/**
	 * Instantiates a new region kml.
	 * @param url
	 * @throws IOException
	 * @throws ParseException 
	 */
	public SocintPolygonKmlParser( URL url, GridSize gridSize ) throws IOException, ParseException {
		super( url, gridSize );
		parseKml();
	}
	
	/**
	 * Instantiates a new region kml.
	 *
	 * @param filepath the filepath
	 * @throws IOException
	 * @throws ParseException
	 */
	public SocintPolygonKmlParser( String filepath ) throws IOException, ParseException {
		super( filepath );
		parseKml();
	}
	
	/**
	 * Instantiates a new region kml.
	 *
	 * @param filepath the filepath
	 * @throws IOException
	 * @throws ParseException
	 */
	public SocintPolygonKmlParser( String filepath, GridSize gridSize ) throws IOException, ParseException {
		super( filepath, gridSize );
		parseKml();
	}
	
	/* Constructor and methods to convert a RegionCsv to a RegionKml object */
	/**
	 * Constructor for a new TaskKML object. Used by TaskCsv.toKML() methods.
	 *
	 * @param regionCsv the region csv
	 */
	public SocintPolygonKmlParser( SocintPolygonCsvParser regionCsv ) {
		super();
		new KmlStylesheet( doc, regionCsv.getFeatureType() );
		addPlacemarks( regionCsv );
	}
	
	/**
	 * Gets the number of regions.
	 *
	 * @return the num regions
	 */
	public int getNumRegions() { return regions.size(); }
	
	/**
	 * Gets the regions.
	 *
	 * @return the regions
	 */
	public ArrayList<SocintPolygon> getRegions() { return regions; }
	
	/**
	 * Adds the placemarks.
	 *
	 * @param regionCsv the region csv
	 */
	private void addPlacemarks( SocintPolygonCsvParser regionCsv ) {
		if(regionCsv == null || regionCsv.getRegions() == null) {
			return;
		}
		
		Iterator<SocintPolygon> iter = regionCsv.getRegions().iterator();
		while ( iter.hasNext() ) {
			SocintPolygon region = iter.next();
			// add extended data info
			ExtendedData ed = new ExtendedData();
			ed.addToData( createTag( "ObjectId", region.getGroup().toString() ) );
			ed.addToData( createTag( "ObjectType", regionCsv.getFeatureType() ) );
						
			doc.createAndAddPlacemark()
				.withStyleUrl("#" + region.getGroup().toString() + "region")
				.withExtendedData(ed)
				.createAndSetPolygon()
				.createAndSetOuterBoundaryIs()
				.createAndSetLinearRing()
				.withCoordinates( region.getCoordinateList() );
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
				if ( data.getName().equals("ObjectId") ) {
					id = data.getValue();
				}
				else if ( data.getName().equals("ObjectType")) {
					type = data.getValue();
				}					
				else {					
				}
			}
			if(id == null || type == null) {
				throw new ParseException("Error in " + kmlFile.getFile() + ": Must contain an ObjectId and ObjectType in ExtendedData.", 0);
			}
			
			// get KML coordinates
			if(placemark.getGeometry() == null || !(placemark.getGeometry() instanceof Polygon)) {
				throw new ParseException("Error in " + kmlFile.getFile() + ": Must contain a Polygon.", 0);
			}			
			Polygon polygon = (Polygon) placemark.getGeometry();
			Boundary outerBoundary = polygon.getOuterBoundaryIs();
			LinearRing linearRing = outerBoundary.getLinearRing();
			List<Coordinate> coordinates = linearRing.getCoordinates();
			
			// create Region
			SocintPolygon socintRegion = new SocintPolygon( id );
			for ( Coordinate coordinate : coordinates ) {
				socintRegion.getVertices().add( new GridLocation2D( id, type, coordinate, gridSize ));
			}
			
			// add region to list
			regions.add(socintRegion);
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
		if(regions != null) {
			for ( SocintPolygon region : regions ) {
				for ( GridLocation2D loc : region.getVertices() )
					csv.write( buildCsvString( loc ));
			}
		}
		csv.close();
	}
}