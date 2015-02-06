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
package org.mitre.icarus.cps.app.widgets.probability_entry;

/**
 * @author Eric Kappotis
 *
 */
public class Setting {
	
	private Double value = 0.0;
	
	private Integer displaySetting = 0;
	
	private Integer maxValue;
	
	private Integer minValue;
	
	private boolean locked;
	
	private boolean current;	
	
	public Setting() {}
	
	public Setting(int minValue, int maxValue, int currentSetting) {
		this.minValue = minValue;
		this.maxValue = maxValue;
		setCurrentSetting(currentSetting);
	}
	
	/**
	 * Copy constructor. 
	 * 
	 * @param copy
	 */
	public Setting(Setting copy) {
		value =copy.value;		
		displaySetting = copy.displaySetting;		
		maxValue = copy.maxValue;		
		minValue = copy.minValue;		
		locked = copy.locked;
		current = copy.current;
	}
	
	/**
	 * Sets both the actual value (double) and the display value (integer).
	 * 
	 * @param value
	 */
	public void setCurrentSetting(Integer value) {		
		this.displaySetting = value;
		this.value = new Double(value != null ? value : 0);
	}
	
	/**
	 * @return the value
	 */
	public Double getValue() {
		return value;
	}
	
	/**
	 * @param value the value to set
	 */
	public void setValue(Double value) {
		this.value = value;		
	}
	
	public void setValue(Integer value) {
		this.value = new Double(value);		
	}
	
	/**
	 * @return the intValue
	 */
	public Integer getIntValue() {
		return (int)Math.round(value);
	}
	
	/**
	 * @param intValue the intValue to set
	 */
	public void setIntValue(Integer intValue) {
		this.value = new Double(intValue != null ? intValue : 0);
	}
	
	/**
	 * @return the displaySetting
	 */
	public Integer getDisplaySetting() {
		return displaySetting;
	}
	/**
	 * @param displaySetting the displaySetting to set
	 */
	public void setDisplaySetting(Integer displaySetting) {
		this.displaySetting = displaySetting;
	}
	
	/**
	 * @return the locked
	 */
	public boolean isLocked() {
		return locked;
	}
	/**
	 * @param locked the locked to set
	 */
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	/**
	 * @return the current
	 */
	public boolean isCurrent() {
		return current;
	}
	/**
	 * @param current the current to set
	 */
	public void setCurrent(boolean current) {
		this.current = current;
	}
	/**
	 * @return the maxValue
	 */
	public Integer getMaxValue() {
		return maxValue;
	}
	/**
	 * @param maxValue the maxValue to set
	 */
	public void setMaxValue(Integer maxValue) {
		this.maxValue = maxValue;
	}
	/**
	 * @return the minValue
	 */
	public Integer getMinValue() {
		return minValue;
	}
	/**
	 * @param minValue the minValue to set
	 */
	public void setMinValue(Integer minValue) {
		this.minValue = minValue;
	}	
}