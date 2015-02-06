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

// TODO: Auto-generated Javadoc
/**
 * Contains information from various INTs for a particular x,y location.
 * 
 * @author CBONACETO
 *
 */
public class LocationIntelReport {
	
	/** The IMINT information at the location (government or military facility). */
	ImintType imintInfo;
	
	/** The MOVINT information at the location (dense or sparse traffic). */
	MovintType movintInfo;
	
	/** The SIGINT information at the location (chatter or silent). */
	SigintType sigintInfo;
	
	/** The SOCINT information at the location (group region). */
	GroupType socintInfo;
	
	/** The HUMINT information at the location (distance from source point). */
	HumintType humintInfo;
	
	/**
	 * No arg constructor.
	 */
	public LocationIntelReport() {}
	
	/**
	 * Instantiates a new location intel report.
	 *
	 * @param socintInfo the socint info
	 */
	public LocationIntelReport( GroupType socintInfo ) {
		this.socintInfo =  socintInfo;
	}
	
	/**
	 * Instantiates a new location intel report.
	 *
	 * @param socintInfo the socint info
	 * @param imintInfo the imint info
	 * @param movintInfo the movint info
	 * @param sigintInfo the sigint info
	 */
	public LocationIntelReport( GroupType socintInfo, ImintType imintInfo, 
			MovintType movintInfo, SigintType sigintInfo ) {
		this.socintInfo =  socintInfo;
		this.imintInfo = imintInfo;
		this.movintInfo = movintInfo;
		this.sigintInfo = sigintInfo;
	}

	/**
	 * Instantiates a new location intel report.
	 *
	 * @param imintInfo the imint info
	 * @param movintInfo the movint info
	 * @param sigintInfo the sigint info
	 * @param socintInfo the socint info
	 * @param humintInfo the humint info
	 */
	public LocationIntelReport(ImintType imintInfo, MovintType movintInfo,
			SigintType sigintInfo, GroupType socintInfo, HumintType humintInfo) {
		this.imintInfo = imintInfo;
		this.movintInfo = movintInfo;
		this.sigintInfo = sigintInfo;
		this.socintInfo = socintInfo;
		this.humintInfo = humintInfo;
	}
	
	/**
	 * Copy constructor.
	 * 
	 * @param copy
	 */
	public LocationIntelReport(LocationIntelReport copy) {
		this.imintInfo = copy.imintInfo;
		this.movintInfo = copy.movintInfo;
		this.sigintInfo = copy.sigintInfo;
		this.socintInfo = copy.socintInfo;
		this.humintInfo = copy.humintInfo;
	}

	/**
	 * Gets the imint info.
	 *
	 * @return the imint info
	 */
	public ImintType getImintInfo() {
		return imintInfo;
	}

	/**
	 * Sets the imint info.
	 *
	 * @param imintInfo the new imint info
	 */
	public void setImintInfo(ImintType imintInfo) {
		this.imintInfo = imintInfo;
	}
	
	/**
	 * Checks if imint info exists.
	 *
	 * @return true, if successful
	 */
	public boolean hasImintInfo() {
		return (this.imintInfo != null);
	}

	/**
	 * Gets the movint info.
	 *
	 * @return the movint info
	 */
	public MovintType getMovintInfo() {
		return movintInfo;
	}

	/**
	 * Sets the movint info.
	 *
	 * @param movintInfo the new movint info
	 */
	public void setMovintInfo(MovintType movintInfo) {
		this.movintInfo = movintInfo;
	}
	
	/**
	 * Checks if movint info exists.
	 *
	 * @return true, if successful
	 */
	public boolean hasMovintInfo() {
		return (this.movintInfo != null);
	}

	/**
	 * Gets the sigint info.
	 *
	 * @return the sigint info
	 */
	public SigintType getSigintInfo() {
		return sigintInfo;
	}

	/**
	 * Sets the sigint info.
	 *
	 * @param sigintInfo the new sigint info
	 */
	public void setSigintInfo(SigintType sigintInfo) {
		this.sigintInfo = sigintInfo;
	}
	
	/**
	 * Checks if sigint info exists.
	 *
	 * @return true, if successful
	 */
	public boolean hasSigintInfo() {
		return (this.sigintInfo != null);
	}

	/**
	 * Gets the socint info.
	 *
	 * @return the socint info
	 */
	public GroupType getSocintInfo() {
		return socintInfo;
	}

	/**
	 * Sets the socint info.
	 *
	 * @param socintInfo the new socint info
	 */
	public void setSocintInfo(GroupType socintInfo) {
		this.socintInfo = socintInfo;
	}
	
	/**
	 * Checks for socint info.
	 *
	 * @return true, if successful
	 */
	public boolean hasSocintInfo() {
		return (this.socintInfo != null);
	}	

	/**
	 * Gets the humint info.
	 *
	 * @return the humint info
	 */
	public HumintType getHumintInfo() {
		return humintInfo;
	}

	/**
	 * Sets the humint info.
	 *
	 * @param humintInfo the new humint info
	 */
	public void setHumintInfo(HumintType humintInfo) {
		this.humintInfo = humintInfo;
	}

	@Override
	public String toString() {
		return "LocationIntelReport [imintInfo=" + imintInfo + ", movintInfo="
				+ movintInfo + ", sigintInfo=" + sigintInfo + ", socintInfo="
				+ socintInfo + ", humintInfo=" + humintInfo + "]";
	}


}