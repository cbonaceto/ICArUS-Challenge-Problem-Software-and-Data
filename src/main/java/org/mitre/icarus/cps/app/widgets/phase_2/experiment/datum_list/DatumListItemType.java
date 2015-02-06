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

import java.awt.Color;

import org.mitre.icarus.cps.app.widgets.phase_2.ColorManager_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.DatumType;

/**
 * An enumeration containing predefined datum list item types.
 * 
 * @author CBONACETO
 *
 */
public enum DatumListItemType {
	OSINT("OSINT", DatumType.OSINT, ColorManager_Phase2.getColor(ColorManager_Phase2.OSINT), false, false), 
	IMINT("IMINT", DatumType.IMINT, ColorManager_Phase2.getColor(ColorManager_Phase2.IMINT), false, false), 
	HUMINT("HUMINT", DatumType.HUMINT, ColorManager_Phase2.getColor(ColorManager_Phase2.HUMINT), false, false), 
	SIGINT("SIGINT", DatumType.SIGINT, ColorManager_Phase2.getColor(ColorManager_Phase2.SIGINT), false, false), 
	P_PROPENSITY("P(Attack | IMINT, OSINT)", DatumType.AttackProbabilityReport_Propensity, 
			ColorManager_Phase2.getColor(ColorManager_Phase2.REPORT), true, false), 
	P_CAPABILITY_PROPENSITY("P(Attack | HUMINT, IMINT, OSINT)", 
			DatumType.AttackProbabilityReport_Capability_Propensity, 
			ColorManager_Phase2.getColor(ColorManager_Phase2.REPORT), true, false),
	P_ACTIVITY("P(Attack | SIGINT)", DatumType.AttackProbabilityReport_Activity, 
			ColorManager_Phase2.getColor(ColorManager_Phase2.REPORT), true, false),
	P_ACTIVITY_CAPABILITY_PROPENSITY("P(Attack | SIGINT, HUMINT, IMINT, OSINT)", 
			DatumType.AttackProbabilityReport_Activity_Capability_Propensity, 
			ColorManager_Phase2.getColor(ColorManager_Phase2.REPORT), true, false);
	
	/** The datum list item */
	private final DatumListItem datumListItem;	
	
	private DatumListItemType(String name, DatumType datumType, Color color, boolean reportedType,
			boolean titleLabel) {
		this.datumListItem = new DatumListItem(name, datumType, color, reportedType, titleLabel);
	}
	
	public DatumListItem getDatumListItem() {
		return datumListItem;
	}

	/**
	 * @param datumType
	 * @return
	 */
	public static DatumListItemType getDatumListItemType(DatumType datumType) {
		switch(datumType) {
		case AttackProbabilityReport_Activity:
			return P_ACTIVITY;
		case AttackProbabilityReport_Activity_Capability_Propensity:
			return P_ACTIVITY_CAPABILITY_PROPENSITY;
		case AttackProbabilityReport_Capability_Propensity:
			return P_CAPABILITY_PROPENSITY;
		case AttackProbabilityReport_Propensity:
			return P_PROPENSITY;
		case HUMINT:
			return HUMINT;
		case IMINT:
			return IMINT;
		case OSINT:
			return OSINT;
		case SIGINT:
			return SIGINT;
		default:
			return null;
		}
	}
}