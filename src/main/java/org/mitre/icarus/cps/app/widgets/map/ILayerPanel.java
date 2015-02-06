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
package org.mitre.icarus.cps.app.widgets.map;

import java.util.Collection;

import javax.swing.JComponent;

import org.mitre.icarus.cps.app.widgets.map.layers.ILayer;
import org.mitre.icarus.cps.app.widgets.map.objects.IMapObject;

/**
 * Interface for layer panel implementations.
 * 
 * @author CBONACETO
 *
 */
public interface ILayerPanel {	

	/**
	 * Set the map the layers are contained in.
	 * 
	 * @param map
	 */
	public void setMap(IMapPanel map);
	
	/**
	 * Set the layers in the layer panel.
	 * 
	 * @param layers the layers
	 */
	public void setLayers(Collection<ILayer<? extends IMapObject>> layers);
	
	/**
	 * Add a layer to the layer panel
	 */
	public void addLayer(ILayer<? extends IMapObject> layer);
	
	/**
	 * Remove a layer from the layer panel
	 */
	public void removeLayer(ILayer<? extends IMapObject> layer);
	
	/**
	 * Remove all layers from the layer panel
	 */
	public void removeAllLayers();
	
	/**
	 * Get the layer panel component.
	 * 
	 * @return the layer panel component
	 */
	public JComponent getLayerPanelComponent();
}