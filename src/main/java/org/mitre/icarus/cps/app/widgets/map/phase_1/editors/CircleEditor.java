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
package org.mitre.icarus.cps.app.widgets.map.phase_1.editors;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

import org.mitre.icarus.cps.app.widgets.map.IMapPanel.CursorType;
import org.mitre.icarus.cps.app.widgets.map.phase_1.IMapPanel_Phase1;
import org.mitre.icarus.cps.app.widgets.map.phase_1.layers.IEditableLayer;
import org.mitre.icarus.cps.app.widgets.map.phase_1.objects.CircleShape;
import org.mitre.icarus.cps.feature_vector.phase_1.GridLocation2D;

/**
 * Editor for circles.
 * 
 * @author CBONACETO
 *
 */
public class CircleEditor implements IMapObjectEditor<CircleShape>, MouseListener, MouseMotionListener {
	
	protected IMapPanel_Phase1 map;
	
	protected IEditableLayer<? super CircleShape> layer;
	
	protected CircleShape circle;
	
	protected boolean editing = false;
	
	private boolean draggable = false;
	
	private boolean overControlPoint = false;
	
	private boolean resizeable = false;
	
	private double minimumRadiusGridSpace = 3.0;
	
	private double mouseOverRadiusThickness = 10.0;
	
	private boolean mouseOffMap = false;	
	
	protected long editStartTime = -1;
	
	@Override
	public void editMapObject(CircleShape circle, IEditableLayer<? super CircleShape> layer, IMapPanel_Phase1 map) {		
		if(!editing) {
			editing = true;
			this.map = map;
			this.layer = layer;
			this.circle = circle;
			circle.setEditing(true);
			editStartTime = -1;

			map.addMouseListener(this);
			map.addMouseMotionListener(this);
		}
	}
	
	@Override
	public void doneEditingMapObject() {
		if(editing) {
			editing = false;
			overControlPoint = false;
			circle.setEditing(false);
			map.removeMouseListener(this);
			map.removeMouseMotionListener(this);
			editStartTime = -1;

			//Notify the layer that this object is no longer being edited
			if(layer != null) {
				layer.doneEditingObject(circle);
			}
		}
	}	
	
	@Override
	public CircleShape getEditingMapObject() {
		if(editing) {
			return circle;
		}
		return null;
	}

	@Override
	public boolean isEditingMapObject() {
		return editing;
	}
	
	public boolean isEditorArmed() {
		return overControlPoint;
	}

	@Override
	public void mouseDragged(MouseEvent event) {
		if(mouseOffMap) {
			return;
		}
		
		if(draggable) {
			GridLocation2D centerLocation = circle.getCenterLocation();
			if(centerLocation == null) {
				circle.setCenterLocation(map.getGridLocation(event.getPoint()));	
			}
			else {
				centerLocation.updateLocation(map.getGridLocation(event.getPoint()));
				circle.locationChanged();
			}			
			map.redraw(circle.getLayer());
		}
		else if(resizeable) {
			GridLocation2D centerLocation = circle.getCenterLocation();			
			GridLocation2D mouseLocation = map.getGridLocation(event.getPoint());
			
			// use the distance formula to determine the distance between the center
			// and the mouse location.
			double radius = Math.sqrt(Math.pow(centerLocation.x - mouseLocation.x, 2) 
					+ Math.pow(centerLocation.y - mouseLocation.y, 2));
			
			if(radius >= minimumRadiusGridSpace) {
				if(circle.getRadiusType() == CircleShape.RadiusType.Radius_GridUnits) {
					circle.setRadius(radius);
				}
				else {
					// convert the radius to pixel space
					circle.setRadius(map.translateToPixels(radius));
				}
				map.redraw();
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent event) {		
		Point2D mousePoint = map.translateMouseToPixel(event.getX(), event.getY());		
		
		if(circle.centerControlPointContains(mousePoint.getX(), mousePoint.getY())) {
			map.setCursor(CursorType.MOVE);
			overControlPoint = true;
		}		
		else { 			
			//TODO: Fix this to also go outside the circle radius by an equal number of pixels
			Ellipse2D totalCircle = circle.getCircle();
			Area circleArea = new Area(totalCircle);
			Ellipse2D innerCircle = 
				new Ellipse2D.Double(totalCircle.getX() + mouseOverRadiusThickness, totalCircle.getY() + mouseOverRadiusThickness, 
						totalCircle.getWidth() - mouseOverRadiusThickness * 2, totalCircle.getHeight() - mouseOverRadiusThickness * 2);		
			circleArea.subtract(new Area(innerCircle));

			if(circleArea.contains(mousePoint)) {
				// based on the angle, we change which way the mouse is going			
				// get the angle from the mouse location to the circle center location (in radians)
				overControlPoint = true;
				Point2D centerLocation_pixels = map.getPixelLocation(circle.getCenterLocation());
				double opposite = mousePoint.getY() - centerLocation_pixels.getY();
				double adjacent = mousePoint.getX() - centerLocation_pixels.getX();			
				double theta = Math.atan(opposite / adjacent);			
				double degrees = theta * 180 / Math.PI;			

				if(Math.abs(degrees) >= 80) {
					map.setCursor(CursorType.EXPAND_NORTH_SOUTH);
				}
				else {
					map.setCursor(CursorType.EXPAND_EAST_WEST);
				}
			}
			else {
				map.restoreCursor();
				overControlPoint = false;
			}
		}		
	}

	@Override
	public void mouseClicked(MouseEvent event) {}

	@Override
	public void mouseEntered(MouseEvent event) {
		mouseOffMap = false;
	}

	@Override
	public void mouseExited(MouseEvent event) {
		mouseOffMap = true;
		//draggable = false;
		//resizeable = false;
	}

	@Override
	public void mousePressed(MouseEvent event) {		
		Point2D mousePoint = map.translateMouseToPixel(event.getX(), event.getY());		

		if(circle.centerControlPointContains(mousePoint.getX(), mousePoint.getY())) {
			draggable = true;
		}
		else {
			Ellipse2D totalCircle = circle.getCircle();
			Area circleArea = new Area(totalCircle);
			Ellipse2D innerCircle = 
				new Ellipse2D.Double(totalCircle.getX() + mouseOverRadiusThickness, totalCircle.getY() + mouseOverRadiusThickness, 
						totalCircle.getWidth() - mouseOverRadiusThickness * 2, totalCircle.getHeight() - mouseOverRadiusThickness * 2);		
			circleArea.subtract(new Area(innerCircle));		
			if(circleArea.contains(mousePoint)) {
				resizeable = true;
			}
		}
		if(draggable || resizeable) {
			editStartTime = System.currentTimeMillis();
		} else {
			editStartTime = -1;
		}
	}

	@Override
	public void mouseReleased(MouseEvent event) {
		draggable = false;
		resizeable = false;
		if(editStartTime > 0) {
			circle.setEditTime(circle.getEditTime() + (System.currentTimeMillis() - editStartTime));
			editStartTime = -1;
			//System.out.println("updated circle edit time: " + circle.getEditTime());
		}
	}

	/**
	 * @param minimumRadiusGridSpace the minimumRadiusGridSpace to set
	 */
	public void setMinimumRadiusGridSpace(double minimumRadiusGridSpace) {
		this.minimumRadiusGridSpace = minimumRadiusGridSpace;
	}

	/**
	 * @return the minimumRadiusGridSpace
	 */
	public double getMinimumRadiusGridSpace() {
		return minimumRadiusGridSpace;
	}	
}