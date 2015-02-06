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
package org.mitre.icarus.cps.app.widgets.map.phase_1.objects;

import org.mitre.icarus.cps.app.widgets.map.objects.AbstractMapObject;
import org.mitre.icarus.cps.app.widgets.map.phase_1.layers.ILayer_Phase1;

/**
 * Abstract implementation of the IMapObject_Phase1 interface that implements some of the interface
 * methods. Map object classes may extend this class for convenience.
 * 
 * @author CBONACETO
 *
 */
public abstract class AbstractMapObject_Phase1 extends AbstractMapObject implements IMapObject_Phase1 {

	/** The layer this map object is contained in */
	protected ILayer_Phase1<? extends IMapObject_Phase1> layer;	

	@Override
	public ILayer_Phase1<? extends IMapObject_Phase1> getLayer() {
		return layer;
	}
	
	@Override
	public void setLayer(ILayer_Phase1<? extends IMapObject_Phase1> layer) {		
		this.layer = layer;
	}	
}