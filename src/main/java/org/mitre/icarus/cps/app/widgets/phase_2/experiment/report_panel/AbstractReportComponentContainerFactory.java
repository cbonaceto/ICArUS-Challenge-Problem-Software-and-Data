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
package org.mitre.icarus.cps.app.widgets.phase_2.experiment.report_panel;

import org.mitre.icarus.cps.app.widgets.phase_2.experiment.report_panel.ReportComponentContainer.Alignment;
import org.mitre.icarus.cps.experiment_core.gui.IConditionComponent;

/**
 * @author CBONACETO
 *
 */
public abstract class AbstractReportComponentContainerFactory {
	
	/**
	 * @param component
	 * @param horizontalAlignment
	 * @param title
	 * @param titleVisible
	 * @return
	 */
	public abstract ReportComponentContainer createReportComponentContainer(IConditionComponent component, 
			Alignment horizontalAlignment, String title, boolean titleVisible);
}