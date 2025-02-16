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
package org.mitre.icarus.cps.app.widgets.phase_2.experiment.datum_list;

import org.mitre.icarus.cps.app.widgets.phase_2.WidgetConstants_Phase2;

/**
 * @author CBONACETO
 *
 */
public class DatumLabelFactory {

	/**
	 * @param datumType
	 * @param showCheckBox
	 * @return
	 */
	public static DatumLabel createDatumLabel(DatumListItem datum, boolean showCheckBox,
			boolean titleLabel, int datumValueAlignment) {
		DatumLabel datumLabel = new DatumLabel(datum, showCheckBox, titleLabel, datumValueAlignment);
		datumLabel.setFont(WidgetConstants_Phase2.FONT_DATUM_LABEL);
		return datumLabel;
	}
}