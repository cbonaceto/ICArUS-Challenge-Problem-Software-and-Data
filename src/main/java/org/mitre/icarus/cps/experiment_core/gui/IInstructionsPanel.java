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

import java.awt.Image;
import java.net.URL;

import javax.swing.JComponent;
import javax.swing.event.HyperlinkListener;

import org.mitre.icarus.cps.experiment_core.condition.InstructionsPage;

/**
 * @author CBONACETO
 *
 */
public interface IInstructionsPanel extends IConditionComponent {
	
	/** Add a hyperlink listener */
	public void addHyperlinkListener(HyperlinkListener listener);
	
	/** Remove a hyperlink listener */
	public void removeHyperlinkListener(HyperlinkListener listener);
	
	public void setInstructionsPage(InstructionsPage page);

	public void setInstructionText(String instructions);
	
	public void setInstructionsImage(Image instructionsImage);
	
	public void setInstructionsURL(String instructionsUrl);
	
	public void setInstructionsURL(URL instructionsUrl);
	
	public void setInstructionsWidget(JComponent widget);
}