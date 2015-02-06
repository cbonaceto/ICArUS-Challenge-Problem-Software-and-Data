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
package org.mitre.icarus.cps.app.experiment.phase_2;

import org.mitre.icarus.cps.exam.base.IcarusExam.NormalizationMode;

/**
 * Constants class with Phase 2 experiment constants.
 * 
 * @author CBONACETO
 *
 */
public class ExperimentConstants_Phase2 {
	
	private ExperimentConstants_Phase2() {}
	
	public static final Integer DEFAULT_INITIAL_PROBABILITY = 0; //Previously null, but behavior was buggy
	
	public static final boolean NORMALIZE_INITIAL_PROBABILITIES = false;
	
	public static final boolean ALWAYS_CONFIRM_NORMALIZATION = false;	
	
	/** Whether to also show the display next/forward button when reviewing previous trial outcomes in a 
	 * batch plot*/
	public static final boolean SHOW_FORWARD_BUTTON_FOR_BATCH_PLOTS = true;
	
	/** Whether to show the trial number on Blue locations on the map when creating a batch plot */
	public static final boolean SHOW_TRIAL_NUMBER_FOR_BATCH_PLOTS = true;
	
	/** Whether to enable tooltips that show P and U when hovering over Blue locations in batch plots */
	public static final boolean ENABLE_HOVER_TOOL_TIPS_FOR_BATCH_PLOTS = false;
	
	/** The normalization mode to use when setting the probabilities of each Red tactic */
	public static final NormalizationMode RED_TACTIC_PROBABILITY_NORMALIZATION_MODE = 
			NormalizationMode.NormalizeDuringInstaneous;
	
	/** Whether to highlight relevant BLUEBOOK cells for participants */
	public static final boolean HIGHLIGHT_BLUEBOOK_CELLS = true;
	
	public static final boolean SHOW_BEGIN_TRIAL_PART_STATE = false;
}