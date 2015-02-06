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

import java.awt.Image;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="TutorialTreeNode", namespace="IcarusCPD_Base")
@XmlSeeAlso({TutorialTreeParentNode.class})
public class TutorialTreeNode {	
	
	/** The name of the tutorial this node is for */
	protected String tutorialName;
	
	/** The name of the tutorial page or section this node is for */
	protected String pageOrSectionName;
	
	/** The page number of the tutorial page this node is for */
	protected Integer pageIndex;
	
	/** A URL to an optional image icon for the node */
	protected String iconUrl;
	
	/** The optional image icon for the node */
	protected transient Image icon;
	
	public TutorialTreeNode() {}	
	
	public TutorialTreeNode(String tutorialName, String pageOrSectionName, Integer pageIndex) {
		this.tutorialName = tutorialName;
		this.pageOrSectionName = pageOrSectionName;
		this.pageIndex = pageIndex;
	}

	/*@XmlAttribute(name="tutorialName")
	public String getTutorialName() {
		return tutorialName;
	}

	public void setTutorialName(String tutorialName) {
		this.tutorialName = tutorialName;
	}*/

	@XmlAttribute(name="pageOrSectionName")
	public String getPageOrSectionName() {
		return pageOrSectionName;
	}

	public void setPageOrSectionName(String pageOrSectionName) {
		this.pageOrSectionName = pageOrSectionName;
	}

	@XmlAttribute(name="pageIndex")
	public Integer getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}

	@XmlAttribute(name="iconUrl")
	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	@XmlTransient
	public Image getIcon() {
		return icon;
	}

	public void setIcon(Image icon) {
		this.icon = icon;
	}
}