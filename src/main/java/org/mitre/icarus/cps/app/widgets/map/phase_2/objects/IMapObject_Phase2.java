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
package org.mitre.icarus.cps.app.widgets.map.phase_2.objects;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;

import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.mitre.icarus.cps.app.widgets.map.objects.IMapObject;
import org.mitre.icarus.cps.app.widgets.map.phase_2.MapPanel_Phase2;
import org.mitre.icarus.cps.app.widgets.map.phase_2.RenderProperties;
import org.mitre.icarus.cps.app.widgets.map.phase_2.layers.ILayer_Phase2;
import org.mitre.icarus.cps.feature_vector.phase_2.coordinate.GeoCoordinate;

/**
 * @author CBONACETO
 *
 */
public interface IMapObject_Phase2 extends IMapObject {	
	
	public String getToolTipText();
	public void setToolTipText(String toolTipText);
	
	public String getInformationText();
	public void setInformationText(String informationText);
	
	public ILayer_Phase2<? extends IMapObject_Phase2, ? extends MapPanel_Phase2> getLayer();	
	public void setLayer(ILayer_Phase2<? extends IMapObject_Phase2, ? extends MapPanel_Phase2> layer);
	
	//New methods under consideration:	
	//public Shape getPixelShape();
	
	/**
	 * @return
	 */
	public Point2D getCenterPixelLocation();

	/**
	 * @return
	 */
	public Point2D getCenterGeoLocation();	

	/**
	 * @return
	 */
	public Shape getGeoShape();	
	
	//public GeoBounds getGeoBounds();
	
	public Shape getPixelShape();	
	
	//public Rectangle2D getPixelBounds();
	
	public boolean containsPixelLocation(Point2D location);
	public boolean containsPixelLocation(double x, double y);

	public boolean containsGeoLocation(Point2D location);
	public boolean containsGeoLocation(GeoCoordinate location);
	public boolean containsGeoLocation(GeoPosition position);
	public boolean containsGeoLocation(double lon, double lat);	

	/**
	 * Render the map object
	 * 
	 * @param g2d
	 * @param map
	 * @param width
	 * @param height
	 * @param renderPropertiesChanged
	 */
	public void render(Graphics2D g, RenderProperties renderProperties, boolean renderPropertiesChanged);
}