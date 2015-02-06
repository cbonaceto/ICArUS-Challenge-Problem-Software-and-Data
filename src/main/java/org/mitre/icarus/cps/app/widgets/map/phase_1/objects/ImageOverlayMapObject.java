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
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.mitre.icarus.cps.app.widgets.map.phase_1.RenderProperties;

public class ImageOverlayMapObject extends AbstractMapObject_Phase1 {
	
	/** The image to overlay.  The image will be stretched to fill from top right to bottom left. */
	protected Image image;
	
	/** The scaled image that has been stretched to fill its bounds */
	protected Image scaledImage;	
	
	/** The current image bounds (in grid coordinates) */
	protected Rectangle2D.Double imageBounds;	
	
	public ImageOverlayMapObject(Image image, Rectangle2D.Double imageBounds) {
		this.image = image;
		this.imageBounds = imageBounds;
	}

	@Override
	public void setMouseOverState(boolean mouseOver, MouseEvent event) {
		//Does nothing
	}

	@Override
	public boolean contains(Point point) {
		if(imageBounds != null) {
			return imageBounds.contains(point);
		}
		return false;
	}

	@Override
	public boolean contains(Point2D point) {
		if(imageBounds != null) {
			return imageBounds.contains(point);
		}
		return false;
	}

	@Override
	public boolean contains(double x, double y) {
		if(imageBounds != null) {
			return imageBounds.contains(x, y);
		}
		return false;
	}

	@Override
	public void render(Graphics2D g, RenderProperties renderData, boolean renderPropertiesChanged) {
		if(image == null) {
			return;
		}
		
		int width_pixels = (int)renderData.translateToPixels(imageBounds.width);
		int height_pixels = (int)renderData.translateToPixels(imageBounds.height);		
		if(width_pixels <= 0 || height_pixels <= 0) {
			return;
		}
		
		if(scaledImage == null || width_pixels != scaledImage.getWidth(null) || 
				height_pixels != scaledImage.getHeight(null)) {
			//Rescale the image
			scaledImage = new BufferedImage((int)width_pixels, (int)height_pixels, BufferedImage.TYPE_INT_ARGB);
			Graphics2D imageGraphics = (Graphics2D)scaledImage.getGraphics();
			imageGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			imageGraphics.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
			//imageGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			imageGraphics.drawImage(image, 0, 0, width_pixels, height_pixels, null);
		}
		
		//Set the transparency
		Composite origComposite = null;
		if(transparency < 1.f) {
			//System.out.println("setting transparency to " + transparency);
			origComposite = g.getComposite();
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));
		}
		
		//Draw the image
		Point2D topLeft_pixel = renderData.translateToPixel(imageBounds.x, imageBounds.y);
		g.drawImage(scaledImage, (int)topLeft_pixel.getX(), (int)topLeft_pixel.getY(), null);
		
		if(origComposite != null) {
			g.setComposite(origComposite);
		}
	}	
}