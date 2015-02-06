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
package org.mitre.icarus.cps.assessment.data_model.base;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlTransient;

import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractExamMetrics;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractSubjectMetrics;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractTaskMetrics;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractTrialData;
import org.mitre.icarus.cps.assessment.data_model.base.data_sets.SingleModelDataSet;

/**
 * 
 * @author cbonaceto
 *
 */
@Entity
public abstract class Site<SMDS extends SingleModelDataSet<D, T, M, S>, 
	D extends AbstractTrialData<?>,
	T extends AbstractTaskMetrics<D>, M extends AbstractExamMetrics<T>, 
	S extends AbstractSubjectMetrics> implements Serializable {	
	
	private static final long serialVersionUID = -34181479163116147L;

	protected int id;
	
	protected String site_id; 
	
	protected String name;	
	
	/** The data sets for the site on each exam */
	protected List<SMDS> dataSets;	

	@Id @GeneratedValue
	@Column( name="siteId" )
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSite_id() {
		return site_id;
	}

	public void setSite_id(String site_id) {
		this.site_id = site_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	//@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	//@JoinColumn(name="siteSingleModelDataSets")
	//@Fetch(value = FetchMode.SUBSELECT)
	@XmlTransient
	public List<SMDS> getDataSets() {
		return dataSets;
	}

	public void setDataSets(List<SMDS> dataSets) {
		this.dataSets = dataSets;
	}
}