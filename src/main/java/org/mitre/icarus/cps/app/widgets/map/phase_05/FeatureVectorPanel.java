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
package org.mitre.icarus.cps.app.widgets.map.phase_05;

import java.awt.*;
import java.util.Collection;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;

import org.mitre.icarus.cps.app.widgets.map.phase_05.LayerTree.LayerTreeNodeEditor;
import org.mitre.icarus.cps.app.widgets.map.phase_05.LayerTree.LayerTreeNodeRenderer;
import org.mitre.icarus.cps.app.widgets.phase_05.ColorManager;
import org.mitre.icarus.cps.experiment_core.gui.IConditionComponent;

/**
 * A panel for drawing all layers in a feature vector.
 * 
 * @author Jing Hu
 */
public class FeatureVectorPanel extends JPanel implements IConditionComponent {
	private static final long serialVersionUID = 1L;
	
	/** The feature vector to render */
	private FeatureVector world;
	
	/** The condition component ID (enables this component to be used as a component
	 * in an experiment panel). */
	private String componentId;
	
	public FeatureVectorPanel() {
		world = new FeatureVector(null);
	}
	
	public FeatureVectorPanel(FeatureVector world) {
		this.world = world;
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment_core.gui.IConditionComponent#getComponent()
	 */
	@Override
	public JComponent getComponent() {
		return this;
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment_core.gui.IConditionComponent#getComponentId()
	 */
	@Override
	public String getComponentId() {
		return componentId;
	}
	
	/**
	 * Set the component ID.
	 * 
	 * @param componentId
	 */
	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}
	
	@Override
	protected void paintComponent(Graphics gfx) {
		//super.paintComponent(gfx);		
		Graphics2D g = (Graphics2D) gfx;
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		
		g.setColor(ColorManager.get(ColorManager.BACKGROUND));
		Insets insets = getInsets();
		int width = getWidth() - insets.left - insets.right;
		int height = getHeight() - insets.top - insets.bottom;
		g.fillRect(insets.left, insets.top, width, height);
		
		Rectangle bounds = world.getBounds();
		
		//FIXME: Figure out how to account for bounds.x and bounds.y
		int colCount = bounds.x + bounds.width; //Jing's original code
		int rowCount = bounds.y + bounds.height; //Jing's original code
		//int colCount = bounds.width;
		//int rowCount = bounds.height;
		
		double xStep = ((double) getWidth())/colCount;
		double yStep = ((double) getHeight())/rowCount;
		
		// Keep world square (added by CAB)
		double startX = insets.left; 
		double startY = insets.top;
		if(xStep < yStep) {
			yStep = xStep;
			startY = (height - yStep * rowCount)/2;
		}
		else {
			xStep = yStep;
			startX = (width - xStep * colCount)/2;
		}		
		
		//Re-center the world in the panel if it wasn't square (added by CAB)
		//g.translate(startX, startY); //Jing's original code		
		g.translate(startX, startY);
		
		// draw the scene
		//System.out.println(xStep + ", " + yStep);
		RenderData r = new RenderData(xStep, yStep, width, height, rowCount, colCount);
		world.render(g, r);
		
		// Draw axis
		/*
		g.setColor(Color.BLACK);
		g.drawLine(0, getHeight()/2, getWidth(), getHeight()/2);
		g.drawLine(getWidth()/2, 0, getWidth()/2, getHeight());
		*/
	}
	
	/**
	 * Get the layer in the feature vector with the given layer ID.
	 * 
	 * @param layerId
	 * @return
	 */
	public Layer getLayer(Integer layerId) {
		return world.getLayer(layerId);
	}
	
	/**
	 * Get all layers in the feature vector.
	 * 
	 * @return
	 */
	public Collection<Layer> getLayers() {
		return world.getLayers();
	}
	
	/**
	 * Get all sectors in the feature vector.
	 * 
	 * @return
	 */
	public Collection<Sector> getSectors() {
		if(world.getSectorLayer() != null) {
			return world.getSectorLayer().getChildren();
		}
		return null;
	}
	
	/**
	 * Get the sector in the feature vector with the given sector ID.
	 * 
	 * @param sectorId
	 * @return
	 */
	public Sector getSector(Integer sectorId) {
		if(world.getSectorLayer() != null) {
			return world.getSectorLayer().getSector(sectorId);
		}
		return null;
	}
	
	/**
	 * Get the feature vector to render.
	 * 
	 * @return
	 */
	public FeatureVector getWorld() {
		return world;
	}

	/**
	 * Set the feature vector to render.
	 * 
	 * @param world
	 */
	public void setWorld(FeatureVector world) {
		this.world = world;
		
		//world.setBounds(new Rectangle(0, 0, getPreferredSize().width, getPreferredSize().height));
		
		/*
		//Add standard layersRectangle bounds = world.getBounds();
		Gridlines grid = new Gridlines(900);
		grid.setUserSelectable(false);
		grid.setVisible(false);
		grid.getBounds().x -= bounds.x;
		grid.getBounds().y -= bounds.y;
		world.addLayer(grid);
		
		OverlayLayer overlay = new OverlayLayer(901);
		overlay.setUserSelectable(false);
		overlay.getBounds().x -= bounds.x;
		overlay.getBounds().y -= bounds.y;
		world.addLayer(overlay);*/
		
		//world.addLayer(new AnnotationLayer());
		
		//setPreferredSize(new Dimension(bounds.width*4, bounds.height*4));
	}
	
	/**
	 * Create a layer selection panel for this feature vector panel.
	 * 
	 * @return
	 */
	public JPanel createLayerPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		
		LayerTree tree = new LayerTree();
		final JTree jTree = new JTree(tree.createLayerTree(world));
		jTree.setRootVisible(false);
		jTree.setCellRenderer(new LayerTreeNodeRenderer());
		jTree.setCellEditor(new LayerTreeNodeEditor(jTree, this));
		jTree.setEditable(true);
				
		JScrollPane sp = new JScrollPane(jTree);
		sp.setBorder(null);
		panel.add(sp);		
		
		return panel;
	}
}
