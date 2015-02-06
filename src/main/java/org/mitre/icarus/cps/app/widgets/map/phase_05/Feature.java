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

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.util.Collection;



/**
 * An object that can be rendered in the map UI
 * @author JINGHU
 *
 */
public abstract class Feature {
	
	/** Feature bounds */
	private Rectangle bounds = new Rectangle();	
	
	/** Whether the feature is visible */
	private boolean visible = true;
	
	/** Whether the feature is enabled */
	private boolean enabled = true;
	
	/** Transparency value of the feature */
	private float transparency = 1.f;
	
	/** Feature name */
	private String name;
	
	/** Parent feature (if any) */
	protected Feature parent;
	
	/** Id of the layer the feature is in */
	protected Integer layerId;
	
	public Feature() {}
	
	public Feature(int layerId) {
		this.layerId = layerId;
	}
	
	/** Copy method */
	protected void copyFromFeature(Feature feature) {
		this.bounds = feature.bounds;
		this.visible = feature.visible;
		this.enabled = feature.enabled;
		this.transparency = feature.transparency;
		this.name = feature.name;
		this.parent = feature.parent;
		this.layerId = feature.layerId;
	}	
	
	/**
	 * Draws this feature on the given graphics context using the given rendering data.
	 * 
	 * @param g
	 * @param r
	 */
	public abstract void draw(Graphics2D g, RenderData r);
	
	/**
	 * Gets the children of this feature. The coordinate system of the children
	 * are also offset by any of the parent's offsets.
	 * 
	 * @return The children of this feature. May be null.
	 */
	public abstract Collection<? extends Feature> getChildren();
	
	/**
	 * Set the feature bounds.
	 * 
	 * @param bounds
	 */
	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}

	/**
	 * Get the feature bounds.
	 * 
	 * @return
	 */
	public Rectangle getBounds() {
		return bounds;
	}
	
	/**
	 * Get whether the feature is visible.
	 * 
	 * @return
	 */
	public boolean isVisible() {
		return visible;
	}
	
	/**
	 * Set whether the feature is visible.
	 * 
	 * @param visible
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * Get whether the feature is enabled.
	 * 
	 * @return
	 */
	public boolean isEnabled() {
		return enabled;
	}
	
	/**
	 * Set whether the feature is enabled.
	 * 
	 * @param enabled
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}	
	
	/**
	 * Get the alpha transparency value of the feature.
	 * 
	 * @return
	 */
	public float getTransparency() {
		return transparency;
	}

	/**
	 * Set the alpha transparency value of the feature.
	 * 
	 * @param transparency
	 */
	public void setTransparency(float transparency) {
		this.transparency = transparency;
	}

	/**
	 * Get the name of the feature.
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Set the name of the feature.
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Get the parent of this feature (if any).
	 * 
	 * @return
	 */
	public Feature getParent() {
		return parent;
	}

	/**
	 * Set the parent of this feature.
	 * 
	 * @param parent
	 */
	public void setParent(Feature parent) {
		this.parent = parent;
	}
	
	/**
	 * Get the ID of the layer this feature is contained in.
	 * 
	 * @return
	 */
	public Integer getLayerId() {
		return layerId;
	}

	/**
	 * Set the ID of the layer this feature is contained in.
	 * 
	 * @param layerId
	 */
	public void setLayerId(Integer layerId) {
		this.layerId = layerId;
	}
	
	/**
	 * Renders a feature by calling its draw method, and then renders its children.
	 * 
	 * @param g the graphics context to use
	 * @param r the render data
	 */
	public void render(Graphics2D g, RenderData r) {
		
		if (!isVisible() || !isEnabled()) {			
			return;
		}
		
		Rectangle bounds = getBounds();
		
		double xOffset = bounds.x*r.tileWidth;
		double yOffset = bounds.y*r.tileHeight;	
		
		Composite origComposite = g.getComposite();
		if(transparency < 1.f) {
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));
		}
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g.translate(xOffset, yOffset);
		draw(g, r);
		
		Collection<? extends Feature> children = getChildren();
		
		if (children != null) {
			for (Feature child : children) {
				child.render(g, r);				
			}
		}
		
		/*if(highlighted) {	
			Rectangle2D rect = new Rectangle2D.Double();
			rect.setFrame(0, 0,
					bounds.width*r.tileWidth, bounds.height*r.tileHeight);			
			g.draw(rect);
		}*/
		
		g.translate(-xOffset, -yOffset);
		g.setComposite(origComposite);
	}	
}
