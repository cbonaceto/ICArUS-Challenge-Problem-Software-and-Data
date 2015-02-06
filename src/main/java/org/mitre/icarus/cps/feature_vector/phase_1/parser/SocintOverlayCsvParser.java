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
import java.util.Scanner;

import org.mitre.icarus.cps.feature_vector.phase_1.FeatureType;
import org.mitre.icarus.cps.feature_vector.phase_1.GridLocation2D;
import org.mitre.icarus.cps.feature_vector.phase_1.GridSize;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;
import org.mitre.icarus.cps.feature_vector.phase_1.SocintOverlay;

import de.micromata.opengis.kml.v_2_2_0.Coordinate;

/**
 * Parses the CSV SOCINT overlay format.
 * 
 * @author Lily Wong
 *
 */
public class SocintOverlayCsvParser extends FeatureVectorCsvParser {
	
	/*
	 * Idea: make Polygons with sides of GridSize
	 * with data pt as origin
	 * 
	 * Assume that all SOCINT values
	 */
	/** The socint pts. */
	SocintOverlay socintPts = new SocintOverlay();	

	/**
	 * Instantiates a new socint csv.
	 *
	 * @param gridSize the grid size
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ParseException the parse exception
	 */
	public SocintOverlayCsvParser(GridSize gridSize) throws IOException, ParseException {
		super(gridSize);
		parseCsv();
	}

	/**
	 * Instantiates a new socint csv.
	 *
	 * @param filepath the filepath
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ParseException the parse exception
	 */
	public SocintOverlayCsvParser(String filepath) throws IOException, ParseException {
		super(filepath);
		parseCsv();
	}

	/**
	 * Instantiates a new socint csv.
	 *
	 * @param filepath the filepath
	 * @param gridSize the grid size
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ParseException the parse exception
	 */
	public SocintOverlayCsvParser(String filepath, GridSize gridSize) throws IOException, ParseException {
		super(filepath, gridSize);
		parseCsv();
	}

	/**
	 * Instantiates a new socint csv.
	 *
	 * @param url the url
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ParseException the parse exception
	 */
	public SocintOverlayCsvParser(URL url) throws IOException, ParseException {
		super(url);
		parseCsv();
	}

	/**
	 * Instantiates a new socint csv.
	 *
	 * @param url the url
	 * @param gridSize the grid size
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ParseException 
	 */
	public SocintOverlayCsvParser(URL url, GridSize gridSize) throws IOException, ParseException {
		super(url, gridSize);
		parseCsv();
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.feature_vector.phase_1.parser.FeatureVectorCsv#parseCsv()
	 */
	@Override
	public void parseCsv() throws IOException, ParseException {
		// set feature type
		featureType = FeatureType.Socint;		
		// parse the csv file
		Scanner scan = new Scanner( csvFile.openStream() );
		try {
			boolean isHeader = true;
			int lineNum = 1;
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
				if ( vals.length == 0 ) {
					return;
				}

				// grab string fields
				String xStr = vals[0];
				String yStr = vals[1];
				String socintStr = vals[2];

				// convert to ints
				int x, y;
				GroupType socint;
				try {
					x = Integer.parseInt(xStr);
					y = Integer.parseInt(yStr);				
					socint = GroupType.values()[Integer.parseInt(socintStr)-1];
				} catch ( NumberFormatException nfe ) {
					throw new ParseException("Error at line " + lineNum + " in " + csvFile.getFile() + " +: X value must be a number.", 
							lineNum);
				}

				// create GridLocations for each data point
				GridLocation2D dataPt = new GridLocation2D(null, null, x, y, gridSize);

				// insert into list of list of pts
				if ( !socintPts.getSocintPts().containsKey(socint) )
					socintPts.getSocintPts().put(socint, new ArrayList<GridLocation2D>());

				socintPts.getSocintPts().get( socint ).add(dataPt);

				lineNum++;
			}
		} catch(ParseException ex) {
			throw ex;
		} finally {
			if(scan != null) {scan.close();}
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

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.feature_vector.phase_1.parser.FeatureVectorCsv#toKml()
	 */
	@Override
	public SocintOverlayKmlParser toKml() {
		return new SocintOverlayKmlParser( this );
	}
	
	public HashMap<GroupType, ArrayList<ArrayList<Coordinate>>> calculatePointRegionCoords() {
		// list of list of polygon socint region bounds for each pt in the region
		HashMap<GroupType, ArrayList<ArrayList<Coordinate>>> coordBounds = new HashMap<GroupType, ArrayList<ArrayList<Coordinate>>>();

		// for each region
		for ( GroupType group : socintPts.getSocintPts().keySet() ) {
			// create new list for this SOCINT value
			coordBounds.put(group, new ArrayList<ArrayList<Coordinate>>());
			
			// calculate the other points that form a rectangle with the data pt as the lower left corner
			for ( GridLocation2D pt : socintPts.getSocintPts().get(group) ) {
				// create new list for this pt
				ArrayList<Coordinate> ptRegionBounds = new ArrayList<Coordinate>();
				
				// insert this lower left hand point
				ptRegionBounds.add(pt.toKmlCoordinate());
				
				// calculate and insert additional pts for polygon bounds
				
				// up 
				GridLocation2D newUp = new GridLocation2D(null, null, pt.getX()+1, pt.getY(), gridSize);
				ptRegionBounds.add(newUp.toKmlCoordinate());
				
				// diagonal
				GridLocation2D newDiag = new GridLocation2D(null, null, pt.getX()+1, pt.getY()+1, gridSize);
				ptRegionBounds.add(newDiag.toKmlCoordinate());
				
				// right
				GridLocation2D newRight = new GridLocation2D(null, null, pt.getX(), pt.getY()+1, gridSize);
				newRight.setY( pt.getY() + 1 );
				ptRegionBounds.add(newRight.toKmlCoordinate());
				
				// add start pts as end pt
				ptRegionBounds.add(pt.toKmlCoordinate());
				
				// add list to list of regions under SOCINT value i
				coordBounds.get(group).add(ptRegionBounds);
			}
		}
		
		return coordBounds;
	}
}