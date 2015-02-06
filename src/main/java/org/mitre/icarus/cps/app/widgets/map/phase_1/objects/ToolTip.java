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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

import org.mitre.icarus.cps.app.widgets.map.phase_1.MapConstants_Phase1;
import org.mitre.icarus.cps.app.widgets.map.phase_1.RenderProperties;
import org.mitre.icarus.cps.app.widgets.map.phase_1.objects.IAnnotationMapObject.AnnotationOrientation;
import org.mitre.icarus.cps.feature_vector.phase_1.GridLocation2D;

/**
 * @author Eric Kappotis
 *
 */
public class ToolTip extends AbstractMapObject_Phase1 {
	
	private boolean selectable = false;
	private IAnnotatableMapObject mapObject;
	protected JLabel toolTipLabel;
	
	protected int offset_pixels = 15;
	protected double preferredToolTipWidth = 150;
	
	double connectorWidth = 10;
	
	protected Color backgroundColor = MapConstants_Phase1.TOOLTIP_BACKGROUND_COLOR;
	protected Color foregroundColor = MapConstants_Phase1.TOOLTIP_BORDER_COLOR;
	
	private Area toolTipArea = null;
	
	protected Rectangle2D labelBounds;
	
	protected static final List<AnnotationOrientation> orderedOrientationList = new ArrayList<AnnotationOrientation>();
	static {
		orderedOrientationList.add(AnnotationOrientation.North);
		//orderedOrientationList.add(AnnotationOrientation.NorthEast);
		orderedOrientationList.add(AnnotationOrientation.East);
		//orderedOrientationList.add(AnnotationOrientation.SouthEast);
		orderedOrientationList.add(AnnotationOrientation.South);
		//orderedOrientationList.add(AnnotationOrientation.SouthWest);
		orderedOrientationList.add(AnnotationOrientation.West);
		//orderedOrientationList.add(AnnotationOrientation.NorthWest);
	}
	
	public ToolTip(IAnnotatableMapObject mapObject) {
		this.setMapObject(mapObject);
		toolTipLabel = new JLabel();
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.gui.feature_vector.phase_1.objects.IMapObject#isSelectable()
	 */
	@Override
	public boolean isSelectable() {
		return selectable;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.gui.feature_vector.phase_1.objects.IMapObject#setSelectable(boolean)
	 */
	@Override
	public void setSelectable(boolean selectable) {
		this.selectable = selectable;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.gui.feature_vector.phase_1.objects.MapObject#render(java.awt.Graphics2D, org.mitre.icarus.cps.gui.feature_vector.phase_1.RenderData)
	 */
	@Override
	public void render(Graphics2D g2d, RenderProperties renderData, boolean renderPropertiesChanged) {
		Color origColor = g2d.getColor();
		Stroke origStroke = g2d.getStroke();
		
		if(objectBoundsChanged || renderPropertiesChanged || labelBounds == null || toolTipArea == null ||
				!mapObject.getToolTipText().equals(toolTipLabel.getText())) {
			//Determine where to place the tool tip
			toolTipLabel.setText(mapObject.getToolTipText());
			//System.out.println("checking orientations");

			// the rounded rectangle that will get rendered for the tool tip
			Point2D endLocationPixel = null;
			Area toolTipArea = null;
			AnnotationOrientation currOrientation = null;
			for(AnnotationOrientation orientation : orderedOrientationList) {
				currOrientation = orientation;
				GridLocation2D endLocation =  mapObject.getAnnotationLineEndLocation(orientation);
				endLocationPixel =renderData.translateToOffsetPixel(endLocation);			

				Area projectedToolTipArea = calculateToolTipArea(orientation, endLocationPixel, renderData);

				// make sure the bounds of the map completely contain the rectangle of the projected tool tip
				if(renderData.renderBounds.contains(projectedToolTipArea.getBounds2D())) {				
					//toolTipArea = projectedToolTipArea;				
					endLocationPixel = renderData.translateToPixel(endLocation);
					toolTipArea = calculateToolTipArea(orientation, endLocationPixel, renderData);
					break;
				}
			}

			// If the tool tip area never gets created, we just place the tool tip to the north and it gets clipped
			if(toolTipArea == null) {
				currOrientation = AnnotationOrientation.North;
				endLocationPixel = renderData.translateToPixel(mapObject.getAnnotationLineEndLocation(currOrientation));			
				toolTipArea = calculateToolTipArea(AnnotationOrientation.North, endLocationPixel, renderData);			
			}
			
			this.toolTipArea = toolTipArea;
			Rectangle2D toolTipBounds = calculateToolTextBounds(currOrientation, endLocationPixel, renderData);
			labelBounds = new Rectangle2D.Double(toolTipBounds.getX() + toolTipBounds.getWidth() / 2.0 - toolTipLabel.getPreferredSize().getWidth() / 2.0, 
					toolTipBounds.getY(), toolTipLabel.getPreferredSize().getWidth(), toolTipLabel.getPreferredSize().getHeight());
		}
		
		if(backgroundColor == null) {
			backgroundColor = MapConstants_Phase1.TOOLTIP_BACKGROUND_COLOR;
		}
		g2d.setColor(backgroundColor);
		g2d.fill(toolTipArea);		

		if(foregroundColor == null) {
			foregroundColor = Color.black;
		}
		
		g2d.setStroke(new BasicStroke(1));
		g2d.setColor(foregroundColor);
		g2d.draw(toolTipArea);
		g2d.setStroke(origStroke);
		
		g2d.setColor(backgroundColor);				
		//g2d.drawLine(startingXLocation, yLocation, startingXLocation + triangleWidth, yLocation);		
		toolTipLabel.setBounds(labelBounds.getBounds());
		g2d.translate(labelBounds.getX(), labelBounds.getY());
		toolTipLabel.paint(g2d);
		g2d.translate(-labelBounds.getX(), -labelBounds.getY());
		g2d.setColor(origColor);
	}
	
	private Rectangle2D calculateToolTextBounds(AnnotationOrientation orientation, Point2D endPointPixel, RenderProperties renderData) {
		Rectangle2D toolTipBounds = null;
		Dimension toolTipTextPreferredSize = toolTipLabel.getPreferredSize();
		
		if(orientation == AnnotationOrientation.North) {
			toolTipBounds = new Rectangle2D.Double(endPointPixel.getX() - preferredToolTipWidth / 2.0, 
					endPointPixel.getY() - offset_pixels - toolTipTextPreferredSize.height, 
					preferredToolTipWidth, toolTipTextPreferredSize.height);			
		} else if(orientation == AnnotationOrientation.East) {
			toolTipBounds = new Rectangle2D.Double(endPointPixel.getX() + offset_pixels, 
					endPointPixel.getY() - toolTipTextPreferredSize.height / 2.0, preferredToolTipWidth, 
					toolTipTextPreferredSize.height);
		} else if(orientation == AnnotationOrientation.South) {
			toolTipBounds = new Rectangle2D.Double(endPointPixel.getX() - preferredToolTipWidth / 2.0,
					endPointPixel.getY() + offset_pixels, 
					preferredToolTipWidth, toolTipTextPreferredSize.height);
		} else {
			toolTipBounds = new Rectangle2D.Double(endPointPixel.getX() - offset_pixels - preferredToolTipWidth,
					endPointPixel.getY() - toolTipTextPreferredSize.height / 2.0, 
					preferredToolTipWidth, toolTipTextPreferredSize.height);
		}
		
		return toolTipBounds;
	}
	
	private Area calculateToolTipArea(AnnotationOrientation orientation, Point2D endPointPixel, RenderProperties renderData) {		
		Rectangle2D toolTipBounds = null;
		Dimension toolTipTextPreferredSize = toolTipLabel.getPreferredSize();
		
		Area toolTipArea = null;
		
		//Point2D endPointPixel = renderData.translateToPixel(mapObject.getAnnotationLineEndLocation(orientation));
		
		if(orientation == AnnotationOrientation.North) {
			toolTipBounds = new Rectangle2D.Double(endPointPixel.getX() - preferredToolTipWidth / 2.0, 
					endPointPixel.getY() - offset_pixels - toolTipTextPreferredSize.height, 
					preferredToolTipWidth, toolTipTextPreferredSize.height);
			
			RoundRectangle2D roundRectangle = new RoundRectangle2D.Double(toolTipBounds.getX(), toolTipBounds.getY(),
					toolTipBounds.getWidth(), toolTipBounds.getHeight(), 15.0, 15.0);			
			
			Path2D path = new Path2D.Double();
					
			Point2D startPoint = new Point2D.Double(endPointPixel.getX(), 
					toolTipBounds.getY() + toolTipBounds.getHeight());
			
			path.moveTo(startPoint.getX(), startPoint.getY());
			path.lineTo(startPoint.getX() + connectorWidth, startPoint.getY());
			path.lineTo(endPointPixel.getX(), endPointPixel.getY());
			path.lineTo(startPoint.getX(), startPoint.getY());
			
			toolTipArea = new Area(roundRectangle);
			Area connectorArea = new Area(path);
			toolTipArea.add(connectorArea);			
		} else if(orientation == AnnotationOrientation.East) {
			toolTipBounds = new Rectangle2D.Double(endPointPixel.getX() + offset_pixels, 
					endPointPixel.getY() - toolTipTextPreferredSize.height / 2.0, preferredToolTipWidth, 
					toolTipTextPreferredSize.height);
			
			RoundRectangle2D roundRectangle = new RoundRectangle2D.Double(toolTipBounds.getX(), toolTipBounds.getY(),
					toolTipBounds.getWidth(), toolTipBounds.getHeight(), 15.0, 15.0);
			
			Path2D path = new Path2D.Double();
			
			Point2D startPoint = new Point2D.Double(endPointPixel.getX() + offset_pixels, 
					toolTipBounds.getY() + toolTipBounds.getHeight() / 2.0 - connectorWidth / 2.0);
			
			path.moveTo(startPoint.getX(), startPoint.getY());
			path.lineTo(startPoint.getX(), startPoint.getY() + connectorWidth);
			path.lineTo(endPointPixel.getX(), endPointPixel.getY());
			path.lineTo(startPoint.getX(), startPoint.getY());
			
			toolTipArea = new Area(roundRectangle);
			Area connectorArea = new Area(path);
			toolTipArea.add(connectorArea);			
		} else if(orientation == AnnotationOrientation.South) {
			toolTipBounds = new Rectangle2D.Double(endPointPixel.getX() - preferredToolTipWidth / 2.0,
					endPointPixel.getY() + offset_pixels, 
					preferredToolTipWidth, toolTipTextPreferredSize.height);
			
			RoundRectangle2D roundRectangle = new RoundRectangle2D.Double(toolTipBounds.getX(), toolTipBounds.getY(),
					toolTipBounds.getWidth(), toolTipBounds.getHeight(), 15.0, 15.0);
			
			Path2D path = new Path2D.Double();
			
			Point2D startPoint = new Point2D.Double(endPointPixel.getX(), endPointPixel.getY() + offset_pixels);
			
			path.moveTo(startPoint.getX(), startPoint.getY());
			path.lineTo(startPoint.getX() + connectorWidth, endPointPixel.getY() + offset_pixels);
			path.lineTo(endPointPixel.getX(), endPointPixel.getY());
			path.lineTo(startPoint.getX(), startPoint.getY());
			
			toolTipArea = new Area(roundRectangle);
			Area connectorArea = new Area(path);
			toolTipArea.add(connectorArea);	
		} else {
			toolTipBounds = new Rectangle2D.Double(endPointPixel.getX() - offset_pixels - preferredToolTipWidth,
					endPointPixel.getY() - toolTipTextPreferredSize.height / 2.0, 
					preferredToolTipWidth, toolTipTextPreferredSize.height);
			
			RoundRectangle2D roundRectangle = new RoundRectangle2D.Double(toolTipBounds.getX(), toolTipBounds.getY(),
					toolTipBounds.getWidth(), toolTipBounds.getHeight(), 15.0, 15.0);
			
			Path2D path = new Path2D.Double();
			
			Point2D startPoint = new Point2D.Double(endPointPixel.getX() - offset_pixels, 
					toolTipBounds.getY() + toolTipBounds.getHeight() / 2.0 - connectorWidth / 2.0);
			
			path.moveTo(startPoint.getX(), startPoint.getY());
			path.lineTo(startPoint.getX(), startPoint.getY() + connectorWidth);
			path.lineTo(endPointPixel.getX(), endPointPixel.getY());
			path.lineTo(startPoint.getX(), startPoint.getY());
			
			toolTipArea = new Area(roundRectangle);
			Area connectorArea = new Area(path);
			toolTipArea.add(connectorArea);
		}
		
		return toolTipArea;
	}
	
	@Override
	public boolean contains(Point point) {
		return contains(point.x, point.y);
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.gui.feature_vector.phase_1.objects.MapObject#contains(java.awt.geom.Point2D)
	 */
	@Override
	public boolean contains(Point2D point) {
		return contains(point.getX(), point.getY());
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.gui.feature_vector.phase_1.objects.MapObject#contains(double, double)
	 */
	@Override
	public boolean contains(double x, double y) {
		if(toolTipArea != null) {
			return toolTipArea.contains(x, y);
		}
		return false;
	}

	/**
	 * @param mapObject the mapObject to set
	 */
	public void setMapObject(IAnnotatableMapObject mapObject) {
		this.mapObject = mapObject;
		objectBoundsChanged = true;
		toolTipArea = null;
	}

	/**
	 * @return the mapObject
	 */
	public IAnnotatableMapObject getMapObject() {
		return mapObject;
	}

	/**
	 * @return the backgroundColor
	 */
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * @param backgroundColor the backgroundColor to set
	 */
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	/**
	 * @return the foregroundColor
	 */
	public Color getForegroundColor() {
		return foregroundColor;
	}

	/**
	 * @param foregroundColor the foregroundColor to set
	 */
	public void setForegroundColor(Color foregroundColor) {
		this.foregroundColor = foregroundColor;
	}

	@Override
	public void setMouseOverState(boolean mouseOver, MouseEvent event) {
		//Does nothing
	}	
}