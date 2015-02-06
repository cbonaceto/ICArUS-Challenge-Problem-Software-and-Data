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
package org.mitre.icarus.cps.feature_vector.phase_2;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * Describes the location and version information of a feature vector file.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="FeatureVectorFileDescriptor", namespace="IcarusCPD_2", propOrder={"fileUrl", "versionId", "timeStamp"})
public class FeatureVectorFileDescriptor {
	
	/** URL to the feature vector file */	
	protected String fileUrl;
	
	/** File version information */
	protected String versionId;
	
	/** File time stamp */
	protected Date timeStamp;	
	
	/**
	 * Construct an empty FeatureVectorFileDescriptor.
	 */
	public FeatureVectorFileDescriptor() {}
	
	/**
	 * Constructor a FeatureVectorFileDescriptor with the given feature vector file URL.
	 * 
	 * @param fileUrl the URL to the feature vector file
	 */
	public FeatureVectorFileDescriptor(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	/**
	 * Get the URL to the feature vector file.
	 * 
	 * @return the URL to the feature vector file
	 */
	@XmlAttribute(name="fileUrl")
	public String getFileUrl() {
		return fileUrl;
	}

	/**
	 * Set the URL to the feature vector file.
	 * 
	 * @param fileUrl the URL to the feature vector file
	 */
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	/**
	 * Get the feature vector file version ID.
	 * 
	 * @return the feature vector file version ID
	 */
	@XmlAttribute(name="versionId")
	public String getVersionId() {
		return versionId;
	}

	/**
	 * Set the feature vector file version ID.
	 * 
	 * @param versionId the feature vector file version ID
	 */
	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}

	/**
	 * Get the feature vector file last-modified time stamp.
	 * 
	 * @return the feature vector file last-modified time stamp
	 */
	@XmlAttribute(name="timeStamp")
	public Date getTimeStamp() {
		return timeStamp;
	}

	/**
	 * Set the feature vector file last-modified time stamp.
	 * 
	 * @param timeStamp the feature vector file last-modified time stamp
	 */
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}	
}