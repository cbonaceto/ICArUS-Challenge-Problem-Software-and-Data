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

import javax.swing.Icon;


/**
 * Layer base class.
 * 
 * @author CBONACETO
 *
 */
public abstract class Layer extends Feature {
	
	/** Layer types */
	public enum LayerType {
		IMINT,
		SIGINT,
		MASINT,
		Annotations,
		Sectors,
		Gridlines,
		Overlay
	}
	
	/** Whether the layer should be shown in the layer tree */
	private boolean showInLayerTree = true;
	
	/** Whether or not the layer visibility is user controllable */
	private boolean visibilityUserControllable = true;
	
	/** Whether or not the layer should always be enabled (e.g., the SectorLayer
	 * is always enabled) */
	private boolean alwaysEnabled = false;
	
	/** Layer icon (if any) */
	private Icon icon;
	
	public Layer(Integer layerId) {
		super(layerId);
	}
	
	/** Get the layer type */
	public abstract LayerType getLayerType();

	public Integer getLayerId() {
		return layerId;
	}

	public boolean isShowInLayerTree() {
		return showInLayerTree;
	}

	public void setShowInLayerTree(boolean showInLayerTree) {
		this.showInLayerTree = showInLayerTree;
	}

	public boolean isVisibilityUserControllable() {
		return visibilityUserControllable;
	}

	public void setVisibilityUserControllable(boolean visibilityUserControllable) {
		this.visibilityUserControllable = visibilityUserControllable;
	}

	public boolean isAlwaysEnabled() {
		return alwaysEnabled;
	}

	public void setAlwaysEnabled(boolean alwaysEnabled) {
		this.alwaysEnabled = alwaysEnabled;
	}

	public Icon getIcon() {
		return icon;
	}

	public void setIcon(Icon icon) {
		this.icon = icon;
	}
}