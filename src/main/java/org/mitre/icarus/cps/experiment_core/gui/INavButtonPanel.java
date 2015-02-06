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
package org.mitre.icarus.cps.experiment_core.gui;

import javax.swing.Icon;
import javax.swing.JComponent;

import org.mitre.icarus.cps.experiment_core.event.SubjectActionListener;

/**
 * Interface for nav button panel implementations.
 * 
 * Nav button panels contain the navigation (back, next, help) 
 * buttons in an experiment.
 * 
 * @author CBONACETO
 *
 */
public interface INavButtonPanel {
	/** Button type constants */
	public static enum ButtonType {Exit, Back, Next, Help, Custom};
	
	public JComponent getNavButtonPanelComponent();
	
	public void setFocusedButton(ButtonType button);
	
	public void setButtonVisible(ButtonType button, boolean visible); 	
	
	public boolean isButtonEnabled(ButtonType button);
	
	public void setButtonEnabled(ButtonType button, boolean enabled); 
	
	public void setButtonText(ButtonType button, String text);
	
	public void setButtonIcon(ButtonType button, Icon icon);
	
	public boolean isSubjectActionListenerPresent(SubjectActionListener listener);
	
	/** Add a listener to be notified when a button is pressed */
	public void addSubjectActionListener(SubjectActionListener listener);
	
	/** Remove a button press listener */
	public void removeSubjectActionListener(SubjectActionListener listener);
}
