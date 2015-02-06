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
package org.mitre.icarus.cps.assessment.data_model.phase_1.data_sets;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.mitre.icarus.cps.assessment.data_model.base.data_sets.AverageHumanDataSet;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.MetricsInfo;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.SubjectMetrics;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.TaskMetrics;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.TrialData;

/**
 * Data set containing the average human results for a Phase 1 exam.
 * 
 * @author CBONACETO
 *
 */
@Entity
@XmlRootElement
public class AverageHumanDataSet_Phase1 extends AverageHumanDataSet<TrialData, TaskMetrics, SubjectMetrics, MetricsInfo> {
	
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinColumn(name="averageHumanDataSetTaskMetrics")
	@Fetch(value = FetchMode.SUBSELECT)
	@XmlElement
	@Override	
	public List<TaskMetrics> getTaskMetrics() {
		return taskMetrics;
	}	
	
	@Override
	public void setTaskMetrics(List<TaskMetrics> taskMetrics) {
		this.taskMetrics = taskMetrics;
	}

	@ManyToOne(cascade=CascadeType.ALL)
	@XmlElement
	@Override
	public SubjectMetrics getSubjectMetrics() {
		return subjectMetrics;
	}	
	
	@Override
	public void setSubjectMetrics(SubjectMetrics subjectMetrics) {
		this.subjectMetrics = subjectMetrics;
	}

	@ManyToOne(cascade=CascadeType.ALL)
	@XmlElement
	@Override
	public MetricsInfo getMetricsInfo() {
		return metricsInfo;
	}

	@Override
	public void setMetricsInfo(MetricsInfo metricsInfo) {
		this.metricsInfo = metricsInfo;
	}	
}