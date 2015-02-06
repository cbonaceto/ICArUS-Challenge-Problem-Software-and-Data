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
package org.mitre.icarus.cps.app.widgets.map.phase_2.objects.annotation.int_annotation;

import org.mitre.icarus.cps.app.experiment.phase_2.MissionControllerUtils;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.annotation.JLabelBalloonContentRenderer;
import org.mitre.icarus.cps.app.widgets.phase_2.experiment.datum_list.DatumListItemType;
import org.mitre.icarus.cps.app.widgets.util.WidgetUtils;
import org.mitre.icarus.cps.exam.phase_2.testing.DatumType;
import org.mitre.icarus.cps.feature_vector.phase_2.int_datum.IntDatum;
import org.mitre.icarus.cps.feature_vector.phase_2.int_datum.SigintDatum;
import org.mitre.icarus.cps.feature_vector.phase_2.int_datum.SigintDatum.SigintType;

/**
 * 
 * @author CBONACETO
 *
 */
public class SigintRenderer extends JLabelBalloonContentRenderer<IntDatum> implements IntBalloonContentRenderer {
	
	private static final long serialVersionUID = 9110495069013066934L;	
	
	protected boolean showSigintProbability = false;
	
	public SigintRenderer() {}
	
	public SigintRenderer(boolean showSigintProbability) {
		this.showSigintProbability = showSigintProbability;
	}

	@Override
	public void setContent(IntDatum content) {
		this.content = content;
		if(content != null && content.getDatumType() == DatumType.SIGINT) {
			SigintDatum sigint = (SigintDatum)content;
			setForeground(DatumListItemType.SIGINT.getDatumListItem().getColor());
			StringBuilder sb = new StringBuilder("<html>");
			sb.append(createSigintString(sigint, showSigintProbability, false));
			sb.append("</html>");			
			setText(sb.toString());
		} else {
			setText("");
		}
	}
	
	/**
	 * @param sigint
	 * @param showSigintProbability
	 * @return
	 */
	public static String createSigintString(SigintDatum sigint, boolean showSigintProbability,
			boolean configureHtmlFontColor) {
		if(sigint != null) {		
			StringBuilder sb = new StringBuilder();
			if(configureHtmlFontColor) {
				sb.append(WidgetUtils.createHtmlFontString(
						DatumListItemType.SIGINT.getDatumListItem().getColor()));
			}
			DatumListItemType sigintType = DatumListItemType.SIGINT;			
			sb.append(sigintType.getDatumListItem().getName() + ": ");
			sb.append(sigint.isRedActivityDetected() != null ? sigint.isRedActivityDetected() ? SigintType.Chatter.toString() : 
				SigintType.Silence.toString() : "");
			if(showSigintProbability) {
				sb.append("<br>");
				DatumListItemType pActivityType = DatumListItemType.P_ACTIVITY;		
				sb.append(pActivityType.getDatumListItem().getName() + ": " + 
						MissionControllerUtils.createIntDatumValueString(sigint));
			}
			if(configureHtmlFontColor) {
				sb.append("</font>");
			}
			return sb.toString();
		}
		return "";
	}

	public boolean isShowSigintProbability() {
		return showSigintProbability;
	}

	public void setShowSigintProbability(boolean showSigintProbability) {
		if(this.showSigintProbability != showSigintProbability) {
			this.showSigintProbability = showSigintProbability;
			this.setContent(content);
		}
	}

	@Override
	public boolean isIntTypeSupported(DatumType intType) {
		return intType == DatumType.SIGINT;
	}
}