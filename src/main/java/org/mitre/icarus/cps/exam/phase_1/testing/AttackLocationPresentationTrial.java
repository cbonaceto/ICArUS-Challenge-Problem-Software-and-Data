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
package org.mitre.icarus.cps.exam.phase_1.testing;

import org.mitre.icarus.cps.exam.phase_1.response.IcarusTrialResponse_Phase1;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupAttack;

/**
 * A presentation trial where the subject/model is shown an attack by a group.
 * 
 * @author CBONACETO
 *
 */
public class AttackLocationPresentationTrial extends IcarusTestTrial_Phase1 {
	
	/** The group attack info */
	protected GroupAttack groupAttack;
	
	/**
	 * No arg constructor.
	 */
	public AttackLocationPresentationTrial() {}
	
	/**
	 * Constructor that takes the groupAttack.
	 * 
	 * @param groupAttack the group attack info
	 */
	public AttackLocationPresentationTrial(GroupAttack groupAttack) {
		this.groupAttack = groupAttack;
	}
	
	/**
	 * Get the group attack info.
	 * 
	 * @return the group attack info
	 */
	public GroupAttack getGroupAttack() {
		return groupAttack;
	}

	/**
	 * Set the group attack info.
	 * 
	 * @param groupAttack the group attack info
	 */
	public void setGroupAttack(GroupAttack groupAttack) {
		this.groupAttack = groupAttack;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.base.testing.IcarusTestTrial#getTestTrialType()
	 */
	@SuppressWarnings("deprecation")
	@Override
	public TestTrialType getTestTrialType() {
		return TestTrialType.AttackPresentation;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_1.testing.IcarusTestTrial_Phase1#getTrialResponse()
	 */
	@Override
	public IcarusTrialResponse_Phase1 getTrialResponse() {
		return null;
	}
}