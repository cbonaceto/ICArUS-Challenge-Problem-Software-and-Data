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
package org.mitre.icarus.cps.app.widgets.map.phase_2.tile_factory;

import org.jdesktop.swingx.mapviewer.TileFactoryInfo;

/**
 * 
 * @author CBONACETO
 *
 */
public class IcarusTileFactoryInfo extends TileFactoryInfo {	
	
	/** The tile image type (the default is png) */
	protected String tileType = "png";	
	
	/** In single tile mode, the URL to a single tile image located at the base URL of name single tile name
	 * is always returned  */
	protected boolean singleTileMode = false;
	
	/** In single tile mode, the name of the tile image */
	protected String singleTileName;

	public IcarusTileFactoryInfo(int minimumZoomLevel, int maximumZoomLevel,
			int totalMapZoom, int tileSize, boolean xr2l, boolean yt2b,
			String baseURL, String xparam, String yparam, String zparam) {
		super(minimumZoomLevel, maximumZoomLevel, totalMapZoom, tileSize, xr2l, yt2b,
				baseURL, xparam, yparam, zparam);
		setBaseURL(baseURL);
	}	
	
	public IcarusTileFactoryInfo(String name, int minimumZoomLevel,
			int maximumZoomLevel, int totalMapZoom, int tileSize, boolean xr2l,
			boolean yt2b, String baseURL, String xparam, String yparam,
			String zparam) {
		super(name, minimumZoomLevel, maximumZoomLevel, totalMapZoom, tileSize, xr2l,
				yt2b, baseURL, xparam, yparam, zparam);
		setBaseURL(baseURL);
	}

	public String getTileType() {
		return tileType;
	}

	public void setTileType(String tileType) {
		this.tileType = tileType;
	}

	public boolean isSingleTileMode() {
		return singleTileMode;
	}

	public void setSingleTileMode(boolean singleTileMode) {
		this.singleTileMode = singleTileMode;
	}

	public String getSingleTileName() {
		return singleTileName;
	}

	public void setSingleTileName(String singleTileName) {
		this.singleTileName = singleTileName;
	}

	public void setBaseURL(String baseURL) {
		if(!baseURL.endsWith("/")) {
			baseURL = baseURL + "/";
		}
		this.baseURL = baseURL;
	}

	/* (non-Javadoc)
	 * @see org.jdesktop.swingx.mapviewer.TileFactoryInfo#getTileUrl(int, int, int)
	 */
	@Override
	public String getTileUrl(int x, int y, int zoom) {
		System.out.println("fetching tile at: " + x + ", " + y + ", " + zoom);
		if(singleTileMode) {
			//System.out.println("fetching tile at: " + x + ", " + y + ", " + zoom + ", tile name: " + baseURL + singleTileName);
			return baseURL + singleTileName;
		} else {
			return baseURL + Integer.toString(zoom) + "/" + Integer.toString(x) + "/" + Integer.toString(y) + "." + tileType;
		}
	}
}