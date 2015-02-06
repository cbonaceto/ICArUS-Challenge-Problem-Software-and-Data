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
package org.mitre.icarus.cps.app.widgets.map.phase_2.editors;

import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

import javax.swing.event.MouseInputListener;

import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.mitre.icarus.cps.app.widgets.map.IMapPanel.CursorType;
import org.mitre.icarus.cps.app.widgets.map.phase_2.MapPanel_Phase2;
import org.mitre.icarus.cps.app.widgets.map.phase_2.layers.IEditableLayer;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.Circle;
import org.mitre.icarus.cps.feature_vector.phase_2.util.GeoUtils;

/**
 * Circle editor controller.
 * 
 * @author CBONACETO
 *
 */
public class CircleEditor<M extends MapPanel_Phase2> implements IMapObjectEditor<Circle, M>, MouseInputListener {
	
	protected M map;
	
	protected IEditableLayer<? super Circle, ? extends MapPanel_Phase2> layer;
	
	protected Circle circle;
	
	protected boolean editing = false;
	
	private boolean draggable = false;
	
	private boolean overControlPoint = false;
	
	private boolean resizeable = false;
	
	private double minimumRadius_degrees = 0.0008;

	private double mouseOverRadiusThickness = 10.0;
	
	private boolean mouseOffMap = false;	
	
	protected long editStartTime = -1;

	public double getMinimumRadius_degrees() {
		return minimumRadius_degrees;
	}

	public void setMinimumRadius_degrees(double minimumRadius_degrees) {
		this.minimumRadius_degrees = minimumRadius_degrees;
	}

	@Override
	public IMapObjectEditor<Circle, M> createEditorInstance() {
		return new CircleEditor<M>();
	}

	@Override
	public void editMapObject(Circle circle, IEditableLayer<? super Circle, ? super M> layer, M map) {
		if(!editing) {
			editing = true;
			overControlPoint = false;
			this.map = map;
			this.layer = layer;
			this.circle = circle;
			circle.setEditing(true);
			editStartTime = -1;
			map.activateEditor(this);
		}
	}		

	@Override
	public void doneEditingMapObject() {
		if(editing) {
			map.deactivateEditor(this);
			editing = false;
			overControlPoint = false;
			circle.setEditing(false);
			editStartTime = -1;

			//Notify the layer that this object is no longer being edited
			if(layer != null) {
				layer.doneEditingObject(circle);
			}
		}
	}	
	
	@Override
	public Circle getEditingMapObject() {
		if(editing) {
			return circle;
		}
		return null;
	}

	@Override
	public boolean isEditingMapObject() {
		return editing;
	}
	
	@Override
	public boolean isEditorArmed() {
		return overControlPoint;
	}	

	@Override
	public MouseInputListener getMouseListener() {
		return this;
	}

	@Override
	public KeyListener getKeyListener() {
		return null;
	}

	@Override
	public void mouseDragged(MouseEvent event) {
		if(mouseOffMap) {
			return;
		}
		
		if(draggable) {
			circle.setCenterGeoLocation(map.convertPointToGeoPosition(event.getPoint()));			
			map.repaint();
		} else if(resizeable) {
			Point2D centerGeoLocation = circle.getCenterGeoLocation();
			GeoPosition mouseGeoLocation = map.convertPointToGeoPosition(event.getPoint());
			
			//Determine the distance between the center and the mouse location to get the new radius in degrees
			//TODO: Should be using GeoUtils to compute this distance in miles
			double radius = Math.sqrt(Math.pow(centerGeoLocation.getX() - mouseGeoLocation.getLongitude(), 2) 
					+ Math.pow(centerGeoLocation.getY() - mouseGeoLocation.getLatitude(), 2));			
			
			if(radius >= minimumRadius_degrees) {
				if(circle.getRadiusType() == Circle.RadiusType.Miles) {
					//circle.setRadius(radius);
					circle.setRadius(radius * 
							GeoUtils.metersToMiles(GeoUtils.computeLengthOfDegreeOfLongitude_meters(centerGeoLocation.getY())));
				} else {
					//convert the radius to pixel space
					circle.setRadius(map.degreesToPixels(radius));
				}
				map.repaint();
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent event) {		
		Point2D mousePixelLocation = map.viewportPixelToWorldPixel(event.getPoint());		
		
		if(circle.findControlPointContains(mousePixelLocation) != null) {
			map.editorArmed(this, CursorType.MOVE);
			overControlPoint = true;
		} else { 			
			//TODO: Fix this to also go outside the circle radius by an equal number of pixels
			Ellipse2D totalCircle = circle.getPixelShape();
			if(totalCircle != null) {
				Area circleArea = new Area(totalCircle);
				Ellipse2D innerCircle = 
						new Ellipse2D.Double(totalCircle.getX() + mouseOverRadiusThickness, totalCircle.getY() + mouseOverRadiusThickness, 
								totalCircle.getWidth() - mouseOverRadiusThickness * 2, totalCircle.getHeight() - mouseOverRadiusThickness * 2);		
				circleArea.subtract(new Area(innerCircle));

				if(circleArea.contains(mousePixelLocation)) {
					// based on the angle, we change which way the mouse is going			
					// get the angle from the mouse location to the circle center location (in radians)
					overControlPoint = true;
					//Point2D centerLocation_pixels = circle.getCenterPixelLocation();
					Point2D centerLocation_pixels = map.geoPositionToWorldPixel(circle.getCenterGeoLocation());
					double opposite = mousePixelLocation.getY() - centerLocation_pixels.getY();
					double adjacent = mousePixelLocation.getX() - centerLocation_pixels.getX();			
					double theta = Math.atan(opposite / adjacent);			
					double degrees = theta * 180 / Math.PI;	

					map.editorArmed(this, (Math.abs(degrees) >= 80) ?  CursorType.EXPAND_NORTH_SOUTH : CursorType.EXPAND_EAST_WEST);
				} else {	
					if(overControlPoint) {
						map.editorUnarmed(this);
					}
					overControlPoint = false;
				}
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
	}

	@Override
	public void mousePressed(MouseEvent event) {		
		Point2D mousePixelLocation = map.viewportPixelToWorldPixel(event.getPoint());		

		if(circle.findControlPointContains(mousePixelLocation) != null) {
			draggable = true;
		} else {
			Ellipse2D totalCircle = circle.getPixelShape();
			if(totalCircle != null) {
				Area circleArea = new Area(totalCircle);
				Ellipse2D innerCircle = 
						new Ellipse2D.Double(totalCircle.getX() + mouseOverRadiusThickness, totalCircle.getY() + mouseOverRadiusThickness, 
								totalCircle.getWidth() - mouseOverRadiusThickness * 2, totalCircle.getHeight() - mouseOverRadiusThickness * 2);		
				circleArea.subtract(new Area(innerCircle));		
				if(circleArea.contains(mousePixelLocation)) {
					resizeable = true;
				}
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
		if(editStartTime > 0 && circle.getEditTime() != null) {
			circle.setEditTime(circle.getEditTime() + (System.currentTimeMillis() - editStartTime));
			editStartTime = -1;
		}
	}
}