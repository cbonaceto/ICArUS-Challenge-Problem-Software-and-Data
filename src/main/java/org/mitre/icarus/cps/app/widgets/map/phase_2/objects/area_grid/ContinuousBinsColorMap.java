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

/**
 * 
 * @author CBONACETO
 *
 */
public class ContinuousBinsColorMap implements IAreaGridColorMap<Double> {
	
	/** Default min & max colors */
	protected static final ColorRGBA defaultMinColor = new ColorRGBA(0, 0, 0);
	protected static final ColorRGBA defaultMaxColor = new ColorRGBA(255, 255, 255);

	/** Default min & max values */
	protected static final Double defaultMinValue = 0.0D;
	protected static final Double defaultMaxValue = 1.0D;

	/** The color for the min value */
	protected ColorRGBA minColor; 
	
	/** The color for the max value */
	protected ColorRGBA maxColor; 

	/** The min value */
	protected Double minValue;
	
	/** The max value */
	protected Double maxValue;
	
	public ContinuousBinsColorMap(Double minValue, Double maxValue) {
		this(minValue, maxValue, defaultMinColor, defaultMaxColor);
	}
	
	public ContinuousBinsColorMap(ColorRGBA minColor, ColorRGBA maxColor) {
		this(defaultMinValue, defaultMaxValue, minColor, maxColor);
	}
	
	public ContinuousBinsColorMap(Double minValue, Double maxValue, ColorRGBA minColor, ColorRGBA maxColor) {
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.minColor = minColor;
		this.maxColor = maxColor;
	}

	public ColorRGBA getMinColor() {
		return minColor;
	}

	public void setMinColor(ColorRGBA minColor) {
		this.minColor = minColor;
	}

	public ColorRGBA getMaxColor() {
		return maxColor;
	}

	public void setMaxColor(ColorRGBA maxColor) {
		this.maxColor = maxColor;
	}

	public Double getMinValue() {
		return minValue;
	}

	public void setMinValue(Double minValue) {
		this.minValue = minValue;
	}

	public Double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(Double maxValue) {
		this.maxValue = maxValue;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.gui.map.phase_2.objects.area_grid.IAreaGridColorMap#getColorForValue(java.lang.Object)
	 */
	@Override
	public int getColorForValue(Double value) {		
		if(value == null || value.isNaN() || value.isInfinite()) {
			//if value is null, NaN or infinite
			return ColorRGBA.getRGBA(0, 0, 0, 0);
		}

		Double percentShaded = value / maxValue;
		if(percentShaded < 0) {
			percentShaded = 0.D;
		} else if(percentShaded > 1) {
			percentShaded = 1.D;
		}
		int r = minColor.red - 
				((int)((minColor.red - maxColor.red) * percentShaded));
		int g = minColor.green - 
				((int)((minColor.green - maxColor.green) * percentShaded)); 		
		int b = minColor.blue - 
				((int)((minColor.blue - maxColor.blue) * percentShaded));

		return ColorRGBA.getRGBA(r, g, b, 255);
	}
}