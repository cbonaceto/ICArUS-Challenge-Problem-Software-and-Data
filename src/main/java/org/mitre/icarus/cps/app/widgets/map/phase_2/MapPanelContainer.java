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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.app.widgets.map.LayerListPanel;
import org.mitre.icarus.cps.app.widgets.map.layers.ILayer;
import org.mitre.icarus.cps.app.widgets.map.objects.IMapObject;
import org.mitre.icarus.cps.app.widgets.map.phase_2.layers.AnnotationLayer;
import org.mitre.icarus.cps.app.widgets.map.phase_2.layers.CircleLayer;
import org.mitre.icarus.cps.app.widgets.map.phase_2.layers.ILayer_Phase2;
import org.mitre.icarus.cps.app.widgets.map.phase_2.layers.ImageOverlayLayer;
import org.mitre.icarus.cps.app.widgets.map.phase_2.layers.InformationBalloonLayer;
import org.mitre.icarus.cps.app.widgets.map.phase_2.layers.PlacemarkLayer;
import org.mitre.icarus.cps.app.widgets.map.phase_2.layers.PolygonLayer;
import org.mitre.icarus.cps.app.widgets.map.phase_2.layers.PolylineLayer;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.BlueLocationPlacemark;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.BlueLocationPlacemark.DisplayMode;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.BlueLocationPlacemark.DisplaySize;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.IMapObject_Phase2;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.ImageOverlay;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.Polygon;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.annotation.InformationBalloon;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.annotation.action_annotation.ActionBalloonContentRenderer;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.annotation.int_annotation.IntBalloonContentRenderer;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.annotation.int_annotation.IntBalloonContentRendererFactory;
import org.mitre.icarus.cps.app.widgets.map.phase_2.tile_factory.ImageScalingTileFactory;
import org.mitre.icarus.cps.app.widgets.phase_2.ColorManager_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.DatumType;
import org.mitre.icarus.cps.experiment_core.condition.InstructionsPage;
import org.mitre.icarus.cps.feature_vector.phase_2.AreaOfInterest;
import org.mitre.icarus.cps.feature_vector.phase_2.BlueLocation;
import org.mitre.icarus.cps.feature_vector.phase_2.Region;
import org.mitre.icarus.cps.feature_vector.phase_2.int_datum.IntDatum;

/**
 * Contains a MapPanel_Phase2 and an optional Layers panel and Legend panel.
 * Also contain convenience methods for displaying INT layers on the map.
 * 
 * @author CBONACETO
 *
 */
public class MapPanelContainer extends JPanel {	
	private static final long serialVersionUID = 2693530748365411306L;
	
	/** Layer type constants */
	public static enum LayerType {AreaOfInterest, Buildings, Terrain, BlueRegion, Osint, 
		Sigint, Imint, ImintDensityMap, BlueLocations, IntBanners, ActionBanners};
		
	/** The current tile factory */
	protected ImageScalingTileFactory tileFactory;

	/** Panel containing the layer panel and legend panel */
	protected JPanel layerAndLegendPanel;
	
	/** The layer panel */
	protected JPanel layerPanelContainer;
	protected LayerListPanel layerPanel;
	
	/** The legend panel */
	protected JPanel legendPanelContainer;
	protected LegendPanel_Phase2 legendPanel;
	
	/** The map panel */
	protected MapPanel_Phase2 mapPanel;
	protected JPanel mapPanelContainer;
	
	/** The current AOI */
	protected AreaOfInterest aoi;
	protected ImageOverlay aoiImageOverlay;
	
	/** The area of interest layer (contains the scene image with roads, and possibly buildings and terrain) */
	protected ImageOverlayLayer<MapPanel_Phase2> aoiLayer;
	
	/** The buildings layer (not currently used, consider removing) */
	protected PolygonLayer<MapPanel_Phase2> buildingLayer;
	
	/** The terrain layer (not currently used, consider removing) */
	protected PolygonLayer<MapPanel_Phase2> terrainLayer;
	
	/** The Blue region layer */
	protected PolygonLayer<MapPanel_Phase2> blueRegionLayer;
	
	/** The Blue locations layer */
	protected PlacemarkLayer<MapPanel_Phase2> blueLocationLayer;	
	
	/** The OSINT layer (contains blue lines drawn from Blue locations to blue border) */
	protected PolylineLayer<MapPanel_Phase2> osintLayer;
	
	/** The IMINT layer (contains IMINT circles around Blue locations showing radius around which density was computed) */
	protected CircleLayer<MapPanel_Phase2> imintLayer;
	
	/** The SIGINT layer (contains SIGINT circles showing SIGINT activity around blue locations) */
	protected CircleLayer<MapPanel_Phase2> sigintLayer;
	
	/** The INT text annotations layer (for OSINT text and IMINT text) */
	protected AnnotationLayer<MapPanel_Phase2> intAnnotationTextLayer;
	
	/** Layer for INT banners and action/outcome banners */
	protected InformationBalloonLayer<MapPanel_Phase2> intBannerLayer;
	
	/** Layer for action/outcome banners */
	protected InformationBalloonLayer<MapPanel_Phase2> actionBannerLayer;
	
	/** Current INT information banner(s) at one or more Blue location(s) */
	protected Map<BlueLocationPlacemark, InformationBalloon<IntDatum>> intBanners;
	
	/** Current action/outcome banners at one or more Blue location(s) */
	protected Map<BlueLocationPlacemark, InformationBalloon<BlueLocationPlacemark>> actionBanners;
	
	/**
	 * Constructor takes the parent component (if any), whether the layer panel is visible,
	 * and whether the legend panel is visible.
	 * 
	 * @param parent
	 * @param layerPanelVisible
	 * @param legendPanelVisible
	 */
	public MapPanelContainer(Component parent, boolean layerPanelVisible, boolean legendPanelVisible) {
		super(new GridBagLayout());
		intBanners = new HashMap<BlueLocationPlacemark, InformationBalloon<IntDatum>>();
		actionBanners = new HashMap<BlueLocationPlacemark, InformationBalloon<BlueLocationPlacemark>>();
		
		//Create the map panel
		mapPanel = new MapPanel_Phase2();			
		mapPanel.setPanEnabled(false);
		mapPanel.setZoomEnabled(false);
		mapPanel.setRecenterOnClickEnabled(false);
		mapPanel.setHorizontalWrapped(false);
		mapPanel.setRestrictOutsidePanning(true);
		//tileFactory = new ImageScalingTileFactory(792, 0.006296933792000914, 0, 5, 11);
		tileFactory = new ImageScalingTileFactory(600, 0.006296933792000914, 0, 5, 11);
		mapPanel.setTileFactory(tileFactory);
		mapPanel.setBackground(Color.WHITE);
		mapPanel.setPreferredSize(new Dimension(MapConstants_Phase2.PREFERRED_MAP_WIDTH, MapConstants_Phase2.PREFERRED_MAP_HEIGHT));
		mapPanel.setBorder(BorderFactory.createEmptyBorder(MapConstants_Phase2.MAP_BORDER_EMPTY.top, 
				MapConstants_Phase2.MAP_BORDER_EMPTY.left,
				MapConstants_Phase2.MAP_BORDER_EMPTY.bottom, 
				MapConstants_Phase2.MAP_BORDER_EMPTY.right));
		mapPanel.setShowScaleBar(true);
		mapPanelContainer = new JPanel(new GridBagLayout());		
		mapPanelContainer.setBackground(mapPanel.getBackground());
		mapPanelContainer.setBorder(WidgetConstants.DEFAULT_BORDER);		
		
		//Initialize the map layers 
		mapPanel.addLayers(createLayers());		
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0; 
		gbc.gridy = 0;
		gbc.weightx = 0;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		
		//Create the layers and legend panels and add them to the layout
		createLayerAndLegendPanel(parent);
		setLayerAndLegendPreferredWidth(204);
		//layerAndLegendPanel.setMinimumSize(layerAndLegendPanel.getPreferredSize());
		gbc.weightx = 0; 
		add(layerAndLegendPanel, gbc);		
		layerPanelContainer.setVisible(layerPanelVisible);
		legendPanelContainer.setVisible(legendPanelVisible);
		layerAndLegendPanel.setVisible(layerPanelVisible || legendPanelVisible);
		
		//Add the map panel to the layout
		gbc.weightx = 1;
		mapPanelContainer.add(mapPanel, gbc);
		gbc.weightx = 1;
		gbc.gridx++;	
		add(mapPanelContainer, gbc);
		
		//Add a resize listener to re-scale the map zoom if the panel size changes to properly scale
		//the area of interest image (if the area of interest has been set)		
		mapPanel.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				//System.out.println("map resized, " + mapPanel.getSize() + ", " + mapPanel.getPreferredSize() + ", " + mapPanelContainer.getPreferredSize());
				sizeMapToAOI();
			}
		});
	}
	
	/**
	 * Creates panel with the layer panel and legend panel
	 */
	protected void createLayerAndLegendPanel(Component parentComponent) {		
		layerAndLegendPanel = new JPanel(new GridBagLayout());
		layerAndLegendPanel.setBackground(Color.white);
		layerAndLegendPanel.setBorder(WidgetConstants.DEFAULT_BORDER);				
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 0;
		gbc.weightx = 1;		
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.fill = GridBagConstraints.HORIZONTAL;		
		
		//Create the layer panel
		layerPanelContainer = new JPanel(new GridBagLayout());
		layerPanelContainer.setBackground(Color.white);
		JLabel label = new JLabel(" Layers");
		label.setOpaque(true);
		label.setFont(WidgetConstants.FONT_PANEL_TITLE);
		label.setHorizontalAlignment(JLabel.LEFT);
		gbc.weighty = 0;
		layerPanelContainer.add(label, gbc);		
		
		gbc.gridy++;
		gbc.weighty = 0;
		layerPanelContainer.add(WidgetConstants.createDefaultSeparator(), gbc);		
		
		layerPanel = new LayerListPanel(mapPanel, parentComponent);
		
		layerPanel.addLayer(aoiLayer, false);
		layerPanel.addLayer(buildingLayer, false);
		layerPanel.addLayer(terrainLayer, false);
		layerPanel.addLayer(blueRegionLayer, false);
		layerPanel.addLayer(blueLocationLayer, false);
		layerPanel.addLayer(osintLayer, false);
		layerPanel.addLayer(imintLayer, false);
		layerPanel.addLayer(sigintLayer, false);
		layerPanel.addLayer(intAnnotationTextLayer, false);
		
		layerPanel.setBackground(Color.white);
		gbc.gridy++;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		layerPanelContainer.add(layerPanel, gbc);
		
		gbc.gridy = 0;
		gbc.weighty = 0.45; //1
		layerAndLegendPanel.add(layerPanelContainer, gbc);				
		
		//Create the legend panel
		legendPanelContainer = new JPanel(new GridBagLayout());
		legendPanelContainer.setBackground(Color.white);
		gbc.gridy = 0;
		gbc.weighty = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		legendPanelContainer.add(WidgetConstants.createDefaultSeparator(), gbc);
		
		label = new JLabel(" Legend");
		label.setOpaque(true);
		label.setFont(WidgetConstants.FONT_PANEL_TITLE);
		label.setHorizontalAlignment(JLabel.LEFT);
		gbc.gridy++;
		gbc.weighty = 0;
		legendPanelContainer.add(label, gbc);
		
		gbc.gridy++;
		gbc.weighty = 0;
		legendPanelContainer.add(WidgetConstants.createDefaultSeparator(), gbc);
		
		legendPanel = new LegendPanel_Phase2();
		legendPanel.setBackground(Color.white);
		gbc.gridy++;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		legendPanelContainer.add(legendPanel, gbc);

		gbc.gridy = 1;
		gbc.weighty = 0.55; //1
		layerAndLegendPanel.add(legendPanelContainer, gbc);		
	}
	
	/**
	 * Creates the map layers
	 */
	protected List<ILayer_Phase2<? extends IMapObject_Phase2, MapPanel_Phase2>> createLayers() {		
		List<ILayer_Phase2<? extends IMapObject_Phase2, MapPanel_Phase2>> layers =
				new LinkedList<ILayer_Phase2<? extends IMapObject_Phase2, MapPanel_Phase2>>();
		
		aoiLayer = new ImageOverlayLayer<MapPanel_Phase2>("Area of Interest", mapPanel);
		aoiLayer.setSelectable(false);
		//aoiLayer.setIcon(ImageManager_Phase2.getImage(ImageManager_Phase2.ROADS_LAYER_ICON));
		aoiLayer.setEnabled(false);
		aoiLayer.setVisible(false);
		layers.add(aoiLayer);
		
		buildingLayer = new PolygonLayer<MapPanel_Phase2>("Buildings", mapPanel);
		buildingLayer.setSelectable(false);
		buildingLayer.setEnabled(false);
		buildingLayer.setVisible(false);
		layers.add(buildingLayer);
		
		terrainLayer = new PolygonLayer<MapPanel_Phase2>("Terrain", mapPanel);
		terrainLayer.setSelectable(false);
		terrainLayer.setEnabled(false);
		terrainLayer.setVisible(false);
		layers.add(terrainLayer);		
		
		blueRegionLayer = new PolygonLayer<MapPanel_Phase2>("Blue Region", mapPanel);	
		blueRegionLayer.setSelectable(false);
		blueRegionLayer.setEnabled(false);
		blueRegionLayer.setVisible(false);
		layers.add(blueRegionLayer);		
		
		osintLayer = new PolylineLayer<MapPanel_Phase2>("OSINT", mapPanel);
		osintLayer.setSelectable(false);
		osintLayer.setEnabled(false);
		osintLayer.setVisible(false);
		layers.add(osintLayer);
		
		sigintLayer = new CircleLayer<MapPanel_Phase2>("SIGINT", mapPanel);
		sigintLayer.setSelectable(false);
		sigintLayer.setEnabled(false);
		sigintLayer.setVisible(false);
		layers.add(sigintLayer);		
		
		imintLayer = new CircleLayer<MapPanel_Phase2>("IMINT", mapPanel);
		imintLayer.setSelectable(false);
		imintLayer.setEnabled(false);
		imintLayer.setVisible(false);
		layers.add(imintLayer);
		
		blueLocationLayer = new PlacemarkLayer<MapPanel_Phase2>("Blue Locations", mapPanel);
		blueLocationLayer.setSelectable(false);
		blueLocationLayer.setEnabled(false);
		blueLocationLayer.setVisible(false);
		layers.add(blueLocationLayer);
		
		intAnnotationTextLayer = new AnnotationLayer<MapPanel_Phase2>("INT Data", mapPanel);
		intAnnotationTextLayer.setSelectable(false);
		intAnnotationTextLayer.setEnabled(false);
		intAnnotationTextLayer.setVisible(false);
		layers.add(intAnnotationTextLayer);
		
		intBannerLayer = new InformationBalloonLayer<MapPanel_Phase2>("INT information", mapPanel);
		intBannerLayer.setSelectable(false);
		intBannerLayer.setEnabled(false);
		intBannerLayer.setVisible(true);
		layers.add(intBannerLayer);
		
		actionBannerLayer = new InformationBalloonLayer<MapPanel_Phase2>("Action choices", mapPanel);
		actionBannerLayer.setSelectable(false);
		actionBannerLayer.setEnabled(false);
		actionBannerLayer.setVisible(true);
		layers.add(actionBannerLayer);
		
		return layers;
	}
	
	public MapPanel_Phase2 getMapPanel() {
		return mapPanel;
	}
	
	/**
	 * Set the preferred size of the map panel.  The width
	 * of the layer/legend panel stays the same, but the height is set to match
	 * the preferred height of the map panel.
	 * 
	 * @param size the preferred size
	 * @param insets
	 */
	public void setMapPreferredSize(Dimension size, Insets insets) {
		mapPanelContainer.setBorder(BorderFactory.createCompoundBorder(WidgetConstants.DEFAULT_BORDER,
				BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right)));
		mapPanel.setPreferredSize(size);
		setLayerAndLegendPanelPreferredSize(new Dimension(layerAndLegendPanel.getPreferredSize().width, 
				mapPanelContainer.getPreferredSize().height));
	}
	
	/**
	 * Set the preferred width of the layer/legend panel.
	 * 
	 * @param width the preferred width
	 */
	public void setLayerAndLegendPreferredWidth(int width) {
		setLayerAndLegendPanelPreferredSize(new Dimension(width, 
				mapPanelContainer.getPreferredSize().height));
	}
	
	protected void setLayerAndLegendPanelPreferredSize(Dimension size) {
		layerAndLegendPanel.setPreferredSize(size);
		layerPanelContainer.setPreferredSize(new Dimension(layerAndLegendPanel.getPreferredSize().width, 
				(int)(layerAndLegendPanel.getPreferredSize().height *0.45f)));
		legendPanelContainer.setPreferredSize(new Dimension(layerAndLegendPanel.getPreferredSize().width, 
				(int)(layerAndLegendPanel.getPreferredSize().height * .55f)));
	}	
	
	public void setAOILayerEnabled(boolean enabled) {
		setLayerEnabled(aoiLayer, enabled);
	}
	
	public void setAOILegendItemVisible(boolean visible) {
		legendPanel.setAoiItemVisible(visible);
	}
	
	public void setAOILayerInstructions(InstructionsPage instructions, String instructionsLinkText) {
		layerPanel.setLayerInstructions(aoiLayer, instructions, instructionsLinkText);
	}
	
	public void showAOILayerInstructions() {
		layerPanel.showInstructionsForLayer(aoiLayer);
	}
	
	/** Get the current area of interest */
	public AreaOfInterest getAOI() {
		return aoi;
	}
	
	/**
	 * Set the current area of interest. The map will automatically zoom to encompass the area.
	 * 
	 * @param aoi
	 */
	public void setAOI(AreaOfInterest aoi) {
		//TODO: Remove map objects from all layers?	
		if(aoiLayer.getMapObjects() != null && !aoiLayer.getMapObjects().isEmpty()) {
			for(ImageOverlay io : aoiLayer.getMapObjects()) {
				io.removeImage();
			}
		}
		aoiLayer.removeAllMapObjects();
		this.aoi = aoi;
		if(aoi != null) {
			GeoPosition topLeft = new GeoPosition(aoi.getTopLeftLat(), aoi.getTopLeftLon());
			GeoPosition bottomRight = new GeoPosition(aoi.getBottomRightLat(), aoi.getBottomRightLon()); 
			aoiImageOverlay = new ImageOverlay(aoi.getSceneImage(), topLeft, bottomRight);
			aoiImageOverlay.setBorderColor(Color.GRAY);
			aoiImageOverlay.setBorderLineWidth(1);
			aoiLayer.addMapObject(aoiImageOverlay);
			
			//May also want to populate building + terrain layers if buildings + terrain not in scene image

			//Create a new tile factory that correctly zooms the image if the current tile factory is incorrectly sized,
			//and center the map on the scene image and zoom to it
			sizeMapToAOI();
		} else {
			aoiImageOverlay = null;
			mapPanel.redraw();
		}
	}
	
	/** Resizes the map tile size (if necessary) to zoom to the AOI. Also centers the map on the AOI and zooms to it
	 * such that the AOI image encompasses the panel. */
	protected void sizeMapToAOI() {
		if(aoiImageOverlay != null && aoiImageOverlay.getImage() != null) {
			//Create a new tile factory that correctly zooms the image if the current tile factory is incorrectly sized
			GeoPosition topLeft = aoiImageOverlay.getTopLeft();
			GeoPosition bottomRight = aoiImageOverlay.getBottomRight();
			int panelWidth_pixels = mapPanel.getSize() != null && mapPanel.getSize().width > 0 ? 
					mapPanel.getSize().width : mapPanel.getPreferredSize().width;
			int panelHeight_pixels = mapPanel.getSize() != null && mapPanel.getSize().height > 0 ? 
					mapPanel.getSize().height : mapPanel.getPreferredSize().height;
			int imageWidth_pixels = aoiImageOverlay.getImage().getWidth(null);
			int imageHeight_pixels = aoiImageOverlay.getImage().getHeight(null);
			int width_pixels = imageWidth_pixels > 0 ? 
				Math.min(imageWidth_pixels, panelWidth_pixels) :
				mapPanel.getPreferredSize().width;
			//Dial down the width if necessary to be sure the image fits within the panel at the correct image aspect ratio
			int scaledImageHeight_pixels = (int)(imageHeight_pixels/(double)imageWidth_pixels * width_pixels);
			if(scaledImageHeight_pixels > panelHeight_pixels) {
				//Adjust the width
				width_pixels = (int)(imageWidth_pixels/(double)imageHeight_pixels * panelHeight_pixels);
			}
			//System.out.println("width pixels: " + width_pixels + ", " + imageWidth_pixels);
			double width_degrees =  Math.abs(topLeft.getLongitude() - bottomRight.getLongitude());			
			if(width_pixels > 0 && (tileFactory.getImageWidth_pixels() != width_pixels || 
					tileFactory.getImageWidth_degrees() != width_degrees)) {
				tileFactory = new ImageScalingTileFactory(width_pixels, width_degrees, 0, 5, 11);
				mapPanel.setTileFactory(tileFactory);
				mapPanel.redraw();
			}		

			//Center the map on the scene image and zoom to it
			mapPanel.setZoom(1);					
			mapPanel.setCenterPosition(new GeoPosition(
				bottomRight.getLatitude() + (topLeft.getLatitude() - bottomRight.getLatitude())/2.d, 
				bottomRight.getLongitude() + (topLeft.getLongitude() - bottomRight.getLongitude())/2.d));
			//HashSet<GeoPosition> positions = new HashSet<GeoPosition>();
			//positions.add(topLeft); positions.add(bottomRight);
			//mapPanel.calculateZoomFrom(positions);
			//mapPanel.setZoom(mapPanel.getZoom() - 1);
			mapPanel.redraw();
		}
	}
	
	/**
	 * @param enabled
	 */
	public void setBlueRegionLayerEnabled(boolean enabled) {
		setLayerEnabled(blueRegionLayer, enabled);
	}
	
	/**
	 * @param visible
	 */
	public void setBlueRegionLegendItemVisible(boolean visible) {
		legendPanel.setBlueRegionItemVisible(visible);
	}
	
	/**
	 * @param blueRegion
	 * @return
	 */
	public Polygon setBlueRegion(Region blueRegion) {
		blueRegionLayer.removeAllMapObjects();
		if(blueRegion != null) {
			Polygon polygon = new Polygon(blueRegion.getVertices());
			polygon.setBorderLineWidth(MapConstants_Phase2.BLUE_REGION_LINE_STYLE.getLineWidth());
			polygon.setBorderLineStyle(MapConstants_Phase2.BLUE_REGION_LINE_STYLE);			
			polygon.setBorderColor(ColorManager_Phase2.getColor(ColorManager_Phase2.BLUE_PLAYER));
			polygon.setForegroundColor(polygon.getBorderColor());
			if(MapConstants_Phase2.FILL_BLUE_REGION) {
				polygon.setBackgroundColor(polygon.getBorderColor());
				polygon.setTransparency(MapConstants_Phase2.BLUE_REGION_TRANSPARENCY);
			}
			blueRegionLayer.addMapObject(polygon);
			mapPanel.redraw();
			return polygon;
		}
		return null;
	}
	
	/**
	 * 
	 */
	public void removeBlueRegion() {
		blueRegionLayer.removeAllMapObjects();
		if(blueRegionLayer.isVisible()) {
			mapPanel.redraw();
		}
	}
	
	/**
	 * @param enabled
	 */
	public void setBlueLocationsLayerEnabled(boolean enabled) {
		setLayerEnabled(blueLocationLayer, enabled);
	}
	
	/**
	 * @param selectable
	 */
	public void setBlueLocationsLayerSelectable(boolean selectable) {
		blueLocationLayer.setSelectable(selectable);
	}
	
	/**
	 * @param visible
	 */
	public void setBlueLocationsLegendItemVisible(boolean visible) {
		legendPanel.setBlueLocationsItemVisible(visible);
	}
	
	/**
	 * @param blueLocation
	 */
	public void addBlueLocation(BlueLocationPlacemark placemark) {
		addBlueLocations(Collections.singleton(placemark), true);
	}
	
	/**
	 * @param blueLocations
	 * @param appendLocations
	 */
	public void addBlueLocations(Collection<BlueLocationPlacemark> placemarks, boolean appendLocations) {
		if(!appendLocations) {
			blueLocationLayer.removeAllMapObjects();
		}
		if(placemarks != null && !placemarks.isEmpty()) {
			for(BlueLocationPlacemark placemark : placemarks) {	
				blueLocationLayer.addMapObject(placemark);
			}
			//Update conflict objects set for INT banners and action banners
			updateINTAndActionBannerConflictObjects();
		}
		if(blueLocationLayer.isVisible()) {
			mapPanel.redraw();
		}
	}
	
	/**
	 * @param location
	 * @param displayName
	 * @param showName
	 * @param displayMode
	 * @param displaySize
	 * @return
	 */
	public BlueLocationPlacemark addBlueLocation(BlueLocation location, String displayName, boolean showName,
			DisplayMode displayMode, DisplaySize displaySize) {
		return addBlueLocation(location, displayName, showName, displayMode, displaySize, true, true);
	} 
	
	/**
	 * @param location
	 * @param displayName
	 * @param showName
	 * @param displayMode
	 * @param displaySize
	 * @param redrawMap
	 * @return
	 */
	protected BlueLocationPlacemark addBlueLocation(BlueLocation location, String displayName, boolean showName,
			DisplayMode displayMode, DisplaySize displaySize, boolean redrawMap, boolean updateBannerConflictObjects) {
		if(location != null) {
			BlueLocationPlacemark blueLocationPlacemark = new BlueLocationPlacemark(location, displayName, showName);
			blueLocationPlacemark.setDisplayMode(displayMode);
			blueLocationPlacemark.setDisplaySize(displaySize);			
			blueLocationLayer.addMapObject(blueLocationPlacemark);
			
			//Update conflict objects set for INT banners and action banners
			if(updateBannerConflictObjects) {
				updateINTAndActionBannerConflictObjects();
			}
			
			if(redrawMap && blueLocationLayer.isVisible()) {
				mapPanel.redraw();
			}
			
			return blueLocationPlacemark;
		}
		return null;
	}	
	
	/**
	 * @param blueLocations
	 * @param displayNames
	 * @param showNames
	 * @param displayMode
	 * @param displaySize
	 * @return
	 */
	public Map<String, BlueLocationPlacemark> addBlueLocations(Collection<BlueLocation> blueLocations, 
			Collection<String> displayNames, boolean showNames, DisplayMode displayMode, DisplaySize displaySize) {
		return addBlueLocations(blueLocations, displayNames, showNames, displayMode, displaySize, true);
	}
	
	/**
	 * @param blueLocations
	 * @param displayNames
	 * @param showNames
	 * @param displayMode
	 * @param displaySize
	 * @param appendLocations
	 * @return
	 */
	public Map<String, BlueLocationPlacemark> addBlueLocations(Collection<BlueLocation> blueLocations, 
			Collection<String> displayNames, boolean showNames, DisplayMode displayMode, DisplaySize displaySize,
			boolean appendLocations) {
		if(!appendLocations) {
			blueLocationLayer.removeAllMapObjects();
		}
		if(blueLocations != null && !blueLocations.isEmpty()) {
			Map<String, BlueLocationPlacemark> placemarks = new HashMap<String, BlueLocationPlacemark>(blueLocations.size());
			Iterator<String> nameIter = displayNames != null ? displayNames.iterator() : null; 
			for(BlueLocation blueLocation : blueLocations) {				
				String displayName = nameIter != null && nameIter.hasNext() ? nameIter.next() : null;
				placemarks.put(blueLocation.getId(), 
					addBlueLocation(blueLocation, displayName, showNames, displayMode, displaySize, false, false));
			}
			//Update conflict objects set for INT banners and action banners
			updateINTAndActionBannerConflictObjects();
			mapPanel.redraw();
			return placemarks;
		} else {
			mapPanel.redraw();
			return null;
		}
	}
	
	/**
	 * @param placemark
	 */
	public void removeBlueLocation(BlueLocationPlacemark placemark) {
		removeBlueLocations(Collections.singleton(placemark));
	}
	
	/**
	 * @param placemarks
	 */
	public void removeBlueLocations(Collection<BlueLocationPlacemark> placemarks) {
		if(placemarks != null && !placemarks.isEmpty()) {
			for(BlueLocationPlacemark placemark : placemarks) {
				blueLocationLayer.removeMapObject(placemark);
			}
			
			//Update conflict objects set for INT banners and action banners
			updateINTAndActionBannerConflictObjects();
			
			mapPanel.redraw();
		}
	}
	
	public void removeAllBlueLocations() {
		blueLocationLayer.removeAllMapObjects();
		
		//Update conflict objects set for INT banners and action banners
		updateINTAndActionBannerConflictObjects();
		
		mapPanel.redraw();
	}	
	
	/**
	 * @param editable
	 */
	public void setBlueLocationsEditable(boolean editable) {		
		blueLocationLayer.setEditable(editable);
		/*if(mapPanel.isEditing()) {
			mapPanel.deactivateEditor(mapPanel.editor);
		}
		PlacemarkEditor<MapPanel_Phase2> editor = new PlacemarkEditor<MapPanel_Phase2>();
		editor.editMapObject(placemark, blueLocationLayer, mapPanel);*/
	}
	
	/**
	 * Update conflict objects set for INT banners and action banners
	 */
	protected void updateINTAndActionBannerConflictObjects() {
		if(!intBanners.isEmpty()) {
			Set<IMapObject_Phase2> conflictObjects = createIntBannerConflictObjectsSet(intBanners.values());
			for(InformationBalloon<?> banner : intBanners.values()) {
				banner.setAutoAdjustOrientation(true, conflictObjects);
			}
		}
		if(!actionBanners.isEmpty()) {
			Set<IMapObject_Phase2> conflictObjects = createActionBannerConflictObjectsSet(actionBanners.values());
			for(InformationBalloon<?> banner : actionBanners.values()) {
				banner.setAutoAdjustOrientation(true, conflictObjects);
			}
		}
	}
	
	@SuppressWarnings("unused")
	public void addIntToBlueLocation(BlueLocationPlacemark blueLocation, DatumType intType, boolean addAllInts,
			boolean updateToolTip) {		
		if(blueLocation != null) {
			List<DatumType> intsAdded = new LinkedList<DatumType>();
			if(addAllInts || intType == DatumType.OSINT) {
				boolean osintAdded = false;
				if(MapConstants_Phase2.SHOW_OSINT_TEXT && !blueLocation.isOsintTextVisible()) {
					blueLocation.setOsintTextVisible(true, intAnnotationTextLayer);
					osintAdded = true;
				}
				if(MapConstants_Phase2.SHOW_OSINT_LINE && !blueLocation.isOsintLineVisible()) {					
					blueLocation.setOsintLineVisible(true, osintLayer);
					osintAdded = true;
				}				
				if(osintAdded) intsAdded.add(DatumType.OSINT);
			}
			if(addAllInts || intType == DatumType.IMINT) {
				boolean imintAdded = false;
				if(MapConstants_Phase2.SHOW_IMINT_TEXT && !blueLocation.isImintTextVisible()) {
					blueLocation.setImintTextVisible(true, intAnnotationTextLayer);
					imintAdded = true;
				}
				if(MapConstants_Phase2.SHOW_IMINT_CIRCLE && !blueLocation.isImintCircleVisible()) {
					blueLocation.setImintCircleVisible(true, imintLayer);
					imintAdded = true;
				}
				if(imintAdded) intsAdded.add(DatumType.IMINT);
			}
			if((addAllInts || intType == DatumType.HUMINT) && !blueLocation.isHumintVisible()) {
				if(MapConstants_Phase2.SHOW_HUMIMINT_TEXT) {
					blueLocation.setHumintTextVisible(true, intAnnotationTextLayer);
				} else {
					blueLocation.setHumintVisible(true);
				}
				intsAdded.add(DatumType.HUMINT);
			}
			if((addAllInts || intType == DatumType.SIGINT) && !blueLocation.isSigintCircleVisible()) {
				blueLocation.setSigintCircleVisible(true, sigintLayer);
				intsAdded.add(DatumType.SIGINT);
			}
			if(updateToolTip) {
				for(DatumType intAdded : intsAdded) {
					blueLocation.updateToolTip(intAdded);					
				}
			}
			mapPanel.redraw();
		}
	}		
	
	/**
	 * @param blueLocation
	 * @param visible
	 * @param intDatum
	 */
	public void setIntBannerVisibleAtBlueLocation(BlueLocationPlacemark blueLocation, boolean visible, IntDatum intDatum) {	
		InformationBalloon<IntDatum> intBanner = intBanners.get(blueLocation);
		if(intBanner == null && visible) {			
			intBanner = new InformationBalloon<IntDatum>(blueLocation, intDatum, 
					IntBalloonContentRendererFactory.createBalloonContentRenderer(intDatum.getDatumType(), null));
			intBanner.setShowCloseButton(false);
			intBannerLayer.addMapObject(intBanner);
			intBanners.put(blueLocation, intBanner);
			
			//Update conflict objects set for all INT banners
			Set<IMapObject_Phase2> conflictObjects = createIntBannerConflictObjectsSet(intBanners.values());
			for(InformationBalloon<?> banner : intBanners.values()) {
				banner.setAutoAdjustOrientation(true, conflictObjects);
			}
		}
		if(intBanner != null) {
			if(intDatum != null) {
				intBanner.setRenderer(IntBalloonContentRendererFactory.createBalloonContentRenderer(
						intDatum.getDatumType(), (IntBalloonContentRenderer)intBanner.getRenderer()));
				//DEBUG CODE
				/*if(intDatum.getDatumType() == DatumType.SIGINT) {
					System.out.println("Showing SIGINT banner: " + ((SigintDatum)intDatum).isRedActivityDetected());
					System.out.println("Renderer: " + intBanner.getRenderer());
				}*/
				//END DEBUG CODE
				intBanner.setContent(intDatum);
			}
			intBanner.setVisible(visible);
			if(intBannerLayer.isVisible()) {
				mapPanel.redraw();
			}
		}
	}	
		
	/**
	 * @param blueLocation
	 */
	public void removeIntBannerAtBlueLocation(BlueLocationPlacemark blueLocation) {
		InformationBalloon<?> intBanner = intBanners.get(blueLocation);
		if(intBanner != null) {
			intBannerLayer.removeMapObject(intBanner);
			intBanners.remove(blueLocation);
			if(!intBanners.isEmpty()) {
				//Update conflict objects set for remaining INT banners
				Set<IMapObject_Phase2> conflictObjects = createIntBannerConflictObjectsSet(intBanners.values());
				for(InformationBalloon<?> banner : intBanners.values()) {
					banner.setAutoAdjustOrientation(true, conflictObjects);
				}
			}
			if(intBannerLayer.isVisible()) {
				mapPanel.redraw();
			}
		}
	}
	
	/**
	 * 
	 */
	public void removeAllIntBanners() {
		intBanners.clear();
		intBannerLayer.removeAllMapObjects();
		if(intBannerLayer.isVisible()) {
			mapPanel.redraw();
		}
	}
	
	public void setActionBannerVisibleAtBlueLocation(BlueLocationPlacemark blueLocation, boolean visible, 
			boolean showBlueAction, boolean showRedAction, 
			boolean showBluePoints, boolean showRedPoints) {
		InformationBalloon<BlueLocationPlacemark> actionBanner = actionBanners.get(blueLocation);
		if(actionBanner == null && visible) {
			actionBanner = new InformationBalloon<BlueLocationPlacemark>(blueLocation, blueLocation, 
					new ActionBalloonContentRenderer());
			actionBanner.setShowCloseButton(false);
			actionBannerLayer.addMapObject(actionBanner);
			actionBanners.put(blueLocation, actionBanner);
			
			//Update conflict objects set for all action banners
			Set<IMapObject_Phase2> conflictObjects = createActionBannerConflictObjectsSet(actionBanners.values());
			for(InformationBalloon<?> banner : actionBanners.values()) {
				banner.setAutoAdjustOrientation(true, conflictObjects);
			}
		}
		if(actionBanner != null) {
			((ActionBalloonContentRenderer)actionBanner.getRenderer()).setShowBlueAndRedAction(
					showBlueAction, showRedAction, showBluePoints, showRedPoints);
			actionBanner.setVisible(visible);
			if(actionBannerLayer.isVisible()) {
				mapPanel.redraw();
			}
		}
	}	
	
	public void removeActionBannerAtBlueLocation(BlueLocationPlacemark blueLocation) {
		InformationBalloon<?> actionBanner = actionBanners.get(blueLocation);
		if(actionBanner != null) {
			actionBannerLayer.removeMapObject(actionBanner);
			actionBanners.remove(blueLocation);
			if(!actionBanners.isEmpty()) {
				//Update conflict objects set for remaining action banners
				Set<IMapObject_Phase2> conflictObjects = createActionBannerConflictObjectsSet(actionBanners.values());
				for(InformationBalloon<BlueLocationPlacemark> banner : actionBanners.values()) {
					banner.setAutoAdjustOrientation(true, conflictObjects);
				}
			}
			if(actionBannerLayer.isVisible()) {
				mapPanel.redraw();
			}
		}
	}
	
	public void removeAllActionBanners() {
		actionBanners.clear();
		actionBannerLayer.removeAllMapObjects();
		if(actionBannerLayer.isVisible()) {
			mapPanel.redraw();
		}
	}
	
	/**
	 * @param banners
	 * @return
	 */
	protected Set<IMapObject_Phase2> createIntBannerConflictObjectsSet(Collection<InformationBalloon<IntDatum>> banners) {
		Set<IMapObject_Phase2> conflictObjects = new HashSet<IMapObject_Phase2>();
		conflictObjects.addAll(banners);
		conflictObjects.addAll(blueLocationLayer.getMapObjects());
		return conflictObjects;
	}
	
	/**
	 * @param banners
	 * @return
	 */
	protected Set<IMapObject_Phase2> createActionBannerConflictObjectsSet(Collection<InformationBalloon<BlueLocationPlacemark>> banners) {
		Set<IMapObject_Phase2> conflictObjects = new HashSet<IMapObject_Phase2>();
		conflictObjects.addAll(banners);
		conflictObjects.addAll(blueLocationLayer.getMapObjects());
		return conflictObjects;
	}
	
	/**
	 * @param visible
	 */
	public void setBatchPlotLegendItemVisible(boolean visible) {
		legendPanel.setBatchPlotItemVisible(visible);
	}
	
	public void setIntAnnotationTextLayerEnabled(boolean enabled) {
		setLayerEnabled(intAnnotationTextLayer, enabled);
	}
	
	public void setOsintLineLayerEnabled(boolean enabled) {		
		setLayerEnabled(osintLayer, enabled);
	}
	
	public void setOsintLegendItemVisible(boolean visible) {
		legendPanel.setOsintItemVisible(visible);
	}
	
	public void setImintCircleLayerEnabled(boolean enabled) {
		setLayerEnabled(imintLayer, enabled);
	}
	
	public void setImintLegendItemVisible(boolean visible) {
		legendPanel.setImintItemVisible(visible);
	}
	
	public void setSigintLayerEnabled(boolean enabled) {
		setLayerEnabled(sigintLayer, enabled);
	}
	
	public void setSigintLegendItemVisible(boolean visible) {
		legendPanel.setSigintItemVisible(visible);
	}
	
	protected void setLayerEnabled(ILayer<? extends IMapObject> layer, boolean enabled) {
		if(layer != null && layer.isEnabled() != enabled) {
			layer.setEnabled(enabled);
			layer.setVisible(enabled);
			layerPanel.setLayerEnabled(layer, enabled);
			mapPanel.redraw();
		}
	}
	
	public boolean isLayerPanelVisible() {
		return layerPanelContainer.isVisible();
	}
	
	public void setLayerPanelVisibile(boolean visible) {		
		if(layerPanelContainer.isVisible() != visible) {
			layerPanelContainer.setVisible(visible);
			boolean layerAndLegendPanelVisible = layerPanelContainer.isVisible() || legendPanelContainer.isVisible();
			if(layerAndLegendPanel.isVisible() != layerAndLegendPanelVisible) {
				layerAndLegendPanel.setVisible(layerAndLegendPanelVisible);
			}		
			revalidate();
			repaint();
		}		
	}
	
	public boolean isLegendPanelVisible() {
		return legendPanelContainer.isVisible();
	}
	
	public void setLegendPanelVisible(boolean visible) {		
		if(legendPanelContainer.isVisible() != visible) {
			legendPanelContainer.setVisible(visible);
			boolean layerAndLegendPanelVisible = layerPanelContainer.isVisible() || legendPanelContainer.isVisible();
			if(layerAndLegendPanel.isVisible() != layerAndLegendPanelVisible) {
				layerAndLegendPanel.setVisible(layerAndLegendPanelVisible);
			}			
			revalidate();
			repaint();
		}
	}	
	
	/**
	 * Get whether to show the map scale bar.
	 * 
	 * @return whether to show the map scale bar
	 */
	public boolean isShowScaleBar() {
		return mapPanel.isShowScaleBar();
	}

	/**
	 * Set whether to show the map scale bar.
	 * 
	 * @param showScale whether to show the map scale bar
	 */
	public void setShowScaleBar(boolean showScaleBar) {
		mapPanel.setShowScaleBar(showScaleBar);
	}	
	
	public boolean isToolTipsEnabled() {
		return mapPanel.isToolTipsEnabled();
	}

	public void setToolTipsEnabled(boolean toolTipsEnabled) {
		mapPanel.setToolTipsEnabled(toolTipsEnabled);
	}
	
	public boolean isHoverToolTipsEnabled() {
		return mapPanel.isHoverToolTipsEnabled();
	}

	public void setHoverToolTipsEnabled(boolean hoverToolTipsEnabled) {
		mapPanel.setHoverToolTipsEnabled(hoverToolTipsEnabled);
	}
	
	public boolean isMultipleToolTipsEnabled() {
		return mapPanel.isMultipleToolTipsEnabled();
	}

	public void setMultipleToolTipsEnabled(boolean multipleToolTipsEnabled) {
		mapPanel.setMultipleToolTipsEnabled(multipleToolTipsEnabled);
	}
	
	public boolean isPanEnabled() {
		return mapPanel.isPanEnabled();
	}
	
	public void setPanEnabled(boolean panEnabled) {
		if(panEnabled != mapPanel.isPanEnabled()) {
			mapPanel.setPanEnabled(panEnabled);
		}
	}
	
	public boolean isZoomEnabled() {
		return mapPanel.isZoomEnabled();
	}
	
	public void setZoomEnabled(boolean zoomEnabled) {
		if(zoomEnabled != mapPanel.isZoomEnabled()) {
			mapPanel.setZoomEnabled(zoomEnabled);
		}
	}
	
	/**
	 * @return
	 */
	public RenderProperties getCurrentRenderProperties() {
		return mapPanel.getCurrentRenderProperties();
	}	
	
	/**
	 * @param layerType
	 */
	public void moveLayerToBottomOfZOrder(LayerType layerType) {
		ILayer_Phase2<? extends IMapObject_Phase2, MapPanel_Phase2> layer = null;
		switch(layerType) {
		case AreaOfInterest: layer = aoiLayer; break;
		case Buildings: layer = buildingLayer; break;
		case Terrain: layer = terrainLayer; break;
		case BlueLocations: layer = blueLocationLayer; break;
		case BlueRegion: layer = blueRegionLayer; break;
		case Imint: layer = imintLayer; break;
		case ImintDensityMap: layer = null; break;
		case Osint: layer = osintLayer; break;
		case Sigint: layer = sigintLayer; break;
		case IntBanners: layer = intBannerLayer; break;
		case ActionBanners: layer = actionBannerLayer; break;
		default: layer = null; break;				
		}		
		if(layer != null) {
			mapPanel.getLayerManager().moveLayerToBottomOfZOrder(layer);
			mapPanel.redraw();
		}
	}
	
	/**
	 * 
	 */
	public void restoresLayerZOrder() {
		mapPanel.getLayerManager().restoreLayerZOrder();
		mapPanel.redraw();
	}
	
	/** Set whether instructions links should be shown in the layers panel. */
	public void setLayerInstructionsEnabled(boolean enabled) {
		layerPanel.setLayerInstructionsEnabled(enabled);
	}	
	
	/**
	 * Set whether the layer instructions window is visible.
	 * 
	 * @param visible
	 */
	public void setLayerInstructionsWindowVisible(boolean visible) {
		layerPanel.setLayerInstructionsWindowVisible(visible);
	}
	
	/**
	 * Add an instructions page not associated with a layer.
	 * 
	 * @param pageName
	 * @param instructions
	 */
	public void addInstructionsPage(String pageName, InstructionsPage instructions) {
		layerPanel.addInstructionsPage(pageName, instructions);
	}
	
	/**
	 * Remove an instructions page not associated with a layer.
	 * 
	 * @param pageName
	 */
	public void removeInstructionsPage(String pageName) {
		layerPanel.removeInstructionsPage(pageName);
	}
	
	/**
	 * Show an instructions page not associated with a layer.
	 * 
	 * @param pageName
	 */
	public void showInstructionsPage(String pageName) {
		layerPanel.showInstructionsPage(pageName);
	}	
	
	/**
	 * Redraw the map.  Call this after changing the map.
	 */
	public void redrawMap() {
		mapPanel.redraw();
	}
	
	/**
	 * Removes all map objects, disables all layers, removes all legend items, and restores the original layer z-order.
	 */
	public void resetMap() {
		removeAllMapObjects(false);
		disableAllLayers();
		removeAllLegendItems();
		mapPanel.getLayerManager().restoreLayerZOrder();
	}
	
	/**
	 * Removes all objects from all map layers.
	 */
	public void removeAllMapObjects(boolean removeAdditionalINTLayers) {
		for(ILayer_Phase2<? extends IMapObject_Phase2, MapPanel_Phase2> layer : mapPanel.getLayerManager()) {
			layer.removeAllMapObjects();
		}
		/*if(removeAdditionalINTLayers) {
			setAdditionalINTLayersEnabled(false);
		}*/
		mapPanel.redraw();
	}
	
	public void disableAllLayers() {
		for(ILayer_Phase2<? extends IMapObject_Phase2, MapPanel_Phase2> layer : mapPanel.getLayerManager()) {
			layer.setEnabled(false);
			layerPanel.setLayerEnabled(layer, false);
		}		
	}
	
	public void removeAllLegendItems() {
		legendPanel.removeAllLegendItems();
	}	
}