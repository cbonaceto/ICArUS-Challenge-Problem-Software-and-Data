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
package org.mitre.icarus.cps.app.widgets.progress_monitor;

/**
 * Interface for implementations that monitor progress (e.g., the load progress of an exam, exam phase, or feature vector. etc.).
 * 
 * @author CBONACETO
 *
 */
public interface IProgressMonitor {	
	
	/**
	 * Get the current progress note.
	 * 
	 * @return the note
	 */
	public String getNote();
	
	/**
	 * Set the current progress note.
	 * 
	 * @param note the note
	 */
	public void setNote(String note);
	
	/**
	 * Get the minimum progress value.
	 * 
	 * @return the minimum progress value
	 */
	public int getMinimum();
	
	/**
	 * Set the minimum progress value.
	 * 
	 * @param minimum the minimum progress value
	 */
	public void setMinimum(int minimum);
	
	/**
	 * Get the maximum progress value.
	 * 
	 * @return the maximum progress vale
	 */
	public int getMaximum();
	
	/**
	 * Set the maximum progress value.
	 * 
	 * @param maximum the maximum progress vale
	 */
	public void setMaximum(int maximum);
	
	/**
	 * Set the current progress value.
	 * 
	 * @param progress the current progress value
	 */
	public void setProgress(int progress);
}