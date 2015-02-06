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
package org.mitre.icarus.cps.app.widgets.map.phase_1;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.mitre.icarus.cps.app.widgets.map.phase_1.layers.ILayer_Phase1;
import org.mitre.icarus.cps.app.widgets.map.phase_1.objects.AbstractMapObject_Phase1;

/**
 * Contains the map layers and provides methods for managing the layers.
 * 
 * @author CBONACETO
 *
 */
public class MapModel {
	
	/** The layers (mapped by layer Id).  Layers are rendered in the order they appear in the list. */
	protected List<ILayer_Phase1<? extends AbstractMapObject_Phase1>> layers =
			new LinkedList<ILayer_Phase1<? extends AbstractMapObject_Phase1>>();
	
	/** Maps layers by their layer ID */
	protected Map<String, ILayer_Phase1<? extends AbstractMapObject_Phase1>> layersMap =
			new HashMap<String, ILayer_Phase1<? extends AbstractMapObject_Phase1>>();		
	
	public MapModel() {
	}	

	/**
	 * Add a layer to the map.  The layer will be added as the last layer in the Z order (rendered last).
	 * 
	 * @param layer - the layer to add
	 */
	public void addLayer(ILayer_Phase1<? extends AbstractMapObject_Phase1> layer) {	
		layer.setzOrderPreference(layers.size());
		layers.add(layer);
		layersMap.put(layer.getId(), layer);
	}
	
	/**
	 * Add a collection of layers to the map.
	 * 
	 * @param layers - the layers to add
	 */
	public void addLayers(Collection<? extends ILayer_Phase1<? extends AbstractMapObject_Phase1>> layers) {
		if(layers != null) {
			for(ILayer_Phase1<? extends AbstractMapObject_Phase1> layer : layers) {
				addLayer(layer);
			}
		}
	}
	
	/**
	 * Remove a layer from the map.
	 * 
	 * @param layer - the layer to remove
	 */
	public void removeLayer(ILayer_Phase1<? extends AbstractMapObject_Phase1> layer) {		
		layers.remove(layer);
		layersMap.remove(layer.getId());		
	}
	
	/**
	 * Remove a layer from the map with the given layer ID.
	 * 
	 * @param layerId
	 */
	public void removeLayer(String layerId) {
		ILayer_Phase1<? extends AbstractMapObject_Phase1> layer = layersMap.get(layerId);
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
	public ILayer_Phase1<? extends AbstractMapObject_Phase1> getLayer(String layerId) {
		return layersMap.get(layerId);
	}
	
	/**
	 * Get all layers in the feature vector.
	 * 
	 * @return
	 */
	public Collection<ILayer_Phase1<? extends AbstractMapObject_Phase1>> getLayers() {
		return layers;
	}	
	
	public void moveLayerToBottomOfZOrder(ILayer_Phase1<? extends AbstractMapObject_Phase1> layer) {		
		int layerIndex = layers.indexOf(layer);
		if(layerIndex >= 0 && layerIndex != layers.size()-1) {
			layers.remove(layerIndex);
			layers.add(layer);
		}		
	}
	
	public void restoreLayerZOrder() {
		Collections.sort(layers, new LayerComparator());
	}
	
	protected static class LayerComparator implements Comparator<ILayer_Phase1<? extends AbstractMapObject_Phase1>> {
		@Override
		public int compare(ILayer_Phase1<? extends AbstractMapObject_Phase1> layer1, ILayer_Phase1<? extends AbstractMapObject_Phase1> layer2) {			
			if(layer1.getzOrderPreference() < layer2.getzOrderPreference()) {
				return -1;
			}
			if(layer1.getzOrderPreference() > layer2.getzOrderPreference()) {
				return 1;
			}
			return 0;
		}		
	}
}