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
package org.mitre.icarus.cps.app.widgets.map.phase_2.objects.area_grid;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.ImageOverlay;
import org.mitre.icarus.cps.feature_vector.phase_2.AreaGrid;
import org.mitre.icarus.cps.feature_vector.phase_2.GeoArea;

/**
 * 
 * @author CBONACETO
 *
 */
public class AreaGridMapObject<T> extends ImageOverlay {	
	
	/** The color map to use to determine the grid square colors */
	protected IAreaGridColorMap<T> colorMap;	
	
	/**
	 * @param areaGrid
	 * @param colorMap
	 */
	public AreaGridMapObject(GeoArea area, AreaGrid<T> areaGrid, IAreaGridColorMap<T> colorMap) {
		super(createAreaGridImage(area, areaGrid, colorMap), 
				new GeoPosition(area.getTopLeftLat(), area.getTopLeftLon()), 
				new GeoPosition(area.getBottomRightLat(), area.getBottomRightLon()));
		this.colorMap = colorMap;
	}
	
	/**
	 * @param areaGrid
	 */
	public void setAreaGrid(GeoArea area, AreaGrid<T> areaGrid) {
		setImage(createAreaGridImage(area, areaGrid, colorMap), 
				new GeoPosition(area.getTopLeftLat(), area.getTopLeftLon()), 
				new GeoPosition(area.getBottomRightLat(), area.getBottomRightLon()));
	}
	
	/**
	 * @param areaGrid
	 * @param colorMap
	 * @return
	 */
	protected static <T> BufferedImage createAreaGridImage(GeoArea area, AreaGrid<T> areaGrid, IAreaGridColorMap<T> colorMap) {
		int gridWidth = area.getGridWidth();
		int gridHeight = area.getGridHeight();
		
		BufferedImage image = new BufferedImage(gridWidth, gridHeight, BufferedImage.TYPE_INT_ARGB);
		
		int[] rgbArray = new int[gridWidth * gridHeight];
		int index = 0;
		for(ArrayList<T> row : areaGrid.getGridSquareValues()) {
			for(T val : row) {
				rgbArray[index] = colorMap.getColorForValue(val);
				index++;
			}
		}
		
		image.setRGB(0, 0, gridWidth, gridHeight, rgbArray, 0, gridWidth);
		
		return image;
	}
}