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

import java.awt.Component;

import javax.swing.ProgressMonitor;

/**
 * A progress monitor implementation that uses a Swing ProgressMonitor.
 * 
 * @author CBONACETO
 *
 */
public class SwingProgressMonitor extends ProgressMonitor implements IProgressMonitor {
	
	/**
	 * Constructor.
	 * 
	 * @param parentComponent
	 * @param message
	 * @param note
	 * @param min
	 * @param max
	 */
	public SwingProgressMonitor(Component parentComponent, Object message, String note, int min, int max) {	
		super(parentComponent, message, note, min, max);		
	}	
}