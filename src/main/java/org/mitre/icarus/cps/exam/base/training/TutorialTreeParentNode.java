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
package org.mitre.icarus.cps.exam.base.training;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

/**
 * @author CBONACETO
 *
 */
@XmlType(name="TutorialTreeParentNode", namespace="IcarusCPD_Base")
public class TutorialTreeParentNode extends TutorialTreeNode {
	
	/** The child nodes */	
	protected List<TutorialTreeNode> children;
	
	public TutorialTreeParentNode() {}
	
	public TutorialTreeParentNode(String tutorialName, String pageOrSectionName, Integer pageIndex) {
		this(tutorialName, pageOrSectionName, pageIndex, null);
	}
	
	public TutorialTreeParentNode(String tutorialName, String pageOrSectionName, Integer pageIndex, 
			List<TutorialTreeNode> children) {
		super(tutorialName, pageOrSectionName, pageIndex);
		this.children = children;
	}
	
	/**
	 * @param child
	 */
	public void addChild(TutorialTreeNode child) {
		if(children == null) {
			children = new LinkedList<TutorialTreeNode>();
		}
		children.add(child);
	}

	/**
	 * @return
	 */
	@XmlElementWrapper(name="Children")
	@XmlElement(name="Child")
	public List<TutorialTreeNode> getChildren() {
		return children;
	}

	/**
	 * @param children
	 */
	public void setChildren(List<TutorialTreeNode> children) {
		this.children = children;
	}
}