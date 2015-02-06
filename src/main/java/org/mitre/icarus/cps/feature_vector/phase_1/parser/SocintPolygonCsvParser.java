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
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.mitre.icarus.cps.feature_vector.phase_1.GridLocation2D;
import org.mitre.icarus.cps.feature_vector.phase_1.GridSize;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;
import org.mitre.icarus.cps.feature_vector.phase_1.SocintPolygon;

/**
 * Parses the CSV SOCINT polygon format.
 * 
 * @author Lily Wong
 *
 */
public class SocintPolygonCsvParser extends FeatureVectorCsvParser {
	
	private ArrayList<SocintPolygon> regions = new ArrayList<SocintPolygon>();
	
	/**
	 * Instantiates a new region csv.
	 *
	 * @param filepath the filepath
	 * @throws IOException, ParseException
	 */
	public SocintPolygonCsvParser(String filepath ) throws IOException, ParseException {
		super(filepath );
		parseCsv();
	}
	
	/**
	 * Instantiates a new region csv.
	 *
	 * @param filepath the filepath
	 * @param gridSize the grid size
	 * @throws IOException, ParseException
	 */
	public SocintPolygonCsvParser(String filepath, GridSize gridSize) throws IOException, ParseException {
		super(filepath, gridSize);
		parseCsv();
	}
	
	/**
	 * Instantiates a new region csv from URL.
	 * @param url
	 * @throws URISyntaxException
	 * @throws IOException, ParseException
	 */
	public SocintPolygonCsvParser(URL url) throws IOException, ParseException {
		super( url );
		parseCsv();
	}
	
	/**
	 * Instantiates a new region csv from URL.
	 * @param url
	 * @throws IOException
	 * @throws ParseException
	 */
	public SocintPolygonCsvParser(URL url, GridSize gridSize) throws IOException, ParseException {
		super( url, gridSize );
		parseCsv();
	}
	
	/**
	 * Gets the number of regions.
	 *
	 * @return the num regions
	 */
	public int getNumRegions() { return regions.size(); }
	
	/**
	 * Gets the list of regions.
	 *
	 * @return the regions
	 */
	public ArrayList<SocintPolygon> getRegions() { return regions; }
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.feature_vector.cpd1.FeatureVectorCsv#parseCsv()
	 */
	public void parseCsv() throws IOException, ParseException {
		// road id, arraylist index
		HashMap<String, Integer> regionGroupToIndex = new HashMap<String, Integer>();
				
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

				if(vals.length < 4) {
					throw new ParseException("Error at line " + lineNum + " in " + csvFile.getFile() + " +: Must contain 4 columns (Group ID, Region, X, Y).", 
							lineNum);
				}

				// 4 main fields
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

				/* if id is A,B,C,D, then group attack is known or this is the group center
				if id is 1,2,3,4, is a road marker */
				@SuppressWarnings("unused")
				GroupType groupId = null;
				try { 
					groupId = GroupType.valueOf(id);
				} catch ( IllegalArgumentException e ) {
					// the object id is not one of the known groups
					throw new ParseException("Error at line " + lineNum + " in " + csvFile.getFile() + 
							": Group type must be one of " + GroupType.createGroupListString() + ".", 
							lineNum);
				}

				// coordinate point
				GridLocation2D point = new GridLocation2D(id,type, x, y, gridSize);

				// if there is not already an arraylist for this region id, create it
				if ( !regionGroupToIndex.containsKey(id) ) {
					regionGroupToIndex.put( id, regions.size() );	// value = ArrayList index
					regions.add( new SocintPolygon(id) );
				} 

				// add point to its list
				regions.get( regionGroupToIndex.get(id) ).getVertices().add( point );

				lineNum++;
			}
		} catch(ParseException ex) {
			throw ex;
		} finally {
			if(scan != null) {scan.close();}
		}
	}	

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.feature_vector.cpd1.FeatureVectorCsv#toKml()
	 */
	@Override
	public SocintPolygonKmlParser toKml() {
		return new SocintPolygonKmlParser( this );
	}

	public static void main(String[] args) {
		SocintPolygonCsvParser regionParser;
		try {
			regionParser = new SocintPolygonCsvParser("data/Phase_1_CPD/Examples/regions.csv");
			System.out.println(regionParser.toKml().getKmlString());			
		} catch (Exception e) {
			e.printStackTrace();
		}			
	}
}