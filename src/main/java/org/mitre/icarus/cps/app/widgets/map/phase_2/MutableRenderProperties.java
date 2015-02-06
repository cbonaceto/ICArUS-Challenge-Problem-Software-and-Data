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
package org.mitre.icarus.cps.app.widgets.map.phase_2;

import java.awt.Font;
import java.awt.Rectangle;

import org.jdesktop.swingx.mapviewer.TileFactory;

/**
 * @author CBONACETO
 *
 */
public class MutableRenderProperties {
	
	protected final RenderProperties renderProperties;
	
	public MutableRenderProperties(TileFactory tileFactory) {
		renderProperties = new RenderProperties(tileFactory);
	}	
	
	public RenderProperties getRenderProperties() {
		return renderProperties;
	}
	
	public void setTileFactory(TileFactory tileFactory) {
		renderProperties.tileFactory = tileFactory;
	}	

	public void setViewportBounds(Rectangle viewportBounds) {
		renderProperties.viewportBounds = viewportBounds;
	}

	public void setZoom(int zoom) {
		renderProperties.zoom = zoom;
	}	

	/*public void setPlacemarkSize_degrees(double placemarkSize_degrees) {
		renderProperties.placemarkSize_degrees = placemarkSize_degrees;
	}*/	

	public void setPlacemarkSize_pixels(double placemarkSize_pixels) {
		renderProperties.placemarkSize_pixels = placemarkSize_pixels;
	}	

	public void setPlacemarkFont(Font placemarkFont) {
		renderProperties.placemarkFont = placemarkFont;
	}	

	public void setMaxToolTipWidth(int maxToolTipWidth) {
		renderProperties.maxToolTipWidth = maxToolTipWidth;
	}	
	
	public void setGeoTranslationsDisabled(boolean geoTranslationsDisabled) {
		renderProperties.geoTranslationsDisabled = geoTranslationsDisabled;
	}
}