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
package org.mitre.icarus.cps.feature_vector.phase_1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;

import org.mitre.icarus.cps.feature_vector.HashMapCache;
import org.mitre.icarus.cps.feature_vector.phase_1.parser.DistanceCsv;
import org.mitre.icarus.cps.feature_vector.phase_1.parser.SocintPolygonCsvParser;
import org.mitre.icarus.cps.feature_vector.phase_1.parser.SocintPolygonKmlParser;
import org.mitre.icarus.cps.feature_vector.phase_1.parser.RoadCsvParser;
import org.mitre.icarus.cps.feature_vector.phase_1.parser.RoadKmlParser;
import org.mitre.icarus.cps.feature_vector.phase_1.parser.SocintOverlayCsvParser;
import org.mitre.icarus.cps.feature_vector.phase_1.parser.SocintOverlayKmlParser;
import org.mitre.icarus.cps.feature_vector.phase_1.parser.TaskCsvParser;
import org.mitre.icarus.cps.feature_vector.phase_1.parser.TaskKmlParser;

/**
 * Manages the caching and loading of feature vectors for tasks, roads, and regions.  
 * This is the primary class to use for parsing CSV and KML feature vector files.
 * The actual parsers are located in the parsers package.
 * 
 * @author CBONACETO
 *
 */
public class FeatureVectorManager {
	
	/** Cached task data. Key is URL to the feature vector file defining the tasks,
	 * value is container for any group centers or attack locations for the task */
	protected HashMapCache<String, TaskData> tasks;
	
	/** Cached roads. Key is URL to the feature vector file defining the roads,
	 * value is list of roads in that file */
	protected HashMapCache<String, ArrayList<Road>> roads;
	
	/** Cached SOCINT regions represented as polygons.  Key is URL to the feature vector file defining the regions,
	 * value is list of regions in that file. */
	protected HashMapCache<String, ArrayList<SocintPolygon>> regions;
	
	/** Cached SOCINT regions represented as an overlay.  Key is URL to the feature vector file defining the regions,
	 * value SOCINT regions overlay for that file. */
	protected HashMapCache<String, SocintOverlay> regionOverlays;
	
	/** Singleton instance of the feature vector manager */
	private static final FeatureVectorManager instance = new FeatureVectorManager();
	
	/** Set to false when building distributions */
	private static boolean outputKml = false;
	
	/**
	 * Protected constructor to create the singleton instance.
	 */
	protected FeatureVectorManager() {
		tasks = new HashMapCache<String, TaskData>(20, 100);
		roads = new HashMapCache<String, ArrayList<Road>>(20, 50);
		regions = new HashMapCache<String, ArrayList<SocintPolygon>>(4, 8);
		regionOverlays = new HashMapCache<String, SocintOverlay>(4, 20);
	}
	
	/**
	 * Get the singleton instance of the feature vector manager.
	 * 
	 * @return the instance
	 */
	public static FeatureVectorManager getInstance() {
		return instance;
	}
	
	/**
	 * Clears all cached data.
	 */
	public void clearAllCachedData() {
		clearTasksCache();
		clearRoadsCache();
		clearRegionsCache();
	}
	
	/**
	 * Clears cached task data.
	 */
	public void clearTasksCache() {
		tasks.clear();
	}
	
	/**
	 * Clear cached roads data.
	 */
	public void clearRoadsCache() {
		roads.clear();
	}
	
	/**
	 * Clear cached regions data.
	 */
	public void clearRegionsCache() {
		regions.clear();
		regionOverlays.clear();
	}
	
	/**
	 * Get the task data contained in a Task 1-7 feature vector file.
	 * 
	 * @param featureVectorUrl the relative URL to the feature vector file
	 * @param baseUrl base URL used to resolve the relative URL
	 * @param gridSize the grid size to use
	 * @return a TaskData instance with group centers and/or attack locations
	 * @throws IOException
	 * @throws ParseException
	 */
	public TaskData getTaskData(String featureVectorUrl, URL baseUrl, GridSize gridSize) throws IOException, ParseException {
		return getTaskData(new URL(baseUrl, featureVectorUrl), null, gridSize);
	}
	
	/**
	 * Get the task data contained in a Task 1-7 feature vector file.
	 * 
	 * @param featureVectorUrl the relative URL to the feature vector file
	 * @param distanceVectorUrl the relative URL to the distances file (if any)
	 * @param baseUrl base URL used to resolve the relative URL
	 * @param gridSize the grid size to use
	 * @return a TaskData instance with group centers and/or attack locations
	 * @throws IOException
	 * @throws ParseException
	 */
	public TaskData getTaskData(String featureVectorUrl, String distanceVectorUrl, URL baseUrl, GridSize gridSize) throws IOException, ParseException {
		return getTaskData(new URL(baseUrl, featureVectorUrl), 
				(distanceVectorUrl != null) ? new URL(baseUrl, distanceVectorUrl) : null,
				gridSize);
	}
	
	/**
	 * Get the task data contained in a Task 1-7 feature vector file.
	 * 
	 * @param featureVectorUrl the URL to the feature vector file
	 * @param gridSize the grid size to use
	 * @return a TaskData instance with group centers and/or attack locations
	 * @throws IOException
	 * @throws ParseException
	 */
	public TaskData getTaskData(URL featureVectorUrl, GridSize gridSize) throws IOException, ParseException {
		return getTaskData(featureVectorUrl, null, gridSize);
	}
	
	/**
	 * Get the task data contained in a Task 1-7 feature vector file.
	 * 
	 * @param featureVectorUrl the URL to the feature vector file
	 * @param distanceVectorUrl the URL to a file containing location distances
	 * @param gridSize the grid size to use
	 * @return a TaskData instance with group centers and/or attack locations
	 * @throws IOException
	 * @throws ParseException
	 */
	public TaskData getTaskData(URL featureVectorUrl, URL distanceVectorUrl, GridSize gridSize) throws IOException, ParseException {
		TaskData taskData = null;
		if(tasks.containsKey(featureVectorUrl.toString())) {
			taskData = tasks.get(featureVectorUrl.toString());
		}
		if(taskData == null) {
			if(featureVectorUrl.getFile() != null) {
				String fileName = featureVectorUrl.getFile().toLowerCase();
				if(fileName.endsWith(".csv")) {
					//Parse CSV version of the task feature vector file
					TaskCsvParser parser = new TaskCsvParser(featureVectorUrl, gridSize); 
					taskData = new TaskData(parser.getAttacks(), parser.getCenters());
					tasks.put(featureVectorUrl.toString(), taskData);
					
					//Load distances from distance file if present
					if(taskData != null && distanceVectorUrl != null) {
						DistanceCsv distanceParser = new DistanceCsv(distanceVectorUrl, gridSize, parser);		
						//Convert distances to miles
						if(distanceParser.getDistances() != null && !distanceParser.getDistances().isEmpty()) {
							ArrayList<Double> distances = distanceParser.getDistances();
							int index = 0;							
							for(Double distance : distanceParser.getDistances()) {
								distances.set(index, gridSize.toMiles(distance));
								index++;
							}
						}
						taskData.setDistances(distanceParser.getDistances());
					}				
					
					//Save a KML version of the task CSV file
					if(outputKml) {
						String kml = parser.toKml().getKmlString();
						String kmlFileName = featureVectorUrl.getFile();
						kmlFileName = kmlFileName.substring(0, kmlFileName.length()-3);
						kmlFileName = kmlFileName.concat("kml");
						URL kmlFileUrl = new File(kmlFileName).toURI().toURL();
						FileWriter fw;
						try {
							fw = new FileWriter(new File(kmlFileUrl.toURI()));
							fw.write(kml);
							fw.flush();
							fw.close();
						} catch (URISyntaxException e) {
							e.printStackTrace();
						}
					}
				}
				else if(fileName.endsWith(".kml")) {
					//Parse KML version of the task feature vector file
					TaskKmlParser parser = new TaskKmlParser(featureVectorUrl, gridSize); 
					taskData = new TaskData(parser.getAttacks(), parser.getCenters());
					tasks.put(featureVectorUrl.toString(), taskData);
				}
				else {
					throw new IOException("Error, feature vector URL does not contain a CSV or KML file.");	
				}	
			} else {
				throw new IOException("Error, feature vector URL does not contain a file.");
			}				
		}
		return taskData;
	}

	/**
	 * Get the roads contained in a roads feature vector file.
	 * 
	 * @param roadsUrl relative URL to the roads file
	 * @param baseUrl base URL used to resolve the relative URL
	 * @param gridSize the grid size to use
	 * @return the roads
	 * @throws IOException
	 * @throws ParseException
	 */
	public ArrayList<Road> getRoads(String roadsUrl, URL baseUrl, GridSize gridSize) throws IOException, ParseException {
		return getRoads(new URL(baseUrl, roadsUrl), gridSize);
	}
	
	/**
	 * Get the roads contained in a roads feature vector file.
	 * 
	 * @param roadsUrl URL to the roads file
	 * @param gridSize the grid size to use
	 * @return the roads
	 * @throws IOException
	 * @throws ParseException
	 */
	public ArrayList<Road> getRoads(URL roadsUrl, GridSize gridSize) throws IOException, ParseException {
		ArrayList<Road> roadsList = null;
		if(roads.containsKey(roadsUrl.toString())) {
			roadsList = roads.get(roadsUrl.toString());
		}
		if(roadsList == null) {
			if(roadsUrl.getFile() != null) {
				String fileName = roadsUrl.getFile().toLowerCase();
				if(fileName.endsWith(".csv")) {
					//Parse CSV version of the roads file
					RoadCsvParser parser = new RoadCsvParser(roadsUrl, gridSize); 
					if(parser.getRoads() != null) {
						roads.put(roadsUrl.toString(), parser.getRoads());
					}
					
					//Save a KML version of the roads CSV file
					if(outputKml) {
						String kml = parser.toKml().getKmlString();
						String kmlFileName = roadsUrl.getFile();
						kmlFileName = kmlFileName.substring(0, kmlFileName.length()-3);
						kmlFileName = kmlFileName.concat("kml");
						URL kmlFileUrl = new File(kmlFileName).toURI().toURL();
						FileWriter fw;
						try {
							fw = new FileWriter(new File(kmlFileUrl.toURI()));
							fw.write(kml);
							fw.flush();
							fw.close();
						} catch (URISyntaxException e) {
							e.printStackTrace();
						}
					}					
					roadsList = parser.getRoads();
				}
				else if(fileName.endsWith(".kml")) {
					//Parse KML version of the roads file
					RoadKmlParser parser = new RoadKmlParser(roadsUrl, gridSize); 
					if(parser.getRoads() != null) {
						roads.put(roadsUrl.toString(), parser.getRoads());
					}
					roadsList = parser.getRoads();
				}
				else {
					throw new IOException("Error, roads URL does not contain a CSV or KML file.");	
				}				
			} else {
				throw new IOException("Error, roads URL does not contain a file.");
			}	
		}
		return roadsList;
	}
	
	/**
	 * Get the regions contained in a regions feature vector file.
	 * 
	 * @param regionsUrl relative URL to the regions file
	 * @param baseUrl the base URL used to resolve the relative URL
	 * @param gridSize the grid size to use
	 * @return the regions
	 * @throws IOException
	 * @throws ParseException
	 */
	public ArrayList<SocintPolygon> getRegions(String regionsUrl, URL baseUrl, GridSize gridSize) throws IOException, ParseException {
		return getRegions(new URL(baseUrl, regionsUrl), gridSize);
	}
	
	/**
	 * Get the regions contained in a regions feature vector file.
	 * 
	 * @param regionsUrl URL to the regions file
	 * @param gridSize the grid size to use
	 * @return the regions
	 * @throws IOException
	 * @throws ParseException
	 */
	public ArrayList<SocintPolygon> getRegions(URL regionsUrl, GridSize gridSize) throws IOException, ParseException {
		ArrayList<SocintPolygon> regionsList = null;
		if(regions.containsKey(regionsUrl.toString())) {
			regionsList = regions.get(regionsUrl.toString());
		}
		if(regionsList == null) {
			if(regionsUrl.getFile() != null) {
				String fileName = regionsUrl.getFile().toLowerCase();
				if(fileName.endsWith(".csv")) {
					//Parse CSV version of the regions file
					SocintPolygonCsvParser parser = new SocintPolygonCsvParser(regionsUrl, gridSize);
					if(parser.getRegions() != null) {
						regions.put(regionsUrl.toString(), parser.getRegions());
					}
					
					//Save a KML version of the regions CSV file
					if(outputKml) {
						String kml = parser.toKml().getKmlString();
						String kmlFileName = regionsUrl.getFile();
						kmlFileName = kmlFileName.substring(0, kmlFileName.length()-3);
						kmlFileName = kmlFileName.concat("kml");
						URL kmlFileUrl = new File(kmlFileName).toURI().toURL();
						FileWriter fw;
						try {
							fw = new FileWriter(new File(kmlFileUrl.toURI()));
							fw.write(kml);
							fw.flush();
							fw.close();
						} catch (URISyntaxException e) {
							e.printStackTrace();
						}
					}					
					regionsList = parser.getRegions();
				}
				else if(fileName.endsWith(".kml")) {
					//Parse KML version of the regions file
					SocintPolygonKmlParser parser = new SocintPolygonKmlParser(regionsUrl, gridSize); 
					if(parser.getRegions() != null) {
						regions.put(regionsUrl.toString(), parser.getRegions());
					}
					regionsList = parser.getRegions();
				}
				else {
					throw new IOException("Error, regions URL does not contain a CSV or KML file.");	
				}				
			} else {
				throw new IOException("Error, regions URL does not contain a file.");
			}				
		}
		return regionsList;
	}
	
	/**
	 * Get the regions overlay contained in a regions feature vector file.
	 * 
	 * @param regionsUrl relative URL to the regions file
	 * @param baseUrl the base URL used to resolve the relative URL
	 * @param gridSize the grid size to use
	 * @return the region overlay
	 * @throws IOException
	 * @throws ParseException
	 */
	public SocintOverlay getRegionsOverlay(String regionsUrl, URL baseUrl, GridSize gridSize) throws IOException, ParseException {
		return getRegionsOverlay(new URL(baseUrl, regionsUrl), gridSize);
	}
	
	/**
	 * Get the regions overlay contained in a regions feature vector file.
	 * 
	 * @param regionsUrl URL to the regions file
	 * @param gridSize the grid size to use
	 * @return the region overlay
	 * @throws IOException
	 * @throws ParseException
	 */
	public SocintOverlay getRegionsOverlay(URL regionsUrl, GridSize gridSize) throws IOException, ParseException {
		SocintOverlay regionsOverlay = null;
		if(regionOverlays.containsKey(regionsUrl.toString())) {
			regionsOverlay = regionOverlays.get(regionsUrl.toString());
		} else {
			if(regionsUrl.getFile() != null) {
				String fileName = regionsUrl.getFile().toLowerCase();
				if(fileName.endsWith(".csv")) {
					//Parse CSV version of the regions file
					SocintOverlayCsvParser parser = new SocintOverlayCsvParser(regionsUrl, gridSize);
					if(parser.getSocintPts() != null) {
						regionOverlays.put(regionsUrl.toString(), parser.getSocintPts());
					}
					
					//Save a KML version of the regions CSV file
					if(outputKml) {
						String kml = parser.toKml().getKmlString();
						String kmlFileName = regionsUrl.getFile();
						kmlFileName = kmlFileName.substring(0, kmlFileName.length()-3);
						kmlFileName = kmlFileName.concat("kml");
						URL kmlFileUrl = new File(kmlFileName).toURI().toURL();
						FileWriter fw;
						try {
							fw = new FileWriter(new File(kmlFileUrl.toURI()));
							fw.write(kml);
							fw.flush();
							fw.close();
						} catch (URISyntaxException e) {
							e.printStackTrace();
						}
					}
					regionsOverlay = parser.getSocintPts();
				}
				else if(fileName.endsWith(".kml")) {
					//Parse KML version of the regions file
					SocintOverlayKmlParser parser = new SocintOverlayKmlParser(regionsUrl, gridSize); 
					if(parser.getSocintPts() != null) {
						regionOverlays.put(regionsUrl.toString(), parser.getSocintPts());
					}
					regionsOverlay = parser.getSocintPts();
				}
				else {
					throw new IOException("Error, regions URL does not contain a CSV or KML file.");	
				}				
			} else {
				throw new IOException("Error, regions URL does not contain a file.");
			}				
		}
		return regionsOverlay;
	}
}