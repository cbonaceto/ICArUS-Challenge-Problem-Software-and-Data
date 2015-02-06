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
import javax.xml.bind.annotation.XmlType;

/**
 * Contains grid size and scale information.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="GridSize", namespace="IcarusCPD_1", 
		propOrder={"gridWidth", "gridHeight", "milesPerGridUnit", 
		"bottomLeftLat", "bottomLeftLon"})
public class GridSize {

	/** The grid width. */
	protected Integer gridWidth;
	
	/** The grid height. */
	protected Integer gridHeight;	
	
	/** The grid scale (miles per grid unit). */
	protected Double milesPerGridUnit;
	
	/** The lat coord of the bottom left of the grid. */
	protected Double bottomLeftLat;
	
	/** The lon coord of the bottom left of the grid. */
	protected Double bottomLeftLon;
	
	/**
	 * Instantiates a new grid size with default values:
	 * gridWidth = gridHeight = 100, bottomLeftLat = bottomLeftLon = 0.0, and 
	 * milesPerGridUnit = 0.2
	 */
	public GridSize() {
		this(100, 100, 0.3D, 0.D, 0.D);
	}
	
	/**
	 * Instantiates a new grid size.
	 *
	 * @param gridWidth the grid width 
	 * @param gridHeight the grid height
	 */
	public GridSize(Integer gridWidth, Integer gridHeight) {
		this(gridWidth, gridHeight, 0.3D, 0.D, 0.D);
	}
	
	/**
	 * Copy constructor.
	 * 
	 * @param copy the GridSize object to make a copy of
	 */
	public GridSize(GridSize copy) {
		if(copy != null) {
			this.gridWidth = copy.gridWidth;
			this.gridHeight = copy.gridHeight;
			this.milesPerGridUnit = copy.milesPerGridUnit;
			this.bottomLeftLat = copy.bottomLeftLat;
			this.bottomLeftLon = copy.bottomLeftLon;
		}
	}

	/**
	 * Instantiates a new grid size with fully specified parameters.
	 *
	 * @param gridWidth the grid width
	 * @param gridHeight the grid height
	 * @param milesPerGridUnit number of miles per grid unit
	 * @param bottomLeftLat the bottom left latitude in degrees
	 * @param bottomLeftLon the bottom left longitude in degrees
	 */
	public GridSize(Integer gridWidth, Integer gridHeight,
			Double milesPerGridUnit, Double bottomLeftLat, Double bottomLeftLon) {
		this.gridWidth = gridWidth;
		this.gridHeight = gridHeight;
		this.milesPerGridUnit = milesPerGridUnit;
		this.bottomLeftLat = bottomLeftLat;
		this.bottomLeftLon = bottomLeftLon;
	}

	/**
	 * Gets the grid width.
	 *
	 * @return the grid width
	 */
	@XmlAttribute(name="gridWidth")
	public Integer getGridWidth() {
		return gridWidth;
	}

	/**
	 * Sets the grid width.
	 *
	 * @param gridWidth the new grid width
	 */
	public void setGridWidth(Integer gridWidth) {
		this.gridWidth = gridWidth;
	}

	/**
	 * Gets the grid height.
	 *
	 * @return the grid height
	 */
	@XmlAttribute(name="gridHeight")
	public Integer getGridHeight() {
		return gridHeight;
	}

	/**
	 * Sets the grid height.
	 *
	 * @param gridHeight the new grid height
	 */
	public void setGridHeight(Integer gridHeight) {
		this.gridHeight = gridHeight;
	}

	/**
	 * Gets the miles per grid unit.
	 *
	 * @return the miles per grid unit
	 */
	@XmlAttribute(name="milesPerGridUnit")
	public Double getMilesPerGridUnit() {
		return milesPerGridUnit;
	}

	/**
	 * Sets the miles per grid unit.
	 *
	 * @param milesPerGridUnit the new miles per grid unit
	 */
	public void setMilesPerGridUnit(Double milesPerGridUnit) {
		this.milesPerGridUnit = milesPerGridUnit;
	}
	
	
	/**
	 * Convert the given distance in grid units to miles. 
	 * 
	 * @param distance_gridUnits the distance in grid units
	 * @return the distance in miles
	 */
	public double toMiles(double distance_gridUnits) {
		if(milesPerGridUnit != null) {
			return milesPerGridUnit * distance_gridUnits;
		}
		return 0;
	}
	
	/**
	 * Convert the given distance in miles to grid units.
	 * 
	 * @param distance_miles the distance in miles
	 * @return the distance in grid units
	 */
	public double toGridUnits(double distance_miles) {
		if(milesPerGridUnit != null) {
			return distance_miles / milesPerGridUnit;
		}
		return 0;
	}
	

	/**
	 * Gets the bottom left lat.
	 *
	 * @return the bottom left latitude in degrees
	 */
	@XmlAttribute(name="bottomLeftLat")
	public Double getBottomLeftLat() {
		return bottomLeftLat;
	}

	/**
	 * Sets the bottom left lat.
	 *
	 * @param bottomLeftLat the new bottom left lat
	 */
	public void setBottomLeftLat(Double bottomLeftLat) {
		this.bottomLeftLat = bottomLeftLat;
	}

	/**
	 * Gets the bottom left lon.
	 *
	 * @return the bottom left longitude in degrees
	 */
	@XmlAttribute(name="bottomLeftLon")	
	public Double getBottomLeftLon() {
		return bottomLeftLon;
	}

	/**
	 * Sets the bottom left lon.
	 *
	 * @param bottomLeftLon the new bottom left lon
	 */
	public void setBottomLeftLon(Double bottomLeftLon) {
		this.bottomLeftLon = bottomLeftLon;
	}

	@Override
	protected GridSize clone() {
		return new GridSize(this);
	}	
}