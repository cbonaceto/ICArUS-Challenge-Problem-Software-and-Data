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
package org.mitre.icarus.cps.app.widgets.map.phase_05;

import java.awt.Graphics2D;
import java.util.Collection;
//import java.util.EnumMap;
//import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;



/**
 * The feature vector contains all elements in the scene, loaded from a feature vector and an object
 * palette definition.
 * 
 * @author CBONACETO
 *
 */
public class FeatureVector extends Feature {
	
	/** The layers (mapped by layer Id).  Layers are ordered by their layer ID, and they
	 * are rendered in this order */
	private SortedMap<Integer, Layer> layers = new TreeMap<Integer, Layer>();
	
	/** The sector layer (if any) */
	private SectorLayer sectorLayer;
	
	/** The name of the object palette used to populate this feature vector */
	private final String objectPalette;
	
	/**
	 * Construct a feature vector with the given object palette name.
	 * 
	 * @param objectPalette
	 */
	public FeatureVector(String objectPalette) {
		setName("Scene");
		this.objectPalette = objectPalette;
	}	

	@Override
	public void draw(Graphics2D g, RenderData r) {}

	@Override
	public Collection<? extends Layer> getChildren() {
		return layers.values();
	}
	
	/**
	 * Add a layer to the feature vector.
	 * 
	 * @param layer - the layer to add
	 */
	public void addLayer(Layer layer) {
		layers.put(layer.getLayerId(), layer);
		if(layer instanceof SectorLayer) {
			this.sectorLayer = (SectorLayer)layer;
		}
	}
	
	/**
	 * Add a collection of layers to the feature vector.
	 * 
	 * @param layers - the layers to add
	 */
	public void addLayers(Collection<? extends Layer> layers) {
		if(layers != null) {
			for(Layer layer : layers) {
				addLayer(layer);
			}
		}
	}
	
	/**
	 * Remove a layer from the feature vector.
	 * 
	 * @param layer - the layer to remove
	 */
	public void removeLayer(Layer layer) {
		layers.remove(layer.getLayerId());
	}
	
	/**
	 * Remove a layer from the feature vector with the given layer ID.
	 * 
	 * @param layerId
	 */
	public void removeLayer(Integer layerId) {
		layers.remove(layerId);
	}
	
	/**
	 * Return whether the feature vector contains the layer with the given layer ID.
	 * 
	 * @param layerId
	 * @return
	 */
	public boolean containsLayer(Integer layerId) {
		return layers.containsKey(layerId);
	}
	
	/**
	 * Get a layer in the feature vector with the given layer ID. 
	 * 
	 * @param layerId
	 * @return
	 */
	public Layer getLayer(Integer layerId) {
		return layers.get(layerId);
	}
	
	/**
	 * Get all layers in the feature vector.
	 * 
	 * @return
	 */
	public Collection<Layer> getLayers() {
		return layers.values();
	}

	/**
	 * Get the sector layer.
	 * 
	 * @return
	 */
	public SectorLayer getSectorLayer() {
		return sectorLayer;
	}

	/**
	 * Get the name of the object palette.
	 * 
	 * @return
	 */
	public String getObjectPalette() {
		return objectPalette;
	}
}