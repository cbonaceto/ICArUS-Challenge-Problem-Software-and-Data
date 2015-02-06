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
package org.mitre.icarus.cps.feature_vector.phase_2;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

/**
 * Defines a rectangular geographic region.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="GeoArea", namespace="IcarusCPD_2")
@XmlSeeAlso({AreaOfInterest.class})
public class GeoArea extends AbstractFeature<de.micromata.opengis.kml.v_2_2_0.GroundOverlay> {	
	
	/** The grid square size (in miles) */	
	protected Double gridSquareSize_miles;
	
	/** The width of the area in grid units */
	protected Integer gridWidth;
	
	/** The height of the area in grid units */
	protected Integer gridHeight;
	
	/** The top left latitude of the area */
	protected Double north;
	
	/** The bottom right latitude of the area */
	protected Double south;
	
	/** The bottom right longitude of the area */
	protected Double east;
	
	/** The top left longitude of the area */
	protected Double west;	
	
	/**
	 * Construct an empty GeoArea.
	 */
	public GeoArea() {}
	
	/**
	 * Construct a GeoArea with the given bounding coordinates.
	 * 
	 * @param topLeftLon West coordinate (the top left longitude of the area)
	 * @param topLeftLat North coordinate (the top left latitude of the area)
	 * @param bottomRightLon East coordinate (the bottom right longitude of the area)
	 * @param bottomRightLat South coordinate (the bottom right latitude of the area)
	 */
	public GeoArea(Double topLeftLon, Double topLeftLat, Double bottomRightLon, Double bottomRightLat) {
		north = topLeftLat;
		south = bottomRightLat;
		east = bottomRightLon;
		west = topLeftLon;
	}
	
	/**
	 * Construct a GeoArea with the given bounding coordinates and grid size.
	 * 
	 * @param topLeftLon West coordinate (the top left longitude of the area)
	 * @param topLeftLat North coordinate (the top left latitude of the area)
	 * @param bottomRightLon East coordinate (the bottom right longitude of the area)
	 * @param bottomRightLat South coordinate (the bottom right latitude of the area)
	 * @param gridWidth the width of the area in grid units
	 * @param gridHeight the height of the area in grid units
	 */
	public GeoArea(Double topLeftLon, Double topLeftLat, Double bottomRightLon, Double bottomRightLat, 
			Integer gridWidth, Integer gridHeight) {
		//TODO: Compute gridSquareSize_miles;
		north = topLeftLat;
		south = bottomRightLat;
		east = bottomRightLon;
		west = topLeftLon;
		this.gridWidth = gridWidth;
		this.gridHeight = gridHeight;
		//this.gridSquareSize_miles = gridSquareSize_miles;
	}
	
	/**
	 * Construct a GeoArea with the given top left coordinate, grid size, and grid square size.
	 * 
	 * @param topLeftLon West coordinate (the top left longitude of the area)
	 * @param topLeftLat North coordinate (the top left latitude of the area)
	 * @param gridWidth the width of the area in grid units
	 * @param gridHeight the height of the area in grid units
	 * @param gridSquareSize_miles the grid square size (in miles)
	 */
	public GeoArea(Double topLeftLon, Double topLeftLat, Integer gridWidth, Integer gridHeight, 
			Double gridSquareSize_miles) {		
		//TODO: Compute bottom right lat/lon
		north = topLeftLat;
		west = topLeftLon;
		//south = bottomRightLat = ;
		//east = bottomRightLon = ;
		this.gridWidth = gridWidth;
		this.gridHeight = gridHeight;
		this.gridSquareSize_miles = gridSquareSize_miles;
	}
	
	/**
	 * Get the grid square size (in miles).
	 * 
	 * @return the grid square size (in miles)
	 */
	@XmlAttribute(name="gridSquareSize_miles")
	public Double getGridSquareSize_miles() {
		return gridSquareSize_miles;
	}

	/**
	 * Set the grid square size (in miles).
	 * 
	 * @param gridSquareSize_miles the grid square size (in miles)
	 */
	public void setGridSquareSize_miles(Double gridSquareSize_miles) {
		this.gridSquareSize_miles = gridSquareSize_miles;
	}

	/**
	 * Get the width of the area in grid units.
	 * 
	 * @return the width of the area in grid units
	 */
	@XmlAttribute(name="gridWidth")
	public Integer getGridWidth() {
		return gridWidth;
	}

	/**
	 * Set the width of the area in grid units.
	 * 
	 * @param gridWidth the width of the area in grid units
	 */
	public void setGridWidth(Integer gridWidth) {
		this.gridWidth = gridWidth;
	}

	/**
	 * Get the height of the area in grid units.
	 * 
	 * @return the height of the area in grid units
	 */
	@XmlAttribute(name="gridHeight")
	public Integer getGridHeight() {
		return gridHeight;
	}

	/**
	 * Set the height of the area in grid units.
	 * 
	 * @param gridHeight the height of the area in grid units
	 */
	public void setGridHeight(Integer gridHeight) {
		this.gridHeight = gridHeight;
	}	

	/**
	 * Get the west coordinate (the top left longitude of the area).
	 * 
	 * @return the west coordinate (the top left longitude of the area)
	 */
	@XmlAttribute(name="topLeftLon")
	public Double getTopLeftLon() {
		return west;
	}

	/**
	 * Set the west coordinate (the top left longitude of the area).
	 * 
	 * @param topLeftLon the west coordinate (the top left longitude of the area)
	 */ 
	public void setTopLeftLon(Double topLeftLon) {
		west = topLeftLon;
	}

	/**
	 * Get the north coordinate (the top left latitude of the area).
	 * 
	 * @return the north coordinate (the top left latitude of the area)
	 */
	@XmlAttribute(name="topLeftLat")
	public Double getTopLeftLat() {
		return north;
	}

	/**
	 * Set the north coordinate (the top left latitude of the area)
	 * 
	 * @param topLeftLat the north coordinate (the top left latitude of the area)
	 */
	public void setTopLeftLat(Double topLeftLat) {
		north = topLeftLat;
	}

	/**
	 * Get the south coordinate (the bottom right latitude of the area).
	 * 
	 * @return the south coordinate (the bottom right latitude of the area)
	 */
	@XmlAttribute(name="bottomRightLat")
	public Double getBottomRightLat() {
		return south;
	}

	/**
	 * Set the south coordinate (the bottom right latitude of the area).
	 * 
	 * @param bottomRightLat the south coordinate (the bottom right latitude of the area)
	 */
	public void setBottomRightLat(Double bottomRightLat) {
		south = bottomRightLat;
	}

	/**
	 * Get the east coordinate (the bottom right longitude of the area).
	 * 
	 * @return the east coordinate (the bottom right longitude of the area)
	 */
	@XmlAttribute(name="bottomRightLon")
	public Double getBottomRightLon() {
		return east;
	}

	/**
	 * Set the east coordinate (the bottom right longitude of the area).
	 * 
	 * @param bottomRightLon the east coordinate (the bottom right longitude of the area)
	 */
	public void setBottomRightLon(Double bottomRightLon) {
		east = bottomRightLon;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.feature_vector.phase_2.IFeature#getKMLGeometry()
	 */
	@Override
	public de.micromata.opengis.kml.v_2_2_0.GroundOverlay getKMLGeometry() {
		de.micromata.opengis.kml.v_2_2_0.LatLonBox latLonBox = 
				new de.micromata.opengis.kml.v_2_2_0.LatLonBox();
		latLonBox.setEast(east);
		latLonBox.setNorth(north);
		latLonBox.setSouth(south);
		latLonBox.setWest(west);		
		de.micromata.opengis.kml.v_2_2_0.GroundOverlay groundOverlay = 
				new de.micromata.opengis.kml.v_2_2_0.GroundOverlay();
		groundOverlay.setLatLonBox(latLonBox);
		return groundOverlay;
	}	
}