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

import java.awt.geom.Point2D;

import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.mitre.icarus.cps.app.widgets.map.objects.AbstractMapObject;
import org.mitre.icarus.cps.app.widgets.map.phase_2.MapPanel_Phase2;
import org.mitre.icarus.cps.app.widgets.map.phase_2.layers.ILayer_Phase2;
import org.mitre.icarus.cps.feature_vector.phase_2.coordinate.GeoCoordinate;

/**
 * Abstract implementation of some methods in the IMapObject_Phase2 interface. In almost all cases,
 * map objects can extend this class instead of directly implementing the IMapObject interface.
 * 
 * @author CBONACETO
 *
 */
public abstract class AbstractMapObject_Phase2 extends AbstractMapObject implements IMapObject_Phase2 {
	
	/** Tool tip text associated with this map object (short informational text about the object shown in tool tips) */
	protected String toolTipText;
	
	/** Information text associated with this map object (longer informational text about the object shown in information balloons) */
	protected String informationText;
	
	/** The layer this map object is contained in */
	protected ILayer_Phase2<? extends IMapObject_Phase2, ? extends MapPanel_Phase2> layer;	
	
	@Override
	public String getToolTipText() {
		return toolTipText;
	}

	@Override
	public void setToolTipText(String toolTipText) {
		this.toolTipText = toolTipText;
	}

	@Override
	public String getInformationText() {
		return informationText;
	}
	
	@Override
	public void setInformationText(String informationText) {
		this.informationText = informationText;
	}

	@Override
	public ILayer_Phase2<? extends IMapObject_Phase2, ? extends MapPanel_Phase2> getLayer() {
		return layer;
	}

	@Override
	public void setLayer(ILayer_Phase2<? extends IMapObject_Phase2, ? extends MapPanel_Phase2> layer) {
		this.layer = layer;
	}	
	
	@Override
	public boolean containsPixelLocation(Point2D location) {
		if(getPixelShape() != null) {
			return getPixelShape().contains(location);
		}
		return false;
	}

	@Override
	public boolean containsPixelLocation(double x, double y) {
		if(getPixelShape() != null) {
			return getPixelShape().contains(x, y);
		}
		return false;
	}
	
	@Override
	public boolean containsGeoLocation(Point2D location) {
		if(getGeoShape() != null) {
			return getGeoShape().contains(location);
		}
		return false;
	}

	@Override
	public boolean containsGeoLocation(GeoCoordinate location) {
		if(getGeoShape() != null) {
			return getGeoShape().contains(location.getLon(), location.getLat());
		}
		return false;
	}

	@Override
	public boolean containsGeoLocation(GeoPosition position) {
		if(getGeoShape() != null) {
			return getGeoShape().contains(position.getLongitude(), position.getLatitude());
		}
		return false;
	}
	
	@Override
	public boolean containsGeoLocation(double lon, double lat) {
		if(getGeoShape() != null) {
			return getGeoShape().contains(lon, lat);
		}
		return false;
	}
}