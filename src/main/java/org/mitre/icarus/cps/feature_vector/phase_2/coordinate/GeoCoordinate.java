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
package org.mitre.icarus.cps.feature_vector.phase_2.coordinate;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 *  A coordinate specified as a (longitude, latitude) in degrees. The coordinate may also be specified
 *  by an (X,Y) point that defines the location of the coordinate relative to a bounding area.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name = "GeoCoordinate", namespace="IcarusCPD_2", propOrder = { "id", "lat", "lon", "x", "y"})
public class GeoCoordinate implements Coordinate {
	
	/** The coordinate ID */
	protected String id;
	
	/** Longitude of location (degrees) */
	protected Double lon;
	
	/** Latitude of location (degrees) */
	protected Double lat;
	
	/** Altitude of location (meters) */
	protected Double alt;
	
	/** X coordinate of location within a GeoArea */
	protected Double x;
	
	/** Y coordinate of location within a GeoArea */	
	protected Double y;
	
	/**
	 * Construct an empty GeoCoordinate.
	 */
	public GeoCoordinate() {}
	
	/**
	 * Construct a GeoCoordinate with the given longitude and latitude in degrees.
	 * 
	 * @param lon longitude of location (degrees)
	 * @param lat latitude of location (degrees)
	 */
	public GeoCoordinate(Double lon, Double lat) {
		this.lon = lon;
		this.lat = lat;
	}
	
	/**
	 * Construct a GeoCoordinate with the given longitude and latitude in degrees and
	 * altitude in meters.
	 * 
	 * @param lon longitude of location (degrees)
	 * @param lat latitude of location (degrees)
	 * @param alt altitude of location (meters)
	 */
	public GeoCoordinate(Double lon, Double lat, Double alt) {
		this.lon = lon;
		this.lat = lat;
		this.alt = alt;
	}	
	
	/**
	 * Construct a GeoCoordinate by parsing the given string containing a comma-separated list of values.
	 * 
	 * @param coordString a comma separated list of values: id, lon, lat, alt.
	 */
	public GeoCoordinate(String coordString) {
		if(coordString == null) {
			throw new IllegalArgumentException("Coordinates string cannot be null");
		}
		String[] coords = coordString.replaceAll(",\\s+", ",").trim().split(",");
		if((coords.length < 1) || (coords.length == 3) || (coords.length > 4)) {
			throw new IllegalArgumentException("Coordinates string must have 1, 3, or 4 comma separated values");
		}		
		id = coords[0];
		lon = Double.parseDouble((coords[1]));
		lat = Double.parseDouble((coords[2]));
		if(coords.length > 3) {
			alt = Double.parseDouble((coords[3]));
		}
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.feature_vector.phase_2.coordinate.Coordinate#getId()
	 */
	@XmlAttribute(name="id")
	public String getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.feature_vector.phase_2.coordinate.Coordinate#setId(java.lang.String)
	 */
	public void setId(String id) {
		this.id = id;
	}	
	
	/**
	 * Get the longitude of the location (degrees).
	 * 
	 * @return the longitude of the location (degrees)
	 */
	@XmlAttribute(name="lon")
	public Double getLon() {
		return lon;
	}

	/**
	 * Set the longitude of the location (degrees).
	 * 
	 * @param lon the longitude of the location (degrees)
	 */
	public void setLon(Double lon) {
		this.lon = lon;		
	}
	
	/**
	 * Get the latitude of the location (degrees).
	 * 
	 * @return the latitude of the location (degrees)
	 */
	@XmlAttribute(name="lat")
	public Double getLat() {
		return lat;
	}

	/**
	 * Set the latitude of the location (degrees).
	 * 
	 * @param lat the latitude of the location (degrees)
	 */
	public void setLat(Double lat) {
		this.lat = lat;	
	}
	
	/**
	 * Get the altitude of the location (meters).
	 * 
	 * @return the altitude of the location (meters)
	 */
	@XmlAttribute(name="alt")
	public Double getAlt() {
		return alt;
	}

	/**
	 * Set the altitude of the location (meters).
	 * 
	 * @param alt the altitude of the location (meters)
	 */
	public void setAlt(Double alt) {
		this.alt = alt;
	}	
	
	/**
	 * Get the X coordinate of the location relative to a bounding area.
	 * 
	 * @return the X coordinate of the location relative to a bounding area
	 */
	@XmlAttribute(name="x")
	public Double getX() {
		return x;
	}

	/**
	 * Set the X coordinate of the location relative to a bounding area.
	 * 
	 * @param x the X coordinate of the location relative to a bounding area
	 */
	public void setX(Double x) {
		this.x = x;
	}

	/**
	 * Get the Y coordinate of the location relative to a bounding area.
	 * 
	 * @return the Y coordinate of the location relative to a bounding area
	 */
	@XmlAttribute(name="y")
	public Double getY() {
		return y;
	}

	/**
	 * Set the Y coordinate of the location relative to a bounding area.
	 * 
	 * @param y the Y coordinate of the location relative to a bounding area
	 */
	public void setY(Double y) {
		this.y = y;
	}
	
	/**
	 * Get a KML coordinate corresponding to this coordinate.
	 * 
	 * @return a KML coordinate corresponding to this coordinate 
	 */
	public de.micromata.opengis.kml.v_2_2_0.Coordinate getKMLRepresentation() {
		return new de.micromata.opengis.kml.v_2_2_0.Coordinate(
				lon != null ? lon : 0, 
				lat != null ? lat : 0, 
				alt != null ? alt : 0);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GeoCoordinate [locationId=" + id 
				+ ", x=" + x + ", y=" + y + ", lat=" + lat + ", lon=" + lon
				+ "]";
	}	
}