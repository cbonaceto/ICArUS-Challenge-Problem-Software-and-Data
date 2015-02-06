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

import java.awt.Color;
import java.awt.Font;

import javax.swing.JComponent;

import org.mitre.icarus.cps.app.widgets.probability_entry.controllers.IProbabilityController;

/**
 * Interface for probability entry components (e.g., stacked boxes, default boxes, text entry, etc.).
 * 
 * @author Eric Kappotis
 *
 */
public interface IProbabilityEntryComponent {
	
	public void setProbabilityController(IProbabilityController controller);	
	
	public int getId();
	public void setId(int id);	
	
	public boolean isEditable();
	public void setEditable(boolean editable);
	
	public void setEnableLocking(boolean enableLocking);	
	public boolean isEnableLocking();
	
	public boolean isLocked();
	public void setLocked(boolean locked);
	
	public boolean isFocused();
	public void setFocused(boolean focused);
	
	
	/**
	 * Get whether the value being displayed is a valid setting.
	 * 
	 * @return 
	 */
	public boolean isDisplayedValueValid();
	
	public Integer getMaxValue();
	public void setMaxValue(Integer value);	
	
	public Double getDoubleValue();
	public void setDoubleValue(Double value);	
	
	public Integer getIntValue();
	
	/**
	 * Sets both the actual value (double) and the display value (integer).
	 * 
	 * @param setting
	 */
	public void setCurrentSetting(Integer setting);
	
	public Integer getPreviousSetting();
	public void setPreviousSetting(Integer setting);
	
	public Integer getDisplaySetting();
	public void setDisplaySetting(Integer setting);
	
	/** Get/set the font for the display of the probability value */
	public Font getFont();
	public void setFont(Font font);
	
	/** Get/set the color for the display of the probability value */
	public Color getProbabilityEntryColor();
	public void setProbabilityEntryColor(Color color);
	
	public long getInteractionTime();	
	public void resetInteractionTime();
	
	public JComponent getComponent();	
}