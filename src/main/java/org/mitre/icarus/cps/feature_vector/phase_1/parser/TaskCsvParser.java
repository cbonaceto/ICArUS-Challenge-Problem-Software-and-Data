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
import java.util.Scanner;

import org.mitre.icarus.cps.feature_vector.phase_1.GridLocation2D;
import org.mitre.icarus.cps.feature_vector.phase_1.GridSize;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupAttack;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupCenter;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;
import org.mitre.icarus.cps.feature_vector.phase_1.HumintType;
import org.mitre.icarus.cps.feature_vector.phase_1.ImintType;
import org.mitre.icarus.cps.feature_vector.phase_1.LocationIntelReport;
import org.mitre.icarus.cps.feature_vector.phase_1.MovintType;
import org.mitre.icarus.cps.feature_vector.phase_1.RoadMarker;
import org.mitre.icarus.cps.feature_vector.phase_1.SigintType;

/**
 * Parses task CSV feature vector files.
 * 
 * @author Lily Wong
 *
 */
public class TaskCsvParser extends FeatureVectorCsvParser {
	
	/* Categorization of all entries in this Task */
	/** The attacks. */
	private ArrayList<GroupAttack> attacks = new ArrayList<GroupAttack>();
	
	/** The centers. */
	private ArrayList<GroupCenter> centers = new ArrayList<GroupCenter>();
	
	/** The markers. */
	private ArrayList<RoadMarker> markers  = new ArrayList<RoadMarker>();
	
	/** Point of comparison for distances, for tasks with INTs */
	private GridLocation2D sourcePoint;
	private boolean isSourcePoint = true;
	
	/** List of all points in the order that they are specified for distance calculation **/
	private ArrayList<GridLocation2D> points = new ArrayList<GridLocation2D>();
	
	/**
	 * Instantiates a new task csv.
	 *
	 * @param filepath the filepath
	 * @throws IOException
	 * @throws ParseException
	 */
	public TaskCsvParser(String filepath ) throws IOException, ParseException {
		super(filepath);
		parseCsv();
	}
	
	/**
	 * Instantiates a new task csv from URL.
	 *
	 * @param url the url
	 * @throws IOException
	 * @throws ParseException
	 */
	public TaskCsvParser(URL url) throws IOException, ParseException {
		super( url );
		parseCsv();
	}
	
	/**
	 * Instantiates a new task csv from URL.
	 *
	 * @param url the url
	 * @throws IOException
	 * @throws ParseException
	 */
	public TaskCsvParser(URL url, GridSize gridSize) throws IOException, ParseException {
		super( url, gridSize );
		parseCsv();
	}
	
	/**
	 * Instantiates a new task csv.
	 *
	 * @param filepath the filepath
	 * @param gridSize the grid size
	 * @throws IOException
	 * @throws ParseException
	 */
	public TaskCsvParser(String filepath, GridSize gridSize ) throws IOException, ParseException {
		super(filepath, gridSize);
		parseCsv();
	}

	/* Accessor methods */
	/**
	 * Gets the attacks.
	 *
	 * @return the attacks
	 */
	public ArrayList<GroupAttack> getAttacks() {
		return attacks;
	}
	
	/**
	 * Gets the centers.
	 *
	 * @return the centers
	 */
	public ArrayList<GroupCenter> getCenters() {
		return centers;
	}
	
	/**
	 * Gets the markers.
	 *
	 * @return the markers
	 */
	public ArrayList<RoadMarker> getMarkers() {
		return markers;
	}
	
	public ArrayList<GridLocation2D> getPoints() {
		return points;
	}

	/**
	 * Get the number of features in this Task CSV.
	 *
	 * @return the number of features
	 */
	public int size() {
		return attacks.size() + centers.size() + markers.size();
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.feature_vector.phase_1.parser.FeatureVectorCsv#parseCsv()
	 */
	public void parseCsv() throws IOException, ParseException {
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
				if ( vals.length == 0 )
					return;

				if(vals.length < 4) {
					throw new ParseException("Error at line " + lineNum + " in " + csvFile.getFile() + " +: Must contain at least 4 columns (Object ID, Object Type, X, Y).", 
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

				if (featureType == null) {
					determineFeatureType( type );
				}					

				GroupType groupId = null;
				try { 
					groupId = GroupType.valueOf(id);
				} catch ( IllegalArgumentException e ) {
					// the object id is an int or is not one of the known groups		
					if(type.equalsIgnoreCase("group")) {
						throw new ParseException("Error at line " + lineNum + " in " + csvFile.getFile() + ": Group type must be one of " + GroupType.values() + ".", 
								lineNum);
					}
				}

				// coordinate point
				GridLocation2D point = new GridLocation2D(id, type, x, y, gridSize);
				points.add(point);

				// parse INT info, if any
				LocationIntelReport locationIntel = null;
				if ( hasInts() ) {
					GroupType gt = null;

					// Task 4, SocInt
					if ( numCols == 5 )	 { 
						try {
							gt = GroupType.valueOf(vals[4]);	
							locationIntel = new LocationIntelReport( gt );
						} catch ( IllegalArgumentException e ) {
							if(vals[4] != null && !vals[4].equals("[]")) {
								try {
									gt = GroupType.values()[Integer.parseInt(vals[4])-1];
									locationIntel = new LocationIntelReport( gt );
								} catch(Exception ex) {
									// unexpected socint value
									throw new ParseException("Error at line " + lineNum + " in " + csvFile.getFile() + 
											": Group type must be one of " + GroupType.createGroupListString() + ".", 
											lineNum);
								}
							}
						}	
					}
					// Tasks 5-7				
					else if ( numCols == 8 ) {					
						ImintType it  = null;
						MovintType mt = null;
						SigintType st = null;

						try {
							gt = GroupType.valueOf(vals[4]);
						} catch ( IllegalArgumentException e ) {
							if(vals[4] != null && !vals[4].equals("[]")) {
								try {
									gt = GroupType.values()[Integer.parseInt(vals[4])-1];
								} catch(Exception ex) {
									// unexpected socint value
									throw new ParseException("Error at line " + lineNum + " in " + csvFile.getFile() + 
											": Group type must be one of " + GroupType.createGroupListString() + ".", 
											lineNum);
								}
							}
						}					
						try {
							it = ImintType.getImintType( Integer.parseInt(vals[5]) );
						}  catch ( NumberFormatException e ) {
							// null INT value
							if(vals[5] != null && !vals[5].equals("[]")) {
								throw new ParseException("Error at line " + lineNum + " in " + csvFile.getFile() + ": Invalid IMINT type: " + vals[5] + ".", 
										lineNum);
							}
						}	
						try {
							mt = MovintType.getMovintType( Integer.parseInt(vals[6]) );
						} catch ( NumberFormatException e ) {
							// null INT value
							if(vals[6] != null && !vals[6].equals("[]")) {
								throw new ParseException("Error at line " + lineNum + " in " + csvFile.getFile() + ": Invalid MOVINT type: " + vals[6] + ".", 
										lineNum);
							}
						}	
						try {
							st = SigintType.getSigintType( Integer.parseInt(vals[7]) );						
						} catch ( NumberFormatException e ) {
							// null INT value
							if(vals[7] != null && !vals[7].equals("[]")) {
								throw new ParseException("Error at line " + lineNum + " in " + csvFile.getFile() + ": Invalid SIGINT type: " + vals[7] + ".", 
										lineNum);
							}
						}	

						locationIntel = new LocationIntelReport( gt, it, mt, st );
					}
					else {
						// throw exception, unexpected # of cols
						throw new ParseException("Error at line " + lineNum + " in " + csvFile.getFile() + ": Expected 4, 5, or 8 columns.", 
								lineNum);
					} 

					// first point in list
					if ( isSourcePoint ) {
						sourcePoint = point;
						isSourcePoint = false;
					} else {
						// calculate distances
						Integer distFromSource = (int) Math.round( SortingUtils.getDist(point, sourcePoint) );
						HumintType humint = new HumintType( sourcePoint, distFromSource );
						locationIntel.setHumintInfo(humint);
					}
				}

				// assign type of this GridLocation2D point, add to a list
				if ( point.isAttackLocation() || point.isRoadMarker() ) {
					GroupAttack ga = new GroupAttack( groupId, point, locationIntel );
					attacks.add(ga);
				}
				else if ( point.isGroupCenter() ) {
					GroupCenter gc = new GroupCenter( groupId, point, locationIntel );
					centers.add(gc);
				}			
				else {
					throw new ParseException("Error at line " + lineNum + " in " + csvFile.getFile() + ": Unrecognized object type: " + type + ".", 
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
	 * @see org.mitre.icarus.cps.feature_vector.cpd1.FeatureVectorCsv#toKml()
	 */
	@Override
	public TaskKmlParser toKml() {
		return new TaskKmlParser( this );
	}	
}