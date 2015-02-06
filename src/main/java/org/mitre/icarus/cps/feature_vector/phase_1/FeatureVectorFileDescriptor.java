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
package org.mitre.icarus.cps.feature_vector.phase_1;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * Describes the location and version information of the KML and CSV versions of a feature vector.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="FeatureVectorFileDescriptor", namespace="IcarusCPD_1")
public class FeatureVectorFileDescriptor {
	
	/** URL to the KML version of the feature vector */	
	protected String featureVectorURL_KML;
	
	/** KML file time stamp */
	protected Date kmlFileTimeStamp;
	
	/** URL to the CSV version of the feature vector */
	protected String featureVectorURL_CSV;
	
	/** CSV file time stamp */
	protected Date csvFileTimeStamp;
	
	/**
	 * No arg constructor.
	 */
	public FeatureVectorFileDescriptor() {}
	
	/**
	 * Constructor that takes the featureVectorURL_KML and featureVectorURL_CSV.
	 * 
	 * @param featureVectorURL_KML the URL to the KML version of the feature vector
	 * @param featureVectorURL_CSV the URL to the CSV version of the feature vector
	 */
	public FeatureVectorFileDescriptor(String featureVectorURL_KML, String featureVectorURL_CSV) {
		this.featureVectorURL_KML = featureVectorURL_KML;
		this.featureVectorURL_CSV = featureVectorURL_CSV;
	}

	/**
	 * Get the URL to the KML version of the feature vector.
	 * 
	 * @return the URL
	 */
	@XmlAttribute(name="featureVectorUrl_KML")
	public String getFeatureVectorURL_KML() {
		return featureVectorURL_KML;
	}

	/**
	 * Set the URL to the KML version of the feature vector.
	 * 
	 * @param featureVectorURL_KML the URL
	 */
	public void setFeatureVectorURL_KML(String featureVectorURL_KML) {
		this.featureVectorURL_KML = featureVectorURL_KML;
	}

	/**
	 * Get the KML file time stamp. 
	 * 
	 * @return the time stamp
	 */
	@XmlAttribute(name="kMLFileTimeStamp")
	public Date getKmlFileTimeStamp() {
		return kmlFileTimeStamp;
	}

	/**
	 * Set the KML file time stamp.
	 * 
	 * @param kmlFileTimeStamp the time stamp
	 */
	public void setKmlFileTimeStamp(Date kmlFileTimeStamp) {
		this.kmlFileTimeStamp = kmlFileTimeStamp;
	}

	/**
	 * Get the URL to the CSV version of the feature vector.
	 * 
	 * @return the URL
	 */
	@XmlAttribute(name="featureVectorUrl_CSV")
	public String getFeatureVectorURL_CSV() {
		return featureVectorURL_CSV;
	}

	/**
	 * Set the URL to the CSV version of the feature vector.
	 * 
	 * @param featureVectorURL_CSV the URL
	 */
	public void setFeatureVectorURL_CSV(String featureVectorURL_CSV) {
		this.featureVectorURL_CSV = featureVectorURL_CSV;
	}

	/**
	 * Get the CSV file time stamp.
	 * 
	 * @return the time stamp
	 */
	@XmlAttribute(name="cSVFileTimeStamp")
	public Date getCsvFileTimeStamp() {
		return csvFileTimeStamp;
	}

	/**
	 * Set the CSV file time stamp.
	 * 
	 * @param csvFileTimeStamp the time stamp
	 */
	public void setCsvFileTimeStamp(Date csvFileTimeStamp) {
		this.csvFileTimeStamp = csvFileTimeStamp;
	}
}