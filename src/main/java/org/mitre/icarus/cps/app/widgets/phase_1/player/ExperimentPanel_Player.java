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
package org.mitre.icarus.cps.app.widgets.phase_1.player;

import java.awt.Component;

import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.app.widgets.phase_1.experiment.ExperimentPanel_Phase1;
import org.mitre.icarus.cps.experiment_core.gui.ExperimentStatusPanel;
import org.mitre.icarus.cps.experiment_core.gui.ConditionPanel.BannerOrientation;

/**
 * @author CBONACETO
 *
 */
public class ExperimentPanel_Player extends ExperimentPanel_Phase1 {
	private static final long serialVersionUID = 5159205619010403188L;	
	
	protected ConditionPanel_Player conditionPanel;
	
	PlayerNavButtonPanel navButtonPanel;

	public ExperimentPanel_Player(Component parent, ConditionPanel_Player conditionPanel) {
		super(parent, conditionPanel, new PlayerNavButtonPanel(), BannerOrientation.Top, 
				new ExperimentStatusPanel(WidgetConstants.FONT_STATUS), BannerOrientation.Top);
		this.navButtonPanel = (PlayerNavButtonPanel)super.navButtonPanel;
		this.navButtonPanel.setSkipToTrialButtonVisible(true);
		this.navButtonPanel.setSkipToMissionButtonVisible(true);
		this.conditionPanel = (ConditionPanel_Player)super.conditionPanel;
		statusPanel.setExperimentDescriptor("Exam");
		statusPanel.setComponentsVisible(true, true, true, true);	
	}
	
	public void setSkipToTrialButtonVisible(boolean visible) {
		navButtonPanel.setSkipToTrialButtonVisible(visible);
	}
	
	public void setSkipToTrialButtonEnabled(boolean enabled) {
		navButtonPanel.setSkipToTrialButtonEnabled(enabled);
	}
	
	public void setSkipToMissionButtonVisible(boolean visible) {
		navButtonPanel.setSkipToMissionButtonVisible(visible);
	}
	
	public void setSkipToMissionButtonEnabled(boolean enabled) {
		navButtonPanel.setSkipToMissionButtonEnabled(enabled);
	}

	@Override
	public ConditionPanel_Player getConditionPanel() {
		return conditionPanel;
	}
}