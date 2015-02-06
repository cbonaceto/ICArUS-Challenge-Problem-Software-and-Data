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
package org.mitre.icarus.cps.app.widgets.phase_05;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.swing.ImageIcon;

import org.mitre.icarus.cps.app.widgets.map.phase_05.Building;
import org.mitre.icarus.cps.app.widgets.map.phase_05.LayoutMode;
import org.mitre.icarus.cps.app.widgets.map.phase_05.MASINTFeature;
import org.mitre.icarus.cps.app.widgets.map.phase_05.RenderData;
import org.mitre.icarus.cps.app.widgets.map.phase_05.SIGINTFeature;
import org.mitre.icarus.cps.app.widgets.map.phase_05.Sector;
import org.mitre.icarus.cps.exam.phase_05.testing.assessment.EvidenceElement.ItemType;
import org.mitre.icarus.cps.feature_vector.phase_05.ParsedPaletteObject;

/**
 * Manages application images.
 * 
 * @author CBONACETO
 *
 */
public class ImageManager {
	
	/** Icon size */
	public static final Dimension IconSize = new Dimension(20, 20);
	
	/** Default size for evidence images */
	public static final Dimension EvidenceImageSize = new Dimension(50, 50);
	
	/** Icon Types */
	public static final String IMINT_LAYER_ICON = "imint_layer";
	public static final String SIGINT_LAYER_ICON = "sigint_layer";
	public static final String MASINT_LAYER_ICON = "masint_layer";
	public static final String SECTOR_LAYER_ICON = "sector_layer";
	
	public static final String BUILDING_ICON = "bulding";
	public static final String ROOFTOP_HARDWARE_ICON = "rooftop_hardware";
	public static final String WATER_ICON = "water";
	public static final String SIGINT_HIT_ICON = "sigint";
	public static final String MASINT1_HIT_ICON = "masint1";
	public static final String MASINT2_HIT_ICON = "masint2";
	/*****/
	
	/** Icon Images */
	private HashMap<String, ImageIcon> images;	
	
	/** Singleton instance of the Image Manager */
	private static final ImageManager imageManager = new ImageManager();	
	
	private ImageManager() {
		images = new HashMap<String, ImageIcon>();
		loadImages();
	}
	
	/** Destroys and re-creates all images */
	public void loadImages() {
		createLayerIcons();
	}
	
	/** Create layer icons (used in the layer panel and legend) */
	private void createLayerIcons() {
		images.put(IMINT_LAYER_ICON, createImIntLayerIcon());
		images.put(BUILDING_ICON, createBuildingIcon(false));
		images.put(ROOFTOP_HARDWARE_ICON, createRooftopHardwareIcon());
		images.put(WATER_ICON, createBuildingIcon(true));
		
		images.put(SIGINT_LAYER_ICON, createSigIntLayerIcon());
		images.put(SIGINT_HIT_ICON, images.get(SIGINT_LAYER_ICON));
		
		images.put(MASINT_LAYER_ICON, createMasIntLayerIcon());
		images.put(MASINT1_HIT_ICON, createMasIntHitIcon(1));
		images.put(MASINT2_HIT_ICON, createMasIntHitIcon(2));
		
		images.put(SECTOR_LAYER_ICON, createSectorLayerIcon());		
	}
	
	private ImageIcon createImIntLayerIcon() {
		BufferedImage img = new BufferedImage(IconSize.width, IconSize.height, 
				BufferedImage.TYPE_INT_ARGB);
		int buffer = 2;
		int width = (IconSize.width - buffer * 3)/2;
		if(width > IconSize.height) {
			width = IconSize.height; 
		}
		int y = (IconSize.height - width)/2;
		Graphics2D g = (Graphics2D)img.getGraphics();
		g.setColor(ColorManager.get(ColorManager.BUILDING));
		g.fillRect(buffer, y, width, width);
		g.setColor(ColorManager.get(ColorManager.WATER));
		g.fillRect(buffer + width + buffer, y, width, width);
		return new ImageIcon(img);
	}
	
	private ImageIcon createBuildingIcon(boolean isWater) {
		BufferedImage img = new BufferedImage(IconSize.width, IconSize.height, 
				BufferedImage.TYPE_INT_ARGB);
		int buffer = 4;
		int width = (IconSize.width - buffer * 2);
		if(width > IconSize.height) {
			width = IconSize.height; 
		}
		int y = (IconSize.height - width)/2;
		Graphics2D g = (Graphics2D)img.getGraphics();
		if(isWater) {
			g.setColor(ColorManager.get(ColorManager.WATER));
		}
		else {
			g.setColor(ColorManager.get(ColorManager.BUILDING));
		}
		g.fillRect(buffer, y, width, width);
		return new ImageIcon(img);
	}
	
	private ImageIcon createRooftopHardwareIcon() {
		BufferedImage img = new BufferedImage(IconSize.width, IconSize.height, 
				BufferedImage.TYPE_INT_ARGB);
		int[][] layout = {{0, 0}};
		Graphics2D g = (Graphics2D)img.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.translate((IconSize.width-LayoutMode.ACC_SIZE)/2, (IconSize.height-LayoutMode.ACC_SIZE)/2);
		LayoutMode.LIST.drawAccessories(g, LayoutMode.ACC_SIZE, LayoutMode.ACC_SIZE , layout);
		return new ImageIcon(img);
	}
	
	private ImageIcon createSigIntLayerIcon() {
		BufferedImage img = new BufferedImage(IconSize.width, IconSize.height, 
				BufferedImage.TYPE_INT_ARGB);
		SIGINTFeature feature = new SIGINTFeature();
		feature.setBounds(new Rectangle(0, 0, IconSize.width, IconSize.height));
		RenderData r = new RenderData(IconSize.width, IconSize.height, 
				IconSize.width, IconSize.height, 
				IconSize.height, IconSize.width);
		Graphics2D g = (Graphics2D)img.getGraphics();
		feature.render(g, r);
		return new ImageIcon(img);
	}
	
	private ImageIcon createMasIntLayerIcon() {
		BufferedImage img = new BufferedImage(IconSize.width, IconSize.height, 
				BufferedImage.TYPE_INT_ARGB);
		MASINTFeature feature = new MASINTFeature();
		feature.setBounds(new Rectangle(0, 0, IconSize.width-1, IconSize.height-1));
		//feature.setMasCount(4);
		feature.setType(1);
		RenderData r = new RenderData(IconSize.width-1, IconSize.height-1, 
				IconSize.width-1, IconSize.height-1, 
				IconSize.height-1, IconSize.width-1);
		Graphics2D g = (Graphics2D)img.getGraphics();
		feature.render(g, r);
		return new ImageIcon(img);
	}
	
	private ImageIcon createMasIntHitIcon(int type) {
		BufferedImage img = new BufferedImage(IconSize.width, IconSize.height, 
				BufferedImage.TYPE_INT_ARGB);
		MASINTFeature feature = new MASINTFeature();
		feature.setBounds(new Rectangle(0, 0, IconSize.width-1, IconSize.height-1));
		//feature.setMasCount(4);
		feature.setType(type);
		RenderData r = new RenderData(IconSize.width-1, IconSize.height-1, 
				IconSize.width-1, IconSize.height-1, 
				IconSize.height-1, IconSize.width-1);
		Graphics2D g = (Graphics2D)img.getGraphics();
		feature.render(g, r);
		return new ImageIcon(img);
	}
	
	/*private ImageIcon createMasIntHitIcon(int type) {
		BufferedImage img = new BufferedImage(IconSize.width, IconSize.height, 
				BufferedImage.TYPE_INT_ARGB);
		int width = 6;
		int buffer = (IconSize.width - width)/2;
		int y = (IconSize.height - width)/2;
		Graphics2D g = (Graphics2D)img.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if(type == 1) {
			g.setColor(ColorManager.get(ColorManager.MASINT1));
		}
		else {
			g.setColor(ColorManager.get(ColorManager.MASINT2));
		}
		g.fillOval(buffer, y, width, width);
		return new ImageIcon(img);
	}*/
	
	private ImageIcon createSectorLayerIcon() {
		BufferedImage img = new BufferedImage(IconSize.width, IconSize.height, 
				BufferedImage.TYPE_INT_ARGB);
		int buffer = 5;
		Graphics2D g = (Graphics2D)img.getGraphics();
		g.setColor(ColorManager.get(ColorManager.SECTOR));
		g.setStroke(Sector.getStroke());
		g.drawRect(buffer, buffer, IconSize.width - buffer*2, IconSize.height - buffer*2);
		return new ImageIcon(img);
	}	

	/**
	 * Get the singleton instance of the image manager.
	 */
	public static ImageManager getImageManager() {
		return imageManager;
	}
	
	/**
	 * Get the image icon for the given imageType.
	 */
	public static ImageIcon getImageIcon(String imageType) {
		return imageManager.images.get(imageType);
	}
	
	/**
	 * Get the image icon for the given imageType.
	 */
	public static Image getImage(String imageType) {
		ImageIcon img = imageManager.images.get(imageType);
		if(img != null) {
			return img.getImage();
		}
		return null;
	}
	
	/**
	 * Get the image for an evidence object of the given type and the optional object definition.
	 */
	public static ImageIcon getEvidenceImage(ItemType itemType, Integer itemId, ParsedPaletteObject object) {
		
		ImageIcon imageIcon = null;
		
		switch(itemType) {
		case Building:
			if(object != null) {
				BufferedImage img = new BufferedImage(EvidenceImageSize.width, EvidenceImageSize.height,
						BufferedImage.TYPE_INT_ARGB);				
				Building building = new Building();
				building.setLayout(object.getLayout());
				building.setLayoutMode(LayoutMode.LIST);
				if(itemId != null) {
					building.setType(itemId);
				}
				building.setBounds(new Rectangle(0, 0, object.getWidth(), object.getHeight()));
				double tileWidth = 1.5;
				double tileHeight = 1.5;
				/*double tileWidth = (double)EvidenceImageSize.width/object.getWidth();
				double tileHeight = (double)EvidenceImageSize.height/object.getHeight();
				if(tileWidth < tileHeight) {
					tileHeight = tileWidth;
				}
				else {
					tileWidth = tileHeight;
				}*/
				RenderData r = new RenderData(tileWidth, tileHeight, 
						EvidenceImageSize.width-1, EvidenceImageSize.height-1, 
						EvidenceImageSize.width-1, EvidenceImageSize.height-1);
				Graphics2D g = (Graphics2D)img.getGraphics();
				g.setColor(Color.white);
				g.fillRect(0, 0, img.getWidth(), img.getHeight());
				g.translate((EvidenceImageSize.width-object.getWidth()*tileWidth)/2, 
						(EvidenceImageSize.height-object.getHeight()*tileHeight)/2);
				building.render(g, r);
				imageIcon = new ImageIcon(img);
			}
			break;
		case Rooftop_Hardware:
			imageIcon = imageManager.images.get(ROOFTOP_HARDWARE_ICON);
			break;
		case Water:
			BufferedImage img = new BufferedImage(EvidenceImageSize.width, EvidenceImageSize.height, 
					BufferedImage.TYPE_INT_ARGB);
			int buffer = 4;
			int width = EvidenceImageSize.width - (buffer * 2);
			if(width > EvidenceImageSize.height) {
				width = EvidenceImageSize.height; 
			}
			int y = (EvidenceImageSize.height - width)/2;
			Graphics2D g = (Graphics2D)img.getGraphics();
			g.setColor(ColorManager.get(ColorManager.WATER));
			g.fillRect(buffer, y, width, width);
			imageIcon = new ImageIcon(img);
			break;
		case SIGINT:
			imageIcon = imageManager.images.get(SIGINT_HIT_ICON);
			break;
		case MASINT_1:
			imageIcon = imageManager.images.get(MASINT1_HIT_ICON);
			break;
		case MASINT_2:
			imageIcon = imageManager.images.get(MASINT2_HIT_ICON);
			break;
		default:
			break;
		}
		
		return imageIcon;
	}
}