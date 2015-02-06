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
package org.mitre.icarus.cps.app.experiment;

import org.mitre.icarus.cps.experiment_core.condition.Condition;
import org.mitre.icarus.cps.experiment_core.subject_data.SubjectConditionData;
import org.mitre.icarus.cps.experiment_core.subject_data.SubjectData;
import org.mitre.icarus.cps.web.model.Site;

/**
 * The ICArUS-specific SubjectData class contains a site ID in addition to the subject ID.
 * 
 * @author CBONACETO
 *
 */
public class IcarusSubjectData extends SubjectData {
	
	/** The subject's site location (e.g., Bedford) */
	protected Site site;
	
	public IcarusSubjectData() {}
	
	public IcarusSubjectData(String subjectId) {
		this(subjectId, 0);		
	}	
	
	public IcarusSubjectData(String subjectId, int currentCondition) {
		super(subjectId, currentCondition);		
	}	
	
	public IcarusSubjectData(String subjectId, Site site) {
		this(subjectId, site, 0);		
	}
	
	public IcarusSubjectData(String subjectId, Site site, int currentCondition) {
		this(subjectId, site, currentCondition, 0);
	}	
	
	public IcarusSubjectData(String subjectId, Site site, int currentCondition, int currentTrial) {
		super(subjectId, currentCondition);
		this.site = site;
		this.currentTrial = currentTrial;
	}
	
	/**
	 * Get the subject's site location (e.g., Bedford).
	 * 
	 * @return the site
	 */
	public Site getSite() {
		return site;
	}

	/**
	 * Set the subject's site location (e.g., Bedford).
	 * 
	 * @param site the site
	 */
	public void setSite(Site site) {
		this.site = site;
	}

	@Override
	public double getScore() {
		return 0;
	}

	@Override
	public SubjectConditionData startCondition(Condition condition) {
		return null;
	}

	@Override
	public String toString() {
		if(site != null) {
			return site.getTag() + "-" + subjectId;
			//return "ID: " + subjectId + ", Site: " + site.getShortName();
		} else {
			return subjectId;
			//return "ID: " + subjectId;
		}
	}	
	
	@Override
	public int compareTo(SubjectData o) {
		if(o != null) {			
			if(subjectId != null && (!subjectId.equals(o.getSubjectId()) || site == null)) {
				return subjectId.compareTo(o.getSubjectId());
			}
			if(site != null && site.getTag() != null && o instanceof IcarusSubjectData) {
				Site site2 = ((IcarusSubjectData)o).getSite();
				if(site2 != null) {
					return site.getTag().compareTo(site2.getTag());
				}
			}
		}
		return -1;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof SubjectData) {
			return(compareTo((SubjectData)obj) == 0);
		} else {
			return false;
		}		
	}
}