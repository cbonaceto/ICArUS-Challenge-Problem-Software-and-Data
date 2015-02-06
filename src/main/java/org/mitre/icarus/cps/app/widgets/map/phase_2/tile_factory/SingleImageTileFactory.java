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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.jdesktop.swingx.mapviewer.Tile;
import org.jdesktop.swingx.mapviewer.TileFactory;
import org.jdesktop.swingx.mapviewer.TileFactoryInfo;

/**
 * A tile factory that always returns a single image.
 * 
 * @author CBONACETO
 *
 */
public class SingleImageTileFactory extends TileFactory {
	
	/** The empty tile image. */
	protected BufferedImage emptyTile;
	
	/**
	 * Creates a new instance of SingleTileFactory using the default tile color.
	 */
	public SingleImageTileFactory() {
		this(Color.WHITE, 1, 15, 17);
	}
	
	/**
	 * Creates a new instance of SingleTileFactory using the default tile color.
	 * 
	 * @param minZoomLevel
	 * @param maxZoomLevel
	 * @param totalMapZoom
	 */
	public SingleImageTileFactory(int minZoomLevel, int maxZoomLevel, int totalMapZoom) {
		this(Color.WHITE, minZoomLevel, maxZoomLevel, totalMapZoom);
	}

	/**
	 * Creates a new instance of SingleTileFactory using the specified tile color.
	 * 
	 * @param tileColor
	 * @param minZoomLevel
	 * @param maxZoomLevel
	 * @param totalMapZoom
	 */
	public SingleImageTileFactory(Color tileColor, int minZoomLevel, int maxZoomLevel, int totalMapZoom) {
		super(createTileFactoryInfo(256, minZoomLevel, maxZoomLevel, totalMapZoom));
		createEmptyTileImage(tileColor);
	}
	
	/**
	 * Creates a new instance of SingleTileFactory using the specified tile color and info.
	 * 
	 * @param tileColor
	 * @param info
	 */
	public SingleImageTileFactory(Color tileColor, TileFactoryInfo info) {
		super(info);
		createEmptyTileImage(tileColor);
	}
	
	/**
	 * Creates a new instance of SingleTileFactory using the specified tile image.
	 * 
	 * @param tileImage
	 */
	public SingleImageTileFactory(BufferedImage tileImage, int minZoomLevel, int maxZoomLevel, int totalMapZoom) {
		super(createTileFactoryInfo(tileImage != null ? tileImage.getWidth() : 256,
				minZoomLevel, maxZoomLevel, totalMapZoom));
		if(tileImage == null || tileImage.getWidth() <= 0 || tileImage.getWidth() != tileImage.getHeight()) {
			throw new IllegalArgumentException("Invalid tile image");
		}		
		this.emptyTile = tileImage;
	}
	
	protected static TileFactoryInfo createTileFactoryInfo(int tileSize, int minZoomLevel, int maxZoomLevel, int totalMapZoom) {
		return new TileFactoryInfo("SingleTileFactory", minZoomLevel, maxZoomLevel, totalMapZoom, tileSize,
				true, true, "", "x", "y", "z");
	}
	
	/**
	 * Create a tile image of size tileSize x tileSize with a solid fill of tileColor.
	 * 
	 * @param tileColor the tile fill color
	 * @return
	 */
	protected void createEmptyTileImage(Color tileColor) {
		int tileSize = getInfo().getTileSize(getInfo().getMinimumZoomLevel());
		emptyTile = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_INT_ARGB);		
		//emptyTile = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = emptyTile.createGraphics();
		g.setColor(tileColor);		
		g.fillRect(0, 0, tileSize, tileSize);		
		g.dispose();
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
				return emptyTile;
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
}