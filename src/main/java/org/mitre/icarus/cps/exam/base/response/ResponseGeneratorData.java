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
package org.mitre.icarus.cps.exam.base.response;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Contains information about the entity (human subject or model) that generated exam responses.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="ResponseGenerator", namespace="IcarusCPD_Base")
public class ResponseGeneratorData {	
	
	/** The site ID */
	protected String siteId;
	
	/** The response generator ID */
	protected String responseGeneratorId;	
	
	/** Whether the response generator is a human subject */
	protected Boolean humanSubject = false;
	
	/**
	 * No arg constructor.
	 */
	public ResponseGeneratorData() {}
	
	/**
	 * Constructor that takes a site ID, resposneGenerator ID, and humanSubject boolean.
	 * 
	 * @param siteId the site ID of the response generator (e.g., MITRE)
	 * @param responseGeneratorId the ID of the response generator (e.g, 101 for a human subject, Model1 for a model)
	 * @param humanSubject whether the response generator is a human subject
	 */
	public ResponseGeneratorData(String siteId, String responseGeneratorId, Boolean humanSubject) {
		this.siteId = siteId;
		this.responseGeneratorId = responseGeneratorId;
		this.humanSubject = humanSubject;
	}
	
	/**
	 * Get whether the response generator is a human subject.
	 * 
	 * @return whether the response generator is a human subject
	 */
	@XmlElement(name="HumanSubject")
	public Boolean isHumanSubject() {
		return humanSubject;
	}

	/**
	 * Set whether the response generator is a human subject.
	 * 
	 * @param humanSubject whether the response generator is a human subject
	 */
	public void setHumanSubject(Boolean humanSubject) {
		this.humanSubject = humanSubject;
	}	

	/**
	 * Get the response generator ID.
	 * 
	 * @return the response generator ID
	 */
	@XmlElement(name="ResponseGeneratorId")
	public String getResponseGeneratorId() {
		return responseGeneratorId;
	}

	/**
	 * Set the response generator ID.
	 * 
	 * @param responseGeneratorId the response generator ID
	 */
	public void setResponseGeneratorId(String responseGeneratorId) {
		this.responseGeneratorId = responseGeneratorId;
	}	
	
	/**
	 * Get the site ID. Each team should have a unique site ID.
	 * 
	 * @return the site ID
	 */
	@XmlElement(name="SiteId")
	public String getSiteId() {
		return siteId;
	}

	/**
	 * Set the site ID. Each team should have a unique site ID.
	 * 
	 * @param siteId the site ID
	 */
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
}