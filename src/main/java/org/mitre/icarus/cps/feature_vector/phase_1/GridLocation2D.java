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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.feature_vector.phase_1.road_network.IntersectionPoint;

import de.micromata.opengis.kml.v_2_2_0.Coordinate;

/**
 * Specifies the (x, y) or (lat, lon) of a location.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="GridLocation2D", namespace="IcarusCPD_1",
		propOrder={"locationId", "x", "y", "lat", "lon"})
public class GridLocation2D implements Comparable<GridLocation2D> {
	
	/** The Constant EARTH_RADIUS_MILES. */
	public static final double EARTH_RADIUS_MILES = 3959;		
	
	/** The location ID. */
	protected String locationId;
	
	/** The location type */
	protected String type;
	
	/** X coordinate. */
	@XmlTransient
	public Double x;
	
	/** Y coordinate. */
	@XmlTransient
	public Double y;
	
	/** Latitude (degrees). */
	@XmlTransient
	public Double lat;
	
	/** Longitude (degrees). */
	@XmlTransient
	public Double lon;
	
	@XmlTransient
	public GridSize gridSize;

	/**
	 * Instantiates an empty new GridLocation2D.
	 */
	public GridLocation2D() {}	
	
	/**
	 * Instantiates a new GridLocation2D with only the specified ID.
	 *
	 * @param locationId the location id
	 */
	public GridLocation2D(String locationId) {
		this.locationId = locationId;
	}
	
	/**
	 * Copy constructor.
	 * 
	 * @param copy the GridLocation2D object to make a copy of
	 */
	public GridLocation2D(GridLocation2D copy) {
		if(copy != null) {
			this.locationId = copy.locationId;
			this.type = copy.type;		
			this.x = copy.x;
			this.y = copy.y;
			this.lat = copy.lat;
			this.lon = copy.lon;
			if(copy.gridSize != null) {
				this.gridSize = copy.gridSize.clone();
			}
		}
	}
	
	/**
	 * Constructor with only grid coordinates.
	 *
	 * @param x the x
	 * @param y the y
	 */
	public GridLocation2D(double x, double y) {
		this(null, x, y);
	}
	
	/**
	 * Grid coordinate constructor with an ID.
	 *
	 * @param id the id
	 * @param x the x
	 * @param y the y
	 */
	public GridLocation2D( String id, double x, double y ) {
		this( id, null, x, y);
	}	
	
	/**
	 * Full grid coordinate constructor.
	 *
	 * @param id the id
	 * @param type the type
	 * @param x the x
	 * @param y the y
	 */
	public GridLocation2D( String id, String type, double x, double y ) {
		this.locationId = id;
		this.type = type;
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Full grid coordinate constructor with specified GridSize.
	 *
	 * @param id the id
	 * @param type the type
	 * @param x the x
	 * @param y the y
	 * @param gridSize the grid size
	 */
	public GridLocation2D( String id, String type, double x, double y, GridSize gridSize ) {
		this.locationId = id;
		this.type = type;
		this.x = x;
		this.y = y;
		this.lat = (gridSize.getMilesPerGridUnit() * (y/EARTH_RADIUS_MILES) * (180/Math.PI)) + gridSize.getBottomLeftLat();
		this.lon = (gridSize.getMilesPerGridUnit() * (x/EARTH_RADIUS_MILES) * (180/Math.PI)) + gridSize.getBottomLeftLon();
		this.gridSize = gridSize;
	}
	
	/**
	 * Full LatLon constructor for parsing KML.
	 *
	 * @param id the id
	 * @param type the type
	 * @param coordinate the coordinate
	 * @param gridSize the grid size
	 */
	public GridLocation2D(String id, String type, Coordinate coordinate, GridSize gridSize ) {
		this.locationId = id;
		this.type = type;
		this.x = lonToX( coordinate, gridSize );
		this.y = latToY( coordinate, gridSize );
		this.lat = coordinate.getLatitude();
		this.lon = coordinate.getLongitude();
		this.gridSize = gridSize;
	}
	
	/* Helper functions for constructor for parsing KML */
	/**
	 * Lon to x. Rounds to nearest whole double.
	 *
	 * @param coordinate JaK KML coordinate object
	 * @param gridSize GridSize object containing a BottomLeftLon and milesPerGridUnit
	 * @return the double
	 */
	private double lonToX(Coordinate coordinate, GridSize gridSize ) {
		return 1.0*Math.round( Math.PI * EARTH_RADIUS_MILES * ( coordinate.getLongitude() - gridSize.getBottomLeftLon() ) / 
				(180 * gridSize.getMilesPerGridUnit()) );
	}
	
	/**
	 * Lat to y. Rounds to nearest whole double.
	 *
	 * @param coordinate the coordinate
	 * @param gridSize the grid size
	 * @return the double
	 */
	private double latToY( Coordinate coordinate, GridSize gridSize ) {
		return  1.0*Math.round( Math.PI * EARTH_RADIUS_MILES * ( coordinate.getLatitude() - gridSize.getBottomLeftLat() ) / 
				(180 * gridSize.getMilesPerGridUnit()) );
	}
	
	/**
	 * Gets the location id.
	 *
	 * @return the location id
	 */
	@XmlAttribute(name="locationId")
	public String getLocationId() {
		return locationId;
	}

	/**
	 * Sets the location id.
	 *
	 * @param locationId the new location id
	 */
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}
	
	/**
	 * Update location.
	 *
	 * @param newLocation the new location
	 */
	public void updateLocation(GridLocation2D newLocation) {
		this.x = newLocation.x;
		this.y = newLocation.y;
		this.lat = newLocation.lat;
		this.lon = newLocation.lon;
	}
	
	/* Only 2 types in the Phase 1 spec */
	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Checks if is attack location.
	 *
	 * @return true, if is attack location
	 */
	public boolean isAttackLocation() {
		if ( type.equals("Location") ) return true;
		return false;
	}
	
	/**
	 * Checks if is group center.
	 *
	 * @return true, if is group center
	 */
	public boolean isGroupCenter() {
		if ( type.equals("Group") ) return true;
		return false;
	}
	
	/**
	 * Checks if is road marker.
	 *
	 * @return true, if is road marker
	 */
	public boolean isRoadMarker() {
		try {			
			Integer.parseInt(locationId);
			return true;
		} catch ( NumberFormatException e ) {
			return false;
		}		
	}

	/**
	 * Gets the x.
	 *
	 * @return the x
	 */
	@XmlAttribute(name="x", required=false)
	public Double getX() {
		return x;
	}

	/**
	 * Sets the x.
	 *
	 * @param x the new x
	 */
	public void setX(Double x) {
		this.x = x;
	}

	/**
	 * Gets the y.
	 *
	 * @return the y
	 */
	@XmlAttribute(name="y", required=false)
	public Double getY() {
		return y;
	}

	/**
	 * Sets the y.
	 *
	 * @param y the new y
	 */
	public void setY(Double y) {
		this.y = y;
	}




	/**
	 * Gets the lat.
	 *
	 * @return the lat
	 */
	@XmlAttribute(name="lat", required=false)
	public Double getLat() {
		return lat;
	}

	/**
	 * Sets the lat.
	 *
	 * @param lat the new lat
	 */
	public void setLat(Double lat) {
		this.lat = lat;
	}

	/**
	 * Gets the lon.
	 *
	 * @return the lon
	 */
	@XmlAttribute(name="lon", required=false)
	public Double getLon() {
		return lon;
	}

	/**
	 * Sets the lon.
	 *
	 * @param lon the new lon
	 */
	public void setLon(Double lon) {
		this.lon = lon;
	}

	public GridSize getGridSize() {
		return gridSize;
	}


	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	/* Comparison with respect to distance from x-axis then distance from y-axis */
	public int compareTo( GridLocation2D o ) {
		// distance from origin
//		if ( this.x < o.x && this.y <= o.y )
//			return -1;
//		else if ( this.x == o.x && this.y == o.y )
//			return 0;
//		else
//			return 1;
		
		// sweep line algorithm
		// x-axis, then y-axis
		if ( this.x.doubleValue() < o.x.doubleValue() )
			return -1;
		else if ( this.x.doubleValue() == o.x.doubleValue() && this.y.doubleValue() < o.y.doubleValue() )	
			return -1;
		else if ( this.x.doubleValue() == o.x.doubleValue() && this.y.doubleValue() == o.y.doubleValue())
			return 0;
		else
			return 1;
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof GridLocation2D) {
			GridLocation2D o = (GridLocation2D) obj;
			result = (this.x.doubleValue() == o.x.doubleValue() && this.y.doubleValue() == o.y.doubleValue());
		}
		else if (obj instanceof IntersectionPoint) {
			IntersectionPoint o = (IntersectionPoint) obj;
			result = (this.x.doubleValue() == o.x.doubleValue() && this.y.doubleValue() == o.y.doubleValue());
		}
		return result;
	}


	@Override
	public int hashCode() {
		return (41 * (41 + x.hashCode()) +  y.hashCode());
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GridLocation2D [locationId=" + locationId + ", type=" + type
				+ ", x=" + x + ", y=" + y + ", lat=" + lat + ", lon=" + lon
				+ "]";
	}
	
	/**
	 * To kml coordinate.
	 *
	 * @return the KML coordinate
	 */
	public Coordinate toKmlCoordinate() {
		return new Coordinate(lon, lat);
	}

	@Override
	protected GridLocation2D clone()  {
		return new GridLocation2D(this);
	}	
}