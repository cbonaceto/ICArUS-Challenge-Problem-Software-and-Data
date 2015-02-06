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
package org.mitre.icarus.cps.app.widgets.map.phase_1;

import java.awt.Point;
import java.awt.geom.Point2D;

import org.mitre.icarus.cps.app.widgets.map.IMapPanel;
import org.mitre.icarus.cps.feature_vector.phase_1.GridLocation2D;
import org.mitre.icarus.cps.feature_vector.phase_1.GridSize;

/**
 * Interface for CPD Phase 1 map GUI implementations.
 * 
 * @author CBONACETO
 *
 */
public interface IMapPanel_Phase1 extends IMapPanel {		
	/**
	 * Get the map model, which contains the map layers. Each layer is comprised of 0 or more map objects.
	 * 
	 * @return the map model
	 */
	public MapModel getMapModel();

	/**
	 * Set the map model.
	 * 
	 * @param mapModel the map model
	 */
	public void setMapModel(MapModel mapModel);
	
	/**
	 * Get the current grid size of the map.
	 * 
	 * @return the grid size
	 */
	public GridSize getGridSize();
	
	/**
	 * Set the current grid size of the map.
	 * 
	 * @param gridSize the grid size
	 */
	public void setGridSize(GridSize gridSize);	
	
	/**
	 * Get the map center location in grid coordinates.
	 * 
	 * @return
	 */
	public GridLocation2D getCenterGridLocation();
	
	/**
	 * Tranlates the given distance in grid units to a distance in pixels.
	 * 
	 * @param gridUnits
	 * @return
	 */
	public double translateToPixels(double gridUnits);
	
	/**
	 * Translates the given distance in pixels to a distance in grid units.
	 * 
	 * @param pixels
	 * @return
	 */
	public double translateToGridUnits(double pixels);
	
	/**
	 * Translates the given mouse location coordinates to pixel coordinates.
	 * 
	 * @param mouseX
	 * @param mouseY
	 * @return
	 */
	public Point2D translateMouseToPixel(int mouseX, int mouseY);
	
	/**
	 * Translates the given mouse location to pixel coordinates.
	 * 
	 * @param mousePoint
	 */
	public void translateMouseToPixel(Point mousePoint);
	
	/**
	 * Get the pixel coordinates corresponding to the given grid location.
	 * 
	 * @param gridLocation
	 * @return
	 */
	public Point2D getPixelLocation(GridLocation2D gridLocation);
	
	/**
	 * Get the pixel coordinates corresponding to the given grid coordinates.
	 * 
	 * @param gridX
	 * @param gridY
	 * @return
	 */
	public Point2D getPixelLocation(double gridX, double gridY);
	
	/**
	 * Get the map grid location corresponding to the given coordinates in pixel space.
	 * 
	 * @param pixelLocation
	 * @return
	 */
	public GridLocation2D getGridLocation(Point2D pixelLocation);
	
	/**
	 * Get the map grid location corresponding to the given coordinates in pixel space.
	 * 
	 * @param pixelX
	 * @param pixelY
	 * @return
	 */
	public GridLocation2D getGridLocation(double pixelX, double pixelY);	
	
	/**
	 * Get the map grid location corresponding to the given mouse coordinates.
	 * 
	 * @param mouseLocation
	 * @return
	 */
	public GridLocation2D getGridLocation(Point mouseLocation);	
}