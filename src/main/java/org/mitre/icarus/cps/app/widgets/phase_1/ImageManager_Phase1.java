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
package org.mitre.icarus.cps.app.widgets.phase_1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.mitre.icarus.cps.app.widgets.AbstractImageManager;
import org.mitre.icarus.cps.app.widgets.ImageManager;
import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.app.widgets.ImageManager.IconSize;
import org.mitre.icarus.cps.app.widgets.map.MapConstants;
import org.mitre.icarus.cps.app.widgets.map.phase_1.MapConstants_Phase1;
import org.mitre.icarus.cps.app.widgets.map.phase_1.RenderProperties;
import org.mitre.icarus.cps.app.widgets.map.phase_1.PlacemarkFactory.AttackLocationType;
import org.mitre.icarus.cps.app.widgets.map.phase_1.objects.AttackLocationPlacemark;
import org.mitre.icarus.cps.app.widgets.map.phase_1.objects.CircleShape;
import org.mitre.icarus.cps.app.widgets.map.phase_1.objects.GroupCenterPlacemark;
import org.mitre.icarus.cps.app.widgets.map.phase_1.objects.PlacemarkMapObject;
import org.mitre.icarus.cps.app.widgets.map.phase_1.objects.PlacemarkMapObject.PlacemarkShape;
import org.mitre.icarus.cps.feature_vector.phase_1.GridLocation2D;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupAttack;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupCenter;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;
import org.mitre.icarus.cps.feature_vector.phase_1.ImintType;
import org.mitre.icarus.cps.feature_vector.phase_1.IntType;
import org.mitre.icarus.cps.feature_vector.phase_1.MovintType;
import org.mitre.icarus.cps.feature_vector.phase_1.SigintType;

/**
 * Manages Phase 1-specific application images.
 * 
 * @author CBONACETO
 *
 */
public class ImageManager_Phase1 extends AbstractImageManager {
	
	/** Placemark-based icon size */
	public static final Dimension PLACEMARK_ICON_SIZE = 
		new Dimension(MapConstants.PLACEMARK_SIZE_PIXELS + 4, MapConstants.PLACEMARK_SIZE_PIXELS + 4);	
	
	/** Icon types for Phase 1 */
	public static final String SIGACTS_LAYER_ICON = "SIGACTS_LAYER";
	public static final String ROADS_LAYER_ICON = "ROADS_LAYER";
	public static final String GROUP_CENTERS_LAYER_ICON = "GROUP_CENTERS_LAYER";
	public static final String GROUP_CIRCLES_LAYER_ICON = "GROUP_CIRCLES_LAYER";
	public static final String IMINT_LAYER_ICON = "IMINT_LAYER";
	public static final String MOVINT_LAYER_ICON = "MOVINT_LAYER";
	public static final String SIGINT_LAYER_ICON = "SIGINT_LAYER";
	public static final String SOCINT_LAYER_ICON = "SOCINT_LAYER";	
	public static final String IMINT_GOVERNMENT_ICON = IntType.IMINT.toString() + "_" + ImintType.Government.toString();
	public static final String IMINT_MILITARY_ICON = IntType.IMINT.toString() + "_" + ImintType.Military.toString();	
	public static final String MOVINT_SPARSE_ICON = IntType.MOVINT.toString() + "_" + MovintType.SparseTraffic.toString();
	public static final String MOVINT_DENSE_ICON = IntType.MOVINT.toString() + "_" + MovintType.DenseTraffic.toString();	
	public static final String SIGINT_SILENT_ICON = IntType.SIGINT.toString() + "_" + SigintType.Silent.toString();
	public static final String SIGINT_CHATTER_ICON = IntType.SIGINT.toString() + "_" + SigintType.Chatter.toString();
	/****/	
	
	/** Initialize the images */
	static {
		createIcons();
	}
	
	private ImageManager_Phase1() {}
	
	/**
	 * Get the image icon for the given imageType.
	 */
	public static ImageIcon getImageIcon(String imageType) {
		return ImageManager.getImageIcon(imageType);
	}
	
	/**
	 * Get the image icon for the given imageType.
	 */
	public static Image getImage(String imageType) {
		return ImageManager.getImage(imageType);
	}
	
	public static Image getSigactLocationImage(String locationName) {
		ImageIcon image = getSigactLocationImageIcon(locationName);
		if(image != null) {
			return image.getImage();
		}
		return null;
	}
	
	public static ImageIcon getSigactLocationImageIcon(String locationName) {
		String iconName = "SIGACT_" + locationName;
		ImageIcon image = ImageManager.getImageIcon(iconName);
		if(image == null) {
			image = new ImageIcon(createSigactLocationIcon(null, 
					null, locationName, AttackLocationType.LOCATION));
			ImageManager.addImage(iconName, image);
		}
		return image;
	}
	
	public static Image getSigactLocationImage(GroupType group) {
		ImageIcon image = getSigactLocationImageIcon(group);
		if(image != null) {
			return image.getImage();
		}
		return null;
	}
	
	public static ImageIcon getSigactLocationImageIcon(GroupType group) {
		String iconName = "SIGACT_" + group.toString();
		ImageIcon image = ImageManager.getImageIcon(iconName);
		if(image == null) {
			image = new ImageIcon(createSigactLocationIcon(group, 
					(WidgetConstants.USE_GROUP_SYMBOLS ? getGroupSymbolImage(group, IconSize.Small) : null), group.toString(), 
					AttackLocationType.GROUP_ATTACK));
			ImageManager.addImage(iconName, image);
		}
		return image;
	}	
	
	public static Image getGroupCenterImage(GroupType group) {
		ImageIcon image = getGroupCenterImageIcon(group);
		if(image != null) {
			return image.getImage();
		}
		return null;
	}
	
	public static ImageIcon getGroupCenterImageIcon(GroupType group) {
		String iconName = "GROUP_CENTER_" + group.toString();
		ImageIcon image = ImageManager.getImageIcon(iconName);
		if(image == null) {
			image = new ImageIcon(createGroupCenterIcon(group, null));
			ImageManager.addImage(iconName, image);
		}
		return image;
	}
	
	public static Image getGroupSymbolImage(GroupType group, IconSize iconSize) {
		ImageIcon image = getGroupSymbolImageIcon(group, iconSize);
		if(image != null) {
			return image.getImage();
		}
		return null;
	}
	
	public static ImageIcon getGroupSymbolImageIcon(GroupType group, IconSize iconSize) {
		return ImageManager.getImageIcon("GROUP_" + group.toString() + "_SYMBOL_" + iconSize.toString());
	}
	
	public static Image getSocintRegionImage(GroupType group) {
		ImageIcon image = getSocintRegionImageIcon(group);
		if(image != null) {
			return image.getImage();
		}
		return null;
	}
	
	public static ImageIcon getSocintRegionImageIcon(GroupType group) {
		String iconName = "SOCINT_" + group.toString();
		ImageIcon image = ImageManager.getImageIcon(iconName);
		if(image == null) {
			image = new ImageIcon(createSocintRegionIcon(group));
			ImageManager.addImage(iconName, image);
		}
		return image;
	}
	
	public static Image getImintImage(ImintType imint) {
		return ImageManager.getImage((imint == ImintType.Government) ? IMINT_GOVERNMENT_ICON : IMINT_MILITARY_ICON);		
	}
	
	public static ImageIcon getImintImageIcon(ImintType imint) {
		return ImageManager.getImageIcon((imint == ImintType.Government) ? IMINT_GOVERNMENT_ICON : IMINT_MILITARY_ICON);		
	}
	
	public static Image getMovintImage(MovintType movint) {
		return ImageManager.getImage((movint == MovintType.SparseTraffic) ? MOVINT_SPARSE_ICON : MOVINT_DENSE_ICON);
	}
	
	public static ImageIcon getMovintImageIcon(MovintType movint) {
		return ImageManager.getImageIcon((movint == MovintType.SparseTraffic) ? MOVINT_SPARSE_ICON : MOVINT_DENSE_ICON);
	}	
	
	public static Image getSigintImage(SigintType sigint) {
		return ImageManager.getImage((sigint == SigintType.Silent) ? SIGINT_SILENT_ICON : SIGINT_CHATTER_ICON);
	}
	
	public static ImageIcon getSigintImageIcon(SigintType sigint) {
		return ImageManager.getImageIcon((sigint == SigintType.Silent) ? SIGINT_SILENT_ICON : SIGINT_CHATTER_ICON);
	}
	
	/** Loads the images */
	private static void createIcons() {		
		//Create INT type icons 
		createINTTypeIcons();
		
		//Create layer icons
		createLayerIcons();		
	}
	
	/** Create layer icons (used in the layer panel and legend) */
	private static void createLayerIcons() {
		try {
			RenderProperties renderData = new RenderProperties(PLACEMARK_ICON_SIZE.width, PLACEMARK_ICON_SIZE.height, 0.3);
			renderData.setRenderBounds(0, 0, renderData.width_gridUnits, renderData.height_gridUnits);
			GridLocation2D center = renderData.getCenterGridLocation();

			//Create SIGACTS layer icon
			PlacemarkMapObject placemark = new PlacemarkMapObject();
			placemark.setCenterLocation(center);
			placemark.setBorderLineWidth(1.f);
			placemark.setMarkerShape(PlacemarkShape.Square);
			BufferedImage image = new BufferedImage(PLACEMARK_ICON_SIZE.width, PLACEMARK_ICON_SIZE.height,
					BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = (Graphics2D)image.getGraphics();
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			placemark.render(g2d, renderData, true);
			ImageManager.addImage(SIGACTS_LAYER_ICON, new ImageIcon(image));

			//Create Roads layer icon
			try {
				ImageManager.addImage(ROADS_LAYER_ICON, 
						new ImageIcon(ImageIO.read(ImageManager.class.getClassLoader().getResourceAsStream("images/phase_1/roads_layer_icon.png"))));
				//System.out.println("Roads icon: " + ImageManager.getImageIcon(ROADS_LAYER_ICON));
			} catch (Exception e) {			
				e.printStackTrace();
			}

			//Create Group Centers layer icon
			placemark.setCenterLocation(center);
			placemark.setBorderLineWidth(0.f);
			placemark.setMarkerShape(PlacemarkShape.Circle);
			placemark.setBackgroundColor(Color.LIGHT_GRAY);
			image = new BufferedImage(PLACEMARK_ICON_SIZE.width, PLACEMARK_ICON_SIZE.height, 
					BufferedImage.TYPE_INT_ARGB);
			g2d = (Graphics2D)image.getGraphics();
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			placemark.render(g2d, renderData, true);
			ImageManager.addImage(GROUP_CENTERS_LAYER_ICON, new ImageIcon(image));

			//Create Group Circles layer icon
			placemark.setBorderLineWidth(1.f);
			placemark.setBorderColor(Color.black);
			placemark.setBackgroundColor(null);
			image = new BufferedImage(PLACEMARK_ICON_SIZE.width, PLACEMARK_ICON_SIZE.height, 
					BufferedImage.TYPE_INT_ARGB);
			g2d = (Graphics2D)image.getGraphics();
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			placemark.render(g2d, renderData, true);
			ImageManager.addImage(GROUP_CIRCLES_LAYER_ICON, new ImageIcon(image));		

			//Create IMINT layer icon
			Image imintGovernment = ImageManager.getImage(IMINT_GOVERNMENT_ICON);
			Image imintMilitary = ImageManager.getImage(IMINT_MILITARY_ICON);
			image = new BufferedImage(2 + imintGovernment.getWidth(null) + 3 + imintMilitary.getWidth(null) + 2, 
					Math.max(imintGovernment.getHeight(null), imintMilitary.getHeight(null)) + 4, BufferedImage.TYPE_INT_ARGB);
			g2d = (Graphics2D)image.getGraphics();
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.drawImage(imintGovernment, 2, (image.getHeight() - imintGovernment.getHeight(null))/2, null);
			g2d.drawImage(imintMilitary, 2 + imintGovernment.getWidth(null) + 3, 
					(image.getHeight() - imintMilitary.getHeight(null))/2, null);
			ImageManager.addImage(IMINT_LAYER_ICON, new ImageIcon(image));

			//Create MOVINT layer icon
			Image movintSparse = ImageManager.getImage(MOVINT_SPARSE_ICON);
			Image movintDense = ImageManager.getImage(MOVINT_DENSE_ICON);
			image = new BufferedImage(2 + movintSparse.getWidth(null) + 3 + movintDense.getWidth(null) + 2, 
					Math.max(movintSparse.getHeight(null), movintDense.getHeight(null)) + 4, BufferedImage.TYPE_INT_ARGB);
			g2d = (Graphics2D)image.getGraphics();
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.drawImage(movintSparse, 2, (image.getHeight() - movintSparse.getHeight(null))/2, null);
			g2d.drawImage(movintDense, 2 + movintSparse.getWidth(null) + 3, 
					(image.getHeight() - movintDense.getHeight(null))/2, null);
			ImageManager.addImage(MOVINT_LAYER_ICON, new ImageIcon(image));

			//Create SIGINT layer icon
			Image sigintSilent = ImageManager.getImage(SIGINT_SILENT_ICON);
			Image sigintChatter = ImageManager.getImage(SIGINT_CHATTER_ICON);		
			image = new BufferedImage(2 + sigintSilent .getWidth(null) + 3 + sigintChatter.getWidth(null) + 2, 
					Math.max(sigintSilent.getHeight(null), sigintChatter.getHeight(null)) + 4, BufferedImage.TYPE_INT_ARGB);
			g2d = (Graphics2D)image.getGraphics();
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.drawImage(sigintSilent, 2, (image.getHeight() - sigintSilent.getHeight(null))/2, null);
			g2d.drawImage(sigintChatter, 2 + sigintSilent.getWidth(null) + 3, 
					(image.getHeight() - sigintChatter.getHeight(null))/2, null);
			ImageManager.addImage(SIGINT_LAYER_ICON, new ImageIcon(image));

			//Create SOCINT layer icon	
			placemark.setCenterLocation(center);
			placemark.setBorderLineWidth(0.f);
			placemark.setMarkerShape(PlacemarkShape.Square);
			placemark.setBackgroundColor(Color.LIGHT_GRAY);
			image = new BufferedImage(PLACEMARK_ICON_SIZE.width, PLACEMARK_ICON_SIZE.height, 
					BufferedImage.TYPE_INT_ARGB);
			g2d = (Graphics2D)image.getGraphics();
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			placemark.render(g2d, renderData, true);
			ImageManager.addImage(SOCINT_LAYER_ICON, new ImageIcon(image));	
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/** Creates the SIGACTs, GROUP CENTERs, IMINT, MOVINT, SIGINT, and SOCINT icons */
	private static void createINTTypeIcons() {
		try {	
			RenderProperties renderData = new RenderProperties(PLACEMARK_ICON_SIZE.width, PLACEMARK_ICON_SIZE.height, 0.3);
			renderData.setRenderBounds(0, 0, renderData.width_gridUnits, renderData.height_gridUnits);
			GridLocation2D center = renderData.getCenterGridLocation();
			
			//Create standard SIGACT location icons, group center icons, and SOCINT region icons
			String[] locations = {"?", "1", "2", "3", "4"};
			for(String location : locations) {	
				//Create attack location icon
				ImageManager.addImage("SIGACT_" + location, 
						new ImageIcon(createSigactLocationIcon(null, null, location, AttackLocationType.LOCATION, renderData)));
			}			
			for(GroupType group : GroupType.values()) {
				//Create group symbol and center icons
				ImageIcon groupSymbolIcon_large = null;
				ImageIcon groupSymbolIcon_small = null;
				if(WidgetConstants.USE_GROUP_SYMBOLS) {
					if(group != GroupType.Unknown && group != GroupType.X && group != GroupType.O) {						
						groupSymbolIcon_large = new ImageIcon(ImageIO.read(ImageManager.class.getClassLoader().getResourceAsStream(
								"images/phase_1/group_" + group.toString() + "_symbol_large.png")));
						ImageManager.addImage("GROUP_" + group.toString() + "_SYMBOL_" + IconSize.Large.toString(), groupSymbolIcon_large);
						groupSymbolIcon_small = new ImageIcon(ImageIO.read(ImageManager.class.getClassLoader().getResourceAsStream(
								"images/phase_1/group_" + group.toString() + "_symbol_small.png")));
						ImageManager.addImage("GROUP_" + group.toString() + "_SYMBOL_" + IconSize.Small.toString(), groupSymbolIcon_small);
					}							
				}
				
				if(group == GroupType.Unknown) {
					//Create group attack location icon for Unknown group
					ImageManager.addImage("SIGACT_" + group.toString(), ImageManager.getImageIcon("SIGACT_?"));
				} else {
					//Create group attack location icon
					ImageManager.addImage("SIGACT_" + group.toString(), 
							new ImageIcon(createSigactLocationIcon(group, 
									(groupSymbolIcon_small != null ? groupSymbolIcon_small.getImage() : null), 
									group.toString(), AttackLocationType.GROUP_ATTACK, renderData)));

					//Create group center icon
					//if(groupCenterIcon == null) {
					ImageManager.addImage("GROUP_CENTER_" + group.toString(), new ImageIcon(
							createGroupCenterIcon(group, 
									(WidgetConstants.USE_GROUP_SYMBOLS && groupSymbolIcon_large != null ? groupSymbolIcon_large.getImage() : null), 
									renderData)));
					//}					

					//Create SOCINT region icon
					ImageManager.addImage("SOCINT_" + group.toString(), new ImageIcon(
							createSocintRegionIcon(group, renderData)));
				}
			}			
			
			//Create IMINT icons
			ImageManager.addImage(IMINT_GOVERNMENT_ICON, 
					new ImageIcon(ImageIO.read(ImageManager.class.getClassLoader().getResourceAsStream("images/phase_1/government_icon_small.png"))));			
			ImageManager.addImage(IMINT_MILITARY_ICON, 
					new ImageIcon(ImageIO.read(ImageManager.class.getClassLoader().getResourceAsStream("images/phase_1/military_icon_small.png"))));
			
			//Create MOVINT icons		
			ImageManager.addImage(MOVINT_SPARSE_ICON, 
					new ImageIcon(ImageIO.read(ImageManager.class.getClassLoader().getResourceAsStream("images/phase_1/sparse_traffic_icon.png"))));
			ImageManager.addImage(MOVINT_DENSE_ICON, 
					new ImageIcon(ImageIO.read(ImageManager.class.getClassLoader().getResourceAsStream("images/phase_1/dense_traffic_icon.png"))));
			
			//Create SIGINT icons
			CircleShape sigintInner = new CircleShape(MapConstants.PLACEMARK_SIZE_PIXELS/2 - 4, CircleShape.RadiusType.Radius_Pixels);
			CircleShape sigintOuter = new CircleShape(MapConstants.PLACEMARK_SIZE_PIXELS/2, CircleShape.RadiusType.Radius_Pixels);
			sigintInner.setCenterLocation(center);
			sigintInner.setBorderColor(Color.black);
			sigintInner.setBorderLineWidth(2.f);
			sigintOuter.setCenterLocation(center);
			sigintOuter.setBorderColor(Color.black);
			sigintOuter.setBorderLineWidth(2.f);
			BufferedImage image = new BufferedImage(PLACEMARK_ICON_SIZE.width, PLACEMARK_ICON_SIZE.height, 
					BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = (Graphics2D)image.getGraphics();
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			sigintInner.render(g2d, renderData, true);
			sigintOuter.render(g2d, renderData, true);
			ImageManager.addImage(SIGINT_CHATTER_ICON, new ImageIcon(image));
			
			sigintInner.setBorderLineWidth(1.f);
			sigintInner.setBorderLineStyle(MapConstants_Phase1.SIGINT_SILENT_LINE_STYLE);
			image = new BufferedImage(PLACEMARK_ICON_SIZE.width, PLACEMARK_ICON_SIZE.height, 
					BufferedImage.TYPE_INT_ARGB);
			g2d = (Graphics2D)image.getGraphics();
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			sigintInner.render(g2d, renderData, true);
			ImageManager.addImage(SIGINT_SILENT_ICON, new ImageIcon(image));			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected static Image createSigactLocationIcon(GroupType group, Image groupSymbol, String locationName, 
			AttackLocationType locationType) {
		RenderProperties renderData = new RenderProperties(PLACEMARK_ICON_SIZE.width, PLACEMARK_ICON_SIZE.height, 0.3);
		renderData.setRenderBounds(0, 0, renderData.width_gridUnits, renderData.height_gridUnits);
		return createSigactLocationIcon(group, groupSymbol, locationName, locationType, renderData);
	}
	
	protected static Image createSigactLocationIcon(GroupType group, Image groupSymbol, String locationName,
			AttackLocationType locationType, RenderProperties renderData) {
		GridLocation2D location = renderData.getCenterGridLocation();
		location.setLocationId(locationName);
		boolean groupAttack = locationType == AttackLocationType.GROUP_ATTACK && group != null && group != GroupType.Unknown;
		AttackLocationPlacemark locationPlacemark = new AttackLocationPlacemark(new GroupAttack(group, location), locationName, 
				false, (groupAttack ? ColorManager_Phase1.getGroupCenterColor(group) : MapConstants.PLACEMARK_MARKER_COLOR), groupSymbol); 				
		BufferedImage image = new BufferedImage(PLACEMARK_ICON_SIZE.width, PLACEMARK_ICON_SIZE.height, 
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D)image.getGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		locationPlacemark.render(g2d, renderData, true);
		return image;
	}
	
	protected static Image createGroupCenterIcon(GroupType group, Image groupSymbol) {
		RenderProperties renderData = new RenderProperties(PLACEMARK_ICON_SIZE.width, PLACEMARK_ICON_SIZE.height, 0.3);
		renderData.setRenderBounds(0, 0, renderData.width_gridUnits, renderData.height_gridUnits);
		return createGroupCenterIcon(group, groupSymbol, renderData);
	}
	
	protected static Image createGroupCenterIcon(GroupType group, Image groupSymbol, RenderProperties renderData) {
		GroupCenterPlacemark groupCenterPlacemark = new GroupCenterPlacemark(
				new GroupCenter(group, renderData.getCenterGridLocation()), groupSymbol);
		if(groupSymbol != null) {
			groupCenterPlacemark.setBackgroundColor(Color.WHITE);
			groupCenterPlacemark.setBorderLineWidth(2f);
			groupCenterPlacemark.setBorderColor(ColorManager_Phase1.getGroupCenterColor(group));
		}
		BufferedImage image = new BufferedImage(PLACEMARK_ICON_SIZE.width, PLACEMARK_ICON_SIZE.height, 
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D)image.getGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(Color.white);
		g2d.fillRect(0, 0, image.getWidth(), image.getHeight());
		groupCenterPlacemark.render(g2d, renderData, true);
		return image;
	}
	
	protected static Image createSocintRegionIcon(GroupType group) {
		RenderProperties renderData = new RenderProperties(PLACEMARK_ICON_SIZE.width, PLACEMARK_ICON_SIZE.height, 0.3);
		renderData.setRenderBounds(0, 0, renderData.width_gridUnits, renderData.height_gridUnits);
		return createSocintRegionIcon(group, renderData);
	}
	
	protected static Image createSocintRegionIcon(GroupType group, RenderProperties renderData) {
		PlacemarkMapObject socintPlacemark = new PlacemarkMapObject();
		socintPlacemark.setTransparency(MapConstants_Phase1.REGION_TRANSPARENCY);
		socintPlacemark.setCenterLocation(renderData.getCenterGridLocation());
		socintPlacemark.setBorderLineWidth(0.f);
		socintPlacemark.setMarkerShape(PlacemarkShape.Square);
		socintPlacemark.setBackgroundColor(ColorManager_Phase1.getGroupRegionColor(group));
		BufferedImage image = new BufferedImage(PLACEMARK_ICON_SIZE.width, PLACEMARK_ICON_SIZE.height, 
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D)image.getGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		socintPlacemark.render(g2d, renderData, true);
		return image;
	}	
}