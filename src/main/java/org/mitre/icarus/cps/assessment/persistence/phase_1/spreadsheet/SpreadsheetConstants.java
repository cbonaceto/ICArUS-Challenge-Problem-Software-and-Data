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
package org.mitre.icarus.cps.assessment.persistence.phase_1.spreadsheet;

import java.util.LinkedHashMap;

/**
 * Defines the workbook sheet titles, descriptions, and number of columns.
 */
public class SpreadsheetConstants {
	
	public static final int NUM_SHEETS = 10;
	
	/** Defines the sheet name that appears on the tab */
	public static final String SHEET_OVERALL = "Overall";
	public static final String SHEET_RR = "RR";
	public static final String SHEET_AA = "AA";
	public static final String SHEET_PM = "PM";
	public static final String SHEET_CS = "CS";
	public static final String SHEET_RSR_Spec = "RSR-Spec";
	public static final String SHEET_RSR_Variants = "RSR-Variants";
	public static final String SHEET_ASR = "ASR";
	public static final String SHEET_RMR = "RMR";
	public static final String SHEET_NFA = "NFA";
	
	/** Used to populate the sheet titles, the first row of each sheet. */
	public static final LinkedHashMap<String,String> SHEET_TITLE_MAP;
	static {
		SHEET_TITLE_MAP = new LinkedHashMap<String,String>(NUM_SHEETS);
		SHEET_TITLE_MAP.put(SHEET_OVERALL, "Overall Results");
		SHEET_TITLE_MAP.put(SHEET_RR, "Representativeness: Bias Magnitude = Nq - Np, Bias Exhibited When Np < Nq");
		SHEET_TITLE_MAP.put(SHEET_AA, "Anchoring and Adjustment: Bias Magnitude = |delta Nq| - |delta Np|, Bias Exhibited When |delta Np| < |delta Nq| and sign(delta Np) = sign(delta Nq)");
		SHEET_TITLE_MAP.put(SHEET_PM, "Probability Matching: Bias Magnitude = RMS(F-I) - RMS(F-P), Bias Exhibited when RMS(F-I) > RMS(F-P)");
		SHEET_TITLE_MAP.put(SHEET_CS, "Confirmation Bias (Seeking): Bias Magnitude = C - 50%, Bias Exhibited When C > 50%");
		SHEET_TITLE_MAP.put(SHEET_RSR_Spec, "Relative Success Rate (RSR) Test Spec");
		SHEET_TITLE_MAP.put(SHEET_RSR_Variants, "Relative Success Rate (RSR) Variants");
		SHEET_TITLE_MAP.put(SHEET_ASR, "Absolute Success Rate (ASR) and RSR Test Spec");
		SHEET_TITLE_MAP.put(SHEET_RMR, "Relative Match Rate (RMR)");
		SHEET_TITLE_MAP.put(SHEET_NFA, "Neural Fidelity Assessment (NFA)");
	}
	
	/** Used for merging table title cells to the appropriate width. */
	public static final LinkedHashMap<String,Integer> SHEET_NUM_COLUMNS_MAP;
	static {
		SHEET_NUM_COLUMNS_MAP = new LinkedHashMap<String,Integer>(NUM_SHEETS);
		SHEET_NUM_COLUMNS_MAP.put(SHEET_OVERALL, 7);
		SHEET_NUM_COLUMNS_MAP.put(SHEET_RR, 7);
		SHEET_NUM_COLUMNS_MAP.put(SHEET_AA, 10);
		SHEET_NUM_COLUMNS_MAP.put(SHEET_PM, 7);
		SHEET_NUM_COLUMNS_MAP.put(SHEET_CS, 8);
		SHEET_NUM_COLUMNS_MAP.put(SHEET_RSR_Spec, 4);
		SHEET_NUM_COLUMNS_MAP.put(SHEET_RSR_Variants, 9);
		SHEET_NUM_COLUMNS_MAP.put(SHEET_ASR, 5);
		SHEET_NUM_COLUMNS_MAP.put(SHEET_RMR, 2);
		SHEET_NUM_COLUMNS_MAP.put(SHEET_NFA, 1);
	}
}
