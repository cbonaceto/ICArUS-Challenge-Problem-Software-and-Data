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

import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.feature_vector.phase_1.road_network.RoadLineSegment;

import de.micromata.opengis.kml.v_2_2_0.Coordinate;

/**
 * Defines a road.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="Road", namespace="IcarusCPD_1")
public class Road {
	
	/** Road object ID. */
	private String id;	
	
	/** The road traffic density. */
	private MovintType trafficDensity;
	
	/** The road coordinates. */
	private ArrayList<GridLocation2D> vertices;
	
	/** The line segments. */
	private ArrayList<RoadLineSegment> lineSegments;
	
	/** List of points to be displayed */
	ArrayList<GridLocation2D> smoothPoints;
	
	/**
	 * Instantiates an empty new road.
	 */
	public Road() {
		this(null, null);
	}
	
	/**
	 * Instantiates a new road.
	 *
	 * @param id the id
	 */
	public Road( String id ) {
		this(id, null);
	}
	
	/**
	 * Instantiates a new road.
	 *
	 * @param id the id
	 * @param trafficDensity the traffic density
	 */
	public Road(String id, MovintType trafficDensity) {
		this.id = id;
		this.trafficDensity = trafficDensity;
		vertices = new ArrayList<GridLocation2D>();
		lineSegments = new ArrayList<RoadLineSegment>();
		smoothPoints = new ArrayList<GridLocation2D>();
	}
	
	/**
	 * Copy constructor. Does a shallow copy on the vertices. *
	 *
	 * @param road the road to copy
	 */
	public Road(Road road) {
		this.id = road.id;		
		this.trafficDensity = road.trafficDensity;
		this.vertices = road.vertices;
		this.lineSegments = road.lineSegments;
		this.smoothPoints = road.smoothPoints;
	}

	/**
	 * Checks if the specified point is within radius units of a point on the road.
	 *
	 * @param target the target
	 * @param radius the radius
	 * @return true, if is point on road
	 */
	public boolean isPointOnRoad( GridLocation2D target, double radius ) {
		for ( GridLocation2D vertex : vertices ) {
			if ( Math.abs(target.getX() - vertex.getX()) <= radius
			  && Math.abs(target.getY() - vertex.getY()) <= radius) 
				return true;
		}
		return false;
	}
	
	/**
	 * Get the the closest point on the road that is within radius units of the specified point.
	 * 
	 * @param target the target point
	 * @param radius the radius 
	 * @return the closest point, or null if no point was within radius units of a point on the road
	 */
	public GridLocation2D getClosestPoint( GridLocation2D target, double radius ) {
		double shortestDist = Double.MAX_VALUE;
		GridLocation2D closest = null;		
		for ( GridLocation2D vertex : vertices ) {
			if ( Math.abs(target.getX() - vertex.getX()) <= radius
					&& Math.abs(target.getY() - vertex.getY()) <= radius ) {
				double dist = Math.sqrt(Math.pow(target.x - vertex.x, 2) + Math.pow(target.y - vertex.y, 2));
				if(dist < shortestDist) {				
					shortestDist = dist;
					closest = vertex;
				}
			}
		}
		if(closest == null) {
			return null;
		}
		return new GridLocation2D(closest);
	}
	
	/**
	 * Get the the closest point on the road to the specified point.
	 * 
	 * @param target the target point
	 * @return the closest point
	 */
	public GridLocation2D getClosestPoint( GridLocation2D target) {
		double shortestDist = Double.MAX_VALUE;
		GridLocation2D closest = null;
		for ( GridLocation2D vertex : vertices ) {
			//if ( Math.abs(target.getX() - vertex.getX()) <= radius
			//		&& Math.abs(target.getY() - vertex.getY()) <= radius ) {
			double dist = Math.sqrt(Math.pow(target.x - vertex.x, 2) + Math.pow(target.y - vertex.y, 2));
			if(dist < shortestDist) {				
				shortestDist = dist;
				closest = vertex;
			}
			//}
		}
		if(closest == null) {
			return null;
		}
		return new GridLocation2D(closest);
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	@XmlAttribute(name="id")
	public String getId() {
		return id;
	}
	
	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the traffic density.
	 *
	 * @return the traffic density
	 */
	@XmlAttribute(name="MOVINT")
	public MovintType getTrafficDensity() {
		return trafficDensity;
	}

	/**
	 * Sets the traffic density.
	 *
	 * @param trafficDensity the new traffic density
	 */
	public void setTrafficDensity(MovintType trafficDensity) {
		this.trafficDensity = trafficDensity;
	}

	/**
	 * Gets the number of vertices.
	 * 
	 * @return the number of vertices
	 */
	public int getVertexCount() {
		if(vertices != null) {
			return vertices.size();
		}
		return 0;
	}
	
	/**
	 * Gets the vertices.
	 *
	 * @return the vertices
	 */
	@XmlTransient
	public ArrayList<GridLocation2D> getVertices() {
		return vertices;
	}

	/**
	 * Sets the vertices.
	 *
	 * @param vertices the new vertices
	 */
	public void setVertices(ArrayList<GridLocation2D> vertices) {
		this.vertices = vertices;
	}	
	
	/**
	 * Gets the line segments.
	 *
	 * @return the line segments
	 */
	public ArrayList<RoadLineSegment> getLineSegments() {
		return lineSegments;
	}

	/**
	 * Gets the vertices as a KML coordinate list.
	 *
	 * @return the coordinate list
	 */
	public ArrayList<Coordinate> getCoordinateList() {
		Iterator<GridLocation2D> iter = vertices.iterator();
		ArrayList<Coordinate> coordinates = new ArrayList<Coordinate>();
		while ( iter.hasNext() ) {
			coordinates.add( iter.next().toKmlCoordinate() );
		}
		return coordinates;
	}
	
	/**
	 * Gets the list of points to be displayed.
	 * @return the smooth points
	 */
	public ArrayList<GridLocation2D> getSmoothPoints() {
		return smoothPoints;
	}

	@Override
	protected Road clone() throws CloneNotSupportedException {		
		return new Road(this);
	}	
}