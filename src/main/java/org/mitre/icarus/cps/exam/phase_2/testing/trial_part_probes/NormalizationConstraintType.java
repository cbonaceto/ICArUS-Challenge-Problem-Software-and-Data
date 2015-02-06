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
package org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes;

import javax.xml.bind.annotation.XmlType;

/**
 * An enumeration of normalization constraint types (LessThanOrEqualTo, EqualTo). 
 * The normalization constraint specifies the relationshiop between the sum of
 * a list of probabilities and a target sum.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="NormalizationConstraintType", namespace="IcarusCPD_2")
public enum NormalizationConstraintType {LessThanOrEqualTo, EqualTo}