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
package org.mitre.icarus.cps.app.widgets.map.phase_2.objects.annotation.action_annotation;

import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.BlueLocationPlacemark;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.annotation.JLabelBalloonContentRenderer;
import org.mitre.icarus.cps.app.widgets.phase_2.experiment.ScorePanel;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.BlueAction;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.RedAction;

/**
 * @author CBONACETO
 *
 */
public class ActionBalloonContentRenderer extends JLabelBalloonContentRenderer<BlueLocationPlacemark> {

	private static final long serialVersionUID = 4713588225503748407L;
	
	protected boolean showBlueAction = false;
	
	protected boolean showRedAction = false;
	
	protected boolean showBluePoints = false;
	
	protected boolean showRedPoints = false;

	@Override
	public void setContent(BlueLocationPlacemark content) {
		this.content = content;
		if(content != null) {
			StringBuilder sb = new StringBuilder("<html>");
			//sb.append(content.getName() + "</b>");
			if(showBlueAction) {				
				sb.append("Blue Action: " + BlueAction.getBlueActionString(content.getBlueAction()));
			}
			if(showRedAction) {
				if(showBlueAction) {
					sb.append("<br>");
				}
				sb.append("Red Action: " + RedAction.getRedActionString(content.getRedAction()));
			}
			if(showBluePoints) {
				if(showBlueAction || showRedAction) {
					sb.append("<br>");
				}
				sb.append("Blue Points: " + (content.getRedBluePayoff() != null ? 
						ScorePanel.formatScore(content.getRedBluePayoff().getBluePoints()) : "0"));
			}
			if(showRedPoints) {
				if(showBlueAction || showRedAction || showRedPoints) {
					sb.append("<br>");
				}
				sb.append("Red Points: " + (content.getRedBluePayoff() != null ? 
						ScorePanel.formatScore(content.getRedBluePayoff().getRedPoints()) : "0"));
			}
			sb.append("</html>");
			setText(sb.toString());
		} else {
			setText("");
		}
	}

	public boolean isShowBlueAction() {
		return showBlueAction;
	}

	public void setShowBlueAction(boolean showBlueAction) {
		this.showBlueAction = showBlueAction;
		setContent(content);
	}

	public boolean isShowRedAction() {
		return showRedAction;
	}

	public void setShowRedAction(boolean showRedAction) {		
		//if(showRedAction != this.showRedAction) {
		this.showRedAction = showRedAction;
		setContent(content);
		//}
	}	
	
	public boolean isShowBluePoints() {
		return showBluePoints;
	}

	public void setShowBluePoints(boolean showBluePoints) {
		this.showBluePoints = showBluePoints;
		setContent(content);
	}

	public boolean isShowRedPoints() {
		return showRedPoints;
	}

	public void setShowRedPoints(boolean showRedPoints) {
		this.showRedPoints = showRedPoints;
		setContent(content);
	}

	/**
	 * @param showBlueAction
	 * @param showRedAction
	 */
	public void setShowBlueAndRedAction(boolean showBlueAction, boolean showRedAction,
			boolean showBluePoints, boolean showRedPoints) {
		this.showBlueAction = showBlueAction;
		this.showRedAction = showRedAction;
		this.showBluePoints = showBluePoints;
		this.showRedPoints = showRedPoints;
		setContent(content);
	}
}