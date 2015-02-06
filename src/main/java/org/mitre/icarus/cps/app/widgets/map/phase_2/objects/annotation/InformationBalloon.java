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
package org.mitre.icarus.cps.app.widgets.map.phase_2.objects.annotation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.mitre.icarus.cps.app.widgets.map.phase_2.MapConstants_Phase2;
import org.mitre.icarus.cps.app.widgets.map.phase_2.RenderProperties;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.AbstractMapObject_Phase2;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.IMapObject_Phase2;

/**
 * Renders information balloon pop-ups.
 * 
 * @author CBONACETO
 *
 */
//TODO: Move balloon if parent map object moved, add max width, add annotation line/connector tail properties that can be set
//TODO: Add option to only render the balloon content and not the balloon outline
public class InformationBalloon<T extends Object> extends AbstractMapObject_Phase2 implements IAnnotationMapObject {	
	
	/** The map object the information balloon is for (If null, balloon positioned at center location) */
	protected IAnnotatableMapObject parent;	
	
	/** The orientation of the information balloon relative to its parent map object */
	protected AnnotationOrientation orientation;
	
	/** Whether to automatically compute the balloon orientation to stay within the map bounds and avoid
	 * overlapping other map objects in the conflict objects set */
	protected boolean autoAdjustOrientation = true;
	
	/** Set of map objects to check for overlap conflicts when determining the best balloon orientation */
	protected Collection<IMapObject_Phase2> conflictObjects;
	
	/** Whether to position the information balloon with respect to the parent map object */
	protected boolean positionRelativeToParent;
	
	/** The information balloon content area renderer */
	protected InformationBalloonContentRenderer<T> renderer;
	
	/** The renderer preferred size */
	protected Dimension rendererPreferredSize;
	
	/** The center location in Geo coordinates */
	private GeoPosition center;
	
	/** The balloon rectangle bounds in Geo coordinates */
	private Rectangle2D balloonBounds = null;	
	
	/** The overall balloon area in pixel coordinates */
	private BalloonBounds balloonBounds_pixels = null;
	
	/** The bounds of the content renderer area in pixels */
	protected Rectangle2D contentRendererBounds_pixels;
	
	protected Point2D endLocationPixel;
	
	protected Color foreground = MapConstants_Phase2.TOOLTIP_BORDER_COLOR;
	
	protected Color background = MapConstants_Phase2.TOOLTIP_BACKGROUND_COLOR;	
	
	protected int preferredBalloonWidth = 180;	
	
	protected int minimumBalloonHeight;	
	
	protected int offset_pixels = MapConstants_Phase2.ANNOTATION_OFFSET_PIXELS;
	
	protected double roundedArcWidth = 10;	
	
	/** Whether to show a close button to close the balloon */
	protected boolean showCloseButton = true;
	
	/** Close button size if close button visible */
	protected int closeButtonSize = 13;	
	
	/** Whether to draw the connecting tail to the object being annotated by the information balloon */
	protected boolean drawBalloonConnector = true;
	
	/** Balloon connector tail width  */
	protected double connectorWidth = 12;
	
	protected static final List<AnnotationOrientation> orderedOrientationList = new ArrayList<AnnotationOrientation>(
			Arrays.asList(AnnotationOrientation.North, AnnotationOrientation.East, 
					AnnotationOrientation.South, AnnotationOrientation.West,
					AnnotationOrientation.NorthEast, AnnotationOrientation.NorthWest,
					AnnotationOrientation.SouthEast, AnnotationOrientation.SouthWest));
	
	public InformationBalloon(GeoPosition center, T content) {
		this((IAnnotatableMapObject)null, content, new JLabelBalloonContentRenderer<T>());
		this.center = center != null ? center : new GeoPosition(0, 0);
		positionRelativeToParent = false;
	}
	
	public InformationBalloon(GeoPosition center, T content, InformationBalloonContentRenderer<T> renderer) {
		this((IAnnotatableMapObject)null, content, renderer);
		this.center = center != null ? center : new GeoPosition(0, 0);
	}
	
	public InformationBalloon(IAnnotatableMapObject parent, T content) {
		this(parent, content, new JLabelBalloonContentRenderer<T>());
	}	
	
	public InformationBalloon(IAnnotatableMapObject parent, T content, InformationBalloonContentRenderer<T> renderer) {
		this.parent = parent;
		positionRelativeToParent = parent != null;
		this.renderer = renderer;
		renderer.setContent(content);
		minimumBalloonHeight = showCloseButton ?  
				Math.max(closeButtonSize + 20, renderer.getPreferredSize().height + 20) :
					renderer.getPreferredSize().height + 20; //Math.max(renderer.getPreferredSize().height + 20, closeButtonSize + 20);
	}

	@Override
	public IAnnotatableMapObject getParent() {
		return parent;
	}

	@Override
	public void setParent(IAnnotatableMapObject parent) {
		this.parent = parent;
		positionRelativeToParent = parent != null;
		if(!positionRelativeToParent && center == null) {
			center = new GeoPosition(0, 0);
		}
		objectBoundsChanged = true;
	}	
	
	public T getContent() {
		return renderer != null ? renderer.getContent() : null;
	}
	
	public void setContent(T content) {
		renderer.setContent(content);
		objectBoundsChanged = true;
	}
	
	public AnnotationOrientation getOrientation() {
		return orientation;
	}

	public void setOrientation(AnnotationOrientation orientation) {
		if(autoAdjustOrientation || orientation != this.orientation) {
			this.orientation = orientation;
			autoAdjustOrientation = false;
			objectBoundsChanged = true;
		}
	}

	public boolean isAutoAdjustOrientation() {
		return autoAdjustOrientation;
	}

	/** 
	 * @param autoAdjustOrientation
	 * @param conflictObjects
	 */
	public void setAutoAdjustOrientation(boolean autoAdjustOrientation, 
			Collection<IMapObject_Phase2> conflictObjects) {
		this.autoAdjustOrientation = autoAdjustOrientation;
		this.conflictObjects = conflictObjects;
		objectBoundsChanged = true;
	}

	public InformationBalloonContentRenderer<T> getRenderer() {
		return renderer;
	}

	public void setRenderer(InformationBalloonContentRenderer<T> renderer) {
		if(renderer != null && renderer != this.renderer) {
			this.renderer = renderer;
			objectBoundsChanged = true;
		}
	}

	public int getPreferredBalloonWidth() {
		return preferredBalloonWidth;
	}

	public void setPreferredBalloonWidth(int preferredBalloonWidth) {
		this.preferredBalloonWidth = preferredBalloonWidth;
		objectBoundsChanged = true;
	}

	public Color getBackground() {
		return background;
	}
	
	public void setBackground(Color background) {
		this.background = background;
		renderer.setBackground(background);
	}
	
	public Color getForeground() {
		return foreground;
	}

	public void setForeground(Color foreground) {
		this.foreground = foreground;
		renderer.setForeground(foreground);
	}

	public boolean isShowCloseButton() {
		return showCloseButton;
	}

	public void setShowCloseButton(boolean showCloseButton) {
		if(showCloseButton != this.showCloseButton) {
			this.showCloseButton = showCloseButton;
			minimumBalloonHeight = showCloseButton ?  
					Math.max(closeButtonSize + 20, renderer.getPreferredSize().height + 20) : 
						renderer.getPreferredSize().height + 20;
			objectBoundsChanged = true;
		}
	}	

	public boolean isDrawBalloonConnector() {
		return drawBalloonConnector;
	}

	public void setDrawBalloonConnector(boolean drawBalloonConnector) {
		if(drawBalloonConnector != this.drawBalloonConnector) {
			this.drawBalloonConnector = drawBalloonConnector;
			objectBoundsChanged = true;
		}
	}

	@Override
	public Point2D getCenterPixelLocation() {
		if(balloonBounds_pixels != null && balloonBounds_pixels.roundRectangle != null) {
			return new Point2D.Double(balloonBounds_pixels.roundRectangle.getCenterX(), 
					balloonBounds_pixels.roundRectangle.getCenterY());
		}
		return null;
	}

	@Override
	public Point2D getCenterGeoLocation() {
		if(center != null) {
			return new Point2D.Double(center.getLongitude(), center.getLatitude());
		}
		return null;
		/*if(balloonArea != null) {
			Rectangle2D bounds = balloonArea.getBounds2D();
			return new Point2D.Double(bounds.getCenterX(), bounds.getCenterY());
		}
		return null;*/
	}

	@Override
	public Shape getGeoShape() {
		return balloonBounds;
	}	

	@Override
	public Shape getPixelShape() {
		return balloonBounds_pixels != null ? balloonBounds_pixels.balloonArea : null;
	}

	@Override
	public boolean containsPixelLocation(Point2D location) {
		if(location != null && balloonBounds_pixels != null) {
			return balloonBounds_pixels.balloonArea.contains(location);
		}
		return false;
	}

	@Override
	public boolean containsPixelLocation(double x, double y) {
		if(balloonBounds_pixels != null) {
			return balloonBounds_pixels.balloonArea.contains(x, y);
		}
		return false;
	}
	
	/**
	 * Return whether the close button contains the given world coordinate pixel location.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean closeButtonContainsPixelLocation(double x, double y) {
		if(showCloseButton && balloonBounds_pixels != null) {
			return balloonBounds_pixels.closeButtonBounds.contains(x, y);
		}
		return false;
	}
	
	@Override
	public void setMouseOverState(boolean mouseOver, MouseEvent event) {}
	
	@Override
	public void setCenterGeoLocation(Point2D centerLocation) {
		if(centerLocation != null) {
			setCenterGeoLocation(centerLocation.getX(), centerLocation.getY());
		}
	}

	@Override
	public void setCenterGeoLocation(GeoPosition centerGeoLocation) {
		if(centerGeoLocation != null) {
			setCenterGeoLocation(centerGeoLocation.getLongitude(), centerGeoLocation.getLatitude());
		}
	}
	
	public void setCenterGeoLocation(double longitude, double latitude) {
		center = new GeoPosition(longitude, latitude);
		positionRelativeToParent = false;
		objectBoundsChanged = true;
	}
	
	@Override
	public Point2D getAnnotationLineStartPixelLocation(AnnotationOrientation orientation) {		
		if(balloonBounds_pixels != null && balloonBounds_pixels.roundRectangle != null) {		
			return getAnnotationLineStartPixelLocation(orientation, balloonBounds_pixels.roundRectangle);
		}		
		/*if(endLocationPixel != null && balloonBounds_pixels != null && balloonBounds_pixels.roundRectangle != null) {
			RoundRectangle2D roundRectangle = balloonBounds_pixels.roundRectangle;
			switch(orientation) {
			case East:
				return new Point2D.Double(endLocationPixel.getX() + offset_pixels, 
						roundRectangle.getY() + roundRectangle.getHeight() / 2.0 - connectorWidth / 2.0);
			case North:
				return new Point2D.Double(endLocationPixel.getX(), 
						roundRectangle.getY() + roundRectangle.getHeight());		
			case NorthEast:
				return new Point2D.Double(endLocationPixel.getX() + roundedArcWidth/2, 
						roundRectangle.getY() + roundRectangle.getHeight());			
			case NorthWest:
				return new Point2D.Double(endLocationPixel.getX() - (roundedArcWidth/2), 
						roundRectangle.getY() + roundRectangle.getHeight());
			case South:				
				return new Point2D.Double(endLocationPixel.getX(), endLocationPixel.getY() + offset_pixels);
			case SouthEast:
				return new Point2D.Double(endLocationPixel.getX() + roundedArcWidth/2, roundRectangle.getY());	
			case SouthWest:
				return new Point2D.Double(endLocationPixel.getX() - (roundedArcWidth/2), roundRectangle.getY());
			case West:
				return new Point2D.Double(endLocationPixel.getX() - offset_pixels, 
						roundRectangle.getY() + roundRectangle.getHeight() / 2.0 - connectorWidth / 2.0);
			default:
				return null;
			}
		}*/
		return null;
	}
	
	private Point2D getAnnotationLineStartPixelLocation(AnnotationOrientation orientation, RoundRectangle2D roundRectangle_pixels) {
		if(roundRectangle_pixels != null) {		
			Point2D centerLocationPixel = new Point2D.Double(
					roundRectangle_pixels.getCenterX(), 
					roundRectangle_pixels.getCenterY());
			return AnnotationUtils.computeAnnotationLineStartPixelLocation(orientation, 
					centerLocationPixel, roundRectangle_pixels);
		}
		return null;
	}
	
	@Override
	public void render(Graphics2D g, RenderProperties renderProperties, boolean renderPropertiesChanged) {
		Color origColor = g.getColor();
		Stroke origStroke = g.getStroke();
	
		if(objectBoundsChanged || renderPropertiesChanged || contentRendererBounds_pixels == null || balloonBounds_pixels == null 
				|| checkRendererPreferredSizeChanged()) {
			if(renderer.getPreferredSize().height < minimumBalloonHeight) {
				renderer.setPreferredSize(new Dimension(renderer.getPreferredSize().width, minimumBalloonHeight));
			}
			rendererPreferredSize = new Dimension(renderer.getPreferredSize().width, renderer.getPreferredSize().height);			
			int preferredBalloonWidth = rendererPreferredSize.width + 6 > this.preferredBalloonWidth ? 
					rendererPreferredSize.width + 6 : this.preferredBalloonWidth;
			int preferredBalloonHeight = rendererPreferredSize.height;
			if(positionRelativeToParent && parent != null && parent.getCenterPixelLocation() != null) {
				//Position information balloon relative to its parent map object
				if(autoAdjustOrientation || orientation == null) {
					//Determine where to place the information balloon relative to its parent to avoid clipping it if possible
					endLocationPixel = null;
					balloonBounds_pixels = null;
					AnnotationOrientation currOrientation = null;
					List<Double> conflictAreas = new ArrayList<Double>(orderedOrientationList.size());
					List<AnnotationOrientation> orientationsToCheck = null;
					if(orientation == null) {
						orientationsToCheck = orderedOrientationList;
					} else {
						orientationsToCheck = new ArrayList<AnnotationOrientation>(orderedOrientationList.size());
						orientationsToCheck.add(orientation);
						for(AnnotationOrientation ao : orderedOrientationList) {
							if(orientation != ao) {
								orientationsToCheck.add(ao);
							}
						}
					}					
					//for(AnnotationOrientation orientation : orderedOrientationList) {
					for(AnnotationOrientation orientation : orientationsToCheck) {
						currOrientation = orientation;
						endLocationPixel = parent.getAnnotationLineEndPixelLocation(orientation);
						BalloonBounds projectedBalloonArea = calculateBalloonArea(orientation, endLocationPixel, renderProperties,
								preferredBalloonWidth, preferredBalloonHeight, null);
						
						//Determine the area of the projected bounds of the information balloon that is outside of the viewport bounds
						//or overlapping map objects in the conflict objects set
						double conflictArea = AnnotationUtils.computeOverlapAndOutsideBoundsArea(
								this, projectedBalloonArea.balloonArea.getBounds(), 
								conflictObjects, renderProperties);
						//System.out.println("Conflict area for " + parent.getName() + " at " + currOrientation + ": " + conflictArea);
						if(conflictArea < 3) {
							balloonBounds_pixels = projectedBalloonArea;
							break;
						} else {
							conflictAreas.add(conflictArea);
						}						
						/*//Make sure the bounds of the map viewport completely contain the rectangle of the projected information balloon
						if(renderProperties.isPixelShapeCompletelyInView(projectedBalloonArea.balloonArea.getBounds())) {					
							//System.out.println("Found suitable orientation: " + orientation + ", " + endLocationPixel);
							balloonBounds_pixels = projectedBalloonArea;
							break;
						}*/
					}					
					//If no conflict-free orientation is found, place the balloon at the orientation with the smallest conflict area
					if(balloonBounds_pixels == null) {
						Double minConflictArea = null;
						int minIndex = 0;
						int index = 0;
						for(Double conflictArea : conflictAreas) {
							if(minConflictArea == null || conflictArea < minConflictArea) {
								minConflictArea = conflictArea;
								minIndex = index;
							}
							index++;
						}						
						//currOrientation = orderedOrientationList.get(minIndex);
						currOrientation = orientationsToCheck.get(minIndex);
						//System.out.println("Using best orientation: " + currOrientation);
						endLocationPixel = parent.getAnnotationLineEndPixelLocation(currOrientation);
						balloonBounds_pixels = calculateBalloonArea(currOrientation, endLocationPixel, renderProperties,
								preferredBalloonWidth, preferredBalloonHeight, balloonBounds_pixels);
					}
					this.orientation = currOrientation;
				} else {
					//Position information the balloon relative to its parent at the specified orientation
					endLocationPixel = parent.getAnnotationLineEndPixelLocation(orientation);
					balloonBounds_pixels = calculateBalloonArea(orientation, endLocationPixel, renderProperties,
							preferredBalloonWidth, preferredBalloonHeight, balloonBounds_pixels);					
				}
				//Compute the information balloon bounds and center in Geo coordinates
				//TODO: Check this logic, may need to fix this			
				balloonBounds = renderProperties.worldPixelRectangleToGeoRectangle(balloonBounds_pixels.roundRectangle.getBounds2D());
				center = new GeoPosition(balloonBounds.getCenterY(), balloonBounds.getCenterX());				
			} else {
				//TODO: Check this logic
				if(center == null) {center = new GeoPosition(0, 0);}
				if(endLocationPixel == null) {endLocationPixel = renderProperties.geoPositionToWorldPixel(center);};
				if(orientation == null) {orientation = AnnotationOrientation.North;}
				balloonBounds_pixels = calculateBalloonArea(orientation, endLocationPixel, renderProperties,
						preferredBalloonWidth, preferredBalloonHeight, balloonBounds_pixels);
				balloonBounds = renderProperties.worldPixelRectangleToGeoRectangle(balloonBounds_pixels.roundRectangle.getBounds2D());
			}
			contentRendererBounds_pixels = new Rectangle2D.Double(
					balloonBounds_pixels.contentAreaBounds.getX() + balloonBounds_pixels.contentAreaBounds.getWidth() / 2.0 - renderer.getPreferredSize().getWidth() / 2.0, 
					balloonBounds_pixels.contentAreaBounds.getY(), 
					renderer.getPreferredSize().getWidth(), renderer.getPreferredSize().getHeight());
		} 
		
		//Render the balloon
		if(background == null) {
			background = MapConstants_Phase2.TOOLTIP_BACKGROUND_COLOR;
		}
		g.setColor(background);
		g.fill(balloonBounds_pixels.balloonArea);
		if(foreground == null) {
			foreground = Color.black;
		}		
		g.setStroke(new BasicStroke(1));
		g.setColor(foreground);
		g.draw(balloonBounds_pixels.balloonArea);
		g.setStroke(origStroke);
		
		//Render the content area
		g.setColor(background);
		renderer.setBounds(contentRendererBounds_pixels.getBounds());
		g.translate(contentRendererBounds_pixels.getX(), contentRendererBounds_pixels.getY());
		renderer.paint(g);		
		g.translate(-contentRendererBounds_pixels.getX(), -contentRendererBounds_pixels.getY());
		
		//Render the close button
		if(showCloseButton) {
			renderCloseButton(g, balloonBounds_pixels.closeButtonBounds, origStroke);
		}
		
		g.setColor(origColor);
	}
	
	/**
	 * Check whether the renderer preferrred size has changed since the last rendering.
	 * 
	 * @return
	 */
	protected boolean checkRendererPreferredSizeChanged() {
		if(rendererPreferredSize != null) {
			Dimension currentPreferredSize = renderer.getPreferredSize();
			if(currentPreferredSize != null) {
				return rendererPreferredSize.width != currentPreferredSize.width ||
						rendererPreferredSize.height != currentPreferredSize.height;
			} else {
				return true;
			}
		} else {
			return true;
		}
	}
	
	private void renderCloseButton(Graphics2D g, Rectangle2D closeButtonBounds, Stroke origStroke) {
		g.setColor(Color.GRAY);
		g.draw(closeButtonBounds);		
		g.setStroke(new BasicStroke(1.75f));	
		g.setColor(Color.RED);
		int offset = 3;
		Line2D line = new Line2D.Double(closeButtonBounds.getX() + offset, 
				closeButtonBounds.getY() + offset, 
				closeButtonBounds.getX() + closeButtonBounds.getWidth() - offset, 
				closeButtonBounds.getY() + closeButtonBounds.getHeight() - offset);
		g.draw(line);
		line.setLine(closeButtonBounds.getX() + offset, 
				closeButtonBounds.getY() + closeButtonBounds.getHeight() - offset, 
				closeButtonBounds.getX() + closeButtonBounds.getWidth() - offset,
				closeButtonBounds.getY() + offset);
		g.draw(line);
		g.setStroke(origStroke);
	}
	
	/**
	 * @param orientation
	 * @param endLocationPixel
	 * @param offset_pixels
	 * @param balloonWidth
	 * @param balloonHeight
	 * @return
	 */
	private RoundRectangle2D calculateBalloonRectangleBounds(boolean positionRelativeToParent, 
			Point2D centerPixel, AnnotationOrientation orientation, 
			Point2D endPointPixel, int offset_pixels, int balloonWidth, int balloonHeight) {				
		Point2D center =  positionRelativeToParent ?
					AnnotationUtils.computeAnnotationCenterLocation_pixels(orientation, endPointPixel, offset_pixels, 
							balloonWidth, balloonHeight) : centerPixel;
		return new RoundRectangle2D.Double(center.getX() - balloonWidth/2.0, center.getY() - balloonHeight/2.0,
				balloonWidth, balloonHeight, roundedArcWidth, roundedArcWidth);
	}
	
	private Rectangle2D calculateContentAreaBounds(Rectangle2D balloonRectangleBounds, boolean showCloseButton) {
		return showCloseButton ? new Rectangle2D.Double(balloonRectangleBounds.getX(), balloonRectangleBounds.getY(),
						balloonRectangleBounds.getWidth() - closeButtonSize - 10,
						balloonRectangleBounds.getHeight()) : balloonRectangleBounds;
	}
	
	/**
	 * @param orientation
	 * @param endPointPixel
	 * @param renderProperties
	 * @return
	 */
//	private Rectangle2D calculateContentAreaBounds(AnnotationOrientation orientation, Point2D endPointPixel, 
//			int balloonWidth, int contentAreaWidth, int contentAreaHeight) {
//		if(orientation == AnnotationOrientation.Center || orientation == AnnotationOrientation.Center_1) {
//			//Case where the annotation orientation is that the center
//			return new Rectangle2D.Double(endPointPixel.getX() - contentAreaWidth/2.0,
//					endPointPixel.getY() - contentAreaHeight/2.0, 
//					contentAreaWidth, contentAreaHeight);
//		} else {			
//			//Case where the annotation orientation is not at the center
//			Double angle = AnnotationUtils.getHeadingRadians(orientation);
//			Double cos = Math.cos(angle);
//			Double sin = Math.sin(angle);
//			return new Rectangle2D.Double(
//					endPointPixel.getX() + (offset_pixels * cos) + ((contentAreaWidth/2.0) * cos) - contentAreaWidth/2.0,
//					endPointPixel.getY() - (offset_pixels * sin) - ((contentAreaHeight/2.0) * sin) - contentAreaHeight/2.0,
//					contentAreaWidth, contentAreaHeight);
//		}
//		/*switch(orientation) {
//		case Center: case Center_1:
//			break;
//		case East:
//			return new Rectangle2D.Double(endPointPixel.getX() + offset_pixels, 
//					endPointPixel.getY() - contentAreaHeight / 2.0, 
//					contentAreaWidth, contentAreaHeight);
//		case North:
//			return new Rectangle2D.Double(endPointPixel.getX() - balloonWidth / 2.0, 
//					endPointPixel.getY() - offset_pixels - contentAreaHeight, 
//					contentAreaWidth, contentAreaHeight);
//		case NorthEast:
//			break;
//		case NorthWest:
//			break;
//		case South:
//			return new Rectangle2D.Double(endPointPixel.getX() - balloonWidth / 2.0,
//					endPointPixel.getY() + offset_pixels, 
//					contentAreaWidth, contentAreaHeight);
//		case SouthEast:
//			break;
//		case SouthWest:
//			break;
//		case West:
//			return new Rectangle2D.Double(endPointPixel.getX() - offset_pixels - balloonWidth,
//					endPointPixel.getY() - contentAreaHeight / 2.0, 
//					contentAreaWidth, contentAreaHeight);
//		default:
//			break;		
//		}
//		return null;*/
//	}	
	
	/**
	 * @param orientation
	 * @param endPointPixel
	 * @param renderProperties
	 * @return
	 */
	private BalloonBounds calculateBalloonArea(AnnotationOrientation orientation, 
			Point2D endLocationPixel, RenderProperties renderProperties,
			int balloonWidth, int balloonHeight, BalloonBounds bounds) { //int contentAreaWidth, int contentAreaHeight) {
		if(bounds == null) {
			bounds = new BalloonBounds();
		}
		/*bounds.contentAreaBounds = calculateContentAreaBounds(orientation, endPointPixel, balloonWidth, 
				contentAreaWidth, contentAreaHeight);
		double width = 0;
		if(showCloseButton) {
			bounds.closeButtonBounds = new Rectangle2D.Double(
					bounds.contentAreaBounds.getX() + bounds.contentAreaBounds.getWidth() + 5,
					bounds.contentAreaBounds.getY() + 5, 
					closeButtonSize, closeButtonSize);
			width = bounds.contentAreaBounds.getWidth() + closeButtonSize + 10;
		} else {
			width = bounds.contentAreaBounds.getWidth();
		}

		//Compute the balloon rectangle
		RoundRectangle2D roundRectangle = new RoundRectangle2D.Double(
			bounds.contentAreaBounds.getX(), bounds.contentAreaBounds.getY(),
			width, bounds.contentAreaBounds.getHeight(), 
			roundedArcWidth, roundedArcWidth);
		bounds.balloonArea = new Area(roundRectangle);*/
		
		//Compute the balloon rectangle
		Point2D centerLocationPixel = positionRelativeToParent ? null : renderProperties.geoPositionToWorldPixel(center); 
		RoundRectangle2D roundRectangle = calculateBalloonRectangleBounds(positionRelativeToParent, 
				centerLocationPixel, orientation, 
				endLocationPixel, offset_pixels, balloonWidth, balloonHeight);
		bounds.roundRectangle = roundRectangle;
		bounds.balloonArea = new Area(roundRectangle);
		
		//Compute the content area bounds
		bounds.contentAreaBounds = calculateContentAreaBounds(roundRectangle.getBounds2D(), showCloseButton);
		
		//Compute the close button bounds
		if(showCloseButton) {
			bounds.closeButtonBounds = new Rectangle2D.Double(
				bounds.contentAreaBounds.getX() + bounds.contentAreaBounds.getWidth() + 5,
				bounds.contentAreaBounds.getY() + 5, 
				closeButtonSize, closeButtonSize);
		} 

		//Compute the points of the balloon connector tail
		if(positionRelativeToParent && drawBalloonConnector && orientation != AnnotationOrientation.Center && orientation != AnnotationOrientation.Center_1) {			
			Path2D path = new Path2D.Double();
			//Point2D startPoint = null;
			Point2D startPoint = getAnnotationLineStartPixelLocation(orientation, roundRectangle);
			switch(orientation) {
			case East:
				path.moveTo(startPoint.getX(), startPoint.getY() - connectorWidth/2);
				path.lineTo(startPoint.getX(), startPoint.getY() + connectorWidth/2);
				path.lineTo(endLocationPixel.getX(), endLocationPixel.getY());
				path.lineTo(startPoint.getX(), startPoint.getY() - connectorWidth/2);
				/*path.moveTo(startPoint.getX(), startPoint.getY());
				path.lineTo(startPoint.getX(), startPoint.getY() + connectorWidth);
				path.lineTo(endLocationPixel.getX(), endLocationPixel.getY());
				path.lineTo(startPoint.getX(), startPoint.getY());*/
				break;
			case North:
				path.moveTo(startPoint.getX() - connectorWidth/2, startPoint.getY());
				path.lineTo(startPoint.getX() + connectorWidth/2, startPoint.getY());
				path.lineTo(endLocationPixel.getX(), endLocationPixel.getY());
				path.lineTo(startPoint.getX() - connectorWidth/2, startPoint.getY());				
				/*path.moveTo(startPoint.getX(), startPoint.getY());
				path.lineTo(startPoint.getX() + connectorWidth, startPoint.getY());
				path.lineTo(endLocationPixel.getX(), endLocationPixel.getY());
				path.lineTo(startPoint.getX(), startPoint.getY());*/
				break;
			case NorthEast:				
				startPoint.setLocation(startPoint.getX() + roundedArcWidth/2, startPoint.getY());
				path.moveTo(startPoint.getX(), startPoint.getY());
				path.lineTo(startPoint.getX() + connectorWidth, startPoint.getY());
				path.lineTo(endLocationPixel.getX(), endLocationPixel.getY());
				path.lineTo(startPoint.getX(), startPoint.getY());
				break;
			case NorthWest:
				startPoint.setLocation(startPoint.getX() - roundedArcWidth/2, startPoint.getY());
				path.moveTo(startPoint.getX(), startPoint.getY());
				path.lineTo(startPoint.getX() - connectorWidth, startPoint.getY());
				path.lineTo(endLocationPixel.getX(), endLocationPixel.getY());
				path.lineTo(startPoint.getX(), startPoint.getY());
				break;
			case South:
				path.moveTo(startPoint.getX() - connectorWidth/2, startPoint.getY());
				path.moveTo(startPoint.getX() + connectorWidth/2, startPoint.getY());
				path.lineTo(endLocationPixel.getX(), endLocationPixel.getY());
				path.lineTo(startPoint.getX() - connectorWidth/2, startPoint.getY());
				/*path.moveTo(startPoint.getX(), startPoint.getY());
				path.lineTo(startPoint.getX() + connectorWidth, endLocationPixel.getY() + offset_pixels);
				path.lineTo(endLocationPixel.getX(), endLocationPixel.getY());
				path.lineTo(startPoint.getX(), startPoint.getY());*/
				break;
			case SouthEast:				
				startPoint.setLocation(startPoint.getX() + roundedArcWidth/2, startPoint.getY());
				path.moveTo(startPoint.getX(), startPoint.getY());
				path.lineTo(startPoint.getX() + connectorWidth, startPoint.getY());
				path.lineTo(endLocationPixel.getX(), endLocationPixel.getY());
				path.lineTo(startPoint.getX(), startPoint.getY());				
				break;
			case SouthWest:
				startPoint.setLocation(startPoint.getX() - roundedArcWidth/2, startPoint.getY());
				path.moveTo(startPoint.getX(), startPoint.getY());
				path.lineTo(startPoint.getX() - connectorWidth, startPoint.getY());
				path.lineTo(endLocationPixel.getX(), endLocationPixel.getY());
				path.lineTo(startPoint.getX(), startPoint.getY());
				break;
			case West:				
				path.moveTo(startPoint.getX(), startPoint.getY() - connectorWidth/2);
				path.lineTo(startPoint.getX(), startPoint.getY() + connectorWidth/2);
				path.lineTo(endLocationPixel.getX(), endLocationPixel.getY());
				path.lineTo(startPoint.getX(), startPoint.getY() - connectorWidth/2);
				/*path.moveTo(startPoint.getX(), startPoint.getY());
				path.lineTo(startPoint.getX(), startPoint.getY() + connectorWidth);
				path.lineTo(endLocationPixel.getX(), endLocationPixel.getY());
				path.lineTo(startPoint.getX(), startPoint.getY());*/
				break;
			default:
				break;
			}			
			bounds.balloonArea.add(new Area(path));
		}
		
		return bounds;
	}	
	
	protected static class BalloonBounds {
		public Rectangle2D contentAreaBounds;
		
		public Rectangle2D closeButtonBounds;
		
		public RoundRectangle2D roundRectangle;
		
		public Area balloonArea;		
	}
}