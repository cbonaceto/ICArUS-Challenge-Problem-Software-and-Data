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

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import org.mitre.icarus.cps.app.widgets.phase_05.ColorManager;
import org.mitre.icarus.cps.app.widgets.phase_05.WidgetConstants_Phase05;

/**
 * Feature that contains the objects in a sector.  Bounds contain
 * all objects in the sector.  Sectors do not render contained objects,
 * just their bounds and possibly their name.
 * 
 * Sectors can also be highlighted or faded.
 * 
 * @author CBONACETO
 *
 */
public class Sector extends Feature {
	/** Sector ID */
	private final int sectorId;
	
	/** Annotation with the sector name */
	private AnnotationFeature titleAnnotation;
	
	/** The objects that comprise the sector */
	private List<Feature> sectorObjects = new ArrayList<Feature>();
	
	private List<Feature> children;
	
	/** Whether the sector is highlighted.  Highlighted sectors are outlined with a bold line */
	private boolean highlighted = false;
	
	/** Whether the sector is faded. Faded sectors are drawn with transparency fadedTransparency */
	private boolean faded = false;
	
	/** The alpha transparency value to use when the sector is faded (default = 0.6f) */
	private float fadedTransparency = WidgetConstants_Phase05.FADED_SECTOR_TRANSPARENCY;

	/** Line stroke to use for the sector outline (dotted) when not highlighted */
	private static final BasicStroke stroke = new BasicStroke(1, 
			BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1, new float[] {2}, 0);
	
	/** Line stroke to use when the sector is highlighted */
	private static final BasicStroke highlightedStroke = new BasicStroke(2, 
			BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1);
	
	public Sector(int sectorId) {
		this.sectorId = sectorId;
		titleAnnotation = new AnnotationFeature();
		titleAnnotation.setVertialTextPosition(AnnotationFeature.TOP);
		titleAnnotation.setFeature(this);
		children = new ArrayList<Feature>();
		children.add(titleAnnotation);
	}
	
	@Override
	public void draw(Graphics2D g, RenderData r) {
		g.setColor(ColorManager.get(ColorManager.SECTOR));
		Stroke origStroke = g.getStroke();
		if(highlighted) {
			g.setStroke(highlightedStroke);
		}
		else {
			g.setStroke(stroke);
		}
		Rectangle2D rect = new Rectangle2D.Double();
		
		Rectangle b = getBounds();
		rect.setFrame(0, 0,
				b.width*r.tileWidth, b.height*r.tileHeight);
		
		g.draw(rect);
		g.setStroke(origStroke);
	}

	public static BasicStroke getStroke() {
		return stroke;
	}

	public int getSectorId() {
		return sectorId;
	}

	@Override
	public void setTransparency(float transparency) {
		super.setTransparency(transparency);
		if(!sectorObjects.isEmpty()) {
			for(Feature feature : sectorObjects) {
				feature.setTransparency(transparency);
			}
		}
	}

	public boolean isFaded() {
		return faded;
	}

	public void setFaded(boolean faded) {
		if(faded != this.faded) {
			this.faded = faded;
			float alpha = 1.f;
			if(faded) {
				alpha = fadedTransparency;
			}
			super.setTransparency(alpha);
			if(!sectorObjects.isEmpty()) {
				for(Feature feature : sectorObjects) {
					feature.setTransparency(alpha);
				}
			}
		}
	}

	public float getFadedTransparency() {
		return fadedTransparency;
	}

	public void setFadedTransparency(float fadedTransparency) {
		this.fadedTransparency = fadedTransparency;
	}


	public boolean isHighlighted() {
		return highlighted;
	}
	
	public void setHighlighted(boolean highlighted) {
		this.highlighted = highlighted;		
	}

	@Override
	public List<Feature> getChildren() {
		return children;
		//return null;
		//return sectorObjects;
	}
	
	public List<Feature> getSectorObjects() {
		return sectorObjects;
	}

	@Override
	public void setName(String name) {
		super.setName(name);
		titleAnnotation.setText(name);
	}	
}