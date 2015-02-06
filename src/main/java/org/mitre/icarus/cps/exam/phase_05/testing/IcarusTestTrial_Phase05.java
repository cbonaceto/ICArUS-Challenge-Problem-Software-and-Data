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
package org.mitre.icarus.cps.exam.phase_05.testing;

import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.base.testing.IcarusTestTrial;
import org.mitre.icarus.cps.exam.phase_05.testing.assessment.AssessmentTrial;
import org.mitre.icarus.cps.exam.phase_05.testing.scene_presentation.ScenePresentationTrial;

@XmlType(name="IcarusTestTrial_Phase05", namespace="IcarusCPD_05")
@XmlSeeAlso({ScenePresentationTrial.class, AnalogicReasoningTrial.class, AssessmentTrial.class})
public abstract class IcarusTestTrial_Phase05 extends IcarusTestTrial {
}