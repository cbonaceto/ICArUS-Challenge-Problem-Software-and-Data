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

import javax.swing.event.MouseInputListener;

import org.mitre.icarus.cps.app.widgets.map.IMapPanel.CursorType;
import org.mitre.icarus.cps.app.widgets.map.phase_2.MapPanel_Phase2;
import org.mitre.icarus.cps.app.widgets.map.phase_2.layers.IEditableLayer;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.Placemark;

/**
 * Placemark editor controller.
 * 
 * @author Craig Bonaceto
 *
 */
public class PlacemarkEditor<M extends MapPanel_Phase2> implements IMapObjectEditor<Placemark, M>, MouseInputListener {

	protected M map;
	
	protected IEditableLayer<? super Placemark, ? extends MapPanel_Phase2> layer;
	
	protected boolean editing = false;
	
	protected Placemark placemark;
	
	private boolean draggable = false;
	
	protected boolean overControlPoint = false;
	
	private boolean mouseOffMap = false;
	
	protected long editStartTime = -1;
	
	@Override
	public IMapObjectEditor<Placemark, M> createEditorInstance() {
		return new PlacemarkEditor<M>();
	}
	
	@Override
	public void editMapObject(Placemark placemark, IEditableLayer<? super Placemark, ? super M> layer, M map) {
		if(!editing) {
			editing = true;
			overControlPoint = false;
			this.map = map;			
			this.placemark = placemark;
			this.layer = layer;
			placemark.setSelectable(false);
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
			placemark.setSelectable(true);
			editStartTime = -1;
			
			//Notify the layer that this object is no longer being edited			
			if(layer != null) {
				layer.doneEditingObject(placemark);
			}
		}
	}
	
	@Override
	public Placemark getEditingMapObject() {
		if(editing) {
			return placemark;
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
	public void mouseClicked(MouseEvent arg0) {	
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		mouseOffMap = false;
	}

	@Override
	public void mouseExited(MouseEvent event) {
		mouseOffMap = true;		
	}

	@Override
	public void mousePressed(MouseEvent event) {		
		if(placemark.getPixelShape() != null && 
				placemark.getPixelShape().contains(map.viewportPixelToWorldPixel(event.getPoint()))) {
			draggable = true;
			editStartTime = System.currentTimeMillis();
		} else {
			editStartTime = -1;
		}
	}

	@Override
	public void mouseReleased(MouseEvent event) {
		draggable = false;
		if(editStartTime > 0 && placemark.getEditTime() != null) {
			placemark.setEditTime(placemark.getEditTime() + (System.currentTimeMillis() - editStartTime));
			editStartTime = -1;
		}
	}

	@Override
	public void mouseDragged(MouseEvent event) {		
		if(mouseOffMap) {
			return;
		}
		
		if(draggable) {			
			placemark.setCenterGeoLocation(map.convertPointToGeoPosition(event.getPoint()));			
			map.redraw(placemark.getLayer());
		}
	}

	@Override
	public void mouseMoved(MouseEvent event) {		
		if(placemark.getPixelShape() != null && 
				placemark.getPixelShape().contains(map.viewportPixelToWorldPixel(event.getPoint()))) {
			if(!overControlPoint) {
				map.editorArmed(this, CursorType.MOVE);
			}
			overControlPoint = true;
		} else {
			if(overControlPoint) {
				map.editorUnarmed(this);
			}
			overControlPoint = false;
		}
	}
}