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

import javax.swing.JComponent;

/**
 * @author Eric Kappotis
 *
 */
public abstract class AbstractProbabilityEntryComponent implements IProbabilityEntryComponent {	

	protected JComponent component;
	
	private final Setting setting;
	
	protected boolean enableLocking;	
	
	public AbstractProbabilityEntryComponent(JComponent component) {
		this.component = component;
		setting = new Setting();		
	}
	
	/**
	 * @return the component
	 */
	public JComponent getComponent() {
		return component;
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.gui.probability_entry.IProbabilityEntryContainer#getSetting()
	 */
	/*@Override
	public Setting getSetting() {
		return this.setting;
	}*/
	
	@Override
	public Integer getMaxValue() {
		return setting.getMaxValue();
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.gui.probability_entry.IProbabilityEntryContainer#setMaxValue(java.lang.Integer)
	 */
	@Override
	public void setMaxValue(Integer value) {
		this.setting.setMaxValue(value);
	}	

	@Override
	public void setEnableLocking(boolean enableLocking) {
		this.enableLocking = enableLocking;	
	}

	@Override
	public boolean isEnableLocking() {
		return enableLocking;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.gui.probability_entry.IProbabilityEntryContainer#isLocked()
	 */
	@Override
	public boolean isLocked() {
		return this.setting.isLocked();
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.gui.probability_entry.IProbabilityEntryContainer#setLocked(boolean)
	 */
	@Override
	public void setLocked(boolean locked) {
		this.setting.setLocked(locked);
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.gui.probability_entry.IProbabilityEntryContainer#setFocused(boolean)
	 */
	@Override
	public void setFocused(boolean focused) {
		this.setting.setCurrent(focused);
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.gui.probability_entry.IProbabilityEntryContainer#isFocused()
	 */
	@Override
	public boolean isFocused() {
		return this.setting.isCurrent();
	}	

	@Override
	public Integer getIntValue() {
		return setting.getIntValue();
	}

	@Override
	public Double getDoubleValue() {
		return setting.getValue();
	}

	@Override
	public void setDoubleValue(Double value) {
		setting.setValue(value);
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.gui.probability_entry.IProbabilityEntryContainer#setCurrentSetting(int)
	 */
	@Override
	public void setCurrentSetting(Integer value) {
		setting.setCurrentSetting(value);
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.gui.probability_entry.IProbabilityEntryContainer#getDisplayValue()
	 */
	@Override
	public Integer getDisplaySetting() {
		return setting.getDisplaySetting();
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.gui.probability_entry.IProbabilityEntryContainer#setDisplayValue(int)
	 */
	@Override
	public void setDisplaySetting(Integer value) {
		setting.setDisplaySetting(value);
	}	
}