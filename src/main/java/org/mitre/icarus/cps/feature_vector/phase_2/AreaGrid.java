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

import java.util.ArrayList;

/**
 * Grids a geographic area into square cells. Allows assignment of a value of class T to each cell.
 * 
 * @author CBONACETO
 *
 */
public abstract class AreaGrid<T> {
	
	/** The values for each grid square */
	protected ArrayList<ArrayList<T>> gridSquareValues;	
	
	/**
	 * @return
	 */
	public ArrayList<ArrayList<T>> getGridSquareValues() {
		return gridSquareValues;
	}
	
	/**
	 * @param gridSquareValues
	 */
	public void setGridSquareValues(ArrayList<ArrayList<T>> gridSquareValues) {
		this.gridSquareValues = gridSquareValues;
	}	

	/**
	 * @param lon
	 * @param lat
	 * @return
	 */
	public T getGridSquareValueAtGeoLocation(Double lon, Double lat) {
		//TODO: Implement this
		return null;
	}
	
	/**
	 * @param x
	 * @param y
	 * @return
	 */
	public T getGridSquareValueAtGridLocation(Double x, Double y) {
		//TODO: Check rounding
		return getGridSquareValueAtGridLocation(x.intValue(), y.intValue());
	}	
	
	/**
	 * @param x
	 * @param y
	 * @return
	 */
	public T getGridSquareValueAtGridLocation(Integer x, Integer y) {
		if(gridSquareValues != null && x < gridSquareValues.size()) {
			ArrayList<T> row = gridSquareValues.get(x);
			if(row != null && y < row.size()) {
				return row.get(y);
			}
		}
		return null;
	}
	
	/**
	 * @param lon
	 * @param lat
	 * @param value
	 */
	public void setGridSquareValueAtGeoLocation(Double lon, Double lat, T value) {
		//TODO: Implement this
	}
	
	/**
	 * @param x
	 * @param y
	 * @param value
	 */
	public void setGridSquareValueAtGridLocation(Double x, Double y, T value) {
		setGridSquareValueAtGridLocation(x.intValue(), y.intValue(), value);
	}
	
	/**
	 * @param x
	 * @param y
	 * @param value
	 */
	public void setGridSquareValueAtGridLocation(Integer x, Integer y, T value) {
		if(gridSquareValues != null && x < gridSquareValues.size()) {
			ArrayList<T> row = gridSquareValues.get(x);
			if(row != null && y < row.size()) {
				row.set(x, value);
			}
		}
	}
}