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

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.SwingConstants;

import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.app.widgets.phase_2.experiment.report_panel.ReportComponentContainer.Alignment;
import org.mitre.icarus.cps.experiment_core.gui.IConditionComponent;

/**
 * @author CBONACETO
 *
 */
public class DefaultReportComponentContainerFactory extends AbstractReportComponentContainerFactory {
	
	public static boolean BORDER_SHOWN = false;
	
	public static int BORDER_THICKNESS = 2;
	
	public static Color BORDER_COLOR = Color.black;
	
	public static boolean TITLE_OPAQUE = false;
	
	public static Color TITLE_BACKGROUND = WidgetConstants.COLOR_COMPONENT_BACKGROUND_GRAY;
	
	public static Color TITLE_FOREGROUND = Color.BLACK;
	
	public static int TITLE_ALIGNMENT = SwingConstants.LEFT; 
	
	public static Font TITLE_FONT = WidgetConstants.FONT_PANEL_TITLE;
	
	public static boolean TITLE_SEPARATOR_SHOWN = true;	
	
	public static int TITLE_SEPARATOR_THICKNESS = 1;
	
	public static Color TITLE_SEPARATOR_COLOR = WidgetConstants.COLOR_BORDER;
	
	public static Color COMPONENT_BACKGROUND = Color.white;

	@Override
	public ReportComponentContainer createReportComponentContainer(IConditionComponent component, 
			Alignment horizontalAlignment, String title, boolean titleVisible) {
		ReportComponentContainer rc = new ReportComponentContainer(component, horizontalAlignment, title, titleVisible);
		if(COMPONENT_BACKGROUND != null) {
			rc.setBackground(COMPONENT_BACKGROUND);
		}
		rc.setTitleFont(TITLE_FONT);
		rc.setTitleAlignment(TITLE_ALIGNMENT);
		rc.setTitleOpaque(TITLE_OPAQUE);
		if(TITLE_OPAQUE && TITLE_BACKGROUND != null) {
			rc.setTitleBackground(TITLE_BACKGROUND);
		}
		if(TITLE_FOREGROUND != null) {
			rc.setTitleForeground(TITLE_FOREGROUND);
		}
		if(BORDER_SHOWN && BORDER_COLOR != null) {
			rc.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, BORDER_THICKNESS));
		}
		if(TITLE_SEPARATOR_SHOWN) {
			rc.configureTitleSeparator(true, TITLE_SEPARATOR_COLOR, TITLE_SEPARATOR_THICKNESS);
		} else {
			rc.configureTitleSeparator(false, TITLE_SEPARATOR_COLOR, TITLE_SEPARATOR_THICKNESS);
		}
		return rc;
	}
}