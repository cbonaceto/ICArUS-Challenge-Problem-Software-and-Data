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
package org.mitre.icarus.cps.exam.phase_05.training;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * A training rule.  This is preliminary and the format will change quite a bit.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="Rule", namespace="IcarusCPD_05")
public class Rule {
	/** The rule ID */
	protected Integer ruleId;
	
	/** Rule text */
	protected String ruleText;

	/**
	 * Get the rule ID.
	 * 
	 * @return
	 */
	@XmlAttribute(name="RuleId")
	public Integer getRuleId() {
		return ruleId;
	}

	/**
	 * Set the rule ID.
	 * 
	 * @param ruleId
	 */
	public void setRuleId(Integer ruleId) {
		this.ruleId = ruleId;
	}

	/**
	 * Get the rule text.
	 * 
	 * @return
	 */
	@XmlElement(name="RuleText")
	public String getRuleText() {
		return ruleText;
	}

	/**
	 * Set the rule text.
	 * 
	 * @param ruleText
	 */
	public void setRuleText(String ruleText) {
		this.ruleText = ruleText;
	}
}
