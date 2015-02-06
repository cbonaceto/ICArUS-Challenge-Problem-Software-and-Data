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

import org.mitre.icarus.cps.feature_vector.phase_1.GridLocation2D;
import org.mitre.icarus.cps.feature_vector.phase_1.GridSize;
import org.mitre.icarus.cps.feature_vector.phase_1.HumintType;
import org.mitre.icarus.cps.feature_vector.phase_1.Phase1Feature;

/**
 * Parses distance CSV files.
 * 
 * @author Lily Wong
 *
 */
public class DistanceCsv extends FeatureVectorCsvParser {
	
	TaskCsvParser taskCsv; // to associate 
	
	HashMap<GridLocation2D,HumintType> humintObjects = new HashMap<GridLocation2D,HumintType>();
	
	ArrayList<Double> distances = new ArrayList<Double>();
	
	boolean isCenters;

	/* Creates associated TaskCsv feature objects */
	public DistanceCsv(String filepath, GridSize gridSize, TaskCsvParser taskCsv) throws IOException, ParseException {
		super(filepath, gridSize);
		this.taskCsv = taskCsv;
		parseCsv();
	}

	public DistanceCsv(String filepath, TaskCsvParser taskCsv) throws IOException, ParseException {
		super(filepath);
		this.taskCsv = taskCsv;
		parseCsv();
	}

	public DistanceCsv(URL url, GridSize gridSize, TaskCsvParser taskCsv) throws IOException, ParseException {
		super(url, gridSize);
		this.taskCsv = taskCsv;
		parseCsv();
	}
	
	/* Parses out distances only */
	public DistanceCsv(String filepath, GridSize gridSize) throws IOException, ParseException {
		super(filepath, gridSize);
		parseCsvSimple();
	}

	public DistanceCsv(String filepath) throws IOException, ParseException {
		super(filepath);
		parseCsvSimple();
	}

	public DistanceCsv(URL url, GridSize gridSize) throws IOException, ParseException {
		super(url, gridSize);
		parseCsvSimple();
	}

	public void parseCsvSimple() throws IOException, ParseException {
		// parse the csv file
		Scanner scan = new Scanner( csvFile.openStream() );
		try {
			boolean isHeader = true;
			GridLocation2D sourcePoint = null;
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
				if ( vals.length == 0 )
					return;

				if(vals.length != 3) {
					throw new ParseException("Error at line " + lineNum + " in " + csvFile.getFile() + " +: Must contain  3 columns (Object ID, Object Type, HUMINT).", 
							lineNum);
				}

				// 3 fields
				//String id 	= vals[0];
				//String type = vals[1];
				int humintDistance = -1;
				try {
					humintDistance = Integer.parseInt(vals[2]);
					distances.add(new Double(humintDistance));
				} catch(NumberFormatException e) {
					if ( sourcePoint != null )
						throw new ParseException("Error at line " + lineNum + " in " + csvFile.getFile() + " +: HUMINT value must be a number.", 
								lineNum);
				}
				lineNum++;
			}
		} catch(ParseException ex) {
			throw ex;
		} finally {
			if(scan != null) {scan.close();}
		}
	}
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.feature_vector.phase_1.parser.FeatureVectorCsv#parseCsv()
	 */
	@Override
	public void parseCsv() throws IOException, ParseException {
		// parse the csv file
		Scanner scan = new Scanner( csvFile.openStream() );
		try {
			boolean isHeader = true;
			GridLocation2D sourcePoint = null;
			int lineNum = 1;
			int index = 0;
			ArrayList<? extends Phase1Feature> featureList = null;
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

				if(vals.length != 3) {
					throw new ParseException("Error at line " + lineNum + " in " + csvFile.getFile() + " +: Must contain  3 columns (Object ID, Object Type, HUMINT).", 
							lineNum);
				}

				// 3 fields
				//String id 	= vals[0];
				String type = vals[1];
				int humintDistance = -1;
				try {
					humintDistance = Integer.parseInt(vals[2]);
					distances.add(new Double(humintDistance));
				} catch(NumberFormatException e) {
					if ( sourcePoint != null )
						throw new ParseException("Error at line " + lineNum + " in " + csvFile.getFile() + " +: HUMINT value must be a number.", 
								lineNum);
					else {
						// determine if source point type is group or location
						if ( type.equals("Location") ) {
							sourcePoint = taskCsv.getAttacks().get(0).getLocation();
							featureList = taskCsv.getCenters();
							isCenters = true;
						} else if ( type.equals("Group") ) {
							sourcePoint = taskCsv.getCenters().get(0).getLocation();
							featureList = taskCsv.getAttacks();
							isCenters = false;
						}

						continue;
					}
				}

				HumintType humint = new HumintType(sourcePoint, humintDistance);

				// disregard header and source point
				Phase1Feature f = featureList.get(index++);
				GridLocation2D g = f.getLocation();

				humintObjects.put(g, humint);

				lineNum++;
			}
		} catch(ParseException ex) {
			throw ex;
		} finally {
			if(scan != null) {scan.close();}
		}
	}

	public HashMap<GridLocation2D, HumintType> getHumintObjects() {
		return humintObjects;
	}

	public ArrayList<Double> getDistances() {
		return distances;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.feature_vector.phase_1.parser.FeatureVectorCsv#toKml()
	 */
	@Override
	public FeatureVectorKmlParser toKml() {
		//Does nothing, there is no KML format for distance files.
		return null;
	}
}