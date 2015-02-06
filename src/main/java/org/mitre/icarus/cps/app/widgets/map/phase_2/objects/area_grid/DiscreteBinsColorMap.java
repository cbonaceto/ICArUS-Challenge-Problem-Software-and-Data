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
package org.mitre.icarus.cps.app.widgets.map.phase_2.objects.area_grid;

import java.util.Collections;
import java.util.List;

/**
 * 
 * @author CBONACETO
 *
 */
public class DiscreteBinsColorMap implements IAreaGridColorMap<Double> {
	
	/** Defines the bin limits */
	protected List<Double> bins;
	
	/** Defines the bin colors */
	protected List<ColorRGBA> binColors;
	
	public DiscreteBinsColorMap(List<Double> bins, List<ColorRGBA> binColors) {
		this.bins = bins;
		this.binColors = binColors;
	}

	@Override
	public int getColorForValue(Double value) {
		//TODO: Finish this
		if(value == null || value.isNaN() || value.isInfinite()) {
			//if value is null, NaN or infinite
			return ColorRGBA.getRGBA(0, 0, 0, 0);
		}
		
		Collections.binarySearch(bins, value);		
		return 0;
	}
}
