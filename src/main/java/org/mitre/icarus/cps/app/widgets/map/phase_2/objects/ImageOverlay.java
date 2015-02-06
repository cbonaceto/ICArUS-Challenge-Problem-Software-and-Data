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
package org.mitre.icarus.cps.app.widgets.map.phase_2.objects;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.mitre.icarus.cps.app.widgets.map.LineStyle;
import org.mitre.icarus.cps.app.widgets.map.phase_2.RenderProperties;
import org.mitre.icarus.cps.feature_vector.phase_2.coordinate.GeoCoordinate;

/**
 * @author CBONACETO
 *
 */
public class ImageOverlay extends AbstractMapObject_Phase2 {
	
	/** The image to overlay.  The image will be stretched to fill from top right to bottom left. */
	protected Image image;
	
	/** The scaled image that has been stretched to fill its bounds in pixel space */
	protected Image scaledImage;	
	
	/** The image bounds (in Geo coordinates) */
	//protected GeoBounds imageBounds;
	protected Path2D.Double imageBounds;
	
	/** The image bounds (in pixels) */
	protected Rectangle2D.Double imageBounds_pixels;
	
	/** The top left coordinate of the image */
	protected GeoPosition topLeft;
	
	/** The bottom right coordinate of the image */
	protected GeoPosition bottomRight;
	
	/** The border color (if any) */
	protected Color borderColor;
	
	/** The border line style (if any) */
	protected LineStyle borderLineStyle;
	
	/**
	 * @param image
	 * @param topLeft
	 * @param bottomRight
	 */
	public ImageOverlay(Image image, GeoCoordinate topLeft, GeoCoordinate bottomRight) {
		setImage(image, new GeoPosition(topLeft.getLat(), topLeft.getLon()), 
				new GeoPosition(bottomRight.getLat(), bottomRight.getLon()));
	}	
	
	public ImageOverlay(Image image, GeoPosition topLeft, GeoPosition bottomRight) {
		setImage(image, topLeft, bottomRight);
	}	
	
	public Image getImage() {
		return image;
	}

	public void setImage(Image image, GeoPosition topLeft, GeoPosition bottomRight) {
		this.image = image;
		if(scaledImage != null) {
			scaledImage.flush();
			scaledImage = null;
		}
		this.topLeft = topLeft;
		this.bottomRight = bottomRight;
		imageBounds = new Path2D.Double();
		imageBounds.moveTo(topLeft.getLongitude(), topLeft.getLatitude());
		imageBounds.lineTo(bottomRight.getLongitude(), topLeft.getLatitude());
		imageBounds.lineTo(bottomRight.getLongitude(), bottomRight.getLatitude());
		imageBounds.lineTo(topLeft.getLongitude(), bottomRight.getLatitude());
		imageBounds.closePath();		
		//imageBounds = new GeoBounds(new HashSet<GeoPosition>(Arrays.asList(topLeft, bottomRight)));
		/*imageBounds = new Rectangle2D.Double(topLeft.getLongitude(), topLeft.getLatitude(), 
				Math.abs(topLeft.getLongitude() - bottomRight.getLongitude()),
				Math.abs(topLeft.getLatitude() - bottomRight.getLatitude()));*/
		imageBounds_pixels = new Rectangle2D.Double(0, 0, 1, 1);
		objectBoundsChanged = true;
	}
	
	public void removeImage() {
		image = null;
		if(scaledImage != null) {
			scaledImage.flush();
			scaledImage = null;
		}
	}
	
	public Color getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
	}

	public float getBorderLineWidth() {
		if(borderLineStyle != null) {
			return borderLineStyle.getLineWidth();
		}
		return 0;
	}
	
	public LineStyle getBorderLineStyle() {
		return borderLineStyle;
	}

	public void setBorderLineStyle(LineStyle borderLineStyle) {
		this.borderLineStyle = borderLineStyle;
	}	

	public void setBorderLineWidth(float lineWidth) {
		if(lineWidth <= 0.f) {
			borderLineStyle = null;
		} else {
			if(borderLineStyle == null) {
				borderLineStyle = new LineStyle(lineWidth);
			} else {
				borderLineStyle.setLineWidth(lineWidth);
			}
		}
	}

	@Override
	public void setMouseOverState(boolean mouseOver, MouseEvent event) {
		//Does nothing
	}

	@Override
	public Point2D getCenterPixelLocation() {
		if(imageBounds_pixels != null) {
			return new Point2D.Double(imageBounds_pixels.getCenterX(), imageBounds_pixels.getCenterY());
		}
		return null;
	}

	@Override
	public Point2D getCenterGeoLocation() {
		if(imageBounds != null) {
			//imageBounds.
			return new Point2D.Double(imageBounds.getBounds2D().getCenterX(), 
					imageBounds.getBounds2D().getCenterY());
		}
		return null;
	}
	
	@Override
	public Rectangle2D.Double getPixelShape() {
		return imageBounds_pixels;
	}

	@Override
	public Path2D.Double getGeoShape() {
		return imageBounds;
	}	
	
	public GeoPosition getTopLeft() {
		return topLeft;
	}

	public GeoPosition getBottomRight() {
		return bottomRight;
	}

	@Override	
	public void render(Graphics2D g, RenderProperties renderProperties, boolean renderPropertiesChanged) {
		if(image != null) {			
			g = (Graphics2D)g.create();			
			
			//TODO: This can be optimized by clipping the image and checking if it's within the bounds of the viewport
			//The image size should only be made as large as the visible viewport at most			

			//Determine the width and height of the image in pixels based on its width and height in degrees		
			Point2D topLeft_pixel = renderProperties.geoPositionToWorldPixel(topLeft); //map.getTileFactory().geoToPixel(topLeft, zoom);
			Point2D bottomRight_pixel = renderProperties.geoPositionToWorldPixel(bottomRight); //map.getTileFactory().geoToPixel(bottomRight, zoom);			
			int width_pixels = (int)Math.abs(topLeft_pixel.getX() - bottomRight_pixel.getX());
			int height_pixels = (int)Math.abs(topLeft_pixel.getY() - bottomRight_pixel.getY());
			//System.out.println(width_pixels + ", " + height_pixels);
			imageBounds_pixels.setFrame(topLeft_pixel.getX(), topLeft_pixel.getY(), width_pixels, height_pixels);
			//System.out.println("Top Left: " + topLeft_pixel + ", Bottom Right: " + bottomRight_pixel + ", Width: " + width_pixels + ", Height: " + height_pixels);			
			if(width_pixels <= 0 || height_pixels <= 0) {
				return;
			}
			
			if(scaledImage == null || width_pixels != scaledImage.getWidth(null) || height_pixels != scaledImage.getHeight(null)) {
				//Rescale the image
				if(scaledImage != null) {
					scaledImage.flush();
					scaledImage = null;
					System.gc();
				}
				//System.out.println(width_pixels + ", " + height_pixels);
				scaledImage = new BufferedImage((int)width_pixels, (int)height_pixels, BufferedImage.TYPE_INT_ARGB);
				Graphics2D imageGraphics = (Graphics2D)scaledImage.getGraphics();
				imageGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				imageGraphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
				//imageGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
				imageGraphics.drawImage(image, 0, 0, width_pixels, height_pixels, null);
			}
			
			//Set the transparency
			float transparency = 1.f;
			if(transparency < 1.f) {				
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));
			}
			
			//Draw the image			
			g.drawImage(scaledImage, (int)topLeft_pixel.getX(), (int)topLeft_pixel.getY(), null);
			
			//Draw the image border
			if(borderLineStyle != null) {
				if(borderColor == null) {
					borderColor = Color.BLACK;
				}
				g.setColor(borderColor);
				g.setStroke(getBorderLineStyle().getLineStroke());
				g.draw(imageBounds_pixels);
			}
		}
	}
}