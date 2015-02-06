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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.app.widgets.map.LayerListPanel;
import org.mitre.icarus.cps.app.widgets.map.phase_1.PlacemarkFactory.AttackLocationType;
import org.mitre.icarus.cps.app.widgets.map.phase_1.layers.AnnotationLayer;
import org.mitre.icarus.cps.app.widgets.map.phase_1.layers.CircleLayer;
import org.mitre.icarus.cps.app.widgets.map.phase_1.layers.ILayer_Phase1;
import org.mitre.icarus.cps.app.widgets.map.phase_1.layers.ImageOverlayLayer;
import org.mitre.icarus.cps.app.widgets.map.phase_1.layers.PlacemarkLayer;
import org.mitre.icarus.cps.app.widgets.map.phase_1.layers.RoadLayer;
import org.mitre.icarus.cps.app.widgets.map.phase_1.objects.AbstractMapObject_Phase1;
import org.mitre.icarus.cps.app.widgets.map.phase_1.objects.AttackLocationPlacemark;
import org.mitre.icarus.cps.app.widgets.map.phase_1.objects.CircleShape;
import org.mitre.icarus.cps.app.widgets.map.phase_1.objects.GroupCenterPlacemark;
import org.mitre.icarus.cps.app.widgets.map.phase_1.objects.ImageOverlayMapObject;
import org.mitre.icarus.cps.app.widgets.map.phase_1.objects.Phase1FeaturePlacemark;
import org.mitre.icarus.cps.app.widgets.map.phase_1.objects.RoadMapObject;
import org.mitre.icarus.cps.app.widgets.phase_1.ColorManager_Phase1;
import org.mitre.icarus.cps.app.widgets.phase_1.ImageManager_Phase1;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.GroupCirclesProbeResponse.GroupCircle;
import org.mitre.icarus.cps.experiment_core.condition.InstructionsPage;
import org.mitre.icarus.cps.feature_vector.phase_1.GridSize;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupAttack;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupCenter;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;
import org.mitre.icarus.cps.feature_vector.phase_1.IntType;
import org.mitre.icarus.cps.feature_vector.phase_1.Phase1Feature;
import org.mitre.icarus.cps.feature_vector.phase_1.Road;
import org.mitre.icarus.cps.feature_vector.phase_1.SocintOverlay;

/**
 * Contains a MapPanel and convenience methods for displaying INT layers on the map
 * for Tasks 1-7.  Also may contain an optional Layers panel and Legend panel.
 * 
 * @author CBONACETO
 *
 */
public class MapPanelContainer extends JPanel {
	
	private static final long serialVersionUID = 1180961875001709364L;	
	
	/** Layer type constants */
	public static enum LayerType {SIGACTS, ROADS, CENTERS, CIRCLES, IMINT, MOVINT, SIGINT, SOCINT};
	
	/** The left panel contains the layer panel and legend panel */
	protected JPanel leftPanel;
	
	/** The layer panel */
	protected JPanel layerPanelContainer;
	protected LayerListPanel layerPanel;
	
	/** The legend panel */
	protected JPanel legendPanelContainer;
	protected LegendPanel_Phase1 legendPanel;
	
	/** The map panel */
	protected MapPanel_Phase1 mapPanel;
	protected JPanel mapPanelContainer;
	
	/** The SIGACTs layer (Tasks 1-7) */
	protected PlacemarkLayer sigactLayer;
	
	/** The Road layer (Tasks 3-7) */
	protected RoadLayer roadLayer;
	
	/** The Group Centers layer (Tasks 3-7) */
	protected PlacemarkLayer groupCentersLayer;
	
	/** The Group Circles layer (Task 2). Note that this layer is not selectable or shown
	 * in the layers or legend panel */
	protected CircleLayer groupCirclesLayer;
	
	/** The IMINT layer (Tasks 5-7) */
	protected AnnotationLayer imintLayer;
	
	/** The MOVINT layer (Tasks 5-7) */
	protected AnnotationLayer movintLayer;
	
	/** The SIGINT layer (Tasks 5-7) */
	protected AnnotationLayer sigintLayer;
	
	/** The SOCINT region layer (Tasks 4-7) */
	protected ImageOverlayLayer regionLayer;	

	/**
	 * Constructor takes the parent component (if any), the grid size to use, whether the layer panel is visible,
	 * and whether the legend panel is visible.
	 * 
	 * @param parent
	 * @param gridSize
	 * @param layerPanelVisible
	 * @param legendPanelVisible
	 */
	public MapPanelContainer(Component parent, GridSize gridSize, boolean layerPanelVisible, boolean legendPanelVisible) {
		super(new GridBagLayout());
		
		//Create the map panel
		mapPanel = new MapPanel_Phase1(gridSize);
		mapPanel.setPreferredSize(new Dimension(MapConstants_Phase1.PREFERRED_MAP_WIDTH, MapConstants_Phase1.PREFERRED_MAP_HEIGHT));
		mapPanel.setBorder(BorderFactory.createEmptyBorder(MapConstants_Phase1.MAP_BORDER.top, MapConstants_Phase1.MAP_BORDER.left,
				MapConstants_Phase1.MAP_BORDER.bottom, MapConstants_Phase1.MAP_BORDER.right));
		mapPanel.setShowScale(true);
		mapPanelContainer = new JPanel(new GridBagLayout());		
		mapPanelContainer.setBackground(mapPanel.getBackground());
		mapPanelContainer.setBorder(WidgetConstants.DEFAULT_BORDER);
		mapPanel.setMapBorderLineWidth(1);
		mapPanel.setAlwaysShowMapBorder(true);
		
		//Initialize the map layers 
		mapPanel.setMapModel(createLayers());
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0; 
		gbc.gridy = 0;
		gbc.weightx = 0;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		
		//Create the layers and legend panels and add them to the layout
		createLeftPanel(parent);
		setLayerAndLegendPreferredWidth(204);
		//leftPanel.setMinimumSize(leftPanel.getPreferredSize());
		gbc.weightx = 0; 
		add(leftPanel, gbc);		
		layerPanelContainer.setVisible(layerPanelVisible);
		legendPanelContainer.setVisible(legendPanelVisible);
		leftPanel.setVisible(layerPanelVisible || legendPanelVisible);
		
		//Add the map panel to the layout
		gbc.weightx = 1;
		mapPanelContainer.add(mapPanel, gbc);
		gbc.weightx = 1;
		gbc.gridx++;	
		add(mapPanelContainer, gbc);		
	}
	
	public MapPanel_Phase1 getMapPanel() {
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
		//mapPanelContainer.setPreferredSize(new Dimension(size.width + insets.left + insets.right,
		//		size.height + insets.top + insets.bottom));
		mapPanel.setPreferredSize(size);
		setLeftPanelPreferredSize(new Dimension(leftPanel.getPreferredSize().width, 
				mapPanelContainer.getPreferredSize().height));
	}
	
	/**
	 * Set the minimum size of the map panel.
	 * 
	 * @param size the minimum size
	 */
	public void setMapMiniumSize(Dimension size) {
		mapPanel.setMinimumSize(size);
	}	
	
	/**
	 * Set the preferred width of the layer/legend panel.
	 * 
	 * @param width the preferred width
	 */
	public void setLayerAndLegendPreferredWidth(int width) {
		setLeftPanelPreferredSize(new Dimension(width, 
				mapPanelContainer.getPreferredSize().height));
	}
	
	protected void setLeftPanelPreferredSize(Dimension size) {
		leftPanel.setPreferredSize(size);
		layerPanelContainer.setPreferredSize(new Dimension(leftPanel.getPreferredSize().width, 
				(int)(leftPanel.getPreferredSize().height *0.45f)));
		legendPanelContainer.setPreferredSize(new Dimension(leftPanel.getPreferredSize().width, 
				(int)(leftPanel.getPreferredSize().height * .55f)));
	}
	
	/**
	 * Creates panel with the layer panel and legend panel
	 */
	protected void createLeftPanel(Component parentComponent) {		
		leftPanel = new JPanel(new GridBagLayout());
		leftPanel.setBackground(Color.white);
		leftPanel.setBorder(WidgetConstants.DEFAULT_BORDER);				
		
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
		layerPanel.addLayer(regionLayer, false);
		layerPanel.addLayer(roadLayer, false);
		layerPanel.addLayer(sigactLayer, false);
		layerPanel.addLayer(groupCentersLayer, false);
		layerPanel.addLayer(groupCirclesLayer, false);
		layerPanel.addLayer(imintLayer, false);
		layerPanel.addLayer(movintLayer, false);
		layerPanel.addLayer(sigintLayer, false);
		layerPanel.setBackground(Color.white);
		//layerPanel.setBorder(BorderFactory.createEmptyBorder(8, 2, 5, 2));
		gbc.gridy++;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		layerPanelContainer.add(layerPanel, gbc);
		
		gbc.gridy = 0;
		gbc.weighty = 0.45; //1
		leftPanel.add(layerPanelContainer, gbc);				
		
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
		
		legendPanel = new LegendPanel_Phase1();
		legendPanel.setBackground(Color.white);
		//legendPanel.setBorder(BorderFactory.createEmptyBorder(8, 2, 5, 2));		
		gbc.gridy++;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		legendPanelContainer.add(legendPanel, gbc);

		gbc.gridy = 1;
		gbc.weighty = 0.55; //1
		leftPanel.add(legendPanelContainer, gbc);		
	}
	
	/**
	 * Creates the map layers
	 */
	protected MapModel createLayers() {		
		MapModel layers = new MapModel();
		
		regionLayer = new ImageOverlayLayer("SOCINT");
		regionLayer.setIcon(ImageManager_Phase1.getImage(ImageManager_Phase1.SOCINT_LAYER_ICON));
		regionLayer.setEnabled(false);
		regionLayer.setVisible(false);				
		layers.addLayer(regionLayer);
		
		roadLayer = new RoadLayer("Roads");
		roadLayer.setIcon(ImageManager_Phase1.getImage(ImageManager_Phase1.ROADS_LAYER_ICON));
		roadLayer.setEnabled(false);
		roadLayer.setVisible(false);
		layers.addLayer(roadLayer);		
		
		sigactLayer = new PlacemarkLayer("SIGACTs", mapPanel);
		sigactLayer.setIcon(ImageManager_Phase1.getImage(ImageManager_Phase1.SIGACTS_LAYER_ICON));
		sigactLayer.setEnabled(false);
		sigactLayer.setVisible(false);
		layers.addLayer(sigactLayer);
		
		//groupCentersLayer = new PlacemarkLayer("HUMINT", mapPanel);
		groupCentersLayer = new PlacemarkLayer("Centers", mapPanel);
		groupCentersLayer.setIcon(ImageManager_Phase1.getImage(ImageManager_Phase1.GROUP_CENTERS_LAYER_ICON));
		groupCentersLayer.setEnabled(false);
		groupCentersLayer.setVisible(false);		
		layers.addLayer(groupCentersLayer);
		
		groupCirclesLayer = new CircleLayer("Group Circles", mapPanel);
		groupCirclesLayer.setIcon(ImageManager_Phase1.getImage(ImageManager_Phase1.GROUP_CIRCLES_LAYER_ICON));
		groupCirclesLayer.setEnabled(false);
		groupCirclesLayer.setVisible(false);
		layers.addLayer(groupCirclesLayer);
		
		imintLayer = new AnnotationLayer("IMINT");
		imintLayer.setIcon(ImageManager_Phase1.getImage(ImageManager_Phase1.IMINT_LAYER_ICON));
		imintLayer.setEnabled(false);
		imintLayer.setVisible(false);
		layers.addLayer(imintLayer);
		
		movintLayer = new AnnotationLayer("MOVINT");
		movintLayer.setIcon(ImageManager_Phase1.getImage(ImageManager_Phase1.MOVINT_LAYER_ICON));
		movintLayer.setEnabled(false);
		movintLayer.setVisible(false);
		layers.addLayer(movintLayer);
		
		sigintLayer = new AnnotationLayer("SIGINT");
		sigintLayer.setIcon(ImageManager_Phase1.getImage(ImageManager_Phase1.SIGINT_LAYER_ICON));
		sigintLayer.setEnabled(false);
		sigintLayer.setVisible(false);
		layers.addLayer(sigintLayer);		
		
		return layers;
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
	 * Redraw the map.  Call this after changing the map.
	 */
	public void redrawMap() {
		mapPanel.redraw();
	}
	
	/**
	 * Get the grid size.
	 * 
	 * @return the grid size
	 */
	public GridSize getGridSize() {
		return mapPanel.getGridSize();
	}
	
	/**
	 * Set the grid size.
	 * 
	 * @param gridSize the grid size
	 */
	public void setGridSize(GridSize gridSize) {
		mapPanel.setGridSize(gridSize);
	}
	
	/**
	 * Get whether to show the map scale.
	 * 
	 * @return whether to show the map scale
	 */
	public boolean isShowScale() {
		return mapPanel.isShowScale();
	}

	/**
	 * Set whether to show the map scale.
	 * 
	 * @param showScale whether to show the map scale
	 */
	public void setShowScale(boolean showScale) {
		mapPanel.setShowScale(showScale);
	}

	/**
	 * Translate the given distance in grid units to the pixel distance.
	 * 
	 * @param gridUnits the distance in grid units
	 * @return the distance in pixels
	 */
	public double translateToPixels(double gridUnits) {
		return mapPanel.translateToPixels(gridUnits);
	}	

	/**
	 * 
	 * 
	 * @param pixels
	 * @return
	 */
	public double translateToGridUnits(double pixels) {
		return mapPanel.translateToGridUnits(pixels);
	}	
	
	public double getPlacemarkSize_gridUnits() {
		return mapPanel.translateToGridUnits(MapConstants_Phase1.PLACEMARK_SIZE_PIXELS);
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
	
	public void setSigactLayerEnabled(boolean enabled) {
		if(sigactLayer.isEnabled() != enabled) {
			sigactLayer.setEnabled(enabled);
			sigactLayer.setVisible(enabled);
			layerPanel.setLayerEnabled(sigactLayer, enabled);			
		}
	}
	
	public void setSigactLayerInstructions(InstructionsPage instructions, String instructionsLinkText) {
		layerPanel.setLayerInstructions(sigactLayer, instructions, instructionsLinkText);
	}	
	
	public void showSigactLayerInstructions() {
		layerPanel.showInstructionsForLayer(sigactLayer);
	}
	
	public AttackLocationPlacemark addSigactLocation(GroupAttack attackLocation, AttackLocationType locationType, 
			boolean highlightLocation, boolean showINTsAtLocation) {
		AttackLocationPlacemark sigactPlacemark = PlacemarkFactory.createAttackLocationPlacemark(attackLocation, 
				locationType, highlightLocation);
		//new AttackLocationPlacemark(attackLocation, locationName, highlightLocation, markerColor);
		StringBuilder toolTip = new StringBuilder("<html>");
		if(attackLocation.getGroup() != null) {
			if(attackLocation.getGroup() == GroupType.Unknown) {
				toolTip.append("Unknown Group Attack");
			}
			else {
				toolTip.append("<u>" + attackLocation.getGroup().getGroupNameAbbreviated() + "</u>" + 
						attackLocation.getGroup().getGroupNameFull().substring(1));
				toolTip.append(" Attack");
			}
		}
		else if(attackLocation.getLocation() !=null && attackLocation.getLocation().getLocationId() != null) {
			toolTip.append("Location ");
			toolTip.append(attackLocation.getLocation().getLocationId());
		}
		else {
			toolTip.append("Unknown Group Attack");
		}
		if(showINTsAtLocation && attackLocation.getIntelReport() != null) {
			if(attackLocation.getIntelReport().getImintInfo() != null) {
				sigactPlacemark.setImintAnnotationVisible(true, imintLayer);
				toolTip.append("<br>IMINT: " + attackLocation.getIntelReport().getImintInfo().toString());
			}
			if(attackLocation.getIntelReport().getMovintInfo() != null) {
				sigactPlacemark.setMovintAnnotationVisible(true, movintLayer);
				toolTip.append("<br>MOVINT: " + attackLocation.getIntelReport().getMovintInfo().toString());
			}
			if(attackLocation.getIntelReport().getSocintInfo() != null) {
				toolTip.append("<br>SOCINT: Region " + attackLocation.getIntelReport().getSocintInfo().toString());
			}
		}
		toolTip.append("</html>");
		sigactPlacemark.setToolTipText(toolTip.toString());
		sigactLayer.addMapObject(sigactPlacemark);
		return sigactPlacemark;
	} 
	
	public Map<String, AttackLocationPlacemark> setSigactLocations(Collection<GroupAttack> attackLocations, 
			AttackLocationType locationsType, boolean showINTsAtLocations) {
		return setSigactLocations(attackLocations, locationsType, showINTsAtLocations, false);
	}
	
	//public Map<String, AttackLocationPlacemark> setSigactLocations(Collection<GroupAttack> attackLocations, 
	//		boolean showINTsAtLocations, boolean useGroupColor, Color defaultMarkerColor,
	//		boolean appendLocations) {
	public Map<String, AttackLocationPlacemark> setSigactLocations(Collection<GroupAttack> attackLocations, 
			AttackLocationType locationsType, boolean showINTsAtLocations, boolean appendLocations) {
		if(!appendLocations) {
			sigactLayer.removeAllMapObjects();
		}
		if(attackLocations != null) {
			Map<String, AttackLocationPlacemark> placemarks = new HashMap<String, AttackLocationPlacemark>(attackLocations.size());
			for(GroupAttack attackLocation : attackLocations) {				
				placemarks.put(attackLocation.getId(), 
						addSigactLocation(attackLocation, locationsType, false, showINTsAtLocations));
			}
			return placemarks;
		}
		return null;
	}
	
	public void removeSigactLocation(AttackLocationPlacemark placemark) {
		sigactLayer.removeMapObject(placemark);
	}
	
	public void setSigactsLegendItemVisible(boolean visible) {
		legendPanel.setSigactsItemVisible(visible);
	}
	
	/**
	 * Set the display name for the sigacts layer in the layer and legend panel.
	 * 
	 * @param name the display name
	 */
	public void setSigactsLayerAndLegendName(String name) {
		layerPanel.setLayerName(sigactLayer, name);
		legendPanel.setSigactsItemName(name);
	}
	
	public void setSigactLocationsInLegend(Collection<GroupAttack> attackLocationsForLegend) {
		legendPanel.setSigactLocations(attackLocationsForLegend);
	}
	
	public void removeAllSigactLocations() {
		sigactLayer.removeAllMapObjects();
	}
	
	public void setRoadLayerEnabled(boolean enabled) {
		if(roadLayer.isEnabled() != enabled) {
			roadLayer.setEnabled(enabled);
			roadLayer.setVisible(enabled);
			layerPanel.setLayerEnabled(roadLayer, enabled);			
		}
	}
	
	public void setRoadLayerInstructions(InstructionsPage instructions, String instructionsLinkText) {
		layerPanel.setLayerInstructions(roadLayer, instructions, instructionsLinkText);
	}
	
	public void showRoadLayerInstructions() {
		layerPanel.showInstructionsForLayer(roadLayer);
	}
	
	public Collection<RoadMapObject> setRoads(Collection<Road> roads) {
		roadLayer.removeAllMapObjects();
		if(roads != null) {
			for(Road road : roads) {
				roadLayer.addMapObject(new RoadMapObject(road));
			}
		}
		return roadLayer.getMapObjects();
	}
	
	public void removeAllRoads() {
		roadLayer.removeAllMapObjects();
	}
	
	public void setRoadsLegendItemVisible(boolean visible) {
		legendPanel.setRoadsItemVisible(visible);
	}
	
	public void setGroupCentersLayerEnabled(boolean enabled) {
		if(groupCentersLayer.isEnabled() != enabled) {
			groupCentersLayer.setEnabled(enabled);
			groupCentersLayer.setVisible(enabled);
			layerPanel.setLayerEnabled(groupCentersLayer, enabled);			
		}
	}
	
	public void setGroupCenterstLayerInstructions(InstructionsPage instructions, String instructionsLinkText) {
		layerPanel.setLayerInstructions(groupCentersLayer, instructions, instructionsLinkText);
	}
	
	public void showGroupCentersLayerInstructions() {
		layerPanel.showInstructionsForLayer(groupCentersLayer);
	}
	
	public void setGroupCentersLayerEditable(boolean editable) {
		groupCentersLayer.setEditable(editable);
	}
	
	public void addIntToFeaturePlacemark(Phase1FeaturePlacemark<?> placemark, IntType intType, boolean addAllInts) {
		if(placemark != null && placemark.getFeature() != null && placemark.getFeature().getIntelReport() != null) {
			Phase1Feature feature = placemark.getFeature();
			StringBuilder toolTip = new StringBuilder(placemark.getToolTipText());
			if(placemark.getToolTipText() != null && placemark.getToolTipText().endsWith("</html>")) {
				toolTip.delete(placemark.getToolTipText().length()-7, placemark.getToolTipText().length());
			}
			if((addAllInts || intType == IntType.IMINT) && feature.getIntelReport().getImintInfo() != null
					&& !placemark.isImintAnnotationVisible()) {
				placemark.setImintAnnotationVisible(true, imintLayer);
				toolTip.append("<br>IMINT: " + feature.getIntelReport().getImintInfo().toString());
			}
			if((addAllInts || intType == IntType.MOVINT) && feature.getIntelReport().getMovintInfo() != null
					&& !placemark.isMovintAnnotationVisible()) {
				placemark.setMovintAnnotationVisible(true, movintLayer);
				toolTip.append("<br>MOVINT: " + feature.getIntelReport().getMovintInfo().toString());
			}
			if((addAllInts || intType == IntType.SIGINT) && feature.getIntelReport().getSigintInfo() != null
					&& !placemark.isSigintAnnotationVisible()) {
				placemark.setSigintAnnotationVisible(true, sigintLayer);
				toolTip.append("<br>SIGINT: " + feature.getIntelReport().getSigintInfo().toString());
			}
			if((addAllInts || intType == IntType.SOCINT) && feature.getIntelReport().getSocintInfo() != null
					&& !placemark.isSigintAnnotationVisible()) {				
				toolTip.append("<br>SOCINT: Region " + feature.getIntelReport().getSocintInfo().toString());
			}
			toolTip.append("</html>");
			placemark.setToolTipText(toolTip.toString());
		}
	}	
	
	public GroupCenterPlacemark addGroupCenter(GroupCenter groupCenter, boolean showINTsAtGroupCenter, boolean editable) {
		GroupCenterPlacemark groupCenterPlacemark = PlacemarkFactory.createGroupCenterPlacemark(groupCenter, editable);
		StringBuilder toolTip = new StringBuilder("<html><u>" + groupCenter.getGroup().getGroupNameAbbreviated() + 
				"</u>" + groupCenter.getGroup().getGroupNameFull().substring(1) + " Center");		
		if(showINTsAtGroupCenter && groupCenter.getIntelReport() != null) {			
			if(groupCenter.getIntelReport().getImintInfo() != null) {
				groupCenterPlacemark.setImintAnnotationVisible(true, imintLayer);
				toolTip.append("<br>IMINT: " + groupCenter.getIntelReport().getImintInfo().toString());
			}
			if(groupCenter.getIntelReport().getMovintInfo() != null) {
				groupCenterPlacemark.setMovintAnnotationVisible(true, movintLayer);
				toolTip.append("<br>MOVINT: " + groupCenter.getIntelReport().getMovintInfo().toString());
			}
			if(groupCenter.getIntelReport().getSigintInfo() != null) {
				groupCenterPlacemark.setSigintAnnotationVisible(true, sigintLayer);
				toolTip.append("<br>SIGINT: " + groupCenter.getIntelReport().getSigintInfo().toString());
			}
			if(groupCenter.getIntelReport().getSocintInfo() != null) {				
				toolTip.append("<br>SOCINT: Region " + groupCenter.getIntelReport().getSocintInfo().toString());
			}
		}
		toolTip.append("</html>");
		groupCenterPlacemark.setToolTipText(toolTip.toString());
		groupCenterPlacemark.setShapeEdited(false);
		groupCentersLayer.addMapObject(groupCenterPlacemark);
		return groupCenterPlacemark;
	}
	
	public Map<GroupType, GroupCenterPlacemark> setGroupCenters(Collection<GroupCenter> groupCenters, boolean showINTsAtGroupCenters) {
		groupCentersLayer.removeAllMapObjects();
		if(groupCenters != null) {
			Map<GroupType, GroupCenterPlacemark> placemarks = new HashMap<GroupType, GroupCenterPlacemark>(groupCenters.size());
			for(GroupCenter groupCenter : groupCenters) {
				placemarks.put(groupCenter.getGroup(), 
						addGroupCenter(groupCenter, showINTsAtGroupCenters, false));
			}
			return placemarks;
		}
		return null;
	}
	
	public void removeAllGroupCenters() {
		groupCentersLayer.removeAllMapObjects();
	}
	
	public void setGroupCenterGroupsForLegend(Collection<GroupType> groups) {
		legendPanel.setGroupCenterGroups(groups);
	}
	
	public void setGroupCentersLegendItemVisible(boolean visible) {
		legendPanel.setGroupCentersItemVisible(visible);
	}	
	
	/**
	 * Set the display name for the group centers layer in the layer and legend panel..
	 * 
	 * @param name the display name
	 */
	public void setGroupCentersLayerAndLegendName(String name) {
		layerPanel.setLayerName(groupCentersLayer, name);
		legendPanel.setGroupCentersItemName(name);
	}
	
	public void setGroupCirclesLayerEnabled(boolean enabled) {
		if(groupCirclesLayer.isEnabled() != enabled) {
			groupCirclesLayer.setEnabled(enabled);
			groupCirclesLayer.setVisible(enabled);
			//layerPanel.setLayerEnabled(groupCirclesLayer, enabled);			
		}
	}
	
	public void setGroupCirclesLayerEditable(boolean editable) {
		groupCirclesLayer.setEditable(editable);
	}
	
	public CircleShape addGroupCircle(GroupCircle groupCircle, boolean editable) {
		CircleShape circle = new CircleShape(groupCircle.getRadius(), CircleShape.RadiusType.Radius_GridUnits);
		circle.setId(groupCircle.getGroup().toString());
		circle.setCenterLocation(groupCircle.getCenterLocation());
		Color groupColor = ColorManager_Phase1.getGroupCenterColor(groupCircle.getGroup());
		circle.setBorderColor(groupColor);
		circle.setControlPointBackgroundColor(groupColor);
		circle.setControlPointBorderColor(Color.DARK_GRAY);
		circle.setBorderLineWidth(1.5f);		
		circle.setEditable(editable);
		circle.setSelectable(false);
		circle.setShapeEdited(false);
		groupCirclesLayer.addMapObject(circle);
		return circle;
	}
		
	public Map<String, CircleShape> setGroupCircles(Collection<GroupCircle> groupCircles, boolean editable) {
		groupCirclesLayer.removeAllMapObjects();
		if(groupCircles != null) {
			Map<String, CircleShape> circles = new HashMap<String, CircleShape>(groupCircles.size());
			for(GroupCircle circle : groupCircles) {
				circles.put(circle.getGroup().toString(), 
						addGroupCircle(circle, editable));
			}
			return circles;
		}
		return null;
	}
	
	public void removeAllGroupCircles() {
		groupCirclesLayer.removeAllMapObjects();
	}
	
	public void setImintLayerEnabled(boolean enabled) {
		if(imintLayer.isEnabled() != enabled) {
			imintLayer.setEnabled(enabled);
			imintLayer.setVisible(enabled);
			layerPanel.setLayerEnabled(imintLayer, enabled);			
		}
	}
	
	public void setImintLayerInstructions(InstructionsPage instructions, String instructionsLinkText) {
		layerPanel.setLayerInstructions(imintLayer, instructions, instructionsLinkText);
	}
	
	public void showImintLayerInstructions() {
		layerPanel.showInstructionsForLayer(imintLayer);
	}
	
	public void setImintLegendItemVisible(boolean visible) {
		legendPanel.setImintItemVisible(visible);
	}
	
	public void setMovintLayerEnabled(boolean enabled) {
		if(movintLayer.isEnabled() != enabled) {
			movintLayer.setEnabled(enabled);
			movintLayer.setVisible(enabled);
			layerPanel.setLayerEnabled(movintLayer, enabled);		
		}
	}
	
	public void setMovintLayerInstructions(InstructionsPage instructions, String instructionsLinkText) {
		layerPanel.setLayerInstructions(movintLayer, instructions, instructionsLinkText);
	}
	
	public void showMovintLayerInstructions() {
		layerPanel.showInstructionsForLayer(movintLayer);
	}
	
	public void setMovintLegendItemVisible(boolean visible) {
		legendPanel.setMovintItemVisible(visible);
	}
	
	public void setSigintLayerEnabled(boolean enabled) {
		if(sigintLayer.isEnabled() != enabled) {
			sigintLayer.setEnabled(enabled);
			sigintLayer.setVisible(enabled);
			layerPanel.setLayerEnabled(sigintLayer, enabled);		
		}
	}
	
	public void setSigintLayerInstructions(InstructionsPage instructions, String instructionsLinkText) {
		layerPanel.setLayerInstructions(sigintLayer, instructions, instructionsLinkText);
	}
	
	public void showSigintLayerInstructions() {
		layerPanel.showInstructionsForLayer(sigintLayer);
	}
	
	public void setSigintLegendItemVisible(boolean visible) {
		legendPanel.setSigintItemVisible(visible);
	}
	
	public void setSocintRegionsLayerEnabled(boolean enabled) {
		if(regionLayer.isEnabled() != enabled) {
			regionLayer.setEnabled(enabled);
			regionLayer.setVisible(enabled);
			layerPanel.setLayerEnabled(regionLayer, enabled);			
		}
	}
	
	public void setSocintRegionsLayerInstructions(InstructionsPage instructions, String instructionsLinkText) {
		layerPanel.setLayerInstructions(regionLayer, instructions, instructionsLinkText);
	}
	
	public void showSocintRegionsLayerInstructions() {
		layerPanel.showInstructionsForLayer(regionLayer);
	}
	
	public ImageOverlayMapObject setSocintRegionsOverlay(SocintOverlay regionsOverlay) {
		regionLayer.removeAllMapObjects();
		if(regionsOverlay != null) {
			ImageOverlayMapObject imageOverlay = new ImageOverlayMapObject(
					regionsOverlay.createSocintOverlayImage(mapPanel.getGridSize()),
					new Rectangle2D.Double(0, mapPanel.getGridSize().getGridHeight(), mapPanel.getGridSize().getGridWidth(), 
							mapPanel.getGridSize().getGridHeight()));
			imageOverlay.setTransparency(MapConstants_Phase1.REGION_TRANSPARENCY);
			regionLayer.addMapObject(imageOverlay);
			return imageOverlay;
		}
		return null;
	}
	
	public void removeAllSocintRegions() {
		regionLayer.removeAllMapObjects();
	}
	
	public void setSocintGroupsForLegend(Collection<GroupType> groups) {
		legendPanel.setSocintGroups(groups);
	}
	
	public void setSocintLegendItemVisible(boolean visible) {
		legendPanel.setSocintItemVisible(visible);
	}
	
	public void setAdditionalINTLayersEnabled(boolean enabled) {
		setImintLayerEnabled(enabled);
		setMovintLayerEnabled(enabled);
		setSigintLayerEnabled(enabled);
		setSocintRegionsLayerEnabled(enabled);
	}
	
	/**
	 * Removes all map objects, disables all layers, removes all legend items, and restores the original layer z-order.
	 */
	public void resetMap() {
		removeAllMapObjects(false);
		disableAllLayers();
		removeAllLegendItems();
		mapPanel.getMapModel().restoreLayerZOrder();
	}
	
	/**
	 * Removes all objects from all map layers.
	 */
	public void removeAllMapObjects(boolean removeAdditionalINTLayers) {
		for(ILayer_Phase1<? extends AbstractMapObject_Phase1> layer : mapPanel.getMapModel().getLayers()) {
			layer.removeAllMapObjects();
		}
		if(removeAdditionalINTLayers) {
			setAdditionalINTLayersEnabled(false);
		}
		mapPanel.redraw();
	}
	
	public void disableAllLayers() {
		for(ILayer_Phase1<? extends AbstractMapObject_Phase1> layer : mapPanel.getMapModel().getLayers()) {
			layer.setEnabled(false);
			layerPanel.setLayerEnabled(layer, false);
		}		
	}
	
	public void removeAllLegendItems() {
		legendPanel.removeAllLegendItems();
	}	
	
	public void setLayerPanelVisibile(boolean visible) {		
		if(layerPanelContainer.isVisible() != visible) {
			layerPanelContainer.setVisible(visible);
			if(!visible && !legendPanelContainer.isVisible()) {
				leftPanel.setVisible(false);
			}		
			revalidate();
			repaint();
		}		
	}
	
	public void setLegendPanelVisible(boolean visible) {		
		if(legendPanelContainer.isVisible() != visible) {
			legendPanelContainer.setVisible(visible);
			if(!visible && !layerPanelContainer.isVisible()) {
				leftPanel.setVisible(false);
			}
			revalidate();
			repaint();
		}
	}	
	
	/**
	 * @param layerType
	 */
	public void moveLayerToBottomOfZOrder(LayerType layerType) {
		ILayer_Phase1<? extends AbstractMapObject_Phase1> layer = null;
		switch(layerType) {
		case SIGACTS: layer = sigactLayer; break;
		case ROADS: layer = roadLayer; break;
		case CENTERS: layer = groupCentersLayer; break; 
		case CIRCLES: layer = groupCirclesLayer; break;
		case IMINT: layer = imintLayer; break;
		case MOVINT: layer = movintLayer; break;
		case SIGINT: layer = sigintLayer; break;
		case SOCINT: layer = regionLayer; break;
		}		
		if(layer != null) {
			mapPanel.getMapModel().moveLayerToBottomOfZOrder(layer);
			mapPanel.redraw();
		}
	}
	
	/**
	 * 
	 */
	public void restoresLayerZOrder() {
		mapPanel.getMapModel().restoreLayerZOrder();
		mapPanel.redraw();
	}

	/** Test main */
	public static void main(String[] args) {
		JFrame frame = new JFrame("Map Test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationByPlatform(true);		
		
		//Add the map panel to the frame
		MapPanelContainer mapPanel = new MapPanelContainer(frame, new GridSize(), true, true);
		frame.getContentPane().add(mapPanel);		
		
		frame.pack();
		frame.setVisible(true);
	}
}