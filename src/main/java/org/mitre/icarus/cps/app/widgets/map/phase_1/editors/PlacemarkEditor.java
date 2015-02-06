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

import org.mitre.icarus.cps.app.widgets.map.IMapPanel.CursorType;
import org.mitre.icarus.cps.app.widgets.map.phase_1.IMapPanel_Phase1;
import org.mitre.icarus.cps.app.widgets.map.phase_1.layers.IEditableLayer;
import org.mitre.icarus.cps.app.widgets.map.phase_1.objects.PlacemarkMapObject;
import org.mitre.icarus.cps.feature_vector.phase_1.GridLocation2D;

/**
 * @author Craig Bonaceto and Eric Kappotis
 *
 */
public class PlacemarkEditor implements IMapObjectEditor<PlacemarkMapObject>, MouseListener, MouseMotionListener {

	protected IMapPanel_Phase1 map;
	
	protected IEditableLayer<? super PlacemarkMapObject> layer;
	
	protected boolean editing = false;
	
	protected PlacemarkMapObject placemark;
	
	private boolean draggable = false;
	
	protected boolean overControlPoint = false;
	
	private boolean mouseOffMap = false;
	
	protected long editStartTime = -1;
	
	@Override
	public void editMapObject(PlacemarkMapObject placemark, IEditableLayer<? super PlacemarkMapObject> layer, IMapPanel_Phase1 map) {
		if(!editing) {
			editing = true;
			this.map = map;			
			this.placemark = placemark;
			this.layer = layer;
			placemark.setSelectable(false);
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
			map.removeMouseListener(this);
			map.removeMouseMotionListener(this);
			placemark.setSelectable(true);
			editStartTime = -1;
			
			//Notify the layer that this object is no longer being edited			
			if(layer != null) {
				layer.doneEditingObject(placemark);
			}
		}
	}
	
	@Override
	public PlacemarkMapObject getEditingMapObject() {
		if(editing) {
			return placemark;
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
	public void mouseClicked(MouseEvent arg0) {}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		mouseOffMap = false;
	}

	@Override
	public void mouseExited(MouseEvent event) {
		mouseOffMap = true;
		//draggable = false;
		//overControlPoint = false;
	}

	@Override
	public void mousePressed(MouseEvent event) {
		if(placemark.contains(map.translateMouseToPixel(event.getX(), event.getY()))) {
			draggable = true;
			editStartTime = System.currentTimeMillis();
		} else {
			editStartTime = -1;
		}
	}

	@Override
	public void mouseReleased(MouseEvent event) {
		draggable = false;
		if(editStartTime > 0) {
			placemark.setEditTime(placemark.getEditTime() + (System.currentTimeMillis() - editStartTime));
			editStartTime = -1;
			//System.out.println("updated center edit time: " + placemark.getEditTime());
		}
	}

	@Override
	public void mouseDragged(MouseEvent event) {
		if(mouseOffMap) {
			return;
		}
		
		if(draggable) {
			GridLocation2D centerLocation = placemark.getCenterLocation();
			if(centerLocation == null) {
				placemark.setCenterLocation(map.getGridLocation(event.getPoint()));	
			}
			else {
				centerLocation.updateLocation(map.getGridLocation(event.getPoint()));
				placemark.locationChanged();
			}
			map.redraw(placemark.getLayer());
		}
	}

	@Override
	public void mouseMoved(MouseEvent event) {
		if(placemark.contains(map.translateMouseToPixel(event.getX(), event.getY()))) {
			map.setCursor(CursorType.MOVE);
			overControlPoint = true;
		}
		else {
			overControlPoint = false;
		}
	}
}