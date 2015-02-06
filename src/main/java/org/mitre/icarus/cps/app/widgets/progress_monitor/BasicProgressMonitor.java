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
 * A basic progress monitor implementation that implements getters and setters for the note,
 * maximium value, minimum value, and current value.
 * 
 * @author CBONACETO
 *
 */
public class BasicProgressMonitor implements IProgressMonitor {
	
	/** The note */
	protected String note;
	
	/** The minimum progress value */
	protected int minimum;
	
	/** The maximum progress value order */
	protected int maximum;
	
	/** The current progress value */
	protected int progress;

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.base.loader.progress_monitor.ILoadProgressMonitor#getNote()
	 */
	@Override
	public String getNote() {
		return note;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.base.loader.progress_monitor.ILoadProgressMonitor#setNote(java.lang.String)
	 */
	@Override
	public void setNote(String note) {
		this.note = note;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.base.loader.progress_monitor.ILoadProgressMonitor#getMinimum()
	 */
	@Override
	public int getMinimum() {
		return minimum;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.base.loader.progress_monitor.ILoadProgressMonitor#setMinimum(int)
	 */
	@Override
	public void setMinimum(int minimum) {
		this.minimum = minimum;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.base.loader.progress_monitor.ILoadProgressMonitor#getMaximum()
	 */
	@Override
	public int getMaximum() {
		return maximum;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.base.loader.progress_monitor.ILoadProgressMonitor#setMaximum(int)
	 */
	@Override
	public void setMaximum(int maximum) {
		this.maximum = maximum;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.base.loader.progress_monitor.ILoadProgressMonitor#setProgress(int)
	 */
	@Override
	public void setProgress(int progress) {
		this.progress = progress;
	}
}