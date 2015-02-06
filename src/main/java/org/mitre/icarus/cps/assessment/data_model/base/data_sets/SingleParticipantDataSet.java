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
package org.mitre.icarus.cps.assessment.data_model.base.data_sets;

import javax.xml.bind.annotation.XmlTransient;

import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractExamMetrics;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractSubjectMetrics;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractTaskMetrics;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractTrialData;
import org.mitre.icarus.cps.assessment.data_model.base.data_sets.DataSet;

/**
 * 
 * @author CBONACETO
 *
 */
public abstract class SingleParticipantDataSet<D extends AbstractTrialData<?>,
	T extends AbstractTaskMetrics<D>, M extends AbstractExamMetrics<T>, 
	S extends AbstractSubjectMetrics> extends DataSet {
	
	protected String site_id;	
	
	protected String response_generator_id;	
		
	protected Long time_stamp;	
	
	protected M examMetrics;
	
	protected S subjectMetrics;

	public String getSite_id() {
		return site_id;
	}

	public void setSite_id(String site_id) {
		this.site_id = site_id;
	}

	public String getResponse_generator_id() {
		return response_generator_id;
	}

	public void setResponse_generator_id(String response_generator_id) {
		this.response_generator_id = response_generator_id;
	}

	public Long getTime_stamp() {
		return time_stamp;
	}

	public void setTime_stamp(Long time_stamp) {
		this.time_stamp = time_stamp;
	}
	
	@XmlTransient
	public M getExamMetrics() {
		return examMetrics;
	}

	public void setExamMetrics(M examMetrics) {
		this.examMetrics = examMetrics;
	}
	
	@XmlTransient
	public S getSubjectMetrics() {
		return subjectMetrics;
	}

	public void setSubjectMetrics(S subjectMetrics) {
		this.subjectMetrics = subjectMetrics;
	}
}