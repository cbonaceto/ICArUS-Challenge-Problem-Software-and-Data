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
package org.mitre.icarus.cps.app.widgets.phase_05.experiment;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.app.widgets.map.phase_05.FeatureVector;
import org.mitre.icarus.cps.app.widgets.map.phase_05.FeatureVectorPanel;
import org.mitre.icarus.cps.app.widgets.map.phase_05.Layer;
import org.mitre.icarus.cps.app.widgets.map.phase_05.LegendPanel;
import org.mitre.icarus.cps.exam.phase_05.IcarusExam_Phase05;
import org.mitre.icarus.cps.exam.phase_05.testing.scene_presentation.SceneItem;
import org.mitre.icarus.cps.exam.phase_05.training.AnnotationGridTrainingTrial;
import org.mitre.icarus.cps.exam.phase_05.training.AnnotationGridTrainingTrial.AnnotationGridColumn;
import org.mitre.icarus.cps.exam.phase_05.training.AnnotationGridTrainingTrial.AnnotationGridRow;
import org.mitre.icarus.cps.experiment_core.gui.JPanelConditionComponent;
import org.mitre.icarus.cps.feature_vector.phase_05.FeatureVectorManager;

/**
 * Panel that contains multiple annotated training grids.
 * 
 * @author CBONACETO
 *
 */
public class AnnotationGridPanel extends JPanelConditionComponent {

	private static final long serialVersionUID = 1L;
	
	private ArrayList<FeatureVectorPanel> imagePanels;

	public AnnotationGridPanel(AnnotationGridTrainingTrial annotationGrid, 
			IcarusExam_Phase05 exam, boolean showLayersAndLegend, int maxNumRows, int maxNumColumns) {
		super("AnnotationGrid");		
		setBackground(Color.white);
		setFont(WidgetConstants.FONT_DEFAULT);
		setLayout(new GridBagLayout());		
		
		//Add annotation grid
		JPanel gridPanel = new JPanel(new GridBagLayout());
		gridPanel.setBorder(WidgetConstants.DEFAULT_BORDER);
		GridBagConstraints gbc = new GridBagConstraints();		
		gbc.gridy = 0;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		
		imagePanels = new ArrayList<FeatureVectorPanel>(
				annotationGrid.getNumRows() * annotationGrid.getNumColumns());
		
		int maxWidth = 0;
		int maxHeight = 0;
		int rowNum = 0;		
		for(AnnotationGridRow row : annotationGrid.getRows()) {
			gbc.gridx = 0;
			gbc.weightx = 0;
			gbc.insets.left = 5;
			gbc.insets.right = 5;
			SceneItem sceneItem = exam.getSceneItem(row.getItemId());
			String rowName = null;
			if(sceneItem != null) {
				rowName = sceneItem.getItemName() + ":";
			}
			else {
				rowName = "Row " + gbc.gridy + ":";
			}			
			gridPanel.add(new JLabel(rowName), gbc);
			gbc.weightx = 1;
			gbc.gridx++;
			gbc.insets.left = 0;
			gbc.insets.right = 0;
			
			int columnNum = 0;
			for(AnnotationGridColumn column : row.getColumns()) {
				FeatureVector world = null;
				try {
					world = FeatureVectorManager.getInstance().getSingleSectorFeatureVector(
							column.getFeatureVectorUrl(), column.getObjectPaletteUrl(), 
							exam.getOriginalPath(), column.getSectorId());
				} catch(Exception ex) {
					System.err.println("Error loading feature vector!");
					ex.printStackTrace();
				}
				if(world != null) {
					world.getSectorLayer().setAlwaysEnabled(false);
					world.getSectorLayer().setEnabled(false);
					if(annotationGrid.getBaseLayers() != null) {
						//Enable any layer that is "always" enabled
						for (Layer layer : world.getLayers()) {
							layer.setEnabled(layer.isAlwaysEnabled());
						}
						//Now enable layers present in base layers						
						for(int layerId : annotationGrid.getBaseLayers()) {
							Layer featureLayer = world.getLayer(layerId);
							if(featureLayer != null) {
								featureLayer.setEnabled(true);
							}
						}
					}
					//world.setBounds(new Rectangle(0, 0, 100, 100));
					//System.out.println(world.getBounds());					
					if(world.getBounds().width > maxWidth) {
						maxWidth = world.getBounds().width;
					}
					if(world.getBounds().height > maxHeight) {
						maxHeight = world.getBounds().height;
					}
					
					JPanel borderPanel = new JPanel(new BorderLayout());
					borderPanel.setBorder(WidgetConstants.DEFAULT_BORDER);
					FeatureVectorPanel imagePanel = new FeatureVectorPanel(world);
					imagePanels.add(imagePanel);
					borderPanel.add(imagePanel);
					//imagePanel.setBorder(border);
					gridPanel.add(borderPanel, gbc);
					gbc.gridx++;
					columnNum++;
				}
			}
		
			if(columnNum < maxNumColumns) {
				//Add empty columns if current number of columns is less than maxNumColumns
				for( ; columnNum < maxNumColumns; columnNum++) {
					gridPanel.add(new JPanel(), gbc);
					gbc.gridx++;
				}
			}
			
			gbc.gridy++;
			rowNum++;
		}
		
		//Make all scenes the same size and square
		for(FeatureVectorPanel panel : imagePanels) {
			//int x = maxSize - origBounds.width + origBounds.x;
			//int y = maxSize - origBounds.height + origBounds.y;
			//int x = maxSize - origBounds.width;// + origBounds.x;
			//int y = maxSize - origBounds.height;// + origBounds.y;
			//panel.getWorld().getBounds().setBounds(x, y, origBounds.width, origBounds.height);			
			Rectangle origBounds = panel.getWorld().getBounds();
			panel.getWorld().getBounds().setBounds(origBounds.x, origBounds.y, maxWidth, maxHeight);
		}
	
		gbc.gridx = 1;
		if(rowNum < maxNumRows) {
			//Add empty rows if current number of rows is less than maxNumRows
			for( ; rowNum < maxNumRows; rowNum++) {
				gridPanel.add(new JPanel(), gbc);			
				gbc.gridy++;
			}
		}
		
		gbc.gridy = 0;
		
		//Add layers and legend panels
		if(showLayersAndLegend) {
			JPanel layerAndLegendPanel = createLayersAndLegendPanel(imagePanels);			
			gbc.weightx = 0;
			add(layerAndLegendPanel, gbc);			
		}
		
		//Add grid panel
		gbc.gridx++;
		gbc.weightx = 1;
		add(gridPanel, gbc);
		
		//Add resize listener to keep scenes same size and square
		/*gridPanel.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent arg0) {
				sizeImagePanels();
			}			
		});*/
	}
	
	/** Keep all scenes the same size and square */
	/*protected void sizeImagePanels() {
		if(!imagePanels.isEmpty()) {
			Dimension size = imagePanels.get(0).getSize();			
			int width = 0;
			if(size.width > size.height) {
				width = size.height;
			}
			else {
				width = size.width;
			}			
			//Rectangle worldBounds = new Rectangle((size.width-width)/2, 
			//		(size.height-width)/2, width, width);
			Rectangle worldBounds = new Rectangle(0, 0, 100, 100);
			//System.out.println(worldBounds);

			for(ImagePanel panel : imagePanels) {				
				//panel.getWorld().setBounds(worldBounds);
				panel.setPreferredSize(new Dimension(width, width));
			}
		}
		repaint();
	}*/
	
	protected JPanel createLayersAndLegendPanel(ArrayList<FeatureVectorPanel> imagePanels) {
		JPanel layerAndLegendPanel = new JPanel(new GridBagLayout());
		layerAndLegendPanel.setBorder(WidgetConstants.DEFAULT_BORDER);
		
		/*ImagePanel imagePanel = imagePanels.get(0);
		if(imagePanel.getWorld() != null && !imagePanel.getWorld().getLayers().isEmpty()) {
			//Enable any layer that is "always" enabled
			for (Layer layer : imagePanel.getLayers()) {
				layer.setEnabled(layer.isAlwaysEnabled() || availableLayers == null);
			}

			//Now enable layers present in availableLayers
			if(availableLayers != null && !availableLayers.isEmpty()) {
				for (LayerData layerData : availableLayers) {
					Layer featureLayer = imagePanel.getLayer(layerData.getLayerID());
					if(featureLayer != null) {
						featureLayer.setEnabled(true);
					}
				}		
			}
		}*/
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 0;
		gbc.weightx = 1;		
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.fill = GridBagConstraints.BOTH;		
		
		/*JLabel label = new JLabel(" Layers");
		label.setOpaque(true);
		label.setFont(GUIConstants.FONT_PANEL_TITLE);
		label.setHorizontalAlignment(JLabel.LEFT);
		gbc.weighty = 0;
		layerAndLegendPanel.add(label, gbc);
		
		gbc.gridy++;
		gbc.weighty = 0;
		layerAndLegendPanel.add(GUIConstants.createDefaultSeparator(), gbc);		
		
		JPanel layerPanel = imagePanel.createLayerPanel();
		layerPanel.setBackground(Color.WHITE);
		layerPanel.setBorder(BorderFactory.createEmptyBorder(8, 2, 5, 2));
		gbc.gridy++;
		gbc.weighty = .7;
		//layerAndLegendPanel.add(layerPanel, gbc);
		 
		gbc.gridy++;
		gbc.weighty = 0;
		layerAndLegendPanel.add(GUIConstants.createDefaultSeparator(), gbc);*/		
		
		JLabel label = new JLabel(" Legend");
		label.setOpaque(true);
		label.setFont(WidgetConstants.FONT_PANEL_TITLE);
		label.setHorizontalAlignment(JLabel.LEFT);
		gbc.gridy++;
		gbc.weighty = 0;
		layerAndLegendPanel.add(label, gbc);
		
		gbc.gridy++;
		gbc.weighty = 0;
		layerAndLegendPanel.add(WidgetConstants.createDefaultSeparator(), gbc);		
		
		JPanel legendPanel = LegendPanel.createDefaultLegendPanel();
		legendPanel.setBackground(Color.WHITE);
		legendPanel.setBorder(BorderFactory.createEmptyBorder(8, 2, 5, 2));
		gbc.gridy++;
		gbc.weighty = .3;
		layerAndLegendPanel.add(legendPanel, gbc);
		
		return layerAndLegendPanel;
	}
}
