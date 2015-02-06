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
package org.mitre.icarus.cps.app.widgets.map.phase_1.objects;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.mitre.icarus.cps.app.widgets.map.phase_1.RenderProperties;
import org.mitre.icarus.cps.feature_vector.phase_1.GridLocation2D;
import org.mitre.icarus.cps.feature_vector.phase_1.SocintPolygon;

/**
 * @author CBONACETO
 *
 */
public class RegionMapObject extends ShapeMapObject {
	
	/** The region */
	protected final SocintPolygon region;	
	
	/** Path defining the region boundaries */
	protected Path2D regionPath;
	
	public RegionMapObject(SocintPolygon region) {
		this.region = region;
		regionPath = new Path2D.Double();
		renderShape = regionPath;
		objectBoundsChanged = true;
	}
	
	@Override
	public String getName() {
		if(region != null && region.getGroup() != null) {
			return region.getGroup().toString();
		}
		return null;
	}
	
	@Override
	public String getId() {
		return getName();
	}

	@Override
	public double getArea_pixels() {
		if(regionPath != null && regionPath.getBounds2D() != null) {
			Rectangle2D bounds = regionPath.getBounds2D();
			return bounds.getWidth() * bounds.getHeight();
		}
		return 0;
	}	

	@Override
	public Point2D getCenterLocation_pixels() {
		if(regionPath != null && regionPath.getBounds2D() != null) {
			return new Point2D.Double(regionPath.getBounds2D().getCenterX(), 
					regionPath.getBounds2D().getCenterY());
		}
		return null;
	}

	public SocintPolygon getRegion() {
		return region;
	}
	
	public void regionChanged() {
		objectBoundsChanged = true;
	}

	@Override
	public void render(Graphics2D g2d, RenderProperties renderData, boolean renderPropertiesChanged) {
		
		Color origColor = g2d.getColor();
		Stroke origStroke = g2d.getStroke();
		
		if(objectBoundsChanged || renderPropertiesChanged || regionPath.getCurrentPoint() == null) {
			regionPath.reset();		

			int vertexCounter = 0;
			Point2D firstPixelPoint = null;

			for(GridLocation2D vertex : region.getVertices()) {
				// translate to pixel coordinates from grid coordinates
				Point2D pixelPoint = renderData.translateToPixel(vertex);
				if(vertexCounter == 0) {
					firstPixelPoint = pixelPoint;
				}

				// add point to path
				if(vertexCounter == 0) {
					regionPath.moveTo(pixelPoint.getX(), pixelPoint.getY());
				}
				else {
					regionPath.lineTo(pixelPoint.getX(), pixelPoint.getY());
				}
				vertexCounter++;
			}

			// connect the last point to the first point
			if(firstPixelPoint != null) {
				regionPath.lineTo(firstPixelPoint.getX(), firstPixelPoint.getY());
			}
		}
		
		//Set the transparency
		Composite origComposite = null;
		if(transparency < 1.f) {
			origComposite = g2d.getComposite();
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));
		}
		
		if(backgroundColor != null) {
			g2d.setColor(backgroundColor);
			g2d.fill(regionPath);
		}
		
		if(borderLineStyle != null) {
			if(borderColor == null) {
				borderColor = Color.black;
			}			
			g2d.setColor(borderColor);
			g2d.setStroke(getBorderLineStyle().getLineStroke());
			g2d.draw(regionPath);
		}
		
		g2d.setColor(origColor);
		g2d.setStroke(origStroke);
		if(origComposite != null) {
			g2d.setComposite(origComposite);
		}
		objectBoundsChanged = false;
	}

	@Override
	public void setMouseOverState(boolean mouseOver, MouseEvent event) {
		// TODO Auto-generated method stub
		
	}
}