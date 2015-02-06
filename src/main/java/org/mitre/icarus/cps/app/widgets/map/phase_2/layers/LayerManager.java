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
package org.mitre.icarus.cps.app.widgets.map.phase_2.layers;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.painter.AbstractPainter;
import org.mitre.icarus.cps.app.widgets.map.phase_2.MapPanel_Phase2;
import org.mitre.icarus.cps.app.widgets.map.phase_2.RenderProperties;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.IMapObject_Phase2;

/**
 * Aggregates one or more layers and draws them in order as the overlay painter for an IcarusMapViewer.
 * 
 * @author CBONACETO
 *
 */
public class LayerManager<M extends MapPanel_Phase2> extends AbstractPainter<M> 
	implements Iterable<ILayer_Phase2<? extends IMapObject_Phase2, M>> {
	
	/** The layers list.  Layers are rendered in the order they appear in the list. */
	protected LinkedList<ILayer_Phase2<? extends IMapObject_Phase2, M>> layers = 
			new LinkedList<ILayer_Phase2<? extends IMapObject_Phase2, M>>();
	
	/** Maps layers by their layer ID */
	protected Map<String, ILayer_Phase2<? extends IMapObject_Phase2, M>> layersMap = 
			new HashMap<String, ILayer_Phase2<? extends IMapObject_Phase2, M>>();
	
	/** Used to determine whether the render properties have changed since the layers were last rendered */
	protected RenderPropertiesChangeDetector renderPropertiesChangeDetector = new RenderPropertiesChangeDetector();
	
	public LayerManager() {}
	
	/**
	 * Add a layer to the map.  The layer will be added as the last layer in the Z order (rendered last).
	 * 
	 * @param layer - the layer to add
	 */
	public void addLayer(ILayer_Phase2<? extends IMapObject_Phase2, M> layer) {	
		layer.setzOrderPreference(layers.size());
		layers.add(layer);
		layersMap.put(layer.getId(), layer);		
	}
	
	/**
	 * Add a collection of layers to the map.
	 * 
	 * @param layers - the layers to add
	 */
	public void addLayers(Collection<? extends ILayer_Phase2<? extends IMapObject_Phase2, M>> layers) {
		if(layers != null) {
			for(ILayer_Phase2<? extends IMapObject_Phase2, M> layer : layers) {
				addLayer(layer);
			}
		}
	}
	
	/**
	 * Remove a layer from the map.
	 * 
	 * @param layer - the layer to remove
	 */
	public void removeLayer(ILayer_Phase2<? extends IMapObject_Phase2, M> layer) {		
		layers.remove(layer);
		layersMap.remove(layer.getId());		
	}
	
	/**
	 * Remove a layer from the map with the given layer ID.
	 * 
	 * @param layerId
	 */
	public void removeLayer(String layerId) {
		ILayer_Phase2<? extends IMapObject_Phase2, M> layer = layersMap.get(layerId);
		if(layer != null) {
			layers.remove(layer);
			layersMap.remove(layerId);
		}
	}
	
	/**
	 * Return whether the map contains the layer with the given layer ID.
	 * 
	 * @param layerId
	 * @return
	 */
	public boolean containsLayer(String layerId) {
		return layersMap.containsKey(layerId);
	}
	
	/**
	 * Get a layer in the feature vector with the given layer ID. 
	 * 
	 * @param layerId
	 * @return
	 */
	public ILayer_Phase2<? extends IMapObject_Phase2, M> getLayer(String layerId) {
		return layersMap.get(layerId);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<ILayer_Phase2<? extends IMapObject_Phase2, M>> iterator() {
		return layers.iterator();
	}
	
	/**
	 * @return
	 */
	public Iterator<ILayer_Phase2<? extends IMapObject_Phase2, M>> descendingIterator() {
		return layers.descendingIterator();
	}
	
	/**
	 * @return
	 */
	public boolean isLayersEmpty() {
		return (layers == null || layers.isEmpty());
	}
	
	public void moveLayerToBottomOfZOrder(ILayer_Phase2<? extends IMapObject_Phase2, M> layer) {		
		int layerIndex = layers.indexOf(layer);
		if(layerIndex >= 0 && layerIndex != layers.size()-1) {
			layers.remove(layerIndex);
			layers.add(layer);
		}		
	}
	
	public void restoreLayerZOrder() {
		Collections.sort(layers, new LayerComparator());
	}
	
	protected static class LayerComparator implements Comparator<ILayer_Phase2<? extends IMapObject_Phase2, ? extends MapPanel_Phase2>> {	
		@Override
		public int compare(ILayer_Phase2<? extends IMapObject_Phase2, ? extends MapPanel_Phase2> layer1,
				ILayer_Phase2<? extends IMapObject_Phase2, ? extends MapPanel_Phase2> layer2) {			
			if(layer1.getzOrderPreference() < layer2.getzOrderPreference()) {
				return -1;
			}
			if(layer1.getzOrderPreference() > layer2.getzOrderPreference()) {
				return 1;
			}
			return 0;
		}		
	}

	@Override
	protected void doPaint(Graphics2D g, M map, int width, int height) {
		RenderProperties renderProperties = map.getCurrentRenderProperties();
		boolean renderPropertiesChanged = renderPropertiesChangeDetector.isRenderPropertiesChanged(renderProperties);
		//System.out.println("Render properties changed: " + renderPropertiesChanged);
		
		//Render the layers in order		
		if(layers != null && !layers.isEmpty())	{
			//Translate from world bitmap to viewport coordinate space						
			g.translate(-renderProperties.getViewportBounds().x, -renderProperties.getViewportBounds().y);
			
			for(ILayer_Phase2<? extends IMapObject_Phase2, ? extends MapPanel_Phase2> layer : layers) {
				if(layer.isVisible()) {
					layer.render(g, renderProperties, renderPropertiesChanged);
				}
			}
		}		
	}
	
	/** Provides functionality to check whether the render properties have changed such that points will be translated 
	 * differently to world pixel space since the last time the layers were rendered. */
	public static class RenderPropertiesChangeDetector {
		/** Previous zoom */		
		private Integer zoom;		
		
		/** Test position */
		private GeoPosition testPosition = new GeoPosition(45, 45);
		
		/** Test position translated to world pixel space */
		private Point2D testPosition_pixels;
		
		/**
		 * @param longitude
		 * @param latitude
		 */
		public void setTestPosition(double longitude, double latitude) {
			testPosition = new GeoPosition(latitude, longitude);
		}
		
		/**
		 * @param currentProperties
		 * @return
		 */
		public boolean isRenderPropertiesChanged(RenderProperties currentProperties) {
			if(zoom == null || testPosition_pixels == null) {
				zoom = currentProperties.getZoom();
				testPosition_pixels = currentProperties.geoPositionToWorldPixel(testPosition);
				return true;
			} else {
				Point2D test = currentProperties.geoPositionToWorldPixel(testPosition);
				if(zoom != currentProperties.getZoom() || test.getX() != testPosition_pixels.getX() || 
						test.getY() != testPosition_pixels.getY()) {
					zoom = currentProperties.getZoom();
					testPosition_pixels = test;
					return true;
				}
				return false;
			}
		}
	}
}