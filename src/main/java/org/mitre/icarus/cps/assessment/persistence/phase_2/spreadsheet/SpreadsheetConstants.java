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
package org.mitre.icarus.cps.assessment.persistence.phase_2.spreadsheet;

import java.util.LinkedHashMap;

/**
 * Defines the workbook sheet titles, descriptions, and number of columns.
 */
public class SpreadsheetConstants {
	
	public static final int NUM_SHEETS = 14;
	
	/** Defines the sheet name that appears on the tab */
	public static final String SHEET_OVERALL = "Overall";
	public static final String SHEET_AA = "AA";
	public static final String SHEET_PDE = "PDE";
	public static final String SHEET_RR = "RR";
	public static final String SHEET_AV = "AV";
	public static final String SHEET_PM = "PM";
	public static final String SHEET_CS = "CS";
	public static final String SHEET_SS = "SS";
	public static final String SHEET_CB = "CB";
	public static final String SHEET_ASR = "ASR";
	public static final String SHEET_RSR_Spec = "RSR-Spec";
	public static final String SHEET_RSR_Variants = "RSR-Variants";
	public static final String SHEET_RMR = "RMR";
	public static final String SHEET_NFA = "NFA";
	
	/** Used to populate the sheet titles, the first row of each sheet. */
	public static final LinkedHashMap<String,String> SHEET_TITLE_MAP;
	static {
		SHEET_TITLE_MAP = new LinkedHashMap<String,String>(NUM_SHEETS);
		SHEET_TITLE_MAP.put(SHEET_OVERALL, "Overall Results");
		SHEET_TITLE_MAP.put(SHEET_AA, "Anchoring and Adjustment (AA): Bias Magnitude = Nq - Np, Bias Exhibited When Np < Nq");
		SHEET_TITLE_MAP.put(SHEET_PDE, "Persistence of Discredited Evidence (PDE): Bias Magnitude = Nq - Np, Bias Exhibited When Np < Nq");
		SHEET_TITLE_MAP.put(SHEET_RR, "Representativeness (RR): Bias Magnitude = P - Q, Bias Exhibited When P > Q");
		SHEET_TITLE_MAP.put(SHEET_AV, "Availability (AV): Bias Magnitude = Nq - Np, Bias Exhibited When Np < Nq");
		SHEET_TITLE_MAP.put(SHEET_PM, "Probability Matching (PM): Bias Magnitude = 100% - n, Bias Exhibited When n < 100% (n = frequency with which normative Blue action selected)");
		SHEET_TITLE_MAP.put(SHEET_CS, "Confirmation Bias (CS): Bias Magnitude = 100% - f, Bias Exhibited When f < 100% (f = frequency with which normative SIGINT location selected)");
		SHEET_TITLE_MAP.put(SHEET_SS, "Satisfaciton of Search (SS): Bias Magnitude = 100% - s, Bias Exhibited When s < 100% (s = percent pf trials reviewed when creating a batch plot)");
		SHEET_TITLE_MAP.put(SHEET_CB, "Change Blindness (CB): Bias Magnitude = 1 - b, Bias Exhibited When b > 1 (b = number of trials needed to detect a change in Red tactics)");
		SHEET_TITLE_MAP.put(SHEET_ASR, "Absolute Success Rate (ASR)");
		SHEET_TITLE_MAP.put(SHEET_RSR_Spec, "Relative Success Rate (RSR) Test Spec");
		SHEET_TITLE_MAP.put(SHEET_RSR_Variants, "Relative Success Rate (RSR) Variants");
		SHEET_TITLE_MAP.put(SHEET_RMR, "Relative Match Rate (RMR)");
		SHEET_TITLE_MAP.put(SHEET_NFA, "Neural Fidelity Assessment (NFA)");
	}
	
	/** Used for merging table title cells to the appropriate width. */
	public static final LinkedHashMap<String,Integer> SHEET_NUM_COLUMNS_MAP;
	static {
		SHEET_NUM_COLUMNS_MAP = new LinkedHashMap<String,Integer>(NUM_SHEETS);
		SHEET_NUM_COLUMNS_MAP.put(SHEET_OVERALL, 7);
		SHEET_NUM_COLUMNS_MAP.put(SHEET_AA, 7);
		SHEET_NUM_COLUMNS_MAP.put(SHEET_PDE, 7);
		SHEET_NUM_COLUMNS_MAP.put(SHEET_RR, 8);
		SHEET_NUM_COLUMNS_MAP.put(SHEET_AV, 7);
		SHEET_NUM_COLUMNS_MAP.put(SHEET_PM, 8);
		SHEET_NUM_COLUMNS_MAP.put(SHEET_CS, 8);
		SHEET_NUM_COLUMNS_MAP.put(SHEET_SS, 8);
		SHEET_NUM_COLUMNS_MAP.put(SHEET_CB, 8);
		SHEET_NUM_COLUMNS_MAP.put(SHEET_ASR, 5);
		SHEET_NUM_COLUMNS_MAP.put(SHEET_RSR_Spec, 5);
		SHEET_NUM_COLUMNS_MAP.put(SHEET_RSR_Variants, 11);
		SHEET_NUM_COLUMNS_MAP.put(SHEET_RMR, 4);
		SHEET_NUM_COLUMNS_MAP.put(SHEET_NFA, 4);
	}
	
	public static final String[] RSR_VARIANTS_DEFINITIONS = new String[] {
		"RSR Test Spec: RSR computed as per the test spec with KLD as the similarity measure and the random (uniform) probabilities as the null model.",
		"RSR Test Spec (Bayesian Model): RSR computed as per the test spec on a Bayesian model (a model that always sets probabilities to their normative values).",
		"RSR-Bayesian: RSR computed with KLD as the similarity measure and the Bayesian probabilities as the null model.",
		"RSR-Spm-Spr-Avg: RSR computed using the average Spm (KLD-based similarity of model to human) and average Spr (KLD-based similarity of random to human), where Spm and Spr are averaged over all trials in a task.",
		"RSR-Spm-Spq-Avg: RSR computed using the average Spm (KLD-based similarity of model to human) and average Spq (KLD-based similarity of Bayesian to human), where Spm and Spq are averaged over all trials in a task.",
		"RSR(RMSE): RSR computed using Root Mean Squared Error (RMSE) as the similarity measure and the random (uniform) probabilities as the null model.",
		"RSR-Bayesian(RMSE): RSR computed using Root Mean Squared Error (RMSE) as the similarity measure and the Bayesian probabilities as the null model."
	};
}
