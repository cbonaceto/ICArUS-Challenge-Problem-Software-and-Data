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
package org.mitre.icarus.cps.app.widgets.map.phase_2;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ToolTipManager;

import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.TileFactory;
import org.mitre.icarus.cps.app.widgets.map.layers.ILayer;
import org.mitre.icarus.cps.app.widgets.map.objects.IMapObject;
import org.mitre.icarus.cps.app.widgets.map.phase_2.editors.IMapObjectEditor;
import org.mitre.icarus.cps.app.widgets.map.phase_2.event.MapEvent;
import org.mitre.icarus.cps.app.widgets.map.phase_2.event.MapEventListener;
import org.mitre.icarus.cps.app.widgets.map.phase_2.layers.IEditableLayer;
import org.mitre.icarus.cps.app.widgets.map.phase_2.layers.ILayer_Phase2;
import org.mitre.icarus.cps.app.widgets.map.phase_2.layers.LayerManager;
import org.mitre.icarus.cps.app.widgets.map.phase_2.layers.LayerManager.RenderPropertiesChangeDetector;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.IMapObject_Phase2;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.ScaleBar;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.annotation.IAnnotatableMapObject;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.annotation.InformationBalloon;

/**
 * 
 * 
 * @author CBONACETO
 *
 */
//TODO: Constrain pan limits, add zoom slider
public class MapPanel_Phase2 extends JXMapViewer implements IMapPanel_Phase2 {
	
	private static final long serialVersionUID = -2757445210154579073L;	
	
	/** Manages the map layers */
	protected LayerManager<MapPanel_Phase2> layerManager;
	
	/** Current map render properties */
	protected MutableRenderProperties mutableRenderProperties;	
	protected RenderProperties renderProperties;
	
	/** Whether the the custom mouse pointer is set */
	protected boolean customCursorSet = false;
	
	/** Whether the cursor is locked (cannot be change until restoreCursor is called) */
	protected boolean cursorLocked = false;
	
	/** The current editor */
	protected IMapObjectEditor<? extends IMapObject_Phase2, ? extends MapPanel_Phase2> editor;
	
	/** Whether the current editor is armed (e.g., actively editing an object) */
	protected boolean editorArmed = false;
	
	/** Whether to show the map scale bar */
	protected boolean showScaleBar = false;
	
	/** The map scale bar */
	protected ScaleBar scaleBar;
	
	/** The mouse listener for panning the map */
	protected IcarusMouseInputListener mouseListener;	
	
	/** The tooltip information balloon manager */	
	protected MapToolTipManager toolTipManager;
	
	/** Whether tooltips on mouse click are enabled */
	protected boolean toolTipsEnabled = true;	

	/** Whether tool tips on mouse hover are enabled */
	protected boolean hoverToolTipsEnabled = false;
	
	/** The map object the mouse is currently over */
	private IMapObject_Phase2 highlightedObject = null;
	
	/** Used to determine whether the render properties have changed since the map was last rendered */
	protected RenderPropertiesChangeDetector renderPropertiesChangeDetector = new RenderPropertiesChangeDetector();	
	
	/** Listeners registered to receive map events */
	private List<MapEventListener> mapEventListeners;
	
	public MapPanel_Phase2() {
		super(false, true);
		mouseListener = new IcarusMouseInputListener();
		initializeMouseAndKeyListeners(mouseListener, null, new PanAndZoomKeyListener());
		mutableRenderProperties = new MutableRenderProperties(getTileFactory());
		renderProperties = mutableRenderProperties.getRenderProperties();
		layerManager = new LayerManager<MapPanel_Phase2>();		
		setOverlayPainter(layerManager);
		//Register with the ToolTipManager to show tool tips when hovering over map objects
		ToolTipManager.sharedInstance().registerComponent(this);
		setHorizontalWrapped(false);
		//super.setToolTipText("Test");
		toolTipManager = new MapToolTipManager();
		mapEventListeners = Collections.synchronizedList(new LinkedList<MapEventListener>());
	}
	
	@Override
	public void setTileFactory(TileFactory factory) {
		super.setTileFactory(factory);
		mutableRenderProperties.setTileFactory(factory);
	}

	/**
	 * @return
	 */
	public RenderProperties getCurrentRenderProperties() {
		updateRenderProperties();
		return renderProperties;
	}	

	/**
	 * Get a reference to the layer manager.
	 * 
	 * @return
	 */
	public LayerManager<MapPanel_Phase2> getLayerManager() {
		return layerManager;
	}
	
	/**
	 * Add a layer to the map.  The layer will be added as the last layer in the Z order (rendered last).
	 * 
	 * @param layer - the layer to add
	 */
	public void addLayer(ILayer_Phase2<? extends IMapObject_Phase2, MapPanel_Phase2> layer) {	
		layerManager.addLayer(layer);		
	}
	
	/**
	 * Add a collection of layers to the map.
	 * 
	 * @param layers - the layers to add
	 */
	public void addLayers(Collection<? extends ILayer_Phase2<? extends IMapObject_Phase2, MapPanel_Phase2>> layers) {
		layerManager.addLayers(layers);
	}
	
	/**
	 * Remove a layer from the map.
	 * 
	 * @param layer - the layer to remove
	 */
	public void removeLayer(ILayer_Phase2<? extends IMapObject_Phase2, MapPanel_Phase2> layer) {		
		layerManager.removeLayer(layer);		
	}
	
	/**
	 * Remove a layer from the map with the given layer ID.
	 * 
	 * @param layerId
	 */
	public void removeLayer(String layerId) {
		layerManager.removeLayer(layerId);
	}
	
	/**
	 * Return whether the map contains the layer with the given layer ID.
	 * 
	 * @param layerId
	 * @return
	 */
	public boolean containsLayer(String layerId) {
		return layerManager.containsLayer(layerId);
	}
	
	/**
	 * Get a layer in the feature vector with the given layer ID. 
	 * 
	 * @param layerId
	 * @return
	 */
	public ILayer_Phase2<? extends IMapObject_Phase2, MapPanel_Phase2> getLayer(String layerId) {
		return layerManager.getLayer(layerId);
	}
	
	@Override
	public void redraw() {
		repaint();
	}	
	
	@Override
	public void redraw(ILayer<? extends IMapObject> modifiedLayer) {
		redraw();
	}
	
	/**
	 * Editors call this method when they begin editing an object.
	 * 
	 * @param editor
	 */
	public void activateEditor(IMapObjectEditor<? extends IMapObject_Phase2, ? extends MapPanel_Phase2> editor) {
		if(this.editor != null) {
			throw new IllegalArgumentException("There is already an active editor. " +
					"This editor must be deactivated before another editor can be activated.");
		}
		this.editor = editor;
		if(editor.getMouseListener() != null) {			
			addMouseListener(editor.getMouseListener());
			addMouseMotionListener(editor.getMouseListener());
		}
		if(editor.getKeyListener() != null) {
			addKeyListener(editor.getKeyListener());
		}
	}
	
	/** Editors call this method when they are done editing an object. */
	public void deactivateEditor(IMapObjectEditor<? extends IMapObject_Phase2, ? extends MapPanel_Phase2> editor) {
		if(editor == this.editor) {
			if(editor.getMouseListener() != null) {
				removeMouseListener(editor.getMouseListener());
				removeMouseMotionListener(editor.getMouseListener());
			}
			if(editor.getKeyListener() != null) {
				removeKeyListener(editor.getKeyListener());
			}			
			this.editor = null;		
			editorArmed = false;	
			mouseListener.setPanningEnabled(true);
			restoreCursor();
		}
	}	
	
	/**
	 * Editors call this method when they are actively editing an object. 
	 */
	public void editorArmed(IMapObjectEditor<? extends IMapObject_Phase2, ? extends MapPanel_Phase2> editor, CursorType editorCursor) {
		if(editor == this.editor) {
			if(!editorArmed) {				
				editorArmed = true;	
				mouseListener.setPanningEnabled(false);
			}
			if(editorCursor != null) {
				setCursor(editorCursor, true);
			}
		}
	}
	
	/**
	 * Editors call this method when they are done actively editing an object. 
	 */
	public void editorUnarmed(IMapObjectEditor<? extends IMapObject_Phase2, ? extends MapPanel_Phase2> editor) {
		if(editor == this.editor) {
			if(editorArmed) {				
				editorArmed = false;	
				mouseListener.setPanningEnabled(true);
				restoreCursor();
			}
		}
	}
	
	/** */
	public boolean isEditing() {
		return editor != null;
	}
	
	/** */
	public boolean isEditorArmed() {
		return editorArmed;
	}	

	/**
	 * Set the current mouse cursor. The mouse cursor cannot be changed again until
	 * restoreCursor is called.
	 * 
	 * @param cursorType the cursor type of the cursor
	 */
	@Override
	public void setCursor(CursorType cursorType) {
		setCursor(cursorType, true);
	}	
	
	protected void setCursor(CursorType cursorType, boolean lockCursor) {
		if(!cursorLocked) {			
			cursorLocked = lockCursor;
			customCursorSet = true;
			if(cursorType == CursorType.EXPAND_EAST_WEST) {
				super.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
			} else if(cursorType == CursorType.EXPAND_NORTH_SOUTH) {
				super.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
			} else if(cursorType == CursorType.HAND) {
				super.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			} else if(cursorType == CursorType.MOVE) {
				super.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
			} else {
				super.setCursor(Cursor.getDefaultCursor());
			}
		}
	}
	
	/**
	 * Restore the mouse cursor to the default cursor.
	 */
	@Override
	public void restoreCursor() {
		if(customCursorSet) {
			super.setCursor(Cursor.getDefaultCursor());
			customCursorSet = false;
		}
		cursorLocked = false;	
	}	
	
	public boolean isShowScaleBar() {
		return showScaleBar;
	}

	public void setShowScaleBar(boolean showScaleBar) {
		if(showScaleBar != this.showScaleBar) {
			this.showScaleBar = showScaleBar;
			if(showScaleBar && scaleBar == null) {
				scaleBar = new ScaleBar();
				scaleBar.setStartLocationOffset(new Point2D.Double(10, 20));
			}
			repaint();
		}
	}

	public boolean isToolTipsEnabled() {
		return toolTipsEnabled;
	}

	public void setToolTipsEnabled(boolean toolTipsEnabled) {
		if(toolTipsEnabled != this.toolTipsEnabled) {		
			this.toolTipsEnabled = toolTipsEnabled;
			if(!toolTipsEnabled) {
				toolTipManager.removeAllToolTips();
				redraw();				
			}			
		}
	}

	public boolean isMultipleToolTipsEnabled() {
		return toolTipManager.isMultipleToolTipsEnabled();
	}

	public void setMultipleToolTipsEnabled(boolean multipleToolTipsEnabled) {
		boolean redrawNeeded = toolTipManager.setMultipleToolTipsEnabled(multipleToolTipsEnabled);
		if(redrawNeeded) {			
			redraw();
		}		
	}

	public boolean isHoverToolTipsEnabled() {
		return hoverToolTipsEnabled;
	}

	public void setHoverToolTipsEnabled(boolean hoverToolTipsEnabled) {
		if(hoverToolTipsEnabled != this.hoverToolTipsEnabled) { 
			this.hoverToolTipsEnabled = hoverToolTipsEnabled;
		}
	}

	@Override
	public String getToolTipText(MouseEvent event) {
		if(hoverToolTipsEnabled && highlightedObject != null && !editorArmed &&
				(!toolTipsEnabled || !toolTipManager.isToolTipPresent(highlightedObject))) {				
			return highlightedObject.getToolTipText();
		} 
		return null;
	}

	/*@Override
	public Point getToolTipLocation(MouseEvent event) {		
		return event.getPoint();
	}*/

	@Override
	protected void doPaintComponent(Graphics gfx) {
		RenderProperties renderProperties = getCurrentRenderProperties();
		boolean renderPropertiesChanged = renderPropertiesChangeDetector.isRenderPropertiesChanged(renderProperties);
		Graphics2D g = (Graphics2D) gfx;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		//Render the map and layers
		super.doPaintComponent(g);
		
		//Render the current tool tip information balloon(s) (if any)
		if(toolTipsEnabled) {
			toolTipManager.renderToolTips(g, renderProperties);
		}		

		//Render the scale bar
		if(showScaleBar && scaleBar != null) {
			scaleBar.render(g, renderProperties, renderPropertiesChanged);
		}
	}

	/**
	 * Converts the specified GeoPosition (Point2D where x = longitude, y = latitude) to a point in the JXMapViewer's local coordinate space. 
	 * This method is especially useful when drawing lat/long positions on the map. 
	 * 
	 * @param pos
	 * @return
	 */
	public Point2D convertGeoPositionToPoint(Point2D pos) {
		return convertGeoPositionToPoint(new GeoPosition(pos.getY(), pos.getX()));
	}	
	
	/**
	 * Converts a distance in degrees of longitude to a distance in pixels.
	 * 
	 * @param degrees
	 * @return
	 */
	public double degreesToPixels(double degrees) {
		updateRenderProperties();
		return renderProperties.degreesToPixels(degrees);
	}
	
	/**
	 * Converts a distance in pixels to a distance in degrees of longitude.
	 * 
	 * @param pixels
	 * @return
	 */
	public double pixelsToDegrees(double pixels) {
		updateRenderProperties();
		return renderProperties.pixelsToDegrees(pixels);
	}
	
	/**
	 * @param pos
	 * @return
	 */
	public Point2D geoPositionToWorldPixel(Point2D pos) {
		updateRenderProperties();
		return renderProperties.geoPositionToWorldPixel(pos);
	}
	
	/**
	 * @param pos
	 * @return
	 */
	public Point2D geoPositionToWorldPixel(GeoPosition pos) {
		updateRenderProperties();
		return renderProperties.geoPositionToWorldPixel(pos);		
	}
	
	/**
	 * @param worldPixel
	 * @return
	 */
	public Point2D worldPixelToViewportPixel(Point2D worldPixel) {
		updateRenderProperties();
		return renderProperties.worldPixelToViewportPixel(worldPixel);
	}
	
	/**
	 * @param viewportPixel
	 * @return
	 */
	public Point2D viewportPixelToWorldPixel(Point2D viewportPixel) {
		updateRenderProperties();
		return renderProperties.viewportPixelToWorldPixel(viewportPixel);
	}
	
	protected void updateRenderProperties() {
		mutableRenderProperties.setViewportBounds(getViewportBounds());
		mutableRenderProperties.setZoom(getZoom());
	}
	
	@Override
	public boolean isMapEventListenerPresent(MapEventListener listener) {
		synchronized(mapEventListeners) {
			return mapEventListeners.contains(listener);
		}
	}

	@Override
	public void addMapEventListener(MapEventListener listener) {
		synchronized(mapEventListeners) {
			mapEventListeners.add(listener);
		}
	}

	@Override
	public void removeMapEventListener(MapEventListener listener) {		
		synchronized(mapEventListeners) {
			mapEventListeners.remove(listener);
		}
	}
	
	protected void fireMapEvent(IMapObject_Phase2 mapObject) {		
		synchronized(mapEventListeners) {
			if(!mapEventListeners.isEmpty()) {
				MapEvent mapEvent = new MapEvent(this, mapObject);
				for(MapEventListener listener : mapEventListeners) {
					listener.mapObjectClicked(mapEvent);
				}
			}
		}
	}

	/** Extends the default PanMouseInputListener in JXMapViewer to properly handle custom cursors, the display
	 * of information balloons when an object is clicked, and editing objects. */
	protected class IcarusMouseInputListener extends PanMouseInputListener {
		
		/** Whether panning is currently enabled */
		private boolean panningEnabled = true;
		
		public boolean isPanningEnabled() {
			return panningEnabled;
		}

		public void setPanningEnabled(boolean panningEnabled) {
			if(this.panningEnabled != panningEnabled) {
				if(this.panningEnabled) {
					//Restore the cursor to the default cursor
					restoreCursor();
				}				
				this.panningEnabled = panningEnabled;
			}
			this.panningEnabled = panningEnabled;
		}
		
		public void mouseClicked(MouseEvent evt) {
			//Determine if an annotable map object was clicked and show its tool tip information balloon
			if(toolTipsEnabled) {			
				Point2D mousePoint = viewportPixelToWorldPixel(evt.getPoint());
				InformationBalloon<IMapObject_Phase2> currentToolTipBalloon = 
						toolTipManager.getFirstToolTipAtLocation(mousePoint, false);
				if(currentToolTipBalloon != null) {				
					//The mouse was clicked on a tool tip information balloon, close the balloon if the close button was clicked
					if(currentToolTipBalloon.closeButtonContainsPixelLocation(mousePoint.getX(), mousePoint.getY())) {
						toolTipManager.removeToolTip(currentToolTipBalloon.getParent());
						redraw();
					}
				} else {
					//Determine if an object was clicked and show its tool tip information balloon
					if(!layerManager.isLayersEmpty()) {					
						IAnnotatableMapObject mouseOverObject = null;
						for(ILayer_Phase2<? extends IMapObject_Phase2, MapPanel_Phase2> layer : layerManager) {
							//System.out.println("Layer: " + layer.getId());
							if(layer.isVisible()) {
								for(IMapObject_Phase2 mapObject : layer.getMapObjects()) {
									if(mapObject instanceof IAnnotatableMapObject &&
											mapObject.containsPixelLocation(mousePoint)) {
										mouseOverObject = (IAnnotatableMapObject)mapObject;									
										break;
									}
								}
							}
						}					
						if(mouseOverObject != null) {
							if(toolTipManager.isToolTipPresent(mouseOverObject)) {
							//if(currentToolTipBalloon != null && currentToolTipBalloon.getParent() == mouseOverObject) {
								//The mouse was pressed again on an object with a tool tip, so remove the tool tip information balloon
								//from that object								
								toolTipManager.removeToolTip(mouseOverObject);
							} else {
								//The mouse was pressed on a new object, so add a tool tip to it
								toolTipManager.addToolTip(mouseOverObject);								
								//Fire a map event
								fireMapEvent(mouseOverObject);
								//DEBUG CODE
								//currentToolTipBalloon.setOrientation(AnnotationOrientation.South);
								//currentToolTipBalloon.setShowCloseButton(true);
								//END DEBUG CODE
							}
							redraw();
						} else {
							//The mouse was clicked on the map, close all tool tips
							toolTipManager.removeAllToolTips();
							redraw();
						}						
					}				
				}
			}
			
			//Call super mouse pressed handler to handle panning			
			//if(panningEnabled) {
			super.mouseClicked(evt);
			//}
		}
		
		public void mousePressed(MouseEvent evt) {			
			if(panningEnabled) {
				super.mousePressed(evt);
			}
		}
		
		public void mouseDragged(MouseEvent evt) {
			if(panningEnabled) {
				super.mouseDragged(evt);
			}
		}

		public void mouseReleased(MouseEvent evt) {
			if(panningEnabled) {
				super.mouseReleased(evt);
			}
		}
		
		@SuppressWarnings("unchecked")
		public void mouseMoved(MouseEvent evt) {
			//Change cursor to a hand if over an annotatable mouse object or the close button in a tool tip information balloon
			Point2D mousePoint = viewportPixelToWorldPixel(evt.getPoint());
			if(toolTipManager.getFirstToolTipAtLocation(mousePoint, true) != null) {			
				if(!editorArmed) {
					setCursor(CursorType.HAND, false);
				}
			} else if(!layerManager.isLayersEmpty()) {
				//Iterate over the layers backwards to find the first object the mouse is over				
				Iterator<ILayer_Phase2<? extends IMapObject_Phase2, MapPanel_Phase2>> iter = layerManager.descendingIterator();
				IMapObject_Phase2 mouseOverObject = null;
				//System.out.println("Iterating Backwards");
				while(iter.hasNext()) {					
					ILayer_Phase2<? extends IMapObject_Phase2, ? extends MapPanel_Phase2> currLayer = iter.next();
					//System.out.println("Layer: " + currLayer.getId());
					if(currLayer.isSelectable() && currLayer.isVisible()) {
						mouseOverObject = currLayer.getFirstObjectAtLocation(mousePoint); 
						if(mouseOverObject != null) {
							if(highlightedObject == null || mouseOverObject != highlightedObject) {
								if(highlightedObject != null) {
									//Set the previous mouse object to not have the mouse over it
									highlightedObject.setMouseOverState(false, evt);
								}
								highlightedObject = mouseOverObject;
								highlightedObject.setMouseOverState(true, evt);

								if(currLayer instanceof IEditableLayer<?, ?>) {
									//Edit the map object if it's editable
									IEditableLayer<IMapObject_Phase2, MapPanel_Phase2> editableLayer = 
											(IEditableLayer<IMapObject_Phase2, MapPanel_Phase2>)currLayer;									
									if(editableLayer.isEditable() && highlightedObject.isEditable()) {
										if(editor != null) {
											editor.doneEditingMapObject();
										}
										editor = editableLayer.editObject(highlightedObject);
										editor.getMouseListener().mouseMoved(evt);
									}
								}

								if(highlightedObject.isSelectable() && !editorArmed) {
									//Show hand cursor
									//TODO: Should only show hand if object has a tool tip								
									setCursor(CursorType.HAND, false);
									panningEnabled = false;
								}							
							}
							break;			
						}
					}
				}
				if(mouseOverObject == null) {
					if(editor != null) {
						//Stop editing the current map object since the mouse is no longer over it
						editor.doneEditingMapObject();
						editorArmed = false;
						editor = null;
					}
					if(highlightedObject != null) {
						//Set the mouse over state to false for the previously highlighted map object
						highlightedObject.setMouseOverState(false, evt);
						highlightedObject = null;
					}
					panningEnabled = true;
					restoreCursor();
				}				
			}
			
			//Call super mouse moved handler to handle panning
			if(panningEnabled) {
				super.mouseMoved(evt);
			}
		}		

		public void mouseEntered(MouseEvent evt) {
			if(panningEnabled) {
				super.mouseEntered(evt);
			}
		}

		public void mouseExited(MouseEvent evt) {
			if(panningEnabled) {
				super.mouseExited(evt);
			}
		}
	}
	
	/** Extends PanKeyListener in JXMapViewer to also zoom using the "+" and "-" keys. */
	protected class PanAndZoomKeyListener extends PanKeyListener {
		@Override
		public void keyPressed(KeyEvent e) {
			if(isZoomEnabled()) {
				switch(e.getKeyChar()) {
				case '+': 
					//Zoom in
					//System.out.println("zooming in to: " + (getZoom() - 1));
					setZoom(getZoom() - 1);
					break;
				case '-':
					//Zoom out
					//System.out.println("zooming out to: " + (getZoom() + 1));
					setZoom(getZoom() + 1);
					break;
				default: 
					//Call the super keyPressed method to handle panning
					super.keyPressed(e);
				}
			} else {
				//Call the super keyPressed method to handle panning
				super.keyPressed(e);
			}
		}
	}
}