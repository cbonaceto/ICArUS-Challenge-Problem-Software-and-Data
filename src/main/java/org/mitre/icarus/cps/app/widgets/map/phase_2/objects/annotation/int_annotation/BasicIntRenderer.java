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

/**
 * @author CBONACETO
 *
 */
public class BasicIntRenderer extends JLabelBalloonContentRenderer<IntDatum> implements IntBalloonContentRenderer {

	private static final long serialVersionUID = 4713588225503748407L;

	@Override
	public void setContent(IntDatum content) {
		this.content = content;
		if(content != null) {
			switch(content.getDatumType()) {
			case HUMINT:
				DatumListItemType humintType = DatumListItemType.HUMINT;
				setForeground(humintType.getDatumListItem().getColor());				
				break;
			case IMINT:
				DatumListItemType imintType = DatumListItemType.IMINT;
				setForeground(imintType.getDatumListItem().getColor());				
				break;
			case OSINT:
				DatumListItemType osintType = DatumListItemType.OSINT;
				setForeground(osintType.getDatumListItem().getColor());			
				break;	
			default:
				break;
			}
			setText(createIntString(content, false));
		} else {
			setText("");
		}		
	}
	
	/**
	 * @param intDatum
	 * @return
	 */
	public static String createIntString(IntDatum intDatum, boolean configureHtmlFontColor) {
		if(intDatum == null) {
			return "";
		} else {
			StringBuilder sb = new StringBuilder();
			switch(intDatum.getDatumType()) {
			case HUMINT:
				if(configureHtmlFontColor) {
					sb.append(WidgetUtils.createHtmlFontString(
							DatumListItemType.HUMINT.getDatumListItem().getColor()));
				}
				sb.append(DatumListItemType.HUMINT.getDatumListItem().getName() + ": " + 
						MissionControllerUtils.createIntDatumValueString(intDatum));
				break;
			case IMINT:
				if(configureHtmlFontColor) {
					sb.append(WidgetUtils.createHtmlFontString(
							DatumListItemType.IMINT.getDatumListItem().getColor()));
				}
				sb.append(DatumListItemType.IMINT.getDatumListItem().getName() + ": " + 
						MissionControllerUtils.createIntDatumValueString(intDatum));
				break;
			case OSINT:
				if(configureHtmlFontColor) {
					sb.append(WidgetUtils.createHtmlFontString(
							DatumListItemType.OSINT.getDatumListItem().getColor()));
				}
				sb.append(DatumListItemType.OSINT.getDatumListItem().getName() + ": " + 
						MissionControllerUtils.createIntDatumValueString(intDatum));
				break;
			default:
				break;
			}
			if(configureHtmlFontColor) {
				sb.append("</font>");
			}
			return sb.toString();
		}
	}

	@Override
	public boolean isIntTypeSupported(DatumType intType) {
		return intType == DatumType.OSINT || intType == DatumType.IMINT || intType == DatumType.HUMINT;
	}
}