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
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractMetricsInfo;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractSubjectMetrics;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractTaskMetrics;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractTrialData;
import org.mitre.icarus.cps.assessment.data_model.base.data_sets.AverageHumanDataSet;
import org.mitre.icarus.cps.assessment.data_model.base.data_sets.SingleModelDataSet;

/**
 * @author cbonaceto
 *
 */
@Entity
public abstract class ProgramPhase<
	ED extends AbstractExamData<AHDS, D, T, S, I>,
	SITE extends Site<SMDS, D, T, M, S>,
	SMDS extends SingleModelDataSet<D, T, M, S>,
	AHDS extends AverageHumanDataSet<D, T, S, I>,
	D extends AbstractTrialData<?>, T extends AbstractTaskMetrics<D>, 
	M extends AbstractExamMetrics<T>, S extends AbstractSubjectMetrics,
	I extends AbstractMetricsInfo> implements Serializable {	
	
	private static final long serialVersionUID = -4889626576224180632L;
	
	protected int id;
	
	protected String name;
	
	protected String description;
	
	/** The exams in the phase */
	protected List<ED> exams;
	
	/** The team sites in the phase */
	protected List<SITE> sites;
	
	/** The phase completion date */
	protected Long completionDate;
	
	@Id @GeneratedValue
	@Column( name="programPhaseId" )
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	//@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	//@JoinColumn(name="programPhaseExamData")
	//@Fetch(value = FetchMode.SUBSELECT)
	@XmlTransient
	public List<ED> getExams() {
		return this.exams;
	}

	public void setExams(List<ED> exams) {
		this.exams = exams;
	}

	//@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	//@JoinColumn(name="programPhaseSites")
	//@Fetch(value = FetchMode.SUBSELECT)
	@XmlTransient
	public List<SITE> getSites() {
		return sites;
	}
	
	public void setSites(List<SITE> sites) {
		this.sites = sites;
	}

	public Long getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(Long completionDate) {
		this.completionDate = completionDate;
	}
}