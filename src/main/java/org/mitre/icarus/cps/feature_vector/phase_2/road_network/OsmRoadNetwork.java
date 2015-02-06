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
package org.mitre.icarus.cps.feature_vector.phase_2.road_network;

//import java.io.IOException;
//
//import org.mitre.icarus.cps.feature_vector.phase_2.coordinate.GeoCoordinate;
//
//import com.graphhopper.reader.OSMReader;
//import com.graphhopper.routing.DijkstraSimple;
//import com.graphhopper.routing.Path;
//import com.graphhopper.storage.Location2IDIndex;
//import com.graphhopper.util.CmdArgs;

/**
 * Road network distance calculation using graphhopper tools
 * to work with OpenStreetMap XML data.
 * 
 * Using graphhopper snapshot as of 12-4-12
 * https://github.com/graphhopper/graphhopper
 * 
 * NOTE: Commented out as road networks are not currently used.
 *       To use code, the imports must also be uncommented and 
 *       lib\graphhopper\graphhopper.jar must be added to the class pth.
 * 
 * @author LWONG
 */
public class OsmRoadNetwork {

//	private String osmFilepath;
//	private OSMReader osmReader;
//	private DijkstraSimple dijkstraSimple;
//	private Location2IDIndex idx;
//
//	/**
//	 * Default constructor: use only to target a prepared graph
//	 * in the default location ChallengeProblemSoftware/graph-gh/
//	 * @throws IOException
//	 */
//	public OsmRoadNetwork() throws IOException {
//		new OsmRoadNetwork(null);
//	}
//	
//	/**
//	 * Specify the full path of a .osm file.  
//	 * Looks for <osmFilename>-gh graph directory in same path
//	 * or creates the graph if it does not exist.
//	 * @param osmFilepath
//	 * @throws IOException
//	 */
//	public OsmRoadNetwork(String osmFilepath) throws IOException {
//		this.osmFilepath = osmFilepath;
//		initializeGraphForDijkstra();
//	}
//
//	/**
//	 * Initializes needed graphhopper structures.
//	 * 
//	 * @throws IOException
//	 */
//	private void initializeGraphForDijkstra() throws IOException {
//		// graphhopper arguments
//		// creates <osmFileName>-gh directory if not exists
//		// graph-gh is default if no osm file provided
//		CmdArgs cmdArgs = new CmdArgs();
//		if ( this.osmFilepath != null ) {
//			String[] values = new String[1];
//			values[0] = "osmreader.osm="+osmFilepath;
//			cmdArgs = CmdArgs.read(values);
//		} 
//
//		// initialize variables for dijkstra's
//		this.osmReader = OSMReader.osm2Graph(cmdArgs);
//		this.dijkstraSimple = new DijkstraSimple(osmReader.getGraph());
//		this.idx = osmReader.getLocation2IDIndex();
//	}
//
//	/**
//	 * Calculate the shortest distance in meters along a road network
//	 * using Dijkstra's shortest path algorithm.
//	 * @param from
//	 * @param to
//	 * @return
//	 */
//	protected double calcDistanceInMeters(GeoCoordinate from, GeoCoordinate to) {
//		return calcDistanceInMeters(from.getLat(), from.getLon(), 
//				to.getLat(), to.getLon());
//	}
//	
//	protected double calcDistanceInMeters(double fromLat, double fromLon, 
//			double toLat, double toLon)  {
//		// reset algorithm internals
//		dijkstraSimple.clear();
//		
//		int fromId = idx.findID(fromLat, fromLon);
//		int toId = idx.findID(toLat, toLon);
//		Path p = dijkstraSimple.calcPath(fromId, toId);
//		return p.distance();
//	}
}