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

import java.awt.image.BufferedImage;

import org.jdesktop.swingx.mapviewer.Tile;
import org.jdesktop.swingx.mapviewer.TileFactory;
import org.jdesktop.swingx.mapviewer.TileFactoryInfo;

/**
 * A tile factory that scales itself to zoom to a geo-referenced image. Always returns a null tile image.
 * 
 * @author CBONACETO
 *
 */
public class ImageScalingTileFactory extends TileFactory {
	
	/** The image width in pixels */
	protected int imageWidth_pixels;
	
	/** The image width in degrees */
	protected double imageWidth_degrees;
	
	/**
	 * 
	 */
	public ImageScalingTileFactory(int imageWidth_pixels, double imageWidth_degrees) {
		this(imageWidth_pixels, imageWidth_degrees, 0, 5, 11);
	}

	/**
	 * @param imageWidth_pixels
	 * @param imageWidth_degrees
	 * @param minZoomLevel
	 * @param maxZoomLevel
	 * @param totalMapZoom
	 */
	public ImageScalingTileFactory(int imageWidth_pixels, double imageWidth_degrees, 
			int minZoomLevel, int maxZoomLevel, int totalMapZoom) {		
		super(createTileFactoryInfo(computeTileSize(imageWidth_pixels, imageWidth_degrees, totalMapZoom), 
				minZoomLevel, maxZoomLevel, totalMapZoom)); 
		this.imageWidth_pixels = imageWidth_pixels;
		this.imageWidth_degrees = imageWidth_degrees;
	}
	
	/**
	 * Creates a new instance of ImageScalingTileFactory using the specified tile info.
	 * 
	 * @param info
	 */
	public ImageScalingTileFactory(TileFactoryInfo info) {
		super(info);
		this.imageWidth_pixels = 0;
		this.imageWidth_degrees = 0;
	}	
	
	protected static TileFactoryInfo createTileFactoryInfo(int tileSize, int minZoomLevel, int maxZoomLevel, int totalMapZoom) {
		return new TileFactoryInfo("SingleTileFactory", minZoomLevel, maxZoomLevel, totalMapZoom, tileSize,
				true, true, "", "x", "y", "z");
	}	
	
	public static int computeTileSize(int imageWidth_pixels, double imageWidth_degrees, int totalMapZoom) {
		return (int)(Math.round((imageWidth_pixels/imageWidth_degrees) * 360) / (Math.pow(2, totalMapZoom-1)));	
	}	

	public int getImageWidth_pixels() {
		return imageWidth_pixels;
	}

	public void setImageWidth_pixels(int imageWidth_pixels) {
		this.imageWidth_pixels = imageWidth_pixels;
	}

	public double getImageWidth_degrees() {
		return imageWidth_degrees;
	}

	public void setImageWidth_degrees(double imageWidth_degrees) {
		this.imageWidth_degrees = imageWidth_degrees;
	}

	/**
	 * Gets an instance of an empty tile for the given tile position and zoom on
	 * the world map.
	 * 
	 * @param x
	 *            The tile's x position on the world map.
	 * @param y
	 *            The tile's y position on the world map.
	 * @param zoom
	 *            The current zoom level.
	 */
	public Tile getTile(int x, int y, int zoom) {
		return new Tile(x, y, zoom) {
			public boolean isLoaded() {
				return true;
			}
			public BufferedImage getImage() {
				return null;
			}
		};
	}

    /**
     * Override this method to load the tile using, for example, an <code>ExecutorService</code>.
     * @param tile The tile to load.
     */
    protected void startLoading(Tile tile){
    	//Does nothing
    }
    
    public static void main(String[] args) {
		double imageWidth_degrees = Math.abs(-71.0745339752029 -  -71.0682370414109); 
		System.out.println(computeTileSize(792, imageWidth_degrees, 11));
	}
}