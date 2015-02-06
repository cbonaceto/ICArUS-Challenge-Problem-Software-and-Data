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
package org.mitre.icarus.cps.experiment_core.condition;

import java.awt.Image;
import java.net.URL;

import javax.swing.JComponent;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * @author CBONACETO
 *
 */
@XmlType(name="InstructionsPage", namespace="IcarusCPD_Base")
public class InstructionsPage {
	/** The text for the instructions page */
	protected String pageText;
	
	/** The URL for instructions page */
	protected URL pageURL;
	
	/** The URL for the instructions page image */
	protected String imageURL;
	
	/** The image for the instructions page. */
	protected Image pageImage;
	
	/** The widget for the instructions page */
	protected JComponent instructionsWidget;
	
	/** Hyperlink to go to when this instructions page is clicked */
	protected String pageClickHyperlink;
	
	public InstructionsPage() {}
	
	public InstructionsPage(String imageURL) {
		this.imageURL = imageURL;
	}
	
	public InstructionsPage(Image pageImage) {
		this.pageImage = pageImage;
	}
	
	public InstructionsPage(JComponent instructionsWidget) {
		this.instructionsWidget = instructionsWidget;
	}

	@XmlElement(name="PageText")
	public String getPageText() {
		return pageText;
	}

	public void setPageText(String pageText) {
		this.pageText = pageText;
	}

	@XmlAttribute(name="pageUrl")
	public URL getPageURL() {
		return pageURL;
	}

	public void setPageURL(URL pageURL) {
		this.pageURL = pageURL;
	}

	@XmlAttribute(name="imageUrl")
	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	@XmlTransient
	public Image getPageImage() {		
		return pageImage;
	}

	public void setPageImage(Image pageImage) {
		this.pageImage = pageImage;
	}

	@XmlTransient	
	public JComponent getInstructionsWidget() {
		return instructionsWidget;
	}

	public void setInstructionsWidget(JComponent instructionsWidget) {
		this.instructionsWidget = instructionsWidget;
	}

	@XmlAttribute(name="pageClickHyperlink")	
	public String getPageClickHyperlink() {
		return pageClickHyperlink;
	}

	public void setPageClickHyperlink(String pageClickHyperlink) {
		this.pageClickHyperlink = pageClickHyperlink;
	}	
}