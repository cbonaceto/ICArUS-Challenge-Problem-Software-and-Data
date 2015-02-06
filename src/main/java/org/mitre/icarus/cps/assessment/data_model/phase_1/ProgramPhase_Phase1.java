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
package org.mitre.icarus.cps.assessment.data_model.phase_1;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.mitre.icarus.cps.assessment.data_model.base.ProgramPhase;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.ExamMetrics;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.MetricsInfo;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.SubjectMetrics;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.TaskMetrics;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.TrialData;
import org.mitre.icarus.cps.assessment.data_model.phase_1.data_sets.AverageHumanDataSet_Phase1;
import org.mitre.icarus.cps.assessment.data_model.phase_1.data_sets.SingleModelDataSet_Phase1;

/**
 * @author CBONACETO
 *
 */
@Entity
public class ProgramPhase_Phase1 extends ProgramPhase<ExamData, Site_Phase1, SingleModelDataSet_Phase1,
	AverageHumanDataSet_Phase1,	TrialData, TaskMetrics, ExamMetrics, SubjectMetrics, MetricsInfo> {
	
	private static final long serialVersionUID = 1182830678130798720L;
	
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinColumn(name="programPhaseExamData")
	@Fetch(value = FetchMode.SUBSELECT)
	@Override
	public List<ExamData> getExams() {
		return this.exams;
	}
	
	@Override
	public void setExams(List<ExamData> exams) {
		this.exams = exams;
	}
	
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinColumn(name="programPhaseSites")
	@Fetch(value = FetchMode.SUBSELECT)
	@Override
	public List<Site_Phase1> getSites() {
		return sites;
	}
	
	@Override
	public void setSites(List<Site_Phase1> sites) {
		this.sites = sites;
	}
}